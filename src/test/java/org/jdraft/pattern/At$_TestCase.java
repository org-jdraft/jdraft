package org.jdraft.pattern;

import junit.framework.TestCase;
import org.jdraft._annotation;
import org.jdraft.macro._abstract;
import org.jdraft.macro._remove;
import org.jdraft.macro._static;
import org.jdraft.macro._toCtor;

public class At$_TestCase extends TestCase {

    public void testConstructorAnnoParameter(){

        $constructor $ct = $constructor.of( new Object(){
            @_toCtor
            @_$({"x", "pName"}) public void c(int x){
                this.x = x;
            }
            int x;
        });
        assertTrue( $ct.parameters.$params.get(0).matches("int anyName"));
    }

    public void testMethod$_Parameter(){
        $method $m = $method.of( new Object(){
           @_$({"x", "pName"}) public void c(int x){
               this.x = x;
           }
           int x;
        });

        assertTrue( $m.parameters.$params.get(0).matches("int anyName"));
        assertTrue( $m.body.matches("this.fieldName = fieldName;"));
    }

    public void testField$_(){
        $field $f = $field.of(new Object(){
           @_$({"id", "fieldName"}) public @_static final int id = 12345;
        });
        //System.out.println($f);
        assertTrue( $f.matches("public static final int ANYTHING = 12345;"));
    }

    public void testClass$_(){
        $class $c = $class.of( new @_$({"fieldName", "nm", "FieldName", "Nm"}) Object(){
           public int fieldName;

           public int getFieldName(){
                return this.fieldName;
           }

           public void setFieldName( int fieldName){
               this.fieldName = fieldName;
           }
        });
        assertTrue($c.fields.get(0).matches("public int anything;"));
        assertTrue($c.methods.get(0).matches("public int getAnything(){ return this.anything; }"));
        assertTrue($c.methods.get(1).matches("public void setAnything(int anything){ this.anything = anything; }"));
        //System.out.println( $c );
    }

    public void testEnum$_(){

        $enum $e = $enum.of( "E", new Object(){
            public @_static int fieldName = 100;

            public @_static int getFieldName(){
                return this.fieldName;
            }

            public @_static void setFieldName( int fieldName){
                this.fieldName = fieldName;
            }
        }).$("fieldName", "nm").$( "FieldName", "Nm");
        assertTrue($e.fields.get(0).matches("public static int anything= 100;"));
        assertTrue($e.methods.get(0).matches("public static int getAnything(){ return this.anything; }"));
        assertTrue($e.methods.get(1).matches("public static void setAnything(int anything){ this.anything = anything; }"));
        //System.out.println( $c );
    }

    public void testInterface$_(){
        $interface $c = $interface.of( new @_$({"fieldName", "nm", "FieldName", "Nm"}) Object(){
            @_remove int fieldName;

            @_abstract public int getFieldName(){
                return this.fieldName;
            }

            @_abstract public void setFieldName( int fieldName){
                this.fieldName = fieldName;
            }
        });

        System.out.println( $c);
        assertTrue($c.fields.isEmpty());
        assertTrue($c.methods.get(0).matches("public int getAnything();"));
        assertTrue($c.methods.get(0).matches("public abstract int getAnything();"));
        assertTrue($c.methods.get(1).matches("public void setAnything(int anything);"));
        assertTrue($c.methods.get(1).matches("public abstract void setAnything(int anything);"));

        //System.out.println( $c );
    }

    public void testAnnotation$_(){
        _annotation _a = _annotation.of( "ANN", new Object(){
            public int fieldName = 100;
        });

        System.out.println( _a );

        $annotation $c = $annotation.of( new Object(){
            public int fieldName = 100;
        });

        assertTrue($c.annotationElements.get(0).matches("int fieldName() default 100;"));
        //System.out.println( $c );
    }

}
