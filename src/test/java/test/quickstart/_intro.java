package test.quickstart;

import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.io._io;
import org.jdraft.macro.*;
import org.jdraft.runtime.*;

import java.io.Serializable;
import java.util.UUID;

public class _intro extends TestCase {

    public void testIntro(){
        //_class represents the Java source code
        _class _c = null;
        create:{ //build a source code from the
            _c = _class.of("math.geometry.Point", new Object(){
                @_get @_set public int x=0,y=0;

                public String toString(){
                    return "("+ x +","+ y+")";
                }
            });
            //_equals.Act.to(_c);
            //_c = _class.of(Point.class);
        }
        inspect:{ //inspect the class and it's fields / members
            assertTrue( _c.isPublic());
            assertFalse( _c.isImplements(Serializable.class));
            // class
            assertFalse( _c.hasField(f->f.isPrivate()));
            assertEquals(2, _c.listFields(f -> f.isPublic()).size());
            assertEquals(5, _c.listMethods().size());
            assertFalse( _c.hasMethod(m-> m.hasAnno(Deprecated.class)));
            assertTrue( _c.getMethod("toString").isType(String.class));
        }
        query:{ //"walk" the syntax tree (deeply) and
            //the int literal "0" is found (2) times
            assertEquals( 2, _c.walk(_intExpr.class).count(i-> i.is(0)));
            assertEquals(2, _c.walk( _intExpr.of(0) ).count());
            //there are (6) uses of the "int" type
            assertEquals( 6, _c.walk(_typeRef.class).list(t-> t.is(int.class)).size());
            //there are (5) literals ...(2) int "0"'s and (3) Strings literals "(",",", ")"
            _c.walk(_expr._literal.class).print();
            //
            assertEquals( 5, _c.walk(_expr._literal.class).count());
        }
        mutate:{ //CRUD create update delete
            _c.addImports(UUID.class);
            _c.setPackage("math.geometry");
            _c.addImplements(Serializable.class);
            _c.addField("public static final String ID = UUID.randomUUID().toString();");
            _c.addMethod("public int hashCode(){ return 31 * x * y * 31; }");
            _equals.Act.to(_c); //use the equals macro to build an equals method
            //update: set all parameters for all methods final
            _c.toMethods( m-> m.forParams(p-> p.setFinal()));
            //remove
            _c.removeField("serialVersionUid");
        }
        print:{ //write the source code of the _c
            System.out.println(_c);
            //write to an output file in the base directory & return/print the fileName
            System.out.println(_io.out("C:\\temp\\", _c)); //C:\temp\math\geometry\Point.java
        }
        compileAndUse: { //compile, load and use the class from the _class source
            //compile and load the source code in _c in a new runtime ClassLoader
            _runtime _r = _runtime.of(_c);

            //create a "proxied" instance & call some methods on it
            _proxy _p = _r.proxy(_c).set("x", 100).set("y", 200);

            Object instance = _p.instance; //retrieve the instance from the _proxy

            assertEquals(100 * 31 * 200 * 31, instance.hashCode());
        }
    }

    public void testSW(){
        class withNums{
            int i=-1;
            public int hashCode(){
                return 31 * i;
            }
        }
        _class _c = _class.of(withNums.class);
        _c.walk(_intExpr.class).forEach(i-> i.setValue(i.getValue()+1));
        System.out.println( _c );
    }

    public void testJavaPoetMain(){
        _class _c = _class.of("com.example.helloworld.HelloWorld").setFinal()
                .main((String[] s)->System.out.println("Hello JDraft!"));
        System.out.println( _c );
    }

    public void testJavaPoetControlFlow(){
        _method _cf = _method.of( new Object(){
            void main(){
                int total = 0;
                for (int i = 0; i < 10; i++) {
                    total += i;
                }
            }
        });
        System.out.println( _cf );
    }

    public void testJavaPoetComputeRange(){
        _method _cr = _method.of( new Object(){
            int multiply10to20() {
                int result = 1;
                for (int i = 10; i < 20; i++) {
                    result = result * i;
                }
                return result;
            }
        });
        System.out.println( _cr);
    }

    public void testJavaPoetTime(){
        _method _tm = _method.of( new Object(){
            void main() {
                long now = System.currentTimeMillis();
                if (System.currentTimeMillis() < now)  {
                    System.out.println("Time travelling, woo hoo!");
                } else if (System.currentTimeMillis() == now) {
                    System.out.println("Time stood still!");
                } else {
                    System.out.println("Ok, time still moving forward");
                }
            }
        });
        System.out.println(_tm);
    }

    public void testJavaPoetExceptionHandling(){
        _method _eh = _method.of( new Object(){
            void main() {
                try {
                    throw new Exception("Failed");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        System.out.println(_eh);
    }

    /**
     * <A HREF="https://github.com/square/javapoet#l-for-literals"></A>
     */
    public void testUpdateCode(){
        computeRange("methodName", 0, 100, "*" );
        computeRange("methodName", -20, 20, "/" );
        computeRange2("methodName", 0, 100, "*" );
    }

    static _method _CR = _method.of( new Object(){
        int $name$( ){
            int result = 0;
            for(int i=$from$;i<$to$;i++){
                result = result % i;
            }
            return result;
        }
        int $from$, $to$;
    });

    public static _method computeRange( String name, int from, int to, String op){
        _method _computeRange = _CR.draft("$name$", name, "$from$", from, "$to$", to, "%", op);

        System.out.println( _computeRange );
        return _computeRange;
    }

    public static _method computeRange2( String name, int from, int to, String op){
        _method _computeRange = _method.of( new Object(){
            int $name$( ){
                int result = 0;
                for(int i=$from$;i<$to$;i++){
                    result = result % i;
                }
                return result;
            }
            int $from$, $to$;
        });

        //_computeRange.getBody().first(_forStmt.class).
        _computeRange.setName(name).getBody().first(_forStmt.class)
                .setInit("int i="+from)
                .setCompare("i < "+to);
        _computeRange.walk(_binaryExpr.class).first().setOperator(op);

        System.out.println( _computeRange );
        return _computeRange;
    }

    public void testDraftReplace(){
        _binaryExpr _e = _binaryExpr.of("i < 2");
        //_binaryExpr _parsed = _binaryExpr.PARSER.apply("3<2");

        //System.out.println( _e.draftReplace("i", "3"));
        _e = _e.draftReplace("i", "3");
        assertTrue( _e.is("3 < 2") );

        class F{
            int f = 0;
            void m(){
                int y = 0;
            }
        }
        _class _c = _class.of(F.class);
        System.out.println( _c.draft("0", "1") );
        System.out.println( _c.draft("0", "1").draft("1", "2") );

        _c.toMembers( m -> m.walk(_intExpr.class).forEach( e-> ((_expr)e).draftReplace("0", "1")));
        System.out.println( _c );
        _c.toMembers( m -> m.walk(_intExpr.class).forEach( e-> ((_expr)e).draftReplace("1", "2")));

        System.out.println( _c );
        _c = _c.draftReplace("2","3");
        System.out.println( _c );
        //_c.toMembers(_method.class, _m-> Print.tree(_m) );

        //_c.toMembers(_method.class, _m-> _m.draftReplace("1", "2"));
        //System.out.println( _c );
    }

    public void testDraftReplaceClass() {
        class $className$ {
            public $className$() {
            }

            public $className$(int i) {
            }
        }
        _class _c = _class.of($className$.class).draftReplace("$className$", "MyClass");
        //produces
        System.out.println( _c);
    }

}
