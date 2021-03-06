package org.jdraft;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.utils.Log;
import org.jdraft.walk.Walk;

import java.util.*;

/**
 * Ways of resolving individual {@link com.github.javaparser.ast.expr.Expression}s, {@link com.github.javaparser.ast.stmt.Statement},
 * {@link BlockStmt}s, {@link BodyDeclaration}s / {@link _java._member}s, etc. based on Position (line, column)
 * within a parsed {@link Node} / {@link _java._domain} object
 *
 * @author Eric
 */
public interface At {
    //;

    /**
     * Singleton instance for comparing AST nodes by
     */
    public static NodeStartPositionComparator COMPARE_NODE_BY_POSITION =
        new NodeStartPositionComparator();

    /*--------------------- LOCATION-BASED RESOLVING IN AST ---------------------------*/
    /**
     * Given a class and a line/column find the "most specific" source code node at this
     * (line:column) cursor position
     *
     * @param clazz the runtime class
     * @param line the line of the source code
     * @return an AST node or null if the position is not within the Clazzes source code
     */
    public static <N extends Node> N nodeAt(Class clazz, int line) {
        return nodeAt( Ast.of(clazz), line);
    }

    /**
     * Given a class and a line/column find the "most specific" node at this (line) cursor position
     *
     * @param top the top node to search through
     * @param line the line of the source code
     * @return an AST node or null if the position is not within the Node
     */
    public static <N extends Node> N nodeAt(Node top, int line){
        final int l = Math.max( Math.abs(line), 1);
        Range r = new Range(new Position(l, 0), new Position(l, Integer.MAX_VALUE -1000));

        List<Node> ns = top.findAll(Node.class, (Node n)-> n.getRange().isPresent()
                && (n.getRange().get().overlapsWith( r ) || r.overlapsWith( n.getRange().get() )) );
        if( ns.isEmpty() ){
            Log.error("None found at : %s ", ()-> l);
            return null;
        }
        //the last node is the "most specific node", but lets climb up (parents)
        //to find a parent that is on the start of the line that contains the small node

        Node theLast = ns.get(ns.size() -1);
        Node theFullLineNode  = theLast;
        boolean done = false;

        while( theFullLineNode.getParentNode().isPresent() && ! done ){
            Node par = theFullLineNode.getParentNode().get();
            if( par.getRange().isPresent() && (! (par instanceof CompilationUnit))){
                if( par.getRange().get().begin.line == line && par.getRange().get().end.line == line){
                    theFullLineNode = par;
                } else{
                    done = true;
                }
            } else{
                done = true;
            }
        }
        return (N)theFullLineNode;
    }

    /**
     * Given a class and a line/column find the "most specific" source code node at this (line:column)
     * cursor position
     *
     * @param clazz the runtime class
     * @param line the line of the source code
     * @param column the column cursor position within the source code
     * @return an AST node or null if the position is not within the Clazzes source code
     */
    public static <N extends Node> N nodeAt(Class clazz, int line, int column) {
        line = Math.max( Math.abs(line), 1);
        return nodeAt( Ast.of(clazz), line, column);
    }

    /**
     * Given top level node to return the most "specific" node that contains the (line:column)
     * cursor position
     * @param top the Top level Ast node to look through
     * @param line the line cursor position
     * @param column the column cursor position
     * @return an AST node or null if the position is not within the top node
     */
    public static <N extends Node> N nodeAt(Node top, int line, int column){
        Position p = new Position(Math.max( Math.abs(line), 1), column);
        List<Node> found = new ArrayList<>();
        top.walk( n-> {
            if( n.getRange().isPresent() && n.getRange().get().contains(p)){
                if( found.isEmpty() ){
                    found.add( n );
                } else{
                    if( found.get(0).containsWithinRange(n) ){
                        found.clear(); //remove the old one
                        found.add(n); //add the new one
                    }
                }
            }
        } );
        if( found.isEmpty() ){
            return null;
        }
        return (N)found.get(0);
    }

