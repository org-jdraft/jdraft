package org.jdraft.pattern;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

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
public class $interface
        implements
        //$pattern<_interface, $interface>,
        $pattern.$java<_interface, $interface>, $member.$named<$interface>,
        $declared<_interface,$interface>, $withAnnoRefs {

    public Predicate<_interface> constraint = t->true;

    public $package packageDecl = $package.of();
    public List<$import> imports = new ArrayList<>();
    public $comment<JavadocComment>javadoc = $comment.javadocComment();
    public $annoRefs annos = $annoRefs.of();
    public $modifiers modifiers = $modifiers.of();
    public $typeParameters typeParameters = $typeParameters.of();
    public $name name = $name.of("$interfaceName$"); //name required

    public List<$field> fields = new ArrayList<>();
    public List<$method> methods = new ArrayList<>();

    public List<$typeRef> extend = new ArrayList<>();

    //nested types???

    /** marker interface for member entities that are part of the class */
    public interface $part{ }

    public static $interface of(){
        return new $interface();
    }

    public static $interface of(Predicate<_interface> constraint ){
        return new $interface().$and(constraint);
    }

    public static $interface of($part part){ return of( new $part[]{part}); }

    public static $interface of($part...parts){
        return new $interface(parts);
    }

    public static $interface of( Class clazz){
        return of( _interface.of(clazz));
    }
    public static $interface of(ClassOrInterfaceDeclaration coid ){
        return of( _interface.of(coid));
    }

    public static $interface of(String code){
        return of( new String[]{ code });
    }

    public static $interface of(String code, String code2){
        return of( new String[]{ code, code2});
    }

    public static $interface of(String...code){
        return of( _interface.of(code));
    }
    /*
    public static $interface of( Object anonymousObjectBody){
        $interface $i = of( _interface.of("$interfaceName$", anonymousObjectBody, Thread.currentThread().getStackTrace()[2]));

        //has$annos.at_$Process(anonymousObjectBody.getClass(), $i);
        return $i;
    }

    public static $interface of( String name, Object anonymousObjectBody ){
        $interface $i =  of( _interface.of(name, anonymousObjectBody, Thread.currentThread().getStackTrace()[2]));
        //has$annos.at_$Process(anonymousObjectBody.getClass(), $i);
        return $i;
    }
    */
    public static $interface of( _interface _c ){
        $interface $c = of();

        if( _c.isTopLevel() ){
            $c.$package( _c.getPackageName() );
            $c.$imports( _c.getImports() );
        }
        List<Node> nots = new ArrayList<>();

        //remove _$not things
        _c.toDeclared(d -> d.hasAnno(_$not.class), d-> {
            System.out.println("NODE" +  d + d.getClass());
            if( d instanceof _field ){
                ((_field) d).getFieldDeclaration().remove();
                nots.add( ((_field) d).getFieldDeclaration() );
                System.out.println("Not Field "+ d);
            } else {
                d.node().remove(); //remove so we dont
                nots.add((BodyDeclaration) d.node());
            }
        } );

        $c.$javadoc(_c.getJavadoc());
        _c.toAnnos(a-> $c.annos.add($annoRef.of(a)));
        $c.modifiers = $modifiers.of(_c.getModifiers());
        $c.$name(_c.getSimpleName());
        _c.getTypeParams().toEach(tp-> $c.typeParameters.$add($typeParameter.of(tp)));
        _c.listAstExtends().forEach(i -> $c.$extend(i));
        _c.toFields(f-> $c.fields.add($field.of(f)));
        _c.toMethods(m -> $c.$methods($method.of(m)));
        _c.toInnerTypes(n -> {
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
            //$member.of( nots ).forEach( $m -> $c.$not(($part)$m) );
        });
        //for all nots add them
        List<$member> $ms = new ArrayList<>();
        for(int i=0;i<nots.size();i++){
            $member $m =  $member.of( (BodyDeclaration)nots.get(i) );
            $c.$not( ($part)$m);
        }
        return $c;
    }

    public static $interface.Or or( _interface... _protos ){
        $interface[] arr = new $interface[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $interface.of( _protos[i]);
        }
        return or(arr);
    }

    public static $interface.Or or( ClassOrInterfaceDeclaration... _protos ){
        $interface[] arr = new $interface[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $interface.of( _protos[i]);
        }
        return or(arr);
    }
    /**
     * Builds a Or matching pattern for many different or patterns
     * @param $as
     * @return
     */
    public static $interface.Or or( $interface...$as ){
        return new Or($as);
    }

    /**
     *
     * @param parts
     * @return
     */
    public static $interface not( $part...parts){
        $interface $i = of();
        $i.$not(parts);
        return $i;
    }

    private $interface(){
    }

    public $interface($part...parts){
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
            if( parts[i] instanceof $import ){
                this.imports.add( ($import)parts[i]);
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
            if( parts[i] instanceof $name ){
                this.name = ($name)parts[i];
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
    public $interface $(String target, String $paramName) {
        this.annos.$(target, $paramName);
        this.fields.forEach(f-> f.$(target, $paramName));
        this.imports.forEach( i-> i.$(target, $paramName));
        this.javadoc.$(target, $paramName);
        this.methods.forEach(m-> m.$(target, $paramName));
        this.modifiers.$(target, $paramName);
        this.name = this.name.$(target, $paramName);
        this.packageDecl = this.packageDecl.$(target, $paramName);
        this.typeParameters = this.typeParameters.$(target, $paramName);
        this.extend.forEach(e-> e.$(target, $paramName));

        //still need nests
        return this;
    }

    @Override
    public $interface $hardcode(Translator translator, Tokens kvs) {

        this.annos.$hardcode(translator, kvs);
        this.fields.forEach(f-> f.$hardcode(translator, kvs));
        this.imports.forEach( i-> i.$hardcode(translator, kvs));
        this.javadoc.$hardcode(translator, kvs);
        this.methods.forEach(m-> m.$hardcode(translator, kvs));
        this.modifiers.$hardcode(translator, kvs);
        this.name = this.name.hardcode$(translator, kvs);
        this.packageDecl = this.packageDecl.$hardcode(translator, kvs);
        this.typeParameters = this.typeParameters.$hardcode(translator, kvs);
        this.extend.forEach( i-> i.$hardcode(translator, kvs));
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
            return matches((ClassOrInterfaceDeclaration)Ast.typeDeclaration(clazz));
        }catch(Exception e){
            return false;
        }
    }

    public boolean matches(String...code ){
        try{
            return matches(_interface.of(code));
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

    public boolean matches( _codeUnit _c){
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
        tokens = $tokens.to( tokens, ()-> this.typeParameters.parse(instance.getTypeParams()) );

        tokens = $tokens.to( tokens, ()-> $type.selectExtends(this.extend, instance) );

        tokens = $tokens.to( tokens, ()-> $type.selectMethods(this.methods, instance ) );
        tokens = $tokens.to( tokens, ()-> $type.selectFields(this.fields, instance ) );

        //nests
        if( tokens != null ){
            return new Select(instance, tokens);
        }
        return null;
    }

    public String toString(){
        if(this.isMatchAny() ){
            return "$interface{ $ANY$ }";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("$interface{").append(System.lineSeparator());
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
        if( ! this.typeParameters.isMatchAny()){
            sb.append( Text.indent(this.typeParameters.toString()));
        }
        if(! this.name.isMatchAny()){
            sb.append( Text.indent(this.name.toString()));
        }
        if(! this.extend.isEmpty()){
            sb.append(Text.indent( "$extend{ ") );
            for(int i=0;i<extend.size();i++){
                if( i > 0 ){
                    sb.append(", ");
                }
                sb.append(this.extend.get(i).toString());
            }
            sb.append(Text.indent("}"));
        }
        if(! this.fields.isEmpty()){
            this.fields.forEach(f -> sb.append(Text.indent(f.toString()) ));
        }
        if(! this.methods.isEmpty()){
            this.methods.forEach(m -> sb.append(Text.indent(m.toString()) ));
        }
        //nests
        sb.append("}");
        return sb.toString();
    }

    /**
     *
     * @param parts
     * @return
     */
    public $interface $not( $part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $annoRef){
                final $annoRef $fa = (($annoRef)parts[i]);
                Predicate<_interface> pf = an-> an.getAnno(a ->$fa.matches(a) ) != null;
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $annoRefs){
                final $annoRefs $fa = (($annoRefs)parts[i]);
                Predicate<_interface> pf = an-> $fa.matches(an.getAnnos());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $typeParameters){
                final $typeParameters $fa = (($typeParameters)parts[i]);
                Predicate<_interface> pf = an-> $fa.matches(an.getTypeParams());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $typeParameter){
                final $typeParameter $fa = (($typeParameter)parts[i]);
                Predicate<_interface> pf = an-> an.getTypeParam(a ->$fa.matches(a) ) != null;
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $modifiers ) {
                final $modifiers $fa = (($modifiers) parts[i]);
                Predicate<_interface> pf = f -> $fa.matches(f.getModifiers());
                $and(pf.negate());
            }
            else if(parts[i] instanceof $field ){
                final $field $fj = (($field)parts[i]);
                Predicate<_interface> aFn = a-> a.firstField(e->$fj.matches(e)) != null; //found one
                $and( aFn.negate() );
            }
            else if(parts[i] instanceof $method ){
                final $method $fj = (($method)parts[i]);
                Predicate<_interface> aFn = a-> a.firstMethod(e->$fj.matches(e)) != null; //found one
                $and( aFn.negate() );
            }
            else if( parts[i] instanceof $import) {
                final $import $fj = (($import)parts[i]);
                Predicate<_interface> aFn = a-> a.firstImport(im->$fj.matches(im)) != null; //found one
                $and( aFn.negate() );
            }
            else if( parts[i] instanceof $package ) {
                final $package $fa = (($package) parts[i]);
                Predicate<_interface> pf = f -> $fa.matches(f.getPackageName());
                $and(pf.negate());
            }
            else if( parts[i] instanceof $name){
                final $name $fn = (($name)parts[i]);
                Predicate<_interface> pf = f-> $fn.matches(f.getName());
                $and( pf.negate() );
            }
            else if(parts[i] instanceof $comment ){
                final $comment $fj = (($comment)parts[i]);
                Predicate<_interface> pf = f-> $fj.matches(f.getJavadoc());
                $and( pf.negate() );
            }
            //Nested classes
            //doesnt do extend, implement
        }
        return this;
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

    public $interface $javadoc(_javadocComment _jd){
        if( _jd != null ){
            this.javadoc = $comment.javadocComment(_jd);
        } else{
            this.javadoc = $comment.javadocComment();
        }
        return this;
    }

    @Override
    public $comment<JavadocComment> get$javadoc() {
        return javadoc;
    }

    public $interface $javadoc($comment<JavadocComment> javadocComment ){
        this.javadoc = javadocComment;
        return this;
    }

    public $interface $extend(ClassOrInterfaceType... coit){
        Arrays.stream(coit).forEach(c -> this.extend.add( $typeRef.of(c)));
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

    public $interface $imports( _imports _is ){
        return $imports( _is.list());
    }

    public $interface $imports( List<_import> _is){
        _is.forEach( i -> this.imports.add($import.of(i)));
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

    public $interface $package(String packageName){
        return $package( $package.of(packageName));
    }

    public $interface $package($package $p ){
        this.packageDecl = $p;
        return this;
    }

    public $interface $package(Predicate<PackageDeclaration> packageMatchFn){
        this.packageDecl = $package.of(packageMatchFn);
        return this;
    }

    @Override
    public $name get$Name(){
        return this.name;
    }

    public $interface $name(Predicate<String> nameMatchFn){
        this.name = $name.of(nameMatchFn);
        return this;
    }

    public $interface $name(String name ){
        this.name = $name.of(name);
        return this;
    }

    public $interface $name($name name ){
        this.name = name;
        return this;
    }

    @Override
    public $annoRefs get$Annos(){
        return this.annos;
    }

    public $interface $annos(Predicate<_annos> annosMatchFn){
        this.annos.$and(annosMatchFn);
        return this;
    }

    public $interface $annos($annoRefs $as ){
        this.annos = $as;
        return this;
    }

    public $interface $annos($annoRef... $a){
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

    public $interface $typeParameters(Predicate<_typeParams> _tpMatchFn){
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
    public Class<_interface> _modelType() {
        return _interface.class;
    }



    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $interface{

        final List<$interface>ors = new ArrayList<>();

        public Or($interface...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $interface $hardcode(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.$hardcode(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$interface.Or{");
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
        public $interface.Select select(_interface astNode){
            $interface $a = whichMatch(astNode);
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
        public $interface whichMatch(_interface ae){
            if( !this.constraint.test( ae ) ){
                return null;
            }
            Optional<$interface> orsel  = this.ors.stream().filter( $p-> $p.match(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
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
