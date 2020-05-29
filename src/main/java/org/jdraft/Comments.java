package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public enum Comments {
    ;

    /**
     * @param <C>                the comment class
     * @param astRootNode        the root node to start the search
     * @param commentTargetClass the TYPE of comment ({@link com.github.javaparser.ast.comments.Comment},
     *                           {@link LineComment}, {@link JavadocComment}, {@link BlockComment})
     * @param commentMatchFn     predicate for selecting comments
     * @return a list of matching comments
     */
    public static <C extends com.github.javaparser.ast.comments.Comment> List<C> list(
            Node astRootNode, Class<C> commentTargetClass, Predicate<C> commentMatchFn) {

        List<C> found = new ArrayList<>();
        forEachIn(astRootNode, c -> {
            if (commentTargetClass.isAssignableFrom(c.getClass())) {
                C cc = (C) c;
                if (commentMatchFn.test(cc)) {
                    found.add(cc);
                }
            }
        });
        return found;
    }

    /**
     * Given an AST node, walk check its comments (in order)
     *
     * @param astRootNode
     * @param commentActionFn
     */
    public static void forEachIn(Node astRootNode, Consumer<com.github.javaparser.ast.comments.Comment> commentActionFn) {
        forEachIn(astRootNode, t -> true, commentActionFn);
    }

    /**
     *
     * @param <C>
     * @param astRootNode
     * @param commentClass
     * @param commentMatchFn
     * @param commentActionFn
     */
    public static <C extends com.github.javaparser.ast.comments.Comment> void forEachIn(
            Node astRootNode, Class<C> commentClass, Predicate<C> commentMatchFn, Consumer<C> commentActionFn) {
        if (astRootNode instanceof NodeWithJavadoc && commentClass == JavadocComment.class) {
            NodeWithJavadoc nwj = (NodeWithJavadoc) astRootNode;
            if (nwj.getJavadocComment().isPresent()) {
                if (commentMatchFn.test((C) nwj.getJavadocComment().get())) {
                    commentActionFn.accept((C) nwj.getJavadocComment().get());
                }
            }
        }
        List<com.github.javaparser.ast.comments.Comment> acs = astRootNode.getAllContainedComments();

        Collections.sort(acs, new CommentPositionComparator());
        LinkedHashSet<com.github.javaparser.ast.comments.Comment> lhs = new LinkedHashSet<>();
        lhs.addAll(acs);

        lhs.stream().filter(c ->
                commentClass.isAssignableFrom(c.getClass())
                        && commentMatchFn.test((C) c))
                .forEach(c -> commentActionFn.accept((C) c));
    }

    /**
     * @param astRootNode
     * @param commentMatchFn
     * @param commentActionFn
     */
    public static void forEachIn(
            Node astRootNode, Predicate<com.github.javaparser.ast.comments.Comment> commentMatchFn, Consumer<com.github.javaparser.ast.comments.Comment> commentActionFn) {

        if (astRootNode == null) {
            return;
        }
        if (astRootNode instanceof NodeWithJavadoc) {
            NodeWithJavadoc nwj = (NodeWithJavadoc) astRootNode;
            if (nwj.getJavadocComment().isPresent()) {
                if (commentMatchFn.test((com.github.javaparser.ast.comments.Comment) nwj.getJavadocComment().get())) {
                    commentActionFn.accept((com.github.javaparser.ast.comments.Comment) nwj.getJavadocComment().get());
                }
            }
        }
        List<com.github.javaparser.ast.comments.Comment> acs = astRootNode.getAllContainedComments();
        List<com.github.javaparser.ast.comments.Comment> ocs = astRootNode.getOrphanComments();
        acs.addAll(ocs);

        Collections.sort(acs, new CommentPositionComparator());
        LinkedHashSet<com.github.javaparser.ast.comments.Comment> lhs = new LinkedHashSet<>();
        lhs.addAll(acs);
        lhs.stream()
                .filter(commentMatchFn).forEach(commentActionFn);
    }

    /**
     *
     * @param comment
     * @param statements
     * @return
     */
    public static BlockStmt replace(com.github.javaparser.ast.comments.Comment comment, Statement...statements){
        Optional<Node> parent = comment.getParentNode();
        if( parent.isPresent() ){
            return replace(parent.get(), comment, statements);
        }
        return null;
    }

    /**
     * Replace some Comment within the Java code with a Statement
     * The comment has to exist within a BlockStmt (a MethodDeclaration, ConstructorDeclaration, InitializerBlock)
     * to work
     * @param comment
     * @param statements
     * @return the BlockStmt that was updated
     */
    public static BlockStmt replace(Node parent, com.github.javaparser.ast.comments.Comment comment, Statement...statements){
        BlockStmt bs = At.blockAt(parent, comment.getRange().get().begin);
        if( bs != null ){
            int statementIndex = At.getStatementIndex(bs, comment.getRange().get().begin);
            for(int i=0;i<statements.length;i++) {
                bs.addStatement(statementIndex+i, statements[i].clone());
            }
            comment.remove();
            return bs;
        }
        return null;
    }

    /**
     * list all comments within this astRootNode (including the comment applied
     * to the astRootNode if the AstRootNode is an instance of {@link NodeWithJavadoc}
     *
     * @param astRootNode the root node to look through
     * @return a list of all comments on or underneath the node
     */
    public static List<com.github.javaparser.ast.comments.Comment> list(Node astRootNode) {
        List<com.github.javaparser.ast.comments.Comment> found = new ArrayList<>();
        forEachIn(astRootNode, c -> found.add(c));
        return found;
    }

    /**
     * list all comments within this astRootNode that match the predicate
     * (including the comment applied to the astRootNode if the AstRootNode is
     * an instance of {@link NodeWithJavadoc})
     *
     * @param astRootNode    the root node to look through
     * @param commentMatchFn matching function for comments
     * @return a list of all comments on or underneath the node
     */
    public static List<com.github.javaparser.ast.comments.Comment> list(Node astRootNode, Predicate<com.github.javaparser.ast.comments.Comment> commentMatchFn) {
        List<com.github.javaparser.ast.comments.Comment> found = new ArrayList<>();
        forEachIn(astRootNode, c -> {
            if (commentMatchFn.test(c)) {
                found.add(c);
            }
        });
        return found;
    }

    /**
     * Get the content inside the comments... meaning no prefixes // line
     * comments are easy, just remove the // prefix /* multi- line block
     * comments and JAVADOC comments * can be difficult becuase they start with
     * * on subsequent lines * /
     *
     * @param astComment
     * @return
     */
    public static String getContent(com.github.javaparser.ast.comments.Comment astComment) {
        return astComment.toString(Print.PRINT_RAW_COMMENTS);
    }

    /**
     * Comparator for Comments within an AST node that organizes based on the
     * start position
     */
    public static class CommentPositionComparator implements Comparator<com.github.javaparser.ast.comments.Comment> {

        @Override
        public int compare(com.github.javaparser.ast.comments.Comment o1, com.github.javaparser.ast.comments.Comment o2) {
            if (o1.getBegin().isPresent() && o2.getBegin().isPresent()) {
                return o1.getBegin().get().compareTo(o2.getBegin().get());
            }
            //if one or the other doesnt have a begin
            // put the one WITHOUT a being BEFORE the other
            // if neither have a being, return
            if (!o1.getBegin().isPresent() && !o2.getBegin().isPresent()) {
                return 0;
            }
            if (o1.getBegin().isPresent()) {
                return -1;
            }
            return 1;
        }
    }
    public static class Classes{
        public static final Class<LineComment> LINE_COMMENT = LineComment.class;
        public static final Class<BlockComment> BLOCK_COMMENT = BlockComment.class;
        public static final Class<JavadocComment> JAVADOC_COMMENT = JavadocComment.class;
    }
}
