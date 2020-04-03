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
        return this.getClass().getCanonicalName().replace("org.jdraft.protptype", "")+"{" + System.lineSeparator()
                + Text.indent(selection.toString()) + System.lineSeparator() +
                Text.indent("Tokens : " + tokens) + System.lineSeparator()
                + "}";
    }

    public S get() {
        return this.selection;
    }

}
