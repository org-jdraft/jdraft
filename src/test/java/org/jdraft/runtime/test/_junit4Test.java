package org.jdraft.runtime.test;

import junit.framework.TestCase;
import org.jdraft._annosTest;
import org.jdraft.runtime._runtimeException;
import org.jdraft.walkTest;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class _junit4Test extends TestCase {

    public void testRun(){
        Result r = _junit4.of(SingleTest.class, SetupTearDownTest.class).run();
        assertEquals(0, r.getFailureCount());
        assertEquals( 2, r.getRunCount() );
    }

    public void testFailExceptionNotPropagated(){
        Result r = _junit4.of(FailAssumptionTest.class).run();
        assertEquals(1, r.getFailureCount());
        r = _junit4.of(FailExceptionTest.class).run();
        assertEquals(1, r.getFailureCount());
    }

    public void testAdd(){
        _junit4 junit = _junit4.of();
        junit.add(SetupTearDownTest.class, SingleTest.class);

        junit.run();
    }

    public static class FailOnFirstExceptionRunListener extends RunListener{
        public void testFailure(Failure failure) {
            System.out.println("Failed, Throwing");
            throw new _runtimeException( failure.getMessage(), failure.getException() );
        }

        public void testAssumptionFailure(Failure failure) {
            System.out.println("Failed Assumption, Throwing");
            throw new _runtimeException( failure.getMessage(), failure.getException() );
        }
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
    public static class SingleTest extends TestCase {
        public void test(){
            assertTrue(1==1);
        }
    }
    public static class FailAssumptionTest extends TestCase {
        public void testAssumption(){
            assertTrue( 1==2 );
        }
    }
    public static class FailExceptionTest extends TestCase {
        public void testThrowExcpetion() throws IOException {
            throw new IOException("random IOE");
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
