package test.byexample.batch;

import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.macro._package;
import org.jdraft.macro._public;
import org.jdraft.runtime._runtime;

import java.util.Optional;
import java.util.UUID;

public class _mergeInterfaceToClass extends TestCase {

    @_package("aaaa.bbbb")
    public class OnlyOne{
        public void alreadyImplemented(){
            System.out.println("keep this one");
        }
    }

    @_package("aaaa.bbbb")
    @_public interface I{

        /** Javadoc*/
        @Deprecated
        int ID = 1234;

        /**Javadoc*/
        @Deprecated
        static String staticMethod(){
            //here we are using a specific API not on the public API of the method
            // I want to ensure this class (UUID) is transposed to the class
            return UUID.randomUUID().toString();
        }

        /** Javadoc */
        @Deprecated
        default void defaultMethod(){}

        default void alreadyImplemented(){
            System.out.println( "OVERWROTE BY ACCIDENT");
        }
    }

    static _interface _i;
    static _class _onlyOne;

    /**
     * Here we are just creating the input that we use for the tests
     */
    public void setUp(){
        _i = _interface.of(I.class).addImports(UUID.class);
        _onlyOne = _class.of(OnlyOne.class).implement(_i);

        //remove the import for I
        _onlyOne.removeImports(_i);
        _onlyOne.removeImplements(_i);
    }

    /**
     * This does the merge
     * @param _i
     * @param _c
     * @return
     */
    public static _class merge(_interface _i, _class _c){
        //copy all static members from the interface to the class:
        //making the members explicitly public (since they are implicitly public on interface)
        _i.forMembers( _m -> {
            if( _m instanceof _modifiers._withStatic && ((_modifiers._withStatic) _m).isStatic()){
                _modifiers._withModifiers _wm = ((_modifiers._withModifiers)_m.copy());
                _wm.getModifiers().setPublic().setStatic();
                _c.add( (_java._memberBodyPart)_wm );
            }
        });

        //transpose all imports from interface to class
        _i.forImports( i-> _c.addImports(i));

        //copy all default methods (that are NOT explicitly implemented)
        _i.forMethods(m-> m.isDefault(), m-> {
            _method _mc = m.copy(); //use a working copy as to not change the existing interface method
            _mc.getModifiers().setDefault(false).setPublic();

            //describe the method signature to check for existing implemented match
            String sig = _method.describeMethodSignature(_mc);

            //check if the target class already has a method with the same signature
            Optional<_method> om = _c.listMethods().stream().filter(mm -> _method.describeMethodSignature(mm).equals(sig) ).findFirst();
            if( !om.isPresent()) { //only add a default method that has NOT been implemented in the class
                _c.add(_mc);
            }
        });

        return _c;
    }

    public void testMergeInterfaceMembersToClass(){
        _class _merged = merge(_i.copy(), _onlyOne.copy() );
        System.out.println( _merged );
        //make sure we moved over the field/s method/s
        assertEquals(4, _merged.listMembers().size());

        //make sure we imported imports from interface
        assertTrue( _merged.hasImports(UUID.class) );

        assertEquals( Stmt.of("System.out.println(\"keep this one\");"),
                _merged.getMethod("alreadyImplemented").getBody().getStatement(0));

        //for good measure, see if we can compile and load an instance
        _runtime.compile(_merged);
    }
}
