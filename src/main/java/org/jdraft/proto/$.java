package org.jdraft.proto;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.Type;
import org.jdraft.Ex;
import org.jdraft.*;
import org.jdraft._anno._annos;
import org.jdraft._parameter._parameters;
import java.lang.annotation.Annotation;
import java.util.function.*;
import org.jdraft._typeParameter._typeParameters;
import org.jdraft.macro._macro;
import org.jdraft.macro._remove;
import org.jdraft.macro.macro;

/**
 * This abstraction is a shortcut to unify all of the $prototypes in a single
 * API that is easy / convenient to use (since modern IDES will autocomplete, this
 * will help simplify getting up to speed/ using the protyotype API)
 * 
 * @author Eric
 */
public final class $ {

    public static Node.TreeTraversal PARENTS = Node.TreeTraversal.PARENTS;
    public static Node.TreeTraversal PRE_ORDER = Node.TreeTraversal.PREORDER;
    public static Node.TreeTraversal POST_ORDER = Node.TreeTraversal.POSTORDER;
    public static Node.TreeTraversal BREADTH_FIRST = Node.TreeTraversal.BREADTHFIRST;
    public static Node.TreeTraversal DIRECT_CHILDREN = Node.TreeTraversal.DIRECT_CHILDREN;

    public static $modifiers PUBLIC = $modifiers.of("public");
    public static $modifiers PRIVATE = $modifiers.of("private");
    public static $modifiers PROTECTED = $modifiers.of("protected");

    public static $modifiers ABSTRACT = $modifiers.of("abstract");
    public static $modifiers STATIC = $modifiers.of("static");
    public static $modifiers FINAL = $modifiers.of("final");

    public static $modifiers NOT_ABSTRACT = $modifiers.of(m -> !m.isAbstract() );
    public static $modifiers NOT_STATIC = $modifiers.of(m -> !m.isStatic() );
    public static $modifiers NOT_FINAL = $modifiers.of(m -> !m.isFinal() );

    public static $modifiers SYNCHRONIZED = $modifiers.of("synchronized");
    public static $modifiers TRANSIENT = $modifiers.of("transient");
    public static $modifiers VOLATILE = $modifiers.of("volatile");
    public static $modifiers NATIVE = $modifiers.of("native");
    public static $modifiers STRICT_FP = $modifiers.of("strictfp");

    /** cant construct one of these */
    private $(){}

    /** Functionality */
    public static boolean hasAncestor( Node node, Predicate<Node> ancestorMatchFn){
        return Ast.hasAncestor(node, ancestorMatchFn );
    }

    /** True if the node has an ancestor */
    public static boolean hasAncestor( Node node, $proto $p ){
        return Ast.hasAncestor(node, a -> $p.match(a) );
    }

    public static boolean hasAncestor( _java _j, $proto $p ){
        return _java.hasAncestor( _j, Node.class, a-> $p.match(a) );
    }

    /** True if the node has an ancestor */
    public static boolean hasDescendant( Node node, $proto $p ){
        return Ast.hasDescendant(node, a -> $p.match(a) );
    }

    public static boolean hasDescendant( _java _j, $proto $p ){
        return _java.hasDescendant( _j, Node.class, a-> $p.match(a) );
    }

    /**
     *
     * @return
     */
    public static $node of() { return $node.of(); }

    /**
     * @param proto the node text or pattern
     * @param nodeClasses
     * @param <N>
     * @return
     */
    public static <N extends Node> $node of(String proto, Class<N>...nodeClasses){
        return $node.of(proto, nodeClasses);
    }

    public static $node of(Class<? extends Node> nodeClass){
        return $node.of(new Class[]{nodeClass});
    }

    public static $node of(Class<? extends Node> nodeClass, Class<? extends Node> nodeClass2){
        return $node.of(new Class[]{nodeClass, nodeClass2});
    }

    public static $node of(Class<? extends Node> nodeClass, Class<? extends Node> nodeClass2, Class<? extends Node> nodeClass3){
        return $node.of(new Class[]{nodeClass, nodeClass2, nodeClass3});
    }

