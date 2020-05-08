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

/**
 * pattern for an annotation expression
 * i.e. @Deprecated, @Generated("12/14/2019")
 *
 *
 * @author Eric
 */
public class $annoRef
    implements Template<_annoRef>,
        $bot.$node<AnnotationExpr, _annoRef, $annoRef>,
        $selector.$node<_annoRef, $annoRef>,
        $constructor.$part, $method.$part,
        $field.$part, $typeParameter.$part, $class.$part, $interface.$part, $enum.$part, $annotation.$part,
        $enumConstant.$part, $type.$part {

    /**
     * Returns the Ast node implementation type that is used to identify the types as walking the AST
     * @return the type that this $proto intercepts
     */
    public Class<AnnotationExpr> astType(){
        return AnnotationExpr.class;
    }

    public static $annoRef of(){
        return new $annoRef( $name.of() );
    }
    
    public static $annoRef of($name name, $memberValue...memberValues ){
        return new $annoRef(name, memberValues);
    }
    
    public static $annoRef of(String codePattern) {
        return new $annoRef(_annoRef.of(codePattern));
    }
    
    public static $annoRef of(String... codePattern) {
        return new $annoRef(_annoRef.of(codePattern));
    }
    
    public static $annoRef of(Predicate<_annoRef> constraint ){
        return of().$and(constraint);
    }
    
    public static $annoRef of(String codePattern, Predicate<_annoRef>constraint) {
        return new $annoRef(_annoRef.of(codePattern)).$and(constraint);
    }
    
    public static $annoRef of(_annoRef _an) {
        return new $annoRef(_an);
    }

    public static $annoRef of(_annoRef _an, Predicate<_annoRef>constraint) {
        return new $annoRef(_an).$and(constraint);
    }
        
    public static $annoRef of(Class<? extends Annotation>sourceAnnoClass) {
        return new $annoRef(_annoRef.of(sourceAnnoClass));
    }
    
    public static $annoRef of (Class<? extends Annotation>sourceAnnoClass, Predicate<_annoRef> constraint) {
        return of( sourceAnnoClass).$and(constraint);
    }

    /**
     *
     * @param anonymousObjectWithAnnotation
     * @return
     */
    public static $annoRef of(Object anonymousObjectWithAnnotation ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expressions.newEx( ste );
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty() ).findFirst().get();
        return of( _annoRef.of(bd.getAnnotation(0) ) );
    }

    public static $annoRef.Or or(Class<? extends Annotation>... annClasses ){
        $annoRef[] $ac = new $annoRef[annClasses.length];
        for(int i=0;i<annClasses.length; i++){
            $ac[i] = $annoRef.of(annClasses[i]);
        }
        return or( $ac );
    }

    public static $annoRef.Or or(_annoRef... anns ){
        $annoRef[] $ac = new $annoRef[anns.length];
        for(int i=0;i<anns.length; i++){
            $ac[i] = $annoRef.of(anns[i]);
        }
        return or( $ac );
    }

    public static $annoRef.Or or($annoRef...$as ){
        return new Or($as);
    }

    public static $annoRef as(String...codePattern ){
        return as( _annoRef.of(codePattern) );
    }

    public static $annoRef as(_annoRef _an){
        $annoRef $a = of( _an);
        //add a constraint to verify there are EXACTLY only the same
        $a.$and( _a-> _an.listKeys().size() == _a.listKeys().size() && _an.listValues().size() == _an.listValues().size());
        return $a;
    }

    /** Default Matching constraint (by default ALWAYS Match)*/
    Predicate<_annoRef> predicate = a -> true;

    /** the id / name of the annotation */
    $name name;

    /** the member values of the annotation */
    List<$memberValue> $mvs = new ArrayList<>();

    public $annoRef copy(){
        return of( this.predicate.and(t->true) )
                .$name( this.name.copy() )
                .$memberValues(this.$mvs.stream().map(mv-> mv.copy()).collect(Collectors.toList()) );
    }

    /**
     *
     * @param name
     * @param mvs
     */
    private $annoRef($name name, $memberValue...mvs ){
       this.name = name;
       Arrays.stream(mvs).forEach( mv -> this.$mvs.add(mv));
    }

    //internal (or) constructor
    protected $annoRef(){ }

    /**
     *
     * @param proto
     */
    public $annoRef(_annoRef proto) {
        this.name = $name.of(proto.getName());
        AnnotationExpr astAnn = proto.ast();
        if (astAnn instanceof NormalAnnotationExpr) {
            NormalAnnotationExpr na = (NormalAnnotationExpr) astAnn;
            na.getPairs().forEach(mv -> $mvs.add($memberValue.of(mv.getNameAsString(), mv.getValue())));
        } else if (astAnn instanceof SingleMemberAnnotationExpr) {
            SingleMemberAnnotationExpr sa = (SingleMemberAnnotationExpr) astAnn;
            $mvs.add($memberValue.of(sa.getMemberValue()));
        }
    }

    public $annoRef setPredicate(Predicate<_annoRef> predicate){
        this.predicate = predicate;
        return this;
    }

    public Predicate<_annoRef> getPredicate(){
        return this.predicate;
    }

    /**
     *
     * @return
     */
    public $annoRef $name(){
        this.name = $name.of();
        return this;
    }

    /**
     * updates the name of the prototype for matching and constructing
     * @param name
     * @return
     */
    public $annoRef $name($name name){
        this.name = name;
        return this;
    }

    /**
     * Updates the name prototype for matching and constructing
     * @param name
     * @return
     */
    public $annoRef $name(String name){
        this.name = $name.of(name);
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
    public $annoRef $markerAnnotation(){
        return $and(_a -> _a.isMarker() );
    }

    public $annoRef $memberValues(List<$memberValue> mvs){
        this.$mvs.addAll(mvs);
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
    public $annoRef $memberValue($memberValue mv ){
        this.$mvs.add(mv);
        return this;
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public $annoRef $memberValue(String key, Expression value){
        this.$mvs.add( new $memberValue(key, value) );
        return this;
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public $annoRef $memberValue(String key, String value){
        this.$mvs.add( new $memberValue(key, value) );
        return this;
    }

    @Override
    public $annoRef $hardcode(Translator translator, Tokens kvs) {
        this.name = this.name.$hardcode(translator,kvs);

        List<$memberValue> sts = new ArrayList<>();
        this.$mvs.forEach(st -> sts.add( st.$hardcode(translator, kvs)));
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
    public $annoRef $and(Predicate<_annoRef> constraint) {
        this.predicate = this.predicate.and(constraint);
        return this;
    }

    public $annoRef $not(Predicate<_annoRef> constraint) {
        return $and( constraint.negate());
    }

    public Tokens parse(_annoRef _a) {
        if( ! this.predicate.test(_a)){
            return null;
        }

        Select ss = name.select(_a.getNameNode());
        if( ss == null ){
            //System.out.println( "Parse null for name "+name+" for \""+_a.getName()+"\"");
            return null;
        }
        Tokens ts = ss.tokens; //name.parse(_a.getName());

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
                List<_annoRef._memberValue> mvpsC = new ArrayList<>();
                astNa.getPairs().forEach(m -> mvpsC.add(_annoRef._memberValue.of(m)));
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
    }

    public boolean matches( AnnotationExpr astAnno){
        return select( astAnno ) != null;
    }

    public boolean matches(String... anno) {
        return matches(_annoRef.of(anno));
    }

    public boolean matches(_annoRef _a) {
        return select(_a) != null;
    }

    public String draftToString(Object...values ){
        return draftToString( Translator.DEFAULT_TRANSLATOR, Tokens.of(values ));
    }

    public String draftToString(Translator translator, Map<String, Object> keyValues) {
        if( keyValues.get("$anno") != null ){
            //override parameter passed in
            $annoRef $a = $annoRef.of( keyValues.get("$anno").toString() );
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
    public _annoRef draft(Translator translator, Map<String, Object> keyValues) {
        if( keyValues.get("$anno") != null ){
            //override parameter passed in
            $annoRef $a = $annoRef.of( keyValues.get("$anno").toString() );
            Map<String,Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$anno"); //remove to avoid stackOverflow
            return $a.draft(translator, kvs);
        }
        return _annoRef.of(draftToString(translator, keyValues));
    }

    @Override
    public $annoRef $(String target, String $paramName) {
        name.$(target, $paramName);
        $mvs.forEach(mv -> mv.key.stencil = mv.key.stencil.$(target, $paramName));
        return this;
    }

    public boolean match( Node node ){
        if( node instanceof AnnotationExpr ){
            return matches( (AnnotationExpr)node);
        }
        return false;
    }

    public Select<_annoRef> select(String str){
        try{
            return select( Ast.anno(str));
        }catch(Exception e) {
            return null;
        }
    }
    public Select<_annoRef> select(Node n){
        if( n instanceof AnnotationExpr){
            return select( (AnnotationExpr)n);
        }
        return null;
    }

    public Select<_annoRef> select(AnnotationExpr astAnn){
        return select(_annoRef.of(astAnn));
    }

    public Select<_annoRef> select(String... anno){
        return select(_annoRef.of(anno));
    }

    public Select<_annoRef> select(_annoRef _a){
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
        this.$mvs.forEach(m -> {
            params.addAll( m.key.$list() );
            params.addAll( m.value.$list() );
        });
        return params;
    }

    @Override
    public List<String> $listNormalized() {
        List<String> params = new ArrayList<>();
        params.addAll( this.name.$listNormalized() );
        this.$mvs.forEach(m -> {
            params.addAll( m.key.stencil.$listNormalized() );
            params.addAll( m.value.$listNormalized() );
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
        return replaceIn(clazz, $annoRef.of(annoType) );
    }

    /**
     *
     * @param <_J>
     * @param _j
     * @param annoType
     * @return
     */
    public <_J extends _java._node> _J replaceIn(_J _j, Class<? extends Annotation> annoType ){
        return (_J)replaceIn(_j, (Template) $annoRef.of(annoType) );
    }

    /**
     *
     * @param <N>
     * @param astNode
     * @param annoType
     * @return
     */
    public <N extends Node> N replaceIn(N astNode, Class<? extends Annotation> annoType ){
        return replaceIn(astNode, $annoRef.of(annoType));
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
        sb.append(this.name.stencil);
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
    public static class $memberValue implements $bot<MemberValuePair, _annoRef._memberValue, $memberValue> {

        public $name key = $name.of();

        public $expression value = $e.of(); //new $ex(Expression.class, "$value$");

        public Predicate<_annoRef._memberValue> constraint = t -> true;

        public static $memberValue of(_annoRef._memberValue _mv){
            return new $memberValue( _mv.getName(), _mv.getValue().ast());
        }

        public static $memberValue of(Predicate<_annoRef._memberValue> matchFn){
            $memberValue $mv = new $memberValue( );
            return $mv.$and(matchFn);
        }

        public static $memberValue of(Expression value) {
            return new $memberValue("$_$", value);
        }

        public static $memberValue of(String key, String value) {
            return new $memberValue(key, value);
        }

        public static $memberValue of(String key, Expression exp) {
            return new $memberValue(key, exp);
        }

        public static $memberValue of() {
            return new $memberValue("$key$", "$value$");
        }

        public $memberValue copy(){
            $memberValue $mv = of();
            $mv.key = key.copy();
            $mv.value = (($e)value).copy();
            return $mv;
        }

        @Override
        public Select<_annoRef._memberValue> select(Node n) {
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
            Expression v = ((_expression)value.draft(translator, keyValues)).ast();
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

        private $memberValue(){ }

        public $memberValue(String name, String value) {
            this(name, Expressions.of(value));
        }

        public $memberValue(String name, Expression value) {
            this.key.stencil = Stencil.of(name);
            Stencil st = Stencil.of(value.toString());
            if( st.isMatchAny() ){
                this.value = $e.of(st);
            } else {
                this.value = $expression.of(value);
            }
        }

        public $memberValue $hardcode(Translator translator, Tokens tokens){
            if( this.key != null ) {
                this.key = this.key.$hardcode(translator, tokens);
            }
            this.value.$hardcode(translator, tokens);
            return this;
        }

        public $memberValue $and(Predicate<_annoRef._memberValue> mvpMatchFn){
            this.constraint = this.constraint.and(mvpMatchFn);
            return this;
        }

        @Override
        public _annoRef._memberValue draft(Translator translator, Map<String, Object> keyValues) {
            return null;
        }

        /**
         *
         * @param target
         * @param $paramName
         * @return
         */
        public $memberValue $(String target, String $paramName) {
            this.key.stencil = this.key.stencil.$(target, $paramName);
            this.value.$(target, $paramName);
            return this;
        }

        @Override
        public List<String> $list() {
            return null;
        }

        @Override
        public List<String> $listNormalized() {
            return null;
        }

        /**
         *
         * @param pairs
         * @return
         */
        public Select selectFirst( List<_annoRef._memberValue> pairs ){
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
        public _annoRef._memberValue firstIn(Node astNode, Predicate<_annoRef._memberValue> nodeMatchFn) {
            Node mvp = Tree.first(astNode, MemberValuePair.class, m -> select(_annoRef._memberValue.of(m) ) != null);
            if( mvp != null ){
                return _annoRef._memberValue.of( (MemberValuePair)mvp);
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
        public Select<_annoRef._memberValue> selectFirstIn(Node astNode, Predicate<Select<_annoRef._memberValue>> predicate) {
            return null;
        }

        @Override
        public List<Select<_annoRef._memberValue>> listSelectedIn(Node astNode) {
            List<Select<_annoRef._memberValue>> sel = new ArrayList<>();
            Tree.in(astNode,
                    MemberValuePair.class,
                    (MemberValuePair n)-> match(n),
                    (MemberValuePair n) -> sel.add( select(n) )
            );
            return  sel;
        }

        @Override
        public <N extends Node> N forEachIn(N astNode, Predicate<_annoRef._memberValue> nodeMatchFn, Consumer<_annoRef._memberValue> nodeActionFn) {
            return Tree.in(astNode,
                    MemberValuePair.class,
                    (MemberValuePair n)-> match(n) && nodeMatchFn.test(_annoRef._memberValue.of(n)),
                    (MemberValuePair n) -> nodeActionFn.accept(_annoRef._memberValue.of(n))
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
            if( constraint.test( _annoRef._memberValue.of( onlyValueExpression.toString()) ) ) {
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
            MemberValuePair mvp = new MemberValuePair("", onlyValueExpression);
            if( constraint.test( _annoRef._memberValue.of(mvp) ) ) {
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
        public Select<_annoRef._memberValue> select(MemberValuePair mvp) {
            return select(_annoRef._memberValue.of(mvp));
        }

        @Override
        public Predicate<_annoRef._memberValue> getPredicate() {
            return null;
        }

        @Override
        public $memberValue setPredicate(Predicate<_annoRef._memberValue> predicate) {
            return null;
        }

        public Select<_annoRef._memberValue> select(_annoRef._memberValue _mvp){
            if (_mvp == null) {
                return null;
            }
            if (constraint.test(_mvp)) {
                Select ss = key.select(_mvp.getNameNode());
                if( ss == null){
                    return null;
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
            return false;
        }

    }

    /**
     * An Or entity that can match against any of the $bot instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $annoRef {

         final List<$annoRef> $annoRefBots = new ArrayList<>();

         public Or($annoRef...$as){
             super();
             Arrays.stream($as).forEach($a -> $annoRefBots.add($a) );
         }

        /**
         * Build and return a copy of this or bot
         * @return
         */
        public Or copy(){
             List<$annoRef> copyBots = new ArrayList<>();
             this.$annoRefBots.forEach(a-> copyBots.add(a.copy()));
             Or theCopy = new Or( copyBots.toArray(new $annoRef[0]) );

             //now copy the predicate and all underlying bots on the baseBot
             theCopy.predicate = this.predicate.and(t->true);
             theCopy.name = this.name.copy();
             this.$mvs.forEach(mv -> theCopy.$mvs.add( mv.copy()));
             return theCopy;
         }

         @Override
         public List<String> $list(){
             return $annoRefBots.stream().map($a ->$a.$list() ).flatMap(Collection::stream).collect(Collectors.toList());
         }

         @Override
         public List<String> $listNormalized(){
             return $annoRefBots.stream().map($a ->$a.$listNormalized() ).flatMap(Collection::stream).distinct().collect(Collectors.toList());
         }

         @Override
         public _annoRef fill(Object...vals){
             throw new _jdraftException("Cannot draft/fill "+getClass()+" pattern"+ this );
         }

         @Override
         public _annoRef fill(Translator tr, Object...vals){
             throw new _jdraftException("Cannot draft/fill "+getClass()+" pattern"+ this );
         }

         @Override
         public _annoRef draft(Translator tr, Map<String,Object> map){
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
             sb.append("$anno.Or{");
             sb.append(System.lineSeparator());
             $annoRefBots.forEach($a -> sb.append( Text.indent($a.toString()) ) );
             sb.append("}");
             return sb.toString();
         }

        /**
         *
         * @param _a
         * @return
         */
        public Select<_annoRef> select(_annoRef _a){
            Select commonSelect = super.select(_a);
            if(  commonSelect == null){
                return null;
            }
            $annoRef $whichBot = whichMatch(_a);
            if( $whichBot == null ){
                return null;
            }
            Select whichSelect = $whichBot.select(_a);
            if( !commonSelect.tokens.isConsistent(whichSelect.tokens)){
                 return null;
            }
            whichSelect.tokens.putAll(commonSelect.tokens);
            return whichSelect;
        }

         public boolean isMatchAny(){
              return false;
          }

         public List<$annoRef> $listOrSelectors() {
            return this.$annoRefBots;
         }

        public $annoRef whichMatch(_annoRef _a){
              return whichMatch(_a.ast());
          }

         /**
          * Return the underlying $anno that matches the AnnotationExpr or null if none of the match
          * @param ae
          * @return
          */
         public $annoRef whichMatch(AnnotationExpr ae){
             if( !this.predicate.test(_annoRef.of(ae) ) ){
                 return null;
             }
             Optional<$annoRef> orsel  = this.$annoRefBots.stream().filter($p-> $p.match(ae) ).findFirst();
             if( orsel.isPresent() ){
                 return orsel.get();
             }
             return null;
         }

         public Tokens parse(_annoRef _a){
             $annoRef $a = whichMatch(_a);
             if( $a != null) {
                 return $a.parse(_a);
             }
             return null;
         }
    }
}
