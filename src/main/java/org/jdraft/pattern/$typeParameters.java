package org.jdraft.pattern;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jdraft.*;
import org.jdraft._typeParams;
import org.jdraft.text.Template;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

/**
 * prototype for an _import declaration on a Java top level _type 
 *
 */
public class $typeParameters
    implements Template<_typeParams>, //$pattern<_typeParameters,$typeParameters>,
        $pattern.$java<_typeParams,$typeParameters>,
        $method.$part, $constructor.$part, $class.$part,$interface.$part, $type.$part {

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
        return new $typeParameters( _typeParams.of(pattern)  );
    }

    public static $typeParameters of( List<$typeParameter> $tps ){
        return new $typeParameters($tps);
    }

    /**
     *
     * @param constraint
     * @return
     */
    public static $typeParameters of( Predicate<_typeParams> constraint ){
        return new $typeParameters( constraint );
    }

    /**
     *
     * @param pattern
     * @param constraint
     * @return
     */
    public static $typeParameters of( String pattern, Predicate<_typeParams> constraint){
        return new $typeParameters( _typeParams.of(pattern) ).$and(constraint);
    }

    /**
     *
     * @param _proto
     * @return
     */
    public static $typeParameters of( _typeParams _proto){
        return new $typeParameters( _proto  );
    }

    /**
     *
     * @param _proto
     * @param constraint
     * @return
     */
    public static $typeParameters of(_typeParams _proto, Predicate<_typeParams> constraint){
        return new $typeParameters( _proto ).$and(constraint);
    }

    public static $typeParameters.Or or( _typeParams... _protos ){
        $typeParameters[] arr = new $typeParameters[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $typeParameters.of( _protos[i]);
        }
        return or(arr);
    }

    public static $typeParameters as(String...str){
        if( str.length == 0 ){
            return none();
        }
        String combined = Text.combine(str);
        if( combined.length() == 0 ){
            return none();
        }
        return as( _typeParams.of(combined) );
    }

    public static $typeParameters as(_typeParams _tps){
        if( _tps.isEmpty() ){
            return none();
        }
        List<$typeParameter> $tps = new ArrayList<>();
        _tps.forEach(t-> $tps.add($typeParameter.as(t) ) );
        return of( $tps ).$and(_ttps -> _ttps.size() == _tps.size() );
    }

    public static $typeParameters.Or or( $typeParameters...$tps ){
        return new Or($tps);
    }

    /**
     *
     * @return
     */
    public static $typeParameters none(){
        return of().$and(tps-> tps.isEmpty());
    }

    public Predicate<_typeParams> constraint = t-> true;
        
    public List<$typeParameter> typeParams = new ArrayList<>();

    private $typeParameters(){
        this(new ArrayList<>());
    }

    private $typeParameters(_typeParams proto ){
        proto.forEach(t-> typeParams.add(new $typeParameter(t )));
    }

    private $typeParameters( List<$typeParameter> $tps){
        this.typeParams.addAll($tps);
    }

    private $typeParameters( Predicate<_typeParams> constraint ){
        this.constraint = constraint;
    }

    public Class<_typeParams> _modelType(){
        return _typeParams.class;
    }

    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified prototype
     */
    public $typeParameters $and(Predicate<_typeParams>constraint ){
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
     * @param node
     * @return
     */
    public boolean match( Node node ){
        if( node instanceof NodeWithTypeParameters ){
            return matches( (NodeWithTypeParameters)node);
        }
        return false;
    }

    public boolean match( _java _j ){
        if( _j instanceof _typeParams._withTypeParams){
            return matches( (_typeParams._withTypeParams)_j);
        }
        return false;
    }

    /**
     * Not great, but working implementation (because I need to synthesize a NodeWithTypeParameters)
     * in order to use the API (wasteful but gets the job done)
     * @param ntps
     * @return
     */
    public boolean matches(NodeList<TypeParameter> ntps){
        //this sucks... but ohh well
        MethodDeclaration dummy = Ast.method("void $DUMMY_TYPE_PARAMETER_HOLDER$(){}");
        ntps.forEach(tp -> dummy.addTypeParameter(tp) );
        return matches(_typeParams.of(dummy));
    }

    public boolean matches(_typeParams._withTypeParams _htp){
        return matches( (NodeWithTypeParameters) ((_java._multiPart)_htp).ast() );
    }

    public boolean isMatchAny(){
        try {
            return this.constraint.test(null) && this.typeParams.isEmpty();
        } catch(Exception e ){
            System.out.println("TYPEPARAMETERS NOT MATCH ANY" );
            return false;
        }
    }

    /**
     *
     * @param nwtp
     * @return
     */
    public boolean matches (NodeWithTypeParameters nwtp ){
        return select(_typeParams.of(nwtp) ) != null;
    }

    /**
     * 
     * @param typeParams
     * @return 
     */
    public boolean matches( String... typeParams ){
        String composite = Text.combine(typeParams);
        //if( composite.length() == 0 ){
        //    return this.isMatchAny();
        //}
        return matches(_typeParams.of(typeParams) );
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
    public boolean matches( _typeParams _t){
        return select( _t ) != null;
    }

    /**
     * 
     * @param astCallable
     * @return 
     */
    public Select select( CallableDeclaration astCallable ){
        return select( _typeParams.of( astCallable ) );
    }
    
    /**
     * 
     * @param _i
     * @return 
     */
    public Select select(_typeParams _i){
        if( this.constraint.test(_i)){            
                
            List<_typeParam> listed = _i.list();
            if( listed.size() < this.typeParams.size() ){
                return null;
            }
            Tokens ts = new Tokens();
            for(int i=0;i<typeParams.size();i++ ){
                $typeParameter $tp = typeParams.get(i);
                
                Optional<_typeParam> ort =
                    listed.stream().filter(rt -> $tp.matches(rt) ).findFirst();
                if( !ort.isPresent() ){
                    return null;
                }
                $typeParameter.Select s = $tp.select( ort.get() );
                if( s == null || !ts.isConsistent(s.$tokens.asTokens()) ){
                    return null;
                }
                ts.putAll(s.tokens().asTokens());
                listed.remove( ort.get() );                
            }
            return new Select(_i, $tokens.of(ts) );
        }            
        return null;
    }
 
    @Override
    public String toString() {
        if( this.isMatchAny() ){
            return "$typeParameters{ $ANY$ }";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("$typeParameters{").append(System.lineSeparator());
        for(int i=0;i<this.typeParams.size(); i++){
            //if( i > 0 ){
            //    sb.append(",");
            //}
            sb.append(Text.indent(this.typeParams.get(i).toString()));
        }
        sb.append("}");
        //this.typeParams.forEach(tp ->sb.append(tp.toString() ));
        return sb.toString();
        //"$typeParameters{"+System.lineSeparator()+
        //        sb.toString()+ System.lineSeparator()+
        //        "}";
    }

    @Override
    public _typeParams draft(Translator translator, Map<String, Object> keyValues) {
        
        _typeParams _ts = _typeParams.of();
        if( keyValues.get("$typeParameters") != null ){ //PARAMETER OVERRIDE
            Object tps = keyValues.get("$typeParameters");
            Map<String,Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$typeParameters");
            
            if( tps instanceof $typeParameters ){
                return (($typeParameters)tps).draft(translator, kvs);
            } 
            if( tps instanceof _typeParams){
                return ($typeParameters.of((_typeParams)tps)).draft(translator, kvs);
            }
            return $typeParameters.of(tps.toString()).draft(translator, kvs);
        }         
        this.typeParams.forEach( tp -> _ts.add( tp.draft(translator, keyValues) ) );
        return _ts;         
    }

    //TODO this seems backwards, select should call parse
    public Tokens parse(_typeParams _ts){
        Select sel = select(_ts);
        if( sel != null ){
            return sel.tokens.asTokens();
        }
        return null;
    }

    /**
     * 
     * @param _ts
     * @param allTokens
     * @return 
     */
    public Tokens parseTo(_typeParams _ts, Tokens allTokens ){
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
    public $typeParameters $(String target, String $paramName) {
        this.typeParams.forEach(t -> t.$(target, $paramName) );
        return this;
    }

    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $typeParameters $hardcode(Translator translator, Tokens kvs ) {
        this.typeParams.forEach(t -> t.$hardcode(translator, kvs) );
        return this;
    }

    @Override
    public List<String> $list() {
        List<String> $names = new ArrayList<>();
        this.typeParams.forEach(t -> $names.addAll( t.$list() ) );
        return $names;
    }

    @Override
    public List<String> $listNormalized() {
        List<String> $namesNormalized = new ArrayList<>();
        this.typeParams.forEach(t -> $namesNormalized.addAll( t.$listNormalized() ) );
        return $namesNormalized.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Returns the first _import that matches the pattern and constraint
     * @param astStartNode the node to look through
     * @param typeParamsMatchFn
     * @return  the first _import that matches (or null if none found)
     */
    @Override
    public _typeParams firstIn(Node astStartNode, Predicate<_typeParams> typeParamsMatchFn){
        if( astStartNode.findCompilationUnit().isPresent() ){
            Optional<CallableDeclaration> f = astStartNode.findCompilationUnit().get()
                .findFirst(CallableDeclaration.class, s ->{
                    Select sel = select(s);
                    return sel != null && typeParamsMatchFn.test(sel.typeParameters);                    
                });         
            if( f.isPresent()){
                return _typeParams.of(f.get());
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
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel()){
                return selectFirstIn( ((_codeUnit) _j).astCompilationUnit(), selectConstraint);
            }
            return selectFirstIn( ((_type)_j).ast(), selectConstraint);
        }
        return selectFirstIn( ((_java._multiPart)_j).ast(), selectConstraint);
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
        return listSelectedIn((_type) _type.of(clazz));
    }
    
    @Override
    public List<Select> listSelectedIn( _java._domain _j){
        if( _j instanceof _codeUnit){
            _codeUnit _c = (_codeUnit) _j;
            if( _c.isTopLevel() ){
                return listSelectedIn(_c.astCompilationUnit());
            }
            _type _t = (_type) _j; //only possible
            return listSelectedIn(_t.ast()); //return the TypeDeclaration, not the CompilationUnit
        }
        return listSelectedIn( ((_java._multiPart) _j).ast());
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn( Class clazz, Predicate<Select> selectConstraint ){
        return listSelectedIn((_type) _type.of(clazz), selectConstraint);
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
    public List<Select> listSelectedIn(_java._domain _n, Predicate<Select> selectConstraint ){
        if( _n instanceof _codeUnit){
            if( ((_codeUnit) _n).isTopLevel()){
                return listSelectedIn( ((_codeUnit) _n).astCompilationUnit(), selectConstraint);
            }
            return listSelectedIn( ((_type)_n).ast(), selectConstraint);
        }
        return listSelectedIn( ((_java._multiPart)_n).ast(), selectConstraint);
    }
  
    /**
     * 
     * @param clazz
     * @param importDecl
     * @return 
     */
    public <_CT extends _type> _CT  replaceIn(Class clazz, String importDecl){
        return (_CT)replaceIn( (_type) _type.of(clazz), importDecl);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param importDecl
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, String importDecl){
        return replaceIn(_j, $typeParameters.of(importDecl));
    }
    
    /**
     * 
     * @param clazz
     * @param _i
     * @return 
     */
    public <_CT extends _type> _CT  replaceIn(Class clazz, _typeParams _i){
        return (_CT)replaceIn( (_type) _type.of(clazz), _i);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param _i
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, _typeParams _i){
        return replaceIn(_j, $typeParameters.of(_i));
    }
    
    /**
     * 
     * @param clazz
     * @param $i
     * @return 
     */
    public <_CT extends _type> _CT  replaceIn(Class clazz, $typeParameters $i ){
        return (_CT)replaceIn((_type) _type.of(clazz), $i);
    }
    
    /**
     * Replace all occurrences of the template in the code with the replacement
     * (composing the replacement from the constructed tokens in the source)
     *
     * @param _j the model to find replacements
     * @param $i the template to be constructed as the replacement
     * @param <_J> the TYPE of model
     * @return
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, $typeParameters $i ){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel()){
                replaceIn( ((_codeUnit) _j).astCompilationUnit(), $i);
                return _j;
            }
            replaceIn( ((_type) _j).ast(), $i);
            return _j;
        }
        replaceIn( ((_java._multiPart) _j).ast(), $i);
        return _j;
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
                    _typeParams _ths = $i.draft(sel.tokens.asTokens());
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
    public <_CT extends _type> _CT  forSelectedIn( Class clazz, Consumer<Select> selectConsumer){
        return (_CT)forSelectedIn((_type) _type.of(clazz), selectConsumer);
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
    public <_CT extends _type> _CT  forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer ){
       return (_CT)forSelectedIn((_type) _type.of(clazz), selectConstraint, selectConsumer);
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
    public <N extends Node> N forEachIn(N astNode, Predicate<_typeParams>_typeParamMatchFn, Consumer<_typeParams> _typeParamActionFn){
        astNode.walk(CallableDeclaration.class, e-> {
            Select sel = select(e);
            if( sel != null && _typeParamMatchFn.test(sel.typeParameters)){
                _typeParamActionFn.accept(sel.typeParameters );
            }
        });
        return astNode;
    }

    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are suppressed.
     */
    public static class Or extends $typeParameters {

        final List<$typeParameters>ors = new ArrayList<>();

        public Or($typeParameters...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $typeParameters $hardcode(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.$hardcode(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$typeParameters.Or{");
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
        public $typeParameters.Select select(_typeParams n){
            $typeParameters $a = whichMatch(n);
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
        public $typeParameters whichMatch(_typeParams tps){
            if( !this.constraint.test(tps ) ){
                return null;
            }
            Optional<$typeParameters> orsel  = this.ors.stream().filter( $p-> $p.matches(tps) ).findFirst();
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
            select_java<_typeParams> {
    
        public final _typeParams typeParameters;
        public final $tokens tokens;

        public Select(_typeParams _i, $tokens tokens){
            this.typeParameters = _i;  
            this.tokens = tokens;
        }
        
        public Select( NodeWithTypeParameters astImport, $tokens tokens){
            this.typeParameters = _typeParams.of(astImport );
            this.tokens = tokens;
        }
        
        @Override
        public $tokens tokens(){
            return tokens;
        }
        
        @Override
        public String toString(){
            return "$typeParameters.Select {"+ System.lineSeparator()+
                Text.indent(typeParameters.toString() )+ System.lineSeparator()+
                Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                "}";
        }
        
        @Override
        public _typeParams _node() {
            return typeParameters;
        }
    }
}
