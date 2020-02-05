package test.byexample.pattern;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.pattern.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.function.Predicate;

/**
 * Match Any Prototypes
 *
 * Simple Prototypes
 *
 * How to Composite Prototypes
 *
 * $method.of( $typeRef.of(int.class), $throws.of( RuntimeException.class));
 *
 */
public class PatternExamplesTest extends TestCase {

    static $ex ANY_EXPR = $.ex();                         //any expression

    //static $expr CONST_NAME = $.expr("Name");          //any expression matching pattern "Name"
    static $ex LITERAL =  $.ex(e -> e.ast().isLiteralExpr());   //any literals (ints, floats, Strings, etc.)
    static $ex LITERAL_ = $.literal();                      //any literals (booleans, int, float, String, etc)

    static $ex<IntegerLiteralExpr, _int> INT_LITERAL = $.intLiteral();                   // any int literal
    static $ex<IntegerLiteralExpr, _int> INT_100 = $.intLiteral(100);           // exact int literal 100
    static $ex<IntegerLiteralExpr, _int> INT_VAL_PARAM = $.intLiteral("$val$");    // any int literal (parameterized)
    static $ex<IntegerLiteralExpr, _int> INT_POSITIVE = $.intLiteral(i -> i.intValue() > 0);  // any int literal > 100 (constrained)

    /*
    $.of();                             // ANY AST Node
        $.of(Name.class);                   // any AST Node of class Name
        $.of( "$name$", Name.class);
        $.of("Eric", Name.class);
     */

    public void testNodeExpression(){
        $.of();                             //any node
        $.of(Expression.class);             //any expression
        $.of(SimpleName.class, Name.class); //any Name OR SimpleName node
    }

    public void testExprShorts(){
        class GTH{
             void m(Object r){

                 if( r instanceof String ){
                    String s = (String)r;
                 }
                 if( r instanceof java.lang.String ){

                 }
             }
        }

        assertEquals( 1, $.cast(String.class).count(GTH.class) );
        assertEquals(2, $ex.instanceOfEx(String.class).count(GTH.class));
        assertEquals(2, $ex.instanceOfEx($typeRef.of(String.class)).count(GTH.class));
    }

    public void testMemberExpressions(){

        $.method();                        //any method
        $.method($.PRIVATE);      // private methods
        $.method($name.of("get$Name$"));     // methods named get???()
        $.method(m->m.hasBody());          // methods with a body

        //a method that returns an int and throws RuntimeException

        $.method(); //unspecific (i.e. all methods)

        //specific
        $.method( $typeRef.of(int.class), $parameter.of(String.class),
                $throws.of( IOException.class));

        $.method()
                .$isParent(ClassOrInterfaceDeclaration.class);


        class GG{
            public int getX(){
                return 100;
            }

            public void testLambda(){
                Predicate<Integer> p = (Integer a)-> true;
            }
        }
        assertNotNull( $.method( $name.of("get$Name$") ).firstIn(GG.class));

        //lambdas containing a Integer type parameter
        assertNotNull( $.lambda( ).$hasChild( $parameter.of( $typeRef.of(Integer.class))) );

    }


    public void testN(){

        class G<T extends java.io.Serializable> {
            public int val = 2;

            public void method( int a, int b){
                System.out.println("2");
            }
        }

        assertEquals( 2, $.literal().count(G.class) ); //(2) literals {2,"2"} above
        assertEquals(1, $.of(2).count(G.class)); //there is (1) instance of the int literal {2} above


        //_java.describe(_class.of(G.class));
        //$node $n = $.of(SimpleName.class); //any SimpleName
        $node $n = $.of(SimpleName.class)
                .$and(n-> !Ast.hasAncestor(n, ClassOrInterfaceType.class)); //but not simpleNames making up fully qualified names


        _class _c = _class.of( G.class);
        $n.forEachIn(_c, e-> System.out.println(e.getClass() + " "+e) );


        assertTrue( $parameter.of(int.class).matches(_parameter.of("int param1")) );
        assertTrue( $parameter.of(String.class).matches(_parameter.of("String a")) );

        assertTrue( $parameters.of(int.class, String.class).matches(_parameters.of("int param1, String a")) );
    }

    public void test$ctorProto(){
        $.constructor( $.PRIVATE ); //private constructors

        $.unary( UnaryExpr.Operator.LOGICAL_COMPLEMENT );
    }
    public void test$methodProto(){
        class AMM{
            /**
             * TODO fix it
             * @param param1
             * @param param2
             * @param <T>
             * @return
             * @throws IOException
             */
            @Deprecated
            private <T extends Serializable> int aMatchingMethod( int param1, String param2) throws IOException{
                // @MED change this
                return 4;
            }
        }

        // find all @Deprecated methods
        assertEquals( 1, $.method($anno.of(Deprecated.class)).count(AMM.class) );
        // find all methods with a TODO javadoc comment
        assertNotNull( $.method($.javadoc(j-> j.getContent().contains("TODO") ) ).firstIn(AMM.class) );

        // find all methods that contain any comment (line comment, block comment, javadoc comment)
        // with my "@MED" signature
        assertNotNull( $method.of(m -> !_java.listComments(m, c-> c.getContent().contains("@MED")).isEmpty()).firstIn(AMM.class) );


        $method $m = $.method( $throws.of(IOException.class) );
        assertEquals(1, $m.count(AMM.class));

        //$protos compose... here we create a $method proto
        $m = $.method(
                $anno.of(Deprecated.class),
                $modifiers.of("private"),
                $typeRef.of(int.class),
                $name.of(i-> i.toString().endsWith("Method")),
                $parameters.of(int.class, String.class),
                $typeParameter.of().$hasChild( $typeRef.of(Serializable.class)),
                $throws.of( IOException.class),
                $body.of( (Object $any$)-> {return $any$;} ) );

        System.out.println( $m.toString() );

        assertEquals(1, $m.count(AMM.class));
    }
}
