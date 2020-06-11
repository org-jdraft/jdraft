package org.jdraft.diff;

import com.github.javaparser.utils.Log;
import org.jdraft.*;

import static org.jdraft.diff.Feature.*;

import org.jdraft.diff._diffNode._change;
import org.jdraft.diff._diffNode._edit;
import org.jdraft.macro.*;

import java.util.List;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import org.jdraft.diff._diffNode._rightOnly;
import org.jdraft.diff._diffNode._leftOnly;

/**
 * 
 * @author Eric
 */
public class _inspectTest extends TestCase {



    public void setUp(){
        Log.setAdapter( new Log.StandardOutStandardErrorAdapter());
    }

    public void tearDown(){
        Log.setAdapter(new Log.SilentAdapter());
    }

    public void testMulti(){
        _class _v1 = _class.of("C");
        _class _v2 = _class.of("C", new @_get @_set Object(){
            int x;
            int y;
            
            void m(){
                System.out.println("m");
            }
            void n(){
                System.out.println("n");
            }
        });
        
        //move ALL members from _v2 to _v1
        _v1.add(_v2.listDeclared().toArray(new _java._declared[0]));
        assertEquals( _v1, _v2);        
    }


    /*
    public void testNewApiSingleChanges(){
        _class _v1 = _class.of("C");
        _class _v2 = _v1.copy();
        assertTrue(_v1.diff(_v2).isEmpty()); //they the same

        _v2.field("int a;");       //add a change to _v2 only
        _diff _d = _v1.diff(_v2);  //diff _v1 with _v2

        assertTrue(_d.isAt(FIELD));      //there is a DIFF AT at least one FIELD
        assertTrue(_d.isAt(FIELD, "a")); //there is a DIFF AT at a field named "a"

        //find the first Diff AT a FIELD and verify it is an Add
        assertTrue(_d.firstAt(FIELD).isAdd()); //Add means added between _v1 and _v2

        //find the first Diff AT a FIELD named "a" and verify it is an Add
        assertTrue(_d.firstAt(FIELD, "a").isAdd());

        //OK, lets change to v2.diff(_v1);
        _d = _v2.diff(_v1); // _v2 -> _v1

        // (v2->v1) find the same diffs, however they are REMOVE, not ADD
        assertTrue(_d.firstAt(FIELD).isRemove()); //Removed between _v2 and _v1

        // (v2->v1) find the same diffs, however they are REMOVE, not ADD
        assertTrue(_d.firstAt(FIELD, "a").isRemove());

        _d = _v1.field("int a;").diff(_v2);
        assertTrue( _d.isEmpty() );

        // alright, we realize if we change the order (_v1->_v2) to (_v2->_v1)
        // from now on well ALWAYS make changes to _v2 to show how they show up
        // in the diff
        _method _m = _method.of("void m(){}");
        _v2.method(_m);
        _d = _v1.diff(_v2);
        assertTrue(_d.isAt(METHOD) ); //there is a METHOD diff
        assertTrue(_d.isAt(METHOD,"m()") ); //there is a diff on METHOD with signature "m()"
        assertTrue(_d.firstAt(METHOD,"m()").isAdd() ); //the first method diff is an Add
        assertEquals( _m, _d.firstAt(METHOD,"m()").asAdd().add); //verify it is the same method
        _v1.method(_m.copy());
        assertTrue( _v1.diff(_v2).isEmpty() );

        //now lets change the v2 method modifiers
        _v2.getMethod("m").setStatic();
        _d = _v1.diff(_v2);

        //System.out.println( _d );
        assertTrue( _d.has(METHOD, MODIFIERS));
        //System.out.println( _d );
        //System.out.println( _path.of(METHOD, "m()", MODIFIERS) );
        assertEquals( _path.of(METHOD, "m()", MODIFIERS), _path.of(METHOD, "m()", MODIFIERS));
        assertTrue( _d.atPath(METHOD, "m()", MODIFIERS).isChange() );

        //update v1 to make them equal now
        _v1.getMethod("m").setStatic();


        //now change the body in v2
        _v2.getMethod("m").setBody(() -> {System.out.println(1);} );
        _d = _v1.diff(_v2);

        //System.out.println( _d );
        assertTrue( _d.has(METHOD, BODY));
        assertTrue( _d.firstAt(BODY).isEdit() );
        assertTrue( _d.atPath(METHOD,"m()", BODY).isEdit() );

        //verify that I can get the edit node and check it against the method
        assertEquals( _m.getBody(), _d.atPath(METHOD,"m()",BODY).asEdit().right );
        assertEquals( _v1.getMethod("m").getBody(), _d.atPath(METHOD,"m()",BODY).asEdit().left );

        _body _v2bd = (_body)_d.atPath(METHOD,"m()",BODY).asEdit().right();


        //Can I MERGE a Change from V2 back to V1
        // YEAH LETS DO THIS AT THE AST LEVEL

        //if its an ADD
        // I just ADD the thing (i.e. ADDMEMBER)
        //if its a REMOVE
        // just remove Member

        //get the line count of the body
        System.out.println( _v2bd.ast().getRange().get().getLineCount() );

        //_d.atPath(METHOD,"m()", BODY).asEdit()

        _constructor ct = _constructor.of("C(){}");
        _v2.constructor(ct);

        _d = _v1.diff(_v2);

        assertTrue(_d.atPath(CONSTRUCTOR, "C()").isAdd());
    }
    */

