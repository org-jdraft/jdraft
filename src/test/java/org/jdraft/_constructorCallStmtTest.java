package org.jdraft;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import junit.framework.TestCase;

import static org.junit.Assert.*;

public class _constructorCallStmtTest extends TestCase {

    public void testSwapThisSuper(){
        _constructorCallStmt _ccs = _constructorCallStmt.of("this(1,2);");
        assertTrue(_ccs.isThis());
        assertFalse(_ccs.isSuper());
        _ccs.setSuper(true);

        assertFalse(_ccs.isThis());
        assertTrue(_ccs.isSuper());
        assertTrue(_ccs.isArgs("1, 2"));
        assertTrue( _ccs.isArgs(_a -> _a.size() == 2));
    }

    public void testCCS(){
        _constructorCallStmt _ccs = _constructorCallStmt.of();
        _ccs.setExpression(_superExpr.of("super(12)"));
        System.out.println( _ccs);


        //Ast.JAVAPARSER.parseExplicitConstructorInvocationStmt("")
        //SuperExpr se = StaticJavaParser.parseExpression("World.super(100)");
        //System.out.println( se );

        ExplicitConstructorInvocationStmt ec = Ast.constructorCallStmt("super(15);");
        Print.tree( ec );

        ExplicitConstructorInvocationStmt ecs = new ExplicitConstructorInvocationStmt();

        ecs.setExpression(new SuperExpr());
        System.out.println( ecs );
    }

    public void testCC(){
        _constructorCallStmt _ccs = _constructorCallStmt.of();
        assertTrue(_ccs.getArgs().isEmpty());
        assertNull(_ccs.getExpression());
        assertTrue(_ccs.getTypeArgs().isEmpty());
        assertFalse( _ccs.hasTypeArgs());
        assertFalse(_ccs.hasArgs());
        assertFalse( _ccs.hasExpression() );

        _ccs.setExpression(_thisExpr.of("World.this"));
        System.out.println( _ccs );

        /*

        _ccs.setExpression(_nameExpr.of("hey"));
        _ccs.ast().setExpression(Expr.nameExpr("hey"));
        System.out.println( _ccs );
        _ccs.ast().setExpression(Expr.castExpr("(think)o"));
        System.out.println( _ccs );
        */

        assertEquals(0, _ccs.countArgs());
        assertTrue(_ccs.allArgs(t-> t instanceof _expr._literal));
        assertFalse(_ccs.allTypeArgs(t-> t.isGenericType()));

        //manipulate args
        _ccs.addArg(1);
        assertEquals(1, _ccs.countArgs());
        assertEquals(1, _ccs.getArgs().count(_intExpr.class));
        assertEquals(1, _ccs.listArgs().size());
        assertEquals(1, _ccs.listArgs(a-> a instanceof _intExpr).size());
        assertEquals(1, _ccs.listArgs(_expr._literal.class).size());
        assertNotNull(_ccs.getArg(a -> a instanceof _expr._literal));
        assertNotNull(_ccs.getArg(_intExpr.class, i -> i.is(1)));

        //manipulate typeArgs
        assertTrue(_ccs.getTypeArgs().isEmpty());
        assertNull(_ccs.getTypeArg(0));
        assertFalse( _ccs.isUsingDiamondOperator());
        _ccs.setUseDiamondOperator();
        assertTrue( _ccs.getTypeArgs().isEmpty());
        assertTrue( _ccs.isUsingDiamondOperator());
        _ccs.addTypeArgs("T");
        assertFalse( _ccs.getTypeArgs().isEmpty());
        assertFalse( _ccs.isUsingDiamondOperator());
        assertEquals(1, _ccs.getTypeArgs().size());
        _ccs.addTypeArgs("U");
        assertEquals(2, _ccs.getTypeArgs().size());
        assertNotNull(_ccs.getTypeArg(0));
        assertNotNull(_ccs.getTypeArg(1));
        assertNotNull(_ccs.getTypeArg(_t -> _t.is("U")));


        //System.out.println( _ccs.ast().getExpression().get() );
    }
    public void testC(){
        _constructorCallStmt _ccs = _constructorCallStmt.of("this(1,2,3);");
        _ccs.setTypeArgs(_typeArgs.of("<A,B>"));
        System.out.println( _ccs.FEATURES.featureList );
        //_ccs//.setExpression(_classExpr.of("AClass.class"));
        //_ccs.ast().setExpression(_classExpr.of("AClass.class").ast());
        System.out.println( _ccs );
    }

}