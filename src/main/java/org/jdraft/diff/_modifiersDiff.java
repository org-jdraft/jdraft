package org.jdraft.diff;

import java.util.Objects;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;

import org.jdraft.*;
import org.jdraft._modifiers._withModifiers;

import org.jdraft.diff._diff.*;

/**
 *
 * @author Eric
 */
public final class _modifiersDiff implements
        _differ<NodeList<Modifier>, _java._multiPart> {

    public static final _modifiersDiff INSTANCE = new _modifiersDiff();
    
    public boolean equivalent(_modifiers left, _modifiers right) {
        return Objects.equals(left, right);
    }

    public _diff diff(_withModifiers leftParent, _withModifiers rightParent){
        return diff( _nodePath.of(),
                new _diffList((_java._multiPart)leftParent, (_java._multiPart)rightParent),
                (_java._multiPart)leftParent,
                (_java._multiPart)rightParent,
                leftParent.getEffectiveModifiers(),
                rightParent.getEffectiveModifiers());
    }
    
    @Override
    public <_PN extends _java._multiPart> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, NodeList<Modifier> left, NodeList<Modifier> right) {
        if (!Objects.equals(left, right)) {
            dt.addDiff(new _changeModifiers(path.in(_java.Feature.MODIFIERS), (_withModifiers) _leftParent, (_withModifiers) _rightParent));
        }
        return dt;
    }

    public static class _changeModifiers
            implements _diffNode<_withModifiers>, _diffNode._change<NodeList<Modifier>> {

        public _nodePath path;
        public _withModifiers leftParent;
        public _withModifiers rightParent;
        public NodeList<Modifier> left;
        public NodeList<Modifier> right;

        public _changeModifiers(_nodePath path, _withModifiers leftParent, _withModifiers rightParent) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.left = leftParent.getEffectiveModifiers();
            this.right = rightParent.getEffectiveModifiers();
        }

        @Override
        public _withModifiers leftParent() {
            return leftParent;
        }

        @Override
        public _withModifiers rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public NodeList<Modifier> left() {
            return left;
        }

        @Override
        public NodeList<Modifier> right() {
            return right;
        }

        @Override
        public void patchLeftToRight() {
            leftParent.setModifiers(left);
            rightParent.setModifiers(left);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.setModifiers(right);
            rightParent.setModifiers(right);
        }

        @Override
        public String toString() {
            return "   ~ " + path;
        }
    }
}
