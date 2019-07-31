package org.jdraft.diff;

import java.util.*;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.type.ReferenceType;

import org.jdraft.*;
import org.jdraft._throws._hasThrows;

import org.jdraft.diff._diff.*;

/**
 *
 * @author Eric
 */
public class _throwsDiff implements _differ<_throws, _node> {

    public static final _throwsDiff INSTANCE = new _throwsDiff();
    
    public boolean equivalent(NodeList<ReferenceType> left, NodeList<ReferenceType> right) {
        return Ast.typesEqual(left, right);
    }

    public _diff diff( _hasThrows left, _hasThrows right){
        return diff( _path.of(), new _mutableDiffList(), (_node)left, (_node)right, left.getThrows(), right.getThrows());
    }
    
    @Override
    public <R extends _node> _diff diff(_path path, _build dt, R leftRoot, R rightRoot, _throws left, _throws right) {
        if (!Objects.equals(left, right)) {
            dt.addDiff(new _change_throws(path.in(_java.Component.THROWS), (_throws._hasThrows) leftRoot, (_throws._hasThrows) rightRoot));
        }
        return (_diff) dt;
    }

    public static class _change_throws
            implements _diffNode<_throws._hasThrows>, _diffNode._change<List<ReferenceType>> {

        public _path path;
        public _throws._hasThrows leftRoot;
        public _throws._hasThrows rightRoot;
        public NodeList<ReferenceType> left;
        public NodeList<ReferenceType> right;

        public _change_throws(_path p, _throws._hasThrows leftRoot, _throws._hasThrows rightRoot) {
            this.path = p;
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            left = new NodeList<>();
            //leftRoot.
            //left.addAll( ((NodeWithThrownExceptions)leftRoot.getThrows().ast()).getThrownExceptions());
            left.addAll(leftRoot.getThrows().astNodeWithThrows.getThrownExceptions());
            right = new NodeList<>();
            right.addAll(rightRoot.getThrows().astNodeWithThrows.getThrownExceptions());
        }

        @Override
        public _throws._hasThrows leftRoot() {
            return leftRoot;
        }

        @Override
        public _throws._hasThrows rightRoot() {
            return rightRoot;
        }

        @Override
        public _path path() {
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
            leftRoot.setThrows(left);
            rightRoot.setThrows(left);
        }

        @Override
        public void patchRightToLeft() {
            leftRoot.setThrows(right);
            rightRoot.setThrows(right);
        }

        @Override
        public String toString() {
            return "   ~ " + path;
        }
    }
}
