package org.jdraft;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.*;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.utils.Log;
import org.jdraft.io._in;
import org.jdraft.io._io;
import org.jdraft.io._ioException;
import org.jdraft.macro.macro;
import org.jdraft.text.Stencil;
import org.jdraft.walk.Walk;

/**
 * The Definition of a Java type (one of : class, enum, interface, @interface)
 * 
 * <PRE>
 * <H2>Traditional Programming Workflow</H2>:
 * 1) write code as text within an editor to create .java files
 * 2) call the javac compiler to convert the .java files to an AST then into
 * Java .class files (ByteCode)
 * 3) run the java command/program to create a Java VM to run the bytecode
 * 
 * you
 *   |----------|----------|----------|
 * .java     [javac]     .class     [JVM] 
 * </PRE>
 * 
 * <H2>Draft Programming Workflow</H2>:
 * 1) (At runtime), build draft objects (which ARE ASTs) /instead of .java files/
 * 2) (At runtime) call the javac compiler to convert the draft objects (which ARE ASTs) into
 * Java .class files (ByteCode)
 * 3) (At runtime) load the .class byteCodes into the current runtime to use the classes
 * <PRE> 
 * you__________
 *   |          |
 *   |    ----jdraft---------------
 *   |   /      |       \          \
 *   |  /------AST       \          \
 *   | /        |         \          \
 *   |----------|----------|----------|
 * .java     [javac]     .class     [JVM]
 * </PRE>
 *
 * <PRE> {@code
 * //Examples:
 * //draft & compile return the (unloaded) byteCode for a drafted class
 * List<_classFile> _cfs = _adhoc.compile(_class.of("C").fields("String firstName, lastName;"));
 *
 * //draft, compile & load a new {@link Class} into a new {@link ClassLoader}:
 * _runtime runtime = _runtime.of(_class.of("C").fields("String firstName, lastName;")).getClass("C");
 *
 * //draft / compile/ load a new {@link Class} into a new {@link ClassLoader}
 * //& create a new instance:
 * Object o = _runtime.of( _class.of("C").fields("String firstName, lastName;")).instance("C");
 * 
 * //draft / compile & load a new Class then create a new proxied instance and 
 * // call the getNum instance method
 * int num = (int)
 *    _runtime.of( _class.of("C", new Object(){ public static int getNum(){return 123;} }))
 *        .proxy("C").call("getNum");
 * }</PRE>
 * @author Eric
 * @param <AST> the Ast {@link TypeDeclaration} ({@link ClassOrInterfaceDeclaration},
 * {@link AnnotationDeclaration}, {@link EnumDeclaration}) that stores the state
 * and maintains the Bi-Directional AST Tree implementation
 * @param <_T> the _type entity that provides logical access to manipulating the
 * AST & NodeWithAnnotations
 */
