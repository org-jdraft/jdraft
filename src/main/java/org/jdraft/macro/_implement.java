package org.jdraft.macro;

import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithImplements;
import org.jdraft._type;

import java.lang.annotation.*;
import java.util.Arrays;

/**
 * Annotation/Macro to add implements and imports to a {@link _type}
 *
 * @see macro
 */
@Retention( RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _implement {

    Class[] value();

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
            //_macro.removeAnnotation(typeDeclaration, _implement.class);
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
