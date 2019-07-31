package org.jdraft;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import junit.framework.TestCase;


public class walkTest extends TestCase {

    public void testWalkPackageInfo(){
        _packageInfo _pi = _packageInfo.of("package aaaa.xxxx.gggg;", "import java.util.*;", "import java.net.URL;");
        _walk.in(_pi, n -> System.out.println(n)); //walk nodes & do some action
        
        List<_import> imports = _walk.list(_pi, _import.class);
        assertEquals(2, imports.size());
        
        _import _i = _walk.first( _walk.PRE_ORDER, _pi, _import.class, t->true);
        assertEquals(_import.of("import java.util.*;"), _i);
        
        _i = _walk.first( _pi, _import.class );
        assertEquals(_import.of("import java.util.*;"), _i);
        
        
        ImportDeclaration astI = _walk.first(_walk.PRE_ORDER, _pi, ImportDeclaration.class, t->true);
        assertEquals(Ast.importDeclaration("import java.util.*;"), astI);
        
        astI = _walk.first(_pi, ImportDeclaration.class );
        assertEquals(Ast.importDeclaration("import java.util.*;"), astI);
        
    }
    
    /**
     * Note: _walk is much different than forMembers because it will walk into
     * nested classes.
     */
    public void testWalkList(){
        _class _c = _class.of("C")
            .fields("int x=1;", "int y=2;", "String z;");
        
        //these arent TOO interesting, just some simple tests
        //do some stuff with ASTs...
        assertEquals(3, _walk.list(_c, Ast.FIELD_DECLARATION).size());
        assertEquals(2, _walk.list(_c, Ast.FIELD_DECLARATION, fd->fd.getVariable(0).getType().isPrimitiveType() ).size());
        
        //using draft classes can also walk, a little more concise IMHO
        assertEquals(3, _walk.list(_c, _field.class).size());
        assertEquals(3, _walk.list(_c, _java.FIELD).size());
        assertEquals(2, _walk.list(_c, _field.class, fd->fd.isPrimitive()).size());
        assertEquals(1, _walk.list(_c, _field.class, fd->fd.isInit(2)).size());
        
        
    }
    
    public void testWalkIn(){
        _class _c = _class.of("C")
                .fields("int x=1;", "int y=2;", "String z;");


        //_walk.in(_c, _class.class, c-> System.out.println(c));
        AtomicInteger at = new AtomicInteger(0);
        _walk.in(_c, TypeDeclaration.class, td->at.incrementAndGet() );
        assertTrue(at.intValue() ==1);
        
        assertTrue( _walk.list(_c, Ast.ENUM_DECLARATION).isEmpty());
        _walk.in(_c, Ast.NODE_WITH_ABSTRACT_MOD, td->System.out.println(td) );
        _walk.in(_c, Ast.IMPORT_DECLARATION, td->System.out.println(td) );

    }
    
    
}
