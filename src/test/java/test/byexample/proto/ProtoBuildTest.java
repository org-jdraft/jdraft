package test.byexample.proto;

import junit.framework.TestCase;
import org.jdraft.proto.*;

/**
 * WHAT are $protos & HOW to build $protos
 *    of() MatchAny $proto
 *    of( String ) Parameterized $proto
 *    of( String ) Constant $proto
 *
 * INTERNALS & INTROSPECTION
 *    WHAT are $protos internally
 *    toString()
 *    Stencil
 *    constraint (add constraint with  and() )
 *    querying the $proto (list$(), list$Normalized())
 *    isMatchAny()
 *    list$()
 *    list$Normalized()
 *
 * MODIFYING $PROTOS (after creation)
 *     $(...) parameterize
 *     hardCode$(...) hardcode parameter(s)
 *     and( Constraint )
 *     $name()
 *     (getting $proto fields)
 *
 * DRAFTING $protos
 *    $Name$ v $name$
 *    draft
 *    fill
 *    Parameteric Overrides
 *    $label:
 *
 * QUERYING & UPDATING SOURCE CODE with $PROTO
 *    match
 *    matches
 *    select
 *       the Select Implementation / Interface
 *    listIn
 *    listSelectedIn
 *    count
 *    firstIn
 *    selectFirstIn
 *    forEachIn
 *    forSelectedIn
 *
 *    replaceIn
 *    replaceSelectedIn
 *
 * Explain how to build $proto instances
 * To use $proto instances we have to build and
 *
 *
 */
public class ProtoBuildTest extends TestCase {

    public void testAll$protoTypes(){
        $node $n = $node.of(); //node
        $n = $.of();

        $id $i = $id.of();
        $i = $.id();

        $expr $e = $expr.of(); //specific $expr
        $stmt $s = $stmt.of(); //specific $stmt
        $anno $a = $anno.of();
        $case $c = $case.of();

        $import $im = $import.of();

        $catch $ca = $catch.of();

        $modifiers $ms = $modifiers.of();
        $parameter $p = $parameter.of();

        //multi- $proto
        $throws $ts = $throws.of();
        $parameters $ps = $parameters.of();
        $code $cd = $code.of();


        //$expr $expr.of("1");
    }
}
