package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;

/**
 * A member within the body of a Class (something defined in the  { }) including {@link _initBlock}s.
 * All _{@link _member}s are {@link _node}s (they are represented by BOTH a meta-representation i.e. {@link _method},
 * and an AST representation {@link com.github.javaparser.ast.body.MethodDeclaration}.
 *
 * {@link _initBlock} IS a {@link _member}, BUT IS NOT a {@link _declared}, because even though
 * {@link _initBlock} is defined within the context of a Class, it is not named/reachable/callable or "declared"
 * and referenced outside of the class where it is defined.
 * <UL>
 * <LI>{@link _initBlock} {@link com.github.javaparser.ast.body.InitializerDeclaration}
 * <LI>{@link _field} {@link com.github.javaparser.ast.body.FieldDeclaration}
 * <LI>{@link _constructor} {@link com.github.javaparser.ast.body.ConstructorDeclaration}
 * <LI>{@link _method} {@link com.github.javaparser.ast.body.MethodDeclaration}
 * <LI>{@link _enum._constant} {@link com.github.javaparser.ast.body.EnumConstantDeclaration}
 * <LI>{@link _annotation._element} {@link com.github.javaparser.ast.body.AnnotationMemberDeclaration}
 * <LI>{@link _type} {@link com.github.javaparser.ast.body.TypeDeclaration}
 * <LI>{@link _class} {@link com.github.javaparser.ast.body.ClassOrInterfaceDeclaration}
 * <LI>{@link _enum} {@link com.github.javaparser.ast.body.EnumDeclaration}
 * <LI>{@link _interface} {@link com.github.javaparser.ast.body.ClassOrInterfaceDeclaration}
 * <LI>{@link _annotation} {@link com.github.javaparser.ast.body.AnnotationDeclaration}
 * </UL>
 *
 * @param <N> the Ast Node instance type
 * @param <_N> the _draft instance type
 * @see _declared (an EXTENSION of {@link _member}s that are also {@link _named}...(all {@link _member}s are
 * {@link _declared}s, ACCEPT {@link _initBlock} which is ONLY a {@link _member}
 */
public interface _member <N extends Node, _N extends _node>
        extends _node<N, _N> {

    /**
     * Returns the parent _member for this _member (if it exists)
     * (traverses up through the parents to the
     *
     * for example:
     * _class _c = _class.of("C", new Object(){
     *     int x,y;
     * });
     *
     * assertEquals(_c, _c.getField(i).getParentMember());
     *
     * @param <_M>
     * @return
     */
    default <_M extends _member> _M getParentMember(){
        if(this instanceof _field){
            _field _f = (_field)this;
            FieldDeclaration fd = _f.getFieldDeclaration();
            if( fd == null ){
                return null;
            }
            BodyDeclaration bd = _walk.first(_walk.PARENTS, fd, BodyDeclaration.class);
            if( bd != null ) {
                return (_M) _java.of(bd);
            }
            return null; //we didnt find a parent that was a BodyDeclaration
        } else{
            BodyDeclaration bd = _walk.first(_walk.PARENTS, ast(), BodyDeclaration.class);
            if( bd != null ) {
                return (_M) _java.of(bd);
            }
            return null; //we didnt find a parent that was a BodyDeclaration
        }
    }
}
