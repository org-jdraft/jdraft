package org.jdraft.diff;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;

import org.jdraft.*;
import org.jdraft._anno._hasAnnos;
import org.jdraft._java.Component;
import org.jdraft.diff._diff.*;

/**
 * Diff for: {@link org.jdraft._anno} {@link org.jdraft._annos} and {@link org.jdraft._anno._hasAnnos}
 *
 * @author Eric
 */
public class _annosDiff
    implements _differ<_annos, _java._compound> {

    public static final _annosDiff INSTANCE = new _annosDiff();
    
     public _diff diff( _hasAnnos left, _hasAnnos right){
        return diff( 
                _nodePath.of(),
                new _diffList( (_java._compound)left, (_java._compound)right),
                (_java._compound)left,
                (_java._compound)right,
                left.getAnnos(), 
                right.getAnnos());
    }
     
    @Override
    public <_PN extends _java._compound> _diff diff(_nodePath path, _build ds, _PN _leftParent, _PN _rightParent, _annos left, _annos right) {
        NodeList<AnnotationExpr> laes = left.astAnnNode.getAnnotations();
        NodeList<AnnotationExpr> raes = right.astAnnNode.getAnnotations();
        for (int i = 0; i < laes.size(); i++) {
            AnnotationExpr e = (AnnotationExpr) laes.get(i);
            //find a matching annotation in other, if one isnt found, then not equal
            
            if (!raes.stream().filter(a -> Ex.equivalent(e, (AnnotationExpr) a)).findFirst().isPresent()) {
            //if (!raes.stream().filter(a -> Ast.annotationEqual(e, (AnnotationExpr) a)).findFirst().isPresent()) {
                
                ds.addDiff(new _leftOnly_anno( //in LEFT not in RIGHT means REMOVE
                        path.in(Component.ANNO, e.getNameAsString()), (_anno._hasAnnos) _leftParent, (_anno._hasAnnos) _rightParent, _anno.of(e)));
            }
        }
        for (int i = 0; i < raes.size(); i++) {
            AnnotationExpr e = (AnnotationExpr) raes.get(i);
            //find a matching annotation in other, if one isnt found, then not equal
            
            if (!laes.stream().filter(a -> Ex.equivalent(e, (AnnotationExpr) a)).findFirst().isPresent()) {
            //if (!laes.stream().filter(a -> Ast.annotationEqual(e, (AnnotationExpr) a)).findFirst().isPresent()) {
                ds.addDiff(new _rightOnly_anno( //in LEFT not in RIGHT means REMOVE
                        path.in(Component.ANNO, e.getNameAsString()), (_anno._hasAnnos) _leftParent, (_anno._hasAnnos) _rightParent, _anno.of(e)));
            }
        }
        return ds;
    }

    public static class _leftOnly_anno
            implements _diffNode<_anno._hasAnnos>, _diffNode._leftOnly<_anno> {

        public _nodePath path;
        public _anno._hasAnnos leftParent;
        public _anno._hasAnnos rightParent;
        public _anno left;

        public _leftOnly_anno(_nodePath p, _anno._hasAnnos left, _anno._hasAnnos right, _anno toRemove) {
            this.path = p;
            this.leftParent = left;
            this.rightParent = right;
            this.left = toRemove.copy();
        }

        @Override
        public _anno._hasAnnos leftParent() {
            return leftParent;
        }

        @Override
        public _anno._hasAnnos rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public _anno left() {
            return left;
        }

        @Override
        public void patchRightToLeft() {
            leftParent.removeAnno(left.ast());
            rightParent.removeAnno(left.ast());
        }

        @Override
        public void patchLeftToRight() {
            
            leftParent.removeAnno(left.ast());
            leftParent.addAnnos(left);
            rightParent.removeAnno(left.ast());
            rightParent.addAnnos(left);
        }

        @Override
        public String toString() {
            return "   - " + path;
        }
    }

    public static class _rightOnly_anno implements _diffNode<_anno._hasAnnos>, _diffNode._rightOnly<_anno> {

        public _nodePath path;
        public _anno._hasAnnos leftParent;
        public _anno._hasAnnos rightParent;
        public _anno right;

        public _rightOnly_anno(_nodePath p, _anno._hasAnnos leftParent, _anno._hasAnnos rightParent, _anno right) {
            this.path = p;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.right = right.copy();
        }

        @Override
        public _anno._hasAnnos leftParent() {
            return leftParent;
        }

        @Override
        public _anno._hasAnnos rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public _anno right() {
            return right;
        }

        @Override
        public void patchRightToLeft() {
            //remove it before just so we dont mistakenly add it twice
            leftParent.removeAnno(right.ast());
            leftParent.addAnnos(right);
            rightParent.removeAnno(right.ast());
            rightParent.addAnnos(right);
        }

        @Override
        public void patchLeftToRight() {
            //remove it before just so we dont mistakenly add it twice
            leftParent.removeAnno(right.ast());
            rightParent.removeAnno(right.ast());
        }

        @Override
        public String toString() {
            return "   + " + path;
        }
    }
}
