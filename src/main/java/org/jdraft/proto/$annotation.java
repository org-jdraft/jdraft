package org.jdraft.proto;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import org.jdraft.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Note... at the moment this is NOT a template... should it be??
 */
public final class $annotation
        implements $proto<_annotation, $annotation>, $proto.$java<_annotation, $annotation> {

    public Predicate<_annotation> constraint = t->true;

    public $package packageDecl = $package.of();
    public List<$import> imports = new ArrayList<>();
    public $comment<JavadocComment>javadoc = $comment.javadocComment();
    public $annos annos = $annos.of();
    public $modifiers modifiers = $modifiers.of();
    public $id name = $id.of("$annotationName$"); //name required

    //body parts
    public List<$field> fields = new ArrayList<>();

    /** marker interface for member entities that are part of the annotation */
    public interface $part{ }

    public static $annotation of(){
        return new $annotation();
    }

    public static $annotation of(Predicate<_annotation> constraint ){
        return new $annotation().$and(constraint);
    }

    public static $annotation of($part...parts){
        return new $annotation(parts);
    }

    private $annotation(){
    }

    public $annotation($part...parts){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $annos ){
                this.annos = ($annos)parts[i];
            }
            if( parts[i] instanceof $anno ){
                this.annos.add(($anno)parts[i]);
            }
            if( parts[i] instanceof $comment){
                this.javadoc = ($comment<JavadocComment>)parts[i];
            }
            if( parts[i] instanceof $field ){
                this.fields.add( ($field) parts[i]);
            }
            if( parts[i] instanceof $import ){
                this.imports.add( ($import)parts[i]);
            }
            if( parts[i] instanceof $modifiers ){
                $modifiers ms = ($modifiers)parts[i];
                this.modifiers.mustInclude.addAll(ms.mustInclude);
                this.modifiers.mustExclude.addAll(ms.mustExclude);
                this.modifiers.$and(ms.constraint);
            }
            if( parts[i] instanceof $id ){
                this.name = ($id)parts[i];
            }
            if( parts[i] instanceof $package ){
                this.packageDecl = ($package)parts[i];
            }
            // $element
            //Nested classes
            //doesnt do javadoc, headercomment, extend, implement
        }
    }

    @Override
    public $annotation hardcode$(Translator translator, Tokens kvs) {
        this.annos.hardcode$(translator, kvs);
        this.fields.forEach(f-> f.hardcode$(translator, kvs));
        this.imports.forEach( i-> i.hardcode$(translator, kvs));
        this.javadoc.hardcode$(translator, kvs);
        this.modifiers.hardcode$(translator, kvs);
        this.name = this.name.hardcode$(translator, kvs);
        this.packageDecl = this.packageDecl.hardcode$(translator, kvs);
        //still need nests
        return this;
    }

    public boolean isMatchAny(){
        try{
            return constraint.test(null) &&
                 this.annos.isMatchAny() &&
                 this.fields.isEmpty() &&
                 this.imports.isEmpty() &&

                 this.javadoc.isMatchAny() &&
                 this.modifiers.isMatchAny() &&
                 this.packageDecl.isMatchAny();
                // nests, $property

            //NESTS
        } catch(Exception e){
            return false;
        }
    }

    public boolean match( _annotation _a){
        return select(_a) != null;
    }

    public boolean matches(CompilationUnit cu){
        if( cu != null){
            if( cu.getTypes().size() == 1 && cu.getType(0) instanceof AnnotationDeclaration){
                return matches( cu.getType(0).asAnnotationDeclaration() );
            }
            if( cu.getPrimaryType().isPresent() ){
                return matches(cu.getPrimaryType().get());
            }
        }
        return false;
    }

    public boolean matches(TypeDeclaration td ){
        if( td instanceof AnnotationDeclaration ){
            return matches( (AnnotationDeclaration) td);
        }
        return false;
    }

    public boolean matches(AnnotationDeclaration coid ){
        if( coid != null){
            return select(_annotation.of(coid)) != null;
        }
        return false;
    }

    public boolean matches( _code _c){
        if( _c instanceof _annotation){
            return matches( (_annotation)_c);
        }
        return false;
    }

    public boolean matches( _annotation _c){
        return select(_c) != null;
    }

    @Override
    public Select select(_annotation instance) {

        //$tokens.to will short circuit
        // IF "tokens" is null: return null (without running the lambda)
        // IF "tokens" is not null : run the lambda and derive "NewTokens" of Map<String,Object>
        // IF "NewTokens" is null : return null (this means that this particular match failed)
        // IF "NewTokens" is not null : check that the "tokens" are consistent with "NewTokens"
        // IF "tokens"/"NewTokens" ARE NOT consistent (i.e. at least one var is assigned (2) distinct values) : return null
        // IF "tokens"/NewTokens" ARE consistent : return the "composite" tokens list (the union of "tokens" & "NewTokens")
        //$tokens tokens = this.headerComment.parse(instance);
        $tokens tokens = $type.selectImports(this.imports, instance);
        //$tokens tokens = this.packageDecl.parse(instance.astCompilationUnit() );
        tokens = $tokens.to( tokens, ()-> this.packageDecl.parse(instance.astCompilationUnit() ) );

        tokens = $tokens.to( tokens, ()-> this.annos.parse(instance));
        tokens = $tokens.to( tokens, ()-> this.javadoc.parse(instance ));
        tokens = $tokens.to( tokens, ()-> this.modifiers.parse(instance));
        tokens = $tokens.to( tokens, ()-> this.name.parse(instance.getName()));
        tokens = $tokens.to( tokens, ()-> $type.selectFields(this.fields, instance ) );

        //nests, elements

        if( tokens != null ){
            return new Select(instance, tokens);
        }
        return null;
    }

    @Override
    public $annotation $and(Predicate<_annotation> constraint) {
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    public $annotation $javadoc(Predicate<JavadocComment> javadocMatchFn ){
        this.javadoc = $comment.javadocComment(javadocMatchFn);
        return this;
    }

    public $annotation $modifiers($modifiers...$mods){
        this.modifiers = $modifiers.of($mods);
        return this;
    }

    public $annotation $javadoc($comment<JavadocComment> javadocComment ){
        this.javadoc = javadocComment;
        return this;
    }

    public $annotation $imports($import...$is ){
        Arrays.stream($is).forEach( i -> this.imports.add(i));
        return this;
    }

    public $annotation $imports(Class... clazzes ){
        Arrays.stream(clazzes).forEach( i -> this.imports.add($import.of(i)));
        return this;
    }

    public $annotation $package($package $p ){
        this.packageDecl = $p;
        return this;
    }

    public $annotation $name(Predicate<String> nameMatchFn){
        this.name = $id.of(nameMatchFn);
        return this;
    }

    public $annotation $name(String name ){
        this.name = $id.of(name);
        return this;
    }

    public $annotation $name($id name ){
        this.name = name;
        return this;
    }

    public $annotation $annos(Predicate<_anno._annos> annosMatchFn){
        this.annos.$and(annosMatchFn);
        return this;
    }

    public $annotation $annos($annos $as ){
        this.annos = $as;
        return this;
    }

    public $annotation $annos($anno... $a){
        this.annos.add($a);
        return this;
    }

    public $annotation $fields($field...$fs){
        Arrays.stream($fs).forEach(f-> this.fields.add(f));
        return this;
    }


    @Override
    public boolean match(Node candidate) {
        if(candidate instanceof AnnotationDeclaration){
            return select( _annotation.of((AnnotationDeclaration)candidate)) != null;
        }
        if( candidate instanceof CompilationUnit){
            //check if it's only got one class
            CompilationUnit cu = (CompilationUnit)candidate;
            if( cu.getTypes().size() == 1){
                return match( cu.getType(0) );
            }
            //check if the primary type is set
            if( cu.getPrimaryType().isPresent() ){
                return match( cu.getPrimaryType().get() );
            }
        }
        //its not a Annotation
        return false;
    }

    @Override
    public _annotation firstIn(Node astStartNode, Predicate<_annotation> nodeMatchFn) {
        Optional<Node> oc = astStartNode.stream().filter(n ->
                (n instanceof AnnotationDeclaration)
                && match(n)
                && nodeMatchFn.test( _annotation.of( (AnnotationDeclaration)n)) ).findFirst();
        if( oc.isPresent()){
            return _annotation.of( (AnnotationDeclaration)oc.get() );
        }
        return null;
    }

    @Override
    public Select selectFirstIn(Node astNode ) {
        Optional<Node> oc = astNode.stream().filter(n ->
                n instanceof AnnotationDeclaration
                        && match(n) ).findFirst();
        if( oc.isPresent()){
            return select( _annotation.of( (AnnotationDeclaration)oc.get() ) );
        }
        return null;
    }

    @Override
    public List<Select> listSelectedIn(Node astNode){
        return listSelectedIn(astNode, t->true);
    }

    public List<Select> listSelectedIn(Node astNode, Predicate<Select>selectMatchFn) {
        List<Select> found = new ArrayList<>();
        astNode.walk(AnnotationDeclaration.class, c->{
            _annotation _a = _annotation.of( c );
            Select sel = select(_a);
            if( sel != null && selectMatchFn.test(sel)){
                found.add(sel);
            }
        });
        return found;
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_annotation> nodeMatchFn, Consumer<_annotation> nodeActionFn) {
        astNode.walk(AnnotationDeclaration.class, c->{
            _annotation _a = _annotation.of( c );
            if( match(_a) && nodeMatchFn.test(_a)){
                nodeActionFn.accept(_a);
            }
        });
        return astNode;
    }

    @Override
    public Class<_annotation> javaType() {
        return _annotation.class;
    }

    /**
     * The selected Class
     */
    public static class Select implements select_java<_annotation>{
        public _annotation selected;
        public $tokens tokens;

        public Select( _annotation _a, $tokens tokens){
            this.selected = _a;
            this.tokens = tokens;
        }

        @Override
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public _annotation _node() {
            return selected;
        }
    }
}
