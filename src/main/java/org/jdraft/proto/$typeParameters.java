package org.jdraft.proto;

import org.jdraft._typeParameter;
import org.jdraft._code;
import org.jdraft._java;
import org.jdraft._type;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import org.jdraft.*;
import org.jdraft._node;
import org.jdraft._typeParameter._typeParameters;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * prototype for an _import declaration on a Java top level _type 
 *
 */
public final class $typeParameters
    implements Template<_typeParameters>, $proto<_typeParameters>, $method.$part, $constructor.$part {
    
    /**
     * 
     * @return 
     */
    public static $typeParameters any(){
        return of();
    }
    
    /**
     * 
     * @return 
     */
    public static $typeParameters none(){
        return of().addConstraint( tps-> tps.isEmpty());
    }
    
    /**
     * Match ANY import
     * @return 
     */
    public static $typeParameters of(){
        return new $typeParameters( t-> true );        
    }
    
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $typeParameters of( String... pattern){
        return new $typeParameters( _typeParameters.of(pattern)  );
    }

    /**
     * 
     * @param constraint
     * @return 
     */
    public static $typeParameters of( Predicate<_typeParameters> constraint ){
        return new $typeParameters( constraint );
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $typeParameters of( String pattern, Predicate<_typeParameters> constraint){        
        return new $typeParameters( _typeParameters.of(pattern) ).addConstraint(constraint);
    }
    
    /**
     * 
     * @param _proto
     * @return 
     */
    public static $typeParameters of( _typeParameters _proto){
        return new $typeParameters( _proto  );
    }

    /**
     * 
     * @param _proto
     * @param constraint
     * @return 
     */
    public static $typeParameters of( _typeParameters _proto, Predicate<_typeParameters> constraint){
        return new $typeParameters( _proto ).addConstraint(constraint);
    }
    
    public Predicate<_typeParameters> constraint = t-> true;
        
    public List<$typeParameter> typeParams = new ArrayList<>();
    
    private $typeParameters(_typeParameters proto ){
        proto.forEach(t-> typeParams.add(new $typeParameter(t )));
    }

    private $typeParameters( Predicate<_typeParameters> constraint ){        
        this.constraint = constraint;
    }
    
    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified prototype
     */
    public $typeParameters addConstraint( Predicate<_typeParameters>constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    /**
     * 
     * @param $tp
     * @return 
     */
    public $typeParameters $add( $typeParameter $tp ){
        this.typeParams.add($tp);
        return this;
    }
    
    /**
     * 
     * @param typeParams
     * @return 
     */
    public boolean matches( String... typeParams ){
        return matches(_typeParameters.of(typeParams) );
    }
    
    /**
     * 
     * @param astCallable
     * @return 
     */
    public boolean matches( CallableDeclaration astCallable ){
        return select(astCallable) != null;
    }
    
    /**
     * 
     * @param _t
     * @return 
     */
    public boolean matches( _typeParameters _t){
        return select( _t ) != null;
    }

    /**
     * 
     * @param astCallable
     * @return 
     */
    public Select select( CallableDeclaration astCallable ){
        return select( _typeParameters.of( astCallable ) );
    }
    
    /**
     * 
     * @param _i
     * @return 
     */
    public Select select(_typeParameters _i){
        if( this.constraint.test(_i)){            
                
            List<_typeParameter> listed = _i.list();
            if( listed.size() < this.typeParams.size() ){
                return null;
            }
            Tokens ts = new Tokens();
            for(int i=0;i<typeParams.size();i++ ){
                $typeParameter $tp = typeParams.get(i);
                
                Optional<_typeParameter> ort = 
                    listed.stream().filter(rt -> $tp.matches(rt) ).findFirst();
                if( !ort.isPresent() ){
                    return null;
                }
                $typeParameter.Select s = $tp.select( ort.get() );
                if( s == null || !ts.isConsistent(s.$args.asTokens()) ){
                    return null;
                }
                ts.putAll(s.args().asTokens());
                listed.remove( ort.get() );                
            }
            return new Select(_i, $args.of(ts) );            
        }            
        return null;
            
    }
 
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<this.typeParams.size(); i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append(this.typeParams.get(i));
        }
        this.typeParams.forEach(tp ->sb.append(tp.toString() ));
        return "($typeParameters) :  <"+sb.toString()+ ">";        
    }

    @Override
    public _typeParameters construct(Translator translator, Map<String, Object> keyValues) {        
        
        _typeParameters _ts = _typeParameters.of();
        if( keyValues.get("$typeParameters") != null ){ //PARAMETER OVERRIDE
            Object tps = keyValues.get("$typeParameters");
            Map<String,Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$typeParameters");
            
            if( tps instanceof $typeParameters ){
                return (($typeParameters)tps).construct(translator, kvs);
            } 
            if( tps instanceof _typeParameters){
                return ($typeParameters.of((_typeParameters)tps)).construct(translator, kvs);
            }
            return $typeParameters.of(tps.toString()).construct(translator, kvs);
        }         
        this.typeParams.forEach( tp -> _ts.add( tp.construct(translator, keyValues) ) );        
        return _ts;         
    }
    
    /**
     * 
     * @param _ts
     * @param allTokens
     * @return 
     */
    public Tokens decomposeTo( _typeParameters _ts, Tokens allTokens ){
        if(allTokens == null){
            return allTokens;
        }
        Select sel = select(_ts);
        if( sel != null ){
            if( allTokens.isConsistent(sel.args.asTokens()) ){
                allTokens.putAll(sel.args.asTokens());
                return allTokens;
            }
        }
        return null;
    }
     
    @Override
    public $typeParameters $(String target, String $Name) {
        this.typeParams.forEach(t -> t.$(target, $Name) );        
        return this;
    }

    /**
     * Hardcode (one or more) parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param hardcodedKeyValues the key parameter NAME and VALUE to hardcode
     * @return the modified Stencil
     */
    public $typeParameters hardcode$( Tokens hardcodedKeyValues ) {
        this.typeParams.forEach(t -> t.hardcode$(hardcodedKeyValues) );
        return this;
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param keyValues the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    public $typeParameters hardcode$( Object... keyValues ) {
        return hardcode$( Translator.DEFAULT_TRANSLATOR, Tokens.of( keyValues ) );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param translator translates values to be hardcoded into the Stencil
     * @param keyValues the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    public $typeParameters hardcode$( Translator translator, Object... keyValues ) {
        return hardcode$( translator, Tokens.of( keyValues ) );
    }

    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $typeParameters hardcode$( Translator translator, Tokens kvs ) {
        this.typeParams.forEach(t -> t.hardcode$(translator, kvs) );
        return this;
    }

    @Override
    public List<String> list$() {
        List<String> $names = new ArrayList<>();
        this.typeParams.forEach(t -> $names.addAll( t.list$() ) );
        return $names;
    }

    @Override
    public List<String> list$Normalized() {
        List<String> $namesNormalized = new ArrayList<>();
        this.typeParams.forEach(t -> $namesNormalized.addAll( t.list$Normalized() ) );
        return $namesNormalized.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Returns the first _import that matches the pattern and constraint
     * @param astNode the node to look through
     * @param typeParamsMatchFn
     * @return  the first _import that matches (or null if none found)
     */
    @Override
    public _typeParameters firstIn( Node astNode, Predicate<_typeParameters> typeParamsMatchFn){
        if( astNode.findCompilationUnit().isPresent() ){
            Optional<CallableDeclaration> f = astNode.findCompilationUnit().get()
                .findFirst(CallableDeclaration.class, s ->{
                    Select sel = select(s);
                    return sel != null && typeParamsMatchFn.test(sel.typeParameters);                    
                });         
            if( f.isPresent()){
                return _typeParameters.of(f.get());
            }
        }
        return null;
    }

    /**
     * Returns the first _import that matches the pattern and constraint
     * @param astNode the node to look through
     * @return  the first _import that matches (or null if none found)
     */
    @Override
    public Select selectFirstIn( Node astNode ){
        if( astNode.findCompilationUnit().isPresent() ){
            Optional<CallableDeclaration> f = astNode.findCompilationUnit().get()
                    .findFirst(CallableDeclaration.class, s -> this.matches(s) );         
            if( f.isPresent()){
                return select(f.get());
            }         
        }
        return null;
    }
    
    /**
     * Returns the first _import that matches the pattern and constraint
     * @param _n the _java node
     * @param selectConstraint
     * @return  the first _import that matches (or null if none found)
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
        if( _n.ast().findCompilationUnit().isPresent() ){
            Optional<CallableDeclaration> f = _n.ast().findCompilationUnit().get()
                    .findFirst(CallableDeclaration.class, s -> {
                        Select sel = this.select(s); 
                        return sel != null && selectConstraint.test(sel);
                    });                
            if( f.isPresent()){
                return select(f.get());
            }
        }
        return null;
        */
    }

    /**
     * Returns the first _import that matches the pattern and constraint
     * @param astNode the node to look through
     * @param selectConstraint
     * @return  the first _import that matches (or null if none found)
     */
    public Select selectFirstIn( Node astNode, Predicate<Select> selectConstraint ){
        if( astNode.findCompilationUnit().isPresent() ){
            Optional<CallableDeclaration> f = astNode.findCompilationUnit().get()
                    .findFirst(CallableDeclaration.class, s -> {
                        Select sel = this.select(s); 
                        return sel != null && selectConstraint.test(sel);
                    });         
            if( f.isPresent()){
                return select(f.get());
            }         
        }
        return null;
    }
    
    @Override
    public List<Select> listSelectedIn( Node astNode ){
        List<Select>sts = new ArrayList<>();
        if(astNode.findCompilationUnit().isPresent() ){
            astNode.findCompilationUnit().get().walk(CallableDeclaration.class, e-> {
                Select s = select( e );
                if( s != null ){
                    sts.add( s);
                }
            });
        }
        return sts;
    }

    /**
     * 
     * @param clazz
     * @return 
     */
    @Override
    public List<Select> listSelectedIn(Class clazz){
        return listSelectedIn(_java.type(clazz));
    }
    
    @Override
    public List<Select> listSelectedIn( _java _j){
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
    public List<Select> listSelectedIn( Class clazz, Predicate<Select> selectConstraint ){
        return listSelectedIn(_java.type(clazz), selectConstraint);
    }
    
    /**
     * 
     * @param astNode
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn( Node astNode, Predicate<Select> selectConstraint ){
        List<Select>sts = new ArrayList<>();
        if(astNode.findCompilationUnit().isPresent() ){
            astNode.findCompilationUnit().get().walk(CallableDeclaration.class, e-> {
                Select s = select( e );
                if( s != null && selectConstraint.test(s)){
                    sts.add( s);
                }
            });
        }
        return sts;
    }

    /**
     * 
     * @param _n
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn( _java _n, Predicate<Select> selectConstraint ){
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
     * @param importDecl
     * @return 
     */
    public _type replaceIn(Class clazz, String importDecl){
        return replaceIn( _java.type(clazz), importDecl);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param importDecl
     * @return 
     */
    public <N extends _java> N replaceIn(N _n, String importDecl){
        return replaceIn(_n, $typeParameters.of(importDecl));
    }
    
    /**
     * 
     * @param clazz
     * @param _i
     * @return 
     */
    public _type replaceIn(Class clazz, _typeParameters _i){
        return replaceIn( _java.type(clazz), _i);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param _i
     * @return 
     */
    public <N extends _java> N replaceIn(N _n, _typeParameters _i){
        return replaceIn(_n, $typeParameters.of(_i));
        /*
        Node astNode = _n.ast();
        if( astNode.findCompilationUnit().isPresent() ){
            astNode.findCompilationUnit().get().walk( CallableDeclaration.class, e-> {
                Select sel = select( e );
                if( sel != null ){
                    _typeParameters _ths = $typeParameters.of(_i).construct(sel.args.asTokens());
                    sel.typeParameters.astHolder().setTypeParameters(_ths.ast());                    
                }
            });
        }
        return _n;
        */
    }
    
    /**
     * 
     * @param clazz
     * @param $i
     * @return 
     */
    public _type replaceIn(Class clazz, $typeParameters $i ){
        return replaceIn(_java.type(clazz), $i);
    }
    
    /**
     * Replace all occurrences of the template in the code with the replacement
     * (composing the replacement from the constructed tokens in the source)
     *
     * @param _n the model to find replacements
     * @param $i the template to be constructed as the replacement
     * @param <N> the TYPE of model
     * @return
     */
    public <N extends _java> N replaceIn(N _n, $typeParameters $i ){
        if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                replaceIn( ((_code) _n).astCompilationUnit(), $i);
                return _n;
            }
            replaceIn( ((_type)_n).ast(), $i);
            return _n;
        }
        replaceIn( ((_node)_n).ast(), $i);        
        return _n;
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param $i
     * @return 
     */
    public <N extends Node> N replaceIn(N astNode, $typeParameters $i ){
        if( astNode.findCompilationUnit().isPresent() ){
            astNode.findCompilationUnit().get().walk(CallableDeclaration.class, e-> {
                Select sel = select( e );
                if( sel != null ){                    
                    _typeParameters _ths = $i.construct(sel.args.asTokens());
                    sel.typeParameters.astHolder().setTypeParameters(_ths.ast());                    
                }
            });
        }
        return astNode;
    }

    /**
     * 
     * @param clazz
     * @param selectConsumer
     * @return 
     */
    public _type forSelectedIn( Class clazz, Consumer<Select> selectConsumer){
        return forSelectedIn(_java.type(clazz), selectConsumer);
    }
    
    /**
     * 
     * @param <T>
     * @param _t
     * @param selectConsumer
     * @return 
     */
    public <T extends _type> T forSelectedIn( T _t, Consumer<Select> selectConsumer ){
        forSelectedIn(_t.astCompilationUnit(), selectConsumer);
        return _t;
    }

    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectConsumer
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Consumer<Select> selectConsumer ){
        if( astNode.findCompilationUnit().isPresent()){
            astNode.findCompilationUnit().get().walk(CallableDeclaration.class, e-> {
                Select sel = select( e );
                if( sel != null ){
                    selectConsumer.accept( sel );
                }
            });
        }
        return astNode;
    }

    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public _type forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer ){
       return forSelectedIn(_java.type(clazz), selectConstraint, selectConsumer); 
    }
    
    /**
     * 
     * @param <T>
     * @param _t
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public <T extends _type> T forSelectedIn(T _t, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer ){
        forSelectedIn(_t.astCompilationUnit(), selectConstraint, selectConsumer);
        return _t;
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
        if( astNode.findCompilationUnit().isPresent()){
            astNode.findCompilationUnit().get().walk(CallableDeclaration.class, e-> {
                Select sel = select( e );
                if( sel != null && selectConstraint.test(sel) ){
                    selectConsumer.accept( sel );
                }
            });
        }
        return astNode;
    }
    
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_typeParameters>_typeParamMatchFn, Consumer<_typeParameters> _typeParamActionFn){
        astNode.walk(CallableDeclaration.class, e-> {
            Select sel = select(e);
            if( sel != null && _typeParamMatchFn.test(sel.typeParameters)){
                _typeParamActionFn.accept(sel.typeParameters );
            }
        });
        return astNode;
    }
 
    /**
     * A Matched Selection result returned from matching a prototype $import
     * inside of some CompilationUnit
     */
    public static class Select implements $proto.selected, 
        $proto.selected_model<_typeParameters> {
    
        public final _typeParameters typeParameters;
        public final $args args;

        public Select(_typeParameters _i, $args tokens){
            this.typeParameters = _i;  
            this.args = tokens;
        }
        
        public Select( NodeWithTypeParameters astImport, $args tokens){
            this.typeParameters = _typeParameters.of(astImport );
            this.args = tokens;
        }
        
        @Override
        public $args args(){
            return args;
        }
        
        @Override
        public String toString(){
            return "$typeParameters.Select {"+ System.lineSeparator()+
                Text.indent(typeParameters.toString() )+ System.lineSeparator()+
                Text.indent("$args : " + args) + System.lineSeparator()+
                "}";
        }
        
        @Override
        public _typeParameters model() {
            return typeParameters;
        }
    }
}
