package org.jdraft.proto;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
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
public final class $interface
        implements $proto<_interface, $interface>, $proto.$java<_interface, $interface> {

    public Predicate<_interface> constraint = t->true;

    public $package packageDecl = $package.of();
    public List<$import> imports = new ArrayList<>();
    public $comment<JavadocComment>javadoc = $comment.javadocComment();
    public $annos annos = $annos.of();
    public $modifiers modifiers = $modifiers.of();
    public $typeParameters typeParameters = $typeParameters.of();
    public $id name = $id.of("$interfaceName$"); //name required

    public List<$field> fields = new ArrayList<>();
    public List<$method> methods = new ArrayList<>();


    public List<$typeRef> extend = new ArrayList<>(); //$typeRef.of();

    //nested types???

    /** marker interface for member entities that are part of the class */
    public interface $part{ }

    public static $interface of(){
        return new $interface();
    }

    public static $interface of(Predicate<_interface> constraint ){
        return new $interface().$and(constraint);
    }

    public static $interface of($part...parts){
        return new $interface(parts);
    }

    private $interface(){
    }

    public $interface($part...parts){
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
            //doesnt do javadoc, headercomment, extend, implement
        }
    }

    @Override
    public $interface hardcode$(Translator translator, Tokens kvs) {

        this.annos.hardcode$(translator, kvs);
        this.fields.forEach(f-> f.hardcode$(translator, kvs));
        this.imports.forEach( i-> i.hardcode$(translator, kvs));
        this.javadoc.hardcode$(translator, kvs);
        this.methods.forEach(m-> m.hardcode$(translator, kvs));
        this.modifiers.hardcode$(translator, kvs);
        this.name = this.name.hardcode$(translator, kvs);
        this.packageDecl = this.packageDecl.hardcode$(translator, kvs);
        this.typeParameters = this.typeParameters.hardcode$(translator, kvs);
        this.extend.forEach( i-> i.hardcode$(translator, kvs));
        //still need nests

        return this;
    }

    public boolean isMatchAny(){
        try{
            return constraint.test(null) &&
                 this.annos.isMatchAny() &&
                 this.fields.isEmpty() &&
                 this.methods.isEmpty() &&
                 this.imports.isEmpty() &&
                 this.javadoc.isMatchAny() &&
                 this.modifiers.isMatchAny() &&
                 this.packageDecl.isMatchAny() &&
                 this.typeParameters.isMatchAny() &&
                 this.extend.isEmpty();
            //NESTS
        } catch(Exception e){
            return false;
        }
    }

    public boolean match( _interface _i){
        return select(_i) != null;
    }

    public boolean matches(CompilationUnit cu){
        if( cu != null){
            if( cu.getTypes().size() == 1 && cu.getType(0) instanceof ClassOrInterfaceDeclaration){
                return matches( cu.getType(0).asClassOrInterfaceDeclaration() );
            }
            if( cu.getPrimaryType().isPresent() ){
                return matches(cu.getPrimaryType().get());
            }
        }
        return false;
    }


    public boolean matches( Class clazz){
        try {
            return matches(Ast.interfaceDecl(clazz));
        }catch(Exception e){
            return false;
        }
    }

    public boolean matches(TypeDeclaration td ){
        if( td instanceof ClassOrInterfaceDeclaration ){
            return matches( (ClassOrInterfaceDeclaration) td);
        }
        return false;
    }

    public boolean matches(ClassOrInterfaceDeclaration coid ){
        if( coid != null && coid.isInterface()){
            return select(_interface.of(coid)) != null;
        }
        return false;
    }

    public boolean matches( _code _c){
        if( _c instanceof _interface){
            return matches( (_interface)_c);
        }
        return false;
    }

    public boolean matches( _interface _c){
        return select(_c) != null;
    }

    @Override
    public Select select(_interface instance) {

        if(!this.constraint.test(instance)){
            return null;
        }
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
        tokens = $tokens.to( tokens, ()-> this.typeParameters.parse(instance.getTypeParameters()) );

        tokens = $tokens.to( tokens, ()-> $type.selectExtends(this.extend, instance) );

        tokens = $tokens.to( tokens, ()-> $type.selectMethods(this.methods, instance ) );
        tokens = $tokens.to( tokens, ()-> $type.selectFields(this.fields, instance ) );

        //nests
        if( tokens != null ){
            return new Select(instance, tokens);
        }
        return null;
    }

    @Override
    public $interface $and(Predicate<_interface> constraint) {
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    public $interface $javadoc(Predicate<JavadocComment> javadocMatchFn ){
        this.javadoc = $comment.javadocComment(javadocMatchFn);
        return this;
    }

    public $interface $modifiers($modifiers...$mods){
        this.modifiers = $modifiers.of($mods);
        return this;
    }

    public $interface $javadoc($comment<JavadocComment> javadocComment ){
        this.javadoc = javadocComment;
        return this;
    }

    public $interface $extend($typeRef... ext ){
        Arrays.stream(ext).forEach(c -> this.extend.add( c));
        return this;
    }

    public $interface $extend(Class... clazz){
         Arrays.stream(clazz).forEach(c -> this.extend.add( $typeRef.of(c)));
         return this;
    }

    public $interface $extend(String...types){
        Arrays.stream(types).forEach(c -> this.extend.add( $typeRef.of(c)));
        return this;
    }

    public $interface $imports($import...$is ){
        Arrays.stream($is).forEach( i -> this.imports.add(i));
        return this;
    }

    public $interface $imports(String...$is ){
        Arrays.stream($is).forEach( i -> this.imports.add($import.of(i)));
        return this;
    }
    public $interface $imports(Class... clazzes ){
        Arrays.stream(clazzes).forEach( i -> this.imports.add($import.of(i)));
        return this;
    }

    public $interface $package($package $p ){
        this.packageDecl = $p;
        return this;
    }

    public $interface $package(Predicate<PackageDeclaration> packageMatchFn){
        this.packageDecl = $package.of(packageMatchFn);
        return this;
    }


    public $interface $name(Predicate<String> nameMatchFn){
        this.name = $id.of(nameMatchFn);
        return this;
    }

    public $interface $name(String name ){
        this.name = $id.of(name);
        return this;
    }

    public $interface $name($id name ){
        this.name = name;
        return this;
    }

    public $interface $annos(Predicate<_anno._annos> annosMatchFn){
        this.annos.$and(annosMatchFn);
        return this;
    }

    public $interface $annos($annos $as ){
        this.annos = $as;
        return this;
    }

    public $interface $annos($anno... $a){
        this.annos.add($a);
        return this;
    }

    public $interface $method(Predicate<_method> _methodMatchFn ){
        this.methods.add($method.of(_methodMatchFn));
        return this;
    }

    public $interface $methods($method...$ms ){
        Arrays.stream($ms).forEach(m-> this.methods.add(m));
        return this;
    }

    public $interface $field(Predicate<_field> _fieldMatchFn ){
        this.fields.add($field.of(_fieldMatchFn));
        return this;
    }

    public $interface $fields($field...$fs){
        Arrays.stream($fs).forEach(f-> this.fields.add(f));
        return this;
    }

    public $interface $typeParameters(Predicate<_typeParameter._typeParameters> _tpMatchFn){
        this.typeParameters.$and(_tpMatchFn);
        return this;
    }

    public $interface $typeParameters($typeParameters $tps ){
        this.typeParameters = $tps;
        return this;
    }

    public $interface $typeParameters($typeParameter... $tps ){
        Arrays.stream($tps).forEach(tp-> this.typeParameters.$add(tp));
        return this;
    }

    @Override
    public boolean match(Node candidate) {
        if(candidate instanceof ClassOrInterfaceDeclaration && ((ClassOrInterfaceDeclaration) candidate).asClassOrInterfaceDeclaration().isInterface()){
            return select( _interface.of((ClassOrInterfaceDeclaration)candidate)) != null;
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
    public _interface firstIn(Node astStartNode, Predicate<_interface> nodeMatchFn) {
        Optional<Node> oc = astStartNode.stream().filter(n ->
                (n instanceof ClassOrInterfaceDeclaration)
                && ( ((ClassOrInterfaceDeclaration)n).isInterface() )
                && match(n)
                && nodeMatchFn.test( _interface.of( (ClassOrInterfaceDeclaration)n)) ).findFirst();
        if( oc.isPresent()){
            return _interface.of( (ClassOrInterfaceDeclaration)oc.get() );
        }
        return null;
    }

    @Override
    public Select selectFirstIn(Node astNode ) {
        Optional<Node> oc = astNode.stream().filter(n ->
                n instanceof ClassOrInterfaceDeclaration
                        && ( ((ClassOrInterfaceDeclaration)n).isInterface() )
                        && match(n) ).findFirst();
        if( oc.isPresent()){
            return select( _interface.of( (ClassOrInterfaceDeclaration)oc.get() ) );
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
            if( c.isInterface() ){
                _interface _i = _interface.of( c );
                Select sel = select(_i);
                if( sel != null && selectMatchFn.test(sel)){
                    found.add(sel);
                }
            }
        });
        return found;
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_interface> nodeMatchFn, Consumer<_interface> nodeActionFn) {
        astNode.walk(ClassOrInterfaceDeclaration.class, c->{
            if( c.isInterface() ){
                _interface _i = _interface.of( c );
                if( match(_i) && nodeMatchFn.test(_i)){
                    nodeActionFn.accept(_i);
                }
            }
        });
        return astNode;
    }

    @Override
    public Class<_interface> javaType() {
        return _interface.class;
    }

    /**
     * The selected Class
     */
    public static class Select implements select_java<_interface>{
        public _interface selected;
        public $tokens tokens;

        public Select( _interface _c, $tokens tokens){
            this.selected = _c;
            this.tokens = tokens;
        }

        @Override
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public _interface _node() {
            return selected;
        }
    }
}
