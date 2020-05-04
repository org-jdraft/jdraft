package org.jdraft.io;

import junit.framework.TestCase;
import org.jdraft._project;

import java.net.URL;

/**
 * Non-test (Main method) for testing downloading from Maven central
 */
public class _mavenCentralTest extends TestCase {

    public void testNothing(){}

    public static void main(String[] args){
        URL downloadJarUrl = _mavenCentral.of(
                "com.github.javaparser",
                "javaparser-core",
                "3.15.18").downloadSourceJarURL();

        System.out.println( downloadJarUrl );

        _project _cus =
                _mavenCentral.of(
                "com.github.javaparser",
                "javaparser-core",
                "3.15.18").load();

        System.out.println( _cus.size() );
    }
}
