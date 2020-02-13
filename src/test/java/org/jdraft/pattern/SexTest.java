package org.jdraft.pattern;

import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import org.jdraft.*;

import java.text.NumberFormat;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import org.junit.Assert;

public class SexTest extends TestCase {

     public void testSpecificFullyQualifiedStaticAssertions(){
        _class _c = _class.of("C", new Object(){
            void m(){
                org.junit.Assert.assertTrue("message", 1==1);
                Assert.assertTrue("message", 1==1);                
                assertTrue("message", 1==1);
            }
        }).addImports(Assert.class);
        
        //change the API AND
        //$typeUse.of(org.junit.Assert.class).replaceIn(_c, )
        
        //$oldAssert this will handle:
        //   org.junit.Assert.assertTrue("message", 1==1);
        //   Assert.assertTrue("message", 1==1);
        //   assertTrue("message", 1==1);
        $ex $oldAssert = $ex.of("$scope$assertTrue($message$, $condition$)");
        $ex $newAssert = $ex.of("Assertions.assertTrue($condition$, $message$)");
        
        $oldAssert.replaceIn(_c, $newAssert);
        
        //Expression exp = Expr.of("Assertions.assertTrue(1==1,\"message\")");
        //System.out.println( exp );
        $ex $e = $ex.of("Assertions.assertTrue(1==1,\"message\")");
        assertTrue($e.listIn(_c).size() == 3);
    }
     
    public static final NumberFormat NF = NumberFormat.getInstance();
    
    
    public void testFloatLiteral(){
        class FF{
            float f = 3.14f;
            double d = 3.145;
        }
        assertEquals(1, $.of(3.14f).countIn(FF.class));
    }
    
    public void testDoubleLiteral(){
        class DD{
            double d = 3.14;
            float f = 3.1f;
        }        
        assertEquals(1, $.of(3.14).countIn(DD.class));
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
        assertEquals(5, $ex.of("3.14").countIn(AClass.class));
        assertEquals(5, $ex.of("3.14f").countIn(AClass.class));
        assertEquals(5, $ex.of("3.14F").countIn(AClass.class));
        assertEquals(5, $ex.of("3.14d").countIn(AClass.class));
        assertEquals(5, $ex.of("3.14D").countIn(AClass.class));
    }
    
    public void testNumLiteralsIntsHexBin(){
        class IntClass{
            int i = 1;
            int ib = 0b1;
            int ibz = 0b01;
            int ih = 0x1;
            int ihz = 0x01;
        }
        assertEquals(5, $ex.of("1").countIn(IntClass.class));
        assertEquals(5, $ex.of("0b1").countIn(IntClass.class));
        assertEquals(5, $ex.of("0b01").countIn(IntClass.class));
        assertEquals(5, $ex.of("0x1").countIn(IntClass.class));
        assertEquals(5, $ex.of("0x01").countIn(IntClass.class));
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
        assertEquals(15, $ex.of("1").countIn(LongClass.class));
        assertEquals(15, $ex.of("0b1").countIn(LongClass.class));
        assertEquals(15, $ex.of("0b01").countIn(LongClass.class));
        assertEquals(15, $ex.of("0x1").countIn(LongClass.class));
        assertEquals(15, $ex.of("0x01").countIn(LongClass.class));
        
        assertEquals(15, $ex.of("1L").countIn(LongClass.class));
        assertEquals(15, $ex.of("0b1L").countIn(LongClass.class));
        assertEquals(15, $ex.of("0b01L").countIn(LongClass.class));
        assertEquals(15, $ex.of("0x1L").countIn(LongClass.class));
        assertEquals(15, $ex.of("0x01L").countIn(LongClass.class));
        
        assertEquals(15, $ex.of("1l").countIn(LongClass.class));
        assertEquals(15, $ex.of("0b1l").countIn(LongClass.class));
        assertEquals(15, $ex.of("0b01l").countIn(LongClass.class));
        assertEquals(15, $ex.of("0x1l").countIn(LongClass.class));
        assertEquals(15, $ex.of("0x01l").countIn(LongClass.class));
    }
    
    /**
     * Had to fix a problem in integer.parseInt("1_000")
     * its like, if you are going to support it, then support it
     */
    public void testNumbersThousandsSeparator(){
        assertTrue( $ex.of(1_000_000).matches("1000000") );
        assertTrue( $ex.of(1000000).matches("1_000_000") );
    }
    
