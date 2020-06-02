package org.jdraft;

import junit.framework.TestCase;

import static org.junit.Assert.*;

public class _constructorCallStmtTest extends TestCase {

    public void testC(){
        _constructorCallStmt _ccs = _constructorCallStmt.of("this(1,2,3);");
        _ccs.setTypeArgs(_typeArgs.of("<A,B>"));
        System.out.println( _ccs.FEATURES.featureList );
        //_ccs//.setExpression(_classExpr.of("AClass.class"));
        //_ccs.ast().setExpression(_classExpr.of("AClass.class").ast());
        System.out.println( _ccs );
    }

}