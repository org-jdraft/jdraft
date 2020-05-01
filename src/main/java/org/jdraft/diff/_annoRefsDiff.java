package org.jdraft.diff;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;

import org.jdraft.*;
import org.jdraft._annoRefs._withAnnoRefs;
import org.jdraft._java.Component;
import org.jdraft.diff._diff.*;

/**
 * Diff for: {@link _annoRef} {@link _annoRefs} and {@link _withAnnoRefs}
 *
 * @author Eric
 */
public final class _annoRefsDiff
    implements _differ<_annoRefs, _java._multiPart> {

    public static final _annoRefsDiff INSTANCE = new _annoRefsDiff();
    
     public _diff diff(_withAnnoRefs left, _withAnnoRefs right){
        return diff( 
                _nodePath.of(),
                new _diffList( (_java._multiPart)left, (_java._multiPart)right),
                (_java._multiPart)left,
                (_java._multiPart)right,
                left.getAnnoRefs(),
                right.getAnnoRefs());
    }
     
    @Override
    public <_PN extends _java._multiPart> _diff diff(_nodePath path, _build ds, _PN _leftParent, _PN _rightParent, _annoRefs left, _annoRefs right) {
        NodeList<AnnotationExpr> laes = left.astAnnNode.getAnnotations();
        NodeList<AnnotationExpr> raes = right.astAnnNode.getAnnotations();
        for (int i = 0; i < laes.size(); i++) {
            AnnotationExpr e = (AnnotationExpr) laes.get(i);
            //find a matching annotation in other, if one isnt found, then not equal
            
            if (!raes.stream().filter(a -> Expressions.equal(e, (AnnotationExpr) a)).findFirst().isPresent()) {
            //if (!raes.stream().filter(a -> Ast.annotationEqual(e, (AnnotationExpr) a)).findFirst().isPresent()) {
                
                ds.addDiff(new _leftOnly_anno( //in LEFT not in RIGHT means REMOVE
                        path.in(Component.ANNO, e.getNameAsString()), (_withAnnoRefs) _leftParent, (_withAnnoRefs) _rightParent, _annoRef.of(e)));
            }
        }
        for (int i = 0; i < raes.size(); i++) {
            AnnotationExpr e = (AnnotationExpr) raes.get(i);
            //find a matching annotation in other, if one isnt found, then not equal
            
            if (!laes.stream().filter(a -> Expressions.equal(e, (AnnotationExpr) a)).findFirst().isPresent()) {
            //if (!laes.stream().filter(a -> Ast.annotationEqual(e, (AnnotationExpr) a)).findFirst().isPresent()) {
                ds.addDiff(new _rightOnly_anno( //in LEFT not in RIGHT means REMOVE
                        path.in(Component.ANNO, e.getNameAsString()), (_withAnnoRefs) _leftParent, (_withAnnoRefs) _rightParent, _annoRef.of(e)));
            }
        }
        return ds;
    }

    public static class _leftOnly_anno
            implements _diffNode<_withAnnoRefs>, _diffNode._leftOnly<_annoRef> {

        public _nodePath path;
        public _withAnnoRefs leftParent;
        public _withAnnoRefs rightParent;
        public _annoRef left;

        public _leftOnly_anno(_nodePath p, _withAnnoRefs left, _withAnnoRefs right, _annoRef toRemove) {
            this.path = p;
            this.leftParent = left;
            this.rightParent = right;
            this.left = toRemove.copy();
        }

        @Override
        public _withAnnoRefs leftParent() {
            return leftParent;
        }

        @Override
        public _withAnnoRefs rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public _annoRef left() {
            return left;
        }

        @Override
        public void patchRightToLeft() {
            leftParent.removeAnnoRef(left.ast());
            rightParent.removeAnnoRef(left.ast());
        }

        @Override
        public void patchLeftToRight() {
            
            leftParent.removeAnnoRef(left.ast());
            leftParent.addAnnoRefs(left);
            rightParent.removeAnnoRef(left.ast());
            rightParent.addAnnoRefs(left);
        }

        @Override
        public String toString() {
            return "   - " + path;
        }
    }

    public static class _rightOnly_anno implements _diffNode<_withAnnoRefs>, _diffNode._rightOnly<_annoRef> {

        public _nodePath path;
        public _withAnnoRefs leftParent;
        public _withAnnoRefs rightParent;
        public _annoRef right;

        public _rightOnly_anno(_nodePath p, _withAnnoRefs leftParent, _withAnnoRefs rightParent, _annoRef right) {
            this.path = p;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.right = right.copy();
        }

        @Override
        public _withAnnoRefs leftParent() {
            return leftParent;
        }

        @Override
        public _withAnnoRefs rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public _annoRef right() {
            return right;
        }

        @Override
        public void patchRightToLeft() {
            //remove it before just so we dont mistakenly add it twice
            leftParent.removeAnnoRef(right.ast());
            leftParent.addAnnoRefs(right);
            rightParent.removeAnnoRef(right.ast());
            rightParent.addAnnoRefs(right);
        }

        @Override
        public void patchLeftToRight() {
            //remove it before just so we dont mistakenly add it twice
            leftParent.removeAnnoRef(right.ast());
            rightParent.removeAnnoRef(right.ast());
        }

        @Override
        public String toString() {
            return "   + " + path;
        }
    }
}
