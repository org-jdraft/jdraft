package org.jdraft.proto;

import org.jdraft.*;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.jdraft.proto.$node.Select;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Prototype representing any uses of a specific Class within code
 * (either simple or partially/fully qualified) including (but not limited to): 
 * <UL>
 *   <LI> Imports:  imports fully.qualified.A, imports static fully.qualified.A.*;
 *   <LI> Annotations: @A, @A, @MemberClass.A, @fully.qualified.MemberClass.A
 *   <LI> Throws: throws A, throws MemberClass.A, throws fully.qualified.A
 *   <LI> Extends: extends A, extends MemberClass.A, extends fully.qualified.MemberClass.A
 *   <LI> Implements: implements A, implements MemberClass.A, implements fully.qualified.MemberClass.A
 *   <LI> Fields/Vars : public A a; public MemberClass.A; public fully.qualified.MemberClass A;
 *   <LI> Return Types: public A m(){}, public MemberClass.A m()
 *   <LI> Parameter Type: public void m(A a), public void m( MemberClass.A a){}
 *   <LI> Casts : a = (A)obj; a = (MemberClass.A)obj; a= (fully.qualified.MemberClass.A)obj;
 *   <LI> Class Expression : A.class.isAssignableFrom(val);
 *   <LI> Static Method Call : A.staticMethod(), MemberClass.A.staticMethod()
 *   <LI> Generics : List<A> as, Map<A, String> am, Map<MemberClass.A, String> 
 *   <LI> TypeParameter: void <M extends A> doIt( M m ){...}
 * </UL>
 * @author Eric
 */
public final class $typeUse {
    
    /**
     * 
     * @param clazz
     * @return 
     */
    public static $typeUse of( Class clazz ){
        return new $typeUse(clazz);
    }
    
    /**
     * 
     * @param clazz
     * @param constraint
     * @return 
     */
    public static $typeUse of( Class clazz, Predicate<Node> constraint ){
        return of(clazz).addConstraint(constraint);
    }
    public final String packageName;
    
    /** 
     * Whenever the fully qualified name (i.e. java.util.Map)
     * (i.e. imports, implements, extends, throws, annotationName, cast, 
     * instanceof, etc.)
     */ 
    public $node $fullName;
    
    /**
     * If the class is a member class (or a member of a member class)
     * then you can refer to it not only by it's fully qualified name:
     * "pkgname.className"... but you can also refer to it by
     * "parentClassName.MemberClassName"
     * 
     * ..and if it's a deeply nested Class
     * "parentClassName.MemberClassName.deeplyNestedClassName"
     * 
     * this will create an ordered list of names that can be used to be refer
     * to the class in this fashion
     */
    public List<$node> $memberNames; 
    
    /**
     * Whenever the simple name (i.e. Map) is used
     * i.e. static method call Map.of(...), static field access, cast, etc.)
     */
    public $node $simpleName;
    
    /**
     * Only match nodes that are of these types
     * (i.e. dont try to toString() a node (to match it) that is not one
     * of these (3) types.
     * 
     * This should greatly speed up the matching, since
     * we wont toString (in order to test) EVERY SINGLE NODE
     * as we walk each entity.
     */
    private static final Predicate<Node> IS_EXPECTED_NODE_TYPE = 
        n-> n instanceof Name || 
        n instanceof SimpleName || 
        n instanceof ClassOrInterfaceType && 
        (n.getParentNode().isPresent() && 
            !(n.getParentNode().get() instanceof Name ||
              n.getParentNode().get() instanceof SimpleName ||      
              n.getParentNode().get() instanceof ClassOrInterfaceType) );
    

    public static $typeUse any(){
        return of();
    }
    
    /**
     * Matches any 
     * @return the classUse
     */
    public static $typeUse of(){
        return new $typeUse("", $node.of("$typeUse$").addConstraint(n -> n.toString().contains(".")),
            Collections.EMPTY_LIST, 
            $node.of("$typeUse$").addConstraint(n -> !n.toString().contains(".") ) ).addConstraint(IS_EXPECTED_NODE_TYPE);
    }    
    
    /**
     * This is for any()
     */
    private $typeUse(String packageName, $node fullName, List<$node>memberNames, $node simpleName ){
        this.packageName = packageName;
        this.$fullName = fullName; //$node.of("$classUse$");
        this.$memberNames = memberNames; //new ArrayList<>();
        this.$simpleName = simpleName; //$node.of("$classUse$");
    }
    
