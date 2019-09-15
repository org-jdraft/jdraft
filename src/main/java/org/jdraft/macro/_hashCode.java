package org.jdraft.macro;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft.*;
import org.jdraft.proto.$stmt;
import org.jdraft.proto.$method;
import com.github.javaparser.ast.stmt.*;

import java.lang.annotation.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Annotation/Macro to create a hashCode() method based on fields of a {@link _type}
 *
 * Builds a hashCode for all non-static FIELDS on the {@link _class}
 * optionally calls super.hashCode() if the class extends something
 * other than Object.
 *
 * can be applied:
 * <PRE>
 * <OL>
 * <LI> via annotation : using the annotation _macro processor {@link _macro#to(Class, _type)}
 * {@code @_hashCode class A{ int x,y,z; }
 * _class _c = $$.to(A.class);
 * }
 * <LI> via _class constructor argument : pass the {@link _hashCode#$} instance in
 * {@code class A{ int x,y,z; }
 * _class _c = _class.of(A.class, _autoHashCode.$);
 * }
 *
 * <LI> via external call: pass the _class in to the {@link _hashCode.Macro#to(_type)}
 * {@code class A{ int x,y,z; }
 * _class _c = _autoHashCode.Macro.to(_class.of(A.class));
 * }
 * </OL>
 * </PRE>
 *
 * @see _macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _hashCode {

    /** Static Instance */
     Macro $ = new Macro();

     /**
      * method template for hashCode with PARAMETERS:
      * <UL>
      * <LI>$seed$ and $prime$ are prime integers populated into the code as initial values
      * <LI>callSuperHashCode: is a boolean which will write (or hide) code for calling the super class
      * <LI>BODY:{} is a BlockStmt of one or more {@link Statement}s for processing each field for the hashcode
      * </UL>
      */
     $method $HASHCODE = $method.of(
         "public int hashCode( ){",
         "    int hash = $seed$;",
         "    int prime = $prime$;",
         "    $callSuperHashCode: hash = hash * prime + super.hashCode();",
         "    $body:{}",
         "    return hash;",
         "}");

    /** Primes used by seeding and factoring in the hashCode method*/
    int[] PRIMES = {
            3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103,
            107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223,
            227, 229, 233, 239, 241, 251, 257, 263, 269, 271
    };

    /** Filters out which FIELDS need to be part of the hashCode calculation*/
    Predicate<_field> HASH_CODE_FIELD_MATCH_FN = f-> !f.isStatic();

     /**
      * Building the {@link Statement} to make up the BODY of the hashCode
      * for all {@link _field}s depending on the {@link _field}s TYPE
      */
     class _fieldToStatement {
        //private static Integer prime, hash;

         public static $stmt $default = $stmt.of( "hash = hash * prime + java.util.Objects.hashCode($name$);");
         public static $stmt $arrayOfPrimitives = $stmt.of( "hash = hash * prime + java.util.Arrays.hashCode($name$);");
         public static $stmt $arrayOfObject = $stmt.of( "hash = hash * prime + java.util.Arrays.deepHashCode($name$);");

         public static $stmt $boolean = $stmt.of( "hash = hash * prime + ($name$ ? 1 : 0 );" );
         public static $stmt $float = $stmt.of( "hash = hash * prime + Float.floatToIntBits($name$);" );
         public static $stmt $double = $stmt.of(
                 "hash = hash * prime + (int)(Double.doubleToLongBits($name$)^(Double.doubleToLongBits($name$) >>> 32));");
         public static $stmt $long = $stmt.of("hash = hash * prime + (int)($name$ ^ ($name$ >>> 32));");
         public static $stmt $simplePrimitive = $stmt.of("hash = hash * prime + $name$;");

         public static Statement constructStmt(_field _f){
            if( _f.getType().isArray() ){
                if( _f.getType().getElementType().isPrimitiveType()){
                    return $arrayOfPrimitives.draft(_f);
                }
                return $arrayOfObject.draft(_f);
            }
            if( _f.getType().isPrimitive()){
                if( _f.isType(boolean.class)){
                    return $boolean.draft(_f);
                }
                if( _f.isType(double.class)){
                    return $double.draft(_f);
                }
                if( _f.isType(float.class)){
                    return $float.draft(_f);
                }
                if( _f.isType(long.class)){
                    return $long.draft(_f);
                }
                return $simplePrimitive.draft(_f);
            }
            return $default.draft(_f);
        }
    }

    class Macro implements _macro<_type> {
         @Override
         public _type apply(_type _t) {
             return to(_t);
         }

        @Override
        public String toString(){
           return "macro[autoHashCode]"; 
        }
        
         public static <T extends _type> T to(T _t){
            if( _t instanceof _class){
                _class _c = (_class)_t;
                Tokens tokens = new Tokens(); /** tokens for the {@link $HASHCODE} template */

                int prime = PRIMES[Math.abs(_c.getFullName().hashCode()) % PRIMES.length];
                tokens.put("prime",prime);
                tokens.put("seed",PRIMES[Math.abs(prime - _c.listFields(HASH_CODE_FIELD_MATCH_FN).size()) % PRIMES.length]);

                if( _c.hasExtends() && !_c.isExtends(Object.class)){ //if _class extends something other than Object
                    tokens.put("callSuperHashCode", true); /** print the code at "callSuperEquals" in {@link #$HASHCODE} */
                }
                BlockStmt body = new BlockStmt();
                //construct Statements for all FIELDS into the BODY BlockStmt
                _c.forFields(HASH_CODE_FIELD_MATCH_FN, f-> body.addStatement(_fieldToStatement.constructStmt(f)));
                tokens.put("body", body); //the body:{} will be replaced with the code in the BlockStmt
                _c.method($HASHCODE.draft(tokens));
            }
            return _t;
        }
    }

    class Act extends macro<_hashCode, TypeDeclaration> {

        public Act(){
            super(_hashCode.class);
        }

        public Act(_hashCode _h){
            super(_h);
        }

        @Override
        public void expand(TypeDeclaration typeDeclaration) {
            Macro.to(_java.type(typeDeclaration));
        }

        @Override
        public String toString(){
            return "macro[hashCode]";
        }
    }
}

