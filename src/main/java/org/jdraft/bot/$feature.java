package org.jdraft.bot;

import org.jdraft._feature;
import org.jdraft.text.Translator;

import java.util.List;
import java.util.Map;

public interface $feature <_T, _F, $B extends $selector<? extends _F, $B>> {

    /**
     * Returns the feature ("edge" class and name) for the feature associated with the bot
     * @return the feature
     */
    _feature<_T, _F> getFeature();

    /**
     * If true, the value of the feature doesn't matter
     * @return if the value of the feature on the target class doesnt matter
     */
    boolean isMatchAny();

    //create and then update the
    _T draftTo( _T instance, Translator tr, Map<String,Object> memberValues);

    /**
     * Given the target type (CONTAINING the Feature)
     * have the bot try selecting the feature from the target type
     * @param targetType the target type containing the feature
     * @return the
     */
    Select selectFrom(_T targetType);

    /*
    public _F draft(Translator translator, Map<String,Object> memberValues){
        if( bot == null){
            return null;
        }
        return bot.draft(translator, memberValues);
    }
     */

    $feature $(String target, String name);

    $feature $hardcode(Translator translator, Map<String,Object>keyValues);

    List<String> $listNormalized();

    List<String> $list();

    $feature<_T, _F, $B> copy();

}
