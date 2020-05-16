/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft.bot;

import com.github.javaparser.ast.expr.ArrayAccessExpr;
import org.jdraft.*;

import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class $arrayAccessExprTest extends TestCase {

	public void testOr(){
		$arrayAccessExpr $aaor = $arrayAccessExpr.or(
				$arrayAccessExpr.of().$index(_intExpr.class),
				$arrayAccessExpr.of().$index(_methodCallExpr.class)
		);

		class FF{
			int i[] = {1,2,3,4,5};

			void m(){
				i[0] = 2;
				i[getInt()] = 5;
			}

			int getInt(){
				return 3;
			}
		}
		assertEquals( 2, $aaor.countIn(FF.class));
		//test predicate applied to or
		assertEquals( 1, $aaor.$and(a->a.getIndex() instanceof _intExpr).countIn(FF.class) );
	}

	public void testAny(){
		$arrayAccessExpr $aa = $arrayAccessExpr.of();
		assertTrue( $aa.isMatchAny());

		assertEquals( 2, $aa.$listBots().size() );
		assertTrue($aa.matches("a[0]"));
		assertTrue($aa.matches("methodCall()[0]"));
		assertTrue($aa.matches("methodCall()[0][r]"));
		assertTrue($aa.matches("methodCall()[0][call()]"));
	}

	public void testSelect() {
		$arrayAccessExpr $aa = $arrayAccessExpr.of();
		assertTrue($aa.select("a[1]") != null);
		assertTrue($aa.select("b[m()]") != null);
		System.out.println( $aa.name );
		System.out.println( $aa.index );
		System.out.println( $aa.toString() );
	}

	public void testPredicateAPI() {
		//only accept int literal predicates
		$arrayAccessExpr $aa = $arrayAccessExpr.of(). $and(a -> a.isIndex(i -> i instanceof _intExpr));
		assertTrue( $aa.matches("x[1]") );
		assertTrue( $aa.matches("y[1123]") );
		assertFalse( $aa.matches("x[m()]") );
		
		$aa.select("x[1]");
		
	}

	public void testRecursive(){
		_arrayAccessExpr _aa = _arrayAccessExpr.of("i[1][3]");
		Print.tree(_aa.ast());
		assertTrue(_aa.isNamed("i[1]"));
		assertTrue(_aa.isIndex("3"));
	}

	//the base API for creating $and, $not, $, and draft
	public void testBaseAPI() {
		$arrayAccessExpr $aa = $arrayAccessExpr.of();
		_arrayAccessExpr _aa = _arrayAccessExpr.of("X[i]");
		_aa = $aa.instance( Exprs.arrayAccessEx("Y[m()]"));
		
		//$and
		$aa.$and(a-> a.getName().is("x"));		
		assertTrue( $aa.matches("x[3]") );		
		assertFalse( $aa.matches("y[3]") );
		
		//$not
		$aa.$not(a-> a.getIndex().is("3"));
		assertTrue( $aa.matches("x[1]") );
		assertTrue( $aa.matches("x[2]") );
		assertFalse( $aa.matches("x[3]") );		
		

		//$
		$aa = $arrayAccessExpr.of("x[1]");
		System.out.println( $aa );
		assertTrue( $aa.name.matches("x") );
		assertTrue( $aa.index.matches("1") );

		assertTrue( $aa.matches("x[1]") );
		//assertEquals("x", $aa.name.draft() );
		assertEquals(_intExpr.of("1"), $aa.index.draft() );


		//$aa.$("1", "idx");
		$aa.$index("$idx$");

		assertTrue( $aa.matches("x[1]") );
		assertTrue( $aa.matches("x[2]") );
		assertTrue($aa.index.matches("m()"));
		assertTrue( $aa.matches("x[m()]") );

		assertTrue( $aa.matches("x[100>>4]") );
		
		
		assertTrue($aa.$list().contains("idx"));
		assertTrue($aa.$listNormalized().contains("idx"));
		
		//draft
		System.out.println("NAME"+ $aa.name);
		System.out.println("INDEX"+ $aa.index);
		
		assertEquals( _arrayAccessExpr.of("x[4]"), $aa.draft("idx", "4"));
	}

	/**
	 * These features are 
	 */
	public void testUniqueAPI() {
		$arrayAccessExpr $aa = $arrayAccessExpr.of();
		$aa.$name("x").$index(_intExpr.of(1));

		assertTrue($aa.name.matches("x"));
		assertTrue($aa.index.matches("1"));

		ArrayAccessExpr aae = Exprs.arrayAccessEx("x[1]");
		assertTrue( $aa.matches(aae));
		assertTrue( $aa.matches("x[1]"));
		assertFalse( $aa.matches("y[1]"));
		assertFalse( $aa.matches("x[2]"));

		
	}
	
    public void testA(){
        $arrayAccessExpr $aa = $arrayAccessExpr.of();
        assertTrue( $aa.matches("v[0]") );
        assertTrue( $aa.matches("v[0][1]") );
        assertTrue( $aa.matches("v[0][3>>>4]") );
        assertTrue( $aa.matches("x[m()]") );
        assertTrue( $aa.matches("m()[4]") );
        assertTrue( $aa.matches("m()[y>>3]") );
        
        $aa = $arrayAccessExpr.of("x[1]");
        assertTrue( $aa.matches("x[1]"));
		assertFalse( $aa.matches("x[1][0]"));
        assertFalse( $aa.matches("x[2]"));
    }

    
    
    
}
