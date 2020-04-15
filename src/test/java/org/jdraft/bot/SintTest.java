package org.jdraft.bot;

import java.util.List;
import java.util.stream.Collectors;

import org.jdraft.text.Stencil;
import org.jdraft.Expressions;
import org.jdraft._class;
import org.jdraft._int;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;

import junit.framework.TestCase;

public class SintTest extends TestCase {
	
	public void testConstruct() {
		//any
		assertTrue($int.of().matches(1));
		assertTrue($int.of().matches(2));
		
		//int constant
		assertTrue($int.of(1).matches(1));
		assertFalse($int.of(1).matches(2));
		
		//String
		assertTrue($int.of("1").matches(1));
		assertFalse($int.of("1").matches(2));
		
		//_int
		assertTrue($int.of(_int.of("1")).matches(1));
		assertFalse($int.of(_int.of("1")).matches(2));
		
		//IntLiteralExpr
		assertTrue($int.of(Expressions.of(1)).matches(1));
		assertFalse($int.of(Expressions.of(1)).matches(2));
		
		//Predicate
		assertTrue( $int.of().$and(i-> i.isBinaryFormat()).matches("0b101001") );
		assertTrue( $int.of().$and(i-> i.isBinaryFormat()).matches("0b11101") );
		
		assertFalse( $int.of().$and(i-> i.isBinaryFormat()).matches("0xdead") );
	}
	
	public void test$And() {
		//odd numbers
		$int $i = $int.of().$and(i -> (i.getValue() & 1) == 1);
		assertTrue($i.matches(1));
		assertFalse($i.matches(0));
		assertTrue($i.matches(0b1));
		assertFalse($i.matches(0b0));		
		assertTrue($i.matches(-1));
		assertTrue($i.matches(-3));
	}
	
	public void test$Not() {
		//not odd numbers
		$int $i = $int.of().$not(i -> (i.getValue() & 1) == 1);
		assertFalse($i.matches(1));
		assertTrue($i.matches(0));
		assertFalse($i.matches(0b1));
		assertTrue($i.matches(0b0));		
		assertFalse($i.matches(-1));
		assertFalse($i.matches(-3));
	}
	
	public void testSelect(){
		//this is a select any int
		Select<_int> is = new $int().select("1");
		assertNotNull( is );
		
		assertTrue(is.get().is("1"));
		assertTrue(is.get().is(1));
	}
	
	public void testFirstIn() {
		class C{	
			int[] x = {12345, 0xDEAD, 0b10110, 1_000};			
		}				
		assertTrue($int.of().$and(i->i.isBinaryFormat()).firstIn(C.class).is("0b10110"));
		assertTrue($int.of(12345).firstIn(C.class).is(12345));
		assertTrue($int.of().$and(i->i.has_Separators()).firstIn(C.class).is("1_000"));
		assertTrue($int.of().$and(i->i.isHexFormat()).firstIn(C.class).is("0xDEAD"));
		
		//now apply the matchFn on the firstIn instead
		//i.e. refine the query withgout modifying the prototype
		assertTrue($int.of().firstIn(C.class, i->i.isBinaryFormat()).is("0b10110"));
		assertTrue($int.of(12345).firstIn(C.class, i->i.is(12345)).is(12345));
		assertTrue($int.of().firstIn(C.class, i->i.has_Separators()).is("1_000"));
		assertTrue($int.of().firstIn(C.class, i->i.isHexFormat()).is("0xDEAD"));
	}

	public void testListIn() {
		class C{	
			int[] x = {12345, 0xDEAD, 0b10110, 1_000};			
		}
		
		assertEquals( 4, $int.of().listIn(C.class).size());
		assertEquals( 1, $int.of(12345).listIn(C.class).size());
	}
	
	public void testSelectFirstIn() {
	
		class C{	
			int[] x = {12345, 0xDEAD, 0b10110, 1_000};			
		}				
		assertTrue($int.of().$and(i->i.isBinaryFormat()).selectFirstIn(C.class).selection.is("0b10110"));
		assertTrue($int.of(12345).selectFirstIn(C.class).selection.is(12345));
		assertTrue($int.of().$and(i->i.has_Separators()).selectFirstIn(C.class).selection.is("1_000"));
		assertTrue($int.of().$and(i->i.isHexFormat()).selectFirstIn(C.class).selection.is("0xDEAD"));
		
		//now apply the matchFn on the firstIn instead
		//i.e. refine the query withgout modifying the prototype
		assertTrue($int.of().selectFirstIn(C.class, i->i.selection.isBinaryFormat()).selection.is("0b10110"));
		assertTrue($int.of(12345).selectFirstIn(C.class, i->i.selection.is(12345)).selection.is(12345));
		assertTrue($int.of().selectFirstIn(C.class, i->i.selection.has_Separators()).selection.is("1_000"));
		assertTrue($int.of().selectFirstIn(C.class, i->i.selection.isHexFormat()).selection.is("0xDEAD"));
	}
	
	public void testForEachIn() {
		class C{
			int[] x = {12345, 0xDEAD, 0b10110, 1_000, 1};
			int j = 1;
			int b = 0b1;
			int h = 0X1;			
		}
		//find each int value that equals 1
		_class _c = $int.of(1).forEachIn(C.class, i-> i.setValue(i.getValue()+1));
		
		assertTrue( $int.of(2).countIn(_c)==2 );
		//System.out.println( _c );
	}
	
