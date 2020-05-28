package org.jdraft.bot;

import org.jdraft._feature;
import org.jdraft.text.Template;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A $bot assigned to match or select from a specific feature
 *
 * //assign the $name.startsWith("get") $bot to the
 * $featureBot<_methodCallExpr, _name, $name> $fb = $featureBot.of( _methodCallExpr.NAME, $name.startsWith("get"));
 * assertTrue($featureBot.matches( "pubilc String getX(){ return this.x }" )); //verify that
 *
 * @param <_T> the target Type (containing the feature)
 * @param <_F> the feature Type
 * @param <$B> the bot type
 */
public class $featureBot<_T, _F, $B extends $selector<_F, $B> & Template<_F>> implements $feature<_T, _F, $B> {

    public static <_T, _F> $featureBot of( _feature<_T, _F> _f){
          return new $featureBot(_f);
    }

    public _feature<_T, _F> feature;

    public $B bot;

    public $featureBot(_feature<_T, _F> _f){
        this.feature = _f;
    }

    public _feature getFeature(){
        return this.feature;
    }

    /**
     * If true, the value of the feature doesn't matter
     * @return if the value of the feature on the target class doesnt matter
     */
    public boolean isMatchAny(){
        return bot == null || bot.isMatchAny();
    }

    @Override
    public _T draftTo(_T instance, Translator tr, Map<String, Object> memberValues) {
        if( this.bot != null ){ //only build it if the bot is not null
            _F featureInstance = this.bot.draft(tr, memberValues);
            this.feature.set(instance, featureInstance);
        }
        return instance;
    }

    public $B getBot(){
        return this.bot;
    }

    public $featureBot<_T, _F, $B> setBot($B bot){
        this.bot = bot;
        return this;
    }

    public _F draft(Translator translator, Map<String,Object> memberValues){
        if( bot == null){
            return null;
        }
        return bot.draft(translator, memberValues);
    }

    public $featureBot $(String target, String name){
        if( bot != null ){
            this.bot.$(target, name);
        }
        return this;
    }

    public $featureBot $hardcode(Translator translator, Map<String,Object>keyValues){
        if( bot != null ){
            this.bot.$hardcode(translator, keyValues);
        }
        return this;
    }

    public List<String> $listNormalized(){
        if( bot != null ){
            return this.bot.$listNormalized();
        }
        return Collections.emptyList();
    }

    public List<String> $list(){
        if( bot != null ){
            return this.bot.$list();
        }
        return Collections.emptyList();
    }

    /**
     * build and return a copy of this
     * @return
     */
    public $featureBot<_T, _F, $B> copy(){
        return of( this.feature).setBot(this.bot.copy());
    }

    public Select<_F> selectFrom(_T targetInstance){
        _F featureInstance = this.feature.get(targetInstance);
        if( this.bot == null ){
            return new Select<_F>(featureInstance, new Tokens());
        }
        return this.bot.select(featureInstance);
    }

    public String toString(){
        return this.feature+System.lineSeparator()+ this.bot;
    }
}