    /**
     * Return the "most specific"  {@link BodyDeclaration} instance that contains the position
     *
     * @see TypeDeclaration
     * @see EnumDeclaration
     * @see ClassOrInterfaceDeclaration
     * @see AnnotationDeclaration
     * @see MethodDeclaration
     * @see FieldDeclaration
     * @see InitializerDeclaration
     * @see EnumConstantDeclaration
     * @see AnnotationMemberDeclaration
     *
     * @param clazz the runtime Class to look through( the source of course)
     * @param pos the position
     * @param <M> the type of BodyDeclaration
     * @return the bodyDeclaration encompassing this position or null if out of range
     */
    public static <M extends BodyDeclaration> M memberAt(Class clazz, Position pos) {
        return memberAt(clazz, pos.line, pos.column);
    }

    /**
     * return a member (method, field, constructor, staticBlock)
     *
     * @see TypeDeclaration
     * @see EnumDeclaration
     * @see ClassOrInterfaceDeclaration
     * @see AnnotationDeclaration
     * @see MethodDeclaration
     * @see FieldDeclaration
     * @see InitializerDeclaration
     * @see EnumConstantDeclaration
     * @see AnnotationMemberDeclaration
     *
     * @param clazz
     * @param line
     * @param column
     * @param <M>
     * @return
     */
    public static <M extends BodyDeclaration> M memberAt(Class clazz, int line, int column) {
        line = Math.max( Math.abs(line), 1); //make sure non negative line number (also non zero)
        return memberAt( Ast.of(clazz), Math.max( line, 1), column);
    }

    /**
     * Return the "most specific"  {@link BodyDeclaration} instance that contains the position
     *
     * @see TypeDeclaration
     * @see EnumDeclaration
     * @see ClassOrInterfaceDeclaration
     * @see AnnotationDeclaration
     * @see MethodDeclaration
     * @see FieldDeclaration
     * @see InitializerDeclaration
     * @see EnumConstantDeclaration
     * @see AnnotationMemberDeclaration
     *
     * @param top the top level node to look through (NOTE: does NOT have to be the CompilationUnit)
     * @param pos the position
     * @param <M> the type of BodyDeclaration
     * @return the bodyDeclaration encompassing this position or null if out of range
     */
    public static <M extends BodyDeclaration> M memberAt(Node top, Position pos) {
        return memberAt(top, pos.line, pos.column);
    }

    /**
     * Finds the most specific {@link BodyDeclaration} member containing this
     * position and returns it (or null if the position is outside for range)
     *
     * @see TypeDeclaration
     * @see EnumDeclaration
     * @see ClassOrInterfaceDeclaration
     * @see AnnotationDeclaration
     * @see MethodDeclaration
     * @see FieldDeclaration
     * @see InitializerDeclaration
     * @see EnumConstantDeclaration
     * @see AnnotationMemberDeclaration
     *
     * @param top
     * @param line
     * @param column
     * @param <M>
     * @return
     */
    public static <M extends BodyDeclaration> M memberAt(Node top, int line, int column) {
        line = Math.max( Math.abs(line), 1);
        Node n = nodeAt( top, line, column);
        if( n == null ){
            return null;
        }
        if( n instanceof BodyDeclaration) {
            return (M)n;
        }
        return (M) n.stream(Node.TreeTraversal.PARENTS).filter( p -> p instanceof BodyDeclaration).findFirst().get();
    }

    /**
     * Finds the most specific {@link BodyDeclaration} member containing this
     * position and returns it (or null if the position is outside for range)
     *
     * @see TypeDeclaration
     * @see EnumDeclaration
     * @see ClassOrInterfaceDeclaration
     * @see AnnotationDeclaration
     * @see MethodDeclaration
     * @see FieldDeclaration
     * @see InitializerDeclaration
     * @see EnumConstantDeclaration
     * @see AnnotationMemberDeclaration
     *
     * @param clazz
     * @param line
     * @param <M>
     * @return
     */
    public static <M extends BodyDeclaration> M memberAt(Class clazz, int line ) {
        return memberAt( Ast.of(clazz), line);
    }

