package org.jdraft.bot;

import org.jdraft.text.Template;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @param <_T> the top member class to start the resolve
 * @param <_R> the resolved member type (to select Tokens against)
 */
public class $memberSelector<_T, _R> implements Function<_T, Tokens> {

    public static<_T, _R> $memberSelector of(Class<_T>targetClass, Class<_R>memberClass, String memberName, Function<_T, _R> memberResolver){
        return new $memberSelector(targetClass, memberClass, memberName, memberResolver);
    }

    public Class<_T> targetClass;
    public Class<_R> memberClass;
    public String memberName;
    public Function <_T,_R> memberResolver;
    public Function<_R, Tokens> memberSelector = null;

    public $memberSelector(Class<_T>targetClass, Class<_R>memberClass, String memberName, Function<_T,_R> memberResolver){
        this.targetClass = targetClass;
        this.memberClass = memberClass;
        this.memberName = memberName;
        this.memberResolver = memberResolver;
    }

    public boolean isMatchAny(){
        return this.memberResolver == null || this.memberSelector == null;
    }

    public $memberSelector setSelector(Function<_R, Tokens> memberSelector){
        this.memberSelector = memberSelector;
        return this;
    }

    @Override
    public Tokens apply(_T t) {
        if( this.memberResolver == null || this.memberSelector == null){
            //there is NOTHING to select (i.e. is match any)
            return new Tokens();
        }
        _R resolved = null;
        try {
            resolved = memberResolver.apply(t);
        }
        catch(Exception e) {
            //System.err.println("Error resolving ");
            return null;
        }
        try{
            return memberSelector.apply(resolved);
        } catch(Exception e){
            //e.printStackTrace();
            //System.err.println("Error selecting; returning null Selection");
            return null;
        }
    }

    public _R draft(Translator translator, Map<String,Object>memberValues){
        if( this.memberSelector instanceof Template ){
            Template t = (Template)this.memberSelector;
            return (_R)t.draft(translator, memberValues);
        }
        return null;
    }

    public $memberSelector $(String target, String name){
        if( this.memberSelector instanceof Template ){
            Template t = (Template)this.memberSelector;
            this.memberSelector = (Function<_R, Tokens>)t.$(target, name);
        }
        return this;
    }

    public $memberSelector $hardcode(Translator translator, Map<String,Object> keyValues){
        if( this.memberSelector instanceof $bot ){
            $bot t = ($bot)this.memberSelector;
            this.memberSelector = (Function<_R, Tokens>)t.$hardcode(translator, keyValues);
        }
        return this;
    }

    public List<String> $listNormalized(){
        if( this.memberSelector instanceof Template ){
            Template t = (Template)this.memberSelector;
            return t.$listNormalized();
        }
        return new ArrayList<>();
    }

    public List<String> $list(){
        if( this.memberSelector instanceof Template ){
            Template t = (Template)this.memberSelector;
            return t.$list();
        }
        return new ArrayList<>();
    }

    public $memberSelector<_T, _R> copy(){
        $memberSelector $copy = new $memberSelector( this.targetClass, this.memberClass, this.memberName, this.memberResolver);
        if( this.memberSelector instanceof $bot){
            $bot $b = ($bot)this.memberSelector;
            $copy.setSelector( (Function<_R, Tokens>)$b.copy() );
        } else {
            $copy.setSelector(this.memberSelector);
        }
        return $copy;
    }

    public String toString(){
        if( this.memberResolver == null || this.memberSelector == null){
            return this.targetClass.getSimpleName()+" -> "+ this.memberClass.getSimpleName()+" "+this.memberName+" : MATCH ANY";
        }
        return this.targetClass.getSimpleName()+" -> "+ this.memberClass.getSimpleName()+" "+this.memberName+" : "+System.lineSeparator()+ Text.indent( this.memberSelector.toString());
    }


}
