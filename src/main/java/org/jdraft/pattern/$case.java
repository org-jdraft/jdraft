package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jdraft.*;
import org.jdraft.text.Template;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

/**
 * pattern of "case" within a Switch Statement (a {@link SwitchEntry})
 *
 * @author Eric
 */
public class $case
    implements $pattern<SwitchEntry, $case>, Template<SwitchEntry>, $body.$part, $method.$part, $constructor.$part {

    public static $case of(){
        return new $case( $ex.of() );
    }

    public static $case of( Predicate<SwitchEntry> constraint ){
        return of().$and(constraint);
    }

    public static $case of( String...acase ){
        return of( Ast.switchEntry(acase ));
    }

    public static $case of( SwitchEntry astSwitchEntry ){
        return new $case(astSwitchEntry );
    }
    
    public static $case of( SwitchEntry astSwitchEntry, Predicate<SwitchEntry> constraint){
        return new $case(astSwitchEntry).$and(constraint);
    }

    public static $case of( $stmt...stmts ){
        return of( $ex.of(), stmts );
    }

    public static $case of($ex expr, $stmt...stmts ){
        return new $case(expr, stmts);
    }

    public static $case.Or or( SwitchEntry... _protos ){
        $case[] arr = new $case[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $case.of( _protos[i]);
        }
        return or(arr);
    }

    public static $case.Or or( $case...$tps ){
        return new $case.Or($tps);
    }

    public static $case as( String...st){
        return as( Ast.switchEntry(st));
    }

    public static $case as( SwitchEntry astSwitchEntry ){
        $case $c = of( astSwitchEntry );

        //need to make sure there are
        final List<$stmt>$sts = new ArrayList<>();
        NodeList<Statement> sts = astSwitchEntry.getStatements();
        for(int i=0;i<sts.size();i++){
            $sts .add( $stmt.of(sts.get(i)));
        }
        $c.$and( s-> {
            if (s.getStatements().size() != astSwitchEntry.getStatements().size()) {
                return false;
            }
            for(int i=0;i<$sts.size();i++){
                if( !$sts.get(i).matches(s.getStatement(i))){
                    return false;
                }
            }
            return true;
        });
        return $c;
    }

    public Predicate<SwitchEntry> constraint = t-> true;
    
    public $ex label = $ex.of();
    
    public List<$stmt> statements = new ArrayList<>();

    $case(){
        this.label = null;
    }


    private $case( $ex $labelExpr ){
        this.label = $labelExpr; 
    }
    
    private $case($ex $labelExpr, $stmt...stmts){
        this.label = $labelExpr;
        Arrays.stream(stmts).forEach(s -> this.statements.add(s));        
    }

    public $case(SwitchEntry se){   
        if( se.getLabels().isNonEmpty() ){
            this.label = $ex.of( se.getLabels().get( 0 ) );
        } 
        for(int i=0;i<se.getStatements().size(); i++){
            this.statements.add($stmt.of(se.getStatements().get(i))); 
        }
    }
    
    public $case $label(){
        this.label = $ex.of();
        return this;
    }

    public boolean match( Node astNode ) {
        if (astNode instanceof SwitchEntry) {
            return matches((SwitchEntry) astNode);
        }
        return false;
    }

    public boolean matches( String... switchCase ){
        return select(switchCase) != null;
    }
    
    public boolean matches(SwitchEntry switchEntry ){
        return select(switchEntry) != null;
    }
    
    public $case $and(Predicate<SwitchEntry> constraint ){
        this.constraint = constraint;
        return this;
    }
    
    private Select selectStatements(SwitchEntry astSwitchEntry, Tokens tokens){
        if( statements.isEmpty()){
            return new Select( astSwitchEntry, $tokens.of(tokens) );
        }
        //if( astSwitchEntry.getStatements().size() >  ){

        //    return null;
        //}
        //if( statements.size() != astSwitchEntry.getStatements().size() ){
        //    return null;
        //}
        Tokens ts = Tokens.of(tokens);
            
        for(int i=0;i< this.statements.size();i++){

            //$stmt.Select ss =
            //    statements.get(i).select(astSwitchEntry.getStatement(i));
            $stmt.Select ss =
                    statements.get(i).selectFirstIn(astSwitchEntry);
            if( ss == null || !ts.isConsistent(ss.tokens.asTokens()) ){
                //System.out.println( "NOT CONSISTENT");
                return null;
            }
            ts.putAll(ss.tokens.asTokens());
        }
        return new Select(astSwitchEntry, $tokens.of(ts));
    }
    
    public Select select( String... switchCase ){
        return select( Ast.caseStmt(switchCase));
    }

    public String toString(){
        if( isMatchAny() ){
            return "$case{ $ANY$ }";
        }
        return "$case{ "+this.label.toString() + ":" +this.statements+ " }";
    }

    public boolean isMatchAny(){
        try{
            return this.constraint.test(null) && this.label.isMatchAny() && this.statements.isEmpty();
        } catch(Exception e){
            return false;
        }
    }

    public Select select( SwitchEntry astSwitchEntry ){
        if( ! constraint.test(astSwitchEntry)){
            System.out.println( "Failed constraint");
            return null;
        }
        if( astSwitchEntry.getLabels().isEmpty() ){
            if( this.label == null || this.label.isMatchAny()){
                System.out.println( "Selecting labelless "+ this.label);
                return selectStatements( astSwitchEntry, new Tokens());
            }
            System.out.println( "Failed constraint label "+ this.label);
            return null;
        }
        Expression label = astSwitchEntry.getLabels().get(0);
        if( this.label == null ){
            System.out.println( "Expected default (labells) "+ label);
            return null;
        }
        $ex.Select sel = this.label.select(label);
        
        if( sel != null ){
            return selectStatements(astSwitchEntry, sel.tokens.asTokens());
        }
        System.out.println("Not same label "+ this.label+" "+label);
        return null;
    }

    @Override
    public SwitchEntry firstIn(Node astStartNode, Predicate<SwitchEntry> caseMatchFn ) {
        Optional<SwitchEntry> ose = 
            astStartNode.findFirst(SwitchEntry.class, se ->{
                Select sel = select(se);
                return sel != null && caseMatchFn.test(se);
            });
        if( ose.isPresent() ){
            return ose.get();
        }
        return null;
    }

    @Override
    public Select selectFirstIn(Node astNode) {
        Optional<SwitchEntry> ose = 
            astNode.findFirst(SwitchEntry.class, se -> select(se) != null );
        if( ose.isPresent() ){
            return select(ose.get());
        }
        return null;
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn(Class clazz, Predicate<Select>selectConstraint ){
        return selectFirstIn( (_type)_java.type(clazz), selectConstraint);
    }
    
    /**
     * 
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn(_mrJava _j, Predicate<Select>selectConstraint ){
        if( _j instanceof _code ){
            _code _c = (_code)_j;
            if( _c.isTopLevel() ){
                return selectFirstIn(_c.astCompilationUnit(), selectConstraint);
            }
            _type _t = (_type)_j; //only possible
            return selectFirstIn(_t.ast(), selectConstraint);
        }
        return selectFirstIn( ((_node)_j).ast(), selectConstraint);
    }
    
    /**
     * 
     * @param astNode
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn(Node astNode, Predicate<Select>selectConstraint ){
        Optional<SwitchEntry> ose = 
            astNode.findFirst(SwitchEntry.class, se -> {
                Select sel = select(se);
                return sel != null && selectConstraint.test(sel);
            });
        if( ose.isPresent() ){
            return select(ose.get());
        }
        return null;
    }

    @Override
    public List<Select> listSelectedIn(Node astNode) {
        List<Select> found = new ArrayList<>();
        forEachIn(astNode, s-> found.add(select(s)));
        return found;        
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public <_J extends _mrJava> List<Select> listSelectedIn(_J _j, Predicate<Select> selectConstraint) {
        if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                return listSelectedIn(_c.astCompilationUnit(), selectConstraint);
            }
            _type _t = (_type) _j; //only possible
            return listSelectedIn(_t.ast(), selectConstraint);
        }
        return listSelectedIn((_node) _j, selectConstraint);
    }
    
    /**
     * 
     * @param astNode
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(Node astNode, Predicate<Select> selectConstraint) {
        List<Select> found = new ArrayList<>();
        forEachIn( astNode, s-> {
            Select sel = select(s);
            if( selectConstraint.test(sel ) ){
                found.add( sel );
            }
        });
        return found;        
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<SwitchEntry> _caseMatchFn, Consumer<SwitchEntry> _nodeActionFn) {
        astNode.walk(SwitchEntry.class, se-> {
            Select sel = select(se);
            if( sel != null && _caseMatchFn.test(se) ) {
                _nodeActionFn.accept(se);
            }
        });
        return astNode;
    }

    /**
     * 
     * @param clazz
     * @param selectActionFn
     * @return 
     */
    public  <_CT extends _type> _CT  forSelectedIn(Class clazz, Consumer<Select> selectActionFn) {
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectActionFn);
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public  <_CT extends _type> _CT forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectConstraint, selectActionFn);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectActionFn
     * @return 
     */
    public <_J extends _mrJava> _J forSelectedIn(_J _j, Consumer<Select> selectActionFn) {
        if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                forSelectedIn(_c.astCompilationUnit(), selectActionFn);
                return _j;
            }
            _type _t = (_type) _j; //only possible
            forSelectedIn(_t.ast(), selectActionFn); //return the TypeDeclaration, not the CompilationUnit            
            return _j;
        }
        forSelectedIn(((_node) _j).ast(), selectActionFn);
        return _j;
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectMatchFn
     * @param selectActionFn
     * @return 
     */
    public <_J extends _mrJava> _J forSelectedIn(_J _j, Predicate<Select> selectMatchFn, Consumer<Select> selectActionFn) {
        if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                forSelectedIn(_c.astCompilationUnit(), selectMatchFn, selectActionFn);
                return _j;
            }
            _type _t = (_type) _j; //only possible
            forSelectedIn(_t.ast(), selectMatchFn, selectActionFn); //return the TypeDeclaration, not the CompilationUnit            
            return _j;
        }
        forSelectedIn(((_node) _j).ast(), selectMatchFn, selectActionFn);
        return _j;
    }
    
    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param selectActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode, Consumer<Select> selectActionFn) {
        astRootNode.walk(SwitchEntry.class, se-> {
            Select sel = select(se);
            if( sel != null ) {
                selectActionFn.accept(sel);
            }
        });
        return astRootNode;
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectMatchFn
     * @param selectActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Predicate<Select> selectMatchFn, Consumer<Select> selectActionFn) {
        astNode.walk(SwitchEntry.class, se-> {
            Select sel = select(se);
            if( sel != null && selectMatchFn.test(sel) ) {
                selectActionFn.accept(sel);
            }
        });
        return astNode;
    }
    
    @Override
    public SwitchEntry draft(Translator translator, Map<String, Object> keyValues) {
        SwitchEntry se = new SwitchEntry();
        //Parameteric override
        if( keyValues.get("$case") != null ){
            $case $a = $case.of( keyValues.get("$case").toString() );
            Map<String,Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$case"); //remove to avoid stackOverflow
            return $a.draft(translator, kvs);
        }
        //parameteric override of the label
        if( keyValues.get("$label") != null ){
            Object ll = keyValues.get("$label" );
            if( ll instanceof $ex){
                NodeList<Expression> labels = new NodeList<>();
                labels.add( (($ex) ll).draft(translator, keyValues) );
                se.setLabels(labels);                 
            } else{
                NodeList<Expression> labels = new NodeList<>();
                labels.add( $ex.of(ll.toString()).draft( translator, keyValues) );
                se.setLabels(labels);                                 
            }                
        } 
        else if( this.label != null ){
            NodeList<Expression> labels = new NodeList<>();
            labels.add( this.label.draft(translator, keyValues) );
            se.setLabels(labels); 
        }        
        if( keyValues.get("$statements") != null ){
            //System.out.println( "has Statements");
            Object ll = keyValues.get("$statements" );
            if( ll instanceof Statement ){
                se.addStatement( $stmt.of((Statement) ll).draft(translator, keyValues) );
            } else if( ll instanceof $stmt ){
                se.addStatement( (($stmt) ll).draft(translator, keyValues) );
            } else if( ll instanceof $stmt[]) {
                $stmt[] sts = ($stmt[])ll;
                Arrays.stream(sts).forEach( s-> se.addStatement(s.draft(translator, keyValues)) );
            } else if( ll instanceof Statement[]) {
                Arrays.stream( (Statement[])ll).forEach( s-> se.addStatement($stmt.of(s).draft(translator, keyValues)) );
            } else {
                //just toString the thing 
                BlockStmt bs = Ast.blockStmt( (String)(ll.toString()) );
                bs.getStatements().forEach(s -> se.addStatement( $stmt.of(s).draft(translator, keyValues)));
            }                     
        } else{
            this.statements.forEach(st -> se.addStatement( st.draft(translator, keyValues) ) );
        }
        return se;
    }

    @Override
    public $case $(String target, String $paramName) {
        if( this.label != null ){
            this.label.$(target, $paramName);
        }
        this.statements.forEach(s -> s.$(target, $paramName));
        return this;
    }

    @Override
    public $case hardcode$(Translator translator, Tokens kvs) {
        this.label = this.label.hardcode$(translator, kvs);
        List<$stmt> sts = new ArrayList<>();
        this.statements.forEach(st -> sts.add( st.hardcode$(translator, kvs)));
        this.statements = sts;
        return this;
    }

    @Override
    public List<String> list$() {
        List<String> all = new ArrayList<>();
        if( this.label != null ){
            all.addAll( this.label.list$() );
        }
        this.statements.forEach(s -> all.addAll(s.list$()));
        return all;
    }

    @Override
    public List<String> list$Normalized() {
        List<String> allN = new ArrayList<>();
        if( this.label != null ){
            allN.addAll( this.label.list$Normalized() );
        }
        this.statements.forEach(s -> allN.addAll(s.list$Normalized()));
        return allN.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Adds a constraint that the beforeExpression occurs in the same context/block before the target Expression
     * @param patternsOccurringBeforeThisNode
     * @return
     */
    public $case $isAfter( $pattern... patternsOccurringBeforeThisNode ){
        Predicate<SwitchEntry> prev = e -> $pattern.BodyScope.findPrevious(e, patternsOccurringBeforeThisNode) != null;
        return $and(prev);
    }

    /**
     * Adds a constraint that the beforeExpression occurs in the same context/block before the target Expression
     * @param patternsOccurringBeforeThisNode
     * @return
     */
    public $case $isNotAfter( $pattern... patternsOccurringBeforeThisNode ){
        Predicate<SwitchEntry> prev = e -> $pattern.BodyScope.findPrevious(e, patternsOccurringBeforeThisNode) != null;
        return $not(prev);
    }

    /**
     *
     * @param patternsOccurringAfterThisNode
     * @return
     */
    public $case $isBefore( $pattern... patternsOccurringAfterThisNode ){
        Predicate<SwitchEntry> prev = e -> $pattern.BodyScope.findNext(e, patternsOccurringAfterThisNode) != null;
        return $and(prev);
    }

    /**
     *
     * @param patternsOccurringAfterThisNode
     * @return
     */
    public $case $isNotBefore( $pattern... patternsOccurringAfterThisNode ){
        Predicate<SwitchEntry> prev = e -> $pattern.BodyScope.findNext(e, patternsOccurringAfterThisNode) != null;
        return $not(prev);
    }

    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $case{

        final List<$case>ors = new ArrayList<>();

        public Or($case...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $case hardcode$(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.hardcode$(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$case.Or{");
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
        public $case.Select select(SwitchEntry astNode){
            $case $a = whichMatch(astNode);
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
        public $case whichMatch(SwitchEntry ae){
            if( !this.constraint.test( ae ) ){
                return null;
            }
            Optional<$case> orsel  = this.ors.stream().filter( $p-> $p.match(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }

    public static class Select 
        implements $pattern.selected, selectAst<SwitchEntry> {
        
        public SwitchEntry astCase;
        public $tokens tokens;
        
        public Select( SwitchEntry astCase, $tokens $nv){
            this.astCase = astCase;
            this.tokens = $nv;
        }

        @Override
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public SwitchEntry ast() {
            return astCase;
        }   
        
        /**
         * The default case has empty labels
         * @return 
         */
        public boolean isDefaultCase(){
            return astCase.getLabels().isEmpty();
        }

        @Override
        public String toString(){
            return "$case.Select{"+ System.lineSeparator()+
                    Text.indent( astCase.toString() )+ System.lineSeparator()+
                    Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                    "}";
        }
    }     
}
