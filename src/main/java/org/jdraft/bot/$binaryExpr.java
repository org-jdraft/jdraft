package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import org.jdraft._binaryExpr;
import org.jdraft._expr;
import org.jdraft._java._domain;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * $bot for matching/selecting/inspecting and modifying {@link _binaryExpr}s/ {@link BinaryExpr}s
 *
 * @author Eric
 */
public class $binaryExpr implements $bot.$node<BinaryExpr, _binaryExpr, $binaryExpr>,
        $selector.$node<_binaryExpr, $binaryExpr>,
        $expr<BinaryExpr, _binaryExpr, $binaryExpr> {

    public static $binaryExpr of() {
        return new $binaryExpr();
    }

    public static $binaryExpr of(_binaryExpr _i) {
        return new $binaryExpr(_i);
    }

    public static $binaryExpr of(BinaryExpr ile) {
        return new $binaryExpr(_binaryExpr.of(ile));
    }

    /**
     * Matches all binaryExpressions using EXACTLY this operator
     * @param operator the operator
     * @return the $binaryExpression bot to match
     */
    public static $binaryExpr of(BinaryExpr.Operator operator){
        return of().$operators(operator);
    }

    public static $binaryExpr and(){
        return of( _binaryExpr.AND );
    }

    public static $binaryExpr binaryAnd(){
        return of( _binaryExpr.BINARY_AND );
    }

    public static $binaryExpr binaryOr(){
        return of( _binaryExpr.BINARY_OR );
    }

    public static $binaryExpr divide(){
        return of( _binaryExpr.DIVIDE );
    }

    public static $binaryExpr equals(){
        return of( _binaryExpr.EQUALS);
    }
    public static $binaryExpr greater(){
        return of( _binaryExpr.GREATER );
    }
    public static $binaryExpr greaterEquals(){
        return of( _binaryExpr.GREATER_EQUALS );
    }
    public static $binaryExpr leftShift(){
        return of( _binaryExpr.LEFT_SHIFT );
    }
    public static $binaryExpr less(){
        return of( _binaryExpr.LESS );
    }
    public static $binaryExpr lessEquals(){
        return of( _binaryExpr.LESS_EQUALS );
    }
    public static $binaryExpr minus(){
        return of( _binaryExpr.MINUS );
    }
    public static $binaryExpr multiply(){
        return of( _binaryExpr.MULTIPLY );
    }
    public static $binaryExpr notEquals(){
        return of( _binaryExpr.NOT_EQUALS );
    }
    public static $binaryExpr or(){
        return of( _binaryExpr.OR );
    }
    public static $binaryExpr plus(){
        return of( _binaryExpr.PLUS);
    }
    public static $binaryExpr rem(){
        return of( _binaryExpr.REMAINDER);
    }
    public static $binaryExpr signedRightShift(){
        return of( _binaryExpr.SIGNED_RIGHT_SHIFT);
    }
    public static $binaryExpr unsignedRightShift(){
        return of( _binaryExpr.UNSIGNED_RIGHT_SHIFT);
    }
    public static $binaryExpr xor(){
        return of( _binaryExpr.XOR);
    }

    /**
     * Matches all binaryExpressions using ANY ONE of the provided operators
     * @param operators
     * @return
     */
    public static $binaryExpr of(BinaryExpr.Operator... operators){
        return of().$operators(operators);
    }

    public static $binaryExpr not(BinaryExpr.Operator... operators){
        return of().$not(operators);
    }

    public static $binaryExpr of(String... code) {
        return of(_binaryExpr.of(code));
    }

    public static $binaryExpr.Or or($binaryExpr...$bs){
        return new $binaryExpr.Or($bs);
    }

    public Predicate<_binaryExpr> getPredicate(){
        return this.predicate;
    }

    /**
     * Build and return a new mutable copy of this bot
     * @return
     */
    public $binaryExpr copy(){
        $binaryExpr $copy = of( ).$and(this.predicate.and(t->true) );
        $copy.$and(this.predicate);
        $copy.operator = this.operator.copy();
        //$copy.excludedOperators.addAll( this.excludedOperators );
        $copy.left = this.left.copy();
        $copy.right = this.right.copy();
        return $copy;
    }

    /**
     * ONLY match _binaryExpressions that use one of the provided operators
     * @param ops the provided operators to match against
     * @return the modified $binaryExpression
     */
    public $binaryExpr $operators(BinaryExpr.Operator... ops){
        this.operator.excludedValues.addAll( ALL_OPERATORS);
        this.operator.include(ops);
        //Arrays.stream(ops).forEach( op -> this.excludedOperators.remove(op));
        return this;
    }

    @Override
    public $binaryExpr $hardcode(Translator translator, Tokens kvs) {
        this.left.$hardcode(translator, kvs);
        this.right.$hardcode(translator, kvs);
        return this;
    }

    public $binaryExpr setPredicate(Predicate<_binaryExpr> predicate){
        this.predicate = predicate;
        return this;
    }

    public $binaryExpr $and(Predicate<_binaryExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $binaryExpr $and(BinaryExpr.Operator...operators) {
        this.operator.include(operators);
        //Arrays.stream(operators).forEach( op -> this.excludedOperators.remove(op));
        return this;
    }

    public $binaryExpr $not(BinaryExpr.Operator...operators) {
        this.operator.exclude(operators);
        //Arrays.stream(operators).forEach( op -> this.excludedOperators.add(op));
        return this;
    }

    public $binaryExpr $not(Predicate<_binaryExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public $binaryExpr $left(String... expression ){
        this.left.setBot( $expr.of(expression) );
        return this;
    }

    public $binaryExpr $left(_expr _e){
        this.left.setBot( $expr.of(_e) );
        return this;
    }

    public $binaryExpr $left($expr $e){
        this.left.setBot( $e );
        return this;
    }

    public $binaryExpr $left(Class<? extends _expr>... implementationClasses ){
        this.left.setBot( $expr.of(implementationClasses) );
        return this;
    }

    public $binaryExpr $right(Class<? extends _expr>... implementationClasses ){
        this.right.setBot( $expr.of(implementationClasses) );
        return this;
    }

    public $binaryExpr $right(String... expression ){
        this.right.setBot( $expr.of(expression) );
        return this;
    }

    public $binaryExpr $right(_expr _e){
        this.right.setBot( $expr.of(_e) );
        return this;
    }

    public $binaryExpr $right($expr $e){
        this.right.setBot( $e );
        return this;
    }

    public Select<_binaryExpr> select(String code) {
        try {
            return select(_binaryExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_binaryExpr> select(String... code) {
        try {
            return select(_binaryExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_binaryExpr> select(Node n) {
        if (n instanceof BinaryExpr) {
            return select(_binaryExpr.of((BinaryExpr) n));
        }
        return null;
    }

    public Select<_binaryExpr> select(Expression e) {
        if (e instanceof BinaryExpr) {
            return select(_binaryExpr.of((BinaryExpr) e));
        }
        return null;
    }

    public Select<_binaryExpr> select(_domain _n) {
        if (_n instanceof _binaryExpr) {
            return select((_binaryExpr) _n);
        }
        return null;
    }

    public Select<_binaryExpr> select(_expr<?, ?> _e) {
        if (_e instanceof _binaryExpr) {
            return select((_binaryExpr) _e);
        }
        return null;
    }

    public Select<_binaryExpr> select(_binaryExpr _i) {
        if (predicate.test(_i) ) {// && !excludedOperators.contains(_i.getOperator())) {
            Tokens ts = Tokens.selectTokens(_i, this.left, this.right, this.operator);
            if (ts != null) {
                return new Select<>(_i, ts);
            }
            return null;
        }
        return null;
    }

    public _binaryExpr instance(String... str) {
        return _binaryExpr.of(str);
    }

    @Override
    public _binaryExpr draft(Translator translator, Map<String, Object> keyValues) {
        _expr _l = this.left.draft(translator, keyValues);
        _expr _r = this.right.draft(translator, keyValues);
        BinaryExpr.Operator op = (BinaryExpr.Operator)this.operator.draft(translator, keyValues);
        /*
        if( this.operator.excludedValues.size() == ALL_OPERATORS.size() - 1){
            //only (1) available operator
            Optional<BinaryExpr.Operator> oo = ALL_OPERATORS.stream().filter(o -> !this.operator.excludedValues.contains(o)).findFirst();
            return _binaryExpression.of(_l, oo.get(), _r);
        }
        Object o = keyValues.get("$operator");

        if( o == null ){
            throw new _jdraftException("no \"$operator\" (BinaryExpr Operator) specified in $binaryExpression or in "+ keyValues);
        }
        if( o instanceof BinaryExpr.Operator){

        }
        */
        return _binaryExpr.of(_l, op, _r);
        /*
        else{
            BinaryExpr.Operator bo = BinaryExpr.Operator.valueOf( o.toString() );
            if( bo == null ){
                throw new _jdraftException("invalid \"$operator\" (BinaryExpr Operator) "+ o);
            }
            return _binaryExpression.of(_l, bo, _r);
        }
         */
    }

    @Override
    public $binaryExpr $(String target, String $Name) {
        this.left.$(target, $Name);
        this.right.$(target, $Name);
        return this;
    }

    @Override
    public List<String> $list() {
        List<String> all = new ArrayList<>();
        all.addAll( this.left.$list() );
        all.addAll( this.right.$list() );
        return new ArrayList<>();
    }

    @Override
    public List<String> $listNormalized() {
        List<String>norm = new ArrayList<>();
        norm.addAll( this.left.$listNormalized());
        norm.addAll( this.right.$listNormalized());
        return norm.stream().distinct().collect(Collectors.toList());
    }

    public boolean isMatchAny() {
        if (this.operator.isMatchAny() && this.left.isMatchAny() && this.right.isMatchAny()) {
            try {
                return this.predicate.test(null);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static final Set<BinaryExpr.Operator> ALL_OPERATORS = Arrays.stream(BinaryExpr.Operator.values()).collect(Collectors.toSet());

    public String toString() {

        Set<BinaryExpr.Operator> ops = new HashSet<>();
        ops.addAll(ALL_OPERATORS);
        //ops.removeAll(this.excludedOperators);

        ops.removeAll(operator.excludedValues);
        return "$binaryExpression{" + System.lineSeparator() +
                Text.indent( this.left.toString()) + System.lineSeparator() +
                Text.indent( ops.toString() )+ System.lineSeparator()+
                Text.indent( this.right.toString()) + System.lineSeparator()
                +"}";
    }

    public Predicate<_binaryExpr> predicate = d -> true;

    public Select.$botFeature<$expr, _binaryExpr, _expr> left =
            Select.$botFeature.of( _binaryExpr.class, _expr.class, "left", b-> b.getLeft());

    public Select.$botFeature<$expr, _binaryExpr, _expr> right =
            Select.$botFeature.of( _binaryExpr.class, _expr.class, "right", b-> b.getRight());

    public Select.$OneOfFeature<_binaryExpr> operator =
            new Select.$OneOfFeature(_binaryExpr.class, "operator", b-> ((_binaryExpr)b).getOperator(), ALL_OPERATORS, new HashSet<>());

    /*
    public Set<BinaryExpr.Operator> excludedOperators = new HashSet<>();

    public Select.$feature<_binaryExpression, BinaryExpr.Operator> operator =
            Select.$feature.of( _binaryExpression.class, BinaryExpr.Operator.class, "operator", b-> b.getOperator())
                    .setSelector( o -> {
                        if( excludedOperators.contains(o)){
                            return null;
                        }
                        return new Tokens();
                    });
    */
    public $binaryExpr() {
    }

    public $binaryExpr(_binaryExpr _i) {
        this.left.setBot( $expr.of(_i.getLeft()) );
        this.right.setBot( $expr.of(_i.getRight()) );
        this.operator.includeOnly( _i.getOperator() );

    }

    /**
     * An Or entity that can match against any of some number of instances
     * NOTE: this can be used as a selector but NOT as a Template
     */
    public static class Or extends $binaryExpr {

        public List<$binaryExpr> $binaryExprs = new ArrayList<>();

        private Or($binaryExpr...bots){
            Arrays.stream(bots).forEach(n-> $binaryExprs.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public Or copy(){
            Or $copy = $binaryExpr.or(new $binaryExpr[0]);
            $copy.$and(this.predicate);
            this.$binaryExprs.forEach( ($b) -> $copy.$binaryExprs.add($b.copy()));
            //$copy.excludedOperators.addAll( this.excludedOperators );
            $copy.operator = this.operator.copy();
            $copy.left = this.left.copy();
            $copy.right = this.right.copy();

            return $copy;
        }

        @Override
        public Select<_binaryExpr> select(_binaryExpr _candidate) {
            Select commonSelect = super.select(_candidate);
            if(  commonSelect == null){
                return null;
            }
            $binaryExpr $whichBot = whichMatch(_candidate);
            if( $whichBot == null ){
                    return null;
            }
            Select whichSelect = $whichBot.select(_candidate);
            if( !commonSelect.tokens.isConsistent(whichSelect.tokens)){
                return null;
            }
            whichSelect.tokens.putAll(commonSelect.tokens);
            return whichSelect;
        }

        /**
         * Return the underlying $arrayAccess that matches the _arrayAccess
         * (or null if none of the $arrayAccess match the candidate _arrayAccess)
         * @param _candidate
         * @return
         */
        public $binaryExpr whichMatch(_binaryExpr _candidate){
            Optional<$binaryExpr> orsel  = this.$binaryExprs.stream().filter($p-> $p.matches(_candidate) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }
}
