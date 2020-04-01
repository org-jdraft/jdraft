package org.jdraft.diff;

import java.util.Objects;

import com.github.javaparser.ast.comments.JavadocComment;

import org.jdraft.*;
import org.jdraft._javadoc._withJavadoc;

import org.jdraft.diff._diff.*;

/**
 * Differ for {@link _javadoc}
 */
public class _javadocDiff implements _differ<_javadocComment, _java._multiPart> {

    public static final _javadocDiff INSTANCE = new _javadocDiff();

    public boolean equivalent(_javadocComment left, _javadocComment right) {
        return Objects.equals(left, right);
    }

    public _diff diff(_withJavadoc leftParent, _withJavadoc rightParent){
        return diff( _nodePath.of(),
            new _diffList((_java._multiPart)leftParent, (_java._multiPart)rightParent),
            (_java._multiPart)leftParent,
            (_java._multiPart)rightParent,
            leftParent.getJavadoc(),
            rightParent.getJavadoc());
    }

    @Override
    public <_PN extends _java._multiPart> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _javadocComment left, _javadocComment right) {
        if (!equivalent(left, right)) {
            dt.addDiff(new _changeJavadoc(path.in(_java.Component.JAVADOC), (_withJavadoc) _leftParent, (_withJavadoc) _rightParent));
        }
        return dt;
    }

    /**
     * Both signifies a delta and provides a means to commit (via right()) or
     * rollback( via left())
     */
    public static class _changeJavadoc
            implements _diffNode, _diffNode._change<JavadocComment> {

        public _nodePath path;
        public _withJavadoc leftParent;
        public _withJavadoc rightParent;
        public JavadocComment leftJavadoc;
        public JavadocComment rightJavadoc;

        public _changeJavadoc(_nodePath _p, _withJavadoc leftParent, _withJavadoc rightParent) {
            this.path = _p;
            this.leftParent = leftParent;
            if (leftParent.hasJavadoc()) {
                this.leftJavadoc = leftParent.getJavadoc().ast().clone();
            }
            this.rightParent = rightParent;
            if (rightParent.hasJavadoc()) {
                this.rightJavadoc = rightParent.getJavadoc().ast().clone();
            }
        }

        @Override
        public void patchLeftToRight() {
            if( leftJavadoc != null ){
                //System.out.println("Setting on "+ left.getClass() );
                //System.out.println("Setting on "+ right.getClass() );
                leftParent.setJavadoc(leftJavadoc.clone());
                rightParent.setJavadoc(leftJavadoc.clone());
                //System.out.println("LEFT JAVADOC"+left.getJavadoc() );
                //System.out.println("RIGHT JAVADOC"+right.getJavadoc() );
                //System.out.println( "ARE THEY EQUAL " + left.getJavadoc().equals( right.getJavadoc() ));
            } else{
                ((_java._declared)leftParent).removeJavadoc();
                ((_java._declared)rightParent).removeJavadoc();
            }
        }

        @Override
        public void patchRightToLeft() {
            leftParent.setJavadoc(rightJavadoc);
            rightParent.setJavadoc(rightJavadoc);
        }

        @Override
        public JavadocComment left() {
            return leftJavadoc;
        }

        @Override
        public JavadocComment right() {
            return rightJavadoc;
        }

        @Override
        public _java._domain leftParent() {
            return leftParent;
        }

        @Override
        public _java._domain rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public String toString() {
            return "   ~ " + path;
        }
    }
}
