package org.jdraft.macro;

import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import org.jdraft.Ex;
import org.jdraft._class;
import org.jdraft._type;

import java.lang.annotation.*;
import java.util.Arrays;

/**
 * Treats the Object as a DTO (Data Transfer Object) adding:
 * <UL>
 * <LI>setXXX() METHODS for appropriate FIELDS</LI>
 * <LI>getXXX() METHODS for appropriate FIELDS</LI>
 * <LI>a toString() method</LI>
 * <LI>a hashCode() method</LI>
 * <LI>an equals(Object o) method</LI>
 * <LI>a default minimal constructor (initializing non initialized final non static FIELDS)</LI>
 * </UL>
 * @see macro
 * @see _get
 * @see _setFluent
 * @see _equals
 * @see _hashCode
 * @see _toString
 * @see _autoConstructor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _dto {

    class Act extends macro<_dto, TypeDeclaration> {

        public Act() {
            super(_dto.class);
        }

        public Act(_dto _d) {
            super(_d);
        }

        /**
         * Provide a abstract class and signature to return a _class that is a Dto
         *
         * @param signature the signature of the class
         * @param body      the BODY (implemented as an annonymous class that can implement or extend a BaseClass)
         * @return
         */
        public static _class of(String signature, Object body) {
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            ObjectCreationExpr oce = Ex.newEx(ste);
            _class _c = _class.of(signature);
            oce.getAnonymousClassBody().get().forEach(b -> _c.ast().addMember(b));
            Arrays.stream(body.getClass().getInterfaces()).forEach(e -> {
                _c.addImplement(e);
                _c.addImports(e);
            });
            if (body.getClass().getSuperclass() != null) {
                _c.addExtend(body.getClass().getSuperclass());
                _c.addImports(body.getClass().getSuperclass());
            }
            to(_c.astCompilationUnit().getType(0));
            return _c;
        }

        @Override
        public void expand(TypeDeclaration typeDeclaration) {
            to(typeDeclaration);
        }

        public static <_T extends _type> _T to( _T _t){
            to((TypeDeclaration)_t.ast());
            return _t;
        }

        public static <T extends TypeDeclaration> T to( T typeDeclaration ){
            _get.Act.to(typeDeclaration);
            _setFluent.Act.to(typeDeclaration);
            _equals.Act.to(typeDeclaration);
            _hashCode.Act.to(typeDeclaration);
            _toString.Act.to(typeDeclaration);
            _autoConstructor.Act.to(typeDeclaration);
            return typeDeclaration;
        }
        @Override
        public String toString() {
            return "macro[dto]";
        }
    }
}