public interface _type<AST extends TypeDeclaration, _T extends _type>
    extends _javadocComment._withJavadoc<_T>, _annos._withAnnos<_T>, _modifiers._withModifiers<_T>,
        _field._withFields<_T>, _java._declared<AST, _T>, _codeUnit<_T>, _tree._node<AST, _T> {

    /**
     * Read the java source code from a url and return the _type(_class, _enum, _interface, _annotation)
     * @param url
     * @param <_T>
     * @return
     */
    static <_T extends _type> _T of(URL url) {
        return of(Ast.JAVAPARSER, url);
    }

    /**
     * Read in a .java file from the url and return the _type(_class, _enum, _interface, _annotation)
     * @param javaParser
     * @param url
     * @param <_T>
     * @return
     */
    static <_T extends _type> _T of(JavaParser javaParser, URL url) {
        try {
            InputStream inStream = url.openStream();
            return of(javaParser, inStream);
        }catch(IOException ioe){
            throw new _ioException("invalid input url \""+url.toString()+"\"", ioe);
        }
    }

    /**
     * Read in a .java file from the InputStream and return the _type(_class, _enum, _interface, _annotation)
     * @param is
     * @return
     */
    static <_T extends _type> _T of(InputStream is) {
        return of( Ast.JAVAPARSER, is);
    }

    /**
     * Read in a .java file from the InputStream and return the _type(_class, _enum, _interface, _annotation)
     * @param javaParser
     * @param is
     * @param <_T>
     * @return
     */
    static <_T extends _type> _T of(JavaParser javaParser, InputStream is) {
        _codeUnit _c = _codeUnit.of(javaParser, is);
        if (_c instanceof _type) {
            return (_T) _c;
        }
        throw new _jdraftException("_code in inputStream " + is + " does not represent a _type");
    }

    /**
     * Read in a .java file from the Path and return the _type(_class, _enum, _interface, _annotation)
     * @param path
     * @return
     */
    static <_T extends _type> _T of(Path path) {
        return of( Ast.JAVAPARSER, path);
    }

    /**
     * Read in a .java file from the Path and return the _type(_class, _enum, _interface, _annotation)
     * @param javaParser the parser
     * @param path path to the file
     * @return the _type
     */
    static <_T extends _type> _T of(JavaParser javaParser, Path path) {
        _codeUnit _c = _codeUnit.of(javaParser, path);
        if (_c instanceof _type) {
            return (_T) _c;
        }
        throw new _jdraftException("_code at " + path + " does not represent a _type");
    }

    /**
     * build and return a new _type based on the code provided
     *
     * @param code the code for the _type
     * @return the _type
     * {@link _class} {@link _enum} {@link _interface}, {@link _annotation}
     */
    static <_T extends _type> _T of(String... code) {
        return of( Ast.JAVAPARSER, code);
    }

    /**
     * build and return a new _type based on the code provided
     *
     * @param javaParser the parser for the code
     * @param code the code for the _type
     * @return the _type
     * {@link _class} {@link _enum} {@link _interface}, {@link _annotation}
     */
    static <_T extends _type> _T of(JavaParser javaParser, String... code) {
        return of(Ast.typeDeclaration(javaParser, code));
    }

    /**
     * Build and return the appropriate _type based on the CompilationUnit
     * (whichever the primary TYPE is)
     *
     * @param astRoot the root AST node containing the top level TYPE
     * @return the _model _type
     */
    static <_T extends _type> _T of(CompilationUnit astRoot) {
        //if only 1 type, it's the top type
        if (astRoot.getTypes().size() == 1) {
            return of(astRoot, astRoot.getType(0));
        }
        //if multiple types find the first public type
        Optional<TypeDeclaration<?>> otd
                = astRoot.getTypes().stream().filter(t -> t.isPublic()).findFirst();
        if (otd.isPresent()) {
            return of(astRoot, otd.get());
        }
        //if there is marked a primary type (via storage) then it's that
        if (astRoot.getPrimaryType().isPresent()) {
            return of(astRoot, astRoot.getPrimaryType().get());
        }
        if (astRoot.getTypes().isEmpty()) {
            throw new _jdraftException("cannot create _type from CompilationUnit with no TypeDeclaration");
        }
        //if we have the storage (and potentially multiple package private types)
        //check the storage to determine if one of them is the right one
        if (astRoot.getStorage().isPresent()) {
            CompilationUnit.Storage st = astRoot.getStorage().get();
            Path p = st.getPath();

            //the storage says it was saved before
            Optional<TypeDeclaration<?>> ott
                    = astRoot.getTypes().stream().filter(t -> p.endsWith(t.getNameAsString() + ".java")).findFirst();
            if (ott.isPresent()) {
                return of(astRoot, ott.get());
            }

            if (p.endsWith("package-info.java")) {
                throw new _jdraftException("cannot create a _type out of a package-info.java");
            }
            if (p.endsWith("module-info.java")) {
                throw new _jdraftException("cannot create a _type out of a module-info.java");
            }

            //ok, well, this is dangerous, but shouldnt be a common occurrence
            // basically we have a compilationUnit with > 1 TypeDeclaration, but
            // none of the TypeDeclarations are public, and a PrimaryType is not
            //defined in the storage, so we just choose the first typeDeclaration
            return of(astRoot, astRoot.getType(0));
        }
        return of(astRoot, astRoot.getType(0));
    }

    /**
     * Return the appropriate _type given the AST TypeDeclaration (also, insure
     * that if it is a Top Level _type,
     *
     * @param td
     * @return
     */
    static <_T extends _type> _T of(TypeDeclaration td) {

        /*** MED
        if (td.isTopLevelType()) {
            return type(td.findCompilationUnit().get(), td);
        }
        */
        if (td instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration) td;
            if (coid.isInterface()) {
                return (_T)_interface.of(coid);
            }
            return (_T)_class.of(coid);
        }
        if (td instanceof EnumDeclaration) {
            return (_T)_enum.of((EnumDeclaration) td);
        }
        return (_T)_annotation.of((AnnotationDeclaration) td);
    }

    /**
     * Builds the appropriate _model _type ({@link _class}, {@link _enum},
     * {@link _interface}, {@link _annotation})
     *
     * @param astRoot the compilationUnit
     * @param td the primary TYPE declaration within the CompilationUnit
     * @return the appropriate _model _type (_class, _enum, _interface,
     * _annotation)
     */
    static <_T extends _type> _T of(CompilationUnit astRoot, TypeDeclaration td) {
        if (td instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration) td;
            if (coid.isInterface()) {
                return (_T)_interface.of(coid);
            }
            return (_T)_class.of(coid);
        }
        if (td instanceof EnumDeclaration) {
            return (_T)_enum.of((EnumDeclaration) td);
        }
        return (_T)_annotation.of((AnnotationDeclaration) td);
    }

    /**
     * given a Class, return the draft model of the source
     * @param clazz
     * @param resolver
     * @return
     */
    static <_T extends _type> _T of(Class clazz, _in._resolver resolver) {
        return of( Ast.JAVAPARSER, clazz, resolver);
    }

    /**
     * given a Class, return the draft model of the source
     * @param clazz
     * @param resolver
     * @return
     */
    static <_T extends _type> _T of(JavaParser jp, Class clazz, _in._resolver resolver) {
        Node n = Ast.typeDeclaration( jp, clazz, resolver );
        TypeDeclaration td = null;
        if( n instanceof CompilationUnit) { //top level TYPE
            CompilationUnit cu = (CompilationUnit) n;
            if (cu.getTypes().size() == 1) {
                td = cu.getType(0);
            } else {
                td = cu.getPrimaryType().get();
            }
        }else {
            td = (TypeDeclaration) n;
        }
        if( td instanceof ClassOrInterfaceDeclaration ){
            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration)td;
            if( coid.isInterface() ){
                return (_T) macro.to(clazz, _interface.of(coid));
            }
            return (_T)macro.to(clazz,  _class.of(coid) );
        }else if( td instanceof EnumDeclaration){
            return (_T)macro.to(clazz, _enum.of( (EnumDeclaration)td));
        }
        return (_T)macro.to(clazz, _annotation.of( (AnnotationDeclaration)td));
    }

    /**
     * Locate the source code for the runtime class (clazz)
     *
     * @see _io#IN_DEFAULT the configuration for where to look for the source code of the class
     * @param clazz the class
     * @return
     */
    static <_T extends _type> _T of(Class clazz) {
        return of(Ast.JAVAPARSER, clazz);
    }

    /**
     * Locate the source code for the runtime class (clazz)
     *
     * @see _io#IN_DEFAULT the configuration for where to look for the source code of the class
     * @param clazz the class
     * @param clazz the class
     * @return
     */
    static <_T extends _type> _T of(JavaParser javaParser, Class clazz) {
        return of(javaParser, clazz, _io.IN_DEFAULT);
    }

    default _T setCompanionTypes(List<_type> _cts){
        List<_type> octs = listCompanionTypes();
        octs.forEach(ct-> removeCompanionType(ct));

        _cts.forEach(_c -> addCompanionTypes(_c));
        return (_T)this;
    }

    default _T setInnerTypes(List<_type> _its){
        List<_type> oits = listInnerTypes();
        oits.forEach(ct-> removeCompanionType(ct));

        _its.forEach(_c -> addInnerType(_c));
        return (_T)this;
    }

    /**
     * If we are a top level _type add the types as companion types
     * (other top level types that are package private) to the CompilationUnit
     * 
     * @param _ts
     * @return 
     */
    default _T addCompanionTypes(_type..._ts ){
        if( this.isTopLevel() ){            
            for(int i=0;i<_ts.length; i++){
                TypeDeclaration td = (TypeDeclaration)(_ts[i].setPackagePrivate()).node();
                if( td.getParentNode().isPresent() ){
                    td.getParentNode().get().remove(td);
                }
                CompilationUnit cu = node().findCompilationUnit().get();
                cu.getTypes().add(td);
            }
            return (_T)this;
        }   
        throw new _jdraftException
            ("cannot add companion Types to a Nested Type \""+this.getName()+"\"");
    }
    
    /**
     * If we are a top level _type add the types as companion types
     * (other top level types that are package private) to the CompilationUnit
     * 
     * @param astTs
     * @return 
     */
    default _T addCompanionTypes(TypeDeclaration...astTs ){
        if( this.isTopLevel() ){            
            for(int i=0;i<astTs.length; i++){
                //manually set it to package private
                astTs[i].setPrivate(false);
                astTs[i].setPublic(false);
                astTs[i].setProtected(false);
                this.astCompilationUnit().addType(astTs[i]);
            }
            return (_T)this;
        }   
        throw new _jdraftException
            ("cannot add companion Types to a Nested Type \""+this.getName()+"\"");
    }

    /**
     * Applies transforms to "companion types" declared in this compilationUnit
     * "companion types" are top level types that are "package private" (i.e. they
     * are neither public, private, or protected)
     * 
     * {@link _type}s (NOTE: this COULD include the primary type if the 
     * "primary type" is also package private)
     * 
     * @param _typeActionFn
     * @return 
     */
    default _T toCompanionTypes(Consumer<_type> _typeActionFn ){
        listCompanionTypes().forEach(_typeActionFn);
        return (_T)this;
    }
    
    /**
     * Applies transforms to "companion types" declared in this compilationUnit
     * "companion types" are top level types that are "package private" (i.e. they
     * are neither public, private, or protected)
     * (i.e. {@link _class}.class {@link _interface}.class {@link _enum}.class, {@link _annotation}.class)
     * with the _typeActionFn
     * 
     * @param <CT>
     * @param packagePrivateType
     * @param _typeActionFn
     * @return the modified T
     */
    default <CT extends _type> _T toCompanionTypes(
        Class<CT> packagePrivateType, Consumer<CT> _typeActionFn ){
        
        _type.this.listCompanionTypes(packagePrivateType).forEach(_typeActionFn);
        return (_T)this;
    }
    
    /**
     * Apply a transform to all "companion types"
     * 
     * "companion types" are top level types that are "package private" (i.e. they
     * are neither public, private, or protected)
     * @param <_CT>
     * @param packagePrivateType
     * @param _typeMatchFn
     * @param _typeActionFn
     * @return the modified T
     */
    default <_CT extends _type> _T toCompanionTypes(
            Class<_CT> packagePrivateType, Predicate<_CT>_typeMatchFn, Consumer<_CT> _typeActionFn ){
        
        _type.this.listCompanionTypes(packagePrivateType, _typeMatchFn).forEach(_typeActionFn);
        return (_T)this;
    }
    
    /**
     * List all top level "companion types" that match the _typeMatchFn
     * 
     * "companion types" are top level types that are "package private" (i.e. they
     * are neither public, private, or protected)
     * 
     * @param _typeMatchFn
     * @return 
     */
    default List<_type> listCompanionTypes(Predicate<_type> _typeMatchFn){
        List<_type> found = new ArrayList<>();
        listCompanionTypes().stream()
                .filter(t-> _typeMatchFn.test(t) )
                .forEach(t-> found.add(t) );
        return found;
    }
    
    /**
     * List all top level "companion types" within this compilationUnit
     * "companion types" are top level types that are "package private" (i.e. they
     * are neither public, private, or protected)
     * (assuming this is a top level type)
     * 
     * @param <_CT>
     * @param _typeClass
     * @return 
     */
    default <_CT extends _type> List<_CT> listCompanionTypes(Class<_CT> _typeClass ){
        List<_CT> found = new ArrayList<>();
        listCompanionTypes().stream()
                .filter(t-> t.getClass().equals(_typeClass) )
                .forEach(t-> found.add((_CT)t ) );
        return found;
    }
    
    /**
     * List all top level "companion types" within this compilationUnit that
     * are of the _typeClass( i.e. {@link _class}, {@link _enum}, ...) and match
     * the _typeMatchFn.
     * 
     * "companion types" are top level types that are "package private" (i.e. they
     * are neither public, private, or protected)
     * (assuming this is a top level type)
     * @param <_CT>
     * @param _typeClass
     * @param _typeMatchFn
     * @return 
     */
    default <_CT extends _type> List<_CT> listCompanionTypes(Class<_CT> _typeClass, Predicate<_CT> _typeMatchFn){
        List<_CT> found = new ArrayList<>();
        _type.this.listCompanionTypes(_typeClass).stream()
                .filter(t-> _typeMatchFn.test(t) )
                .forEach(t-> found.add((_CT)t ) );
        return found;
    }
    
    /**
     * List all top level "companion types" within this compilationUnit
     * 
     * "companion types" are top level types that are "package private" 
     * (i.e. they are neither public, private, or protected)
     * 
     * NOTE: according to the JLS there can be ONLY ONE public top level type (or none)
     * but each "compilationUnit" can have 
     * @return 
     */
    default List<_type> listCompanionTypes(){
        if( isTopLevel() ){
            String name = this.getName();
            List<_type> _ts = new ArrayList<>();
            List<TypeDeclaration<?>> tds = 
                this.astCompilationUnit().getTypes();
            tds.stream().filter(t-> !t.isPublic()).forEach(t ->{
                if( !t.getNameAsString().equals(name) ) {
                    _ts.add(of(t));
                }
            } );
            return _ts;
        }
        return Collections.EMPTY_LIST;
    }
    
    /**
     * Attempts to find a "companion type" by name and return it
     * 
     * "companion types" are top level types that are "package private" 
     * (i.e. they are neither public, private, or protected)
     * 
     * @param name the name of the package private type to get
     * @return the package private type
     */
    default _type getCompanionType( String name ){
        List<_type> ts = _type.this.listCompanionTypes( t-> t.getName().equals(name) ); 
        if( ts.isEmpty() ){
            return null;            
        }
        return ts.get(0); //just return the first one        
    }
    
    /**
     * Attempts to find a "companion type" by name and type and return it
     * 
     * "companion types" are top level types that are "package private" 
     * (i.e. they are neither public, private, or protected)
     * 
     * 
     * @param <_CT> the package private type {@link _class}{@link _enum}
     * {@link _interface}, {@link _annotation}
     * @param typeClass
     * 
     * @param name the name of the package private type to get
     * @return the package private type
     */
    default  <_CT extends _type> _CT getCompanionType(Class<_CT>typeClass, String name ){
        List<_CT> ts = listCompanionTypes( typeClass, t-> t.getName().equals(name) );
        if( ts.isEmpty() ){
            return null;            
        }
        return ts.get(0); //just return the first one        
    }

    /**
     * Looks for all "companion types" that match the _typeMatchFn and removes them
     * "companion types" are top level types that are "package private" (i.e. they
     * are neither public, private, or protected)
     *
     * @param _typeMatchFn
     * @return the modified T
     */
    default _T removeCompanionTypes(Predicate<_type> _typeMatchFn){
        listCompanionTypes(_typeMatchFn)
                .forEach( t -> astCompilationUnit().remove( t.node() ) );
        return (_T)this;
    }

    /**
     * Removes the companionType given the type
     * @param _t
     * @return
     */
    default _T removeCompanionType(_type _t){
        listCompanionTypes(t -> t.equals(_t))
                .forEach( t -> astCompilationUnit().remove( t.node() ) );
        return (_T)this;
    }

    /**
     * Looks for all "companion types" that match the _typeMatchFn and removes them
     * "companion types" are top level types that are "package private" (i.e. they
     * are neither public, private, or protected)
     *
     * @param name the name of the companion type to remove
     * @return the modified T
     */
    default _T removeCompanionType(String name ){
        return removeCompanionTypes( t-> t.getName().equals(name) );
    }

    /**
     * Gets the Primary Type according to the CompilationUnit
     * the "primary type" is the type associated with the file name)
     * 
     * NOTE: it is not always easy to define there does NOT have to be a primary 
     * type for a compilationUnit.  
     * 
     * If we can find the compilationUnit, the compilationUnit Should be
     * associated with a fileName (in the Storage field).  the primary type
     * does NOT have to be public, but must be using the same name as the 
     * fileName
     * 
     * @return the primary type (if it can be resolved) or null
     */
    default _type getPrimaryType(){
        if( this.isTopLevel() ){
            if (this.astCompilationUnit().getPrimaryType().isPresent()) {
                return of( this.astCompilationUnit().getPrimaryType().get() );
            }            
            Optional<TypeDeclaration<?>> ot = 
                this.astCompilationUnit().getTypes().stream().filter(t -> t.isPublic() ).findFirst();
            if( ot.isPresent() ){
                return of( (TypeDeclaration)ot.get());
            }
        }        
        return null;
    }

    /**
     * Adds {@link _initBlock}s ({@link _field}s, {@link _method}s, {@link _constructor}s,{@link _constant}s,
     * {@link _annoMember}s), and inner {@link _type}s, {@link _enum}s, {@link _class}es, {@link _interface}s,
     * {@link _annotation}s) to the BODY of the {@link _type} and return the modified {@link _type}
     * @param members the members to be added
     * @return the modified _type
     */
    default _T add(_java._member... members){
        Arrays.stream(members).forEach(_m -> {
            if(_m instanceof _field){
                this.node().addMember( ((_field)_m).getFieldDeclaration() );
            } else{
                this.node().addMember( (BodyDeclaration)_m.node() );
            }
        }  );
        return (_T)this;
    }

    /**
     *
     * @param _memberClass
     * @param members
     * @return
     */
    default _T addBeforeFirst(Class<? extends _java._member> _memberClass, _java._member...members){
        return addBeforeFirst( m-> _memberClass.isAssignableFrom(m.getClass()), members );
    }

    /**
     * Adds all of the members to the AST
     * @param _matchFn
     * @param members
     * @return
     */
    default _T addBeforeFirst(Predicate<_java._member> _matchFn, _java._member...members){
        List<_java._member> _m = listMembers(_matchFn);
        if( _m.isEmpty() ){
            for(int i=0;i<members.length; i++){
                if( members[i] instanceof _field ){
                    node().getMembers().add( ((_field)members[i]).getFieldDeclaration());
                } else {
                    node().getMembers().add(members[i].node());
                }
            }
        }
        else{
            _java._member _found = _m.get(0);

            BodyDeclaration before = null;
            if( _found instanceof _field ){
                before = ((_field) _found).getFieldDeclaration();
            } else{
                before = (BodyDeclaration)_found.node();
            }

            for(int i=0;i<members.length; i++) {

                if( members[i] instanceof _field){
                    node().getMembers().addBefore( ((_field)members[i]).getFieldDeclaration(), before);
                } else{
                    node().getMembers().addBefore( (BodyDeclaration)members[i].node(), before);
                }
            }
        }
        return (_T)this;
    }

    /**
     *
     * @param _memberClass
     * @param _ms
     * @return
     */
    default _T addAfterLast(Class<? extends _java._member> _memberClass, _java._member... _ms){
        return addAfterLast( m-> _memberClass.isAssignableFrom(m.getClass()), _ms );
    }

    /**
     *
     * @param _matchFn
     * @param members
     * @return
     */
    default _T addAfterLast(Predicate<_java._member> _matchFn, _java._member...members){
        List<_java._member> _m = listMembers(_matchFn);
        if( _m.isEmpty() ){
            for(int i=0;i<members.length; i++){
                if( members[i] instanceof _field ){
                    node().getMembers().add( ((_field)members[i]).getFieldDeclaration());
                } else {
                    node().getMembers().add(members[i].node());
                }
            }
        }
        else{
            _java._member _found = _m.get(_m.size() -1);

            BodyDeclaration after = null;
            if( _found instanceof _field ){
                after = ((_field) _found).getFieldDeclaration();
            } else{
                after = (BodyDeclaration)_found.node();
            }

            Node nextNode = null;
            for(int i=0;i<members.length; i++) {

                if( members[i] instanceof _field){
                    nextNode = ((_field)members[i]).getFieldDeclaration();
                    node().getMembers().addAfter( nextNode, after);
                } else{
                    nextNode = (BodyDeclaration)members[i].node();
                    node().getMembers().addAfter( nextNode, after);
                }
                after = (BodyDeclaration)nextNode;
            }
        }
        return (_T)this;
    }

    /**
     * Set all of the members of this type
     * @param members all of the members (in the order they appear)
     * @return
     */
    default _T setMembers(List<_java._member> members){
        this.node().getMembers().clear();
        members.forEach(m -> this.node().addMember( (BodyDeclaration)m.node()) );
        return (_T)this;
    }

    /**
     *
     * @param _matchFn
     * @return
     */
    default List<_java._member> listMembers(Predicate<_java._member> _matchFn){
        List<_java._member> _ms = new ArrayList<>();
        NodeList<BodyDeclaration<?>> bds = node().getMembers();

        bds.forEach(b -> {
            if( b instanceof FieldDeclaration ){
                FieldDeclaration fd = (FieldDeclaration)b;
                fd.getVariables().forEach( v-> {
                    _field _f = _field.of(v);
                    if( _matchFn.test(_f)) {
                        _ms.add(_f);
                    }
                });
            } else {
                _java._member _mbp = (_java._member) _java.of(b);
                if( _matchFn.test(_mbp)) {
                    _ms.add(_mbp);
                }
            }
        } );
        return _ms;
    }

    /**
     * List the {@link _java._member}s: ({@link _initBlock}s, {@link _field}s, {@link _method}s, {@link _constructor}s,
     * {@link _constant}s, {@link _annoMember}s) , and inner{@link _type}s, {@link _enum}s,
     * {@link _class}es, {@link _interface}s, {@link _annotation}s) on the _type
     * @return a List of {@link _java._member}s on the {@link _type}
     */
    default List<_java._member> listMembers(){
        List<_java._member> _ms = new ArrayList<>();
        NodeList<BodyDeclaration<?>> bds = new NodeList<>();

        if( node() instanceof EnumDeclaration ){
            EnumDeclaration ed = (EnumDeclaration) node();
            bds.addAll( ed.getEntries() );
        }

        bds.addAll( node().getMembers() );

        bds.forEach(b -> {
            if( b instanceof FieldDeclaration ){
                FieldDeclaration fd = (FieldDeclaration)b;
                fd.getVariables().forEach( v-> _ms.add(_field.of(v)));
            } else {
                _ms.add((_java._member) _java.of(b));
            }
        } );
        return _ms;
    }

    /**
     * List the {@link _java._member}s of the memberClass: ({@link _initBlock}s, {@link _field}s, {@link _method}s,
     * {@link _constructor}s,{@link _constant}s, {@link _annoMember}s) , and inner {@link _type}s,
     * {@link _enum}s, {@link _class}es, {@link _interface}s, {@link _annotation}s) on the _type
     * @param memberClass
     * @param <_M>
     * @return
     */
    default <_M extends _java._member> List<_M> listMembers(Class<_M> memberClass){
        List<_M> _ms = new ArrayList<>();
        NodeList<BodyDeclaration<?>> bds = node().getMembers();
        bds.forEach(b -> {
            _java._domain _mem = _java.of(b);
            if (memberClass.isAssignableFrom(_mem.getClass())) {
                _ms.add((_M) _mem);
            }
        });
        return _ms;
    }

    /**
     * List the {@link _java._member}s of the memberClass: ({@link _initBlock}s, {@link _field}s, {@link _method}s,
     * {@link _constructor}s,{@link _constant}s, {@link _annoMember}s) , and inner {@link _type}s,
     * {@link _enum}s, {@link _class}es, {@link _interface}s, {@link _annotation}s) on the _type
     * @param memberClass
     * @param <_M>
     * @return
     */
    default <_M extends _java._member> List<_M> listMembers(Class<_M> memberClass, Predicate<_M> _memberMatchFn){
        List<_M> _ms = new ArrayList<>();
        NodeList<BodyDeclaration<?>> bds = node().getMembers();
        bds.forEach(b -> {
            if(b instanceof FieldDeclaration){
                 FieldDeclaration fd = (FieldDeclaration)b;
                 if (memberClass.isAssignableFrom(_field.class)) {
                    fd.getVariables().forEach( v->{
                        _field _f = _field.of(v);
                        if (_memberMatchFn.test((_M) _f)) {
                           _ms.add((_M) _f);
                        }
                    });
                 }
            } else {
                _java._member _mem = (_java._member) _java.of(b);
                if (memberClass.isAssignableFrom(_mem.getClass()) && _memberMatchFn.test((_M) _mem)) {
                    _ms.add((_M) _mem);
                }
            }
        });
        return _ms;
    }

    /**
     * Gets the first {@link _java._member} of the memberClass
     * @param <_M> the _member type (i.e. _initBlock, _method.class, _field.class, _staticBlock.class)
     * @param memberClass the Class (i.e. _initBlock.class, _method.class, _field.class, _staticBlock.class)
     * @return the first member found (null if none found)
     */
    default <_M extends _java._member> _M getMember(Class<_M> memberClass){
        List<_M> _ms = listMembers(memberClass);
        if( _ms.isEmpty()){
            return null;
        }
        return _ms.get(0);
    }

    /**
     * Gets the first _member of the _memberClass matching the _memberMatchFn
     * @param <_M> the type (i.e. _initBlock.class, _method.class, _field.class, _staticBlock.class)
     * @param memberClass the Class (i.e. _initBlock.class, _method.class, _field.class, _staticBlock.class)
     * @param _memberMatchFn function for matching a specific _declaration type
     * @return a single _member (or null if none found)
     */
    default <_M extends _java._member> _M getMember(Class<_M> memberClass, Predicate<_M> _memberMatchFn){
        List<_M> _ms = listMembers(memberClass, _memberMatchFn);
        if( _ms.isEmpty()){
            return null;
        }
        return _ms.get(0);
    }

    /**
     * apply a _memberActionFn to all  {@link _java._member}s (i.e. _initBlock.class, _method.class, _field.class, _staticBlock.class)
     * @param _memberActionFn the action function to apply to _members
     * @return the modified T
     */
    default _T toMembers(Consumer<_java._member> _memberActionFn){
        listMembers().forEach(_memberActionFn);
        return (_T)this;
    }

    /**
     *
     * @param _memberMatchFn
     * @param _memberAction
     * @return
     */
    default _T toMembers(Predicate<_java._member> _memberMatchFn, Consumer<_java._member> _memberAction){
        listMembers(_memberMatchFn).forEach(_memberAction);
        return (_T)this;
    }

    /**
     * find {@link _java._member}s that are of the specific memberClass and perform the _memberAction on them
     * @param <_M> the type (i.e. _initBlock.class, _method.class, _field.class, _staticBlock.class)
     * @param memberClass the Class (i.e. _initBlock.class, _method.class, _field.class, _staticBlock.class)
     * @param _memberAction the action function to apply to _members
     * @return the modified T
     */
    default <_M extends _java._member> _T toMembers(Class<_M> memberClass, Consumer<_M> _memberAction){
        listMembers(memberClass).forEach(_memberAction);
        return (_T)this;
    }

    /**
     * find _members that are of the specific class and perform the _memberAction on them
     * @param <_M> the type (i.e. _initBlock.class, _method.class, _field.class, _staticBlock.class)
     * @param memberClass the member Class (i.e. _initBlock, _field, _method, _constructor)
     * @param _memberMatchFn the matching function for selecting which member to take action on
     * @param _memberAction the Action function to apply to member
     * @return the modified T
     */
    default <_M extends _java._member> _T toMembers(Class<_M> memberClass, Predicate<_M> _memberMatchFn, Consumer<_M> _memberAction){
        listMembers(memberClass, _memberMatchFn).forEach(_memberAction);
        return (_T)this;
    }

    /**
     *
     * @param _members
     * @return
     */
    default _T removeMembers( _java._member... _members){
        Arrays.stream(_members).forEach( _m -> this.node().remove(_m.node()));
        return (_T)this;
    }

    /**
     *
     * @param _memberMatchFn
     * @return
     */
    default List<_java._member> removeMembersIf(Predicate<_java._member> _memberMatchFn){
        List<_java._member> mbp = listMembers(_java._member.class, _memberMatchFn);
        mbp.forEach(m -> removeMembers(m) );
        return mbp;
    }

    /**
     * remove all members ({@link _initBlock}s, {@link _field}s, {@link _method}s, {@link _constructor}s,{@link _constant}s,
     * {@link _annoMember}s, and inner {@link _type}s, {@link _enum}s, {@link _class}es, {@link _interface}s,
     * {@link _annotation}s) on the _type that are of the declarationClass and match the _declarationMatchFn
     * @param <_M> the _member type (i.e. _initBlock.class, _method.class, _field.class, _constructor.class, _initBlock.class)
     * @param memberClass the Class (i.e. _initBlock.class, _method.class, _field.class, _constructor.class, _initBlock.class)
     * @param _memberMatchFn function for matching a specific member type to remove
     * @return the removed {@link _java._member}s
     */
    default <_M extends _java._member> List<_M> removeMembersIf(Class<_M> memberClass, Predicate<_M> _memberMatchFn){
        List<_M> ms = listMembers( memberClass, _memberMatchFn);
        ms.forEach( m -> this.node().remove(m.node()) );
        return ms;
    }

    /**
     * finds the first declaration {@link _java._declared}s of Class: ({@link _field}s,
     * {@link _method}s, {@link _constructor}s,{@link _constant}s, {@link _annoMember}s) ,
     * and inner {@link _type}s, {@link _enum}s, {@link _class}es, {@link _interface}s, {@link _annotation}s)
     *  with the name and returns it
     *
     * @param name
     * @param <_D>
     */
    default <_D extends _java._declared> _D getDeclared(String name ){
        List<_java._declared> _ds = listMembers(_java._declared.class, d-> d.isNamed(name));
        Optional<_java._declared> od = _ds.stream().filter(d-> d.getName().equals(name)).findFirst();
        if( od.isPresent() ){
            return (_D)od.get();
        }
        return null;
    }

    /**
     * find _declarations that are of the specific class and perform the _declarationAction on them
     * @param <_D>
     * @param declarationClass the _declaration Class (i.e. _field, _method, _constructor)
     * @param _declarationAction the Action function to apply to _declarations
     * @return the modified T
     */
    default <_D extends _java._declared> _T toDeclared(Class<_D> declarationClass, Consumer<_D> _declarationAction){
        listMembers(declarationClass).forEach(_declarationAction);
        return (_T)this;
    }

    /**
     * find _declaration that are of the specific class and perform the _declarationAction on them
     * @param <_D>
     * @param declarationClass the _declaration Class (i.e. _field, _method, _constructor)
     * @param _declarationMatchFn the matching function for selecting which _declaration to take action on
     * @param _declarationAction the Action function to apply to _declaration
     * @return the modified T
     */
    default <_D extends _java._declared> _T toDeclared(Class<_D> declarationClass, Predicate<_D> _declarationMatchFn, Consumer<_D> _declarationAction){
        listMembers(declarationClass, _declarationMatchFn).forEach(_declarationAction);
        return (_T)this;
    }

    /**
     * Iterate & apply the action function to all {@link _java._declared}s ({@link _field}s,
     * {@link _method}s, {@link _constructor}s,{@link _constant}s, {@link _annoMember}s),
     * and inner {@link _type}s, {@link _enum}s, {@link _class}es, {@link _interface}s, {@link _annotation}s)
     * that satisfy the _declarationMatchFn
     * @param _declarationMatchFn function for selecting which _declarations to apply the _declarationActionFn
     * @param _declarationAction the action to apply to all selected _declarations that satisfy the _declarationMatchFn
     * @return the modified T type
     */
    default _T toDeclared(Predicate<_java._declared> _declarationMatchFn, Consumer<_java._declared> _declarationAction ){
        listMembers(_java._declared.class, _declarationMatchFn).forEach(_declarationAction);
        return (_T)this;
    }

    /**
     * @param <_D> the type (i.e. _method.class, _field.class, _staticBlock.class)
     * @param declarationClass the Class (i.e. _method.class, _field.class, _staticBlock.class)
     * @param name the name of the declaration
     * @return a List of the _declarations (empty if none found)
     */
    default <_D extends _java._declared> _D getDeclared(Class<_D> declarationClass, String name){
        List<_D> _ds = listMembers(declarationClass);
        Optional<_D> od = _ds.stream().filter(d-> d.getName().equals(name)).findFirst();
        if( od.isPresent() ){
            return od.get();
        }
        return null;
    }

    /**
     *
     * @return a list of matching methods
     */
    default List<_method> listMethods(){
        return listMembers(_method.class);
    }

    /**
     *
     * @param _methodMatchFn function for matching appropriate methods
     * @return a list of matching methods
     */
    default List<_method> listMethods(Predicate<_method> _methodMatchFn ){
        return listMembers(_method.class, _methodMatchFn);
    }

    /**
     * Does this _type have a {@link PackageDeclaration} Node set?
     * @return true if the TYPE is a top level TYPE AND has a declared package
     */
    default boolean hasPackage(){
        return getPackage() != null;
    }

    /**
     * Builds the full NAME for the TYPE based on it's position within a class
     * (i.e. if its a inner type) and its package)
     *
     * @return the full NAME of the TYPE (separated by '.'s)
     */
    @Override
    default String getFullName(){
        return getFullName( node() );
    }

    /**
     * Gets the simple name of the Type
     * @return 
     */
    @Override
    default String getSimpleName(){
        TypeDeclaration astType = node();
        return astType.getNameAsString();        
    }
    
    /**
     * Builds the full NAME for the TYPE based on it's  position within a class
     * (and its package)
     *
     * @param astType
     * @return the full NAME of the TYPE (separated by '.'s)
     */
    static String getFullName(TypeDeclaration astType){
        if( astType.isTopLevelType() ){
            if( astType.findCompilationUnit().get().getPackageDeclaration().isPresent()) {
                String pkgName = astType.findCompilationUnit().get().getPackageDeclaration().get().getNameAsString();
                return pkgName +"."+ astType.getNameAsString();
            }
            return astType.getNameAsString(); //top level TYPE but no package declaration
        }
        if(!astType.getParentNode().isPresent()){
            return astType.getNameAsString();
        }
        //prefix back to parents
        String name = astType.getNameAsString();
        Node n = astType.getParentNode().get();
        if( n instanceof TypeDeclaration ){
            return getFullName( (TypeDeclaration)n)+"."+name;
        }
        if( n instanceof LocalClassDeclarationStmt ){
          LocalClassDeclarationStmt lc = (LocalClassDeclarationStmt) n;
          return lc.getClassDeclaration().getNameAsString();
        }
        if( n instanceof ObjectCreationExpr){ //anonymous class
            ObjectCreationExpr oce = (ObjectCreationExpr) n;
            String designator = "";
            if( oce.findCompilationUnit().isPresent()){
                CompilationUnit cu = oce.findCompilationUnit().get();
                if(cu.getPrimaryTypeName().isPresent()){
                    designator = cu.getPrimaryTypeName().get();
                }
            }
            Optional<Node> on = oce.stream(Node.TreeTraversal.PARENTS).filter(node -> node instanceof CallableDeclaration).findFirst();
            if( on.isPresent() ){
                designator = designator +"_"+((CallableDeclaration)on.get()).getNameAsString();
            }
            if(oce.getRange().isPresent()){
                int line = oce.getRange().get().begin.line;
                int column = oce.getRange().get().begin.column;
                designator = designator +"_"+line+"_"+column;
            }
            return "anonymous_"+designator;
        }
        else {
            NodeWithName nwn = (NodeWithName) n;
            name = nwn.getNameAsString() + "." + name;
            if (n.getParentNode().isPresent()) {
                return getFullName((TypeDeclaration) n) + "." + name;
            }
        }
        return name;
    }

    /**
     * Remove the package declaration and return the modified TYPE
     * @return
     */
    default _T removePackage(){
        CompilationUnit cu = astCompilationUnit();
        if( cu == null ){
            return (_T)this;
        }
        if( cu.getPackageDeclaration().isPresent() ){
            cu.removePackageDeclaration();
        }
        return (_T)this;
    }

    /**
     * 
     * @param pd
     * @return
     */
    default _T setPackage( PackageDeclaration pd){
        if( !this.isTopLevel() ){ //this "means" that the class is an inner class
            // and we should move it OUT into it's own class at this package
            CompilationUnit cu = new CompilationUnit();
            cu.setPackageDeclaration(pd);
            cu.addType( (TypeDeclaration) this.node() );
            return (_T) this;
        }
        CompilationUnit cu = astCompilationUnit();
        cu.setPackageDeclaration( pd );
        return (_T)this;
    }

    /**
     * Sets the package this TYPE is in
     * @param packageName
     * @return the modified TYPE
     */
    default _T setPackage(String packageName ){
        if( !this.isTopLevel() ){ //this "means" that the class is an inner class
            // and we should move it OUT into it's own class at this package
            CompilationUnit cu = new CompilationUnit();    
            cu.setPackageDeclaration(packageName);
            cu.addType( (TypeDeclaration) this.node() );
            return (_T) this;
        }
        CompilationUnit cu = astCompilationUnit();
        cu.setPackageDeclaration( packageName );        
        return (_T)this;
    }

    @Override
    default _T copy(){
        if( this.isTopLevel()){
            if( this instanceof _class ) {
                return (_T)_class.of(this.astCompilationUnit().clone());
            }
            if( this instanceof _enum ) {
                return (_T)_enum.of(this.astCompilationUnit().clone());
            }
            if( this instanceof _interface ) {
                return (_T)_interface.of(this.astCompilationUnit().clone());
            }
            return (_T)_annotation.of(this.astCompilationUnit().clone());
        }
        if( this instanceof _class ) {
            return (_T)_class.of( ((_class) this).node().clone());
        }
        if( this instanceof _enum ) {
            return (_T)_enum.of( ((_enum) this).node().clone());
        }
        if( this instanceof _interface ) {
            return (_T)_interface.of( ((_interface) this).node().clone());
        }
        return (_T)_annotation.of( ((_annotation) this).node().clone());
    }

    @Override
    default _modifiers getModifiers(){
        return _modifiers.of(this.node() );
    }

    /**
     *
     * @return
     */
    default boolean isPublic(){
        return this.node().isPublic();
    }

    /**
     * Is this type of "package private" accessibility
     * (i.e. it does NOT have public private or protected modifier)
     * <PRE>
     * interface F{}
     * class C{}
     * @interface A{}
     * enum E{}
     * </PRE>
     * @return 
     */
    default boolean isPackagePrivate(){
        return !this.node().isProtected() &&
                !this.node().isPrivate() &&
                !this.node().isPublic();
    }

    /**
     *
     * @return
     */
    default boolean isProtected(){
        return this.node().isProtected();
    }

    /**
     *
     * @return
     */
    default boolean isPrivate(){
        return this.node().isPrivate();
    }

    /**
     *
     * @return
     */
    default boolean isStatic(){
        return this.node().isStatic();
    }

    /**
     *
     * @return
     */
    default boolean isStrictFp(){
        return this.node().isStrictfp();
    }

    /**
     *
     * @return
     */
    default _T setPublic(){
        this.node().setPrivate(false);
        this.node().setProtected(false);
        this.node().setPublic(true);
        return (_T)this;
    }

    /**
     *
     * @return
     */
    default _T setProtected(){
        this.node().setPublic(false);
        this.node().setPrivate(false);
        this.node().setProtected(true);
        return (_T)this;
    }

    /**
     *
     * @return
     */
    default _T setPrivate(){
        this.node().setPublic(false);
        this.node().setPrivate(true);
        this.node().setProtected(false);
        return (_T)this;
    }

    /**
     * Sets the type to be package private (i.e. NO public, protected or private modifier)
     * i.e.
     * <PRE>
     * class P{
     * }
     * </PRE>
     * @return the modified T
     */
    default _T setPackagePrivate(){
        this.node().setPublic(false);
        this.node().setPrivate(false);
        this.node().setProtected(false);
        return (_T)this;
    }

    @Override
    default NodeList<Modifier> getEffectiveAstModifiersList() {
        NodeList<Modifier> implied = Modifiers.getImpliedModifiers( this.node() );
        return Modifiers.merge( implied, this.node().getModifiers());
    }

    @Override
    default _T setName(String name ){
        if( name.contains(".") ){
            //setting the package AND type name all at once
            String packageName = name.substring(0, name.lastIndexOf(".") );
            String typeName = name.substring(name.lastIndexOf(".")+1);
            this.setPackage(packageName);
            return setName(typeName);
        }
        node().setName( name );
        if( this instanceof _class ){
            //make sure to rename the CONSTRUCTORS
            _class _c = (_class)this;
            _c.toConstructors(c-> c.setName(name));
        }
        if( this instanceof _enum ){
            //make sure to rename the CONSTRUCTORS
            _enum _e = (_enum)this;
            _e.toConstructors(c-> c.setName(name));
        }
        return (_T)this;
    }

    default SimpleName getNameNode() { return node().getName(); }

    @Override
    default String getName(){
        return node().getNameAsString();
    }

    /**
     * does this type extend the other type?
     * NOTE: this does not work for Generics, use 
     * {@link #isExtends(ClassOrInterfaceType) }
     * @param _t the type to check
     * @return true if this type extends _t
     */
    default boolean isExtends( _type _t ){
        return isExtends(_t.getFullName() );
    }
    
    /**
     * 
     * @param astType
     * @return 
     */
    default boolean isExtends( ClassOrInterfaceType astType ){

        if( this instanceof _type._withExtends){
            NodeList<ClassOrInterfaceType> impls =
                    ((NodeWithExtends)((_type)this).node()).getExtendedTypes();
            if( astType.getTypeArguments().isPresent() ){ //if I DO have type args
                return impls.stream().anyMatch(i -> Types.equal(i, astType));
            } else{
                //they didnt provide typeArgs so match against no type args
                return impls.stream().anyMatch(i -> Types.equal( Types.of(i.toString(Print.PRINT_NO_ANNOTATIONS_OR_TYPE_PARAMETERS)), astType));
            }
            /*
            NodeList<ClassOrInterfaceType> extnds = 
                ((NodeWithExtends)((_type)this).ast()).getExtendedTypes();
            if( astType.getTypeArguments().isPresent()){
                return extnds.stream().anyMatch(i -> Ast.typesEqual(i, astType));
            } else {
                return extnds.stream().anyMatch(i -> i.toString(Ast.PRINT_NO_ANNOTATIONS_OR_TYPE_PARAMETERS)
                        .equals(astType.toString(Ast.PRINT_NO_ANNOTATIONS_OR_TYPE_PARAMETERS)));
            }
             */
        }
        return false;
    }
    
    /**
     * does this type extend the type described in baseType?  
     * (Note this will parse/deal with Generics appropriately)
     * @param baseType
     * @return true if the type extends the baseType
     */
    default boolean isExtends( String baseType ){
        try{
            ClassOrInterfaceType coit = (ClassOrInterfaceType) Types.of( baseType );
            return isExtends(coit);
        } catch( Exception e){
            return false;
        }
    }
    
    /**
     * does this type extend this specific class 
     * (Note: this does not handle generic base classes... for that use 
     * {@link #isExtends(com.github.javaparser.ast.type.ClassOrInterfaceType)}
     * @param clazz the class type
     * @return true if the type extends this (raw) type
     */
    default boolean isExtends( Class clazz ){
        TypeDeclaration td =(TypeDeclaration) ((_type)this).node();
        List<ClassOrInterfaceType> coit = null;
        if( td.isClassOrInterfaceDeclaration() ) {
            coit = td.asClassOrInterfaceDeclaration().getExtendedTypes();
        } else { //not an enum or class
            return false;
        }
        if( coit.isEmpty() ){
            return false;
        }

        String canonicalName = clazz.getCanonicalName();
        String packageName = clazz.getPackage().getName();
        String simpleName = clazz.getSimpleName();

        { /* 1) implement a fully qualified class name  */
            Optional<ClassOrInterfaceType> ot =
                    coit.stream().filter(impl -> impl.toString(Print.PRINT_NO_ANNOTATIONS_OR_TYPE_PARAMETERS).equals(canonicalName)).findFirst();
            if (ot.isPresent()) {
                return true; //it directly implements the fully qualified class name
            }
        }

        CompilationUnit cu = this.astCompilationUnit();

        if( cu != null ){
            // 2...4 We need access to the CompilationUnit

            //we need to check that we Are implementing the SimpleName
            Optional<ClassOrInterfaceType> ot = //we need to Strip all implements of (potential) Annotations & Type params
                    coit.stream().filter(impl -> impl.toString(Print.PRINT_NO_ANNOTATIONS_OR_TYPE_PARAMETERS).equals(simpleName)).findFirst();
            if( ot.isPresent() ) {
                //2) a)If this class is in the same package as the target class
                if (cu.getPackageDeclaration().isPresent() &&
                        cu.getPackageDeclaration().get().getNameAsString().equals(packageName)) {
                    //2) b) ...and I am implementing the simpleName
                    return true;
                }
                //3) if we imported if I can find an import the fully qua
                if (cu.getImports().stream().anyMatch(id -> id.getNameAsString().equals(canonicalName))) {
                    return true;
                }
                //4) if we wildcard-imported the package
                if (cu.getImports().stream().anyMatch(id -> id.isAsterisk() &&
                        id.getNameAsString().equals(packageName)) ){
                    return true;
                }
                //5) if the class is in the standard package "java.lang" (i.e. Cloneable)
                if(clazz.getPackage().getName().equals("java.lang")){
                    return true;
                }
                Log.info("WE are importing %s but can't verify it is precisely %s", ()->simpleName, ()->canonicalName);
                return false;
            }
        }
        return false;
        /*
        try{
            return isExtends( (ClassOrInterfaceType)Ast.typeRef( clazz ) ) ||
                isExtends( (ClassOrInterfaceType)Ast.typeRef( clazz.getSimpleName() ) );
        }catch( Exception e){}
        return false;

         */
    }
   
    /**
     * 
     * @param str
     * @return 
     */
    default boolean isImplements( String str ){
        try{
            ClassOrInterfaceType coit = (ClassOrInterfaceType) Types.of( str );
            return isImplements( coit );
        } catch( Exception e){}
        
        if( str.contains(".") ){
            return isImplements (str.substring(str.lastIndexOf(".")+1 ) );
        }
        return false;        
    }

    /**
     * does this _type implement this (specific) classOrInterfaceType
     * (NOTE: this WILL handle generics, i.e. (Fileable<File>)
     * @param astType 
     * @return 
     */
    default boolean isImplements( ClassOrInterfaceType astType ){
        NodeList<ClassOrInterfaceType> impls =
                ((NodeWithImplements)((_type)this).node()).getImplementedTypes();
        if( astType.getTypeArguments().isPresent() ){ //if I DO have type args
            return impls.stream().filter(i -> Types.equal(i, astType)).findFirst().isPresent();
        } else{
            //they didnt provide typeArgs so match against no type args
            return impls.stream().filter(i -> Types.equal( Types.of(i.toString(Print.PRINT_NO_ANNOTATIONS_OR_TYPE_PARAMETERS)), astType)).findFirst().isPresent();
        }
        //TODO ONE possible issue is if I have Generic Type "Type<String>" and "Type<java.lang.String>"
    }

    /**
     * does this class implement this (raw) interface
     *
     * i.e. Use cases where we want to return true for:
     * _class _c = _class.of(C.class).isImplements(java.util.Map.class);
     *
     * <PRE>
     * // 1) directly implement a fully qualified class name (easy)
     * class C implements java.util.Map{}
     *
     * // 2) class located in the "java.util" package & we implement "Map" (simple name)
     * package java.util;
     * class C implements Map{}
     *
     * //3) we import "java.util.Map" class directly & implement "Map" (simple name)
     * import java.util.Map;
     * class C implements Map{}
     *
     * //4) we wildcard import "java.util" the classes package & implement Map (simple name)
     * import java.util.*;
     * class C implements Map{}
     *
     * //if the class is in the standard library (i.e. java.lang.Cloneable) no need to import
     * class C implements Cloneable{}
     *
     *
     * //TODO what about implementing inner classes??
     * //?? this WONT handle implementing subclasses (use TypeTree) (put TypeTree on ThreadLocal?)
     * </PRE>
     * @param clazz the (raw...not generic) interface class
     * @return true 
     */
    default boolean isImplements( Class clazz ){
        TypeDeclaration td =(TypeDeclaration) ((_type)this).node();
        List<ClassOrInterfaceType> coit = null;
        if( td.isClassOrInterfaceDeclaration() ) {
            coit = td.asClassOrInterfaceDeclaration().getImplementedTypes();
        } else if ( td.isEnumDeclaration() ) {
            coit = td.asEnumDeclaration().getImplementedTypes();
        } else { //not an enum or class
            return false;
        }
        if( coit.isEmpty() ){
            return false;
        }

        String canonicalName = clazz.getCanonicalName();
        String packageName = clazz.getPackage().getName();
        String simpleName = clazz.getSimpleName();

        { /* 1) implement a fully qualified class name  */
            Optional<ClassOrInterfaceType> ot =
                    coit.stream().filter(impl -> impl.toString(Print.PRINT_NO_ANNOTATIONS_OR_TYPE_PARAMETERS).equals(canonicalName)).findFirst();
            if (ot.isPresent()) {
                return true; //it directly implements the fully qualified class name
            }
        }

        CompilationUnit cu = this.astCompilationUnit();

        if( cu != null ){
            // 2...4 We need access to the CompilationUnit

            //we need to check that we Are implementing the SimpleName
            Optional<ClassOrInterfaceType> ot = //we need to Strip all implements of (potential) Annotations & Type params
                    coit.stream().filter(impl -> impl.toString(Print.PRINT_NO_ANNOTATIONS_OR_TYPE_PARAMETERS).equals(simpleName)).findFirst();
            if( ot.isPresent() ) {
                //2) a)If this class is in the same package as the target class
                if (cu.getPackageDeclaration().isPresent() &&
                        cu.getPackageDeclaration().get().getNameAsString().equals(packageName)) {
                    //2) b) ...and I am implementing the simpleName
                    return true;
                }
                //3) if we imported if I can find an import the fully qua
                if (cu.getImports().stream().anyMatch(id -> id.getNameAsString().equals(canonicalName))) {
                    return true;
                }
                //4) if we wildcard-imported the package
                if (cu.getImports().stream().anyMatch(id -> id.isAsterisk() &&
                        id.getNameAsString().equals(packageName)) ){
                    return true;
                }
                //5) if the class is in the standard package "java.lang" (i.e. Cloneable)
                if(clazz.getPackage().getName().equals("java.lang")){
                    return true;
                }
                Log.info("WE are importing %s but can't verify it is precisely %s", ()->simpleName, ()->canonicalName);
                return false;
            }
        }
        return false;
    }
    
    /**
     * Does this type implement this (raw...not generic) interface type
     * @param _i the interface type to check
     * @return true if this _type implements _i 
     */
    default boolean isImplements( _interface _i){
        return isImplements(_i.getFullName() );        
    }

    @Override
    default List<_field> listFields() {
        List<_field> _fs = new ArrayList<>();
        node().getFields().forEach(f->{
            FieldDeclaration fd = ((FieldDeclaration)f);
            for(int i=0;i<fd.getVariables().size();i++){
                _fs.add(_field.of(fd.getVariable( i ) ) );
            }
        });
        return _fs;
    }

    /**
     * List the fully qualified names of all types defined in this _type
     * @return 
     */
    default List<String> listTypeNames(){
        List<_type> _ts = Walk.list(this, _type.class);
        List<String>names = new ArrayList<>();
        _ts.forEach(t-> names.add(t.getFullName()));
        return names;
    }

    /**
     * returns a _type or a inner _type if
     * the NAME provided matches i.e.
     *
     * _class _c = _class.of("MyClass").PACKAGE_NAME("com.mypkg");
     * assertEquals( _c, _c.getType( "com.mypkg.MyClass" ) );
     * _class _c = _class.of("MyTopClass").PACKAGE_NAME("com.myo")
     * .$( _class.of( "MyNest") );
     *
     * //getAt the top level class (either by fully qualified NAME or simple NAME)
     * assertEquals( _c , _c.getType("com.myo.MyTopClass"));
     * assertEquals( _c , _c.getType("MyTopClass"));
     *
     * //getAt the $
     * assertEquals( _c.getNest("MyNest"), _c.getType("com.myo.MyTopClass.MyNest"));
     * assertEquals( _c.getNest("MyNest"), _c.getType("com.myo.MyTopClass$MyNest"));
     * assertEquals( _c.getNest("MyNest"), _c.getType("MyTopClass.MyNest"));
     * assertEquals( _c.getNest("MyNest"), _c.getType("MyTopClass$MyNest"));
     *
     * @param name the NAME (the simple NAME, canonical NAME, or fully qualified NAME
     * (with '.' or '$' annotation for inner _type)
     * @return this _type a inner _type or null if not found
     */
    default _type getType(String name ){
        String fn = this.getFullName();
        if( name.startsWith( fn ) ){
            String left = name.substring( fn.length() );
            if( left.length() == 0 ){
                return this;
            }
            if( left.startsWith( "." ) ){
                return this.getInnerType( left.substring( 1 ) );
            }
            if( left.startsWith( "$") ){
                return this.getInnerType( left.substring( 1 ) );
            }
        }
        if( name.length() > 0 && name.startsWith(this.getName()) ){
            //maybe I want an inner
            String left = name.substring( this.getName().length() );
            if( left.length() == 0 ){
                return this;
            }
            if( left.startsWith( "." ) ){
                return this.getInnerType( left.substring( 1 ) );
            }
            if( left.startsWith( "$") ){
                return this.getInnerType( left.substring( 1 ) );
            }
        }
        return this.getInnerType( name );
    }

    /**
     * Does this TYPE have a main method?
     * @return
     */
    default boolean hasMain(){
        if( this instanceof _method._withMethods){
            return ((_method._withMethods)this).listMethods(_method.IS_MAIN).size() > 0;
        }
        return false;
    }

    /**
     * Add a inner type to this _type
     * @param type the TYPE to add
     * @return
     */
    default _T addInnerType(_type type ){
        ((TypeDeclaration)this.node()).addMember( (TypeDeclaration)type.node() );
        return (_T)this;
    }

    /**
     * apply a function to matching inner types of this TYPE
     * @param _typeMatchFn
     * @param _typeActionFn
     * @return the modified _type
     */
    default _T toInnerTypes(Predicate<_type> _typeMatchFn, Consumer<_type> _typeActionFn ){
        listInnerTypes(_typeMatchFn).forEach( _typeActionFn );
        return (_T)this;
    }

    /**
     * apply a function to NESTS of this _type
     * @param _typeActionFn
     * @return the modified _type
     */
    default _T toInnerTypes(Consumer<_type> _typeActionFn ){
        listInnerTypes().forEach( _typeActionFn );
        return (_T)this;
    }

    /**
     * apply a function to matching inner types of this TYPE
     * @param _typeMatchFn
     * @param _typeActionFn
     * @return the modified _type
     */
    default List<_type> forInnerTypes(Predicate<_type> _typeMatchFn, Consumer<_type> _typeActionFn ){
        List<_type> _types = listInnerTypes(_typeMatchFn);
        _types.forEach( _typeActionFn );
        return _types;
    }

    /**
     * apply a function to NESTS of this _type
     * @param _typeActionFn
     * @return the modified _type
     */
    default List<_type> forInnerTypes(Consumer<_type> _typeActionFn ){
        List<_type> _ts = listInnerTypes();
        _ts.forEach( _typeActionFn );
        return _ts;
    }

    /**
     * Gets the inner TYPE with the NAME
     * @param name
     * @return the inner TYPE with the NAME or null if not found
     */
    default _type getInnerType(String name ){
        List<_type> ts = listInnerTypes(t-> t.getName().equals(name));
        if( ts.size() == 1 ){
            return ts.get(0);
        }
        return null;
    }

    /**
     * 
     * @param innerTypeToRemove
     * @return 
     */
    default <_IT extends _type> _T removeInnerType(_IT innerTypeToRemove ){
        listInnerTypes(t-> t.equals(innerTypeToRemove) ).forEach(n -> n.node().removeForced() );
        return (_T) this;
    }
    
    /**
     * 
     * @param innerTypeToRemove
     * @return 
     */
    default <_IT extends _type> _T removeInnerType(TypeDeclaration innerTypeToRemove ){
        return removeInnerType( (_type) of(innerTypeToRemove ) );
    }
    
    /**
     * list all inner types that match this _typeMatchFn
     * @param typeMatchFn function to
     * @return matching inner types or empty list if none found
     */
    default List<_type> listInnerTypes(Predicate<? super _type> typeMatchFn ){
        return listInnerTypes().stream().filter( typeMatchFn ).collect(Collectors.toList());
    }

    /**
     *
     * @param _typeClass
     * @param _typeMatchFn
     * @param <_NT>
     * @return
     */
    default <_NT extends _type> List<_NT> listInnerTypes(Class<_NT> _typeClass, Predicate<_NT> _typeMatchFn){
        return (List<_NT>) listInnerTypes().stream()
                .filter(_t -> _typeClass.isAssignableFrom(_t.getClass()) && _typeMatchFn.test((_NT)_t) )
                .collect(Collectors.toList());
    }

    /**
     * list all inner children underneath this logical _type
     * (1-level, DIRECT CHILDREN, and NOT grand children or great grand children)
     * for a more comprehensive gathering of all types, call:
     *
     * @return the direct children (inner {@link _type}s) of this {@link _type}
     */
    default List<_type> listInnerTypes(){
        NodeList<BodyDeclaration<?>> bds = node().getMembers();
        List<BodyDeclaration> ts =
                bds.stream().filter( n-> n instanceof TypeDeclaration )
                    .collect(Collectors.toList());

        List<_type> inners = new ArrayList<>();
        ts.forEach( t-> {
            if( t instanceof ClassOrInterfaceDeclaration ){
                ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration)t;
                if( coid.isInterface() ){
                    inners.add( _interface.of( coid ));
                } else{
                    inners.add( _class.of( coid ));
                }
            }
            else if ( t instanceof EnumDeclaration ){
                inners.add( _enum.of( (EnumDeclaration)t));
            }
            else{
                inners.add( _annotation.of( (AnnotationDeclaration)t));
            }
        });
        return inners;
    }

    /**
     * {@link _type} that can implement an {@link _interface}
     * @param <_HI> the TYPE (either {@link _class}, or {@link _enum})
     */
    interface _withImplements<_HI extends _type & _withImplements> {

        default boolean hasImplements(){
            return !listAstImplements().isEmpty();
        }

        default boolean hasImplements(String implType ){
            Stencil st = Stencil.of(implType);
            if( st.isMatchAny() ){
                return true;
            }
            if( st.isFixedText() ){
                try {
                    return hasImplements(_typeRef.of(implType));
                }catch(Exception e){
                    return false;
                }
            }
            _typeRef _ti = _typeRef.of(implType);
            final Stencil ns = Stencil.of(_ti.toString());
            return listImplements().stream().anyMatch(t-> ns.matches(t.toString()));
        }

        default boolean hasImplements(Class... clazzes){
            for(int i=0;i<clazzes.length; i++){
                _typeRef _aI = _typeRef.of(clazzes[i]);
                if( !listImplements().stream().anyMatch(t-> t.equals(_aI))){
                    return false;
                }
            }
            return true;
        }

        default boolean hasImplements(_typeRef... _impls){
            for(int i=0;i<_impls.length; i++){
                _typeRef _aI = _impls[i];
                if( !listImplements().stream().anyMatch(t-> t.equals(_aI))){
                    return false;
                }
            }
            return true;
        }

        default boolean hasImplements(Predicate<_typeRef> _matchFn){
            return listImplements().stream().anyMatch(_matchFn);
        }

        default List<_typeRef> listImplements(){
            return listAstImplements().stream().map(i-> _typeRef.of(i)).collect(Collectors.toList());
        }

        default List<_typeRef> listImplements(Predicate<_typeRef> matchFn){
            return listAstImplements().stream().map(i-> _typeRef.of(i)).filter(matchFn).collect(Collectors.toList());
        }

        default _HI setImplements( List<_typeRef> trs){
            ((NodeWithImplements)((_type)this).node()).getImplementedTypes().clear();
            addImplements(trs);
            return (_HI) this;
        }

        default _HI toImplements(Consumer<_typeRef> _actionFn){
            listImplements().forEach(_actionFn);
            return (_HI)this;
        }

        default _HI toImplements(Predicate<_typeRef> _matchFn, Consumer<_typeRef> _actionFn){
            listImplements(_matchFn).forEach(_actionFn);
            return (_HI)this;
        }

        default List<_typeRef> forImplements(Consumer<_typeRef> _actionFn){
            List<_typeRef> is = listImplements();
            is.forEach(_actionFn);
            return is;
        }

        default List<_typeRef> forImplements(Predicate<_typeRef> _matchFn, Consumer<_typeRef> _actionFn){
            List<_typeRef> is = listImplements(_matchFn);
            is.forEach(_actionFn);
            return is;
        }

        default List<ClassOrInterfaceType> listAstImplements(){
            return ((NodeWithImplements)((_type)this).node()).getImplementedTypes();
        }

        default _HI addImplements(_interface..._interfaces ){
            Arrays.stream(_interfaces).forEach(i-> addImplements(i.getFullName() ) );
            return (_HI)this;
        }        

        default _HI addImplements(List<_typeRef> _trs){
            _trs.forEach(_tr -> addImplements( (ClassOrInterfaceType)_tr.node()));
            return (_HI)this;
        }

        default _HI addImplements(ClassOrInterfaceType... toImplement ){
            NodeWithImplements nwi = ((NodeWithImplements)((_type)this).node());
            Arrays.stream( toImplement ).forEach(i -> nwi.addImplementedType( i ) );
            return (_HI)this;
        }
        
        default _HI addImplements(Class... toImplement ){
            NodeWithImplements nwi = ((NodeWithImplements)((_type)this).node());
            
            Arrays.stream( toImplement )
                .forEach(i -> {
                        ClassOrInterfaceType coit = (ClassOrInterfaceType) Types.of(i);
                        nwi.addImplementedType( coit );   
                        ((_type)this).addImports(i);
                    });
            return (_HI)this;
        }

        default _HI addImplements(String... toImplement ){
            NodeWithImplements nwi = ((NodeWithImplements)((_type)this).node());
            Arrays.stream( toImplement ).forEach(i -> nwi.addImplementedType( i ) );
            return (_HI)this;
        }

        /**
         *
         * @param clazz
         * @return
         */
        default _HI removeImplements(Class clazz ){
            NodeWithImplements nwi = ((NodeWithImplements)((_type)this).node());
            _typeRef _tr = _typeRef.of(clazz);
            nwi.getImplementedTypes().removeIf(t-> t.equals( _tr.node()));
            return (_HI)this;
        }


        /**
         * removes all implements and returns the type
         * @return
         */
        default _HI removeImplements(){
            NodeWithImplements nwi = ((NodeWithImplements)((_type)this).node());
            nwi.getImplementedTypes().clear();
            return (_HI)this;
        }

        /**
         *
         * @param coit
         * @return
         */
        default _HI removeImplements(ClassOrInterfaceType coit ){
            NodeWithImplements nwi = ((NodeWithImplements)((_type)this).node());
            nwi.getImplementedTypes().remove(coit);
            return (_HI)this;
        }

        /**
         * If you pass in a fully qualified name, this will remove ONLY this name i.e. "aaaa.bbbb.C"
         * if you pass in a simple name "C" this will remove ALL that match this
         * @param toRemove
         * @return
         */
        default _HI removeImplements(String toRemove){
            NodeWithImplements nwi = ((NodeWithImplements)((_type)this).node());
            if( toRemove.contains(".") ) { //they passed in a fully qualified name, so remove exact match
                nwi.getImplementedTypes().removeIf(i -> ((ClassOrInterfaceType) i).getNameAsString().equals(toRemove));
            } else{
                nwi.getImplementedTypes().removeIf(i -> {
                    String lastName = ((ClassOrInterfaceType) i).getNameAsString();
                    int dotIndex = lastName.lastIndexOf('.');
                    if( dotIndex > 0 ){
                        lastName = lastName.substring(dotIndex + 1);
                    }
                    return Objects.equals( lastName, toRemove);
                });
            }
            return (_HI)this;
        }

        default _HI removeImplements(_interface _i){
            removeImplements(_i.getSimpleName());
            removeImplements(_i.getFullName());
            return (_HI)this;
        }
    }

    /**
     * A TYPE that can extend (another {@link _class} or {@link _interface})
     * @param <_WE>
     */
    interface _withExtends<_WE extends _type & _withExtends> {

        boolean hasExtends();

        NodeList<ClassOrInterfaceType> listAstExtends();

        List<_typeRef> listExtends();

        _WE addExtend(ClassOrInterfaceType toExtend );

        default _WE addExtend(_class _c ){
            addExtend( _c.getFullName() );
            return (_WE)this;
        }

        default _WE addExtend(_interface _i ){
            addExtend( _i.getFullName() );
            return (_WE)this;
        }

        _WE addExtend(Class toExtend );

        _WE addExtend(String toExtend );

        _WE removeExtends(Class clazz);

        _WE removeExtends(ClassOrInterfaceType coit );
    }

    /**
     * Implementations of {@link _type}
     */
    class Impl{
        public static final Class<_class> CLASS = _class.class;
        public static final Class<_enum> ENUM = _enum.class;
        public static final Class<_interface> INTERFACE = _interface.class;
        public static final Class<_annotation> ANNOTATION = _annotation.class;

        public static final Class<? extends _type>[] ALL = new Class[]{ CLASS, ENUM, INTERFACE, ANNOTATION};
    }
}
