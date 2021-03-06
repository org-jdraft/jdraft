package org.jdraft;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import org.jdraft._java.*;
import com.github.javaparser.utils.Log;
import org.jdraft.io._in;
import org.jdraft.io._io;
import org.jdraft.io._ioException;
import org.jdraft.macro._toCtor;
import org.jdraft.macro.macro;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;

/**
 * Top-Level jdraft object representing a Java class, and implementation of a {@link _type}<BR/>
 *
 * Logical Mutable Model of the source code representing a Java class.<BR>
 *
 * Implemented as a "Logical Facade" on top of an AST
 * ({@link ClassOrInterfaceDeclaration}) for logical manipulation
 *
 * @author Eric
 */
public final class _class implements _type<ClassOrInterfaceDeclaration, _class>,
        _method._withMethods<_class>, _constructor._withConstructors<_class, ClassOrInterfaceDeclaration>,
        _typeParams._withTypeParams<_class>, _initBlock._withInitBlocks<_class>,
        _modifiers._withAbstract<_class>, _modifiers._withFinal<_class>,
        _modifiers._withStatic<_class>, _type._withImplements<_class>,
        _type._withExtends<_class> {

    public static final Function<String, _class> PARSER = s-> _class.of(s);

    public static _class of(){
        return of( new ClassOrInterfaceDeclaration());
    }

    /**
     * Build a _class from the source of the Class, while applying any
     * runtime _macro ANNOTATIONS (i.e. @_static, @_final...) to the components on the
     * class; and finally calling the optional typeFns in order to mutate the {@link _class}<BR/>
     * Example:
     * <PRE>
     *     @_package("aaaa") class C { int a,b; @_static String NAME = "A";}
     *     _class _c = _class.of( C.class, _autoDto.$);
     *
     *     // the above will build the class _c from the source of C,
     *     // apply the @_package, and @_static macros to update _c
     *     // then apply the {@link org.jdraft.macro._dto} _macro to _c
     * </PRE>
     *
     * @param clazz
     * //@param typeFns
     * @return
     */
    public static _class of( Class clazz) {
        return of(Ast.JAVAPARSER, clazz);
    }

    /**
     * Build a _class from the source of the Class, while applying any
     * runtime _macro ANNOTATIONS (i.e. @_static, @_final...) to the components on the
     * class; and finally calling the optional typeFns in order to mutate the {@link _class}<BR/>
     * Example:
     * <PRE>
     *     @_package("aaaa") class C { int a,b; @_static String NAME = "A";}
     *     _class _c = _class.of( C.class, _autoDto.$);
     *
     *     // the above will build the class _c from the source of C,
     *     // apply the @_package, and @_static macros to update _c
     *     // then apply the {@link org.jdraft.macro._dto} _macro to _c
     * </PRE>
     *
     * @param javaParser the javaParser instance
     * @param clazz
     * //@param typeFns
     * @return
     */
    public static _class of( JavaParser javaParser, Class clazz) {
        TypeDeclaration n = Ast.typeDeclaration( javaParser, clazz );
        if( n instanceof ClassOrInterfaceDeclaration){
            _class _c = of(n);
            
            _c = macro.to(clazz, n); //run annotation macros on the class
            Set<Class> importClasses = _import.inferImportsFrom(clazz);
            _c.addImports(importClasses.toArray(new Class[0]));
            return _c;
        }
        if( clazz.isInterface() ){
            throw new _jdraftException("cannot create _class from (interface) "+ clazz);
        }
        if( clazz.isAnnotation() ){
            throw new _jdraftException("cannot create _class from annotation "+ clazz);
        }
        if( clazz.isEnum() ){
            throw new _jdraftException("cannot create _class from enum "+ clazz);
        }
        throw new _jdraftException("Abstract or synthetic classes are not supported"+ clazz);
    }

    /**
     *
     * @param p
     * @return
     */
    public static _class of( Path p) {
        return of(Ast.JAVAPARSER, p);
    }

    /**
     *
     * @param javaParser
     * @param p
     * @return
     */
    public static _class of(JavaParser javaParser, Path p){
        return of(javaParser, _io.inFile(p));
    }

    /**
     * return a _class from this input
     * @param in
     * @return 
     */
    public static _class of( _in in ) {
        return of( Ast.JAVAPARSER, in);
    }

    /**
     *
     * @param javaParser
     * @param in
     * @return
     */
    public static _class of( JavaParser javaParser, _in in ){
        return of( javaParser, in.getInputStream() );
    }

    /**
     * create and return a _class representing the Class source within the inputStream
     * @param is
     * @return 
     */
    public static _class of( InputStream is ) {
        return of( Ast.JAVAPARSER, is);
    }

    /**
     *
     * @param javaParser
     * @param is
     * @return
     */
    public static _class of( JavaParser javaParser, InputStream is ) {
        return (_class) _type.of(javaParser, is);
    }

    /**
     *
     * @param url
     * @return
     */
    public static _class of(URL url) {
        return of(Ast.JAVAPARSER, url);
    }

    /**
     *
     * @param javaParser
     * @param url
     * @return
     */
    public static _class of(JavaParser javaParser, URL url) {
        try {
            InputStream inStream = url.openStream();
            return of(javaParser, inStream);
        }catch(IOException ioe){
            throw new _ioException("invalid input url \""+url.toString()+"\"", ioe);
        }
    }

    /**
     * Return the _class represented by this single line class definition
     * @param classDef the definition of the class as a String
     * @return the _class model
     */
    public static _class of( String classDef){
        return of( Ast.JAVAPARSER, new String[]{classDef});
    }

    /**
     *
     * @param javaParser
     * @param classDef
     * @return
     */
    public static _class of(JavaParser javaParser, String classDef){
        return of( javaParser, new String[]{classDef});
    }

    /**
     * we need this as to not confuse the caller API with
     * (2) Strings with a String, and Object () for anonymousClassBody
     * @param classDef1
     * @param classDef2
     * @return
     * @see #of(String, Object)
     */
    public static _class of(String classDef1, String classDef2){
        return of( new String[]{classDef1, classDef2});
    }

    /**
     * if you pass a single line, with a single token (NO SPACES) into this, we create a shortcut class
     * you can specify the PACKAGE_NAME.className
     *
     * Shortcut classes infer you want to get
     * <UL>
     * <LI>shortcut classes, i.e._class.of("C") -> creates "public class C{}"
     * <LI>shortcut classes, i.e._class.of("C<T>") -> creates "public class C<T>{}"
     * <LI>shortcut classes, i.e. _class.of("aaaa.bbbb.C") -> creates "package aaaa.bbbb;  public class C{}"
     * <LI>shortcut classes, i.e. _class.of("aaaa.bbbb.C<Obj>") -> creates "package aaaa.bbbb;  public class C<Obj>{}"
     * </UL>
     * 
     * @param classDef
     * @return 
     */ 
    public static _class of( String...classDef ) {
        return of(Ast.JAVAPARSER, classDef);
    }

    /**
     *
     * @param javaParser
     * @param classDef
     * @return
     */
    public static _class of( JavaParser javaParser, String...classDef ) {
        if( classDef.length == 0){
            return of();
        }
        if( classDef.length == 1){
            String[] strs = classDef[0].split(" ");

            if( strs.length == 1 ){
                //definitely shortcut classes

                String shortcutClass = strs[0];
                String packageName = null;
                int lastDotIndex = shortcutClass.lastIndexOf('.');
                if( lastDotIndex >0 ){
                    packageName = shortcutClass.substring(0, lastDotIndex );
                    shortcutClass = shortcutClass.substring(lastDotIndex + 1);
                    if(!shortcutClass.endsWith("}")){
                        shortcutClass = shortcutClass + "{}";
                    }

                    return of( Ast.of( javaParser,"package "+packageName+";"+System.lineSeparator()+
                        "public class "+shortcutClass));
                }
                if(!shortcutClass.endsWith("}")){
                    shortcutClass = shortcutClass + "{}";
                }
                _class _c = of( Ast.of(javaParser,"public class "+shortcutClass));
                return _c;
            }
            else{
                if( !classDef[0].trim().endsWith("}" ) ){
                    classDef[0] = classDef[0]+"{}";
                }
            }
        }
        //check if the classDef has a "private " before " class ", if so, remove it
        //because it'll fail, so remove the private, then add it back in manually
        String cc = Text.combine(classDef);
        int classIndex = cc.indexOf(" class ");
        int privateIndex = cc.indexOf( "private ");
        if( privateIndex >= 0 && privateIndex < classIndex ){
            cc = cc.substring(0, privateIndex)+ cc.substring(privateIndex + "private ".length());
            _class _c = of( Ast.of(javaParser, cc ));
            _c.setPrivate();
            return _c;
        }
        return of( Ast.of( javaParser, classDef ));
    }

    /**
     * return the _class represented by this Ast TypeDeclaration
     * @param astTypeDecl the ast type declaration
     * @return the _class representing the TypeDeclaration
     */
    public static _class of( TypeDeclaration astTypeDecl ){
        if( astTypeDecl instanceof ClassOrInterfaceDeclaration && !astTypeDecl.asClassOrInterfaceDeclaration().isInterface() ) {
            return new _class( (ClassOrInterfaceDeclaration)astTypeDecl);
        }
        throw new _jdraftException("Expected AST ClassOrInterfaceDeclaration as Class, got "+ astTypeDecl.getClass() );
    }
    
    public static _class of( CompilationUnit cu ){
        NodeList<TypeDeclaration<?>> tds = cu.getTypes();
        if( tds.size() == 1 ){ //if there is only (1) type, trust its the main type
            return of( (ClassOrInterfaceDeclaration)tds.get(0) );
        }
        //if there is any public type its the top
        Optional<TypeDeclaration<?>> ptd = tds.stream().filter(t-> t.isPublic()).findFirst();
        if( ptd.isPresent() ){
            return of( ptd.get() );
        }
        //here there must be more than one type, but cant decide maybe check storage
        if( cu.getPrimaryTypeName().isPresent() ){
            return of( cu.getClassByName( cu.getPrimaryTypeName().get() ).get() );
        }
        if( cu.getTypes().size() > 1 ){
            //I want to class
            Optional<TypeDeclaration<?>> coid = cu.getTypes().stream().filter(
                    t-> t instanceof ClassOrInterfaceDeclaration && ((ClassOrInterfaceDeclaration) t).isInterface()).findFirst();
            if( coid.isPresent() ){
                return of( coid.get());
            }
            return of( cu.getType(0) );
        }
        throw new _jdraftException("Unable to locate primary TYPE in "+ cu);
    }

    public static _class of( ClassOrInterfaceDeclaration astClass ){
        return new _class( astClass );
    }

    /**
     * builds a class with the signature and the anonymous class body
     * applies any macro annotations to the Object Body
     * then apply the typeFunctions in order
     * for example:
     * <PRE>
     * _class.of("aaaa.bbb.C", new Object(){ @_init("100") int x,y }, @_autoGetter.$);
     * //produces
     * package aaaa.bbb;
     * 
     * public class C{
     *   int x =100, y = 100;
     * 
     *   public int getX(){
     *       return x;
     *   }
     * 
     *   public int getY(){
     *       return y;
     *   }
     * }
     * </PRE>
     * @param signature
     * @param anonymousClassBody
     * @return
     */
    public static _class of( String signature, Object anonymousClassBody) {
        return of(signature, anonymousClassBody, Thread.currentThread().getStackTrace()[2]);
    }

    /**
     *
     * @param signature
     * @param anonymousClassBody
     * @param ste
     * @return
     */
    public static _class of( String signature, Object anonymousClassBody, StackTraceElement ste) {
        //System.err.println("Got here");
        _class _c = _class.of(signature);
        Class theClass = anonymousClassBody.getClass();

        //interfaces to implement
        if(theClass.getInterfaces().length > 0){
            for(int i=0; i< theClass.getInterfaces().length; i++){
                _c.addImports(theClass.getInterfaces()[i]);
                _c.addImplement(theClass.getInterfaces()[i]);
            }
        }
        //extends to extend
        if( theClass.getSuperclass() != Object.class ){
            _c.addImports(theClass.getSuperclass());
            _c.addExtend(theClass.getSuperclass());
        }
        //
        ObjectCreationExpr oce = Expr.newExpr(ste);

        if( oce.getAnonymousClassBody().isPresent() ) {
            //System.err.println("Got here" + oce);
            NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
            for (int i = 0; i <bds.size(); i++){
                // check if the class has (one or more) void methods named the same as 
                // the Class name (these "methods" are really "constructors"
                BodyDeclaration bd = bds.get(i);
                if( bd instanceof MethodDeclaration ){
                    MethodDeclaration md = (MethodDeclaration)bd;
                    if( md.getNameAsString().equals(_c.getName() ) && md.getType().isVoidType() ){
                        _c.addConstructor(_constructor.of(_toCtor.Act.fromMethod(md)));
                    } else{
                        _c.node().addMember( bd );
                    }
                } else{
                    _c.node().addMember( bd );
                }
            }
        }
        //add imports from methods return types parameter types
        Set<Class>toImport = _import.inferImportsFrom(anonymousClassBody);

        _c.addImports(toImport.toArray(new Class[0]));

        TypeDeclaration td = _c.astCompilationUnit().getType(0);
        macro.to( theClass, td);
        return _c;
    }

    /** could be a single statement, or a block stmt */
    public static _feature._one<_class, _imports> IMPORTS = new _feature._one<>(_class.class, _imports.class,
            _feature._id.IMPORTS,
            a -> a.getImports(),
            (_class a, _imports b) -> a.setImports(b), PARSER);

    public static _feature._one<_class, _package> PACKAGE = new _feature._one<>(_class.class, _package.class,
            _feature._id.PACKAGE,
            a -> a.getPackage(),
            (_class a, _package b) -> a.setPackage(b), PARSER);

    public static _feature._one<_class, _annos> ANNOS = new _feature._one<>(_class.class, _annos.class,
            _feature._id.ANNOS,
            a -> a.getAnnos(),
            (_class a, _annos b) -> a.setAnnos(b), PARSER);

    public static _feature._one<_class, _javadocComment> JAVADOC = new _feature._one<>(_class.class, _javadocComment.class,
            _feature._id.JAVADOC,
            a -> a.getJavadoc(),
            (_class a, _javadocComment b) -> a.setJavadoc(b), PARSER);

    public static _feature._one<_class, _modifiers> MODIFIERS = new _feature._one<>(_class.class, _modifiers.class,
            _feature._id.MODIFIERS,
            a -> a.getModifiers(),
            (_class a, _modifiers b) -> a.setModifiers(b), PARSER);

    public static _feature._one<_class, String> NAME = new _feature._one<>(_class.class, String.class,
            _feature._id.NAME,
            a -> a.getName(),
            (_class a, String s) -> a.setName(s), PARSER);

    public static _feature._many<_class, _java._member> MEMBERS = new _feature._many<>(_class.class, _java._member.class,
            _feature._id.MEMBERS,
            _feature._id.MEMBER,
            a -> a.listMembers(),
            (_class a, List<_java._member>mems) -> a.setMembers(mems), PARSER, s-> _member.of(_class.class, s))
            .featureImplementations(_constructor.class, _method.class, _initBlock.class, _field.class, /*inner type*/_class.class, _enum.class, _annotation.class, _interface.class)
            .setOrdered(false);/** the order of declaration doesnt matter (MOSTLY) */

    public static _feature._one<_class, _typeParams> TYPE_PARAMS = new _feature._one<>(_class.class, _typeParams.class,
            _feature._id.TYPE_PARAMS,
            a -> a.getTypeParams(),
            (_class a, _typeParams b) -> a.setTypeParams(b), PARSER);

    public static _feature._one<_class, _typeRef> EXTENDS = new _feature._one<>(_class.class, _typeRef.class,
            _feature._id.EXTENDS,
            a -> a.getExtends(),
            (_class a, _typeRef exts) -> a.setExtends(exts), PARSER);

    public static _feature._many<_class, _typeRef> IMPLEMENTS = new _feature._many<>(_class.class, _typeRef.class,
            _feature._id.IMPLEMENTS,
            _feature._id.IMPLEMENT,
            a -> a.listImplements(),
            (_class a, List<_typeRef>mems) -> a.setImplements(mems), PARSER, s->_typeRef.of(s))
            .setOrdered(false);/** the order of declaring implements doesnt matter { class A implements B, C  === class A implements C, B }*/

    public static _feature._features<_class> FEATURES = _feature._features.of(_class.class, PARSER,
                    PACKAGE, IMPORTS, JAVADOC, ANNOS, MODIFIERS, NAME, TYPE_PARAMS, EXTENDS, IMPLEMENTS, MEMBERS);

    public _class( ClassOrInterfaceDeclaration node){
        this.node = node;
    }

    public _feature._features<_class> features(){
        return FEATURES;
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _class replace(ClassOrInterfaceDeclaration replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public ClassOrInterfaceDeclaration node() {
        return this.node;
    }

    /**
     * <PRE>
     * i.e. 
     * _class _c = _class.of("C").impl(
     *    new Descriptive(){
     *       public String describe() throws IOException{
     *           return "a description";
     *       }
     * });
     *</PRE>
     * will update C, and import any classes on the interface that is 
     * implemented.
     * <PRE>
     * //NOTE this import is on the public Descriptive API, so it gets added
     * import java.io.IOException;
     * 
     * public class C implements Descriptive{
     *     public String describe() throws IOException {
     *         return "a description";
     *     }
     * }
     * </PRE>
     * @param anonymousImplementation
     * @return the modified Class
     */
    public _class addToBody(Object anonymousImplementation ) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];

        for(int i=0;i<anonymousImplementation.getClass().getInterfaces().length;i++ ){
            addImplements( new Class[]{anonymousImplementation.getClass().getInterfaces()[i]} );
            addImports( new Class[]{anonymousImplementation.getClass().getInterfaces()[i]});
        }
        ObjectCreationExpr oce = Expr.newExpr(ste);
        if( oce.getAnonymousClassBody().isPresent()){

            oce.getAnonymousClassBody().get().forEach(m->{
                this.node().addMember(m);
            } );

        }
        Set<Class> ims = _import.inferImportsFrom(anonymousImplementation);
        ims.forEach( i -> addImports(i) );

        //macro.to( anonymousImplementation.getClass(), this.ast());
        return this;
    }

    /**
     * Adds a private property (a private field, and public get() and set() methods)
     * @param typeClass the type of the property
     * @param propertyName the name of the property
     * @return the modified _class
     */
    public _class addProperty(Class typeClass, String propertyName){
        return addProperty(typeClass, propertyName, (_expr)null);
    }

    /**
     * Adds a private property (a private field, and public get() and set() methods) & sets the fields init value as an expression
     * @param typeClass the type of the property
     * @param propertyName the name of the property
     * @param init the initial value to set the field as an expression
     * @return the modified _class
     */
    public _class addProperty(Class typeClass, String propertyName, String init){
        try{
            return addProperty(typeClass, propertyName, _expr.of(init));
        }catch(Exception e){
            throw new _jdraftException("Invalid initializer expression \""+init+"\"");
        }
    }

    public _class addProperty( String fieldDeclaration ){
        try{
            return addProperty(_field.of(fieldDeclaration));
        }catch(Exception e){
            throw new _jdraftException("Invalid fieldDeclaration \""+fieldDeclaration+"\"");
        }
    }

    public _class addProperty(_field _f){
        addField(_f.node());
        _method _g = _method.get(_f);
        _method _s = _method.set(_f);
        addMethods(_g, _s);
        return this;
    }

    /**
     * Adds a private property (a private field, and public get() and set() methods) & sets the fields init value
     * @param typeClass the type of the property
     * @param propertyName the name of the property
     * @param initExpr the initial value to set the field
     * @return the modified _class
     */
    public _class addProperty(Class typeClass, String propertyName, _expr initExpr){
        _field _f = _field.of(typeClass, propertyName);
        if( initExpr != null ){
            _f.setInit(initExpr);
        }
        _f.setPrivate();
        return addProperty( _f );
    }

    @Override
    public _class setFields(List<_field> fields) {
        this.node.getMembers().removeIf(m -> m instanceof FieldDeclaration );
        fields.forEach(f-> addField(f));
        return this;
    }

    /** Make sure to route this to the correct (default method)
     * @param classToImplement 
     * @return  
     */
    public _class addImplement(String classToImplement ){
        return addImplements( new String[]{classToImplement});
    }

    public _class addImplement(_interface _i) {
        addImports(_i);
        return addImplements(new _interface[]{_i});
    }

    public _class removeImplements( Class toRemove ){
        this.node.getImplementedTypes().removeIf(im -> im.getNameAsString().equals( toRemove.getSimpleName() ) ||
                im.getNameAsString().equals(toRemove.getCanonicalName()) );
        return this;
    }

    @Override
    public _class removeImplements( ClassOrInterfaceType toRemove ){
        this.node.getImplementedTypes().remove( toRemove );
        return this;
    }

    @Override
    public _class removeExtends( Class toRemove ){
        this.node.getExtendedTypes().removeIf(im -> im.getNameAsString().equals( toRemove.getSimpleName() ) ||
                im.getNameAsString().equals(toRemove.getCanonicalName()) );
        return this;
    }


    @Override
    public _class removeExtends( ClassOrInterfaceType toRemove ){
        this.node.getExtendedTypes().remove( toRemove );
        return this;
    }

    /** 
     * Make sure to route this to the correct (default method)
     * @param clazz the class to implement
     * @return the modified _class
     */
    public _class addImplement(Class clazz ){
        addImports(clazz);
        return addImplements( new Class[]{clazz} );
    }

    @Override
    public _class setJavadoc(String... content) {
        ((NodeWithJavadoc) this.node()).setJavadocComment(Text.combine(content));
        return this;
    }

    @Override
    public _class setJavadoc(JavadocComment astJavadocComment) {
        ((NodeWithJavadoc) this.node()).setJavadocComment(astJavadocComment);
        return this;
    }

    /**
     * i.e. 
     * <PRE>
     * _class _c = _class.of("C").extend(
     *    new Descriptive(){
     *       public String describe(){
     *           return "a description";
     *       }
     * });
     *
     * public class C extends Descriptive{
     *     public String describe(){
     *         return "a description";
     *     }
     * }
     * </PRE>
     * @param anonymousImplementationBody anonymous Object that implements the interface and the method(s)
     *                                    required that will be "imported" in the _class
     * @return the modified Class
     */
    public _class addExtend(Object anonymousImplementationBody ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];

        Class sup = anonymousImplementationBody.getClass().getSuperclass();
        addExtend(sup);
        addImports( new Class[]{sup} );
        ObjectCreationExpr oce = Expr.newExpr(ste);
        if( oce.getAnonymousClassBody().isPresent()){
            oce.getAnonymousClassBody().get().forEach( m->this.node().addMember(m) );
        }
        _import.inferImportsFrom(anonymousImplementationBody).forEach( i -> addImports(i) );
        return this;
    }

    /**
     * Given an abstract Object, add the members of the BODY into the
     * _class (after running any _macro ANNOTATIONS on
     * add the source content to the _class, an
     *
     * NOTE: If the Object is an interface, then implement the interface
     * If the Object is an AbstractClass then extends the abstract class
     * <PRE>
     *     _class _c = _class.of("C").field("int i;");
     *     _c.BODY( new Object(){
     *          @_remove int i; //_remove will removeIn this from _c
     *          int j;
     *          @_final public int add( ){
     *              return i + j;
     *          }
     *     });
     * //will produce the following _class:
     * public class C{
     *     int i;
     *     int j;
     *
     *     public final int add(){
     *         return i + j;
     *     }
     * }
     * </PRE>
     *
     * @param anonymousClassBody anonymous Class that defines FIELDS and METHODS that will be merged
     * into the _class
     * @return the modified _class
     */
    public _class addMembers(Object anonymousClassBody ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expr.newExpr(ste);

        //create a temp _class to add these to so I can run _macro ANNOTATIONS on them        
        _class _temp = _class.of("UNKNOWN");
        if( oce != null && oce.getAnonymousClassBody().isPresent() ){
            //add the anonymous class members to the temp class
            oce.getAnonymousClassBody().get().forEach(b-> _temp.node().addMember(b));
        }
        //run the macros on the temp class (we might removeIn some stuff, etc.)
        macro.to( anonymousClassBody.getClass(), _temp);

        //now add the finished members from temp to this _class
        _temp.node().getMembers().forEach(m -> {
            if( m instanceof ConstructorDeclaration ){
                ConstructorDeclaration cd = (ConstructorDeclaration)m;
                cd.setName(this.getName()); //if we add a member that is a constructor, update it's name
                this.node.addMember(cd);
            } else {
                this.node.addMember(m);
            }
        });
        
        //create the approrpriate imports based on the signature of the 
        // added fields and methods, throws, etc.
        _import.inferImportsFrom(anonymousClassBody).forEach( i -> addImports(i) );
        
        Class[] ints = anonymousClassBody.getClass().getInterfaces();
        Arrays.stream(ints).forEach( i -> addImplement(i) );
        
        if( anonymousClassBody.getClass().getSuperclass() != null && anonymousClassBody.getClass().getSuperclass() != Object.class ){
            this.addExtend( anonymousClassBody.getClass().getSuperclass() );
        }
        return this;
    }

    /**
     * the AST storing the state of the _class
     * the _class is simply a facade into the state astClass
     *
     * the _class facade is a "Logical View" of the _class state stored in the
     * AST and can interpret or manipulate the AST without:
     * having to deal with syntax issues
     */
    private ClassOrInterfaceDeclaration node;

    @Override
    public boolean isTopLevel(){
        return node().isTopLevelType();
    }
    
    @Override
    public CompilationUnit astCompilationUnit(){
        if( node().isTopLevelType()){
            return (CompilationUnit) node().getParentNode().get(); //astCompilationUnit.get();
        }
        //it might be a member class
        if( this.node.findCompilationUnit().isPresent()){
            return this.node.findCompilationUnit().get();
        }
        return null; //its an orphan
    }
    
    @Override
    public boolean hasExtends(){
        return !this.node.getExtendedTypes().isEmpty();
    }

    @Override
    public NodeList<ClassOrInterfaceType> listAstExtends(){
        return node.getExtendedTypes();
    }

    @Override
    public List<_typeRef> listExtends() {
        if( hasExtends()) {
            return Stream.of(getExtends()).collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    public _typeRef getExtends( ){
        if( hasExtends() ) {
            return _typeRef.of(getExtendsNode());
        }
        return null;
    }

    public _class setExtends(Class clazz){
        return setExtends(_typeRef.of(clazz));
    }

    public _class setExtends( _typeRef _tr){
        this.node.getExtendedTypes().clear();
        this.node.addExtendedType( (ClassOrInterfaceType)_tr.node());
        return this;
    }

    public ClassOrInterfaceType getExtendsNode(){
        List<ClassOrInterfaceType> exts = node.getExtendedTypes();
        if( exts.isEmpty() ){
            return null;
        }
        return exts.get( 0 );
    }

    @Override
    public _class addExtend(ClassOrInterfaceType toExtend ){
        this.node.getExtendedTypes().clear();
        this.node.addExtendedType( toExtend );
        return this;
    }

    @Override
    public _class addExtend(Class toExtend ){
        addImports(toExtend);
        return addExtend( (ClassOrInterfaceType) Types.of(toExtend.getCanonicalName() ) );
    }

    @Override
    public _class addExtend(String toExtend ){
        this.node.getExtendedTypes().clear();
        this.node.addExtendedType( toExtend );
        return this;
    }

    @Override
    public List<_method> listMethods() {
        List<_method> _ms = new ArrayList<>();
        node.getMethods().forEach(m-> _ms.add(_method.of( m ) ) );
        return _ms;
    }

    @Override
    public List<_method> listMethods(Predicate<_method> _methodMatchFn ){
        List<_method> _ms = new ArrayList<>();
        node.getMethods().forEach(m-> {
            _method _m = _method.of( m);
            if( _methodMatchFn.test(_m)){
                _ms.add(_m ); 
            }
        } );
        return _ms;
    }

    public _method getMethod( int index ){
        return _method.of(node.getMethods().get( index ));
    }

    @Override
    public _class addMethod(MethodDeclaration method ) {
        node.addMember( method );
        return this;
    }

    @Override
    public _class addField(VariableDeclarator field ) {
        if(! field.getParentNode().isPresent()){
            throw new _jdraftException("cannot add Var without parent FieldDeclaration");
        }
        FieldDeclaration fd = (FieldDeclaration)field.getParentNode().get();
        //we already added it to the parent
        if( this.node.getFields().contains( fd ) ){
            if( !fd.containsWithinRange( field ) ){
                fd.addVariable( field );
            }
            return this;
        }
        this.node.addMember( fd );
        return this;
    }
 
    @Override
    public List<_constructor> listConstructors() {
        List<_constructor> _cs = new ArrayList<>();
        node.getConstructors().forEach(c-> _cs.add( _constructor.of(c) ));
        return _cs;
    }

    @Override
    public _constructor getConstructor(int index){
        return _constructor.of(node.getConstructors().get( index ));
    }

    @Override
    public _class addConstructor(ConstructorDeclaration constructor ) {
        constructor.setName(this.getName()); //alwyas set the constructor NAME to be the classes NAME
        this.node.addMember( constructor );
        return this;
    }

    @Override
    public _annos getAnnos() {
        return _annos.of(this.node);
    }

    /*
    @Override
    */
    public boolean is( String...classDeclaration){
        Stencil st = Stencil.of(classDeclaration);
        if( st.isMatchAny() ){
            return true;
        }
        if( st.isFixedText() ) {
            try {
                _class _o = of(classDeclaration);
                return _o.equals(this);
            } catch (Exception e) {
                return false;
            }
        } else{
            //neither fixed text nor
            _class _o = of(classDeclaration);
            return _tree._node.leftDeepFeatureCompare(_o, this );
        }
    }

    @Override
    public boolean is( ClassOrInterfaceDeclaration astC ){
        try{
            _class _o = of( astC );
            return _o.equals( this );
        }catch(Exception e){
            return false;
        }
    }
    
    @Override
    public boolean isAbstract(){
        return this.node.isAbstract();
    }

    @Override
    public boolean isFinal(){
        return this.node.isFinal();
    }

    @Override
    public boolean isStatic() {
        return this.node.isStatic();
    }
    
    @Override
    public _class setFinal(boolean toSet){
        this.node.setFinal(toSet);
        return this;
    }

    @Override
    public String toString(){
        if( this.node.isTopLevelType()){
            return this.astCompilationUnit().toString();            
        }
        return this.node.toString();
    }   

    @Override
    public boolean equals( Object obj ) {
        if( this == obj ) {
            return true;
        }
        if( obj == null ) {
            return false;
        }
        if( getClass() != obj.getClass() ) {
            return false;
        }
        final _class other = (_class)obj;

        if( !Objects.equals( this.getPackage(), other.getPackage() ) ) {
            Log.trace("Expected package %s got: %s", this::getPackage, other::getPackage);
            return false;
        }
        if( !Objects.equals( this.getJavadoc(), other.getJavadoc() ) ) {
            Log.trace("Expected javadoc %s got: %s", this::getJavadoc, other::getJavadoc);
            return false;
        }
        if( ! Expr.equalAnnos(this.node, other.node)){
            Log.trace("Expected annos %s got: %s", this.node::getAnnotations, other.node::getAnnotations);
            return false;
        }
        if( !Objects.equals( this.getModifiers(), other.getModifiers() ) ) {
            Log.trace("Expected modifiers %s got: %s", this::getModifiers, other::getModifiers);
            return false;
        }
        if( !Objects.equals( this.getTypeParams(), other.getTypeParams() ) ) {
            Log.trace("Expected typeParameters %s got: %s", this::getTypeParams, other::getTypeParams);
            return false;
        }
        if( !Objects.equals( this.getName(), other.getName() ) ) {
            Log.trace("Expected name %s got: %s", this::getName, other::getName);
            return false;
        }
        if( !Types.equal(this.getExtendsNode(), other.getExtendsNode()) ){
            Log.trace("Expected extends %s got: %s", this::getExtendsNode, other::getExtendsNode);
            return false;
        }
        if( !_imports.Compare.importsEqual(this.node, other.node)){
            Log.trace("Expected imports %s got: %s", this::listImports, other::listImports);
            return false;
        }
        if( !Types.equal( this.listAstImplements(), other.listAstImplements())){
            Log.trace("Expected implements %s got: %s", this::listAstImplements, other::listAstImplements);
            return false;
        }
        Set<_initBlock> tsb = new HashSet<>();
        Set<_initBlock> osb = new HashSet<>();
        tsb.addAll(listInitBlocks());
        osb.addAll(other.listInitBlocks());
        if( !Objects.equals( tsb, osb)){
            Log.trace("Expected initBlocks %s got: %s", ()->tsb, ()->osb);
            return false;
        }
        Set<_field> tf = new HashSet<>();
        Set<_field> of = new HashSet<>();
        tf.addAll( this.listFields());
        of.addAll( other.listFields());

        if( !Objects.equals( tf, of ) ) {
            Log.trace("Expected fields %s got: %s", ()->tf, ()->of);
            return false;
        }

        Set<_method> tm = new HashSet<>();
        Set<_method> om = new HashSet<>();
        tm.addAll( this.listMethods());
        om.addAll( other.listMethods());

        if( !Objects.equals( tm, om ) ) {
            Log.trace("Expected methods %s got: %s", ()->tm, ()->om);
            return false;
        }

        Set<_constructor> tc = new HashSet<>();
        Set<_constructor> oc = new HashSet<>();
        tc.addAll( this.listConstructors());
        oc.addAll( other.listConstructors());

        if( !Objects.equals( tc, oc ) ) {
            Log.trace("Expected constructors %s got: %s", ()->tc, ()->oc);
            return false;
        }

        Set<_type> tn = new HashSet<>();
        Set<_type> on = new HashSet<>();
        tn.addAll( this.listInnerTypes());
        on.addAll( other.listInnerTypes());

        if( !Objects.equals( tn, on ) ) {
            Log.trace("Expected inner types %s got: %s", ()->tn, ()->on);
            return false;
        }

        tn.clear();
        on.clear();
        tn.addAll(this.listCompanionTypes());
        on.addAll(other.listCompanionTypes());
        if( !Objects.equals( tn, on ) ) {
            Log.trace("Expected companion types %s got: %s", ()->tn, ()->on);
            return false;
        }
        return true;
    }

    @Override
    public int hashCode(){
        int hash = 3;

        Set<_field> tf = new HashSet<>();
        tf.addAll( this.listFields());
        Set<_method> tm = new HashSet<>();
        tm.addAll( this.listMethods());
        Set<_constructor> tc = new HashSet<>();
        tc.addAll( this.listConstructors());
        Set<_type> tn = new HashSet<>();
        tn.addAll( this.listInnerTypes());

        //Set<_type> ct = new HashSet<>();

        Set<Integer> cths = new HashSet<>();
        /***** TODO THIS IS BROKEN AND COMPANION TYPES SUCK

        List<_type> cts = this.listCompanionTypes();
        if( cts.size() > 0 ){
            System.out.println( "FOUND "+cts.size()+" COMPANION TYPES IN "+ this.getFullName());
            for(int i=0;i<cts.size();i++){
                //OHHHHHH THIS SUCKS
                TypeDeclaration t = (TypeDeclaration)cts.get(i).ast().clone();
                t.remove();
                cths.add( _java.type(t).hashCode() );
            }
        }
        //ct.addAll( cts );
        */
        Set<_initBlock> sbs = new HashSet<>();
        sbs.addAll( this.listInitBlocks());

        hash = 47 * hash + Objects.hash( this.getPackage(), this.getName(),
                this.getJavadoc(), this.getAnnos(), this.getModifiers(),
                this.getTypeParams(), Types.hash(this.getExtendsNode()),
                sbs, Types.hash( node().getImplementedTypes() ),
                Expr.hashAnnos(node),
                tf, tm, tc, tn, cths);

        return hash;
    }    
}
