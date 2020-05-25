package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import org.jdraft.text.Text;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
public class _constant implements _java._declared<EnumConstantDeclaration, _constant>,
        _javadocComment._withJavadoc<_constant>,
        _annoExprs._withAnnoExprs<_constant>,
        _method._withMethods<_constant>,
        _field._withFields<_constant>,
        _args._withArgs<EnumConstantDeclaration, _constant> {

    public static _constant of(){
        return of( new EnumConstantDeclaration());
    }

    public static _constant of( String... ecd ){
        return of(Ast.constantDecl(ecd));
    }

    public static _constant of( EnumConstantDeclaration ecd ){
        return new _constant( ecd);
    }

    public static _feature._one<_constant, String> NAME = new _feature._one<>(_constant.class, String.class,
            _feature._id.NAME,
            a -> a.getName(),
            (_constant a, String o) -> a.setName(o));

    public static _feature._one<_constant, _args> ARGS = new _feature._one<>(_constant.class, _args.class,
            _feature._id.ARGS_EXPRS,
            a -> a.getArgs(),
            (_constant a, _args o) -> a.setArgs(o));

    public static _feature._meta<_constant> META = _feature._meta.of(_constant.class, NAME, ARGS);

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

    public boolean hasArgs(){
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
    public _constant setName(String name ){
        this.astConstant.setName( name );
        return this;
    }

    /**
     * Parses the String representing arguments
     * i.e.
     * <CODE>
     * _constant.of("A")
     *     .arguments("1, 'c'");
     * </CODE>
     * ...will create the constant A(1,'c')
     *
     * @param arguments
     * @return
     */
    public _constant setArgs(String ...arguments){
        String args = Text.combine(arguments);
        if( args.startsWith("(") && args.endsWith(")") ){
            args = args.substring(1, args.length() -1);
        }
        EnumDeclaration ed = (EnumDeclaration)Ast.typeDecl("enum E{ A("+args+"); }");
        NodeList<Expression> argsList = ed.getEntry(0).getArguments();
        argsList.forEach(a-> addArg(a));
        return this;
    }

    public _constant setBody(String...bodyCode){
        EnumDeclaration ed = (EnumDeclaration)Ast.typeDecl("enum E{ A{"+Text.combine(bodyCode)+" }; }");
        ed.getEntry(0).getClassBody().forEach(bd -> this.add(bd));
        return this;
    }

    /**
     * Allows you to add _method, and _field entities to the constant
     * @param _ds any declared (_method, _field) to the constant classBody
     * @return the modified _constant
     */
    public _constant add( _java._declared... _ds ){
        Arrays.stream(_ds).forEach(_d -> this.astConstant.getClassBody().add( (BodyDeclaration)_d.ast()) );
        return this;
    }

    /**
     * Add FieldDeclaration or MethodDeclarations to the classBody of this constant and return the modified constant
     *
     */
    public _constant add( BodyDeclaration... bds ){
        Arrays.stream(bds).forEach(bd -> this.astConstant.getClassBody().add(bd) );
        return this;
    }

    public _constant addArg(Expression e ){
        this.astConstant.addArgument( e );
        return this;
    }

    public _constant addArg(String str ){
        this.astConstant.addArgument( str );
        return this;
    }

    public _constant setArgs(NodeList<Expression> arguments ){
        this.astConstant.setArguments(arguments);
        return this;
    }

    public _constant setArgs(List<Expression> arguments ){
        NodeList<Expression> nles = new NodeList<>();
        nles.addAll(arguments);
        this.astConstant.setArguments(nles);
        return this;
    }

    @Override
    public _annoExprs getAnnoExprs() {
        return _annoExprs.of(this.astConstant );
    }


    @Override
    public _constant setJavadoc(String... content) {
        ((NodeWithJavadoc) this.ast()).setJavadocComment(Text.combine(content));
        return this;
    }

    @Override
    public _constant setJavadoc(JavadocComment astJavadocComment) {
        ((NodeWithJavadoc) this.ast()).setJavadocComment(astJavadocComment);
        return this;
    }

    @Override
    public List<_method> listMethods() {
        List<_method> ms = new ArrayList<>();
        this.astConstant.getClassBody().stream().filter(b -> b instanceof MethodDeclaration).forEach(m -> ms.add(_method.of( (MethodDeclaration)m )) );
        return ms;
    }

    @Override
    public List<_method> listMethods(Predicate<_method> _methodMatchFn ){
        return listMethods().stream().filter(_methodMatchFn).collect(Collectors.toList());
    }


    @Override
    public _constant addMethod(MethodDeclaration method ) {
        this.astConstant.getClassBody().add( method );
        return this;
    }

    public _constant addField(FieldDeclaration field ) {
        this.astConstant.getClassBody().add( field );
        return this;
    }

    @Override
    public _constant addField(VariableDeclarator field ) {
        if(! field.getParentNode().isPresent()){
            throw new _jdraftException("cannot add Var without parent FieldDeclaration "+ field);
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
    public _constant setFields(List<_field> fields) {
        this.removeFields(t->true);
        fields.forEach(f-> addField(f));
        return this;
    }

    public SimpleName getNameNode() { return this.astConstant.getName(); }

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
        if( !Exprs.equalAnnos(this.astConstant, other.astConstant ) ){
            return false;
        }
        if( !Objects.equals( this.getJavadoc(), other.getJavadoc() ) ) {
            return false;
        }
        if( !Objects.equals( this.getName(), other.getName() ) ) {
            return false;
        }
        if( !Objects.equals(this.listArgs(), other.listArgs() ) ) {
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

    public Map<_java.Feature, Object> features( ) {
        Map<_java.Feature, Object> parts = new HashMap<>();
        parts.put( _java.Feature.ANNO_EXPRS, this.listAnnoExprs() );
        parts.put( _java.Feature.JAVADOC, this.getJavadoc() );
        parts.put( _java.Feature.NAME, this.getName());
        parts.put( _java.Feature.ARGS_EXPRS, this.listArgs());
        parts.put( _java.Feature.METHODS, this.listMethods() );
        parts.put( _java.Feature.FIELDS, this.listFields() );
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
                Exprs.hashAnnos(astConstant),
                getJavadoc(),
                getName(), listArgs() );
        return hash;
    }
}