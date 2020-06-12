package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.utils.Log;
import org.jdraft.text.Text;

import java.util.Objects;
import java.util.function.Function;

/**
 * a property entry added to an annotation
 * <PRE>
 * // VALUE is an entry of the Speed annotation
 * public @interface Speed{
 *     int VALUE() default 0; //<--- this thing
 * }
 * </PRE>
 * NOTE: we called this an entry and NOT a member, because we use the
 * term "member" to be any member implementation (_field, _method, etc.)
 * of a type (as it is documented in Java), so we devised the term _entry
 * to mean (specifically) a property of an {@link _annotation}
 * (it is also a _member) and maps to an AnnotationMemberDeclaration
 */
public final class _annoMember implements _javadocComment._withJavadoc<_annoMember>,
        _java._withNameType<AnnotationMemberDeclaration, _annoMember>,
        _java._declared<AnnotationMemberDeclaration, _annoMember> {

    public static final Function<String, _annoMember> PARSER = s-> _annoMember.of(s);

    public static _annoMember of(AnnotationMemberDeclaration astEntry){
        return new _annoMember( astEntry );
    }

    public static _annoMember of(String...code ){
        return new _annoMember( Ast.annotationMemberDeclaration( code ) );
    }

    public static _annoMember of(Type type, String name ){
        AnnotationMemberDeclaration amd = new AnnotationMemberDeclaration();
        amd.setName(name);
        amd.setType(type);
        return of( amd );
    }

    public static _annoMember of(Type type, String name, Expression defaultValue ){
        AnnotationMemberDeclaration amd = new AnnotationMemberDeclaration();
        amd.setName(name);
        amd.setType(type);
        amd.setDefaultValue(defaultValue);
        return of( amd );
    }

    public static _feature._one<_annoMember, _modifiers> MODIFIERS = new _feature._one<>(_annoMember.class, _modifiers.class,
            _feature._id.MODIFIERS,
            a->a.getModifiers(),
            (_annoMember a, _modifiers _m)-> a.setModifiers(_m) , PARSER);

    public static _feature._one<_annoMember, _typeRef> TYPE = new _feature._one<>(_annoMember.class, _typeRef.class,
            _feature._id.TYPE,
            a->a.getType(),
            (_annoMember a, _typeRef _e)-> a.setType(_e) , PARSER);

    public static _feature._one<_annoMember, String> NAME = new _feature._one<>(_annoMember.class, String.class,
            _feature._id.NAME,
            a -> a.getName(),
            (_annoMember a, String name) -> a.setName(name), PARSER);

    public static _feature._one<_annoMember, _expr> DEFAULT = new _feature._one<>(_annoMember.class, _expr.class,
            _feature._id.DEFAULT,
            a->a.getDefaultValue(),
            (_annoMember a, _expr _e)-> a.setDefaultValue(_e), PARSER );

    public static _feature._features<_annoMember> FEATURES = _feature._features.of(_annoMember.class,  PARSER, MODIFIERS, TYPE, NAME, DEFAULT);

    protected final AnnotationMemberDeclaration astAnnMember;

    public _annoMember(AnnotationMemberDeclaration astAnnMember ){
        this.astAnnMember = astAnnMember;
    }

    public _feature._features<_annoMember> features(){
        return FEATURES;
    }

    @Override
    public _annoMember copy(){
        return of( this.astAnnMember.toString() );
    }

    /*
    @Override
    public boolean is(String...stringRep){
        return is(Ast.annotationMemberDeclaration(stringRep));
    }
     */

    @Override
    public boolean is(AnnotationMemberDeclaration amd ){
        return _annoMember.of(amd).equals(this);
    }

    public Node getNameNode() { return this.astAnnMember.getName(); }

    public _modifiers getModifiers(){
        return _modifiers.of( this.astAnnMember );
    }

    public _annoMember setModifiers(_modifiers _ms){
        this.astAnnMember.setModifiers( _ms.ast());
        return this;
    }

    @Override
    public _annoMember setName(String name){
        this.astAnnMember.setName( name );
        return this;
    }

    @Override
    public AnnotationMemberDeclaration ast(){
        return this.astAnnMember;
    }

    @Override
    public _annoMember setType(Type t){
        this.astAnnMember.setType( t );
        return this;
    }

    @Override
    public _annoMember setJavadoc(String... content) {
        ((NodeWithJavadoc) this.ast()).setJavadocComment(Text.combine(content));
        return this;
    }

    @Override
    public _annoMember setJavadoc(JavadocComment astJavadocComment) {
        ((NodeWithJavadoc) this.ast()).setJavadocComment(astJavadocComment);
        return this;
    }

    @Override
    public String getName(){
        return this.astAnnMember.getNameAsString();
    }

    @Override
    public _typeRef getType(){
        return _typeRef.of(this.astAnnMember.getType());
    }

    public boolean hasDefaultValue(){
        return this.astAnnMember.getDefaultValue().isPresent();
    }

    public _annoMember removeDefaultValue(){
        this.astAnnMember.removeDefaultValue();
        return this;
    }

    public _annoMember setDefaultValue(int intValue ){
        this.astAnnMember.setDefaultValue( Expr.of( intValue ) );
        return this;
    }

    public _annoMember setDefaultValue(long longValue ){
        this.astAnnMember.setDefaultValue( Expr.of( longValue ) );
        return this;
    }

    public _annoMember setDefaultValue(char charValue ){
        this.astAnnMember.setDefaultValue( Expr.of( charValue ) );
        return this;
    }

    public _annoMember setDefaultValue(boolean booleanValue ){
        this.astAnnMember.setDefaultValue( Expr.of( booleanValue ) );
        return this;
    }

    public _annoMember setDefaultValueNull(){
        this.astAnnMember.setDefaultValue( Expr.nullExpr() );
        return this;
    }

    public _annoMember setDefaultValue(float floatValue ){
        this.astAnnMember.setDefaultValue( Expr.of( floatValue ) );
        return this;
    }

    public _annoMember setDefaultValue(double doubleValue ){
        this.astAnnMember.setDefaultValue( Expr.of( doubleValue ) );
        return this;
    }

    public _annoMember setDefaultValue(String defaultValueExpression){
        this.astAnnMember.setDefaultValue( Ast.expression( defaultValueExpression) );
        return this;
    }

    public _annoMember setDefaultValue(_expr _e){
        return setDefaultValue( _e.ast());
    }

    public _annoMember setDefaultValue(Expression e){
        this.astAnnMember.setDefaultValue( e );
        return this;
    }

    public _expr getDefaultValue(){
        if( this.astAnnMember.getDefaultValue().isPresent()){
            return _expr.of( this.astAnnMember.getDefaultValue().get());
        }
        return null;
    }

    public Expression getDefaultAstValue(){
        if( this.astAnnMember.getDefaultValue().isPresent()){
            return this.astAnnMember.getDefaultValue().get();
        }
        return null;
    }

    @Override
    public _annos getAnnos() {
        return _annos.of(this.astAnnMember );
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
        final _annoMember other = (_annoMember)obj;
        if( this.astAnnMember == other.astAnnMember){
            return true; //two _element instances pointing to same AstMemberDeclaration
        }
        if( ! Expr.equalAnnos(this.astAnnMember, other.astAnnMember)){
            return false;
        }
        if( !Objects.equals( this.getJavadoc(), other.getJavadoc() ) ) {
            Log.trace("expected javadoc %s got %s", this::getJavadoc, other::getJavadoc);
            return false;
        }
        if( !Objects.equals( this.getName(), other.getName() ) ) {
            Log.trace("expected name %s got %s", this::getName, other::getName);
            return false;
        }
        if( !Types.equal( astAnnMember.getType(), other.astAnnMember.getType())){
            Log.trace("expected type %s got %s", astAnnMember::getType, other.astAnnMember::getType);
            return false;
        }
        if( !Objects.equals( this.getDefaultAstValue(), other.getDefaultAstValue() ) ) {
            Log.trace("expected name %s got %s", this::getDefaultAstValue, other::getDefaultAstValue);
            return false;
        }
        return true;
    }

    /*
    public Map<_java.Feature, Object> features( ) {
        Map<_java.Feature, Object> parts = new HashMap<>();
        parts.put( _java.Feature.ANNO_EXPRS, this.listAnnoExprs() );
        parts.put( _java.Feature.JAVADOC, this.getJavadoc() );
        parts.put( _java.Feature.NAME, this.getName() );
        parts.put( _java.Feature.TYPE, this.getTypeRef() );
        parts.put( _java.Feature.DEFAULT_EXPR, this.getDefaultAstValue() );
        return parts;
    }
     */

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hash(
                Expr.hashAnnos(this.astAnnMember),
                this.getJavadoc(),
                this.getName(),
                Types.hash(this.astAnnMember.getType()),
                this.getDefaultAstValue() );
        return hash;
    }

    @Override
    public String toString(){
        return this.astAnnMember.toString();
    }
}
