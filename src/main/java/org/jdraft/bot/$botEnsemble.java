package org.jdraft.bot;

import org.jdraft._jdraftException;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Defines the base functionality for a $bot with predicate and a list of {@link $feature}s
 * Where a given bot is really an ensemble of other (lower level) $feature $bots
 * <PRE>
 * for example:
 * a {@link $binaryExpr} is a bot for matching/selecting binary expressions (i.e. "a + b")
 * is a {@link $botEnsemble} containing (3) featureBots:
 * <UL>
 *  <LI>{@link $binaryExpr#left}
 *  <LI>{@link $binaryExpr#operator}
 *  <LI>{@link $binaryExpr#right}
 * </UL>
 * along with a predicate: {@link $binaryExpr#predicate}
 * </PRE>
 *
 * @param <_T> the target type the bot operates on
 * @param <$B> the specific $bot type that extends the $botEnsemble
 */
public abstract class $botEnsemble<_T,$B extends $botEnsemble> {

    public Predicate<_T> predicate = t->true;

    public Predicate<_T> getPredicate(){
        return predicate;
    }

    public $B setPredicate(Predicate<_T> predicate){
        this.predicate = predicate;
        return ($B)this;
    }

    public abstract List<$feature<_T, ?,?>> $listFeatures();

    /**
     * Given a
     * @param targetInstance
     * @return
     */
    public Select<_T> select(_T targetInstance){
        try{
            if( this.predicate.test(targetInstance)) {
                Tokens ts = tokensFrom(targetInstance, $listFeatures());
                if (ts == null) {
                    return null;
                }
                return new Select<>(targetInstance, ts);
            }
        }catch(Exception e){
        }
        return null;
    }

    /**
     *
     * @param targetInstance
     * @param $fs
     * @param <_T>
     * @return
     */
    private static <_T> Tokens tokensFrom(_T targetInstance, List<$feature<_T, ?, ?>> $fs ){

        Tokens allTokens = new Tokens();
        for( int i=0; i< $fs.size(); i++){
            try {
                Select sel = $fs.get(i).selectFrom(targetInstance); //apply(_t);

                if( sel == null || !allTokens.isConsistent(sel.tokens)){
                    //when the first token select returns null or succeeds & isn't consistent with existingTokens
                    return null;
                }
                allTokens.putAll(sel.tokens);
                //System.out.println(" ExistingTokens "+ existingTokens);
            }catch(Exception e){
                //throw?
                throw new _jdraftException("Exception with $feature ["+ i+" ] "+ $fs.get(i)+System.lineSeparator()+"...with input "+targetInstance, e);
            }
        }
        return allTokens;
    }

    /**
     * The selectors MUST appear in the order of appearance
     * @param $featureAction
     * @return
     */
    public $B $forFeatures(Consumer<$feature<_T, ?, ?>> $featureAction){
        $listFeatures().stream().forEach($ms -> $featureAction.accept($ms));
        return ($B)this;
    }

    public $B $(String target, String $name){
        $forFeatures($ms-> $ms.$(target, $name));
        return ($B)this;
    }

    public List<String> $list(){
        List<String>strs = new ArrayList<>();
        $forFeatures($ms-> strs.addAll($ms.$list()));
        return strs;
    }

    public List<String> $listNormalized(){
        List<String>strs = new ArrayList<>();
        $forFeatures($ms-> strs.addAll($ms.$listNormalized()));
        return strs.stream().distinct().collect(Collectors.toList());
    }

    public $B $hardcode(Translator translator, Map<String,Object> map){
        $forFeatures($fs-> {
            //System.out.println( "Calling HARDCODE$ on "+ $fs.getFeature());
            $fs = $fs.$hardcode(translator, map);
            //System.out.println( "DONE "+ $fs);
        } );
        return ($B)this;
    }

    public $B $hardcode(Translator translator, Tokens ts){
        $forFeatures($fs-> {
            //System.out.println( "Calling HARDCODE$");
            $fs = $fs.$hardcode(translator, ts);
            //System.out.println( "Calling "+$fs);
        } );
        return ($B)this;
    }

    public boolean isMatchAny() {
        if (this.$listFeatures().stream().allMatch($ms -> $ms.isMatchAny())) {
            try {
                return this.predicate.test(null);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
