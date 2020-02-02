package org.jdraft.diff;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.type.TypeParameter;

import org.jdraft.*;
import org.jdraft._typeParameter._hasTypeParameters;
import org.jdraft.diff._diff.*;

/**
 *
 * @author Eric
 */
public class _typeParametersDiff
        implements _differ<_typeParameters, _java._node> {

    public static final _typeParametersDiff INSTANCE = new _typeParametersDiff();
    
    public boolean equivalent(NodeList<TypeParameter> left, NodeList<TypeParameter> right) {
        return Ast.typesEqual(left, right);
    }

    public _diff diff( _hasTypeParameters leftParent, _hasTypeParameters rightParent){
        return diff( _nodePath.of(),
                new _diffList( (_java._node)leftParent, (_java._node)rightParent),
                (_java._node)leftParent,
                (_java._node)rightParent,
                leftParent.getTypeParameters(),
                rightParent.getTypeParameters());
    }

    @Override
    public <_PN extends _java._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _typeParameters left, _typeParameters right) {
        
        if (!Ast.typesEqual( ((NodeWithTypeParameters)left.astHolder()).getTypeParameters(), 
                ((NodeWithTypeParameters)right.astHolder()).getTypeParameters())) {
            dt.addDiff(new _change_typeParameters(path.in(_java.Component.TYPE_PARAMETERS), 
                    (_typeParameter._hasTypeParameters) _leftParent, (_typeParameter._hasTypeParameters) _rightParent));
        }
        return dt;
    }

    public static class _change_typeParameters
            implements _diffNode<_typeParameter._hasTypeParameters>, _diffNode._change<NodeList<TypeParameter>> {

        public _nodePath path;
        public _typeParameter._hasTypeParameters leftParent;
        public _typeParameter._hasTypeParameters rightParent;
        public NodeList<TypeParameter> left;
        public NodeList<TypeParameter> right;

        public _change_typeParameters(_nodePath path, _typeParameter._hasTypeParameters leftParent, _typeParameter._hasTypeParameters rightParent) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;

            this.left = new NodeList<>();
            this.right = new NodeList<>();
            this.left.addAll(leftParent.listAstTypeParameters());
            this.right.addAll(rightParent.listAstTypeParameters());
        }

        @Override
        public _typeParameter._hasTypeParameters leftParent() {
            return leftParent;
        }

        @Override
        public _typeParameter._hasTypeParameters rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public NodeList<TypeParameter> left() {
            return left;
        }

        @Override
        public NodeList<TypeParameter> right() {
            return right;
        }

        @Override
        public void patchLeftToRight() {
            this.leftParent.removeTypeParameters();
            this.leftParent.typeParameters(left);

            this.rightParent.removeTypeParameters();
            this.rightParent.typeParameters(left);
        }

        @Override
        public void patchRightToLeft() {
            this.leftParent.removeTypeParameters();
            this.leftParent.typeParameters(right);

            this.rightParent.removeTypeParameters();
            this.rightParent.typeParameters(right);
        }

        @Override
        public String toString() {
            return "   ~ " + path;
        }
    }
}
