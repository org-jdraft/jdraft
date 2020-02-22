package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.utils.Log;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jdraft.*;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

/**
 * A Conjunction over one or more patterns OR'ed together
 *
 * NOTE: MOST $patterns contain MONOTONIC or builders:
 * $method $m = $method.of(
 *     $method.of($.NON_STATIC).$and(m->m.isImplemented()).$isParentMember($class.of($.PRIVATE)),
 *     $method.of($.NON_STATIC).$and(m->m.isImplemented(()).$isParentMember($enum.of()),
 *     $method.of($.NON_STATIC).$isParentMember($interface.of()) );
 *
 * //with a top level find all non static and implemented methods within
 * $or<$method> $t = $or.of(
 *     $method.of($.NON_STATIC).$and(m->m.isImplemented()).$isParentMember($class.of($.PRIVATE)),
 *     $method.of($.NON_STATIC).$and(m->m.isImplemented(()).$isParentMember($enum.of()),
 *     $method.of($.NON_STATIC).$isParentMember($interface.of()) );
 *
 * $t.listIn( _archive.of("C:\\temp\\MyJar.jar") );
 *
 * @author Eric
 */
public final class $or<$p extends $pattern>
        implements $pattern<Node, $or> {

    /**
     * matches OR
     * @param prototypes
     * @return
     */
    public static <$p extends $pattern> $or of($p... prototypes ){
        return new $or(prototypes);
    }

    public List<$p> patterns;

    private $or( $p ...$ps){
        this.patterns = Arrays.stream($ps).collect(Collectors.toList());
    }

    /**
     *
     * @return
     */
    private Predicate<Node> buildNodePredicate(){
        Predicate<Node>constraint = n->true;
        for(int i=0; i< patterns.size(); i++){
            final $pattern $pat = patterns.get(i);
            constraint = constraint.or( (n) -> $pat.match(n) );
        }
        return constraint;
    }

    /**
     *
     * @param translator
     * @param kvs
     * @return
     */
    public $or $hardcode(Translator translator, Tokens kvs ) {
        throw new $pattern.$exception("cannot modify $or");
    }

    public boolean isMatchAny(){
        return false;
    }

    public boolean match(Node node ){
        return select(node) != null;
    }

    public boolean match(_java._domain _j){
        if( _j instanceof _java._multiPart){
            return match( ((_java._multiPart)_j).ast());
        }
        return false;
    }

    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified prototype
     */
    public $or $and(Predicate<Node>constraint ){
        throw new $pattern.$exception("cannot modify $or");
    }

   /**
    *
    * @param target
    * @param $paramName
    * @return
    */
    public $or $(String target, String $paramName) {
        throw new $pattern.$exception("cannot modify $or");
    }

    /**
     *
     * @return
     */
    public List<String> list$() {
        List<String> ss = new ArrayList<>();
        this.patterns.forEach(p -> ss.addAll(list$()));
        return ss;
    }

    /**
     *
     * @return
     */
    public List<String> list$Normalized() {
        List<String> ss = new ArrayList<>();
        this.patterns.forEach(p -> ss.addAll(list$Normalized()));
        return ss.stream().distinct().collect(Collectors.toList());
    }

    public Select select(_java._multiPart _n ){
        return select( _n.ast());
    }

    /**
     *
     * @param astNode
     * @return
     */
    public Select select(Node astNode){
        Optional<$p> op  = this.patterns.stream().filter( $p-> $p.match(astNode) ).findFirst();
        if( op.isPresent() ){
            $p $pat = op.get();
            Object o = $pat.firstIn(astNode);
            if( o instanceof _java._domain){
                $pattern.select_java sel = ($pattern.select_java) $pat.select( o );
                return new Select($pat, sel._node(), sel.tokens().asTokens() );
            } else{
                $pattern.selectAst sel = ($pattern.selectAst) $pat.select( o );
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
        if( _j instanceof _compilationUnit){
            _compilationUnit _c = (_compilationUnit) _j;
            if( _c.isTopLevel() ){
                return listSelectedIn(_c.astCompilationUnit());
            }
            _type _t = (_type) _j; //only possible
            return listSelectedIn(_t.ast()); //return the TypeDeclaration, not the CompilationUnit
        }
        return listSelectedIn( ((_java._multiPart) _j).ast());
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
        if( _j instanceof _compilationUnit){
            if( ((_compilationUnit) _j).isTopLevel()){
                return listSelectedIn( ((_compilationUnit) _j).astCompilationUnit(), selectConstraint);
            }
            return listSelectedIn( ((_type)_j).ast(), selectConstraint);
        }
        return listSelectedIn( ((_java._multiPart)_j).ast(), selectConstraint);
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

    public <_J extends _model> _J replaceIn(_J _j, $or $replacement) {
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
    */
    /**
     *
     * @param <N>
     * @param astNode
     * @param $replacement
     * @return

    public <N extends Node> N replaceIn(N astNode, $or $replacement) {
        astNode.walk(n -> {
            Select sel = select(n);
            if( sel != null ) {
                //Tokens ts = this.nodeStencil.parse( n.toString(Ast.PRINT_NO_COMMENTS) );
                //if( ts != null ){
                String constructed = $replacement.(sel.tokens);
                if( ! replaceNode( n, constructed ) ){
                        //System.out.println("DIDNT REPLACE "+ n);
                 }
                //}
            }
        });
        return astNode;
    }
    */

    /**
     *
     * @param <_J>
     * @param _j
     * @param replacement
     * @return
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, String replacement) {
        if( _j instanceof _compilationUnit){
            if( ((_compilationUnit) _j).isTopLevel()){
                replaceIn( ((_compilationUnit) _j).astCompilationUnit(), replacement);
                return _j;
            }
            replaceIn( ((_type) _j).ast(), replacement);
            return _j;
        }
        replaceIn( ((_java._multiPart) _j).ast(), replacement);
        return _j;
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
            Select sel = select(n);
            if( sel != null ) {
                Stencil rep = Stencil.of(replacement);
                String repString = rep.draft(sel.tokens);
                boolean isRep = replaceNode( n, repString );
                if( !isRep ){
                    Log.error("Didnt replace %s with string %s", ()->astNode,()->replacement );
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
            Select sel = select(n);
            if( sel != null ) {
                boolean isRep = replaceNode( n, replacement );
                if( !isRep ){
                    Log.error("Didnt replace %s with Node %s", ()->astNode,()->replacement );
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
        if( _j instanceof _compilationUnit){
            if( ((_compilationUnit) _j).isTopLevel()){
                replaceIn( ((_compilationUnit) _j).astCompilationUnit(), replacement);
                return _j;
            }
            replaceIn( ((_type) _j).ast(), replacement);
            return _j;
        }
        replaceIn( ((_java._multiPart) _j).ast(), replacement);
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
        if( _j instanceof _compilationUnit){
            if( ((_compilationUnit) _j).isTopLevel()){
                forSelectedIn( ((_compilationUnit) _j).astCompilationUnit(), nodeActionFn);
                return _j;
            }
            forSelectedIn( ((_type) _j).ast(), nodeActionFn);
            return _j;
        }
        forSelectedIn( ((_java._multiPart) _j).ast(), nodeActionFn);
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
        if( _j instanceof _compilationUnit){
            if( ((_compilationUnit) _j).isTopLevel()){
                forSelectedIn( ((_compilationUnit) _j).astCompilationUnit(), nodeActionFn);
                return _j;
            }
            forSelectedIn( ((_type) _j).ast(), nodeActionFn);
            return _j;
        }
        forSelectedIn( ((_java._multiPart) _j).ast(), nodeActionFn);
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
            return Ast.COMPARE_NODE_BY_POSITION.compare(o1.ast(), o2.ast());
        }
    }

    /**
     * A Matched Selection result returned from matching a prototype Node
     * @param <T> the underlying Node implementation, because classUse can be
     * a ClassOrInterfaceType, or a SimpleName, or a Name depending on the
     * scenario and how it is used (in an annotation, a throws class, an extends
     * implements, CastExpr, etc.)
     */
    public static class Select<$P extends $pattern,T> implements selected {
        public T selected;
        public $P pattern;
        public $tokens tokens;

        @Override
        public $tokens tokens(){
            return tokens;
        }
        
        public Select( $P pattern, T node, Tokens tokens){
            this.pattern = pattern;
            this.selected = node;
            this.tokens = this.tokens.of(tokens);
        }

        public $P pattern() { return pattern; }

        public T selected() {
            return selected;
        }
        
        @Override
        public String toString(){
            return "$node.Select{"+ System.lineSeparator()+
                Text.indent( selected.toString() )+ System.lineSeparator()+
                Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                "}";
        }
        /*
        public boolean isName(){
            return node instanceof Name;
        }
        
        public boolean isSimpleName(){
            return node instanceof SimpleName;
        }
        
        public boolean isClassOrInterfaceType(){
            return node instanceof ClassOrInterfaceType;
        }
         */
    }
}