    public static $node of(Class<? extends Node> nodeClass, Class<? extends Node> nodeClass2, Class<? extends Node> nodeClass3, Class<? extends Node> nodeClass4){
        return $node.of(new Class[]{nodeClass, nodeClass2, nodeClass3, nodeClass4});
    }

    public static $id id (){
        return $id.of();
    }

    public static $id id( String pattern ){
        return $id.of(pattern);
    }

    public static $id id( Predicate<String> idMatchFn){
        return $id.of(idMatchFn);
    }

    public static $id name (){
        return $id.of();
    }

    public static $id name( String pattern ){
        return $id.of(pattern);
    }

    public static $id name( Predicate<String> idMatchFn){
        return $id.of(idMatchFn);
    }

    /**
     *
     * @param nodeClasses
     * @param <N>
     * @return
     */
    public static <N extends Node> $node of(Class<N>...nodeClasses){
        return $node.of(nodeClasses);
    }

    /**
     *
     * @param $ps
     * @return
     */
    public static $node of( $proto ... $ps ){
        return $node.of( $ps );
    }

    public static $anno anno(){
        return $anno.of();
    }
    
    public static $anno anno( $id name, $anno.$memberValue...memberValues ){
        return $anno.of(name, memberValues);
    }
    
    public static $anno anno( Predicate<_anno> constraint){
        return $anno.of().$and(constraint);
    }
    
    public static $anno anno( String pattern){
        return $anno.of(pattern);
    }
    
    public static $anno anno(String pattern, Predicate<_anno> constraint){
        return $anno.of(pattern).$and(constraint);
    }
    
    public static $anno anno( Class<? extends Annotation> clazz ){
        return $anno.of(clazz);
    }
    
    public static $anno anno( Class<? extends Annotation> clazz, Predicate<_anno> constraint){
        return $anno.of(clazz).$and(constraint);
    }
    
    public static $annos annos(){
        return $annos.of();
    }
    
    public static $annos annos( Predicate<_annos> constraint ){
        return $annos.of().$and(constraint);
    }
    
    public static $annos annos( _annos _anns ){
        return $annos.of(_anns);
    }
    
    public static $annos annos( _annos _anns, Predicate<_annos> constraint){
        return $annos.of(_anns).$and(constraint);
    }
    
    public static $annos annos($anno... annos){
        return $annos.of(annos);
    }

    /**
     * @see JavadocComment
     * @return
     */
    public static $comment<JavadocComment> javadoc(){
        return $comment.javadocComment();
    }

    /**
     * @see JavadocComment
     * @param contentPattern
     * @return
     */
    public static $comment<JavadocComment> javadoc( String contentPattern){
        return $comment.javadocComment(contentPattern);
    }

