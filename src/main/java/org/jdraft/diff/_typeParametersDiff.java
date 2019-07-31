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
        implements _differ<_typeParameter._typeParameters, _node> {

    public static final _typeParametersDiff INSTANCE = new _typeParametersDiff();
    
    public boolean equivalent(NodeList<TypeParameter> left, NodeList<TypeParameter> right) {
        return Ast.typesEqual(left, right);
    }

    public _diff diff( _hasTypeParameters left, _hasTypeParameters right){
        return diff( _path.of(), 
                new _diff._mutableDiffList(), 
                (_node)left, 
                (_node)right, 
                left.getTypeParameters(), 
                right.getTypeParameters());
    }
    @Override
    public <R extends _node> _diff diff(_path path, _build dt, R leftRoot, R rightRoot, _typeParameter._typeParameters left, _typeParameter._typeParameters right) {
        
        if (!Ast.typesEqual( ((NodeWithTypeParameters)left.astHolder()).getTypeParameters(), 
                ((NodeWithTypeParameters)right.astHolder()).getTypeParameters())) {
            dt.addDiff(new _change_typeParameters(path.in(_java.Component.TYPE_PARAMETERS), 
                    (_typeParameter._hasTypeParameters) leftRoot, (_typeParameter._hasTypeParameters) rightRoot));
        }
        return (_diff) dt;
    }

    public static class _change_typeParameters
            implements _diffNode<_typeParameter._hasTypeParameters>, _diffNode._change<NodeList<TypeParameter>> {

        public _path path;
        public _typeParameter._hasTypeParameters leftRoot;
        public _typeParameter._hasTypeParameters rightRoot;
        public NodeList<TypeParameter> left;
        public NodeList<TypeParameter> right;

        public _change_typeParameters(_path path, _typeParameter._hasTypeParameters leftRoot, _typeParameter._hasTypeParameters rightRoot) {
            this.path = path;
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;

            this.left = new NodeList<>();
            this.right = new NodeList<>();
            this.left.addAll(leftRoot.listAstTypeParameters());
            this.right.addAll(rightRoot.listAstTypeParameters());
        }

        @Override
        public _typeParameter._hasTypeParameters leftRoot() {
            return leftRoot;
        }

        @Override
        public _typeParameter._hasTypeParameters rightRoot() {
            return rightRoot;
        }

        @Override
        public _path path() {
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
            this.leftRoot.removeTypeParameters();
            this.leftRoot.typeParameters(left);

            this.rightRoot.removeTypeParameters();
            this.rightRoot.typeParameters(left);
        }

        @Override
        public void patchRightToLeft() {
            this.leftRoot.removeTypeParameters();
            this.leftRoot.typeParameters(right);

            this.rightRoot.removeTypeParameters();
            this.rightRoot.typeParameters(right);
        }

        @Override
        public String toString() {
            return "   ~ " + path;
        }
    }
}
