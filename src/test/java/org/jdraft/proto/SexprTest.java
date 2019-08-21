package org.jdraft.proto;

import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import org.jdraft.Expr;
import org.jdraft._class;
import java.text.NumberFormat;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import org.junit.Assert;

public class SexprTest extends TestCase {

     public void testSpecificFullyQualifiedStaticAssertions(){
        _class _c = _class.of("C", new Object(){
            void m(){
                org.junit.Assert.assertTrue("message", 1==1);
                Assert.assertTrue("message", 1==1);                
                assertTrue("message", 1==1);
            }
        }).imports(Assert.class);
        
        //change the API AND
        //$typeUse.of(org.junit.Assert.class).replaceIn(_c, )
        
        //$oldAssert this will handle:
        //   org.junit.Assert.assertTrue("message", 1==1);
        //   Assert.assertTrue("message", 1==1);
        //   assertTrue("message", 1==1);
        $expr $oldAssert = $expr.of("$scope$assertTrue($message$, $condition$)");
        $expr $newAssert = $expr.of("Assertions.assertTrue($condition$, $message$)");
        
        $oldAssert.replaceIn(_c, $newAssert);
        
        //Expression exp = Expr.of("Assertions.assertTrue(1==1,\"message\")");
        //System.out.println( exp );
        $expr $e = $expr.of("Assertions.assertTrue(1==1,\"message\")");
        assertTrue($e.listIn(_c).size() == 3);
    }
     
    public static final NumberFormat NF = NumberFormat.getInstance();
    
    
    public void testFloatLiteral(){
        class FF{
            float f = 3.14f;
            double d = 3.145;
        }
        assertEquals(1, $.of(3.14f).count(FF.class));
    }
    
    public void testDoubleLiteral(){
        class DD{
            double d = 3.14;
            float f = 3.1f;
        }        
        assertEquals(1, $.of(3.14).count(DD.class));
    }
    
    public void testNumberLiteralsFloatDouble(){
        class AClass{
            float ff = 3.14f;
            float fF = 3.14F;
            double d = 3.14;
            double dd = 3.14d;
            double dD = 3.14D;            
        }
        //doesnt matter if you use the F/f/d/D postfixes, we can match
        assertEquals(5, $expr.of("3.14").count(AClass.class));
        assertEquals(5, $expr.of("3.14f").count(AClass.class));
        assertEquals(5, $expr.of("3.14F").count(AClass.class));
        assertEquals(5, $expr.of("3.14d").count(AClass.class));
        assertEquals(5, $expr.of("3.14D").count(AClass.class));        
    }
    
    public void testNumLiteralsIntsHexBin(){
        class IntClass{
            int i = 1;
            int ib = 0b1;
            int ibz = 0b01;
            int ih = 0x1;
            int ihz = 0x01;
        }
        assertEquals(5, $expr.of("1").count(IntClass.class));
        assertEquals(5, $expr.of("0b1").count(IntClass.class));
        assertEquals(5, $expr.of("0b01").count(IntClass.class));
        assertEquals(5, $expr.of("0x1").count(IntClass.class));
        assertEquals(5, $expr.of("0x01").count(IntClass.class));        
    }
    
    public void testNumLiteralsLongsHexBin(){
        class LongClass{
            long l = 1;
            long lb = 0b1;
            long lbz = 0b01;
            long lh = 0x1;
            long lhz = 0x01;
            
            long lL = 1L;
            long lLb = 0b1L;
            long lLbz = 0b01L;
            long lLh = 0x1L;
            long lLhz = 0x01L;
            
            
            long ll = 1L;
            long llb = 0b1L;
            long llbz = 0b01L;
            long llh = 0x1L;
            long llhz = 0x01L;
        }
        assertEquals(15, $expr.of("1").count(LongClass.class));
        assertEquals(15, $expr.of("0b1").count(LongClass.class));
        assertEquals(15, $expr.of("0b01").count(LongClass.class));
        assertEquals(15, $expr.of("0x1").count(LongClass.class));
        assertEquals(15, $expr.of("0x01").count(LongClass.class));        
        
        assertEquals(15, $expr.of("1L").count(LongClass.class));
        assertEquals(15, $expr.of("0b1L").count(LongClass.class));
        assertEquals(15, $expr.of("0b01L").count(LongClass.class));
        assertEquals(15, $expr.of("0x1L").count(LongClass.class));
        assertEquals(15, $expr.of("0x01L").count(LongClass.class));        
        
        assertEquals(15, $expr.of("1l").count(LongClass.class));
        assertEquals(15, $expr.of("0b1l").count(LongClass.class));
        assertEquals(15, $expr.of("0b01l").count(LongClass.class));
        assertEquals(15, $expr.of("0x1l").count(LongClass.class));
        assertEquals(15, $expr.of("0x01l").count(LongClass.class));
    }
    
