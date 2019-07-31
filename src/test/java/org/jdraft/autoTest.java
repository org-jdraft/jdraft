package org.jdraft;

import org.jdraft.macro._static;
import org.jdraft.macro._replace;
import org.jdraft.macro._implement;
import org.jdraft.macro._public;
import org.jdraft._class;
import junit.framework.TestCase;

import java.io.Serializable;

public class autoTest extends TestCase {

    @_public
    //@_replace({"Internal", "Modified"})
    @_implement(Serializable.class)
    class Internal{

        @_replace({"int", "float"})
        public int value;

        @_public
        @_replace({"message", "hello world"})
        void doRep( ){
            System.out.println( "message" );
        }

        @_static
        @_replace()
        void anotherRep(){
            System.out.println( "message"); //isnt replaced
        }

        @_replace({"Start","End","first", "last"})
        void letsGetThisPartyStarted(){
            int first = 0;
        }
    }

    public void testY(){
        _class _c = _class.of( Internal.class );
        System.out.println("NOT PROCESSED" + _c );

        _c = _class.of(Internal.class);
        System.out.println("PROCESSED" + _c );
    }
}
