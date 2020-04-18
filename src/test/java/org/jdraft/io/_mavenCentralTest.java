package org.jdraft.io;

import junit.framework.TestCase;

import java.net.URL;

public class _mavenCentralTest extends TestCase {

    public void testNothing(){}

    public static void main(String[] args){
        URL downloadJarUrl = _mavenCentral.of(
                "com.github.javaparser",
                "javaparser-core",
                "3.15.18").downloadSourceJarURL();

        System.out.println( downloadJarUrl );

        _sources _sc = _sources.of(
                _mavenCentral.of(
                "com.github.javaparser",
                "javaparser-core",
                "3.15.18"));

        System.out.println( _sc.size() );
    }
}
