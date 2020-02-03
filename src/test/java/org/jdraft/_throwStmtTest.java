package org.jdraft;

import com.github.javaparser.ast.visitor.TreeVisitor;
import junit.framework.TestCase;

public class _throwStmtTest extends TestCase {
    public void testEmpty(){
        _throwStmt _ts = _throwStmt.of();
        System.out.println(_ts);

        //
        _ts = _throwStmt.of(()-> {throw new RuntimeException("this is the message");});
        System.out.println( _ts );
    }
}
