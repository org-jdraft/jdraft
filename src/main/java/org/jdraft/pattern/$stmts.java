package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithStatements;
import com.github.javaparser.ast.stmt.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;

import org.jdraft.*;
import org.jdraft.macro._remove;

/**
 * Template of a Java statements (one or more {@link Statement}s
 *
 * NOTE: although this does not implement the Template<> and $query<> interfaces
 * it follows the same naming conventions
 */
public class $stmts implements Template<List<Statement>>, $pattern<List<Statement>, $stmts>, $body.$part {
    
    /**
     * Build a dynamic code snippet based on the content of a method defined within an anonymous Object
     * <PRE>
     * i.e.
     * $statements $s = $statements.of( new Object(){
     *     int m(String $name$, Integer $init$){
     *         // The code inside is used
     *
     *         if( $name$ == null ){
     *              return $init$;
     *         }
     *         return $name$.hashCode();
     *     }
     * });
     * System.out.println( $s.draft( _field.of("int x;") ));
     * </PRE>
     * @param anonymousObjectWithBody
     * @return the dynamic code snippet
     */
    public static $stmts of(Object anonymousObjectWithBody ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Ex.anonymousObjectEx(ste);
        //find the first method that doesnt have removeIn on it and has a BODY
        // to get it's contents
        MethodDeclaration theMethod = (MethodDeclaration)
            oce.getAnonymousClassBody().get().stream().filter(m -> m instanceof MethodDeclaration &&
                !m.isAnnotationPresent(_remove.class) &&
                ((MethodDeclaration) m).getBody().isPresent()).findFirst().get();
        return $stmts.of( theMethod.getBody().get());
    }

    public static $stmts of(String proto ){
        return $stmts.of(new String[] {proto});
    }

    public static $stmts of(Ex.Command c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return $stmts.of( Ex.lambdaEx(ste));
    }

    public static <T extends Object> $stmts of(Consumer<T> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return $stmts.of( Ex.lambdaEx(ste));
    }

    public static <T extends Object, U extends Object> $stmts of(Function<T,U> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return $stmts.of( Ex.lambdaEx(ste));
    }

    public static <T extends Object, U extends Object, V extends Object> $stmts of(BiFunction<T,U, V> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return $stmts.of( Ex.lambdaEx(ste));
    }

    public static <T extends Object, U extends Object> $stmts of(BiConsumer<T,U> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return $stmts.of( Ex.lambdaEx(ste));
    }

    public static <T extends Object, U extends Object, V extends Object> $stmts of(Ex.TriConsumer<T,U,V> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return $stmts.of( Ex.lambdaEx(ste));
    }

    public static <T extends Object, U extends Object, V extends Object, Z extends Object> $stmts of(Ex.QuadConsumer<T,U,V,Z> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return $stmts.of( Ex.lambdaEx(ste));
    }

    public static $stmts of(LambdaExpr astLambda ){
        Statement st = Stmt.from(astLambda);
        return new $stmts(st);
    }

    public static $stmts of(_body _b ){
        return $stmts.of( _b.ast() );
    }

    public static $stmts of(BlockStmt astBlockStmt ){
        $stmts $s = new $stmts();
        astBlockStmt.getStatements().forEach(s -> $s.add(s));
        return $s;
    }
    
    public static $stmts of(){
        return new $stmts( $stmt.of() );
    }
     
    /**
     * 
     * @param protoSnippet
     * @return 
     */
    public static $stmts of(String... protoSnippet) {
        $stmts $s = new $stmts();
        BlockStmt bs = Ast.blockStmt(protoSnippet);

        bs.getStatements().forEach(s -> $s.add(s));
        return $s;
    }

    public static $stmts.Or or( List<Statement>... _protos ){
        $stmts[] arr = new $stmts[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $stmts.of( _protos[i]);
        }
        return or(arr);
    }

    public static $stmts.Or or( $stmts...$tps ){
        return new $stmts.Or($tps);
    }

    /**     */
    public Predicate<List<Statement>> constraint = t-> true;
    
    /** */
    public List<$stmt> $sts = new ArrayList<>();


    public $stmts(){
    }

    private $stmts(Statement astProtoStmt ){
        if( astProtoStmt instanceof BlockStmt ){
            astProtoStmt.asBlockStmt().getStatements().forEach(s -> add(s) );
        } else{
            add(astProtoStmt);
        }
    }

    public $stmts($stmt $st){
        add($st);
    }

