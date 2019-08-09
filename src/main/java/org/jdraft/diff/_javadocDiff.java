package org.jdraft.diff;

import java.util.Objects;

import com.github.javaparser.ast.comments.JavadocComment;

import org.jdraft.*;
import org.jdraft._javadoc._hasJavadoc;

import org.jdraft.diff._diff.*;

/**
 * Differ for {@link _javadoc}
 */
public class _javadocDiff implements _differ<_javadoc, _node> {

    public static final _javadocDiff INSTANCE = new _javadocDiff();

    public boolean equivalent(_javadoc left, _javadoc right) {
        return Objects.equals(left, right);
    }

    public _diff diff( _hasJavadoc leftParent, _hasJavadoc rightParent){
        return diff( _path.of(), 
            new _diffList((_node)leftParent, (_node)rightParent),
            (_node)leftParent,
            (_node)rightParent,
            leftParent.getJavadoc(),
            rightParent.getJavadoc());
    }

    @Override
    public <_PN extends _node> _diff diff(_path path, _build dt, _PN _leftParent, _PN _rightParent, _javadoc left, _javadoc right) {
        if (!equivalent(left, right)) {
            dt.addDiff(new _changeJavadoc(path.in(_java.Component.JAVADOC), (_javadoc._hasJavadoc) _leftParent, (_javadoc._hasJavadoc) _rightParent));
        }
        return dt;
    }

    /**
     * Both signifies a delta and provides a means to commit (via right()) or
     * rollback( via left())
     */
    public static class _changeJavadoc
            implements _diffNode, _diffNode._change<JavadocComment> {

        public _path path;
        public _javadoc._hasJavadoc leftParent;
        public _javadoc._hasJavadoc rightParent;
        public JavadocComment leftJavadoc;
        public JavadocComment rightJavadoc;

        public _changeJavadoc(_path _p, _javadoc._hasJavadoc leftParent, _javadoc._hasJavadoc rightParent) {
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
                leftParent.javadoc(leftJavadoc.clone());
                rightParent.javadoc(leftJavadoc.clone());
                //System.out.println("LEFT JAVADOC"+left.getJavadoc() );
                //System.out.println("RIGHT JAVADOC"+right.getJavadoc() );
                //System.out.println( "ARE THEY EQUAL " + left.getJavadoc().equals( right.getJavadoc() ));
            } else{
                leftParent.removeJavadoc();
                rightParent.removeJavadoc();
            }
        }

        @Override
        public void patchRightToLeft() {
            leftParent.javadoc(rightJavadoc);
            rightParent.javadoc(rightJavadoc);
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
        public _java leftParent() {
            return leftParent;
        }

        @Override
        public _java rightParent() {
            return rightParent;
        }

        @Override
        public _path path() {
            return path;
        }

        @Override
        public String toString() {
            return "   ~ " + path;
        }
    }
}
