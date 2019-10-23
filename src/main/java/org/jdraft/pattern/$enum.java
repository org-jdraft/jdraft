package org.jdraft.pattern;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jdraft.*;

/**
 * Note... at the moment this is NOT a template... should it be??
 */
public class $enum
        implements $pattern<_enum, $enum>, $pattern.$java<_enum, $enum>, $member.$named<$enum>, $declared<_enum,$enum>,
        has$Annos {

    public Predicate<_enum> constraint = t->true;

    public $package packageDecl = $package.of();
    public List<$import> imports = new ArrayList<>();
    public $comment<JavadocComment>javadoc = $comment.javadocComment();
    public $annos annos = $annos.of();
    public $modifiers modifiers = $modifiers.of();
    public $name name = $name.of("$enumName$"); //name required

    //body parts
    public List<$constructor> ctors = new ArrayList<>();
    public List<$field> fields = new ArrayList<>();
    public List<$method> methods = new ArrayList<>();
    public List<$initBlock> initBlocks = new ArrayList<>();
    public List<$enumConstant> enumConstants = new ArrayList<>();

    public List<$typeRef> implement = new ArrayList<>();

    //nested types???

    /** marker interface for member entities that are part of the class */
    public interface $part{ }

    public static $enum of(){
        return new $enum();
    }

    public static $enum of(Predicate<_enum> constraint ){
        return new $enum().$and(constraint);
    }

    public static $enum of($part part){ return of(new $part[]{part}); }

    public static $enum of($part...parts){
        return new $enum(parts);
    }

    public static $enum of( Object anonymousObjectBody){
        $enum $e = of( _enum.of("$enumName$", anonymousObjectBody, Thread.currentThread().getStackTrace()[2]));
        //handle @_$ (parameterize) annotations
        //has$annos.at_$Process(anonymousObjectBody.getClass(), $e);
        return $e;
    }

    public static $enum of( String name, Object anonymousObjectBody ){
        $enum $e = of( _enum.of(name, anonymousObjectBody, Thread.currentThread().getStackTrace()[2]));

        //handle @_$ (parameterize) annotations
        //has$annos.at_$Process(anonymousObjectBody.getClass(), $e);
        return $e;
    }

    public static $enum of( EnumDeclaration ed){
        return of( _enum.of(ed));
    }

    public static $enum of( _enum _e ){
        $enum $e = of();
        if( _e.isTopLevel() ){
            $e.$package( _e.getPackage() );
            $e.$imports( _e.getImports() );
        }

        List<Node>nots = new ArrayList<>();

        //remove _$not things
        _e.forDeclared( d -> d.hasAnno(_$not.class), d-> {
            //System.out.println("NODE" +  d + d.getClass());
            if( d instanceof _field ){
                ((_field) d).getFieldDeclaration().remove();
                nots.add( d.ast() );
                //System.out.println("Field "+ d);
            } else {
                d.ast().remove(); //remove so we dont
                nots.add((BodyDeclaration) d.ast());
            }
        } );


        $e.$javadoc(_e.getJavadoc());
        _e.forAnnos(a-> $e.annos.add($anno.of(a)));
        $e.modifiers = $modifiers.of(_e.getModifiers());
        $e.$name(_e.getSimpleName());
        _e.listImplements().forEach(i -> $e.$implement(i));
        _e.forConstants(cn -> $e.$constant($enumConstant.of(cn)));
        _e.forInitBlocks(ib -> $e.initBlocks.add($initBlock.of(ib.ast())));
        _e.forConstructors(ct -> $e.ctors.add($constructor.of(ct)));
        _e.forFields(f-> $e.fields.add($field.of(f)));
        _e.forMethods(m -> $e.$methods($method.of(m)));
        _e.forNests( n -> {
            if( n instanceof _class) {
                $e.$hasChild( $class.of((_class)n) );
            }
            if( n instanceof _enum) {
                $e.$hasChild( $enum.of((_enum)n) );
            }
            if( n instanceof _interface) {
                $e.$hasChild( $interface.of((_interface)n) );
            }
            if( n instanceof _annotation) {
                $e.$hasChild( $annotation.of((_annotation)n) );
            }
        });

        //handle @_$not annotated $patterns
        for(int i=0;i<nots.size();i++){
            if( nots.get(i) instanceof VariableDeclarator ){
                $member $m = $field.of((VariableDeclarator) nots.get(i));
                $e.$not(($part) $m);
            } else {
                $member $m = $member.of((BodyDeclaration) nots.get(i));
                $e.$not(($part) $m);
            }
        }

        return $e;
    }

    public static $enum.Or or( _enum... _protos ){
        $enum[] arr = new $enum[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $enum.of( _protos[i]);
        }
        return or(arr);
    }

    public static $enum.Or or( EnumDeclaration... _protos ){
        $enum[] arr = new $enum[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $enum.of( _protos[i]);
        }
        return or(arr);
    }


    /**
     * Builds a Or matching pattern for many different or patterns
     * @param $as
     * @return
     */
    public static $enum.Or or( $enum...$as ){
        return new Or($as);
    }

    public static $enum not( $part...parts ){
        $enum $e = of();
        $e.$not(parts);
        return $e;
    }

    private $enum(){
    }

    public $enum($part...parts){
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
            if( parts[i] instanceof $constructor ){
                this.ctors.add (($constructor)parts[i]);
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
                this.modifiers = $modifiers.of( this.modifiers, ($modifiers)parts[i]);
                //$modifiers ms = ($modifiers)parts[i];
                //this.modifiers.mustInclude.addAll(ms.mustInclude);
                //this.modifiers.mustExclude.addAll(ms.mustExclude);
                //this.modifiers.$and(ms.constraint);
            }
            if( parts[i] instanceof $enumConstant ){
                this.enumConstants.add( ($enumConstant)parts[i] );
            }
            if( parts[i] instanceof $name ){
                this.name = ($name)parts[i];
            }
            if( parts[i] instanceof $package ){
                this.packageDecl = ($package)parts[i];
            }
            //Need constant
            //Nested classes
        }
    }

    public $enum $not( $part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $anno ){
                final $anno $fa = (($anno)parts[i]);
                Predicate<_enum> pf = an-> an.getAnno( a ->$fa.match(a) ) != null;
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $annos ){
                final $annos $fa = (($annos)parts[i]);
                Predicate<_enum> pf = an-> $fa.matches(an.getAnnos());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $modifiers ) {
                final $modifiers $fa = (($modifiers) parts[i]);
                Predicate<_enum> pf = f -> $fa.matches(f.getModifiers());
                $and(pf.negate());
            }
            else if(parts[i] instanceof $field ){
                final $field $fj = (($field)parts[i]);
                Predicate<_enum> aFn = a-> a.getField(e->$fj.match(e)) != null; //found one
                $and( aFn.negate() );
            }
            else if(parts[i] instanceof $method ){
                final $method $fj = (($method)parts[i]);
                Predicate<_enum> aFn = a-> a.getMethod(e->$fj.match(e)) != null; //found one
                $and( aFn.negate() );
            }
            else if(parts[i] instanceof $enumConstant ){
                final $enumConstant $fj = (($enumConstant)parts[i]);
                Predicate<_enum> aFn = a-> a.getConstant(e->$fj.match(e)) != null; //found one
                $and( aFn.negate() );
            }
            else if(parts[i] instanceof $constructor ){
                final $constructor $fj = (($constructor)parts[i]);
                Predicate<_enum> aFn = a-> a.getConstructor(e->$fj.match(e)) != null; //found one
                $and( aFn.negate() );
            }
            else if(parts[i] instanceof $initBlock){
                final $initBlock $fj = (($initBlock)parts[i]);
                Predicate<_enum> aFn = a-> a.getInitBlock(e->$fj.match(e)) != null; //found one
                $and( aFn.negate() );
            }
            else if( parts[i] instanceof $import) {
                final $import $fj = (($import)parts[i]);
                Predicate<_enum> aFn = a-> a.getImport(im->$fj.match(im)) != null; //found one
                $and( aFn.negate() );
            }
            else if( parts[i] instanceof $package ) {
                final $package $fa = (($package) parts[i]);
                Predicate<_enum> pf = f -> $fa.matches(f.getPackage());
                $and(pf.negate());
            }
            else if( parts[i] instanceof $name){
                final $name $fn = (($name)parts[i]);
                Predicate<_enum> pf = f-> $fn.matches(f.getName());
                $and( pf.negate() );
            }
            else if(parts[i] instanceof $comment ){
                final $comment $fj = (($comment)parts[i]);
                Predicate<_enum> pf = f-> $fj.matches(f.getJavadoc());
                $and( pf.negate() );
            }
            //Nested classes
            //doesnt do extend, implement
        }
        return this;
    }

    @Override
    public $enum $(String target, String $paramName) {

        this.annos.$(target, $paramName);
        this.ctors.forEach( c-> c.$(target, $paramName));
        this.fields.forEach(f-> f.$(target, $paramName));
        this.imports.forEach( i-> i.$(target, $paramName));
        this.initBlocks.forEach( i-> i.$(target, $paramName));
        this.javadoc.$(target, $paramName);
        this.methods.forEach(m-> m.$(target, $paramName));
        this.modifiers.$(target, $paramName);
        this.name = this.name.$(target, $paramName);
        this.packageDecl = this.packageDecl.$(target, $paramName);

        this.enumConstants.forEach(c -> c.$(target, $paramName));
        this.implement.forEach( i-> i.$(target, $paramName));
        //still need nests

        return this;
    }

    @Override
    public $enum hardcode$(Translator translator, Tokens kvs) {
        this.annos.hardcode$(translator, kvs);
        this.ctors.forEach( c-> c.hardcode$(translator, kvs));
        this.fields.forEach(f-> f.hardcode$(translator, kvs));

        this.imports.forEach( i-> i.hardcode$(translator, kvs));
        this.initBlocks.forEach( i-> i.hardcode$(translator, kvs));
        this.javadoc.hardcode$(translator, kvs);
        this.methods.forEach(m-> m.hardcode$(translator, kvs));
        this.modifiers.hardcode$(translator, kvs);
        this.name = this.name.hardcode$(translator, kvs);
        this.packageDecl = this.packageDecl.hardcode$(translator, kvs);

        this.enumConstants.forEach(e-> e.hardcode$(translator, kvs));

        this.implement.forEach( i-> i.hardcode$(translator, kvs));

        //still need nests
        return this;
    }

    public String toString(){
        if(this.isMatchAny() ){
            return "$enum{ $ANY$ }";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("$enum{").append(System.lineSeparator());
        if( this.packageDecl != null && !this.packageDecl.isMatchAny() ){
            sb.append( Text.indent(this.packageDecl.toString())).append(System.lineSeparator());
        }
        if( !this.imports.isEmpty()){
            this.imports.forEach( i -> sb.append( Text.indent(i.toString()) ));
        }
        if(this.javadoc.isMatchAny()){
            sb.append( Text.indent(this.javadoc.toString()));
        }
        if(!this.annos.isMatchAny()){
            sb.append( Text.indent(this.annos.toString()));
        }
        if(! this.modifiers.isMatchAny()){
            sb.append( Text.indent(this.modifiers.toString()));
        }
        if(! this.name.isMatchAny()){
            sb.append( Text.indent(this.name.toString()));
        }
        if(! this.implement.isEmpty()){
            sb.append(Text.indent( "$implement{ ") );
            for(int i=0;i<implement.size();i++){
                if( i > 0 ){
                    sb.append(", ");
                }
                sb.append(this.implement.get(i).toString());
            }
            sb.append(Text.indent("}"));
        }
        if(! this.enumConstants.isEmpty() ){
            this.enumConstants.forEach(c -> sb.append(Text.indent(c.toString())) );
        }
        if(! this.fields.isEmpty()){
            this.fields.forEach(f -> sb.append(Text.indent(f.toString())) );
        }
        if(! this.initBlocks.isEmpty()){
            this.initBlocks.forEach(i -> sb.append(Text.indent(i.toString())) );
        }
        if(! this.ctors.isEmpty()){
            this.ctors.forEach(c -> sb.append(Text.indent(c.toString())) );
        }
        if(! this.methods.isEmpty()){
            this.methods.forEach(m -> sb.append(Text.indent(m.toString())) );
        }
        sb.append("}");
        //nests
        return sb.toString();
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
                 this.enumConstants.isEmpty() &&
                 this.javadoc.isMatchAny() &&
                 this.modifiers.isMatchAny() &&
                 this.packageDecl.isMatchAny() &&
                 this.implement.isEmpty();
            //NESTS
        } catch(Exception e){
            return false;
        }
    }

    public boolean match( _enum _e){
        return select(_e) != null;
    }

    public boolean matches(CompilationUnit cu){
        if( cu != null){
            if( cu.getTypes().size() == 1 && cu.getType(0) instanceof EnumDeclaration){
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
            return matches(Ast.enumDecl(clazz));
        }catch(Exception e){
            return false;
        }
    }

    public boolean matches(TypeDeclaration td ){
        if( td instanceof EnumDeclaration ){
            return matches( (EnumDeclaration) td);
        }
        return false;
    }

    public boolean matches(String...code){
        try{
            return matches( _enum.of(code));
        }catch(Exception e){
            return false;
        }
    }

    public boolean matches(EnumDeclaration coid ){
        if( coid != null ){
            return select(_enum.of(coid)) != null;
        }
        return false;
    }

    public boolean matches( _code _c){
        if( _c instanceof _enum){
            return matches( (_enum)_c);
        }
        return false;
    }

    public boolean matches( _enum _e){
        return select(_e) != null;
    }

    @Override
    public Select select(_enum instance) {

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
        tokens = $tokens.to( tokens, ()-> this.packageDecl.parse(instance.astCompilationUnit() ) );

        tokens = $tokens.to( tokens, ()-> this.annos.parse(instance));
        tokens = $tokens.to( tokens, ()-> this.javadoc.parse(instance ));
        tokens = $tokens.to( tokens, ()-> this.modifiers.parse(instance));
        tokens = $tokens.to( tokens, ()-> this.name.parse(instance.getName()));

        //ADDED THIS
        tokens = $tokens.to( tokens, ()-> selectEnumConstants(this.enumConstants, instance) );
        tokens = $tokens.to( tokens, ()-> $type.selectImplements(this.implement, instance) );

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

    public static $pattern.$tokens selectEnumConstants(List<$enumConstant> $protoConst, _enum _e ){
        Map<$enumConstant, List<$enumConstant.Select>> selectMap = new HashMap<>();

        for(int i=0;i<$protoConst.size(); i++) {
            final $enumConstant t = $protoConst.get(i);
            List<$enumConstant.Select>matches = new ArrayList<>();
            _e.listConstants().forEach( c ->{
                $enumConstant.Select sel = t.select( c);
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
    public $enum $and(Predicate<_enum> constraint) {
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    public $enum $javadoc(Predicate<JavadocComment> javadocMatchFn ){
        this.javadoc = $comment.javadocComment(javadocMatchFn);
        return this;
    }

    public $enum $modifiers($modifiers...$mods){
        this.modifiers = $modifiers.of($mods);
        return this;
    }

    public $enum $constant( String... $ec ){
        Arrays.stream( $ec ).forEach( ec -> this.enumConstants.add($enumConstant.of(ec)));
        return this;
    }

    public $enum $constant( $enumConstant... $ec ){
        Arrays.stream( $ec ).forEach( ec -> this.enumConstants.add(ec));
        return this;
    }

    public $enum $initBlock($initBlock... $ibs ){
        Arrays.stream($ibs).forEach(i -> this.initBlocks.add(i));
        return this;
    }
    //TODO other static blocks?

    public $enum $javadoc(_javadoc _jd ){
        if( _jd != null ) {
            this.javadoc = $comment.javadocComment(_jd);
        }else{
            this.javadoc = $comment.javadocComment();
        }
        return this;
    }

    @Override
    public $comment<JavadocComment> get$javadoc() {
        return javadoc;
    }

    public $enum $javadoc($comment<JavadocComment> javadocComment ){
        this.javadoc = javadocComment;
        return this;
    }

    public $enum $implement(ClassOrInterfaceType... clazz){
        Arrays.stream(clazz).forEach(c -> this.implement.add( $typeRef.of(c)));
        return this;
    }

    public $enum $implement(Class... clazz){
         Arrays.stream(clazz).forEach(c -> this.implement.add( $typeRef.of(c)));
         return this;
    }

    public $enum $implement(String...types){
        Arrays.stream(types).forEach(c -> this.implement.add( $typeRef.of(c)));
        return this;
    }


    public $enum $implement($typeRef...impl){
        Arrays.stream(impl).forEach(i -> this.implement.add(i));
        return this;
    }

    public $enum $imports(_import._imports _is){
        _is.forEach( i -> this.imports.add($import.of(i)));
        return this;
    }

    public $enum $imports($import...$is ){
        Arrays.stream($is).forEach( i -> this.imports.add(i));
        return this;
    }

    public $enum $imports(Class... clazzes ){
        Arrays.stream(clazzes).forEach( i -> this.imports.add($import.of(i)));
        return this;
    }

    public $enum $package(String packageName){
        return $package( $package.of(packageName));
    }

    public $enum $package($package $p ){
        this.packageDecl = $p;
        return this;
    }

    @Override
    public $name get$Name(){
        return this.name;
    }

    public $enum $name(Predicate<String> nameMatchFn){
        this.name = $name.of(nameMatchFn);
        return this;
    }

    public $enum $name(String name ){
        this.name = $name.of(name);
        return this;
    }

    public $enum $name($name name ){
        this.name = name;
        return this;
    }

    @Override
    public $annos get$Annos(){
        return this.annos;
    }

    public $enum $annos(Predicate<_anno._annos> annosMatchFn){
        this.annos.$and(annosMatchFn);
        return this;
    }

    public $enum $annos($annos $as ){
        this.annos = $as;
        return this;
    }

    public $enum $annos($anno... $a){
        this.annos.add($a);
        return this;
    }

    public $enum $methods($method...$ms ){
        Arrays.stream($ms).forEach(m-> this.methods.add(m));
        return this;
    }

    public $enum $fields($field...$fs){
        Arrays.stream($fs).forEach(f-> this.fields.add(f));
        return this;
    }

    public $enum $constructors($constructor...$cs){
        Arrays.stream($cs).forEach(c-> this.ctors.add(c));
        return this;
    }

    @Override
    public boolean match(Node candidate) {
        if(candidate instanceof EnumDeclaration){
            return select( _enum.of((EnumDeclaration)candidate)) != null;
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
        //its not an Enum
        return false;
    }

    @Override
    public _enum firstIn(Node astStartNode, Predicate<_enum> nodeMatchFn) {
        Optional<Node> oc = astStartNode.stream().filter(n ->
                (n instanceof EnumDeclaration)
                && match(n)
                && nodeMatchFn.test( _enum.of( (EnumDeclaration)n)) ).findFirst();
        if( oc.isPresent()){
            return _enum.of( (EnumDeclaration)oc.get() );
        }
        return null;
    }

    @Override
    public Select selectFirstIn(Node astNode ) {
        Optional<Node> oc = astNode.stream().filter(n ->
                n instanceof EnumDeclaration
                        && match(n) ).findFirst();
        if( oc.isPresent()){
            return select( _enum.of( (EnumDeclaration)oc.get() ) );
        }
        return null;
    }

    @Override
    public List<Select> listSelectedIn(Node astNode){
        return listSelectedIn(astNode, t->true);
    }

    public List<Select> listSelectedIn(Node astNode, Predicate<Select>selectMatchFn) {
        List<Select> found = new ArrayList<>();
        astNode.walk(EnumDeclaration.class, c->{
            _enum _e = _enum.of( c );
            Select sel = select(_e);
            if( sel != null && selectMatchFn.test(sel)){
                found.add(sel);
            }
        });
        return found;
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_enum> nodeMatchFn, Consumer<_enum> nodeActionFn) {
        astNode.walk(EnumDeclaration.class, c->{
            _enum _e = _enum.of( c );
            if( match(_e) && nodeMatchFn.test(_e)){
                nodeActionFn.accept(_e);
            }
        });
        return astNode;
    }

    @Override
    public Class<_enum> _modelType() {
        return _enum.class;
    }


    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $enum{

        final List<$enum>ors = new ArrayList<>();

        public Or($enum...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $enum.Or hardcode$(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.hardcode$(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$enum.Or{");
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
        public $enum.Select select(_enum astNode){
            $enum $a = whichMatch(astNode);
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
        public $enum whichMatch(_enum ae){
            if( !this.constraint.test( ae ) ){
                return null;
            }
            Optional<$enum> orsel  = this.ors.stream().filter( $p-> $p.match(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }

    /**
     * The selected Class
     */
    public static class Select implements select_java<_enum>{
        public _enum selected;
        public $tokens tokens;

        public Select( _enum _c, $tokens tokens){
            this.selected = _c;
            this.tokens = tokens;
        }

        @Override
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public _enum _node() {
            return selected;
        }
    }
}
