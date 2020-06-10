package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jdraft.*;
import org.jdraft._annoExprs;
import org.jdraft.text.*;
import org.jdraft.walk.Walk;

/**
 * @author Eric
 */
public class $parameter implements Template<_param>, //$pattern<_parameter, $parameter>,
        $pattern.$java<_param,$parameter>, $method.$part, $constructor.$part {

    public Class<_param> _modelType(){
        return _param.class;
    }

    /**
     * the component parts of a $parameter
     */
    public interface $part{}
    
    
    /** a constraint to be applied for matching*/
    public Predicate<_param> constraint = t->true;
    
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
    public $annoRefs annos = new $annoRefs();
    
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
        return new $parameter( _param.of(astParam), p->true);
    }

    /**
     * Build and return a parameter
     * @param _p the prototype parameter
     * @return the $parameter
     */
    public static $parameter of( _param _p ){
        return new $parameter( _p, p->true );
    }


    /**
     * Build and return a parameter
     * @param parameter
     * @return
     */
    public static $parameter of( String parameter ){
        return new $parameter( _param.of(parameter), p->true );
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

    public static $parameter.Or or( _param... _protos ){
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
        return as( _param.of(param) );
    }

    public static $parameter as( Parameter p){
        return as(_param.of(p) );
    }

    public static $parameter as( _param _p){
        $annoRefs $as = $annoRefs.none();
        if( _p.hasAnnoExprs() ){
            $as = $annoRefs.as(_p); //set the EXACT annos
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
            else if( parts[i] instanceof $annoRefs){
                annos = ($annoRefs)parts[i];
            }
            else if( parts[i] instanceof $annoRef){
                annos.$annosList.add( ($annoRef)parts[i]);
            } 
            else if(parts[i] instanceof FINAL_PARAMETER){
                this.isFinal = true;
            }
            else if(parts[i] instanceof VAR_ARG_PARAMETER){
                this.isVarArg = true;
            }
            else{
                throw new _jdraftException("unable to process part ["+i+"] "+ parts[i]+" not of expected type");
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
            if( parts[i] instanceof $annoRef){
                final $annoRef $fa = (($annoRef)parts[i]);
                Predicate<_param> pf = f-> $fa.countIn(f) > 0;
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $name){
                final $name $fn = (($name)parts[i]);
                Predicate<_param> pf = f-> $fn.matches(f.getName());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $typeRef){
                final $typeRef $fj = (($typeRef)parts[i]);
                Predicate<_param> pf = f-> $fj.matches(f.getType());
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
        this( $annoRefs.of(), type, name );
    }
    
    /**
     * 
     * @param annos
     * @param type
     * @param name 
     */
    public $parameter($annoRefs annos, $typeRef type, $name name ){
        this.annos = annos;
        this.type = type;
        this.name = name;        
    }
    
    /**
     * 
     * @param _p
     * @param constraint 
     */
    public $parameter(_param _p, Predicate<_param> constraint){
        if( _p.isFinal() ){
            this.isFinal = true;
        }
        if( _p.isVarArg() ){
            this.isVarArg = true;
        }
        this.name.nameStencil = Stencil.of(_p.getName() );
        this.type = $typeRef.of(_p.getType());
        this.annos = $annoRefs.of( _p.getAnnoExprs() );
    }
    
    public $parameter $anno(){
        this.annos = $annoRefs.of();
        return this;
    }
    
    public $parameter $anno( $annoRef $ann ){
        this.annos.$annosList.add($ann);
        return this;
    }
    
    public $parameter $annos( $annoRefs $anns){
        this.annos = $anns;
        return this;
    }
    
    public $parameter $annos( Predicate<_annoExprs> annosPredicate){
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
        this.type.type = Types.of( type );
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
    public String composeToString(Translator translator, Map<String, Object> keyValues) {
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
    
    public $parameter $hardcode(Object...keyValues){
        this.name.hardcode$(Translator.DEFAULT_TRANSLATOR, keyValues);
        this.annos.$hardcode(Translator.DEFAULT_TRANSLATOR, keyValues);
        this.type.$hardcode(Translator.DEFAULT_TRANSLATOR, keyValues);
        return this;
    }
    
    @Override
    public _param draft(Translator translator, Map<String, Object> keyValues) {
        return _param.of( composeToString(translator, keyValues));
    }
    
    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified prototype
     */
    public $parameter $and(Predicate<_param>constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    /**
     * 
     * @param astParam
     * @return 
     */
    public boolean matches( Parameter astParam ){
        return select(_param.of(astParam)) != null;
    }
    
    /**
     * 
     * @param _p
     * @return 
     */
    public boolean matches( _param _p ){
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
        return select(_param.of(astParameter));
    }
    
    /**
     * 
     * @param parameter
     * @return 
     */
    public Select select( String parameter){
        try{
            return select( _param.of(parameter));
        }catch(Exception e){
            return null;
        }        
    }
    
    /**
     * 
     * @param _p
     * @return 
     */
    public Select select( _param _p ){

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
        $annoRefs.Select ans = annos.select(_p.ast() );
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
    public Template<_param> $hardcode(Translator translator, Map<String, Object> keyValues) {
        return null;
    }

    @Override
    public $parameter $hardcode(Translator translator, Tokens kvs) {
        this.annos = this.annos.$hardcode(translator, kvs);
        this.type = this.type.$hardcode(translator, kvs);
        this.name = this.name.hardcode$(translator, kvs);
        return this;
    }

    @Override
    public List<String> $list() {
        List<String> all = new ArrayList<>();
        all.addAll( this.annos.$list());
        all.addAll( this.type.$list() );
        all.addAll( this.name.list$() );
        return all;
    }

    @Override
    public List<String> $listNormalized() {
        List<String> all = new ArrayList<>();
        all.addAll( this.annos.$listNormalized());
        all.addAll( this.type.$listNormalized() );
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
        return listSelectedIn( (_type) _type.of(clazz));
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
        return listSelectedIn( (_type) _type.of(clazz), selectConstraint);
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
    public List<Select> listSelectedIn(_java._domain _j, Predicate<Select> selectConstraint) {
        List<Select> found = new ArrayList<>();
        Walk.in(_j, _param.class, p-> {
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
    public <N extends Node> N forEachIn(N astNode, Predicate<_param> _parameterMatchFn, Consumer<_param> _parameterActionFn) {
        astNode.walk(Parameter.class, p-> {
            Select sel = select(p);
            if( sel != null && _parameterMatchFn.test(sel._param)){
                _parameterActionFn.accept(_param.of(p));
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
        return (_CT)forSelectedIn( (_type) _type.of(clazz), selectActionFn);
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
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Consumer<Select> selectActionFn) {
        return Walk.in(_j, _param.class, p->{
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
        return (_CT)forSelectedIn((_type) _type.of(clazz), selectConstraint, selectActionFn);
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
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        return Walk.in(_j, _param.class, p->{
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
    public _param firstIn(Class clazz ){
        return firstIn( (_type) _type.of(clazz));
    }
    
    /**
     * Returns the first _field that matches the pattern and constraint
     * @param _j the _java node
     * @return  the first _field that matches (or null if none found)
     */
    @Override
    public _param firstIn(_java._domain _j){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel()){
                return firstIn( ((_codeUnit) _j).astCompilationUnit() );
            }
            return firstIn( ((_type) _j).ast() );
        }
        return firstIn( ((_tree._node) _j).ast() );
    }
    
    /**
     * Returns the first _field that matches the pattern and constraint
     * @param astStartNode the AST Node
     * @param _parameterMatchFn
     * @return  the first _field that matches (or null if none found)
     */
    @Override
    public _param firstIn(Node astStartNode, Predicate<_param> _parameterMatchFn){
        Optional<Parameter> p = astStartNode.findFirst(Parameter.class, s ->{
            Select sel = select(s);
            return sel != null && _parameterMatchFn.test( sel._param );
          });         
        if( p.isPresent()){
            return _param.of(p.get());
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
        return selectFirstIn((_type) _type.of(clazz), selectConstraint);
    }
    
    /**
     * Returns the first _field that matches the pattern and constraint
     * @param _j the _java node
     * @param selectConstraint
     * @return  the first _field that matches (or null if none found)
     */
    public Select selectFirstIn(_java._domain _j, Predicate<Select>selectConstraint ){

        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel()){
                return selectFirstIn( ((_codeUnit) _j).astCompilationUnit(), selectConstraint );
            }
            return selectFirstIn( ((_type)_j).ast(), selectConstraint);
        }
        return selectFirstIn( ((_tree._node)_j).ast(), selectConstraint);
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
        public $parameter $hardcode(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.$hardcode(translator, kvs));
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
            if( !this.constraint.test(_param.of(parameter) ) ){
                return null;
            }
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
        implements $pattern.selected, select_java<org.jdraft._param>, selectAst<Parameter> {
        
        public final org.jdraft._param _param;
        public final $tokens tokens;

        public Select (org.jdraft._param _p, Tokens tokens){
            this._param = _p;
            this.tokens = $tokens.of(tokens);
        }
        
        public Select (org.jdraft._param _p, $tokens $a){
            this._param = _p;
            tokens = $a;
        }
        
        public Select ( Parameter astParam, $tokens tokens){
            this( org.jdraft._param.of(astParam), tokens);
        }
        
        public boolean hasAnno( Class<? extends Annotation> annotationClass){            
            return _param.hasAnnoExpr(annotationClass);
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
        public org.jdraft._param _node() {
            return _param;
        }        
    }    
}
