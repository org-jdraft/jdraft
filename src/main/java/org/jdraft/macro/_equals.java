package org.jdraft.macro;

import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import org.jdraft.pattern.$method;
import org.jdraft.pattern.$stmt;
import org.jdraft.text.Tokens;

import java.lang.annotation.* ;
import java.util.List;
import java.util.function.Predicate;

/**
 * Annotation/Macro for synthesizing an equals() based on the _fields of a _type
 *
 * Builds a an typesEqual(Object){...} method for all non_static FIELDS on the _type
 * Works on {@link _class} and {@link org.jdraft._enum} {@link _type}s
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _equals {

    /** Select which FIELDS are being checked for the typesEqual method */
    Predicate<_field> FIELDS_FOR_EQUALS = f -> !f.isStatic();

    /** template statements for typesEqual based on the field TYPE */
    $stmt $float = $stmt.of("eq = eq && Float.compare(this.$name$,test.$name$) == 0;");
    $stmt $double = $stmt.of("eq = eq && Double.compare(this.$name$,test.$name$) == 0;");
    $stmt $primitive = $stmt.of("eq = eq && this.$name$ == test.$name$;");
    $stmt $arrayOfPrimitives = $stmt.of("eq = eq && java.util.Arrays.equals(this.$name$,test.$name$);");
    $stmt $arrayOfObject = $stmt.of("eq = eq && java.util.Arrays.deepEquals(this.$name$,test.$name$);");
    $stmt $default = $stmt.of("eq = eq && java.util.Objects.equals(this.$name$,test.$name$);");

    /** NOTE: we made this a String (not a lambda, etc.) to improve startup perf */
    $method $equals = $method.of(
            "public boolean equals(Object o){",
            "if(o == null) {",
            "   return false;",
            "}",
            "if(this == o) {",
            "    return true;",
            "}",
            "if(getClass() != o.getClass()){",
            "    return false;",
            "}",
            "$className$ test = ($className$)o;",
            "boolean eq = true;",
            "$callSuperEquals: {eq = super.equals( test );}",
            "$BODY:{}",
            "return eq;",
            "}");

    class Act extends macro<_equals, TypeDeclaration> {

        public Act(){
            super(_equals.class);
        }

        public Act(_equals _e){
            super(_e);
        }

        @Override
        public void expand(TypeDeclaration typeDeclaration) {
            to( (_type)_java.type(typeDeclaration));
        }

        @Override
        public String toString(){
            return "macro[equals]";
        }

        public static <T extends TypeDeclaration> T to (T t){
            to( (_type)_java.type(t));
            return t;
        }

        public static <_T extends _type> _T to( _T _t ){
            if( _t instanceof _class ) {
                _class _c = (_class)_t;

                Tokens ts = new Tokens();
                ts.put("className", _c.getName());
                if( _c.hasExtends() ){
                    //ts.put("$callSuperEquals", true);
                    ts.put("callSuperEquals", true);
                }
                //_1_build the BODY of statements for checking the FIELDS
                // then update the $typesEqual method template with the code
                // by populating the $BODY{}
                BlockStmt body = new BlockStmt();

                List<_field> _fs = _t.listFields(FIELDS_FOR_EQUALS);

                _fs.forEach(f-> {
                    if( f.isTypeRef(float.class) ){
                        body.addStatement( $float.fill(f.getName()).ast());
                    }else if( f.isTypeRef(double.class)){
                        body.addStatement($double.fill(f.getName()).ast());
                    }else if( f.isPrimitive() ){
                        body.addStatement($primitive.fill(f.getName()).ast());
                    }else{
                        if( f.isArray()){
                            if( f.getTypeRef().getElementType().isPrimitiveType() ){
                                body.addStatement($arrayOfPrimitives.fill(f.getName()).ast());
                            } else {
                                body.addStatement($arrayOfObject.fill(f.getName()).ast());
                            }
                        }else {
                            body.addStatement($default.fill(f.getName()).ast());
                        }
                    }
                });
                ts.put("BODY", body);
                _c.addMethod( $equals.draft(ts) );
            }
            return _t;
        }
    }
}

