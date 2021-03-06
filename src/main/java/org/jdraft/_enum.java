package org.jdraft;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
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
public final class _enum implements _type<EnumDeclaration, _enum>, _method._withMethods<_enum>,
        _constructor._withConstructors<_enum, EnumDeclaration>, _initBlock._withInitBlocks<_enum>,
        _type._withImplements<_enum> {

    public static final Function<String, _enum> PARSER = s-> _enum.of(s);

    public static _enum of(){
        return of( new EnumDeclaration());
    }

    public static _enum of( Path p) {
        return of(Ast.JAVAPARSER, p);
    }
    public static _enum of(JavaParser javaParser, Path p){
        return of(javaParser, _io.inFile(p));
    }

    public static _enum of(URL url) {
        return of(Ast.JAVAPARSER, url);
    }

    public static _enum of(JavaParser javaParser, URL url){
        try {
            InputStream inStream = url.openStream();
            return of(javaParser, inStream);
        }catch(IOException ioe){
            throw new _ioException("invalid input url \""+url.toString()+"\"", ioe);
        }
    }

    public static _enum of( Class<? extends Enum> clazz ) {
        return of(Ast.JAVAPARSER, clazz);
    }
    public static _enum of( JavaParser javaParser, Class<? extends Enum> clazz ){
        Node n = Ast.typeDeclaration(javaParser, clazz );
        if( n instanceof CompilationUnit ){
            return macro.to(clazz, of( (CompilationUnit)n));
        }
        _enum _e = of( (EnumDeclaration)n);
        Set<Class> importClasses = _import.inferImportsFrom(clazz);
        _e.addImports(importClasses.toArray(new Class[0]));
        return macro.to(clazz, _e);
    }

    public static _enum of(InputStream is) {
        return of(Ast.JAVAPARSER, is);
    }

    public static _enum of(JavaParser javaParser, InputStream is) {
        return of( Ast.of(javaParser, is) );
    }

    public static _enum of( _in in ) {
        return of(Ast.JAVAPARSER, in);
    }

    public static _enum of( JavaParser javaParser, _in in ) {
        return of( javaParser, in.getInputStream());
    }

    public static _enum of( String enumCode1, String enumCode2 ) {
        return of(Ast.JAVAPARSER, new String[]{enumCode1, enumCode2});
    }

    public static _enum of( String...enumCode ) {
        return of( Ast.JAVAPARSER, enumCode);
    }

    public static _enum of( JavaParser javaParser, String...classDef ){
        if( classDef.length == 0 ){
            return of();
        }
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
                    return of( Ast.of( javaParser,"package "+packageName+";"+System.lineSeparator()+
                            "public enum "+shortcutClass));
                }
                if(!shortcutClass.endsWith("}")){
                    shortcutClass = shortcutClass + "{}";
                }
                return of( Ast.of(javaParser,"public enum "+shortcutClass));
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
        return of( Ast.of( javaParser, classDef ));
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
        ObjectCreationExpr oce = Expr.newExpr( ste );
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
                    if( Types.equal( fd.getVariable(0).getType(), Types.of(_constant.class) ) ){

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
                            _e.addConstant(ecd); //add the constant
                        }
                    } else{
                        _e.node().addMember(fd);
                    }
                } else {
                    _e.node().addMember(bds.get(i));
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

    /** could be a single statement, or a block stmt */
    public static _feature._one<_enum, _imports> IMPORTS = new _feature._one<>(_enum.class, _imports.class,
            _feature._id.IMPORTS,
            a -> a.getImports(),
            (_enum a, _imports b) -> a.setImports(b), PARSER);

    public static _feature._one<_enum, _package> PACKAGE = new _feature._one<>(_enum.class, _package.class,
            _feature._id.PACKAGE,
            a -> a.getPackage(),
            (_enum a, _package b) -> a.setPackage(b), PARSER);

    public static _feature._one<_enum, _annos> ANNO_EXPRS = new _feature._one<>(_enum.class, _annos.class,
            _feature._id.ANNOS,
            a -> a.getAnnos(),
            (_enum a, _annos b) -> a.setAnnos(b), PARSER);

    public static _feature._one<_enum, _javadocComment> JAVADOC = new _feature._one<>(_enum.class, _javadocComment.class,
            _feature._id.JAVADOC,
            a -> a.getJavadoc(),
            (_enum a, _javadocComment b) -> a.setJavadoc(b), PARSER);

    public static _feature._one<_enum, _modifiers> MODIFIERS = new _feature._one<>(_enum.class, _modifiers.class,
            _feature._id.MODIFIERS,
            a -> a.getModifiers(),
            (_enum a, _modifiers b) -> a.setModifiers(b), PARSER);

    public static _feature._one<_enum, String> NAME = new _feature._one<>(_enum.class, String.class,
            _feature._id.NAME,
            a -> a.getName(),
            (_enum a, String s) -> a.setName(s), PARSER);

    public static _feature._many<_enum, _java._member> MEMBERS = new _feature._many<>(_enum.class, _java._member.class,
            _feature._id.MEMBERS,
            _feature._id.MEMBER,
            a -> a.listMembers(),
            (_enum a, List<_java._member>mems) -> a.setMembers(mems), PARSER, s-> _java._member.of(_enum.class, s))
            .featureImplementations(_constant.class, _constructor.class, _method.class, _field.class, /*inner type*/_class.class, _enum.class, _annotation.class, _interface.class)
            .setOrdered(false); /** the order of declarations doesnt matter mostly */

    /**
     * the order of the implements doesnt matter (i.e. enum A implements B, C === enum A implements C, B)
     */
    public static _feature._many<_enum, _typeRef> IMPLEMENTS = new _feature._many<>(_enum.class, _typeRef.class,
            _feature._id.IMPLEMENTS,
            _feature._id.IMPLEMENT,
            a -> a.listImplements(),
            (_enum a, List<_typeRef>mems) -> a.setImplements(mems), PARSER, s-> _typeRef.of(s))
            .setOrdered(false);

    public static _feature._features<_enum> FEATURES = _feature._features.of(_enum.class, PARSER,
            PACKAGE, IMPORTS, JAVADOC, ANNO_EXPRS, MODIFIERS, NAME, IMPLEMENTS, MEMBERS);

    public _enum( EnumDeclaration astClass ){
        this.node = astClass;
    }

    public _feature._features<_enum> features(){
        return FEATURES;
    }

    /**
     * the AST storing the state of the _class
     * the _class is simply a facade into the state astClass
     *
     * the _class facade is a "Logical View" of the _class state stored in the
     * AST and can interpret or manipulate the AST without:
     * having to deal with syntax issues
     */
    private EnumDeclaration node;

    @Override
    public EnumDeclaration node(){
        return this.node;
    }
    
    @Override
    public boolean isTopLevel(){
        return node.isTopLevelType();
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _enum replace(EnumDeclaration replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public _enum setJavadoc(String... content) {
        ((NodeWithJavadoc) this.node()).setJavadocComment(Text.combine(content));
        return this;
    }

    @Override
    public _enum setJavadoc(JavadocComment astJavadocComment) {
        ((NodeWithJavadoc) this.node()).setJavadocComment(astJavadocComment);
        return this;
    }

    @Override
    public _enum setFields(List<_field> fields) {
        this.node.getMembers().removeIf(m -> m instanceof FieldDeclaration );
        fields.forEach(f-> addField(f));
        return this;
    }

    @Override
    public CompilationUnit astCompilationUnit(){
        //it might be a member class
        if( this.node.findCompilationUnit().isPresent()){
            return this.node.findCompilationUnit().get();
        }
        return null; //its an orphan
    }

    @Override
    public _annos getAnnos() {
        return _annos.of(this.node);
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

    @Override
    public _enum addMethod(MethodDeclaration method ) {
        node.addMember( method );
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
    public _enum addConstructor(ConstructorDeclaration constructor ) {
        constructor.setName(this.getName()); //set the constructor NAME
        constructor.setPrivate(true);
        constructor.setPublic(false);
        constructor.setProtected(false);
        this.node.addMember( constructor );
        return this;
    }

    public _enum addConstants(String...onePerConstant ){
        Arrays.stream(onePerConstant).forEach( c-> addConstant(c) );
        return this;
    }

    public List<_constant> listConstants() {
        List<_constant> _cs = new ArrayList<>();
        node.getEntries().forEach(c-> _cs.add( _constant.of(c) ));
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
                node.getEntries().stream().filter(e-> e.getNameAsString().equals( name ) ).findFirst();
        if( ed.isPresent()){
            return _constant.of(ed.get());
        }
        return null;
    }

    @Override
    public _enum addField(VariableDeclarator field ) {
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

    public _enum addConstant(_constant _c ){
        this.node.addEntry( _c.node() );
        return this;
    }
    
    public _enum addConstant(String...constantDecl ) {
        return addConstant( Ast.constantDeclaration( constantDecl ));
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
    public _enum addConstant(String signature, Object anonymousBody ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expr.newExpr(ste);
        _constant _ct = _constant.of( Ast.constantDeclaration(signature));
        if( oce.getAnonymousClassBody().isPresent()){
            // here, I'm putting the BODY into a temp _class, so that I can apply
            // annotation macros to it
            _class _c = _class.of("C");
            NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
            for(int i=0; i<bds.size();i++){
                _c.node().addMember(bds.get(i));
            }
            //apply macros to the constant BODY (here stored in a class)
            _c = macro.to(anonymousBody.getClass(), _c);
            //the potentially modified BODY members are added
            _ct.node().setClassBody(_c.node().getMembers());
        }
        return addConstant(_ct.node());
    }

    public _enum addConstant(EnumConstantDeclaration constant ) {
        this.node.addEntry( constant );
        return this;
    }

    @Override
    public String toString(){
        if( this.node().isTopLevelType() ){
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
        final _enum other = (_enum)obj;

        if( this.node == other.node){
            return true; //two _enum instances pointing to the same EnumDeclaration instance
        }
        if( !Objects.equals( this.getPackage(), other.getPackage() ) ) {
            return false;
        }
        if( ! Expr.equalAnnos(this.node, other.node)){
            return false;
        }     
        if( !Objects.equals( this.getJavadoc(), other.getJavadoc() ) ) {
            return false;
        }
        if( !Modifiers.modifiersEqual(node, other.node)){
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

        if( !Types.equal( this.listAstImplements(), other.listAstImplements())){
            return false;
        }

        if( ! _imports.Compare.importsEqual(node, other.node)){
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
        tn.addAll( this.listInnerTypes() );
        on.addAll( other.listInnerTypes() );

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

    public _enum removeConstant( _constant _c ){
        forConstants( c -> {
            if( c.equals( _c) ){                
                c.node().remove();
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
        tn.addAll( this.listInnerTypes() );

        Set<_type> ct = new HashSet<>();
        //ct.addAll( this.listCompanionTypes());

        Set<_initBlock> sbs = new HashSet<>();
        sbs.addAll( this.listInitBlocks() );

        hash = 53 * hash + Objects.hash( this.getPackage(),
                Expr.hashAnnos(node),
                this.getJavadoc(), 
                this.getEffectiveAstModifiersList(),
                this.getName(), 
                sbs,
                _imports.Compare.importsHash(node),
                Types.hash( node.getImplementedTypes()),
                tc, tct, tf, tm, tn, ct);

        return hash;
    }
}
