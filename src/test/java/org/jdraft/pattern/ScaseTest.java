package org.jdraft.pattern;

import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import org.jdraft.Ast;
import org.jdraft.Expr;
import java.util.ArrayList;
import junit.framework.TestCase;
import org.jdraft._switchCase;

/**
 *
 * @author Eric
 */
public class ScaseTest extends TestCase {

    public void testOfMatch(){
        //System.out.println( Ast.switchEntry("'a': System.out.println( 1 ); assert(true);") );
        $switchEntry $c = $switchEntry.of( "'a' : System.out.println( 1 ); ");

        assertTrue( $c.matches("'a': System.out.println( 1 );") );
        //extra statements ok
        assertTrue( $c.matches("'a': System.out.println( 1 ); assert(true);") );
        assertTrue( $c.matches("'a': System.out.println( 1 ); break;") );
        assertTrue( $c.matches("'a': System.out.println( 1 ); break outer;") );
        assertTrue( $c.matches("'a': assert(true); System.out.println( 1 ); break outer;") ); //statement does not have to be first

        assertTrue( $c.matches("'a': if(true){ assert(true); System.out.println( 1 ); }break outer;") ); //statement can be nested

        assertFalse( $c.matches("'b': System.out.println( 1 );"));//different label
        assertFalse( $c.matches("'a': System.out.println( 2 );"));//different statement
        assertFalse( $c.matches("'a': "));//different (no) statement

        $c = $switchEntry.of( "'a' : System.out.println( 1 ); break outer;");
        assertTrue( $c.matches("'a': System.out.println( 1 ); break outer;") );
        assertTrue( $c.matches("'a': System.out.println( 1 ); System.out.println(2); break outer;") );

    }

    public void testAsMatch(){
        $switchEntry $c = $switchEntry.as( "'a' : System.out.println( 1 ); ");
        assertTrue( $c.matches("'a': System.out.println( 1 );") ); //matches exact

        assertFalse( $c.matches("'a': System.out.println( 1 ); assert(true);") );
        assertFalse( $c.matches("'a': System.out.println( 1 ); break;") );
        assertFalse( $c.matches("'a': System.out.println( 1 ); break outer;") );

        $c = $switchEntry.as( "'a' : System.out.println( 1 ); break outer;");
        assertTrue( $c.matches("'a': System.out.println( 1 ); break outer;") );
        assertFalse( $c.matches("'a': System.out.println( 1 ); System.out.println(2); break outer;") );
    }

    public void testConstruct(){
        $switchEntry $c = $switchEntry.of( $ex.of("$val$")
            .$and(i -> i.node().isIntegerLiteralExpr() && Integer.parseInt( i.node().asIntegerLiteralExpr().getValue() ) % 2 == 1 ),
            $stmt.of("System.out.println($val$);"));
        System.out.println( $c.draft("val", 1) );
    }
    
    public void testConstructAny(){
        _switchCase se =
            $switchEntry.of().draft("$label", Expr.of(1),
                "$statements", "System.out.println(1);");
        System.out.println( se );
    }
    
    public void testStaticCase(){
        String sss = "case 0: System.out.println(1);";
        SwitchEntry se = Ast.switchEntry(sss);
        $switchEntry $c = $switchEntry.of( se );
        assertNotNull( $c.select(se));
        assertNotNull( $c.select(sss));
        assertEquals( se, $c.select(se)._se.node());
        assertTrue( $c.matches(se));
        assertTrue( $c.matches(sss));
        
        //label (w/o code)
        se = Ast.switchEntry("case 'c':");
        $c = $switchEntry.of( se );
        assertNotNull( $c.select("case 'c':"));
        
        se = Ast.switchEntry("default : System.out.println(\"default\");");
        $c = $switchEntry.of( se);
        assertNotNull( $c.select(se) );        
        assertTrue( $c.matches(se));
        assertNotNull( $c.select("default:System.out.println(\"default\");") );        
        assertTrue( $c.matches("default:System.out.println(\"default\");"));
    }
    
    public void testCaseAny(){
        $switchEntry $c = $switchEntry.of();
        assertTrue( $c.matches("case 1:") );
        assertTrue( $c.matches("case 1: return 2;") );
        assertTrue( $c.matches("case 'c': System.out.println(1); System.out.println(2);break;") );
        
        assertTrue( $c.matches("default: System.out.println(1);") );
        assertTrue( $c.matches("default: System.out.println(1); System.out.println(2);break;") );
    }
    
    
    public void testDynamicCase(){
        $switchEntry $c = $switchEntry.of($ex.any(), $stmt.of( (String $content$)-> System.out.println($content$)));
        
        assertTrue($c.select("default: System.out.println(1);").is("content", 1));
        assertTrue($c.select("case 1: System.out.println('a');").is("content", 'a'));
        assertTrue($c.select("case 'w': System.out.println(3578);").is("content", 3578));
        
        assertTrue($c.matches(Ast.switchEntry("default: System.out.println(1);")));
        assertTrue($c.matches(Ast.switchEntry("case 1: System.out.println('a');")));
        
        
    }
    
    public static void main(String[] args){
        $switchEntry $c = $switchEntry.of($ex.of("$val$"), $stmt.of( (String $val$)-> System.out.println($val$) ));
        SwitchEntry se = Ast.switchEntry( "case 'a': System.out.println('a');");
        assertTrue($c.select(se).is("val", 'a'));
    }
    
    public void testCorrelatedCase(){
        $switchEntry $c = $switchEntry.of($ex.of("$val$"), $stmt.of( (String $val$)-> System.out.println($val$) ));
        assertTrue($c.select("case 'a': System.out.println('a');").is("val", 'a'));
        
        assertNull($c.select("case 'a': System.out.println('b');"));        
    }
    
    public void testTT(){        
        class CC{
            void m(int n){
                switch(n){
                    case 0: System.out.println("hi");
                    case 1: 
                    case 2: System.out.println("one or two"); break;
                    case 3: break;
                    case 4: System.out.println(1); 
                            System.out.println(2);
                            System.out.println(3);
                            break;
                    case 5:        
                    case 6: throw new RuntimeException();   
                    case 7: { System.out.println( "Block"); System.out.println("BB");}
                    default : System.out.println( "default");
                }
            }
        }
        assertEquals( 9, $switchEntry.of().countIn(CC.class));
        
        assertEquals(2, $.of(1).countIn(CC.class));
        
        
        ArrayList<$switchEntry> $switchEntries = new ArrayList<>();
        SwitchStmt sts = $stmt.switchStmt().firstIn(CC.class).node();
        sts.getEntries().forEach( se -> $switchEntries.add( new $switchEntry(se) ) );
        
        $switchEntries.forEach(c-> System.out.println(c.label + " "+ c.statements));
    }
    
}