    /**
     * Adds a new stmt to the end of the prototype snippet
     *
     * @param astProtoStmt
     * @return
     */
    public $stmts add(Statement astProtoStmt){
        $sts.add(new $stmt(astProtoStmt) );
        return this;
    }

    public $stmts add($stmt $st ){
        $sts.add($st);
        return this;
    }

    /**
     * Adds a constraint the selecting / matching a list of statements
     * 
     * @param constraint
     * @return the modified snip
     */
    public $stmts $and(Predicate<List<Statement>> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    @Override
    public $stmts $(String target, String $paramName){
        this.$sts.forEach(s -> s.$(target, $paramName));
        return this;
    }

    @Override
    public List<String> list$Normalized(){
        List<String>normalized$ = new ArrayList<>();
        $sts.forEach(s -> {
            List<String> ls = s.list$Normalized();
            ls.forEach( l-> {
                if (!normalized$.contains(l)) {
                    normalized$.add(l);
                }
            });
        });
        return normalized$;
    }

    @Override
    public List<String> list$(){
        List<String>$s = new ArrayList<>();
        $sts.forEach(s -> {
            List<String> ls = s.list$();
            ls.forEach( l-> {
                if (!$s.contains(l)) {
                    $s.add(l);
                }
            });
        });
        return $s;
    }

    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $stmts hardcode$(Translator translator, Tokens kvs ) {
        this.$sts.forEach( $s -> $s.hardcode$(translator, kvs));
        return this;
    }
    
    /**
     * 
     * @param tokens
     * @return 
     */
    public List<Statement> draft(Tokens tokens ){
        return $stmts.this.draft( Translator.DEFAULT_TRANSLATOR, tokens.map());
    }

    /**
     * 
     * @param _n
     * @return 
     */
    public List<Statement> draft(_node _n ){
        return draft(_n.tokenize());
    }

    @Override
    public List<Statement> draft(Translator t, Map<String,Object> tokens ){
        List<Statement>sts = new ArrayList<>();
        $sts.forEach(stmt -> {
            if( stmt.statementClass == LabeledStmt.class &&
                    stmt.stmtStencil.getTextForm().startsWithText()) {
                    //&&
                    //stmt.stencil.getTextBlanks().getFixedText().startsWith("$") ){
                /* Dynamic labeled Statements are Labeled Statements like this:
                 * $callSuperEquals: eq = super.typesEqual($b$) && eq;
                 *
                 * THEY MUST start with a $ and be Labeled Statements
                 * we process these by checking to see if a parameter
                 * "$callSuperEquals" if the VALUE associated with $doThis is passed into the input,
                 * and the VALUE of $doThis is NOT NULL or NOT Boolean.FALSE
                 */
                String sttext = stmt.stmtStencil.getTextForm().getFixedText();
                String name = sttext.substring(0, sttext.indexOf(":") );
                Object val = tokens.get(name );
                if( val instanceof BlockStmt ){
                    //we are filling in with static code
                    ((BlockStmt)val).getStatements().forEach( s-> sts.add(s));
                }
                else if( val instanceof Statement){
                    sts.add( (Statement)val);
                }
                else if( val instanceof String){
                    sts.add( Stmt.of((String)val));
                }
                else if( val != null && val != Boolean.FALSE ){
                    //construct the statement (it
                    LabeledStmt ls = (LabeledStmt)stmt.draft(t, tokens);
                    Statement st = ls.getStatement();
                    if( st instanceof BlockStmt ) {
                        //add each of the statements
                        st.asBlockStmt().getStatements().forEach( s-> sts.add(s));
                    }else{
                        sts.add(st ); //just add the derived statement
                    }
                }
            } else { //it is NOT a dymanically labeled Statement, so just process normally
                sts.add(stmt.draft(t, tokens));
            }
        });       
        return sts;
    }


    @Override
    public <S extends selected> S select(List<Statement> instance) {
        //return null;
        return (S)select(instance.get(0));
    }

    /**
     * 
     * @param astStmt
     * @return 
     */
    public Select select( Statement astStmt ){
        int idx = 0;
        List<Statement> ss = new ArrayList<>();
        ss.add(astStmt);
        if( this.$sts.size() > 1  ){
            if( !astStmt.getParentNode().isPresent() ) {
                //System.out.println("cant be match, no parent");
                return null; //cant be
            }
            NodeWithStatements nws = (NodeWithStatements)astStmt.getParentNode().get();
            List<Statement>childs = nws.getStatements();
            idx = childs.indexOf(astStmt);
            if( childs.size() - idx < this.$sts.size() ){
                return null; //cant be a match, b/c not enough sibling nodes to match $snip
            }
            for(int i = 1; i<this.$sts.size(); i++){ //add the rest of the statements to the array
                //System.out.println( "adding "+ childs.get(idx+i) );
                ss.add( (Statement)childs.get(idx+i) );
            }
        }
        //check if we can partsMap the first one
        Tokens all = new Tokens();
        for(int i = 0; i< this.$sts.size(); i++) {
            //$args ts = this.$sts.get(i).deconstruct(ss.get(i));
            $stmt.Select sel = this.$sts.get(i).select(ss.get(i) );
            if (sel == null) {
                //System.out.println( "NO Match with >"+ this.$sts.get(i)+ "< against >"+ss.get(i)+"< tokens");
                return null;
            }
            String[] keys = sel.tokens.keySet().toArray(new String[0]);
            for(int j=0;j<keys.length;j++){
                if( all.containsKeys(keys[j]) && !all.get( keys[j] ).equals( sel.tokens.get(keys[j])) ){
                    //System.out.println( "Inconsistent Key Values");
                    return null; //inconsistent key values
                }
            }
            all.putAll(sel.tokens);
        }
        if( !this.constraint.test(ss) ){
            return null; //failed the constraint
        }
        return new Select( ss, all);
    }

    public boolean match( Node node ) {
        if (node instanceof Statement) {
            return matches((Statement) node);
        }
        return false;
    }

    @Override
    public boolean isMatchAny(){
        return false;
    }

    public boolean matches( List<Statement> stmts ){
        throw new _draftException("Cant do this yet...addded for _proto convenience");
    }
    /**
     * 
     * @param astStmt
     * @return 
     */
    public boolean matches( Statement astStmt ){
        return select(astStmt) != null;
    }

    /**
     * Returns the first List<Statement>  that matches the pattern and constraint
     * @param astStartNode the node to look through
     * @param statementsMatchFn
     * @return  the first List<Statement> that matches (or null if none found)
     */
    @Override
    public List<Statement> firstIn(Node astStartNode, Predicate<List<Statement>> statementsMatchFn){
        List<List<Statement>>ss = listIn(astStartNode);
        if( ss.isEmpty() ){
            return null;
        }        
        return ss.get(0);        
    }

    @Override
    public List<List<Statement>> listIn(Node astStartNode, Predicate<List<Statement>> statementsMatchFn){
        List<List<Statement>>sts = new ArrayList<>();
        astStartNode.walk(this.$sts.get(0).statementClass, st -> {
            Select sel = select( (Statement)st );
            if( sel != null && statementsMatchFn.test(sel.statements)){
                sts.add( sel.statements );
            }
        });
        return sts;
    }
    
    @Override
    public Select selectFirstIn( Class clazz){
        return selectFirstIn((_type)_java.type(clazz));
    }
    
    /**
     * 
     * @param astNode the top ast node to run
     * @return the Select
     */
    @Override
    public Select selectFirstIn( Node astNode ){
        Optional<Node> os = astNode.stream().filter(
            n -> n instanceof Statement &&
                select( (Statement)n ) !=null ).findFirst();
        
        if( os.isPresent() ){
            return select( (Statement)os.get());
        }
        return null;
    }

    /**
     * 
     * @param <N>
     * @param astNode
     * @param statementsMatchFn
     * @param statementsConsumer
     * @return 
     */
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<List<Statement>>statementsMatchFn, Consumer<List<Statement>> statementsConsumer ){
        astNode.walk(this.$sts.get(0).statementClass, st -> {
            Select sel = select( (Statement)st );
            if( sel != null &&  statementsMatchFn.test(sel.statements)){
                statementsConsumer.accept( sel.statements );
            }
        });
        return astNode;
    }

