package org.jdraft.pattern;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
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
public class $annotation
        implements //$pattern<_annotation, $annotation>,
        $pattern.$java<_annotation, $annotation>, $member.$named<$annotation>,
        $declared<_annotation,$annotation>, has$Annos {

    public Predicate<_annotation> constraint = t->true;

    public $package packageDecl = $package.of();
    public List<$import> imports = new ArrayList<>();
    public $comment<JavadocComment>javadoc = $comment.javadocComment();
    public $annos annos = $annos.of();
    public $modifiers modifiers = $modifiers.of();
    public $name name = $name.of("$annotationName$"); //name required

    public List<$annotationEntry> annotationElements = new ArrayList<>();

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

    /** We need this as to not interfere with the anonymousBodyConstructor */
    public static $annotation of($part part){ return of( new $part[]{part}); }

    public static $annotation of($part...parts){
        return new $annotation(parts);
    }

    public static $annotation of( AnnotationDeclaration astAd ){
        return of( _annotation.of(astAd));
    }

    public static $annotation of( Object anonymousObjectBody){
        $annotation $a = of( _annotation.of("$annotationName$", anonymousObjectBody, Thread.currentThread().getStackTrace()[2]));
        //has$annos.at_$Process(anonymousObjectBody.getClass(), $a);
        return $a;
    }

    public static $annotation of( String name, Object anonymousObjectBody ){
        $annotation $a = of( _annotation.of(name, anonymousObjectBody, Thread.currentThread().getStackTrace()[2]));
        //has$annos.at_$Process(anonymousObjectBody.getClass(), $a);
        return $a;
    }

    public static $annotation of( _annotation _a ){
        $annotation $c = of();
        if( _a.isTopLevel() ){
            $c.$package( _a.getPackageName() );
            $c.$imports( _a.getImports() );
        }

        //not annotated elements
        List<Node>nots = new ArrayList<>();

        //remove _$not things
        _a.forDeclared( d -> d.hasAnno(_$not.class), d-> {
            //System.out.println("NODE" +  d + d.getClass());
            if( d instanceof _field ){
                ((_field) d).getFieldDeclaration().remove();
                nots.add( d.ast() );
                //System.out.println("Field "+ d);
            } else {
                d.ast().remove(); //remove so we dont
                nots.add(d.ast());
            }
        } );

        $c.$javadoc(_a.getJavadoc());
        _a.forAnnos(a-> $c.annos.add($anno.of(a)));
        $c.modifiers = $modifiers.of(_a.getModifiers());
        $c.$name(_a.getSimpleName());
        _a.forEntries(e -> $c.$elements($annotationEntry.of(e)));
        _a.forFields(f-> $c.fields.add($field.of(f)));

        _a.forInnerTypes(n -> {
            if( n instanceof _class) {
                $c.$hasChild( $class.of((_class)n) );
            }
            if( n instanceof _enum) {
                $c.$hasChild( $enum.of((_enum)n) );
            }
            if( n instanceof _interface) {
                $c.$hasChild( $interface.of((_interface)n) );
            }
            if( n instanceof _annotation) {
                $c.$hasChild( $annotation.of((_annotation)n) );
            }
        });

        for(int i=0;i<nots.size();i++){
            if( nots.get(i) instanceof VariableDeclarator ){
                $member $m = $field.of((VariableDeclarator) nots.get(i));
                $c.$not(($part) $m);
            } else {
                $member $m = $member.of((BodyDeclaration) nots.get(i));
                $c.$not(($part) $m);
            }
        }
        return $c;
    }

    public static $annotation.Or or( _annotation... _protos ){
        $annotation[] arr = new $annotation[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $annotation.of( _protos[i]);
        }
        return or(arr);
    }

    public static $annotation.Or or( AnnotationDeclaration... _protos ){
        $annotation[] arr = new $annotation[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $annotation.of( _protos[i]);
        }
        return or(arr);
    }

    /**
     * Builds a Or matching pattern for many different or patterns
     * @param $as
     * @return
     */
    public static $annotation.Or or( $annotation...$as ){
        return new Or($as);
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
                this.modifiers = $modifiers.of( this.modifiers, ($modifiers)parts[i]);
                //$modifiers ms = ($modifiers)parts[i];
                //this.modifiers.mustInclude.addAll(ms.mustInclude);
                //this.modifiers.mustExclude.addAll(ms.mustExclude);
                //this.modifiers.$and(ms.constraint);
            }
            if( parts[i] instanceof $name ){
                this.name = ($name)parts[i];
            }
            if( parts[i] instanceof $annotationEntry){
                this.annotationElements.add( ($annotationEntry)parts[i]);
            }
            if( parts[i] instanceof $package ){
                this.packageDecl = ($package)parts[i];
            }
            // $element
            //Nested classes
        }
    }

    public $annotation $not(final $annotation.$part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $anno ){
                final $anno $fa = (($anno)parts[i]);
                Predicate<_annotation> pf = an-> an.getAnno( a ->$fa.match(a) ) != null;
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $annos ){
                final $annos $fa = (($annos)parts[i]);
                Predicate<_annotation> pf = an-> $fa.matches(an.getAnnos());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $modifiers ) {
                final $modifiers $fa = (($modifiers) parts[i]);
                Predicate<_annotation> pf = f -> $fa.matches(f.getModifiers());
                $and(pf.negate());
            }
            else if(parts[i] instanceof $field ){
                final $field $fj = (($field)parts[i]);
                Predicate<_annotation> aFn = a-> a.getField(e->$fj.match(e)) != null; //found one
                $and( aFn.negate() );
            }
            else if( parts[i] instanceof $import) {
                final $import $fj = (($import)parts[i]);
                Predicate<_annotation> aFn = a-> a.getImport(im->$fj.match(im)) != null; //found one
                $and( aFn.negate() );
            }
            else if( parts[i] instanceof $package ) {
                final $package $fa = (($package) parts[i]);
                Predicate<_annotation> pf = f -> $fa.matches(f.getPackageName());
                $and(pf.negate());
            }
            else if( parts[i] instanceof $name){
                final $name $fn = (($name)parts[i]);
                Predicate<_annotation> pf = f-> $fn.matches(f.getName());
                $and( pf.negate() );
            }
            else if(parts[i] instanceof $comment ){
                final $comment $fj = (($comment)parts[i]);
                Predicate<_annotation> pf = f-> $fj.matches(f.getJavadoc());
                $and( pf.negate() );
            }
            else if(parts[i] instanceof $annotationEntry){
                final $annotationEntry $fj = (($annotationEntry)parts[i]);
                Predicate<_annotation> aFn = a-> a.getEntry(e->$fj.match(e)) != null;
                $and( aFn.negate() );
            }
        }
        return this;
    }

    /**
     * Adds a NOT constraint to the {@link #constraint} based on one or more $method.$part
     * @param parts
     * @return
     */
    public static $annotation not(final $annotation.$part...parts ){
        $annotation $a = of();
        $a.$not(parts);
        return $a;
    }


    @Override
    public $annotation $(String target, String $paramName) {

        this.annos.$(target, $paramName);
        this.fields.forEach(f-> f.$(target, $paramName));
        this.imports.forEach( i-> i.$(target, $paramName));
        this.javadoc.$(target, $paramName);
        this.modifiers.$(target, $paramName);
        this.name = this.name.$(target, $paramName);
        this.packageDecl = this.packageDecl.$(target, $paramName);
        this.annotationElements.forEach( ae -> ae.$(target, $paramName));
        //still need nests

        return this;
    }

    @Override
    public $annotation $hardcode(Translator translator, Tokens kvs) {
        this.annos.$hardcode(translator, kvs);
        this.fields.forEach(f-> f.$hardcode(translator, kvs));
        this.imports.forEach( i-> i.$hardcode(translator, kvs));
        this.annotationElements.forEach(e ->e.$hardcode(translator, kvs));
        this.javadoc.$hardcode(translator, kvs);
        this.modifiers.$hardcode(translator, kvs);
        this.name = this.name.hardcode$(translator, kvs);
        this.packageDecl = this.packageDecl.$hardcode(translator, kvs);
        //still need nests
        return this;
    }

    @Override
    public boolean isMatchAny(){
        try{
            return constraint.test(null) &&
                 this.annos.isMatchAny() &&
                 this.fields.isEmpty() &&
                 this.imports.isEmpty() &&
                 this.annotationElements.isEmpty() &&
                 this.javadoc.isMatchAny() &&
                 this.modifiers.isMatchAny() &&
                 this.packageDecl.isMatchAny();
                // nests

            //NESTS
        } catch(Exception e){
            return false;
        }
    }

    public boolean match( _annotation _a){
        return select(_a) != null;
    }

    public boolean matches( Class clazz){
        try {
            return matches(Ast.typeDecl(clazz));
        }catch(Exception e){
            return false;
        }
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

    public boolean matches(String...code ){
        try {
            return matches(_annotation.of(code));
        }catch(Exception e){
            return false;
        }
    }

    public boolean matches( _codeUnit _c){
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
        tokens = $tokens.to( tokens, ()-> selectAnnotationElements(this.annotationElements, instance));
        tokens = $tokens.to( tokens, ()-> $type.selectFields(this.fields, instance ) );

        //nests

        if( tokens != null ){
            return new Select(instance, tokens);
        }
        return null;
    }

    public String toString(){
        if(this.isMatchAny() ){
            return "$class{ $ANY$ }";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("$class{").append(System.lineSeparator());
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
        if( ! this.annotationElements.isEmpty()){
            this.annotationElements.forEach(e -> sb.append(Text.indent(e.toString()) ));
        }
        if(! this.name.isMatchAny()){
            sb.append( Text.indent(this.name.toString()));
        }
        if(! this.fields.isEmpty()){
            this.fields.forEach(f -> sb.append(Text.indent(f.toString()) ));
        }
        sb.append("}");
        //nests
        return sb.toString();
    }

    /* These are methods shared/used by all $type implementations */
    static $pattern.$tokens selectAnnotationElements(List<$annotationEntry> $protoTypes, _annotation _a){
        Map<$annotationEntry, List<$annotationEntry.Select>> selectMap = new HashMap<>();

        for(int i=0;i<$protoTypes.size(); i++) {
            final $annotationEntry t = $protoTypes.get(i);
            List<$annotationEntry.Select>matches = new ArrayList<>();
            _a.listElements().forEach( c ->{
                $annotationEntry.Select sel = t.select( c );
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
    public $annotation $and(Predicate<_annotation> constraint) {
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    public $annotation $modifiers($modifiers...$mods){
        this.modifiers = $modifiers.of($mods);
        return this;
    }

    @Override
    public $comment<JavadocComment> get$javadoc() {
        return javadoc;
    }

    public $annotation $javadoc(Predicate<JavadocComment> javadocMatchFn ){
        this.javadoc = $comment.javadocComment(javadocMatchFn);
        return this;
    }

    public $annotation $javadoc(_javadocComment _jd){
        if( _jd != null ){
            this.javadoc = $comment.javadocComment(_jd);
        } else{
            this.javadoc = $comment.javadocComment();
        }
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

    public $annotation $imports( _imports _is ){
        return $imports( _is.list());
    }

    public $annotation $imports( List<_import> _is){
        _is.forEach( i -> this.imports.add($import.of(i)));
        return this;
    }

    public $annotation $imports(Class... clazzes ){
        Arrays.stream(clazzes).forEach( i -> this.imports.add($import.of(i)));
        return this;
    }

    public $annotation $package(String packageName){
        return $package( $package.of(packageName));
    }


    public $annotation $package($package $p ){
        this.packageDecl = $p;
        return this;
    }

    @Override
    public $name get$Name(){
        return this.name;
    }

    public $annotation $name(Predicate<String> nameMatchFn){
        this.name = $name.of(nameMatchFn);
        return this;
    }

    public $annotation $name(String name ){
        this.name = $name.of(name);
        return this;
    }

    public $annotation $name($name name ){
        this.name = name;
        return this;
    }

    public $annos get$Annos(){
        return this.annos;
    }

    public $annotation $annos(Predicate<_annos> annosMatchFn){
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

    public $annotation $elements( $annotationEntry...$aes ){
        Arrays.stream($aes).forEach(a-> this.annotationElements.add(a));
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
    public Class<_annotation> _modelType() {
        return _annotation.class;
    }


    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $annotation{

        final List<$annotation>ors = new ArrayList<>();

        public Or($annotation...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $annotation $hardcode(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.$hardcode(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$annotation.Or{");
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
        public $annotation.Select select(_annotation astNode){
            $annotation $a = whichMatch(astNode);
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
        public $annotation whichMatch(_annotation ae){
            if( !this.constraint.test( ae ) ){
                return null;
            }
            Optional<$annotation> orsel  = this.ors.stream().filter( $p-> $p.match(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
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
