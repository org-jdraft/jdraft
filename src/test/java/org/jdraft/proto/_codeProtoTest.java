/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft.proto;

import org.jdraft.proto.$import;
import org.jdraft._code;
import org.jdraft._moduleInfo;
import org.jdraft._packageInfo;
import java.net.URL;
import junit.framework.TestCase;

/**
 * Verify the module info and package info classes work
 * @author Eric
 */
public class _codeProtoTest extends TestCase {
    
    public void testPkgInfo(){
        _code _pi = _packageInfo.of("package aaaa.ffff.gggg;", "import java.util.*;", "import java.net.URL;");
        assertEquals(1, $import.of(URL.class).count(_pi));
        assertEquals(2, $import.any().count(_pi));        
    }
    
    public void testModuleInfo(){
        _code _pi = _moduleInfo.of("import java.util.*;", "import java.net.URL;", "module aaaa.ffff.gggg{}" );
        assertEquals(1, $import.of(URL.class).count(_pi));
        assertEquals(2, $import.any().count(_pi));        
    }
}
