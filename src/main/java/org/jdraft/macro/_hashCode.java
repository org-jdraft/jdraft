package org.jdraft.macro;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft.*;
//import org.jdraft.pattern.$stmt;
//import org.jdraft.pattern.$method;
import com.github.javaparser.ast.stmt.*;
import org.jdraft.text.Stencil;
import org.jdraft.text.Tokens;

import java.lang.annotation.*;
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
 * <LI> via annotation : using the annotation _macro processor {@link macro#to(Class, _type)}
 * {@code @_hashCode class A{ int x,y,z; }
 * _class _c = _class.of(A.class);
 * }
 * <LI> via external call: pass the _class in to the {@link _hashCode.Act#to(TypeDeclaration)}
 * {@code class A{ int x,y,z; }
 * _class _c = _hashCode.Act.to(_class.of(A.class));
 * }
 * </OL>
 * </PRE>
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _hashCode {

     /**
      * method template for hashCode with PARAMETERS:
      * <UL>
      * <LI>$seed$ and $prime$ are prime integers populated into the code as initial values
      * <LI>callSuperHashCode: is a boolean which will write (or hide) code for calling the super class
      * <LI>BODY:{} is a BlockStmt of one or more {@link Statement}s for processing each field for the hashcode
      * </UL>
      */
     Stencil $HASHCODE = Stencil.of(
         "public int hashCode( ){",
         "    int hash = $seed$;",
         "    int prime = $prime$;",
         "    $callSuperHashCode$",  //"    $callSuperHashCode: hash = hash * prime + super.hashCode();",
         "    $body$",               //"    $body:{}",
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

         public static Stencil $default = Stencil.of( "hash = hash * prime + java.util.Objects.hashCode($name$);\n");
         public static Stencil $arrayOfPrimitives = Stencil.of( "hash = hash * prime + java.util.Arrays.hashCode($name$);\n");
         public static Stencil $arrayOfObject = Stencil.of( "hash = hash * prime + java.util.Arrays.deepHashCode($name$);\n");

         public static Stencil $boolean = Stencil.of( "hash = hash * prime + ($name$ ? 1 : 0 );\n" );
         public static Stencil $float = Stencil.of( "hash = hash * prime + Float.floatToIntBits($name$);\n" );
         public static Stencil $double = Stencil.of(
                 "hash = hash * prime + (int)(Double.doubleToLongBits($name$)^(Double.doubleToLongBits($name$) >>> 32));\n");
         public static Stencil $long = Stencil.of("hash = hash * prime + (int)($name$ ^ ($name$ >>> 32));\n");
         public static Stencil $simplePrimitive = Stencil.of("hash = hash * prime + $name$;\n");

         public static Statement constructStmt(_field _f){
             if( _f.getTypeRef().isArrayType() ){
                 if( _f.getTypeRef().getElementType().isPrimitiveType()){
                     return Stmts.of($arrayOfPrimitives.draft("name", _f.getName(), "type", _f.getTypeRef()));
                 }
                 return Stmts.of($arrayOfObject.draft("name", _f.getName(), "type", _f.getTypeRef()));
             }
             if( _f.getTypeRef().isPrimitive()){
                 if( _f.isTypeRef(boolean.class)){
                     return Stmts.of($boolean.draft("name", _f.getName(), "type", _f.getTypeRef()));
                 }
                 if( _f.isTypeRef(double.class)){
                     return Stmts.of($double.draft("name", _f.getName(), "type", _f.getTypeRef()));
                 }
                 if( _f.isTypeRef(float.class)){
                     return Stmts.of($float.draft("name", _f.getName(), "type", _f.getTypeRef()));
                 }
                 if( _f.isTypeRef(long.class)){
                     return Stmts.of($long.draft("name", _f.getName(), "type", _f.getTypeRef()));
                 }
                 return Stmts.of($simplePrimitive.draft("name", _f.getName(), "type", _f.getTypeRef()));
             }
             return Stmts.of($default.draft("name", _f.getName(), "type", _f.getTypeRef()));
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
            to( typeDeclaration);
         }


         public static <_T extends _type> _T to(_T _t){
              to((TypeDeclaration)_t.ast());
              return _t;
         }

         public static <T extends TypeDeclaration> T to(T t){
            if( t instanceof ClassOrInterfaceDeclaration){
                _class _c = _class.of(t);
                Tokens tokens = new Tokens(); /** tokens for the {@link $HASHCODE} template */

                int prime = PRIMES[Math.abs(_c.getFullName().hashCode()) % PRIMES.length];
                tokens.put("prime",prime);
                tokens.put("seed",PRIMES[Math.abs(prime - _c.listFields(HASH_CODE_FIELD_MATCH_FN).size()) % PRIMES.length]);

                if( _c.hasExtends() && !_c.isExtends(Object.class)){ //if _class extends something other than Object
                    tokens.put("callSuperHashCode",  "hash = hash * prime + super.hashCode();"); /** print the code at "callSuperEquals" in {@link #$HASHCODE} */
                } else{
                    tokens.put("callSuperHashCode", "");
                }
                BlockStmt body = new BlockStmt();
                //construct Statements for all FIELDS into the BODY BlockStmt
                _c.forFields(HASH_CODE_FIELD_MATCH_FN, f-> body.addStatement(_fieldToStatement.constructStmt(f)));
                tokens.put("body", body); //the body:{} will be replaced with the code in the BlockStmt
                _c.addMethod($HASHCODE.draft(tokens));
            }
            return t;
        }

        @Override
        public String toString(){
            return "macro[hashCode]";
        }
    }
}

