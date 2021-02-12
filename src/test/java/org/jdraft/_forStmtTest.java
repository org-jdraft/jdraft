package org.jdraft;

import com.github.javaparser.ast.expr.BinaryExpr;
import junit.framework.TestCase;

import java.util.stream.Collectors;

public class _forStmtTest extends TestCase {

    public void testFromScratch(){
        _forStmt _fs = _forStmt.of();
        System.out.println( _fs );

        _fs.setInit("int i=0;");
        assertTrue( _fs.isInit("int i=0") );
        assertTrue( _fs.isInit(v-> v.has("int i=0")) );

        System.out.println( _fs );
        _fs.setCompare("i<100");
        assertTrue(_fs.isCompare(_binaryExpr.LESS));
        assertTrue(_fs.isCompare(c-> c instanceof _binaryExpr ));

        //this is an example of WHY we need all these seemingly OVER-MODELLED
        //is(...) methods... because we often analyze the feature things that are on the composite thing
        // and it becomes a huge strung together mess to extract and then test everything individually
        // alternatively, is... makes these tests more composeable and readable
        // (here, using isCompare( targetClass, predicate )
        assertTrue(_fs.isCompare(_binaryExpr.class,
                _b-> _b.isLessThan() && _b.isLeft(_nameExpr.class) && _b.isRight(_intExpr.class) ));

        //alternatively the code might look like this (its more complex)
        //its also "disjointed" because we have to define a temporary variable (_be)
        _expr _e = _fs.getCompare();
        if( _e instanceof _binaryExpr){
            _binaryExpr _be = (_binaryExpr)_e;
            assertTrue(_be.getOperator() == BinaryExpr.Operator.LESS
                    && _be.getLeft() instanceof _nameExpr && _be.getRight() instanceof _intExpr);
        }

        System.out.println( _fs );
        _fs.setUpdate("i++");

        System.out.println( _fs );

        _fs.setBody("{}");
        assertTrue( _fs.isBody("{}") );
    }

    public void testNoInitHas(){
        _forStmt _fs = _forStmt.of("for(;;){}");
        assertFalse(_fs.hasInit());
        assertFalse(_fs.hasInit("int i"));
        assertFalse(_fs.hasInit(_i-> _i.isType(_intExpr.class)));
    }

    public void testMulitpleInitsMultipleUpdates(){
        _forStmt _fs = _forStmt.of();
        _fs.setInit("int i=0,j=100");
        _fs.setCompare("i<j");
        _fs.setUpdate("i++,j--");
        System.out.println( _fs);

        //verify the hasInit works with predicate
        assertFalse(_fs.hasInit(_i-> _i.isType(String.class)));

        assertTrue(_fs.hasInit(_i-> _i.isType(int.class)));

        //simpler form the forStatement has an initializer variable that is of type
        assertTrue(_fs.hasInit(int.class));

        //make sure in order matches
        assertEquals( _fs, _forStmt.of(()-> {for(int i=0,j=100;i<j;i++,j--) return;}));
        //make sure out of order matches
        assertEquals( _fs, _forStmt.of(()-> {for(int j=100,i=0;i<j;j--,i++) return;}));

        //make sure hashcode matches
        assertEquals(
                _forStmt.of(()-> {for(int i=0,j=100;i<j;i++,j--) return;}).hashCode(),
                _forStmt.of(()-> {for(int j=100,i=0;i<j;j--,i++) return;}).hashCode()
        );
    }

    public void testWalk(){
        _forStmt _fs = _forStmt.of( ()->{
            int j = 100;
            for(int i=0;i<100;i++, j--){
                System.out.println(i);
                assert 1==1 : "message";
            }
        });
        assertEquals(4, _fs.walk(_intExpr.class).count());
        _fs.walk(_intExpr.class).forEach(_i -> System.out.println(_i));

        _fs.walk(_intExpr.class).forEach(_i-> _i.setValue(_i.getValue()+1));
        _fs.walk(_intExpr.class).forEach(_i -> System.out.println(_i));

        //
        _fs.walk(_intExpr.class).stream().map( _i-> _i.getValue()).collect(Collectors.toList());

    }

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