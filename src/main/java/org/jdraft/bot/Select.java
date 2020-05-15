package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import org.jdraft.text.*;

import java.util.*;
import java.util.function.Function;

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
     * $bot that is assigned to test from a feature that is resolved (& extract Tokens)
     * @param <$B> the $bot type
     * @param <_T> the top level class type (where the resolve starts)
     * @param <_F> the resolved feature type (the thing the $bot tests)
     */
    public static class $botFeature<$B extends $bot, _T, _F> extends $feature<_T, _F>{
        public static<_T, _R> $botFeature of(Class<_T>targetClass, Class<_R>memberClass, String memberName, Function<_T, _R> memberResolver){
            return new $botFeature(targetClass, memberClass, memberName, memberResolver);
        }

        public static<$B extends $bot, _T, _R> $botFeature of(Class<_T>targetClass, Class<_R>memberClass, String memberName, Function<_T, _R> memberResolver, $B bot){
            return new $botFeature(targetClass, memberClass, memberName, memberResolver).setBot(bot);
        }

        public $botFeature(Class<_T>targetClass, Class<_F> featureClass, String featureName, Function<_T,_F> featureResolver){
            super( targetClass = targetClass, featureClass = featureClass, featureName, featureResolver);
        }

        public $B bot;

        public $botFeature setBot($B bot){
            this.bot = bot;
            return this;
        }

        public $B getBot(){
            return bot;
        }

        public boolean isMatchAny(){
            return this.bot == null || this.bot.isMatchAny();
        }

        public $feature $(String target, String name){
            this.bot.$(target, name);
            return this;
        }

        public $feature $hardcode(Translator translator, Map<String,Object> keyValues){
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

        public $botFeature<$B, _T, _F> copy(){
            $botFeature  $copy = new $botFeature( this.targetClass, this.featureClass, this.featureName+"", this.featureResolver.andThen(Function.identity()));
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

    /**
     *
     * @param <_T> the top class containing the feature (to be resolved)
     * @param <_R> the resolved feature type (to be selected from)
     */
    public static class $feature<_T, _R> implements Function<_T, Tokens> {

        public static<_T, _R> $feature of(Class<_T>targetClass, Class<_R>memberClass, String memberName, Function<_T, _R> memberResolver){
            return new $feature(targetClass, memberClass, memberName, memberResolver);
        }

        public static<_T, _R> $feature of(Class<_T>targetClass, Class<_R>memberClass, String memberName, Function<_T, _R> memberResolver, Function<_R, Tokens> memberSelector){
            return new $feature(targetClass, memberClass, memberName, memberResolver).setSelector(memberSelector);
        }

        public Class<_T> targetClass;
        public Class<_R> featureClass;
        public String featureName;
        public Function<_T,_R> featureResolver;
        public Function<_R, Tokens> featureSelector = null;

        public $feature(Class<_T>targetClass, Class<_R> featureClass, String featureName, Function<_T,_R> featureResolver){
            this.targetClass = targetClass;
            this.featureClass = featureClass;
            this.featureName = featureName;
            this.featureResolver = featureResolver;
        }

        public boolean isMatchAny(){
            return this.featureResolver == null || this.featureSelector == null;
        }

        public Function<_R, Tokens> getFeatureSelector(){
            return this.featureSelector;
        }

        public $feature setSelector(Function<_R, Tokens> memberSelector){
            this.featureSelector = memberSelector;
            return this;
        }

        @Override
        public Tokens apply(_T t) {
            if( this.featureResolver == null || this.featureSelector == null){
                //System.out.println("NOTHING WRONG WITH "+ this.toString());
                //there is NOTHING to select (i.e. is match any)
                return new Tokens();
            }
            _R resolved = null;
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
                Tokens ts = featureSelector.apply(resolved);
                //System.out.println("TOKS"+ts );
                return ts;
            } catch(Exception e){
                //e.printStackTrace();
                //System.err.println("Error selecting; returning null Selection");
                return null;
            }
        }

        public _R draft(Translator translator, Map<String,Object> memberValues){
            if( this.featureSelector instanceof Template){
                Template t = (Template)this.featureSelector;
                return (_R)t.draft(translator, memberValues);
            }
            return null;
        }

        public $feature $(String target, String name){
            if( this.featureSelector instanceof Template ){
                Template t = (Template)this.featureSelector;
                this.featureSelector = (Function<_R, Tokens>)t.$(target, name);
            }
            return this;
        }

        public $feature $hardcode(Translator translator, Map<String,Object> keyValues){
            if( this.featureSelector instanceof $bot ){
                $bot t = ($bot)this.featureSelector;
                this.featureSelector = (Function<_R, Tokens>)t.$hardcode(translator, keyValues);
            }
            return this;
        }

        public List<String> $listNormalized(){
            if( this.featureSelector instanceof Template ){
                Template t = (Template)this.featureSelector;
                return t.$listNormalized();
            }
            return new ArrayList<>();
        }

        public List<String> $list(){
            if( this.featureSelector instanceof Template ){
                Template t = (Template)this.featureSelector;
                return t.$list();
            }
            return new ArrayList<>();
        }

        public $feature<_T, _R> copy(){
            $feature $copy = new $feature( this.targetClass, this.featureClass, this.featureName+"", this.featureResolver.andThen(Function.identity()));
            if( this.featureSelector instanceof $bot){
                $bot $b = ($bot)this.featureSelector;
                $copy.setSelector( (Function<_R, Tokens>)$b.copy() );
            } else {
                $copy.setSelector(this.featureSelector);
            }
            return $copy;
        }

        public String toString(){
            if( isMatchAny() ){
                return this.targetClass.getSimpleName()+" -> "+ this.featureClass.getSimpleName()+" "+this.featureName +" : MATCH ANY";
            }
            return this.targetClass.getSimpleName()+" -> "+ this.featureClass.getSimpleName()+" "+this.featureName +" : "+System.lineSeparator()+ Text.indent( this.featureSelector.toString());
        }
    }

    /**
     * Selector for a Feature that cannot have one of the provided values (within a finite set of values)
     * (we keep track of ALL possible values and exclusions)
     *
     * @see $binaryExpression#operator
     * @see $unary#operator TODO
     */
    public static class $OneOfFeature<_T> extends $feature<_T, Object> {

        public Set allPossibleValues = new HashSet<>();
        public Set excludedValues = new HashSet<>();

        public $OneOfFeature(Class<_T>targetClass, String memberName, Function<_T, Object> memberResolver, Set allPossibleValues, Set excludedValues){
            super( targetClass, Object.class, memberName, memberResolver );
            this.allPossibleValues = allPossibleValues;
            setExcluded(excludedValues);
        }

        public $OneOfFeature includeOnly(Object...values){
            this.excludedValues.addAll(this.allPossibleValues);
            Arrays.stream(values).forEach(i-> this.excludedValues.remove(i));
            //System.out.println( "Exclusions "+ this.excludedValues);
            setSelector(b-> {
                if(excludedValues == null){
                    return new Tokens();
                }
                if( this.excludedValues.contains(b) ){
                    return null;//it was excluded
                }
                return new Tokens();
            });
            return this;
        }

        public $OneOfFeature setExcluded(Set s){
            this.excludedValues = s;
            setSelector(b-> {
                if(this.excludedValues == null){
                    return new Tokens();
                }
                if( this.excludedValues.contains(b) ){
                    return null;//it was excluded
                }
                return new Tokens();
            });
            return this;
        }

        public $OneOfFeature exclude(Object...toExclude){
            if( this.excludedValues == null) {
                this.excludedValues = new HashSet<>();
            }
            Arrays.stream(toExclude).forEach( e -> this.excludedValues.add(e));
            setSelector(b-> {
                if(this.excludedValues == null){
                    return new Tokens();
                }
                if( this.excludedValues.contains(b) ){
                    return null;//it was excluded
                }
                return new Tokens();
            });
            return this;
        }

        public $OneOfFeature include(Object...toInclude){
            if( this.excludedValues == null) {
                this.excludedValues = new HashSet<>();
            }
            Arrays.stream(toInclude).forEach( e -> this.excludedValues.remove(e));
            setSelector(b-> {
                if(this.excludedValues == null){
                    return new Tokens();
                }
                if( this.excludedValues.contains(b) ){
                    return null;//it was excluded
                }
                return new Tokens();
            });
            return this;
        }

        public Set getExcluded(){
            return this.excludedValues;
        }

        public Object draft(Translator translator, Map<String,Object> memberValues){
            Set remainSet = new HashSet<>();
            remainSet.addAll(this.allPossibleValues);
            remainSet.removeAll(this.excludedValues);
            //System.out.println( remainSet );
            if( remainSet.size() == 1 ){
                return remainSet.toArray()[0];
            }
            Object n = memberValues.get(this.featureName);
            return n;
        }

        public $OneOfFeature<_T> copy(){
             Set all = new HashSet<>();
             all.addAll( this.allPossibleValues);
             Set ex = new HashSet<>();
             ex.addAll(this.excludedValues);
             return new $OneOfFeature<_T>( this.targetClass, this.featureName, this.featureResolver, all, ex);
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
    public static class $BooleanFeature<_T> extends $feature<_T, Boolean> {

        public Boolean expectedValue = null;

        public $BooleanFeature(Class<_T>targetClass, String memberName, Function<_T, Boolean> memberResolver){
            super( targetClass, Boolean.class, memberName, memberResolver );
            setExpected(null);
        }

        public $BooleanFeature(Class<_T>targetClass, String memberName, Function<_T, Boolean> memberResolver, Boolean expectedValue){
            super( targetClass, Boolean.class, memberName, memberResolver );
            setExpected(expectedValue);
        }

        public $BooleanFeature setExpected(Boolean bl){
            this.expectedValue = bl;
            setSelector(b-> {
                if(expectedValue == null){
                    return new Tokens();
                }
                if( Objects.equals(expectedValue, b)){
                    return new Tokens();
                }
                return null;
            });
            return this;
        }

        public Boolean getExpected(){
            return this.expectedValue;
        }

        public $BooleanFeature<_T> copy(){
            if( this.expectedValue == null ){
                return new $BooleanFeature<_T>( this.targetClass, this.featureName, this.featureResolver, null);
            }
            return new $BooleanFeature<_T>( this.targetClass, this.featureName, this.featureResolver, this.expectedValue.booleanValue());
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
    public static class $StringFeature<_T> extends $feature<_T, String> {

        public Stencil stencil = null;

        public $StringFeature(Class<_T>targetClass, String memberName, Function<_T, String> memberResolver){
            super( targetClass, String.class, memberName, memberResolver );
        }

        public $StringFeature(Class<_T>targetClass, String memberName, Function<_T, String> memberResolver, String stencil){
            super( targetClass, String.class, memberName, memberResolver );
            setStencil(stencil);
        }

        public $StringFeature(Class<_T>targetClass, String memberName, Function<_T, String> memberResolver, Stencil stencil){
            super( targetClass, String.class, memberName, memberResolver );
            setStencil(stencil);
        }

        public $StringFeature setStencil(String s) {
            return setStencil(Stencil.of(s));
        }

        public $StringFeature setStencil(Stencil st){
            this.stencil = st;
            setSelector(s-> {
                if(stencil == null){
                    return new Tokens();
                }
                Tokens ts = stencil.parse(s);
                if( ts != null ){
                    return ts;
                }
                return null;
            });
            return this;
        }

        public Stencil getStencil(){
            return this.stencil;
        }

        public $StringFeature<_T> copy(){
            if( this.stencil == null ){
                return new $StringFeature<_T>( this.targetClass, this.featureName, this.featureResolver);
            }
            return new $StringFeature<_T>( this.targetClass, this.featureName, this.featureResolver, this.stencil.copy());
        }

        public boolean isMatchAny(){
            return this.stencil == null || this.stencil.isMatchAny();
        }
    }
}
