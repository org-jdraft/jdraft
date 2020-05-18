package org.jdraft;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.type.*;
import org.jdraft.io._in;
import org.jdraft.io._io;
import org.jdraft.io._ioException;
import org.jdraft.macro.macro;
import org.jdraft.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

/**
 * Representation of the source code of a Java interface.<BR>
 * (i.e. "public interface Marker {}")
 *
 * Implemented as a "Logical Facade" on top of an AST
 * ({@link ClassOrInterfaceDeclaration}) for logical manipulation.
 */
public final class _interface implements _type<ClassOrInterfaceDeclaration, _interface>,
        _method._withMethods<_interface>, _typeParams._withTypeParams<_interface>,
        _type._withExtends<_interface> {

    public static _interface of(){
        return of( new ClassOrInterfaceDeclaration() );
    }

    public static _interface of(URL url) {
        return of(Ast.JAVAPARSER, url);
    }

    public static _interface of(JavaParser javaParser, URL url){
        try {
            InputStream inStream = url.openStream();
            return of(javaParser, inStream);
        }catch(IOException ioe){
            throw new _ioException("invalid input url \""+url.toString()+"\"", ioe);
        }
    }

    public static _interface of( Path p) {
        return of(Ast.JAVAPARSER, p);
    }

    public static _interface of(JavaParser javaParser,  Path p){
        return of(javaParser, _io.inFile(p));
    }

    public static _interface of(InputStream is) {
        return of( Ast.JAVAPARSER, is);
    }

    public static _interface of(JavaParser javaParser, InputStream is) {
        return of( Ast.of(javaParser, is) );
    }

    public static _interface of( _in in ) {
        return of( Ast.JAVAPARSER, in);
    }

    public static _interface of( JavaParser javaParser, _in in ){
        return of( javaParser, in.getInputStream());
    }

    public static _interface of( Class clazz ) {
        return of(Ast.JAVAPARSER, clazz);
    }

    public static _interface of( JavaParser javaParser, Class clazz ){
        Node n = Ast.typeDecl( javaParser, clazz );
        if( n instanceof CompilationUnit ){
            return macro.to(clazz, of( (CompilationUnit)n));
        } 
        
        return macro.to(clazz, of((ClassOrInterfaceDeclaration)n));
    }

    /**
     * Build and return the interface based on the String definition passed in
     * this also handles "shortcut" declarations:
     * <PRE>
     *     _interface.of("I")
     *     // is:
     *     public interface I{}
     *
     *     _interface.of("aaaa.vvvv.I")
     *     // is:
     *     package aaaa.vvvv;
     *     public interface I{}
     *
     * </PRE>
     * @param interfaceDef String definition of the interface, (each element represents a line)
     * @return the _interface declaration
     */
    public static _interface of( String...interfaceDef ) {
        return of(Ast.JAVAPARSER, interfaceDef);
    }

    public static _interface of( JavaParser javaParser, String...interfaceDef ){
        if( interfaceDef.length == 0 ){
            return of();
        }
        //Handle shortcut interfaces (i.e. _interface.of("I"), _interface.of("aaaa.bbbb.I")
        if( interfaceDef.length == 1){
            String[] strs = interfaceDef[0].split(" ");
            if( strs.length == 1 ){
                //definately shortcut classes
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
                            "public interface "+shortcutClass));
                }
                if(!shortcutClass.endsWith("}")){
                    shortcutClass = shortcutClass + "{}";
                }
                return of( Ast.of(javaParser,"public interface "+shortcutClass));
            }
        }
        //check if the interfaceDef has a "private " before " class ", if so, remove it
        //because it'll fail, so remove the private, then add it back in manually
        String cc = Text.combine(interfaceDef);
        int classIndex = cc.indexOf(" interface ");
        int privateIndex = cc.indexOf( "private ");
        if( privateIndex >= 0 && privateIndex < classIndex ){
            cc = cc.substring(0, privateIndex)+ cc.substring(privateIndex + "private ".length());
            _interface _i = of( Ast.of( cc ));
            _i.setPrivate();
            return _i;
        }
        return of( Ast.of( javaParser, interfaceDef ));
    }

    public static _interface of( String interfaceCode1, String interfaceCode2) {
        return of( Ast.JAVAPARSER, new String[]{interfaceCode1, interfaceCode2});
    }

    /**
     *
     * @param signature
     * @param anonymousBody
     * @return
     */
    public static _interface of( String signature, Object anonymousBody) {
        return of( signature, anonymousBody, Thread.currentThread().getStackTrace()[2] );
    }

    /**
     *
     * @param signature
     * @param anonymousBody
     * @param ste
     * @return
     */
    public static _interface of( String signature, Object anonymousBody, StackTraceElement ste) {
        final _interface _i = of( signature );

        //interfaces to be extended
        if(anonymousBody.getClass().getInterfaces().length > 0){
            Arrays.stream(anonymousBody.getClass().getInterfaces()).forEach( i -> {
                _i.addImports(i);
                _i.addExtend(i);
            });
        }

        //need to process these ANNOTATIONS
        //@_abstract
        //@_default
        //@_static
        ObjectCreationExpr oce = Exprs.newExpr(ste);
        if( oce.getAnonymousClassBody().isPresent() ){
            oce.getAnonymousClassBody().get().forEach( e -> _i.astInterface.addMember(e) );
        }
        macro.to(anonymousBody.getClass(), _i);

        //look at the anonymous body (runtime class) which can infer all the imports
        Set<Class> importClasses = _import.inferImportsFrom(anonymousBody);
        _i.addImports(importClasses.toArray(new Class[0]));
        
        //actually, all methods that are NOT static or default need to have their
        //bodies removed with ; (since it's an interface)
        _i.forMethods(m-> !m.isDefault() && !m.isStatic(), 
                m -> m.ast().removeBody() );
        return _i;
    }

    public static _interface of( CompilationUnit cu ){
        NodeList<TypeDeclaration<?>> tds = cu.getTypes();
        if( tds.size() == 1 ){
            return of( (ClassOrInterfaceDeclaration)tds.get(0) );
        }
        if( cu.getTypes().size() > 1 ){
            //I want to class
            Optional<TypeDeclaration<?>> coid = cu.getTypes().stream().filter(
                    t-> t instanceof ClassOrInterfaceDeclaration && ((ClassOrInterfaceDeclaration) t).isInterface()).findFirst();
            if( coid.isPresent() ){
                return of( (ClassOrInterfaceDeclaration)coid.get());
            }
            //return of( cu.getType(0) );
        }
        if( cu.getPrimaryTypeName().isPresent() ){
            return of( cu.getInterfaceByName( cu.getPrimaryTypeName().get() ).get() );
        }
        throw new _jdraftException("Unable to locate primary TYPE in "+ cu);
    }

    public static _interface of( ClassOrInterfaceDeclaration astClass ){
        return new _interface( astClass );
    }



    public _interface( ClassOrInterfaceDeclaration astClass ){
        this.astInterface = astClass;
    }

    /**
     * the AST storing the state of the _class
     * the _class is simply a facade into the state astClass
     *
     * the _class facade is a "Logical View" of the _class state stored in the
     * AST and can interpret or manipulate the AST without:
     * having to deal with syntax issues
     */
    private final ClassOrInterfaceDeclaration astInterface;

    @Override
    public ClassOrInterfaceDeclaration ast(){
        return this.astInterface;
    }
    
    @Override
    public boolean isTopLevel(){
        return ast().isTopLevelType();
    }

    @Override
    public CompilationUnit astCompilationUnit(){
        if( this.ast().isTopLevelType()){
            return ast().findCompilationUnit().get();
        }
        //it might be a member class
        if( this.astInterface.findCompilationUnit().isPresent()){
            return this.astInterface.findCompilationUnit().get();
        }
        return null; //its an orphan
    }

    @Override
    public _interface setJavadoc(String... content) {
        ((NodeWithJavadoc) this.ast()).setJavadocComment(Text.combine(content));
        return this;
    }

    @Override
    public _interface setJavadoc(JavadocComment astJavadocComment) {
        ((NodeWithJavadoc) this.ast()).setJavadocComment(astJavadocComment);
        return this;
    }

    @Override
    public boolean hasExtends(){
        return !this.astInterface.getExtendedTypes().isEmpty();
    }

    @Override
    public NodeList<ClassOrInterfaceType> listExtends(){
        return astInterface.getExtendedTypes();
    }


    @Override
    public _interface removeExtends( Class toRemove ){
        this.astInterface.getExtendedTypes().removeIf( im -> im.getNameAsString().equals( toRemove.getSimpleName() ) ||
                im.getNameAsString().equals(toRemove.getCanonicalName()) );
        return this;
    }

    @Override
    public _interface removeExtends( ClassOrInterfaceType toRemove ){
        this.astInterface.getExtendedTypes().remove( toRemove );
        return this;
    }

    @Override
    public _interface addExtend(ClassOrInterfaceType toExtend ){
        this.astInterface.addExtendedType( toExtend );
        return this;
    }

    public _interface addExtend(Class...toExtends ){
        Arrays.stream(toExtends).forEach( e -> addExtend( (ClassOrInterfaceType) Types.of(e) ) );
        return this;
    }
    
    @Override
    public _interface addExtend(Class toExtend ){
        this.astInterface.addExtendedType( (ClassOrInterfaceType) Types.of(toExtend) );
        this.astInterface.tryAddImportToParentCompilationUnit(toExtend);
        return this;
    }

    @Override
    public _interface addExtend(String toExtend ){
        this.astInterface.addExtendedType( toExtend);
        return this;
    }


    @Override
    public List<_method> listMethods() {
        List<_method> _ms = new ArrayList<>();
        astInterface.getMethods().forEach( m-> _ms.add(_method.of( m ) ) );
        return _ms;
    }
    
    @Override
    public List<_method> listMethods(Predicate<_method> _methodMatchFn ){
        List<_method> _ms = new ArrayList<>();
        astInterface.getMethods().forEach( m-> {
            _method _m = _method.of( m);
            if( _methodMatchFn.test(_m)){
                _ms.add(_m ); 
            }
        } );
        return _ms;
    }

    @Override
    public _interface addMethod(MethodDeclaration method ) {
        astInterface.addMember( method );
        return this;
    }

    @Override
    public _interface addField(VariableDeclarator field ) {
        if(! field.getParentNode().isPresent()){
            throw new _jdraftException("cannot add Var without parent FieldDeclaration");
        }
        FieldDeclaration fd = (FieldDeclaration)field.getParentNode().get();
        //we already added it to the parent
        if( this.astInterface.getFields().contains( fd ) ){
            if( !fd.containsWithinRange( field ) ){
                fd.addVariable( field );
            }
            return this;
        }
        this.astInterface.addMember( fd );
        return this;
    }

    @Override
    public _annoExprs getAnnoExprs() {
        return _annoExprs.of(this.astInterface);
    }
    
    @Override
    public String toString(){
        if( this.ast().isTopLevelType() ){
            return this.astCompilationUnit().toString();            
        }        
        return this.astInterface.toString();        
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
        final _interface other = (_interface)obj;

        if( this.astInterface == other.astInterface ){
            return true; //two _interfaces pointing to the same InterfaceDeclaration
        }
        if( !Objects.equals( this.getPackage(), other.getPackage())){
            return false;
        }
        if( ! Exprs.equalAnnos(this.astInterface, other.astInterface)){
            return false;
        }

        if( this.hasJavadoc() != other.hasJavadoc() ){
            return false;
        }
        if( this.hasJavadoc() && !Objects.equals( this.getJavadoc().getText().trim(), other.getJavadoc().getText().trim())){
            return false;
        }

        if( !Objects.equals( this.getModifiers(), other.getModifiers())){
            return false;
        }
        if( !Objects.equals( this.getTypeParams(), other.getTypeParams())){
            return false;
        }

        Set<_method> tm = new HashSet<>();
        Set<_method> om = new HashSet<>();
        tm.addAll(  this.listMethods());
        om.addAll(  other.listMethods());

        if( !Objects.equals( tm, om)){
            return false;
        }

        Set<_field> tf = new HashSet<>();
        Set<_field> of = new HashSet<>();
        tf.addAll(  this.listFields());
        of.addAll(  other.listFields());

        if( !Objects.equals( tf, of)){
            return false;
        }
        if( !Types.equal( astInterface.getExtendedTypes(), other.astInterface.getExtendedTypes() )){
            return false;
        }

        if( !_imports.Compare.importsEqual( astInterface, other.astInterface)){
            return false;
        }
        Set<_type> tn = new HashSet<>();
        Set<_type> on = new HashSet<>();
        tn.addAll( this.listInnerTypes() );
        on.addAll( other.listInnerTypes() );
        if( !Objects.equals( tn, on)){
            return false;
        }

        Set<_type> ctn = new HashSet<>();
        Set<_type> con = new HashSet<>();
        ctn.addAll( this.listCompanionTypes() );
        con.addAll( other.listCompanionTypes() );
        if( !Objects.equals( ctn, con)){
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        //we want to have consistent order for METHODS,fileds, extends, imports
        //and NESTS, for the hash code so we put the
        Set<_method> tm = new HashSet<>();
        tm.addAll(  this.listMethods());

        Set<_field> tf = new HashSet<>();
        tf.addAll( this.listFields());

        Set<Integer> te = new HashSet<>();
        this.listExtends().forEach(e-> te.add( Types.hash(e)));

        Set<_type> inners = new HashSet<>();
        inners.addAll(  this.listInnerTypes() );

        Set<_type> companionTypes = new HashSet<>();
        //companionTypes.addAll(listCompanionTypes());

        hash = 53 * Objects.hash( this.getPackage(), 
            Exprs.hashAnnos(astInterface),
            this.getJavadoc(),this.getModifiers(), this.getTypeParams(),
            tm, tf,
            _imports.Compare.importsHash(astInterface),
            Types.hash(astInterface.getExtendedTypes()),
            inners,
            companionTypes);

        return hash;
    }

    public Map<_java.Feature, Object> features( ) {
        Map<_java.Feature, Object> parts = new HashMap<>();
        parts.put( _java.Feature.HEADER_COMMENT, this.getHeaderComment() );
        parts.put( _java.Feature.PACKAGE, this.getPackage() );
        parts.put( _java.Feature.IMPORTS, this.getImports().list() );
        parts.put( _java.Feature.ANNO_EXPRS, this.listAnnoExprs() );
        parts.put( _java.Feature.JAVADOC, this.getJavadoc() );
        parts.put( _java.Feature.EXTENDS_TYPES, this.listExtends() );
        parts.put( _java.Feature.NAME, this.getName() );

        parts.put( _java.Feature.MODIFIERS, this.getModifiers() );
        parts.put( _java.Feature.TYPE_PARAMS, this.getTypeParams() );
        parts.put( _java.Feature.FIELDS, this.listFields() );
        parts.put( _java.Feature.METHODS, this.listMethods() );
        parts.put( _java.Feature.INNER_TYPES, this.listInnerTypes() );
        parts.put( _java.Feature.COMPANION_TYPES, this.listCompanionTypes() );
        return parts;
    }

    @Override
    public boolean is( String...interfaceDeclaration ){
        try{
            return of(interfaceDeclaration).equals(this);
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean is( ClassOrInterfaceDeclaration astI){
        try{
            return of(astI).equals(this);
        }catch(Exception e){
            return false;
        }
    }    
}
