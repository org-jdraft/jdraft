package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.UnaryExpr;
import org.jdraft._expr;
import org.jdraft._java._domain;
import org.jdraft._unaryExpr;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * $bot for matching/selecting/inspecting and modifying {@link _unaryExpr}s/ {@link UnaryExpr}s
 *
 * @author Eric
 */
public class $unaryExpr implements $bot.$node<UnaryExpr, _unaryExpr, $unaryExpr>,
        $selector.$node<_unaryExpr, $unaryExpr>,
        $expr<UnaryExpr, _unaryExpr, $unaryExpr> {

    public static $unaryExpr of() {
        return new $unaryExpr();
    }

    public static $unaryExpr of(_unaryExpr _i) {
        return new $unaryExpr(_i);
    }

    public static $unaryExpr of(_expr _e){
        return of().$expression(_e);
    }

    public static $unaryExpr of(UnaryExpr ile) {
        return new $unaryExpr(_unaryExpr.of(ile));
    }

    /**
     * Matches all binaryExpressions using EXACTLY this operator
     * @param operator the operator
     * @return the $binaryExpression bot to match
     */
    public static $unaryExpr of(UnaryExpr.Operator operator){
        return of().$operators(operator);
    }

    public static $unaryExpr not(UnaryExpr.Operator... operators){
        return of().$not(operators);
    }

    public static $unaryExpr bitwiseComplement(){
        return of( _unaryExpr.BITWISE_COMPLEMENT);
    }
    public static $unaryExpr logicalComplement(){
        return of( _unaryExpr.LOGICAL_COMPLEMENT );
    }
    public static $unaryExpr minus(){
        return of( _unaryExpr.MINUS );
    }
    public static $unaryExpr plus(){
        return of( _unaryExpr.PLUS );
    }
    public static $unaryExpr postDecrement(){
        return of( _unaryExpr.POST_DECREMENT );
    }
    public static $unaryExpr postIncrement(){
        return of( _unaryExpr.POST_INCREMENT );
    }
    public static $unaryExpr preDecrement(){
        return of( _unaryExpr.PRE_DECREMENT );
    }
    public static $unaryExpr preIncrement(){
        return of( _unaryExpr.PRE_INCREMENT );
    }

    /**
     * Matches all binaryExpressions using ANY ONE of the provided operators
     * @param operators
     * @return
     */
    public static $unaryExpr of(UnaryExpr.Operator... operators){
        return of().$operators(operators);
    }

    public static $unaryExpr of(String... code) {
        return of(_unaryExpr.of(code));
    }

    public static $unaryExpr.Or or($unaryExpr...$bs){
        return new $unaryExpr.Or($bs);
    }

    public Predicate<_unaryExpr> getPredicate(){
        return this.predicate;
    }

    /**
     * Build and return a new mutable copy of this bot
     * @return
     */
    public $unaryExpr copy(){
        $unaryExpr $copy = of( ).$and(this.predicate.and(t->true) );
        $copy.$and(this.predicate);
        $copy.operator = this.operator.copy();
        //$copy.excludedOperators.addAll( this.excludedOperators );
        $copy.expression = this.expression.copy();
        return $copy;
    }

    /**
     * ONLY match _binaryExpressions that use one of the provided operators
     * @param ops the provided operators to match against
     * @return the modified $binaryExpression
     */
    public $unaryExpr $operators(UnaryExpr.Operator... ops){
        this.operator.includeOnly(ops);
        //this.excludedOperators.addAll(ALL_OPERATORS);
        //Arrays.stream(ops).forEach( op -> this.excludedOperators.remove(op));
        return this;
    }

    @Override
    public $unaryExpr $hardcode(Translator translator, Tokens kvs) {
        this.expression.$hardcode(translator, kvs);
        return this;
    }

    public $unaryExpr setPredicate(Predicate<_unaryExpr> predicate){
        this.predicate = predicate;
        return this;
    }

    public $unaryExpr $and(Predicate<_unaryExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $unaryExpr $and(UnaryExpr.Operator...operators) {
        this.operator.include(operators);
        //Arrays.stream(operators).forEach( op -> this.excludedOperators.remove(op));
        return this;
    }

    public $unaryExpr $not(UnaryExpr.Operator...operators) {
        this.operator.exclude(operators);
        //Arrays.stream(operators).forEach( op -> this.operator.e.excludedOperators.add(op));
        return this;
    }

    public $unaryExpr $not(Predicate<_unaryExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public $unaryExpr $expression(String... expression ){
        this.expression.setBot( $expr.of(expression) );
        return this;
    }

    public $unaryExpr $expression(_expr _e){
        this.expression.setBot( $expr.of(_e) );
        return this;
    }

    public $unaryExpr $expression($expr $e){
        this.expression.setBot( $e );
        return this;
    }

    public $unaryExpr $expression(Class<? extends _expr>... implementationClasses ){
        this.expression.setBot( $expr.of(implementationClasses) );
        return this;
    }

    public Select<_unaryExpr> select(String code) {
        try {
            return select(_unaryExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_unaryExpr> select(String... code) {
        try {
            return select(_unaryExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_unaryExpr> select(Node n) {
        if (n instanceof UnaryExpr) {
            return select(_unaryExpr.of((UnaryExpr) n));
        }
        return null;
    }

    public Select<_unaryExpr> select(Expression e) {
        if (e instanceof UnaryExpr) {
            return select(_unaryExpr.of((UnaryExpr) e));
        }
        return null;
    }

    public Select<_unaryExpr> select(_domain _n) {
        if (_n instanceof _unaryExpr) {
            return select((_unaryExpr) _n);
        }
        return null;
    }

    public Select<_unaryExpr> select(_expr<?, ?> _e) {
        if (_e instanceof _unaryExpr) {
            return select((_unaryExpr) _e);
        }
        return null;
    }

    public Select<_unaryExpr> select(_unaryExpr _i) {
        if (predicate.test(_i) ) {// && !excludedOperators.contains(_i.getOperator())) {
            Tokens ts = Tokens.selectTokens(_i, this.expression, this.operator);
            if (ts != null) {
                return new Select<>(_i, ts);
            }
            return null;
        }
        return null;
    }

    public _unaryExpr instance(String... str) {
        return _unaryExpr.of(str);
    }

    @Override
    public _unaryExpr draft(Translator translator, Map<String, Object> keyValues) {
        _expr _l = this.expression.draft(translator, keyValues);

        UnaryExpr.Operator op = (UnaryExpr.Operator)this.operator.draft(translator, keyValues);
        return _unaryExpr.of(_l, op);
        /*
        if( this.excludedOperators.size() == ALL_OPERATORS.size() - 1){
            //only (1) available operator
            Optional<UnaryExpr.Operator> oo = ALL_OPERATORS.stream().filter(o -> !this.excludedOperators.contains(o)).findFirst();
            return _unary.of(_l, oo.get());
        }
        Object o = keyValues.get("$operator");

        if( o == null ){
            throw new _jdraftException("no \"$operator\" (BinaryExpr Operator) specified in $binaryExpression or in "+ keyValues);
        }
        if( o instanceof UnaryExpr.Operator){
            return _unary.of(_l, (UnaryExpr.Operator)o );
        }
        else{
            UnaryExpr.Operator bo = UnaryExpr.Operator.valueOf( o.toString() );
            if( bo == null ){
                throw new _jdraftException("invalid \"$operator\" (BinaryExpr Operator) "+ o);
            }
            return _unary.of(_l, bo);
        }
         */
    }

    @Override
    public $unaryExpr $(String target, String $Name) {
        this.expression.$(target, $Name);
        return this;
    }

    @Override
    public List<String> $list() {
        List<String> all = new ArrayList<>();
        all.addAll( this.expression.$list() );
        return new ArrayList<>();
    }

    @Override
    public List<String> $listNormalized() {
        List<String>norm = new ArrayList<>();
        norm.addAll( this.expression.$listNormalized());
        return norm.stream().distinct().collect(Collectors.toList());
    }

    public boolean isMatchAny() {
        if (this.operator.isMatchAny()  && this.expression.isMatchAny()) {
            try {
                return this.predicate.test(null);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static final Set<UnaryExpr.Operator> ALL_OPERATORS = Arrays.stream(UnaryExpr.Operator.values()).collect(Collectors.toSet());

    public String toString() {

        Set<UnaryExpr.Operator> ops = new HashSet<>();
        ops.addAll(ALL_OPERATORS);
        ops.removeAll(this.operator.excludedValues);
        return "$unary{" + System.lineSeparator() +
                Text.indent( this.expression.toString()) + System.lineSeparator() +
                Text.indent( ops.toString() )+ System.lineSeparator()
                +"}";
    }

    public Predicate<_unaryExpr> predicate = d -> true;

    public Select.$botFeature<$expr, _unaryExpr, _expr> expression =
            Select.$botFeature.of( _unaryExpr.class, _expr.class, "expression", u-> u.getExpression());

    public Select.$OneOfFeature<_unaryExpr> operator = new Select.$OneOfFeature<>(_unaryExpr.class, "operator", u-> u.getOperator(), ALL_OPERATORS, new HashSet<>());

    /*
    public Set<UnaryExpr.Operator> excludedOperators = new HashSet<>();

    public Select.$feature<_unary, UnaryExpr.Operator> operator =
            Select.$feature.of( _unary.class, UnaryExpr.Operator.class, "operator", b-> b.getOperator())
                    .setSelector( o -> {
                        if( excludedOperators.contains(o)){
                            return null;
                        }
                        return new Tokens();
                    });
    */
    public $unaryExpr() {
    }

    public $unaryExpr(_unaryExpr _i) {
        this.expression.setBot( $expr.of(_i.getExpression()) );
        this.operator.includeOnly(_i.getOperator() );
        //Arrays.stream (UnaryExpr.Operator.values()).forEach(o-> excludedOperators.add(o) );
        //excludedOperators.remove( _i.getOperator());
    }

    /**
     * An Or entity that can match against any of some number of instances
     * NOTE: this can be used as a selector but NOT as a Template
     */
    public static class Or extends $unaryExpr {

        public List<$unaryExpr> $unaryExpressions = new ArrayList<>();

        private Or($unaryExpr...bots){
            Arrays.stream(bots).forEach(n-> $unaryExpressions.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public Or copy(){
            Or $copy = $unaryExpr.or(new $unaryExpr[0]);
            $copy.$and(this.predicate);

            $copy.operator = this.operator.copy();
            //this.$unaryExpressions.forEach( ($b) -> $copy.$unaryExpressions.add($b.copy()));
            //$copy.excludedOperators.addAll( this.excludedOperators );
            $copy.expression = this.expression.copy();

            return $copy;
        }

        @Override
        public Select<_unaryExpr> select(_unaryExpr _candidate) {
            Select commonSelect = super.select(_candidate);
            if(  commonSelect == null){
                return null;
            }
            $unaryExpr $whichBot = whichMatch(_candidate);
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
        public $unaryExpr whichMatch(_unaryExpr _candidate){
            Optional<$unaryExpr> orsel  = this.$unaryExpressions.stream().filter($p-> $p.matches(_candidate) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }
}
