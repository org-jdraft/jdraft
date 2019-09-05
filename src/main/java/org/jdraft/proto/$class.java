package org.jdraft.proto;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import org.jdraft.Tokens;
import org.jdraft.Translator;
import org.jdraft._class;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Note... at the moment this is NOT a template... should it be??
 */
public final class $class
        implements $proto<_class, $class>, $proto.$java<_class,$class> {

    public Predicate<_class> constraint = t->true;

    public $comment<BlockComment> headerComment = $comment.blockComment();
    public $package packageDecl = $package.of();
    public List<$import> imports = new ArrayList<>();
    public $comment<JavadocComment>javadoc = $comment.javadocComment();
    public $annos annos = $annos.of();
    public $modifiers modifiers = $modifiers.of();
    public $typeParameters typeParameters = $typeParameters.of();
    public $id name = $id.of("$name$"); //name required

    //body parts
    public List<$constructor> ctors = new ArrayList<>();
    public List<$field> fields = new ArrayList<>();
    public List<$method> methods = new ArrayList<>();
    public List<$initBlock> initBlocks = new ArrayList<>();

    //nested types???

    /** marker interface for member entities that are part of the class */
    public interface $part{ }

    public static $class of(){
        return new $class();
    }

    public static $class of(Predicate<_class> constraint ){
        return new $class().$and(constraint);
    }

    public static $class of( $part...parts){
        return new $class(parts);
    }

    private $class(){
    }

    public $class($part...parts){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $annos ){
                this.annos = ($annos)parts[i];
            }
            if( parts[i] instanceof $anno ){
                this.annos.add(($anno)parts[i]);
            }
            if( parts[i] instanceof $constructor ){
                this.annos = ($annos)parts[i];
            }
            if( parts[i] instanceof $field ){
                this.fields.add( ($field) parts[i]);
            }
            if( parts[i] instanceof $import ){
                this.imports.add( ($import)parts[i]);
            }
            if( parts[i] instanceof $initBlock ){
                this.initBlocks.add( ($initBlock) parts[i]);
            }
            if( parts[i] instanceof $method ){
                this.methods.add( ($method)parts[i]);
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
            if( parts[i] instanceof $typeParameters){
                this.typeParameters = ($typeParameters)parts[i];
            }
            if( parts[i] instanceof $typeParameter){
                this.typeParameters.$add(  ($typeParameter)parts[i]);
            }
            //Nested classes
        }
    }

    @Override
    public $class hardcode$(Translator translator, Tokens kvs) {

        this.annos.hardcode$(translator, kvs);
        this.ctors.forEach( c-> c.hardcode$(translator, kvs));
        this.fields.forEach(f-> f.hardcode$(translator, kvs));
        this.headerComment.hardcode$(translator, kvs);
        this.imports.forEach( i-> i.hardcode$(translator, kvs));
        this.initBlocks.forEach( i-> i.hardcode$(translator, kvs));
        this.javadoc.hardcode$(translator, kvs);
        this.methods.forEach(m-> m.hardcode$(translator, kvs));
        this.modifiers.hardcode$(translator, kvs);
        this.name = this.name.hardcode$(translator, kvs);
        this.packageDecl = this.packageDecl.hardcode$(translator, kvs);
        this.typeParameters = this.typeParameters.hardcode$(translator, kvs);

        //still need nests

        return this;
    }

    public boolean isMatchAny(){
        try{
            return constraint.test(null) &&
                 this.annos.isMatchAny() &&
                 this.ctors.isEmpty() &&
                 this.fields.isEmpty() &&
                 this.methods.isEmpty() &&
                 this.initBlocks.isEmpty() &&
                 this.imports.isEmpty() &&
                 this.headerComment.isMatchAny() &&
                 this.javadoc.isMatchAny() &&
                 this.modifiers.isMatchAny() &&
                 this.packageDecl.isMatchAny() &&
                 this.typeParameters.isMatchAny();
            //NESTS
        } catch(Exception e){
            return false;
        }
    }


    @Override
    public Select select(_class instance) {

        //$tokens.to will short circuit
        // IF "tokens" is null: return null (without running the lambda)
        // IF "tokens" is not null : run the lambda and derive "NewTokens" of Map<String,Object>
        // IF "NewTokens" is null : return null (this means that this particular match failed)
        // IF "NewTokens" is not null : check that the "tokens" are consistent with "NewTokens"
        // IF "tokens"/"NewTokens" ARE NOT consistent (i.e. at least one var is assigned (2) distinct values) : return null
        // IF "tokens"/NewTokens" ARE consistent : return the "composite" tokens list (the union of "tokens" & "NewTokens")
        $tokens tokens = this.headerComment.parse(instance);
        tokens = $tokens.to( tokens, ()-> this.packageDecl.parse(instance.astCompilationUnit() ) );
        tokens = $tokens.to( tokens, ()-> $type.selectImports(this.imports, instance));
        tokens = $tokens.to( tokens, ()-> this.annos.parse(instance));
        tokens = $tokens.to( tokens, ()-> this.javadoc.parse(instance ));
        tokens = $tokens.to( tokens, ()-> this.modifiers.parse(instance));
        tokens = $tokens.to( tokens, ()->this.name.parse(instance.getName()));
        tokens = $tokens.to( tokens, ()-> this.typeParameters.parse(instance.getTypeParameters()) );

        tokens = $tokens.to( tokens, ()-> $type.selectConstructors(this.ctors, instance ) );
        tokens = $tokens.to( tokens, ()-> $type.selectMethods(this.methods, instance ) );
        tokens = $tokens.to( tokens, ()-> $type.selectFields(this.fields, instance ) );
        tokens = $tokens.to( tokens, ()-> $type.selectInitBlocks(this.initBlocks, instance ) );

        //nests
        if( tokens != null ){
            return new Select(instance, tokens);
        }
        return null;
    }

    @Override
    public $class $and(Predicate<_class> constraint) {
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    @Override
    public boolean match(Node candidate) {
        if(candidate instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration) candidate).asClassOrInterfaceDeclaration().isInterface()){
            return select( _class.of((ClassOrInterfaceDeclaration)candidate)) != null;
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
        //its not a Class
        return false;
    }

    @Override
    public _class firstIn(Node astStartNode, Predicate<_class> nodeMatchFn) {
        Optional<Node> oc = astStartNode.stream().filter(n ->
                (n instanceof ClassOrInterfaceDeclaration)
                && ( !((ClassOrInterfaceDeclaration)n).isInterface() )
                && match(n)
                && nodeMatchFn.test( _class.of( (ClassOrInterfaceDeclaration)n)) ).findFirst();
        if( oc.isPresent()){
            return _class.of( (ClassOrInterfaceDeclaration)oc.get() );
        }
        return null;
    }

    @Override
    public Select selectFirstIn(Node astNode ) {
        Optional<Node> oc = astNode.stream().filter(n ->
                n instanceof ClassOrInterfaceDeclaration
                        && ( !((ClassOrInterfaceDeclaration)n).isInterface() )
                        && match(n) ).findFirst();
        if( oc.isPresent()){
            return select( _class.of( (ClassOrInterfaceDeclaration)oc.get() ) );
        }
        return null;
    }

    @Override
    public List<Select> listSelectedIn(Node astNode){
        return listSelectedIn(astNode, t->true);
    }

    public List<Select> listSelectedIn(Node astNode, Predicate<Select>selectMatchFn) {
        List<Select> found = new ArrayList<>();
        astNode.walk(ClassOrInterfaceDeclaration.class, c->{
            if( !c.isInterface() ){
                _class _c = _class.of( c );
                Select sel = select(_c);
                if( sel != null && selectMatchFn.test(sel)){
                    found.add(sel);
                }
            }
        });
        return found;
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_class> nodeMatchFn, Consumer<_class> nodeActionFn) {
        astNode.walk(ClassOrInterfaceDeclaration.class, c->{
            if( !c.isInterface() ){
                _class _c = _class.of( c );
                if( match(_c) && nodeMatchFn.test(_c)){
                    nodeActionFn.accept(_c);
                }
            }
        });
        return astNode;
    }

    @Override
    public Class<_class> javaType() {
        return _class.class;
    }

    public static class Select implements $proto.select_java<_class>{
        public _class selected;
        public $tokens tokens;

        public Select( _class _c, $tokens tokens){
            this.selected = _c;
            this.tokens = tokens;
        }

        @Override
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public _class _node() {
            return selected;
        }
    }
}
