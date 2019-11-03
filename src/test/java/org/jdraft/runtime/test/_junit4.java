package org.jdraft.runtime.test;

import junit.framework.TestCase;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<RunListener> listeners = new ArrayList<>();

    public Consumer<Result> resultConsumer = null;

    /* Constructors */
    public _junit4(Class<? extends TestCase>...testClasses){
        this(null, testClasses );
    }
    public _junit4(Consumer<Result> resultConsumer, Class<? extends TestCase>...testClasses){
        this( Stream.of(SILENT_LISTENER).collect(Collectors.toList()), resultConsumer, testClasses );
    }

    public _junit4(List<RunListener> runListeners, Consumer<Result> resultConsumer, Class<? extends TestCase>...testClasses){
        this.listeners = runListeners;
        this.resultConsumer = resultConsumer;
        Arrays.stream(testClasses).forEach(tc -> this.testClasses.add(tc));
    }

    public _junit4 addTestClasses(Class<? extends TestCase>...testClasses){
        Arrays.stream(testClasses).forEach( tc -> {
            if( !this.testClasses.contains(tc) ){
                this.testClasses.add( tc );
            }
        });
        return this;
    }

    public Result run(){
        JUnitCore junit = new JUnitCore();
        listeners.forEach(junit::addListener);
        Result r = junit.run( testClasses.toArray(new Class[0]) );
        if( this.resultConsumer != null ){
            this.resultConsumer.accept(r);
        }
        return r;
    }
}