	public void testForSelectedIn() {
		class C{
			int[] x = {12345, 0xDEAD, 0b10110, 1_000, 1};
			int j = 1;
			int b = 0b1;
			int h = 0X1;			
		}
		//find each int value that equals 1
		_class _c = $int.of().forSelectedIn(C.class, s-> s.selection.setValue(s.selection.getValue()+1));
		
		//verify there are now (4) 
		assertTrue( $int.of(2).countIn(_c)==4 );
		//System.out.println( _c );
	}
	
	public void testRemoveIn() {
		class C{
			int[] x = {12345, 0xDEAD, 0b10110, 1_000, 1};
			int j = 1;
			int b = 0b1;
			int h = 0X1;			
		}
		//remove the int values that are equal to 1 {1, 1, 0b1, 0X1}
		_class _c = $int.of(1).removeIn(C.class);		

		//verify there are (4) int literals left {1234, 0xDEAD, 0b10110, 1_000}
		assertEquals(6,  $int.of().countIn(_c));
	}
	
	public void testReplaceIn() {
		class C{
			int[] x = {12345, 0xDEAD, 0b10110, 1_000, 1};
			int j = 1;
			int b = 0b1;
			int h = 0X1;	
			Object oo = 3;
		}
	
		_class _c = $int.of("1").replaceIn(C.class, new IntegerLiteralExpr(2) );
		//System.out.println( _c );
		//System.out.println( $int.of(2).listIn(_c) );
		assertTrue(  $int.of(2).countIn(_c) == 2 );
		
		//here I want to change the int literal oo value (3) 
		//to be a DIFFERENT node type (StringLiteral) with value "Hello"
		_c = $int.of("3").replaceIn(C.class, Expressions.stringLiteralEx("Hello") );
		
		assertTrue( _c.getField("oo").isInit("Hello"));		
	}
	
	public void testReplaceSelectedIn() {
		class C{
			int[] x = {12345, 0xDEAD, 0b10110, 1_000, 1};
			int j = 1;
			int b = 0b1;
			int h = 0X1;			
		}
		
		//add 1 to the (2) int literal values equal to 1 {1, 1}
		_class _c = $int.of(1).replaceSelectedIn(C.class, s-> new IntegerLiteralExpr( s.selection.getValue()+1 ) );
		
		//verify there are (4) int literals NOW equal to value 2 
		assertEquals(2,  $int.of(2).countIn(_c));
		
		//Template<_int> an = $int.of(3);
		
		//change EVERY int literal to 12
		_c = $int.of(Stencil.of("$val$"))
				.replaceSelectedIn(C.class, $int.of(12));
		
		assertEquals( 8, $int.of(12).countIn(_c));
		//System.out.println( _c);
	}
	
	public void testStreamIn() {
		class C{
			int[] x = {12345, 0xDEAD, 0b10110, 1_000, 1};
			int j = 1;
			int b = 0b1;
			int h = 0X1;			
		}
		
		assertEquals(8, $int.of().streamIn(C.class).count());
		
		//skip/ limit
		assertEquals(3, $int.of().streamIn(C.class).skip(5).count());
		assertEquals(2, $int.of().streamIn(C.class).limit(2).count());
		
		//distinct
		//there are (7) distinct int values (1) occurs more than once
		assertEquals(7, $int.of().streamIn(C.class).distinct().count());
		
		//map & map/reduce
		//collect the values as Integers
		List<Integer> lis = 
				$int.of().streamIn(C.class).map(i-> i.getValue()).collect(Collectors.toList());
		
		//System.out.println( $int.of().streamIn(C.class).map(i->i.getValue()).reduce( (a,b)-> a + b ) );
		
		//use map-reduce for all values
		assertEquals(new Integer(70376), 
				$int.of().streamIn(C.class).map(i->i.getValue()).reduce((a,b)->a+b).get());
		
		//max with custom comparator
		_int _max = 
				$int.of().streamIn(C.class).max((a,b)-> Integer.compare(a.getValue(), b.getValue())).get();
		
		assertEquals( 0xDEAD, _max.getValue());
		
		//map max
		assertEquals( new Integer(0xDEAD), 
				$int.of().streamIn(C.class).map(i->i.getValue()).max((a,b)-> Integer.compare(a, b)).get());
		
		//map min
		assertEquals( new Integer(1), 
				$int.of().streamIn(C.class).map(i->i.getValue()).min((a,b)-> Integer.compare(a, b)).get());		
	}		
	
	public void testPrintIn() {
		class C{
			int[] x = {12345, 0xDEAD, 0b10110, 1_000, 1};
			int j = 1;
			int b = 0b1;
			int h = 0X1;			
		}
		
		$int.of().printIn(C.class);
	}
	
	public void testDescribeIn() {
		class C{
			int[] x = {12345, 0xDEAD, 0b10110, 1_000, 1};
			int j = 1;
			int b = 0b1;
			int h = 0X1;			
		}
		
		$int.of().printEachTreeIn(C.class);
	}
}
