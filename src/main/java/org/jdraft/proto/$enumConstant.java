package org.jdraft.proto;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import org.jdraft.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Note... at the moment this is NOT a template... should it be??
 */
public final class $enumConstant
        implements $proto<_enum._constant, $enumConstant>, $proto.$java<_enum._constant, $enumConstant>, $enum.$part {

    public Predicate<_enum._constant> constraint = t->true;

    public $comment<JavadocComment>javadoc = $comment.javadocComment();
    public $annos annos = $annos.of();
    public $id name = $id.of("$enumConstantName$"); //name required
    //args?
    public List<$ex> args = new ArrayList<>();

    public List<$field> fields = new ArrayList<>();
    public List<$method> methods = new ArrayList<>();

    /** marker interface for member entities that are part of the class */
    public interface $part{ }

    public static $enumConstant of(){
        return new $enumConstant();
    }

    public static $enumConstant of ( String... enumConstant ){
        return of( _enum._constant.of(enumConstant) );
    }

    public static $enumConstant of( _enum._constant _ec ){
        $enumConstant ec = new $enumConstant();
        if( _ec.hasAnnos() ) {
            ec.annos = $annos.of(_ec.getAnnos());
        }
        if( _ec.hasJavadoc()) {
            ec.javadoc = $comment.javadocComment(_ec.getJavadoc());
        }
        ec.name = $id.of(_ec.getName());
        if( _ec.hasArguments() ) {
            _ec.listArguments().forEach(a -> ec.args.add($ex.of(a)));
        }
        if( _ec.hasFields() ) {
            _ec.listFields().forEach(f -> ec.fields.add($field.of(f)));
        }
        if( _ec.hasMethods() ) {
            _ec.listMethods().forEach(m -> ec.methods.add($method.of(m)));
        }
        return ec;
    }

    public static $enumConstant of(Predicate<_enum._constant> constraint ){
        return new $enumConstant().$and(constraint);
    }

    public static $enumConstant of($part...parts){
        return new $enumConstant(parts);
    }

    private $enumConstant(){
    }

    public $enumConstant($part...parts){
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
            if( parts[i] instanceof $method ){
                this.methods.add( ($method)parts[i]);
            }
            if( parts[i] instanceof $ex ){
                this.args.add( ($ex)parts[i]);
            }
            if( parts[i] instanceof $id ){
                this.name = ($id)parts[i];
            }
        }
    }

    @Override
    public $enumConstant hardcode$(Translator translator, Tokens kvs) {
        this.annos.hardcode$(translator, kvs);
        this.fields.forEach(f-> f.hardcode$(translator, kvs));
        this.javadoc.hardcode$(translator, kvs);
        this.methods.forEach(m-> m.hardcode$(translator, kvs));
        this.args.forEach( a-> a.hardcode$(translator,kvs));
        this.name = this.name.hardcode$(translator, kvs);

        return this;
    }

    public boolean isMatchAny(){
        try{
            return constraint.test(null) &&
                 this.annos.isMatchAny() &&
                 this.fields.isEmpty() &&
                 this.methods.isEmpty() &&
                 this.args.isEmpty() &&
                 this.name.isMatchAny() &&
                 this.javadoc.isMatchAny();
        } catch(Exception e){
            return false;
        }
    }

    public boolean match( _enum._constant _e){
        return select(_e) != null;
    }

    public boolean matches(EnumConstantDeclaration coid ){
        if( coid != null ){
            return select(_enum._constant.of(coid)) != null;
        }
        return false;
    }

    public boolean matches( _enum._constant _e){
        return select(_e) != null;
    }

    @Override
    public Select select(_enum._constant instance) {

        //$tokens.to will short circuit
        // IF "tokens" is null: return null (without running the lambda)
        // IF "tokens" is not null : run the lambda and derive "NewTokens" of Map<String,Object>
        // IF "NewTokens" is null : return null (this means that this particular match failed)
        // IF "NewTokens" is not null : check that the "tokens" are consistent with "NewTokens"
        // IF "tokens"/"NewTokens" ARE NOT consistent (i.e. at least one var is assigned (2) distinct values) : return null
        // IF "tokens"/NewTokens" ARE consistent : return the "composite" tokens list (the union of "tokens" & "NewTokens")
        $tokens tokens = new $tokens();
        tokens = $tokens.to( tokens, ()-> this.annos.parse(instance));
        tokens = $tokens.to( tokens, ()-> this.javadoc.parse(instance ));
        tokens = $tokens.to( tokens, ()-> this.name.parse(instance.getName()));
        tokens = $tokens.to( tokens, ()-> $type.selectMethods(this.methods, instance ) );
        tokens = $tokens.to( tokens, ()-> $type.selectFields(this.fields, instance ) );
        tokens = $tokens.to( tokens, ()-> selectArgs(this.args, instance));
        //nests
        if( tokens != null ){
            return new Select(instance, tokens);
        }
        return null;
    }

    public static $proto.$tokens selectArgs(List<$ex> $protoArgs, _enum._constant _ec ){
        Map<$ex, List<$ex.Select>> selectMap = new HashMap<>();

        for(int i=0;i<$protoArgs.size(); i++) {
            final $ex t = $protoArgs.get(i);
            List<$ex.Select>matches = new ArrayList<>();
            _ec.listArguments().forEach( c ->{
                $ex.Select sel = t.select(c);
                if( sel != null ){
                    matches.add(sel);
                }
            } );
            if( matches.isEmpty()){
                return null; //couldnt match a $constructor to ANY constructors
            } else{
                selectMap.put(t, matches); //associated the matches with
            }
        }
        //Now create a map with ALL tokens
        $proto.$tokens all = $proto.$tokens.of();
        selectMap.values().forEach( ls -> ls.forEach( s-> all.putAll(s.tokens()) ));
        all.remove("type");
        all.remove("name");
        return all;
    }


    @Override
    public $enumConstant $and(Predicate<_enum._constant> constraint) {
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    public $enumConstant $javadoc(Predicate<JavadocComment> javadocMatchFn ){
        this.javadoc = $comment.javadocComment(javadocMatchFn);
        return this;
    }

    public $enumConstant $javadoc($comment<JavadocComment> javadocComment ){
        this.javadoc = javadocComment;
        return this;
    }

    public $enumConstant $name(Predicate<String> nameMatchFn){
        this.name = $id.of(nameMatchFn);
        return this;
    }

    public $enumConstant $name(String name ){
        this.name = $id.of(name);
        return this;
    }

    public $enumConstant $name($id name ){
        this.name = name;
        return this;
    }

    public $enumConstant $annos(Predicate<_anno._annos> annosMatchFn){
        this.annos.$and(annosMatchFn);
        return this;
    }

    public $enumConstant $annos($annos $as ){
        this.annos = $as;
        return this;
    }

    public $enumConstant $annos($anno... $a){
        this.annos.add($a);
        return this;
    }

    public $enumConstant $methods($method...$ms ){
        Arrays.stream($ms).forEach(m-> this.methods.add(m));
        return this;
    }

    public $enumConstant $args( $ex...args ){
        Arrays.stream(args).forEach(a-> this.args.add(a));
        return this;
    }

    public $enumConstant $fields($field...$fs){
        Arrays.stream($fs).forEach(f-> this.fields.add(f));
        return this;
    }

    @Override
    public boolean match(Node candidate) {
        if(candidate instanceof EnumConstantDeclaration){
            return select( _enum._constant.of((EnumConstantDeclaration)candidate)) != null;
        }
        return false;
    }

    @Override
    public _enum._constant firstIn(Node astStartNode, Predicate<_enum._constant> nodeMatchFn) {
        Optional<Node> oc = astStartNode.stream().filter(n ->
                (n instanceof EnumConstantDeclaration)
                && match(n)
                && nodeMatchFn.test( _enum._constant.of( (EnumConstantDeclaration)n)) ).findFirst();
        if( oc.isPresent()){
            return _enum._constant.of( (EnumConstantDeclaration)oc.get() );
        }
        return null;
    }

    @Override
    public Select selectFirstIn(Node astNode ) {
        Optional<Node> oc = astNode.stream().filter(n ->
                n instanceof EnumConstantDeclaration
                        && match(n) ).findFirst();
        if( oc.isPresent()){
            return select( _enum._constant.of( (EnumConstantDeclaration)oc.get() ) );
        }
        return null;
    }

    @Override
    public List<Select> listSelectedIn(Node astNode){
        return listSelectedIn(astNode, t->true);
    }

    public List<Select> listSelectedIn(Node astNode, Predicate<Select>selectMatchFn) {
        List<Select> found = new ArrayList<>();
        astNode.walk(EnumConstantDeclaration.class, c->{
            _enum._constant _e = _enum._constant.of( c );
            Select sel = select(_e);
            if( sel != null && selectMatchFn.test(sel)){
                found.add(sel);
            }
        });
        return found;
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_enum._constant> nodeMatchFn, Consumer<_enum._constant> nodeActionFn) {
        astNode.walk(EnumConstantDeclaration.class, c->{
            _enum._constant _e = _enum._constant.of( c );
            if( match(_e) && nodeMatchFn.test(_e)){
                nodeActionFn.accept(_e);
            }
        });
        return astNode;
    }

    @Override
    public Class<_enum._constant> javaType() {
        return _enum._constant.class;
    }

    /**
     * The selected Class
     */
    public static class Select implements select_java<_enum._constant>{
        public _enum._constant selected;
        public $tokens tokens;

        public Select( _enum._constant _c, $tokens tokens){
            this.selected = _c;
            this.tokens = tokens;
        }

        @Override
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public _enum._constant _node() {
            return selected;
        }
    }
}
