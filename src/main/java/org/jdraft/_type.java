package org.jdraft;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

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
import org.jdraft.macro.macro;

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
 * AST
 *            & NodeWithAnnotations
 */
public interface _type<AST extends TypeDeclaration, _T extends _type>
    extends _javadoc._withJavadoc<_T>, _annos._withAnnos<_T>, _modifiers._withModifiers<_T>,
        _field._withFields<_T>, _java._declaredBodyPart<AST, _T>, _codeUnit<_T>, _java._multiPart<AST, _T> {

    /*
    static <_T extends _type> _T of( String...typeCode ){
        return (_T)of(Ast.of(typeCode));
    }
    static <_T extends _type> _T  of(CompilationUnit cu){
        if( cu.getPrimaryType().isPresent()){
            return of( (TypeDeclaration)cu.getPrimaryType().get() );
        }
        if( cu.getTypes().size() == 1){
            return of( cu.getType(0));
        }
        return of(cu.getType(0));
    }
    */
    /*
    static <_T extends _type> _T of( TypeDeclaration td ){
        if( td instanceof ClassOrInterfaceDeclaration){
            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration) td;
            if( !coid.isInterface()) {
                return (_T)_class.of(coid);
            }
            return (_T)_interface.of(coid);
        }
        if( td instanceof EnumDeclaration){
            return (_T)_enum.of( (EnumDeclaration)td);
        }
        return (_T)_annotation.of( (AnnotationDeclaration) td);
    }
    */

    /**
     * Read in a .java file from the InputStream and return the _type(_class, _enum, _interface, _annotation)
     * @param is
     * @return
     */
    static <_T extends _type> _T of(InputStream is) {
        _codeUnit _c = _codeUnit.of(is);
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
        _codeUnit _c = _codeUnit.of(path);
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
        return of(Ast.typeDecl(code));
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
    static <_T extends _type> _T of(Class clazz, _in._resolver resolver){
        Node n = Ast.typeDecl( clazz, resolver );
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
        return of(clazz, _io.IN_DEFAULT);
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
                TypeDeclaration td = (TypeDeclaration)(_ts[i].setPackagePrivate()).ast();
                if( td.getParentNode().isPresent() ){
                    td.getParentNode().get().remove(td);
                }
                CompilationUnit cu = ast().findCompilationUnit().get();
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
    default _T forCompanionTypes(Consumer<_type> _typeActionFn ){
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
    default <CT extends _type> _T forCompanionTypes(
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
    default <_CT extends _type> _T forCompanionTypes(
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
                .forEach( t -> astCompilationUnit().remove( t.ast() ) );
        return (_T)this;
    }

    /**
     * Removes the companionType given the type
     * @param _t
     * @return
     */
    default _T removeCompanionType(_type _t){
        listCompanionTypes(t -> t.equals(_t))
                .forEach( t -> astCompilationUnit().remove( t.ast() ) );
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
     * {@link _annotation._entry}s), and inner {@link _type}s, {@link _enum}s, {@link _class}es, {@link _interface}s,
     * {@link _annotation}s) to the BODY of the {@link _type} and return the modified {@link _type}
     * @param members the members to be added
     * @return the modified _type
     */
    default _T add(_java._memberBodyPart... members){
        Arrays.stream(members).forEach(_m -> {
            if(_m instanceof _field){
                this.ast().addMember( ((_field)_m).getFieldDeclaration() );
            } else{
                this.ast().addMember( (BodyDeclaration)_m.ast() );
            }
        }  );
        return (_T)this;
    }

    default List<_java._memberBodyPart> listMembers( Predicate<_java._memberBodyPart> _matchFn){
        List<_java._memberBodyPart> _ms = new ArrayList<>();
        NodeList<BodyDeclaration<?>> bds = ast().getMembers();

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
                _java._memberBodyPart _mbp = (_java._memberBodyPart) _java.of(b);
                if( _matchFn.test(_mbp)) {
                    _ms.add(_mbp);
                }
            }
        } );
        return _ms;
    }

    /**
     * List the {@link _java._memberBodyPart}s: ({@link _initBlock}s, {@link _field}s, {@link _method}s, {@link _constructor}s,
     * {@link _constant}s, {@link _annotation._entry}s) , and inner{@link _type}s, {@link _enum}s,
     * {@link _class}es, {@link _interface}s, {@link _annotation}s) on the _type
     * @return a List of {@link _java._declaredBodyPart}s on the type
     */
    default List<_java._memberBodyPart> listMembers(){
        List<_java._memberBodyPart> _ms = new ArrayList<>();
        NodeList<BodyDeclaration<?>> bds = ast().getMembers();

        bds.forEach(b -> {
            if( b instanceof FieldDeclaration ){
                FieldDeclaration fd = (FieldDeclaration)b;
                fd.getVariables().forEach( v-> _ms.add(_field.of(v)));
            } else {
                _ms.add((_java._memberBodyPart) _java.of(b));
            }
        } );
        return _ms;
    }

    /**
     * List the {@link _java._memberBodyPart}s of the memberClass: ({@link _initBlock}s, {@link _field}s, {@link _method}s,
     * {@link _constructor}s,{@link _constant}s, {@link _annotation._entry}s) , and inner {@link _type}s,
     * {@link _enum}s, {@link _class}es, {@link _interface}s, {@link _annotation}s) on the _type
     * @param memberClass
     * @param <_M>
     * @return
     */
    default <_M extends _java._memberBodyPart> List<_M> listMembers(Class<_M> memberClass){
        List<_M> _ms = new ArrayList<>();
        NodeList<BodyDeclaration<?>> bds = ast().getMembers();
        bds.forEach(b -> {
            _java._domain _mem = _java.of(b);
            if (memberClass.isAssignableFrom(_mem.getClass())) {
                _ms.add((_M) _mem);
            }
        });
        return _ms;
    }

    /**
     * List the {@link _java._memberBodyPart}s of the memberClass: ({@link _initBlock}s, {@link _field}s, {@link _method}s,
     * {@link _constructor}s,{@link _constant}s, {@link _annotation._entry}s) , and inner {@link _type}s,
     * {@link _enum}s, {@link _class}es, {@link _interface}s, {@link _annotation}s) on the _type
     * @param memberClass
     * @param <_M>
     * @return
     */
    default <_M extends _java._memberBodyPart> List<_M> listMembers(Class<_M> memberClass, Predicate<_M> _memberMatchFn){
        List<_M> _ms = new ArrayList<>();
        NodeList<BodyDeclaration<?>> bds = ast().getMembers();
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
                _java._memberBodyPart _mem = (_java._memberBodyPart) _java.of(b);
                if (memberClass.isAssignableFrom(_mem.getClass()) && _memberMatchFn.test((_M) _mem)) {
                    _ms.add((_M) _mem);
                }
            }
        });
        return _ms;
    }

    /**
     * Gets the first {@link _java._memberBodyPart} of the memberClass
     * @param <_M> the _member type (i.e. _initBlock, _method.class, _field.class, _staticBlock.class)
     * @param memberClass the Class (i.e. _initBlock.class, _method.class, _field.class, _staticBlock.class)
     * @return the first member found (null if none found)
     */
    default <_M extends _java._memberBodyPart> _M getMember(Class<_M> memberClass){
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
    default <_M extends _java._memberBodyPart> _M getMember(Class<_M> memberClass, Predicate<_M> _memberMatchFn){
        List<_M> _ms = listMembers(memberClass, _memberMatchFn);
        if( _ms.isEmpty()){
            return null;
        }
        return _ms.get(0);
    }

    /**
     * apply a _memberActionFn to all  {@link _java._memberBodyPart}s (i.e. _initBlock.class, _method.class, _field.class, _staticBlock.class)
     * @param _memberActionFn the action function to apply to _members
     * @return the modified T
     */
    default _T forMembers(Consumer<_java._memberBodyPart> _memberActionFn){
        listMembers().forEach(_memberActionFn);
        return (_T)this;
    }


    default _T forMembers(Predicate<_java._memberBodyPart> mb, Consumer<_java._memberBodyPart> _memberAction){
        listMembers(mb).forEach(_memberAction);
        return (_T)this;
    }

    /**
     * find {@link _java._memberBodyPart}s that are of the specific memberClass and perform the _memberAction on them
     * @param <_M> the type (i.e. _initBlock.class, _method.class, _field.class, _staticBlock.class)
     * @param memberClass the Class (i.e. _initBlock.class, _method.class, _field.class, _staticBlock.class)
     * @param _memberAction the action function to apply to _members
     * @return the modified T
     */
    default <_M extends _java._memberBodyPart> _T forMembers(Class<_M> memberClass, Consumer<_M> _memberAction){
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
    default <_M extends _java._memberBodyPart> _T forMembers(Class<_M> memberClass, Predicate<_M> _memberMatchFn, Consumer<_M> _memberAction){
        listMembers(memberClass, _memberMatchFn).forEach(_memberAction);
        return (_T)this;
    }

    default _T removeMembers( _java._memberBodyPart... _members){
        Arrays.stream(_members).forEach( _m -> this.ast().remove(_m.ast()));
        return (_T)this;
    }

    default List<_java._memberBodyPart> removeMembers(Predicate<_java._memberBodyPart> _memberMatchFn){
        List<_java._memberBodyPart> mbp = listMembers(_java._memberBodyPart.class, _memberMatchFn);
        mbp.forEach(m -> removeMembers(m) );
        return mbp;
    }

    /**
     * remove all members ({@link _initBlock}s, {@link _field}s, {@link _method}s, {@link _constructor}s,{@link _constant}s,
     * {@link _annotation._entry}s, and inner {@link _type}s, {@link _enum}s, {@link _class}es, {@link _interface}s,
     * {@link _annotation}s) on the _type that are of the declarationClass and match the _declarationMatchFn
     * @param <_M> the _member type (i.e. _initBlock.class, _method.class, _field.class, _constructor.class, _initBlock.class)
     * @param memberClass the Class (i.e. _initBlock.class, _method.class, _field.class, _constructor.class, _initBlock.class)
     * @param _memberMatchFn function for matching a specific member type to remove
     * @return the removed {@link _java._memberBodyPart}s
     */
    default <_M extends _java._memberBodyPart> List<_M> removeMembers(Class<_M> memberClass, Predicate<_M> _memberMatchFn){
        List<_M> ms = listMembers( memberClass, _memberMatchFn);
        ms.forEach( m -> this.ast().remove(m.ast()) );
        return ms;
    }

    default List<_java._declaredBodyPart> removeDeclared(Predicate<_java._declaredBodyPart> _declaredMatchFn){
        List<_java._declaredBodyPart> ms = listDeclared( (Predicate<_java._declaredBodyPart>)_declaredMatchFn);
        ms.forEach( m -> this.ast().remove(m.ast()) );
        return ms;
    }

    /**
     * Iterate & apply some functional action towards _declarations on the _type
     * @param _declarationAction the action to apply to ALL declarations
     * @return the (modified) _T _type
     */
    default _T forDeclared(Consumer<_java._declaredBodyPart> _declarationAction ){
        return forDeclared(t-> true, _declarationAction );
    }

    /**
     * find _declarations that are of the specific class and perform the _declarationAction on them
     * @param <_D>
     * @param declarationClass the _declaration Class (i.e. _field, _method, _constructor)
     * @param _declarationAction the Action function to apply to _declarations
     * @return the modified T
     */
    default <_D extends _java._declaredBodyPart> _T forDeclared(Class<_D> declarationClass, Consumer<_D> _declarationAction){
        listDeclared(declarationClass).forEach(_declarationAction);
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
    default <_D extends _java._declaredBodyPart> _T forDeclared(Class<_D> declarationClass, Predicate<_D> _declarationMatchFn, Consumer<_D> _declarationAction){
        listDeclared(declarationClass, _declarationMatchFn).forEach(_declarationAction);
        return (_T)this;
    }

    /**
     * Iterate & apply the action function to all {@link _java._declaredBodyPart}s ({@link _field}s,
     * {@link _method}s, {@link _constructor}s,{@link _constant}s, {@link _annotation._entry}s),
     * and inner {@link _type}s, {@link _enum}s, {@link _class}es, {@link _interface}s, {@link _annotation}s)
     * that satisfy the _declarationMatchFn
     * @param _declarationMatchFn function for selecting which _declarations to apply the _declarationActionFn
     * @param _declarationAction the action to apply to all selected _declarations that satisfy the _declarationMatchFn
     * @return the modified T type
     */
    default _T forDeclared(Predicate<_java._declaredBodyPart> _declarationMatchFn, Consumer<_java._declaredBodyPart> _declarationAction ){
        listDeclared(_declarationMatchFn).forEach(_declarationAction);
        return (_T)this;
    }

    /**
     * List the {@link _java._declaredBodyPart}s ({@link _field}s, {@link _method}s, {@link _constructor}s,{@link _constant}s,
     * {@link _annotation._entry}s), and inner {@link _type}s, {@link _enum}s, {@link _class}es, {@link _interface}s,
     * {@link _annotation}s) on the _type
     * @return a List of {@link _java._declaredBodyPart}s on the _type
     */
    default List<_java._declaredBodyPart> listDeclared(){
        return listDeclared(_java._declaredBodyPart.class, t->true);
    }

    /**
     * List all _declarations ({@link _field}s, {@link _method}s, {@link _constructor}s,{@link _constant}s,
     * {@link _annotation._entry}s) , and inner {@link _type}s, {@link _enum}s, {@link _class}es, {@link _interface}s,
     * {@link _annotation}s) on the _type matching the _declarationMatchFn
     * @param _declarationMatchFn
     * @return a list of _declarations
     */
    default List<_java._declaredBodyPart> listDeclared(Predicate<_java._declaredBodyPart> _declarationMatchFn){
        return listDeclared(_java._declaredBodyPart.class, _declarationMatchFn);
    }
    
    /**
     * list all _declared ({@link _field}s, {@link _method}s, {@link _constructor}s,{@link _constant}s,
     * {@link _annotation._entry}s), and inner {@link _type}s, {@link _enum}s, {@link _class}es, {@link _interface}s,
     * {@link _annotation}s) oon the _type that are of the declarationClass
     * @param <_D> the _declaration type (i.e. _method.class, _field.class, _staticBlock.class)
     * @param declarationClass the Class (i.e. _method.class, _field.class, _staticBlock.class)
     * @return a List of the types (empty if none found)
     */
    default <_D extends _java._declaredBodyPart> List<_D> listDeclared(Class<_D> declarationClass){
        return listDeclared(declarationClass, t->true);
    }
    
    /**
     * list all _declarations ({@link _field}s, {@link _method}s, {@link _constructor}s,{@link _constant}s,
     * {@link _annotation._entry}s, and inner {@link _type}s, {@link _enum}s, {@link _class}es, {@link _interface}s,
     * {@link _annotation}s) on the _type that are of the declarationClass
     * @param <_D> the _declaration type (i.e. _method.class, _field.class, _constructor.class)
     * @param declarationClass the Class (i.e. _method.class, _field.class, _constructor.class)
     * @param _declarationMatchFn function for matching a specific member type
     * @return a List of the types (empty if none found)
     */
    default <_D extends _java._declaredBodyPart> List<_D> listDeclared(Class<_D> declarationClass, Predicate<_D> _declarationMatchFn){
        return listMembers(declarationClass, _declarationMatchFn);
    }

    /**
     * Gets the first _declaration matching the _declarationMatchFn
     * @param _declarationMatchFn
     * @return the first _declaration found (or null if none found)
     */
    default _java._declaredBodyPart getDeclared(Predicate<_java._declaredBodyPart> _declarationMatchFn){
        List<_java._declaredBodyPart> _ds = listDeclared(_declarationMatchFn);
        if( _ds.isEmpty()){
            return null;
        }
        return _ds.get(0);
    }

    /**
     * Gets the first _declaration of the _declarationClass
     * @param <_D> the type (i.e. _method.class, _field.class, _staticBlock.class)
     * @param declarationClass the Class (i.e. _method.class, _field.class, _staticBlock.class)
     * @return the first _declaration found (null if none found)
     */
    default <_D extends _java._declaredBodyPart> _D getDeclared(Class<_D> declarationClass){
        List<_D> _ds = listDeclared(declarationClass);
        if( _ds.isEmpty()){
            return null;
        }
        return _ds.get(0);
    }

    /**
     * Gets the first _declaration of the declarationClass matching the _declarationMatchFn
     * @param <_D> the type (i.e. _method.class, _field.class, _staticBlock.class)
     * @param declarationClass the Class (i.e. _method.class, _field.class, _staticBlock.class)
     * @param _declarationMatchFn function for matching a specific _declaration type
     * @return a List of the _declarations (empty if none found)
     */
    default <_D extends _java._declaredBodyPart> _D getDeclared(Class<_D> declarationClass, Predicate<_D> _declarationMatchFn){
        List<_D> _ds = listDeclared(declarationClass, _declarationMatchFn);
        if( _ds.isEmpty()){
            return null;
        }
        return _ds.get(0);
    }

    /**
     * finds the first declaration {@link _java._declaredBodyPart}s of Class: ({@link _field}s,
     * {@link _method}s, {@link _constructor}s,{@link _constant}s, {@link _annotation._entry}s) ,
     * and inner {@link _type}s, {@link _enum}s, {@link _class}es, {@link _interface}s, {@link _annotation}s)
     *  with the name and returns it
     *
     * @param name
     * @param <_D>
     */
    default <_D extends _java._declaredBodyPart> _D getDeclared(String name ){
        List<_java._declaredBodyPart> _ds = listDeclared();
        Optional<_java._declaredBodyPart> od = _ds.stream().filter(d-> d.getName().equals(name)).findFirst();
        if( od.isPresent() ){
            return (_D)od.get();
        }
        return null;
    }

    /**
     * @param <_D> the type (i.e. _method.class, _field.class, _staticBlock.class)
     * @param declarationClass the Class (i.e. _method.class, _field.class, _staticBlock.class)
     * @param name the name of the declaration
     * @return a List of the _declarations (empty if none found)
     */
    default <_D extends _java._declaredBodyPart> _D getDeclared(Class<_D> declarationClass, String name){
        List<_D> _ds = listDeclared(declarationClass);
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
        return listDeclared(_method.class);
    }

    /**
     *
     * @param _methodMatchFn function for matching appropriate methods
     * @return a list of matching methods
     */
    default List<_method> listMethods(Predicate<_method> _methodMatchFn ){
        return listDeclared(_method.class, _methodMatchFn);
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
        return getFullName( ast() );
    }

    /**
     * Gets the simple name of the Type
     * @return 
     */
    @Override
    default String getSimpleName(){
        TypeDeclaration astType = ast();
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
     * Determines the package this class is in
     * @return
     */
    default String getPackage(){
        CompilationUnit cu = astCompilationUnit();
        if( cu == null ){
            return null;
        }
        if( cu.getPackageDeclaration().isPresent() ){
            return cu.getPackageDeclaration( ).get().getNameAsString();
        }
        return null;
    }

    default _T setPackage( PackageDeclaration pd){
        if( !this.isTopLevel() ){ //this "means" that the class is an inner class
            // and we should move it OUT into it's own class at this package
            CompilationUnit cu = new CompilationUnit();
            cu.setPackageDeclaration(pd);
            cu.addType( (TypeDeclaration) this.ast() );
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
            cu.addType( (TypeDeclaration) this.ast() );            
            return (_T) this;
        }
        CompilationUnit cu = astCompilationUnit();
        cu.setPackageDeclaration( packageName );        
        return (_T)this;
    }

    /**
     * Does this type reside in this package
     * @param packageName
     * @return 
     */
    default boolean isInPackage( String packageName){
        String pn  = getPackage();
        if( pn == null){
            return packageName == null || packageName.length() == 0;
        }
        if( Objects.equals(pn, packageName) ){
            return true;
        }
        if( packageName !=null ){
            return packageName.indexOf(pn) == 0;
        }
        return false;
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
            return (_T)_class.of( ((_class) this).ast().clone());
        }
        if( this instanceof _enum ) {
            return (_T)_enum.of( ((_enum) this).ast().clone());
        }
        if( this instanceof _interface ) {
            return (_T)_interface.of( ((_interface) this).ast().clone());
        }
        return (_T)_annotation.of( ((_annotation) this).ast().clone());
    }

    @Override
    default _modifiers getModifiers(){
        return _modifiers.of(this.ast() );
    }

    default boolean isPublic(){
        return this.ast().isPublic();
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
        return !this.ast().isProtected() &&
                !this.ast().isPrivate() &&
                !this.ast().isPublic();
    }

    default boolean isProtected(){
        return this.ast().isProtected();
    }

    default boolean isPrivate(){
        return this.ast().isPrivate();
    }

    default boolean isStatic(){
        return this.ast().isStatic();
    }

    default boolean isStrictFp(){
        return this.ast().isStrictfp();
    }

    default _T setPublic(){
        this.ast().setPrivate(false);
        this.ast().setProtected(false);
        this.ast().setPublic(true);        
        return (_T)this;
    }

    default _T setProtected(){
        this.ast().setPublic(false);
        this.ast().setPrivate(false);
        this.ast().setProtected(true);
        return (_T)this;
    }
    
    default _T setPrivate(){
        this.ast().setPublic(false);
        this.ast().setPrivate(true);
        this.ast().setProtected(false);
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
        this.ast().setPublic(false);
        this.ast().setPrivate(false);
        this.ast().setProtected(false);
        return (_T)this;
    }

    @Override
    default NodeList<Modifier> getEffectiveModifiers() {
        NodeList<Modifier> implied = Ast.getImpliedModifiers( this.ast() );
        return Ast.merge( implied, this.ast().getModifiers());
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
        ast().setName( name );
        if( this instanceof _class ){
            //make sure to rename the CONSTRUCTORS
            _class _c = (_class)this;
            _c.forConstructors( c-> c.setName(name));
        }
        if( this instanceof _enum ){
            //make sure to rename the CONSTRUCTORS
            _enum _e = (_enum)this;
            _e.forConstructors( c-> c.setName(name));
        }
        return (_T)this;
    }

    default SimpleName getNameNode() { return ast().getName(); }

    @Override
    default String getName(){
        return ast().getNameAsString();
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
                    ((NodeWithExtends)((_type)this).ast()).getExtendedTypes();
            if( astType.getTypeArguments().isPresent() ){ //if I DO have type args
                return impls.stream().anyMatch(i -> Ast.typesEqual(i, astType));
            } else{
                //they didnt provide typeArgs so match against no type args
                return impls.stream().anyMatch(i -> Ast.typesEqual( Ast.typeRef(i.toString(Print.PRINT_NO_ANNOTATIONS_OR_TYPE_PARAMETERS)), astType));
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
            ClassOrInterfaceType coit = (ClassOrInterfaceType)Ast.typeRef( baseType );
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
        TypeDeclaration td =(TypeDeclaration) ((_type)this).ast();
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
            ClassOrInterfaceType coit = (ClassOrInterfaceType)Ast.typeRef( str );
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
                ((NodeWithImplements)((_type)this).ast()).getImplementedTypes();
        if( astType.getTypeArguments().isPresent() ){ //if I DO have type args
            return impls.stream().filter(i -> Ast.typesEqual(i, astType)).findFirst().isPresent();
        } else{
            //they didnt provide typeArgs so match against no type args
            return impls.stream().filter(i -> Ast.typesEqual( Ast.typeRef(i.toString(Print.PRINT_NO_ANNOTATIONS_OR_TYPE_PARAMETERS)), astType)).findFirst().isPresent();
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
        TypeDeclaration td =(TypeDeclaration) ((_type)this).ast();
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
        ast().getFields().forEach( f->{
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
        List<_type> _ts = Tree.list(this, _type.class);
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
    default _T addInner(_type type ){
        ((TypeDeclaration)this.ast()).addMember( (TypeDeclaration)type.ast() );
        return (_T)this;
    }

    /**
     * Flatten all instances of the label included in this TYPE
     * @param labelName the NAME of the label to flatten
     * @return
     */
    default _T flattenLabel(String labelName ){
        Ast.flattenLabel( this.ast(), labelName);
        return (_T) this;
    }

    /**
     * apply a function to matching inner types of this TYPE
     * @param _typeMatchFn
     * @param _typeActionFn
     * @return the modified _type
     */
    default _T forInnerTypes(Predicate<_type> _typeMatchFn, Consumer<_type> _typeActionFn ){
        listInnerTypes(_typeMatchFn).forEach( _typeActionFn );
        return (_T)this;
    }

    /**
     * apply a function to NESTS of this _type
     * @param _typeActionFn
     * @return the modified _type
     */
    default _T forInnerTypes(Consumer<_type> _typeActionFn ){
        listInnerTypes().forEach( _typeActionFn );
        return (_T)this;
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
        listInnerTypes(t-> t.equals(innerTypeToRemove) ).forEach(n -> n.ast().removeForced() );
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
        NodeList<BodyDeclaration<?>> bds = ast().getMembers();
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
            return !listImplements().isEmpty();
        }
        
        default List<ClassOrInterfaceType> listImplements(){
            return ((NodeWithImplements)((_type)this).ast()).getImplementedTypes();
        }

        default _HI addImplements(_interface..._interfaces ){
            Arrays.stream(_interfaces).forEach(i-> addImplements(i.getFullName() ) );
            return (_HI)this;
        }        
        
        default _HI addImplements(ClassOrInterfaceType... toImplement ){
            NodeWithImplements nwi = ((NodeWithImplements)((_type)this).ast());
            Arrays.stream( toImplement ).forEach(i -> nwi.addImplementedType( i ) );
            return (_HI)this;
        }
        
        default _HI addImplements(Class... toImplement ){
            NodeWithImplements nwi = ((NodeWithImplements)((_type)this).ast());
            
            Arrays.stream( toImplement )
                .forEach(i -> {
                        ClassOrInterfaceType coit = (ClassOrInterfaceType)Ast.typeRef(i);                    
                        nwi.addImplementedType( coit );   
                        ((_type)this).addImports(i);
                    });
            return (_HI)this;
        }

        default _HI addImplements(String... toImplement ){
            NodeWithImplements nwi = ((NodeWithImplements)((_type)this).ast());
            Arrays.stream( toImplement ).forEach(i -> nwi.addImplementedType( i ) );
            return (_HI)this;
        }

        /**
         *
         * @param clazz
         * @return
         */
        _HI removeImplements(Class clazz );

        /**
         *
         * @param coit
         * @return
         */
        _HI removeImplements(ClassOrInterfaceType coit );

        /**
         * If you pass in a fully qualified name, this will remove ONLY this name i.e. "aaaa.bbbb.C"
         * if you pass in a simple name "C" this will remove ALL that match this
         * @param toRemove
         * @return
         */
        default _HI removeImplements(String toRemove){
            NodeWithImplements nwi = ((NodeWithImplements)((_type)this).ast());
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
            //Arrays.stream( toImplement ).forEach(i -> nwi.addImplementedType( i ) );
            return (_HI)this;
        }
    }

    /**
     * A TYPE that can extend (another {@link _class} or {@link _interface})
     * @param <_WE>
     */
    interface _withExtends<_WE extends _type & _withExtends> {

        boolean hasExtends();

        NodeList<ClassOrInterfaceType> listExtends();

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
}