    /**
     * Had to fix a problem in integer.parseInt("1_000")
     * its like, if you are going to support it, then support it
     */
    public void testNumbersThousandsSeparator(){
        assertTrue( $expr.of(1_000_000).matches("1000000") );
        assertTrue( $expr.of(1000000).matches("1_000_000") );
    }
    
    public void testLiteralsWithPrefix(){
        
        
        Number f2 = Expr.parseNumber("3.14F");
        Number f3 = Expr.parseNumber("3.14f");
        
        Number d = Expr.parseNumber("3.14");
        Number d1 = Expr.parseNumber("3.14d");
        Number d2 = Expr.parseNumber("3.14D");
        
        assertEquals( f2, f3);        
        assertEquals( d1, d2);
        
        int one = 1;
        int hex = 0x01;
        int Hex = 0X01;
        int binary = 0b1;
        int Binary = 0B1;
        
         
        int ihex = 0xFFFFFFFF;
        long lhex = 0xFFFFFFFFFFFFFFFFL;
        
        int ibin = 0b11111111111111111111111111111111;
        long lbin = 0b1111111111111111111111111111111111111111111111111111111111111111L;
        assertEquals( hex, one);
        assertEquals( one, binary);
        assertEquals( hex, binary);
        assertEquals( Hex, binary);
        assertEquals( Hex, Binary);
        assertEquals( Hex, one);
        assertEquals( Binary, binary);
        
        assertTrue( $expr.compareNumberLiterals( "1", "0x1"));
        assertTrue( $expr.compareNumberLiterals( "1", "0X1"));
        assertTrue( $expr.compareNumberLiterals( "1", "0b1"));
        assertTrue( $expr.compareNumberLiterals( "1", "0B1"));
        
        assertTrue( $expr.compareNumberLiterals( "15", "0xF"));
        assertTrue( $expr.compareNumberLiterals( "15", "0XF"));
        
        //assertEquals( 1, $expr.parseNumber("0x1"));
        
        //assertEquals( 1, $expr.parseNumber("0x1"));
        //assertEquals( 15, $expr.parseNumber("0xF"));
        //assertEquals( 1, $expr.parseNumber("0x01")); 
        //assertEquals( 1, $expr.parseNumber("0b01"));
        
        assertEquals( Expr.parseNumber("0x01"), Expr.parseNumber("0b01") );
        
        System.out.println( Expr.parseNumber("1").getClass() );
        System.out.println( Expr.parseNumber("0x01").getClass() );
        
        //assertEquals( $expr.parseNumber("0x01"), $expr.parseNumber("1") );
        //assertEquals( $expr.parseNumber("0X01"), $expr.parseNumber("0B01") );
        
        
        
        IntegerLiteralExpr eone = new IntegerLiteralExpr("1");
        IntegerLiteralExpr ehex = new IntegerLiteralExpr("0x01");
        IntegerLiteralExpr ebin = new IntegerLiteralExpr("0b01");
        IntegerLiteralExpr eHex = new IntegerLiteralExpr("0X01");
        IntegerLiteralExpr eBin = new IntegerLiteralExpr("0B01");
        
        $expr<IntegerLiteralExpr> $one = $expr.intLiteral("1");
        $expr<IntegerLiteralExpr> $hex = $expr.intLiteral("0x01");
        $expr<IntegerLiteralExpr> $bin = $expr.intLiteral("0b01");
        $expr<IntegerLiteralExpr> $Hex = $expr.intLiteral("0X01");
        $expr<IntegerLiteralExpr> $Bin = $expr.intLiteral("0B01");
        
        assertTrue($one.matches("1"));
        assertTrue($one.matches("0x01"));
        assertTrue($one.matches("0b01"));
        assertTrue($one.matches("0X01"));
        assertTrue($one.matches("0B01"));
        
        
        assertTrue($hex.matches("1"));
        assertTrue($hex.matches("0x01"));
        assertTrue($hex.matches("0b01"));
        assertTrue($hex.matches("0X01"));
        assertTrue($hex.matches("0B01"));
        
        assertTrue($bin.matches("1"));
        assertTrue($bin.matches("0x01"));
        assertTrue($bin.matches("0b01"));        
        assertTrue($bin.matches("0X01"));
        assertTrue($bin.matches("0B01"));        
        
        assertTrue($Hex.matches("1"));
        assertTrue($Hex.matches("0x01"));
        assertTrue($Hex.matches("0b01"));
        assertTrue($Hex.matches("0X01"));
        assertTrue($hex.matches("0B01"));
        
        assertTrue($Bin.matches("1"));
        assertTrue($Bin.matches("0x01"));
        assertTrue($Bin.matches("0b01"));        
        assertTrue($Bin.matches("0X01"));
        assertTrue($Bin.matches("0B01"));                
    }
    
