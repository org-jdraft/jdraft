package org.jdraft.diff;

import java.util.*;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.type.ReferenceType;

import org.jdraft.*;
import org.jdraft._throws._withThrows;

import org.jdraft.diff._diff.*;

/**
 *
 * @author Eric
 */
public final class _throwsDiff implements _differ<_throws, _tree._node> {

    public static final _throwsDiff INSTANCE = new _throwsDiff();
    
    public boolean equivalent(NodeList<ReferenceType> left, NodeList<ReferenceType> right) {
        return Types.equal(left, right);
    }

    public _diff diff(_withThrows leftParent, _withThrows rightParent){
        return diff( _nodePath.of(), new _diffList((_tree._node)leftParent, (_tree._node)rightParent), (_tree._node)leftParent, (_tree._node)rightParent, leftParent.getThrows(), rightParent.getThrows());
    }
    
    @Override
    public <_PN extends _tree._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _throws left, _throws right) {
        if (!Objects.equals(left, right)) {
            dt.addDiff(new _change_throws(path.in(Feature.THROWS), (_withThrows) _leftParent, (_withThrows) _rightParent));
        }
        return dt;
    }

    public static class _change_throws
            implements _diffNode<_withThrows>, _diffNode._change<List<ReferenceType>> {

        public _nodePath path;
        public _withThrows leftParent;
        public _withThrows rightParent;
        public NodeList<ReferenceType> left;
        public NodeList<ReferenceType> right;

        public _change_throws(_nodePath p, _withThrows leftParent, _withThrows rightParent) {
            this.path = p;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            left = new NodeList<>();
            //leftRoot.
            //left.addAll( ((NodeWithThrownExceptions)leftRoot.getThrows().ast()).getThrownExceptions());
            left.addAll(leftParent.getThrows().parentNode.getThrownExceptions());
            right = new NodeList<>();
            right.addAll(rightParent.getThrows().parentNode.getThrownExceptions());
        }

        @Override
        public _withThrows leftParent() {
            return leftParent;
        }

        @Override
        public _withThrows rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return this.path;
        }

        @Override
        public List<ReferenceType> left() {
            return left;
        }

        @Override
        public List<ReferenceType> right() {
            return right;
        }

        @Override
        public void patchLeftToRight() {
            leftParent.setThrows(left);
            rightParent.setThrows(left);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.setThrows(right);
            rightParent.setThrows(right);
        }

        @Override
        public String toString() {
            return "   ~ " + path;
        }
    }
}
