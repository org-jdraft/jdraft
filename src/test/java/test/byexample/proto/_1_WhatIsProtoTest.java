package test.byexample.proto;

import com.github.javaparser.ast.body.FieldDeclaration;
import junit.framework.TestCase;

import org.jdraft.Ast;
import org.jdraft.Ex;
import org.jdraft._field;
import org.jdraft._method;
import org.jdraft.proto.*;

/**
 * Proto representation org.jdraft.proto.$field
 *     Meta Representation org.jdraft._field
 *         Ast Representation org.github.javaparser.ast.body.FieldDeclaration
 *             String Representation
 *     Runtime Representation java.lang.reflect.Field
 * tags:
 *
 * $proto.$
 * $proto($expr) IntLiteral
 * $proto($node) composition
 * $proto($expr) query
 * $proto($expr) composition
 * $proto($expr) listIn()
 * $proto($expr) $hasParent()
 */
public class _1_WhatIsProtoTest extends TestCase {

    /**
     * the proto abstraction is a level of abstraction above the meta-representation.
     * For example a meta-representation of a field :
     * _field _f = _field.of("int i;");
     * ...represents a (single) field that is EXACTLY "int" type and name "i"
     * (with no modifiers, no annotations, no javadoc, no initial value)
     */
    public void test_MetaRepresentationV$ProtoRepresentation(){

        // a meta-representation of a _field (_f):
        //...represents a SPECIFIC field that is EXACTLY "int" type and name "i"
        //(ALSO with no modifiers, no annotations, no javadoc, no initial value)
        //meta-representations are EXPLICITLY PRECISE, meaning they are EXACT in specification
        _field _f = _field.of("int i;");

        // a prototype-representation of a field ($f)
        // ...represents ANY field that has the type "int" and name "i"
        // (it MAY have ANY modifiers, ANY annotations, ANY javadoc, ANY initial value)
        // prototype-representations are IMPLICITLY LENIENT, meaning:
        // they match SIMILAR VARIANTS provided there is NO CONFLICT
        //... so you can say the prototype below MATCHES/EXPECTS int and i, (for type and name)
        // AND ACCEPTS any VARIANT for any other properties (annos, javadoc, modifiers, init value)
        $field $f = $field.of("int i;");

        //For categorization:
        // we can ask the prototype if any String representation,
        // AST representation(FieldDeclaration), or meta-representation (_field) matches the
        // prototype instance by using match(...) or matches(...)
        assertTrue( $f.matches("int i;")); //the string representation matches the prototype
        FieldDeclaration astField = Ast.field("int i;"); //Ast representation
        assertTrue( $f.matches( astField)); //the AST representation matches the prototype
        assertTrue( $f.matches(_f) ); //the meta-representation matches the prototype

        //...$f doesn't only match a single explicit field representation
        // but other representations (by default, prototypes are "IMPLICITLY LENIENT")
        // or another way of putting it: it will match UNLESS YOU EXPLICITLY SPECIFY what to NOT MATCH.

        //in this case, the proto representation $f matches against ANY field declaration with name "i" and type "int"
        assertTrue( $f.matches("int i = 0;")); //with init
        assertTrue( $f.matches("public static final int i;")); //with modifiers
        assertTrue( $f.matches("public static final int i = 0;")); //with modifiers & init
        assertTrue( $f.matches("/** javadoc */ @ann public static final int i = methodCall(5+4);")); //

        //while _f (the meta representation) will only match fields that are EXACTLY name "i" & type "int"
        //(meta-representation perform matching (to String representations) using the is(...) method
        assertTrue( _f.is("int i;")); //string exact matches
        assertTrue( _f.is(Ast.field("int i;"))); //FieldDeclaration exact matches
        assertTrue( _f.equals(_field.of("int i;"))); //another _field exact matches
        //... (AND NOTHING ELSE)
        assertFalse( _f.is("int i = 0;"));
        assertFalse( _f.is("public static final int i;"));
        assertFalse( _f.is("public static final int i = 0;"));
        assertFalse( _f.is("/** javadoc */ @ann public static final int i = methodCall(5+4);"));
    }

