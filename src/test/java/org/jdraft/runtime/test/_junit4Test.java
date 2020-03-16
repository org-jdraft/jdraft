package org.jdraft.runtime.test;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.macro._extend;
import org.jdraft.macro._packageName;
import org.jdraft.runtime._runtime;
import org.junit.runner.Result;

import java.io.IOException;

public class _junit4Test extends TestCase {

    public void testRun(){
        Result r = _junit4.of(Single.class, SetupTearDownTest.class).run();
        assertEquals(0, r.getFailureCount());
        assertEquals( 2, r.getRunCount() );
    }

    public void testFailExceptionNotPropagated(){
        Result r = _junit4.of(Inner.failAssumption).run();
        assertEquals(1, r.getFailureCount());
        r = _junit4.of(Inner.failException).run();
        assertEquals(1, r.getFailureCount());
    }

    public void testAdd(){
        _junit4 junit = _junit4.of();
        junit.add(SetupTearDownTest.class, Single.class);

        junit.run();
    }

    public static class SetupTearDownTest extends TestCase{
        public void setUp(){
            System.out.println( "Set up ");
        }
        public void tearDown(){
            System.out.println( "tear down");
        }
        public void testDo(){
            int a=1,b=1;
            assertTrue(a==b);
        }
    }
    public static class Single extends TestCase {
        public void test(){
            assertTrue(1==1);
        }
    }

    public static class Inner {

        public static Class<TestCase> failAssumption = (Class<TestCase>) _runtime.Class( _class.of(FailAssumption.class));
        public static Class<TestCase> failException = (Class<TestCase>) _runtime.Class( _class.of( FailException.class) );
        static {
            System.out.println (_class.of(FailAssumption.class));
            System.out.println (_class.of(FailException.class));
        }
        @_packageName("test")
        @_extend(TestCase.class)
        public class FailAssumption {
            public void testAssumption() {
                assertTrue(1 == 2);
            }
        }
        @_packageName("test")
        @_extend(TestCase.class)
        public class FailException  {
            public void testThrowException() throws IOException {
                throw new IOException("random IOE");
            }
        }
    }

    /** Stop on failure
    public void testStopOnFailure(){
        //hopefully this wont exit the editor
        _junit4 _j4 = _junit4.of( new RunListener(){
             public void testFailure(Failure failure) throws Exception {
                 //System.out.println( "failed on "+ failure );
                 PrintStream ps = new PrintStream( new ByteArrayOutputStream() );
                 failure.getException().printStackTrace(ps);

                 //System.exit(-1); //yeah this STOPS THE WHOLE JVM NOT WHAT I WANT
             }
             public void testAssumptionFailure(Failure failure)  {
                 //System.out.println( "failed on "+ failure );
                 PrintStream ps = new PrintStream( new ByteArrayOutputStream() );
                 failure.getException().printStackTrace(ps);
                 //System.exit(-1);
             }
        }, FailExceptionTest.class);

        try{
            _j4.run();
        } catch(Exception e){
            System.out.println( "Expected");
        }
    }
     */

    /** test parallel runs */
    public void testSerialVParallelRun(){
        Result rSerial = _junit4.of(ParallelTest1.class, ParallelTest2.class).run();
        System.out.println( "Serial Time   : "+ rSerial.getRunTime()+"ms");
        Result rParallel = _junit4.of(ParallelTest1.class, ParallelTest2.class).runParallel();
        System.out.println( "Parallel Time : "+ rParallel.getRunTime()+"ms");
    }

    public static class ParallelTest1 extends TestCase {
        public void test1a() {
            lookBusy(25);
        }
        public void test1b() {
            lookBusy(25);
        }
    }

    public static class ParallelTest2 extends TestCase {
        public void test2a() {
            lookBusy(25);
        }

        public void test2b() {
            lookBusy(25);
        }
    }

    public static void lookBusy(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.out.println("interrupted");
        }
    }

}
