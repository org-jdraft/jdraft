package test.byexample.pattern;

import junit.framework.TestCase;
import org.jdraft._field;
import org.jdraft.pattern.$field;

public class _Pattern_DraftFillTest extends TestCase {

    /**
     * prototypes can be "specialized" to produce a meta-representation versions
     * using the draft() / fill() ) methods
     *
     * for "Fully qualified" prototypes (i.e. containing enough information to be valid meta-representations)
     */
    public void testProtoDraftFill(){

        /* a simple (not-parameterized) prototype */
        $field $f = $field.of("int i;");



        // we can call draft() to return a (_field) meta-representation instance
        // there are no parameters to specialize here
        _field _f = $f.draft();
    }

    public void testParametricSpecialization(){
        //a parameterized $field expects the parameters
        // "name" and "type" to specialize using draft()/fill
        $field $f = $field.of("public $type$ $name$;");

        // fill() is an alternative shorthand for draft(), it is used when
        // we KNOW the ORDER of the parameters {"type", "name} and we
        // PASS IN THE VALUES ONLY IN ORDER for specialization
        _field _f = $f.fill(int.class, "x");

        //using draft, we can pass in alternating key/value pairs
        _f = $f.draft("type", int.class, "name", "x");
        //draft will "plug in" the values for the parameters based on the name
        assertTrue( _f.is("public int x;"));

        //fill will "plug in" the values (first the $type$, then the $name$)
        assertTrue( _f.is("public int x;"));
    }

}
