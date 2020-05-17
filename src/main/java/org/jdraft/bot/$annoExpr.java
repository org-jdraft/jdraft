package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.expr.*;
import org.jdraft.*;
import org.jdraft.pattern.*;
import org.jdraft.text.*;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * pattern for an annotation expression
 * i.e. @Deprecated, @Generated("12/14/2019")
 *
 * @author Eric
 */
public class $annoExpr
    extends $baseBot<_annoExpr, $annoExpr>
    implements Template<_annoExpr>,
        $bot.$node<AnnotationExpr, _annoExpr, $annoExpr>,
        $selector.$node<_annoExpr, $annoExpr>,
        $constructor.$part, $method.$part,
        $field.$part, $typeParameter.$part, $class.$part, $interface.$part, $enum.$part, $annotation.$part,
        $enumConstant.$part, $type.$part {

    public static $annoExpr of(){
        return new $annoExpr( $name.of() );
    }
    
    public static $annoExpr of($name name, $pair...memberValues ){
        return new $annoExpr(name, memberValues);
    }
    
    public static $annoExpr of(String codePattern) {
        return new $annoExpr(_annoExpr.of(codePattern));
    }
    
    public static $annoExpr of(String... codePattern) {
        return new $annoExpr(_annoExpr.of(codePattern));
    }
    
    public static $annoExpr of(Predicate<_annoExpr> constraint ){
        return of().$and(constraint);
    }
    
    public static $annoExpr of(String codePattern, Predicate<_annoExpr>constraint) {
        return new $annoExpr(_annoExpr.of(codePattern)).$and(constraint);
    }
    
    public static $annoExpr of(_annoExpr _an) {
        return new $annoExpr(_an);
    }

    public static $annoExpr of(_annoExpr _an, Predicate<_annoExpr>constraint) {
        return new $annoExpr(_an).$and(constraint);
    }
        
    public static $annoExpr of(Class<? extends Annotation>sourceAnnoClass) {
        return new $annoExpr(_annoExpr.of(sourceAnnoClass));
    }
    
    public static $annoExpr of (Class<? extends Annotation>sourceAnnoClass, Predicate<_annoExpr> constraint) {
        return of( sourceAnnoClass).$and(constraint);
    }

    /**
     *
     * @param anonymousObjectWithAnnotation
     * @return
     */
    public static $annoExpr of(Object anonymousObjectWithAnnotation ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Exprs.newEx( ste );
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty() ).findFirst().get();
        return of( _annoExpr.of(bd.getAnnotation(0) ) );
    }

    public static $annoExpr.Or or(Class<? extends Annotation>... annClasses ){
        $annoExpr[] $ac = new $annoExpr[annClasses.length];
        for(int i=0;i<annClasses.length; i++){
            $ac[i] = $annoExpr.of(annClasses[i]);
        }
        return or( $ac );
    }

    public static $annoExpr.Or or(_annoExpr... anns ){
        $annoExpr[] $ac = new $annoExpr[anns.length];
        for(int i=0;i<anns.length; i++){
            $ac[i] = $annoExpr.of(anns[i]);
        }
        return or( $ac );
    }

    public static $annoExpr.Or or($annoExpr...$as ){
        return new Or($as);
    }

    public static $annoExpr as(String...codePattern ){
        return as( _annoExpr.of(codePattern) );
    }

    public static $annoExpr as(_annoExpr _an){
        $annoExpr $a = of( _an);
        //add a constraint to verify there are EXACTLY only the same
        $a.$and( _a-> _an.listKeys().size() == _a.listKeys().size() && _an.listValues().size() == _a.listValues().size());
        return $a;
    }

    /** Default Matching constraint (by default ALWAYS Match)*/
    //public Predicate<_annoExpr> predicate = a -> true;

    /** the id / name of the annotation */
    //public $name name;
    public Select.$botSelect<$name, _annoExpr, _name> name =
            Select.$botSelect.of( _annoExpr.class, _name.class, "name", b-> _name.of(b.getNameNode()));


    /** the member values of the annotation */
    public Select.$botListSelect<$pair, _annoExpr, _annoExpr._pair> pairs =
            new Select.$botListSelect(_annoExpr.class, _annoExpr._pair.class, "pairs", _ae-> ((_annoExpr)_ae).listPairs());

    public $annoExpr copy(){
        $annoExpr $copy = of( this.predicate.and(t->true) );
        $copy.name = this.name.copy();
        //$copy.$name( this.name.copy() );
        $copy.pairs = this.pairs.copy(); //$memberValues(this.$mvs.stream().map(mv-> mv.copy()).collect(Collectors.toList()) );
        return $copy;
    }

    /**
     *
     * @param name
     * @param mvs
     */
    private $annoExpr($name name, $pair...mvs ){
       this.name.setBot( name );
       this.pairs.setBotList(Stream.of(mvs).collect(Collectors.toList()));
       //Arrays.stream(mvs).forEach( mv -> this.$mvs.add(mv));
    }

    //internal (or) constructor
    protected $annoExpr(){ }

    /**
     *
     * @param proto
     */
    public $annoExpr(_annoExpr proto) {
        this.name.setBot($name.of(proto.getName()));
        AnnotationExpr astAnn = proto.ast();
        if (astAnn instanceof NormalAnnotationExpr) {
            NormalAnnotationExpr na = (NormalAnnotationExpr) astAnn;

            na.getPairs().forEach(mv -> pairs.add($pair.of(mv.getNameAsString(), mv.getValue())));
        } else if (astAnn instanceof SingleMemberAnnotationExpr) {

            SingleMemberAnnotationExpr sa = (SingleMemberAnnotationExpr) astAnn;
            Stencil st =Stencil.of( sa.getMemberValue().toString() );
            if( st.isMatchAny() ){ //i.e. @A($any$) which matches @A, @A(1), @A(k=1), @A(k=1v=2)...
                pairs.setMatchAll(st.$list().get(0));
            } else {
                pairs.add($pair.of(sa.getMemberValue()));
            }
        }
    }
    /**
     *
     * @return
     */
    public $annoExpr $name(){
        this.name.setBot($name.of());
        return this;
    }

    /**
     * updates the name of the prototype for matching and constructing
     * @param name
     * @return
     */
    public $annoExpr $name($name name){
        this.name.setBot( name);
        return this;
    }

    /**
     * Updates the name prototype for matching and constructing
     * @param name
     * @return
     */
    public $annoExpr $name(String name){
        this.name.setBot($name.of(name));
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

    public $annoExpr $markerAnnotation(){
        return $and(_a -> _a.isMarker() );
    }
    */

    public $annoExpr $pairs(List<$pair> mvs){
        this.pairs.add(mvs.toArray(new $pair[0]));
        return this;
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
    public $annoExpr $pair($pair mv ){
        this.pairs.add(mv);
        return this;
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public $annoExpr $pair(String key, Expression value){
        this.pairs.add( new $pair(key, value) );
        return this;
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public $annoExpr $pair(String key, String value){
        this.pairs.add( new $pair(key, value) );
        return this;
    }


    @Override
    public $annoExpr $hardcode(Translator translator, Tokens kvs) {

        this.name.$hardcode(translator, kvs);

        Object val = kvs.get(this.pairs.getMatchAllName());
        if (val != null) {
            //System.out.println( "HARDCODING "+ this.pairs.getMatchAllName() +" WITH "+ val);
            //I previously had @A($value$), now I'm trying to hardcode value
            this.pairs.add($pair.of(Exprs.of(val.toString())));
            this.pairs.setMatchAll(false);
        }
        this.pairs.$hardcode(translator, kvs);
        return this;
    }

    @Override
    public List<Select.$feature<_annoExpr, ?>> $listSelectors() {
        return Stream.of(this.name, this.pairs).collect(Collectors.toList());
    }

    public Tokens parse(_annoExpr _a) {
        return Select.tokensFrom(_a, this.$listSelectors());

        /*
        //System.out.println("PARSE");
        if( ! this.predicate.test(_a)){
            //System.out.println("PREDICATE FAILED" );
            return null;
        }
        //Tokens ts = Select.tokensFrom(_a, )
        Select ss = name.select(_a.getNameNode());
        if( ss == null ){
            //System.out.println( "Parse null for name "+name+" for \""+_a.getName()+"\"");
            return null;
        }
        //System.out.println("NAME PASSED" );
        Tokens ts = ss.tokens; //name.parse(_a.getName());

        Tokens mvs = this.pairs.apply(_a);
        if( ts.isConsistent(mvs)){
            mvs.putAll(ts);
            return ts;
        }
        return null;
        /*
        if ($mvs.isEmpty() ) {
            //System.out.println( "Returning "+ts+" for name \""+_a.getName()+"\"");
            return ts;
        }

        if( $mvs.size() == 1 ){
            if($mvs.get(0).isMatchAny() || $mvs.get(0).key.matches("value") && $mvs.get(0).value.isMatchAny()){
                ts.add( $mvs.get(0).value.$list().get(0), _a.getKeyValuesMap() );
                return ts;
            }
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
            //System.out.println( "SINGLE MEMBER");
            if ($mvs.size() == 1) {
                if( !$mvs.get(0).key.matches("value") ){
                    //System.out.println(">>>>>>>>>>>>SHOULDNT MATCH");
                    return null;
                }
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
                //System.out.println( "Is match Any Pairs");
                return ts;
            }
            if ($mvs.size() <= astNa.getPairs().size()) {
                //System.out.println( "Checking pairs"+astNa.getPairs());
                List<_annoExpr._memberValue> mvpsC = new ArrayList<>();
                astNa.getPairs().forEach(m -> mvpsC.add(_annoExpr._memberValue.of(m)));
                for (int i = 0; i < $mvs.size(); i++) {
                    Select sel = $mvs.get(i).selectFirst(mvpsC);
                    if (sel == null) {
                        return null;
                    } else {
                        mvpsC.remove(sel.selection); //whenever I find a match, I remove the matcher
                        if( !ts.isConsistent(sel.tokens)){
                            return null;
                        }
                        //System.out.println( "    Adding Tokens "+ ts );
                        ts.putAll(sel.tokens);
                        //System.out.println( "    Added Tokens "+ ts );
                    }
                }
                return ts;
            }
            return null;
        }

        return null;
         */
    }

    public boolean matches( AnnotationExpr astAnno){
        return select( astAnno ) != null;
    }

    public boolean matches(String... anno) {
        return matches(_annoExpr.of(anno));
    }

    public boolean matches(_annoExpr _a) {
        return select(_a) != null;
    }

    public String draftToString(Object...values ){
        return draftToString( Translator.DEFAULT_TRANSLATOR, Tokens.of(values ));
    }

    public String draftToString(Translator translator, Map<String, Object> keyValues) {
        if( keyValues.get("$anno") != null ){
            //override parameter passed in
            $annoExpr $a = $annoExpr.of( keyValues.get("$anno").toString() );
            Map<String,Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$anno"); //remove to avoid stackOverflow
            return $a.draftToString(translator, kvs);
        }
        _annoExpr _a = _annoExpr.of();
        _a.setName(name.draft(translator, keyValues).toString());
        _a.setPairs(pairs.draft(translator, keyValues));
        return _a.toString();
        /*
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
         */
    }

    @Override
    public _annoExpr draft(Translator translator, Map<String, Object> keyValues) {
        if( keyValues.get("$anno") != null ){
            //override parameter passed in
            $annoExpr $a = $annoExpr.of( keyValues.get("$anno").toString() );
            Map<String,Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$anno"); //remove to avoid stackOverflow
            return $a.draft(translator, kvs);
        }
        return _annoExpr.of(draftToString(translator, keyValues));
    }

    @Override
    public $annoExpr $(String target, String $paramName) {
        name.$(target, $paramName);
        pairs.$(target, $paramName);
        //$mvs.forEach(mv -> mv.key.stencil = mv.key.stencil.$(target, $paramName));
        return this;
    }

    public boolean match( Node node ){
        if( node instanceof AnnotationExpr ){
            return matches( (AnnotationExpr)node);
        }
        return false;
    }

    public Select<_annoExpr> select(String str){
        try{
            return select( Ast.anno(str));
        }catch(Exception e) {
            return null;
        }
    }
    public Select<_annoExpr> select(Node n){
        if( n instanceof AnnotationExpr){
            return select( (AnnotationExpr)n);
        }
        return null;
    }

    public Select<_annoExpr> select(AnnotationExpr astAnn){
        return select(_annoExpr.of(astAnn));
    }

    public Select<_annoExpr> select(String... anno){
        return select(_annoExpr.of(anno));
    }

    public Select<_annoExpr> select(_annoExpr _a){
        //System.out.println( "IN SELECT "+ _a);
        if(this.predicate.test(_a)){
            Tokens ts = parse(_a);
            if( ts != null ){
                return new Select(_a, ts);
            }
        }
        return null;
    }

    @Override
    public List<String> $list() {
        List<String> params = new ArrayList<>();
        params.addAll( this.name.$list() );
        params.addAll( this.pairs.$list() );
        /*
        this.$mvs.forEach(m -> {
            params.addAll( m.key.$list() );
            params.addAll( m.value.$list() );
        });
         */
        return params;
    }

    @Override
    public List<String> $listNormalized() {
        List<String> params = new ArrayList<>();
        params.addAll( this.name.$listNormalized() );
        params.addAll( this.pairs.$listNormalized());
        /*
        this.$mvs.forEach(m -> {
            params.addAll( m.key.stencil.$listNormalized() );
            params.addAll( m.value.$listNormalized() );
        });
         */
        return params.stream().distinct().collect(Collectors.toList() );
    }

    /**
     * Return a
     * @param clazz
     * @param annoType
     * @return
     */
    public _type replaceIn(Class clazz, Class<? extends Annotation> annoType ){
        return replaceIn(clazz, $annoExpr.of(annoType) );
    }

    /**
     *
     * @param <_J>
     * @param _j
     * @param annoType
     * @return
     */
    public <_J extends _java._node> _J replaceIn(_J _j, Class<? extends Annotation> annoType ){
        return (_J)replaceIn(_j, (Template) $annoExpr.of(annoType) );
    }

    /**
     *
     * @param <N>
     * @param astNode
     * @param annoType
     * @return
     */
    public <N extends Node> N replaceIn(N astNode, Class<? extends Annotation> annoType ){
        return replaceIn(astNode, $annoExpr.of(annoType));
    }

    /**
     * builds and returns a toString representation of the $anno
     */
    @Override
    public String toString(){
        if( this.isMatchAny() ){
            return "$anno{ [MATCH ANY] }";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("@");
        sb.append(this.name.getBot().stencil);
        if( this.pairs.botList.isEmpty() ){
            return "$anno{ "+sb.toString()+" }";
        }
        sb.append("( " +this.pairs.toString() + ")");
        //if( this.$mvs.isEmpty() ){

        //}
        /*
        sb.append("(");
        for(int i=0;i<this.$mvs.size();i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append( $mvs.toString() );
        }
        sb.append(")");
        sb.append(System.lineSeparator());
         */
        return "$anno{"+System.lineSeparator()+sb.toString()+"}";
    }

    /**
     * prototype for member values (i.e. the key values inside the annotation)
     * i.e. @A(key="value")
     */
    public static class $pair implements $bot<MemberValuePair, _annoExpr._pair, $pair> {

        public $name key = $name.of();

        public $expr value = $e.of(); //new $ex(Expression.class, "$value$");

        public Predicate<_annoExpr._pair> constraint = t -> true;

        public static $pair of(_annoExpr._pair _mv){
            return new $pair( _mv.getName(), _mv.getValue().ast());
        }

        public static $pair of(Predicate<_annoExpr._pair> matchFn){
            $pair $mv = new $pair( );
            return $mv.$and(matchFn);
        }

        public static $pair of(Expression value) {
            //return new $memberValue("$_$", value);
            return new $pair("value", value);
        }

        public static $pair of(String key, String value) {
            return new $pair(key, value);
        }

        public static $pair of(String key, Expression exp) {
            return new $pair(key, exp);
        }

        public static $pair of() {
            return new $pair("$key$", "$value$");
        }

        public $pair copy(){
            $pair $mv = of();
            $mv.key = key.copy();
            $mv.value = (($e)value).copy();
            return $mv;
        }

        @Override
        public Select<_annoExpr._pair> select(Node n) {
            if( n instanceof MemberValuePair ){
                return select( (MemberValuePair)n);
            }
            return null;
        }

        public String compose(Translator translator, Map<String, Object> keyValues) {
            String k = null;
            if( !key.isMatchAny() ){
                k = key.draft(translator, keyValues).toString();
            }
            Expression v = ((_expr)value.draft(translator, keyValues)).ast();
            if (k == null || k.length() == 0) {
                return v.toString();
            }
            return k + "=" + v;
        }

        public boolean isMatchAny() {
            boolean k = key.isMatchAny();
            boolean v = value.isMatchAny();
            return  k && v;
        }

        private $pair(){ }

        public $pair(String name, String value) {
            this(name, Exprs.of(value));
        }

        public $pair(String name, Expression value) {
            this.key.stencil = Stencil.of(name);
            Stencil st = Stencil.of(value.toString());
            if( st.isMatchAny() ){
                this.value = $e.of(st);
            } else {
                this.value = $expr.of(value);
            }
        }

        public $pair $hardcode(Translator translator, Tokens tokens){
            if( this.key != null ) {
                this.key = this.key.$hardcode(translator, tokens);
            }
            //m.out.println("VALUE B4"+this.value+ tokens);
            this.value.$hardcode(translator, tokens);
            //System.out.println("VALUE After"+this.value+tokens);
            return this;
        }

        public $pair $and(Predicate<_annoExpr._pair> mvpMatchFn){
            this.constraint = this.constraint.and(mvpMatchFn);
            return this;
        }

        @Override
        public _annoExpr._pair draft(Translator translator, Map<String, Object> keyValues) {
            _expr _ex = (_expr)this.value.draft(translator, keyValues);
            String key = this.key.draft(translator, keyValues).toString();
            return _annoExpr._pair.of( new MemberValuePair(key, _ex.ast()));
        }

        /**
         *
         * @param target
         * @param $paramName
         * @return
         */
        public $pair $(String target, String $paramName) {
            this.key.stencil = this.key.stencil.$(target, $paramName);
            this.value.$(target, $paramName);
            return this;
        }

        @Override
        public List<String> $list() {
            List<String> strs = new ArrayList<>();
            strs.addAll(this.key.$list());
            strs.addAll(this.value.$list());
            return strs;
        }

        @Override
        public List<String> $listNormalized() {
            List<String> strs = new ArrayList<>();
            strs.addAll(this.key.$listNormalized());
            strs.addAll(this.value.$listNormalized());
            return strs.stream().distinct().collect(Collectors.toList());
        }

        /**
         *
         * @param pairs
         * @return
         */
        public Select selectFirst( List<_annoExpr._pair> pairs ){
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
        public _annoExpr._pair firstIn(Node astNode, Predicate<_annoExpr._pair> nodeMatchFn) {
            Node mvp = Tree.first(astNode, MemberValuePair.class, m -> select(_annoExpr._pair.of(m) ) != null);
            if( mvp != null ){
                return _annoExpr._pair.of( (MemberValuePair)mvp);
            }
            return null;
        }

        @Override
        public Select selectFirstIn(Node astNode) {
            MemberValuePair mvp = Tree.first(astNode, MemberValuePair.class, m -> matches(m) );
            if( mvp != null ){
                return select(mvp);
            }
            return null;
        }

        @Override
        public Select<_annoExpr._pair> selectFirstIn(Node astNode, Predicate<Select<_annoExpr._pair>> predicate) {
            Optional<Node> on = astNode.stream().filter( n-> {
                if( n instanceof MemberValuePair){
                    _annoExpr._pair _mv = _annoExpr._pair.of( (MemberValuePair)n);
                    Select<_annoExpr._pair> smv = select(_mv);
                    if( smv != null && predicate.test(smv)){
                        return true;
                    }
                }
                return false;
            }).findFirst();
            if( on.isPresent() ){
                return select( (MemberValuePair)on.get() );
            }
            return null;
        }

        @Override
        public List<Select<_annoExpr._pair>> listSelectedIn(Node astNode) {
            List<Select<_annoExpr._pair>> sel = new ArrayList<>();
            Tree.in(astNode,
                    MemberValuePair.class,
                    (MemberValuePair n)-> match(n),
                    (MemberValuePair n) -> sel.add( select(n) )
            );
            return  sel;
        }

        @Override
        public <N extends Node> N forEachIn(N astNode, Predicate<_annoExpr._pair> nodeMatchFn, Consumer<_annoExpr._pair> nodeActionFn) {
            return Tree.in(astNode,
                    MemberValuePair.class,
                    (MemberValuePair n)-> match(n) && nodeMatchFn.test(_annoExpr._pair.of(n)),
                    (MemberValuePair n) -> nodeActionFn.accept(_annoExpr._pair.of(n))
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
            if( constraint.test( _annoExpr._pair.of( onlyValueExpression.toString()) ) ) {
                //because values can be arrays we dont want to test for direct equality of the
                //expression, but rather whether we can select the expression from the Expression value
                // for example,
                // if I have the value $ex = $ex.of(1);
                // it SHOULD match against Ex.of( "{0,1,2,3}" );
                //System.out.println( value );
                //
                Select sel = value.selectFirstIn(onlyValueExpression);
                if( sel == null ){
                    return null;
                }
                return sel.tokens;
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
            //System.out.println( "Selecting Only Value");
            MemberValuePair mvp = new MemberValuePair("", onlyValueExpression);
            if( constraint.test( _annoExpr._pair.of(mvp) ) ) {
                //because values can be arrays we dont want to test for direct equality of the
                //expression, but rather whether we can select the expression from the Expression value
                // for example,
                // if I have the value $ex = $ex.of(1);
                // it SHOULD match against Ex.of( "{0,1,2,3}" );
                //System.out.println( value );
                //System.out.println( mvp.getValue() );

                Select sel = value.selectFirstIn(mvp.getValue());

                //$ex.Select sel = value.select(onlyValueExpression);
                if( sel != null ){
                    return new Select(mvp, sel.tokens);
                }
            }
            return null;
        }

        /**
         *
         * @param mvp
         * @return
         */
        public Select<_annoExpr._pair> select(MemberValuePair mvp) {
            return select(_annoExpr._pair.of(mvp));
        }

        @Override
        public Predicate<_annoExpr._pair> getPredicate() {
            return this.constraint;
        }

        @Override
        public $pair setPredicate(Predicate<_annoExpr._pair> predicate) {
            this.constraint = predicate;
            return this;
        }

        public Select<_annoExpr._pair> select(_annoExpr._pair _mvp){
            if (_mvp == null) {
                //System.out.println("MVP NULL");
                return null;
            }
            //System.out.println("Checking constraint");
            if (constraint.test(_mvp)) {

                if( _mvp.isValueOnly() ){
                    //System.out.println("valueOnly");
                    if( !this.key.isMatchAny() ){
                        //System.out.println("NOT Match any key ");
                        Select s = this.key.select("value");
                        if( s == null ) {
                            //System.out.println("Not seelcted value  ");
                            return null;
                        }
                    } else{
                        //System.out.println("Match any key ");
                    }
                }
                Select ss = key.select(_mvp.getNameNode());
                if( ss == null){
                    return null;
                } else{
                    //System.out.println( key.stencil+" selected "+ _mvp.getNameNode() );
                }
                //$selector.Select ts = key.select(mvp.get)
                //Tokens ts = key.parse(mvp.getNameAsString());
                //if( ts == null ){
                //    return null;
               // }
                //because values can be arrays we dont want to test for direct equality of the
                //expression, but rather whether we can select the expression from the Expression value
                // for example,
                // if I have the value $ex = $ex.of(1);
                // it SHOULD match against Ex.of( "{0,1,2,3}" );
                //System.out.println( value );
                //System.out.println( mvp.getValue() );

                Select sel = value.selectFirstIn(_mvp.getValue());
                //$ex.Select sel = value.select(mvp.getValue());
                if( sel == null || !ss.tokens.isConsistent(sel.tokens)){
                    return null;
                }
                Tokens tts = new Tokens();
                tts.putAll(ss.tokens);
                tts.putAll(sel.tokens);
                return new Select(_mvp, tts);
            }
            return null;
        }

        @Override
        public boolean matches(String candidate) {
            return matches(_annoExpr._pair.of(candidate));
        }
    }

    /**
     * An Or entity that can match against any of the $bot instances provided
     * NOTE: template features (draft/fill) are suppressed.
     */
    public static class Or extends $annoExpr {

         final List<$annoExpr> $annoExprBots = new ArrayList<>();

         public Or($annoExpr...$as){
             super();
             this.name.setBot(null);
             this.pairs = new Select.$botListSelect(_annoExpr.class, _annoExpr._pair.class, "pairs", _ae-> ((_annoExpr)_ae).listPairs());
             Arrays.stream($as).forEach($a -> $annoExprBots.add($a) );
         }

        /**
         * Build and return a copy of this or bot
         * @return
         */
        public Or copy(){
             List<$annoExpr> copyBots = new ArrayList<>();
             this.$annoExprBots.forEach(a-> copyBots.add(a.copy()));
             Or theCopy = new Or( copyBots.toArray(new $annoExpr[0]) );

             //now copy the predicate and all underlying bots on the baseBot
             theCopy.predicate = this.predicate.and(t->true);
             theCopy.name = this.name.copy();
             theCopy.pairs = this.pairs.copy();
             return theCopy;
         }

        public List<String> $list(){
            List<String> list = new ArrayList<>();
            list.addAll( super.$list());
            this.$annoExprBots.forEach($ar -> list.addAll( $ar.$list()));
            return list;
        }

        public List<String> $listNormalized(){
            List<String> list = new ArrayList<>();
            list.addAll( super.$listNormalized());
            this.$annoExprBots.forEach($ar -> list.addAll( $ar.$listNormalized()));
            return list.stream().distinct().collect(Collectors.toList());
        }

         @Override
         public _annoExpr fill(Object...vals){
             throw new _jdraftException("Cannot draft/fill "+getClass()+" pattern"+ this );
         }

         @Override
         public _annoExpr fill(Translator tr, Object...vals){
             throw new _jdraftException("Cannot draft/fill "+getClass()+" pattern"+ this );
         }

         @Override
         public _annoExpr draft(Translator tr, Map<String,Object> map){
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
         public String toString(){
             StringBuilder sb = new StringBuilder();
             sb.append("$annoExpr.Or{");
             sb.append(System.lineSeparator());
             $annoExprBots.forEach($a -> sb.append( Text.indent($a.toString()) ) );
             sb.append("}");
             return sb.toString();
         }

        /**
         *
         * @param _a
         * @return
         */
        public Select<_annoExpr> select(_annoExpr _a){
            Select commonSelect = super.select(_a);
            //System.out.println("COMMON SELECT " + commonSelect );
            if(  commonSelect == null){
                //System.out.println( "Failed common select");
                return null;
            }
            $annoExpr $whichBot = whichMatch(_a);
            if( $whichBot == null ){
                //System.out.println( "NO whichMatch");
                return null;
            }
            Select whichSelect = $whichBot.select(_a);
            if( !commonSelect.tokens.isConsistent(whichSelect.tokens)){
                //System.out.println( "NOT consistent" + whichSelect.tokens);
                 return null;
            }
            whichSelect.tokens.putAll(commonSelect.tokens);
            return whichSelect;
        }

         public boolean isMatchAny(){
              return false;
          }

         public List<$annoExpr> $listOrSelectors() {
            return this.$annoExprBots;
         }

         public $annoExpr.Or $hardcode(Translator tr, Tokens ts){
            super.$hardcode(tr, ts);
            this.$annoExprBots.forEach($ar -> $ar.$hardcode(tr, ts));
            return this;
         }


        public $annoExpr whichMatch(_annoExpr _a){
              return whichMatch(_a.ast());
          }

         /**
          * Return the underlying $anno that matches the AnnotationExpr or null if none of the match
          * @param ae
          * @return
          */
         public $annoExpr whichMatch(AnnotationExpr ae){
             if( !this.predicate.test(_annoExpr.of(ae) ) ){
                 return null;
             }
             Optional<$annoExpr> orsel  = this.$annoExprBots.stream().filter($p-> $p.match(ae) ).findFirst();
             if( orsel.isPresent() ){
                 return orsel.get();
             }
             return null;
         }

         public Tokens parse(_annoExpr _a){
             $annoExpr $a = whichMatch(_a);
             if( $a != null) {
                 return $a.parse(_a);
             }
             return null;
         }
    }
}
