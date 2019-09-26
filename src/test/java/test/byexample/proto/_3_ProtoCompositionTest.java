package test.byexample.proto;


import junit.framework.TestCase;
import org.jdraft.pattern.$;
import org.jdraft.pattern.$node;
import org.jdraft.pattern.$stmt;

public class _3_ProtoCompositionTest extends TestCase {

    public void testCompose(){
        $stmt $ifThenBraces = $stmt.of("if($cond$){ then(); }");
        $stmt $ifThen = $stmt.of("if($cond$) then();");
        $node $ifThenComp = $.of($ifThenBraces, $ifThen );
    }

}
