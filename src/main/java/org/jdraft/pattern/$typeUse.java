package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jdraft.*;
import org.jdraft.pattern.$node.Select;

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
        return of(clazz).and(constraint);
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

    /**
     * Matches any 
     * @return the classUse
     */
    public static $typeUse of(){
        return new $typeUse("", $node.of("$typeUse$").$and(n -> n.toString().contains(".")),
            Collections.EMPTY_LIST, 
            $node.of("$typeUse$").$and(n -> !n.toString().contains(".") ) ).and(IS_EXPECTED_NODE_TYPE);
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
            .$and( IS_EXPECTED_NODE_TYPE );
        //Note: there can be (0, 1, or more OTHER nodes that represent
        //Inner member classes, i.e. not fully qualified 
        
        this.$simpleName = new $node(type.getSimpleName()).$and(IS_EXPECTED_NODE_TYPE);
        $memberNames = $typeUse.buildMemberClassNames( type );
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public $typeUse and(Predicate<Node> constraint){
        $fullName.$and(constraint);
        $memberNames.forEach(m -> m.$and(constraint));
        $simpleName.$and(constraint);
        return this;
    }
    
    public $node.Select select( Node astNode ){
        
        Select sel = $fullName.select(astNode);
        if( sel != null ){
            return sel;
        }
        Optional<$node> on = 
            $memberNames.stream().filter( m -> m.select(astNode) != null ).findFirst();
        if( on.isPresent() ){
            return on.get().select(astNode);
        }
        sel = $simpleName.select(astNode);
        return sel;
    }
    
    public <_J extends _java._domain> _J replaceIn(_J _j, Class replacement) {
        if( _j instanceof _compilationUnit){
            if( ((_compilationUnit) _j).isTopLevel()){
                replaceIn( ((_compilationUnit) _j).astCompilationUnit(), replacement);
                return _j;
            }
            replaceIn( ((_type) _j).ast(), replacement);
            return _j;
        }
        replaceIn( ((_java._node) _j).ast(), replacement);
        return _j;
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
            nodes.add( $node.of(currentPath).$and(IS_EXPECTED_NODE_TYPE) );
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

    public <_CT extends _type> _CT  replaceIn(Class clazz, String replacement){
        return (_CT)replaceIn( (_type)_java.type(clazz), replacement);
    }
    
    public <_J extends _java._domain> _J replaceIn(_J _j, String replacement) {
        if( _j instanceof _compilationUnit){
            if( ((_compilationUnit) _j).isTopLevel()){
                replaceIn( ((_compilationUnit) _j).astCompilationUnit(), replacement);
                return _j;
            }
            replaceIn( ((_type) _j).ast(), replacement);
            return _j;
        }
        replaceIn( ((_java._node) _j).ast(), replacement);
        return _j;
    }
    
    public <N extends Node> N replaceIn(N astRootNode, Node replacement) {
        $fullName.replaceIn(astRootNode, replacement);
        $memberNames.forEach(n -> n.replaceIn(astRootNode, replacement));
        $simpleName.replaceIn(astRootNode, replacement);        
        return astRootNode;
    }

    public <_J extends _java._domain> _J replaceIn(_J _j, Node replacement) {
        if( _j instanceof _compilationUnit){
            if( ((_compilationUnit) _j).isTopLevel()){
                replaceIn( ((_compilationUnit) _j).astCompilationUnit(), replacement);
                return _j;
            }
            replaceIn( ((_type) _j).ast(), replacement);
            return _j;
        }
        replaceIn( ((_java._node) _j).ast(), replacement);
        return _j;
    }
    
    public <_CT extends _type> _CT  replaceIn(Class clazz, Node replacement){
        return (_CT)replaceIn((_type)_java.type(clazz), replacement);
    }
    
    public <_CT extends _type> _CT  removeIn(Class clazz){
        return (_CT)removeIn((_type)_java.type(clazz) );
    }
    
    public <_N extends _java._node> _N removeIn(_N _n ) {
        if( _n instanceof _compilationUnit){
            if( ((_compilationUnit) _n).isTopLevel()){
                removeIn( ((_compilationUnit) _n).astCompilationUnit() );
                return _n;
            }
            removeIn( ((_type)_n).ast() );
            return _n;
        }
        removeIn( ((_java._node)_n).ast() );
        return _n;
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
        return listIn((_type)_java.type(clazz));
    }

    public <_N extends _java._node> List<Node> listIn(_N _node ){
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
        Collections.sort(uniqueSels, Ast.COMPARE_NODE_BY_POSITION);

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
    
    public <_J extends _java._domain> _J forEachIn(_J _j, Consumer<Node> nodeActionFn ) {
        $fullName.forEachIn(_j, nodeActionFn);
        $memberNames.forEach(n -> n.forEachIn(_j, nodeActionFn ) );
        $simpleName.forEachIn(_j, nodeActionFn);
        return _j;
    }
    
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Consumer<$node.Select> selectActionFn ) {
        $fullName.forSelectedIn(_j, selectActionFn);
        $memberNames.forEach( e-> e.forSelectedIn(_j, selectActionFn) );
        $simpleName.forSelectedIn(_j, selectActionFn);
        return _j;
    }
     
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Predicate<$node.Select> selectConstraint, Consumer<$node.Select> selectActionFn ) {
        $fullName.forSelectedIn(_j, selectConstraint, selectActionFn);
        $memberNames.forEach( e-> e.forSelectedIn(_j, selectConstraint, selectActionFn) );
        $simpleName.forSelectedIn(_j, selectConstraint, selectActionFn);
        return _j;
    }
    
    public <_J extends _java._domain> List<$node.Select> listSelectedIn(_J _j){
        List<$node.Select> sels = new ArrayList<>();
        sels.addAll( $fullName.listSelectedIn(_j) );
        $memberNames.forEach( e-> sels.addAll( e.listSelectedIn(_j) ) );
        sels.addAll( $simpleName.listSelectedIn(_j) );
        
        List<$node.Select>res = sels.stream().distinct().collect(Collectors.toList());
        Collections.sort(res, $node.SELECT_START_POSITION_COMPARATOR);
        return res;               
    }
    
    public <_J extends _java._domain> List<$node.Select> listSelectedIn(_J _j, Predicate<$node.Select> selectConstraint){
        List<$node.Select> sels = new ArrayList<>();
        sels.addAll( $fullName.listSelectedIn(_j, selectConstraint) );
        $memberNames.forEach( e-> sels.addAll( e.listSelectedIn(_j) ) );
        sels.addAll( $simpleName.listSelectedIn(_j) );
        
        //dedupe
        List<$node.Select>uniqueSels = sels.stream().distinct().collect(Collectors.toList());
        
        //the selections are interleaved, so lets organize them in positional 
        //order (in the file)
        Collections.sort(uniqueSels, $node.SELECT_START_POSITION_COMPARATOR);
        
        return uniqueSels;        
    }
    
    public _type forEachIn(Class clazz, Consumer<Node> nodeActionFn ) {
        return forEachIn( (_type)_java.type(clazz), nodeActionFn);
    }
    
    public _type forSelectedIn(Class clazz, Consumer<$node.Select> selectActionFn ) {
        return forSelectedIn( (_type)_java.type(clazz), selectActionFn);
    }
     
    public _type forSelectedIn(Class clazz, Predicate<$node.Select> selectConstraint, Consumer<$node.Select> selectActionFn ) {
        return forSelectedIn((_type)_java.type(clazz), selectConstraint, selectActionFn);
    }
    
    public List<$node.Select> listSelectedIn( Class clazz ){        
        return listSelectedIn( (_type)_java.type(clazz));
    }
    
    public List<$node.Select> listSelectedIn( Class clazz, Predicate<$node.Select> selectConstraint){
        return listSelectedIn( (_type)_java.type(clazz));
    }    
}
