package org.jdraft.pattern;

import junit.framework.TestCase;

public class SnotTest extends TestCase {

    public void testNot(){
        assertTrue( $annotation.not( $.name("A$suf$") ).matches("@interface FF{ }"));
        assertTrue( $annotationEntry.not($name.of("B")).matches("int value();"));
        assertTrue( $class.not($.PRIVATE).matches( "public class C{}") );
        assertTrue( $constructor.not($.PRIVATE).matches("public A(){}") );
        assertTrue( $enum.not($annoRef.of(Deprecated.class)).matches( "public enum E{}") );
        assertTrue( $enumConstant.not($annoRef.of(Deprecated.class)).matches( "E()") );
        assertTrue( $field.not($.TRANSIENT).matches("public int i;"));
        assertTrue( $interface.not($typeParameters.of(t-> t.isEmpty())).matches("interface I<T>{}") );
        assertTrue( $method.not( $.FINAL).matches("public void v(){}") );
        assertTrue( $parameter.not( $typeRef.of(int.class) ).matches("String i") );
        assertTrue( $typeParameter.not($typeRef.of(String.class)).matches("T") );
        assertTrue( $var.not($typeRef.of(int.class)).matches("float f = 1.0f;"));

    }
}
