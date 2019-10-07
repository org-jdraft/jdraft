package org.jdraft;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.jdraft._anno.*;
import org.jdraft.io._in;
import org.jdraft.macro.macro;

import java.io.InputStream;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * Logical Mutable Model of the source code representing a Java enum.<BR>
 *
 * Implemented as a "Logical Facade" on top of an AST
 * ({@link EnumDeclaration}) for logical manipulation
 *
 * @author Eric
 */
public final class _enum implements _type<EnumDeclaration, _enum>,_method._hasMethods<_enum>,
        _constructor._hasConstructors<_enum, EnumDeclaration>, _initBlock._hasInitBlocks<_enum>,
        _type._hasImplements<_enum>{

    public static _enum of( Class<? extends Enum> clazz ){
        Node n = Ast.typeDecl( clazz );
        if( n instanceof CompilationUnit ){
            return macro.to(clazz, of( (CompilationUnit)n));
        }
        _enum _e = of( (EnumDeclaration)n);
        Set<Class> importClasses = _import.inferImportsFrom(clazz);
        _e.imports(importClasses.toArray(new Class[0]));
        return macro.to(clazz, _e);
    }

    public static _enum of( String...classDef ){
        if( classDef.length == 1){
            String[] strs = classDef[0].split(" ");
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
                    return of( Ast.of( "package "+packageName+";"+System.lineSeparator()+
                            "public enum "+shortcutClass));
                }
                if(!shortcutClass.endsWith("}")){
                    shortcutClass = shortcutClass + "{}";
                }
                return of( Ast.of("public enum "+shortcutClass));
            }
        }
        String cc = Text.combine(classDef);
        int enumIndex = cc.indexOf(" enum ");
        int privateIndex = cc.indexOf( "private ");
        if( privateIndex >= 0 && privateIndex < enumIndex ){
            cc = cc.substring(0, privateIndex)+ cc.substring(privateIndex + "private ".length());
            _enum _i = of( Ast.of( cc ));
            _i.setPrivate();
            return _i;
        }
        return of( Ast.of( classDef ));
    }

    public static _enum of( CompilationUnit cu ){
        if( cu.getPrimaryTypeName().isPresent() ){
            return of(cu.getEnumByName( cu.getPrimaryTypeName().get() ).get() );
        }
        NodeList<TypeDeclaration<?>> tds = cu.getTypes();
        if( tds.size() == 1 ){
            return of( (EnumDeclaration)tds.get(0) );
        }
        throw new _draftException("Unable to locate EnumDeclaration in "+ cu);
    }

    public static _enum of( EnumDeclaration astClass ){
        return new _enum( astClass );
    }

    public static _enum of(InputStream is){
        return of( StaticJavaParser.parse(is) );
    }

    public static _enum of( _in in ){
        return of( in.getInputStream());
    }

    //
    public static _enum of(String signature, Object anonymousBody){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        _enum _e = _enum.of(signature);
        ObjectCreationExpr oce = Ex.anonymousObjectEx( ste );
        if( oce.getAnonymousClassBody().isPresent()) {
            NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
            for(int i=0; i<bds.size(); i++) {
                if(bds.get(i) instanceof FieldDeclaration){
                    //System.out.println( "FIELD "+ bds.get(i));
                    //check if it's an _enum.constant
                    /*
                    _enum _e = _enum.of("E", new Object(){
                        _constant C; //
                        _constant A,B;
                        _constant D = new _constant();
                        _constant E = new _constant(1);
                        _constant F = new _constant(1, 3);
                        _constant G = new _constant("A", 4){

                        }
                    });
                     */
                    FieldDeclaration fd = (FieldDeclaration)bds.get(i);

                    if( Ast.typesEqual( fd.getVariable(0).getType(), Ast.typeRef(_constant.class) ) ){

                        for(int f=0;f<fd.getVariables().size();f++){
                            VariableDeclarator vd = fd.getVariable(f);
                            EnumConstantDeclaration ecd = new EnumConstantDeclaration();
                            ecd.setName(vd.getNameAsString());
                            if(vd.getInitializer().isPresent()){
                                Expression init = vd.getInitializer().get();
                                if( init.isObjectCreationExpr() ){
                                    //add arguments A(1, "name")
                                    ObjectCreationExpr foce = (ObjectCreationExpr) init;
                                    foce.getArguments().forEach( fa -> ecd.addArgument(fa));
                                    //add body fields and methods A(){...}
                                    if( foce.getAnonymousClassBody().isPresent()){
                                        foce.getAnonymousClassBody().get().forEach(e-> ecd.getClassBody().add(e));
                                    }
                                }
                            }
                            if( ((FieldDeclaration)vd.getParentNode().get()).getJavadoc().isPresent() ){
                                ecd.setJavadocComment( ((FieldDeclaration)vd.getParentNode().get()).getJavadoc().get() );
                            }
                            _e.constant(ecd); //add the constant
                        }
                    } else{
                        _e.ast().addMember(fd);
                    }
                } else {
                    _e.ast().addMember(bds.get(i));
                }
            }
        }
        Set<Class> importClasses = _import.inferImportsFrom(anonymousBody);
        importClasses.remove(_enum._constant.class);
        _e.imports(importClasses.toArray(new Class[0]));
        
        _e = macro.to(anonymousBody.getClass(), _e);
        return _e;
    }

    public _enum( EnumDeclaration astClass ){
        this.astEnum = astClass;
    }

    /**
     * the AST storing the state of the _class
     * the _class is simply a facade into the state astClass
     *
     * the _class facade is a "Logical View" of the _class state stored in the
     * AST and can interpret or manipulate the AST without:
     * having to deal with syntax issues
     */
    private final EnumDeclaration astEnum;

    @Override
    public EnumDeclaration ast(){
        return this.astEnum;
    }
    
    @Override
    public boolean isTopLevel(){
        return astEnum.isTopLevelType();
    }

    @Override
    public boolean is(String...stringRep){
        try{
            return equals(of(Ast.of(stringRep)));
        } catch(Exception e){
            return false;
        }
    }
    
    @Override
    public boolean is(EnumDeclaration ed){
        return of(ed).equals(this);
    }
    
    @Override
    public CompilationUnit astCompilationUnit(){
        //it might be a member class
        if( this.astEnum.findCompilationUnit().isPresent()){
            return this.astEnum.findCompilationUnit().get();
        }
        return null; //its an orphan
    }

    @Override
    public _annos getAnnos() {
        return _annos.of(this.astEnum );
    }
   
    @Override
    public List<_method> listMethods() {
        List<_method> _ms = new ArrayList<>();
        astEnum.getMethods().forEach( m-> _ms.add(_method.of( m ) ) );
        return _ms;
    }

    @Override
    public List<_method> listMethods(Predicate<_method> _methodMatchFn ){
        List<_method> _ms = new ArrayList<>();
        astEnum.getMethods().forEach( m-> {
            _method _m = _method.of( m);
            if( _methodMatchFn.test(_m)){
                _ms.add(_m ); 
            }
        } );
        return _ms;
    }

    @Override
    public _enum method( MethodDeclaration method ) {
        astEnum.addMember( method );
        return this;
    }

    @Override
    public List<_constructor> listConstructors() {
        List<_constructor> _cs = new ArrayList<>();
        astEnum.getConstructors().forEach( c-> _cs.add( _constructor.of(c) ));
        return _cs;
    }

    @Override
    public _constructor getConstructor(int index){
        return _constructor.of(astEnum.getConstructors().get( index ));
    }

    @Override
    public _enum constructor( ConstructorDeclaration constructor ) {
        constructor.setName(this.getName()); //set the constructor NAME
        constructor.setPrivate(true);
        constructor.setPublic(false);
        constructor.setProtected(false);
        this.astEnum.addMember( constructor );
        return this;
    }

    public _enum constants( String...onePerConstant ){
        Arrays.stream(onePerConstant).forEach( c-> constant(c) );
        return this;
    }

    public List<_constant> listConstants() {
        List<_constant> _cs = new ArrayList<>();
        astEnum.getEntries().forEach( c-> _cs.add( _constant.of(c) ));
        return _cs;
    }

    public List<_constant> listConstants( Predicate<_constant> constantMatchFn ){
        return listConstants().stream().filter( constantMatchFn ).collect( Collectors.toList());
    }

    public _enum forConstants( Consumer<_constant> constantConsumer ){
        listConstants().forEach( constantConsumer );
        return this;
    }

    public _enum forConstants( Predicate<_constant>constantMatchFn, Consumer<_constant> constantConsumer ){
        listConstants( constantMatchFn).forEach( constantConsumer );
        return this;
    }

    public _constant getConstant(Predicate<_constant> constantMatchFn){

        List<_constant> _ct = listConstants(constantMatchFn );
        if( _ct.isEmpty() ){
            return null;
        }
        return _ct.get(0);
    }

    public _constant getConstant(String name){
        Optional<EnumConstantDeclaration> ed =
                astEnum.getEntries().stream().filter( e-> e.getNameAsString().equals( name ) ).findFirst();
        if( ed.isPresent()){
            return _constant.of(ed.get());
        }
        return null;
    }

    @Override
    public _enum field( VariableDeclarator field ) {
        if(! field.getParentNode().isPresent()){
            throw new _draftException("cannot add Var without parent FieldDeclaration");
        }
        FieldDeclaration fd = (FieldDeclaration)field.getParentNode().get();
        //we already added it to the parent
        if( this.astEnum.getFields().contains( fd ) ){
            if( !fd.containsWithinRange( field ) ){
                fd.addVariable( field );
            }
            return this;
        }
        this.astEnum.addMember( fd );
        return this;
    }

    public _enum constant( _constant _c ){
        this.astEnum.addEntry( _c.ast() );
        return this;
    }
    
    public _enum constant ( String...constantDecl ) {
        return constant( Ast.constantDecl( constantDecl ));
    }

    /**
     * Build a constant with the signature and add an anonymous BODY
     * (And process any Annotation Macros that are within the anonymous BODY)
     * <PRE>
     *  _enum.of("E", new Object() { @_final int i; }, _autoConstructor.$)
     *         .constant("G(2)", new Object(){
     *                     @_final int ff =100;
     *                     public String toString(){
     *                         return ff+"";
     *                     }
     *                 });
     *  //will create
     *  public enum E{
     *      G(2){
     *          final int ff = 100;
     *
     *          public String toString(){
     *              return ff+"";
     *          }
     *      }
     *  }
     * </PRE>
     * @param signature
     * @param anonymousBody
     * @return
     */
    public _enum constant (String signature, Object anonymousBody ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Ex.anonymousObjectEx(ste);
        _constant _ct = _constant.of( Ast.constantDecl(signature));
        if( oce.getAnonymousClassBody().isPresent()){
            // here, I'm putting the BODY into a temp _class, so that I can apply
            // annotation macros to it
            _class _c = _class.of("C");
            NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
            for(int i=0; i<bds.size();i++){
                _c.ast().addMember(bds.get(i));
            }
            //apply macros to the constant BODY (here stored in a class)
            _c = macro.to(anonymousBody.getClass(), _c);
            //the potentially modified BODY members are added
            _ct.ast().setClassBody(_c.ast().getMembers());
        }
        return constant(_ct.ast());
    }

    public _enum constant ( EnumConstantDeclaration constant ) {
        this.astEnum.addEntry( constant );
        return this;
    }

    @Override
    public String toString(){
        if( this.ast().isTopLevelType() ){
            return this.astCompilationUnit().toString();            
        }
        return this.astEnum.toString();
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
        final _enum other = (_enum)obj;

        if( this.astEnum == other.astEnum ){
            return true; //two _enum instances pointing to the same EnumDeclaration instance
        }
        if( !Objects.equals( this.getPackage(), other.getPackage() ) ) {
            return false;
        }
        if( ! Ex.equivalentAnnos(this.astEnum, other.astEnum)){
            return false;
        }     
        if( !Objects.equals( this.getJavadoc(), other.getJavadoc() ) ) {
            return false;
        }
        if( !Ast.modifiersEqual(astEnum, other.astEnum)){
            return false;
        }
        if( !Objects.equals( this.getName(), other.getName() ) ) {
            return false;
        }
        Set<_initBlock>tsb = new HashSet<>();
        Set<_initBlock>osb = new HashSet<>();
        tsb.addAll(listInitBlocks());
        osb.addAll(other.listInitBlocks());
        if( !Objects.equals( tsb, osb ) ) {
            return false;
        }

        if( !Ast.typesEqual( this.listImplements(), other.listImplements())){
            return false;
        }

        if( ! Ast.importsEqual( astEnum, other.astEnum )){
            return false;
        }
        Set<_constant> tc = new HashSet<>();
        Set<_constant> oc = new HashSet<>();
        tc.addAll( this.listConstants() );
        oc.addAll( other.listConstants() );

        if( !Objects.equals( tc, oc ) ) {
            return false;
        }

        Set<_constructor> tct = new HashSet<>();
        Set<_constructor> oct = new HashSet<>();
        tct.addAll( this.listConstructors() );
        oct.addAll( other.listConstructors() );

        if( !Objects.equals( tct, oct ) ) {
            return false;
        }

        Set<_field> tf = new HashSet<>();
        Set<_field> of = new HashSet<>();
        tf.addAll( this.listFields() );
        of.addAll( other.listFields() );

        if( !Objects.equals( tf, of ) ) {
            return false;
        }

        Set<_method> tm = new HashSet<>();
        Set<_method> om = new HashSet<>();
        tm.addAll( this.listMethods() );
        om.addAll( other.listMethods() );

        if( !Objects.equals( tm, om ) ) {
            return false;
        }

        Set<_type> tn = new HashSet<>();
        Set<_type> on = new HashSet<>();
        tn.addAll( this.listNests() );
        on.addAll( other.listNests() );

        if( !Objects.equals( tn, on ) ) {
            return false;
        }

        Set<_type> tcc = new HashSet<>();
        Set<_type> occ = new HashSet<>();
        tcc.addAll( this.listCompanionTypes() );
        occ.addAll( other.listCompanionTypes() );

        if( !Objects.equals( tcc, occ ) ) {
            return false;
        }
        return true;
    }

    @Override
    public _enum removeImplements( ClassOrInterfaceType toRemove ){
        this.astEnum.getImplementedTypes().remove( toRemove );
        return this;
    }

    @Override
    public _enum removeImplements( Class toRemove ){
        this.astEnum.getImplementedTypes().removeIf( im -> im.getNameAsString().equals( toRemove.getSimpleName() ) ||
                im.getNameAsString().equals(toRemove.getCanonicalName()) );
        return this;
    }

    public _enum removeConstant( _constant _c ){
        forConstants( c -> {
            if( c.equals( _c) ){                
                c.ast().remove();
            }
        });        
        return this;
    }

    public _enum removeConstant( String name ){
        _constant c = getConstant(name);
        if( c != null ){
            removeConstant( c );
        }
        return this;
    }
    
    /**
     * Remove all constants that match the constantMatchFn
     * @param _constantMatchFn
     * @return  the modified _enum
     */
    public _enum removeConstants( Predicate<_constant> _constantMatchFn ){
        listConstants(_constantMatchFn).forEach(c -> removeConstant(c) );
        return this;
    }

    @Override
    public Map<_java.Component, Object> components( ) {
        Map<_java.Component, Object> parts = new HashMap<>();
        parts.put( _java.Component.HEADER_COMMENT, this.getHeaderComment() );
        parts.put( _java.Component.PACKAGE, this.getPackage() );
        parts.put( _java.Component.IMPORTS, this.getImports().list() );
        parts.put( _java.Component.ANNOS, this.listAnnos() );
        parts.put( _java.Component.IMPLEMENTS, this.listImplements() );
        parts.put( _java.Component.JAVADOC, this.getJavadoc() );
        parts.put( _java.Component.CONSTANTS, this.listConstants());
        parts.put( _java.Component.INIT_BLOCKS, this.listInitBlocks());
        parts.put( _java.Component.NAME, this.getName() );
        parts.put( _java.Component.MODIFIERS, this.getModifiers() );
        parts.put( _java.Component.CONSTRUCTORS, this.listConstructors() );
        parts.put( _java.Component.METHODS, this.listMethods() );
        parts.put( _java.Component.FIELDS, this.listFields() );
        parts.put( _java.Component.NESTS, this.listNests() );
        parts.put( _java.Component.COMPANION_TYPES, this.listCompanionTypes());
        return parts;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        Set<_constant> tc = new HashSet<>();
        tc.addAll( this.listConstants() );

        Set<_constructor> tct = new HashSet<>();
        tct.addAll( this.listConstructors() );

        Set<_field> tf = new HashSet<>();
        tf.addAll( this.listFields() );

        Set<_method> tm = new HashSet<>();
        tm.addAll( this.listMethods() );

        Set<_type> tn = new HashSet<>();
        tn.addAll( this.listNests() );

        Set<_type> ct = new HashSet<>();
        ct.addAll( this.listCompanionTypes());

        Set<_initBlock> sbs = new HashSet<>();
        sbs.addAll( this.listInitBlocks() );

        hash = 53 * hash + Objects.hash( this.getPackage(),
                Ex.hashAnnos(astEnum),
                this.getJavadoc(), 
                this.getEffectiveModifiers(),
                this.getName(), 
                sbs,
                Ast.importsHash( astEnum ),
                Ast.typesHashCode( astEnum.getImplementedTypes()),
                tc, tct, tf, tm, tn, ct);

        return hash;
    }

    /**
     * individual constant within an _enum
     * <PRE>
     * //A, and B are CONSTANTS of _enum E
     * enum E{
     *    A(),
     *    B('a');
     * }
     * </PRE>
     */
    public static class _constant implements _javadoc._hasJavadoc<_constant>,
            _anno._hasAnnos<_constant>,_method._hasMethods<_constant>, _field._hasFields<_constant>,
            _declared<EnumConstantDeclaration, _constant> {

        public static _constant of( String... ecd ){
            return of(Ast.constantDecl(ecd));
        }
        
        public static _constant of( EnumConstantDeclaration ecd ){
            return new _constant( ecd);
        }

        public _constant( EnumConstantDeclaration ecd ){
            this.astConstant = ecd;
        }

        /**
         * This is a "prototype" object, it exists as a temporary
         * "holder" to represent an enum constant
         * <PRE>
         *     _enum _e = _enum.of("E", new Object(){
         *         _constant Yes, No, MaybeSo;
         *     }
         *     //is equivalent to :
         *     public enum E{
         *         Yes, No, MaybeSo;
         *     }
         * </PRE>
         * <PRE>
         *     //for example:
         *     _enum _e = _enum.of("E", new Object(){
         *         _constant C;
         *         _constant F = new _constant("A");    //prototype constructor (to pass args in)
         *         _constant G = new _constant("A", 1);
         *         _constant H = new _constant("B",2){ //prototype constructor (args & body w field(s) and method(s))
         *             int field = 200;
         *             public String toString(){
         *                 return "Hello "+field;
         *             }
         *         }
         *     }
         *     //equivlanet to:
         *     enum E{
         *         C,
         *         F("A"),
         *         G("A",1),
         *         H("B",2){
         *             int field = 200;
         *             public String toString(){
         *                 return "Hello "+field;
         *             }
         *         }
         *     }
         *
         * </PRE>
         */
        public _constant(Object...constructorArgs){

            //throw new RuntimeException("This constant constructor only exists for 'prototypes', it does not actualy build a _constant");
        }

        public EnumConstantDeclaration astConstant;

        @Override
        public EnumConstantDeclaration ast(){
            return astConstant;
        }        
        
        @Override
        public _constant copy(){
            return of(this.astConstant.toString());
        }
        
        public boolean hasArguments(){
            return this.astConstant.getArguments().size() > 0;
        }

        @Override
        public boolean is(String...stringRep){
           try{
               return is(Ast.constantDecl(stringRep));
           }     
           catch(Exception e){
               return false;
           }
        }
        
        @Override
        public boolean is(EnumConstantDeclaration ecd ){
            return of(ecd).equals(this);
        }
        
        @Override
        public _constant name( String name ){
            this.astConstant.setName( name );
            return this;
        }

        public _constant addArgument( int i){
            return addArgument( Ex.of(i) );
        }
        
        public _constant addArgument( boolean b){
            return addArgument( Ex.of(b) );
        }
        
        public _constant addArgument( float f){
            return addArgument( Ex.of(f) );
        }
        
        public _constant addArgument( long l){
            return addArgument( Ex.of(l) );
        }
        
        public _constant addArgument( double d){
            return addArgument( Ex.of(d) );
        }
        
        public _constant addArgument( char c){
            return addArgument( Ex.of(c) );
        }
        
        public _constant addArgument( Expression e ){
            this.astConstant.addArgument( e );
            return this;
        }

        public _constant addArgument( String str ){
            this.astConstant.addArgument( str );
            return this;
        }

        public _constant clearArguments(){
            this.astConstant.getArguments().clear();
            return this;
        }

        public _constant setArgument( int index, Expression e){
            this.astConstant.getArguments().set( index, e );
            return this;
        }
        
        public _constant setArguments( NodeList<Expression> arguments ){
            this.astConstant.setArguments(arguments);
            return this;
        }
        
        public _constant setArguments( List<Expression> arguments ){
            NodeList<Expression> nles = new NodeList<>();
            nles.addAll(arguments);
            this.astConstant.setArguments(nles);
            return this;
        }
        
        public _constant setArgument( int index, boolean b){
            return setArgument(index, Ex.of(b));
        }
        
        public _constant setArgument( int index, int i){
            return setArgument(index, Ex.of(i));
        }
        
        public _constant setArgument( int index, char c){
            return setArgument(index, Ex.of(c));
        }

        public _constant setArgument( int index, float f){
            return setArgument(index, Ex.of(f));
        }
        
        public _constant setArgument( int index, long l){
            return setArgument(index, Ex.of(l));
        }
        
        public _constant setArgument(int index, double d){
            return setArgument(index, Ex.of(d));
        }
        
        public _constant forArguments( Consumer<Expression> expressionAction ){
            this.listArguments().forEach(expressionAction);
            return this;
        }
        
        public _constant forArguments( Predicate<Expression> expressionMatchFn, Consumer<Expression> expressionAction ){
            this.listArguments(expressionMatchFn).forEach(expressionAction);
            return this;
        }
        
        public _constant removeArgument( int index ){
            this.astConstant.getArguments().remove(index);
            return this;
        }
        
        public _constant removeArguments( Predicate<Expression> argumentMatchFn ){
            listArguments(argumentMatchFn).forEach(e -> e.removeForced() );
            return this;
        }
        
        public List<Expression> listArguments(){
            return this.astConstant.getArguments();
        }
        
        public List<Expression>listArguments(Predicate<Expression> expressionMatchFn){
            return this.astConstant.getArguments().stream().filter( expressionMatchFn ).collect(Collectors.toList() );
        }

        public Expression getArgument( int index ){
            return listArguments().get( index );
        }

        @Override
        public _annos getAnnos() {
            return _annos.of(this.astConstant );
        }

        @Override
        public List<_method> listMethods() {
            List<_method> ms = new ArrayList<>();
            this.astConstant.getClassBody().stream().filter(b -> b instanceof MethodDeclaration).forEach( m -> ms.add(_method.of( (MethodDeclaration)m )) );
            return ms;
        }
        
         @Override
        public List<_method> listMethods(Predicate<_method> _methodMatchFn ){
            return listMethods().stream().filter(_methodMatchFn).collect(Collectors.toList());            
        }
        

        @Override
        public _constant method( MethodDeclaration method ) {
            this.astConstant.getClassBody().add( method );
            return this;
        }

        @Override
        public _constant field( VariableDeclarator field ) {
            if(! field.getParentNode().isPresent()){
                throw new _draftException("cannot add Var without parent FieldDeclaration");
            }
            FieldDeclaration fd = (FieldDeclaration)field.getParentNode().get();
            //we already added it to the parent
            if( this.astConstant.getClassBody().contains( fd ) ){
                if( !fd.containsWithinRange( field ) ){
                    fd.addVariable( field );
                }
                return this;
            }
            this.astConstant.getClassBody().add( fd );
            return this;
        }

        @Override
        public List<_field> listFields() {
            List<_field> ms = new ArrayList<>();
            this.astConstant.getClassBody().stream().filter(b -> b instanceof FieldDeclaration)
                    .forEach( f->{
                        if( ((FieldDeclaration)f).getVariables().size() == 1 ){
                            ms.add(_field.of( ((FieldDeclaration)f).getVariable( 0 ) ) );
                        } else{
                            for(int i=0;i<((FieldDeclaration)f).getVariables().size();i++){
                                ms.add(_field.of( ((FieldDeclaration)f).getVariable( i ) ) );
                            }
                        }
                    });
            return ms;
        }

        @Override
        public String getName(){
            return this.astConstant.getNameAsString();
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
            final _constant other = (_constant)obj;
            if( this.astConstant == other.astConstant){
                return true; //two _constant pointing to the same AstEnumDeclaration
            }
            if( !Ex.equivalentAnnos(this.astConstant, other.astConstant ) ){
                  return false;  
            }
            if( !Objects.equals( this.getJavadoc(), other.getJavadoc() ) ) {
                return false;
            }
            if( !Objects.equals( this.getName(), other.getName() ) ) {
                return false;
            }
            if( !Objects.equals(this.listArguments(), other.listArguments() ) ) {
                return false;
            }
            Set<_method> tms = new HashSet<>();
            Set<_method> oms = new HashSet<>();
            tms.addAll( this.listMethods());
            oms.addAll( other.listMethods());

            if( !Objects.equals( tms, oms ) ) {
                return false;
            }
            Set<_field> tfs = new HashSet<>();
            Set<_field> ofs = new HashSet<>();
            tfs.addAll( this.listFields());
            ofs.addAll( other.listFields());
            if( !Objects.equals( tfs, ofs ) ) {
                return false;
            }
            return true;
        }

        @Override
        public Map<_java.Component, Object> components( ) {
            Map<_java.Component, Object> parts = new HashMap<>();
            parts.put( _java.Component.ANNOS, this.listAnnos() );
            parts.put( _java.Component.JAVADOC, this.getJavadoc() );
            parts.put( _java.Component.NAME, this.getName());
            parts.put( _java.Component.ARGUMENTS, this.listArguments());
            parts.put( _java.Component.METHODS, this.listMethods() );
            parts.put( _java.Component.FIELDS, this.listFields() );
            return parts;
        }

        @Override
        public String toString(){
            return this.astConstant.toString();
        }

        
        @Override
        public int hashCode() {
            int hash = 7;
            Set<_method> tms = new HashSet<>();
            tms.addAll( this.listMethods());

            Set<_field> tfs = new HashSet<>();
            tfs.addAll( this.listFields());

            hash = 13 * hash + Objects.hash( tms, tfs, 
                    Ex.hashAnnos(astConstant),
                    getJavadoc(),
                    getName(), listArguments() );
            return hash;
        }        
    }
}
