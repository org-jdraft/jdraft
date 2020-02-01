package org.jdraft.pattern;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.*;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * $pattern based on a uninterrupted String (and NOT a "specific" node class)
 * this String CANNOT have any internal whitespace but it MAY have pattern variables:
 * i.e.
 * $token $t = $token.of("com.github.javaparser$pkg$.name"); //(this is a valid token )
 *
 * @author Eric
 */
public class $token implements $pattern<Node, $token> {

    public static $token of( String tokenPattern ){
        return new $token(tokenPattern);
    }

    /** We use the $node to emulate a token... and preclude specific Node types from matching */
    private final $node $n;

    public $token(String tokenPattern ){
        $n = $node.of(tokenPattern)
                .$not(CompilationUnit.class, Parameter.class, BodyDeclaration.class, Statement.class, CatchClause.class, LambdaExpr.class, ObjectCreationExpr.class);
    }

    @Override
    public boolean isMatchAny() {
        return $n.isMatchAny();
    }

    @Override
    public $token $(String target, String $paramName) {
        $n.$(target, $paramName);
        return this;
    }

    @Override
    public $token $and(Predicate<Node> constraint) {
        $n.$and(constraint);
        return this;
    }

    @Override
    public $token hardcode$(Translator translator, Tokens kvs) {
        $n.hardcode$(translator, kvs);
        return this;
    }

    @Override
    public boolean match(Node candidate) {
        return $n.match(candidate);
    }

    @Override
    public Node firstIn(Node astStartNode, Predicate<Node> nodeMatchFn) {
        return $n.firstIn(astStartNode, nodeMatchFn);
    }

    @Override
    public $node.Select select(Node instance) {
        return $n.select(instance);
    }

    @Override
    public $node.Select selectFirstIn(Node astNode) {
        return $n.selectFirstIn(astNode);
    }

    @Override
    public List<$node.Select> listSelectedIn(Node astNode) {
        return $n.listSelectedIn(astNode);
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<Node> nodeMatchFn, Consumer<Node> nodeActionFn) {
        return $n.forEachIn(astNode, nodeMatchFn, nodeActionFn);
    }

    /**
     *
     * @param <_J>
     * @param _j
     * @param replacement
     * @return
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, String replacement) {
        return $n.replaceIn(_j, replacement);
    }

    /**
     *
     * @param <_J>
     * @param _j
     * @param $replacement
     * @return
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, $node $replacement) {
        return $n.replaceIn(_j, $replacement);
    }

    /**
     *
     * @param <N>
     * @param astNode
     * @param $replacement
     * @return
     */
    public <N extends Node> N replaceIn(N astNode, $node $replacement) {
        return this.$n.replaceIn(astNode,$replacement);
    }

    public <_CP extends _code._provider> _CP replaceIn(_CP codeProvider, String replacement ){
        return this.$n.replaceIn(codeProvider,replacement);
    }

    /**
     * Adds a constraint that the beforeExpression occurs in the same context/block before the target Expression
     * @param patternsOccurringBeforeThisNode
     * @return
     */
    public $token $isAfter( $pattern... patternsOccurringBeforeThisNode ){
        this.$n.$isAfter(patternsOccurringBeforeThisNode);
        return this;
    }

    /**
     * Adds a constraint that the beforeExpression occurs in the same context/block before the target Expression
     * @param patternsOccurringBeforeThisNode
     * @return
     */
    public $token $isNotAfter( $pattern... patternsOccurringBeforeThisNode ){
        this.$n.$isNotAfter(patternsOccurringBeforeThisNode);
        return this;
    }

    /**
     *
     * @param patternsOccurringAfterThisNode
     * @return
     */
    public $token $isBefore( $pattern... patternsOccurringAfterThisNode ){
        this.$n.$isBefore(patternsOccurringAfterThisNode);
        return this;
    }

    /**
     *
     * @param patternsOccurringAfterThisNode
     * @return
     */
    public $token $isNotBefore( $pattern... patternsOccurringAfterThisNode ){
        this.$n.$isNotBefore(patternsOccurringAfterThisNode);
        return this;
    }
}
