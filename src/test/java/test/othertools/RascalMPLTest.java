package test.othertools;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._field;
import org.jdraft._method;
import org.jdraft.pattern.$;
import org.jdraft.pattern.$class;
import org.jdraft.pattern.$field;
import org.jdraft.pattern.$method;

import java.util.List;

/**
 * Examples to compare/contrast with Rascal MPL
 * https://www.rascal-mpl.org
 */
public class RascalMPLTest extends TestCase {

    /**
     * Using Rascal MPL to find public fields
     * https://youtu.be/Ffx7VtEOSx4?t=462
     */
    public void testFindPublicFieldsInSource(){
        $field $f = $field.of( $.PUBLIC );
        assertTrue( $f.matches( "public int i;") ); // here we can test it matches a simple field

        class LE {
            public int x;
            public int y;
        }
        /* to run this on the entire source directory replace LE.class with: _batch.of("C:\\jdraft\\project\\jdraft")); */
        /* to run this on the .jar file with source replace LE.class with: _archive.of("C:\\temp\\MyProject-src.jar")); */
        assertEquals(2, $f.countIn( LE.class ));
        // instead of just counting we can print each field to System out as we encounter
        $f.printIn( LE.class );

        // we can also collect/list all matching fields:
        List<_field> _fs = $f.listIn( LE.class );
        assertEquals( 2, _fs.size());

        // here we check for public fields on an interface... note although i, j are NOT explicitly public
        // they ARE IMPLICITLY PUBLIC because fields with initializers on imterfaces are public by default
        assertEquals( 2, $f.countIn(I.class));
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
     * NOTE: changed the requirements of this assignment to be more practical/real world
     * 1) we DONT want to consider STATIC fields (don't use getters and setters to these fields)
     * 2) we DONT want to create set methods on FINAL fields (only get() methods)
     *
     * 3) Also I added some additional tests to verify the refactored code turns out as we expect:
     */
    public void testConvertPublicFieldsOnClassesToPrivateWithGetSet(){
        /* pattern that finds all fields that are public and non-static defined in a Class */
        $field $publicFieldOnClass = $field.of($.PUBLIC, $.NOT_STATIC)
                .$isParentMember( $class.of() ); /* only do this on classes (not interfaces/enums) */

        class Sample {
            public int a;
            public int b;
        }
        /* read the Sample.class and modifiy it set fields and methods */
        _class _c = $publicFieldOnClass.forEachIn(Sample.class, f-> {
            f.setPrivate(); //make the field private
            _method._hasMethods _hms = f.getParentMember();
            _hms.addMethod( $getMethod.draft("type", f.getType(), "name", f.getName()) );

            if( !f.isFinal()){ /* 2) only create the set method for NON-final fields */
                _hms.addMethod( $setMethod.draft("type", f.getType(), "name", f.getName()) );
            }
        });
        /* 3) extra validation to verify results */
        assertEquals(2, $getMethod.countIn(_c) ); //verify (2) get methods in the result
        assertEquals(2, $setMethod.countIn(_c) ); //verify (2) set methods in the result
        assertEquals( 2, $field.of($.PRIVATE).countIn(_c)); //verify (2) private fields in the result
        $publicFieldOnClass.printIn( _c );
        System.out.println( $publicFieldOnClass );
        assertEquals( 0, $publicFieldOnClass.countIn(_c)); //verify (0) public fields in the result
        System.out.println( _c );
    }

    /** $method pattern for a get() method */
    private static final $method $getMethod = $method.of("public $type$ get$Name$(){ return $name$; }");

    /** $method pattern for a set() method */
    private static final $method $setMethod = $method.of("public void set$Name$($type$ $name$){ this.$name$=$name$;}");
}