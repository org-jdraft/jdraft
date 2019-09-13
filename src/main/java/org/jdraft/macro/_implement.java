package org.jdraft.macro;

import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithImplements;
import org.jdraft._type;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Annotation/Macro to add implements and imports to a {@link _type}
 * can be accessed by:
 * <UL>
 *     <LI>{@link _macro#to(Class, _type)} via the @_implement annotation
 *     <LI>creating a new instance and calling the {@link _macro#to(Class, _type)}  method
 *         _t = new _implement.Macro(Serializable.class).expand( _t );
 *     <LI>calling the _implement.Macro.to(_t, Serializable.class);
 * </UL>
 *
 * @see _macro
 */
@Retention( RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _implement {

    Class[] value();

    class Macro implements _macro<_type> {

        Class[] toImplement;

        @Override
        public String toString(){
            String s = "";
            for(int i=0;i<toImplement.length;i++){
                if( i > 0){
                    s +=",";
                }
                s += toImplement[i].getCanonicalName();
            }
            return "macro[toImplement("+s+")]"; 
        }
        
        public Macro( _implement _i ){
            /** Transfer the properties on the _implement annotation to a field in the Macro instance*/
            this.toImplement = _i.value();
        }

        /** Here we construct a Macro with the properties (no annotation) */
        public Macro( Class...toImplement ){
            this.toImplement = toImplement;
        }

        public _type apply( _type  _t){
            return to( _t, toImplement );
        }

        public static <T extends _type> T to( T _t, Class...toImplement){
            if( _t instanceof _type._hasImplements) {
                ((_type._hasImplements)_t).implement(toImplement);
            }
            return _t;
        }
    }

    class Act extends macro<_implement, TypeDeclaration>{

        Class[] toImplement;

        public Act( _implement _i ){
            super(_i);
            this.toImplement = _i.value();
        }

        public Act( Class[] toImplement ){
            super(_implement.class);
            this.toImplement = toImplement;
        }

        @Override
        public void expand(TypeDeclaration typeDeclaration) {
            if( typeDeclaration instanceof NodeWithImplements ){
                NodeWithImplements nwi = (NodeWithImplements) typeDeclaration;
                Arrays.stream( toImplement ).forEach( i -> nwi.addImplementedType(i));
            }
            _macro.removeAnnotation(typeDeclaration, _implement.class);
        }

        @Override
        public String toString(){
            String s = "";
            for(int i=0;i<toImplement.length;i++){
                if( i > 0){
                    s +=",";
                }
                s += toImplement[i].getCanonicalName();
            }
            return "macro[toImplement("+s+")]";
        }
    }
}
