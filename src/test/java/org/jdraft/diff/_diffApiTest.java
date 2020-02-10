package org.jdraft.diff;

import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft._java.Component;
import org.jdraft.macro.*;

/**
 *
 * @author Eric
 */
public class _diffApiTest extends TestCase {

    public void testDiffClasses(){
        class A { int a; int b=100; }
        class B { int a; int b=200; String name; }

        _diff _d = _diff.of( A.class, B.class );
        assertEquals("B", _d.listChanges().get(0).right() );
        assertEquals(Ex.of(100), _d.listChanges().get(1).left() );
        assertEquals(Ex.of(200), _d.listChanges().get(1).right() );
        assertEquals(_field.of("String name;"), _d.listRightOnlys().get(0).right() );

        //we can even directly patch
        //take the left value and patch it right (set B.b on class B to be 100, the value from A.b)
        _d.firstOn(_field.class, "b").patchLeftToRight();

        _class rr = (_class)_d.list().get(0).rightParent();

    }

    /**
     * Left only diff means there is an entity that is on the left 
     * but not on the right
     */
    public void testLeftOnlyDiff(){
         @_dto
        class FF{
            int x,y;
        }
        @_name("FF") @_dto
        class EE{
            int x,y;
        }
        //diff the two classes above (after the macro annotations of course)
        assertTrue(_diff.of(FF.class, EE.class).isEmpty());
        
        _class _a = _class.of(FF.class);
        _class _b = _class.of(EE.class);
        
        assertTrue(_diff.of(_a, _b).isEmpty());
        
        //add deprecated to _a only
        _a.addAnnos(Deprecated.class);
        
        _diff _d = _diff.of(_a, _b);
        
        assertEquals(1, _d.size());//there is (1) diff
        
        //ensure there is a diff AT this path (the entire path)
        assertNotNull(_d.at(_anno.class, "Deprecated"));
        assertNotNull(_d.at(_nodePath.of(_anno.class,"Deprecated")));
        
        //check there is a certain type of diff
        assertNotNull(_d.leftOnlyAt(_nodePath.of(_anno.class, "Deprecated"))); //at this "left"
        assertNotNull(_d.leftOnlyAt(_anno.class));
        
        assertNotNull(_d.leftOnlyOn(_anno.class, "Deprecated") ); //anywhere on the deprecated annotation
        assertNotNull(_d.leftOnlyOn(_anno.class) ); //anywhere on an annotation
        
        assertEquals(1, _d.listLeftOnlys().size()); //there is ONLY 1 "Left only" diff
        
        //get the first diff on the _anno
        assertNotNull(_d.firstOn(_anno.class)); 
        assertNotNull(_d.firstOn(_anno.class, "Deprecated")); 

        //verify there is a diff on a         
        //System.out.println( _d);
        assertTrue(_d.hasLeftOnlyOn(_anno.class));
        assertTrue(_d.hasLeftOnlyAt(_anno.class, "Deprecated"));     
    }
    
