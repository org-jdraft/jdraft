/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

/**
 *
 * @author Eric
 */
public class _runtimeResourcesTest extends TestCase {
     
    public void testGetResource(){
        
        _runtime ah = _runtime.of("package xxxx.yyyy;", "public class ZZ{}");
        Class c = ah.getClass("xxxx.yyyy.ZZ"); //make sure I can get the Class
        URL sourceUrl = c.getClassLoader().getResource("xxxx.yyyy.ZZ.java"); 
        assertNotNull( sourceUrl ); //verify I can get the .java source of the class
        
        System.out.println( sourceUrl );
        URL classUrl = c.getClassLoader().getResource("xxxx.yyyy.ZZ.class");
        System.out.println( classUrl );
        assertNotNull( classUrl ); //verify I can get the .class bytecode
        
        assertNull( c.getClassLoader().getResource("notFound.java") );
    }
    /**
     * Make sure when we build an AdhocClassLoader, we let users get the 
     * source .java and bytecode .class resources as a Stream
     *  NOTE THESE SHOULD ALL WORK, BUT SOME ARE SLOW
     */
    /*    SLOW but works
    public void testGetJavaAndClassResourceAsStream() throws IOException{
        _runtime ah = _runtime.of( "package aaaa;", "public class B{}");
        Class c = ah.getClass("aaaa.B");
        
        InputStream ss = c.getClassLoader().getResourceAsStream("/aaaa/B.java");        
        assertNotNull( ss);
        ss.close();
        
        ss = c.getClassLoader().getResourceAsStream("/aaaa/B.class");
        assertNotNull( ss);
        ss.close();
        
        //you MAY have a leading slash but this is SLOOOOOOWWW
        /*
        ss = c.getClassLoader().getResourceAsStream("//aaaa//B.java");
        assertNotNull( ss);
        ss.close();
        
        ss = c.getClassLoader().getResourceAsStream("//aaaa//B.class");
        assertNotNull( ss);
        ss.close();
        */
        /*
        ss = c.getClassLoader().getResourceAsStream("aaaa//B.java");
        assertNotNull( ss);
        ss.close();
        */
        /*
        ss = c.getClassLoader().getResourceAsStream("aaaa//B.class");
        assertNotNull( ss);
        ss.close();
        */
        /*
        ss = c.getClassLoader().getResourceAsStream("aaaa/B.java");
        assertNotNull( ss);
        ss.close();
        
        ss = c.getClassLoader().getResourceAsStream("aaaa/B.class");
        assertNotNull( ss);
        ss.close();
        */
        /*
        //NOTE: this isn't really the PROPER WAY to identify Resources, but we'll allow it
        assertNotNull( c.getClassLoader().getResourceAsStream("aaaa.B.java"));
        assertNotNull( c.getClassLoader().getResourceAsStream("aaaa.B.class"));      

    }    */
}
