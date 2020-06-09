package test.othertools;

import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import junit.framework.TestCase;
import org.jdraft.Print;
import org.jdraft.Walk;
import org.jdraft._ifStmt;
import org.jdraft.pattern.$;
import org.jdraft.pattern.$stmt;

public class SpoonComingTest extends TestCase {

    public void testOffShootDepth(){
        $stmt<IfStmt, _ifStmt> $ifReturn = ($stmt<IfStmt, _ifStmt>) $.ifStmt().$hasDescendant(1, $.returnStmt());
        class Ex{
            public int ex(int a){
                if( a > 0 ){
                    System.out.println( "This will even work");
                    System.out.println( "Because we are not looking for the exact pattern");
                    do { //adding another level
                        return 2;
                    }while(a < 0 );
                } else {
                    do { //adding another level
                        return 4;
                    }while(a != 6 );
                }
            }
        }
        //if I wanted to look for the returnStmts themselves
        assertEquals(2, $.returnStmt().countIn(Ex.class));

        //returnStmts that are direct children of ifStmts
        assertEquals(0, $.returnStmt().$isParent($.ifStmt()).countIn(Ex.class));
        //they aren NOT

        //verify, without "depth" we can find the if statement (in this case, (1) IfStmt has (2) returns
        assertEquals( 1, $.ifStmt().$hasDescendant($.returnStmt()).countIn(Ex.class) );
        //if I set depth to (1)
        assertEquals(0, $ifReturn.countIn(Ex.class));
    }

    /**
     * https://github.com/SpoonLabs/coming#change-pattern-specification
     *
     * <pattern>
     * 	<entity id=``1" type=``Return">
     * 		<parent parentId=``2" distance=``2" />
     * 	</entity>
     * 	<entity id=``2" type=``If" />
     * 	<action entityId=``1" type=``INS" />
     * 	<action entityId=``2" type=``INS" />
     * </pattern>
     */
    public void testIfReturnConstraint(){
        $stmt<IfStmt, _ifStmt> $ifReturn = $.ifStmt().$and( (is) ->
                Walk.hasDescendant( ((_ifStmt)is).ast().getThenStmt(), n -> n instanceof ReturnStmt) ); //.$hasDescendant(2, $.returnStmt());
        class Ex{
            public int ex(int a){
                if( a > 0 ){
                    System.out.println( "This will even work");
                    System.out.println( "Because we are not looking for the exact pattern");
                    return 2;
                } else {
                    return 4;
                }
            }
        }
        Print.tree( ((_ifStmt)$.ifStmt().firstIn(Ex.class)).ast() );

        assertEquals(1, $ifReturn.countIn(Ex.class));
    }

    /**
     * Alternative way of representing an if statement with a returnStatement
     */
    public void testIfReturnPattern(){
        $stmt $ifReturn = $.ifStmt("if($cond$){ return $ret$;}");

        class Ex{
            public int ex(int a){
                if( a > 0 ){
                    //System.out.println("Wont Work until I comment this out because pattern doesnt match");
                    return 2;
                } else {
                    return 4;
                }
            }
        }
        assertEquals(1, $ifReturn.countIn(Ex.class));
    }
}
