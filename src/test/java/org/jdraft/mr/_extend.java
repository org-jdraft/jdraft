package org.jdraft.mr;

import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithExtends;
import org.jdraft._draftException;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.Consumer;

/**
 * Here is an annotation that has values...
 * the following will work:
 * <PRE>{@code
 *
 * @_extend(BaseClass.class) class C{}
 * @_extend({Serializable.class, Blahde.class})
 * interface I{}
 * <p>
 * }</PRE>
 */
@Retention(RetentionPolicy.RUNTIME)
@interface _extend {

    Class[] value();

    class AnnoMacro implements Consumer<TypeDeclaration> {

        public _extend _e;

        public AnnoMacro(_extend _e) {
            this._e = _e;
        }

        public void accept(TypeDeclaration node) {
            if (node instanceof NodeWithExtends) {
                NodeWithExtends nwe = (NodeWithExtends) node;
                for (int i = 0; i < _e.value().length; i++) {
                    nwe.addExtendedType(_e.value()[i]);
                }
                _annoMacro.removeAnnotation(node, this._e);
            } else {
                if (_e.value().length > 0) {
                    throw new _draftException("cannot add extends to node of " + node.getClass());
                }
            }
        }
    }
}
