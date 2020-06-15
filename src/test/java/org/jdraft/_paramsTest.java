package org.jdraft;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eric
 */
public class _paramsTest extends TestCase {

    public void testNewApiEmpty(){
        _params _ts = _params.of();
        assertEquals(_ts, _ts.copy());
        assertEquals(_ts.hashCode(), _ts.copy().hashCode());

        assertTrue(_ts.is(""));
        assertFalse(_ts.has(_param.of("int i")));
        assertTrue(_ts.is( new ArrayList<>()));
        assertTrue(_ts.isEmpty());
        assertEquals(0, _ts.size());
        assertTrue(_ts.list().isEmpty());
        assertTrue(_ts.astList().isEmpty());
        _ts.toEach(t-> System.out.println(t));

    }

    public void testFromScratch(){
        _params _ps = _params.of();
        assertTrue( _ps.isEmpty());
        _ps.add("int a");
        assertFalse( _ps.isEmpty());

    }
    public void testNames(){
        _params _ps = _params.of( (Object a, Object b)->{} );
        List<String> names = _ps.names();
        assertEquals( "a", names.get(0) );
        assertEquals( "b", names.get(1) );
    }

    public void testRemove(){
        _params _ps = _params.of( "int a", "String name", "Map m");
        _ps.remove( p-> p.isType(int.class));

        System.out.println( _ps );
    }
    public void testP(){
        _params _ps = _params.of("int a, String b");
        assertEquals( 2, _ps.size() );
        assertEquals( _param.of( "int a" ), _ps.getAt( 0 ) );
        assertEquals( _param.of( "String b" ), _ps.getAt( 1 ) );
        assertTrue( _ps.is("int  a", "String  b" ));
    }
    
    public void testFullThing(){
        _params _ps = _params.of( "final List<String,Integer>vals, @ann(key=1,VALUE=2) @ann2(key2=1,value2=1)Object...vargs");
        assertEquals(2, _ps.size());
        assertEquals( _param.of("final List<String,Integer>vals"), _ps.getAt( 0));
        //verify that with annotation ELEMENTS out of order, and ANNOTATIONS out of order, I can STILL
        // understand that the two are equivalent
        assertEquals( _param.of("@ann2(value2=1,key2=1) @ann(VALUE=2,key=1)Object... vargs"), _ps.getAt( 1));
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
        _params _ps = _params.of();
        _ps.add("int x");
        _ps.add("int y");

        assertEquals( _ps, _ps.copy());
    }

    public void testEqualsOrderDoesntMatter(){
        _params _ps1 = _params.of();
        _params _ps2 = _params.of();
        assertEquals( _ps1, _ps2);

        assertEquals( _params.of("int a"), _params.of("int a") );
        assertEquals( _params.of("final int a"), _params.of("final int a") );

        assertEquals( _params.of("int a,String[] b"), _params.of("int a,String[] b") );

        assertEquals( _params.of("final @ann1 @ann2 Map<Integer,String>a"), _params.of("final @ann1 @ann2 Map<Integer,String>a") );

        _ps1 = _params.of("final @ann1 @ann2 Map<Integer,String>a");
        _ps2 = _params.of("final @ann1 @ann2 Map<Integer,String>a");

        _params _p1 = _params.of("final @ann1 @ann2 Map<Integer,String>a");
        _params _p2 = _params.of("final @ann1 @ann2 Map<Integer,String>a");

        assertEquals(_p1,_p2);
        _p2 = _params.of("final @ann2 @ann1 Map<Integer,String>a");

        assertEquals(_p1,_p2);

        assertTrue( _ps1.equals( _ps2 ) );

        assertEquals(
                _params.of("final @ann1 @ann2 Map<Integer,String>a"),
                _params.of("final @ann2 @ann1 Map<Integer,String>a") );

    }

    public void testConstruct() {
        _param _pa = _param.of( "String NAME" );
        assertEquals( "String", _pa.getType().toString() );
        assertEquals( "NAME", _pa.getName().toString() );

        //_p = _param.of( String.class, "NAME" );
        //assertEquals( "java.lang.String", _param.getType() );
        //assertEquals( "NAME", _param.getName() );
        _pa = _param.of( "@ann String NAME" );
        assertEquals( "@ann", _pa.getAnnos().toString().trim() );
        assertEquals( "String", _pa.getType().toString() );
        assertEquals( "NAME", _pa.getName().toString() );

        _pa = _param.of( "@ann1 @ann2 String NAME" );
        assertEquals( "@ann1", _pa.getAnnos().getAt( 0 ).toString() );
        assertEquals( "@ann2", _pa.getAnnos().getAt( 1 ).toString() );
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
        _pa = _param.of( "final String NAME" );
        assertEquals( "", _pa.getAnnos().toString() );
        assertEquals( true, _pa.isFinal() );
        assertEquals( "String", _pa.getType().toString() );
        assertEquals( "NAME", _pa.getName().toString() );

        _pa = _param.of( "final String... NAME" );
        assertEquals( "", _pa.getAnnos().toString() );
        assertEquals( true, _pa.isFinal() );
        assertEquals( true, _pa.isVarArg() );
        assertEquals( "String", _pa.getType().toString() );
        assertEquals( "NAME", _pa.getName().toString() );

        _pa = _param.of( "@ann1 @ann2 final Map<Integer,String>... countToName" );
        assertEquals( "@ann1", _pa.getAnnos().getAt( 0 ).toString() );
        assertEquals( "@ann2", _pa.getAnnos().getAt( 1 ).toString() );
        assertEquals( true, _pa.isFinal() );
        assertEquals( true, _pa.isVarArg() );
        assertEquals( "Map<Integer, String>", _pa.getType().toString() );
        assertTrue( _pa.isVarArg() );
        assertEquals( "countToName", _pa.getName().toString() );


        //assertEquals( "@ann1 @ann2 final Map<Integer,String>... countToName", _pa.toString() );
        //System.out.println( _pa );
    }

    public void testParamsOf(){
        _params _ps =
                _params.of( "@ann final Map<? extends Integer, String>compose, String... names" );
        assertEquals( _ps.size(), 2);
        assertNotNull( _ps.getAt( 0 ).getAnnos().get("ann"));
        assertTrue( _ps.getAt( 0 ).isFinal() );
        assertEquals(
                _ps.getAt(0).getType(),
                _typeRef.of("Map<? extends Integer, String>" ) );
        assertFalse( _ps.getAt(0).isVarArg() );
        assertTrue( _ps.getAt(1).isVarArg() );

    }
}
