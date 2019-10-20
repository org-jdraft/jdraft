package org.jdraft.pattern;

import org.jdraft._code;
import org.jdraft._typeRef;
import org.jdraft._field;
import org.jdraft._java;
import org.jdraft._type;
import org.jdraft._walk;
import org.jdraft.Ex;
import org.jdraft.Ast;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.jdraft.*;
import org.jdraft._node;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * prototype for querying and composing a {@link VariableDeclarator}
 *
 * NOTE: this INCLUDES BOTH: 
 * <UL>
 *    <LI>FIELDS (member fields defined on a type)
 *    <LI>VARIABLES (variables created within a scope body)
 * </UL>
 * 
 * if you would like to operate ONLY on fields, you have the same features on
 * $field... (use this abstraction instead)
 * 
 * if you want to operate ONLY on (local body variables) you can use Select
 * and 
 */
public class $var
    implements Template<VariableDeclarator>, $pattern<VariableDeclarator, $var>, $body.$part {
    
    /** marker interface for components that are a part of a var */ 
    public interface $part{}

    /**
     * build a var prototype to match any var (field or local variable declaration)
     * @return the var prototype that matches any var
     */
    public static $var of(){
        return $var.of("$type$ $name$");
    }

    /**
     *
     * @param parts
     * @return
     */
    public static $var of( $part...parts){
        return new $var(parts);
    }

    /**
     * Selects the vars that are of
     * @param typeClass
     * @return
     */
    public static $var of( Class...typeClass){
        if( typeClass.length == 1){
            return ofType(typeClass[0]);
        }

        return of().$and(v-> Arrays.stream(typeClass).anyMatch(tc-> Ast.typesEqual(v.getType(), Ast.typeRef(tc))));
    }

    /**
     *
     * @param constraint
     * @return
     */
    public static $var of( Predicate<VariableDeclarator> constraint){
        return of().$and(constraint);
    }


    /**
     *
     * @param pattern
     * @return
     */
    public static $var of( String pattern ){
        return $var.of(new String[]{pattern});
    }

    /**
     *
     * @param pattern
     * @param constraint
     * @return
     */
    public static $var of( String pattern, Predicate<VariableDeclarator> constraint){
        return $var.of(new String[]{pattern}).$and(constraint);
    }

    /**
     *
     * @param pattern
     * @return
     */
    public static $var of(String...pattern){
        return new $var( Ast.varDecl(pattern ) );
    }

    /**
     *
     * @param proto
     * @param constraint
     * @return
     */
    public static $var of(VariableDeclarator proto, Predicate<VariableDeclarator> constraint){
        return new $var( proto  ).$and(constraint);
    }

    /**
     *
     * @param proto
     * @return
     */
    public static $var of(VariableDeclarator proto){
        return new $var( proto  );
    }

    /**
     * a Var Prototype only specifying the type
     * @param varType
     * @return
     */
    private static $var ofType( Class varType ){
        return of( $typeRef.of(varType));
    }

    /**
     * Returns a matcher to match against any one of the provided $pattern implementations
     * @param vars the potential vars
     * @return a new $var Or implementation (which extends $var)
     */
    public static $var.Or or( $var...vars ){
        return new Or(vars);
    }

    public static $var.Or or( VariableDeclarator... _protos ){
        $var[] arr = new $var[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $var.of( _protos[i]);
        }
        return or(arr);
    }

    public static $var as(String...pattern){
        return as( Ast.varDecl(pattern ) );
    }

    public static $var as(VariableDeclarator proto ) {
        $typeRef $t = $typeRef.as(proto.getType());
        $name $n = $name.as(proto.getNameAsString());
        $var $v = of($t, $n);

        if( proto.getInitializer().isPresent()){
            $v.init = $ex.of(proto.getInitializer().get());
        } else{
            $v.$and( v-> !v.getInitializer().isPresent());
        }
        return $v;
    }

    public static $var not($part...parts){
        return of().$not(parts);
    }

    /**
     * Only select local variables (NOT member fields)
     * @return
     */
    public static $var local(){
        return of().$local();
    }

    /**
     * Selects the vars that are of
     * @param typeClass
     * @return
     */
    public static $var local( Class...typeClass){
        if( typeClass.length == 1){
            return ofType(typeClass[0]).$local();
        }
        return of( v-> Arrays.stream(typeClass).anyMatch( tc-> Ast.typesEqual(v.getType(), Ast.typeRef(tc)))).$local();
    }

    /**
     *
     * @param pattern
     * @return
     */
    public static $var local( String pattern){
        return of(pattern).$local();
    }

    /**
     *
     * @param parts
     * @return
     */
    public static $var local( $part...parts){
        return of(parts).$local();
    }

    /**
     *
     * @param constraint
     * @return
     */
    public static $var local( Predicate<VariableDeclarator> constraint){
        return of(constraint).$local();
    }

    /**
     * Only select local variables (NOT member fields)
     * @return
     */
    public static $var member(){
        return of().$member();
    }

    /**
     * specify to select only member vars (i.e. fields) with the pattern
     * @param pattern
     * @return
     */
    public static $var member( String pattern){
        return of(pattern).$member();
    }

    /**
     * Selects the vars that are of
     * @param typeClass
     * @return
     */
    public static $var member( Class...typeClass){
        if( typeClass.length == 1){
            return ofType(typeClass[0]).$member();
        }
        return of( v-> Arrays.stream(typeClass).anyMatch( tc-> Ast.typesEqual(v.getType(), Ast.typeRef(tc)))).$member();
    }

    /**
     * specify to select only member vars (i.e. fields) based on the $parts
     * @param parts
     * @return
     */
    public static $var member( $part...parts){
        return of(parts).$member();
    }

    /**
     * specify to select only member vars (i.e. fields) bnased on this constraint
     * @param constraint
     * @return
     */
    public static $var member( Predicate<VariableDeclarator> constraint){
        return of(constraint).$member();
    }



    /** Matching constraint */
    public Predicate<VariableDeclarator> constraint = t -> true;
    
    /** */
    public $typeRef type = $typeRef.of("$type$");
    
    /** */
    public $name name = $name.of();
    
    /** */
    public $ex init = $ex.of("$init$");

    public static final PrettyPrinterConfiguration NO_COMMENTS = new PrettyPrinterConfiguration()
        .setPrintComments(false).setPrintJavadoc(false);
  
    private $var( $part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $typeRef ){
                this.type = ($typeRef)parts[i];
            }
            else if( parts[i] instanceof $name ){
                this.name = ($name)parts[i];
            }
            else{
                this.init = ($ex)parts[i];
            }
        }
    }

    public $var $not( $part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $name){
                final $name $fn = (($name)parts[i]);
                Predicate<VariableDeclarator> pf = f-> $fn.matches(f.getNameAsString());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $typeRef){
                final $typeRef $fj = (($typeRef)parts[i]);
                Predicate<VariableDeclarator> pf = f-> $fj.matches(f.getType());
                $and( pf.negate() );
            } else if( parts[i] instanceof $ex){
                final $ex $fj = (($ex)parts[i]);
                Predicate<VariableDeclarator> pf = f-> {
                    if(f.getInitializer().isPresent() ){
                        return $fj.match(f.getInitializer().get());
                    }else{
                        return $fj.match((Node)null);
                    }
                };
                $and( pf.negate() );
            }
        }
        return this;
    }

    private $var( VariableDeclarator astProtoVar ){
        this.name = $name.of(astProtoVar.getNameAsString());
        this.type = $typeRef.of(astProtoVar.getTypeAsString());
        if( astProtoVar.getInitializer().isPresent() ){
            this.init = $ex.of(astProtoVar.getInitializer().get());
            this.constraint = v -> v.getInitializer().isPresent();
        }           
    }
    
    public $var $and(Predicate<VariableDeclarator> constraint){
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    public $var $local(){
        return $and(v-> Ast.isParent(v, Ast.VAR_DECLARATION_EXPR));
    }

    /**
     * specify to select only member vars (i.e. fields)
     * @return
     */
    public $var $member(){
        return $and(v-> Ast.isParent(v, Ast.FIELD_DECLARATION));
    }

    public $var $name( String name ){
        this.name.nameStencil = Stencil.of(name);
        return this;
    }
    
    public $var $name( String name, Predicate<String> constraint){
        this.name.nameStencil = Stencil.of(name);
        this.name.and(constraint);
        return this;
    }
    
    public $var $name(Predicate<String> constraint){
        this.name.and(constraint);
        return this;
    }

    public $var $type(){
        this.type = $typeRef.of();
        return this;
    }
    
    public $var $type( Class clazz){
        this.type = $typeRef.of(clazz);
        return this;
    }
    
    public $var $type( $typeRef $t ){
        this.type = $t;
        return this;
    }
     
    public $var $type( String type ){
        this.type.type = Ast.typeRef(type);
        return this;
    }
    
    public $var $type( String type, Predicate<_typeRef> constraint){
        this.type = $typeRef.of(type).$and(constraint);
        return this;
    }
    
    public $var $type(Predicate<_typeRef> constraint){
        this.type.$and(constraint);
        return this;
    }
    
    public $var $init(){
        this.init = $ex.of();
        return this;
    }
    
    public <E extends Expression> $var $init( Predicate<E> constraint ){
        this.init.$and(constraint);
        return this;
    }
    
    /** What about NO init?? ...I can put that in the lambda
     * @param expr
     * @return 
     */    
    public $var $init( String...expr ){
        this.init = $ex.of(expr);
        return this;
    }
    
    /**
     * Select/match only variables that do not have an init
     * @return 
     */
    public $var $noInit(){
        return $and(v -> !v.getInitializer().isPresent());
    }



    public <E extends Expression> $var $init( E initExprProto){
        this.init = $ex.of(initExprProto);
        return this;
    }
    
    public <E extends Expression> $var $init( E initExprProto, Predicate<E> constraint){
        this.init = $ex.of(initExprProto).$and(constraint);
        return this;
    }

    public boolean match( Node n){
        if( n instanceof VariableDeclarator ){
            return matches( (VariableDeclarator) n);
        }
        return false;
    }

    /**
     * 
     * @param var
     * @return 
     */
    public boolean matches( String...var ){
        return matches(Ast.varDecl(var));
    }

    /**
     * 
     * @param astVar
     * @return 
     */
    public boolean matches( VariableDeclarator astVar ){
        return select(astVar ) != null;
    }

    @Override
    public boolean isMatchAny(){
        try{
            return this.constraint.test(null) && this.init.isMatchAny() && this.type.isMatchAny()
                    && this.name.isMatchAny();
        } catch(Exception e){
            return false;
        }
    }

    /**
     * 
     * @param astVar
     * @return 
     */
    public Select select(VariableDeclarator astVar){       
        Tokens all = new Tokens();
        if(this.constraint.test(astVar)) {            
            if( !this.init.isMatchAny() && !astVar.getInitializer().isPresent()){
                //we EXPECT some type of init, but it's null
                return null;
            }
            if( astVar.getInitializer().isPresent()){
                $ex.Select sel = this.init.select(astVar.getInitializer().get());
                if( sel == null ){
                    return null;
                }
                all = sel.tokens.asTokens();
            }
            all = this.name.parseTo(astVar.getNameAsString(), all);
            all = this.type.parseTo(_typeRef.of(astVar.getType()), all);
            
            if( all != null ){
                return new Select(astVar, $tokens.of(all));
            }            
        }
        return null;                
    }
    
    @Override
    public String toString() {
        if( this.init.isMatchAny()){
            return "$var{ (" + this.type + ") " + this.name+ "}";
        }
        return "$var{ (" + this.type + ") " + this.name+ " = "+this.init+" }";
        //return "($var) : \"" + this.type + " "+this.name + " = "+this.init+";\"";
    }

    @Override
    public VariableDeclarator draft(Translator translator, Map<String, Object> keyValues) {
        Tokens base = new Tokens();
        base.put("init", "");
        base.putAll(keyValues);
        
        String in = init.draft(translator, base).toString();
        if( in != null ){
            return Ast.varDecl(this.type.draft(translator, base)+ " "+ this.name.draft(translator, base)+" = "+in+";");
        }        
        return Ast.varDecl(this.type.draft(translator, base)+ " "+ this.name.draft(translator, base)+";");
    }
   
    @Override
    public VariableDeclarator draft(Map<String, Object> keyValues) {
        return draft( Translator.DEFAULT_TRANSLATOR, keyValues);
    }

    @Override
    public VariableDeclarator draft(Object... keyValues) {
        return draft( Translator.DEFAULT_TRANSLATOR, Tokens.of(keyValues));
    }

    @Override
    public VariableDeclarator draft(Translator translator, Object... keyValues) {
        return draft( translator, Tokens.of(keyValues));
    }

    @Override
    public VariableDeclarator fill(Object... values) {
        return fill( Translator.DEFAULT_TRANSLATOR, values );
    }

    @Override
    public $var $(String target, String $paramName) {
        this.name.$(target, $paramName);
        this.type.$(target, $paramName);
        this.init.$(target, $paramName);
        return this;
    }
    
    @Override
    public VariableDeclarator fill(Translator translator, Object... values) {
        List<String> vars = this.type.list$Normalized();
        vars.addAll(this.name.list$Normalized());
        vars =  vars.stream().distinct().collect(Collectors.toList());
        
        List<String> allVars = new ArrayList<>();
        allVars.addAll( vars );
        allVars.addAll( this.init.list$Normalized() );
        allVars = allVars.stream().distinct().collect(Collectors.toList());
        
        if( values.length == allVars.size() ){
            Map<String,Object> toCompose = new HashMap<>();
            for(int i=0;i<vars.size();i++){
                toCompose.put(allVars.get(i), values[i]);
            }
            return draft(translator, toCompose);
        }
        if( values.length == vars.size() ){ //no init
            Map<String,Object> toCompose = new HashMap<>();
            for(int i=0;i<vars.size();i++){
                toCompose.put(allVars.get(i), values[i]);
            }
            return Ast.varDecl( type.draft(translator, toCompose) + " "+ name.draft(translator, toCompose) );
        }
        throw new _draftException("Expected fill fields of size ("+allVars.size()+") or ("+vars.size()+") got ("+values.length+")");
    }

    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $var hardcode$( Translator translator, Tokens kvs ) {
        this.name = this.name.hardcode$(translator, kvs);
        this.type = this.type.hardcode$(translator, kvs);
        this.init = this.init.hardcode$(translator, kvs);        
        return this;
    }

    @Override
    public List<String> list$() {
        List<String> $names = this.type.list$();
        $names.addAll( this.name.list$());
        $names.addAll( this.init.list$());
        return $names;        
    }

    @Override
    public List<String> list$Normalized() {
        List<String> $names = this.type.list$Normalized();
        $names.addAll( this.name.list$Normalized());
        $names.addAll( this.init.list$Normalized());
        
        return $names.stream().distinct().collect(Collectors.toList());        
    }

    /**
     * Returns the first VaribleDeclarator that matches the pattern and constraint
     * @param astStartNode the node to look through
     * @param varMatchFn
     * @return  the first VaribleDeclarator that matches (or null if none found)
     */
    @Override
    public VariableDeclarator firstIn(Node astStartNode, Predicate<VariableDeclarator> varMatchFn){
        Optional<VariableDeclarator> f = astStartNode.findFirst(VariableDeclarator.class, s -> {
            Select sel = select(s);
            return sel != null && varMatchFn.test(sel.astVar);
            });         
        if( f.isPresent()){
            return f.get();
        }
        return null;
    }
    
    /**
     * 
     * @param clazz
     * @return 
     */
    @Override
    public Select selectFirstIn( Class clazz){
        return selectFirstIn( (_type)_java.type(clazz));
    }

    /**
     * Returns the first Select VaribleDeclarator that matches the pattern and constraint
     * @param astNode the node to look through
     * @return  the first Select VaribleDeclarator that matches (or null if none found)
     */
    @Override
    public Select selectFirstIn( Node astNode ){
        Optional<VariableDeclarator> f = astNode.findFirst(VariableDeclarator.class, s -> this.matches(s) );         
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
    public Select selectFirstIn( Class clazz, Predicate<Select> selectConstraint){
       return selectFirstIn( (_type)_java.type(clazz), selectConstraint);
    }
    
    /**
     * Returns the first VaribleDeclarator that matches the pattern and constraint
     * @param _j the _java node
     * @param selectConstraint
     * @return  the first VaribleDeclarator that matches (or null if none found)
     */
    public Select selectFirstIn(_model _j, Predicate<Select> selectConstraint ){
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                return selectFirstIn( ((_code) _j).astCompilationUnit(), selectConstraint);
            }
            return selectFirstIn( ((_type)_j).ast(), selectConstraint);
        }
        return selectFirstIn( ((_node)_j).ast(), selectConstraint);
    }

    /**
     * Returns the first Select VaribleDeclarator that matches the pattern and constraint
     * @param astNode the node to look through
     * @param selectConstraint
     * @return  the first Select VaribleDeclarator that matches (or null if none found)
     */
    public Select selectFirstIn( Node astNode, Predicate<Select> selectConstraint ){
        Optional<VariableDeclarator> f = astNode.findFirst(VariableDeclarator.class, s -> this.matches(s) );         
        if( f.isPresent()){
            Select sel = select(f.get());
            if( selectConstraint.test(sel)){
                return sel;
            }
        }
        return null;
    }

    @Override
    public List<Select> listSelectedIn(Class clazz){
        return listSelectedIn( (_type)_java.type(clazz));
    }
    
    @Override
    public List<Select> listSelectedIn(Node astNode ){
        List<Select>sts = new ArrayList<>();
        astNode.walk(VariableDeclarator.class, e-> {
            Select s = select( e );
            if( s != null ){
                sts.add( s);
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
        astNode.walk(VariableDeclarator.class, e-> {
            Select s = select( e );
            if( s != null && selectConstraint.test(s)){
                sts.add( s);
            }
        });
        return sts;
    }

    /**
     * 
     * @param _n
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(_model _n, Predicate<Select> selectConstraint){
        List<Select>sts = new ArrayList<>();
        if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                return listSelectedIn( ((_code) _n).astCompilationUnit(), selectConstraint);
            }
            return listSelectedIn( ((_type)_n).ast(), selectConstraint);
        }
        return listSelectedIn( ((_node)_n).ast(), selectConstraint);
    }

    /**
     * 
     * @param clazz
     * @param $replaceProto
     * @return 
     */
    public <_CT extends _type> _CT replaceIn(Class clazz, $var $replaceProto ){
        return (_CT)replaceIn( (_type)_java.type(clazz), $replaceProto);
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param $replaceProto
     * @return 
     */
    public <N extends Node> N replaceIn(N astNode, $var $replaceProto ){
        astNode.walk(VariableDeclarator.class, e-> {
            Select sel = select( e );
            if( sel != null ){
                sel.astVar.replace($replaceProto.draft(sel.tokens) );
            }
        });
        return astNode;
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param $replaceProto
     * @return 
     */
    public <_J extends _model> _J replaceIn(_J _j, $var $replaceProto ){
        _walk.in(_j, VariableDeclarator.class, e-> {
            Select sel = select( e );
            if( sel != null ){
                sel.astVar.replace($replaceProto.draft(sel.tokens) );
            }
        });
        return _j;
    }

    /**
     * 
     * @param clazz
     * @param selectConsumer
     * @return 
     */
    public < _CT extends _type> _CT forSelectedIn( Class clazz, Consumer<Select> selectConsumer){
        return (_CT)forSelectedIn( (_type)_java.type(clazz), selectConsumer);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConsumer
     * @return 
     */
    public <_J extends _model> _J forSelectedIn(_J _j, Consumer<Select> selectConsumer ){
        _walk.in(_j, VariableDeclarator.class, e-> {
            Select sel = select( e );
            if( sel != null ){
                selectConsumer.accept( sel );
            }
        });
        return _j;
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectConsumer
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Consumer<Select> selectConsumer ){
        astNode.walk(VariableDeclarator.class, e-> {
            Select sel = select( e );
            if( sel != null ){
                selectConsumer.accept( sel );
            }
        });
        return astNode;
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public <_CT extends _type> _CT forSelectedIn( Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer){
        return (_CT)forSelectedIn( (_type)_java.type(clazz), selectConstraint, selectConsumer);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public <_J extends _model> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer ){
        _walk.in(_j, VariableDeclarator.class, e-> {
            Select sel = select( e );
            if( sel != null && selectConstraint.test(sel)){
                selectConsumer.accept( sel );
            }
        });
        return _j;
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer ){
        astNode.walk(VariableDeclarator.class, e-> {
            Select sel = select( e );
            if( sel != null && selectConstraint.test(sel)){
                selectConsumer.accept( sel );
            }
        });
        return astNode;
    }
    
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<VariableDeclarator> varMatchFn, Consumer<VariableDeclarator> varActionFn){
        astNode.walk(VariableDeclarator.class, e-> {
            Select sel = select( e );
            if( sel != null && varMatchFn.test(sel.astVar)){
                varActionFn.accept( e );
            }
        });
        return astNode;
    }

    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are suppressed.
     */
    public static class Or extends $var {

        final List<$var>ors = new ArrayList<>();

        public Or($var...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $var hardcode$(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.hardcode$(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$var.Or{");
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
        public $var.Select select(VariableDeclarator n){
            $var $a = whichMatch(n);
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
         * @param tps
         * @return
         */
        public $var whichMatch(VariableDeclarator tps){
            Optional<$var> orsel  = this.ors.stream().filter( $p-> $p.matches(tps) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }/* Or */

    /**
     * A Matched Selection result returned from matching a prototype $var
     * inside of some Node or _node
     */
    public static class Select implements $pattern.selected,
            selectAst<VariableDeclarator> {
        
        public final VariableDeclarator astVar;
        public final $tokens tokens;

        public Select( VariableDeclarator v, $tokens tokens){
            this.astVar = v;
            this.tokens = tokens;
        }
        
        @Override
        public $tokens tokens(){
            return tokens;
        }
        
        @Override
        public String toString(){
            return "$var.Select{"+ System.lineSeparator()+
                    Text.indent(astVar.toString() )+ System.lineSeparator()+
                    Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                    "}";
        }

        /**
         * Is the variable selected a part of a field (member) variable 
         * (opposite of isLocalVar)
         * @return 
         */
        public boolean isFieldVar(){
            return astVar.getParentNode().isPresent() && astVar.getParentNode().get() instanceof FieldDeclaration;
        }
        
        /**
         * is this selected var part of a local variable (i.e. declared within a
         * body or lambda, not a "member" of a Class) as apposed to a Field
         * @return 
         */
        public boolean isLocalVar(){
            return !isFieldVar();
        }
        
        public boolean isNamed(String name){
            return Objects.equals( astVar.getNameAsString(), name);
        }
        
        public boolean is( String... varDeclaration ){
            try{
                return _field.of(astVar).is(varDeclaration);
            }catch(Exception e){
                return false; 
            }
        }
        
        public boolean hasInit(){
            return getInit() != null;
        }
        
        public Expression getInit(){
            if( astVar.getInitializer().isPresent() ){
                return astVar.getInitializer().get();
            }
            return null;
        }
        
        public boolean isInit( Predicate<Expression> initMatchFn){
            return astVar.getInitializer().isPresent() && initMatchFn.test( astVar.getInitializer().get());
        }
        
        public boolean isInit(String init){
            return Objects.equals( getInit(), Ex.of(init));
        }
        
        public boolean isInit(byte init){
            return Objects.equals( getInit(), Ex.of(init));
        }
        
        public boolean isInit(short init){
            return Objects.equals( getInit(), Ex.of(init));
        }
        
        public boolean isInit(long init){
            return Objects.equals( getInit(), Ex.of(init));
        }
        
        public boolean isInit(char init){
            return Objects.equals( getInit(), Ex.of(init));
        }
        
        public boolean isInit(double init){
            return Objects.equals( getInit(), Ex.of(init));
        }
        
        public boolean isInit(float init){
            return Objects.equals( getInit(), Ex.of(init));
        }
        
        public boolean isInit(boolean init){
            return Objects.equals( getInit(), Ex.of(init));
        }
        
        public boolean isInit(int init){
            return Objects.equals( getInit(), Ex.of(init));
        }
        
        public boolean isInit(Expression init){
            return Objects.equals( getInit(), init);
        }
        
        public boolean isType( Class expectedType){
            return _typeRef.of(astVar.getType()).is( expectedType );
        }
        
        public boolean isType( String expectedType){
            return _typeRef.of(astVar.getType()).is( expectedType );
        }
        
        public boolean isType( _typeRef expectedType){
            return _typeRef.of(astVar.getType()).equals( expectedType );
        }
        
        public boolean isType( Type expectedType){
            return _typeRef.of(astVar.getType()).is( expectedType );
        }
        
        @Override
        public VariableDeclarator ast() {
            return astVar;
        }
    }
}