    //Prototypes are IMPLICITLY LENIENT by default,
    // so the LESS constraints we provide the MORE a prototype will match against candidates
    // prototypes with NO constraints are called "MatchAny" prototypes
    //and we can be very granular as to HOW LENIENT we want to be (what we "match against").
    public void testProtoMatchAny(){

        /**
         * $node represents ANY ast node...
         * All entities that make up Java code are Ast Nodes all inherit from {@link com.github.javaparser.ast.Node}
         * (i.e. {@link com.github.javaparser.ast.expr.Expression}, //any expression
         * {@link com.github.javaparser.ast.expr.BinaryExpr), //a binary expression
         * {@link com.github.javaparser.ast.expr.BinaryExpr), //a binary expression
         */
        $node $anyNode = $node.of();

        //...from a simple atomic IntLiteral ("1")
        assertTrue($anyNode.match( Ex.of(1) ));

        //...to a 100+ line composite CompilationUnit made of other nodes
        assertTrue($anyNode.match( Ast.of(_1_WhatIsProtoTest.class) ));

        //...prototypes instanced that will match ANY implementation are "special"
        // they are called "matchAny" prototypes, you can find out if a
        // matchAnyPrototype by calling isMatchAny()
        assertTrue( $anyNode.isMatchAny() );

        // matchAny prototypes are also used for matching any nodes of a certain type/category
        // for example: the building blocks of most java code are "Expressions" which are
        // implementations of {@link com.github.javaparser.ast.expr.Expression}
        $ex $anyExpression = $ex.of();
        assertTrue( $anyExpression.isMatchAny() ); //matches any expression
        assertTrue( $anyExpression.matches("1") ); //match an int literal
        assertTrue( $anyExpression.matches("()->System.out.println(1)")); //a lambda expression

        //match any statement
        $stmt $anyStmt = $stmt.of();
        assertTrue( $anyStmt.isMatchAny() );
        assertTrue( $anyStmt.matches("assert (true); ") );
        assertTrue( $anyStmt.matches("return 100") );

        //match any Field
        $field $anyField = $field.of();

        //todo remove after fix
        assertTrue( $anyField.constraint.test(null));

        /*
        assertTrue( $anyField.init.isMatchAny() );
        assertTrue( $anyField.annos.isMatchAny() );
        assertTrue( $anyField.type.isMatchAny() );
        assertTrue( $anyField.javadoc.isMatchAny() );
        assertTrue( $anyField.name.isMatchAny() );
        assertTrue( $anyField.modifiers.isMatchAny() );
        */
        assertTrue( $anyField.isMatchAny());
        assertTrue( $anyField.matches("int i;"));
        assertTrue( $anyField.matches("public static final int i=213;"));

        //match any Method
        $method $anyMethod = $method.of();
        assertTrue( $anyMethod.isMatchAny() );
        assertTrue( $anyMethod.matches("public void m();") );

        //match any Class
        $class $anyClass = $class.of();
        assertTrue( $anyClass.isMatchAny());
        assertTrue( $anyClass.matches("public class AnyClass{}") );
    }

    /**
     * Composite Prototypes (ones with multiple parts) can be built by their component
     */
    public void testProtoFromComponentParts(){
        //a meta-representation of a simple getter method
        _method _getX = _method.of(new Object(){
            public int getX(){
                return this.x;
            }
            int x;
        });

        System.out.println( _getX );

        //the following $method proto representations will match it
        assertTrue( $method.of().matches(_getX) ); //anyMatch $method will match it of course
        assertTrue( $method.of($.PUBLIC).matches(_getX) ); //match only public methods
        assertTrue( $method.of($.NOT_STATIC).matches(_getX) ); //match only NON-static methods
        assertTrue( $method.of($name.of("getX")).matches(_getX) ); //method with name matching stencil get$Name$
        assertTrue( $method.of($name.of("get$Name$")).matches(_getX) ); //method with name matching stencil get$Name$
        assertTrue( $method.of( $typeRef.of(int.class)).matches(_getX) ); //methods with int return type
        assertTrue( $method.of( $body.of("return this.$n$;")).matches(_getX)); //with specific body

        assertFalse( $method.of($.PRIVATE ).matches(_getX));
        assertFalse( $method.of($name.of("getY")).matches(_getX));
        assertFalse( $method.of($name.of("getY")).matches(_getX));

        $method $m = $method.of( $.PUBLIC, $.NOT_STATIC, $name.of("get$Name$"), $typeRef.of("$type$"), $body.of("return this.$name$;") );
        System.out.println( $m );
        assertTrue($m.matches(_getX));

        //a prototype via stencil
        assertTrue( $method.of(new Object(){
            public int get$Name$(){
                return this.$name$;
            }
            int $name$;
        }).matches(_getX) );
    }

