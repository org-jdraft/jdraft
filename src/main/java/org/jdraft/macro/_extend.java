package org.jdraft.macro;

import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithExtends;
import org.jdraft._draftException;
import org.jdraft._type;

import java.lang.annotation.*;

/**
 * Annotation / Macro to add extend to a {@link _type}
 *
 * @see macro
 */
@Retention( RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.TYPE_USE})
public @interface _extend{
    Class[] value();

    class Act extends macro<_extend, TypeDeclaration>{

        public Class[] classes;

        public Act(_extend _e) {
            super(_e);
            this.classes = _e.value();
        }

        public Act( Class... classes ) {
            super( _extend.class);
            this.classes = classes;
        }

        public void expand(TypeDeclaration node) {
            to(node, this.classes);
        }

        public static <T extends TypeDeclaration> T to( T node, Class... classes){
            if (node instanceof NodeWithExtends) {
                NodeWithExtends nwe = (NodeWithExtends) node;
                for (int i = 0; i < classes.length; i++) {
                    nwe.addExtendedType(classes[i]);
                }
            } else {
                if (classes.length > 0) {
                    throw new _draftException("cannot add extends to node of " + node.getClass());
                }
            }
            return node;
        }

        @Override
        public String toString(){
            String s = "";
            for(int i=0;i<classes.length;i++){
                if( i > 0){
                    s +=",";
                }
                s += classes[i].getCanonicalName();
            }
            return "macro[extend("+s+")]";
        }
    }
}