    public void testFloatAndDouble(){
        //assertEquals( new DoubleLiteralExpr("3.14F"), new DoubleLiteralExpr("3.14f") );
        
        $expr<DoubleLiteralExpr>d = $expr.of( new DoubleLiteralExpr("3.14") );
        $expr<DoubleLiteralExpr>dd = $expr.of( new DoubleLiteralExpr("3.14d") );
        $expr<DoubleLiteralExpr>dD = $expr.of( new DoubleLiteralExpr("3.14D"));
        
        assertTrue( d.matches("3.14") );
        assertTrue( d.matches("3.14d") );
        assertTrue( d.matches("3.14D") );
        
        assertTrue( dd.matches("3.14") );
        assertTrue( dd.matches("3.14d") );
        assertTrue( dd.matches("3.14D") );
        
        assertTrue( dD.matches("3.14") );
        assertTrue( dD.matches("3.14d") );
        assertTrue( dD.matches("3.14D") );
        
        
        $expr<DoubleLiteralExpr>f = $expr.of( new DoubleLiteralExpr("3.14") );
        $expr<DoubleLiteralExpr>fd = $expr.of( new DoubleLiteralExpr("3.14f") );
        $expr<DoubleLiteralExpr>fD = $expr.of( new DoubleLiteralExpr("3.14F"));
        
        assertTrue( d.matches("3.14") );
        assertTrue( d.matches("3.14f") );
        assertTrue( d.matches("3.14F") );
        
        assertTrue( dd.matches("3.14") );
        assertTrue( dd.matches("3.14f") );
        assertTrue( dd.matches("3.14F") );
        
        assertTrue( dD.matches("3.14") );
        assertTrue( dD.matches("3.14f") );
        assertTrue( dD.matches("3.14F") );        
    }
    
    
    public void testCase(){
        class DD{
            String t = "101";
        
            public void tt(){
                switch(t) {
                    case "+":  //<===add hello world
                        System.out.println("hello");
                        break;
                    case "/":   
                        break;
                }
            }
        }
        _class _c = _class.of( DD.class);
        
        _c.ast().walk(Expression.class, e-> System.out.println(e+ " "+e.getClass() ) );
        
        _c.ast().walk(Statement.class, e-> System.out.println(e+ " "+e.getClass() ) );
        
        SwitchStmt ss = $stmt.switchStmt().firstIn(DD.class);
        List<SwitchEntry> ses = ss.getEntries();
        
    }
    
    static class Base{
        public static int V = 100;
        public int val() {
            return 101;
        }
    }
    class Derived extends Base{
        public int val(){
            return super.val();
        }
        public int tVal(){
            return this.val();
        }
    }
    
    public void testSuperThis(){
        _class _c = _class.of(Derived.class);
        $expr.thisExpr().listIn(_c);
        $expr.superExpr().listIn(_c);
    }
    
    public void testGenericExpr(){
        //LocalClassDeclarationExpr lc =  Expr.("class $any${}");
        
        //find EVERY lambda with a comment
        $expr $anyLambda = $expr.of("($params$)->$body$", e -> e.getComment().isPresent() );
        
        System.out.println( Expr.lambda("/** comment */ ()->true") );
        
        assertTrue( $anyLambda.matches( Expr.lambda("/** comment */ ()->true") ) );
        
        assertTrue( $expr.lambda(l -> l.getComment().isPresent() ).matches("/** comment */ ()->true;") );
        
        /** A comment */
        //lass C{}
        
        //this disregards comments
        //StaticJavaParser.parseExpression(expression)
        
        
        
        //_class _c = _class.of($exprTest.class);
        //assertNotNull( $anyLocal.firstIn(_c) );
        
    }
    public void testStatic$expr(){
        _class _c = _class.of("C", new Object(){
            @aa(2) 
            int a = 1;
            int b = 3 + 4;
        });        
        assertEquals( 1, $expr.of(2).listIn(_c).size());
        assertEquals( 4, $expr.of(2).$("2", "number").listIn(_c).size());
    }
    
    
    
    @interface aa{
        int value();
    }

