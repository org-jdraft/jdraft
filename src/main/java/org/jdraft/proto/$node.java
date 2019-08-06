package org.jdraft.proto;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.jdraft.*;
import org.jdraft.Ast;
import org.jdraft._code;
import org.jdraft._java;
import org.jdraft._node;
import org.jdraft._type;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Any Ast Node that can be matched, extracted, Selected, Removed, Replaced
 * 
 * usually a simple String pattern (or Node-based lambda) will suffice 
 * 
 * sometimes we need this because the "type" of the thing that we are looking for 
 * is not always important
 * 
 * @author Eric
 */
public class $node implements $proto<Node> {
    
    /**
     * 
     * @param name
     * @return 
     */
    public static $node of( String name ){
        return new $node( name );
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $node of( Predicate<Node> constraint ){
        return new $node("$name$").addConstraint(constraint);
    }
    
    /** the string pattern */
    public Stencil pattern;
    
    public Predicate<Node> constraint = t -> true;
    
    public $node( String pattern ){
        this( Stencil.of(pattern) );
    }
    
    public $node( Stencil pattern ){
        this.pattern = pattern;
        this.constraint = t-> true;
    }
    
    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified prototype
     */
    public $node addConstraint( Predicate<Node>constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
   /**
    * 
    * @param target
    * @param $Name
    * @return 
    */
    public $node $(String target, String $Name) {
        this.pattern = this.pattern.$(target, $Name);
        return this;
    }
    
    /**
     * 
     * @return 
     */
    public List<String> list$() {
        return this.pattern.list$();
    }

    /**
     * 
     * @return 
     */
    public List<String> list$Normalized() {
        return this.pattern.list$Normalized();
    }

    /**
     * 
     * @param astNode
     * @return 
     */
    public Select select(Node astNode){
        if( this.constraint.test(astNode)) {
            Tokens ts = this.pattern.decompose( astNode.toString(Ast.PRINT_NO_COMMENTS) );
            if( ts != null ){
                return new Select( astNode, ts);
            }
        }
        return null;
    }
    
    /**
     * Returns the first Statement that matches the 
     * @param astNode the 
     * @param _nodeMatchFn 
     * @return 
     */
    @Override
    public Node firstIn( Node astNode,  Predicate<Node> _nodeMatchFn){
        Optional<Node> f = astNode.findFirst( Node.class, n ->{
                Select sel = select(n);
                return select(n) != null && _nodeMatchFn.test(n);
        });         
        
        if( f.isPresent()){
            return f.get();
        }
        return null;
    }    
    
    /**
     * Selects the first instance
     * @param astNode
     * @return 
     */
    @Override
    public Select selectFirstIn( Node astNode ){
        Optional<Node> f = astNode.findFirst( Node.class, 
                n -> select(n) != null );         
        
        if( f.isPresent()){
            return select(f.get());
        }
        return null;
    }
    
    @Override
    public List<Select> listSelectedIn(Node astRootNode) {
        List<Select> found = new ArrayList<>();
        astRootNode.walk(n -> {
            Select select = select(n);
            if( select != null ){
                found.add(select); 
            }            
        });
        return found;
    }

    @Override
    public List<Select> listSelectedIn(_java _j) {
        if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                return listSelectedIn(_c.astCompilationUnit());
            }
            _type _t = (_type) _j; //only possible
            return listSelectedIn(_t.ast()); //return the TypeDeclaration, not the CompilationUnit
        }
        return listSelectedIn( ((_node) _j).ast());
    }


