/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft.bot;

import com.github.javaparser.ast.expr.ArrayAccessExpr;
import org.jdraft.Ast;
import org.jdraft.Ex;
import org.jdraft._arrayAccess;
import org.jdraft._int;

import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SarrayAccessTest extends TestCase {

	public void testAny(){
		$arrayAccess $aa = $arrayAccess.of();
		assertTrue( $aa.isMatchAny());

		assertEquals( 2, $aa.$listBots().size() );
		assertTrue($aa.matches("a[0]"));
		assertTrue($aa.matches("methodCall()[0]"));
		assertTrue($aa.matches("methodCall()[0][r]"));
		assertTrue($aa.matches("methodCall()[0][call()]"));
	}

	public void testSelect() {
		$arrayAccess $aa = $arrayAccess.of();
		assertTrue($aa.select("a[1]") != null);
		assertTrue($aa.select("b[m()]") != null);
		System.out.println( $aa.name );
		System.out.println( $aa.index );
		System.out.println( $aa.toString() );
	}

	public void testPredicateAPI() {
		//only accept int literal predicates
		$arrayAccess $aa = $arrayAccess.of(a -> a.isIndex(i -> i instanceof _int));
		assertTrue( $aa.matches("x[1]") );
		assertTrue( $aa.matches("y[1123]") );
		assertFalse( $aa.matches("x[m()]") );
		
		$aa.select("x[1]");
		
	}

	public void testRecursive(){
		_arrayAccess _aa = _arrayAccess.of("i[1][3]");
		Ast.describe(_aa.ast());
		assertTrue(_aa.isNamed("i[1]"));
		assertTrue(_aa.isIndex("3"));
	}

	//the base API for creating $and, $not, $, and draft
	public void testBaseAPI() {
		$arrayAccess $aa = $arrayAccess.of();
		_arrayAccess _aa = _arrayAccess.of("X[i]");
		_aa = $aa.instance( Ex.arrayAccessEx("Y[m()]"));
		
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
		$aa = $arrayAccess.of("x[1]");
		System.out.println( $aa );
		assertTrue( $aa.name.matches("x") );
		assertTrue( $aa.index.matches("1") );

		assertTrue( $aa.matches("x[1]") );
		//assertEquals("x", $aa.name.draft() );
		assertEquals(_int.of("1"), $aa.index.draft() );


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
		
		assertEquals( _arrayAccess.of("x[4]"), $aa.draft("idx", "4"));
	}

	/**
	 * These features are 
	 */
	public void testUniqueAPI() {
		$arrayAccess $aa = $arrayAccess.of();
		$aa.$name("x").$index(_int.of(1));

		assertTrue($aa.name.matches("x"));
		assertTrue($aa.index.matches("1"));

		ArrayAccessExpr aae = Ex.arrayAccessEx("x[1]");
		assertTrue( $aa.matches(aae));
		assertTrue( $aa.matches("x[1]"));
		assertFalse( $aa.matches("y[1]"));
		assertFalse( $aa.matches("x[2]"));

		
	}
	
    public void testA(){
        $arrayAccess $aa = $arrayAccess.of();
        assertTrue( $aa.matches("v[0]") );
        assertTrue( $aa.matches("v[0][1]") );
        assertTrue( $aa.matches("v[0][3>>>4]") );
        assertTrue( $aa.matches("x[m()]") );
        assertTrue( $aa.matches("m()[4]") );
        assertTrue( $aa.matches("m()[y>>3]") );
        
        $aa = $arrayAccess.of("x[1]");
        assertTrue( $aa.matches("x[1]"));
		assertFalse( $aa.matches("x[1][0]"));
        assertFalse( $aa.matches("x[2]"));
    }

    
    
    
}
