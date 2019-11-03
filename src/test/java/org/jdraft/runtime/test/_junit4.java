package org.jdraft.runtime.test;

import junit.framework.TestCase;
import org.junit.experimental.ParallelComputer;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * a "builder" to dynamically configure the TestClasses,
 * TestListeners and Result Consumer
 * and run selected JUnit Tests at runtime
 */
public class _junit4 {

    /** writes Junit events  to System out as Text */
    public static TextListener SYSTEM_OUT_LISTENER = new TextListener(System.out);

    public static RunListener SILENT_LISTENER = new RunListener();

    public static Consumer<Result> SYSTEM_OUT_RESULT_CONSUMER = result ->{
        System.out.println("Finished. Result: Failures: " +
                result.getFailureCount() + ". Ignored: " +
                result.getIgnoreCount() + ". Tests run: " +
                result.getRunCount() + ". Time: " +
                result.getRunTime() + "ms.");
    };

    public List<Class<? extends TestCase>> testClasses = new ArrayList<>();

    public List<RunListener> listeners;

    public List<Consumer<Result>> resultConsumers;

    public static _junit4 of(Consumer<Result> resultConsumer, Class<? extends TestCase>...testClasses){
        return new _junit4(new ArrayList<>(), new ArrayList<>(), testClasses).add(resultConsumer);
    }

    public static _junit4 of(RunListener runListener, Class<? extends TestCase>...testClasses){
        return new _junit4(new ArrayList<>(), new ArrayList<>(), testClasses).add(runListener);
    }

    public static _junit4 of(Class<? extends TestCase>...testClasses){
        return new _junit4(new ArrayList<>(), new ArrayList<>(), testClasses);
    }

    public _junit4(List<RunListener> runListeners, List<Consumer<Result>> resultConsumers, Class<? extends TestCase>...testClasses){
        this.listeners = runListeners;
        this.resultConsumers = resultConsumers;
        Arrays.stream(testClasses).forEach(tc -> this.testClasses.add(tc));
    }

    public _junit4 add(Class<? extends TestCase>...testClasses){
        Arrays.stream(testClasses).forEach( tc -> {
            if( !this.testClasses.contains(tc) ){
                this.testClasses.add( tc );
            }
        });
        return this;
    }

    /**
     * Adds one or more result consumer to handle the Results before they are returned
     * @param resultConsumers
     * @return
     */
    public _junit4 add(Consumer<Result>...resultConsumers){
        Arrays.stream(resultConsumers).forEach( rc->this.resultConsumers.add(rc));
        return this;
    }

    /**
     *
     * @param runListeners
     * @return
     */
    public _junit4 add(RunListener...runListeners){
        Arrays.stream(runListeners).forEach( rl -> {System.out.println(rl); this.listeners.add( rl );} );
        return this;
    }

    public Result run(){
        JUnitCore junit = new JUnitCore();
        listeners.forEach(junit::addListener);
        Result result = junit.run( testClasses.toArray(new Class[0]) );
        resultConsumers.forEach( rc -> rc.accept(result));
        return result;
    }

    public Result runParallel() {
        return runParallel(true, true);
    }

    public Result runParallel( boolean runTestClassesInParallel, boolean runTestMethodsInParallel){
        JUnitCore junit = new JUnitCore();
        listeners.forEach(junit::addListener);
        Result result = junit.run( new ParallelComputer(runTestClassesInParallel, runTestMethodsInParallel), testClasses.toArray(new Class[0]) );
        resultConsumers.forEach( rc -> rc.accept(result));
        return result;
    }
}
