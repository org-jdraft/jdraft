package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft._anno;
import org.jdraft._annoEntryPair;
import org.jdraft.text.Tokens;

import java.util.ArrayList;
import java.util.List;

public class $botListSelectTest extends TestCase {

    public void testSingleMemberAnno(){
        Select.$botSetSelect $bls = new Select.$botSetSelect(_anno.class, _annoEntryPair.class, "memberValues", _ae-> ((_anno)_ae).listEntryPairs());

        $annoEntryPair $mv = $annoEntryPair.of(_annoEntryPair.of("1"));
        List<$annoEntryPair> $mvs = new ArrayList<>();
        $mvs.add($mv);
        $bls.setBotList($mvs);

        Tokens ts = $bls.apply(_anno.of("@A(1)"));
        System.out.println(ts );

        assertNotNull( $bls.apply(_anno.of("@A(1)")));
        assertNotNull( $bls.apply(_anno.of("@A(value=1)")));
        assertNull( $bls.apply(_anno.of("@A(2)")));
        assertNull( $bls.apply(_anno.of("@A(value=2)")));
    }

    public void testNormalAnno(){
        Select.$botSetSelect $bls = new Select.$botSetSelect(_anno.class, _annoEntryPair.class, "memberValues", _ae-> ((_anno)_ae).listEntryPairs());

        $annoEntryPair $mv = $annoEntryPair.of(_annoEntryPair.of("value=1"));
        List<$annoEntryPair> $mvs = new ArrayList<>();
        $mvs.add($mv);
        $bls.setBotList($mvs);

        assertNotNull( $bls.apply(_anno.of("@A(1)")));
        assertNotNull( $bls.apply(_anno.of("@A(value=1)")));
        assertNull( $bls.apply(_anno.of("@A(2)")));
        assertNull( $bls.apply(_anno.of("@A(value=2)")));
    }

    public void testOrder(){
        Select.$botSetSelect $bls = new Select.$botSetSelect(_anno.class, _annoEntryPair.class, "memberValues", _ae-> ((_anno)_ae).listEntryPairs());

        $annoEntryPair $mv1 = $annoEntryPair.of(_annoEntryPair.of("key=1"));
        $annoEntryPair $mv2 = $annoEntryPair.of(_annoEntryPair.of("val=2"));
        List<$annoEntryPair> $mvs = new ArrayList<>();
        $mvs.add($mv1);
        $mvs.add($mv2);
        $bls.setBotList($mvs);

        assertNotNull( $bls.apply(_anno.of("@A(key=1,val=2)")));
        assertNotNull( $bls.apply(_anno.of("@B(key=1,val=2)")));
        assertNotNull( $bls.apply(_anno.of("@DoesntMatter(val=2, key=1)")));
    }

    public void testMatchAll(){
        Select.$botSetSelect $bls = new Select.$botSetSelect(_anno.class, _annoEntryPair.class, "memberValues", _ae-> ((_anno)_ae).listEntryPairs());

        $bls.setMatchAll(true);

        assertNotNull( $bls.apply(_anno.of("@A")));
        assertNotNull( $bls.apply(_anno.of("@A")).get("memberValues"));
        System.out.println( $bls.apply(_anno.of("@A")) );
        System.out.println( $bls.apply(_anno.of("@A(1)")) );
        System.out.println( $bls.apply(_anno.of("@A(key=1,val=2)")) );
        assertNotNull( $bls.apply(_anno.of("@A(1)")).get("memberValues"));
        assertNotNull( $bls.apply(_anno.of("@A(value=1)")));
        assertNotNull( $bls.apply(_anno.of("@A(2)")));
        assertNotNull( $bls.apply(_anno.of("@A(value=2)")));
        assertNotNull( $bls.apply(_anno.of("@A(key=1,val=2)")));
    }
}
