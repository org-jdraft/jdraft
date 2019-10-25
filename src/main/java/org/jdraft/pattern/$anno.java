package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.expr.*;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jdraft.*;

/**
 * prototype for an annotation
 *
 * @author Eric
 */
public class $anno
    implements Template<_anno>, $pattern<_anno, $anno>, $pattern.$java<_anno, $anno>,
        $constructor.$part, $method.$part,
        $field.$part, $parameter.$part, $typeParameter.$part, $class.$part, $interface.$part, $enum.$part, $annotation.$part,
        $enumConstant.$part{

    /**
     * Returns the Ast node implementation type that is used to identify the types as walking the AST
     * @return the type that this $proto intercepts
     */
    public Class<AnnotationExpr> astType(){
        return AnnotationExpr.class;
    }

    /**
     * Returns the _java node implementation type
     * @return
     */
    public Class<_anno> _modelType(){
        return _anno.class;
    }

    public static $anno of(){
        return new $anno( $id.of() );
    }
    
    public static $anno of( $id name, $memberValue...memberValues ){
        return new $anno(name, memberValues);
    }
    
    public static $anno of(String codePattern) {
        return new $anno(_anno.of(codePattern));
    }
    
    public static $anno of(String... codePattern) {
        return new $anno(_anno.of(codePattern));
    }
    
    public static $anno of( Predicate<_anno> constraint ){
        return of().$and(constraint);
    }
    
    public static $anno of(String codePattern, Predicate<_anno>constraint) {
        return new $anno(_anno.of(codePattern)).$and(constraint);
    }
    
    public static $anno of(_anno _an) {
        return new $anno(_an);
    }

    public static $anno of(_anno _an, Predicate<_anno>constraint) {
        return new $anno(_an).$and(constraint);
    }
        
    public static $anno of(Class<? extends Annotation>sourceAnnoClass) {
        return new $anno(_anno.of(sourceAnnoClass));
    }
    
    public static $anno of (Class<? extends Annotation>sourceAnnoClass, Predicate<_anno> constraint) {
        return of( sourceAnnoClass).$and(constraint);
    }

    /**
     *
     * @param anonymousObjectWithAnnotation
     * @return
     */
    public static $anno of( Object anonymousObjectWithAnnotation ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Ex.anonymousObjectEx( ste );
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty() ).findFirst().get();
        return of( _anno.of(bd.getAnnotation(0) ) );
    }


    public static $anno.Or or( Class<? extends Annotation>... annClasses ){
        $anno[] $ac = new $anno[annClasses.length];
        for(int i=0;i<annClasses.length; i++){
            $ac[i] = $anno.of(annClasses[i]);
        }
        return or( $ac );
    }

    public static $anno.Or or( _anno... anns ){
        $anno[] $ac = new $anno[anns.length];
        for(int i=0;i<anns.length; i++){
            $ac[i] = $anno.of(anns[i]);
        }
        return or( $ac );
    }

    public static $anno.Or or( $anno...$as ){
        return new Or($as);
    }

    public static $anno as(String...codePattern ){
        return as( _anno.of(codePattern) );
    }

    public static $anno as( _anno _an){
        $anno $a = of( _an);
        //add a constraint to verify there are EXACTLY only the same
        $a.$and( _a-> _an.listKeys().size() == _a.listKeys().size() && _an.listValues().size() == _an.listValues().size());
        return $a;
    }

    /** Default Matching constraint (by default ALWAYS Match)*/
    Predicate<_anno> constraint = a -> true;

    /** the id / name of the annotation */
    $id name;

    /** the member values of the annotation */
    List<$memberValue> $mvs = new ArrayList<>();

    /**
     * 
     * @param name
     * @param mvs
     */
    private $anno( $id name, $memberValue...mvs ){
       this.name = name;
       Arrays.stream(mvs).forEach( mv -> this.$mvs.add(mv));       
    }

    //internal (or) constructor
    protected $anno(){
    }

    /**
     *
     * @param proto
     */
    public $anno(_anno proto) {
        this.name = $id.of(proto.getName());
        AnnotationExpr astAnn = proto.ast();
        if (astAnn instanceof NormalAnnotationExpr) {
            NormalAnnotationExpr na = (NormalAnnotationExpr) astAnn;
            na.getPairs().forEach(mv -> $mvs.add($memberValue.of(mv.getNameAsString(), mv.getValue())));
        } else if (astAnn instanceof SingleMemberAnnotationExpr) {            
            SingleMemberAnnotationExpr sa = (SingleMemberAnnotationExpr) astAnn;
            $mvs.add($memberValue.of(sa.getMemberValue()));
        }
    }

    /**
     *
     * @return
     */
    public $anno $name(){
        this.name = $id.of();
        return this;
    }
    
    /**
     * updates the name of the prototype for matching and constructing
     * @param name
     * @return 
     */
    public $anno $name($id name){
        this.name = name;
        return this;
    }
    
    /**
     * Updates the name prototype for matching and constructing
     * @param name
     * @return 
     */
    public $anno $name(String name){
        this.name = $id.of(name);
        return this;
    }

    /**
     * Adds a constraint to only match against
     * <PRE>
     * marker annotations : @A
     * NOT single value  : @A(1)
     * NOT key/value     : @A(key=1)
     * </PRE>
     * @return the modified
     */
    public $anno $markerAnnotation(){
        return $and(_a -> _a.isMarker() );
    }

    /**
     * Adds a member value to the annotation (A KeyValue pair)
     * i.e.
     * <PRE>
     *     $anno $a = $anno.of("A"); // @A
     *     $a.$memberValue(new $memberValue("Name", "EE") );  //@A("name", "EE")
     * </PRE>
     * @param mv
     * @return 
     */
    public $anno $memberValue( $memberValue mv ){
        this.$mvs.add(mv);
        return this;
    }
    
    /**
     * 
     * @param key
     * @param value
     * @return 
     */
    public $anno $memberValue(String key, Expression value){
        this.$mvs.add( new $memberValue(key, value) );
        return this;
    }
    
    /**
     * 
     * @param key
     * @param value
     * @return 
     */
    public $anno $memberValue(String key, String value){
        this.$mvs.add( new $memberValue(key, value) );
        return this;
    }

    @Override
    public $anno hardcode$(Translator translator, Tokens kvs) {
        this.name = this.name.hardcode$(translator,kvs);

        //this.label = this.label.hardcode$(translator, kvs);
        List<$memberValue> sts = new ArrayList<>();
        this.$mvs.forEach(st -> sts.add( st.hardcode$(translator, kvs)));

        return this;
    }

    /**
     * 
     * @return 
     */
    public boolean isMatchAny(){
        return name.isMatchAny() && ( this.$mvs.isEmpty() || (this.$mvs.size() ==1 && this.$mvs.get(0).isMatchAny() ));
    }
    
    /**
     * ADDS an additional matching constraint to the prototype
     *
     * @param constraint a constraint to be added
     * @return the modified prototype
     */
    public $anno $and(Predicate<_anno> constraint) {
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    public Tokens parse(_anno _a) {
        if( ! this.constraint.test(_a)){
            return null;
        }
        
        Tokens ts = name.parse(_a.getName());
        if( ts == null ){
            //System.out.println( "Parse null for name "+name+" for \""+_a.getName()+"\"");
            return null;
        }
        if ($mvs.isEmpty() ) {
            //System.out.println( "Returning "+ts+" for name \""+_a.getName()+"\"");
            return ts;
        }
        AnnotationExpr astAnn = _a.ast();
        if (astAnn instanceof MarkerAnnotationExpr) {
            //System.out.println( "Marketer");            
            if ($mvs.size() == 1) {
                if ($mvs.get(0).isMatchAny()) {
                    return ts;
                }
                return null;
            }            
            return null;
        }
        if (astAnn instanceof SingleMemberAnnotationExpr) {
            if ($mvs.size() == 1) {
                SingleMemberAnnotationExpr sme = (SingleMemberAnnotationExpr) astAnn;
                Tokens props = $mvs.get(0).parse(sme.getMemberValue());
                if( props == null ){
                    return null;
                }
                if (ts.isConsistent(props)) {                    
                    ts.putAll(props);
                    return ts;
                } 
            }
            return null;
        }
        if (astAnn instanceof NormalAnnotationExpr) {
            NormalAnnotationExpr astNa = (NormalAnnotationExpr) astAnn;
            //System.out.println( "Normal "+ astAnn +" mvs size "+ $mvs.size()+" "+astNa+" "+astNa.getPairs());
            if( astNa.getPairs().isEmpty() ){
                //System.out.println( "Empty parameters ");
                if( $mvs.isEmpty() ){
                    //System.out.println( "returning "+ ts );
                    return ts;
                }
                if( $mvs.size() == 1 && $mvs.get(0).isMatchAny() ){
                    //System.out.println( "returning "+ ts );
                    return ts;
                }
                return null;
            }
            if( $mvs.size() == 0 ){
                return ts;
            }
            
            if( $mvs.size() == 1 && astNa.getPairs().isEmpty() && $mvs.get(0).isMatchAny() ){
                return ts;
            }
            if ($mvs.size() <= astNa.getPairs().size()) {
                //System.out.println( "Checking pairs"+astNa.getPairs());
                List<MemberValuePair> mvpsC = new ArrayList<>();
                mvpsC.addAll(astNa.getPairs());
                for (int i = 0; i < $mvs.size(); i++) {
                    $memberValue.Select sel = $mvs.get(i).selectFirst(mvpsC);
                    if (sel == null) {
                        return null;
                    } else {
                        mvpsC.remove(sel.astMvp); //whenever I find a match, I remove the matcher
                        if( !ts.isConsistent(sel.tokens.asTokens())){
                            return null;
                        }
                        //System.out.println( "    Adding Tokens "+ ts );
                        ts.putAll(sel.tokens.asTokens());
                        //System.out.println( "    Added Tokens "+ ts );
                    }
                }
                return ts;                 
            }
            return null;
        }
        return null;
    }

    public boolean matches( AnnotationExpr astAnno){
        return select( astAnno ) != null;
    }
    
    public boolean matches(String... anno) {
        return matches(_anno.of(anno));
    }

    public boolean matches(_anno _a) {
        return select(_a) != null;
    }

    public String draftToString(Object...values ){
        return draftToString( Translator.DEFAULT_TRANSLATOR, Tokens.of(values ));
    }
    
    public String draftToString(Translator translator, Map<String, Object> keyValues) {
        if( keyValues.get("$anno") != null ){
            //override parameter passed in
            $anno $a = $anno.of( keyValues.get("$anno").toString() );
            Map<String,Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$anno"); //remove to avoid stackOverflow
            return $a.draftToString(translator, kvs);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("@");
        sb.append(name.draft(translator, keyValues));
        String properties = "";
        for (int i = 0; i < $mvs.size(); i++) {
            if ((properties.length() > 0) && (!properties.endsWith(","))) {
                properties = properties + ",";
            }
            properties += $mvs.get(i).compose(translator, keyValues);
        }
        if (properties.length() == 0) {
            return sb.toString();
        }
        return sb.toString() + "(" + properties + ")";
    }
    
    @Override
    public _anno draft(Translator translator, Map<String, Object> keyValues) {
        if( keyValues.get("$anno") != null ){
            //override parameter passed in
            $anno $a = $anno.of( keyValues.get("$anno").toString() );
            Map<String,Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$anno"); //remove to avoid stackOverflow
            return $a.draft(translator, kvs);
        }
        return _anno.of(draftToString(translator, keyValues));
    }

    @Override
    public $anno $(String target, String $paramName) {
        name.$(target, $paramName);
        $mvs.forEach(mv -> mv.key.idStencil = mv.key.idStencil.$(target, $paramName));
        return this;
    }

    public boolean match( Node node ){
        if( node instanceof AnnotationExpr ){
            return matches( (AnnotationExpr)node);
        }
        return false;
    }

    public Select select( AnnotationExpr astAnn){
        return select(_anno.of(astAnn));
    }
    
    public Select select(String... anno){
        return select(_anno.of(anno));
    }
    
    public Select select(_anno _a){
        if(this.constraint.test(_a)){
            Tokens ts = parse(_a);
            if( ts != null ){
                return new Select(_a, ts);
            }
        }
        return null;
    }
    
    @Override
    public List<String> list$() {
        List<String> params = new ArrayList<>();
        params.addAll( this.name.list$() );
        this.$mvs.forEach(m -> {
            params.addAll( m.key.idStencil.list$() );
            params.addAll( m.value.list$() ); 
        });
        return params;
    }

    @Override
    public List<String> list$Normalized() {
        List<String> params = new ArrayList<>();
        params.addAll( this.name.list$() );
        this.$mvs.forEach(m -> {
            params.addAll( m.key.idStencil.list$Normalized() );
            params.addAll( m.value.list$Normalized() ); 
        });
        return params.stream().distinct().collect(Collectors.toList() );
    }

    /**
     * Return a
     * @param clazz
     * @param annoType
     * @return
     */
    public _type replaceIn(Class clazz, Class<? extends Annotation> annoType ){
        return replaceIn(clazz, $anno.of(annoType) );
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param annoType
     * @return 
     */
    public <_J extends _model> _J replaceIn(_J _j, Class<? extends Annotation> annoType ){
        return replaceIn(_j, $anno.of(annoType) );
    }

    /**
     * 
     * @param <N>
     * @param astNode
     * @param annoType
     * @return 
     */
    public <N extends Node> N replaceIn(N astNode, Class<? extends Annotation> annoType ){
        return replaceIn(astNode, $anno.of(annoType));
    }

    @Override
    public _anno firstIn(Node astNode, Predicate<_anno> _annoMatchFn) {
        Optional<Node>on = 
            astNode.stream().filter(
                n -> {
                    if( n instanceof AnnotationExpr){
                        Select sel = select((AnnotationExpr)n); 
                        return ( sel != null && _annoMatchFn.test(sel._ann));
                    } 
                    return false;
                }).findFirst();
        if( on.isPresent() ){
            return _anno.of( (AnnotationExpr)on.get());
        }
        return null;
    }

    /**
     * 
     * @param astNode
     * @return 
     */
    @Override
    public Select selectFirstIn(Node astNode) {
        Optional<Node>on = 
            astNode.stream().filter(
                n -> n instanceof AnnotationExpr 
                    && select((AnnotationExpr)n) != null )
                .findFirst();
        if( on.isPresent() ){
            return select( (AnnotationExpr)on.get());
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
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn( Class clazz, Predicate<Select> selectConstraint ){
        return selectFirstIn((_type)_java.type(clazz), selectConstraint );
    }
    
    /**
     * 
     * @param astNode
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn(Node astNode, Predicate<Select> selectConstraint) {
        Optional<Node>on = 
            astNode.stream().filter(
                n -> {
                    if( n instanceof AnnotationExpr){
                        Select sel = select( (AnnotationExpr)n);
                        return sel != null && selectConstraint.test(sel);
                    }
                    return false;
                }).findFirst();
        
        if( on.isPresent() ){
            return select( (AnnotationExpr)on.get());
        }
        return null;        
    }

    /**
     * 
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn(_model _j, Predicate<Select>selectConstraint) {
        if( _j instanceof _code ){
            _code _c = (_code)_j;
            if( _c.isTopLevel() ){
                return selectFirstIn(_c.astCompilationUnit(), selectConstraint);
            }
            _type _t = (_type)_j; //only possible
            return selectFirstIn(_t.ast(), selectConstraint); //return the TypeDeclaration, not the CompilationUnit            
        }
        return selectFirstIn(((_node)_j).ast(), selectConstraint);
    }
    
    @Override
    public List<Select> listSelectedIn(Node astNode) {
        List<Select> found = new ArrayList<>();
        astNode.walk(AnnotationExpr.class, a-> {
            Select sel = select(_anno.of(a));
            if( sel != null ){
                found.add( sel  );
            }
        });
        return found;
    }

    /**
     * 
     * @param astNode
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(Node astNode, Predicate<Select> selectConstraint) {
        List<Select> found = new ArrayList<>();
        astNode.walk(AnnotationExpr.class, a-> {
            Select sel = select(_anno.of(a));
            if( sel != null && selectConstraint.test(sel)){
                found.add( sel  );
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
        return listSelectedIn((_type)_java.type(clazz), selectConstraint);
    }
    
    /**
     * 
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(_model _j, Predicate<Select> selectConstraint) {
         if( _j instanceof _code ){
            _code _c = (_code)_j;
            if( _c.isTopLevel() ){
                return listSelectedIn(_c.astCompilationUnit(), selectConstraint);
            }
            _type _t = (_type)_j; //only possible
            return listSelectedIn(_t.ast(), selectConstraint); //return the TypeDeclaration, not the CompilationUnit            
        }
        return listSelectedIn(((_node)_j).ast(), selectConstraint);
    }    
    
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_anno> _nodeMatchFn, Consumer<_anno> _nodeActionFn) {
        astNode.walk(AnnotationExpr.class, a-> {
            Select sel = select(_anno.of(a));
            if( sel != null && _nodeMatchFn.test(sel._ann)){
                _nodeActionFn.accept(sel._ann);
            }
        });
        return astNode;
    }

    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Consumer<Select> selectActionFn) {
        astNode.walk(AnnotationExpr.class, a-> {
            Select sel = select(_anno.of(a));
            if( sel != null ){
                selectActionFn.accept(sel);
            }
        });
        return astNode;
    }
    
    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        astRootNode.walk(AnnotationExpr.class, a-> {
            Select sel = select(_anno.of(a));
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
     * @return 
     */
    public <_J extends _model> _J forSelectedIn(_J _j, Consumer<Select> selectActionFn) {
         if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                forSelectedIn(_c.astCompilationUnit(), selectActionFn);
                return _j;
            }
            _type _t = (_type) _j; //only possible
            forSelectedIn(_t.ast(), selectActionFn); //return the TypeDeclaration, not the CompilationUnit            
            return _j;
        }
        forSelectedIn(((_node) _j).ast(), selectActionFn);
        return _j;
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public <_J extends _model> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
         if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                forSelectedIn(_c.astCompilationUnit(), selectConstraint, selectActionFn);
                return _j;
            }
            _type _t = (_type) _j; //only possible
            forSelectedIn(_t.ast(), selectConstraint, selectActionFn); //return the TypeDeclaration, not the CompilationUnit            
            return _j;
        }
        forSelectedIn(((_node) _j).ast(), selectActionFn);
        return _j;
    }
    
    /**
     * 
     * @param clazz
     * @param selectActionFn
     * @return 
     */
    public <_CT extends _type> _CT forSelectedIn(Class clazz, Consumer<Select> selectActionFn) {
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectActionFn);
    }

    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public <_CT extends _type> _CT forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        return (_CT)forSelectedIn((_type)_java.type (clazz), selectConstraint, selectActionFn);
    }
    
    /**
     * builds and returns a toString representation of the $anno
     */
    @Override
    public String toString(){
        if( this.isMatchAny() ){
            return "$anno{ $ANY$ }";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("@");
        sb.append(this.name.idStencil);
        if( this.$mvs.isEmpty() ){
            return "$anno{ "+sb.toString()+" }";
        }
        sb.append("(");
        for(int i=0;i<this.$mvs.size();i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append( $mvs.toString() );            
        }
        sb.append(")");
        sb.append(System.lineSeparator());
        return "$anno{"+System.lineSeparator()+sb.toString()+"}";
    }
    
    /**
     * prototype for member values (i.e. the key values inside the annotation)
     * i.e. @A(key="value")
     */
    public static class $memberValue implements $pattern<MemberValuePair, $memberValue> {

        public $id key = $id.of();


        public $ex value = new $ex(Expression.class, "$value$");

        public Predicate<MemberValuePair> constraint = t -> true;

        public static $memberValue of(Expression value) {
            return new $memberValue("$_$", value);
        }

        public static $memberValue of(String key, String value) {
            return new $memberValue(key, value);
        }

        public String compose(Translator translator, Map<String, Object> keyValues) {
            String k = null;
            if( !key.isMatchAny() ){
                k = key.draft(translator, keyValues);
            }
            Expression v = value.draft(translator, keyValues);
            if (k == null || k.length() == 0) {                
                return v.toString();
            }
            return k + "=" + v;
        }

        public boolean isMatchAny() {
            boolean k = key.isMatchAny();
            boolean v = value.isMatchAny();
            return   k && v;
        }

        public static $memberValue of(String key, Expression exp) {
            return new $memberValue(key, exp);
        }

        public static $memberValue of() {
            return new $memberValue("$key$", "$value$");
        }
        //public static $memberValue any() {
        //    return new $memberValue("$key$", "$value$");
       // }

        public $memberValue(String name, String value) {
            this(name, Ex.of(value));
        }

        public $memberValue(String name, Expression value) {
            this.key.idStencil = Stencil.of(name);
            Stencil st = Stencil.of(value.toString());
            if( st.isMatchAny() ){
                this.value = new $ex(Expression.class, value.toString() );
            } else {
                this.value = $ex.of(value);
            }
        }

        public $memberValue hardcode$( Translator translator, Tokens tokens){
            if( this.key != null ) {
                this.key = this.key.hardcode$(translator, tokens);
            }
            this.value = this.value.hardcode$(translator, tokens);
            return this;
        }

        public $memberValue $and(Predicate<MemberValuePair> mvpMatchFn){
            this.constraint = this.constraint.and(mvpMatchFn);
            return this;
        }

        /**
         *
         * @param target
         * @param $paramName
         * @return
         */
        public $memberValue $(String target, String $paramName) {
            this.key.idStencil = this.key.idStencil.$(target, $paramName);
            this.value = this.value.$(target, $paramName);
            return this;
        }

        /**
         *
         * @param pairs
         * @return
         */
        public Select selectFirst( List<MemberValuePair> pairs ){
            for(int i=0;i<pairs.size();i++){
                Select sel = select(pairs.get(i) );
                if( sel != null ){
                    return sel;
                }
            }            
            return null;
        }

        public boolean match( Node node ){
            if( node instanceof MemberValuePair ){
                return matches( (MemberValuePair) node);
            }
            return false;
        }

        @Override
        public MemberValuePair firstIn(Node astNode, Predicate<MemberValuePair> nodeMatchFn) {
            return _walk.first(astNode, MemberValuePair.class, m -> matches(m) && nodeMatchFn.test(m));
        }

        @Override
        public Select selectFirstIn(Node astNode) {
            MemberValuePair mvp = _walk.first(astNode, MemberValuePair.class, m -> matches(m) );
            if( mvp != null ){
                return select(mvp);
            }
            return null;
        }

        @Override
        public List<Select> listSelectedIn(Node astNode) {
            List<Select> sel = new ArrayList<>();
            _walk.in(astNode,
                    MemberValuePair.class,
                    (MemberValuePair n)-> match(n),
                    (MemberValuePair n) -> sel.add( select(n) )
            );
            return sel;
        }

        @Override
        public <N extends Node> N forEachIn(N astNode, Predicate<MemberValuePair> nodeMatchFn, Consumer<MemberValuePair> nodeActionFn) {
            return _walk.in(astNode,
                    MemberValuePair.class,
                    (MemberValuePair n)-> match(n) && nodeMatchFn.test(n),
                    (MemberValuePair n) -> nodeActionFn.accept(n)
                );
        }

        /**
         * 
         * @param mvp
         * @return 
         */
        public boolean matches(MemberValuePair mvp) {
            return select(mvp) != null;
        }

        /**
         * 
         * @param onlyValueExpression
         * @return 
         */
        public Tokens parse(Expression onlyValueExpression){
            if( constraint.test( new MemberValuePair("_", onlyValueExpression) ) ) {
                //because values can be arrays we dont want to test for direct equality of the
                //expression, but rather whether we can select the expression from the Expression value
                // for example,
                // if I have the value $ex = $ex.of(1);
                // it SHOULD match against Ex.of( "{0,1,2,3}" );
                //System.out.println( value );
                //
                $ex.Select<?>sel = value.selectFirstIn(onlyValueExpression);
                if( sel == null ){
                    return null;
                }
                return sel.tokens().asTokens();
            }
            return null;
        }
        
        /**
         * 
         * @param mvp
         * @return 
         */
        public Tokens parse(MemberValuePair mvp ){
            if (mvp == null) {
                return null;
            }
            if (constraint.test(mvp)) {
                Tokens ts = key.parse(mvp.getNameAsString());
                $ex.Select sel = value.selectFirstIn(mvp.getValue());
                if( sel == null || !ts.isConsistent(sel.tokens.asTokens())){
                    return null;
                }
                ts.putAll(sel.tokens.asTokens());
                return ts;
            }
            return null;
        }
        
        /**
         * When we have an anno like
         * @param onlyValueExpression
         * @return 
         * @count(100)
         * (a SingleMemberAnnotationExpr)
         * 
         */
        public Select select (Expression onlyValueExpression){            
            MemberValuePair mvp = new MemberValuePair("", onlyValueExpression); 
            if( constraint.test( mvp ) ) {
                //because values can be arrays we dont want to test for direct equality of the
                //expression, but rather whether we can select the expression from the Expression value
                // for example,
                // if I have the value $ex = $ex.of(1);
                // it SHOULD match against Ex.of( "{0,1,2,3}" );
                //System.out.println( value );
                //System.out.println( mvp.getValue() );

                $ex.Select sel = value.selectFirstIn(mvp.getValue());

                //$ex.Select sel = value.select(onlyValueExpression);
                if( sel != null ){
                    return new Select(mvp, sel.tokens.asTokens());
                }
            }
            return null;
        }
        
        /**
         * 
         * @param mvp
         * @return 
         */
        public Select select(MemberValuePair mvp) {
            if (mvp == null) {
                return null;
            }
            if (constraint.test(mvp)) {
                Tokens ts = key.parse(mvp.getNameAsString());
                if( ts == null ){
                    return null;
                }
                //because values can be arrays we dont want to test for direct equality of the
                //expression, but rather whether we can select the expression from the Expression value
                // for example,
                // if I have the value $ex = $ex.of(1);
                // it SHOULD match against Ex.of( "{0,1,2,3}" );
                //System.out.println( value );
                //System.out.println( mvp.getValue() );

                $ex.Select sel = value.selectFirstIn(mvp.getValue());
                //$ex.Select sel = value.select(mvp.getValue());
                if( sel == null || !ts.isConsistent(sel.tokens.asTokens())){
                    return null;
                }
                ts.putAll(sel.tokens.asTokens());
                return new Select(mvp, ts);
            }
            return null;
        }

        public static class Select
            implements $pattern.selected, selectAst<MemberValuePair> {

            public final MemberValuePair astMvp;
            public final $tokens tokens;

            public Select(MemberValuePair astMvp, Tokens tokens) {
                this.astMvp = astMvp;
                this.tokens = $tokens.of(tokens);
            }

            @Override
            public $tokens tokens() {
                return tokens;
            }

            @Override
            public String toString() {
                return "$anno.$memberValue.Select {" + System.lineSeparator()
                        + Text.indent(astMvp.toString()) + System.lineSeparator()
                        + Text.indent("$tokens : " + tokens) + System.lineSeparator()
                        + "}";
            }

            /**
             * It's not a "true" member value pair, but rather a synthesized
             * MemberValue Pair with no name... this is for cases where we have an 
             * annotation like 
             * @name("Eric")
             * as apposed to cases where the key and value are spelled out
             * @name(value="Eric")
             * 
             * @return true if its a "value only" member value pair
             */
            public boolean isValueOnly(){
                return astMvp.getNameAsString().equals("_");
            }
            
            @Override
            public MemberValuePair ast() {
                return astMvp;
            }

            public String getName() {
                return astMvp.getNameAsString();
            }

            public Expression getValue() {
                return astMvp.getValue();
            }
        }
    }

    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $anno{

         final List<$anno>ors = new ArrayList<>();

         public Or($anno...$as){
             super();
             Arrays.stream($as).forEach($a -> ors.add($a) );
         }

         @Override
         public List<String> list$(){
             return ors.stream().map( $a ->$a.list$() ).flatMap(Collection::stream).collect(Collectors.toList());
         }

         @Override
         public List<String> list$Normalized(){
             return ors.stream().map( $a ->$a.list$Normalized() ).flatMap(Collection::stream).distinct().collect(Collectors.toList());
         }

         @Override
         public _anno fill(Object...vals){
             throw new _jdraftException("Cannot draft/fill "+getClass()+" pattern"+ this );
         }

         @Override
         public _anno fill(Translator tr, Object...vals){
             throw new _jdraftException("Cannot draft/fill "+getClass()+" pattern"+ this );
         }

         @Override
         public _anno draft(Translator tr, Map<String,Object> map){
              throw new _jdraftException("Cannot draft "+getClass()+" pattern"+ this );
         }

         @Override
         public String draftToString(Object...vals){
             throw new _jdraftException("Cannot draft "+getClass()+" pattern"+ this );
         }

         @Override
         public String draftToString(Translator tr, Map<String,Object> map){
            throw new _jdraftException("Cannot draft "+getClass()+" pattern"+ this );
         }

        @Override
        public $anno hardcode$(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.hardcode$(translator, kvs));
            return this;
        }

         @Override
         public String toString(){
             StringBuilder sb = new StringBuilder();
             sb.append("$anno.Or{");
             sb.append(System.lineSeparator());
             ors.forEach($a -> sb.append( Text.indent($a.toString()) ) );
             sb.append("}");
             return sb.toString();
         }

         /**
          *
          * @param astNode
          * @return
          */
          public $anno.Select select(AnnotationExpr astNode){
              $anno $a = whichMatch(astNode);
              if( $a != null ){
                  return $a.select(astNode);
              }
              return null;
          }

          public boolean isMatchAny(){
              return false;
          }

          public $anno whichMatch(_anno _a){
              return whichMatch(_a.ast());
          }

        /**
         * Return the underlying $anno that matches the AnnotationExpr or null if none of the match
         * @param ae
         * @return
         */
         public $anno whichMatch(AnnotationExpr ae){
             if( !this.constraint.test(_anno.of(ae) ) ){
                 return null;
             }
             Optional<$anno> orsel  = this.ors.stream().filter( $p-> $p.match(ae) ).findFirst();
             if( orsel.isPresent() ){
                 return orsel.get();
             }
             return null;
         }

         public Tokens parse(_anno _a){
             $anno $a = whichMatch(_a);
             if( $a != null) {
                 return $a.parse(_a);
             }
             return null;
         }
    }

    /**
     * A Matched Selection result returned from matching a prototype $a
     * inside of some Node or _node
     */
    public static class Select
        implements $pattern.selected, select_java<_anno>, selectAst<AnnotationExpr> {

        public final _anno _ann;
        public final $tokens tokens;

        public Select(_anno _a, Tokens tokens) {
            this(_a, $tokens.of(tokens));
        }

        public Select(_anno _a, $tokens tokens) {
            this._ann = _a;
            this.tokens = tokens;
        }

        public Select(AnnotationExpr astAnno, $tokens tokens) {
            this._ann = _anno.of( astAnno );
            this.tokens = tokens;
        }

        @Override
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public String toString() {
            return "$anno.Select {" + System.lineSeparator()
                + Text.indent(_ann.toString()) + System.lineSeparator()
                + Text.indent("$tokens : " + tokens) + System.lineSeparator()
                + "}";
        }

        /**
         * Is the name of the selected _anno the name provided
         * @param name
         * @return 
         */
        public boolean isNamed(String name) {
            return _ann.isNamed(name);
        }

        public boolean isSingleValue() {
            return _ann.ast() instanceof SingleMemberAnnotationExpr;
        }

        public boolean hasKeyValues() {
            return _ann.ast() instanceof NormalAnnotationExpr;
        }

        public boolean hasNoValues() {
            return _ann.ast() instanceof MarkerAnnotationExpr;
        }

        @Override
        public AnnotationExpr ast() {
            return _ann.ast();
        }

        @Override
        public _anno _node() {
            return _ann;
        }
    }
}
