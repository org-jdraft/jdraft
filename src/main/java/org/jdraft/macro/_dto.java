package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import org.jdraft.Ex;
import org.jdraft._class;
import org.jdraft._java;
import org.jdraft._type;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.function.Consumer;

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
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _dto {

    /*
    Macro $ = new Macro();

    Act A = new Act();

    class Macro implements _macro<_type> {

        @Override
        public String toString(){
           return "macro[dto]";
        }
        
        @Override
        public _type apply(_type _t) {
            return to(_t);
        }

        public static <T extends _type> T to(T t) {
            t = _get.Macro.to(t);
            t = _setFluent.Macro.to(t);
            t = _equals.Macro.to(t);
            t = _hashCode.Macro.to(t);
            t = _toString.Macro.to(t);
            t = _autoConstructor.Macro.to(t);

            t.removeAnnos(_dto.class);
            return t;
        }


    }
    */

    class Act extends macro<_dto, TypeDeclaration> { //} Consumer<TypeDeclaration> {

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
            ObjectCreationExpr oce = Ex.anonymousObjectEx(ste);
            _class _c = _class.of(signature);
            oce.getAnonymousClassBody().get().forEach(b -> _c.ast().addMember(b));
            Arrays.stream(body.getClass().getInterfaces()).forEach(e -> {
                _c.implement(e);
                _c.imports(e);
            });
            if (body.getClass().getSuperclass() != null) {
                _c.extend(body.getClass().getSuperclass());
                _c.imports(body.getClass().getSuperclass());
            }
            to(_c.astCompilationUnit().getType(0));
            return _c;
        }

        @Override
        public void expand(TypeDeclaration typeDeclaration) {
            to(typeDeclaration);
        }


        public static <T extends TypeDeclaration> T to( T typeDeclaration ){
            _type t = _java.type(typeDeclaration);
            typeDeclaration = _get.Act.to(typeDeclaration);
            typeDeclaration = _setFluent.Act.to(typeDeclaration);
            typeDeclaration = _equals.Act.to(typeDeclaration);
            typeDeclaration = _hashCode.Act.to(typeDeclaration);
            typeDeclaration = _toString.Act.to(typeDeclaration);
            _autoConstructor.Act.to(typeDeclaration);

            t.removeAnnos(_dto.class);
            return typeDeclaration;
        }
        @Override
        public String toString() {
            return "macro[dto]";
        }
    }

}
