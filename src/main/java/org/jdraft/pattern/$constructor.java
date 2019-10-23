package org.jdraft.pattern;

import com.github.javaparser.utils.Log;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import org.jdraft.*;
import org.jdraft._anno._annos;
import org.jdraft._parameter._parameters;
import org.jdraft._typeParameter._typeParameters;
import org.jdraft.macro._remove;
import org.jdraft.macro._toCtor;
import org.jdraft.macro.macro;

/**
 * prototype/template for a Java {@link _constructor}
 */
public class $constructor
    implements Template<_constructor>, $pattern<_constructor, $constructor>,
        $pattern.$java<_constructor, $constructor>, $class.$part, $enum.$part, $member.$named<$constructor>,
        $declared<_constructor,$constructor>, has$Annos {

    public Class<_constructor> _modelType(){
        return _constructor.class;
    }

    /**
     * Marker interface for designating prototypes that are "part" of
     * the $constructor
     * (i.e. all of the components $annos, $annos, $name, $body,... )
     * that make up the $constructor
     */
    public interface $part{ }
    
    /**
     * 
     * @param ctorPattern
     * @return 
     */
    public static $constructor of( String ctorPattern ){
        return of(new String[]{ctorPattern});
    }
    
    /**
     * Pass in an anonymous Object containing the method to import
     * NOTE: if the anonymous Object contains more than one method, ENSURE only one method
     * DOES NOT have the @_remove annotation, (mark all trivial METHODS with @_remove)
     *
     * @param anonymousObjectContainingMethod
     * @return
     */
    public static $constructor of( Object anonymousObjectContainingMethod ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return of( ste, anonymousObjectContainingMethod);
    }

    /**
     *
     * @param ste
     * @param anonymousObjectContainingMethod
     * @return
     */
    public static $constructor of( StackTraceElement ste, Object anonymousObjectContainingMethod ){
        //MED
        $constructor $ct = of( from(ste, anonymousObjectContainingMethod) );
        return $ct;
    }

    /**
     *
     * @return
     */
    public static $constructor of(){
        return new $constructor( $name.of("$name$"), $body.of() );
    }

    /**
     *
     * @param cd
     * @return
     */
    public static $constructor of( ConstructorDeclaration cd){
        return of( _constructor.of(cd));
    }

    /**
     * 
     * @param _ct
     * @return 
     */
    public static $constructor of( _constructor _ct ){
        return new $constructor( _ct);
    }

    /**
     * 
     * @param _ct
     * @param constraint
     * @return 
     */
    public static $constructor of( _constructor _ct, Predicate<_constructor> constraint){
        return new $constructor( _ct).$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $constructor of( String...pattern ){
        return new $constructor(_constructor.of(pattern));
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $constructor of( String pattern, Predicate<_constructor> constraint ){
        return new $constructor(_constructor.of(pattern) ).$and(constraint);
    }

    /**
     * 
     * @param component
     * @return 
     */
    public static $constructor of( $part component ){       
        $part[] pr = new $part[]{component};
        return of(pr);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $constructor of( Predicate<_constructor> constraint){
        return of().$and(constraint);
    }
    
    /**
     * 
     * @param components
     * @return 
     */
    public static $constructor of( $part...components ){       
        return new $constructor( components );
    }

    public static $constructor.Or or( _constructor... _protos ){
        $constructor[] arr = new $constructor[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $constructor.of( _protos[i]);
        }
        return or(arr);
    }

    public static $constructor.Or or( ConstructorDeclaration... _protos ){
        $constructor[] arr = new $constructor[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $constructor.of( _protos[i]);
        }
        return or(arr);
    }

    public static $constructor.Or or( $constructor...$tps ){
        return new $constructor.Or($tps);
    }

    public static $constructor as( Object anonymousObjectContainingMethod ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return as( ste, anonymousObjectContainingMethod);
    }

    public static $constructor as( StackTraceElement ste, Object anonymousObjectContainingMethod ){
        _constructor _ct = from(ste, anonymousObjectContainingMethod);
        return as(_ct, anonymousObjectContainingMethod );
    }

    public static $constructor as( _constructor _ct, Object anonymousObjectContainingMethod ){
        $constructor $ct = new $constructor();
        if( _ct.hasJavadoc() ){
            $ct.javadoc = $comment.javadocComment(_ct.getJavadoc());
        }
        if( _ct.hasAnnos() ){
            $ct.annos = $annos.as(_ct.getAnnos() );
        } else{
            $ct.annos = $annos.none();
        }
        $ct.modifiers = $modifiers.as(_ct );
        if( !_ct.hasTypeParameters() ){
            final _typeParameters etps = _ct.getTypeParameters();
            $ct.typeParameters = $typeParameters.as(etps);
        } else{
            $ct.typeParameters = $typeParameters.none();
        }
        $ct.name = $name.as(_ct.getName() );
        if( _ct.hasParameters() ){
            $ct.parameters = $parameters.as(_ct.getParameters());
        } else{
            $ct.parameters = $parameters.none();
        }
        $ct.thrown = $throws.as( _ct.getThrows() );
        $ct.body = $body.as(_ct.getBody());

        return $ct;
    }

    private static _constructor from( StackTraceElement ste, Object anonymousObjectContainingMethod ){
        ObjectCreationExpr oce = Ex.anonymousObjectEx( ste );

        _class _c = _class.of("C");
        if( oce.getAnonymousClassBody().isPresent() ){
            NodeList<BodyDeclaration<?>> bs = oce.getAnonymousClassBody().get();
            bs.forEach( b -> _c.ast().addMember(b));
        }

        //run macros on the things
        macro.to( anonymousObjectContainingMethod.getClass(), _c.ast());

        MethodDeclaration theMethod = (MethodDeclaration)
                oce.getAnonymousClassBody().get().stream().filter(m -> m instanceof MethodDeclaration &&
                        !m.isAnnotationPresent(_remove.class) ).findFirst().get();

        _method _m = _method.of(theMethod);

        //get the Runtime Reflection Method
        Method rm = Arrays.stream(anonymousObjectContainingMethod.getClass().getDeclaredMethods()).filter(mm ->_m.hasParametersOf(mm)).findFirst().get();

        //build the base method first
        _constructor _ct = _constructor.of( theMethod.getNameAsString() + " " +_parameter._parameters.of( theMethod )+"{}" );

        //MODIFIERS
        if( theMethod.isPublic() ){
            _ct.setPublic();
        }
        if(theMethod.isProtected()){
            _ct.setProtected();
        }
        if(theMethod.isPrivate()){
            _ct.setPrivate();
        }
        if( theMethod.hasJavaDocComment() ){
            _ct.javadoc(theMethod.getJavadocComment().get());
        }
        _ct.setThrows( theMethod.getThrownExceptions() );
        _ct.anno( theMethod.getAnnotations()); //add annos
        _ct.removeAnnos(_toCtor.class); //remove the _ctor anno if it exists
        _ct.setBody( theMethod.getBody().get() ); //BODY
        return _ct;
    }

    /**
     *
     * @param parts
     * @return
     */
    public static $constructor not( $part...parts){
        $constructor $ct = of();
        $ct.$not(parts);
        return $ct;
    }

    public Predicate<_constructor> constraint = t -> true;
    
    public $comment<JavadocComment> javadoc = $comment.javadocComment();
    public $annos annos = new $annos();
    public $modifiers modifiers = $modifiers.of();
    
    public $typeParameters typeParameters = $typeParameters.of();
    public $name name = $name.of();
    public $parameters parameters = $parameters.of();
    public $throws thrown = $throws.of();
    public $body body = $body.of();


    private $constructor( _constructor _ct ){
        this(_ct, t-> true );
    }
    
    private $constructor(){        
    }
    
    /**
     * i.e. 
     * 
     * //look for all constructors with a matching annotation to A
     * $constructor $ct = $constructor.of( $anno.of("A") );
     * 
     * @param components 
     */
    private $constructor( $part...components ){        
        for(int i=0;i<components.length; i++){
            if( components[i] instanceof $annos ){
                this.annos = ($annos)components[i];
            }
            else if( components[i] instanceof $anno ){
                this.annos.$annosList.add( ($anno)components[i] );
            }
            else if( components[i] instanceof $modifiers ){
                this.modifiers = $modifiers.of( this.modifiers, ($modifiers)components[i]);
                //this.modifiers = ($modifiers)components[i];
            }
            else if( components[i] instanceof $name ){
                this.name = ($name)components[i];
            }
            else if( components[i] instanceof $parameters ){
                this.parameters = ($parameters)components[i];
            }
            else if(components[i] instanceof $parameter){
                this.parameters.$add( ($parameter)components[i] );
            }
            else if( components[i] instanceof $body ){
                this.body = ($body)components[i];
            }
            else if( components[i] instanceof $stmt ){
                this.body.$and( ($stmt)components[i]);
            }
            else if( components[i] instanceof $ex ){
                this.body.$and( ($ex)components[i] );
            }
            else if( components[i] instanceof $throws ){
                this.thrown = ($throws)components[i];
            }
            else if( components[i] instanceof $typeParameters){
                this.typeParameters = ($typeParameters)components[i];
            }
            else if( components[i] instanceof $typeParameter){
                this.typeParameters.typeParams.add( ($typeParameter)components[i]);
            }
            else if( components[i] instanceof $comment){
                this.javadoc = ($comment<JavadocComment>)components[i];
            }
            else{
                throw new _draftException("Unable to use $proto component " +components[i]+" for $constructor" );
            }            
        }
    }

    /**
     *
     * @return
     */
    public String toString(){
        if( isMatchAny() ){
            return "$constructor{ $ANY$ }";
        }
        StringBuilder str = new StringBuilder();
        str.append("$constructor{").append( System.lineSeparator() );
        if( !javadoc.isMatchAny() ){
            str.append( Text.indent( javadoc.toString()));
        }
        if( !annos.isMatchAny() ){
            str.append(Text.indent(annos.toString()));
        }
        if( !modifiers.isMatchAny() ){
            str.append(Text.indent( modifiers.toString()) );
        }
        if( ! typeParameters.isMatchAny() ){
            str.append(Text.indent( typeParameters.toString()));
        }
        if( ! name.isMatchAny() ){
            str.append(Text.indent( name.toString()));
        }
        if( ! parameters.isMatchAny() ){
            str.append(Text.indent( parameters.toString()));
        }
        if( ! thrown.isMatchAny() ){
            str.append(Text.indent( thrown.toString()));
        }
        if( ! body.isMatchAny() ){
            str.append(Text.indent( body.toString()));
        }
        str.append("}");
        return str.toString();
    }

    /**
     * 
     * @param _ct
     * @param constraint
     */
    private $constructor( _constructor _ct, Predicate<_constructor> constraint){
        
        if( _ct.hasJavadoc() ){
            javadoc = $comment.javadocComment(_ct.getJavadoc());
        }        
        if( _ct.hasAnnos() ){
            annos = $annos.of(_ct.getAnnos() );
        }
        modifiers = $modifiers.of(_ct );        
        if( !_ct.hasTypeParameters() ){
            final _typeParameters etps = _ct.getTypeParameters();
            typeParameters = $typeParameters.of(etps);
        }
        name = $name.of(_ct.getName() );
        if( _ct.hasParameters() ){
            parameters = $parameters.of(_ct.getParameters());
        }        
        thrown = $throws.of( _ct.getThrows() );        
        if( _ct.hasBody() ){            
            body = $body.of(_ct.ast());    
        }
        this.constraint = constraint;
    }
    
    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified prototype
     */
    public $constructor $and(Predicate<_constructor>constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    /**
     * Adds a NOT constraint to the {@link #constraint} based on one or more $method.$part
     * @param parts
     * @return
     */
    public $constructor $not(final $part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $anno ){
                final $anno $fa = (($anno)parts[i]);
                Predicate<_constructor> pf = f-> $fa.count(f) > 0;
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $modifiers ){
                final $modifiers $fa = (($modifiers)parts[i]);
                Predicate<_constructor> pf = f-> $fa.matches(f.getModifiers());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $parameters ){
                final $parameters $fa = (($parameters)parts[i]);
                Predicate<_constructor> pf = f-> $fa.matches(f.getParameters());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $parameter ){
                final $parameter $fa = (($parameter)parts[i]);
                Predicate<_constructor> pf = f-> $fa.count(f) > 0;
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $typeParameters ){
                final $typeParameters $fa = (($typeParameters)parts[i]);
                Predicate<_constructor> pf = f-> $fa.matches(f.getTypeParameters());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $typeParameter ){
                final $typeParameter $fa = (($typeParameter)parts[i]);
                Predicate<_constructor> pf = f-> $fa.count(f) > 0;
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $name){
                final $name $fn = (($name)parts[i]);
                Predicate<_constructor> pf = f-> $fn.matches(f.getName());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $throws){
                final $throws $ft = (($throws)parts[i]);
                Predicate<_constructor> pf = f-> $ft.matches(f.getThrows());
                $and( pf.negate() );
            }
            else if(parts[i] instanceof $comment ){
                final $comment $fj = (($comment)parts[i]);
                Predicate<_constructor> pf = f-> $fj.matches(f.getJavadoc());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $body){
                final $body $fj = (($body)parts[i]);
                Predicate<_constructor> pf = f-> $fj.matches(f.getBody());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $stmt){
                final $stmt $fj = (($stmt)parts[i]);
                Predicate<_constructor> pf = f-> $fj.firstIn(f.getBody()) != null;
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $ex){
                final $ex $fj = (($ex)parts[i]);
                Predicate<_constructor> pf = f-> $fj.firstIn(f.getBody()) != null;
                $and( pf.negate() );
            }
        }
        return this;
    }

    public boolean match( Node n){
        if( n instanceof ConstructorDeclaration ){
            return matches( (ConstructorDeclaration) n);
        }
        return false;
    }

    @Override
    public List<String> list$Normalized(){
        List<String>normalized$ = new ArrayList<>();
        normalized$.addAll( javadoc.list$Normalized() );
        normalized$.addAll( annos.list$Normalized() );
        normalized$.addAll( typeParameters.list$Normalized() );
        normalized$.addAll( name.list$Normalized() );
        normalized$.addAll( parameters.list$Normalized() );
        
        normalized$.addAll( thrown.list$Normalized() );
        normalized$.addAll( body.list$Normalized());
        return normalized$.stream().distinct().collect(Collectors.toList());        
    }

    @Override
    public List<String> list$(){
        List<String>all$ = new ArrayList<>();
        all$.addAll( javadoc.list$() );
        all$.addAll( annos.list$() );
        all$.addAll( typeParameters.list$() );
        all$.addAll( name.list$() );
        all$.addAll( parameters.list$() );
        all$.addAll( thrown.list$() );
        all$.addAll( body.list$() );        
        return all$;
    }

    @Override
    public $comment<JavadocComment> get$javadoc() {
        return javadoc;
    }

    public $constructor $javadoc(){
        this.javadoc = $comment.javadocComment();
        return this;
    }
    
    public $constructor $javadoc(_javadoc _jd){
        this.javadoc = $comment.javadocComment(_jd);
        return this;
    }
    
    public $constructor $javadoc( String... form ){
        this.javadoc.contentsStencil = Stencil.of((Object[])form);
        return this;
    }
    
    public $constructor $javadoc( Predicate<JavadocComment> _javadocMatchFn){
        this.javadoc.$and(_javadocMatchFn);
        return this;
    }

    public $constructor $javadoc( $comment<JavadocComment> $javadocComment){
        this.javadoc = $javadocComment;
        return this;
    }

    public $constructor $parameters( ){
        this.parameters = $parameters.of();
        return this;
    }
    
    public $constructor $parameters($parameters $ps ){
        this.parameters = $ps;
        return this;
    }
    
    public $constructor $parameters( _parameters _ps ){
        this.parameters = $parameters.of(_ps);
        return this;
    }
    
    public $constructor $parameters(Predicate<_parameters> constraint){
        this.parameters.$and(constraint);
        return this;
    }

    @Override
    public $annos get$Annos(){
        return this.annos;
    }

    public $constructor $annos(){
        this.annos = $annos.of();
        return this;
    }
    
    public $constructor $annos( Predicate<_annos> constraint ){
        this.annos.$and(constraint);
        return this;
    }
    
    public $constructor $annos( $annos $as){
        this.annos = $as;
        return this;
    }
    
    public $constructor $annos(String...annoPatterns ){
        this.annos.add(annoPatterns);
        return this;
    }
    
    public $constructor $anno( Class<? extends Annotation> aClass ){
        this.annos.$annosList.add($anno.of(aClass));
        return this;
    }
    public $constructor $anno( $anno $a){
        this.annos.$annosList.add($a);
        return this;
    }

    @Override
    public $name get$Name(){
        return this.name;
    }

    public $constructor $name(){
        this.name = $name.of();
        return this;
    }
    
    public $constructor $name( Predicate<String> constraint){
        this.name.and(constraint);
        return this;
    }
    
    public $constructor $name(String name){
        this.name.nameStencil = Stencil.of(name);
        return this;
    }
    
    public $constructor $name($name name ){
        this.name = name;
        return this;
    }
    
    public $constructor $typeParameters(Predicate<_typeParameters> constraint){
        this.typeParameters.$and(constraint);
        return this;
    }
    
    public $constructor $typeParameters( $typeParameters $tps ){
        this.typeParameters = $tps;
        return this;
    }
    
    public $constructor $typeParameters(){
        this.typeParameters = $typeParameters.of();
        return this;
    }
    
    public $constructor $typeParameters(String... typeParameters){
        this.typeParameters = $typeParameters.of(typeParameters);
        return this;
    }

    public $constructor $modifiers( Predicate<_modifiers> constraint ){
        this.modifiers.$and(constraint);
        return this;
    }
    
    public $constructor $modifiers(){
        this.modifiers = $modifiers.of();
        return this;
    }
    
    public $constructor $modifiers( Modifier...modifiers ){
        this.modifiers = $modifiers.of(modifiers);
        return this;
    }
    
    public $constructor $modifiers( $modifiers $ms ){
        this.modifiers = $ms;
        return this;
    }
    
    public $constructor $throws(){
        this.thrown = $throws.of();
        return this;
    }
    
    public $constructor $throws( Predicate<_throws> constraint ){
        this.thrown.$and(constraint);
        return this;
    }
    
    public $constructor $throws( Class<? extends Throwable>... throwClasses){
        this.thrown = $throws.of(throwClasses);
        return this;
    }
    
    public $constructor $throws( $throws $th ){
        this.thrown = $th;
        return this;
    }
    
    public $constructor $body( Predicate<_body> constraint){
        this.body.$and(constraint);
        return this;
    }
    
    public $constructor $body (){
        this.body = $body.of();
        return this;
    }
    
    public $constructor $body( $body body ){
        this.body = body;
        return this;
    }
    
    public $constructor $body( String... body){
        this.body = $body.of(body);
        return this;
    }
    
    public $constructor $emptyBody(){
        body = $body.of("{}");
        return this;
    }
    
    @Override
    public _constructor fill(Translator translator, Object... values) {
        List<String> nom = this.list$Normalized();
        nom.remove( "javadoc");
        nom.remove( "annos");
        nom.remove( "modifiers");
        nom.remove( "typeParameters");
        nom.remove( "parameters");
        nom.remove( "throws");
        nom.remove("body");
        if( nom.size() != values.length ){
            throw new _draftException("Fill expected ("+nom.size()+") values "+ nom+" got ("+values.length+")");
        }
        Tokens ts = new Tokens();
        for(int i=0;i<nom.size();i++){
            ts.put( nom.get(i), values[i]);
        }
        return draft(translator, ts);
    }

    /**
     *
     * @return
     */
    public boolean isMatchAny(){
        try {
            return this.constraint.test(null) && this.javadoc.isMatchAny() && this.annos.isMatchAny()
                    && this.modifiers.isMatchAny() && this.name.isMatchAny() && this.typeParameters.isMatchAny()
                    && this.thrown.isMatchAny() && this.body.isMatchAny();
        } catch(Exception e){
            Log.info("$constructor not match any" );
            return false;
        }
    }

    @Override
    public _constructor draft(Translator translator, Map<String, Object> keyValues) {
        
        //the base values (so we dont get Nulls for base values
        Tokens base = Tokens.of(
                "javadoc", "", 
                "annos", "", 
                "modifiers", "", 
                "typeParameters", "", 
                "parameters", "()", 
                "throws", "", 
                "body", "");
        
        base.putAll(keyValues);
        
        StringBuilder sb = new StringBuilder();   
        JavadocComment jdc = javadoc.draft(translator, base );
        if( jdc != null ){
            sb.append(jdc);        
        }
        sb.append(System.lineSeparator());
        sb.append( annos.draft(translator, base));
        sb.append(System.lineSeparator());
        sb.append( modifiers.draft(translator, base));
        sb.append(" ");
        sb.append( typeParameters.draft(translator, base));
        sb.append(" ");
        sb.append( name.draft(translator, base));
        sb.append(" ");
        sb.append( parameters.draft(translator, base));
        sb.append(" ");
        sb.append( thrown.draft(translator, base));
        sb.append(System.lineSeparator());
        sb.append( body.draft(translator, keyValues));
        return _constructor.of(sb.toString() );        
    }
    
    /**
     * 
     * @param _n
     * @return 
     */
    public _constructor draft(_node _n ){
        return draft(_n.tokenize() );
    }

    public static final BlockStmt EMPTY = Stmt.blockStmt("{}");

    /**
     * 
     * @param _ct
     * @return 
     */
    public Select select( _constructor _ct){
        if( !this.constraint.test(_ct)){
            Log.info( "failed constraint");
            return null;
        }
        if( modifiers.select(_ct) == null ){
            return null;
        }
        Tokens all = new Tokens();
        if( _ct.getJavadoc() != null ){
            all = javadoc.parseTo(_ct.getJavadoc().ast(), all);
        } else{
            all = javadoc.parseTo(null, all);
        }
        all = annos.parseTo(_ct.getAnnos(), all);
        all = typeParameters.parseTo(_ct.getTypeParameters(), all);
        all = name.parseTo(_ct.getName(), all);
        all = parameters.parseTo(_ct.getParameters(), all);
        all = thrown.parseTo(_ct.getThrows(), all);
        all = body.parseTo(_ct.getBody(), all);
        if( all != null ){
            return new Select( _ct, $tokens.of(all));
        }
        return null;        
    }

    /**
     * 
     * @param astCtor
     * @return 
     */
    public Select select( ConstructorDeclaration astCtor){
        return select(_constructor.of(astCtor));
    }

    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $constructor hardcode$( Translator translator, Tokens kvs ) {
        javadoc = javadoc.hardcode$(translator, kvs);
        annos = annos.hardcode$(translator, kvs);
        typeParameters = typeParameters.hardcode$(translator, kvs);
        name.nameStencil = name.nameStencil.hardcode$(translator, kvs);
        parameters = parameters.hardcode$(translator, kvs);
        thrown = thrown.hardcode$(translator, kvs);
        body = body.hardcode$(translator, kvs );
        
        return this;
    }

    /** Post - parameterize, create a parameter from the target string named $Name#$*/
    @Override
    public $constructor $(String target, String $paramName) {
        javadoc = javadoc.$(target, $paramName);
        annos = annos.$(target, $paramName);
        typeParameters = typeParameters.$(target, $paramName);
        name.nameStencil = name.nameStencil.$(target, $paramName);
        parameters = parameters.$(target, $paramName);
        thrown = thrown.$(target, $paramName);
        body = body.$(target, $paramName);
        return this;
    }

    /**
     * 
     * @param astExpr
     * @param $name
     * @return 
     */
    public $constructor $(Expression astExpr, String $name ){
        String exprString = astExpr.toString();
        return $(exprString, $name);
    }

    /**
     * 
     * @param _ct
     * @return 
     */
    public boolean matches( _constructor _ct ){
        return select( _ct ) != null;
    }

    /**
     *
     * @param code
     * @return
     */
    public boolean matches( String...code ){
        try {
            return matches(_constructor.of(code));
        }catch(Exception e){
            return false;
        }
    }

    /**
     * 
     * @param astCtor
     * @return 
     */
    public boolean matches( ConstructorDeclaration astCtor ){
        return select(astCtor ) != null;
    }

    /**
     * Returns the first _constructor that matches the pattern and constraint
     * @param astNode the node to look through
     * @param _ctorMatchFn additional matching function for selecting the constructor
     * @return  the first _constructor that matches (or null if none found)
     */
    @Override
    public _constructor firstIn(Node astNode, Predicate<_constructor> _ctorMatchFn){
        Optional<ConstructorDeclaration> f = astNode.findFirst(
            ConstructorDeclaration.class, s ->{
                Select sel = select(s); 
                return sel != null && _ctorMatchFn.test(sel._ct);
            });         
        if( f.isPresent()){
            return _constructor.of(f.get());
        }
        return null;
    }

    /**
     * Returns the first _constructor that matches the pattern and constraint
     * @param clazz the runtime class (WITH source available in classpath)
     * @return  the first _constructor that matches (or null if none found)
     */ 
    @Override
    public Select selectFirstIn( Class clazz){
        return selectFirstIn( (_type)_java.type(clazz));
    }
     
    /**
     * Returns the first _constructor that matches the pattern and constraint
     * @param clazz the runtime class (WITH source available in classpath)
     * @param selectConstraint
     * @return  the first _constructor that matches (or null if none found)
     */
    public Select selectFirstIn( Class clazz, Predicate<Select> selectConstraint){
        return selectFirstIn( (_type)_java.type(clazz), selectConstraint);
    }
    
    /**
     * Returns the first _constructor that matches the pattern and constraint
     * @param astNode the node to look through
     * @return  the first _constructor that matches (or null if none found)
     */
    @Override
    public Select selectFirstIn( Node astNode ){
        Optional<ConstructorDeclaration> f = astNode.findFirst(ConstructorDeclaration.class, s -> this.matches(s) );         
        if( f.isPresent()){
            return select(f.get());
        }
        return null;
    }
    
    /**
     * Returns the first _constructor that matches the pattern and constraint
     * @param _j the _java node
     * @param selectConstraint
     * @return  the first _constructor that matches (or null if none found)
     */
    public Select selectFirstIn(_model _j, Predicate<Select> selectConstraint){
        if( _j instanceof _code){
            if( ((_code) _j).isTopLevel()){
                Optional<ConstructorDeclaration> f = (((_code) _j).astCompilationUnit().findFirst(
                    ConstructorDeclaration.class, s -> {
                        Select sel = this.select(s);
                        return sel != null && selectConstraint.test(sel);
                    }));               
                if( f.isPresent()){
                    return select(f.get());
                }
                return null;
            }
            else{
                _type _t = (_type)_j;
                Optional<ConstructorDeclaration> f = (_t.ast().findFirst(
                    ConstructorDeclaration.class, s -> {
                        Select sel = this.select(s);
                        return sel != null && selectConstraint.test(sel);
                    }));               
                if( f.isPresent()){
                    return select(f.get());
                }
                return null;
            }
        } else{
            Optional<ConstructorDeclaration> f = ((_node)_j).ast().findFirst(
                ConstructorDeclaration.class, s -> {
                    Select sel = this.select(s);
                    return sel != null && selectConstraint.test(sel);
                });               
            if( f.isPresent()){
                return select(f.get());
            }
            return null;
        }
    }

    /**
     * Returns the first _constructor that matches the pattern and constraint
     * @param astNode the node to look through
     * @param selectConstraint
     * @return  the first _constructor that matches (or null if none found)
     */
    public Select selectFirstIn( Node astNode, Predicate<Select> selectConstraint){
        Optional<ConstructorDeclaration> f = astNode.findFirst(ConstructorDeclaration.class, s -> {
            Select sel = this.select(s);
            return sel != null && selectConstraint.test(sel);
            });         
        if( f.isPresent()){
            return select(f.get());
        }
        return null;
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(Class clazz, Predicate<Select> selectConstraint){
        return listSelectedIn( (_type)_java.type(clazz), selectConstraint);
    }
    
    /**
     * 
     * @param astNode
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(Node astNode, Predicate<Select> selectConstraint){
        List<Select>sts = new ArrayList<>();
        astNode.walk(ConstructorDeclaration.class, m-> {
            Select sel = select( m );
            if( sel != null && selectConstraint.test(sel)) {
                sts.add(sel);
            }
        });
        return sts;
    }
    
    @Override
    public List<Select> listSelectedIn(Node astNode){
        List<Select>sts = new ArrayList<>();
        astNode.walk(ConstructorDeclaration.class, m-> {
            Select sel = select( m );
            if( sel != null ){
                sts.add(sel);
            }
        });
        return sts;
    }

    /**
     * 
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(_model _j, Predicate<Select> selectConstraint){
        List<Select>sts = new ArrayList<>();
        _walk.in(_j, ConstructorDeclaration.class, m -> {
            Select sel = select( m );
            if( sel != null && selectConstraint.test(sel)){
                sts.add(sel);
            }
        });
        return sts;
    }
    
    @Override
    public List<Select> listSelectedIn(_model _j){
        if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                return listSelectedIn(_c.astCompilationUnit());
            }
            _type _t = (_type) _j; //only possible
            return listSelectedIn(_t.ast()); //return the TypeDeclaration, not the CompilationUnit
        }
        return listSelectedIn( ((_node) _j).ast());
    }
    
    /**
     * 
     * @param clazz
     * @param selectActionFn
     * @return 
     */
    public <_CT extends _type> _CT forSelectedIn(Class clazz, Consumer<Select> selectActionFn ){
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectActionFn );
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public <_CT extends _type> _CT forSelectedIn(Class clazz, Predicate<Select>selectConstraint, Consumer<Select> selectActionFn ){
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectConstraint, selectActionFn );
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectedActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Consumer<Select> selectedActionFn ){
        astNode.walk( ConstructorDeclaration.class, c-> {
            Select s = select(c );
            if( s != null ){
                selectedActionFn.accept( s );
            }
        });
        return astNode;
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectedActionFn
     * @return 
     */
    public <_J extends _model> _J forSelectedIn(_J _j, Consumer<Select> selectedActionFn ){
        _walk.in(_j, _constructor.class, c ->{
            Select s = select(c );
            if( s != null ){
                selectedActionFn.accept( s );
            }
        });
        return _j;
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectConstraint
     * @param selectedActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(
        N astNode, Predicate<Select> selectConstraint, Consumer<Select> selectedActionFn ){
        
        astNode.walk( ConstructorDeclaration.class, c-> {
            Select s = select(c );
            if( s != null && selectConstraint.test(s)){
                selectedActionFn.accept( s );
            }
        });
        return astNode;
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @param selectedActionFn
     * @return 
     */
    public <_J extends _model> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> selectedActionFn ){
        _walk.in(_j, _constructor.class, c ->{
            Select s = select(c );
            if( s != null && selectConstraint.test(s)){
                selectedActionFn.accept( s );
            }
        });
        return _j;
    }
    
    /**
     * 
     * @param clazz
     * @param $replace
     * @return 
     */
    public <_CT extends _type> _CT replaceIn(Class clazz, $constructor $replace ){
        return (_CT)forSelectedIn((_type)_java.type(clazz), s -> {
            _constructor repl = $replace.draft(Translator.DEFAULT_TRANSLATOR, s.tokens);
            s._ct.ast().replace(repl.ast());
        });
    }

    /**
     * 
     * @param clazz
     * @param replacementProto
     * @return 
     */
    public <_CT extends _type> _CT  replaceIn(Class clazz,  String... replacementProto ){
        return (_CT)replaceIn((_type)_java.type(clazz), $constructor.of(replacementProto));
    }
    
    /**
     * 
     * @param clazz
     * @param _ct
     * @return 
     */
    public <_CT extends _type> _CT replaceIn(Class clazz,  _constructor _ct ){
        return (_CT)replaceIn((_type)_java.type(clazz), $constructor.of(_ct));
    }
    
    /**
     * 
     * @param clazz
     * @param astCtor
     * @return 
     */
    public <_CT extends _type> _CT replaceIn(Class clazz, ConstructorDeclaration astCtor ){
        return (_CT)replaceIn( (_type)_java.type(clazz), $constructor.of(astCtor));
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param $replace
     * @return 
     */
    public <_J extends _model> _J replaceIn(_J _j, $constructor $replace ){
        return forSelectedIn(_j, s -> {
            _constructor repl = $replace.draft(Translator.DEFAULT_TRANSLATOR, s.tokens.asTokens());
            s._ct.ast().replace(repl.ast());
        });
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param replacementProto
     * @return 
     */
    public <_J extends _model> _J replaceIn(_J _j, String... replacementProto ){
        return replaceIn(_j, $constructor.of(replacementProto));
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param _ct
     * @return 
     */
    public <_J extends _model> _J replaceIn(_J _j, _constructor _ct ){
        return replaceIn(_j, $constructor.of(_ct));
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param astCtor
     * @return 
     */
    public <_J extends _model> _J replaceIn(_J _j, ConstructorDeclaration astCtor ){
        return replaceIn(_j, $constructor.of(astCtor));
    }    
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param _ctorMatchFn
     * @param _constructorActionFn
     * @return 
     */
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_constructor> _ctorMatchFn, Consumer<_constructor> _constructorActionFn){
        astNode.walk(ConstructorDeclaration.class, e-> {
            Select sel = select(e);
            if( sel != null && _ctorMatchFn.test(sel._ct)){
                _constructorActionFn.accept( sel._ct );
            }
        });
        return astNode;
    }


    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $constructor{

        final List<$constructor>ors = new ArrayList<>();

        public Or($constructor...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $constructor hardcode$(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.hardcode$(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$constructor.Or{");
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
        public $constructor.Select select(_constructor astNode){
            $constructor $a = whichMatch(astNode);
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
        public $constructor whichMatch(_constructor ae){
            if( !this.constraint.test( ae ) ){
                return null;
            }
            Optional<$constructor> orsel  = this.ors.stream().filter( $p-> $p.match(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }


    /**
     * A Matched Selection result returned from matching a prototype $ctor
     * inside of some Node or _node
     */
    public static class Select implements $pattern.selected,
            selectAst<ConstructorDeclaration>,
            select_java<_constructor> {
        
        public final _constructor _ct;
        public final $tokens tokens;

        public Select( _constructor _m, $tokens tokens ){
            this._ct = _m;
            this.tokens = tokens;
        }
                
        public Select( ConstructorDeclaration astMethod, $tokens tokens ){
            this._ct = _constructor.of(astMethod);
            this.tokens = tokens;
        }

        @Override
        public $tokens tokens(){
            return tokens;
        }
        
        @Override
        public String toString(){
            return "$constructor.Select{"+ System.lineSeparator()+
                Text.indent(_ct.toString() )+ System.lineSeparator()+
                Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                "}";
        }

        @Override
        public ConstructorDeclaration ast() {
            return _ct.ast();
        }

        @Override
        public _constructor _node() {
            return _ct;
        }
        
        public boolean isNamed(String name){
            return _ct.isNamed(name);
        }
        
        public boolean isParameters(String...params ){
            return _ct.getParameters().is(params);
        }
        
        public boolean isVarArg(){
            return _ct.isVarArg();
        }
        
        public boolean hasBody(){
            return _ct.hasBody();
        }
        
        public boolean isThrows(String...throwDecl){
            return _ct.getThrows().is(throwDecl);
        }
        
        public boolean hasThrows(){
            return _ct.hasThrows();
        }
        
        public boolean hasThrow(Class<? extends Throwable> throwsClass){            
            return _ct.hasThrow(throwsClass);
        }
        
        public boolean hasParameters(){            
            return _ct.hasParameters();
        }
        
        public boolean is(String...methodDeclaration){
            return _ct.is(methodDeclaration);
        }
        
        public boolean hasTypeParameters(){            
            return _ct.hasTypeParameters();
        }
    }
}
