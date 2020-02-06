package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jdraft.*;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

/**
 * Note... at the moment this is NOT a template... should it be??
 */
public class $annotationEntry
        implements //$pattern<_annotation._entry, $annotationEntry>,
        $pattern.$java<_annotation._entry, $annotationEntry>,
        $annotation.$part, $member.$named<$annotationEntry>, $declared<_annotation._entry, $annotationEntry>, $type.$part  {

    /** marker interface for member entities that are part of the class */
    public interface $part{ }

    public Predicate<_annotation._entry> constraint = t->true;

    public $comment<JavadocComment>javadoc = $comment.javadocComment();
    public $name name = $name.of("$annotationElementName$"); //name required
    public $typeRef type = $typeRef.of();
    public $ex defaultValue = null;

    public static $annotationEntry of(){
        return new $annotationEntry();
    }

    public static $annotationEntry of (String... annotationElement ){
        return of( _annotation._entry.of(annotationElement) );
    }

    public static $annotationEntry of(AnnotationMemberDeclaration _ec ){
        return of( _annotation._entry.of(_ec));
    }

    public static $annotationEntry of(_annotation._entry _ec ){
        $annotationEntry ec = new $annotationEntry();

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

    public static $annotationEntry of(Predicate<_annotation._entry> constraint ){
        return new $annotationEntry().$and(constraint);
    }

    public static $annotationEntry of($part...parts){
        return new $annotationEntry(parts);
    }

    public static $annotationEntry.Or or(_annotation._entry... _protos ){
        $annotationEntry[] arr = new $annotationEntry[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $annotationEntry.of( _protos[i]);
        }
        return or(arr);
    }

    public static $annotationEntry.Or or($annotationEntry...$tps ){
        return new $annotationEntry.Or($tps);
    }

    public static $annotationEntry as(String...annotationElement){
        return as( _annotation._entry.of(annotationElement) );
    }

    public static $annotationEntry as(AnnotationMemberDeclaration _ec ){
        return as( _annotation._entry.of(_ec));
    }

    public static $annotationEntry as(_annotation._entry _ec ){
        $annotationEntry ae = new $annotationEntry();

        if( _ec.hasJavadoc()) {
            ae.javadoc = $comment.javadocComment(_ec.getJavadoc());
        }
        ae.name = $name.as(_ec.getName());
        ae.type = $typeRef.as(_ec.getType() );
        if( _ec.hasDefaultValue() ){
            ae.defaultValue = $ex.of(_ec.getDefaultValue());
        } else{
            ae.$and( _ae -> !_ae.hasDefaultValue());
        }
        return ae;
    }

    /**
     * Adds a NOT constraint to the {@link #constraint} based on one or more $method.$part
     * @param parts
     * @return
     */
    public static $annotationEntry not(final $annotationEntry.$part...parts ){
        $annotationEntry $ae = of();
        $ae.$not(parts);
        return $ae;
    }

    private $annotationEntry(){
    }

    public $annotationEntry($part...parts){
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
    public $annotationEntry $(String target, String $paramName) {
        this.javadoc.$(target, $paramName);
        this.name = this.name.$(target, $paramName);
        this.type = this.type.$(target, $paramName);
        if( this.defaultValue != null ) {
            this.defaultValue.$(target, $paramName);
        }
        return this;
    }

    @Override
    public $annotationEntry hardcode$(Translator translator, Tokens kvs) {
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

    public String toString(){
        if( this.isMatchAny() ){
            return "$annotationElement{ $ANY$ }";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("$annotationElement{").append(System.lineSeparator());
        if( !this.javadoc.isMatchAny() ){
            sb.append( Text.indent(this.javadoc.toString())).append(System.lineSeparator());
        }
        if( !this.name.isMatchAny() ){
            sb.append( Text.indent(this.name.toString())).append(System.lineSeparator());
        }
        if( !this.type.isMatchAny() ){
            sb.append( Text.indent(this.type.toString())).append(System.lineSeparator());
        }
        if( this.defaultValue != null && !this.defaultValue.isMatchAny() ){
            sb.append( Text.indent( this.defaultValue.toString())).append(System.lineSeparator());
        }
        sb.append("}");
        return sb.toString();
    }

    public boolean match(_annotation._entry _e){
        return select(_e) != null;
    }

    public boolean matches(String...code){
        try {
            return matches(_annotation._entry.of(code));
        }catch(Exception e){
            return false;
        }
    }

    public boolean matches(AnnotationMemberDeclaration amd ){
        if( amd != null ){
            return select(_annotation._entry.of(amd)) != null;
        }
        return false;
    }

    public boolean matches( _annotation._entry _e){
        return select(_e) != null;
    }

    @Override
    public Select select(_annotation._entry instance) {

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
    public $annotationEntry $not(final $part...parts ){
        for(int i=0;i<parts.length;i++){
            if(parts[i] instanceof $comment ){
                final $comment $fj = (($comment)parts[i]);
                Predicate<_annotation._entry> pf = f-> $fj.matches(f.getJavadoc());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $typeRef ){
                final $typeRef $fa = (($typeRef)parts[i]);
                Predicate<_annotation._entry> pf = f-> $fa.matches(f.getType());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $name){
                final $name $fn = (($name)parts[i]);
                Predicate<_annotation._entry> pf = f-> $fn.matches(f.getName());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $ex){
                final $ex $fj = (($ex)parts[i]);
                Predicate<_annotation._entry> pf = f-> $fj.matches(f.getDefaultValue());
                $and( pf.negate() );
            }
        }
        return this;
    }

    @Override
    public $annotationEntry $and(Predicate<_annotation._entry> constraint) {
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    @Override
    public $comment<JavadocComment> get$javadoc() {
        return javadoc;
    }

    public $annotationEntry $javadoc(Predicate<JavadocComment> javadocMatchFn ){
        this.javadoc = $comment.javadocComment(javadocMatchFn);
        return this;
    }

    public $annotationEntry $javadoc(_javadoc javadocComment ){
        this.javadoc = $comment.javadocComment(javadocComment);
        return this;
    }

    public $annotationEntry $javadoc($comment<JavadocComment> javadocComment ){
        this.javadoc = javadocComment;
        return this;
    }

    @Override
    public $name get$Name(){
        return this.name;
    }

    public $annotationEntry $name(Predicate<String> nameMatchFn){
        this.name = $name.of(nameMatchFn);
        return this;
    }

    public $annotationEntry $name(String name ){
        this.name = $name.of(name);
        return this;
    }

    public $annotationEntry $name($name name ){
        this.name = name;
        return this;
    }

    @Override
    public boolean match(Node candidate) {
        if(candidate instanceof AnnotationMemberDeclaration){
            return select( _annotation._entry.of((AnnotationMemberDeclaration)candidate)) != null;
        }
        return false;
    }

    @Override
    public _annotation._entry firstIn(Node astStartNode, Predicate<_annotation._entry> nodeMatchFn) {
        Optional<Node> oc = astStartNode.stream().filter(n ->
                (n instanceof AnnotationMemberDeclaration)
                && match(n)
                && nodeMatchFn.test( _annotation._entry.of( (AnnotationMemberDeclaration)n)) ).findFirst();
        if( oc.isPresent()){
            return _annotation._entry.of( (AnnotationMemberDeclaration)oc.get() );
        }
        return null;
    }

    @Override
    public Select selectFirstIn(Node astNode ) {
        Optional<Node> oc = astNode.stream().filter(n ->
                n instanceof AnnotationMemberDeclaration
                        && match(n) ).findFirst();
        if( oc.isPresent()){
            return select( _annotation._entry.of( (AnnotationMemberDeclaration)oc.get() ) );
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
            _annotation._entry _e = _annotation._entry.of( c );
            Select sel = select(_e);
            if( sel != null && selectMatchFn.test(sel)){
                found.add(sel);
            }
        });
        return found;
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_annotation._entry> nodeMatchFn, Consumer<_annotation._entry> nodeActionFn) {
        astNode.walk(AnnotationMemberDeclaration.class, c->{
            _annotation._entry _e = _annotation._entry.of( c );
            if( match(_e) && nodeMatchFn.test(_e)){
                nodeActionFn.accept(_e);
            }
        });
        return astNode;
    }

    @Override
    public Class<_annotation._entry> _modelType() {
        return _annotation._entry.class;
    }

    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $annotationEntry {

        final List<$annotationEntry>ors = new ArrayList<>();

        public Or($annotationEntry...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $annotationEntry hardcode$(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.hardcode$(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$annotationElement.Or{");
            sb.append(System.lineSeparator());
            ors.forEach($a -> sb.append( Text.indent($a.toString()) ) );
            sb.append("}");
            return sb.toString();
        }

        /**
         *
         * @param astNode
         * @return
         */
        public $annotationEntry.Select select(_annotation._entry astNode){
            $annotationEntry $a = whichMatch(astNode);
            if( $a != null ){
                return $a.select(astNode);
            }
            return null;
        }

        public boolean isMatchAny(){
            return false;
        }

        /**
         * Return the underlying $anno that matches the AnnotationExpr or null if none of the match
         * @param ae
         * @return
         */
        public $annotationEntry whichMatch(_annotation._entry ae){
            if( !this.constraint.test( ae ) ){
                return null;
            }
            Optional<$annotationEntry> orsel  = this.ors.stream().filter($p-> $p.match(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }

    /**
     * The selected Class
     */
    public static class Select implements select_java<_annotation._entry>{
        public _annotation._entry selected;
        public $tokens tokens;

        public Select(_annotation._entry _c, $tokens tokens){
            this.selected = _c;
            this.tokens = tokens;
        }

        @Override
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public _annotation._entry _node() {
            return selected;
        }
    }
}