    public void testField(){
        _field _f1 =_field.of("int a;");
        _field _f2 =_field.of("int a;");
        
        _class _c1 = _class.of("C");
        _class _c2 = _class.of("C").addField(_f2);
               
        _diff dt = _diff.of(_c1, _c2);
        assertNotNull( dt.firstAt(FIELD, "a") );
        assertTrue(dt.firstAt(FIELD, "a") instanceof _rightOnly); //its added between left and right
        
        _c1.addField(_f1);
        dt = _diff.of(_c1, _c2);
        assertTrue( dt.isEmpty() );
        
        _f1.setInit(1);
        dt = _diff.of(_c1, _c2);
        System.out.println( dt );
        assertTrue(dt.at(_nodePath.of(FIELD,"a",INIT)) instanceof _change); //field init is change from left to right
        _f2.setInit(1);
        _f2.addAnnoExprs("@Ann");
        dt = _diff.of(_c1, _c2);
        assertTrue(dt.firstAt(ANNO_EXPR) instanceof _rightOnly);
        
        _f1.addAnnoExprs("@Ann");
        _f1.setPrivate();
        dt = _diff.of(_c1, _c2);
        assertTrue(dt.firstAt(MODIFIERS) instanceof _change ); 
        
        _f2.setPrivate();        
        _f2.setJavadoc("A javadoc");
        dt = _diff.of(_c1, _c2);
        assertTrue(dt.firstAt(JAVADOC) instanceof _change );
        
        _f1.setJavadoc("A javadoc");
        _f1.setType(float.class);
        _f1.setInit(1.0f);
        dt = _diff.of(_c1, _c2);
        assertTrue(dt.firstAt(TYPE) instanceof _change );
        
        _f2.setType(float.class);
        _f2.setInit(1.0f);
        _f2.setName("b");
        dt = _diff.of(_c1, _c2);
        //When we change the name of the field, we are affectively changing the API
        // so instead of it being considered a CHANGE it is rather a: 
        // REMOVE (of the old field "a")
        // ADD ( of the new field "b")
        // ...since all accesses to "a" are have to change to "b"
        assertTrue(dt.listAt(FIELD).size() == 2 );
        
        _f1.setName("b");
        dt = _diff.of(_c1, _c2);
        assertTrue(dt.isEmpty());        
    }
    