    public void testLiteralsWithPrefix(){
        
        
        Number f2 = Ex.parseNumber("3.14F");
        Number f3 = Ex.parseNumber("3.14f");
        
        Number d = Ex.parseNumber("3.14");
        Number d1 = Ex.parseNumber("3.14d");
        Number d2 = Ex.parseNumber("3.14D");
        
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
        
        assertTrue( $ex.compareNumberLiterals( "1", "0x1"));
        assertTrue( $ex.compareNumberLiterals( "1", "0X1"));
        assertTrue( $ex.compareNumberLiterals( "1", "0b1"));
        assertTrue( $ex.compareNumberLiterals( "1", "0B1"));
        
        assertTrue( $ex.compareNumberLiterals( "15", "0xF"));
        assertTrue( $ex.compareNumberLiterals( "15", "0XF"));
        
        //assertEquals( 1, $expr.parseNumber("0x1"));
        
        //assertEquals( 1, $expr.parseNumber("0x1"));
        //assertEquals( 15, $expr.parseNumber("0xF"));
        //assertEquals( 1, $expr.parseNumber("0x01")); 
        //assertEquals( 1, $expr.parseNumber("0b01"));
        
        assertEquals( Ex.parseNumber("0x01"), Ex.parseNumber("0b01") );
        
        System.out.println( Ex.parseNumber("1").getClass() );
        System.out.println( Ex.parseNumber("0x01").getClass() );
        
        //assertEquals( $expr.parseNumber("0x01"), $expr.parseNumber("1") );
        //assertEquals( $expr.parseNumber("0X01"), $expr.parseNumber("0B01") );
        
        
        
        IntegerLiteralExpr eone = new IntegerLiteralExpr("1");
        IntegerLiteralExpr ehex = new IntegerLiteralExpr("0x01");
        IntegerLiteralExpr ebin = new IntegerLiteralExpr("0b01");
        IntegerLiteralExpr eHex = new IntegerLiteralExpr("0X01");
        IntegerLiteralExpr eBin = new IntegerLiteralExpr("0B01");
        
        $ex<IntegerLiteralExpr, _int, $ex> $one = $ex.intLiteralEx("1");
        $ex<IntegerLiteralExpr, _int, $ex> $hex = $ex.intLiteralEx("0x01");
        $ex<IntegerLiteralExpr, _int, $ex> $bin = $ex.intLiteralEx("0b01");
        $ex<IntegerLiteralExpr, _int, $ex> $Hex = $ex.intLiteralEx("0X01");
        $ex<IntegerLiteralExpr, _int, $ex> $Bin = $ex.intLiteralEx("0B01");
        
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
        
        $ex<DoubleLiteralExpr, _double, $ex> d = $ex.of( new DoubleLiteralExpr("3.14") );
        $ex<DoubleLiteralExpr, _double, $ex> dd = $ex.of( new DoubleLiteralExpr("3.14d") );
        $ex<DoubleLiteralExpr, _double, $ex> dD = $ex.of( new DoubleLiteralExpr("3.14D"));
        
        assertTrue( d.matches("3.14") );
        assertTrue( d.matches("3.14d") );
        assertTrue( d.matches("3.14D") );
        
        assertTrue( dd.matches("3.14") );
        assertTrue( dd.matches("3.14d") );
        assertTrue( dd.matches("3.14D") );
        
        assertTrue( dD.matches("3.14") );
        assertTrue( dD.matches("3.14d") );
        assertTrue( dD.matches("3.14D") );
        
        
        $ex<DoubleLiteralExpr, _double, $ex> f = $ex.of( new DoubleLiteralExpr("3.14") );
        $ex<DoubleLiteralExpr, _double, $ex> fd = $ex.of( new DoubleLiteralExpr("3.14f") );
        $ex<DoubleLiteralExpr, _double, $ex> fD = $ex.of( new DoubleLiteralExpr("3.14F"));
        
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
        
        SwitchStmt ss = $stmt.switchStmt().firstIn(DD.class).ast();
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
        $ex.thisEx().listIn(_c);
        $ex.superEx().listIn(_c);
    }


    /*
    public void testSuperEx(){
        assertTrue( $ex.superEx().matches("super.val()") );

    }

     */

    class C{
        C(int i){
        }
        C(String s){

        }
    }
    class D extends C{
        D(int i){
            super(i);
        }
    }

    public void testGetS(){
        _class _c = _class.of(D.class);
        _java.describe(_c);
    }

    public void testBodyNot(){
        $body $b = $body.of( ()-> System.out.println(1) );
        assertTrue( $b.matches("System.out.println(1);"));
        $b.$not( $ex.of("super.$methodName$($any$)") );
        assertTrue( $b.matches("System.out.println(1);"));
        assertFalse( $b.matches("super.val(1);", "System.out.println(1);"));

        System.out.println( Ex.of("super.val($any$)").getClass() );
        //$.superCallStmt();
    }

