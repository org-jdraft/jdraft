package test.spoon;

import com.github.javaparser.ast.stmt.IfStmt;
import junit.framework.TestCase;
import org.jdraft.proto.$;
import org.jdraft.proto.$stmt;

public class SpoonComingTest extends TestCase {

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
        $stmt<IfStmt> $ifReturn = $.ifStmt().$hasDescendant(2, $.returnStmt());
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
        assertEquals(1, $ifReturn.count(Ex.class));
    }

    /**
     * ALternative way of representing an if statement with a returnStatement
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
        assertEquals(1, $ifReturn.count(Ex.class));
    }
}
