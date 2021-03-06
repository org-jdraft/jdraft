package org.jdraft.diff;

import java.util.Objects;

import com.github.javaparser.ast.body.ReceiverParameter;
import org.jdraft.*;
import org.jdraft._receiverParam._withReceiverParam;

import org.jdraft.diff._diff.*;

public final class _receiverParamDiff
        implements  _differ<_receiverParam, _tree._node> {

    public static final _receiverParamDiff INSTANCE = new _receiverParamDiff();
    
    public _diff diff(_withReceiverParam left, _withReceiverParam right){
        return diff( _nodePath.of(),
                new _diffList( (_tree._node)left, (_tree._node)right),
                (_tree._node)left,
                (_tree._node)right,
                left.getReceiverParam(),
                right.getReceiverParam());
    }
    
    @Override
    public <_PN extends _tree._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _receiverParam left, _receiverParam right) {
        if (!Objects.equals(left, right)) {
            dt.addDiff(new change_receiverParameter(path.in(Feature.RECEIVER_PARAM), (_withReceiverParam) _leftParent, (_withReceiverParam) _rightParent));
        }
        return (_diff) dt;
    }

    public static class change_receiverParameter 
            implements _diffNode<_withReceiverParam>, _diffNode._change<ReceiverParameter> {

        public _nodePath path;
        public _withReceiverParam leftParent;
        public _withReceiverParam rightParent;
        public ReceiverParameter left;
        public ReceiverParameter right;

        public change_receiverParameter(_nodePath path, _withReceiverParam leftParent, _withReceiverParam rightParent) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            if (leftParent.hasReceiverParam()) {
                this.left = leftParent.getReceiverParam().node.clone();
            }
            if (rightParent.hasReceiverParam()) {
                this.right = rightParent.getReceiverParam().node.clone();
            }
        }

        @Override
        public _withReceiverParam leftParent() {
            return leftParent;
        }

        @Override
        public _withReceiverParam rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public ReceiverParameter left() {
            return left;
        }

        @Override
        public ReceiverParameter right() {
            return right;
        }

        @Override
        public void patchLeftToRight() {
            leftParent.setReceiverParam(left);
            rightParent.setReceiverParam(left);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.setReceiverParam(right);
            rightParent.setReceiverParam(right);
        }

        @Override
        public String toString() {
            return "   ~ " + path;
        }
    }
}