    public void test$exprPostParameterize(){
        //a template expression
        $expr $e = $expr.of("1 + 2");
        //post parameterize the + as an operator
        $e.$("+", "op");

        //verify I can match against other expressions
        assertTrue($e.matches("1 + 2"));
        assertTrue($e.matches("1 * 2"));
        assertTrue($e.matches("1 / 2"));
        assertTrue($e.matches("1 >> 2"));
        assertTrue($e.matches("1 << 2"));

        //works for expressions obviously
        assertTrue($e.matches(Expr.of("1 + 2")));

        //select returns the expression and
        assertNotNull($e.select(Expr.of("1 + 2")));

        //select returns the selected tokens
        assertTrue($e.select(Expr.of("1 * 2")).tokens.is("op", "*"));

        $e = $expr.binary("$a$ + $b$");
        @aa(1 + 2)
        class G{
            int f = 3 + 5;

            @aa(8+9)
            int g = 6 + 7;

        }
        _class _c = _class.of(G.class);
        assertEquals(4, $e.listSelectedIn(_c).size());
    }

    public void testExprOf(){
        $expr $e = $expr.of("1 + 2");
        assertEquals( $e.draft(), Expr.of("1 + 2"));
        assertTrue( $e.matches(Expr.of("1+2")));

        $e = $expr.of("$a$ + $b$");
        assertTrue( $e.matches(Expr.of("1+2")));
    }

    public void testExprTypes(){
        boolean var = true;

        assertTrue( $expr.assign("$var$=100").matches("y=100"));
        assertTrue($expr.arrayAccess("arr[$index$]").matches("arr[0]"));
        assertTrue( $expr.arrayCreation("new $arr$[][]").matches("new xy[][]"));
        assertTrue($expr.arrayInitializer("{$any$}").matches("{1,2,3}"));
        assertTrue($expr.binary("$left$ > $right$").matches("a > b"));
        assertTrue($expr.of(true).matches("true"));
        assertTrue($expr.booleanLiteral(true).matches("true"));
        assertTrue($expr.of('c').matches( "'c'"));
        assertTrue($expr.cast("($type$)o").matches("(String)o"));
        assertTrue($expr.classExpr("$type$.class").matches("String.class"));
        assertTrue( $expr.conditional("($left$ < $right$) ? $left$ : $right$;").matches("(a < b) ? a : b;"));
        assertEquals( Expr.of(3.14f), Expr.of(3.14f));
        assertEquals( Expr.of(12.3), Expr.of("12.3"));
        assertEquals( Expr.of("12.3f"), Expr.of("12.3f"));
        assertTrue($expr.doubleLiteral(12.3).matches("12.3"));
        assertTrue($expr.doubleLiteral("12.3f").matches("12.3f"));
        assertTrue($expr.enclosedExpr("($a$ + $b$)").matches("(100 + 200)"));
        assertTrue($expr.fieldAccess("my.$field$").matches("my.a"));
        assertTrue($expr.intLiteral(100).matches("100"));
        assertTrue($expr.of(100).matches("100"));
        assertTrue($expr.instanceOf("$obj$ instanceof String").matches("a instanceof String"));
        assertTrue( $expr.longLiteral(100).matches( Expr.longLiteral( "100")));
        assertTrue($expr.lambda("$param$ -> a.$method$()").matches("x-> a.toString()"));
        assertTrue($expr.methodCall("$methodCall$($params$)").matches("a()"));
        assertTrue($expr.methodCall("$methodCall$($params$)").matches("a(1,2,3)"));
        assertTrue($expr.methodReference("$target$::$methodName$").matches("String::toString"));
        assertTrue($expr.nullExpr().matches("null"));
        assertTrue($expr.name("eric").matches("eric"));
        assertTrue($expr.objectCreation("new $Object$()").matches("new String()"));
        assertTrue($expr.objectCreation("new $Object$($params$)").matches("new Date(101010)"));
        assertTrue($expr.stringLiteral("Hello $name$").matches("\"Hello Eric\""));
        assertTrue($expr.superExpr().matches("super"));
        assertTrue( $expr.thisExpr().matches("this"));
        assertTrue( $expr.typeExpr("$type$").matches(Expr.typeExpr("AType")));
        assertTrue( $expr.unary("!$var$").matches("!isDead"));
        assertTrue($expr.varLocal("int $var$").matches("int x"));

        //assertEquals(0b1010000101000101101000010100010110100001010001011010000101000101L, 
        //        0b1010000101000101101000010100010110100001010001011010000101000101L);
        
        //Long.parseUnsignedLong("1010000101000101101000010100010110100001010001011010000101000101", 2);
        
        //Long.parseLong("1010000101000101101000010100010110100001010001011010000101000101", 2);
        
        $expr $e = $expr.longLiteral("0b0010000101000101101000010100010110100001010001011010000101000101L");
        assertEquals( $e.expressionClass, LongLiteralExpr.class);
        System.out.println("PATTERN" + $e.exprStencil);
        
        LongLiteralExpr lle = (LongLiteralExpr)Expr.of("0b0010000101000101101000010100010110100001010001011010000101000101L");
        System.out.println("VALUE  " + lle.getValue() );
        assertTrue( $e.matches(lle) );
        assertTrue( 
            $expr.of("0b0010000101000101101000010100010110100001010001011010000101000101L")
            .matches("0b0010000101000101101000010100010110100001010001011010000101000101L"));
    }

