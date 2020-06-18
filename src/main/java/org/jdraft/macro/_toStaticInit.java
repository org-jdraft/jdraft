package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import org.jdraft._jdraftException;
import org.jdraft._initBlock;
import org.jdraft._method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Annotation _macro to convert a {@link _method} to a static {@link _initBlock}
 * (removing the
 * _method from its container and adding an {@link _initBlock})
 *
 * useful when the BODY of a {@link org.jdraft._class} or {@link org.jdraft._enum} is defined using
 * an anonymous object and (since anonymous objects cant have init blocks) for example:
 * <PRE>
 *     _class _c = _class.of("aaaa.bbbb.C", new Object(){
 *         public @_final int x = 1002;
 *
 *         @_toStaticInit void si(){
 *             System.out.println("In Static Init"+x);
 *         }
 *     }
 *     // _c will be:
 *     package aaaa.bbbb;
 *     public class C{
 *         public final int x;
 *
 *         static{
 *             System.out.println("In Static Init"+x);
 *         }
 *     }
 * </PRE>
 *
 * @see macro
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface _toStaticInit {

    class Act extends macro<_toStaticInit, MethodDeclaration> {

        public Act(){
            super(_toStaticInit.class);
        }

        public Act( _toStaticInit _tc){
            super(_tc);
        }

        @Override
        public String toString(){
            return "macro[toInitStatic]";
        }

        @Override
        public void expand(MethodDeclaration methodDeclaration) {
            to(methodDeclaration);
        }

        public static InitializerDeclaration fromMethod( MethodDeclaration md ){
            return fromMethod(_method.of(md)).node();
        }

        /**
         * given a _method turn it into an {@link _initBlock}
         */
        public static _initBlock fromMethod( _method _m ){
            _initBlock _ib = _initBlock.of();
            _ib.setStatic();
            _ib.setBody( _m.getBody() );
            if( _m.hasJavadoc() ){
                _ib.node().setJavadocComment(_m.node().getJavadocComment().get());
            }
            return _ib;
        }

        public static InitializerDeclaration to(MethodDeclaration methodDeclaration){
            List<TypeDeclaration>tds = new ArrayList<>();
            //_macro.removeAnnotation(methodDeclaration, _toStaticInit.class);
            _initBlock _ct = fromMethod( _method.of(methodDeclaration));
            Optional<Node> op = methodDeclaration.stream(Node.TreeTraversal.PARENTS).filter(n-> n instanceof EnumDeclaration
                    || (n instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration)n).isInterface())).findFirst();
            //remove the old method, add the constructor
            if( !op.isPresent()){
                throw new _jdraftException("Could not find parent of "+ methodDeclaration+" to replace as initBlock");
            }
            TypeDeclaration td = (TypeDeclaration)op.get();

            td.addMember(_ct.node());
            //remove the old method
            boolean isRemoved = methodDeclaration.remove();
            return _ct.node();
        }
    }
}
