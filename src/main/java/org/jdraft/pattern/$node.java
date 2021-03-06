package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jdraft.*;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;
import org.jdraft.walk.Walk;

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
public class $node implements $pattern<Node, $node>, $body.$part, $method.$part, $constructor.$part {

    /**
     * NOTE: this explicitly adds a $not constraint for specific Node types
     * @param pattern
     * @return 
     */
    public static $node of( String pattern ){
        //if you do this...
        return new $node( pattern ); //.$not(CompilationUnit.class);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $node of( Predicate<Node> constraint ){
        return new $node("$node$").$and(constraint);
    }

    /**
     * matches OR
     * @param prototypes
     * @return
     */
    public static $node of( $pattern... prototypes ){
        Predicate<Node> constraint = (n)-> prototypes[0].match(n);
        for(int i=1; i< prototypes.length; i++){
            final $pattern $p = prototypes[i];
            final Predicate<Node> pm = (n) -> $p.match(n);
            constraint = constraint.or(pm);
        }
        return of( constraint );
    }

    public static $node of(){
        return of( "$node$");
    }

    /**
     *
     * @param nodeType
     * @return
     */
    public static $node of( Class nodeType ){
        return of( new Class[]{nodeType} );
    }

    /**
     *
     * @param nodeTypes
     * @return
     */
    public static $node of( Class...nodeTypes ){
        return of( "$node$", nodeTypes);
    }

    public static $node of( Node node ){
        return of( node.toString(Print.PRINT_NO_ANNOTATIONS_OR_COMMENTS), node.getClass() );
    }

    /**
     *
     * @param pattern
     * @param nodeTypes
     * @return
     */
    public static $node of( String pattern, Class...nodeTypes ){
        return of(pattern).$and(n-> Walk.isNodeOfType(n, nodeTypes));
    }

    public static $node.Or or( Node... _protos ){
        $node[] arr = new $node[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $node.of( _protos[i]);
        }
        return or(arr);
    }

    public static $node.Or or( $node...$tps ){
        return new $node.Or($tps);
    }

    /** the string pattern */
    public Stencil nodeStencil;
    
    public Predicate<Node> constraint = t -> true;

    private $node(){
        nodeStencil = Stencil.of("$node$");
    }

    public $node( String pattern){
        this( Stencil.of(pattern) );
    }
    
    public $node( Stencil nodeStencil){
        this.nodeStencil = nodeStencil;
        this.constraint = t-> true;
    }

    /**
     *
     * @param translator
     * @param kvs
     * @return
     */
    public $node $hardcode(Translator translator, Tokens kvs ) {
        this.nodeStencil = this.nodeStencil.$hardcode(translator, kvs);
        return this;
    }

    public boolean isMatchAny(){
        try{
            return this.nodeStencil.isMatchAny() && this.constraint.test(null);
        } catch(Exception e){
            return false;
        }
    }

    public boolean match(Node node ){
        return select(node) != null;
    }

    public boolean match(_java._domain _j){
        if( _j instanceof _tree._node){
            return match( ((_tree._node)_j).node());
        }
        return false;
    }

    public $node $not( Class...clazz ){
        Set<Class> nodeClasses = new HashSet<>();
        Arrays.stream(clazz).forEach( c -> nodeClasses.add(c));
        Predicate<Node> constr = (n)-> nodeClasses.stream().anyMatch( nc-> nc.isAssignableFrom(n.getClass()));
        return $not(constr);
    }

    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified prototype
     */
    public $node $and(Predicate<Node>constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }


   /**
    * 
    * @param target
    * @param $paramName
    * @return 
    */
    public $node $(String target, String $paramName) {
        this.nodeStencil = this.nodeStencil.$(target, $paramName);
        return this;
    }
    
    /**
     * 
     * @return 
     */
    public List<String> list$() {
        return this.nodeStencil.$list();
    }

    /**
     * 
     * @return 
     */
    public List<String> list$Normalized() {
        return this.nodeStencil.$listNormalized();
    }

    /**
     * 
     * @param astNode
     * @return 
     */
    public Select select(Node astNode){
        if( this.constraint.test(astNode)) {
            Tokens ts = this.nodeStencil.parse( astNode.toString(Print.PRINT_NO_COMMENTS) );
            if( ts != null ){
                return new Select( astNode, ts);
            }
        }
        return null;
    }
    
