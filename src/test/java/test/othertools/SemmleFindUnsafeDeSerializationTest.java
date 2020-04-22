package test.othertools;

import junit.framework.TestCase;
import org.jdraft.bot.$methodCall;
import org.jdraft.pattern.$body;

import java.io.InputStream;

/**
 * https://www.youtube.com/watch?v=XsUcSd75K00
 *
 * We dont have "flow control"... this should probably be constructed "outside"
 * since we need to create a flow graph... thats a little invovled, so as of right now
 * we cant do this
 */
public class SemmleFindUnsafeDeSerializationTest extends TestCase {

    public void testDeser(){
        class C{
            Object fromXML(){ return null; }
            Object fromXML(InputStream is){ return null; }

            void ff(){
                fromXML();
                fromXML(null);
            }
        }
        //here Im just illustrating that I can find
        assertEquals(2, $methodCall.of("fromXML").countIn(C.class));

        //this is how I might find methods that have a call to fromXML in their body
        //$method.of( $body.of($methodCall.of("fromXML")) );

    }
}
