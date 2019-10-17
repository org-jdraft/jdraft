package org.jdraft.pattern;

import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import org.jdraft.Ast;
import org.jdraft.Ex;
import java.util.ArrayList;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class ScaseTest extends TestCase {

    public void testOfMatch(){
        //System.out.println( Ast.switchEntry("'a': System.out.println( 1 ); assert(true);") );
        $case $c = $case.of( "'a' : System.out.println( 1 ); ");

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

        $c = $case.of( "'a' : System.out.println( 1 ); break outer;");
        assertTrue( $c.matches("'a': System.out.println( 1 ); break outer;") );
        assertTrue( $c.matches("'a': System.out.println( 1 ); System.out.println(2); break outer;") );

    }

    public void testAsMatch(){
        $case $c = $case.as( "'a' : System.out.println( 1 ); ");
        assertTrue( $c.matches("'a': System.out.println( 1 );") ); //matches exact

        assertFalse( $c.matches("'a': System.out.println( 1 ); assert(true);") );
        assertFalse( $c.matches("'a': System.out.println( 1 ); break;") );
        assertFalse( $c.matches("'a': System.out.println( 1 ); break outer;") );

        $c = $case.as( "'a' : System.out.println( 1 ); break outer;");
        assertTrue( $c.matches("'a': System.out.println( 1 ); break outer;") );
        assertFalse( $c.matches("'a': System.out.println( 1 ); System.out.println(2); break outer;") );
    }

    public void testConstruct(){
        $case $c = $case.of( $ex.of("$val$")
            .$and(i -> i.isIntegerLiteralExpr() && Integer.parseInt( i.asIntegerLiteralExpr().getValue() ) % 2 == 1 ),
            $stmt.of("System.out.println($val$);"));
        System.out.println( $c.draft("val", 1) );
    }
    
    public void testConstructAny(){
        SwitchEntry se = 
            $case.of().draft("$label", Ex.of(1),
                "$statements", "System.out.println(1);");
        System.out.println( se );
    }
    
    public void testStaticCase(){
        String sss = "case 0: System.out.println(1);";
        SwitchEntry se = Ast.switchEntry(sss);
        $case $c = $case.of( se );
        assertNotNull( $c.select(se));
        assertNotNull( $c.select(sss));
        assertEquals( se, $c.select(se).astCase);
        assertTrue( $c.matches(se));
        assertTrue( $c.matches(sss));
        
        //label (w/o code)
        se = Ast.switchEntry("case 'c':");
        $c = $case.of( se );
        assertNotNull( $c.select("case 'c':"));
        
        se = Ast.switchEntry("default : System.out.println(\"default\");");
        $c = $case.of( se);
        assertNotNull( $c.select(se) );        
        assertTrue( $c.matches(se));
        assertNotNull( $c.select("default:System.out.println(\"default\");") );        
        assertTrue( $c.matches("default:System.out.println(\"default\");"));
    }
    
    public void testCaseAny(){
        $case $c = $case.of();
        assertTrue( $c.matches("case 1:") );
        assertTrue( $c.matches("case 1: return 2;") );
        assertTrue( $c.matches("case 'c': System.out.println(1); System.out.println(2);break;") );
        
        assertTrue( $c.matches("default: System.out.println(1);") );
        assertTrue( $c.matches("default: System.out.println(1); System.out.println(2);break;") );
    }
    
    
    public void testDynamicCase(){
        $case $c = $case.of($ex.of(), $stmt.of( (String $content$)-> System.out.println($content$)));
        
        assertTrue($c.select("default: System.out.println(1);").is("content", 1));
        assertTrue($c.select("case 1: System.out.println('a');").is("content", 'a'));
        assertTrue($c.select("case 'w': System.out.println(3578);").is("content", 3578));
        
        assertTrue($c.matches(Ast.switchEntry("default: System.out.println(1);")));
        assertTrue($c.matches(Ast.switchEntry("case 1: System.out.println('a');")));
        
        
    }
    
    public static void main(String[] args){
        $case $c = $case.of($ex.of("$val$"), $stmt.of( (String $val$)-> System.out.println($val$) ));
        SwitchEntry se = Ast.switchEntry( "case 'a': System.out.println('a');");
        assertTrue($c.select(se).is("val", 'a'));
    }
    
    public void testCorrelatedCase(){
        $case $c = $case.of($ex.of("$val$"), $stmt.of( (String $val$)-> System.out.println($val$) ));
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
        assertEquals( 9, $case.of().count(CC.class));
        
        assertEquals(2, $.of(1).count(CC.class));
        
        
        ArrayList<$case> $cases = new ArrayList<>();
        SwitchStmt sts = $stmt.switchStmt().firstIn(CC.class);
        sts.getEntries().forEach( se -> $cases.add( new $case(se) ) );
        
        $cases.forEach( c-> System.out.println(c.label + " "+ c.statements));        
    }
    
}
