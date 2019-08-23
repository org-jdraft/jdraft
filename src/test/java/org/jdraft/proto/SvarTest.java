package org.jdraft.proto;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.ForEachStmt;
import org.jdraft.Ast;
import org.jdraft.Stmt;
import org.jdraft._class;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SvarTest extends TestCase {
    
    public void testLoop(){
        class C{
            int[] array;
            int xx;
            int sum1, sum2;
            
            public void l(){
                for (int item : array){
                    if (xx == 123){
                        sum1 += item; //processing
                    }
                }
                for (int item : array){
                    //processing
                    sum2 += item; //processing
                }
            }
        }
        _class _c = _class.of(C.class);
        $stmt<ForEachStmt> $s = $stmt.forEachStmt("for(int $it$: array){ any(); }")
            .$(Stmt.of("any();"), "body");
        
        //ok this will verify, we can find (2) separate forEach statements
        assertEquals(2, $s.count(_c)); //we dont need it, we're just verifying we can find it
        
        AtomicInteger count = new AtomicInteger(0);
        $s.forSelectedIn(_c, 
            ($stmt.Select<ForEachStmt> s)->{
                String iteratorName = s.get("it").toString();
                String replaceName = iteratorName+"_"+count.incrementAndGet();
                System.out.println( "replaceing "+iteratorName+" with "+ replaceName);
                $node.of(iteratorName).replaceIn(s.astStatement, replaceName);
            });
        
        System.out.println (_c );
    }
    
    public void test$VParam(){
        class F{
            Consumer<Object> po = System.out::println;

            void d(){
                po = (o) ->System.out.println(o);
            }

        }
        System.out.println( $var.of().listIn(F.class) );
        System.out.println( $parameter.of().firstIn(F.class).getType().ast().getClass() );
        System.out.println( $parameter.of().listIn(F.class));
    }
    
    public void testWithInit(){
        assertTrue($var.of("int i").matches("int i"));
        assertTrue($var.of("int i").matches("int i=100"));        
    }
    
    public void testVarOrField(){
        class Holder{
            int x,y,z;
            
            public void m(){
                int u = 100;
                System.out.println(u + 3);
            }
        }
        assertEquals( 4, $var.of().listIn(Holder.class).size() );
        
        //
        assertEquals( 1, $var.of().listIn(Holder.class, (v)-> v.getInitializer().isPresent()).size() );
        assertEquals( 1, $var.of().listSelectedIn(Holder.class, s-> s.hasInit()).size() );

        assertEquals( 4, $var.of("int $name$").listIn(Holder.class).size() );

        assertEquals( 4, $var.of(int.class).listIn(Holder.class).size() );

        assertEquals( 4, $var.of($typeRef.of(int.class)).listIn(Holder.class).size() );
        
        assertEquals( 3, $var.of(int.class).listSelectedIn(Holder.class, s-> s.isFieldVar()).size() );
        assertEquals( 3, $var.member(int.class).listSelectedIn(Holder.class).size() );
        assertEquals( 1, $var.local(int.class).listSelectedIn(Holder.class).size() );

        //assertEquals( 3, $var.member(int.class).listSelectedIn(Holder.class, s-> s.isFieldVar()).size() );
                //.stream().filter( s-> s.isFieldVar() ).collect(Collectors.toList()).size() );
        //assertEquals( 4, $var.se(int.class).forSelected(Holder.class).size() );
        
        //assertNotNull($var.first(Holder.class, "int $name$"));
    }
    
    public void testVar(){
        $var $anyInt = $var.of("int $name$");
        $var $anyString = $var.of("String $name$");
        $var $anyInit = $var.of("$type$ $name$", v-> v.getInitializer().isPresent() );
        
        VariableDeclarator v = Ast.var("int count");
        assertEquals( $anyInt.fill("count"), v );
        
        _class _c = _class.of("C", new Object(){
            int x, y = 0;            
            int z;
            String n;
        });
        
        assertEquals( 3, $anyInt.listIn(_c).size());
        assertEquals( 1, $anyString.listIn(_c).size());
        assertEquals( 1, $anyInit.listIn(_c).size());
        
        //I want to list all var names
        
        List<String> varNames = new ArrayList<>();
        $var.of().forEachIn(_c, vv -> varNames.add(vv.getNameAsString()));
        System.out.println( varNames );
        
        List<String> typeNames = new ArrayList<>();
        $var.of().forEachIn(_c, vv -> typeNames.add(vv.getTypeAsString()));
        System.out.println( typeNames );
    }

}
