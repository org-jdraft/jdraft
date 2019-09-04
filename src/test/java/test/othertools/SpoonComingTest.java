package test.othertools;

import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft.proto.$;
import org.jdraft.proto.$stmt;

public class SpoonComingTest extends TestCase {

    public void testOffShootDepth(){
        $stmt<IfStmt> $ifReturn = $.ifStmt().$hasDescendant(1, $.returnStmt());
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
        assertEquals(0, $ifReturn.count(Ex.class));
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
        $stmt<IfStmt> $ifReturn = $.ifStmt().$and( (IfStmt is) ->
                Ast.hasDescendant( is.getThenStmt(), n -> n instanceof ReturnStmt) ); //.$hasDescendant(2, $.returnStmt());
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
        Ast.describe( $.ifStmt().firstIn(Ex.class) );

        assertEquals(1, $ifReturn.count(Ex.class));
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
        assertEquals(1, $ifReturn.count(Ex.class));
    }
}
