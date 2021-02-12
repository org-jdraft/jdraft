package org.jdraft;

import junit.framework.TestCase;

public class _labeledStmtTest extends TestCase {

    public void testMultiLabel(){

        @Deprecated final class I{
            void m() {
                l1: l2: l3: System.out.println(1);
            }
        }
        //a statement with 3 labels is considered (3) labeled statements
        assertEquals( 3, _class.of(I.class).walk(_labeledStmt.class).count());
    }

    public void testEqualsHashcode(){
        label: { int i,j = 0; }

        _labeledStmt _ls = _labeledStmt.of( "label:{ int i,j; }");
        assertTrue(_ls.equals( _labeledStmt.of("label:{ int i, j; }")));
        assertTrue(_ls.equals( _labeledStmt.of("label:{ int j, i; }")));

        assertEquals( _labeledStmt.of( "label:{ int i,j; }").hashCode(), _labeledStmt.of( "label:{ int j,i; }").hashCode());
    }

    public void testFromScratch(){
        _labeledStmt _ls = _labeledStmt.of();
        System.out.println( _ls );

        _ls.setLabel("Heyo");
    }
}