package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithThrownExceptions;
import com.github.javaparser.ast.type.ReferenceType;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.javaparser.ast.type.Type;
import org.jdraft.*;
import org.jdraft.text.Template;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

/**
 * pattern for an _throws declaration (i.e. on methods and constructors)
 * <PRE>
 *     void m() throws IOException {}
 *     void m() throws URLSyntaxException, IOException {}
 *
 * </PRE>>
 */
public class $throws
    implements Template<_throws>, //$pattern<_throws, $throws>,
        $pattern.$java<_throws,$throws>, $method.$part, $constructor.$part {

    public Class<_throws> _modelType(){
        return _throws.class;
    }

    /**
     * Matches entities that CAN have throws but have none
     * @return 
     */
    public static $throws none(){
        return of().$and(t-> t.isEmpty() );
    }

    public static $throws as(){
        return none();
    }

    public static $throws not(Class...clazzes ){
        return of().$not(clazzes);
    }

    public static $throws not(_typeRef...types ){
        return of().$not(types);
    }

    /**
     * Match ANY import
     * @return 
     */
    public static $throws of(){
        return new $throws( t-> true );        
    }


    public static $throws as( String... pattern){
        _throws _ths =  _throws.of(pattern);
        return new $throws( _ths ).$and(t-> t.size() ==_ths.size() );
    }

    public static $throws as( Class<? extends Throwable>...throwsClasses ){
        _throws _ths =  _throws.of(throwsClasses);
        return new $throws( _ths ).$and(t-> t.size() ==_ths.size() );
    }

    /**
     *
     * @param _proto
     * @return
     */
    public static $throws as( _throws _proto){
        return new $throws( _proto  ).$and(t-> t.size() ==_proto.size() );
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
        return new $throws( _throws.of(pattern) ).$and(constraint);
    }
    
    /**
     * 
     * @param exception
     * @param constraint
     * @return 
     */
    public static $throws of( Class<? extends Throwable> exception, Predicate<_throws> constraint){
        return new $throws( exception  ).$and(constraint);
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
        return new $throws( _proto ).$and(constraint);
    }

    public static $throws.Or or( _throws... _protos ){
        $throws[] arr = new $throws[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $throws.of( _protos[i]);
        }
        return or(arr);
    }

    public static $throws.Or or( $throws...$tps ){
        return new $throws.Or($tps);
    }

    public Predicate<_throws> constraint = t-> true;

    public List<$typeRef> throws$ids = new ArrayList<>();
    
    private <C extends Throwable> $throws( Class<C>...thrownExceptions ){
        this(_throws.of(thrownExceptions));
    } 
    
    //private $throws(_throws proto ){
    //    proto.forEach( t-> throws$ids.add(new $id(t.toString())));
    //}

    private $throws(_throws proto ){
        proto.forEach( t-> throws$ids.add($typeRef.of(t.toString())));
    }

    private $throws( Predicate<_throws> constraint ){        
        this.constraint = constraint;
    }
    
    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified prototype
     */
    public $throws $and(Predicate<_throws>constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    public $throws $not( Class...clazz ){
        Arrays.stream(clazz).forEach(c-> $not(_typeRef.of(c) ) ) ;
        return this;
    }

    public $throws $not( Type... types ){
        Arrays.stream(types).forEach(c-> $not(_typeRef.of(c) ) ) ;
        return this;
    }

    public $throws $not( _typeRef... types ){
        Arrays.stream(types).forEach(_t-> $not(_t) ) ;
        return this;
    }

    public $throws $not( _typeRef type ){
        Predicate<_throws> pt = (thr)-> thr.has(type);
        return $and( pt.negate() );
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
            List<ReferenceType> listed = _i.listAstElements();
            if( listed.size() < this.throws$ids.size() ){
                return null;
            }
            Tokens ts = new Tokens();
            for(int i = 0; i< throws$ids.size(); i++ ){
                //$id $tp = throws$ids.get(i);
                $typeRef $tp = throws$ids.get(i);
                
                Optional<ReferenceType> ort = 
                    listed.stream().filter(rt -> $tp.matches(rt.asString()) ).findFirst();
                if( !ort.isPresent() ){
                    return null;
                }
                ts = $tp.parseTo( ort.get(), ts );
                //ts = $tp.parseTo( ort.get().asString(), ts );
                listed.remove( ort.get() );
                if( ts == null ){
                    return null;
                }        
            }
            if( ts != null ){
                return new Select(_i, $tokens.of(ts) );
            }
        }
        return null;
    }

    public boolean isMatchAny(){
        try{
            return this.constraint.test(null) && this.throws$ids.isEmpty();
        }catch(Exception e){
            //System.out.println("THROWS NOT MATCH ANY" );
            return false;
        }
    }
    @Override
    public String toString() {
        if(this.isMatchAny() ){
            return "$throws{ $ANY$ }";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("$throws{ ");
        for(int i=0;i<this.throws$ids.size(); i++){
            if( i > 0 ){
                sb.append(", ");
            }
            sb.append(this.throws$ids.get(i));
        }
        sb.append(" }");
        return sb.toString();
    }

    @Override
    public _throws draft(Translator translator, Map<String, Object> keyValues) {
        _throws _ts = new _throws();
        if( keyValues.get("$throws$") != null ){ //PARAMETER OVERRIDE
            $throws $ths = $throws.of( keyValues.get("$throws$").toString() );
            Map<String,Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$throws$");
            return $ths.draft(translator, kvs);
        } 
        this.throws$ids.forEach(tp -> _ts.add( tp.draft(translator, keyValues) ) );
        return _ts;
    }
    
    /**
     * 
     * @param _ts
     * @param allTokens
     * @return 
     */
    public Tokens parseTo(_throws _ts, Tokens allTokens ){
        if(allTokens == null){
            return allTokens;
        }
        Select sel = select(_ts);
        if( sel != null ){
            if( allTokens.isConsistent(sel.tokens.asTokens()) ){
                allTokens.putAll(sel.tokens.asTokens());
                return allTokens;
            }
        }
        return null;
    }
     
    @Override
    public $throws $(String target, String $paramName) {
        this.throws$ids.forEach(t -> t.$(target, $paramName) );
        return this;
    }

    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $throws hardcode$( Translator translator, Tokens kvs ) {
        this.throws$ids.forEach(t -> t.hardcode$(translator, kvs) );
        return this;
    }

    @Override
    public List<String> list$() {
        List<String> $names = new ArrayList<>();
        this.throws$ids.forEach(t -> $names.addAll( t.list$() ) );
        return $names;
    }

    @Override
    public List<String> list$Normalized() {
        List<String> $namesNormalized = new ArrayList<>();
        this.throws$ids.forEach(t -> $namesNormalized.addAll( t.list$Normalized() ) );
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
     * @param _j the _java node
     * @param selectConstraint
     * @return  the first _import that matches (or null if none found)
     */
    public Select selectFirstIn( _java _j, Predicate<Select> selectConstraint ){
        if( _j instanceof _compilationUnit){
            if( ((_compilationUnit) _j).isTopLevel()){
                return selectFirstIn( ((_compilationUnit) _j).astCompilationUnit(), selectConstraint);
            }
            return selectFirstIn( ((_type)_j).ast(), selectConstraint);
        }
        return selectFirstIn( ((_java._compound)_j).ast(), selectConstraint );
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
        return listSelectedIn( (_type)_java.type(clazz), selectConstraint);
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
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(_java._domain _j, Predicate<Select> selectConstraint ){
        if( _j instanceof _compilationUnit){
            if( ((_compilationUnit) _j).isTopLevel()){
                return listSelectedIn( ((_compilationUnit) _j).astCompilationUnit(), selectConstraint );
            }
            return listSelectedIn( ((_type)_j).ast(), selectConstraint);
        }
        return listSelectedIn( ((_java._compound)_j).ast(), selectConstraint );
    }

    /**
     * 
     * @param clazz
     * @param throwClasses
     * @return 
     */
    public <_CT extends _type> _CT replaceIn(Class clazz, Class<? extends Throwable>... throwClasses){
        return (_CT)replaceIn((_type)_java.type(clazz), _throws.of(throwClasses));
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param throwClasses
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, Class<? extends Throwable>... throwClasses){
        return replaceIn(_j, $throws.of(throwClasses));
    }
    
    /**
     * 
     * @param clazz
     * @param importDecl
     * @return 
     */
    public <_CT extends _type> _CT replaceIn(Class clazz, String importDecl){
        return (_CT)replaceIn( (_type)_java.type(clazz), importDecl);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param importDecl
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, String importDecl){
        return replaceIn(_j, $throws.of(importDecl));
    }
    
    /**
     * 
     * @param clazz
     * @param _i
     * @return 
     */
    public <_CT extends _type> _CT replaceIn(Class clazz, _throws _i){
        return (_CT)replaceIn( (_type)_java.type(clazz), _i);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param _replaceThrows
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, _throws _replaceThrows){
        if( _j instanceof _compilationUnit){
            if( ((_compilationUnit) _j).isTopLevel()){
                replaceIn( ((_compilationUnit) _j).astCompilationUnit(), $throws.of(_replaceThrows));
                return _j;
            }
            replaceIn( ((_type) _j).ast(), $throws.of(_replaceThrows));
            return _j;
        }
        replaceIn( ((_java._compound) _j).ast(), $throws.of(_replaceThrows));
        return _j;
    }
    
    /**
     * 
     * @param clazz
     * @param $i
     * @return 
     */
    public <_CT extends _type> _CT replaceIn(Class clazz, $throws $i ){
        return (_CT)replaceIn((_type)_java.type(clazz), $i);
    }

    /**
     * Replace all occurrences of the template in the code with the replacement
     * (composing the replacement from the constructed tokens in the source)
     *
     * @param _j the model to find replacements
     * @param $replaceThrows the template to be constructed as the replacement
     * @param <_J> the TYPE of model
     * @return
     */
    public <_J extends _java> _J replaceIn(_J _j, $throws $replaceThrows ){
        if( _j instanceof _compilationUnit){
            if( ((_compilationUnit) _j).isTopLevel()){
                replaceIn( ((_compilationUnit) _j).astCompilationUnit(), $replaceThrows);
                return _j;
            }
            replaceIn( ((_type) _j).ast(), $replaceThrows);
            return _j;
        }
        replaceIn( ((_java._compound) _j).ast(), $replaceThrows);
        return _j;
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param $replaceThrows
     * @return 
     */
    public <N extends Node> N replaceIn(N astNode, $throws $replaceThrows ){
        if( astNode.findCompilationUnit().isPresent() ){
            astNode.findCompilationUnit().get().walk(CallableDeclaration.class, e-> {
                Select sel = select( e );
                if( sel != null ){
                    for(int i = 0; i< this.throws$ids.size(); i++){
                        final NodeList<ReferenceType> nodes = sel.thrown.astNodeWithThrows.getThrownExceptions();
                        $typeRef th = this.throws$ids.get(i);
                        nodes.removeIf(t -> th.matches(t) );
                        //$id th = this.throws$ids.get(i);
                        //nodes.removeIf(t -> th.matches(t.toString()) );
                    }
                    _throws _ths = $replaceThrows.draft(sel.tokens.asTokens());
                    sel.thrown.addAll(_ths.listAstElements());
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
    public <_CT extends _type> _CT forSelectedIn( Class clazz, Consumer<Select> selectConsumer){
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectConsumer);
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
    public <_CT extends _type> _CT forSelectedIn( Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer){
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectConstraint, selectConsumer);
    }
    
    /**
     * 
     * @param <_T>
     * @param _t
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public <_T extends _type> _T forSelectedIn(_T _t, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer ){
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

    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are suppressed.
     */
    public static class Or extends $throws {

        final List<$throws>ors = new ArrayList<>();

        protected Or($throws...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $throws hardcode$(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.hardcode$(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$throws.Or{");
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
        public $throws.Select select(_throws n){
            $throws $a = whichMatch(n);
            if( $a != null ){
                return $a.select(n);
            }
            return null;
        }

        public boolean isMatchAny(){
            return false;
        }

        /**
         * Return the underlying $throws that matches the _throws or null if none of the match
         * @param th
         * @return
         */
        public $throws whichMatch(_throws th){
            if( !this.constraint.test(th ) ){
                return null;
            }
            Optional<$throws> orsel  = this.ors.stream().filter( $p-> $p.matches(th) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }

    /**
     * A Matched Selection result returned from matching a prototype $import
     * inside of some CompilationUnit
     */
    public static class Select implements $pattern.selected,
            select_java<_throws> {
    
        public final _throws thrown;
        public final $tokens tokens;

        public Select(_throws _i, $tokens tokens){
            this.thrown = _i;  
            this.tokens = tokens;
        }
        
        public Select( NodeWithThrownExceptions astImport, $tokens tokens){
            this.thrown = _throws.of(astImport );
            this.tokens = tokens;
        }
        
        @Override
        public $tokens tokens(){
            return tokens;
        }
        
        @Override
        public String toString(){
            return "$throws.Select {"+ System.lineSeparator()+
                Text.indent(thrown.toString() )+ System.lineSeparator()+
                Text.indent("$tokens : " + tokens) + System.lineSeparator()+
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
