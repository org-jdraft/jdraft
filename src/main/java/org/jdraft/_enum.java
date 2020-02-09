package org.jdraft;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.jdraft.io._in;
import org.jdraft.macro.macro;
import org.jdraft.text.Text;

import java.io.InputStream;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * Representation of the source code a Java enum.({@link EnumDeclaration})<BR>
 * i.e.<PRE>
 *     public enum Suit{
 *         HEART,CLUBS,SPADES,DIAMONDS;
 *     }
 * </PRE>
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
        _e.addImports(importClasses.toArray(new Class[0]));
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
        throw new _jdraftException("Unable to locate EnumDeclaration in "+ cu);
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

    /**
     * ---Mostly called internally & from $enum $pattern --
     *
     * @param signature the signature
     * @param anonymousBody
     * @param ste the stackTraceElement to refer back to the code used to build the enum
     * (we scrape the code from the calling source)
     * @return
     */
    public static _enum of( String signature, Object anonymousBody, StackTraceElement ste){
        _enum _e = _enum.of(signature);
        ObjectCreationExpr oce = Ex.newEx( ste );
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
        importClasses.remove(_constant.class);
        _e.addImports(importClasses.toArray(new Class[0]));

        _e = macro.to(anonymousBody.getClass(), _e);
        return _e;
    }

    /**
     * Allows the definition of the Enum AND _constants
     * <PRE>
     *  _enum _e = _enum.of("E", new Object(){
     *     _constant C; //raw
     *     _constant A,B;
     *     _constant D = new _constant();
     *     _constant E = new _constant(1);
     *     _constant F = new _constant(1, 3);
     *     _constant G = new _constant("A", 4){
     *          public String toString(){
     *              return
     *          }
     *      }
     *  });
     *  </PRE>
     * @param signature
     * @param anonymousBody
     * @return
     */
    public static _enum of(String signature, Object anonymousBody){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return of(signature, anonymousBody, ste);
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
    public _enum javadoc(String... content) {
        ((NodeWithJavadoc) this.ast()).setJavadocComment(Text.combine(content));
        return this;
    }

    @Override
    public _enum javadoc(JavadocComment astJavadocComment) {
        ((NodeWithJavadoc) this.ast()).setJavadocComment(astJavadocComment);
        return this;
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
            throw new _jdraftException("cannot add Var without parent FieldDeclaration");
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
        ObjectCreationExpr oce = Ex.newEx(ste);
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
}
