package org.jdraft.proto;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import org.jdraft.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Note... at the moment this is NOT a template... should it be??
 */
public final class $annotationElement
        implements $proto<_annotation._element, $annotationElement>, $proto.$java<_annotation._element, $annotationElement>,
        $annotation.$part, $member.$named<$annotationElement> {

    /** marker interface for member entities that are part of the class */
    public interface $part{ }

    public Predicate<_annotation._element> constraint = t->true;

    public $comment<JavadocComment>javadoc = $comment.javadocComment();
    public $name name = $name.of("$annotationElementName$"); //name required
    public $typeRef type = $typeRef.of();
    public $ex defaultValue = null;

    public static $annotationElement of(){
        return new $annotationElement();
    }

    public static $annotationElement of (String... annotationElement ){
        return of( _annotation._element.of(annotationElement) );
    }

    public static $annotationElement of(_annotation._element _ec ){
        $annotationElement ec = new $annotationElement();

        if( _ec.hasJavadoc()) {
            ec.javadoc = $comment.javadocComment(_ec.getJavadoc());
        }
        ec.name = $name.of(_ec.getName());
        ec.type = $typeRef.of(_ec.getType() );
        if( _ec.hasDefaultValue() ){
            ec.defaultValue = $ex.of(_ec.getDefaultValue());
        }
        return ec;
    }

    public static $annotationElement of(Predicate<_annotation._element> constraint ){
        return new $annotationElement().$and(constraint);
    }

    public static $annotationElement of($part...parts){
        return new $annotationElement(parts);
    }

    private $annotationElement(){
    }

    public $annotationElement($part...parts){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $comment){
                this.javadoc = ($comment<JavadocComment>)parts[i];
            }
            if( parts[i] instanceof $name ){
                this.name = ($name)parts[i];
            }
            if( parts[i] instanceof $typeRef ){
                this.type = ($typeRef)parts[i];
            }
            if( parts[i] instanceof $ex ){
                this.defaultValue = ($ex)parts[i];
            }
        }
    }

    @Override
    public $annotationElement hardcode$(Translator translator, Tokens kvs) {
        this.javadoc.hardcode$(translator, kvs);
        this.name = this.name.hardcode$(translator, kvs);
        this.type.hardcode$(translator, kvs);
        if( this.defaultValue != null ) {
            this.defaultValue.hardcode$(translator, kvs);
        }
        return this;
    }

    public boolean isMatchAny(){
        try{
            return constraint.test(null) &&
                 this.javadoc.isMatchAny() &&
                 this.name.isMatchAny() &&
                 this.type.isMatchAny() &&
                 this.defaultValue == null;
        } catch(Exception e){
            return false;
        }
    }

    public boolean match(_annotation._element _e){
        return select(_e) != null;
    }

    public boolean matches(AnnotationMemberDeclaration amd ){
        if( amd != null ){
            return select(_annotation._element.of(amd)) != null;
        }
        return false;
    }

    public boolean matches( _annotation._element _e){
        return select(_e) != null;
    }

    @Override
    public Select select(_annotation._element instance) {

        if( !this.constraint.test(instance) ){
            return null;
        }
        //$tokens.to will short circuit
        // IF "tokens" is null: return null (without running the lambda)
        // IF "tokens" is not null : run the lambda and derive "NewTokens" of Map<String,Object>
        // IF "NewTokens" is null : return null (this means that this particular match failed)
        // IF "NewTokens" is not null : check that the "tokens" are consistent with "NewTokens"
        // IF "tokens"/"NewTokens" ARE NOT consistent (i.e. at least one var is assigned (2) distinct values) : return null
        // IF "tokens"/NewTokens" ARE consistent : return the "composite" tokens list (the union of "tokens" & "NewTokens")
        Tokens ts = this.type.parseTo(instance.getType(), new Tokens() );
        if( ts == null ){
            return null;
        }
        $tokens tokens = new $tokens(ts);
        tokens = $tokens.to( tokens, ()-> this.javadoc.parse(instance ));
        tokens = $tokens.to( tokens, ()-> this.name.parse(instance.getName()));
        if(this.defaultValue != null){
            tokens = $tokens.to(tokens, ()-> this.defaultValue.select(instance.getDefaultValue()).tokens);
        }
        if( tokens != null ){
            return new Select(instance, tokens);
        }
        return null;
    }

    /**
     * Adds a NOT constraint to the {@link #constraint} based on one or more $method.$part
     * @param parts
     * @return
     */
    public $annotationElement $not(final $part...parts ){
        for(int i=0;i<parts.length;i++){
            if(parts[i] instanceof $comment ){
                final $comment $fj = (($comment)parts[i]);
                Predicate<_annotation._element> pf = f-> $fj.matches(f.getJavadoc());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $typeRef ){
                final $typeRef $fa = (($typeRef)parts[i]);
                Predicate<_annotation._element> pf = f-> $fa.matches(f.getType());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $name){
                final $name $fn = (($name)parts[i]);
                Predicate<_annotation._element> pf = f-> $fn.matches(f.getName());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $ex){
                final $ex $fj = (($ex)parts[i]);
                Predicate<_annotation._element> pf = f-> $fj.matches(f.getDefaultValue());
                $and( pf.negate() );
            }
        }
        return this;
    }

    @Override
    public $annotationElement $and(Predicate<_annotation._element> constraint) {
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    public $annotationElement $javadoc(Predicate<JavadocComment> javadocMatchFn ){
        this.javadoc = $comment.javadocComment(javadocMatchFn);
        return this;
    }

    public $annotationElement $javadoc($comment<JavadocComment> javadocComment ){
        this.javadoc = javadocComment;
        return this;
    }

    public $annotationElement $name(Predicate<String> nameMatchFn){
        this.name = $name.of(nameMatchFn);
        return this;
    }

    public $annotationElement $name(String name ){
        this.name = $name.of(name);
        return this;
    }

    public $annotationElement $name($name name ){
        this.name = name;
        return this;
    }

    @Override
    public boolean match(Node candidate) {
        if(candidate instanceof AnnotationMemberDeclaration){
            return select( _annotation._element.of((AnnotationMemberDeclaration)candidate)) != null;
        }
        return false;
    }

    @Override
    public _annotation._element firstIn(Node astStartNode, Predicate<_annotation._element> nodeMatchFn) {
        Optional<Node> oc = astStartNode.stream().filter(n ->
                (n instanceof AnnotationMemberDeclaration)
                && match(n)
                && nodeMatchFn.test( _annotation._element.of( (AnnotationMemberDeclaration)n)) ).findFirst();
        if( oc.isPresent()){
            return _annotation._element.of( (AnnotationMemberDeclaration)oc.get() );
        }
        return null;
    }

    @Override
    public Select selectFirstIn(Node astNode ) {
        Optional<Node> oc = astNode.stream().filter(n ->
                n instanceof AnnotationMemberDeclaration
                        && match(n) ).findFirst();
        if( oc.isPresent()){
            return select( _annotation._element.of( (AnnotationMemberDeclaration)oc.get() ) );
        }
        return null;
    }

    @Override
    public List<Select> listSelectedIn(Node astNode){
        return listSelectedIn(astNode, t->true);
    }

    public List<Select> listSelectedIn(Node astNode, Predicate<Select>selectMatchFn) {
        List<Select> found = new ArrayList<>();
        astNode.walk(AnnotationMemberDeclaration.class, c->{
            _annotation._element _e = _annotation._element.of( c );
            Select sel = select(_e);
            if( sel != null && selectMatchFn.test(sel)){
                found.add(sel);
            }
        });
        return found;
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_annotation._element> nodeMatchFn, Consumer<_annotation._element> nodeActionFn) {
        astNode.walk(AnnotationMemberDeclaration.class, c->{
            _annotation._element _e = _annotation._element.of( c );
            if( match(_e) && nodeMatchFn.test(_e)){
                nodeActionFn.accept(_e);
            }
        });
        return astNode;
    }

    @Override
    public Class<_annotation._element> javaType() {
        return _annotation._element.class;
    }

    /**
     * The selected Class
     */
    public static class Select implements select_java<_annotation._element>{
        public _annotation._element selected;
        public $tokens tokens;

        public Select( _annotation._element _c, $tokens tokens){
            this.selected = _c;
            this.tokens = tokens;
        }

        @Override
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public _annotation._element _node() {
            return selected;
        }
    }
}