    //assertTrue( $ex.superEx().matches("super(1)"));

    //$body $bd = $body.of("System.out.println(1);")
    //        .$not( $ex.superEx() );
    //assertFalse($bd.matches("super(a);", "System.out.println(1);"));

    public void testGenericExpr(){
        //LocalClassDeclarationExpr lc =  Expr.("class $any${}");
        
        //find EVERY lambda with a comment
        $ex $anyLambda = $ex.of("($params$)->$body$", e -> e.ast().getComment().isPresent() );
        
        System.out.println( Ex.lambdaEx("/** comment */ ()->true") );
        
        assertTrue( $anyLambda.matches( Ex.lambdaEx("/** comment */ ()->true") ) );
        
        assertTrue( $ex.lambdaEx(l -> l.ast().getComment().isPresent() ).matches("/** comment */ ()->true;") );

    }

    public void testStatic$expr(){
        _class _c = _class.of("C", new Object(){
            @aa(2) 
            int a = 1;
            int b = 3 + 4;
        });        
        assertEquals( 1, $ex.of(2).listIn(_c).size());
        assertEquals( 4, $ex.of(2).$("2", "number").listIn(_c).size());
    }
    
    
    
    @interface aa{
        int value();
    }

    public void test$exprPostParameterize(){
        //a template expression
        $ex $e = $ex.of("1 + 2");
        //post parameterize the + as an operator
        $e.$("+", "op");

        //verify I can match against other expressions
        assertTrue($e.matches("1 + 2"));
        assertTrue($e.matches("1 * 2"));
        assertTrue($e.matches("1 / 2"));
        assertTrue($e.matches("1 >> 2"));
        assertTrue($e.matches("1 << 2"));

        //works for expressions obviously
        assertTrue($e.matches(Ex.of("1 + 2")));

        //select returns the expression and
        assertNotNull($e.select(Ex.of("1 + 2")));

        //select returns the selected tokens
        assertTrue($e.select(Ex.of("1 * 2")).tokens.is("op", "*"));

        $e = $ex.binaryEx("$a$ + $b$");
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
        $ex<Expression, _expression, $ex> $e = $ex.of("1 + 2");
        assertEquals( $e.draft().ast(), Ex.of("1 + 2"));
        assertTrue( $e.matches(Ex.of("1+2")));

        $e = $ex.of("$a$ + $b$");
        assertTrue( $e.matches(Ex.of("1+2")));
    }