    /**
     * 
     * @param clazz
     * @return 
     */
    @Override
    public List<Select> listSelectedIn(Class clazz){
        return listSelectedIn((_type)_java.type(clazz));
    }
    
    @Override
    public List<Select> listSelectedIn(_model _j){
        if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                return listSelectedIn(_c.astCompilationUnit());
            }
            _type _t = (_type) _j; //only possible
            return listSelectedIn(_t.ast()); //return the TypeDeclaration, not the CompilationUnit
        }
        return listSelectedIn( ((_node) _j).ast());
    }

    @Override
    public List<Select> listSelectedIn(Node astNode ){
        List<Select>sts = new ArrayList<>();
        astNode.walk(this.$sts.get(0).statementClass, st -> {
            Select sel = select( (Statement)st );
            if( sel != null ){
                sts.add( sel );
            }
        });
        return sts;
    }

    @Override
    public <N extends Node> N removeIn( N astNode ){
        listSelectedIn(astNode ).forEach(s -> s.statements.forEach(st-> st.removeForced()));
        return astNode;
    }

    @Override
    public <_J extends _model> _J removeIn(_J _j){
        List<Select> sels= (List<Select>)listSelectedIn(_j);
        sels.forEach(s -> s.statements.forEach(st-> st.removeForced()));
        return _j;
    }
    
    /**
     * 
     * @param clazz
     * @param $repl
     * @return 
     */
    public <_CT extends _type> _CT replaceIn( Class clazz, $stmt $repl ){
        return (_CT)replaceIn((_type)_java.type(clazz), $repl);
    }
    
    public <_CT extends _type> _CT replaceIn( Class clazz, String... repl ){
        return (_CT)replaceIn((_type)_java.type(clazz), $stmts.of(repl));
    }
    
    public <N extends Node> N replaceIn(N astNode, String...repl ){        
        return $stmts.this.replaceIn(astNode, $stmts.of(repl));
    }
        
    /**
     * 
     * @param <N>
     * @param astNode
     * @param $repl
     * @return 
     */
    public <N extends Node> N replaceIn(N astNode, $stmt $repl ){
        $stmts $sn = new $stmts($repl);
        return $stmts.this.replaceIn(astNode, $sn);
    }

    /**
     * 
     * @param clazz
     * @param $repl
     * @return 
     */
    public <_CT extends _type> _CT replaceIn( Class clazz, $stmts $repl ){
        return (_CT)replaceIn((_type)_java.type(clazz), $repl);
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param $repl
     * @return 
     */
    public <N extends Node> N replaceIn(N astNode, $stmts $repl ){
        AtomicInteger ai = new AtomicInteger(0);

        astNode.walk(this.$sts.get(0).statementClass, st-> {
            Select sel = select( (Statement)st );
            if( sel != null ){
                //constrct the replacement snippet
                List<Statement> replacements = $repl.draft(sel.tokens);
                Statement firstStmt = sel.statements.get(0);
                Node par = firstStmt.getParentNode().get();
                NodeWithStatements parentNode = (NodeWithStatements)par;
                int addIndex = par.getChildNodes().indexOf( firstStmt );
                LabeledStmt ls = Stmt.labeledStmt("$replacement$:{}");
                // we want to add the contents of the replacement to a labeled statement,
                // because, (if we did it INLINE, we could  end up in an infinite loop, searching the
                // tree up to a cursor, then adding some code AT the cursor, then finding a match within the added
                // code, then adding more code, etc. etc.
                // this way, WE ADD A SINGLE LABELED STATEMENT AT THE LOCATION OF THE FIRST MATCH
                // (which contains multiple statements)
                // then, we move to the next statement skipping over the code that was added
                //at the end we "flatten the labeled statements" making them inline
                for(int i=0;i<replacements.size(); i++){
                    ls.getStatement().asBlockStmt().addStatement( replacements.get(i) );
                }
                //add the labeled statement after the last statement
                parentNode.addStatement(addIndex +1, ls);

                //removeIn all but the matched statements
                sel.statements.forEach( s-> s.remove() );
                ai.incrementAndGet();
                //System.out.println("PAR AFTER Remove "+ par );
            }
        });

        Ast.flattenLabel(astNode, "$replacement$");
        return astNode;
    }

    public <_J extends _model> _J replaceIn(_J _j, String... repl ){
        return replaceIn(_j, $stmts.of(repl));
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param $repl
     * @return 
     */
    public <_J extends _model> _J replaceIn(_J _j, $stmt $repl ){
        $stmts $sn = new $stmts($repl);
        return $stmts.this.replaceIn(_j, $sn);
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param $repl
     * @return 
     */
    public <_J extends _model> _J replaceIn(_J _j, $stmts $repl ){
        AtomicInteger ai = new AtomicInteger(0);

        _walk.in(_j, this.$sts.get(0).statementClass, st-> {
        //_le.walk( this.$sts.get(0).statementClass, st-> {
            Select sel = select( (Statement)st );
            if( sel != null ){
                //construct the replacement snippet
                List<Statement> replacements = $repl.draft(sel.tokens);
                Statement firstStmt = sel.statements.get(0);
                Node par = firstStmt.getParentNode().get();
                NodeWithStatements parentNode = (NodeWithStatements)par;
                int addIndex = par.getChildNodes().indexOf( firstStmt );
                LabeledStmt ls = Stmt.labeledStmt("$replacement$:{}");
                // we want to add the contents of the replacement to a labeled statement,
                // because, (if we did it INLINE, we could  end up in an infinite loop, searching the
                // tree up to a cursor, then adding some code AT the cursor, then finding a match within the added
                // code, then adding more code, etc. etc.
                // this way, WE ADD A SINGLE LABELED STATEMENT AT THE LOCATION OF THE FIRST MATCH
                // (which contains multiple statements)
                // then, we move to the next statement skipping over the code that was added
                //at the end we "flatten the labeled statements" making them inline
                for(int i=0;i<replacements.size(); i++){
                    ls.getStatement().asBlockStmt().addStatement( replacements.get(i) );
                }
                //add the labeled statement after the last statement
                parentNode.addStatement(addIndex +1, ls);

                //removeIn all but the matched statements
                sel.statements.forEach( s-> s.remove() );
                ai.incrementAndGet();
                //System.out.println("PAR AFTER Remove "+ par );
            }
        });
        if( _j instanceof _node ){
            Ast.flattenLabel( ((_node) _j).ast(), "$replacement$");
        }
        return _j;
    }

    /**
     * 
     * @param clazz
     * @param selectedAction
     * @return 
     */
    public <_CT extends _type> _CT forSelectedIn( Class clazz, Consumer<Select>selectedAction ){
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectedAction );
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectedAction
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Consumer<Select>selectedAction ){
        astNode.walk( this.$sts.get(0).statementClass, st-> {
            Select sel = select( (Statement)st );
            if( sel != null ){
                selectedAction.accept(sel);
            }
        });
        return astNode;
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectedAction
     * @return 
     */
    public <_J extends _model> _J forSelectedIn(_J _j, Consumer<Select>selectedAction ){
        _walk.in(_j, this.$sts.get(0).statementClass, st-> {
            Select sel = select( (Statement)st );
            if( sel != null ){
                selectedAction.accept(sel);
            }
        });
        return _j;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        this.$sts.forEach(s -> sb.append(s).append( System.lineSeparator() ));
        return "$stmts { "+ System.lineSeparator() + Text.indent( sb.toString() ) + "}";
    }


    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are suppressed.
     */
    public static class Or extends $stmts {

        final List<$stmts>ors = new ArrayList<>();

        public Or($stmts...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $stmts hardcode$(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.hardcode$(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$stmts.Or{");
            sb.append(System.lineSeparator());
            ors.forEach($a -> sb.append( Text.indent($a.toString()) ) );
            sb.append("}");
            return sb.toString();
        }

        /**
         *
         * @param n
         * @return
         */
        public Select select(Statement n){
            $stmts $a = whichMatch(n);
            if( $a != null ){
                return $a.select(n);
            }
            return null;
        }

        public boolean isMatchAny(){
            return false;
        }


        //had to mess with the Select to ensure the match abides by the constraint
        public $stmts whichMatch(Statement st){
            Optional<$stmts> orsel  = this.ors.stream().filter( $p-> {
                $stmts.Select ss = $p.select(st);
                return ( ss != null && this.constraint.test( ss.statements) );
            }  ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }


        /**
         * Return the underlying $method that matches the Method or null if none of the match
         * @param stmts
         * @return
         */
        public $stmts whichMatch(List<Statement> stmts){
            if( !this.constraint.test(stmts ) ){
                return null;
            }
            Optional<$stmts> orsel  = this.ors.stream().filter( $p-> $p.matches(stmts) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }


    /**
     * A Matched Selection result returned from matching a prototype $field
     * inside of some Node or _node
     */
    public static class Select implements $pattern.selected {
        public List<Statement> statements;
        public $tokens tokens;

        @Override
        public $tokens tokens(){
            return tokens;
        }
        
        public Select( List<Statement> statements, Tokens tokens){
            this.statements = statements;
            this.tokens = this.tokens.of(tokens);
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            this.statements.forEach( s -> sb.append(s).append( System.lineSeparator()) );
            return "$snip.Select{"+ System.lineSeparator()+
                Text.indent( sb.toString() )+ System.lineSeparator()+
                Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                "}";
        }
        
        public int stmtCount(){
            return statements.size();
        }
    }
}