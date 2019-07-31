package org.jdraft.io;

import test.bulk.XmlDomFile;

import java.nio.file.Paths;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class DOMFileTest extends TestCase {
    
    public void testDOMFile(){
        XmlDomFile dom = new XmlDomFile(Paths.get("C:\\temp\\x.xml"), "<A id=\"id\">name</A>"  );
        assertNotNull( dom.getModel().getFirstChild() );        
    }
}