    /**
     * prototypes are mutable, constraints and modifications can be changed after construction
     */
    public void test$andConstraint(){
        $field $f = $field.of( "int i;");

        assertTrue($f.matches("int i;"));
        assertTrue($f.matches("static int i;"));
        assertTrue($f.matches("@A(1) public static int i;"));

        /* Modify $f (only match fields that have an init) */
        $f.$and(f -> f.hasInit() ); //modify the prototype $f to be more strict

        assertFalse($f.matches("int i;")); //no init
        assertFalse($f.matches("static int i;")); //no init
        assertFalse($f.matches("@A(1) public static int i;")); //no init

        assertTrue( $f.matches("int i=0;"));
    }


    public void testDollarPrefix(){
        // prototypes are easily recognized by the $ prefix
        // a prototype field ($field) is easy to distinguish
        // ...from a meta-representation of a field (_field)
        // ...from a AST representation of a field (FieldDeclaration)
        // ...from a Runtime Reflective representation of a field ( Field)

        //by convention, we use the $ prefix as the variable name($f), so it is easy to distinguish


    }

    @interface LitAnno{
        int value();
        String name() default "provided";
    }

    public void testProtoQuery(){
        class MostlyIntLiterals {
            int negative = -1;

            @LitAnno(0)
            int odd = 1;
            int even = 2;
            int addition = 3 + 4;
            int[] arr = {5,6,7,8,9,10};
            String name = "StringLiteral";

            public void inMatch( int a){
                a+=11;
                //assertTrue( -a );
            }
        }

        //this will print each node (from the CompilationUnit to each expression)
        System.out.println( $.of().listIn(MostlyIntLiterals.class));

        // $.expr() represents ALL expressions (Note: some expressions are composed of other expressions)
        //[-1, 1, @LitAnno(0), 0, 1, 2, 3 + 4, 3, 4, { 5, 6, 7, 8, 9, 10 }, 5, 6, 7, 8, 9, 10, "StringLiteral", a += 11, a, 11]
        System.out.println( $.expr().listIn(MostlyIntLiterals.class) );

        /**
         * $.literal() represents all {@link com.github.javaparser.ast.expr.LiteralExpr}
         * NOTE the first 1 (should it be -1**)
         * [1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, "StringLiteral", 11]
         */
        System.out.println( $.literal().listIn(MostlyIntLiterals.class) );

        //NOTE the first 1 (should it be -1?)
        //[1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        System.out.println( $.intLiteral().listIn(MostlyIntLiterals.class));

        // ** above the -1 is actually a unaryExpr with two child nodes
        //              UnaryExpr (-1)
        //               /           \
        //   (an Operator "-")  (an IntLiteralExpr "1")

        //lets build a $expr<UnaryExpr> that represents + or - and some int (i.e. "+5", "-3")
        $ex $plusMinusInt = $.unary($.PLUS,$.MINUS) // matches UnaryExpr with either operator
                .$and(u-> u.getExpression().isIntegerLiteralExpr()); //verifies the UnaryExpr expression is an int literal

        //query the class and verify we can find the UnaryExpr -1
        //[-1]
        System.out.println( $plusMinusInt.listIn(MostlyIntLiterals.class));

        //match all int literals THAT ARE NOT children of of a +/- unaryExpression
        $ex $unsignedIntLiteral = $.intLiteral().$hasParent(p -> !$plusMinusInt.match(p) );

        //now combine the $proto matchers for finding:
        // 1) UnaryExpressions with +/- operators and Int literals
        // 2) IntLiterals that ARE NOT children of (1)
        $node $ints = $.of( $plusMinusInt, $unsignedIntLiteral );

        //here we use the prototype to list the numbers in order
        //[-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        System.out.println( $ints.listIn(MostlyIntLiterals.class));
    }
}
