package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import org.jdraft._java;

import java.util.function.Predicate;

/**
 * Entity capable of selection which entails both
 * <UL>
 *     <LI>determining if a "node" meets a criteria</LI>
 *     <LI>extracting parameters within the text of the {@link _java._node}</LI>
 * </UL>
 * @param <_S>
 * @param <$S>
 */
public interface $selector<_S, $S> {

    /** matching function to match against a candidate (usually defaults to (t)->true to match all) */
    Predicate<_S> getPredicate();

    /**
     * REPLACES the existing predicate/matching function, note although this is available
     * most of the time you'll want to mutate the existing predicate with $and() or $not()
     * @see #$and(Predicate)
     * @see #$not(Predicate)
     */
    $S setPredicate( Predicate<_S> predicate);

    /**
     * Return a Select if the candidate matches
     * (including the candidate and tokens extracted)
     *
     * @param candidate
     * @return
     */
    Select<_S> select(_S candidate);

    /**
     * Does this textual representation match the normalized AST version?
     * @param candidate
     * @return
     */
    boolean matches(String candidate);

    /**
     *
     * @param candidate
     * @return
     */
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
     * --Predicate Updater -- (i.e. updates/adds a new Predicate constraint to the existing predicate constraints)
     *
     * Update the matchFn with a new MatchFn that will be "ANDED" to the existing matchFn
     * @param matchFn a new functional matching constraint to the prototype
     * @return the modified $selector
     */
    default $S $and(Predicate<_S> matchFn){
        setPredicate( getPredicate().and(matchFn));
        return ($S)this;
    }

    /**
     * --Constraint updater -- (i.e. updates constraints on the prototype and returns the modified prototype)
     *
     * Add a (NOT) matching constraint to add to the $prototype
     * @param matchFn a constraint to be negated and added (via and) to the constraint
     * @return the modified $selector
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

}
