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
        String all = Text.combine(code);

        return new _annoMember( Ast.annotationMemberDeclaration( all ) );
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

    protected AnnotationMemberDeclaration node;

    public _annoMember(AnnotationMemberDeclaration node){
        this.node = node;
    }

    public _feature._features<_annoMember> features(){
        return FEATURES;
    }

    @Override
    public _annoMember copy(){
        return of( this.node.toString() );
    }

    public _annoMember replace(AnnotationMemberDeclaration ae){
        this.node.replace(ae);
        this.node = ae;
        return this;
    }

    @Override
    public boolean is(AnnotationMemberDeclaration amd ){
        return _annoMember.of(amd).equals(this);
    }

    public Node getNameNode() { return this.node.getName(); }

    public _modifiers getModifiers(){
        return _modifiers.of( this.node);
    }

    public _annoMember setModifiers(_modifiers _ms){
        this.node.setModifiers( _ms.ast());
        return this;
    }

    @Override
    public _annoMember setName(String name){
        this.node.setName( name );
        return this;
    }

    @Override
    public AnnotationMemberDeclaration node(){
        return this.node;
    }

    @Override
    public _annoMember setType(Type t){
        this.node.setType( t );
        return this;
    }

    @Override
    public _annoMember setJavadoc(String... content) {
        ((NodeWithJavadoc) this.node()).setJavadocComment(Text.combine(content));
        return this;
    }

    @Override
    public _annoMember setJavadoc(JavadocComment astJavadocComment) {
        ((NodeWithJavadoc) this.node()).setJavadocComment(astJavadocComment);
        return this;
    }

    @Override
    public String getName(){
        return this.node.getNameAsString();
    }

    @Override
    public _typeRef getType(){
        return _typeRef.of(this.node.getType());
    }

    public boolean hasDefaultValue(){
        return this.node.getDefaultValue().isPresent();
    }

    public _annoMember removeDefaultValue(){
        this.node.removeDefaultValue();
        return this;
    }

    public _annoMember setDefaultValue(int intValue ){
        this.node.setDefaultValue( Expr.of( intValue ) );
        return this;
    }

    public _annoMember setDefaultValue(long longValue ){
        this.node.setDefaultValue( Expr.of( longValue ) );
        return this;
    }

    public _annoMember setDefaultValue(char charValue ){
        this.node.setDefaultValue( Expr.of( charValue ) );
        return this;
    }

    public _annoMember setDefaultValue(boolean booleanValue ){
        this.node.setDefaultValue( Expr.of( booleanValue ) );
        return this;
    }

    public _annoMember setDefaultValueNull(){
        this.node.setDefaultValue( Expr.nullExpr() );
        return this;
    }

    public _annoMember setDefaultValue(float floatValue ){
        this.node.setDefaultValue( Expr.of( floatValue ) );
        return this;
    }

    public _annoMember setDefaultValue(double doubleValue ){
        this.node.setDefaultValue( Expr.of( doubleValue ) );
        return this;
    }

    public _annoMember setDefaultValue(String defaultValueExpression){
        this.node.setDefaultValue( Ast.expression( defaultValueExpression) );
        return this;
    }

    public _annoMember setDefaultValue(_expr _e){
        return setDefaultValue( _e.node());
    }

    public _annoMember setDefaultValue(Expression e){
        this.node.setDefaultValue( e );
        return this;
    }

    public _expr getDefaultValue(){
        if( this.node.getDefaultValue().isPresent()){
            return _expr.of( this.node.getDefaultValue().get());
        }
        return null;
    }

    public Expression getDefaultAstValue(){
        if( this.node.getDefaultValue().isPresent()){
            return this.node.getDefaultValue().get();
        }
        return null;
    }

    @Override
    public _annos getAnnos() {
        return _annos.of(this.node);
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
        if( this.node == other.node){
            return true; //two _element instances pointing to same AstMemberDeclaration
        }
        if( ! Expr.equalAnnos(this.node, other.node)){
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
        if( !Types.equal( node.getType(), other.node.getType())){
            Log.trace("expected type %s got %s", node::getType, other.node::getType);
            return false;
        }
        if( !Objects.equals( this.getDefaultAstValue(), other.getDefaultAstValue() ) ) {
            Log.trace("expected name %s got %s", this::getDefaultAstValue, other::getDefaultAstValue);
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hash(
                Expr.hashAnnos(this.node),
                this.getJavadoc(),
                this.getName(),
                Types.hash(this.node.getType()),
                this.getDefaultAstValue() );
        return hash;
    }

    @Override
    public String toString(){
        return this.node.toString();
    }
}
