package org.jdraft.bot;

import org.jdraft._feature;
import org.jdraft._tree;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A list of $bots assigned to match or select from a specific list of features
 *
 * //assign the $name.startsWith("get") $bot to the
 * $featureBot<_methodCallExpr, _name, $name> $fb = $featureBot.of( _methodCallExpr.NAME, $name.startsWith("get"));
 * assertTrue($featureBot.matches( "pubilc String getX(){ return this.x }" )); //verify that
 *
 * @param <_T> the target Type (containing the feature)
 * @param <_F> the feature Type
 * @param <$B> the bot type
 */
public class $featureBotList<_T, _F, $B extends $bot<_F, $B>> implements $feature<_T, _F, $B>{

    public static <_T, _F> $featureBotList of(_feature._many<_T, _F> _f){
          return new $featureBotList(_f);
    }

    public _feature._many<_T, _F> feature;

    public List<$B> botList = new ArrayList<>();

    public $B getBot(int index){
        return botList.get(index);
    }

    public _feature getFeature(){
        return this.feature;
    }

    /**
     * IF we want to ALWAYS return the entire (list) implementation
     * i.e. if we configure a $bot that matches everything
     * $methodCall $mc = $methodCall.of("print($all$);");
     * ...
     * above $all$ will mean match ANY arguments passed print (no args, 1 arg, 10 args)
     * to we want this to MATCH AND RETURN the entire arguments list with the matchAllName
     * set to "all"
     */
    public String matchAllName;

    public $featureBotList(_feature._many<_T, _F> _f){
        this.feature = _f;
    }

    /**
     *
     * @return if the matchAllName is set
     * @see #matchAllName
     */
    public boolean isMatchAll(){
        return this.matchAllName != null;
    }

    /**
     * gets the matchAllName
     * @return the match all name, null if this is not a match all
     * @see #matchAllName
     */
    public String getMatchAllName(){
        return this.matchAllName;
    }

    /**
     * NOTE: setting this to null will remove the matchAllName
     * @param matchAll if true sets matchAll to be the featureName, if false, sets to not match all
     * @return
     */
    public $featureBotList<_T, _F, $B> setMatchAll(Boolean matchAll){
        if( matchAll == true ){
            this.matchAllName = this.feature.featureId.name;
        } else{
            this.matchAllName = null;
        }
        return this;
    }

    /**
     * NOTE: setting this to null will remove the matchAllName
     * @param matchAllName
     * @return
     */
    public $featureBotList<_T, _F, $B> setMatchAll(String matchAllName){
        this.matchAllName = matchAllName;
        return this;
    }

    /**
     * If true, the value of the feature doesn't matter
     * @return if the value of the feature on the target class doesnt matter
     */
    public boolean isMatchAny(){
        return botList.isEmpty();
    }

    @Override
    public _T draftTo(_T instance, Translator tr, Map<String, Object> memberValues) {
        List<_F> features = new ArrayList<>();
        for(int i=0;i<this.botList.size(); i++){
            _F feature = this.botList.get(i).draft(tr, memberValues);
            features.add(feature);
        }
        this.feature.set(instance, features);
        return instance;
    }

    @Override
    public Select<List<_F>> selectFrom(_T targetInstance) {
        List<_F> featureInstanceList = this.feature.get(targetInstance);

        if( this.matchAllName != null ){
            return new Select<List<_F>>(featureInstanceList, Tokens.of(this.matchAllName, featureInstanceList));
        }
        if (this.botList.isEmpty()) {
            return new Select<List<_F>>(featureInstanceList, Tokens.of( ));
        }
        if(featureInstanceList.size() < this.botList.size() ){
            return null;
        }
        //ordered ? or unordered
        try{
            List<_F>leftList = new ArrayList<>();
            leftList.addAll(featureInstanceList);

            Tokens all = new Tokens();
            for(int i=0;i<this.botList.size();i++){
                $B bot = this.botList.get(i);
                Optional<_F> found = leftList.stream().filter(e -> bot.matches(e)).findFirst();
                if( !found.isPresent() ){
                    return null;
                }
                Select<_F> sel = bot.select(found.get());
                leftList.remove(sel.select);
                if( all.isConsistent(sel.tokens) ){
                    all.putAll(sel.tokens);
                } else{
                    return null;
                }
            }
            return new Select<List<_F>>(featureInstanceList, all);
        } catch(Exception e){
            return null;
        }
    }

    public $featureBotList<_T, _F, $B> add( $B bot){
        this.botList.add(bot);
        return this;
    }

    public $featureBotList<_T, _F, $B> setBotList(List<$B> botList){
        this.botList = botList;
        return this;
    }

    public List<_F> draft(Translator translator, Map<String,Object> memberValues){
        List<_F> drafted = new ArrayList<>();

        for(int i=0;i<this.botList.size(); i++){
            drafted.add(botList.get(i).draft(translator, memberValues));
        }
        return drafted;
    }

    public $featureBotList $(String target, String name){
        this.botList.forEach(e -> e.$(target, name));
        return this;
    }

    public $featureBotList $hardcode(Translator translator, Map<String,Object>keyValues){
        if( this.isMatchAll() ){
            if( keyValues.get(this.matchAllName) != null){
                //System.out.println( ">>>>>> upgrading the matchAll");
                //System.out.println( ">>>>>> upgrading the matchAll to "+keyValues.get(this.matchAllName));
                _F element = this.feature.elementParser.apply( keyValues.get(this.matchAllName).toString() );
                //System.out.println( ">>>>>> generated "+element+" "+element.getClass());

                this.botList.add( ($B)$bot.of( (_tree._node)element) );
                //System.out.println( ">>>>>> generated "+this.botList);
                this.matchAllName = null;
            }
        }
        this.botList.forEach(e -> e.$hardcode(translator, keyValues));
        return this;
    }

    public List<String> $listNormalized(){
        List<String> l = new ArrayList<>();
        this.botList.forEach($b -> l.addAll( $b.$list()));
        return l.stream().distinct().collect(Collectors.toList());
    }

    public List<String> $list(){
        List<String> l = new ArrayList<>();
        this.botList.forEach($b -> l.addAll( $b.$list()));
        return l;
    }

    /**
     * build and return a copy of this
     * @return
     */
    public $featureBotList<_T, _F, $B> copy(){
        //System.out.println( "ORIG IS "+ this.botList.size() + this.botList);
        List<$B> $botCopyList = this.botList.stream().map($b -> $b.copy()).collect(Collectors.toList());
        //System.out.println( "COPY IS "+ $botCopyList.size() + $botCopyList);
        return of( this.feature).setBotList($botCopyList).setMatchAll(this.matchAllName);
    }

    public String toString(){
        return this.feature+System.lineSeparator()+ this.botList;
    }
}
