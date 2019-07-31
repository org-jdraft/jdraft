package org.jdraft;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.jdraft._anno.*;
import org.jdraft.io._in;
import org.jdraft.macro._macro;

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
        _constructor._hasConstructors<_enum, EnumDeclaration>, _staticBlock._hasStaticBlocks<_enum>,
        _type._hasImplements<_enum>{

    public static _enum of( Class<? extends Enum> clazz ){
        Node n = Ast.type( clazz );
        if( n instanceof CompilationUnit ){
            return _macro.to(clazz, of( (CompilationUnit)n));
        }
        _enum _e = of( (EnumDeclaration)n);
        Set<Class> importClasses = _import.inferImportsFrom(clazz);
        _e.imports(importClasses.toArray(new Class[0]));
        return _macro.to(clazz, _e);        
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
        throw new _jDraftException("Unable to locate EnumDeclaration in "+ cu);
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

    public static _enum of(String signature, Object anonymousBody, Function<_type,_type>... typeFns ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        _enum _e = _enum.of(signature);
        ObjectCreationExpr oce = Expr.anonymousObject( ste );
        if( oce.getAnonymousClassBody().isPresent()) {
            NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
            for(int i=0; i<bds.size(); i++) {
                _e.ast().addMember(bds.get(i));
            }
        }
        Set<Class> importClasses = _import.inferImportsFrom(anonymousBody);
        _e.imports(importClasses.toArray(new Class[0]));
        
        _e = _macro.to(anonymousBody.getClass(), _e);
        for(int i=0;i<typeFns.length; i++){
            _e = (_enum)typeFns[i].apply(_e);
        }
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
    
    /**
     * list all the members that match the predicate
     *
     * @param _memberMatchFn
     * @return matching members
     */
    @Override
    public List<_member> listMembers( Predicate<_member> _memberMatchFn ){
        return listMembers().stream().filter(_memberMatchFn).collect(Collectors.toList());
    }

    /**
     * lists all of the members that are of a specific member class
     * @param <M> the specific member class to find
     * @param memberClass the member class
     * @return the list of members
     */
    @Override
    public <M extends _member> List<M> listMembers( Class<M> memberClass ){
        List<M> found = new ArrayList<>();
        listMembers().stream().filter(m -> memberClass.isAssignableFrom(m.getClass()))
                .forEach(m -> found.add( (M)m) );
        return found;
    }
    
    /**
     * lists all of the members that are of a specific member class
     * @param <M> the specific member class to find
     * @param memberClass the member class
     * @param _memberMatchFn a matching function for selecting which members
     * @return the list of members
     */
    @Override
    public <M extends _member> List<M> listMembers( Class<M> memberClass, Predicate<M> _memberMatchFn){
        return listMembers(memberClass).stream().filter(_memberMatchFn).collect(Collectors.toList());        
    }
    
    @Override
    public _member getMember(Predicate<_member> memberMatchFn){
        List<_member> mems = listMembers(memberMatchFn);
        if( mems.isEmpty()){
            return null;
        }
        return mems.get(0);
    }
    
    @Override
    public <M extends _member> M getMember(Class<M> memberClass ){
        List<M> mems = listMembers(memberClass);
        if( mems.isEmpty()){
            return null;
        }
        return mems.get(0);
    }
    
    @Override
    public <M extends _member> M getMember(Class<M> memberClass, Predicate<M> memberMatchFn){
        List<M> mems = listMembers(memberClass, memberMatchFn);
        if( mems.isEmpty()){
            return null;
        }
        return mems.get(0);
    }
    
    @Override
    public <M extends _member> M getMember(Class<M> memberClass, String memberName){
        List<M> mems = listMembers(memberClass, m-> m.getName().equals(memberName));            
        if( mems.isEmpty()){
            return null;
        }
        return mems.get(0);
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
            throw new _jDraftException("cannot add Var without parent FieldDeclaration");
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
        return constant( Ast.constant( constantDecl ));
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
        ObjectCreationExpr oce = Expr.anonymousObject(ste);
        _constant _ct = _constant.of( Ast.constant(signature));
        if( oce.getAnonymousClassBody().isPresent()){
            // here, I'm putting the BODY into a temp _class, so that I can apply
            // annotation macros to it
            _class _c = _class.of("C");
            NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
            for(int i=0; i<bds.size();i++){
                _c.ast().addMember(bds.get(i));
            }
            //apply macros to the constant BODY (here stored in a class)
            _c = _macro.to(anonymousBody.getClass(), _c);
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
        if( ! Expr.equivalentAnnos(this.astEnum, other.astEnum)){
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
        Set<_staticBlock>tsb = new HashSet<>();
        Set<_staticBlock>osb = new HashSet<>();
        tsb.addAll(listStaticBlocks());
        osb.addAll(other.listStaticBlocks());
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
    public List<_member> listMembers(){
        List<_member> _mems = new ArrayList<>();
        forFields( f-> _mems.add( f));
        forMethods(m -> _mems.add(m));
        forConstructors(c -> _mems.add(c));
        forConstants(c-> _mems.add(c));
        forNests(n -> _mems.add(n));
        //forCompanionTypes(ct-> _mems.add(ct));
        return _mems;
    }

    @Override
    public _enum forMembers( Predicate<_member>_memberMatchFn, Consumer<_member> _memberActionFn){
        listMembers(_memberMatchFn).forEach(m -> _memberActionFn.accept(m) );
        return this;
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
    public Map<_java.Component, Object> componentsMap( ) {
        Map<_java.Component, Object> parts = new HashMap<>();
        parts.put( _java.Component.HEADER_COMMENT, this.getHeaderComment() );
        parts.put( _java.Component.PACKAGE_NAME, this.getPackage() );
        parts.put( _java.Component.IMPORTS, this.getImports().list() );
        parts.put( _java.Component.ANNOS, this.listAnnos() );
        parts.put( _java.Component.IMPLEMENTS, this.listImplements() );
        parts.put( _java.Component.JAVADOC, this.getJavadoc() );
        parts.put( _java.Component.CONSTANTS, this.listConstants());
        parts.put( _java.Component.STATIC_BLOCKS, this.listStaticBlocks());
        parts.put( _java.Component.NAME, this.getName() );
        parts.put( _java.Component.MODIFIERS, this.getModifiers() );
        parts.put( _java.Component.CONSTRUCTORS, this.listConstructors() );
        parts.put( _java.Component.METHODS, this.listMethods() );
        parts.put( _java.Component.FIELDS, this.listFields() );
        parts.put( _java.Component.NESTS, this.listNests() );
        parts.put( Component.COMPANION_TYPES, this.listCompanionTypes());
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

        Set<_staticBlock> sbs = new HashSet<>();
        sbs.addAll( this.listStaticBlocks() );

        hash = 53 * hash + Objects.hash( this.getPackage(),
                Expr.hashAnnos(astEnum),
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
    public static final class _constant implements _javadoc._hasJavadoc<_constant>,
            _anno._hasAnnos<_constant>,_method._hasMethods<_constant>, _field._hasFields<_constant>,
            _member<EnumConstantDeclaration, _constant> {

        public static _constant of( String... ecd ){
            return of(Ast.constant(ecd));
        }
        
        public static _constant of( EnumConstantDeclaration ecd ){
            return new _constant( ecd);
        }

        public _constant( EnumConstantDeclaration ecd ){
            this.astConstant = ecd;
        }

        public final EnumConstantDeclaration astConstant;

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
               return is(Ast.constant(stringRep));
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
            return addArgument( Expr.of(i) );
        }
        
        public _constant addArgument( boolean b){
            return addArgument( Expr.of(b) );
        }
        
        public _constant addArgument( float f){
            return addArgument( Expr.of(f) );
        }
        
        public _constant addArgument( long l){
            return addArgument( Expr.of(l) );
        }
        
        public _constant addArgument( double d){
            return addArgument( Expr.of(d) );
        }
        
        public _constant addArgument( char c){
            return addArgument( Expr.of(c) );
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
            return setArgument(index, Expr.of(b));
        }
        
        public _constant setArgument( int index, int i){
            return setArgument(index, Expr.of(i));
        }
        
        public _constant setArgument( int index, char c){
            return setArgument(index, Expr.of(c));
        }

        public _constant setArgument( int index, float f){
            return setArgument(index, Expr.of(f));
        }
        
        public _constant setArgument( int index, long l){
            return setArgument(index, Expr.of(l));
        }
        
        public _constant setArgument(int index, double d){
            return setArgument(index, Expr.of(d));
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
                throw new _jDraftException("cannot add Var without parent FieldDeclaration");
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
            if( !Expr.equivalentAnnos(this.astConstant, other.astConstant ) ){
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
        public Map<_java.Component, Object> componentsMap( ) {
            Map<_java.Component, Object> parts = new HashMap<>();
            parts.put( _java.Component.ANNOS, this.listAnnos() );
            parts.put( _java.Component.JAVADOC, this.getJavadoc() );
            parts.put( _java.Component.NAME, this.getName());
            parts.put( _java.Component.ARGUMENTS, this.listArguments());
            parts.put( _java.Component.NAME, this.getName() );
            parts.put( _java.Component.METHODS, this.listMethods() );
            parts.put( _java.Component.FIELDS, this.listFields() );
            return parts;
        }

        @Override
        public String toString(){
            return this.astConstant.toString();
        }
        
        @Override
        public String toString( PrettyPrinterConfiguration ppv ){
            return this.astConstant.toString(ppv);
        }
        
        @Override
        public int hashCode() {
            int hash = 7;
            Set<_method> tms = new HashSet<>();
            tms.addAll( this.listMethods());

            Set<_field> tfs = new HashSet<>();
            tfs.addAll( this.listFields());

            hash = 13 * hash + Objects.hash( tms, tfs, 
                    Expr.hashAnnos(astConstant),
                    getJavadoc(),
                    getName(), listArguments() );
            return hash;
        }        
    }
}
