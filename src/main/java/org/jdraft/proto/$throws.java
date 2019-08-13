package org.jdraft.proto;

import org.jdraft._code;
import org.jdraft._java;
import org.jdraft._throws;
import org.jdraft._type;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithThrownExceptions;
import com.github.javaparser.ast.type.ReferenceType;
import org.jdraft.*;
import org.jdraft._node;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * prototype for an _import declaration on a Java top level _type 
 *
 */
public final class $throws
    implements Template<_throws>, $proto<_throws>, $method.$part, $constructor.$part {
    
    /**
     * Matches entities that CAN have throws but have none
     * @return 
     */
    public static $throws none(){
        return of().addConstraint( t-> t.isEmpty() );
    }
    
    /**
     * 
     * @return 
     */
    public static $throws any(){
        return of();
    }
    
    /**
     * Match ANY import
     * @return 
     */
    public static $throws of(){
        return new $throws( t-> true );        
    }
    
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $throws of( String... pattern){
        return new $throws( _throws.of(pattern)  );
    }

    /**
     * 
     * @param throwsClasses
     * @return 
     */
    public static $throws of( Class<? extends Throwable>...throwsClasses ){
        return new $throws( _throws.of(throwsClasses ) );
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $throws of( Predicate<_throws> constraint ){
        return new $throws( constraint );
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $throws of( String pattern, Predicate<_throws> constraint){        
        return new $throws( _throws.of(pattern) ).addConstraint(constraint);
    }
    
    /**
     * 
     * @param exception
     * @param constraint
     * @return 
     */
    public static $throws of( Class<? extends Throwable> exception, Predicate<_throws> constraint){
        return new $throws( exception  ).addConstraint(constraint);
    }
    
    /**
     * 
     * @param _proto
     * @return 
     */
    public static $throws of( _throws _proto){
        return new $throws( _proto  );
    }

    /**
     * 
     * @param _proto
     * @param constraint
     * @return 
     */
    public static $throws of( _throws _proto, Predicate<_throws> constraint){
        return new $throws( _proto ).addConstraint(constraint);
    }
    
    public Predicate<_throws> constraint = t-> true;
        
    public List<$id> throwsPatterns = new ArrayList<>();
    
    private <C extends Throwable> $throws( Class<C>...thrownExceptions ){
        this(_throws.of(thrownExceptions));
    } 
    
    private $throws(_throws proto ){
        proto.forEach( t-> throwsPatterns.add(new $id(t.toString())));
    }

    private $throws( Predicate<_throws> constraint ){        
        this.constraint = constraint;
    }
    
    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified prototype
     */
    public $throws addConstraint( Predicate<_throws>constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    /**
     * 
     * @param thrws
     * @return 
     */
    public boolean matches( String... thrws ){
        return matches(_throws.of(thrws) );
    }

    public boolean match( Node n){
        if( n instanceof NodeWithThrownExceptions ){
            return matches( (NodeWithThrownExceptions)n);
        }
        return false;
    }

    /**
     * 
     * @param nwt
     * @return 
     */
    public boolean matches( NodeWithThrownExceptions nwt ){
        return select( nwt ) != null;
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
    public boolean matches( _throws _t){
        return select( _t ) != null;
    }

    /**
     * 
     * @param astCallable
     * @return 
     */
    public Select select( NodeWithThrownExceptions astCallable ){
        return select( _throws.of( astCallable ) );
    }
    
    /**
     * 
     * @param astCallable
     * @return 
     */
    public Select select( CallableDeclaration astCallable ){
        return select( _throws.of( astCallable ) );
    }
    
    /**
     * 
     * @param _i
     * @return 
     */
    public Select select(_throws _i){
        if( this.constraint.test(_i)){            
            List<ReferenceType> listed = _i.list();
            if( listed.size() < this.throwsPatterns.size() ){
                return null;
            }
            Tokens ts = new Tokens();
            for(int i=0;i<throwsPatterns.size();i++ ){
                $id $tp = throwsPatterns.get(i);
                
                Optional<ReferenceType> ort = 
                    listed.stream().filter(rt -> $tp.matches(rt.asString()) ).findFirst();
                if( !ort.isPresent() ){
                    return null;
                }
                ts = $tp.decomposeTo( ort.get().asString(), ts );
                listed.remove( ort.get() );
                if( ts == null ){
                    return null;
                }        
            }
            if( ts != null ){
                return new Select(_i, $args.of(ts) );
            }
        }
        return null;
    }
 
    @Override
    public String toString() {
        return "($throws) : \"" +this.throwsPatterns + "\"";
    }

    @Override
    public _throws compose(Translator translator, Map<String, Object> keyValues) {
        _throws _ts = new _throws();
        if( keyValues.get("$throws$") != null ){ //PARAMETER OVERRIDE
            $throws $ths = $throws.of( keyValues.get("$throws$").toString() );
            Map<String,Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$throws$");
            return $ths.compose(translator, kvs);
        } 
        this.throwsPatterns.forEach( tp -> _ts.add( tp.compose(translator, keyValues) ) );        
        return _ts;
    }
    
    /**
     * 
     * @param _ts
     * @param allTokens
     * @return 
     */
    public Tokens decomposeTo( _throws _ts, Tokens allTokens ){
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
    public $throws $(String target, String $Name) {
        this.throwsPatterns.forEach(t -> t.$(target, $Name) );        
        return this;
    }

    /**
     * Hardcode (one or more) parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param hardcodedKeyValues the key parameter NAME and VALUE to hardcode
     * @return the modified Stencil
     */
    public $throws hardcode$( Tokens hardcodedKeyValues ) {
        this.throwsPatterns.forEach(t -> t.hardcode$(Translator.DEFAULT_TRANSLATOR, hardcodedKeyValues) );
        return this;
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param keyValues the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    public $throws hardcode$( Object... keyValues ) {
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
    public $throws hardcode$( Translator translator, Object... keyValues ) {
        return hardcode$( translator, Tokens.of( keyValues ) );
    }

    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $throws hardcode$( Translator translator, Tokens kvs ) {
        this.throwsPatterns.forEach(t -> t.hardcode$(translator, kvs) );
        return this;
    }

    @Override
    public List<String> list$() {
        List<String> $names = new ArrayList<>();
        this.throwsPatterns.forEach(t -> $names.addAll( t.list$() ) );
        return $names;
    }

    @Override
    public List<String> list$Normalized() {
        List<String> $namesNormalized = new ArrayList<>();
        this.throwsPatterns.forEach(t -> $namesNormalized.addAll( t.list$Normalized() ) );
        return $namesNormalized.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Returns the first _import that matches the pattern and constraint
     * @param astStartNode the node to look through
     * @param throwsMatchFn
     * @return  the first _import that matches (or null if none found)
     */
    @Override
    public _throws firstIn(Node astStartNode, Predicate<_throws> throwsMatchFn){
        if( astStartNode.findCompilationUnit().isPresent() ){
            Optional<CallableDeclaration> f = astStartNode.findCompilationUnit().get()
                .findFirst(CallableDeclaration.class, s ->{
                    Select sel = select(s);
                    return sel != null && throwsMatchFn.test(sel.thrown);
                });         
            if( f.isPresent()){
                return _throws.of(f.get());
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
        return selectFirstIn( ((_node)_n).ast(), selectConstraint );
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
                return listSelectedIn( ((_code) _n).astCompilationUnit(), selectConstraint );
            }
            return listSelectedIn( ((_type)_n).ast(), selectConstraint);
        }
        return listSelectedIn( ((_node)_n).ast(), selectConstraint );                
        //return listSelectedIn( _n.ast() );        
    }

    /**
     * Build ans return a _type with the import prototypes removed
     * @param clazz
     * @return 
     
    @Override
    public _type removeIn( Class clazz){
        return removeIn( _java.type(clazz));
    }
    */ 
    
    /**
     * Remove all matching occurrences of the proto in the node and return the
     * modified node
     * @param astNode the root node to start search
     * @param <N> the input node TYPE
     * @return the modified node
     
    @Override
    public <N extends Node> N removeIn(N astNode ){
        astNode.walk( CallableDeclaration.class, e-> {
            Select sel = select( e );
            if( sel != null ){
                for(int i=0;i< this.throwsPatterns.size(); i++){
                    final NodeList<ReferenceType> nodes = sel.thrown.astNodeWithThrows.getThrownExceptions();
                    $id th = this.throwsPatterns.get(i);
                    nodes.removeIf(t -> th.matches(t.toString()) );
                }                
            }
        });
        return astNode;
    }
    */ 

    /**
     *
     * @param _n the root model node to start from
     * @param <N> the TYPE of model node
     * @return the modified model node
     
    @Override
    public <N extends _node> N removeIn(N _n ){
        removeIn( _n.ast() );
        return _n;
    }
    */ 

    /**
     * 
     * @param clazz
     * @param throwClasses
     * @return 
     */
    public _type replaceIn(Class clazz, Class<? extends Throwable>... throwClasses){
        return replaceIn(_java.type(clazz), _throws.of(throwClasses));
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param throwClasses
     * @return 
     */
    public <N extends _java> N replaceIn(N _n, Class<? extends Throwable>... throwClasses){
        return replaceIn(_n, $throws.of(throwClasses));
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
        return replaceIn(_n, $throws.of(importDecl));
    }
    
    /**
     * 
     * @param clazz
     * @param _i
     * @return 
     */
    public _type replaceIn(Class clazz, _throws _i){
        return replaceIn( _java.type(clazz), _i);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param _i
     * @return 
     */
    public <N extends _java> N replaceIn(N _n, _throws _i){
        if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                replaceIn( ((_code) _n).astCompilationUnit(), $throws.of(_i));
                return _n;
            }
            replaceIn( ((_type)_n).ast(), $throws.of(_i));
            return _n;
        }
        replaceIn( ((_node)_n).ast(), $throws.of(_i));        
        return _n;
        /*
        
        Node astNode = _n.ast();
        if( astNode.findCompilationUnit().isPresent() ){
            astNode.findCompilationUnit().get().walk( CallableDeclaration.class, e-> {
                Select sel = select( e );
                if( sel != null ){
                    $throws res = $throws.of(_i);
                    _throws ct = res.construct(sel.args.asTokens());
                    for(int i=0;i< this.throwsPatterns.size(); i++){
                        final NodeList<ReferenceType> nodes = sel.thrown.astNodeWithThrows.getThrownExceptions();
                        $id th = this.throwsPatterns.get(i);
                        nodes.removeIf(t -> th.matches(t.toString()) );
                    }
                    sel.thrown.astNodeWithThrows.getThrownExceptions().addAll( res.construct(sel.args).list() );
                    //sel.ast().replace(_i.ast() );
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
    public _type replaceIn(Class clazz, $throws $i ){
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
    public <N extends _java> N replaceIn(N _n, $throws $i ){
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
    public <N extends Node> N replaceIn(N astNode, $throws $i ){
        if( astNode.findCompilationUnit().isPresent() ){
            astNode.findCompilationUnit().get().walk(CallableDeclaration.class, e-> {
                Select sel = select( e );
                if( sel != null ){
                    for(int i=0;i< this.throwsPatterns.size(); i++){
                        final NodeList<ReferenceType> nodes = sel.thrown.astNodeWithThrows.getThrownExceptions();
                        $id th = this.throwsPatterns.get(i);
                        nodes.removeIf(t -> th.matches(t.toString()) );
                    }
                    _throws _ths = $i.compose(sel.args.asTokens());
                    sel.thrown.addAll(_ths.list());
                    //sel.ast().replace($i.construct(sel.args).ast() );
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
    public _type forSelectedIn( Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer){
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
    public <N extends Node> N forEachIn(N astNode, Predicate<_throws> _throwsMatchFn, Consumer<_throws> _importActionFn){
        astNode.walk(CallableDeclaration.class, e-> {
            Select sel = select(e);
            if( sel != null && _throwsMatchFn.test(sel.thrown)){
                _importActionFn.accept(sel.thrown );
            }
        });
        return astNode;
    }

    /*
    @Override
    public <N extends _node> N forEachIn(N _n, Consumer<_throws> _importActionFn){
        forEachIn( _n.ast(), _importActionFn);        
        return _n;
    }
    */

    /**
     * A Matched Selection result returned from matching a prototype $import
     * inside of some CompilationUnit
     */
    public static class Select implements $proto.selected, 
        $proto.selected_model<_throws> {
    
        public final _throws thrown;
        public final $args args;

        public Select(_throws _i, $args tokens){
            this.thrown = _i;  
            this.args = tokens;
        }
        
        public Select( NodeWithThrownExceptions astImport, $args tokens){
            this.thrown = _throws.of(astImport );
            this.args = tokens;
        }
        
        @Override
        public $args args(){
            return args;
        }
        
        @Override
        public String toString(){
            return "$throws.Select {"+ System.lineSeparator()+
                Text.indent(thrown.toString() )+ System.lineSeparator()+
                Text.indent("$args : " + args) + System.lineSeparator()+
                "}";
        }

        public boolean is(String... throwsDecl){
            return thrown.is(throwsDecl);
        }
        
        public boolean is(Class... clazz){
            return thrown.is(clazz);
        }
        
        public boolean has(Class clazz){
            return thrown.has( clazz );
        }
        
        public boolean has(Class... clazz){
            return thrown.has( clazz );
        }
        
        public boolean has(String thrown){
            return this.thrown.has( thrown );
        }
        
        public boolean has(String... throwNames){
            return thrown.has( throwNames );
        }
        
        @Override
        public _throws _node() {
            return thrown;
        }
    }
}
