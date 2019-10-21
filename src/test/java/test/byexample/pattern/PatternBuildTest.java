package test.byexample.pattern;

import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import junit.framework.TestCase;
import org.jdraft.pattern.*;

/**
 * WHAT are $protos & HOW to build $protos
 * $proto are models to represent a certain "category" of Ast Node that can be drafted (built)
 * or used to
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
 * MODIFYING/REFINING $PROTOS (after creation)
 *     $(...) parameterize
 *     hardCode$(...) hardcode parameter(s)
 *     and( Constraint )
 *     $name()
 *     $XXX()
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
public class PatternBuildTest extends TestCase {

    public void testAll$protoTypes(){
        $node $n = $node.of(); //node
        $n = $.of();

        $id $i = $id.of();
        $i = $.id();

        $ex $e = $ex.of(); //specific $expr
        $.ex();
        $stmt $s = $stmt.of(); //specific $stmt
        $.stmt();
        $anno $a = $anno.of();
        $.anno();
        $case $c = $case.of();
        $.switchCase();

        $import $im = $import.of();
        $.importDecl();

        $catch $ca = $catch.of();
        $.catchClause();

        $modifiers $ms = $modifiers.of();
        $.modifiers();
        //$.PUBLIC.listIn()
        $.modifiers( $.PUBLIC, $.STATIC); //compose
        $.modifiers( $.PUBLIC ).$not( $.STATIC);

        $parameter $p = $parameter.of();
        $.parameter();

        $comment $co = $comment.of(); //any comment (line, block, javadoc)
        $.comment();
        $comment<JavadocComment> $jc = $comment.javadocComment(); //any /** javadoc comment */
        $.javadoc();
        $comment<LineComment> $lc = $comment.lineComment(); //any  // line comment
        $.lineComment();

        $.typeParameter();
        $typeParameter.of();

        $comment<BlockComment>$bc = $comment.blockComment(); //any /* block comment */
        $.blockComment();

        $typeRef.of();
        $.typeRef();

        //components
        $.method();

        $.constructor();

        //$.staticBlock();
        $.field();
        $.var();
        $var.of();

        // $at(line, column)
        // $at( line )
        // $at( position )

        // $before(int line)
        // $before(int line,int column)
        // $before(Position);

        // $within(Range range);
        // $between(line, column, line column)
        // $between( $position, $position )
        // $between( node, node);


        //multi- $proto
        $throws $ts = $throws.of();
        $.throwStmt();

        $typeParameters.of();
        $.typeParameters();

        $parameters $ps = $parameters.of();
        $.parameters();

        $stmts $cd = $stmts.of();



        //$expr $expr.of("1");
    }



    static class SC{
        private static final void sc(){}
    }
    public void testMods(){
        class GG{
            public void a(){}

        }
        //count the number of public non-static/non-final methods in the
        assertEquals(1, $method.of( $modifiers.of($.PUBLIC).$not($.STATIC,$.FINAL)).count(GG.class) );
        assertEquals(1, $method.of( $modifiers.of($.PRIVATE, $.STATIC, $.FINAL)).count(SC.class) );
    }
}
