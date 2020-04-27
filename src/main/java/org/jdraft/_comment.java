package org.jdraft;

import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * interface for Java comment types
 *
 * @see _lineComment
 * @see _blockComment
 * @see _javadocComment for a javadoc attributed to a AST node (i.e. must start with / * * end with * / AND
 * be
 */
public interface _comment<C extends Comment, _C extends _comment> extends _java._node<C, _C>, _java._withText<_C> {

    static <_C extends _comment> _C of( String... comm ){
        String str = Text.combine(comm);
        if( str.startsWith("//")){
            return (_C)_lineComment.of(str);
        }
        if( str.startsWith("/**") ){
            return (_C)_blockComment.of(str);
        }
        return (_C)_javadocComment.of(str);
    }

    static <_C extends _comment> _C of( Comment comment ){
        if( comment instanceof JavadocComment ){
            return (_C)_javadocComment.of( (JavadocComment)comment);
        }
        if( comment instanceof LineComment ){
            return (_C)_lineComment.of( (LineComment)comment);
        }
        return (_C)_blockComment.of( (BlockComment)comment);
    }

    /**
     * _node that is attributed to this comment
     * i.e.
     *  class C{
     *     void m(){
     *         // print just in case
     *         System.out.println("id "+ id);
     *     }
     * }
     *
     * $comment.of("// print just in case")
     * is attributed to the _expressionStmt System.out.println("id "+ id);
     *
     * @return the commented node (or null if the comment is an orphaned node)
     */
    _java._node getAttributedNode();

    /**
     * Is there attribution to an AST node for this
     * @return
     */
    default boolean isAttributed(){
        return getAttributedNode() != null;
    }

    /**
     * Are the contents of this Comment equal to the String provided
     * @param contents the full contents of the String
     * @return
     */
    default boolean isText(String contents ){
        return Objects.equals( this.getText(), contents);
    }

    /**
     * Gets the contents of the Javadoc as a single String
     * @return
     */
    default String getText(){
        return Comments.getContent(ast());
    }

    /**
     *
     * @param textStencil
     * @return
     */
    default boolean contains( CharSequence textStencil ){
        return contains(Stencil.of(textStencil) );
    }

    /**
     * Can we find an instanceof the the stencil pattern anywhere in the code?
     * @param stencil the stencil to match within the javadoc contents
     * @return true if this Stencil pattern is found, false otherwise
     */
    default boolean contains( Stencil stencil ){
        return parseFirst(stencil) != null;
    }

    /**
     * create a Stencil from the stencilText, then look for the Stencil inside the text matching
     * and extracting the first instance found into tokens and returning them.
     * Return null if no instance of the text is found
     *
     * @param stencilText text to build a Stencil to match against
     * @return Tokens of the first Stencil match, or null if no match
     */
    default Tokens parseFirst(String stencilText ){
        return parseFirst(Stencil.of(stencilText));
    }

    /**
     *
     * @param stencil
     * @return
     */
    default Tokens parseFirst(Stencil stencil ){
        return stencil.parseFirst(getText());
    }

    /**
     * Returns the content as a list of Strings (one for each line)
     * @return
     */
    default List<String> listContents(){
        return Text.lines( getText() );
    }

    /**
     * Is this comment NOT attributed to a given Ast Node?
     * @return whether this comment is orphaned( Not attributed to an AST node)
     */
    default boolean isOrphaned(){
        return this.ast().isOrphan();
    }

    /**
     * Is this comment attributed to a Member
     * _method, _field, _class, _constructor?

    default boolean isOnMember(){
        return !this.isOrphaned() && this.getCommentedNode() instanceof _java._member;
    }
     */

    /**
     * Is the comment attributed to any of these _node classes
     * @param nodeClasses
     * @return true if the comment is attributed to
     */
    default boolean isAttributedTo(Class<? extends _java._node>... nodeClasses){
        if( this.getAttributedNode() != null){
            Arrays.stream( nodeClasses ).anyMatch( c-> c.isAssignableFrom(this.getAttributedNode().getClass()));
        }
        return false;
    }

    /**
     * Sets the entire contents of the comment
     * @param contents
     * @return
     */
    default _C setText( String...contents ){
        return setText( STANDARD_STYLE, Text.combine(contents) );
    }

    /**
     * Sets the contents of the comment
     * @param contents
     * @return
     */
    default _C setText(String contents){
        return setText( STANDARD_STYLE, contents);
    }

    /**
     *
     * @param style
     * @param contents
     * @return
     */
    default _C setText(_style style, String...contents){
        return setText( style, Text.combine(contents) );
    }

    /**
     * Because {@link _blockComment}s/ {@link _javadocComment}s can span multiple lines
     *  / **
     *    * contents
     *    * second line
     *    * /
     *  the "actual contents" within the block contents should "really" only be (2) lines :
     *  [0] "contents"
     *  [1] "second line"
     *
     *  (or a single String "contents\nsecond line", because the prefix spaces, tabs and *'s are
     *   not really relevant contents of the comment)
     *
     *   alternatively, just "getContents()" returns:
     *   [0] ""
     *   [1] "   * contents"
     *   [2] "   * second line"
     *   [3] "  "
     *
     * ...this will return what is meant to be Content
     * (and not extraneous spaces, tabs and *s to signify a comment line)
     * @return
     */
    default _C setText(_style style, String contents){
        if( this.ast().getRange().isPresent()){
            //it's already attached, So I need to infer what indentation I need
            int column = Math.max( (this.ast().getRange().get().begin.column) -1, 0);

            String indent = "";
            for(int i=0;i<column;i++){
                indent += " "; //or tabs??
            }
            String cont = _comment.formatContents(style, indent, contents);
            ast().setContent( cont );
            return (_C)this;
        }
        ast().setContent( _comment.formatContents(style, "", contents) );
        return (_C)this;
    }

    /*
     * multi
     * line
     * content
     */
    _style STANDARD_STYLE = new _style();

    /* multi
     * line
     * content
     */
    _style FIRST_LINE_STYLE =
            new _style(false, 1, true, true);

    /*
      multi
      line
      comment
     */
    _style OPEN_STYLE =
            new _style(true, 1, false, true);

    /* multi
       line
       comment */
    _style COMPACT_OPEN_STYLE =
            new _style(false, 1, false, false);

    /*multi
      line
      comment*/
    _style ULTRA_COMPACT_STYLE =
            new _style(false, 0, false, false);

    /**
     * A Style to apply to the raw string contents of comments
     * (i.e. typically applied to multi-line {@link _blockComment} and {@link _javadocComment})
     *
     * this is never represented in the actual AST, but rather exists to add some Quality of Life
     * improvements to updating comments within the AST
     */
    class _style {

        /** IF the comment contents is more than one line, does the contents start on the first line */
        public boolean skipContentsOnFirstLine = true;

        /** The number of spaces between the star and content / *_    _* / (the _'s here represent 1 space) */
        public int starPadding = 1;

        /** Prefix multiple line contents with the "*" ? */
        public boolean starPrefixAllLines = true;

        /**
         * does the closing * / tag appear on separate line (aligned in the left gutter)?
         */
        public boolean alignCloseTagOnLeft = true; //

        public _style(){}

        public _style(boolean skipContentsOnFirstLine, int starPadding,
                      boolean starPrefixAllLines, boolean alignCloseTagOnLeft){
            this.skipContentsOnFirstLine = skipContentsOnFirstLine;
            this.starPadding = starPadding;
            this.starPrefixAllLines = starPrefixAllLines;
            this.alignCloseTagOnLeft = alignCloseTagOnLeft;
        }
    }

    /**
     *
     * @param style
     * @param contents
     * @return
     */
    static String formatContents(_style style, String contents){
        return formatContents(style, "", contents);
    }

    /**
     * remove the preceding / * and the ending * / and create the contents
     * @param style
     * @param indent the indention of the tag (this indention is only applied
     * @param rawContents
     * @return
     */
    static String formatContents(_style style, String indent, String rawContents){
        List<String> ls = Text.lines(rawContents);
        if( ls.size() == 1 ){
            String trim = rawContents.trim();
            if( trim.startsWith("/*") ){
                rawContents = trim.substring(2).trim();
            }
            if( rawContents.endsWith("*/")){
                rawContents = rawContents.substring(0, rawContents.length() -2);
            }
            for(int i=0;i<style.starPadding; i++){
                rawContents = " "+rawContents+" ";
            }

            return rawContents;
        }
        //there are more than 1 line, here we need to "play" with the indent
        StringBuilder sb = new StringBuilder();
        if( style.skipContentsOnFirstLine ){
            ls.add(0, "");
        }
        for(int i=0;i<ls.size();i++){
            if( i == 0 ){
                for(int j = 0; j<style.starPadding; j++){
                    sb.append(" ");
                }
                sb.append(ls.get(0)).append(System.lineSeparator());
            }
            else {
                sb.append(indent);
                if( style.starPrefixAllLines){
                    sb.append(" *");
                }
                else{
                    sb.append("  ");
                }
                for(int j = 0; j<style.starPadding; j++){
                    sb.append(" ");
                }

            }
            if( i == ls.size() - 1 ){ //last line
                //check if the last line
                if( style.alignCloseTagOnLeft ){
                    if( ls.get(i).equals(System.lineSeparator()) ){
                        //the last line is a blank line, so DONT add it
                        sb.append(System.lineSeparator()); //keep the blank line
                        sb.append(indent);//add an indent
                    } else{
                        sb.append(ls.get(i)).append(System.lineSeparator());
                        sb.append(indent);
                    }
                } else{
                    if( i > 0 ) {
                        sb.append(ls.get(i)); //.append(System.lineSeparator());
                        for(int j = 0; j<style.starPadding; j++){
                            sb.append(" ");
                        }
                    }
                }
            } else{ //just
                if( i > 0 ) {
                    sb.append(ls.get(i)).append(System.lineSeparator());
                }
            }
        }
        if( style.alignCloseTagOnLeft ){
            sb.append(" "); //because the start tag has a / * (the slash before the *) the close tag on the last line needs indent 1
        }
        return sb.toString();
    }


    /**
     * Look for matches to matchStencil in the contents of the comment and replace with the replaceStencil
     * for example:
     * <PRE>
     * _comment _c = _comment.of("//<code>System.out.println(getValue());</code>");
     * _c.matchReplace("<code>$content$</code>", "{@code $content$}");
     *
     * //verify we matched the old <code></code> tags to {@code}
     * assertEquals( "{@code System.out.println}", _c.getContents());
     * </PRE>
     * @param matchStencil stencil for matching input pattern
     * @param replaceStencil stencil for drafting the replacement
     * @return
     */
    default _C matchReplace(String matchStencil, String replaceStencil){
        return matchReplace(Stencil.of(matchStencil), Stencil.of(replaceStencil));
    }

    /**
     * Look for matches to matchStencil in the contents of the comment and replace with the replaceStencil
     * for example:
     * <PRE>
     * Stencil matchStencil = Stencil.of("<code>$content$</code>");
     * Stencil replaceStencil = Stencil.of("{@code $content$}");
     * _comment _c = _comment.of("//<code>System.out.println(getValue());</code>");
     * _c.matchReplace(matchStencil, replaceStencil);
     *
     * //verify we matched the old <code></code> tags to {@code}
     * assertEquals( "{@code System.out.println}", _c.getContents());
     * </PRE>
     * @param matchStencil stencil for matching input pattern
     * @param replaceStencil stencil for drafting the replacement
     * @return
     */
    default _C matchReplace(Stencil matchStencil, Stencil replaceStencil){
        matchStencil.getTextForm().isStartsWithBlank();

        setText(Stencil.matchReplace( getText(), matchStencil, replaceStencil));
        return (_C)this;
    }
}