    public void testExprTypes(){
        boolean var = true;

        assertTrue( $ex.assignEx("$var$=100").matches("y=100"));
        assertTrue($ex.arrayAccessEx("arr[$index$]").matches("arr[0]"));
        assertTrue( $ex.arrayCreationEx("new $arr$[][]").matches("new xy[][]"));
        assertTrue($ex.arrayInitEx("{$any$}").matches("{1,2,3}"));
        assertTrue($ex.binaryEx("$left$ > $right$").matches("a > b"));
        assertTrue($boolean.of(true).matches("true"));
        assertTrue($ex.booleanLiteralEx(true).matches("true"));
        assertTrue($ex.of('c').matches( "'c'"));
        assertTrue($ex.castEx("($type$)o").matches("(String)o"));
        assertTrue($ex.classEx("$type$.class").matches("String.class"));
        assertTrue( $ex.conditionalEx("($left$ < $right$) ? $left$ : $right$;").matches("(a < b) ? a : b;"));
        assertEquals( Ex.of(3.14f), Ex.of(3.14f));
        assertEquals( Ex.of(12.3), Ex.of("12.3"));
        assertEquals( Ex.of("12.3f"), Ex.of("12.3f"));
        assertTrue($ex.doubleLiteralEx(12.3).matches("12.3"));
        assertTrue($ex.doubleLiteralEx("12.3f").matches("12.3f"));
        assertTrue($ex.enclosedEx("($a$ + $b$)").matches("(100 + 200)"));
        assertTrue($ex.fieldAccessEx("my.$field$").matches("my.a"));
        assertTrue($ex.intLiteralEx(100).matches("100"));
        assertTrue($ex.of(100).matches("100"));
        assertTrue($ex.instanceOfEx("$obj$ instanceof String").matches("a instanceof String"));
        assertTrue( $ex.longLiteralEx(100).matches( Ex.longLiteralEx( "100")));
        assertTrue($ex.lambdaEx("$param$ -> a.$method$()").matches("x-> a.toString()"));
        assertTrue($ex.methodCallEx("$methodCall$($params$)").matches("a()"));
        assertTrue($ex.methodCallEx("$methodCall$($params$)").matches("a(1,2,3)"));
        assertTrue($ex.methodReferenceEx("$target$::$methodName$").matches("String::toString"));
        assertTrue($ex.nullEx().matches("null"));
        assertTrue($ex.nameEx("eric").matches("eric"));
        assertTrue($ex.objectCreationEx("new $Object$()").matches("new String()"));
        assertTrue($ex.objectCreationEx("new $Object$($params$)").matches("new Date(101010)"));
        assertTrue($ex.stringLiteralEx("Hello $name$").matches("\"Hello Eric\""));
        assertTrue($ex.superEx().matches("super"));
        assertTrue( $ex.thisEx().matches("this"));
        assertTrue( $ex.typeEx("$type$").matches(Ex.typeEx("AType")));
        assertTrue( $ex.unaryEx("!$var$").matches("!isDead"));
        assertTrue($ex.varLocalEx("int $var$").matches("int x"));

        //assertEquals(0b1010000101000101101000010100010110100001010001011010000101000101L, 
        //        0b1010000101000101101000010100010110100001010001011010000101000101L);
        
        //Long.parseUnsignedLong("1010000101000101101000010100010110100001010001011010000101000101", 2);
        
        //Long.parseLong("1010000101000101101000010100010110100001010001011010000101000101", 2);
        
        $ex $e = $ex.longLiteralEx("0b0010000101000101101000010100010110100001010001011010000101000101L");
        assertEquals( $e.astExpressionClass, LongLiteralExpr.class);
        System.out.println("PATTERN" + $e.exprStencil);
        
        LongLiteralExpr lle = (LongLiteralExpr) Ex.of("0b0010000101000101101000010100010110100001010001011010000101000101L");
        System.out.println("VALUE  " + lle.getValue() );
        assertTrue( $e.matches(lle) );
        assertTrue( 
            $ex.of("0b0010000101000101101000010100010110100001010001011010000101000101L")
            .matches("0b0010000101000101101000010100010110100001010001011010000101000101L"));
    }

    public void testSelect(){
        $ex<IntegerLiteralExpr, _int, $ex> e = $ex.intLiteralEx("1").$("1", "val");
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
        List<$ex.Select<IntegerLiteralExpr, _int>> sel =  e.listSelectedIn( _class.of(C.class) );
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
        e.forEachIn(_class.of(C.class), ie -> ints.add(ie.ast().asInt()));
        assertTrue( ints.contains(1) && ints.contains(2) && ints.contains(3) && ints.contains(4) && ints.contains(5) && ints.contains(6));
    }
    
    public void testAPI(){
        
        assertTrue($ex.arrayAccessEx(a -> a.getIndex().ast().isIntegerLiteralExpr() ).matches("a[1]"));
        assertTrue($ex.arrayAccessEx("a[$any$]", a -> a.getIndex().ast().isIntegerLiteralExpr() ).matches("a[1]"));
        assertFalse($ex.arrayAccessEx("a[$any$]", a -> a.getIndex().ast().isIntegerLiteralExpr() ).matches("a[b.count()]"));
        _class _c = _class.of("C").addField("int i=1;");
        
        assertEquals(1, $ex.of(1).listIn(_c ).size());
        
        System.out.println( Ex.of("1").getClass() );
        
        System.out.println( $ex.of(1).replaceIn(_c, Ex.of(2)) );

        System.out.println( $ex.of("1").replaceIn(_c, "2") );
        
        assertTrue( $ex.of("1").replaceIn(_c,"2").getField("i").isInit(2));
        
        //assertTrue( $expr.replace(_c, "1", "2").getField("i").initIs("2"));
        
        assertTrue($ex.of("2").listIn(_c).size() == 1);
        
        //assertTrue( $expr.list(_c, "2").size() == 1 );
        
        //look for every literal
        $ex $bin =
            $ex.binaryEx("$a$ > $b$",
                b-> b.getLeft().ast().isIntegerLiteralExpr() && b.getRight().ast().isIntegerLiteralExpr());
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
        assertNotNull( $ex.intLiteralEx("2").firstIn(_c));
        assertNotNull( $ex.intLiteralEx(1).firstIn(_c));
        
        Predicate<_int> p = (i)-> i.ast().asInt() % 2 == 1;
        $ex.intLiteralEx( p );
        
        assertNotNull( $ex.intLiteralEx( (i)-> i.getValue() % 2 == 1 ).firstIn(_c));
               
        $ex $e = $ex.of(1).$("1", "num");
        
    }
}
