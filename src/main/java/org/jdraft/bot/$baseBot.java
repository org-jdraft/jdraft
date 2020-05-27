package org.jdraft.bot;

import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Defines the base functionality for a $bot with predicate and a list of {@link Select.$feature}s
 * @param <_T> the type the bot is familiar with
 * @param <$B> the underlying bot type (the subtype)
 */
public abstract class $baseBot<_T,$B extends $baseBot> {

    public Predicate<_T> predicate = t->true;

    public Predicate<_T> getPredicate(){
        return predicate;
    }

    public $B setPredicate(Predicate<_T> predicate){
        this.predicate = predicate;
        return ($B)this;
    }

    /**
     * Given a
     * @param _p
     * @return
     */
    public Select<_T> select(_T _p){
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
    public $B $forFeatureSelectors(Consumer<Select.$feature<_T, ?>> featureSelectorAction){
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

    public $B $hardcode(Translator translator, Map<String,Object> map){
        $forFeatureSelectors($fs-> {
            $fs = $fs.$hardcode(translator, map);
        } );
        return ($B)this;
    }

    public $B $hardcode(Translator translator, Tokens ts){
        $forFeatureSelectors($fs-> {
            //System.out.println( "Calling HARDCODE "+ $fs);
            $fs = $fs.$hardcode(translator, ts);
            //System.out.println( "DONE"+ $fs);
        } );
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

    public abstract List<Select.$feature<_T, ?>> $listSelectors();

}