    /**
     *
     * @see JavadocComment
     * @param constraint
     * @return
     */
    public static $comment<JavadocComment> javadoc( Predicate<JavadocComment> constraint ){
        return $comment.javadocComment(constraint);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @return
     */
    public static $constructor constructor(){
        return $constructor.of();
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param constraint
     * @return
     */
    public static $constructor constructor( Predicate<_constructor> constraint){
        return $constructor.of().$and(constraint);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param _proto
     * @return
     */
    public static $constructor constructor( _constructor _proto ){
        return $constructor.of(_proto);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param _proto
     * @param constraint
     * @return
     */
    public static $constructor constructor( _constructor _proto, Predicate<_constructor> constraint ){
        return $constructor.of(_proto).$and(constraint);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param pattern
     * @return
     */
    public static $constructor constructor( String pattern){
        return $constructor.of(pattern);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param pattern
     * @param constraint
     * @return
     */
    public static $constructor constructor( String pattern, Predicate<_constructor> constraint){
        return $constructor.of(pattern).$and(constraint);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param pattern
     * @return
     */
    public static $constructor constructor( String...pattern){
        return $constructor.of(pattern);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param part
     * @return
     */
    public static $constructor constructor($constructor.$part part){
        return $constructor.of(part);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param parts
     * @return
     */
    public static $constructor constructor($constructor.$part...parts){
        return $constructor.of(parts);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param anonymousObjectContainingMethod
     * @return
     */
    public static $constructor constructor(Object anonymousObjectContainingMethod){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return $constructor.of( ste, anonymousObjectContainingMethod);
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @return
     */
    public static $method method(){
        return $method.of();
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @param methodPrototype
     * @return
     */
    public static $method method(String methodPrototype){
        return $method.of(methodPrototype);
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @param ms
     * @return
     */
    public static $method method(String...ms){
        return $method.of(ms);
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @param anonymousObjectContainingMethod
     * @return
     */
    public static $method method(Object anonymousObjectContainingMethod){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Ex.anonymousObjectEx(ste);
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        //removeIn all things that aren't METHODS or METHODS WITH @_remove
        bds.removeIf(b -> b.isAnnotationPresent(_remove.class) || (!(b instanceof MethodDeclaration)));
        //there should be only (1) method left, if > 1 take the first method
        MethodDeclaration md = (MethodDeclaration) bds.get(0);
        _method _m =  macro.to(anonymousObjectContainingMethod.getClass(), md);

        return $method.of( _m);
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @param constraint
     * @return
     */
    public static $method method(Predicate<_method> constraint){
        return $method.of().$and(constraint);
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @param ms
     * @return
     */
    public static $method method(_method ms){
        return $method.of(ms);
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @param ms
     * @param _methodMatchFn
     * @return
     */
    public static $method method(_method ms, Predicate<_method> _methodMatchFn){
        return $method.of(ms).$and(_methodMatchFn);
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @param part
     * @return
     */
    public static $method method($method.$part part){
        return $method.of(part);
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @param parts
     * @return
     */
    public static $method method($method.$part...parts){
        return $method.of(parts);
    }

    public static $field field( $field.$part...parts ){
        return $field.of(parts);
    }

    public static $field field(){
        return $field.of();
    }

    public static $field field(String fc){
        return $field.of(fc);
    }
    
    public static $field field(Predicate<_field> constraint){
        return $field.of().$and(constraint);
    }

    public static $field field(_field f){
        return $field.of(f);
    }

    /**
     * @see com.github.javaparser.ast.ImportDeclaration
     * @see _import
     * @return
     */
    public static $import importDecl(){
        return $import.of();
    }

    /**
     * @see com.github.javaparser.ast.ImportDeclaration
     * @see _import
     * @param pattern
     * @return
     */
    public static $import importDecl(String pattern){
        return $import.of(pattern);
    }

    /**
     * @see com.github.javaparser.ast.ImportDeclaration
     * @see _import
     * @param constraint
     * @return
     */
    public static $import importDecl(Predicate<_import> constraint){
        return $import.of().$and(constraint);
    }

    /**
     * @see com.github.javaparser.ast.ImportDeclaration
     * @see _import
     * @param imp
     * @return
     */
    public static $import importDecl(_import imp){
        return $import.of(imp);
    }

    /**
     * @see com.github.javaparser.ast.Modifier
     * @see com.github.javaparser.ast.nodeTypes.NodeWithModifiers
     * @see _modifiers
     * @return
     */
    public static $modifiers modifiers(){
        return $modifiers.of();
    }

    /**
     * @see com.github.javaparser.ast.Modifier
     * @see com.github.javaparser.ast.nodeTypes.NodeWithModifiers
     * @see _modifiers
     * @param $mods
     * @return
     */
    public static $modifiers modifiers( $modifiers... $mods ){
        return $modifiers.of( $mods );
    }

    /**
     * @see com.github.javaparser.ast.Modifier
     * @see com.github.javaparser.ast.nodeTypes.NodeWithModifiers
     * @see _modifiers
     * @param ms
     * @return
     */
    public static $modifiers modifiers(String...ms){
        return $modifiers.of(ms);
    }

    /**
     * @see com.github.javaparser.ast.Modifier
     * @see com.github.javaparser.ast.nodeTypes.NodeWithModifiers
     * @see _modifiers
     * @param constraint
     * @return
     */
    public static $modifiers modifiers(Predicate<_modifiers> constraint){
        return $modifiers.of().$and(constraint);
    }

    /**
     * @see com.github.javaparser.ast.Modifier
     * @see com.github.javaparser.ast.nodeTypes.NodeWithModifiers
     * @see _modifiers
     * @param ms
     * @return
     */
    public static $modifiers modifiers(_modifiers ms){
        return $modifiers.of(ms);
    }

    public static $package packageDecl( ){
        return $package.of();
    }

    public static $package packageDecl(Predicate<PackageDeclaration> packageNameMatchFn ){
        return $package.of(packageNameMatchFn);
    }

    public static $package packageDecl(String pattern ){
        return $package.of(pattern);
    }

    public static $package packageDecl(String pattern, Predicate<PackageDeclaration> packageFn){
        return $package.of(pattern, packageFn);
    }

    /**
     * Static initializer block of a type
     * @return a prototype representing any static initializer block
     */
    public static $initBlock staticBlock( ){
        return $initBlock.of().setStatic();
    }

    /**
     * Static initializer block of a type
     * @return a prototype representing any static initializer block
     */
    public static $initBlock staticBlock(Predicate<_initBlock> packageNameMatchFn ){
        return $initBlock.of( packageNameMatchFn).setStatic();
    }

    /**
     * Static initializer block of a type
     * @return a prototype representing any static initializer block
     */
    public static $initBlock staticBlock(String pattern ){
        return $initBlock.of(pattern).setStatic();
    }

    /**
     * Static initializer block of a type
     * @return a prototype representing any static initializer block
     */
    public static $initBlock staticBlock(String... pattern ){
        return $initBlock.of(pattern).setStatic();
    }

    public static $initBlock initBlock( ){
        return $initBlock.of();
    }

    public static $initBlock initBlock(Predicate<_initBlock> packageNameMatchFn ){
        return $initBlock.of( packageNameMatchFn);
    }

    public static $initBlock initBlock(String... pattern ){
        return $initBlock.of(pattern);
    }

    public static $parameters parameters(){
        return $parameters.of();
    }

    public static $parameters parameters(String... parameters){
        return $parameters.of(parameters);
    }
    
    public static $parameters parameters(Predicate<_parameters> constraint){
        return $parameters.of().$and(constraint);
    }
    
    public static $parameter parameter(){
        return $parameter.of();
    }

    public static $parameter parameter(String p){
        return $parameter.of(p);
    }

    public static $parameter parameter( _parameter p){
        return $parameter.of(p);
    }

    public static $parameter parameter(Predicate<_parameter> constraint){
        return $parameter.of().$and(constraint);
    }
    

    public static $throws throwsDecl(){
        return $throws.of();
    }

    public static $throws throwsDecl(String...th){
        return $throws.of(th);
    }

    public static $throws throwsDecl(_throws ts){
        return $throws.of(ts);
    }

    public static $throws throwsDecl(Predicate<_throws> ts){
        return $throws.of().$and( ts);
    }
    
    public static $typeParameter typeParameter(){
        return $typeParameter.of();
    }

    public static $typeParameter typeParameter(String tp){
        return $typeParameter.of(tp);
    }

    public static $typeParameter typeParameter(_typeParameter tp){
        return $typeParameter.of(tp);
    }

    public static $typeParameter typeParameter(Predicate<_typeParameter> tp){
        return $typeParameter.of().$and(tp);
    }
    
    public static $typeParameters typeParameters(){
        return $typeParameters.of();
    }

    public static $typeParameters typeParameters(String...tps){
        return $typeParameters.of(tps);
    }

    public static $typeParameters typeParameters(_typeParameters tps){
        return $typeParameters.of(tps);
    }

    public static $typeParameters typeParameters(Predicate<_typeParameters> tps){
        return $typeParameters.of().$and(tps);
    }
    
    public static $typeRef typeRef(){
        return $typeRef.of();
    }

    public static $typeRef typeRef(String tr){
        return $typeRef.of(tr);
    }

    public static $typeRef typeRef( Class clazz){
        return $typeRef.of(clazz);
    }

    public static $typeRef typeRef(Type tr){
        return $typeRef.of(tr);
    }

    public static $typeRef typeRef(Predicate<_typeRef> tr){
        return $typeRef.of().$and(tr);
    }

    public static $var var(){
        return $var.of();
    }

    public static $var var(String var){
        return $var.of(var);
    }

    public static $var var(VariableDeclarator var){
        return $var.of(var);
    }
    
    public static $var var(Predicate<VariableDeclarator> vd){
        return $var.of().$and(vd);
    }
}
