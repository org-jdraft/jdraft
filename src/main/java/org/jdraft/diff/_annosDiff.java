package org.jdraft.diff;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;

import org.jdraft.*;
import org.jdraft._annos._withAnnos;
import org.jdraft.diff._diff.*;

/**
 * Diff for: {@link _anno} {@link _annos} and {@link _withAnnos}
 *
 * @author Eric
 */
public final class _annosDiff
    implements _differ<_annos, _tree._node> {

    public static final _annosDiff INSTANCE = new _annosDiff();
    
     public _diff diff(_withAnnos left, _withAnnos right){
        return diff( 
                _nodePath.of(),
                new _diffList( (_tree._node)left, (_tree._node)right),
                (_tree._node)left,
                (_tree._node)right,
                left.getAnnos(),
                right.getAnnos());
    }
     
    @Override
    public <_PN extends _tree._node> _diff diff(_nodePath path, _build ds, _PN _leftParent, _PN _rightParent, _annos left, _annos right) {
        NodeList<AnnotationExpr> laes = left.astAnnNode.getAnnotations();
        NodeList<AnnotationExpr> raes = right.astAnnNode.getAnnotations();
        for (int i = 0; i < laes.size(); i++) {
            AnnotationExpr e = (AnnotationExpr) laes.get(i);
            //find a matching annotation in other, if one isnt found, then not equal
            
            if (!raes.stream().filter(a -> Expr.equal(e, (AnnotationExpr) a)).findFirst().isPresent()) {
            //if (!raes.stream().filter(a -> Ast.annotationEqual(e, (AnnotationExpr) a)).findFirst().isPresent()) {
                
                ds.addDiff(new _leftOnly_anno( //in LEFT not in RIGHT means REMOVE
                        path.in(Feature.ANNO_EXPR, e.getNameAsString()), (_withAnnos) _leftParent, (_withAnnos) _rightParent, _anno.of(e)));
            }
        }
        for (int i = 0; i < raes.size(); i++) {
            AnnotationExpr e = (AnnotationExpr) raes.get(i);
            //find a matching annotation in other, if one isnt found, then not equal
            
            if (!laes.stream().filter(a -> Expr.equal(e, (AnnotationExpr) a)).findFirst().isPresent()) {
            //if (!laes.stream().filter(a -> Ast.annotationEqual(e, (AnnotationExpr) a)).findFirst().isPresent()) {
                ds.addDiff(new _rightOnly_anno( //in LEFT not in RIGHT means REMOVE
                        path.in(Feature.ANNO_EXPR, e.getNameAsString()), (_withAnnos) _leftParent, (_withAnnos) _rightParent, _anno.of(e)));
            }
        }
        return ds;
    }

    public static class _leftOnly_anno
            implements _diffNode<_withAnnos>, _diffNode._leftOnly<_anno> {

        public _nodePath path;
        public _withAnnos leftParent;
        public _withAnnos rightParent;
        public _anno left;

        public _leftOnly_anno(_nodePath p, _withAnnos left, _withAnnos right, _anno toRemove) {
            this.path = p;
            this.leftParent = left;
            this.rightParent = right;
            this.left = toRemove.copy();
        }

        @Override
        public _withAnnos leftParent() {
            return leftParent;
        }

        @Override
        public _withAnnos rightParent() {
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

    public static class _rightOnly_anno implements _diffNode<_withAnnos>, _diffNode._rightOnly<_anno> {

        public _nodePath path;
        public _withAnnos leftParent;
        public _withAnnos rightParent;
        public _anno right;

        public _rightOnly_anno(_nodePath p, _withAnnos leftParent, _withAnnos rightParent, _anno right) {
            this.path = p;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.right = right.copy();
        }

        @Override
        public _withAnnos leftParent() {
            return leftParent;
        }

        @Override
        public _withAnnos rightParent() {
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
