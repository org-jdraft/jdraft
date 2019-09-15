package org.jdraft;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.*;
import com.github.javaparser.ast.nodeTypes.*;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

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
 *   |    ----draft---------------- 
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
 * List<_byteCodeFile> _cfs = _adhoc.compile(_class.of("C").fields("String firstName, lastName;"));
 *
 * //draft, compile & load a new {@link Class} into a new {@link ClassLoader}:
 * _adhoc adhoc = _adhoc.of(_class.of("C").fields("String firstName, lastName;")).getClass("C");
 *
 * //draft / compile/ load a new {@link Class} into a new {@link ClassLoader}
 * //& create a new instance:
 * Object o = _adhoc.of( _class.of("C").fields("String firstName, lastName;")).instance("C");
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
 */
public interface _type<AST extends TypeDeclaration & NodeWithJavadoc & NodeWithModifiers & NodeWithAnnotations, _T extends _type>
    extends _javadoc._hasJavadoc<_T>, _anno._hasAnnos<_T>, _modifiers._hasModifiers<_T>,
        _field._hasFields<_T>, _member<AST, _T>, _code<_T>, _node<AST, _T>, _java {

    /**
     * 
     * @param _methodMatchFn function for matching appropriate methods
     * @return a list of matching methods
     */
    List<_method> listMethods(Predicate<_method> _methodMatchFn );

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
        throw new _draftException
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
        throw new _draftException
            ("cannot add companion Types to a Nested Type \""+this.getName()+"\"");
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
                    _ts.add(_java.type(t));
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
     * Gets the Primary Type according to the CompilationUnit
     * the "primary type" is the type associated with the file name)
     * 
     * NOTE: it is not always easy to define there does NOT have to be a primary 
     * type for a compilationUnit.  
     * 
     * If we can find the compilationUnit, the compilationUnit SHould be 
     * associated with a fileName (in the Storage field).  the primary type
     * does NOT have to be public, but must be using the same name as the 
     * fileName
     * 
     * @return the primary type (if it can be resolved) or null
     */
    default _type getPrimaryType(){
        if( this.isTopLevel() ){
            if (this.astCompilationUnit().getPrimaryType().isPresent()) {
                return _java.type( this.astCompilationUnit().getPrimaryType().get() );
            }            
            Optional<TypeDeclaration<?>> ot = 
                this.astCompilationUnit().getTypes().stream().filter(t -> t.isPublic() ).findFirst();
            if( ot.isPresent() ){
                return _java.type(ot.get());
            }
        }        
        return null;
    }
     
    /**
     * find members that are of the specific class and perform the _memberAction on them
     * @param <_M>
     * @param memberClass the member Class (i.e. _field, _method, _constructor)
     * @param _memberAction the Action function to apply to candidates
     * @return the modified T
     */
    default <_M extends _member> _T forMembers(Class<_M> memberClass, Consumer<_M> _memberAction){
        listMembers(memberClass).forEach(_memberAction);
        return (_T)this;
    }

    /**
     * perform some action on the code if the _type extends a 
     * @param clazz specific class to test for extension
     * @param extendsTypeAction the action to perform if the type extends
     * @return true if the action was taken, false otherwise
     */
    default boolean ifExtends( Class clazz, Consumer<_hasExtends> extendsTypeAction){
        if( this instanceof _class && ((_class)this).isExtends(clazz) ){
            extendsTypeAction.accept((_class)this);            
            return true;
        }
        if( this instanceof _interface && ((_interface)this).isExtends(clazz) ){
            extendsTypeAction.accept((_interface)this);
            return true;
        }
        return false;
    }
    
    /**
     * Perform some action on the code if the code implements the specific class
     * @param clazz specific class to test for implementing
     * @param implementsTypeAction the action to perform if the type implements
     * @return true if the action was taken, false otherwise
     */
    default boolean ifImplements( Class clazz, Consumer<_hasImplements> implementsTypeAction ){
        if( this instanceof _class && ((_class)this).isImplements(clazz) ){
            implementsTypeAction.accept( ((_class)this) );
            return true;
        }       
        if( this instanceof _enum && ((_enum)this).isImplements(clazz) ){
            implementsTypeAction.accept( ((_enum)this) );
            return true;
        }
        return false;
    }
    
    /**
     * find members that are of the specific class and perform the _memberAction on them
     * @param <_M>
     * @param memberClass the member Class (i.e. _field, _method, _constructor)
     * @param _memberMatchFn the matching function for selecting which members to take action on
     * @param _memberAction the Action function to apply to candidates
     * @return the modified T
     */
    default <_M extends _member> _T forMembers(Class<_M> memberClass, Predicate<_M> _memberMatchFn, Consumer<_M> _memberAction){
        listMembers(memberClass, _memberMatchFn).forEach(_memberAction);
        return (_T)this;
    }
    
    /**
     * Iterate & apply the action function to all Members that satisfy the _memberMatchFn
     * @param _memberMatchFn function for selecting which members to apply the _memberActionFn
     * @param _memberAction the action to apply to all selected members that satisfy the _memberMatchFn
     * @return the modified T type
     */
    _T forMembers(Predicate<_member> _memberMatchFn, Consumer<_member> _memberAction );

    /**
     * add one or more _member(s) (_field, _method, _constructor, enum._constant, etc.) to the BODY of the _type
     * and return the modified _type
     * @param _members
     * @return
     */
    default _T add(_member..._members ){
        Arrays.stream(_members).forEach( _m -> {
            if(_m instanceof _field){
                this.ast().addMember( ((_field)_m).getFieldDeclaration() );
            } else{
                this.ast().addMember( (BodyDeclaration)_m.ast() );
            }
        }  );
        return (_T)this;
    }
    
    /**
     * Apply a single type Function to the type and return the modified _type
     * @param _typeFn the function on the type
     * @return the modified type

    default _T apply(Function<_type, _type> _typeFn ){
        return (_T)_typeFn.apply(this);
    }
    */
    
    /**
     * Apply each of the Macros and return the modified T
     * @param _typeFn all of the macros to execute
     * @return the modified T after applying the macros

    default _T apply(Function<_type, _type>..._typeFn ){
        for(int i=0; i < _typeFn.length; i++){
            _typeFn[i].apply(this);
        }
        return (_T)this;
    }
    */

    /**
     * List the members (_fields, _methods, _constructors,...) of the _type
     * @return
     */
    List<_member> listMembers();

    /**
     * List all _members matching the _memberMatchFn
     * @param _memberMatchFn
     * @return 
     */
    List<_member>listMembers(Predicate<_member> _memberMatchFn);
    
    /**
     * list all _members that are of the _memberClass
     * @param <_M> the type (i.e. _method.class, _field.class, _staticBlock.class)
     * @param memberClass the Class (i.e. _method.class, _field.class, _staticBlock.class)
     * @return a List of the types (empty if none found)
     */
    <_M extends _member> List<_M>listMembers(Class<_M> memberClass);
    
    /**
     * list all _members that are of the _memberClass
     * @param <_M> the type (i.e. _method.class, _field.class, _staticBlock.class)
     * @param memberClass the Class (i.e. _method.class, _field.class, _staticBlock.class)
     * @param memberMatchFn function for matching a specific member type
     * @return a List of the types (empty if none found)
     */
    <_M extends _member> List<_M> listMembers(Class<_M> memberClass, Predicate<_M> memberMatchFn);

    /**
     *
     * @param memberClass
     * @param memberMatchFn
     * @param <_M>
     * @return
     */
    default <_M extends _member> List<_M> removeMembers(Class<_M> memberClass, Predicate<_M> memberMatchFn){
        List<_M> ms = listMembers( memberClass, memberMatchFn);

        ms.forEach( m -> this.ast().remove(m.ast()) );

        return ms;
    }

    /**
     * Gets the first _member matching the _memberMatchFn
     * @param _memberMatchFn
     * @return the first member found (or null if none found)
     */
    _member getMember(Predicate<_member> _memberMatchFn);
    
    /**
     * Gets the first _member of the memberClass
     * @param <_M> the type (i.e. _method.class, _field.class, _staticBlock.class)
     * @param memberClass the Class (i.e. _method.class, _field.class, _staticBlock.class)
     * @return the first member found (null if none found)
     */
    <_M extends _member> _M getMember(Class<_M> memberClass);
    
    /**
     * Gets the first _member of the memberClass matching the _memberMatchFn
     * @param <_M> the type (i.e. _method.class, _field.class, _staticBlock.class)
     * @param memberClass the Class (i.e. _method.class, _field.class, _staticBlock.class)
     * @param memberMatchFn function for matching a specific member type
     * @return a List of the types (empty if none found)
     */
    <_M extends _member> _M getMember(Class<_M> memberClass, Predicate<_M> memberMatchFn);
    
    /**
     * Gets the first _member of the memberClass with the given name
     * i.e. <PRE>
     * _type _t = _class.of(SomeClass.class);
     * 
     * _t.getMember(_method.class, "m");//gets the method named "m" from _t
     * _t.getMember(_field.class, "ID");//gets the _field named "ID" from _t
     * </PRE>
     * @param <_M> the type (i.e. _method.class, _field.class, _staticBlock.class)
     * @param memberClass the Class (i.e. _method.class, _field.class, _staticBlock.class)
     * @param memberName the name of the member
     * @return a List of the types (empty if none found)
     */
    <_M extends _member> _M getMember(Class<_M> memberClass, String memberName);
    
    /**
     * Does this _type have a {@link PackageDeclaration} Node set?
     * @return true if the TYPE is a top level TYPE AND has a declared package
     */
    default boolean hasPackage(){
        return getPackage() != null;
    }

    /**
     * Builds the full NAME for the TYPE based on it's nest position within a class
     * (and its package)
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
     * Builds the full NAME for the TYPE based on it's nest position within a class
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
        } else {
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
    default _T name(String name ){
        if( name.contains(".") ){
            //setting the package AND type name all at once
            String packageName = name.substring(0, name.lastIndexOf(".") );
            String typeName = name.substring(name.lastIndexOf(".")+1);
            this.setPackage(packageName);
            return name(typeName);
        }
        ast().setName( name );
        if( this instanceof _class ){
            //make sure to rename the CONSTRUCTORS
            _class _c = (_class)this;
            _c.forConstructors( c-> c.name(name));
        }
        if( this instanceof _enum ){
            //make sure to rename the CONSTRUCTORS
            _enum _e = (_enum)this;
            _e.forConstructors( c-> c.name(name));
        }
        return (_T)this;
    }

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
        if( this instanceof _hasExtends ){
            NodeList<ClassOrInterfaceType> extnds = 
                ((NodeWithExtends)((_type)this).ast()).getExtendedTypes();
            return extnds.stream().filter(i -> Ast.typesEqual(i, astType)).findFirst().isPresent();
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
            return isExtends( (ClassOrInterfaceType)Ast.typeRef( baseType ) );
        } catch( Exception e){}
        
        if( baseType.contains(".") ){
            return isExtends (baseType.substring(baseType.lastIndexOf(".")+1 ) );
        }
        return false;
    }
    
    /**
     * does this type extend this specific class 
     * (Note: this does not handle generic base classes... for that use 
     * {@link #isExtends(com.github.javaparser.ast.type.ClassOrInterfaceType)}
     * @param clazz the class type
     * @return true if the type extends this (raw) type
     */
    default boolean isExtends( Class clazz ){
        try{
            return isExtends( (ClassOrInterfaceType)Ast.typeRef( clazz ) ) ||
                isExtends( (ClassOrInterfaceType)Ast.typeRef( clazz.getSimpleName() ) );
        }catch( Exception e){}
        return false;
    }
   
    /**
     * 
     * @param str
     * @return 
     */
    default boolean isImplements( String str ){
        try{
            return isImplements( (ClassOrInterfaceType)Ast.typeRef( str ) );
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
        return impls.stream().filter(i -> Ast.typesEqual(i, astType)).findFirst().isPresent();        
    }
        
    /**
     * does this class implement this (raw) interface
     * @param clazz the (raw...not generic) interface class
     * @return true 
     */
    default boolean isImplements( Class clazz ){        
        return isImplements( StaticJavaParser.parseClassOrInterfaceType(clazz.getCanonicalName()) ) 
                || isImplements( StaticJavaParser.parseClassOrInterfaceType(clazz.getSimpleName()) );        
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
        List<_type> _ts = _walk.list(this, _type.class);
        List<String>names = new ArrayList<>();
        _ts.forEach(t-> names.add(t.getFullName()));
        return names;
    }

    /**
     * Iterate & apply some functional action towards members of the type
     * @param _memberAction the action to apply to ALL members
     * @return the (modified) T type
     */
    default _T forMembers(Consumer<_member> _memberAction ){
        return forMembers( t-> true, _memberAction );
    }

    /**
     * returns a _type or a nested _type if
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
     * (with '.' or '$' notation for nested _type)
     * @return this _type a nested _type or null if not found
     */
    default _type getType(String name ){
        String fn = this.getFullName();
        if( name.startsWith( fn ) ){
            String left = name.substring( fn.length() );
            if( left.length() == 0 ){
                return this;
            }
            if( left.startsWith( "." ) ){
                return this.getNest( left.substring( 1 ) );
            }
            if( left.startsWith( "$") ){
                return this.getNest( left.substring( 1 ) );
            }
        }
        if( name.length() > 0 && name.startsWith(this.getName()) ){
            //maybe I want a nested
            String left = name.substring( this.getName().length() );
            if( left.length() == 0 ){
                return this;
            }
            if( left.startsWith( "." ) ){
                return this.getNest( left.substring( 1 ) );
            }
            if( left.startsWith( "$") ){
                return this.getNest( left.substring( 1 ) );
            }
        }
        return this.getNest( name );
    }

    /**
     * Does this TYPE have a main method?
     * @return
     */
    default boolean hasMain(){
        if( this instanceof _method._hasMethods){
            return ((_method._hasMethods)this).listMethods(_method.IS_MAIN).size() > 0;
        }
        return false;
    }

    /**
     * Ass a nested class to this _type
     * @param type the TYPE to add
     * @return
     */
    default _T nest(_type type ){
        //this.ast().addMember(type.ast());
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
     * apply a function to matching nested types of this TYPE
     * @param _typeMatchFn
     * @param _typeActionFn
     * @return the modified _type
     */
    default _T forNests(Predicate<_type> _typeMatchFn, Consumer<_type> _typeActionFn ){
        listNests(_typeMatchFn).forEach( _typeActionFn );
        return (_T)this;
    }

    /**
     * apply a function to NESTS of this _type
     * @param _typeActionFn
     * @return the modified _type
     */
    default _T forNests(Consumer<_type> _typeActionFn ){
        listNests().forEach( _typeActionFn );
        return (_T)this;
    }

    /**
     * Get the nested _class with this NAME defined within this _type (or null)
     * @param name
     * @return the nested _class or null if not found
     */
    default _class getNestedClass( String name){
        List<_type> ts = listNests( (t)-> t instanceof _class && t.getName().equals(name));
        if( ts.size() == 1 ){
            return (_class)ts.get(0);
        }
        return null;
    }

    /**
     * Gets the nested _interface with this NAME defined in this _type
     * @param name
     * @return the _interface or null if not found
     */
    default _interface getNestedInterface( String name){
        List<_type> ts = listNests( (t)-> t instanceof _interface && t.getName().equals(name));
        if( ts.size() == 1 ){
            return (_interface)ts.get(0);
        }
        return null;
    }

    /**
     * Gets the nested _enum with this NAME defined in this _type
     * @param name
     * @return the _enum or null if not found
     */
    default _enum getNestedEnum( String name){
        List<_type> ts = listNests( (t)-> t instanceof _enum && t.getName().equals(name));
        if( ts.size() == 1 ){
            return (_enum)ts.get(0);
        }
        return null;
    }

    /**
     * Gets the nested _annotation defined in this _type
     * @param name the NAME of the annotation
     * @return the _annotation (if found) or null
     */
    default _annotation getNestedAnnotation( String name){
        List<_type> ts = listNests( (t)-> t instanceof _annotation && t.getName().equals(name));
        if( ts.size() == 1 ){
            return (_annotation)ts.get(0);
        }
        return null;
    }

    /**
     * Gets the nested TYPE with the NAME
     * @param name
     * @return the nested TYPE with the NAME or null if not found
     */
    default _type getNest( String name ){
        List<_type> ts = listNests( t-> t.getName().equals(name));
        if( ts.size() == 1 ){
            return ts.get(0);
        }
        return null;
    }

    /**
     * 
     * @param nestToRemove
     * @return 
     */
    default <_NT extends _type> _T removeNest(_NT nestToRemove ){
        listNests( t-> t.equals(nestToRemove) ).forEach( n -> n.ast().removeForced() );
        return (_T) this;
    }
    
    /**
     * 
     * @param nestToRemove
     * @return 
     */
    default <_NT extends _type> _T removeNest(TypeDeclaration nestToRemove ){
        return removeNest( (_type)_java.type(nestToRemove ) );
    }
    
    /**
     * list all nested types that match this _typeMatchFn
     * @param typeMatchFn function to
     * @return matching nested types or empty list if none found
     */
    default List<_type> listNests( Predicate<? super _type> typeMatchFn ){
        return listNests().stream().filter( typeMatchFn ).collect(Collectors.toList());
    }

    /**
     *
     * @param _typeClass
     * @param _typeMatchFn
     * @param <_NT>
     * @return
     */
    default <_NT extends _type> List<_NT> listNests(Class<_NT> _typeClass, Predicate<_NT> _typeMatchFn){
        return (List<_NT>)listNests().stream()
                .filter(_t -> _typeClass.isAssignableFrom(_t.getClass()) && _typeMatchFn.test((_NT)_t) )
                .collect(Collectors.toList());
    }

    /**
     * list all nested children underneath this logical _type
     * (1-level, DIRECT CHILDREN, and NOT grand children or great grand children)
     * for a more comprehensive gathering of all types, call:
     *
     * @return the direct children (nested {@link _type}s) of this {@link _type}
     */
    default List<_type> listNests(){
        NodeList<BodyDeclaration> bds = ast().getMembers();
        List<BodyDeclaration> ts =
                bds.stream().filter( n-> n instanceof TypeDeclaration )
                    .collect(Collectors.toList());

        List<_type> nests = new ArrayList<>();
        ts.forEach( t-> {
            if( t instanceof ClassOrInterfaceDeclaration ){
                ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration)t;
                if( coid.isInterface() ){
                    nests.add( _interface.of( coid ));
                } else{
                    nests.add( _class.of( coid ));
                }
            }
            else if ( t instanceof EnumDeclaration ){
                nests.add( _enum.of( (EnumDeclaration)t));
            }
            else{
                nests.add( _annotation.of( (AnnotationDeclaration)t));
            }
        });
        return nests;
    }

    /**
     * {@link _type} that can implement an {@link _interface}
     * @param <_HI> the TYPE (either {@link _class}, or {@link _enum})
     */
    interface _hasImplements<_HI extends _type & _hasImplements> {

        default boolean hasImplements(){
            return !listImplements().isEmpty();
        }
        
        default List<ClassOrInterfaceType> listImplements(){
            return ((NodeWithImplements)((_type)this).ast()).getImplementedTypes();
        }

        default _HI implement(_interface..._interfaces ){
            Arrays.stream(_interfaces).forEach(i-> implement(i.getFullName() ) );
            return (_HI)this;
        }        
        
        default _HI implement(ClassOrInterfaceType... toImplement ){
            NodeWithImplements nwi = ((NodeWithImplements)((_type)this).ast());
            Arrays.stream( toImplement ).forEach(i -> nwi.addImplementedType( i ) );
            return (_HI)this;
        }
        
        default _HI implement(Class... toImplement ){
            NodeWithImplements nwi = ((NodeWithImplements)((_type)this).ast());
            
            Arrays.stream( toImplement )
                .forEach(i -> {
                        ClassOrInterfaceType coit = (ClassOrInterfaceType)Ast.typeRef(i);                    
                        nwi.addImplementedType( coit );   
                        ((_type)this).imports(i);
                    });
            return (_HI)this;
        }

        default _HI implement(String... toImplement ){
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
    }

    /**
     * A TYPE that can extend (another {@link _class} or {@link _interface})
     * @param <_HE>
     */
    interface _hasExtends<_HE extends _type & _hasExtends> {

        boolean hasExtends();

        NodeList<ClassOrInterfaceType> listExtends();

        _HE extend(ClassOrInterfaceType toExtend );

        default _HE extend(_class _c ){
            extend( _c.getFullName() );
            return (_HE)this;
        }

        default _HE extend(_interface _i ){
            extend( _i.getFullName() );
            return (_HE)this;
        }

        _HE extend(Class toExtend );

        _HE extend(String toExtend );

        _HE removeExtends(Class clazz);

        _HE removeExtends(ClassOrInterfaceType coit );
    }            
}
