package test.byexample.runtime;

import junit.framework.TestCase;
import org.jdraft._annoExprsTest;
import org.jdraft.macro._addImports;
import org.jdraft.macro._packageName;
import org.jdraft.macro._static;
import org.jdraft.runtime._runtime;
import org.jdraft.WalkTest;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class RuntimeDynamicTestSuite extends TestCase {

    public void testBuildAndRunDynamicTestRunner(){
        @_packageName("autotest")
        @_addImports({_annoExprsTest.class, WalkTest.class, JUnitCore.class, TextListener.class, Result.class})
        class ManualTestRunner {
            //need to import

            public @_static void run() {
                JUnitCore junit = new JUnitCore();
                junit.addListener(new TextListener(System.out));
                Result result = junit.run(
                        _annoExprsTest.class,
                        WalkTest.class);

                resultReport(result);
            }

            public @_static void resultReport(Result result) {
                System.out.println("Finished. Result: Failures: " +
                        result.getFailureCount() + ". Ignored: " +
                        result.getIgnoreCount() + ". Tests run: " +
                        result.getRunCount() + ". Time: " +
                        result.getRunTime() + "ms.");
            }
        }
        _runtime.of( ManualTestRunner.class ).eval( "run()");
    }
}