    public void testDiffLeftOnly(){
        class A{
            class Inner{
                int g = 100;
            }
        }
        
        @_name("A")
        class B{
            class Inner{
                int g = 100;
            }
        }
        //first start verifying they are both equal
        _class _a = _class.of(A.class);
        _class _b = _class.of(B.class);
        
        assertTrue(_diff.of(_a, _b).isEmpty());
        
        //change something on the left (_a) add annotation to field g
        _a.getNest("Inner").getField("g").addAnnos(Deprecated.class);
        
        _diff _d = _diff.of(_a,_b);
        //How many changes?
        assertEquals(1, _d.size() ); //1 diff
        _diffNode _dn = _d.list().get(0);
        //looking at the individual _diffNode
        assertTrue(_dn.isLeftOnly());
        
        //assertTrue(_dn.at(Component.ANNOTATION));
        assertTrue(_dn.at("Deprecated"));        
        assertTrue(_dn.at(Component.ANNO, "Deprecated"));
        assertTrue(_dn.at(_anno.class, "Deprecated"));
        assertTrue(_dn.at("Deprecated"));        
        assertTrue(_dn.at(Component.ANNO, "Deprecated"));
        
        //What has changed? (a Deprecated _anno)
        assertTrue( _d.isDiffOf(_anno.of(Deprecated.class)));//the diff Object is the Deprecated _anno
        
        //you wont use this much, but it's available (pass in the FULL PATH to where the diff occurs)
        assertEquals(_dn, _d.at(Component.NEST, "", _class.class, "Inner", _field.class, "g", _anno.class, "Deprecated"));
        assertEquals(_dn, _d.at(_nodePath.of(Component.NEST, "", _class.class, "Inner", _field.class, "g", _anno.class, "Deprecated")));
        
        //Where does the change occur
        // At = on what "leaf node" in the AST
        // On = within some path of the AST 
        assertTrue(_d.isAt(_anno.class));        
        assertTrue(_d.isAt(Component.ANNO));
        assertTrue(_d.isAt("Deprecated"));
        
        assertTrue(_d.isAt(Component.ANNO, "Deprecated"));
        assertTrue(_d.isAt(_anno.class, "Deprecated"));
        
        //return the first matching 
        assertEquals(_dn, _d.firstAt("Deprecated"));
        assertEquals(_dn, _d.firstAt(_anno.class));
        assertEquals(_dn, _d.firstAt(_anno.class, "Deprecated"));
        assertEquals(_dn, _d.firstAt(Component.ANNO));
        assertEquals(_dn, _d.firstAt(Component.ANNO, "Deprecated"));
        
        //lets verify the leftOnlyAPI        
        //"AT" means the "leaf node" (or the last component of the Path)
        assertEquals(_dn, _d.leftOnlyAt(_anno.class, "Deprecated")); 
        assertEquals(_dn, _d.leftOnlyAt("Deprecated"));
        assertEquals(_dn, _d.leftOnlyAt(_anno.class )); 
        //because there is NOT a leaf change on the class, this returns null
        
        //it's a diff NOT at the _class (but rather "on" a subtree of the
        // class (specifically at a _anno of a _field)
        assertNull( _d.leftOnlyAt(_class.class)); 
        assertEquals(_dn, _d.leftOnlyOn(_class.class)); //the path of the diff CONTAINS "_class)
        
        //ON (works for the direct at location)
        assertTrue(_d.isOn("Deprecated"));
        assertTrue(_d.isOn(_anno.class));
        assertTrue(_d.isOn(Component.ANNO));
        assertTrue(_d.isOn(Component.ANNO, "Deprecated"));
        assertTrue(_d.isOn(_anno.class, "Deprecated"));
        
        //On checks the input to each node in the path graph to the _diffNode
        assertTrue(_d.isOn(Component.NEST));
        assertTrue(_d.isOn(Component.CLASS, "Inner"));
        assertTrue(_d.isOn("g"));
        assertTrue(_d.isOn(_field.class, "g"));
        assertTrue(_d.isOn(Component.FIELD, "g"));
        assertTrue(_d.isOn(_class.class));
        assertTrue(_d.isOn("Inner"));
        
        assertEquals(_dn, _d.on(_anno.class));
        assertEquals(_dn, _d.on(_anno.class, "Deprecated"));
        assertEquals(_dn, _d.on(Component.ANNO));
        assertEquals(_dn, _d.on(Component.ANNO, "Deprecated"));
        
        //"ON" means get the first one that is (at or below) this path
        assertEquals(_dn,_d.leftOnlyOn("Deprecated"));        
        assertEquals(_dn,_d.leftOnlyOn(_anno.class, "Deprecated"));         
        assertEquals(_dn,_d.leftOnlyOn(_anno.class)); 
        //tghere IS a change at (or beneath) the a class
        assertEquals(_dn,_d.leftOnlyOn(_class.class)); //it is On the _class
        //there is a change at of beneath a class named "Inner"
        assertEquals(_dn,_d.leftOnlyOn(_class.class, "Inner")); 
        assertEquals(_dn,_d.leftOnlyOn("Inner"));               
        
        //Merge the change on the left (set init to 200)
        _dn.patchLeftToRight();
        
        assertEquals(_a, _b);
        assertTrue(_a.getNest("Inner").getField("g").hasAnno(Deprecated.class));
        assertTrue(_b.getNest("Inner").getField("g").hasAnno(Deprecated.class));
    }
    
    
    /**
     * Do the tests above but change the order (so the add is to the right)
     */
    public void testDiffRightOnly(){
        class A{
            class Inner{
                int g = 100;
            }
        }
        
        @_name("A")
        class B{
            class Inner{
                int g = 100;
            }
        }
        //first start verifying they are both equal
        _class _a = _class.of(A.class);
        _class _b = _class.of(B.class);
        
        assertTrue(_diff.of(_b, _a).isEmpty());
        
        //change something on the left (_a) add annotation to field g
        _a.getNest("Inner").getField("g").addAnnos(Deprecated.class);
        
        _diff _d = _diff.of(_b,_a);
        
        //How many changes?
        assertEquals(1, _d.size() ); //1 diff
        _diffNode _dn = _d.list().get(0);
        
        //What has changed? (a Deprecated _anno)
        assertTrue( _d.isDiffOf(_anno.of(Deprecated.class)));//the diff Object is the Deprecated _anno
        
        //you wont use this much, but it's available (pass in the FULL PATH to where the diff occurs)
        assertEquals(_dn, _d.at(Component.NEST, "", _class.class, "Inner", _field.class, "g", _anno.class, "Deprecated"));
        assertEquals(_dn, _d.at(_nodePath.of(Component.NEST, "", _class.class, "Inner", _field.class, "g", _anno.class, "Deprecated")));
        
        //Where does the change occur
        // At = on what "leaf node" in the AST
        // On = within some path of the AST 
        assertTrue(_d.isAt(_anno.class));        
        assertTrue(_d.isAt(Component.ANNO));
        assertTrue(_d.isAt("Deprecated"));
        
        assertTrue(_d.isAt(Component.ANNO, "Deprecated"));
        assertTrue(_d.isAt(_anno.class, "Deprecated"));
        
        //return the first matching 
        assertEquals(_dn, _d.firstAt("Deprecated"));
        assertEquals(_dn, _d.firstAt(_anno.class));
        assertEquals(_dn, _d.firstAt(_anno.class, "Deprecated"));
        assertEquals(_dn, _d.firstAt(Component.ANNO));
        assertEquals(_dn, _d.firstAt(Component.ANNO, "Deprecated"));
        
        //lets verify the leftOnlyAPI        
        //"AT" means the "leaf node" (or the last component of the Path)
        assertEquals(_dn, _d.rightOnlyAt(_anno.class, "Deprecated")); 
        assertEquals(_dn, _d.rightOnlyAt("Deprecated"));
        assertEquals(_dn, _d.rightOnlyAt(_anno.class )); 
        //because there is NOT a leaf change on the class, this returns null
        
        //it's a diff NOT at the _class (but rather "on" a subtree of the
        // class (specifically at a _anno of a _field)
        assertNull( _d.rightOnlyAt(_class.class)); 
        assertEquals(_dn, _d.rightOnlyOn(_class.class)); //the path of the diff CONTAINS "_class)
        
        //ON (works for the direct at location)
        assertTrue(_d.isOn("Deprecated"));
        assertTrue(_d.isOn(_anno.class));
        assertTrue(_d.isOn(Component.ANNO));
        assertTrue(_d.isOn(Component.ANNO, "Deprecated"));
        assertTrue(_d.isOn(_anno.class, "Deprecated"));
        
        //On checks the input to each node in the path graph to the _diffNode
        assertTrue(_d.isOn(Component.NEST));
        assertTrue(_d.isOn(Component.CLASS, "Inner"));
        assertTrue(_d.isOn("g"));
        assertTrue(_d.isOn(_field.class, "g"));
        assertTrue(_d.isOn(Component.FIELD, "g"));
        assertTrue(_d.isOn(_class.class));
        assertTrue(_d.isOn("Inner"));
        
        assertEquals(_dn, _d.on(_anno.class));
        assertEquals(_dn, _d.on(_anno.class, "Deprecated"));
        assertEquals(_dn, _d.on(Component.ANNO));
        assertEquals(_dn, _d.on(Component.ANNO, "Deprecated"));
        
        //"ON" means get the first one that is (at or below) this path
        assertEquals(_dn,_d.rightOnlyOn("Deprecated"));        
        assertEquals(_dn,_d.rightOnlyOn(_anno.class, "Deprecated"));         
        assertEquals(_dn,_d.rightOnlyOn(_anno.class)); 
        //tghere IS a change at (or beneath) the a class
        assertEquals(_dn,_d.rightOnlyOn(_class.class)); //it is On the _class
        //there is a change at of beneath a class named "Inner"
        assertEquals(_dn,_d.rightOnlyOn(_class.class, "Inner")); 
        assertEquals(_dn,_d.rightOnlyOn("Inner"));           
        
        //apply the right only change to all changes
        //this will add the Deprecated Anno
        _dn.patchRightToLeft();
        assertEquals(_a, _b);
        assertTrue( _a.getNest("Inner").getField("g").hasAnno(Deprecated.class) );
        assertTrue( _b.getNest("Inner").getField("g").hasAnno(Deprecated.class) );
    }
    
    
    /**
     * Changes occur generally to "required properties" (names etc)
     * i.e.
     * //here we changed the 
     * int x=0;    int x=3;
     */
    public void testChange(){
        class A{
            class Inner{
                int g = 100;
            }
        }
        
        @_name("A")
        class B{
            class Inner{
                int g = 100;
            }
        }
        //first start verifying they are both equal
        _class _a = _class.of(A.class);
        _class _b = _class.of(B.class);
        
        assertTrue(_diff.of(_a, _b).isEmpty());
        
        //change something on _b (the right)
        _a.getNest("Inner").getField("g").setInit(200);
        
        _diff _d = _diff.of(_a, _b);
        
        _diffNode _dn = _d.list().get(0);
        
        assertTrue(_dn.isChange());
        assertEquals(Ex.of(200), _dn.asChange().left());//the left value
        assertEquals(Ex.of(100), _dn.asChange().right());//the right value
        assertTrue( _dn.at(Component.INIT) );
        assertTrue(_dn.on(_class.class));
        assertTrue(_dn.on("Inner"));
        
        assertTrue(_d.isDiffOf(Ex.of(200))); //there is some change from/to 200
        assertTrue(_d.isDiffOf(Ex.of(100))); //there is some change from/to 100
        
        assertTrue( _d.hasChange() );
        assertTrue( _d.hasChangeAt(Component.INIT));        
        
        assertTrue( _d.hasChangeOn(Component.INIT));
        assertTrue( _d.hasChangeOn(Component.FIELD));
        assertTrue( _d.hasChangeOn(Component.FIELD, "g"));       
        
        //OK, here's where it gets interesting, merge the change from leftToRight
        //which means take the value 200 in a and merge the change into b        
        _dn.patchLeftToRight(); //keep the left version (200)
        
        //after merging the change make sure 
        // _a and _b are both equal
        assertTrue( _diff.of(_a, _b).isEmpty());
        //AND verify they are now BOTH 200
        assertEquals( _a.getNest("Inner").getField("g").getInit(), Ex.of(200));
        assertEquals( _b.getNest("Inner").getField("g").getInit(), Ex.of(200));
        //assertEquals( _a.getNest("Inner").getField("g"), _a.getNest("Inner").getField("g"));        
    }
    
    /*
    public void testFD(){
        _lambda _l = of( (a)-> {
            System.out.println(12);
            return 123;
        });
        
        System.out.println( "BODY" + _l.getBody() );
        
    }
    */
    public void testEdit(){
        class E{
            class Inner{
                public void m(){
                    System.out.println(1);
                    assert(1==1);
                    System.out.println(2);
                }
            }
        }
        @_name("E")
        class F{
            class Inner{
                public void m(){
                    System.out.println(1);
                    assert(1==1);
                    System.out.println(2);
                }
            }
        }
        _class _a = _class.of(E.class);
        _class _b = _class.of(F.class);
        assertEquals(_a, _b);
        assertTrue( _diff.of(_a,_b).isEmpty());
        
        _method m = (_method)_a.getNest("Inner").getDeclared( _method.class );
        
        //add a new Statement at the beginning of the method
        m.add(0, Stmt.of(()->{ System.out.println("started");}) );
        
        assertTrue(_diff.of(_a, _b).hasEdit());
        //now make a change to the code of _a
        //List<_method> _lm = _a.getNest("Inner").listMembers(_method.class);
        
        //_method _m = _a.getNest("Inner").listMembers(_method.class).get(0);
    }
}