    public void testBody(){
        _method _m1 = _method.of( new Object(){ void m(){} } );
        _method _m2 = _method.of( new Object(){ void m(){} } );
        _class _c1 = _class.of("C").addMethod(_m1);
        _class _c2 = _class.of("C").addMethod(_m2);
        assertTrue(_diff.of(_c1, _c2).isEmpty());
        
        _m1.add(Stmt.of(()->System.out.println(1)));
        
        //_m1.setBody(()->{System.out.println(1);} );
        
        _diff _dt = _diff.of(_c1, _c2);
        assertTrue(_dt.firstAt(BODY) instanceof _edit );
        assertTrue(_dt.firstAt(BODY) instanceof _edit );
        
        _dt.listEdits();
        
        _dt.first(d -> d instanceof _edit );


        List<_diffNode._edit> le = _dt.listEdits();
        _diffNode._edit dne = le.get(0);
        System.out.println("REMOVED: " + le.get(0).forRemoves(r -> System.out.println( r)));
        
        //_dt.first(BODY).textDiff().forRemoves()...;
        //_dt.first(BODY).textDiff().for
        
        
        
        // changing any of these
        // name, parameter types
        // will diff as an ADD / REMOVE instead of a change        
    }    
    
    
    public void testMethodDiff(){
        _class _c1 = _class.of("C").addMethod("void m(){}");
        _class _c2 = _class.of("C").addMethod("void m(){}");
        _c2.toMethods(m-> m.addAnnoExprs(Deprecated.class));
        
        //System.out.println( _c2 );
        _diff dt = _diff.of(_c1, _c2);
        
        System.out.println( dt );
        
        assertEquals( 1, dt.listAt(ANNO_EXPR).size() );
        assertTrue(dt.firstAt(ANNO_EXPR) instanceof _rightOnly ); //its Added from left -> right
        assertNotNull(dt.firstOn(_method.class, "m()"));
        assertNotNull(dt.firstAt(ANNO_EXPR));
       
        _c1.toMethods(m -> m.setFinal());
        
        dt = _diff.of(_c1, _c2);
        //System.out.println( dt );
        assertNotNull(
            dt.firstAt(MODIFIERS) );     
        
        assertTrue(
            dt.firstAt(MODIFIERS) instanceof _change );            
        //System.out.println( dt );        
    }
    public void testClassDiff(){
        class C{
            int a;
            void m(){}
            C(){}
        }

        @_rename("C")
        class D{
            int a;
            void m(){}
            D(){}
        }
        _class _c1 = _class.of(C.class);
        _class _c2 = _class.of(D.class);
        
        assertTrue(_diff.of(_c1, _c2).isEmpty());
        
        _diff dt = _diff.of(_c1.setName("D"), _c2);
        
        //there is a name diff
        System.out.println( dt );
        
        //inserted is [+] null, right
        //deleted  is [-] left, null
        //similar is  [~] left, right
        
        //undercomponent 
        //onComponent    (NAME)
        
        assertTrue(dt.list(d -> ((_diffNode)d).at(NAME)).size() == 1);
        
        dt.forEach(d -> System.out.println( d.path().featurePath) );
        assertTrue(dt.listAt(CONSTRUCTOR).size() >= 1);
        
        _c1.firstMethodNamed("m").addAnnoExprs(Deprecated.class);
        dt = _diff.of(_c1, _c2);
        assertTrue(dt.firstAt(ANNO_EXPR) instanceof _leftOnly); //its removed from left -> right
        
        
        
    }
    
    public void testInspectBodyDiffs(){
        class C{
            public void m(){
               System.out.println(1);
               System.out.println(2);
            } 
        }
        @_rename("C")
        class D{
            public void m(){
               System.out.println(2);
               System.out.println(1);
            }
        }
        _class _c = _class.of( C.class );
        _class _c2 = _class.of( D.class );
        _diff.of(_c, _c2);
        //_c.diff(_c2);
        _diff dt = _diff.of(_c, _c2);
        System.out.println( dt );
        List<_nodePath> paths = dt.paths();
        System.out.println( dt.at( paths.get(0) ) );
        
        /*
        //transposed
        _method _c = _method.of(new Object(){
           public void m(){
               System.out.println(1);
               System.out.println(2);
           } 
        });
        _method _m2 = _method.of(new Object(){
           public void m(){
               System.out.println(2);
               System.out.println(1);
           } 
        });
        DiffTree dt = _inspect.INSPECT_METHOD.diffTree(_m, _m2);
        
        System.out.println( dt );
        */
        
    }

    public void testAnonymousClassAnnotations(){
        _class _c = _class.of( "AAAA", new @_get Object(){
           int x;
        });

        System.out.println( _c );
    }

    /** TODO FIX THE Anonymous Annotations (add @_dto) */
    public void testInspectMethods(){
        //add @_dto
        _class _c = _class.of("A", new Object(){
            @_final String name = "bozo";
            int x,y,z;                
            public @_static int calc(){
                return 1 * 2 * 3;
            }            
        });
        
        _class _d = _class.of("A", new Object(){
            final String name = "bozo";
            int x,y,z;                       
            public @_static int calc(){
                return 1 * 2 * 3;
            }            
        });
        
        List<_method> ms = _c.listMethods();
        List<_method> ms2 = _d.listMethods();
        
        
        /*
        assertTrue(_field.INSPECT_FIELDS.diff(_c.listFields(), _d.listFields()).isEmpty());
        assertTrue(_method.INSPECT_METHODS.diff(ms, ms2).isEmpty());                   
        assertTrue(Diff.of(_c, _d).isEmpty());        
        
        assertTrue(_field.INSPECT_FIELDS.diff(_c.listFields(), _d.listFields()).isEmpty());
        assertTrue(_method.INSPECT_METHODS.diff(ms, ms2).isEmpty());
        assertTrue(Diff.of(_c, _d).isEmpty());                
        */
    }
    
