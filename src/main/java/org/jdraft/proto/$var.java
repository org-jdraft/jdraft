package org.jdraft.proto;

import org.jdraft._code;
import org.jdraft._typeRef;
import org.jdraft._field;
import org.jdraft._java;
import org.jdraft._type;
import org.jdraft._walk;
import org.jdraft.Expr;
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
 * Template for a {@link VariableDeclarator}
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
    implements Template<VariableDeclarator>, $proto<VariableDeclarator> {
    
    /** marker interface for components that are a part of a var */ 
    public interface $part{}   
    
    /**
     * 
     * @return 
     */
    public static $var any(){
        return of();
    }
    
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
     * 
     * @param constraint
     * @return 
     */
    public static $var of( Predicate<VariableDeclarator> constraint){
        return of().addConstraint(constraint);
    }
    
    /**
     * a Var Prototype only specifying the type
     * @param varType
     * @return 
     */
    public static $var ofType( Class varType ){
        final _typeRef e = _typeRef.of(varType );
        return of().addConstraint( v-> e.is(v.getType()) );
    }
    
    /**
     * a Var Prototype only specifying the type
     * @param varType
     * @return 

    public static $var ofType( Type varType ){
        final _typeRef e = _typeRef.of(varType );
        return of().addConstraint( v-> e.is(v) );
    }
    */

    /**
     * a Var Prototype only specifying the type
     * @param varType
     * @return 

    public static $var ofType( _typeRef varType ){        
        return of().addConstraint( v-> varType.is(v) );
    }    
    */

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
        return $var.of(new String[]{pattern}).addConstraint(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $var of(String...pattern){
        return new $var( Ast.variable(pattern ) );
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
     * 
     * @param proto
     * @param constraint
     * @return 
     */
    public static $var of(VariableDeclarator proto, Predicate<VariableDeclarator> constraint){
        return new $var( proto  ).addConstraint(constraint);
    }
    
    /** Matching constraint */
    public Predicate<VariableDeclarator> constraint = t -> true;
    
    /** */
    public $typeRef type = $typeRef.of("$type$");
    
    /** */
    public $id name = $id.of();
    
    /** */
    public $expr init = $expr.of("$init$");
    //public $component<Expression> init = new $component( "$init$", t->true);

    public static final PrettyPrinterConfiguration NO_COMMENTS = new PrettyPrinterConfiguration()
        .setPrintComments(false).setPrintJavadoc(false);
  
    private $var( $part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $typeRef ){
                this.type = ($typeRef)parts[i];
            }
            else if( parts[i] instanceof $id ){
                this.name = ($id)parts[i];
            }
            else{
                this.init = ($expr)parts[i];
            }
        }
    }
    
    private $var( VariableDeclarator astProtoVar ){
        this.name = $id.of(astProtoVar.getNameAsString());
        this.type = $typeRef.of(astProtoVar.getTypeAsString());
        if( astProtoVar.getInitializer().isPresent() ){
            this.init = $expr.of(astProtoVar.getInitializer().get());
            //this.init = $component.of(astProtoVar.getInitializer().get());
            this.constraint = v -> v.getInitializer().isPresent();
        }           
    }
    
    public $var addConstraint(Predicate<VariableDeclarator> constraint){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    public $var $name( String name ){
        this.name.pattern = Stencil.of(name);
        return this;
    }
    
    public $var $name( String name, Predicate<String> constraint){
        this.name.pattern = Stencil.of(name);
        this.name.addConstraint(constraint);
        return this;
    }
    
    public $var $name(Predicate<String> constraint){
        this.name.addConstraint(constraint);
        return this;
    }

    public $var $type(){
        this.type = $typeRef.any();
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
        this.type.typePattern = Stencil.of(name);
        return this;
    }
    
    public $var $type( String type, Predicate<_typeRef> constraint){
        this.type = $typeRef.of(type).addConstraint(constraint);
        return this;
    }
    
    public $var $type(Predicate<_typeRef> constraint){
        this.type.addConstraint(constraint);
        return this;
    }
    
    public $var $init(){
        this.init = $expr.any();
        return this;
    }
    
    public <E extends Expression> $var $init( Predicate<E> constraint ){
        this.init.addConstraint(constraint);
        return this;
    }
    
    /** What about NO init?? ...I can put that in the lambda
     * @param expr
     * @return 
     */    
    public $var $init( String...expr ){
        this.init = $expr.of(expr);
        return this;
    }
    
    /**
     * Select/match only variables that do not have an init
     * @return 
     */
    public $var noInit(){
        return addConstraint( v -> !v.getInitializer().isPresent());
    }
    
    public <E extends Expression> $var $init( E initExprProto){
        this.init = $expr.of(initExprProto);
        return this;
    }
    
    public <E extends Expression> $var $init( E initExprProto, Predicate<E> constraint){
        this.init = $expr.of(initExprProto).addConstraint(constraint);
        return this;
    }    
      
    /**
     * 
     * @param var
     * @return 
     */
    public boolean matches( String...var ){
        return matches(Ast.variable(var));
    }

    /**
     * 
     * @param astVar
     * @return 
     */
    public boolean matches( VariableDeclarator astVar ){
        return select(astVar ) != null;
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
                $expr.Select sel = this.init.select(astVar.getInitializer().get());
                if( sel == null ){
                    return null;
                }
                all = sel.args.asTokens();
                //all = this.init.decomposeTo(astVar.getInitializer().get(), all );
                //if( all == null ){
                //    return null;
                //}                             
            }
            all = this.name.decomposeTo(astVar.getNameAsString(), all);
            all = this.type.decomposeTo(_typeRef.of(astVar.getType()), all);
            
            if( all != null ){
                return new Select(astVar, $args.of(all));
            }            
        }
        return null;                
    }
    
    @Override
    public String toString() {
        if( this.init.isMatchAny()){
            return "($var) : \"" + this.type + " "+this.name+ ";\"";
        }
        return "($var) : \"" + this.type + " "+this.name+ " = "+this.init+";\"";
    }

    @Override
    public VariableDeclarator compose(Translator translator, Map<String, Object> keyValues) {
        Tokens base = new Tokens();
        base.put("init", "");
        base.putAll(keyValues);
        
        String in = init.compose(translator, base).toString();
        if( in != null ){
            return Ast.variable(this.type.compose(translator, base)+ " "+ this.name.compose(translator, base)+" = "+in+";");
        }        
        return Ast.variable(this.type.compose(translator, base)+ " "+ this.name.compose(translator, base)+";");
    }
   
    @Override
    public VariableDeclarator compose(Map<String, Object> keyValues) {
        return compose( Translator.DEFAULT_TRANSLATOR, keyValues);
    }

    @Override
    public VariableDeclarator compose(Object... keyValues) {
        return compose( Translator.DEFAULT_TRANSLATOR, Tokens.of(keyValues));
    }

    @Override
    public VariableDeclarator compose(Translator translator, Object... keyValues) {
        return compose( translator, Tokens.of(keyValues));
    }

    @Override
    public VariableDeclarator fill(Object... values) {
        return fill( Translator.DEFAULT_TRANSLATOR, values );
    }

    @Override
    public $var $(String target, String $Name) {
        this.name.$(target, $Name);
        this.type.$(target, $Name);
        this.init.$(target, $Name);
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
            return compose(translator, toCompose);
        }
        if( values.length == vars.size() ){ //no init
            Map<String,Object> toCompose = new HashMap<>();
            for(int i=0;i<vars.size();i++){
                toCompose.put(allVars.get(i), values[i]);
            }
            return Ast.variable( type.compose(translator, toCompose) + " "+ name.compose(translator, toCompose) );
        }
        throw new _draftException("Expected fill fields of size ("+allVars.size()+") or ("+vars.size()+") got ("+values.length+")");
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param kvs the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    public $var hardcode$( Tokens kvs ) {
        return hardcode$( Translator.DEFAULT_TRANSLATOR, kvs );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param keyValues the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    public $var hardcode$( Object... keyValues ) {
        return hardcode$( Translator.DEFAULT_TRANSLATOR, Tokens.of( keyValues ) );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param translator translates values to be hardcoded into the Stencil
     * @param keyValues the key parameter NAME and String VALUE to assign to the
     * @return the modified $field
     */
    public $var hardcode$( Translator translator, Object... keyValues ) {
        return hardcode$( translator, Tokens.of( keyValues ) );
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
     * @param astNode the node to look through
     * @param varMatchFn
     * @return  the first VaribleDeclarator that matches (or null if none found)
     */
    @Override
    public VariableDeclarator firstIn( Node astNode, Predicate<VariableDeclarator> varMatchFn){
        Optional<VariableDeclarator> f = astNode.findFirst(VariableDeclarator.class, s -> {
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
        return selectFirstIn(_java.type(clazz));
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
       return selectFirstIn(_java.type(clazz), selectConstraint); 
    }
    
    /**
     * Returns the first VaribleDeclarator that matches the pattern and constraint
     * @param _n the _java node
     * @param selectConstraint
     * @return  the first VaribleDeclarator that matches (or null if none found)
     */
    public Select selectFirstIn( _java _n, Predicate<Select> selectConstraint ){
        if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                return selectFirstIn( ((_code) _n).astCompilationUnit(), selectConstraint);
            }
            return selectFirstIn( ((_type)_n).ast(), selectConstraint);
        }
        return selectFirstIn( ((_node)_n).ast(), selectConstraint);        
        /*
        Optional<VariableDeclarator> f = _n.ast().findFirst(VariableDeclarator.class, s -> this.matches(s) );         
        if( f.isPresent()){
            Select sel = select(f.get());
            if( selectConstraint.test(sel)){
                return sel;
            }
        }
        return null;
        */
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
        return listSelectedIn(_java.type(clazz));
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
    public List<Select> listSelectedIn(_java _j){
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
        return listSelectedIn(_java.type(clazz), selectConstraint);
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
    public List<Select> listSelectedIn(_java _n, Predicate<Select> selectConstraint){
        List<Select>sts = new ArrayList<>();
        if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                return listSelectedIn( ((_code) _n).astCompilationUnit(), selectConstraint);
            }
            return listSelectedIn( ((_type)_n).ast(), selectConstraint);
        }
        return listSelectedIn( ((_node)_n).ast(), selectConstraint);        
        
        /*
        
        _walk.in(_n, VariableDeclarator.class, e -> {
            Select s = select( e );
            if( s != null && selectConstraint.test(s)){
                sts.add( s);
            }
        });
        return sts;
        */
    }

    /**
     * 
     * @param clazz
     * @param $replaceProto
     * @return 
     */
    public _type replaceIn(Class clazz, $var $replaceProto ){
        return replaceIn( _java.type(clazz), $replaceProto);    
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
                sel.astVar.replace($replaceProto.compose(sel.args) );
            }
        });
        return astNode;
    }

    /**
     * 
     * @param <N>
     * @param _le
     * @param $replaceProto
     * @return 
     */
    public <N extends _java> N replaceIn(N _le, $var $replaceProto ){
        _walk.in(_le, VariableDeclarator.class, e-> {
            Select sel = select( e );
            if( sel != null ){
                sel.astVar.replace($replaceProto.compose(sel.args) );
            }
        });
        return _le;
    }

    /**
     * 
     * @param clazz
     * @param selectConsumer
     * @return 
     */
    public _type forSelectedIn( Class clazz, Consumer<Select> selectConsumer){
        return forSelectedIn( _java.type(clazz), selectConsumer);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param selectConsumer
     * @return 
     */
    public <N extends _java> N forSelectedIn(N _n, Consumer<Select> selectConsumer ){
        _walk.in(_n, VariableDeclarator.class, e-> {
            Select sel = select( e );
            if( sel != null ){
                selectConsumer.accept( sel );
            }
        });
        return _n;
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
    public _type forSelectedIn( Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer){
        return forSelectedIn( _java.type(clazz), selectConstraint, selectConsumer);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public <N extends _java> N forSelectedIn(N _n, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer ){
        _walk.in(_n, VariableDeclarator.class, e-> {
            Select sel = select( e );
            if( sel != null && selectConstraint.test(sel)){
                selectConsumer.accept( sel );
            }
        });
        return _n;
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
     * A Matched Selection result returned from matching a prototype $var
     * inside of some Node or _node
     */
    public static class Select implements $proto.selected, 
            $proto.selectedAstNode<VariableDeclarator> {
        
        public final VariableDeclarator astVar;
        public final $args args;

        public Select( VariableDeclarator v, $args tokens){
            this.astVar = v;
            this.args = tokens;
        }
        
        @Override
        public $args args(){
            return args;
        }
        
        @Override
        public String toString(){
            return "$var.Select{"+ System.lineSeparator()+
                    Text.indent(astVar.toString() )+ System.lineSeparator()+
                    Text.indent("$args : " + args) + System.lineSeparator()+
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
            return Objects.equals( getInit(), Expr.of(init));
        }
        
        public boolean isInit(byte init){
            return Objects.equals( getInit(), Expr.of(init));
        }
        
        public boolean isInit(short init){
            return Objects.equals( getInit(), Expr.of(init));
        }
        
        public boolean isInit(long init){
            return Objects.equals( getInit(), Expr.of(init));
        }
        
        public boolean isInit(char init){
            return Objects.equals( getInit(), Expr.of(init));
        }
        
        public boolean isInit(double init){
            return Objects.equals( getInit(), Expr.of(init));
        }
        
        public boolean isInit(float init){
            return Objects.equals( getInit(), Expr.of(init));
        }
        
        public boolean isInit(boolean init){
            return Objects.equals( getInit(), Expr.of(init));
        }
        
        public boolean isInit(int init){
            return Objects.equals( getInit(), Expr.of(init));
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