    /**
     * 
     * @param type 
     */
    public $typeUse( Class type ){
        if( type.getPackage() != null ) {
            this.packageName = type.getPackage().getName();
        } else{
            this.packageName ="";
        }
        //this.type = type;
        this.$fullName = new $node(type.getCanonicalName())
            .addConstraint( IS_EXPECTED_NODE_TYPE );
        //Note: there can be (0, 1, or more OTHER nodes that represent
        //Inner member classes, i.e. not fully qualified 
        
        this.$simpleName = new $node(type.getSimpleName()).addConstraint(IS_EXPECTED_NODE_TYPE);        
        $memberNames = $typeUse.buildMemberClassNames( type );
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public $typeUse addConstraint(Predicate<Node> constraint){
        $fullName.addConstraint(constraint);
        $memberNames.forEach(m -> m.addConstraint(constraint));
        $simpleName.addConstraint(constraint);        
        return this;
    }
    
    public $node.Select select( Node n ){
        
        Select sel = $fullName.select(n);
        if( sel != null ){
            return sel;
        }
        Optional<$node> on = 
            $memberNames.stream().filter( m -> m.select(n) != null ).findFirst();
        if( on.isPresent() ){
            return on.get().select(n);
        }
        sel = $simpleName.select(n);
        return sel;
    }
    
    public <N extends _java> N replaceIn(N _n, Class replacement) {
        if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                replaceIn( ((_code) _n).astCompilationUnit(), replacement);
                return _n;
            }
            replaceIn( ((_type)_n).ast(), replacement);
            return _n;
        }
        replaceIn( ((_node)_n).ast(), replacement);        
        return _n;
        /*
        if( _n instanceof _type && ((_type)_n).isTopLevel()){
            replaceIn( ((_type)_n).astCompilationUnit(), replacement);
            return _n;
        }
        replaceIn(_n.ast(), replacement);
        return _n;
        */
    }
    
    private static List<$node> buildMemberClassNames(Class clazz ){
        List<$node> nodes = new ArrayList<>();
        String currentPath = clazz.getSimpleName();
        buildMemberClassNames(clazz, currentPath, nodes);
        
        //reverse the order of the names... (which puts the LONGER NAMES UP FRONT)
        // so that when we match/replace, we look for a pattern in the longer 
        // name FIRST before looking at shorter names (to match and replace)
        Collections.reverse(nodes);
        return nodes;
    }
    
    private static void buildMemberClassNames(Class clazz, String currentPath, List<$node> nodes){        
        if( clazz.isMemberClass()){
            Class declaringClass = clazz.getDeclaringClass();
            currentPath = declaringClass.getSimpleName()+"."+currentPath;
            nodes.add( $node.of(currentPath).addConstraint(IS_EXPECTED_NODE_TYPE) );
            buildMemberClassNames( declaringClass, currentPath, nodes);
        }
    }
    
    public <N extends Node> N replaceIn(N astRootNode, Class replacement) {
        $fullName.replaceIn(astRootNode, replacement.getCanonicalName());
        
        //List<p_node> nodes = buildMemberClass_p_nodes( type );
        $memberNames.forEach(n -> n.replaceIn(astRootNode, replacement.getSimpleName()));

        $simpleName.replaceIn(astRootNode, replacement.getSimpleName());
        
        return astRootNode;
    }
   
    public <N extends Node> N replaceIn(N astRootNode, String replacement) {
        $fullName.replaceIn(astRootNode, replacement);
        $memberNames.forEach(n -> n.replaceIn(astRootNode, replacement));
        $simpleName.replaceIn(astRootNode, replacement);        
        return astRootNode;
    }

    public _type replaceIn(Class clazz, String replacement){
        return replaceIn( _java.type(clazz), replacement);
    }
    
    public <N extends _java> N replaceIn(N _n, String replacement) {
        if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                replaceIn( ((_code) _n).astCompilationUnit(), replacement);
                return _n;
            }
            replaceIn( ((_type)_n).ast(), replacement);
            return _n;
        }
        replaceIn( ((_node)_n).ast(), replacement);        
        return _n;
        /*
        if( _n instanceof _type && ((_type)_n).isTopLevel()){
            replaceIn( ((_type)_n).astCompilationUnit(), replacement);
            return _n;
        }
        replaceIn(_n.ast(), replacement);
        return _n;
        */
    }
    
    public <N extends Node> N replaceIn(N astRootNode, Node replacement) {
        $fullName.replaceIn(astRootNode, replacement);
        $memberNames.forEach(n -> n.replaceIn(astRootNode, replacement));
        $simpleName.replaceIn(astRootNode, replacement);        
        return astRootNode;
    }

    public <N extends _java> N replaceIn(N _n, Node replacement) {
        if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                replaceIn( ((_code) _n).astCompilationUnit(), replacement);
                return _n;
            }
            replaceIn( ((_type)_n).ast(), replacement);
            return _n;
        }
        replaceIn( ((_node)_n).ast(), replacement);        
        return _n;
        /*
        if( _n instanceof _type && ((_type)_n).isTopLevel()){
            replaceIn( ((_type)_n).astCompilationUnit(), replacement);
            return _n;
        }
        replaceIn(_n.ast(), replacement);
        return _n;
        */
    }
    
    public _type replaceIn(Class clazz, Node replacement){
        return replaceIn(_java.type(clazz), replacement);
    }
    
    public _type removeIn(Class clazz){
        return removeIn(_java.type(clazz) );
    }
    
    public <N extends _node> N removeIn(N _n ) {
        if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                removeIn( ((_code) _n).astCompilationUnit() );
                return _n;
            }
            removeIn( ((_type)_n).ast() );
            return _n;
        }
        removeIn( ((_node)_n).ast() );        
        return _n;
        /*
        if( _n instanceof _type && ((_type)_n).isTopLevel()){
            removeIn( ((_type)_n).astCompilationUnit() );
            return _n;
        }
        removeIn(_n.ast() );
        return _n;
        */
    }
    
    public <N extends Node> N removeIn(N astRootNode ) {
        $fullName.removeIn(astRootNode);
        $memberNames.forEach(n -> n.removeIn(astRootNode));
        $simpleName.removeIn(astRootNode);        
        return astRootNode;
    }
    
    public <N extends Node> N forEachIn(N astRootNode, Consumer<Node> nodeActionFn ) {
        $fullName.forEachIn(astRootNode, nodeActionFn);
        $memberNames.forEach(n -> n.forEachIn(astRootNode, nodeActionFn ) );
        $simpleName.forEachIn(astRootNode, nodeActionFn);        
        return astRootNode;
    }
    
    public <N extends Node> N forSelectedIn(N astRootNode, Consumer<$node.Select> selectActionFn ) {
        $fullName.forSelectedIn(astRootNode, selectActionFn);
        $memberNames.forEach( e-> e.forSelectedIn(astRootNode, selectActionFn) );
        $simpleName.forSelectedIn(astRootNode, selectActionFn);
        return astRootNode;
    }
     
    public <N extends Node> N forSelectedIn(N astRootNode, Predicate<$node.Select> selectConstraint, Consumer<$node.Select> selectActionFn ) {
        $fullName.forSelectedIn(astRootNode, selectConstraint, selectActionFn);
        $memberNames.forEach( e-> e.forSelectedIn(astRootNode, selectConstraint, selectActionFn) );
        $simpleName.forSelectedIn(astRootNode, selectConstraint, selectActionFn);
        return astRootNode;
    }

    public List<Node> listIn( Class clazz ){
        return listIn(_java.type(clazz));
    }

    public <_N extends _node> List<Node> listIn( _N _node ){
        return listIn( _node.ast() );
    }

    public <N extends Node> List<Node> listIn( N astRootNode ){
        List<Node> sels = new ArrayList<>();
        sels.addAll( $fullName.listIn(astRootNode) );
        $memberNames.forEach( e-> sels.addAll( e.listIn(astRootNode) ) );
        sels.addAll( $simpleName.listIn(astRootNode) );

        //dedupe
        List<Node>uniqueSels = sels.stream().distinct().collect(Collectors.toList());

        //the selections are interleaved, so lets organize them in positional
        //order (in the file) as to not surprise the caller
        Collections.sort(uniqueSels, Ast.COMPARE_NODE_BY_LOCATION);

        return uniqueSels;
    }

    public <N extends Node> List<$node.Select> listSelectedIn( N astRootNode ){
        List<$node.Select> sels = new ArrayList<>();
        sels.addAll( $fullName.listSelectedIn(astRootNode) );
        $memberNames.forEach( e-> sels.addAll( e.listSelectedIn(astRootNode) ) );
        sels.addAll( $simpleName.listSelectedIn(astRootNode) );
        
        //dedupe
        List<$node.Select>uniqueSels = sels.stream().distinct().collect(Collectors.toList());
        
        //the selections are interleaved, so lets organize them in positional 
        //order (in the file) as to not surprise the caller
        Collections.sort(uniqueSels, $node.SELECT_START_POSITION_COMPARATOR);
        
        return uniqueSels;        
    }
    
    public <N extends Node> List<$node.Select> listSelectedIn( N astRootNode, Predicate<$node.Select> selectConstraint){
        List<$node.Select> sels = new ArrayList<>();
        sels.addAll( $fullName.listSelectedIn(astRootNode, selectConstraint) );
        $memberNames.forEach( e-> sels.addAll( e.listSelectedIn(astRootNode) ) );
        sels.addAll( $simpleName.listSelectedIn(astRootNode) );
        
        //dedupe
        List<$node.Select>uniqueSels = sels.stream().distinct().collect(Collectors.toList());
        
        //the selections are interleaved, so lets organize them in positional 
        //order (in the file)
        Collections.sort(uniqueSels, $node.SELECT_START_POSITION_COMPARATOR);
        
        return uniqueSels;        
    }
    
    public <N extends _java> N forEachIn(N _n, Consumer<Node> nodeActionFn ) {
        $fullName.forEachIn(_n, nodeActionFn);
        $memberNames.forEach(n -> n.forEachIn(_n, nodeActionFn ) );
        $simpleName.forEachIn(_n, nodeActionFn);        
        return _n;
    }
    
    public <N extends _java> N forSelectedIn(N _n, Consumer<$node.Select> selectActionFn ) {
        $fullName.forSelectedIn(_n, selectActionFn);
        $memberNames.forEach( e-> e.forSelectedIn(_n, selectActionFn) );
        $simpleName.forSelectedIn(_n, selectActionFn);
        return _n;
    }
     
    public <N extends _java> N forSelectedIn(N _n, Predicate<$node.Select> selectConstraint, Consumer<$node.Select> selectActionFn ) {
        $fullName.forSelectedIn(_n, selectConstraint, selectActionFn);
        $memberNames.forEach( e-> e.forSelectedIn(_n, selectConstraint, selectActionFn) );
        $simpleName.forSelectedIn(_n, selectConstraint, selectActionFn);
        return _n;
    }
    
    public <N extends _java> List<$node.Select> listSelectedIn( N _n ){
        List<$node.Select> sels = new ArrayList<>();
        sels.addAll( $fullName.listSelectedIn(_n) );
        $memberNames.forEach( e-> sels.addAll( e.listSelectedIn(_n) ) );
        sels.addAll( $simpleName.listSelectedIn(_n) );
        
        List<$node.Select>res = sels.stream().distinct().collect(Collectors.toList());
        Collections.sort(res, $node.SELECT_START_POSITION_COMPARATOR);
        return res;               
    }
    
    public <N extends _java> List<$node.Select> listSelectedIn( N _n, Predicate<$node.Select> selectConstraint){
        List<$node.Select> sels = new ArrayList<>();
        sels.addAll( $fullName.listSelectedIn(_n, selectConstraint) );
        $memberNames.forEach( e-> sels.addAll( e.listSelectedIn(_n) ) );
        sels.addAll( $simpleName.listSelectedIn(_n) );
        
        //dedupe
        List<$node.Select>uniqueSels = sels.stream().distinct().collect(Collectors.toList());
        
        //the selections are interleaved, so lets organize them in positional 
        //order (in the file)
        Collections.sort(uniqueSels, $node.SELECT_START_POSITION_COMPARATOR);
        
        return uniqueSels;        
    }
    
    public _type forEachIn(Class clazz, Consumer<Node> nodeActionFn ) {
        return forEachIn( _java.type(clazz), nodeActionFn);
    }
    
    public _type forSelectedIn(Class clazz, Consumer<$node.Select> selectActionFn ) {
        return forSelectedIn( _java.type(clazz), selectActionFn);
    }
     
    public _type forSelectedIn(Class clazz, Predicate<$node.Select> selectConstraint, Consumer<$node.Select> selectActionFn ) {
        return forSelectedIn(_java.type(clazz), selectConstraint, selectActionFn);
    }
    
    public List<$node.Select> listSelectedIn( Class clazz ){        
        return listSelectedIn( _java.type(clazz));
    }
    
    public List<$node.Select> listSelectedIn( Class clazz, Predicate<$node.Select> selectConstraint){
        return listSelectedIn( _java.type(clazz));        
    }    
}
