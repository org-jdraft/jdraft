package org.jdraft.proto;

import org.jdraft._code;
import org.jdraft._typeRef;
import org.jdraft._java;
import org.jdraft._type;
import org.jdraft._walk;
import org.jdraft.Ast;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.Type;
import org.jdraft.*;
import org.jdraft._node;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Template for a Java Type {@link Type} Reference
 */
public final class $typeRef
    implements Template<_typeRef>, $proto<_typeRef>, $method.$part, $field.$part, 
        $parameter.$part, $typeParameter.$part, $var.$part {
    
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $typeRef of(String pattern ){
        return new $typeRef(Ast.typeRef(pattern));
    }
 
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $typeRef of(String pattern, Predicate<_typeRef> constraint){
        return new $typeRef(Ast.typeRef(pattern)).addConstraint(constraint);
    }
    
    /**
     * 
     * @param typeClass
     * @return 
     */
    public static $typeRef of( Class typeClass ){
        return $typeRef.of( Ast.typeRef(typeClass) );
    }

    /**
     * 
     * @param astType
     * @return 
     */
    public static $typeRef of(Type astType ){
        return new $typeRef( astType );
    }

    /**
     * 
     * @param astType
     * @param constraint
     * @return 
     */
    public static $typeRef of(Type astType, Predicate<_typeRef> constraint){
        return new $typeRef( astType ).addConstraint(constraint);
    }
    
    /**
     * 
     * @param _proto
     * @return 
     */
    public static $typeRef of( _typeRef _proto){
        return new $typeRef(_proto.ast());
    }
    
    /**
     * 
     * @param _proto
     * @param constraint
     * @return 
     */
    public static $typeRef of( _typeRef _proto, Predicate<_typeRef> constraint){
        return new $typeRef(_proto.ast()).addConstraint(constraint);
    }
    
    /** Matching constraint */
    public Predicate<_typeRef> constraint = (t)-> true;
    
    /** type code Pattern
    public Stencil typePattern;
     */

    /** This MIGHT be an explicit type OR it might be a pattern */
    public Type type;

    /**
     * 
     * @param astProtoType 
     */
    public $typeRef( Type astProtoType ){
        this.type = astProtoType.clone();
        //this.typePattern = Stencil.of(_typeRef.of(astProtoType).toString() );
    }

    /**
     * 
     * @param _proto the proto to build the $typeRef prototype from 
     */
    public $typeRef(_typeRef _proto)
    {
        this.type = _proto.ast().clone();
        //this.typePattern = Stencil.of(_proto.toString() );
    }
    
    /**
     * 
     * @param pattern 
     */
    private $typeRef(String pattern){
        this.type = Ast.typeRef(pattern);
        //this.typePattern = Stencil.of(pattern);
    } 

    private Stencil typePattern(){
        return Stencil.of(this.type.toString());
    }

    /**
     * Will this typeDecl match ALL typeDecl's 
     * @return 
     */
    public boolean isMatchAny(){

        if( this.typePattern().isMatchAny() ){
            try{ //test that the predicate is a match all, if we pass in null
                return this.constraint.test(null);
            }catch(Exception e){
                return false;
            }
        }
        return false;
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public $typeRef addConstraint( Predicate<_typeRef> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    @Override
    public $typeRef $(String target, String $name ) {
        Stencil st = typePattern();
        st = st.$(target, $name);
        //st.toString();
        this.type = Ast.typeRef(st.toString());
        return this;
        //String str = this.type.toString();

        //this.typePattern = this.typePattern.$(target, $name);
        //return this;
    }

    /**
     * 
     * @param astExpr
     * @param $name
     * @return 
     */
    public $typeRef $(Expression astExpr, String $name){
        return $( astExpr.toString(), $name);
        //this.typePattern = this.typePattern.$(astExpr.toString(), $name);
        //return this;
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param kvs the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    public $typeRef hardcode$( Tokens kvs ) {
        return hardcode$( Translator.DEFAULT_TRANSLATOR, kvs );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param keyValues the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    public $typeRef hardcode$( Object... keyValues ) {
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
    public $typeRef hardcode$( Translator translator, Object... keyValues ) {
        return hardcode$( translator, Tokens.of( keyValues ) );
    }

    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $typeRef hardcode$( Translator translator, Tokens kvs ) {
        Stencil st = typePattern();
        st = st.hardcode$(translator, kvs);
        this.type = Ast.typeRef(st.toString());
        //this.typePattern = this.typePattern.hardcode$(translator,kvs);
        return this;
    }
    
    /**
     * 
     * @param _t
     * @param allTokens
     * @return 
     */
    public Tokens decomposeTo( _typeRef _t, Tokens allTokens ){
        if(allTokens == null){
            return allTokens;
        }
        Select sel = select(_t);
        if( sel != null ){
            if( allTokens.isConsistent(sel.args.asTokens()) ){
                allTokens.putAll(sel.args.asTokens());
                return allTokens;
            }
        }
        return null;
    }
    
    public static $typeRef any(){
        return of();
    }
    
    /**
     * 
     * @return 
     */
    public static $typeRef of() {
        return new $typeRef("$type$");
    }
    
    /**
     * A TypeRef 
     * @param constraint
     * @return 
     */
    public static $typeRef of( Predicate<_typeRef> constraint ){
        return of().addConstraint(constraint);
    }
    
    /**
     * 
     * @param _n
     * @return 
     */
    public _typeRef compose(_node _n ){
        return compose(_n.decompose());
    }
    
    @Override
    public _typeRef compose(Translator t, Map<String,Object> tokens ){
        return _typeRef.of(typePattern().compose( t, tokens ));
    }

    public boolean match( Node n){
        if( n instanceof Type ){
            return matches( (Type) n);
        }
        return false;
    }

    /**
     * 
     * @param type
     * @return 
     */
    public boolean matches( String type ){
        return select( Ast.typeRef(type)) != null;
    }

    /**
     * 
     * @param _tr
     * @return 
     */
    public boolean matches( _typeRef _tr ){
        return select(_tr) != null;
    }
    
    /**
     * 
     * @param astType
     * @return 
     */
    public boolean matches( Type astType ){
        return select(astType) != null;
    }

    @Override
    public List<String> list$(){
        return this.typePattern().list$();
    }

    @Override
    public List<String> list$Normalized(){
        return this.typePattern().list$Normalized();
    }
    
    /**
     * 
     * @param _tr
     * @return 
     */
    public Select select( _typeRef _tr){

        if( this.constraint.test(_tr ) ) {
            if( Ast.typesEqual(_tr.ast(), this.type)){
                return new Select( _tr, Tokens.of());
            }
            Tokens ts = typePattern().decompose(_tr.toString() );
            if( ts != null ){
                return new Select( _tr, ts); //$args.of(ts);
            }
        }        
        return null;
    }
    
    /**
     * 
     * @param astType
     * @return 
     */
    public Select select( Type astType){
        return select(_typeRef.of(astType));       
    }
    
    /**
     * 
     * @param type
     * @return 
     */
    public Select select( String type ){
        return select(_typeRef.of(type));
    }
   
    /**
     * Returns the first Type that matches the pattern and constraint
     * @param astStartNode the node to look through
     * @param _typeRefActionFn
     * @return  the first Type that matches (or null if none found)
     */
    @Override
    public _typeRef firstIn(Node astStartNode, Predicate<_typeRef> _typeRefActionFn){
        Optional<Type> f = astStartNode.findFirst(Type.class, s ->{
            Select sel = select(s);
            return sel != null && _typeRefActionFn.test(sel.type);
            });         
        if( f.isPresent()){
            return _typeRef.of(f.get());
        }
        return null;
    }

    /**
     * Returns the first Type that matches the pattern and constraint
     * @param astNode the node to look through
     * @return  the first Type that matches (or null if none found)
     */
    @Override
    public Select selectFirstIn( Node astNode ){
        Optional<Type> f = astNode.findFirst(Type.class, s -> this.matches(s) );         
        if( f.isPresent()){
            return select(f.get());
        }
        return null;
    }

    @Override
    public List<Select> listSelectedIn(Node astNode ){
        List<Select>sts = new ArrayList<>();
        astNode.walk( Type.class, e-> {
            Select s = select( e );
            if( s != null ){
                sts.add( s);
            }
        });
        return sts;
    }
    
    /**
     * 
     * @param astNode
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(Node astNode, Predicate<Select>selectConstraint){
        List<Select>sts = new ArrayList<>();
        astNode.walk( Type.class, e-> {
            Select s = select( e );
            if( s != null && selectConstraint.test(s)){
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
     * @param _n
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(_java _n,Predicate<Select> selectConstraint){
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
     * @param selectConsumer
     * @return 
     */
    public _type forSelectedIn( Class clazz, Consumer<Select> selectConsumer ){
        return forSelectedIn(_java.type(clazz), selectConsumer);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param selectConsumer
     * @return 
     */
    public <N extends _java> N forSelectedIn(N _n, Consumer<Select> selectConsumer ){
        _walk.in(_n, Type.class, e-> {
            Select sel = select( e );
            if( sel != null ){
                selectConsumer.accept( sel );
            }
        });
        return _n;
    }

    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public _type forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer ){
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
        _walk.in(_n, Type.class, e-> {
            Select sel = select( e );
            if( sel != null && selectConstraint.test(sel) ){
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
        astNode.walk(Type.class, e-> {
            Select sel = select( e );
            if( sel != null ){
                selectConsumer.accept( sel );
            }
        });
        return astNode;
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
        astNode.walk(Type.class, e-> {
            Select sel = select( e );
            if( sel != null && selectConstraint.test(sel)){
                selectConsumer.accept( sel );
            }
        });
        return astNode;
    }
    
    /**
     * 
     * @param clazz
     * @param replacementType
     * @return 
     */
    public _type replaceIn(Class clazz, Class replacementType){
        return replaceIn(_java.type(clazz), replacementType);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param replacementType
     * @return 
     */
    public <N extends _java> N replaceIn(N _n, Class replacementType){
        return replaceIn(_n, $typeRef.of(replacementType));
    }
    
    /**
     * 
     * @param clazz
     * @param replacementType
     * @return 
     */
    public _type replaceIn( Class clazz, _typeRef replacementType ){
        return replaceIn(_java.type(clazz), replacementType);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param replacementType
     * @return 
     */
    public <N extends _java> N replaceIn(N _n, _typeRef replacementType){
        return replaceIn(_n, $typeRef.of(replacementType));
    }

    /**
     * Find and replaceIn
     * @param _n
     * @param astReplacementType
     * @param <N>
     * @return
     */
    public <N extends _java> N replaceIn(N _n, Type astReplacementType){
        return replaceIn(_n, $typeRef.of(astReplacementType));
    }

    /**
     * Find and replaceIn the TYPE
     *
     * <PRE>
     * for example:
     * _class _c = _class.of(MyType.class);
     * //replaceIn all instances of TreeSet with HashSet within the code
     * _c = $typeRef.of("TreeSet<$key$>").replaceIn(_c, $typeRef.of("HashSet<$key$>"));
     * </PRE>
     *
     * @param _n
     * @param $replacementType the template for the replacement TYPE
     * @param <N>
     * @return
     */
    public <N extends _java> N replaceIn(N _n, $typeRef $replacementType){
        _walk.in(_n, Type.class, e -> {
            Select select = select(e);
            if( select != null ){
                if( !e.replace($replacementType.compose(select.args).ast() )){
                    throw new _draftException("unable to replaceIn "+ e + " in "+ _n+" with "+$replacementType);
                }
            }
        });
        return _n;
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param replacementPattern
     * @return 
     */
    public <N extends _java> N replaceIn(N _n, String...replacementPattern){
        return replaceIn(_n, $typeRef.of( Text.combine(replacementPattern)) );
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_typeRef>typeRefMatchFn, Consumer<_typeRef> typeRefActionFn){
        astNode.walk(Type.class, t-> {
            Select select = select(t);
            if( select != null && typeRefMatchFn.test(select.type)){
                typeRefActionFn.accept( select.type );
            }
        });
        return astNode;
    }

    @Override
    public String toString() {
        return "(_typeRef) : \"" + this.typePattern() + "\"";
    }

    /**
     * 
     */
    public static class Select implements $proto.selected, 
            $proto.selected_model<_typeRef> {
        
        /** The underlying selected _typeRef */
        public _typeRef type;
        
        /** the arguments selected*/
        public $args args;

        public Select(_typeRef _tr, Tokens tokens ){
            this.type = _tr;
            this.args = $args.of(tokens);
        }
        
        public Select( Type type, $args tokens){
            this.type = _typeRef.of(type);
            this.args = tokens;
        }
        
        @Override
        public $args args(){
            return args;
        } 
        
        @Override
        public String toString(){
            return "$typeRef.Select {"+ System.lineSeparator()+
                Text.indent(type.toString() )+ System.lineSeparator()+
                Text.indent("$args : " + args) + System.lineSeparator()+
                "}";
        }

        public Type ast() {
            return type.ast();
        }

        @Override
        public _typeRef _node() {
            return type;
        }
        
        public boolean is(String typeRef){
            return this.type.is(typeRef);
        }
        
        public boolean is(Class typeClass){
            return this.type.is(typeClass);
        }
        
        public boolean is(Type astType){
            return this.type.is(astType);
        }
        
        public boolean isArray(){
            return this.type.isArray();
        }
        
        /**
         * Unknown Type is used in lambda like:
         * <PRE>
         * (o)-> System.out.println(o);
         * 
         * ...where o is a parameter with unspecified type
         * </PRE>
         * @return 
         */
        public boolean isUnknownType(){
            return this.type.isUnknownType();
        }
        
        public boolean isPrimitive(){            
            return this.type.isPrimitive();
        }
    }
}
