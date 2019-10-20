package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jdraft.*;
import org.jdraft._anno._annos;

/**
 * @author Eric
 */
public class $parameter implements Template<_parameter>, $pattern<_parameter, $parameter>,
        $pattern.$java<_parameter,$parameter>, $method.$part, $constructor.$part {

    public Class<_parameter> _modelType(){
        return _parameter.class;
    }

    /**
     * the component parts of a $parameter
     */
    public interface $part{}
    
    
    /** a constraint to be applied for matching*/
    public Predicate<_parameter> constraint = t->true;
    
    /** 
     * in this context, false will match final or non final, 
     * will compose w/o final 
     */
    public Boolean isFinal = null;
    
    /** 
     * in this context, false will match varARg or non varArg, 
     * will compose w/o varArg 
     */
    public Boolean isVarArg = null;
    
    /** Name of the parameter */
    public $name name = $name.of();
    
    /** the underlying type of the parameter */
    public $typeRef type = $typeRef.of();
    
    /** annos prototype */
    public $annos annos = new $annos();
    
    /**
     * <PRE>
     * This signifies that :
     * when matching the parameter MUST be final
     * when composing the parameter will be made final
     * </PRE>
     */
    public static class FINAL_PARAMETER implements $part { }
    
    /**
     * <PRE>
     * This signifies that :
     * when matching the parameter MUST be a Var Arg
     * when composing the parameter will be made a Var Arg
     * </PRE>
     */
    public static class VAR_ARG_PARAMETER implements $part { }
    
    /**
     * Build a prototype of a parameter accepting these parts
     * @param parts
     * @return 
     */
    public static $parameter of( $part... parts ){
        return new $parameter(parts);
    }

    /**
     *
     * @return
     */
    public static $parameter of(){
        return new $parameter( $typeRef.of( ), $name.of() );
    }


    /**
     *
     * @param astParam
     * @return
     */
    public static $parameter of( Parameter astParam ){
        return new $parameter( _parameter.of(astParam), p->true);
    }

    /**
     * Build and return a parameter
     * @param _p the prototype parameter
     * @return the $parameter
     */
    public static $parameter of( _parameter _p ){
        return new $parameter( _p, p->true );
    }


    /**
     * Build and return a parameter
     * @param parameter
     * @return
     */
    public static $parameter of( String parameter ){
        return new $parameter( _parameter.of(parameter), p->true );
    }

    public static $parameter of( Class parameterType ){
        return new $parameter( $typeRef.of(parameterType));
    }

    /**
     * Build a prototype of a parameter accepting these parts
     * @param part the single part of the parameter
     * @return 
     */
    public static $parameter of( $part part ){
        $part[] parts = new $part[]{part};
        return new $parameter(parts);
    }

    public static $parameter.Or or( _parameter... _protos ){
        $parameter[] arr = new $parameter[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $parameter.of( _protos[i]);
        }
        return or(arr);
    }

    public static $parameter.Or or( $parameter...$tps ){
        return new $parameter.Or($tps);
    }

    public static $parameter as( String param ){
        return as( _parameter.of(param) );
    }

    public static $parameter as( _parameter _p){
        $annos $as = $annos.none();
        if( _p.hasAnnos() ){
            $as = $annos.as(_p); //set the EXACT annos
        }
        $name $nm = $name.of( _p.getName() );
        $typeRef $tr = $typeRef.as(_p.getType());
        $parameter $p = of( $as, $nm, $tr );
        if( _p.isFinal() ){
            $p.isFinal = true;
        } else{
            $p.isFinal = false;
        }
        if( _p.isVarArg() ){
            $p.isVarArg = true;
        } else{
            $p.isVarArg = false;
        }
        return $p;
        //return of( $as, $nm, $tr ).$and( _pa -> Objects.equals(_pa.isFinal(), _p.isFinal()) && Objects.equals(_pa.isVarArg(), _p.isVarArg() ) );
    }

    /**
     *
     * @param parts
     * @return
     */
    public static $parameter not( $part...parts){
        $parameter $p = of();
        $p.$not(parts);
        return $p;
    }

    /**
     * 
     * @param parts 
     */
    public $parameter( $part...parts){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $name){
                name = ($name)parts[i];
            }
            else if( parts[i] instanceof $typeRef){
                type = ($typeRef)parts[i];
            }
            else if( parts[i] instanceof $annos ){
                annos = ($annos)parts[i];
            }
            else if( parts[i] instanceof $anno){
                annos.$annosList.add( ($anno)parts[i]);
            } 
            else if(parts[i] instanceof FINAL_PARAMETER){
                this.isFinal = true;
            }
            else if(parts[i] instanceof VAR_ARG_PARAMETER){
                this.isVarArg = true;
            }
            else{
                throw new _draftException("unable to process part ["+i+"] "+ parts[i]+" not of expected type");
            }
        }
    }

    /**
     * Adds a NOT constraint to the {@link #constraint} based on one or more $parameter.$part
     * @param parts
     * @return
     */
    public $parameter $not(final $part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $anno ){
                final $anno $fa = (($anno)parts[i]);
                Predicate<_parameter> pf = f-> $fa.count(f) > 0;
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $name){
                final $name $fn = (($name)parts[i]);
                Predicate<_parameter> pf = f-> $fn.matches(f.getName());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $typeRef){
                final $typeRef $fj = (($typeRef)parts[i]);
                Predicate<_parameter> pf = f-> $fj.matches(f.getType());
                $and( pf.negate() );
            }
        }
        return this;
    }


    /**
     * 
     * @param type
     * @param name 
     */
    public $parameter( $typeRef type, $name name ){
        this( $annos.of(), type, name );
    }
    
    /**
     * 
     * @param annos
     * @param type
     * @param name 
     */
    public $parameter( $annos annos, $typeRef type, $name name ){
        this.annos = annos;
        this.type = type;
        this.name = name;        
    }
    
    /**
     * 
     * @param _p
     * @param constraint 
     */
    public $parameter( _parameter _p, Predicate<_parameter> constraint){
        if( _p.isFinal() ){
            this.isFinal = true;
        }
        if( _p.isVarArg() ){
            this.isVarArg = true;
        }
        this.name.nameStencil = Stencil.of(_p.getName() );
        this.type = $typeRef.of(_p.getType());     
        this.annos = $annos.of( _p.getAnnos() );        
    }
    
    public $parameter $anno(){
        this.annos = $annos.of();
        return this;
    }
    
    public $parameter $anno( $anno $ann ){
        this.annos.$annosList.add($ann);
        return this;
    }
    
    public $parameter $annos( $annos $anns){
        this.annos = $anns;
        return this;
    }
    
    public $parameter $annos( Predicate<_annos> annosPredicate){
        this.annos.$and(annosPredicate);
        return this;
    }
    
    public $parameter $name(){
        this.name = $name.of();
        return this;
    }
    
    public $parameter $name( $name name ){
        this.name = name;
        return this;
    }
    
    public $parameter $name( String name ){
        this.name.nameStencil = Stencil.of(name);
        return this;
    }
    
    public $parameter $name( String name, Predicate<String> constraint){
        this.name= $name.of(name).and(constraint);
        return this;
    }
    
    public $parameter $name( Predicate<String> constraint){
        this.name.and(constraint);
        return this;
    }
    
    public $parameter $type(){
        this.type = $typeRef.of();
        return this;
    }
    
    public $parameter $type( String type ){
        this.type.type = Ast.typeRef( type );
        return this;
    }
    
    public $parameter $type( String type, Predicate<_typeRef> constraint){
        this.type = $typeRef.of( type, constraint);
        return this;
    }
    
    public $parameter $type( Predicate<_typeRef> constraint){
        this.type.$and(constraint);
        return this;
    }    
    
    /**
     * Will this $parameter match ANY
     * @return 
     */
    public boolean isMatchAny(){
        return this.annos.isMatchAny() && isFinal == null  && isVarArg == null && this.name.isMatchAny() && this.type.isMatchAny();
    }

    public boolean match( Node n){
        if( n instanceof Parameter ){
            return matches( (Parameter) n);
        }
        return false;
    }

    /**
     * Compose the parameter into a String 
     * @param translator
     * @param keyValues
     * @return 
     */
    public String composeToString( Translator translator, Map<String, Object> keyValues) {
        StringBuilder sb = new StringBuilder();
        //here use a single " " as a separator between annos and after the last anno
        sb.append( this.annos.draft(translator, keyValues, " ") );
        if( isFinal != null && isFinal){
            sb.append("final ");
        }
        sb.append( type.draft(translator, keyValues).toString() );
        if( isVarArg != null && isVarArg){
            sb.append("...");
        }
        sb.append(" ");
        sb.append(name.draft(translator, keyValues));
        return sb.toString();
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if( !this.annos.isMatchAny() ) {
            sb.append(this.annos.toString());
        }
        if( isFinal != null && isFinal ){
            sb.append("final ");
        }
        sb.append( type.type );
        if( isVarArg != null && isVarArg){
            sb.append("...");
        }
        sb.append(" ");
        sb.append(name.nameStencil);
        //System.out.println("PPP"+ sb.toString() );
        return sb.toString();        
    }
    
    public $parameter hardcode$(Object...keyValues){
        this.name.hardcode$(Translator.DEFAULT_TRANSLATOR, keyValues);
        this.annos.hardcode$(Translator.DEFAULT_TRANSLATOR, keyValues);
        this.type.hardcode$(Translator.DEFAULT_TRANSLATOR, keyValues);
        return this;
    }
    
    @Override
    public _parameter draft(Translator translator, Map<String, Object> keyValues) {
        return _parameter.of( composeToString(translator, keyValues));
    }
    
    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified prototype
     */
    public $parameter $and(Predicate<_parameter>constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    /**
     * 
     * @param astParam
     * @return 
     */
    public boolean matches( Parameter astParam ){
        return select(_parameter.of(astParam)) != null;
    }
    
    /**
     * 
     * @param _p
     * @return 
     */
    public boolean matches( _parameter _p ){
        return select(_p) != null;
    }
    
    /**
     * 
     * @param parameter
     * @return 
     */
    public boolean matches( String parameter ){
        return select(parameter) != null;
    }
    
    /**
     * 
     * @param astParameter
     * @return 
     */
    public Select select( Parameter astParameter ){
        return select(_parameter.of(astParameter));
    }
    
    /**
     * 
     * @param parameter
     * @return 
     */
    public Select select( String parameter){
        try{
            return select( _parameter.of(parameter));
        }catch(Exception e){
            return null;
        }        
    }
    
    /**
     * 
     * @param _p
     * @return 
     */
    public Select select( _parameter _p ){

        if( this.isFinal != null ){
            if( this.isFinal ) {
                if( !_p.isFinal()){
                    return null;
                }
            }
            else{
                if( _p.isFinal() ){
                    return null;
                }
            }
        }
        if( this.isVarArg != null ){
            if( this.isVarArg ) {
                if( !_p.isVarArg()){
                    return null;
                }
            }
            else{
                if( _p.isVarArg() ){
                    return null;
                }
            }
        }
        /*
        if( this.isFinal && !_p.isFinal() 
            || this.isVarArg && !_p.isVarArg() ){            
            return null;
        }
        */
        $annos.Select ans = annos.select(_p.ast() );
        if( ans == null ){
            return null;
        }
        Tokens all = ans.tokens.asTokens();
        
        $typeRef.Select sel = type.select(_p.getType());
        
        if( sel != null ){            
            if( !all.isConsistent( sel.tokens.asTokens() ) ){
                return null;
            }
            all.putAll(sel.tokens.asTokens() );
            
            all = this.name.parseTo(_p.getName(), all);
            
            if( all != null ){
                return new Select(_p, all);
            }
        }
        return null;        
    }
    
    @Override
    public $parameter $(String target, String $paramName) {
        this.annos.$(target, $paramName);
        this.name.$(target, $paramName);
        this.type.$(target, $paramName);
        return this;
    }

    @Override
    public $parameter hardcode$(Translator translator, Tokens kvs) {
        this.annos = this.annos.hardcode$(translator, kvs);
        this.type = this.type.hardcode$(translator, kvs);
        this.name = this.name.hardcode$(translator, kvs);
        return this;
    }

    @Override
    public List<String> list$() {
        List<String> all = new ArrayList<>();
        all.addAll( this.annos.list$());
        all.addAll( this.type.list$() );
        all.addAll( this.name.list$() );
        return all;
    }

    @Override
    public List<String> list$Normalized() {
        List<String> all = new ArrayList<>();
        all.addAll( this.annos.list$Normalized());
        all.addAll( this.type.list$Normalized() );
        all.addAll( this.name.list$Normalized() );
        return all.stream().distinct().collect(Collectors.toList());
    }
    
    /**
     * 
     * @param clazz
     * @return 
     */
    @Override
    public List<$parameter.Select>listSelectedIn( Class clazz){
        return listSelectedIn( (_type)_java.type(clazz));
    }
    
    @Override
    public List<Select> listSelectedIn(Node astNode) {
        List<Select> found = new ArrayList<>();
        astNode.walk(Parameter.class, p-> {
            Select sel = select(p);
            if( sel != null ){
                found.add(sel);
            }
        });
        return found;
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(Class clazz, Predicate<Select> selectConstraint) {
        return listSelectedIn( (_type)_java.type(clazz), selectConstraint);
    }
    
    /**
     * 
     * @param astRootNode
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(Node astRootNode, Predicate<Select> selectConstraint) {
        List<Select> found = new ArrayList<>();
        astRootNode.walk(Parameter.class, p-> {
            Select sel = select(p);
            if( sel != null && selectConstraint.test(sel)){
                found.add(sel);
            }
        });
        return found;
    }

    /**
     * 
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(_model _j, Predicate<Select> selectConstraint) {
        List<Select> found = new ArrayList<>();
        _walk.in(_j, _parameter.class, p-> {
            Select sel = select(p);
            if( sel != null && selectConstraint.test(sel)){
                found.add(sel);
            }
        });
        return found;
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param _parameterMatchFn
     * @param _parameterActionFn
     * @return 
     */
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_parameter> _parameterMatchFn, Consumer<_parameter> _parameterActionFn) {
        astNode.walk(Parameter.class, p-> {
            Select sel = select(p);
            if( sel != null && _parameterMatchFn.test(sel._param)){
                _parameterActionFn.accept(_parameter.of(p));
            }
        });
        return astNode;
    }

    /**
     * 
     * @param clazz
     * @param selectActionFn
     * @return 
     */
    public <_CT extends _type> _CT forSelectedIn(Class clazz, Consumer<Select> selectActionFn) {
        return (_CT)forSelectedIn( (_type)_java.type(clazz), selectActionFn);
    }

    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param selectActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode, Consumer<Select> selectActionFn) {        
        astRootNode.walk(Parameter.class, p-> {
            Select sel = select(p);
            if( sel != null ){
                selectActionFn.accept(sel);
            }
        });
        return astRootNode;
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectActionFn
     * @return 
     */
    public <_J extends _model> _J forSelectedIn(_J _j, Consumer<Select> selectActionFn) {
        return _walk.in(_j, _parameter.class, p->{
            Select sel = select(p);
            if( sel != null ){
                selectActionFn.accept(sel);
            }
        });
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public <_CT extends _type> _CT forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectConstraint, selectActionFn);
    }
    
    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param selectActionFn
     * @param selectConstraint
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {        
        astRootNode.walk(Parameter.class, p-> {
            Select sel = select(p);
            if( sel != null && selectConstraint.test(sel)){
                selectActionFn.accept(sel);
            }
        });
        return astRootNode;
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectActionFn
     * @param selectConstraint
     * @return 
     */
    public <_J extends _model> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        return _walk.in(_j, _parameter.class, p->{
            Select sel = select(p);
            if( sel != null && selectConstraint.test(sel)){
                selectActionFn.accept(sel);
            }
        });
    }
    
    /**
     * 
     * @param clazz
     * @return 
     */
    public _parameter firstIn( Class clazz ){
        return firstIn( (_type)_java.type(clazz));
    }
    
    /**
     * Returns the first _field that matches the pattern and constraint
     * @param _j the _java node
     * @return  the first _field that matches (or null if none found)
     */
    @Override
    public _parameter firstIn( _model _j){
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                return firstIn( ((_code) _j).astCompilationUnit() );
            }
            return firstIn( ((_type) _j).ast() );
        }
        return firstIn( ((_node) _j).ast() );
    }
    
    /**
     * Returns the first _field that matches the pattern and constraint
     * @param astStartNode the AST Node
     * @param _parameterMatchFn
     * @return  the first _field that matches (or null if none found)
     */
    @Override
    public _parameter firstIn(Node astStartNode, Predicate<_parameter> _parameterMatchFn){
        Optional<Parameter> p = astStartNode.findFirst(Parameter.class, s ->{
            Select sel = select(s);
            return sel != null && _parameterMatchFn.test( sel._param );
          });         
        if( p.isPresent()){
            return _parameter.of(p.get());
        }
        return null;
    }
    
    /**
     * Returns the first _field that matches the pattern and constraint
     * @param astNode the AST Node
     * @return  the first _field that matches (or null if none found)
     */
    @Override
    public Select selectFirstIn( Node astNode ){
        Optional<Parameter> p = astNode.findFirst(Parameter.class, s -> this.matches(s) );         
        if( p.isPresent()){
            return select( p.get() );
        }
        return null;
    }
    
    /**
     * Returns the first _field that matches the pattern and constraint
     * @param clazz class
     * @param selectConstraint
     * @return  the first _field that matches (or null if none found)
     */
    public Select selectFirstIn( Class clazz, Predicate<Select>selectConstraint ){
        return selectFirstIn((_type)_java.type(clazz), selectConstraint);
    }
    
    /**
     * Returns the first _field that matches the pattern and constraint
     * @param _j the _java node
     * @param selectConstraint
     * @return  the first _field that matches (or null if none found)
     */
    public Select selectFirstIn(_model _j, Predicate<Select>selectConstraint ){

        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                return selectFirstIn( ((_code) _j).astCompilationUnit(), selectConstraint );
            }
            return selectFirstIn( ((_type)_j).ast(), selectConstraint);
        }
        return selectFirstIn( ((_node)_j).ast(), selectConstraint);
    }
    
    /**
     * Returns the first _field that matches the pattern and constraint
     * @param astNode the AST Node
     * @param selectConstraint
     * @return  the first _field that matches (or null if none found)
     */
    public Select selectFirstIn( Node astNode, Predicate<Select>selectConstraint ){
        Optional<Parameter> p = astNode.findFirst(Parameter.class, 
            pa -> {
                Select s = this.select(pa); 
                return s != null && selectConstraint.test(s);
            });         
        if( p.isPresent()){
            return select(p.get());
        }
        return null;        
    }

    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are suppressed.
     */
    public static class Or extends $parameter{

        final List<$parameter>ors = new ArrayList<>();

        public Or($parameter...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $parameter hardcode$(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.hardcode$(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$parameter.Or{");
            sb.append(System.lineSeparator());
            ors.forEach($a -> sb.append( Text.indent($a.toString()) ) );
            sb.append("}");
            return sb.toString();
        }

        /**
         *
         * @param n
         * @return
         */
        public $parameter.Select select(Parameter n){
            $parameter $a = whichMatch(n);
            if( $a != null ){
                return $a.select(n);
            }
            return null;
        }

        public boolean isMatchAny(){
            return false;
        }

        /**
         * Return the underlying $method that matches the Method or null if none of the match
         * @param parameter
         * @return
         */
        public $parameter whichMatch(Parameter parameter){
            Optional<$parameter> orsel  = this.ors.stream().filter( $p-> $p.match(parameter) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }

    /**
     * A Matched Selection result returned from matching a prototype $anno
     * inside of some Node or _node
     */
    public static class Select 
        implements $pattern.selected, select_java<_parameter>, selectAst<Parameter> {
        
        public final _parameter _param;
        public final $tokens tokens;

        public Select ( _parameter _p, Tokens tokens){
            this._param = _p;
            this.tokens = $tokens.of(tokens);
        }
        
        public Select ( _parameter _p, $tokens $a){
            this._param = _p;
            tokens = $a;
        }
        
        public Select ( Parameter astParam, $tokens tokens){
            this( _parameter.of(astParam), tokens);
        }
        
        public boolean hasAnno( Class<? extends Annotation> annotationClass){            
            return _param.hasAnno(annotationClass);
        }
        
        public boolean is(String...expectedParameter ){            
            return _param.is(expectedParameter);            
        }
        
        public boolean isType( Class typeClass ){
            return _param.isType(typeClass);            
        }
        
        public boolean isFinal(){
            return _param.isFinal();
        }
        
        public boolean isVarArg(){
            return _param.isVarArg();
        }
        
        public boolean isNamed(String name){
            return _param.isNamed(name);
        }
        
        /**
         * <PRE>
         * Unknown type is used for Lambda expressions, where you can define it like:
         * (u) -> System.out.println(u);
         * 
         * </PRE>
         * 
         * @return 
         */
        public boolean isUnknownType(){
            return this._param.getType().isUnknownType();
        }
        
        @Override
        public $tokens tokens(){
            return tokens;
        }
        
        @Override
        public String toString(){
            return "$parameter.Select {"+ System.lineSeparator()+
                Text.indent(_param.toString() )+ System.lineSeparator()+
                Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                "}";
        }
        
        @Override
        public Parameter ast(){
            return _param.ast();
        } 
        
        @Override
        public _parameter _node() {
            return _param;
        }        
    }    
}
