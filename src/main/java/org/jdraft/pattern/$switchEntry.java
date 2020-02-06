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
public class $switchEntry
    implements $pattern<_switchEntry, $switchEntry>, Template<_switchEntry>, $body.$part, $method.$part, $constructor.$part {

    public static $switchEntry of(){
        return new $switchEntry( $ex.any() );
    }

    public static $switchEntry of(Predicate<_switchEntry> constraint ){
        return of().$and(constraint);
    }

    public static $switchEntry of(String...acase ){
        return of( Ast.switchEntry(acase ));
    }

    public static $switchEntry of(SwitchEntry astSwitchEntry ){
        return new $switchEntry(astSwitchEntry );
    }
    
    public static $switchEntry of(SwitchEntry astSwitchEntry, Predicate<_switchEntry> constraint){
        return new $switchEntry(astSwitchEntry).$and(constraint);
    }

    public static $switchEntry of($stmt...stmts ){
        return of( $ex.any(), stmts );
    }

    public static $switchEntry of($ex expr, $stmt...stmts ){
        return new $switchEntry(expr, stmts);
    }

    public static $switchEntry.Or or(SwitchEntry... _protos ){
        $switchEntry[] arr = new $switchEntry[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $switchEntry.of( _protos[i]);
        }
        return or(arr);
    }

    public static $switchEntry.Or or($switchEntry...$tps ){
        return new $switchEntry.Or($tps);
    }

    public static $switchEntry as(String...st){
        return as( Ast.switchEntry(st));
    }

    public static $switchEntry as(SwitchEntry astSwitchEntry ){
        $switchEntry $c = of( astSwitchEntry );

        //need to make sure there are
        final List<$stmt>$sts = new ArrayList<>();
        NodeList<Statement> sts = astSwitchEntry.getStatements();
        for(int i=0;i<sts.size();i++){
            $sts .add( $stmt.of(sts.get(i)));
        }
        $c.$and( s-> {
            if (s.ast().getStatements().size() != astSwitchEntry.getStatements().size()) {
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

    public Predicate<_switchEntry> constraint = t-> true;
    
    public $ex label = $ex.any();
    
    public List<$stmt> statements = new ArrayList<>();

    $switchEntry(){
        this.label = null;
    }


    private $switchEntry($ex $labelExpr ){
        this.label = $labelExpr; 
    }
    
    private $switchEntry($ex $labelExpr, $stmt...stmts){
        this.label = $labelExpr;
        Arrays.stream(stmts).forEach(s -> this.statements.add(s));        
    }

    public $switchEntry(SwitchEntry se){
        if( se.getLabels().isNonEmpty() ){
            this.label = $ex.of( se.getLabels().get( 0 ) );
        } 
        for(int i=0;i<se.getStatements().size(); i++){
            this.statements.add($stmt.of(se.getStatements().get(i))); 
        }
    }
    
    public $switchEntry $label(){
        this.label = $ex.any();
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
    
    public $switchEntry $and(Predicate<_switchEntry> constraint ){
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

    public Select select( _switchEntry _se){
        return select(_se.ast());
    }

    public Select select( SwitchEntry astSwitchEntry ){
        if( ! constraint.test(_switchEntry.of(astSwitchEntry))){
            //System.out.println( "Failed constraint");
            return null;
        }
        if( astSwitchEntry.getLabels().isEmpty() ){
            if( this.label == null || this.label.isMatchAny()){
                //System.out.println( "Selecting labelless "+ this.label);
                return selectStatements( astSwitchEntry, new Tokens());
            }
            //System.out.println( "Failed constraint label "+ this.label);
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
    public _switchEntry firstIn(Node astStartNode, Predicate<_switchEntry> caseMatchFn ) {
        Optional<SwitchEntry> ose = 
            astStartNode.findFirst(SwitchEntry.class, se ->{
                Select sel = select(se);
                return sel != null && caseMatchFn.test(sel._se);
            });
        if( ose.isPresent() ){
            return _switchEntry.of(ose.get());
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
    public Select selectFirstIn(_java._domain _j, Predicate<Select>selectConstraint ){
        if( _j instanceof _compilationUnit){
            _compilationUnit _c = (_compilationUnit)_j;
            if( _c.isTopLevel() ){
                return selectFirstIn(_c.astCompilationUnit(), selectConstraint);
            }
            _type _t = (_type)_j; //only possible
            return selectFirstIn(_t.ast(), selectConstraint);
        }
        return selectFirstIn( ((_java._node)_j).ast(), selectConstraint);
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
    public <_J extends _java._domain> List<Select> listSelectedIn(_J _j, Predicate<Select> selectConstraint) {
        if( _j instanceof _compilationUnit){
            _compilationUnit _c = (_compilationUnit) _j;
            if( _c.isTopLevel() ){
                return listSelectedIn(_c.astCompilationUnit(), selectConstraint);
            }
            _type _t = (_type) _j; //only possible
            return listSelectedIn(_t.ast(), selectConstraint);
        }
        return listSelectedIn((_java._node) _j, selectConstraint);
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
    public <N extends Node> N forEachIn(N astNode, Predicate<_switchEntry> _caseMatchFn, Consumer<_switchEntry> _nodeActionFn) {
        astNode.walk(SwitchEntry.class, se-> {
            Select sel = select(se);
            if( sel != null && _caseMatchFn.test(sel._se) ) {
                _nodeActionFn.accept(sel._se);
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
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Consumer<Select> selectActionFn) {
        if( _j instanceof _compilationUnit){
            _compilationUnit _c = (_compilationUnit) _j;
            if( _c.isTopLevel() ){
                forSelectedIn(_c.astCompilationUnit(), selectActionFn);
                return _j;
            }
            _type _t = (_type) _j; //only possible
            forSelectedIn(_t.ast(), selectActionFn); //return the TypeDeclaration, not the CompilationUnit            
            return _j;
        }
        forSelectedIn(((_java._node) _j).ast(), selectActionFn);
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
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Predicate<Select> selectMatchFn, Consumer<Select> selectActionFn) {
        if( _j instanceof _compilationUnit){
            _compilationUnit _c = (_compilationUnit) _j;
            if( _c.isTopLevel() ){
                forSelectedIn(_c.astCompilationUnit(), selectMatchFn, selectActionFn);
                return _j;
            }
            _type _t = (_type) _j; //only possible
            forSelectedIn(_t.ast(), selectMatchFn, selectActionFn); //return the TypeDeclaration, not the CompilationUnit            
            return _j;
        }
        forSelectedIn(((_java._node) _j).ast(), selectMatchFn, selectActionFn);
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
    public _switchEntry draft(Translator translator, Map<String, Object> keyValues) {
        SwitchEntry se = new SwitchEntry();
        //Parameteric override
        if( keyValues.get("$case") != null ){
            $switchEntry $a = $switchEntry.of( keyValues.get("$case").toString() );
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
        return _switchEntry.of(se);
    }

    @Override
    public $switchEntry $(String target, String $paramName) {
        if( this.label != null ){
            this.label.$(target, $paramName);
        }
        this.statements.forEach(s -> s.$(target, $paramName));
        return this;
    }

    @Override
    public $switchEntry hardcode$(Translator translator, Tokens kvs) {
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
    public $switchEntry $isAfter($pattern... patternsOccurringBeforeThisNode ){
        Predicate<_switchEntry> prev = e -> $pattern.BodyScope.findPrevious(e.ast(), patternsOccurringBeforeThisNode) != null;
        return $and(prev);
    }

    /**
     * Adds a constraint that the beforeExpression occurs in the same context/block before the target Expression
     * @param patternsOccurringBeforeThisNode
     * @return
     */
    public $switchEntry $isNotAfter($pattern... patternsOccurringBeforeThisNode ){
        Predicate<_switchEntry> prev = e -> $pattern.BodyScope.findPrevious(e.ast(), patternsOccurringBeforeThisNode) != null;
        return $not(prev);
    }

    /**
     *
     * @param patternsOccurringAfterThisNode
     * @return
     */
    public $switchEntry $isBefore($pattern... patternsOccurringAfterThisNode ){
        Predicate<_switchEntry> prev = e -> $pattern.BodyScope.findNext(e.ast(), patternsOccurringAfterThisNode) != null;
        return $and(prev);
    }

    /**
     *
     * @param patternsOccurringAfterThisNode
     * @return
     */
    public $switchEntry $isNotBefore($pattern... patternsOccurringAfterThisNode ){
        Predicate<_switchEntry> prev = e -> $pattern.BodyScope.findNext(e.ast(), patternsOccurringAfterThisNode) != null;
        return $not(prev);
    }

    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $switchEntry {

        final List<$switchEntry>ors = new ArrayList<>();

        public Or($switchEntry...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $switchEntry hardcode$(Translator translator, Tokens kvs) {
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

        public $switchEntry.Select select(_switchEntry _se){
            return select(_se.ast());
        }

        /**
         *
         * @param astNode
         * @return
         */
        public $switchEntry.Select select(SwitchEntry astNode){
            $switchEntry $a = whichMatch(astNode);
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
        public $switchEntry whichMatch(SwitchEntry ae){
            if( !this.constraint.test( _switchEntry.of(ae) ) ){
                return null;
            }
            Optional<$switchEntry> orsel  = this.ors.stream().filter($p-> $p.match(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }

    public static class Select 
        implements $pattern.selected, selectAst<SwitchEntry> {
        
        //public SwitchEntry astCase;
        public _switchEntry _se;
        public $tokens tokens;
        
        public Select( SwitchEntry astCase, $tokens $nv){
            //this.astCase = astCase;
            this._se = _switchEntry.of(astCase);
            this.tokens = $nv;
        }

        @Override
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public SwitchEntry ast() {
            return _se.ast();
        }   
        
        /**
         * The default case has empty labels
         * @return 
         */
        public boolean isDefaultCase(){
            return _se.isDefault();
        }

        @Override
        public String toString(){
            return "$case.Select{"+ System.lineSeparator()+
                    Text.indent( _se.toString() )+ System.lineSeparator()+
                    Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                    "}";
        }
    }     
}
