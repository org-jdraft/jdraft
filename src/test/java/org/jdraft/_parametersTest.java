package org.jdraft;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eric
 */
public class _parametersTest extends TestCase {

    public void testNewApiEmpty(){
        _parameters _ts = _parameters.of();
        assertEquals(_ts, _ts.copy());
        assertEquals(_ts.hashCode(), _ts.copy().hashCode());

        assertTrue(_ts.is(""));
        assertFalse(_ts.has(_parameter.of("int i")));
        assertTrue(_ts.is( new ArrayList<>()));
        assertTrue(_ts.isEmpty());
        assertEquals(0, _ts.size());
        assertTrue(_ts.list().isEmpty());
        assertTrue(_ts.listAstElements().isEmpty());
        _ts.forEach(t-> System.out.println(t));

    }

    public void testFromScratch(){
        _parameters _ps = _parameters.of();
        assertTrue( _ps.isEmpty());
        _ps.add("int a");
        assertFalse( _ps.isEmpty());

    }
    public void testNames(){
        _parameters _ps = _parameters.of( (Object a, Object b)->{} );
        List<String> names = _ps.names();
        assertEquals( "a", names.get(0) );
        assertEquals( "b", names.get(1) );
    }

    public void testRemove(){
        _parameters _ps = _parameters.of( "int a", "String name", "Map m");
        _ps.remove( p-> p.isType(int.class));

        System.out.println( _ps );
    }
    public void testP(){
        _parameters _ps = _parameters.of("int a, String b");
        assertEquals( 2, _ps.size() );
        assertEquals( _parameter.of( "int a" ), _ps.get( 0 ) );
        assertEquals( _parameter.of( "String b" ), _ps.get( 1 ) );
        assertTrue( _ps.is("int  a", "String  b" ));
    }
    
    public void testFullThing(){
        _parameters _ps = _parameters.of( "final List<String,Integer>vals, @ann(key=1,VALUE=2) @ann2(key2=1,value2=1)Object...vargs");
        assertEquals(2, _ps.size());
        assertEquals( _parameter.of("final List<String,Integer>vals"), _ps.get( 0));
        //verify that with annotation ELEMENTS out of order, and ANNOTATIONS out of order, I can STILL
        // understand that the two are equivalent
        assertEquals( _parameter.of("@ann2(value2=1,key2=1) @ann(VALUE=2,key=1)Object... vargs"), _ps.get( 1));
    }


    public static class B{

        public void param(){

        }

        public String method(){
            return "STR";
        }

        public String method( int a ){
            return "STR";
        }

        public String method( String a ){
            return "STR";
        }
    }

    public void testParamsCopy(){
        _parameters _ps = _parameters.of();
        _ps.add("int x");
        _ps.add("int y");

        assertEquals( _ps, _ps.copy());
    }

    public void testEqualsOrderDoesntMatter(){
        _parameters _ps1 = _parameters.of();
        _parameters _ps2 = _parameters.of();
        assertEquals( _ps1, _ps2);

        assertEquals( _parameters.of("int a"), _parameters.of("int a") );
        assertEquals( _parameters.of("final int a"), _parameters.of("final int a") );

        assertEquals( _parameters.of("int a,String[] b"), _parameters.of("int a,String[] b") );

        assertEquals( _parameters.of("final @ann1 @ann2 Map<Integer,String>a"), _parameters.of("final @ann1 @ann2 Map<Integer,String>a") );

        _ps1 = _parameters.of("final @ann1 @ann2 Map<Integer,String>a");
        _ps2 = _parameters.of("final @ann1 @ann2 Map<Integer,String>a");

        _parameters _p1 = _parameters.of("final @ann1 @ann2 Map<Integer,String>a");
        _parameters _p2 = _parameters.of("final @ann1 @ann2 Map<Integer,String>a");

        assertEquals(_p1,_p2);
        _p2 = _parameters.of("final @ann2 @ann1 Map<Integer,String>a");

        assertEquals(_p1,_p2);

        assertTrue( _ps1.equals( _ps2 ) );

        assertEquals(
                _parameters.of("final @ann1 @ann2 Map<Integer,String>a"),
                _parameters.of("final @ann2 @ann1 Map<Integer,String>a") );

    }

    public void testConstruct() {
        _parameter _pa = _parameter.of( "String NAME" );
        assertEquals( "String", _pa.getType().toString() );
        assertEquals( "NAME", _pa.getName().toString() );

        //_p = _param.of( String.class, "NAME" );
        //assertEquals( "java.lang.String", _param.getType() );
        //assertEquals( "NAME", _param.getName() );
        _pa = _parameter.of( "@ann String NAME" );
        assertEquals( "@ann", _pa.getAnnos().toString().trim() );
        assertEquals( "String", _pa.getType().toString() );
        assertEquals( "NAME", _pa.getName().toString() );

        _pa = _parameter.of( "@ann1 @ann2 String NAME" );
        assertEquals( "@ann1", _pa.getAnnos().get( 0 ).toString() );
        assertEquals( "@ann2", _pa.getAnnos().get( 1 ).toString() );
        assertEquals( "String", _pa.getType().toString() );
        assertEquals( "NAME", _pa.getName().toString() );

        /*
        _param = _param.of(_anno.of( "@ann" ), "String", "NAME" );
        assertEquals( "@ann", _param.getAnnos().toString() );
        assertEquals( "String", _param.getType() );
        assertEquals( "NAME", _param.getName() );

        _param = _param.of(_anno.of( "@ann1" ),
            _anno.of( "@ann2" ), "String", "NAME" );
        assertEquals( "@ann1 @ann2", _param.getAnnos().toString() );
        assertEquals( "String", _param.getType() );
        assertEquals( "NAME", _param.getName() );
         */
        _pa = _parameter.of( "final String NAME" );
        assertEquals( "", _pa.getAnnos().toString() );
        assertEquals( true, _pa.isFinal() );
        assertEquals( "String", _pa.getType().toString() );
        assertEquals( "NAME", _pa.getName().toString() );

        _pa = _parameter.of( "final String... NAME" );
        assertEquals( "", _pa.getAnnos().toString() );
        assertEquals( true, _pa.isFinal() );
        assertEquals( true, _pa.isVarArg() );
        assertEquals( "String", _pa.getType().toString() );
        assertEquals( "NAME", _pa.getName().toString() );

        _pa = _parameter.of( "@ann1 @ann2 final Map<Integer,String>... countToName" );
        assertEquals( "@ann1", _pa.getAnnos().get( 0 ).toString() );
        assertEquals( "@ann2", _pa.getAnnos().get( 1 ).toString() );
        assertEquals( true, _pa.isFinal() );
        assertEquals( true, _pa.isVarArg() );
        assertEquals( "Map<Integer, String>", _pa.getType().toString() );
        assertTrue( _pa.isVarArg() );
        assertEquals( "countToName", _pa.getName().toString() );


        //assertEquals( "@ann1 @ann2 final Map<Integer,String>... countToName", _pa.toString() );
        //System.out.println( _pa );
    }

    public void testParamsOf(){
        _parameters _ps =
                _parameters.of( "@ann final Map<? extends Integer, String>compose, String... names" );
        assertEquals( _ps.size(), 2);
        assertNotNull( _ps.get( 0 ).getAnnos().get("ann"));
        assertTrue( _ps.get( 0 ).isFinal() );
        assertEquals(
                _ps.get(0).getType(),
                _typeRef.of("Map<? extends Integer, String>" ) );
        assertFalse( _ps.get(0).isVarArg() );
        assertTrue( _ps.get(1).isVarArg() );

    }
}
