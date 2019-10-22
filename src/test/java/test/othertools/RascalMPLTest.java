package test.othertools;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._method;
import org.jdraft.pattern.$;
import org.jdraft.pattern.$class;
import org.jdraft.pattern.$field;
import org.jdraft.pattern.$method;

public class RascalMPLTest extends TestCase {

    /**
     * Using Rascal MPL to find public fields
     * https://youtu.be/Ffx7VtEOSx4?t=462
     * NOTE:
     */
    public void testFindPublicFieldsInSource(){
        $field $f = $field.of( $.PUBLIC );
        assertTrue( $f.matches( "public int i;") );
        class C{
            public int x;
            public int y;
        }
        /* to run this on the entire source _batch.of("C:\\jdraft\\project\\jdraft")); */
        assertEquals(2, $f.count( C.class ));
        // here we check for public fields... note although i, j are NOT explicitly public
        // they ARE IMPLICITLY PUBLIC because fields with initializers on imterfaces are public by default
        assertEquals( 2, $f.count(I.class));
    }

    /**
     * NOTE: even though i, j are NOT EXPLICITLY marked as public
     * they ARE IMPLICITLY public because of the context (a field on an interface with an initializer public by default)
     */
    interface I{
        int i=100;
        int j=200;
    }

    /**
     * Rascal MPL: Transform all public fields to be private and generates use getter and setter methods
     * https://youtu.be/Ffx7VtEOSx4?t=967
     *
     * we wanted to change the requirements of this assignment to be more practical
     * 1) we DONT want to consider STATIC fields (dont use getters and setters to these fields)
     * 2) we DONT want to create set methods on FINAL fields (only get() methods)
     *
     */
    public void testConvertPublicFieldsOnClassesToPrivateWithGetSet(){
        /* pattern that finds all fields that are public and non-static defined in a Class */
        $field $publicFieldOnClass = $field.of($.PUBLIC, $.NOT_STATIC).$isParentMember( $class.of() );

        class Sample {
            public int a;
            public int b;
        }
        _class _c = $publicFieldOnClass.forEachIn(Sample.class, f-> {
            f.setPrivate(); //make the field private
            _method._hasMethods _hms = f.getParentMember();
            _hms.method( $getMethod.draft("type", f.getType(), "name", f.getName()) );

            if( !f.isFinal()){ /* 2) only create the set method for NON-final fields */
                _hms.method( $setMethod.draft("type", f.getType(), "name", f.getName()) );
            }
        });
        System.out.println( _c );
    }

    /** $pattern for a get() method */
    private static final $method $getMethod = $method.of(new Object(){
        public $type$ get$Name$(){
            return $name$;
        }
        class $type${}
        $type$ $name$;
    });

    /** $pattern for a set() method */
    private static final $method $setMethod = $method.of(new Object(){
        public void set$Name$($type$ $name$){
            this.$name$ = $name$;
        }
        class $type${}
        $type$ $name$;
    });
}
