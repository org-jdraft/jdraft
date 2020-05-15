package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import org.jdraft._jdraftException;
import org.jdraft.text.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A Matched Selection result returned from matching
 * (includes the {@link org.jdraft._java._domain} node relevant "Tokens" extracted when selecting")
 *
 * This is the return type from {@link $bot#select(Node)} methods, where we might want to
 * extract-then-transpose or extract-then-inspect relevant tokens
 *
 * @param <S> the selected instance type (i.e. _java._domain node), like {@link org.jdraft._method}, {@link org.jdraft._field},...)
 */
public class Select<S> {

    public S selection;
    public Tokens tokens;

    public Select(S selection, Tokens tokens) {
        this.selection = selection;
        this.tokens = tokens;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + System.lineSeparator()
                + Text.indent(selection.toString()) + System.lineSeparator() +
                Text.indent("Tokens : " + tokens) + System.lineSeparator()
                + "}";
    }

    public S get() {
        return this.selection;
    }

    public String get(String key){
        Object obj = tokens.get(key);
        if( obj != null ){
            return obj.toString();
        }
        return null;
    }

    /**
     * Verify that the value associated with this key is equal to
     * (Just saves a getTokens() step for the caller)
     * @param key the key for the token
     * @param expectedValue the expected value for the token
     * @return true if the expected value for the token matches expectedValue, false otherwise
     */
    public boolean is(String key, String expectedValue){
        return tokens.is( key, expectedValue);
    }

    /**
     * Verify all of the keyValues internally are equal to the expectedKeyValues
     * @param expectedKeyValues a list of alternating key/value pairs for expected values
     * @return true if the keyValue pairs passed in match within the selection
     */
    public boolean is(Object...expectedKeyValues){
        return tokens.is(expectedKeyValues);
    }

    /**
     *
     * @param <_T> the target type containing the feature (to be resolved)
     * @param <_F> the resolved feature type (to be selected from)
     */
    public static abstract class $featureRule<_T, _F> implements Function<_T, Tokens> {

        /**
         * The target class containing the feature to be tested
         */
        public Class<_T> targetClass;

        /**
         * The type of the resolved feature class (i.e. a {@link org.jdraft._java._node}, or a Boolean, or Enum)
         */
        public Class<_F> featureClass;

        /**
         * the name of the feature (i.e. since we can have one or more features on the target class
         * that can have a
         */
        public String featureName;

        /**
         * A function to resolve/retrieve the underlying feature from the targetClass
         * (i.e. if the feature is the parameterName,
         */
        public Function<_T, _F> featureResolver;

        public $featureRule(Class<_T>targetClass, Class<_F> featureClass, String featureName, Function<_T, _F> featureResolver){
            this.targetClass = targetClass;
            this.featureClass = featureClass;
            this.featureName = featureName;
            this.featureResolver = featureResolver;
        }

        /**
         * If true, the value of the feature doesn't matter
         * @return if the value of the feature on the target class doesnt matter
         */
        public abstract boolean isMatchAny();

        public abstract _F draft(Translator translator, Map<String,Object> memberValues);

        public $featureRule $(String target, String name){
            return this;
        }

        public abstract $featureRule $hardcode(Translator translator, Map<String,Object>keyValues);

        public abstract List<String> $listNormalized();

        public abstract List<String> $list();

        /**
         * build and return a copy of this 
         * @return
         */
        public abstract $featureRule<_T, _F> copy();
    }
    
    /**
     * $bot that is assigned to test the feature of a targetClass (it resolves a feature value from the target class and
     * contains a bot to test the value of the feature & extracts tokens from the feature)
     *
     * @param <$B> the $bot type
     * @param <_T> the target class type (where the resolve starts)
     * @param <_F> the resolved feature type (within the target type instance) (the thing the $bot tests)
     */
    public static class $botRule<$B extends $bot, _T, _F> extends $featureRule<_T, _F> {

        public static<_T, _R> $botRule of(Class<_T>targetClass, Class<_R>memberClass, String memberName, Function<_T, _R> memberResolver){
            return new $botRule(targetClass, memberClass, memberName, memberResolver);
        }

        public static<$B extends $bot, _T, _R> $botRule of(Class<_T>targetClass, Class<_R>memberClass, String memberName, Function<_T, _R> memberResolver, $B bot){
            return new $botRule(targetClass, memberClass, memberName, memberResolver).setBot(bot);
        }

        public $botRule(Class<_T>targetClass, Class<_F> featureClass, String featureName, Function<_T,_F> featureResolver){
            super(targetClass, featureClass, featureName, featureResolver);
        }

        public $B bot;

        public $botRule setBot($B bot){
            this.bot = bot;
            return this;
        }

        public $B getBot(){
            return bot;
        }

        public boolean isMatchAny(){
            return this.bot == null || this.bot.isMatchAny();
        }

        public $featureRule $(String target, String name){
            this.bot.$(target, name);
            return this;
        }

        public $featureRule $hardcode(Translator translator, Map<String,Object> keyValues){
            this.bot.$hardcode(translator,keyValues);
            return this;
        }

        public List<String> $list(){
            if( this.bot != null ) {
                return this.bot.$list();
            }
            return new ArrayList<>();
        }

        public List<String> $listNormalized(){
            if( this.bot != null ) {
                return this.bot.$listNormalized();
            }
            return new ArrayList<>();
        }

        public _F draft(Translator translator, Map<String,Object> memberValues){
            if( this.bot != null ) {
                return (_F) this.bot.draft(translator, memberValues);
            }
            return null;
        }

        public $botRule<$B, _T, _F> copy(){
            $botRule $copy = new $botRule( this.targetClass, this.featureClass, this.featureName+"", this.featureResolver.andThen(Function.identity()));
            if( this.bot != null ){
                $copy.setBot( ($B)this.bot.copy() );
            }
            return $copy;
        }

        @Override
        public Tokens apply(_T t) {
            if( this.featureResolver == null || this.bot == null){
                //System.out.println("NOTHING WRONG WITH "+ this.toString());
                //there is NOTHING to select (i.e. is match any)
                return new Tokens();
            }
            _F resolved = null;
            try {
                resolved = featureResolver.apply(t);
                //System.out.println("RESOLVED" + resolved +" for "+ toString());
            }
            catch(Exception e) {
                //System.err.println("Error resolving "+toString());
                e.printStackTrace();
                return null;
            }
            try{
                Select s = this.bot.select(resolved); //featureSelector.apply(resolved);
                //System.out.println("TOKS"+s );
                if( s != null ){
                    return s.tokens;
                }
                return null;
            } catch(Exception e){
                //e.printStackTrace();
                //System.err.println("Error selecting; returning null Selection");
                return null;
            }
        }
    }

    public static class $PredicateRule<_T, _F> extends $featureRule<_T, _F> {

        public Predicate<_F> predicate;

        public $PredicateRule(Class<_T>targetClass, Class<_F>featureClass, String memberName, Function<_T, _F> featureResolver, Predicate<_F> predicate){
            super( targetClass, featureClass, memberName, featureResolver );
            this.predicate = predicate;
        }

        public _F draft(Translator translator, Map<String,Object> memberValues){
            return null;
        }

        public boolean isMatchAny(){
            try {
                return predicate.test(null);
            }catch(Exception e){
            }
            return false;
        }

        public $PredicateRule $(String target, String name){
            return this;
        }

        public $PredicateRule $hardcode(Translator translator, Map<String,Object> keyValues){
            return this;
        }

        public List<String> $list(){
            return new ArrayList<>();
        }

        public List<String> $listNormalized(){
            return new ArrayList<>();
        }

        public $PredicateRule<_T, _F> copy(){
            $PredicateRule $copy = new $PredicateRule( this.targetClass, this.featureClass, this.featureName+"",
                    this.featureResolver, this.predicate.and(t->true));
            return $copy;
        }

        @Override
        public Tokens apply(_T t) {
            if( this.featureResolver == null || this.predicate == null){
                //System.out.println("NOTHING WRONG WITH "+ this.toString());
                //there is NOTHING to select (i.e. is match any)
                return new Tokens();
            }
            _F resolved = null;
            try {
                resolved = featureResolver.apply(t);
                //System.out.println("RESOLVED" + resolved +" for "+ toString());
            }
            catch(Exception e) {
                //System.err.println("Error resolving "+toString());
                e.printStackTrace();
                return null;
            }
            try{
                if( this.predicate.test(resolved) ){
                    return new Tokens();
                }
                return null;
            } catch(Exception e){
                return null;
            }
        }
    }

    /**
     * Selector for a Feature that cannot have one of the provided values (within a finite set of values)
     * (we keep track of ALL possible values and exclusions)
     *
     * @see $binaryExpr#operator
     * @see $unaryExpr#operator
     */
    public static class $OneOfRule<_T> extends $featureRule<_T, Object> {

        public Set allPossibleValues = new HashSet<>();
        public Set excludedValues = new HashSet<>();

        public $OneOfRule(Class<_T>targetClass, String memberName, Function<_T, Object> memberResolver, Set allPossibleValues, Set excludedValues){
            super( targetClass, Object.class, memberName, memberResolver );
            this.allPossibleValues = allPossibleValues;
            setExcluded(excludedValues);
        }

        public $OneOfRule includeOnly(Object...values){
            this.excludedValues.addAll(this.allPossibleValues);
            Arrays.stream(values).forEach(i-> this.excludedValues.remove(i));
            return this;
        }

        public $OneOfRule setExcluded(Set s){
            this.excludedValues = s;
            return this;
        }

        public $OneOfRule exclude(Object...toExclude){
            if( this.excludedValues == null) {
                this.excludedValues = new HashSet<>();
            }
            Arrays.stream(toExclude).forEach( e -> this.excludedValues.add(e));
            return this;
        }

        public Tokens apply(_T t) {
            if( this.featureResolver == null){
                return new Tokens();
            }
            Object resolved = null;
            try {
                resolved = featureResolver.apply(t);
            }
            catch(Exception e) {
                e.printStackTrace();
                return null;
            }
            if( this.excludedValues.contains(resolved ) ){
                return null;
            }
            return new Tokens();
        }

        public $OneOfRule include(Object...toInclude){
            if( this.excludedValues == null) {
                this.excludedValues = new HashSet<>();
            }
            Arrays.stream(toInclude).forEach( e -> this.excludedValues.remove(e));
            return this;
        }

        public Set getExcluded(){
            return this.excludedValues;
        }

        public Set getAllPossibleValues(){ return this.allPossibleValues; }

        public Set getIncludedValues(){
            Set s = new HashSet<>();
            s.addAll(this.allPossibleValues);
            s.removeAll(this.excludedValues);
            return s;
        }

        public Object draft(Translator translator, Map<String,Object> memberValues){
            Set remainSet = getIncludedValues();
            //System.out.println( remainSet );
            if( remainSet.size() == 1 ){
                return remainSet.toArray()[0];
            }
            Object n = memberValues.get(this.featureName);
            return n;
        }

        @Override
        public $OneOfRule<_T> $hardcode(Translator translator, Map<String, Object> keyValues) {
            return this;
        }

        @Override
        public List<String> $listNormalized() {
            return new ArrayList<>();
        }

        @Override
        public List<String> $list() {
            return new ArrayList<>();
        }

        public $OneOfRule<_T> copy(){
             Set all = new HashSet<>();
             all.addAll( this.allPossibleValues);
             Set ex = new HashSet<>();
             ex.addAll(this.excludedValues);
             return new $OneOfRule<_T>( this.targetClass, this.featureName, this.featureResolver, all, ex);
        }

        public boolean isMatchAny(){
            return this.excludedValues == null || excludedValues.isEmpty();
        }
    }

    /**
     * Selector for a Boolean Feature value,
     * <UL>
     * <LI>if the value of the Boolean is null means "I dont care what the Resolved Feature value is" (matches true OR false or null)
     * <LI>true means I match ONLY resolved features values that are true</LI>
     * <LI>false means I match ONLY resolved feature values that are false</LI>
     * </UL>
     * @see $parameter#isVarArg
     * @see $parameter#isFinal
     */
    public static class $BooleanRule<_T> extends $featureRule<_T, Boolean> {

        public Boolean expectedValue = null;

        public $BooleanRule(Class<_T>targetClass, String memberName, Function<_T, Boolean> memberResolver){
            super( targetClass, Boolean.class, memberName, memberResolver );
            setExpected(null);
        }

        public $BooleanRule(Class<_T>targetClass, String memberName, Function<_T, Boolean> memberResolver, Boolean expectedValue){
            super( targetClass, Boolean.class, memberName, memberResolver );
            setExpected(expectedValue);
        }

        public $BooleanRule setExpected(Boolean bl){
            this.expectedValue = bl;
            return this;
        }

        public Boolean getExpected(){
            return this.expectedValue;
        }

        public Tokens apply(_T t) {
            if( this.featureResolver == null){
                return new Tokens();
            }
            Object resolved = null;
            try {
                resolved = featureResolver.apply(t);
            }
            catch(Exception e) {
                e.printStackTrace();
                return null;
            }
            if( this.expectedValue == null || Objects.equals(this.expectedValue, resolved)){
                return new Tokens();
            }
            return null;
        }

        public $BooleanRule<_T> copy(){
            if( this.expectedValue == null ){
                return new $BooleanRule<_T>( this.targetClass, this.featureName, this.featureResolver, null);
            }
            return new $BooleanRule<_T>( this.targetClass, this.featureName, this.featureResolver, this.expectedValue.booleanValue());
        }

        public Boolean draft(Translator translator, Map<String,Object> memberValues){
            if( this.expectedValue == null){
                throw new _jdraftException("Could not draft boolean");
            }
            return this.expectedValue;
        }

        @Override
        public List<String> $listNormalized() {
            return new ArrayList<>();
        }

        @Override
        public List<String> $list() {
            return new ArrayList<>();
        }

        public $BooleanRule $hardcode(Translator translator, Map<String,Object> keyValues){
            return this;
        }

        public boolean isMatchAny(){
            return this.expectedValue == null;
        }
    }

    /**
     * Selector for a String Feature value,
     * <UL>
     * <LI>if the value of the Boolean is null means "I dont care what the Resolved Feature value is" (matches true OR false or null)
     * <LI>true means I match ONLY resolved features values that are true</LI>
     * <LI>false means I match ONLY resolved feature values that are false</LI>
     * </UL>
     */
    public static class $StencilRule<_T> extends $featureRule<_T, String> {

        public Stencil stencil = null;

        public $StencilRule(Class<_T>targetClass, String memberName, Function<_T, String> memberResolver){
            super( targetClass, String.class, memberName, memberResolver );
        }

        public $StencilRule(Class<_T>targetClass, String memberName, Function<_T, String> memberResolver, String stencil){
            super( targetClass, String.class, memberName, memberResolver );
            setStencil(stencil);
        }

        public $StencilRule(Class<_T>targetClass, String memberName, Function<_T, String> memberResolver, Stencil stencil){
            super( targetClass, String.class, memberName, memberResolver );
            setStencil(stencil);
        }

        public $StencilRule setStencil(String s) {
            return setStencil(Stencil.of(s));
        }

        public $StencilRule setStencil(Stencil st){
            this.stencil = st;
            return this;
        }

        public Stencil getStencil(){
            return this.stencil;
        }

        public $StencilRule<_T> copy(){
            if( this.stencil == null ){
                return new $StencilRule<_T>( this.targetClass, this.featureName, this.featureResolver);
            }
            return new $StencilRule<_T>( this.targetClass, this.featureName, this.featureResolver, this.stencil.copy());
        }

        public boolean isMatchAny(){
            return this.stencil == null || this.stencil.isMatchAny();
        }

        public Tokens apply(_T t) {
            if( this.featureResolver == null){
                return new Tokens();
            }
            String resolved = null;
            try {
                resolved = featureResolver.apply(t);
            }
            catch(Exception e) {
                e.printStackTrace();
                return null;
            }
            if(this.stencil == null ){
                return new Tokens();
            }
            Tokens ts = this.stencil.parse(resolved);
            return ts;
        }

        @Override
        public String draft(Translator translator, Map<String, Object> memberValues) {
            if( this.stencil != null ){
                return this.stencil.draft(translator, memberValues);
            }
            throw new _jdraftException("No stencil to draft for "+ featureName+" of "+targetClass);
        }

        @Override
        public $StencilRule $hardcode(Translator translator, Map<String, Object> keyValues) {
            if( this.stencil != null ){
                this.stencil.$hardcode(translator, keyValues);
            }
            return this;
        }

        @Override
        public List<String> $listNormalized() {
            if( this.stencil != null ){
                return this.stencil.$listNormalized();
            }
            return new ArrayList<>();
        }

        @Override
        public List<String> $list() {
            if( this.stencil != null ){
                return this.stencil.$list();
            }
            return new ArrayList<>();
        }
    }
}
