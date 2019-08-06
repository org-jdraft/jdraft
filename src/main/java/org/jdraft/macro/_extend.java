package org.jdraft.macro;

import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithExtends;
import org.jdraft._draftException;
import org.jdraft._type;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.function.Consumer;

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

    class Act implements Consumer<TypeDeclaration> {

        public Class[] classes;

        public Act(_extend _e) {
            this(_e.value() );
        }

        public Act( Class[] classes ) { this.classes = classes; }

        public void accept(TypeDeclaration node) {
            if (node instanceof NodeWithExtends) {
                NodeWithExtends nwe = (NodeWithExtends) node;
                for (int i = 0; i < classes.length; i++) {
                    nwe.addExtendedType(classes[i]);
                }
                _macro.removeAnnotation(node, _extend.class);
            } else {
                if (classes.length > 0) {
                    throw new _draftException("cannot add extends to node of " + node.getClass());
                }
            }
        }
    }
}
