package org.jdraft.prototype;

import com.github.javaparser.ast.Node;
import org.jdraft._java;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;

import java.util.function.Predicate;

/**
 * Entity capable of
 * @param <_S>
 * @param <$S>
 */
public interface $selector<_S, $S> {

    Predicate<_S> getPredicate();

    //Select<_S> select(_java._node _jn);

    /**
     * Return a Select if the candidate matches
     * (including the candidate and tokens extracted)
     *
     * @param candidate
     * @return
     */
    Select<_S> select(_S candidate);

    default boolean matches(_S candidate){
        return select(candidate) != null;
    }

    /**
     * Does this match ANY input (INCLUDING NULL or no input at all)
     * in other words if an entity was found that PASSED the isMatchAny function it can be considered
     * ENTIRELY OPTIONAL (and or even null)
     * @return
     */
    boolean isMatchAny();

    /**
     * --Constraint updater -- (i.e. updates constraints on the prototype and returns the modified prototype)
     *
     * Update the matchFn with a new MatchFn that will be "ANDED" to the existing matchFn
     * @param matchFn a new functional matching constraint to the prototype
     * @return the modified prototype
     */
    $S $and(Predicate<_S> matchFn);

    /**
     * --Constraint updater -- (i.e. updates constraints on the prototype and returns the modified prototype)
     *
     * Add a (NOT) matching constraint to add to the $prototype
     * @param matchFn a constraint to be negated and added (via and) to the constraint
     * @return the modified prototype
     */
    default $S $not(Predicate<_S> matchFn) {
        return $and( matchFn.negate() );
    }

    /**
     * A Selector that selects a single AST _node type of thing
     *
     * @param <_N>
     * @param <$N>
     */
    interface $node<_N, $N> extends $selector<_N, $N> {

        Select<_N> select(String... code);

        default Select<_N> select(String code){
            return select( new String[]{code});
        }

        Select<_N> select(Node n);

        default Select<_N> select(_java._node _jn ){
            try{
                return select( (_N) _jn );
            }catch(Exception e){
                return null;
            }
        }

        default boolean matches(String...code){
           return select(code) != null;
        }

        default boolean matches(String code){
            return select(code) != null;
        }

        default boolean matches(Node n) {
            return select(n) != null;
        }
    }

    /**
     * A Matched Selection result returned from matching
     * (including relevant "Tokens" extracted when selecting"
     * @param <S> the selected instance type
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
}
