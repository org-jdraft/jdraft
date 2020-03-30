package org.jdraft;

import junit.framework.TestCase;

import java.io.IOException;
import java.net.URISyntaxException;

public class _catchTest extends TestCase {

    public void testCatch(){

        _tryStmt _ts = _tryStmt.of();

        _ts.addCatch( _catch.of(IOException.class, URISyntaxException.class));

        _catch _c = _catch.of( IOException.class, URISyntaxException.class);
        assertTrue( _c.hasType(IOException.class) );
        assertTrue( _c.hasType(URISyntaxException.class) );

        _c = _catch.of( IOException.class);
        assertTrue( _c.hasType(IOException.class) );
        assertFalse( _c.hasType(URISyntaxException.class) );

        System.out.println( _c );
    }

}
