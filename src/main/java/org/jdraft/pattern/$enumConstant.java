package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jdraft.*;
import org.jdraft._constant;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

/**
 * Note... at the moment this is NOT a template... should it be??
 */
public class $enumConstant
        implements //$pattern<_constant, $enumConstant>,
        $pattern.$java<_constant, $enumConstant>, $enum.$part,
        $member.$named<$enumConstant>, $declared<_constant,$enumConstant>, $type.$part  {

    public Predicate<_constant> constraint = t->true;

    public $comment<JavadocComment>javadoc = $comment.javadocComment();
    public $annoRefs annos = $annoRefs.of();
    public $name name = $name.of("$enumConstantName$"); //name required
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
        return of( _constant.of(enumConstant) );
    }

    public static $enumConstant of( EnumConstantDeclaration ecd ){
        return of( _constant.of(ecd));
    }

    public static $enumConstant of( _constant _ec ){
        $enumConstant ec = new $enumConstant();
        if( _ec.hasAnnoExprs() ) {
            ec.annos = $annoRefs.of(_ec.getAnnoExprs());
        }
        if( _ec.hasJavadoc()) {
            ec.javadoc = $comment.javadocComment(_ec.getJavadoc());
        }
        ec.name = $name.of(_ec.getName());
        if( _ec.hasArgs() ) {
            _ec.listArgs().forEach(a -> ec.args.add($ex.of(a)));
        }
        if( _ec.hasFields() ) {
            _ec.listFields().forEach(f -> ec.fields.add($field.of(f)));
        }
        if( _ec.hasMethods() ) {
            _ec.listMethods().forEach(m -> ec.methods.add($method.of(m)));
        }
        return ec;
    }

    public static $enumConstant of(Predicate<_constant> constraint ){
        return new $enumConstant().$and(constraint);
    }

    public static $enumConstant of($part...parts){
        return new $enumConstant(parts);
    }

    public static $enumConstant.Or or( _constant... _protos ){
        $enumConstant[] arr = new $enumConstant[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $enumConstant.of( _protos[i]);
        }
        return or(arr);
    }

    public static $enumConstant.Or or( EnumConstantDeclaration... _protos ){
        $enumConstant[] arr = new $enumConstant[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $enumConstant.of( _protos[i]);
        }
        return or(arr);
    }

    public static $enumConstant.Or or( $enumConstant...$tps ){
        return new $enumConstant.Or($tps);
    }

    public static $enumConstant as(EnumConstantDeclaration ecd ){
        return as(_constant.of(ecd));
    }

    public static $enumConstant as(String... constant ){
        return as(_constant.of(constant));
    }

    public static $enumConstant as( _constant _ec ){
        $enumConstant ec = new $enumConstant();
        if( _ec.hasAnnoExprs() ) {
            ec.annos = $annoRefs.as(_ec.getAnnoExprs());
        } else{
            ec.annos = $annoRefs.none();
        }
        if( _ec.hasJavadoc()) {
            ec.javadoc = $comment.javadocComment(_ec.getJavadoc());
        }
        ec.name = $name.as(_ec.getName());
        if( _ec.hasArgs() ) {
            _ec.listArgs().forEach(a -> ec.args.add($ex.of(a)));
            ec.$and(c-> {
                if( c.listArgs().size() != _ec.listArgs().size()){
                    //System.out.println( "Diff Arg Size " );
                    return false;
                }
                for(int i = 0; i < c.listArgs().size(); i++){
                    if( !ec.args.get(i).matches(c.getArg(i).ast())){
                        return false;
                    }
                }
                return true;
            });
        }
        if( _ec.hasFields() ) {
            _ec.listFields().forEach(f -> ec.fields.add($field.as(f)));
        }
        if( _ec.hasMethods() ) {
            _ec.listMethods().forEach(m -> ec.methods.add($method.as(m)));
        }
        //make sure they have EXACTLY this many arguments, fields and methods
        ec.$and( c -> c.listArgs().size() == _ec.listArgs().size() );
        ec.$and( c -> c.listFields().size() == _ec.listFields().size() );
        ec.$and( c -> c.listMethods().size() == _ec.listMethods().size() );

        return ec;
    }

    public static $enumConstant not($part...parts){
        $enumConstant $ec = of();
        $ec.$not(parts);
        return $ec;
    }

    $enumConstant(){
    }

    public $enumConstant($part...parts){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $annoRefs){
                this.annos = ($annoRefs)parts[i];
            }
            if( parts[i] instanceof $annoRef){
                this.annos.add(($annoRef)parts[i]);
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
            if( parts[i] instanceof $name ){
                this.name = ($name)parts[i];
            }
        }
    }

    public String toString(){
        if(this.isMatchAny() ){
            return "$enumConstant{ $ANY$ }";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("$enumConstant{").append(System.lineSeparator());

        if(this.javadoc.isMatchAny()){
            sb.append( Text.indent(this.javadoc.toString()));
        }
        if(!this.annos.isMatchAny()){
            sb.append( Text.indent(this.annos.toString()));
        }
        if(! this.name.isMatchAny()){
            sb.append( Text.indent(this.name.toString()));
        }
        if(! this.args.isEmpty() ){
            sb.append(Text.indent( "$args{ ") );
            for(int i=0;i<args.size();i++){
                if( i > 0 ){
                    sb.append(", ");
                }
                sb.append(this.args.get(i).toString());
            }
            sb.append(Text.indent("}"));
        }
        if(! this.fields.isEmpty()){
            this.fields.forEach(f -> sb.append(Text.indent(f.toString()) ));
        }
        if(! this.methods.isEmpty()){
            this.methods.forEach(m -> sb.append(Text.indent(m.toString()) ));
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public $enumConstant $(String target, String $paramName) {

        this.annos.$(target, $paramName);
        this.fields.forEach(f-> f.$(target, $paramName));
        this.javadoc.$(target, $paramName);
        this.methods.forEach(m-> m.$(target, $paramName));
        this.args.forEach(a -> a.$(target, $paramName));
        this.name = this.name.$(target, $paramName);
        return this;
    }

    @Override
    public $enumConstant $hardcode(Translator translator, Tokens kvs) {
        this.annos.$hardcode(translator, kvs);
        this.fields.forEach(f-> f.$hardcode(translator, kvs));
        this.javadoc.$hardcode(translator, kvs);
        this.methods.forEach(m-> m.$hardcode(translator, kvs));
        this.args.forEach( a-> a.$hardcode(translator,kvs));
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

    public boolean match( _constant _e){
        return select(_e) != null;
    }

    public boolean matches(String...code){
        try{
            return matches(_constant.of(code));
        }catch(Exception e){
            return false;
        }
    }

    public boolean matches(EnumConstantDeclaration coid ){
        if( coid != null ){
            return select(_constant.of(coid)) != null;
        }
        return false;
    }

    public boolean matches( _constant _e){
        return select(_e) != null;
    }

    @Override
    public Select select(_constant instance) {

        if( !this.constraint.test(instance)){
            return null;
        }
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

    /**
     * Adds a NOT constraint to the {@link #constraint} based on one or more $method.$part
     * @param parts
     * @return
     */
    public $enumConstant $not(final $part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $annoRef){
                final $annoRef $fa = (($annoRef)parts[i]);
                Predicate<_constant> pf = f-> $fa.countIn(f) > 0;
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $name){
                final $name $fn = (($name)parts[i]);
                Predicate<_constant> pf = f-> $fn.matches(f.getName());
                $and( pf.negate() );
            }
            else if(parts[i] instanceof $comment ){
                final $comment $fj = (($comment)parts[i]);
                Predicate<_constant> pf = f-> $fj.matches(f.getJavadoc());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $method){
                final $method $fj = (($method)parts[i]);
                Predicate<_constant> pf = f-> !f.listMethods(m -> $fj.matches(m)).isEmpty();
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $field){
                final $field $fj = (($field)parts[i]);
                Predicate<_constant> pf = f-> !f.listFields(ff -> $fj.matches(ff)).isEmpty();
                $and( pf.negate() );
            }
        }
        return this;
    }

    public static $pattern.$tokens selectArgs(List<$ex> $protoArgs, _constant _ec ){
        Map<$ex, List<$ex.Select>> selectMap = new HashMap<>();

        for(int i=0;i<$protoArgs.size(); i++) {
            final $ex t = $protoArgs.get(i);
            List<$ex.Select>matches = new ArrayList<>();
            _ec.listArgs().forEach(c ->{
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
        $pattern.$tokens all = $pattern.$tokens.of();
        selectMap.values().forEach( ls -> ls.forEach( s-> all.putAll(s.tokens()) ));
        all.remove("type");
        all.remove("name");
        return all;
    }


    @Override
    public $enumConstant $and(Predicate<_constant> constraint) {
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    @Override
    public $comment<JavadocComment> get$javadoc() {
        return javadoc;
    }

    public $enumConstant $javadoc(Predicate<JavadocComment> javadocMatchFn ){
        this.javadoc = $comment.javadocComment(javadocMatchFn);
        return this;
    }

    public $enumConstant $javadoc(_javadocComment javadocComment ){
        this.javadoc = $comment.javadocComment(javadocComment);
        return this;
    }

    public $enumConstant $javadoc($comment<JavadocComment> javadocComment ){
        this.javadoc = javadocComment;
        return this;
    }

    @Override
    public $name get$Name(){
        return this.name;
    }

    public $enumConstant $name(Predicate<String> nameMatchFn){
        this.name = $name.of(nameMatchFn);
        return this;
    }

    public $enumConstant $name(String name ){
        this.name = $name.of(name);
        return this;
    }

    public $enumConstant $name($name name ){
        this.name = name;
        return this;
    }

    public $enumConstant $annos(Predicate<_annoExprs> annosMatchFn){
        this.annos.$and(annosMatchFn);
        return this;
    }

    public $enumConstant $annos($annoRefs $as ){
        this.annos = $as;
        return this;
    }

    public $enumConstant $annos($annoRef... $a){
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
            return select( _constant.of((EnumConstantDeclaration)candidate)) != null;
        }
        return false;
    }

    @Override
    public _constant firstIn(Node astStartNode, Predicate<_constant> nodeMatchFn) {
        Optional<Node> oc = astStartNode.stream().filter(n ->
                (n instanceof EnumConstantDeclaration)
                && match(n)
                && nodeMatchFn.test( _constant.of( (EnumConstantDeclaration)n)) ).findFirst();
        if( oc.isPresent()){
            return _constant.of( (EnumConstantDeclaration)oc.get() );
        }
        return null;
    }

    @Override
    public Select selectFirstIn(Node astNode ) {
        Optional<Node> oc = astNode.stream().filter(n ->
                n instanceof EnumConstantDeclaration
                        && match(n) ).findFirst();
        if( oc.isPresent()){
            return select( _constant.of( (EnumConstantDeclaration)oc.get() ) );
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
            _constant _e = _constant.of( c );
            Select sel = select(_e);
            if( sel != null && selectMatchFn.test(sel)){
                found.add(sel);
            }
        });
        return found;
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_constant> nodeMatchFn, Consumer<_constant> nodeActionFn) {
        astNode.walk(EnumConstantDeclaration.class, c->{
            _constant _e = _constant.of( c );
            if( match(_e) && nodeMatchFn.test(_e)){
                nodeActionFn.accept(_e);
            }
        });
        return astNode;
    }

    @Override
    public Class<_constant> _modelType() {
        return _constant.class;
    }

    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $enumConstant{

        final List<$enumConstant>ors = new ArrayList<>();

        public Or($enumConstant...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $enumConstant $hardcode(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.$hardcode(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$enumConstant.Or{");
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
        public $enumConstant.Select select(_constant astNode){
            $enumConstant $a = whichMatch(astNode);
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
        public $enumConstant whichMatch(_constant ae){
            if( !this.constraint.test( ae ) ){
                return null;
            }
            Optional<$enumConstant> orsel  = this.ors.stream().filter( $p-> $p.match(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }

    /**
     * The selected Class
     */
    public static class Select implements select_java<_constant>{
        public _constant selected;
        public $tokens tokens;

        public Select( _constant _c, $tokens tokens){
            this.selected = _c;
            this.tokens = tokens;
        }

        @Override
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public _constant _node() {
            return selected;
        }
    }
}
