package org.jdraft.diff;

import java.util.*;
import com.github.javaparser.ast.ImportDeclaration;

import org.jdraft.*;
import org.jdraft._imports;
import org.jdraft.diff._diff.*;

public final class _importsDiff
        implements _differ<List<ImportDeclaration>, _tree._node> {

    public static final _importsDiff INSTANCE = new _importsDiff();
    
    public boolean equivalent(List<ImportDeclaration> left, List<ImportDeclaration> right) {
        Set<ImportDeclaration> ls = new HashSet<>();
        Set<ImportDeclaration> rs = new HashSet<>();
        ls.addAll(left);
        rs.addAll(right);
        return Objects.equals(ls, rs);
    }

    public _diff diff( _type left, _type right){
        return diff( 
                _nodePath.of(),
                new _diffList(left, right),
                left,
                right, 
                left.astCompilationUnit().getImports(), 
                right.astCompilationUnit().getImports());
    }
    
    public _diff diff( _imports left, _imports right ){
        
        return diff( _nodePath.of(), new _diffList(null, null),
                _type.of(left.parentNode), _type.of(right.parentNode), left.parentNode.getImports(), right.parentNode.getImports());
    }
    
    public <_PN extends _tree._node> _diff diff(_nodePath path, _build dt, _PN leftParent, _PN rightParent, _type left, _type right) {
        if( left.isTopLevel() && right.isTopLevel() ){
            return diff( path, dt, leftParent, rightParent, left.astCompilationUnit().getImports(), right.astCompilationUnit().getImports() );
        }
        if( !left.isTopLevel() && !right.isTopLevel() ){
            return dt;
        }
        if( !left.isTopLevel() ){
            right.getImports().toEach(i -> dt.addDiff(new _rightOnly_import(
                path.in(Feature.IMPORT),
                (_type) leftParent,
                (_type) rightParent,
                i.node)) );
            return dt;
        } 
        
        left.getImports().toEach(i -> dt.addDiff(new _leftOnly_import(
                path.in(Feature.IMPORT),
                (_type) leftParent,
                (_type) rightParent,
                i.node)) );
            return dt;    
    }
    
    public <_PN extends _tree._node> _diff diff(_nodePath path, _build dt, _PN leftParent, _PN rightParent, _imports left, _imports right) {
        return diff( path, dt, leftParent, rightParent, left.parentNode.getImports(), right.parentNode.getImports() );
    }
    
    @Override
    public <_PN extends _tree._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, List<ImportDeclaration> left, List<ImportDeclaration> right) {
        Set<ImportDeclaration> ls = new HashSet<>();
        Set<ImportDeclaration> rs = new HashSet<>();
        Set<ImportDeclaration> both = new HashSet<>();
        ls.addAll(left);
        rs.addAll(right);

        both.addAll(left);
        both.retainAll(right);

        ls.removeAll(both);
        rs.removeAll(both);

        ls.forEach(c -> dt.addDiff(new _leftOnly_import(
                path.in(Feature.IMPORT),
                (_type) _leftParent,
                (_type) _rightParent, c)));

        rs.forEach(c -> dt.addDiff(new _rightOnly_import(
                path.in(Feature.IMPORT),
                (_type) _leftParent,
                (_type) _rightParent, c)));

        return dt;
    }

    public static class _rightOnly_import implements _diffNode<_type>, _diffNode._rightOnly<ImportDeclaration> {

        public _nodePath path;
        public _type leftParent;
        public _type rightParent;
        public ImportDeclaration right;

        public _rightOnly_import(_nodePath path, _type leftParent, _type rightParent, ImportDeclaration right) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.right = Ast.importDeclaration(right.toString());
        }

        @Override
        public _type leftParent() {
            return leftParent;
        }

        @Override
        public _type rightParent() {
            return rightParent;
        }

        @Override
        public void patchLeftToRight() {
            leftParent.removeImports(right);
            rightParent.removeImports(right);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.addImports(right);
            rightParent.addImports(right);
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public ImportDeclaration right() {
            return right;
        }

        @Override
        public String toString() {
            return "   + " + path;
        }

    }

    public static class _leftOnly_import implements _diffNode<_type>, _diffNode._leftOnly<ImportDeclaration> {

        public _nodePath path;
        public _type leftParent;
        public _type rightParent;
        public ImportDeclaration left;

        public _leftOnly_import(_nodePath path, _type leftParent, _type rightParent, ImportDeclaration left) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.left = Ast.importDeclaration(left.toString());
        }

        @Override
        public _type leftParent() {
            return leftParent;
        }

        @Override
        public _type rightParent() {
            return rightParent;
        }

        @Override
        public void patchLeftToRight() {
            leftParent.addImports(left);
            rightParent.addImports(left);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.removeImports(left);
            rightParent.removeImports(left);
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public ImportDeclaration left() {
            return left;
        }

        @Override
        public String toString() {
            return "   - " + path;
        }
    }
}
