package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.Type;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import org.jdraft.*;
import org.jdraft._annoExprs;
import org.jdraft._params;
import org.jdraft._typeParams;
import org.jdraft.macro._remove;
import org.jdraft.macro.macro;
import org.jdraft.text.*;

/**
 *
 * pattern/template for a Java {@link _method}
 */
public class $method
    implements Template<_method>, //$pattern<_method, $method>,
        $pattern.$java<_method,$method>, $class.$part,
        $interface.$part, $enum.$part,$enumConstant.$part, $member.$named<$method>, $declared<_method,$method>, $withAnnoRefs,
        $type.$part {

    /**
     * Marker interface for categorizing/identifying parts that make up the
     * $method
     */
    public interface $part{}

    public Class<_method> _modelType(){
        return _method.class;
    }

    /**
     * 
     * @param protoMethod
     * @return 
     */
    public static $method of( String protoMethod ){
        return of(new String[]{protoMethod});
    }
    
    /**
     * Pass in an anonymous Object containing the method to import
     * NOTE: if the anonymous Object contains more than one method, ENSURE only one method
     * DOES NOT have the @_remove annotation, (mark all trivial METHODS with @_remove)
     *
     * ALSO does a "Post Parameterize" step
     *
     * @param anonymousObjectContainingMethod
     * @return
     * @see _remove for methods that exist in the anonymous object & contain
     */
    public static $method of( Object anonymousObjectContainingMethod ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        _method _m = from(ste, anonymousObjectContainingMethod);
        $method $m = of( _m );
        return $m;
    }

    /**
     *
     * @return
     */
    public static $method of(){
        return new $method(_method.of("$type$ $name$();") ).anyBody();
    }


    public static $method of( MethodDeclaration astMd){
        return of( _method.of(astMd));
    }

    /**
     * 
     * @param _m
     * @return 
     */
    public static $method of( _method _m ){
        return new $method( _m);
    }

    /**
     * 
     * @param _m
     * @param constraint
     * @return 
     */
    public static $method of( _method _m, Predicate<_method> constraint){
        return new $method( _m).$and(constraint);
    }
        
    /**
     * 
     * @param clazz
     * @param name
     * @return 
     */
    public static $method of( Class clazz, String name ){
        _method._withMethods _hm = (_method._withMethods) _type.of(clazz);
        return of( _hm.getMethod(name) );
    }       
    
    /**
     * 
     * @param proto
     * @return 
     */
    public static $method of( String...proto ){
        return new $method(_method.of(proto));
    }
    
    /**
     * 
     * @param proto
     * @param constraint
     * @return 
     */
    public static $method of( String proto, Predicate<_method> constraint ){
        return new $method(_method.of(proto) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $method of( Predicate<_method> constraint ){
        return of().$and(constraint);
    }
    
    /**
     * 
     * @param part
     * @return 
     */
    public static $method of( $part part ){
        $part[] parts = new $part[]{part};
        return of(parts);
    }

    public static $method of( String name, $part...parts){
        return new $method(parts).$name(name);
    }

    /**
     * Build a method from the constituent parts
     * @param parts the parts of the $method
     * @return the $method
     */
    public static $method of( $part...parts ){
        return new $method( parts ); 
    }


    public static $method.Or or( _method... _protos ){
        $method[] arr = new $method[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $method.of( _protos[i]);
        }
        return or(arr);
    }

    public static $method.Or or( $method...$tps ){
        return new $method.Or($tps);
    }


    public static _method from (StackTraceElement ste, Object anonymousObjectContainingMethod ){
        ObjectCreationExpr oce = Expr.newExpr( ste );
        if( anonymousObjectContainingMethod instanceof $pattern){
            throw new UnsupportedOperationException("We cant create an instance of $method from unsupported $pattern");
        }

        MethodDeclaration theMethod = (MethodDeclaration)
                oce.getAnonymousClassBody().get().stream().filter(m -> m instanceof MethodDeclaration &&
                        !m.isAnnotationPresent(_remove.class) ).findFirst().get();
        //apply macros
        _method _m = macro.to(anonymousObjectContainingMethod.getClass(), theMethod  );
        return _m;
    }

    public static $method as( Object anonymousObjectContainingMethod ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        _method _m = from(ste, anonymousObjectContainingMethod);
        return as(_m);
    }

    public static $method as( String...method ){
        return as( _method.of(method));
    }

    public static $method as( _method _m ){
        $method $m = new $method();
        if( _m.hasJavadoc() ){
            $m.javadoc = $comment.javadocComment(_m.getJavadoc());
        }
        if( _m.hasAnnoExprs() ){
            $m.annos = $annoRefs.as(_m.getAnnoExprs() );
        } else{
            $m.annos = $annoRefs.none();
        }
        $m.modifiers = $modifiers.as(_m );
        $m.type = $typeRef.as(_m.getTypeRef());
        if( !_m.hasTypeParams() ){
            final _typeParams etps = _m.getTypeParams();
            $m.typeParameters = $typeParameters.as(etps);
        } else{
            $m.typeParameters = $typeParameters.none();
        }
        $m.name = $name.as(_m.getName() );
        if( _m.hasParams() ){
            $m.parameters = $parameters.as(_m.getParams());
        } else{
            $m.parameters = $parameters.none();
        }
        $m.thrown = $throws.as( _m.getThrows() );
        $m.body = $body.as(_m.getBody());
        return $m;
    }

    /**
     * Matcher for methods not composed of the parts passed in
     * @param parts parts for excluding methods
     * @return a $method matcher
     */
    public static $method not($part...parts ){
        $method $m = of();
        $m.$not(parts);
        return $m;
    }

    public Predicate<_method> constraint = t -> true;

    public $comment<JavadocComment> javadoc = $comment.javadocComment("$javadoc$");
    public $annoRefs annos = new $annoRefs();
    public $modifiers modifiers = $modifiers.of();
    public $typeRef type = $typeRef.of();

    public $typeParameters typeParameters = $typeParameters.of();
    public $name name = $name.of();    
    public $parameters parameters = $parameters.of();
    public $throws thrown = $throws.of();
    public $body body = $body.of();
    
    private $method( _method _p ){
        this( _p, t-> true );
    }

    public String toString(){
        if( isMatchAny() ){
            return "$method{ $ANY$ }";
        }
        StringBuilder str = new StringBuilder();
        str.append("$method{").append( System.lineSeparator() );
        if( !javadoc.isMatchAny() ){
            str.append( Text.indent( javadoc.toString()));
        }
        if( !annos.isMatchAny() ){
            str.append(Text.indent(annos.toString()));
        }
        if( !modifiers.isMatchAny() ){
            str.append(Text.indent( modifiers.toString()) );
        }
        if( !type.isMatchAny() ){
            str.append(Text.indent( type.toString()));
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
     * Build a $method from component parts
     * @param parts 
     */
    private $method($part ...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $annoRefs){
                this.annos = ($annoRefs)parts[i];
            }
            else if(parts[i] instanceof $annoRef){
                this.annos.$annosList.add( ($annoRef)parts[i]);
            }
            else if(parts[i] instanceof $modifiers){
                this.modifiers = $modifiers.of( this.modifiers, ($modifiers)parts[i]);
            }
            else if(parts[i] instanceof $typeRef){
                this.type = ($typeRef)parts[i];
            }
            else if(parts[i] instanceof $name){
                this.name = ($name)parts[i];
            }
            else if(parts[i] instanceof $parameters){
                this.parameters = ($parameters)parts[i];
            }
            else if(parts[i] instanceof $parameter){
                this.parameters.$add(  ($parameter)parts[i] );
            }
            else if(parts[i] instanceof $body){
                this.body = ($body)parts[i];
            }
            else if(parts[i] instanceof $stmt){
                this.body.$and( ($stmt)parts[i]);
            }
            else if(parts[i] instanceof $ex){
                this.body.$and( ($ex)parts[i]);
            }
            else if(parts[i] instanceof $switchEntry){
                this.body.$and( ($switchEntry)parts[i]);
            }
            else if(parts[i] instanceof $catch){
                this.body.$and( ($catch)parts[i]);
            }
            else if(parts[i] instanceof $var){
                this.body.$and( ($var)parts[i]);
            }
            else if(parts[i] instanceof $node){
                this.body.$and( ($node)parts[i]);
            }
            else if( parts[i] instanceof $throws ){
                this.thrown = ($throws)parts[i];
            }
            else if( parts[i] instanceof $typeParameters ){
                this.typeParameters = ($typeParameters)parts[i];
            }
            else if( parts[i] instanceof $typeParameter ){
                this.typeParameters.typeParams.add( ($typeParameter)parts[i]);
            }
            else if( parts[i] instanceof $comment ){
                this.javadoc = ($comment<JavadocComment>)parts[i];
            }
            else{
                throw new _jdraftException("Unable to add $part "+ parts[i]+" to $method" );
            }
        }
    }
    
    /**
     * 
     * @param _m the method prototype
     * @param
     */
    private $method( _method _m, Predicate<_method> constraint){
        if( _m.hasJavadoc() ){
            javadoc = $comment.javadocComment(_m.getJavadoc() );
        }        
        if( _m.hasAnnoExprs() ){
            annos = $annoRefs.of(_m.getAnnoExprs() );
        }
        modifiers = $modifiers.of(_m);
        type = $typeRef.of(_m.getTypeRef() );
        if( !_m.hasTypeParams() ){
            final _typeParams etps = _m.getTypeParams();
            typeParameters = $typeParameters.of( etps );           
        }
        name = $name.of(_m.getName());
        if( _m.hasParams() ){
            parameters = $parameters.of(_m.getParams());
        }        
        thrown = $throws.of(_m.getThrows());
        body = $body.of( _m.ast() );
        this.constraint = constraint;
    }
    
    /**
     * matches a body that is implemented i.e. "m(){ ... }" 
     * -or- not implemented
     * "m();"
     * @return the $method prototype
     */
    public $method anyBody(){
        this.body = $body.of();
        return this;
    }

    /**
     * Does this prototype match ANY _method?
     * @return true if this prototype will match ANY method
     */
    public boolean isMatchAny(){
        try {
            return this.constraint.test(null) &&
                    this.annos.isMatchAny() &&
                    this.name.isMatchAny() &&
                    this.type.isMatchAny() &&
                    this.typeParameters.isMatchAny() &&
                    this.thrown.isMatchAny() &&
                    this.body.isMatchAny() &&
                    this.javadoc.isMatchAny() &&
                    this.modifiers.isMatchAny() &&
                    this.parameters.isMatchAny();
        } catch(Exception e){
          return false;
        }
    }

    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified prototype
     */
    public $method $and(Predicate<_method>constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    /**
     * Add a constraint the the method must have all of these annotations
     * @param annotationClass the type of annotation
     * @return the modified $method
     */
    public $method $and(Class<? extends Annotation>...annotationClass){
        for(int i=0;i<annotationClass.length;i++){
            $and( $annoRef.of(annotationClass[i]) );
        }
        return this;
    }

    /**
     * Adds a NOT constraint to the {@link #constraint} based on one or more $method.$part
     * @param parts
     * @return
     */
    public $method $and(final $part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $annoRef){
                final $annoRef $fa = (($annoRef)parts[i]);
                Predicate<_method> pf = f-> $fa.countIn(f) > 0;
                $and( pf  );
            }
            else if( parts[i] instanceof $modifiers ){
                final $modifiers $fa = (($modifiers)parts[i]);
                Predicate<_method> pf = f-> $fa.matches(f.getModifiers());
                $and( pf );
            }
            else if( parts[i] instanceof $parameters ){
                final $parameters $fa = (($parameters)parts[i]);
                Predicate<_method> pf = f-> $fa.matches(f.getParams());
                $and( pf  );
            }
            else if( parts[i] instanceof $parameter ){
                final $parameter $fa = (($parameter)parts[i]);
                Predicate<_method> pf = f-> $fa.countIn(f) > 0;
                $and( pf  );
            }
            else if( parts[i] instanceof $typeParameters ){
                final $typeParameters $fa = (($typeParameters)parts[i]);
                Predicate<_method> pf = f-> $fa.matches(f.getTypeParams());
                $and( pf  );
            }
            else if( parts[i] instanceof $typeParameter ){
                final $typeParameter $fa = (($typeParameter)parts[i]);
                Predicate<_method> pf = f-> $fa.countIn(f) > 0;
                $and( pf  );
            }
            else if( parts[i] instanceof $typeRef){
                final $typeRef $ft = (($typeRef)parts[i]);
                Predicate<_method> pf = f-> $ft.matches(f.getTypeRef());
                $and( pf );
            }
            else if( parts[i] instanceof $name){
                final $name $fn = (($name)parts[i]);
                Predicate<_method> pf = f-> $fn.matches(f.getName());
                $and( pf );
            }
            else if( parts[i] instanceof $throws){
                final $throws $ft = (($throws)parts[i]);
                Predicate<_method> pf = f-> $ft.matches(f.getName());
                $and( pf  );
            }
            else if(parts[i] instanceof $comment ){
                final $comment $fj = (($comment)parts[i]);
                Predicate<_method> pf = f-> $fj.matches(f.getJavadoc());
                $and( pf  );
            }
            else if( parts[i] instanceof $body){
                final $body $fj = (($body)parts[i]);
                Predicate<_method> pf = f-> $fj.matches(f.getBody());
                $and( pf  );
            }
            else if( parts[i] instanceof $stmt){
                final $stmt $fj = (($stmt)parts[i]);
                Predicate<_method> pf = f-> $fj.firstIn(f.getBody()) != null;
                $and( pf  );
            }
            else if( parts[i] instanceof $ex){
                final $ex $fj = (($ex)parts[i]);
                Predicate<_method> pf = f-> $fj.firstIn(f.getBody()) != null;
                $and( pf  );
            }

        }
        return this;
    }

    public $method $notThrows(Class<? extends Throwable>...exceptionClass){
        for(int i=0;i<exceptionClass.length;i++){
            $not($throws.of(exceptionClass[i]));
        }
        return this;
    }

    /**
     * The method can NOT have
     * @param annotationClass
     * @return
     */
    public $method $not(Class<? extends Annotation>...annotationClass){
        for(int i=0;i<annotationClass.length;i++){
            $not( $annoRef.of(annotationClass[i]) );
        }
        return this;
    }

    /**
     * Adds a NOT constraint to the {@link #constraint} based on one or more $method.$part
     * @param parts
     * @return
     */
    public $method $not(final $part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $annoRef){
                final $annoRef $fa = (($annoRef)parts[i]);
                Predicate<_method> pf = f-> f.getAnnoExprs().first(a -> $fa.matches(a) ) != null;
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $modifiers ){
                final $modifiers $fa = (($modifiers)parts[i]);
                Predicate<_method> pf = f-> $fa.matches(f.getModifiers());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $parameters ){
                final $parameters $fa = (($parameters)parts[i]);
                Predicate<_method> pf = f-> $fa.matches(f.getParams());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $parameter ){
                final $parameter $fa = (($parameter)parts[i]);
                Predicate<_method> pf = f-> $fa.countIn(f) > 0;
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $typeParameters ){
                final $typeParameters $fa = (($typeParameters)parts[i]);
                Predicate<_method> pf = f-> $fa.matches(f.getTypeParams());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $typeParameter ){
                final $typeParameter $fa = (($typeParameter)parts[i]);
                Predicate<_method> pf = f-> $fa.countIn(f) > 0;
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $typeRef){
                final $typeRef $ft = (($typeRef)parts[i]);
                Predicate<_method> pf = f-> $ft.matches(f.getTypeRef());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $name){
                final $name $fn = (($name)parts[i]);
                Predicate<_method> pf = f-> $fn.matches(f.getName());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $throws){
                final $throws $ft = (($throws)parts[i]);
                Predicate<_method> pf = f-> $ft.matches(f.getName());
                $and( pf.negate() );
            }
            else if(parts[i] instanceof $comment ){
                final $comment $fj = (($comment)parts[i]);
                Predicate<_method> pf = f-> $fj.matches(f.getJavadoc());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $body){
                final $body $fj = (($body)parts[i]);
                Predicate<_method> pf = f-> $fj.matches(f.getBody());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $stmt){
                this.body.$not(($stmt)parts[i]);
                //final $stmt $fj = (($stmt)parts[i]);
                //Predicate<_method> pf = f-> $fj.firstIn(f.getBody()) != null;
                //$and( pf.negate() );
            }
            else if( parts[i] instanceof $ex){
                this.body.$not(($ex)parts[i]);
                //final $ex $fj = (($ex)parts[i]);
                //Predicate<_method> pf = f-> $fj.firstIn(f.getBody()) != null;
                //$and( pf.negate() );
            }
            else if(parts[i] instanceof $switchEntry){
                this.body.$not( ($switchEntry)parts[i]);
            }
            else if(parts[i] instanceof $catch){
                this.body.$not( ($catch)parts[i]);
            }
            else if(parts[i] instanceof $var){
                this.body.$not( ($var)parts[i]);
            }
            else if(parts[i] instanceof $node){
                this.body.$not( ($node)parts[i]);
            }
        }
        return this;
    }

    public boolean match( Node n){
        if( n instanceof MethodDeclaration ){
            return matches( (MethodDeclaration)n);
        }
        return false;
    }

    @Override
    public List<String> $listNormalized(){
        List<String>normalized$ = new ArrayList<>();
        if( javadoc.contentsStencil.isMatchAny() && javadoc.contentsStencil.$list().contains("javadoc")){
            //Javadoc is OPTIONAL (it's only 
        } else{
            //all$.addAll( javadoc.list$() );
            normalized$.addAll( javadoc.$listNormalized() );
        }        
        normalized$.addAll( annos.$listNormalized() );
        normalized$.addAll( typeParameters.$listNormalized() );
        normalized$.addAll( type.$listNormalized() );
        normalized$.addAll( name.nameStencil.$listNormalized() );
        normalized$.addAll( parameters.$listNormalized() );
        normalized$.addAll( thrown.$listNormalized() );
        normalized$.addAll( body.$listNormalized() );
        return normalized$.stream().distinct().collect(Collectors.toList());        
    }

    @Override
    public List<String> $list(){
        List<String>all$ = new ArrayList<>();
        if( javadoc.contentsStencil.isMatchAny() && javadoc.contentsStencil.$list().contains("javadoc")){
            //Javadoc is OPTIONAL (it's only 
        } else{
            all$.addAll( javadoc.$list() );
        }
        all$.addAll( annos.$list() );
        all$.addAll( typeParameters.$list() );
        all$.addAll( type.$list() );
        all$.addAll( name.nameStencil.$list() );
        all$.addAll( parameters.$list() );
        all$.addAll( thrown.$list() );
        all$.addAll( body.$list() );
        return all$;
    }
    
    public $method $parameters(){
        this.parameters = $parameters.of();
        return this;
    }
    
    public $method $parameters( $parameters $ps ){
        this.parameters = $ps;
        return this;
    }
    
    public $method $parameters( String...params ){
        this.parameters = $parameters.of(params);
        return this;
    }
    
    public $method $parameters( Predicate<_params> constraint){
        this.parameters.$and( constraint);
        return this;
    }
    
    public $method $throws(){
        this.thrown = $throws.of();
        return this;
    }
    
    public $method $throws($throws $ts){
        this.thrown = $ts;
        return this;
    }
    
    public $method $throws(Class<? extends Throwable>...thro ){
        this.thrown = $throws.of(thro);
        return this;
    }
    
    public $method $throws( Predicate<_throws> constraint ){
        this.thrown.$and(constraint);
        return this;
    }
    
    public $method $throws(String...thro ){
        this.thrown = $throws.of(thro);
        return this;
    }

    @Override
    public $annoRefs get$Annos() {
        return this.annos;
    }

    public $method $annos(){
        this.annos = $annoRefs.of();
        return this;
    }
    
    public $method $annos( Predicate<_annoExprs> as ){
        this.annos.$and(as);
        return this;
    }
    
    public $method $annos(String...annoPatterns ){
        this.annos.add(annoPatterns);
        return this;
    }
    
    public $method $annos($annoRefs $as ){
        this.annos = $as;
        return this;
    }
    
    public $method $anno( Class clazz){
        this.annos.$annosList.add($annoRef.of(clazz) );
        return this;
    }
    
    public $method $anno( _annoExpr _an){
        this.annos.$annosList.add($annoRef.of(_an) );
        return this;
    }

    @Override
    public $name get$Name(){
        return this.name;
    }

    public $method $name(){
        this.name = $name.of();
        return this;
    }
    
    public $method $name( Predicate<String> str){
        this.name.and(str);
        return this;
    }
    
    public $method $name($name id){
        this.name = id;
        return this;
    }

    @Override
    public $method $name(String name){
        this.name = $name.of(name);
        return this;
    }
    
    public $method $type(){
        this.type = $typeRef.of();
        return this;
    }
    
    public $method $type(Predicate<_typeRef> _tr){
        this.type.$and(_tr);
        return this;
    }
    
    public $method $type(Class typeClazz){
        this.type = $typeRef.of(typeClazz);
        return this;
    }
    
    public $method $type( String typeRef){
        this.type = $typeRef.of(typeRef);
        return this;
    }
    
    public $method $type( $typeRef $tr){
        this.type = $tr;
        return this;
    }
    
    public $method $typeParameters(){
        this.typeParameters = $typeParameters.of(); //pattern("$typeParameters$");
        return this;
    }
    
    public $method $typeParameters($typeParameter $tp){
        this.typeParameters.typeParams.add($tp);
        return this;
    }
    
    public $method $typeParameters(Predicate<_typeParams> constraint){
        this.typeParameters.$and(constraint);
        return this;
    }
    
    public $method $typeParameters(String... tps){
        this.typeParameters = $typeParameters.of(tps);
        return this;
    }

    public $method $modifiers(){
        this.modifiers = $modifiers.of();
        return this;
    }    

    public $method $modifiers( String... modifiers ){
        this.modifiers = $modifiers.of( modifiers);
        return this;
    }

    public $method $modifiers(_modifiers _ms){
        this.modifiers = $modifiers.of(_ms);
        return this;
    }    
    
    public $method $modifiers(Predicate<_modifiers> constraint){
        this.modifiers.$and(constraint);
        return this;
    }
    
    public $method $modifiers( $modifiers $m){
        this.modifiers = $m;
        return this;
    }

    @Override
    public $comment<JavadocComment> get$javadoc() {
        return javadoc;
    }

    @Override
    public $method $javadoc(Predicate<JavadocComment> javadocMatchFn ){
        this.javadoc = $comment.javadocComment(javadocMatchFn);
        return this;
    }

    @Override
    public $method $javadoc ($comment<JavadocComment> javadoc) {
        this.javadoc = javadoc;
        return this;
    }

    public $method $javadoc(_javadocComment javadocComment ){
        this.javadoc = $comment.javadocComment(javadocComment);
        return this;
    }

    public $method $javadoc(){
        this.javadoc = $comment.javadocComment("$javadoc$");
        return this;
    }
    
    public $method $javadoc( String... form ){
        this.javadoc.contentsStencil = Stencil.of((Object[])form);
        return this;
    }
    
    public $method $body (){
        this.body = $body.of();
        return this;
    }
    
    public $method $body ($body $bd ){
        this.body = $bd;
        return this;
    }
    
    public $method $body( Predicate<_body> constraint){
        this.body.$and(constraint);
        return this;
    }

    /**
     * pass in an anonymous Object containing a body that will be used as
     * a template for the $body of this method
     * @param anonymousClassWithMethodContainingBody
     * @return
     */
    public $method $body( Object anonymousClassWithMethodContainingBody ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expr.newExpr(ste);
        Optional<BodyDeclaration<?>> on = oce.getAnonymousClassBody().get().stream().filter(m -> 
            m instanceof MethodDeclaration 
            && !((MethodDeclaration)m)
                .getAnnotationByClass(_remove.class).isPresent() )
                .findFirst();
        if(!on.isPresent()){
            throw new _jdraftException("Could not locate the method containing the body in "+ oce);
        }
        MethodDeclaration md = (MethodDeclaration)on.get();
        md.getParentNode().get().remove(md); //decouple it from the "old" 
        _body _bd = _body.of( md );
        return $body( _bd );
    }
    
    public $method $body(_body _bd ){
       this.body.$bodyStmts( _bd ); 
        return this;
    }
    
    public $method notImplemented(){
        this.body = $body.of(";");
        return this;
    }
    
    public $method emptyBody(){
        this.body = $body.of("{}");
        return this;
    }
    
    @Override
    public _method fill(Translator translator, Object... values) {
        List<String> nom = this.$listNormalized();
        nom.remove( "javadoc");
        nom.remove( "annos");
        nom.remove( "modifiers");
        nom.remove( "typeParameters");
        nom.remove( "parameters");
        nom.remove( "throws");
        nom.remove( "body");
        if( nom.size() != values.length ){
            throw new _jdraftException("Fill expected ("+nom.size()+") values "+ nom+" got ("+values.length+")");
        }
        Tokens ts = new Tokens();
        for(int i=0;i<nom.size();i++){
            ts.put( nom.get(i), values[i]);
        }
        return draft(translator, ts);
    }
    
    @Override
    public _method draft(Translator translator, Map<String, Object> keyValues) {
        
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
            sb.append(System.lineSeparator());
        }        
        sb.append( annos.draft(translator, base));
        sb.append(System.lineSeparator());
        sb.append( modifiers.draft(translator, base));
        sb.append(" ");
        sb.append( typeParameters.draft(translator, base));
        sb.append(" ");
        sb.append( type.draft(translator, base));
        sb.append(" ");
        sb.append( name.draft(translator, base));
        sb.append(" ");
        sb.append( parameters.draft(translator, base));
        sb.append(" ");
        sb.append( thrown.draft(translator, base));
        sb.append(System.lineSeparator());
        
        String bd = body.draft(translator, base).toString();
        if( bd.length() ==0 ){
            //it's an "anyBody", so I default it to an empty implementation
            sb.append("{}");
        } else{
            sb.append( bd );
        }
        return _method.of(sb.toString());        
    }
    
    /**
     * 
     * @param _n
     * @return 

    public _method draft(_java._multiPart _n ){
        return draft(_n.tokenize() );
    }
    */

    public static final BlockStmt EMPTY = Stmt.blockStmt("{}");

    /**
     * 
     * @param method
     * @return 
     */ 
    public Select select( String...method){
        return select(_method.of(method));
    }
    
    /**
     * 
     * @param _m
     * @return 
     */
    public Select select( _method _m){
        if( !this.constraint.test(_m)){
            return null;
        }
        if( modifiers.select(_m) == null){
            return null;
        }
        Tokens all = new Tokens();
        if( _m.hasJavadoc() ){
            all = javadoc.parseTo(_m.getJavadoc().ast(), all);
        } else{
            if(!javadoc.isMatchAny() ){
                return null;
            }
        }
        all = annos.parseTo(_m.getAnnoExprs(), all);
        all = typeParameters.parseTo(_m.getTypeParams(), all);
        all = type.parseTo(_m.getTypeRef(), all);
        all = name.parseTo(_m.getName(), all);
        all = parameters.parseTo(_m.getParams(), all);
        all = thrown.parseTo(_m.getThrows(), all);
        all = body.parseTo(_m.getBody(), all);
        if( all != null ){
            return new Select( _m, $tokens.of(all));
        }
        return null;        
    }

    /**
     * 
     * @param astMethod
     * @return 
     */
    public Select select( MethodDeclaration astMethod){
        return select(_method.of(astMethod ));
    }

    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $method $hardcode(Translator translator, Tokens kvs ) {
        javadoc = javadoc.$hardcode(translator, kvs);
        annos = annos.$hardcode(translator, kvs);
        typeParameters = typeParameters.$hardcode(translator, kvs);
        type = type.$hardcode(translator, kvs);
        name.nameStencil = name.nameStencil.$hardcode(translator, kvs);
        parameters = parameters.$hardcode(translator, kvs);
        thrown = thrown.$hardcode(translator, kvs);
        body = body.$hardcode(translator, kvs);
        
        return this;
    }

    /** Post - parameterize, create a parameter from the target string named $Name#$*/
    @Override
    public $method $(String target, String $paramName) {
        javadoc = javadoc.$(target, $paramName);
        annos = annos.$(target, $paramName);
        typeParameters = typeParameters.$(target, $paramName);
        type = type.$(target, $paramName);
        name.nameStencil = name.nameStencil.$(target, $paramName);
        parameters = parameters.$(target, $paramName);
        thrown = thrown.$(target, $paramName);
        body = body.$(target, $paramName);
        return this;
    }

    @Override
    public Template<_method> $hardcode(Translator translator, Map<String, Object> keyValues) {
        return this.$hardcode(translator, Tokens.of(keyValues));
    }

    /**
     * 
     * @param astExpr
     * @param $name
     * @return 
     */
    public $method $(Expression astExpr, String $name ){
        String exprString = astExpr.toString();
        return $(exprString, $name);
    }

    /**
     * 
     * @param method
     * @return 
     */
    public boolean matches( String method ){
        return select(_method.of(method)) != null;
    }
    
    /**
     * 
     * @param method
     * @return 
     */
    public boolean matches( String... method ){
        return select(_method.of(method)) != null;
    }
    
    /**
     * 
     * @param _m
     * @return 
     */
    public boolean matches( _method _m ){
        return select( _m ) != null;
    }

    /**
     * 
     * @param astMethod
     * @return 
     */
    public boolean matches( MethodDeclaration astMethod ){
        return select(astMethod ) != null;
    }

    /**
     * Returns the first _method that matches the pattern and constraint
     * @param astNode the node to look through
     * @param _methodMatchFn
     * @return  the first _method that matches (or null if none found)
     */
    @Override
    public _method firstIn(Node astNode, Predicate<_method> _methodMatchFn){
        Optional<MethodDeclaration> f = astNode.findFirst(MethodDeclaration.class, s -> {
            Select sel = select(s);
            return sel != null && _methodMatchFn.test(sel._m);
        });         
        if( f.isPresent()){
            return _method.of(f.get());
        }
        return null;
    }

    /**
     * Returns the first _method that matches the pattern and constraint
     * @param clazz the runtime class (WITH source available in classpath)
     * @return  the first _method that matches (or null if none found)
     */
    @Override
    public Select selectFirstIn( Class clazz){
        return selectFirstIn( (_type) _type.of(clazz));
    }
    
    /**
     * Returns the first _method that matches the pattern and constraint
     * @param clazz the runtime class (WITH source available in classpath)
     * @param selectConstraint
     * @return  the first _method that matches (or null if none found)
     */
    public Select selectFirstIn( Class clazz, Predicate<Select> selectConstraint){
        return selectFirstIn( (_type) _type.of(clazz), selectConstraint);
    }
    
    /**
     * Returns the first _method that matches the pattern and constraint
     * @param astNode the node to look through
     * @return  the first _method that matches (or null if none found)
     */
    @Override
    public Select selectFirstIn( Node astNode ){
        Optional<MethodDeclaration> f = astNode.findFirst(MethodDeclaration.class, s -> this.matches(s) );         
        if( f.isPresent()){
            return select(f.get());
        }
        return null;
    }
    
    /**
     * Returns the first _method that matches the pattern and constraint
     * @param _j the _java node
     * @param selectConstraint
     * @return  the first _method that matches (or null if none found)
     */
    public Select selectFirstIn(_java._domain _j, Predicate<Select> selectConstraint){
         if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel()){
                return selectFirstIn( ((_codeUnit) _j).astCompilationUnit(), selectConstraint);
            } else{
                return selectFirstIn(((_type) _j).ast(), selectConstraint);
            }
        }
        return selectFirstIn(((_java._node) _j).ast(), selectConstraint);
    }

    /**
     * Returns the first _method that matches the pattern and constraint
     * @param astNode the node to look through
     * @param selectConstraint
     * @return  the first _method that matches (or null if none found)
     */
    public Select selectFirstIn( Node astNode, Predicate<Select> selectConstraint){
        Optional<MethodDeclaration> f = astNode.findFirst(MethodDeclaration.class, s -> {
            Select sel = this.select(s);
            return sel != null && selectConstraint.test(sel);
            });         
        if( f.isPresent()){
            return select(f.get());
        }
        return null;
    }
    
    @Override
    public List<Select> listSelectedIn(Node astNode){
        List<Select>sts = new ArrayList<>();
        astNode.walk(MethodDeclaration.class, m-> {
            Select sel = select( m );
            if( sel != null ){
                sts.add(sel);
            }
        });
        return sts;
    }

    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(Class clazz, Predicate<Select> selectConstraint){
        return listSelectedIn( (_type) _type.of(clazz), selectConstraint);
    }
    
    /**
     * 
     * @param astNode
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(Node astNode, Predicate<Select> selectConstraint){
        List<Select>sts = new ArrayList<>();
        astNode.walk(MethodDeclaration.class, m-> {
            Select sel = select( m );
            if( sel != null && selectConstraint.test(sel)){
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
    public List<Select> listSelectedIn(_java._domain _j, Predicate<Select> selectConstraint){
        List<Select>sts = new ArrayList<>();
        Tree.in(_j, MethodDeclaration.class, m -> {
            Select sel = select( m );
            if( sel != null && selectConstraint.test(sel)){
                sts.add(sel);
            }
        });
        return sts;
    }

    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectedActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Consumer<Select> selectedActionFn ){
        astNode.walk( MethodDeclaration.class, m-> {
            Select s = select( m );
            if( s != null ){
                selectedActionFn.accept( s );
            }
        });
        return astNode;
    }

    /**
     * 
     * @param clazz
     * @param selectedActionFn
     * @return 
     */
    public <_CT extends _type> _CT  forSelectedIn(Class clazz, Consumer<Select> selectedActionFn ){
        return (_CT)forSelectedIn( (_type) _type.of(clazz), selectedActionFn);
    }    
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectedActionFn
     * @return 
     */
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Consumer<Select> selectedActionFn ){
        Tree.in(_j, _method.class, m ->{
            Select s = select( m );
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
    public <N extends Node> N forSelectedIn(N astNode, Predicate<Select> selectConstraint, Consumer<Select> selectedActionFn ){
        astNode.walk( MethodDeclaration.class, m-> {
            Select s = select( m );
            if( s != null && selectConstraint.test(s)){
                selectedActionFn.accept( s );
            }
        });
        return astNode;
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param selectedActionFn
     * @return 
     */
    public <_CT extends _type> _CT  forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectedActionFn ){
        return (_CT)forSelectedIn((_type) _type.of(clazz), selectConstraint, selectedActionFn);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @param selectedActionFn
     * @return 
     */
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> selectedActionFn ){
        Tree.in(_j, _method.class, m ->{
            Select s = select( m );
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
    public <_CT extends _type> _CT replaceIn(Class clazz,  $method $replace ){
        return (_CT) forSelectedIn( (_type) _type.of(clazz), s -> {
            _method repl = $replace.draft(Translator.DEFAULT_TRANSLATOR, s.tokens());
            s._m.ast().replace(repl.ast());
        });
    }

    /**
     * 
     * @param clazz
     * @param replacementProto
     * @return 
     */
    public <_CT extends _type> _CT  replaceIn(Class clazz,  String... replacementProto ){
        return (_CT)replaceIn((_type) _type.of(clazz), $method.of(replacementProto));
    }
    
    /**
     * 
     * @param clazz
     * @param method
     * @return 
     */
    public <_CT extends _type> _CT  replaceIn(Class clazz,  _method method ){
        return (_CT)replaceIn( (_type) _type.of(clazz), $method.of(method));
    }
    
    /**
     * 
     * @param clazz
     * @param astMethod
     * @return 
     */
    public <_CT extends _type> _CT  replaceIn(Class clazz, MethodDeclaration astMethod ){
        return (_CT)replaceIn( (_type) _type.of(clazz), $method.of(astMethod));
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param $replace
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, $method $replace ){
        return forSelectedIn(_j, s -> {
            _method repl = $replace.draft(Translator.DEFAULT_TRANSLATOR, s.tokens.asTokens());
            s._m.ast().replace(repl.ast());
        });
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param replacementProto
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, String... replacementProto ){
        return replaceIn(_j, $method.of(replacementProto));
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param method
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, _method method ){
        return replaceIn(_j, $method.of(method));
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param astMethod
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, MethodDeclaration astMethod ){
        return replaceIn(_j, $method.of(astMethod));
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param _methodMatchFn
     * @param _methodActionFn
     * @return 
     */
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_method>_methodMatchFn, Consumer<_method> _methodActionFn){
        astNode.walk(MethodDeclaration.class, e-> {
            Select sel = select(e);
            if( sel != null && _methodMatchFn.test(sel._m)){
                _methodActionFn.accept( sel._m );
            }
        });
        return astNode;
    }

    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are suppressed.
     */
    public static class Or extends $method{

        final List<$method>ors = new ArrayList<>();

        public Or($method...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $method $hardcode(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.$hardcode(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$method.Or{");
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
        public $method.Select select(MethodDeclaration astNode){
            $method $a = whichMatch(astNode);
            if( $a != null ){
                return $a.select(astNode);
            }
            return null;
        }

        /**
         *
         * @param _m
         * @return
         */
        public $method.Select select(_method _m){
            $method $a = whichMatch(_m);
            if( $a != null ){
                return $a.select(_m);
            }
            return null;
        }

        public boolean isMatchAny(){
            return false;
        }

        public $method whichMatch(_method _m){
            return whichMatch(_m.ast());
        }

        /**
         * Return the underlying $method that matches the Method or null if none of the match
         * @param ae
         * @return
         */
        public $method whichMatch(MethodDeclaration ae){
            if( !this.constraint.test(_method.of(ae) ) ){
                return null;
            }
            Optional<$method> orsel  = this.ors.stream().filter( $p-> $p.match(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }
    
    /**
     * A Matched Selection result returned from matching a prototype $method
     * inside of some Node or _node
     */
    public static class Select implements $pattern.selected,
            selectAst<MethodDeclaration>,
            select_java<_method> {
        
        public final _method _m;
        public final $tokens tokens;

        public Select( _method _m, $tokens tokens ){
            this._m = _m;
            this.tokens = tokens;
        }
                
        public Select( MethodDeclaration astMethod, $tokens tokens ){
            this._m = _method.of(astMethod);
            this.tokens = tokens;
        }

        @Override
        public $tokens tokens(){
            return tokens;
        }
        
        @Override
        public String toString(){
            return "$method.Select{"+ System.lineSeparator()+
                Text.indent(_m.toString() )+ System.lineSeparator()+
                Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                "}";
        }

        @Override
        public MethodDeclaration ast() {
            return _m.ast();
        }

        @Override
        public _method _node() {
            return _m;
        }
        
        public boolean isType( Class type ){
            return _m.isTypeRef(type);
        }
        
        public boolean isType( String type ){
            return _m.isTypeRef(type);
        }
        
        public boolean isType( Type type ){
            return _m.isTypeRef(type);
        }
        
        public boolean isType( _typeRef _tr ){
            return _m.isTypeRef(_tr);
        }
        
        public boolean isVarArg(){
            return _m.isVarArg();
        }
        
        public boolean isAbstract(){
            return _m.isAbstract();
        }
        
        public boolean hasBody(){
            return _m.hasBody();
        }
        
        public boolean isVoid(){            
            return _m.isVoid();
        }
        
        public boolean hasThrows(){
            return _m.hasThrows();
        }
        
        public boolean hasThrow(Class<? extends Throwable> throwsClass){            
            return _m.hasThrow(throwsClass);
        }
        
        public boolean hasParameters(){            
            return _m.hasParams();
        }
        
        public boolean is(String...methodDeclaration){
            return _m.is(methodDeclaration);
        }
        
        public boolean hasTypeParameters(){            
            return _m.hasTypeParams();
        }
    }
}
