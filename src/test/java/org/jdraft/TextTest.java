package org.jdraft;

import org.jdraft.Text;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class TextTest extends TestCase {
    
    public void testLeadingWhitespace(){
        String s = "";
        assertEquals("", Text.getLeadingSpaces(s, 0));
        
        s = "s";
        assertEquals("", Text.getLeadingSpaces(s, 0));
        
        
        s = " s";//single space
        assertEquals(" ", Text.getLeadingSpaces(s, 1));
        s = "  s";//two spaces
        assertEquals("  ", Text.getLeadingSpaces(s, s.indexOf("s")));
        s = "    s";//four spaces
        assertEquals("    ", Text.getLeadingSpaces(s, s.indexOf("s")));
        
        s = "\ts";
        assertEquals("\t", Text.getLeadingSpaces(s, 1));
        s = System.lineSeparator()+"s";
        System.out.println( s.indexOf("s"));
        assertEquals( System.lineSeparator(), Text.getLeadingSpaces( s, s.indexOf("s")) );        
    }
    
    
    
    public void testGetExtractString(){        
        assertEquals("s", Text.matchFirstPaddedTarget("s", "s"));
        assertEquals(" s", Text.matchFirstPaddedTarget(" s", "s"));
        assertEquals("s ", Text.matchFirstPaddedTarget("s ", "s"));
        assertEquals(" s ", Text.matchFirstPaddedTarget(" s ", "s"));
        
        assertEquals(" s ", Text.matchFirstPaddedTarget("1 s ", "s"));
        assertEquals(" s ", Text.matchFirstPaddedTarget("1 s 1", "s"));        
    }
    
    public void testTrailingSpaces(){
        String s = "";
        assertEquals("", Text.getTrailingSpaces(s, 0));
        
        s = "s";
        assertEquals("", Text.getTrailingSpaces(s, 0));
        
        s = "s ";//single space
        assertEquals(" ", Text.getTrailingSpaces(s, s.indexOf("s")));
        s = "s  ";//two spaces
        assertEquals("  ", Text.getTrailingSpaces(s, 0));
        s = "s    ";//four spaces
        assertEquals("    ", Text.getTrailingSpaces(s, 0));
        
        s = "s"+'\t';
        assertEquals("\t", Text.getTrailingSpaces(s, 0));
        s = "s"+System.lineSeparator();
        //System.out.println( s.indexOf("s"));
        assertEquals( System.lineSeparator(), Text.getTrailingSpaces( s, 0) );                
    }
    
}
