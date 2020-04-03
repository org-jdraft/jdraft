package org.jdraft.bot;

import org.jdraft._java;
import org.jdraft.text.Tokens;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This is HOW the Larger level $bots (i.e. {@link $methodCall}) organize and assign the embedded bots
 * ({@link $methodCall#scope}, {@link $methodCall#name}, {@link $methodCall#arguments}, {@link $methodCall#typeArguments}
 * Assigns a bot to a specific syntactical entity:
 * (for example we might have a $name bot instance and we want to assign it to
 * the name of a $method bot... it will take in the _method, and it will know how to select
 * the name of a particular method.
 *
 * internally stores
 * A function for extracting the target entity from the candidate (i.e. (_meth)-> _meth.getName() )
 * applying a specific bot to it (i.e.
 * 1) extracting a child entity from it's parent
 * 2) using a specific bot to test
 */
public class $botSelect<_JP extends _java._domain, _JC> implements BiFunction<_JP, Tokens, Tokens> {

    /** extract the (_JC) child entity from the (_JP)parent (so the bot can operate on the _JC entity)*/
    public Function<_JP,_JC> extract;

    /** the bot that will select from the _JC child entity*/
    public $bot<?, _JC, ?> bot;

    public $botSelect(Function<_JP, _JC> extract, $bot<?,_JC,?> bot){
        this.extract = extract;
        this.bot = bot;
    }

    public $botSelect setBot($bot<?, _JC, ?> bot ){
        this.bot = bot;
        return this;
    }

    /**
     * given a candidate Parent object, and Tokens,
     * 1) assuming the seriesTokens are not null
     * 2) extract the child _JC from the parent _JP
     * 3) try to select with the bot with _JC as input
     *
     * @param j
     * @param seriesTokens
     * @return
     */
    @Override
    public Tokens apply(_JP j, Tokens seriesTokens) {
        if( seriesTokens == null ){
            return null; //if tokens was null, it means the bot before in the series failed, so dont waste time testing
        }
        _JC _jc = extract.apply(j); //extract the _JC child entity from the _JP parent
        Select<_JC> sel = bot.select(_jc); //
        if( sel == null ){
            return null;
        }
        if( seriesTokens.isConsistent(sel.tokens)){ //verify consistency, (no two tokens with the same name have a different value
            seriesTokens.putAll(sel.tokens); //if they are consistent, add/merge the tokens
            return seriesTokens; //return the updated tokens including the selected tokens from the bot
        }
        return null;
    }
}
