package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jdraft.*;

/**
 * prototype for different types of {@link com.github.javaparser.ast.comments.Comment}s
 *
 * @author Eric
 * @param <C> the underlying comment type
 */
public class $comment <C extends Comment>
    implements $pattern<C, $comment<C>>, Template<C>, $constructor.$part, $method.$part, $field.$part, $class.$part,
        $interface.$part, $enum.$part, $annotation.$part,$enumConstant.$part, $annotationElement.$part, $body.$part {
    
    public static $comment<Comment> of(){
        return new $comment();
    }
    
    public static<C extends Comment> $comment<C> of( String pattern, Predicate<C> constraint ){
        return (($comment<C>) new $comment(pattern)).$and(constraint);
        //return (($comment<C>) new $comment(Ast.comment(pattern))).$and(constraint);
    }

    public static $comment<Comment> of( Predicate<Comment> constraint ){
        return of().$and(constraint);
    }

    public static <C extends Comment> $comment<C> of(C comment){
        return new $comment(comment);
    }

    public static <C extends Comment> $comment<C> of( String comment ){
        return new $comment( comment);
    }

    public static <C extends Comment> $comment<C> of( String...comment ){
        return new $comment(comment);
    }

    public static $comment.Or or( Comment... _protos ){
        $comment[] arr = new $comment[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $comment.of( _protos[i]);
        }
        return or(arr);
    }

    public static $comment.Or or( $comment...$tps ){
        return new $comment.Or($tps);
    }

    /** Look for an exact match for the comment */
    public static<C extends Comment> $comment<C> as( String pattern ) {
        $comment<C> $c = of( pattern );
        $c.$and(c->$c.contentsStencil.parse(Ast.getContent( c ) ) != null);
        return $c;
    }

    /** Look for an exact match for the comment */
    public static<C extends Comment> $comment<C> as( C comment ) {
        $comment<C> $c = of( comment );
        $c.$and(c->$c.contentsStencil.parse(Ast.getContent( c ) ) != null);
        return $c;
    }

    public static $comment<JavadocComment> javadocComment(){
        $comment $c = new $comment().omitBlockComments().omitLineComments();
        $c.contentsStencil = Stencil.of("$javadoc$");
        return $c;
    }

    public static $comment<JavadocComment> javadocComment( _javadoc _jc){
        return new $comment( _jc.ast()).omitBlockComments().omitLineComments();
    }
    
    public static $comment<JavadocComment> javadocComment(String...comment){
        return new $comment(Ast.javadocComment(comment)).omitBlockComments().omitLineComments();
    }
        
    public static $comment<JavadocComment> javadocComment(String pattern, Predicate<Comment> constraint){
        return new $comment(Ast.javadocComment(pattern)).omitBlockComments().omitLineComments()
            .$and(constraint);
    }
    
    public static $comment<JavadocComment> javadocComment(Predicate<JavadocComment> constraint){
        return new $comment().omitBlockComments().omitLineComments()
            .$and(constraint);
    }
        
    public static $comment<BlockComment> blockComment(){
        return new $comment().omitJavadocComments().omitLineComments();
    }

    public static $comment<BlockComment> blockComment(String...blockComment){
        return new $comment(Ast.blockComment(blockComment)).omitJavadocComments().omitLineComments();
    }
    
    public static $comment<BlockComment> blockComment(String pattern, Predicate<BlockComment> constraint){
        return new $comment(Ast.blockComment(pattern)).omitJavadocComments().omitLineComments()
            .$and(constraint);
    }
    
    public static $comment<BlockComment> blockComment(Predicate<Comment> constraint){
        return new $comment<BlockComment>().omitJavadocComments().omitLineComments()
            .$and(constraint);
    }
    
    public static $comment<LineComment> lineComment(){
        return new $comment().omitBlockComments().omitJavadocComments();
    }

    public static $comment<LineComment> lineComment(String lineComment){
        return new $comment(Ast.lineComment(lineComment)).omitBlockComments().omitJavadocComments();
    }
    
    public static $comment<LineComment> lineComment(String pattern, Predicate<LineComment> constraint){
        return new $comment(Ast.lineComment( pattern))
                .omitBlockComments().omitJavadocComments()
                .$and(constraint);
    }
    
    public static $comment<LineComment> lineComment(Predicate<LineComment> constraint){
        return new $comment().omitBlockComments().omitJavadocComments()
            .$and(constraint);
    }

    
    public static final Set<Class<? extends Comment>> ALL_COMMENT_CLASSES = new HashSet<>();
    
    static {
        ALL_COMMENT_CLASSES.add(BlockComment.class);
        ALL_COMMENT_CLASSES.add(LineComment.class);
        ALL_COMMENT_CLASSES.add(JavadocComment.class);
    }    
    
    /** the classes of comment classes that are matched against */
    public Set<Class<? extends Comment>> commentClasses = new HashSet<>();
    
    /** The pattern for the contents of the Comment */
    public Stencil contentsStencil = Stencil.of("$comment$");
    
    public Predicate<C> constraint = t -> true;
    
    /** this is the any() constructor private intentionally*/
    private $comment(){
        commentClasses.addAll(ALL_COMMENT_CLASSES);
        contentsStencil = Stencil.of("$comment$");
    }

    public $comment (String... comment){
        String trimmed = Text.combine(comment);
        if (trimmed.startsWith("/**") ){
            commentClasses.add(JavadocComment.class);
            contentsStencil = Stencil.of( Ast.getContent( Ast.comment(comment) ).trim() );
        } else if( trimmed.startsWith("/*") ){
            commentClasses.add(BlockComment.class);
            contentsStencil = Stencil.of( Ast.getContent( Ast.comment(comment) ).trim() );
        } else if( trimmed.startsWith("//")){
            commentClasses.add(LineComment.class);
            Comment com = Ast.comment(comment);
            String content = Ast.getContent(com );
            //System.out.println("CON" + content );
            contentsStencil = Stencil.of( content.trim() );
        } else{
            commentClasses.addAll(ALL_COMMENT_CLASSES);
            contentsStencil = Stencil.of( Ast.getContent( Ast.comment(comment) ).trim() );
        }
    }

    /**
     * A $proto based on a particular kind of comment
     * @param <C>
     * @param astComment 
     */
    public <C extends Comment> $comment( C astComment ){
        if(astComment != null ) {
            this.commentClasses.add(astComment.getClass());
            this.contentsStencil = Stencil.of(Ast.getContent(astComment).trim());
        }
    }
    
    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $comment hardcode$(Translator translator, Tokens kvs ) {
        this.contentsStencil = this.contentsStencil.hardcode$(translator, kvs);
        return this;
    }
    
    public $comment $and(Predicate<C> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    public $comment matchBlockComments(){
        this.commentClasses.add(BlockComment.class);
        return this;
    }
    
    public $comment matchLineComments(){
        this.commentClasses.add(LineComment.class);
        return this;
    }
    
    public $comment matchJavadocComments(){
        this.commentClasses.add(JavadocComment.class );
        return this;
    }
    
    public $comment omitBlockComments(){
        this.commentClasses.remove(BlockComment.class);
        return this;
    }
    
    public $comment omitLineComments(){
        this.commentClasses.remove(LineComment.class);
        return this;
    }
    
    public $comment omitJavadocComments(){
        this.commentClasses.remove(JavadocComment.class );
        return this;
    }

    public boolean matches( _javadoc _j ){
        if( _j == null ){
            return this.isMatchAny();
        }
        return matches( _j.ast());
    }

    public boolean matches( String...comment ){
        Comment com = Ast.comment(comment);
        return matches( com );
    }

    public boolean matches( _javadoc._hasJavadoc _j){
        if( !_j.hasJavadoc() ){
             return this.isMatchAny();
        }
        return matches(_j.getJavadoc().ast());
    }

    public boolean matches( Node nodeWithComment){
        if( !nodeWithComment.getComment().isPresent() ){
            return isMatchAny();
        }
        return matches( nodeWithComment.getComment().get());
    }

    public boolean matches( Comment astComment ){
        return select(astComment) != null;
    }

    /**
     *
     * @param comment
     * @return
     */
    public Select select( String...comment ){
        return select( Ast.comment(comment));
    }

    /**
     *
     * @param astComment
     * @return
     */
    public Select select( Comment astComment ){
        if(! this.commentClasses.contains(astComment.getClass())){
            //System.out.println( "Coment"+ astComment+ " "+astComment.isJavadocComment()+ " "+astComment.getClass());
            //System.out.println( "not correct comment class"+ this.commentClasses+" "+ astComment.getClass());
            return null;
        }
        if( !this.constraint.test( (C)astComment) ){
            //System.out.println( "failed constraint");
            return null;
        }

        /** NEW looks for the existence (i.e. match anywhere within the comment content */
        Tokens ts = this.contentsStencil.parseFirst(Ast.getContent(astComment));
        if( ts == null ){
            return null;
        }
        return new Select(astComment, $tokens.of(ts));

        /* OLD looks for an exact match
        Tokens ts = this.contentsStencil.parse(Ast.getContent(astComment));
        if( ts == null ){
            return null;
        }
        return new Select( astComment, $tokens.of( ts ));

        /* */
    }

    /**
     *
     * @return
     */
    public boolean isMatchAny(){
        try{
            return 
                this.contentsStencil.isMatchAny() && this.constraint.test(null);
        }catch(Exception e){
            //System.out.println("COMMENT NOT MATCH ANY" );
            return false;
        }
    }

    public $tokens parse(_javadoc._hasJavadoc hj){
        //System.out.println( "1>>>>>>>>>>>>> parsin "+hj);
        //System.out.println( "1>>>>>>>>>>>>> parsin "+this.commentClasses);
        _javadoc _jd = hj.getJavadoc();
        if( _jd == null){
            //System.out.println( "Javadoc is null");
            if (isMatchAny()) {
                return $tokens.of();
            }
            return null;
            //return parse( (Comment)null);
        }
        //System.out.println( _jd);
        //System.out.println( "CONTENT"+ _jd.getContent());
        return parse(_jd.ast() );
    }

    /** TODO i should reverse this (seelct should call parse */
    public $tokens parse(Comment comment) {
        if (comment == null) {
            if (isMatchAny()) {
                return $tokens.of();
            } else {
                return null;
            }
        }
        //System.out.println( comment.getClass() +  "  "+ comment );
        Select sel = select(comment);
        if (sel != null) {
            return sel.tokens;
        }
        return null;
    }

    /**
     *
     * @param comment
     * @param allTokens
     * @return
     */
    public Tokens parseTo(Comment comment, Tokens allTokens ){
        if(allTokens == null){
            return allTokens;
        }
        if( comment == null ){
            if( isMatchAny() ){
                return allTokens;
            } else{
                return null;
            }
        }
        Select sel = select(comment);
        if( sel != null ){
            if( allTokens.isConsistent(sel.tokens.asTokens()) ){
                allTokens.putAll(sel.tokens.asTokens());
                return allTokens;
            }
        }
        return null;
    }

    @Override
    public C firstIn(Node astNode, Predicate<C> commentMatchFn) {
        
        //this is extra work, but it "acts" like we want it to
        List<C> found = listIn(astNode, commentMatchFn);
        Ast.sortNodesByPosition(found);
        if( found.isEmpty() ){
            return null;
        }
        return found.get(0);
    }

    @Override
    public Select selectFirstIn(Node astNode) {
        List<C> found = listIn(astNode );
        Ast.sortNodesByPosition(found);
        if( found.isEmpty() ){
            return null;
        }
        return select(found.get(0));        
    }

    /**
     *
     * @param astNode
     * @param selectConstraint
     * @return
     */
    public Select selectFirstIn(Node astNode, Predicate<Select> selectConstraint) {
        List<Select> found = listSelectedIn(astNode, selectConstraint);
        Collections.sort( found, (s1, s2)-> Ast.COMPARE_NODE_BY_LOCATION.compare(s1.comment, s2.comment));
        if( found.isEmpty() ){
            return null;
        }
        return found.get(0);        
    }

    @Override
    public List<Select> listSelectedIn(Node astNode) {
        List<Select> found = new ArrayList<>();
        forSelectedIn(astNode, s -> found.add(s));
        return found;
    }

    /**
     *
     * @param astNode
     * @param selectConstraint
     * @return
     */
    public List<Select> listSelectedIn(Node astNode, Predicate<Select> selectConstraint) {
        List<Select> found = new ArrayList<>();
        forSelectedIn( astNode, selectConstraint, s -> found.add(s));
        return found;
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<C> commentMatchFn, Consumer<C> _nodeActionFn) {
        Ast.forComments(astNode, c->{
            Select s = select(c);
            if( s != null && commentMatchFn.test( (C)s.comment) ) {
                _nodeActionFn.accept( (C)c);
            }
        });
        return astNode;
    }

    /**
     *
     * @param _j
     * @param selectActionFn
     * @param <_J>
     * @return
     */
    public <_J extends _java> _J forSelectedIn(_model _j, Consumer<Select> selectActionFn) {
        _java.forComments(_j, c->{
            Select s = select(c);
            if( s != null ){
                selectActionFn.accept(s);
            }
        });
        return (_J)_j;
    }
    
    public <N extends Node> N forSelectedIn(N astNode, Consumer<Select> selectActionFn) {
        //_walk will organize by order
        Ast.forComments(astNode, c->{
            Select s = select(c);
            if( s != null ){
                selectActionFn.accept(s);
            }
        });
        return astNode;
    }
    
    public <_J extends _model> _J forSelectedIn(_model _j, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        _java.forComments(_j, c->{
            Select s = select(c);
            if( s != null && selectConstraint.test(s)){
                selectActionFn.accept(s);
            }
        });
        return (_J)_j;
    }
    
    public <N extends Node> N forSelectedIn(N astNode, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        Ast.forComments(astNode, c->{
            Select s = select(c);
            if( s != null && selectConstraint.test(s)){
                selectActionFn.accept(s);
            }
        });
        return astNode;
    }
        
    @Override
    public <N extends Node> N removeIn(N astNode) {
        return forEachIn(astNode, n-> n.remove() );
    }

    public JavadocComment draftJavadocComment(Translator translator, Map<String, Object> keyValues) {
        String contents = this.contentsStencil.draft(translator, keyValues);
        if( contents.trim().length() == 0 ){
            return null;
        }
        if( contents.trim().equals( "null" ) ){
            return null;
        }
        return new JavadocComment( contents );
    }
    
    public BlockComment draftBlockComment(Translator translator, Map<String, Object> keyValues) {
        
        String contents = this.contentsStencil.draft(translator, keyValues);
        if( contents.trim().length() == 0 || contents.equals("null")){
            return null;
        }
        return new BlockComment( contents );
    }
    
    public LineComment draftLineComment(Translator translator, Map<String,Object> keyValues){
        String contents = this.contentsStencil.draft(translator, keyValues);
        if( contents.trim().length() == 0 || contents.equals("null")){
            return null;
        }
        return new LineComment( contents );
    }
    
    @Override
    public C draft(Translator translator, Map<String, Object> keyValues) {
        if( !keyValues.containsKey("comment")){
            keyValues.put("comment", "");
        }        
        if( !keyValues.containsKey("javadoc")){
            keyValues.put("javadoc", "");
        }      
        if( this.commentClasses.isEmpty()){
            return (C) draftBlockComment( translator, keyValues );
        }
        if( this.commentClasses.size() == 1 ){
            Class cc = this.commentClasses.toArray(new Class[0])[0];
            if( cc == JavadocComment.class){
                return (C) draftJavadocComment(translator, keyValues);
            }
            if( cc == BlockComment.class){
                return (C) draftBlockComment( translator, keyValues );
            }
            return (C) draftLineComment(translator, keyValues );
        }
        if( this.commentClasses.contains(JavadocComment.class)){
            return (C) draftJavadocComment(translator, keyValues);
        }
        if( this.commentClasses.contains(BlockComment.class)){
            return (C) draftJavadocComment(translator, keyValues);
        }
        return (C) draftLineComment(translator, keyValues);
    }

    @Override
    public $comment $(String target, String $paramName) {
        this.contentsStencil = this.contentsStencil.$(target, $paramName);
        return this;
    }

    @Override
    public List<String> list$() {
        return this.contentsStencil.list$();
    }

    @Override
    public List<String> list$Normalized() {
        return this.contentsStencil.list$Normalized();
    }

    public boolean match( Node n){
        if( n instanceof Comment ){
            return matches( (Comment) n);
        }
        return false;
    }

    public String toString(){
        if( this.isMatchAny() ){
            return "$comment { $ANY$ }";
        }
        StringBuilder str = new StringBuilder();
        if( this.commentClasses.size() == 1 ) {
            if( commentClasses.contains(JavadocComment.class)) {
                str.append("$javadoc{").append(System.lineSeparator())
                        .append("    ").append(contentsStencil.toString()).append(System.lineSeparator())
                        .append("}").append(System.lineSeparator());
            }
            else if( commentClasses.contains(BlockComment.class)) {
                str.append("$comment<BlockComment>{").append(System.lineSeparator())
                        .append("    ").append(contentsStencil.toString()).append(System.lineSeparator())
                        .append("}").append(System.lineSeparator());
            }
            else {
                str.append("$comment<LineComment>{").append(System.lineSeparator())
                        .append("    ").append(contentsStencil.toString()).append(System.lineSeparator())
                        .append("}").append(System.lineSeparator());
            }
        }
        else{
            str.append("$comment{").append(System.lineSeparator())
                    .append("    ").append(contentsStencil.toString()).append(System.lineSeparator())
                    .append("}").append(System.lineSeparator());
        }
        return str.toString();
    }


    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $comment{

        final List<$comment>ors = new ArrayList<>();

        public Or($comment...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $comment hardcode$(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.hardcode$(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$comment.Or{");
            sb.append(System.lineSeparator());
            ors.forEach($a -> sb.append( Text.indent($a.toString()) ) );
            sb.append("}");
            return sb.toString();
        }

        /**
         *
         * @param astNode
         * @return
         */
        public $comment.Select select(Comment astNode){
            $comment $a = whichMatch(astNode);
            if( $a != null ){
                return $a.select(astNode);
            }
            return null;
        }

        public boolean isMatchAny(){
            return false;
        }

        /**
         * Return the underlying $anno that matches the AnnotationExpr or null if none of the match
         * @param ae
         * @return
         */
        public $comment whichMatch(Comment ae){
            Optional<$comment> orsel  = this.ors.stream().filter( $p-> $p.match(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }

    /**
     * 
     * @param <C> 
     */
    public static class Select<C extends Comment> 
        implements $pattern.selected,
            selectAst<C> {

        public C comment;
        public $tokens tokens;
        
        public Select( C comment, $tokens tokens){
            this.comment = comment;
            this.tokens = tokens;
        }
        
        @Override
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public C ast() {
            return comment;
        }
        
        public boolean isOrphan(){
            return comment.isOrphan();
        }
        
        public boolean isBlockComment(){
            return comment instanceof BlockComment;
        }
        
        public boolean isLineComment(){
            return comment instanceof LineComment;
        }
        
        public boolean isJavadocComment(){
            return comment instanceof JavadocComment;
        }
        
        public String getContent(){
            return Ast.getContent(comment);
        }

        @Override
        public String toString(){
            return "$field.Select{"+ System.lineSeparator()+
                    Text.indent( comment.toString() )+ System.lineSeparator()+
                    Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                    "}";
        }
    }    
}
