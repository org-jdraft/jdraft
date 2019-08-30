package test.byexample.proto;


import junit.framework.TestCase;
import org.jdraft.proto.$;
import org.jdraft.proto.$node;
import org.jdraft.proto.$stmt;

public class _3_ProtoCompositionTest extends TestCase {

    public void testCompose(){
        $stmt $ifThenBraces = $stmt.of("if($cond$){ then(); }");
        $stmt $ifThen = $stmt.of("if($cond$) then();");
        $node $ifThenComp = $.of($ifThenBraces, $ifThen );
    }

}
