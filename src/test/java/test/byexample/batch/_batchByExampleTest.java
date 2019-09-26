package test.byexample.batch;

import junit.framework.TestCase;
import org.jdraft.io._batch;
import org.jdraft.proto.$;
import org.jdraft.proto.$id;
import org.jdraft.proto.$name;

public class _batchByExampleTest extends TestCase {

    class C1{
        int a;
        public void getX(){
            switch(a){
                case 1 : System.out.println(1);
            }
        }
    }
    class C2{}

    public void testB(){


        //print all public and non static methods with name pattern get??? & containing a switch Statement
        // that occur in these two classes
        $.method($.PUBLIC,$.NOT_STATIC, $name.of("get$Name$")).$hasDescendant($.switchStmt())
                .forEachIn( new Class[]{C1.class, C2.class}, m->System.out.println(m) );
    }

    public void testBatchLinkedWith(){
        //_code._hasCode()
        _batch.of("C:\\temp");
    }
}
