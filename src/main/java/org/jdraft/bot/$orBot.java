package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import org.jdraft._java;
import org.jdraft.text.Template;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface for building a implementation of a $bot that can logically -or- together
 * multiple like-$bots to have it operate as a single $bot
 *
 * i.e.
 * $methodCall $print = $methodCall.of("print");
 * $methodCall $println = $methodCall.of("println");
 *
 * //matches print() or println() method calls
 * $methodCall $printOrPrintln = $methodCall.or( $print, $println );
 *
 * @param <_E>
 * @param <$E>
 */
public interface $orBot<E, _E, $E extends $bot<E, _E, $E>> extends $bot<E, _E, $E>{ //$orBot<_E, $E extends $selector<_E, $E>> extends $selector<_E, $E>{

    default boolean isMatchAny(){
        return false;
    }

    /**
     * List each of the $E $bot instances for the Or bot
     * @return a list of the $methodCalls to OR
     */
    List<$E> listEach();

    /**
     * iterate over each $bot and parameterize
     * @param key
     * @param value
     * @return
     */
    default $E $(String key, String value){
        listEach().forEach( b -> b.$(key, value));
        return ($E)this;
    }

    /**
     *
     * @param t
     * @param keyValues
     * @return
     */
    default $E $hardcode(Translator t, Map<String,Object> keyValues){
        this.listEach().forEach( b -> b.$hardcode(t, keyValues) );
        return ($E)this;
    }

    /**
     * Hardcode the key values on each of the Or bots
     * @param t
     * @param keyValues
     * @return
     */
    default $E $hardcode(Translator t, Tokens keyValues){
        listEach().forEach( b -> b.$hardcode(t, keyValues));
        return ($E)this;
    }

    /**
     * Return the underlying $arguments that matches the _arguments
     * (or null if none of the $arguments match the candidate _arguments)
     * @param ae
     * @return
     */
    default $E whichMatch(_E ae){
        if( !getPredicate().test(ae ) ){
            return null;
        }
        Optional<$E> orsel  = listEach().stream().filter($p-> (($E)$p).matches( (_E)ae ) ).findFirst();
        if( orsel.isPresent() ){
            return orsel.get();
        }
        return null;
    }

    default Select<_E> select(Node node){
        try{
            return select( (_E) _java.of(node) );
        } catch(Exception e){
            return null;
        }
    }

    @Override
    default Select<_E> select(_E candidate) {
        $E $matched = whichMatch(candidate);
        if( $matched == null ){
            return null;
        }
        return $matched.select(candidate);
    }

    default String describe(){
        StringBuilder sb = new StringBuilder();
        sb.append( this.getClass().getDeclaringClass().getSimpleName()+".Or{").append(System.lineSeparator());
        for(int i = 0; i< listEach().size(); i++){
            sb.append( Text.indent( this.listEach().get(i).toString()) );
        }
        sb.append("}");
        return sb.toString();
    }
}
