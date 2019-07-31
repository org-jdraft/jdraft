package org.jdraft.macro;

import org.jdraft._type;

import java.lang.annotation.*;
import java.util.Arrays;

/**
 * Annotation Macro to add imports to a _type
 */
@Retention( RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface _extend{
    Class[] value();

    class Macro implements _macro<_type> {
        Class[] toExtend;

        public Macro( _extend _e ){
            this.toExtend = _e.value();
        }

        @Override
        public String toString(){
            String s = "";
            for(int i=0;i<toExtend.length;i++){
                if( i > 0){
                    s +=",";
                }
                s += toExtend[i].getCanonicalName();
            }
            return "macro[extend("+s+")]"; 
        }
        
        public Macro( Class...toExtend ){
            this.toExtend = toExtend;
        }

        public _type apply( _type  _t){
            return to( _t, toExtend );
        }

        public static <T extends _type> T to( T _t, Class... toExtend ){
            if( _t instanceof _type._hasExtends) {
                Arrays.stream(toExtend).forEach(e -> ((_type._hasExtends)_t).extend(e).imports(e));
            }
            return _t;
        }
    }
}