    public void testSelect(){
        $expr<IntegerLiteralExpr> e = $expr.intLiteral("1").$("1", "val");
        assertTrue(e.matches("1"));
        class C{
            public void f(){
                int i = 1;
                System.out.println(2);
                System.out.println("A2");
                assert( 3== 4);
                System.out.println("multiple"+ 5);
            }

            public void g(){
                System.out.println("another method"+6+" values");
            }
        }
        List<$expr.Select<IntegerLiteralExpr>> sel =  e.listSelectedIn( _class.of(C.class) );
        assertEquals(6, sel.size()); //verify that I have (6) selections

        //System.out.println(">>"+ sel.get(0).tokens );
        assertTrue(sel.get(0).tokens.is("val", "1"));
        assertTrue(sel.get(1).tokens.is("val", "2"));
        assertTrue(sel.get(2).tokens.is("val", "3"));
        assertTrue(sel.get(3).tokens.is("val", "4"));
        assertTrue(sel.get(4).tokens.is("val", "5"));
        assertTrue(sel.get(5).tokens.is("val", "6"));

        //use forAllIn to
        List<Integer>ints = new ArrayList<>();
        e.forEachIn(_class.of(C.class), ie -> ints.add(ie.asInt()));
        assertTrue( ints.contains(1) && ints.contains(2) && ints.contains(3) && ints.contains(4) && ints.contains(5) && ints.contains(6));
    }
    
    public void testAPI(){
        
        assertTrue($expr.arrayAccess(a -> a.getIndex().isIntegerLiteralExpr() ).matches("a[1]"));
        assertTrue($expr.arrayAccess("a[$any$]", a -> a.getIndex().isIntegerLiteralExpr() ).matches("a[1]"));
        assertFalse($expr.arrayAccess("a[$any$]", a -> a.getIndex().isIntegerLiteralExpr() ).matches("a[b.count()]"));
        _class _c = _class.of("C").field("int i=1;");
        
        assertEquals(1, $expr.of(1).listIn(_c ).size());
        
        System.out.println( Expr.of("1").getClass() );
        
        System.out.println( $expr.of(1).replaceIn(_c, Expr.of(2)) );

        System.out.println( $expr.of("1").replaceIn(_c, "2") );
        
        assertTrue( $expr.of("1").replaceIn(_c,"2").getField("i").isInit(2));
        
        //assertTrue( $expr.replace(_c, "1", "2").getField("i").initIs("2"));
        
        assertTrue($expr.of("2").listIn(_c).size() == 1);
        
        //assertTrue( $expr.list(_c, "2").size() == 1 );
        
        //look for every literal
        $expr $bin = 
            $expr.binary("$a$ > $b$", 
                b-> b.getLeft().isIntegerLiteralExpr() && b.getRight().isIntegerLiteralExpr());
        assertTrue($bin.matches("3 > 2"));
        assertFalse($bin.matches("3L > 2"));
        
        
        
        /*
        assertTrue( 
                $expr.of("a[$any$]")
                .constraint( (ArrayAccessExpr a) -> a.getIndex().isIntegerLiteralExpr() ).matches("a[1]"));
        */
    }
    
     public void testSelectlistQuery(){
        
        class C{
            int i = 1;
            int j = 2;            
            
        }             
        _class _c = _class.of(C.class);
        assertNotNull( $expr.intLiteral("2").firstIn(_c));        
        assertNotNull( $expr.intLiteral(1).firstIn(_c));                
        
        Predicate<IntegerLiteralExpr> p = (IntegerLiteralExpr i)-> i.asInt() % 2 == 1;
        $expr.intLiteral( p );
        
        assertNotNull( $expr.intLiteral( (i)-> i.asInt() % 2 == 1 ).firstIn(_c)); 
               
        $expr $e = $expr.of(1).$("1", "num");
        
    }
}
