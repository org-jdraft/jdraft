package org.jdraft.bot;

import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Defines the base functionality for a $bot with predicate and a list of {@link Select.$feature}s
 * @param <_N>
 * @param <$B>
 */
public abstract class $baseBot<_N,$B extends $baseBot> {

    public Predicate<_N> predicate = t->true;

    public Predicate<_N> getPredicate(){
        return predicate;
    }

    public $B setPredicate(Predicate<_N> predicate){
        this.predicate = predicate;
        return ($B)this;
    }

    public Select<_N> select(_N _p){
        try{
            if( this.predicate.test(_p)) {
                Tokens ts = Select.tokensFrom(_p, $listSelectors());
                if (ts == null) {
                    return null;
                }
                return new Select<>(_p, ts);
            }
        }catch(Exception e){
        }
        return null;
    }

    /**
     * The selectors MUST appear in the order of appearance
     * @param featureSelectorAction
     * @return
     */
    public $B $forFeatureSelectors(Consumer<Select.$feature<_N, ?>> featureSelectorAction){
        $listSelectors().stream().forEach($ms -> featureSelectorAction.accept($ms));
        return ($B)this;
    }

    public $B $(String target, String $name){
        $forFeatureSelectors($ms-> $ms.$(target, $name));
        return ($B)this;
    }

    public List<String> $list(){
        List<String>strs = new ArrayList<>();
        $forFeatureSelectors($ms-> strs.addAll($ms.$list()));
        return strs;
    }

    public List<String> $listNormalized(){
        List<String>strs = new ArrayList<>();
        $forFeatureSelectors($ms-> strs.addAll($ms.$listNormalized()));
        return strs.stream().distinct().collect(Collectors.toList());
    }

    public $B $hardcode(Translator translator, Tokens ts){
        $forFeatureSelectors($ms-> $ms.$hardcode(translator, ts));
        return ($B)this;
    }

    public boolean isMatchAny() {
        if (this.$listSelectors().stream().allMatch($ms -> $ms.isMatchAny())) {
            try {
                return this.predicate.test(null);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public abstract List<Select.$feature<_N, ?>> $listSelectors();

}