    /*
    public void testInspect_class(){
        _class _c = _class.of("A", new Object(){
            int x,y,z;
            
            
            public int b(){
                return 100;
            }
            
            String name = "Eric";
            
        }, _autoDto.$);
        
        _class _d = _class.of("A", new Object(){            
            String name = "Eric";
            
            public int b(){
                return 100;
            }
            int x,y,z;
        }, _autoDto.$);
        
        assertEquals( _c, _c);
        assertEquals( _c, _d);
        System.out.println( _c);
        System.out.println( _d);
        
        //System.out.println( _inspect.INSPECT_CLASS.diff(_c, _d). );
        
        assertTrue( _inspect.INSPECT_CLASS.diff(_c, _d).isEmpty());
    }
    */
    
    /*
    public void testInspectMethod(){
        _method _m1 = _method.of(new Object(){ 
            void a(){}
        });
        _method _m2 = _method.of(new Object(){ 
            void a(){}
        });
        
        assertTrue(_method.INSPECT_METHOD.diff(_m1, _m2).isEmpty());
        _m1.name("b");
        System.out.println( _method.INSPECT_METHOD.diff(_m1, _m2) );
        
        assertTrue(_method.INSPECT_METHOD.diff(_m1, _m2).firstOf(_java.Component.NAME) != null);
        _m1.setBody( ()-> System.out.println(1) );
        
        _diff dl = _method.INSPECT_METHOD.diff(_m1, _m2);
        assertTrue(dl.firstOf(BODY) != null);
        
        //_textDiff dmp = (_textDiff)dl.left(_java.Component.BODY);
        //System.out.println( dmp );
        //System.out.println( dmp.left() );
        //System.out.println( dmp.right() );
        
        //_m1.setBody( (a,b)-> System.out.println(1) );
    }
    
    public void testInspectStringBase(){
        StringInspect si = new StringInspect(NAME);
        assertTrue( si.equivalent("a", "a") );
        assertFalse( si.equivalent("A", "a") );
        
        assertTrue( si.diff("a", "a").isEmpty() );
        assertTrue( si.diff("a", "b").has(NAME) );        
    }
   
    
    interface C{
        void t() throws IOException, FileNotFoundException;
        void t2() throws java.io.FileNotFoundException, java.io.IOException;
        
        void t3() throws IOException;        
        void t4() throws FileNotFoundException;
    }
    
    public void testTypeListInspect(){
        _interface _i = _interface.of( C.class );
        
        assertTrue( _throws.INSPECT_THROWS.equivalent( 
            _i.getMethod("t").getThrows(),
            _i.getMethod("t2").getThrows() ));
        
        assertFalse( _throws.INSPECT_THROWS.equivalent( 
            _i.getMethod("t").getThrows(),
            _i.getMethod("t3").getThrows()));
        
        assertFalse( _throws.INSPECT_THROWS.equivalent( 
            _i.getMethod("t3").getThrows(),
            _i.getMethod("t4").getThrows()));
        
        
        assertTrue( _throws.INSPECT_THROWS.diff( 
            _i.getMethod("t").getThrows(),
            _i.getMethod("t2").getThrows()).isEmpty() );
        
        assertTrue( _throws.INSPECT_THROWS.diff( 
            _i.getMethod("t").getThrows(),
            _i.getMethod("t3").getThrows()).has( THROWS ) );
        
        assertTrue( _throws.INSPECT_THROWS.diff( 
            _i.getMethod("t3").getThrows(),
            _i.getMethod("t4").getThrows()).size() == 2 );        
    }
    
    class NTP{}
    class TP<T>{}
    class TPE<T extends Serializable>{}
    class TPE2<T extends java.io.Serializable>{}
    
    class TP21<T extends Serializable, R extends Runnable>{}
    class TP22<R extends java.lang.Runnable, T extends java.io.Serializable>{} //out of order & 
    
    public void testTypeParameters(){
        _typeParameters _ntp = _class.of(NTP.class).getTypeParameters();
        _typeParameters _tps = _class.of(TP.class).getTypeParameters();
        _typeParameters _tpe = _class.of(TPE.class).getTypeParameters();
        _typeParameters _tpe2 = _class.of(TPE2.class).getTypeParameters();
        
        _typeParameters _tp21 = _class.of(TP21.class).getTypeParameters();
        _typeParameters _tp22 = _class.of(TP22.class).getTypeParameters();
        
        assertTrue( _typeParameter.INSPECT_TYPE_PARAMETERS.equivalent(_ntp.ast(), _ntp.ast()) ); //none        
        assertTrue( _typeParameter.INSPECT_TYPE_PARAMETERS.equivalent(_tps.ast(), _tps.ast()) );//one simple        
        assertTrue( _typeParameter.INSPECT_TYPE_PARAMETERS.equivalent(_tpe.ast(), _tpe2.ast()) ); //one fully qual other not        
        assertTrue( _typeParameter.INSPECT_TYPE_PARAMETERS.equivalent(_tp21.ast(), _tp22.ast()) ); //out of order one fully qualified other not
        
        assertTrue( _typeParameter.INSPECT_TYPE_PARAMETERS.diff(_ntp, _tp22 ).has(TYPE_PARAMETER) );
        assertTrue( _typeParameter.INSPECT_TYPE_PARAMETERS.diff(_ntp, _tp22 ).size() == 2 );                
    }
    
    public @interface A{}    
    public @interface B{}
    
    public void testAnnos(){
        class F{
            @Deprecated int c;
            @java.lang.Deprecated int e;
            
            @A int a;
            @B int b;
               
            @A @B int ab;
            @draft.java.diff._inspectTest.B @draft.java.diff._inspectTest.A int ba;
        }
        _class _c = _class.of( F.class );
        _annos c = _c.getField("c").getAnnos();
        _annos e = _c.getField("e").getAnnos();
        
        _annos a = _c.getField("a").getAnnos();
        _annos b = _c.getField("b").getAnnos();
        
        _annos ab = _c.getField("ab").getAnnos();
        _annos ba = _c.getField("ba").getAnnos();
        
        //hashcode should be the same
        assertEquals( ab.hashCode(), ba.hashCode() );
        assertEquals( ab,ba );
        
        System.out.println( _anno.INSPECT_ANNOS.diff(c, e) );
        
        assertTrue(_anno.INSPECT_ANNOS.equivalent(c, e));
        assertTrue(_anno.INSPECT_ANNOS.equivalent(ab, ba));        
    }
    
    public void testParameters(){
        class C{
            void no(){}
            void oneString(String one){}
            void twoStrings(String one, String two){}
            void oneInt(int one){}
            void twoInt(int one, int two){}
        }
        _class _c = _class.of(C.class);
        assertTrue( _parameter.INSPECT_PARAMETERS.equivalent(
                _parameters.of(""), _parameters.of("()") ) );
        
        assertTrue( _parameter.INSPECT_PARAMETERS.equivalent(
                _parameters.of("String one"), _parameters.of("(String one)") ) );
        
        assertTrue( _parameter.INSPECT_PARAMETERS.equivalent(
                _parameters.of("()"), _parameters.of("()") ) );
        
        assertTrue( _parameter.INSPECT_PARAMETERS.diff(
                _parameters.of("()"), _parameters.of("(int i)") ).has(PARAMETER, "0") );
        
        assertTrue( _parameter.INSPECT_PARAMETERS.diff(
                _parameters.of("(int i)"), _parameters.of("()") ).has(PARAMETER, "0") );
        
        assertTrue( _parameter.INSPECT_PARAMETERS.diff(
                _parameters.of("(int i)"), _parameters.of("(String i)") ).has(PARAMETER, "0") );
        
        assertTrue( _parameter.INSPECT_PARAMETERS.diff(
                _parameters.of("(int i)"), _parameters.of("(int i, String s)") ).has(PARAMETER, "1") );
        
        assertTrue( _parameter.INSPECT_PARAMETERS.diff(
                _parameters.of("(int i, String s)"), _parameters.of("(int i)") ).has(PARAMETER, "1") );        
    }   
*/
}
