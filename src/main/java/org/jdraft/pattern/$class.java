package org.jdraft.pattern;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jdraft.*;
import org.jdraft.prototype._$;
import org.jdraft.text.Template;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

/**
 * Note... at the moment this is NOT a template... should it be??
 */
public class $class
        implements $pattern.$java<_class,$class>, $member.$named<$class>, $declared<_class,$class>, has$Annos, Template<_class> {

    public Predicate<_class> constraint = t->true;

    public $package packageDecl = $package.of();
    public List<$import> imports = new ArrayList<>();
    public $comment<JavadocComment>javadoc = $comment.javadocComment();
    public $annos annos = $annos.of();
    public $modifiers modifiers = $modifiers.of();
    public $typeParameters typeParameters = $typeParameters.of();
    public $name name = $name.of("$className$"); //name required

    //body parts
    public List<$constructor> ctors = new ArrayList<>();
    public List<$field> fields = new ArrayList<>();
    public List<$method> methods = new ArrayList<>();
    public List<$initBlock> initBlocks = new ArrayList<>();

    public $typeRef extend = $typeRef.of();
    public List<$typeRef> implement = new ArrayList<>();

    //nested types???
    public List<$type> nests = new ArrayList<>();

    /** marker interface for member entities that are part of the class */
    public interface $part{ }

    public static $class of( String signature, Object anonymousClass){
        return of( signature, anonymousClass, Thread.currentThread().getStackTrace()[2]);
    }

    private static $class of( String signature, Object anonymousClass, StackTraceElement ste){
        _class _c = _class.of(signature, anonymousClass, ste); //Thread.currentThread().getStackTrace()[2] );
        List<BodyDeclaration> nots = new ArrayList<>();

        //only _declared members can have annotations (i.e. @_$not )
        _c.forDeclared(d -> d.hasAnno(_$not.class), d -> {
            if( d instanceof _field ){
                nots.add(((_field) d).getFieldDeclaration());
                ((_field)d).getFieldDeclaration().remove(); //remove it from the AST so we dont treat it as an $and
            } else {
                nots.add((BodyDeclaration) d.ast());
                d.ast().remove(); //remove it from the AST so we dont treat it as an $and
            }
        });
        $class $c = of(_c);

        //remove the @_$not annotations
        nots.forEach(b ->
                //FIRST REMOVE THE _$not annotation we dont want to match against it
                b.getAnnotations().removeIf( a -> ((AnnotationExpr)a).getNameAsString().equals(_$not.class.getSimpleName())
                        || ((AnnotationExpr)a).getNameAsString().equals(_$not.class.getCanonicalName() ))  );
        List<$member> $mems = $member.of(nots);
        $mems.forEach($m -> $c.$not( ($class.$part)$m));

        //handle the @_$ parameterize annotations
        //has$annos.at_$Process( anonymousClass.getClass(), $c);

        return $c;
    }

    /**
     * creates a $class from the source of a runtime class (assuming the class is in the classpath)
     * @param clazz
     * @return
     */
    public static $class of( Class clazz){
        _class _c = _class.of(clazz);
        return of( _c );
    }

    public static $class of( String cl ){
        return of( new String[]{cl} );
    }
    public static $class of(String...cl ){
        return of( _class.of( cl) );
    }

    public static $class of( Object anonymousClass ){
        return of( "$className$", anonymousClass, Thread.currentThread().getStackTrace()[2]);
    }

    public static $class of( ClassOrInterfaceDeclaration coid ){
        return of( _class.of(coid));
    }

    public static $class of( _class _c ){
        $class $c = of();
        if( _c.isTopLevel() && (_c.getPackage() != null) ){
            $c.$package( _c.getPackage() );
            $c.$imports( _c.getImports() );
        }

        /** this section is about parameterizing class level @_$ annotations in the source code */
        parameterize$Annos : {
            //String parameterized = _$.Parameterize.toString(_c);
            //_c = _class.of( parameterized );
            _c = _$.Parameterize.update(_c);

        }
        /** end of parameterizing @_$ class level annotations in the source code */

        $c.$javadoc(_c.getJavadoc());
        _c.forAnnos(a-> $c.annos.add($anno.of(a)));
        $c.modifiers = $modifiers.of(_c.getModifiers());
        $c.$name(_c.getSimpleName());
        _c.getTypeParameters().forEach(tp-> $c.typeParameters.$add($typeParameter.of(tp)));
        if( _c.getExtends() != null) {
            $c.extend = $typeRef.of(_c.getExtends());
        }
        _c.listImplements().forEach(i -> $c.$implements(i));

        _c.forInitBlocks(ib -> $c.initBlocks.add($initBlock.of(ib.ast())));


        _c.forConstructors(ct -> $c.ctors.add($constructor.of(_$.Parameterize.update(ct))));
        _c.forFields(f-> $c.fields.add($field.of(_$.Parameterize.update(f))));
        _c.forMethods(m -> $c.$methods($method.of(_$.Parameterize.update(m))));

        //hmm this loses the order of things
        //_c.forConstructors(ct -> $c.ctors.add($constructor.of(_$.Parameterize.toString(ct))));
        //_c.forFields(f-> $c.fields.add($field.of(_$.Parameterize.toString(f))));
        //_c.forMethods(m -> $c.$methods($method.of(_$.Parameterize.toString(m))));
        /*
        _c.forConstructors(ct -> $c.ctors.add($constructor.of(ct)));
        _c.forFields(f-> $c.fields.add($field.of(f)));
        _c.forMethods(m -> $c.$methods($method.of(m)));
        */

        _c.forNests( n -> {
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
        return $c;
    }

    public static $class of(){
        return new $class();
    }

    public static $class of(Predicate<_class> constraint ){
        return new $class().$and(constraint);
    }

    /**
     * We need this since the Anonymous Object version seems to take precedence over the
     *
     * @param part
     * @return
     */
    public static $class of( $part part ){
        return of( new $part[]{part});
    }

    public static $class of( $part...parts){
        return new $class(parts);
    }

    public static $class.Or or( _class... _protos ){
        $class[] arr = new $class[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $class.of( _protos[i]);
        }
        return or(arr);
    }

    public static $class.Or or( ClassOrInterfaceDeclaration... _protos ){
        $class[] arr = new $class[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $class.of( _protos[i]);
        }
        return or(arr);
    }
    /**
     * Builds a Or matching pattern for many different or patterns
     * @param $as
     * @return
     */
    public static $class.Or or( $class...$as ){
        return new Or($as);
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
            //doesnt do extend, implement
        }
    }

    public $class $not( $part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $anno ){
                final $anno $fa = (($anno)parts[i]);
                Predicate<_class> pf = an-> an.getAnno( a ->$fa.match(a) ) != null;
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $annos ){
                final $annos $fa = (($annos)parts[i]);
                Predicate<_class> pf = an-> $fa.matches(an.getAnnos());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $modifiers ) {
                final $modifiers $fa = (($modifiers) parts[i]);
                Predicate<_class> pf = f -> $fa.matches(f.getModifiers());
                $and(pf.negate());
            }
            else if(parts[i] instanceof $field ){
                final $field $fj = (($field)parts[i]);
                Predicate<_class> aFn = a-> a.getField(e->$fj.match(e)) != null; //found one
                $and( aFn.negate() );
            }
            else if(parts[i] instanceof $method ){
                final $method $fj = (($method)parts[i]);
                Predicate<_class> aFn = a-> a.getMethod(e->$fj.match(e)) != null; //found one
                $and( aFn.negate() );
            }
            else if(parts[i] instanceof $constructor ){
                final $constructor $fj = (($constructor)parts[i]);
                Predicate<_class> aFn = a-> a.getConstructor(e->$fj.match(e)) != null; //found one
                $and( aFn.negate() );
            }
            else if(parts[i] instanceof $initBlock){
                final $initBlock $fj = (($initBlock)parts[i]);
                Predicate<_class> aFn = a-> a.getInitBlock(e->$fj.match(e)) != null; //found one
                $and( aFn.negate() );
            }
            else if( parts[i] instanceof $import) {
                final $import $fj = (($import)parts[i]);
                Predicate<_class> aFn = a-> a.getImport(im->$fj.match(im)) != null; //found one
                $and( aFn.negate() );
            }
            else if( parts[i] instanceof $package ) {
                final $package $fa = (($package) parts[i]);
                Predicate<_class> pf = f -> $fa.matches(f.getPackage());
                $and(pf.negate());
            }
            else if( parts[i] instanceof $name){
                final $name $fn = (($name)parts[i]);
                Predicate<_class> pf = f-> $fn.matches(f.getName());
                $and( pf.negate() );
            }
            else if(parts[i] instanceof $comment ){
                final $comment $fj = (($comment)parts[i]);
                Predicate<_class> pf = f-> $fj.matches(f.getJavadoc());
                $and( pf.negate() );
            }
            //Nested classes
            //doesnt do extend, implement
        }
        return this;
    }

    /**
     * Build a new pattern that matches classes that do NOT have the following parts
     * @param parts the parts to exclude from matching
     * @return the $class pattern
     */
    public static $class not( $part...parts ){
        $class $c = of();
        $c.$not(parts);
        return $c;
    }

    @Override
    public _class draft(Translator translator, Map<String, Object> keyValues) {

        //the base values (so we dont get Nulls for base values
        Tokens base = Tokens.of();
        base.putAll(keyValues);
        _class _c = _class.of(this.name.draft(translator, base));
        if( !this.packageDecl.isMatchAny() ) {
            _c.setPackage( this.packageDecl.draft(translator, base) );
        }
        if(!this.javadoc.isMatchAny()){
            _c.setJavadoc(this.javadoc.draft(translator, base));
        }
        _c.setModifiers( this.modifiers.draft(translator, base));
        _c.typeParameters( this.typeParameters.draft(translator, base));
        this.imports.stream().forEach( i -> _c.addImports( i.draft(translator, base)));

        if( !extend.isMatchAny() ) {
            String e = this.extend.draft(translator, base).toString();
            if( e != null && e.length() > 0 ) {
                _c.addExtend(e);
            }
        }
        this.implement.forEach(i -> _c.implement( i.draft(translator, base).toString() ) );

        _c.addAnnos( this.annos.draft(translator, base).ast() );
        this.initBlocks.forEach(ib -> _c.initBlock( ib.draft(translator, base)));
        this.methods.forEach(m -> _c.addMethod( m.draft(translator, base)) );
        this.fields.forEach(f-> _c.addField(f.draft(translator, base)));
        this.ctors.forEach(c -> {
            _constructor _ctor = c.draft(translator, base);
            _c.addConstructor(_ctor);
        });

        /* TODO type?
        this.nests.forEach(n -> {
            _type _t = n.draft(translator, base);
            _c.nest(_t);
        });
        */

        return _c;
    }

    @Override
    public $class $(String target, String $paramName) {

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
        this.typeParameters = this.typeParameters.$(target, $paramName);
        this.extend.$(target, $paramName);
        this.implement.forEach( i-> i.$(target, $paramName));
        //still need nests
        this.nests.forEach(n -> n.$(target, $paramName));
        return this;
    }

    @Override
    public List<String> list$() {
        return null;
    }

    @Override
    public List<String> list$Normalized() {
        return null;
    }

    @Override
    public $class $hardcode(Translator translator, Tokens kvs) {

        this.annos.$hardcode(translator, kvs);
        this.ctors.forEach( c-> c.$hardcode(translator, kvs));
        this.fields.forEach(f-> f.$hardcode(translator, kvs));
        this.imports.forEach( i-> i.$hardcode(translator, kvs));
        this.initBlocks.forEach( i-> i.$hardcode(translator, kvs));
        this.javadoc.$hardcode(translator, kvs);
        this.methods.forEach(m-> m.$hardcode(translator, kvs));
        this.modifiers.$hardcode(translator, kvs);
        this.name = this.name.hardcode$(translator, kvs);
        this.packageDecl = this.packageDecl.$hardcode(translator, kvs);
        this.typeParameters = this.typeParameters.$hardcode(translator, kvs);
        this.extend.$hardcode(translator, kvs);
        this.implement.forEach( i-> i.$hardcode(translator, kvs));
        //still need nests
        this.nests.forEach(n -> n.$hardcode(translator, kvs));

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

                 //this.headerComment.isMatchAny() &&
                 this.javadoc.isMatchAny() &&
                 this.modifiers.isMatchAny() &&
                 this.packageDecl.isMatchAny() &&
                 this.typeParameters.isMatchAny() &&

                 //extends, implements
                 this.extend.isMatchAny() &&
                 this.implement.isEmpty() &&
                 this.nests.isEmpty();
            //NESTS
        } catch(Exception e){
            return false;
        }
    }

    public boolean match( _class _c){
        return select(_c) != null;
    }

    public boolean matches(String...code){
        try{
            return matches(Ast.of(code));
        } catch(Exception e){
            return false;
        }
    }

    public boolean matches( Class clazz){
        try {
            return matches(Ast.classDecl(clazz));
        }catch(Exception e){
            return false;
        }
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

    public boolean matches(TypeDeclaration td ){
        if( td instanceof ClassOrInterfaceDeclaration ){
            return matches( (ClassOrInterfaceDeclaration) td);
        }
        return false;
    }

    public boolean matches(ClassOrInterfaceDeclaration coid ){
        if( coid != null && ! coid.isInterface()){
            return select(_class.of(coid)) != null;
        }
        return false;
    }

    public boolean matches( _codeUnit _c){
        if( _c instanceof _class){
            return matches( (_class)_c);
        }
        return false;
    }

    public boolean matches( _class _c){
        return select(_c) != null;
    }

    public Select select( CompilationUnit cu ){
        _class _c = _class.of(cu);
        return select(_c);
    }

    public Select select( ClassOrInterfaceDeclaration coid ){
        _class _c = _class.of(coid);
        return select(_c);
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
        //$tokens tokens = this.headerComment.parse(instance);
        if(!this.constraint.test(instance)){
            return null;
        }
        $tokens tokens = $type.selectImports(this.imports, instance);
        //$tokens tokens = this.packageDecl.parse(instance.astCompilationUnit() );
        tokens = $tokens.to( tokens, ()-> this.packageDecl.parse(instance.astCompilationUnit() ) );

        tokens = $tokens.to( tokens, ()-> this.annos.parse(instance));
        tokens = $tokens.to( tokens, ()-> this.javadoc.parse(instance ));
        tokens = $tokens.to( tokens, ()-> this.modifiers.parse(instance));
        tokens = $tokens.to( tokens, ()-> this.name.parse(instance.getName()));
        tokens = $tokens.to( tokens, ()-> this.typeParameters.parse(instance.getTypeParameters()) );

        tokens = $tokens.to( tokens, ()-> $type.selectExtends(this.extend, instance) );
        tokens = $tokens.to( tokens, ()-> $type.selectImplements(this.implement, instance) );

        tokens = $tokens.to( tokens, ()-> $type.selectFields(this.fields, instance ) );
        tokens = $tokens.to( tokens, ()-> $type.selectInitBlocks(this.initBlocks, instance ) );
        tokens = $tokens.to( tokens, ()-> $type.selectConstructors(this.ctors, instance ) );
        tokens = $tokens.to( tokens, ()-> $type.selectMethods(this.methods, instance ) );

        //nests
        tokens = $tokens.to( tokens, ()-> $type.selectNests(this.nests, instance ) );
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
        if( ! this.typeParameters.isMatchAny()){
            sb.append( Text.indent(this.typeParameters.toString()));
        }
        if(! this.name.isMatchAny()){
            sb.append( Text.indent(this.name.toString()));
        }
        if(! this.extend.isMatchAny()){
            sb.append( Text.indent("$extend{ "+this.extend.toString()+" }") );
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
        if(! this.fields.isEmpty()){
            this.fields.forEach(f -> sb.append(Text.indent(f.toString()) ));
        }
        if(! this.initBlocks.isEmpty()){
            this.initBlocks.forEach(i -> sb.append(Text.indent(i.toString()) ));
        }
        if(! this.ctors.isEmpty()){
            this.ctors.forEach(c -> sb.append(Text.indent(c.toString()) ));
        }
        if(! this.methods.isEmpty()){
            this.methods.forEach(m -> sb.append(Text.indent(m.toString()) ));
        }
        if(! this.nests.isEmpty()){
            this.nests.forEach(n -> sb.append(Text.indent(n.toString()) ));
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public $class $and(Predicate<_class> constraint) {
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    public $class $javadoc( _javadoc _jd){
        if( _jd != null ) {
            this.javadoc = $comment.javadocComment(_jd);
        } else{
            this.javadoc = $comment.javadocComment();
        }
        return this;
    }
    public $class $javadoc( Predicate<JavadocComment> javadocMatchFn ){
        this.javadoc = $comment.javadocComment(javadocMatchFn);
        return this;
    }

    public $class $modifiers( $modifiers...$mods){
        this.modifiers = $modifiers.of($mods);
        return this;
    }

    public $class $initBlock( $initBlock... $ibs ){
        Arrays.stream($ibs).forEach(i -> this.initBlocks.add(i));
        return this;
    }
    //TODO other static blocks

    @Override
    public $comment<JavadocComment> get$javadoc() {
        return javadoc;
    }

    public $class $javadoc( $comment<JavadocComment> javadocComment ){
        this.javadoc = javadocComment;
        return this;
    }

    public $class $implements(ClassOrInterfaceType... coits ){
        Arrays.stream(coits).forEach(c -> this.implement.add( $typeRef.of(c)));
        return this;
    }

    public $class $implements(Class... clazz){
         Arrays.stream(clazz).forEach(c -> this.implement.add( $typeRef.of(c)));
         return this;
    }

    public $class $implements(String...types){
        Arrays.stream(types).forEach(c -> this.implement.add( $typeRef.of(c)));
        return this;
    }

    public $class $implements($typeRef...impl){
        Arrays.stream(impl).forEach(i -> this.implement.add(i));
        return this;
    }

    public $class $imports( _imports _is ){
        return $imports( _is.list());
    }

    public $class $imports( List<_import> _is){
        _is.forEach( i -> this.imports.add($import.of(i)));
        return this;
    }

    public $class $imports( $import...$is ){
        Arrays.stream($is).forEach( i -> this.imports.add(i));
        return this;
    }

    public $class $imports( Class... clazzes ){
        Arrays.stream(clazzes).forEach( i -> this.imports.add($import.of(i)));
        return this;
    }

    public $class $package( String packageName ){
        this.packageDecl = $package.of(packageName);
        return this;
    }

    public $class $package( $package $p ){
        this.packageDecl = $p;
        return this;
    }

    @Override
    public $name get$Name(){
        return this.name;
    }

    public $class $name( Predicate<String> nameMatchFn){
        this.name = $name.of(nameMatchFn);
        return this;
    }

    public $class $name( String name ){
        this.name = $name.of(name);
        return this;
    }

    public $class $name( $name name ){
        this.name = name;
        return this;
    }

    public $annos get$Annos(){
        return this.annos;
    }

    public $class $annos(Predicate<_annos> annosMatchFn){
        this.annos.$and(annosMatchFn);
        return this;
    }

    public $class $annos( $annos $as ){
        this.annos = $as;
        return this;
    }

    public $class $annos( $anno... $a){
        this.annos.add($a);
        return this;
    }

    public $class $nests( $type... $nt ){
         Arrays.stream($nt).forEach( t -> this.nests.add(t) );
         return this;
    }

    public $class $methods( $method...$ms ){
        Arrays.stream($ms).forEach(m-> this.methods.add(m));
        return this;
    }

    public $class $fields( $field...$fs){
        Arrays.stream($fs).forEach(f-> this.fields.add(f));
        return this;
    }

    public $class $constructors( $constructor...$cs){
        Arrays.stream($cs).forEach(c-> this.ctors.add(c));
        return this;
    }

    public $class $typeParameters( $typeParameters $tps ){
        this.typeParameters = $tps;
        return this;
    }

    public $class $typeParameters( $typeParameter... $tps ){
        Arrays.stream($tps).forEach(tp-> this.typeParameters.$add(tp));
        return this;
    }

    public $class $extends( Class clazz ){
        this.extend = $typeRef.of(clazz);
        return this;
    }

    public $class $extends( String typeRef ){
        this.extend = $typeRef.of(typeRef);
        return this;
    }

    public $class $extends( $typeRef $tr ){
        this.extend = $tr;
        return this;
    }

    @Override
    public boolean match(Node candidate) {
        if(candidate instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration) candidate).asClassOrInterfaceDeclaration().isInterface()){
            return select( _class.of((ClassOrInterfaceDeclaration)candidate)) != null;
        }

        /*
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
        */
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
    public Class<_class> _modelType() {
        return _class.class;
    }


    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $class{

        final List<$class>ors = new ArrayList<>();

        public Or($class...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $class $hardcode(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.$hardcode(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$class.Or{");
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
        public $class.Select select(_class astNode){
            $class $a = whichMatch(astNode);
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
        public $class whichMatch(_class ae){
            if( !this.constraint.test( ae ) ){
                return null;
            }
            Optional<$class> orsel  = this.ors.stream().filter( $p-> $p.match(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }

    /**
     * The selected Class
     */
    public static class Select implements $pattern.select_java<_class>, $pattern.selectAst<ClassOrInterfaceDeclaration>{
        public _class selected;
        public $tokens tokens;

        public Select( _class _c, $tokens tokens){
            this.selected = _c;
            this.tokens = tokens;
        }

        public Select( ClassOrInterfaceDeclaration coid, $tokens tokens){
            this.selected = _class.of(coid);
            this.tokens = tokens;
        }

        @Override
        public $tokens tokens() {
            return tokens;
        }

        public ClassOrInterfaceDeclaration ast(){
            return selected.ast();
        }

        @Override
        public _class _node() {
            return selected;
        }
    }
}
