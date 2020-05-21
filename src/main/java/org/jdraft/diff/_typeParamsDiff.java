package org.jdraft.diff;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.type.TypeParameter;

import org.jdraft.*;
import org.jdraft._typeParams._withTypeParams;
import org.jdraft.diff._diff.*;

/**
 *
 * @author Eric
 */
public final class _typeParamsDiff
        implements _differ<_typeParams, _java._node> {

    public static final _typeParamsDiff INSTANCE = new _typeParamsDiff();
    
    public boolean equivalent(NodeList<TypeParameter> left, NodeList<TypeParameter> right) {
        return Types.equal(left, right);
    }

    public _diff diff(_withTypeParams leftParent, _withTypeParams rightParent){
        return diff( _nodePath.of(),
                new _diffList( (_java._node)leftParent, (_java._node)rightParent),
                (_java._node)leftParent,
                (_java._node)rightParent,
                leftParent.getTypeParams(),
                rightParent.getTypeParams());
    }

    @Override
    public <_PN extends _java._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _typeParams left, _typeParams right) {
        
        if (!Types.equal( ((NodeWithTypeParameters)left.astHolder()).getTypeParameters(),
                ((NodeWithTypeParameters)right.astHolder()).getTypeParameters())) {
            dt.addDiff(new _change_typeParams(path.in(_java.Feature.TYPE_PARAMS),
                    (_withTypeParams) _leftParent, (_withTypeParams) _rightParent));
        }
        return dt;
    }

    public static class _change_typeParams
            implements _diffNode<_withTypeParams>, _diffNode._change<NodeList<TypeParameter>> {

        public _nodePath path;
        public _withTypeParams leftParent;
        public _withTypeParams rightParent;
        public NodeList<TypeParameter> left;
        public NodeList<TypeParameter> right;

        public _change_typeParams(_nodePath path, _withTypeParams leftParent, _withTypeParams rightParent) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;

            this.left = new NodeList<>();
            this.right = new NodeList<>();
            this.left.addAll(leftParent.listAstTypeParameters());
            this.right.addAll(rightParent.listAstTypeParameters());
        }

        @Override
        public _withTypeParams leftParent() {
            return leftParent;
        }

        @Override
        public _withTypeParams rightParent() {
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
            this.leftParent.removeTypeParams();
            this.leftParent.setTypeParams(left);

            this.rightParent.removeTypeParams();
            this.rightParent.setTypeParams(left);
        }

        @Override
        public void patchRightToLeft() {
            this.leftParent.removeTypeParams();
            this.leftParent.setTypeParams(right);

            this.rightParent.removeTypeParams();
            this.rightParent.setTypeParams(right);
        }

        @Override
        public String toString() {
            return "   ~ " + path;
        }
    }
}
