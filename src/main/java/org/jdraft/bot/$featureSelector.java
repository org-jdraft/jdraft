package org.jdraft.bot;

import org.jdraft.text.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 *
 * @param <_T> the top member class to start the resolve
 * @param <_R> the resolved member type (to select Tokens against)
 */
public class $featureSelector<_T, _R> implements Function<_T, Tokens> {

    public static<_T, _R> $featureSelector of(Class<_T>targetClass, Class<_R>memberClass, String memberName, Function<_T, _R> memberResolver){
        return new $featureSelector(targetClass, memberClass, memberName, memberResolver);
    }

    public static<_T, _R> $featureSelector of(Class<_T>targetClass, Class<_R>memberClass, String memberName, Function<_T, _R> memberResolver, Function<_R, Tokens> memberSelector){
        return new $featureSelector(targetClass, memberClass, memberName, memberResolver).setSelector(memberSelector);
    }

    public Class<_T> targetClass;
    public Class<_R> featureClass;
    public String featureName;
    public Function <_T,_R> featureResolver;
    public Function<_R, Tokens> featureSelector = null;

    public $featureSelector(Class<_T>targetClass, Class<_R> featureClass, String featureName, Function<_T,_R> featureResolver){
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

    public $featureSelector setSelector(Function<_R, Tokens> memberSelector){
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

    public _R draft(Translator translator, Map<String,Object>memberValues){
        if( this.featureSelector instanceof Template ){
            Template t = (Template)this.featureSelector;
            return (_R)t.draft(translator, memberValues);
        }
        return null;
    }

    public $featureSelector $(String target, String name){
        if( this.featureSelector instanceof Template ){
            Template t = (Template)this.featureSelector;
            this.featureSelector = (Function<_R, Tokens>)t.$(target, name);
        }
        return this;
    }

    public $featureSelector $hardcode(Translator translator, Map<String,Object> keyValues){
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

    public $featureSelector<_T, _R> copy(){
        $featureSelector $copy = new $featureSelector( this.targetClass, this.featureClass, this.featureName, this.featureResolver);
        if( this.featureSelector instanceof $bot){
            $bot $b = ($bot)this.featureSelector;
            $copy.setSelector( (Function<_R, Tokens>)$b.copy() );
        } else {
            $copy.setSelector(this.featureSelector);
        }
        return $copy;
    }

    public String toString(){
        if( this.featureResolver == null || this.featureSelector == null){
            return this.targetClass.getSimpleName()+" -> "+ this.featureClass.getSimpleName()+" "+this.featureName +" : MATCH ANY";
        }
        return this.targetClass.getSimpleName()+" -> "+ this.featureClass.getSimpleName()+" "+this.featureName +" : "+System.lineSeparator()+ Text.indent( this.featureSelector.toString());
    }

    /**
     * Selector for a Boolean value,
     * <UL>
     * <LI>null means (matches true OR false or null)
     * <LI>true means matches true ONLY</LI>
     * <LI>false means matches false ONLY</LI>
     * </UL>
     * (i.e. isVarArg, isFinal)
     */
    public static class $BooleanFeatureSelector implements Function<Boolean,Tokens>{

        private Boolean expectedValue = null;

        public $BooleanFeatureSelector(){
            this(null);
        }
        public $BooleanFeatureSelector(Boolean expectedValue){
            this.expectedValue = expectedValue;
        }

        @Override
        public Tokens apply(Boolean aBoolean) {
            if( expectedValue == null || Objects.equals(expectedValue, aBoolean)){
                return new Tokens();
            }
            return null;
        }
    }

    public static class $StringFeatureSelector implements Function<String,Tokens>{

        private Stencil stencil = null;

        public $StringFeatureSelector(){ }

        public $StringFeatureSelector(String stencil){
            this.stencil = Stencil.of(stencil);
        }

        public $StringFeatureSelector(Stencil stencil){
            this.stencil = stencil;
        }

        @Override
        public Tokens apply(String aString) {
            if( stencil == null){
                return new Tokens();
            }
            if( aString == null){
                return null;
            }
            Tokens ts = stencil.parse(aString);
            if( ts != null ){
                return ts;
            }
            return null;
        }
    }
}
