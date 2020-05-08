package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;
import org.jdraft.*;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * $bot for inspecting, drafting and mutating {@link _parameter}s / {@link Parameter}s
 */
public class $parameter implements $bot.$node<Parameter, _parameter, $parameter>,
        //$bot.$node<Parameter, _parameter, $parameter>,
        //$bot.$multiBot<Parameter, _parameter, $parameter>,
        $selector.$node<_parameter, $parameter> {

    interface $part{}

    public static $parameter of(Expressions.Command lambdaWithParameter ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $parameter of(Consumer<? extends Object> lambdaWithParameter ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $parameter of(Supplier<? extends Object> lambdaWithParameter ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $parameter of(BiConsumer<? extends Object, ? extends Object> lambdaWithParameter ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $parameter of(Expressions.TriConsumer<? extends Object, ? extends Object, ? extends Object> lambdaWithParameter ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $parameter of(Expressions.QuadConsumer<? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithParameter ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $parameter of(Function<? extends Object, ? extends Object> lambdaWithParameter ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $parameter of(BiFunction<? extends Object, ? extends Object,? extends Object> lambdaWithParameter ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $parameter of(Expressions.TriFunction<? extends Object, ? extends Object,? extends Object, ? extends Object> lambdaWithParameter ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $parameter of(Expressions.QuadFunction<? extends Object, ? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithParameter ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    private static $parameter from(StackTraceElement ste ){
        return of( _parameter.from( ste) );
    }

    public static $parameter of( _typeRef _tr){
        return of().$typeRef(_tr);
    }

    public static $parameter of( Class parameterType ){
        return of().$typeRef(parameterType);
    }

    public static $parameter of( String parameterCode ){
        return of( _parameter.of(parameterCode) );
    }

    public static $parameter of() {
        return new $parameter();
    }

    public static $parameter of(_parameter _p) {
        return new $parameter(_p);
    }

    public static $parameter of(Parameter ile) {
        return new $parameter(_parameter.of(ile));
    }

    public static $parameter of(String... code) {
        return of(_parameter.of(Text.combine( code)));
    }

    public static $parameter as(String...code ){
        return as( _parameter.of(Text.combine(code)));
    }

    /**
     * Exactly match against this parameter (i.e. exactly these annos, final, vararg, etc.
     * @param _p the parameter to match exactly to
     * @return the parameter bot
     */
    public static $parameter as(_parameter _p){
        return of()
                .$name(_p.getName())
                .$typeRef(_p.getTypeRef())
                .$annoRefs($annoRefs.as(_p.ast()))
                .$isVarArg(_p.isVarArg())
                .$isFinal(_p.isFinal());
    }

    /**
     * Whether to match varArg parameters ... note null means I dont care one way or the other
     * @param b whether to match varArg Parameters (null means dont care either way)
     * @return the $parameter bot
     */
    public $parameter $isVarArg(Boolean b){
        this.varArg = b;
        this.isVarArg.setSelector(bool-> {
            if(Objects.equals(bool, this.varArg)){
                return new Tokens();
            }
            return null;
        });
        return this;
    }

    /**
     * Whether to match final parameters ... note null means I dont care one way or the other
     * @param b whether to match varArg Parameters (null means dont care either way)
     * @return the $parameter bot
     */
    public $parameter $isFinal(Boolean b){
        this.finalMod = b;
        this.isFinal.setSelector(bool-> {
            if(Objects.equals(bool, this.finalMod)){
                return new Tokens();
            }
            return null;
        });
        return this;
    }

    public static $parameter.Or or($parameter...$params ){
        return new Or($params);
    }

    /**
     * the internals of what an instance of $methodCall.Or is (by example):
     *
     * <PRE>
     * //define (2) instances of $methodCall bots
     * $methodCall $a = $methodCall.of().$isInCodeUnit(c-> c instanceof _class);
     * $methodCall $b = $methodCall.of().$isInCodeUnit(c-> c instanceof _interface);
     *
     * // build an $methodCall.Or instance with the (2) $methodCall bots {$a,$b}
     * $methodCall.Or $or = $methodCall.or($a, $b);
     *
     * //NOTE: the $or instance IS A $methodCall itself, this is done because there may be
     * // some instance that expects a $methodCall, and this will satisfy the requirement
     * $methodCall $mc = ($methodCall) $mcor;
     *
     * //here we modify the "base instance", of the $or, we add a constraint that applies physically to
     * //the underlying $or instance (which again IS a $methodCall), so we update it's
     * //predicate, but it can be "thought of" LOGICALLY as applying this constraint to BOTH
     * //individual bots {$a, $b}
     * $or.$isInCodeUnit(c-> c.importsClass(IOException.class));
     *
     * //When we try to select, we ALWAYS FIRST check the base "$or" instances select/match function
     * //here the match/select returns FALSE, because the base constraint (import IOException)
     * //is NOT met, even though one of the individual bots ($a) DOES match all of its constraints
     * assertFalse($or.isIn("class C{ long t = System.getTimeMillis(); }"));
     *
     * //here the match/select DOES work, because the base constrains (imports IOException) are met,
     * // AND one of the OR constraints match
     * (here specifically $a, which looks for ANY methodCall that is defined within a _class)
     * assertTrue($or.IsIn("import java.io.IOException;","class C{ long t = System.getTimeMillis(); }");
     * </PRE>
     */
    public static class Or extends $parameter {

         public List<$parameter> $parameterBots = new ArrayList<>();

         public Or($parameter...$mcs){
             Arrays.stream($mcs).forEach(m -> $parameterBots.add(m));
         }

        public List<$parameter> $listOrSelectors() {
            return $parameterBots;
        }

         public Or copy(){
             Or or = new Or($listOrSelectors().toArray(new $parameter[0]));
             or.$and( this.predicate.and(t->true) );
             //I need to port over all of the common things
             or.annoRefs = this.annoRefs.copy();
             or.name = this.name.copy();
             or.type = this.type.copy();
             or.finalMod = this.finalMod;
             or.isFinal = this.isFinal;
             or.varArg = this.varArg;
             or.isVarArg = this.isVarArg;
             return or;
         }

         public Select<_parameter> select(_parameter _candidate){
             Select commonSelect = super.select(_candidate);
             if(  commonSelect == null){
                 return null;
             }
             $parameter $whichBot = whichMatch(_candidate);
             if( $whichBot == null ){
                 return null;
             }
             Select whichSelect = $whichBot.select(_candidate);
             if( !commonSelect.tokens.isConsistent(whichSelect.tokens)){
                 return null;
             }
             whichSelect.tokens.putAll(commonSelect.tokens);
             return whichSelect;
         }

        /**
         * Return the underlying $arguments that matches the _arguments
         * (or null if none of the $arguments match the candidate _arguments)
         * @param candidate
         * @return
         */
        public $parameter whichMatch(_parameter candidate){
            Optional<$parameter> orsel  = this.$parameterBots.stream().filter($p-> $p.matches( candidate ) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }

        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append( "$parameter.Or{").append(System.lineSeparator());
            for(int i = 0; i< $listOrSelectors().size(); i++){
                sb.append( Text.indent( this.$listOrSelectors().get(i).toString()) );
            }
            sb.append("}");
            return sb.toString();
        }
    }

    public Predicate<_parameter> predicate = d -> true;

    public $memberSelector<_parameter, _name> name =
            $memberSelector.of( _parameter.class, _name.class, "name", p-> _name.of(p.getNameNode()));

    public $memberSelector<_parameter, _typeRef> type =
            $memberSelector.of( _parameter.class, _typeRef.class, "type", p->p.getTypeRef());

    public $memberSelector<_parameter, _annoRefs> annoRefs =
            $memberSelector.of( _parameter.class, _annoRefs.class, "annoRefs", p->p.getAnnoRefs());

    public $memberSelector<_parameter, Boolean> isVarArg =
            $memberSelector.of( _parameter.class, Boolean.class, "isVarArg", p->p.isVarArg());

    public $memberSelector<_parameter, Boolean> isFinal =
            $memberSelector.of( _parameter.class, Boolean.class, "isFinal", p->p.isFinal());

    public Boolean varArg;
    public Boolean finalMod;

    public $parameter() { }

    public $parameter(_parameter _p){
        if( _p.hasAnnoRefs() ) {
            annoRefs.setSelector($annoRefs.of(_p.ast()));
        }
        name.setSelector( $name.of(_p.getName()) );
        type.setSelector( $typeRef.of(_p.getTypeRef()));
        if( _p.isVarArg() ){
            isVarArg.setSelector( (b)->{
                if(b){
                    return new Tokens();
                }
                return null;
            } );
        }
        if( _p.isFinal()){
            isFinal.setSelector( (b)->{
                if(b){
                    return new Tokens();
                }
                return null;
            } );
        }
    }

    /**
     * Build and return a new independent mutable copy of this $bot
     * @return
     */
    public $parameter copy(){
        $parameter $p = of().$and( this.predicate.and(t->true) );
        $p.annoRefs = this.annoRefs.copy();
        $p.name = this.name.copy();
        $p.type = this.type.copy();
        $p.isFinal = this.isFinal;
        $p.finalMod = this.finalMod;
        $p.isVarArg = this.isVarArg;
        $p.varArg = this.varArg;
        return $p;
    }

    @Override
    public $parameter $hardcode(Translator translator, Tokens kvs) {
        $forMemberSelectors($ms -> $ms.$hardcode(translator, kvs));
        /*
        this.annoRefs.$hardcode(translator, kvs);
        this.name.$hardcode(translator, kvs);
        this.type.$hardcode(translator, kvs);
        */
        return this;
    }

    public Predicate<_parameter> getPredicate(){
        return this.predicate;
    }

    public $parameter setPredicate(Predicate<_parameter> predicate){
        this.predicate = predicate;
        return this;
    }

    public $parameter $forMemberSelectors( Consumer<$memberSelector<_parameter, ?>> memberSelectorAction){
        $memberSelectors().stream().forEach($ms -> memberSelectorAction.accept($ms));
        return this;
    }

    public List<$memberSelector<_parameter, ?>> $memberSelectors(){
        List<$memberSelector<_parameter, ?>> mss = new ArrayList<>();
        mss.add(this.annoRefs);
        mss.add(this.isFinal);
        mss.add(this.type);
        mss.add(this.name);
        mss.add(this.isVarArg);
        return mss;
    }

    private Function<_parameter, Tokens>[] $memberFunctions(){
        return (Function<_parameter, Tokens>[]) new Function[]{
             this.annoRefs, this.isFinal, this.type, this.name, this.isVarArg
        };
    }

    public boolean isMatchAny(){
        if( this.$memberSelectors().stream().allMatch($ms -> $ms.isMatchAny())){
            try {
                return this.predicate.test(null);
            } catch(Exception e){
                return false;
            }
        }
        return false;
        /*
        if( this.name.isMatchAny() &&
            this.annoRefs.isMatchAny() &&
            this.type.isMatchAny() &&
            this.isFinal.isMatchAny() &&
            this.isVarArg.isMatchAny() ){
            try {
                return this.predicate.test(null);
            } catch(Exception e){ }
        }
        return false;
         */
    }

    public Select<_parameter> select(String... code) {
        try {
            return select(_parameter.of(Text.combine(code)));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_parameter> select(Node n) {
        if (n instanceof Parameter) {
            return select(_parameter.of((Parameter) n));
        }
        return null;
    }

    public Select<_parameter> select(_java._domain _n) {
        if (_n instanceof _parameter) {
            return select((_parameter) _n);
        }
        return null;
    }

    public Select<_parameter> select(_parameter _p){
        try{
            if( this.predicate.test(_p)) {
                Tokens ts = Tokens.selectTokens(_p, this.$memberFunctions());
                if (ts == null) {
                    return null;
                }
                return new Select<>(_p, ts);
            }
        }catch(Exception e){
        }
        return null;
    }

    public _parameter draft(Translator tr, Map<String,Object> keyValues){
        _parameter _p = _parameter.of();
        _p.setAnnoRefs( this.annoRefs.draft(tr, keyValues) );
        _p.setName(this.name.draft(tr, keyValues).name.toString());
        _p.setTypeRef(this.type.draft(tr, keyValues));
        if( this.finalMod != null){
            _p.setFinal(finalMod);
        }
        if( this.varArg != null ){
            _p.setVarArg( this.varArg);
        }
        return _p;
    }

    @Override
    public $parameter $(String target, String $Name) {
        $forMemberSelectors($ms -> $ms.$(target, $Name) );
        return this;
    }

    @Override
    public List<String> $list() {
        List<String> ps = new ArrayList<>();
        $forMemberSelectors($ms -> ps.addAll( $ms.$list()) );
        return ps;
    }

    @Override
    public List<String> $listNormalized() {
        List<String> ps = new ArrayList<>();
        $forMemberSelectors($ms -> ps.addAll( $ms.$listNormalized()) );
        return ps.stream().distinct().collect(Collectors.toList());
    }

    public $parameter $annoRefs(){
        this.annoRefs.setSelector(null);
        return this;
    }

    public $parameter $annoRefs($annoRefs $arfs){
        this.annoRefs.setSelector($arfs);
        return this;
    }

    public $parameter $annoRefs( $annoRef...$ars){
        this.annoRefs.setSelector( $annoRefs.of($ars));
        return this;
    }

    public $parameter $typeRef(){
        this.type.setSelector( $typeRef.of());
        return this;
    }

    public $parameter $typeRef(Predicate<_typeRef> predicate){
        if( this.type.memberSelector == null ){
            this.type.setSelector( $typeRef.of().$and(predicate));
        } else{
            $typeRef $tr = ($typeRef)this.type.getMemberSelector();
            $tr.$and(predicate);
        }
        return this;
    }

    public $parameter $typeRef(_typeRef _tr){
        return $typeRef( $typeRef.of(_tr));
    }

    public $parameter $typeRef($typeRef $as){
        this.type.setSelector( $as );
        return this;
    }

    public $parameter $typeRef(String ts){
        return $typeRef($typeRef.of(ts));
    }

    public $parameter $typeRef(Class clazz){
        return $typeRef($typeRef.of(clazz));
    }

    public $parameter $typeRef(String... ts){
        return $typeRef($typeRef.of(Text.combine( ts)));
    }

    public $parameter $name(){
        this.name.setSelector($name.of());
        return this;
    }

    public $parameter $name(Predicate<String> matchFn){
        $name $n = ($name)this.name.getMemberSelector();
        if( $n == null ){
            $n = $name.of();
            this.name.setSelector($n);
        }
        $n.$and(n -> matchFn.test( n.toString()));
        return this;
    }

    public $parameter $name(String name){
        this.name.setSelector( $name.of(name) );
        return this;
    }

    public $parameter $name($name $n){
        this.name.setSelector( $n );
        return this;
    }
}