    /**
     * Finds the most specific {@link BodyDeclaration} member containing this
     * line position and returns it (or null if the position is outside for range)
     *
     * @see TypeDeclaration
     * @see EnumDeclaration
     * @see ClassOrInterfaceDeclaration
     * @see AnnotationDeclaration
     * @see MethodDeclaration
     * @see FieldDeclaration
     * @see InitializerDeclaration
     * @see EnumConstantDeclaration
     * @see AnnotationMemberDeclaration
     *
     * @param top the top node
     * @param line a 1-based line number (we start with line 1)
     * @param <M> a BodyDeclaration (Member)
     * @return an instance of a BodyDeclaration AST Node (or null if not found)
     */
    public static <M extends BodyDeclaration> M memberAt(Node top, int line ) {
        //we cant have negative or 0 line numbers
        final int l = Math.max( Math.abs(line), 1);//if it's 0, we "really mean" 1
        Node n = nodeAt( top, line);
        if( n == null ){
            Log.info("Line number : %s is outside of member %s ", ()->l, ()->top.getClass() );
            return null;
        }
        if( n instanceof BodyDeclaration) {
            Log.info("Found member %s containing node %s at line : %s ", ()->n.getClass(), ()->n.getClass(), ()->l );
            return (M)n;
        }
        Optional<Node> on = n.stream(Node.TreeTraversal.PARENTS).filter(p -> p instanceof BodyDeclaration).findFirst();
        if( on.isPresent() ){
            Log.info("Found member %s containing node %s at line : %s ", ()->on.get().getClass(), ()->n.getClass(), ()->l );
            return (M) on.get();
        }
        Log.info("No Parent Member found containing Node %s at line : %s ", ()->n.getClass(), ()->l );
        return null;
    }

    /**
     * return the most specific member ({@link _method}, {@link _field}, {@link _constructor},
     * {@link _initBlock}...) that is at of or contains the line number
     *
     * @see _type
     * @see _enum
     * @see _class
     * @see _interface
     * @see _annotation
     * @see _method
     * @see _field
     * @see _initBlock
     * @see _constant
     * @see _annoMember
     *
     * @param clazz
     * @param line
     * @param column
     * @param <_M>
     * @return
     */
    public static <_M extends _java._member> _M  _memberAt(Class clazz, int line, int column) {
        return _memberAt( Ast.of(clazz), Math.max( line, 1), column);
    }

    /**
     * return the most specific member ({@link _method}, {@link _field}, {@link _constructor},
     * {@link _initBlock}...) that is at of or contains the line number
     *
     * @param model
     * @param line
     * @param column
     * @param <_M>
     * @return
     */
    public static <_M extends _java._member> _M  _memberAt(_java._member model, int line, int column) {
        return _memberAt(model.node(), line, column);
    }

    /**
     * return the most specific member ({@link _method}, {@link _field}, {@link _constructor},
     * {@link _initBlock}...) that is at of or contains the line number
     *
     * @see _type
     * @see _enum
     * @see _class
     * @see _interface
     * @see _annotation
     * @see _method
     * @see _field
     * @see _initBlock
     * @see _constant
     * @see _annoMember
     *
     * @param top the top node
     * @param line a 1-based line number (we start with line 1)
     * @param column the column within the file
     * @param <_M> a _member implementation
     * @return
     */
    public static <_M extends _java._member> _M  _memberAt(Node top, int line, int column) {
        BodyDeclaration astM = At.memberAt(top, line, column);
        if( astM == null ){
            return null;
        }
        return (_M)_java.of(astM);
    }

