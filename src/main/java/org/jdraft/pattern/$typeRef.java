package org.jdraft.pattern;

import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.Type;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.github.javaparser.ast.type.WildcardType;
import org.jdraft.*;
import org.jdraft.text.*;

/**
 * Template for a Java Type {@link Type} Reference
 */
public class $typeRef
    implements Template<_typeRef>, $pattern<_typeRef, $typeRef>, $method.$part, $field.$part,
        $parameter.$part, $typeParameter.$part, $var.$part, $annotationEntry.$part {

    public static final $typeRef VOID = of( new VoidType() );

    /**
     * 
     * @param pattern
     * @return 
     */
    public static $typeRef of(String pattern ){
        return new $typeRef(Types.of(pattern));
    }
 
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $typeRef of(String pattern, Predicate<_typeRef> constraint){
        return new $typeRef(Types.of(pattern)).$and(constraint);
    }
    
    /**
     * 
     * @param typeClass
     * @return 
     */
    public static $typeRef of( Class typeClass ){
        return $typeRef.of( Types.of(typeClass) );
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
        return new $typeRef( astType ).$and(constraint);
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
        return new $typeRef(_proto.ast()).$and(constraint);
    }

    public static $typeRef.Or or( Class... typeClasses ){
        $typeRef[] arr = new $typeRef[typeClasses.length];
        for(int i=0;i<typeClasses.length;i++){
            arr[i] = $typeRef.of( typeClasses[i]);
        }
        return or(arr);
    }

    public static $typeRef.Or or( _typeRef... _protos ){
        $typeRef[] arr = new $typeRef[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $typeRef.of( _protos[i]);
        }
        return or(arr);
    }

    public static $typeRef.Or or( $typeRef...typeRefs ){
        return new Or(typeRefs);
    }

    public static $typeRef as(String exact){
        return as(_typeRef.of(exact));
    }

    public static $typeRef as(Class clazz){
        return as( _typeRef.of(clazz));
    }

    public static $typeRef as( Type type ){
        return as( _typeRef.of(type));
    }

    public static $typeRef as(_typeRef _exact){
        $typeRef $t = of(_exact);

        $t.$and(_t->
            _annoExprs.of(_t.ast()).equals(_annoExprs.of(_exact.ast())) && /* Type Annotations */
            _t.getArrayDimensions() == _exact.getArrayDimensions() && /* Array Dimensions */
            Types.equal( _t.getTypeArguments(), _exact.getTypeArguments() ) && /* Type Arguments */
            Types.equal(_t.getBaseType().ast(), _exact.getBaseType().ast() )
            /* Base Type */
          );

        return $t;
    }

    /** Matching constraint */
    public Predicate<_typeRef> constraint = (t)-> true;

    /** This MIGHT be an explicit type OR it might be a pattern */
    public Type type;

    /**
     * 
     * @param astProtoType 
     */
    public  $typeRef( Type astProtoType ){
        this.type = astProtoType.clone();
    }

    /**
     * 
     * @param _proto the proto to build the $typeRef prototype from 
     */
    public $typeRef(_typeRef _proto) {
        this.type = _proto.ast().clone();
    }
    
    /**
     * 
     * @param pattern 
     */
    private $typeRef(String pattern){
        this.type = Types.of(pattern);
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
    public $typeRef $and(Predicate<_typeRef> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    @Override
    public $typeRef $(String target, String $paramName) {
        Stencil st = typePattern();
        st = st.$(target, $paramName);
        this.type = Types.of(st.toString());
        return this;
    }

    @Override
    public Template<_typeRef> $hardcode(Translator translator, Map<String, Object> keyValues) {
        return null;
    }

    /**
     * 
     * @param astExpr
     * @param $name
     * @return 
     */
    public $typeRef $(Expression astExpr, String $name){
        return $( astExpr.toString(), $name);
    }

    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $typeRef $hardcode(Translator translator, Tokens kvs ) {
        Stencil st = typePattern();
        //System.out.println("FIRST "+st +" "+kvs);
        st = st.$hardcode(translator, kvs);
        //System.out.println( "NEXT"+st);
        this.type = Types.of(st.toString());
        return this;
    }

    /**
     *
     * @param allTokens
     * @return
     */
    public Tokens parseTo(Type t, Tokens allTokens ){
        return parseTo( _typeRef.of(t), allTokens);
        /*
        if(allTokens == null){
            return null;
        }
        Select sel = select(_t);
        if( sel != null ){
            if( allTokens.isConsistent(sel.tokens.asTokens()) ){
                allTokens.putAll(sel.tokens.asTokens());
                return allTokens;
            }
        }
        return null;
        */
    }

    /**
     * 
     * @param _t
     * @param allTokens
     * @return 
     */
    public Tokens parseTo(_typeRef _t, Tokens allTokens ){
        if(allTokens == null){
            return null;
        }
        Select sel = select(_t);
        if( sel != null ){
            if( allTokens.isConsistent(sel.tokens.asTokens()) ){
                allTokens.putAll(sel.tokens.asTokens());
                return allTokens;
            }
        }
        return null;
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
        return of().$and(constraint);
    }
    
    /**
     * 
     * @param
     * @return 

    public _typeRef draft(_java._multiPart _n ){
        return draft(_n.tokenize());
    }
     */
    
    @Override
    public _typeRef draft(Translator t, Map<String,Object> tokens ){
        return _typeRef.of(typePattern().draft( t, tokens ));
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
        return select( Types.of(type)) != null;
    }

    /**
     * 
     * @param _tr
     * @return 
     */
    public boolean matches( _typeRef _tr ){
        return select(_tr) != null;
    }

    public boolean matches(java.lang.reflect.Type t){
        return matches(_typeRef.of(t));
    }

    public boolean matches(Class clazz){
        return matches(_typeRef.of(clazz));
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
    public List<String> $list(){
        return this.typePattern().$list();
    }

    @Override
    public List<String> $listNormalized(){
        return this.typePattern().$listNormalized();
    }
    
    /**
     * 
     * @param _tr
     * @return 
     */
    public Select select( _typeRef _tr){

        if( this.constraint.test(_tr ) ) {
            if( _tr.ast().isTypeParameter() ){
                return null;
            }
            if( _tr.isArrayType() && !this.type.isArrayType() ){
                //both array types:
                //still match
                return select( _typeRef.of(_tr.getElementType()));
            }
            if( _tr.isGenericType() && !(this.type.isClassOrInterfaceType() && this.type.asClassOrInterfaceType().getTypeArguments().isPresent()) ){
                return select( _tr.getErasedType() );
            }
            if( _tr.isWildcard() ){
                if( this.type.isWildcardType() ){
                    WildcardType wct = this.type.asWildcardType();
                    /** TODO THIS IS A BROKEN MESS... I NEED AN INNER IMPL SPECIFICALLY FOR WILDCARD CLASSES
                     * ELSE THIS IS GOING TO BE A SLOW THING
                    if( this.type.asWildcardType().getExtendedType().isPresent()){
                        if( !wct.getExtendedType().isPresent() ){
                            return null;
                        }
                        Select ext = this.type.asWildcardType().
                    }
                    */
                    return null;
                }
                //check whether the type matches EITHER the super or extendsed type
                Optional<ReferenceType> et = _tr.ast().asWildcardType().getExtendedType();
                if( et.isPresent() ){
                    Select sel = select( et.get() );
                    if( sel != null ){
                        return new Select( _tr, sel.tokens.asTokens());
                    }
                }
                Optional<ReferenceType> st = _tr.ast().asWildcardType().getSuperType();
                if( st.isPresent() ){
                    Select sel = select( st.get() );
                    if( sel != null ){
                        return new Select( _tr, sel.tokens.asTokens());
                    }
                }
                return null; //couldnt match either the super OR extended Type
            }
            if( _tr.hasAnnoExprs() && this.type.getAnnotations().isEmpty()){ //the candidate has annotation(s) the target does not
                try {
                    return select(_tr.toString(Print.PRINT_NO_ANNOTATIONS_OR_COMMENTS));
                } catch(Exception e){
                    //System.out.println( "BEFORE "+ _tr +" "+_tr.ast().getClass());
                    //System.out.println( "AFTER "+ _tr.toString(Ast.PRINT_NO_ANNOTATIONS_OR_COMMENTS) );
                    throw new _jdraftException("Failed on type "+ _tr+ " with "+ this );
                }
            }
            Tokens ats = new Tokens();
            _annoExprs _as = _typeRef.of(this.type).getAnnoExprs();
            if( _tr.hasAnnoExprs() && !_as.isEmpty() ){
                //System.out.println ("BOTH HAVE ANNOS");
                ats = $annoRefs.of(_as).parse(_tr.getAnnoExprs());
                if( ats == null ){
                    //System.out.println( "Tokens not equal");
                    return null;
                }
                Select sel = $typeRef.of(this.type.toString(Print.PRINT_NO_ANNOTATIONS_OR_COMMENTS))
                        .select(_tr.toString(Print.PRINT_NO_ANNOTATIONS_OR_COMMENTS));
                if( sel == null){
                    return null;
                }
                sel.type = _tr;
                sel.tokens.putAll(ats);
                return sel;
            }
            if( Types.equal(_tr.ast(), this.type)){
                return new Select( _tr, Tokens.of());
            }

            Tokens ts = typePattern().parse(_tr.toString() );
            if( ts != null ){
                ts.putAll(ats);
                return new Select( _tr, ts );
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
    public List<Select> listSelectedIn(_java._domain _j){
        if( _j instanceof _codeUnit){
            _codeUnit _c = (_codeUnit) _j;
            if( _c.isTopLevel() ){
                return listSelectedIn(_c.astCompilationUnit());
            }
            _type _t = (_type) _j; //only possible
            return listSelectedIn(_t.ast()); //return the TypeDeclaration, not the CompilationUnit
        }
        return listSelectedIn( ((_java._node) _j).ast());
    }

    /**
     * 
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(_java._domain _j, Predicate<Select> selectConstraint){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel()){
                return listSelectedIn( ((_codeUnit) _j).astCompilationUnit(), selectConstraint);
            }
            return listSelectedIn( ((_type)_j).ast(), selectConstraint);
        }
        return listSelectedIn( ((_java._node)_j).ast(), selectConstraint);
    }
    
    /**
     * 
     * @param clazz
     * @param selectConsumer
     * @return 
     */
    public <_CT extends _type> _CT forSelectedIn( Class clazz, Consumer<Select> selectConsumer ){
        return (_CT)forSelectedIn( (_type) _type.of(clazz), selectConsumer);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConsumer
     * @return 
     */
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Consumer<Select> selectConsumer ){
        Tree.in(_j, Type.class, e-> {
            Select sel = select( e );
            if( sel != null ){
                selectConsumer.accept( sel );
            }
        });
        return _j;
    }

    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public <_CT extends _type> _CT  forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer ){
        return (_CT)forSelectedIn( (_type) _type.of(clazz), selectConstraint, selectConsumer);
    }
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer ){
        Tree.in(_j, Type.class, e-> {
            Select sel = select( e );
            if( sel != null && selectConstraint.test(sel) ){
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
    public <_CT extends _type> _CT  replaceIn(Class clazz, Class replacementType){
        return (_CT)replaceIn( (_type) _type.of(clazz), replacementType);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param replacementType
     * @return 
     */
    public <N extends _java._domain> N replaceIn(N _n, Class replacementType){
        return replaceIn(_n, $typeRef.of(replacementType));
    }
    
    /**
     * 
     * @param clazz
     * @param replacementType
     * @return 
     */
    public <_CT extends _type> _CT  replaceIn( Class clazz, _typeRef replacementType ){
        return (_CT)replaceIn( (_type) _type.of(clazz), replacementType);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param replacementType
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, _typeRef replacementType){
        return replaceIn(_j, $typeRef.of(replacementType));
    }

    /**
     * Find and replaceIn
     * @param _j
     * @param astReplacementType
     * @param <_J>
     * @return
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, Type astReplacementType){
        return replaceIn(_j, $typeRef.of(astReplacementType));
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
     * @param _j
     * @param $replacementType the template for the replacement TYPE
     * @param <_J>
     * @return
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, $typeRef $replacementType){
        Tree.in(_j, Type.class, e -> {
            Select select = select(e);
            if( select != null ){
                if( !e.replace($replacementType.draft(select.tokens).ast() )){
                    throw new _jdraftException("unable to replaceIn "+ e + " in "+ _j +" with "+$replacementType);
                }
            }
        });
        return _j;
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param replacementPattern
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, String...replacementPattern){
        return replaceIn(_j, $typeRef.of( Text.combine(replacementPattern)) );
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
        return "$typeRef{ "+ this.typePattern() + " }";
    }

    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are suppressed.
     */
    public static class Or extends $typeRef {

        final List<$typeRef>ors = new ArrayList<>();

        public Or($typeRef...$as){
            super("$typeRef$");
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $typeRef $hardcode(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.$hardcode(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$typeRef.Or{");
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
        public $typeRef.Select select(_typeRef n){
            $typeRef $a = whichMatch(n);
            if( $a != null ){
                return $a.select(n);
            }
            return null;
        }

        public boolean isMatchAny(){
            return false;
        }

        /**
         * Return the underlying $typeRef that matches the Method or null if none of the match
         * @param tps
         * @return
         */
        public $typeRef whichMatch(_typeRef tps){
            if( !this.constraint.test(tps ) ){
                return null;
            }
            Optional<$typeRef> orsel  = this.ors.stream().filter( $p-> $p.matches(tps) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }

    /**
     * 
     */
    public static class Select implements $pattern.selected,
            select_java<_typeRef> {
        
        /** The underlying selected _typeRef */
        public _typeRef type;
        
        /** the tokens parsed*/
        public $tokens tokens;

        public Select(_typeRef _tr, Tokens tokens ){
            this.type = _tr;
            this.tokens = $tokens.of(tokens);
        }
        
        public Select( Type type, $tokens tokens){
            this.type = _typeRef.of(type);
            this.tokens = tokens;
        }
        
        @Override
        public $tokens tokens(){
            return tokens;
        } 
        
        @Override
        public String toString(){
            return "$typeRef.Select {"+ System.lineSeparator()+
                Text.indent(type.toString() )+ System.lineSeparator()+
                Text.indent("$tokens : " + tokens) + System.lineSeparator()+
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
            return this.type.isArrayType();
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
