package org.jdraft.proto;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import org.jdraft.*;
import org.jdraft._java;
import org.jdraft._node;
import org.jdraft._type;
import org.jdraft.proto.$expr.Select;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 *
 * @author Eric
 * @param <T>
 */
public interface $exprProto<T extends Expression> 
    extends $field.$part, $proto<T, $exprProto<T>>, $var.$part, Template<T> {

    $exprProto<T> $(String target, String $name);

    /**
     *
     * @param astExpr
     * @param $name
     * @return
     */
    $exprProto<T> $(Expression astExpr, String $name);

    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified $expr prototype
     */
    $exprProto<T> and(Predicate<T> constraint);

    /**
     *
     * @param _n
     * @return
     */
    T draft(_node _n);

    T draft(Translator t, Map<String, Object> tokens);

    T fill(Object... values);

    /**
     *
     * @param clazz the target class to search through
     * @param selectConsumer the consumer to operate on all selected entities
     * @return the (potentially modified) _type of the clazz
     */
    <_CT extends _type> _CT forSelectedIn(Class clazz, Consumer<Select<T>> selectConsumer);

    /**
     *
     * @param <_J>
     * @param _j
     * @param selectConsumer
     * @return
     */
    <_J extends _java> _J forSelectedIn(_J _j, Consumer<Select<T>> selectConsumer);

    /**
     *
     * @param <N>
     * @param astNode
     * @param selectConsumer
     * @return
     */
    <N extends Node> N forSelectedIn(N astNode, Consumer<Select<T>> selectConsumer);

    /**
     *
     * @param clazz
     * @param selectConstraint
     * @param selectConsumer
     * @return
     */
    <_CT extends _type> _CT forSelectedIn(Class clazz, Predicate<Select<T>> selectConstraint, Consumer<Select<T>> selectConsumer);

    /**
     *
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @param selectConsumer
     * @return
     */
    <_J extends _java> _J forSelectedIn(_J _j, Predicate<Select<T>> selectConstraint, Consumer<Select<T>> selectConsumer);

    /**
     *
     * @param <N>
     * @param astNode
     * @param selectConstraint
     * @param selectConsumer
     * @return
     */
    <N extends Node> N forSelectedIn(N astNode, Predicate<Select<T>> selectConstraint, Consumer<Select<T>> selectConsumer);

    /**
     * Does this $expr match ANY
     * @return
     */
    boolean isMatchAny();

    List<String> list$();

    List<String> list$Normalized();

    List<T> listIn(_java _j);

    List<T> listIn(Node astNode);

    List<Select<T>> listSelectedIn(Node astNode);

    List<Select<T>> listSelectedIn(_java _j);

    List<Select<T>> listSelectedIn(Class clazz);

    /**
     *
     * @param clazz
     * @param selectConstraint
     * @return
     */
    List<Select<T>> listSelectedIn(Class clazz, Predicate<Select<T>> selectConstraint);

    /**
     *
     * @param astNode
     * @param selectConstraint
     * @return
     */
    List<Select<T>> listSelectedIn(Node astNode, Predicate<Select<T>> selectConstraint);

    /**
     *
     * @param _n
     * @param selectConstraint
     * @return
     */
    List<Select<T>> listSelectedIn(_java _n, Predicate<Select<T>> selectConstraint);

    /**
     *
     * @param expression
     * @return
     */
    boolean matches(String... expression);

    /**
     *
     * @param astExpr
     * @return
     */
    boolean matches(Expression astExpr);

    /**
     *
     * @param clazz
     * @param astExprReplace
     * @return
     */
    <_CT extends _type> _CT replaceIn(Class clazz, Node astExprReplace);

    /**
     *
     * @param <_J>
     * @param _j
     * @param astExprReplace
     * @return
     */
    <_J extends _java> _J replaceIn(_J _j, Node astExprReplace);

    /**
     *
     * @param <_J>
     * @param _j
     * @param protoReplaceExpr
     * @return
     */
    <_J extends _java> _J replaceIn(_J _j, String protoReplaceExpr);

    /**
     *
     * @param clazz
     * @param $replaceProto
     * @return
     */
    <_CT extends _type> _CT replaceIn(Class clazz, $expr $replaceProto);

    /**
     *
     * @param <_J>
     * @param _j
     * @param $replaceProto
     * @return
     */
    <_J extends _java> _J replaceIn(_J _j, $expr $replaceProto);

    /**
     *
     * @param astExpr
     * @return
     */
    Select select(Expression astExpr);

    /**
     * 
     * @param expr
     * @return 
     */
    Select<T> select(String... expr);
    /**
     *
     * @param clazz
     * @param selectConstraint
     * @return
     */
    Select<T> selectFirstIn(Class clazz, Predicate<Select<T>> selectConstraint);

    /**
     * Returns the first Expression that matches the pattern and constraint
     * @param _n the _java node
     * @param selectConstraint
     * @return  the first Expression that matches (or null if none found)
     */
    Select<T> selectFirstIn(_java _n, Predicate<Select<T>> selectConstraint);

    /**
     * Returns the first Expression that matches the pattern and constraint
     * @param astNode the node to look through
     * @param selectConstraint
     * @return  the first Expression that matches (or null if none found)
     */
    Select<T> selectFirstIn(Node astNode, Predicate<Select<T>> selectConstraint);
    
}
