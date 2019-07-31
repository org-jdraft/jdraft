package org.jdraft.macro;

import org.jdraft._type;

import java.lang.annotation.*;

/**
 * Annotation Macro to add implements and imports to a _type
 * can be accessed by:
 * <UL>
 *     <LI>{@link _macro#to(Class, _type)} via the @_implement annotation
 *     <LI>creating a new instance and calling the {@link _macro#to(Class, _type)}  method
 *         _t = new _implement.Macro(Serializable.class).expand( _t );
 *     <LI>calling the _implement.Macro.to(_t, Serializable.class);
 * </UL>
 */
@Retention( RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
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
}