    /**
     * 
     * @param astRootNode
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(Node astRootNode, Predicate<Select> selectConstraint) {
        List<Select> found = new ArrayList<>();
        astRootNode.walk(n -> {
            Select select = select(n);
            if( select != null && selectConstraint.test(select)){
                found.add(select); 
            }            
        });
        return found;
    }

    /**
     * 
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(_java _j, Predicate<Select> selectConstraint) {
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                return listSelectedIn( ((_code) _j).astCompilationUnit(), selectConstraint);
            }
            return listSelectedIn( ((_type)_j).ast(), selectConstraint);
        }
        return listSelectedIn( ((_node)_j).ast(), selectConstraint);
    }
    
    private static boolean replaceNode( Node target, Node replacement ){
        boolean isRep = false;
        
        if( target.getClass() != replacement.getClass() ){            
            if( target instanceof ClassOrInterfaceType ){
                isRep = target.replace( Ast.typeRef( replacement.toString() ) );                
            } else if( target instanceof Name) {
                isRep = target.replace( new Name( replacement.toString()) );                
            } else if( target instanceof SimpleName) {
                isRep = target.replace( new SimpleName( replacement.toString()) );
            } else if( target instanceof VariableDeclarator ){
                System.out.println( "replacing Variable "+ target);
                VariableDeclarator vd = (VariableDeclarator)target;
                vd.setName(replacement.toString());
                isRep = true;
            }
        }
        //
        if( ! isRep ){
            isRep = target.replace(replacement);                                
        }
        return isRep;        
    }
    
    private static boolean replaceNode( Node target, String replacement ){
        if( target instanceof VariableDeclarator ){
            //since a Variable Declarator CAN match
            ((VariableDeclarator)target).setName(replacement);
            return true;
        }  
        Node n = Ast.nodeOf(target.getClass(), replacement);        
        return replaceNode( target, n);                        
    }
     
    /**
     * 
     * @param <_J>
     * @param _j
     * @param $replacement
     * @return 
     */
    public <_J extends _java> _J replaceIn(_J _j, $node $replacement) {
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                replaceIn( ((_code) _j).astCompilationUnit(), $replacement);
                return _j;
            }
            replaceIn( ((_type) _j).ast(), $replacement);
            return _j;
        }
        replaceIn( ((_node) _j).ast(), $replacement);
        return _j;
    }
    
    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param $replacement
     * @return 
     */
    public <N extends Node> N replaceIn(N astRootNode, $node $replacement) {
        astRootNode.walk(n -> {
            if( this.constraint.test(n) ) {
                Tokens ts = this.pattern.decompose( n.toString(Ast.PRINT_NO_COMMENTS) );
                if( ts != null ){
                    String constructed = $replacement.pattern.compose(ts);
                    if( ! replaceNode( n, constructed ) ){
                        //System.out.println("DIDNT REPLACE "+ n);
                    }                    
                }                
            }
        });
        return astRootNode;
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param replacement
     * @return 
     */
    public <_J extends _java> _J replaceIn(_J _j, String replacement) {
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                replaceIn( ((_code) _j).astCompilationUnit(), replacement);
                return _j;
            }
            replaceIn( ((_type) _j).ast(), replacement);
            return _j;
        }
        replaceIn( ((_node) _j).ast(), replacement);
        return _j;
    }
    
    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param replacement
     * @return 
     */
    public <N extends Node> N replaceIn(N astRootNode, String replacement) {
        astRootNode.walk(n -> {
            if( this.constraint.test(n)) {
                String st = n.toString(Ast.PRINT_NO_COMMENTS);
                Tokens ts = this.pattern.decompose( st );
                if( ts != null ){
                    //System.out.println( "replacing "+ n +" of "+n.getClass()+" with "+ replacement );
                    boolean isRep = replaceNode( n, replacement );                    
                    if( !isRep ){
                    }
                }                
            }
        });
        return astRootNode;
    }
    
    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param replacement
     * @return 
     */
    public <N extends Node> N replaceIn(N astRootNode, Node replacement) {
        astRootNode.walk(n -> {
            if( this.constraint.test(n)) {
                Tokens ts = this.pattern.decompose( n.toString(Ast.PRINT_NO_COMMENTS) );
                if( ts != null ){
                    replaceNode( n, replacement );                    
                }                
            }
        });
        return astRootNode;
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param replacement
     * @return 
     */
    public <_J extends _java> _J replaceIn(_J _j, Node replacement) {
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                replaceIn( ((_code) _j).astCompilationUnit(), replacement);
                return _j;
            }
            replaceIn( ((_type) _j).ast(), replacement);
            return _j;
        }
        replaceIn( ((_node) _j).ast(), replacement);
        return _j;
    }
    
    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param selectActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode, Consumer<Select> selectActionFn) {
         astRootNode.walk(n -> {
            Select sel = select(n);
            if( sel != null ){
                selectActionFn.accept(sel);
            }            
        });
        return astRootNode;
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param nodeActionFn
     * @return 
     */
    public <_J extends _java> _J forSelectedIn(_J _j, Consumer<Select> nodeActionFn) {
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                forSelectedIn( ((_code) _j).astCompilationUnit(), nodeActionFn);
                return _j;
            }
            forSelectedIn( ((_type) _j).ast(), nodeActionFn);
            return _j;
        }
        forSelectedIn( ((_node) _j).ast(), nodeActionFn);
        return _j;
    }
    
    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode, Predicate<Select>selectConstraint, Consumer<Select> selectActionFn) {
         astRootNode.walk(n -> {
            Select sel = select(n);
            if( sel != null && selectConstraint.test(sel)){
                selectActionFn.accept(sel);
            }            
        });
        return astRootNode;
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @param nodeActionFn
     * @return 
     */
    public <_J extends _java> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> nodeActionFn) {
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                forSelectedIn( ((_code) _j).astCompilationUnit(), nodeActionFn);
                return _j;
            }
            forSelectedIn( ((_type) _j).ast(), nodeActionFn);
            return _j;
        }
        forSelectedIn( ((_node) _j).ast(), nodeActionFn);
        return _j;
    }    
    
    @Override
    public <N extends Node> N forEachIn(N astRootNode, Predicate<Node> nodeMatchFn, Consumer<Node> nodeActionFn) {
         astRootNode.walk(n -> {
            Select sel = select(n);
            if( sel != null && nodeMatchFn.test(n) ){
                nodeActionFn.accept(n);
            }            
        });
        return astRootNode;
    }

    /**
     * Singleton that compares the start position of Select entities 
     * (based on the Ast node that was selected )
     */
    public static final SelectStartPositionComparator SELECT_START_POSITION_COMPARATOR = 
        new SelectStartPositionComparator();   
    
    /**
     * A compares the start position of Select Ast node entities
     */
    public static class SelectStartPositionComparator implements Comparator<$proto.selectedAstNode>{
        @Override
        public int compare($proto.selectedAstNode o1, $proto.selectedAstNode o2) {
            return Ast.COMPARE_NODE_BY_LOCATION.compare(o1.ast(), o2.ast());
        }        
    }
    
    /**
     * A Matched Selection result returned from matching a prototype Node
     * @param <T> the underlying Node implementation, because classUse can be
     * a ClassOrInterfaceType, or a SimpleName, or a Name depending on the 
     * scenario and how it is used (in an annotation, a throws class, an extends
     * implements, CastExpr, etc.)
     */     
    public static class Select<T extends Node> implements $proto.selected, $proto.selectedAstNode<T> {
        public T node;
        public $args args;

        @Override
        public $args args(){
            return args;
        }
        
        public Select( T node, Tokens tokens){
            this.node = node;
            this.args = args.of(tokens);
        }

        @Override
        public T ast() {
            return node;
        }
        
        @Override
        public String toString(){
            return "$node.Select{"+ System.lineSeparator()+
                Text.indent( node.toString() )+ System.lineSeparator()+
                Text.indent("$args : " + args) + System.lineSeparator()+
                "}";
        }
        
        public boolean isName(){
            return node instanceof Name;
        }
        
        public boolean isSimpleName(){
            return node instanceof SimpleName;
        }
        
        public boolean isClassOrInterfaceType(){
            return node instanceof ClassOrInterfaceType;
        }
    }
}