    /**
     * Returns the first Statement that matches the 
     * @param astNode the
     * @param nodeMatchFn
     * @return 
     */
    @Override
    public Node firstIn(Node astNode, Predicate<Node> nodeMatchFn){
        Optional<Node> f = astNode.findFirst( Node.class, n ->{
                Select sel = select(n);
                return select(n) != null && nodeMatchFn.test(n);
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
    public List<Select> listSelectedIn(Node astNode) {
        List<Select> found = new ArrayList<>();
        astNode.walk(n -> {
            Select select = select(n);
            if( select != null ){
                found.add(select); 
            }            
        });
        return found;
    }

    @Override
    public List<Select> listSelectedIn(_java._domain _j) {
        if( _j instanceof _codeUnit){
            _codeUnit _c = (_codeUnit) _j;
            if( _c.isTopLevel() ){
                return listSelectedIn(_c.astCompilationUnit());
            }
            _type _t = (_type) _j; //only possible
            return listSelectedIn(_t.node()); //return the TypeDeclaration, not the CompilationUnit
        }
        return listSelectedIn( ((_tree._node) _j).node());
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
    public List<Select> listSelectedIn(_java._domain _j, Predicate<Select> selectConstraint) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel()){
                return listSelectedIn( ((_codeUnit) _j).astCompilationUnit(), selectConstraint);
            }
            return listSelectedIn( ((_type)_j).node(), selectConstraint);
        }
        return listSelectedIn( ((_tree._node)_j).node(), selectConstraint);
    }
    
    private static boolean replaceNode( Node target, Node replacement ){
        boolean isRep = false;
        
        if( target.getClass() != replacement.getClass() ){            
            if( target instanceof ClassOrInterfaceType ){
                isRep = target.replace( Types.of( replacement.toString() ) );
            } else if( target instanceof Name) {
                isRep = target.replace( new Name( replacement.toString()) );                
            } else if( target instanceof SimpleName) {
                isRep = target.replace( new SimpleName( replacement.toString()) );
            } else if( target instanceof VariableDeclarator ){
                //System.out.println( "replacing Variable "+ target);
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
    public <_J extends _java._domain> _J replaceIn(_J _j, $node $replacement) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel()){
                replaceIn( ((_codeUnit) _j).astCompilationUnit(), $replacement);
                return _j;
            }
            replaceIn( ((_type) _j).node(), $replacement);
            return _j;
        }
        replaceIn( ((_tree._node) _j).node(), $replacement);
        return _j;
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param $replacement
     * @return 
     */
    public <N extends Node> N replaceIn(N astNode, $node $replacement) {
        astNode.walk(n -> {
            if( this.constraint.test(n) ) {
                Tokens ts = this.nodeStencil.parse( n.toString(Print.PRINT_NO_COMMENTS) );
                if( ts != null ){
                    String constructed = $replacement.nodeStencil.draft(ts);
                    if( ! replaceNode( n, constructed ) ){
                        //System.out.println("DIDNT REPLACE "+ n);
                    }                    
                }                
            }
        });
        return astNode;
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param replacement
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, String replacement) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel()){
                replaceIn( ((_codeUnit) _j).astCompilationUnit(), replacement);
                return _j;
            }
            replaceIn( ((_type) _j).node(), replacement);
            return _j;
        }
        replaceIn( ((_tree._node) _j).node(), replacement);
        return _j;
    }

    public <_CP extends _project> _CP replaceIn(_CP codeProvider, String replacement ){
        codeProvider.forEach(c-> replaceIn(c, replacement) );
        return codeProvider;
    }

    /**
     * 
     * @param <N>
     * @param astNode
     * @param replacement
     * @return 
     */
    public <N extends Node> N replaceIn(N astNode, String replacement) {
        astNode.walk(n -> {
            if( this.constraint.test(n)) {
                String st = n.toString(Print.PRINT_NO_COMMENTS);
                Tokens ts = this.nodeStencil.parse( st );
                if( ts != null ){
                    //System.out.println( "replacing "+ n +" of "+n.getClass()+" with "+ replacement );
                    boolean isRep = replaceNode( n, replacement );                    
                    if( !isRep ){
                    }
                }                
            }
        });
        return astNode;
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param replacement
     * @return 
     */
    public <N extends Node> N replaceIn(N astNode, Node replacement) {
        astNode.walk(n -> {
            if( this.constraint.test(n)) {
                Tokens ts = this.nodeStencil.parse( n.toString(Print.PRINT_NO_COMMENTS) );
                if( ts != null ){
                    replaceNode( n, replacement );                    
                }                
            }
        });
        return astNode;
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param replacement
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, Node replacement) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel()){
                replaceIn( ((_codeUnit) _j).astCompilationUnit(), replacement);
                return _j;
            }
            replaceIn( ((_type) _j).node(), replacement);
            return _j;
        }
        replaceIn( ((_tree._node) _j).node(), replacement);
        return _j;
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Consumer<Select> selectActionFn) {
        astNode.walk(n -> {
            Select sel = select(n);
            if( sel != null ){
                selectActionFn.accept(sel);
            }            
        });
        return astNode;
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param nodeActionFn
     * @return 
     */
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Consumer<Select> nodeActionFn) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel()){
                forSelectedIn( ((_codeUnit) _j).astCompilationUnit(), nodeActionFn);
                return _j;
            }
            forSelectedIn( ((_type) _j).node(), nodeActionFn);
            return _j;
        }
        forSelectedIn( ((_tree._node) _j).node(), nodeActionFn);
        return _j;
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Predicate<Select>selectConstraint, Consumer<Select> selectActionFn) {
         astNode.walk(n -> {
            Select sel = select(n);
            if( sel != null && selectConstraint.test(sel)){
                selectActionFn.accept(sel);
            }            
        });
        return astNode;
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @param nodeActionFn
     * @return 
     */
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> nodeActionFn) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel()){
                forSelectedIn( ((_codeUnit) _j).astCompilationUnit(), nodeActionFn);
                return _j;
            }
            forSelectedIn( ((_type) _j).node(), nodeActionFn);
            return _j;
        }
        forSelectedIn( ((_tree._node) _j).node(), nodeActionFn);
        return _j;
    }    
    
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<Node> nodeMatchFn, Consumer<Node> nodeActionFn) {
         astNode.walk(n -> {
            Select sel = select(n);
            if( sel != null && nodeMatchFn.test(n) ){
                nodeActionFn.accept(n);
            }            
        });
        return astNode;
    }

    /**
     * Adds a constraint that the beforeExpression occurs in the same context/block before the target Expression
     * @param patternsOccurringBeforeThisNode
     * @return
     */
    public $node $isAfter( $pattern... patternsOccurringBeforeThisNode ){
        Predicate<Node> prev = e -> $pattern.BodyScope.findPrevious(e, patternsOccurringBeforeThisNode) != null;
        return $and(prev);
    }

    /**
     * Adds a constraint that the beforeExpression occurs in the same context/block before the target Expression
     * @param patternsOccurringBeforeThisNode
     * @return
     */
    public $node $isNotAfter( $pattern... patternsOccurringBeforeThisNode ){
        Predicate<Node> prev = e -> $pattern.BodyScope.findPrevious(e, patternsOccurringBeforeThisNode) != null;
        return $not(prev);
    }

    /**
     *
     * @param patternsOccurringAfterThisNode
     * @return
     */
    public $node $isBefore( $pattern... patternsOccurringAfterThisNode ){
        Predicate<Node> prev = e -> $pattern.BodyScope.findNext(e, patternsOccurringAfterThisNode) != null;
        return $and(prev);
    }

    /**
     *
     * @param patternsOccurringAfterThisNode
     * @return
     */
    public $node $isNotBefore( $pattern... patternsOccurringAfterThisNode ){
        Predicate<Node> prev = e -> $pattern.BodyScope.findNext(e, patternsOccurringAfterThisNode) != null;
        return $not(prev);
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
    public static class SelectStartPositionComparator implements Comparator<selectAst>{
        @Override
        public int compare(selectAst o1, selectAst o2) {
            return At.COMPARE_NODE_BY_POSITION.compare(o1.ast(), o2.ast());
        }        
    }

    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are suppressed.
     */
    public static class Or extends $node{

        final List<$node>ors = new ArrayList<>();

        public Or($node...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $node $hardcode(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.$hardcode(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$node.Or{");
            sb.append(System.lineSeparator());
            ors.forEach($a -> sb.append( Text.indent($a.toString()) ) );
            sb.append("}");
            return sb.toString();
        }

        /**
         *
         * @param n
         * @return
         */
        public $node.Select select(Node n){
            $node $a = whichMatch(n);
            if( $a != null ){
                return $a.select(n);
            }
            return null;
        }

        public boolean isMatchAny(){
            return false;
        }

        /**
         * Return the underlying $method that matches the Method or null if none of the match
         * @param n
         * @return
         */
        public $node whichMatch(Node n){
            if( !this.constraint.test(n ) ){
                return null;
            }
            Optional<$node> orsel  = this.ors.stream().filter( $p-> $p.match(n) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }

    /**
     * A Matched Selection result returned from matching a prototype Node
     * @param <T> the underlying Node implementation, because classUse can be
     * a ClassOrInterfaceType, or a SimpleName, or a Name depending on the 
     * scenario and how it is used (in an annotation, a throws class, an extends
     * implements, CastExpr, etc.)
     */     
    public static class Select<T extends Node> implements $pattern.selected, selectAst<T> {
        public T node;
        public $tokens tokens;

        @Override
        public $tokens tokens(){
            return tokens;
        }
        
        public Select( T node, Tokens tokens){
            this.node = node;
            this.tokens = this.tokens.of(tokens);
        }

        @Override
        public T ast() {
            return node;
        }
        
        @Override
        public String toString(){
            return "$node.Select{"+ System.lineSeparator()+
                Text.indent( node.toString() )+ System.lineSeparator()+
                Text.indent("$tokens : " + tokens) + System.lineSeparator()+
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