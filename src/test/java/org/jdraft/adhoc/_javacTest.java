package org.jdraft.adhoc;

import java.util.ArrayList;
import java.util.List;
import org.jdraft._class;
import org.jdraft.macro.*;
import junit.framework.TestCase;

public class _javacTest extends TestCase {

    public void testJ(){
        _class _c = _toString.Macro.to( _class.of("aaaa.bbb.C").field("int x,y,z;"));
        List<_bytecodeFile> _cfs =_adhoc.compile(_c);

        //use
        //_javac.of( _c);
        List<String>javacOptions = new ArrayList<>();
        javacOptions.add("-parameters");
        _adhoc.compile( javacOptions, _c);
    }

    /** TODO Maybe figure this out later 
    public void testParentChildClassLoader(){
        _class _a = _class.of("aaaa.bbbb.A", new Object(){ int x,y,z; }, _dto.$);
        _class _b = _class.of("aaaa.bbbb.B", new Object(){ float a, b,c;}, _dto.$).extend(_a);
        //System.out.println( _a );

        //I HAVE to call _new b/c I have to realize/ lo
        AdhocClassLoader _parentClassLoader = (AdhocClassLoader)_new.of(_a).getClass().getClassLoader();

        //compile b using the base class Loader (_clA) containing _a, it's base class
        //Adhoc.compile(_parentClassLoader, _b );
        //_project.of(_parentClassLoader, _b );

    }
    */ 
}