    /**
     * return the most specific member ({@link _method}, {@link _field}, {@link _constructor},
     * {@link _initBlock}...) that is at of or contains the line number
     *
     * @see _type
     * @see _enum
     * @see _class
     * @see _interface
     * @see _annotation
     * @see _method
     * @see _field
     * @see _initBlock
     * @see _constant
     * @see _annoMember
     *
     * @param clazz the runtime Class
     * @param line a 1-based line number (we start with line 1)
     * @param <_M> a _member implementation
     * @return
     */
    public static <_M extends _java._member> _M _memberAt(Class clazz, int line ) {
        return _memberAt( Ast.of(clazz), line);
    }

    /**
     * return the most specific member ({@link _method}, {@link _field}, {@link _constructor},
     * {@link _initBlock}...) that is at of or contains the line number
     *
     * @see _type
     * @see _enum
     * @see _class
     * @see _interface
     * @see _annotation
     * @see _method
     * @see _field
     * @see _initBlock
     * @see _constant
     * @see _annoMember
     *
     * @param _mem
     * @param line
     * @param <_M>
     * @return
     */
    public static <_M extends _java._member> _M  _memberAt(_java._member _mem, int line ) {
        return _memberAt(_mem.node(), line );
    }

    /**
     * finds the closest/most specific _java _member CONTAINING this position and
     * returns it (or null if the position is outside for range)
     *
     * @see _type
     * @see _enum
     * @see _class
     * @see _interface
     * @see _annotation
     * @see _method
     * @see _field
     * @see _initBlock
     * @see _constant
     * @see _annoMember
     *
     * @param top the top node
     * @param line a 1-based line number (we start with line 1)
     * @param <_M> a _member implementation
     * @return an instance of a BodyDeclaration AST Node (or null if not found)
     */
    public static <_M extends _java._member> _M _memberAt(Node top, int line ) {
        BodyDeclaration astM = At.memberAt(top, line);
        if( astM == null ){
            return null;
        }
        return (_M)_java.of(astM);
    }

    /**
     * Given a BlockStmt and a position (row/column) figure out the Statement Index
     * 0...N where the position is
     *
     * @see Ast#reparse(CompilationUnit)
     * @param bs
     * @param pos
     * @return
     */
    public static int getStatementIndex(BlockStmt bs, Position pos){
        List<Statement> st = bs.getStatements();
        if(st.isEmpty() ){
            return 0;
        }
        int index = 0;
        for(int i=0;i<st.size();i++){
            if( st.get(i).getRange().get().isAfter(pos) ){
                return index;
            }
            index++;
        }
        return index;
    }

    /**
     * find the block at the Start Position
     * @param container
     * @param startPosition
     * @return the BlockStmt or null
     */
    public static BlockStmt blockAt(Node container, Position startPosition ){
        if( !container.getRange().get().contains( startPosition ) ){
            //System.out.println("Not contained");
            return null;
        }
        Node found = At.nodeAt(container, startPosition.line, startPosition.column);
        if( found instanceof BlockStmt ){
            return ((BlockStmt) found).asBlockStmt();
        }
        Optional<Node> on = found.stream(Walk.PARENTS).filter(n -> BlockStmt.class.isAssignableFrom(n.getClass())).findFirst();
        if( on.isPresent()){
            return (BlockStmt)on.get();
        }
        return null;
    }

    /**
     * Sorts a list of nodes by the position:
     * HUGE CAVEAT: Asts that have been modified often will have nodes out of
     * order, in this case it is best to serialize the AST to a String then read it
     * back in to ensure the node positions are parsed and maintained.
     *
     * @param <N>
     * @param unsorted
     * @return
     */
    public static <N extends Node> List<N> sortNodesByPosition(List<N> unsorted){
        Collections.sort(unsorted, COMPARE_NODE_BY_POSITION);
        return unsorted;
    }

    /**
     * Comparator for Nodes within an AST node that organizes based on the
     * start position.
     */
    public static class NodeStartPositionComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            if (o1.getBegin().isPresent() && o2.getBegin().isPresent()) {
                int comp = o1.getBegin().get().compareTo(o2.getBegin().get());
                if( comp != 0 ){
                    return comp;
                }
                int comp2 = o1.getEnd().get().compareTo(o2.getEnd().get());
                return comp2;
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
}
