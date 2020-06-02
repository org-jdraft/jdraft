package org.jdraft;

import com.github.javaparser.ast.expr.BinaryExpr;
import junit.framework.TestCase;

public class _forStmtTest extends TestCase {

    public void testIs(){
        _forStmt _f  = _forStmt.of( ()-> {
            for(int i=0;i<100; i++)
                System.out.println( i );
        });

        assertTrue( _f.is("/*comment*/ for(int i=0;i<100;i++) /*comm*/ System.out.println(i);"));
    }
    public void testEquality(){
        _variablesExpr _ve = _variablesExpr.of("int i, j");
        assertTrue( _ve.is("int j, i"));
        assertTrue( _ve.isAt(0, "int i"));
        assertTrue( _ve.isAt(1, "int j"));

        _ve = _variablesExpr.of("int i=100, j=200");
        assertTrue( _ve.is("int i=100, j=200"));
        assertTrue( _ve.is("int j=200, i=100"));
        assertTrue( _ve.isAt(0, "int i=100"));
        assertTrue( _ve.isAt(1, "int j=200"));
    }

    public void testInfiniteLoop(){
        _forStmt _fs = _forStmt.of("for(;;){ }");
        System.out.println( _fs );

        assertFalse(_fs.hasCompare());
        assertFalse(_fs.isCompare(_binaryExpr.class));
        assertFalse(_fs.isCompare(BinaryExpr.Operator.LESS));
        assertFalse(_fs.isCompare(_binaryExpr.of("a < b")));
        assertFalse(_fs.hasInit());
        assertFalse(_fs.hasUpdate());
        assertTrue(_fs.hasBody());
        assertTrue(_fs.isBody("{}"));
        assertTrue(_fs.getBody().isImplemented());
        assertTrue(_fs.getBody().isEmpty());
    }

    public void testInitCompUpdates(){
        _forStmt _fs = _forStmt.of( ()-> {
            for(int i=0;i<100;i++){
                System.out.println( 1 );
            }
        });

        assertTrue( _fs.hasInit());
        assertTrue( _fs.isInit("int i=0"));
        assertTrue( _fs.isInit(_variablesExpr.of("int i=0")));

        //System.out.println( _fs );
        assertTrue(_fs.isCompare(_binaryExpr.class));
        assertTrue(_fs.isCompare(BinaryExpr.Operator.LESS));
        assertEquals( _fs.getCompare(), _binaryExpr.of("i < 100"));
        assertTrue(_fs.isCompare(_binaryExpr.of("i < 100")));
        assertTrue(_fs.isCompare("i < 100"));

        assertTrue( _fs.hasUpdate() );
        assertTrue( _fs.isUpdate("i++") );

        _fs = _forStmt.of( ()->{
            for(int i=0,j=1; i<100; i++, j++){

            }
        });

        //doesnt matter the order of the updates
        assertTrue( _fs.isUpdate("i++, j++"));
        assertTrue( _fs.isUpdate("j++, i++"));
    }



    /**
     * Make sure we can parse wild and crazy versions of for loops
     */
    public void testFors(){
        _forStmt _fs = _forStmt.of( () -> {
            for (int a = 0, b[] = {1}, c[][] = {{1}, {2}}; a < 10; a++, b[0]++) {
                // something
            }
        });

        assertEquals(_variablesExpr.of("int a = 0, b[] = {1}, c[][] = {{1},{2}}"), _fs.getInit());

        assertNotNull( _fs.getInit() );
        assertEquals( _expr.of("a < 10"), _fs.getCompare() );
        assertEquals( _expr.of("a++"), _fs.listUpdates().get(0) );
        assertEquals( _expr.of("b[0]++"), _fs.listUpdates().get(1) );
        System.out.println( _fs);

    }

}