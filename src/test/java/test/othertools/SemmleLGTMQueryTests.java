package test.othertools;

import junit.framework.TestCase;
import org.jdraft.Statements;
import org.jdraft.pattern.*;

import java.util.ArrayList;
import java.util.List;

/**
 * https://blog.semmle.com/secure-software-github-semmle/
 *
 */
public class SemmleLGTMQueryTests extends TestCase {

    /**
     * https://lgtm.com/query
     *
     * Semmle LGTM query:
     * Basic query: Array access
     * Finds array access expressions with an index expression consisting of a unary assignment.
     *
     * from ArrayAccess a
     * where a.getIndexExpr() instanceof UnaryAssignExpr
     * select a
     *
     */
    public void testQueryArrayAccessIntoUnaryExpression(){
        $ex $e = $ex.arrayAccessEx().$hasChild( $ex.unaryEx() );
        //now use it
        assertNotNull($e.firstIn( Statements.of("$buffer[position++] = (byte)(value >>> 8);") ));
    }

    /**
     * Semmle LGTM query <A HREF="https://lgtm.com/query/">online</A>:
     * import java
     *
     * from Method m
     * where m.getReturnType() instanceof VoidType
     * select m
     *
     */
    public void testQueryReturnTypeVoid(){
        $method $m = $method.of( $typeRef.VOID );
        class C{
            void m(){ }
        }
        assertNotNull($m.firstIn(C.class));
    }

    /**
     * import java
     *
     * from ReturnStmt r
     * where r.getResult() instanceof NullLiteral
     * select r
     */
    public void testReturnNullStatements(){
        $stmt $s = $.returnStmt("return null;");
        class C{
            Object oo(){
                return null;
            }
        }
        assertNotNull($s.firstIn(C.class));
    }

    /**
     * TODO I can make this better
     *
     */
    public void testAContainerNeverUsed(){
        //TODO make $typeRef constructor for many Classes
        $typeRef $containerType = $typeRef.of(List.class);


        //another

        //an outside access to the field
        $ex $outsideFieldAccess = $.fieldAccessExpr("$className$.$fieldName$");

        //$reflectivelyRead ??

        //omit private fields, & fields with
        //$field means only (member)fields (not $vars or $params)
        $field $f = $field.of( $containerType ) //all fields that are "containers" (i.e. collections, list, map, HashSet, LinkedList)
                .$not(
                        $.PRIVATE,
                        $anno.of("@SuppressWarnings(\"unused\")"),
                        $anno.of("@lombok.Getter") //could make this "better" by passing in Lombok class
                );
                //and the parent isnt a private
                //.$not(v-> Ast.isParent(v, FieldDeclaration.class) ); //a local var (not a field or parameter)

        class SomeClass{
            List v = new ArrayList<>();
            private List l = new ArrayList();
            @SuppressWarnings("unused")
            List t = new ArrayList();

            void some(){
                v.get(0);
            }
        }
        //all methodCalls with scope
        $.ex("v").printIn(SomeClass.class);
        //$$.methodCall().$and(m -> m.getScope().isPresent() && m.getScope().get().).printIn(SomeClass.class);

        //gets the list of all container classes
        //$f.printIn(SomeClass.class);

        //$field.of($typeRef.of(List.class)).$not($.PRIVATE).printIn(SomeClass.class);
        //$field.of($typeRef.of(List.class)).$not($.PRIVATE, $anno.of("Getter")).printIn(SomeClass.class);
        $field.of($typeRef.of(List.class)).$not($.PRIVATE, $anno.of("@SuppressWarnings(\"unused\")")).printIn(SomeClass.class);
        //$field.of( $containerType )
    }
}
