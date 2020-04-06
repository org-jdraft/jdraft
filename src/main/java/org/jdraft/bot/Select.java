package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;

/**
 * A Matched Selection result returned from matching
 * (includes the {@link org.jdraft._java._domain} node relevant "Tokens" extracted when selecting")
 *
 * This is the return type from {@link $bot#select(Node)} methods, where we might want to
 * extract-then-transpose or extract-then-inspect relevant tokens
 *
 * @param <S> the selected instance type (i.e. _java._domain node), like {@link org.jdraft._method}, {@link org.jdraft._field},...)
 */
public class Select<S> {

    public S selection;
    public Tokens tokens;

    public Select(S selection, Tokens tokens) {
        this.selection = selection;
        this.tokens = tokens;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + System.lineSeparator()
                + Text.indent(selection.toString()) + System.lineSeparator() +
                Text.indent("Tokens : " + tokens) + System.lineSeparator()
                + "}";
    }

    public S get() {
        return this.selection;
    }

    public String get(String key){
        Object obj = tokens.get(key);
        if( obj != null ){
            return obj.toString();
        }
        return null;
    }

    /**
     * Verify that the value associated with this key is equal to
     * (Just saves a getTokens() step for the caller)
     * @param key
     * @param expectedValue
     * @return
     */
    public boolean is(String key, String expectedValue){
        return tokens.is( key, expectedValue);
    }

    /**
     * Verify all of the keyValues internally are equal to the expectedKeyValues
     * @param expectedKeyValues
     * @return
     */
    public boolean is(Object...expectedKeyValues){
        return tokens.is(expectedKeyValues);
    }
}
