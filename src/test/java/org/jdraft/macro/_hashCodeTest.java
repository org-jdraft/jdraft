package org.jdraft.macro;

import com.github.javaparser.ast.stmt.EmptyStmt;
import org.jdraft.*;
import org.jdraft.pattern.*;
import org.jdraft.runtime.*;

import junit.framework.TestCase;

import java.util.List;
import java.util.UUID;

public class _hashCodeTest extends TestCase {

    public void testCallingStrategies(){
        class A{ int x,y,z;}

        /** 1) call the _class constructor with the {@link _autoHashCode#$} argument */
        //_class _1 = _class.of(A.class, _hashCode.$);

        /** 2) call {@link draft.java._type#apply(macro[])} with {@link _autoHashCode#$} */
        _class _2 = (_class)_hashCode.Act.to( _class.of(A.class) ); //.apply(_hashCode.$);

        /** 3) call with static to method {@link _autoHashCode.Macro#to(T t)} */
        _class _3 = _hashCode.Act.to(_class.of(A.class));

        /** 4) annotate with @_autoHashCode & call annotation Macro processor {@link $$#to(T t)} */

        @_name("A") @_hashCode class B{ int x,y,z;}
        _class _4 = _class.of(B.class).name("A");

        //assertEquals( _1, _2);
        assertEquals( _2, _3);
        assertEquals( _3, _4);
    }

    public void test$HashCode(){
        //test the "outer" HashCode template
        _method _m = _hashCode.$HASHCODE.draft("seed",1,"prime",2);
        System.out.println( _m );
        _m.ast().walk(EmptyStmt.class, es-> es.remove() );
        System.out.println( _m );
        assertEquals( 3, _hashCode.$HASHCODE.draft("seed",1,"prime",2).getBody().getStatements().size() );
        assertEquals( 4, _hashCode.$HASHCODE.draft("seed", 1,"prime", 2, "callSuperHashCode", true, "body", false).getBody().getStatements().size() );
        assertEquals( 5, _hashCode.$HASHCODE.draft("seed", 1,"prime", 2, "callSuperHashCode", true, "body", Stmt.of("System.out.println(1);")).getBody().getStatements().size() );
    }

    public void testR(){
        @_promote
        @_hashCode
        class K{
            int i;
            boolean b;
            byte by;
            short ss;
            char c;
            float f;
            double d;
            long l;

            byte[] bys;
            String str;
            UUID[] uuids;
        }
        _class _c = _class.of(K.class);
        System.out.println( _c );

        _hashCode.Act.to(_c);
        _method _m = _c.getMethod("hashCode");

        assertEquals(1, $stmt.of("int hash = $any$;").listIn(_m).size() );
        assertEquals(1, $stmt.of("int prime = $any$;").listIn(_m).size() );

        $stmt $st = $stmt.of("hash = hash * prime + $fieldHash$;");

        //List l = $st.findAllIn(_m);
        //assertEquals( 4, l.size());

        //verify that
        List<$stmt.Select> ss = $st.listSelectedIn(_m);
        assertTrue( ss.stream().filter(s-> s.tokens.is("fieldHash", Ex.of("( b ? 1 : 0 )").toString())).findAny().isPresent());
        assertTrue( ss.stream().filter(s-> s.tokens.is("fieldHash", "i") ).findAny().isPresent()); //int
        assertTrue( ss.stream().filter(s-> s.tokens.is("fieldHash", "by") ).findAny().isPresent()); //ibyte
        assertTrue( ss.stream().filter(s-> s.tokens.is("fieldHash", "ss") ).findAny().isPresent()); //short
        assertTrue( ss.stream().filter(s-> s.tokens.is("fieldHash", "c") ).findAny().isPresent()); //char
        assertTrue( ss.stream().filter(s-> s.tokens.is("fieldHash", "Float.floatToIntBits(f)") ).findAny().isPresent()); //float
        assertTrue( ss.stream().filter(s-> s.tokens.is("fieldHash", "(int) (Double.doubleToLongBits(d) ^ (Double.doubleToLongBits(d) >>> 32))")).findAny().isPresent());
        assertTrue( ss.stream().filter(s-> s.tokens.is("fieldHash", "(int) (l ^ (l >>> 32))")).findAny().isPresent());

        assertTrue( ss.stream().filter(s-> s.tokens.is("fieldHash", "java.util.Arrays.hashCode(bys)")).findAny().isPresent());
        assertTrue( ss.stream().filter(s-> s.tokens.is("fieldHash", "java.util.Objects.hashCode(str)")).findAny().isPresent());
        assertTrue( ss.stream().filter(s-> s.tokens.is("fieldHash", "java.util.Arrays.deepHashCode(uuids)")).findAny().isPresent());
    }

    /**
     * Verify that if _2_template a class with _autoEquals
     * AND it is a derived class, it will call super.typesEqual()
     */
    public void testSubClassCallsSuperEqualsAutoEquals(){

        @_hashCode
        class A{
            public int a = 100;
        }

        @_hashCode
        class B extends A{
            public boolean b = true;
        }

        _class _a = _class.of( A.class);
        _class _b = _class.of( B.class);

        //compile the classes
        _runtime _ab = _runtime.of(_a, _b);
        //create (2) new instances
        _proxy _a1 = _ab.proxy(_b);
        _proxy _a2 = _ab.proxy(_b);

        assertTrue( _a1.hashCode() == _a2.hashCode() ); //make sure they are equal

        _a1.set("a", 200); //change the field in the instances base class

        assertFalse( _a1.hashCode() == _a2.hashCode() ); //verify that it is no longer equal
        //i.e. verify the B class calls super.hashCode()
    }
}
