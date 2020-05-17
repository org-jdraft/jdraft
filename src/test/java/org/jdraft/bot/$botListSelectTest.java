package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft._annoExpr;
import org.jdraft.text.Tokens;

import java.util.ArrayList;
import java.util.List;

public class $botListSelectTest extends TestCase {

    public void testSingleMemberAnno(){
        Select.$botSetSelect $bls = new Select.$botSetSelect(_annoExpr.class, _annoExpr._pair.class, "memberValues", _ae-> ((_annoExpr)_ae).listPairs());

        $annoExpr.$pair $mv = $annoExpr.$pair.of(_annoExpr._pair.of("1"));
        List<$annoExpr.$pair> $mvs = new ArrayList<>();
        $mvs.add($mv);
        $bls.setBotList($mvs);

        Tokens ts = $bls.apply(_annoExpr.of("@A(1)"));
        System.out.println(ts );

        assertNotNull( $bls.apply(_annoExpr.of("@A(1)")));
        assertNotNull( $bls.apply(_annoExpr.of("@A(value=1)")));
        assertNull( $bls.apply(_annoExpr.of("@A(2)")));
        assertNull( $bls.apply(_annoExpr.of("@A(value=2)")));
    }

    public void testNormalAnno(){
        Select.$botSetSelect $bls = new Select.$botSetSelect(_annoExpr.class, _annoExpr._pair.class, "memberValues", _ae-> ((_annoExpr)_ae).listPairs());

        $annoExpr.$pair $mv = $annoExpr.$pair.of(_annoExpr._pair.of("value=1"));
        List<$annoExpr.$pair> $mvs = new ArrayList<>();
        $mvs.add($mv);
        $bls.setBotList($mvs);

        assertNotNull( $bls.apply(_annoExpr.of("@A(1)")));
        assertNotNull( $bls.apply(_annoExpr.of("@A(value=1)")));
        assertNull( $bls.apply(_annoExpr.of("@A(2)")));
        assertNull( $bls.apply(_annoExpr.of("@A(value=2)")));
    }

    public void testOrder(){
        Select.$botSetSelect $bls = new Select.$botSetSelect(_annoExpr.class, _annoExpr._pair.class, "memberValues", _ae-> ((_annoExpr)_ae).listPairs());

        $annoExpr.$pair $mv1 = $annoExpr.$pair.of(_annoExpr._pair.of("key=1"));
        $annoExpr.$pair $mv2 = $annoExpr.$pair.of(_annoExpr._pair.of("val=2"));
        List<$annoExpr.$pair> $mvs = new ArrayList<>();
        $mvs.add($mv1);
        $mvs.add($mv2);
        $bls.setBotList($mvs);

        assertNotNull( $bls.apply(_annoExpr.of("@A(key=1,val=2)")));
        assertNotNull( $bls.apply(_annoExpr.of("@B(key=1,val=2)")));
        assertNotNull( $bls.apply(_annoExpr.of("@DoesntMatter(val=2, key=1)")));
    }

    public void testMatchAll(){
        Select.$botSetSelect $bls = new Select.$botSetSelect(_annoExpr.class, _annoExpr._pair.class, "memberValues", _ae-> ((_annoExpr)_ae).listPairs());

        $bls.setMatchAll(true);

        assertNotNull( $bls.apply(_annoExpr.of("@A")));
        assertNotNull( $bls.apply(_annoExpr.of("@A")).get("memberValues"));
        System.out.println( $bls.apply(_annoExpr.of("@A")) );
        System.out.println( $bls.apply(_annoExpr.of("@A(1)")) );
        System.out.println( $bls.apply(_annoExpr.of("@A(key=1,val=2)")) );
        assertNotNull( $bls.apply(_annoExpr.of("@A(1)")).get("memberValues"));
        assertNotNull( $bls.apply(_annoExpr.of("@A(value=1)")));
        assertNotNull( $bls.apply(_annoExpr.of("@A(2)")));
        assertNotNull( $bls.apply(_annoExpr.of("@A(value=2)")));
        assertNotNull( $bls.apply(_annoExpr.of("@A(key=1,val=2)")));
    }
}
