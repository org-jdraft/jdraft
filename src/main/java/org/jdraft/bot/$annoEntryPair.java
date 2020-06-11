package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import org.jdraft.*;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * bot for inspecting and mutating a member values pair (i.e. the key values inside the annotation)
 * i.e. @A(key="value")
 */
public class $annoEntryPair
        //extends $baseBot<_entryPair, $entryPair>
        extends $botEnsemble<_annoEntryPair, $annoEntryPair>
        implements $bot<_annoEntryPair, $annoEntryPair> {

    /** key of the key/value pair
    public Select.$botSelect<$name, _entryPair, _name> key =
            Select.$botSelect.of(_entryPair.class, _name.class, "key", p -> _name.of(p.getNameNode()));

    /** value of the key/value pair
    public Select.$botSelect<$expr, _entryPair, _expr> value =
            Select.$botSelect.of(_entryPair.class, _expr.class, "value", p -> p.getValue());
    */

    /** key of the key/value pair*/
    public $featureBot<_annoEntryPair, String, $id> key =
            $featureBot.of(_annoEntryPair.NAME);

    /** value of the key/value pair */
    public $featureBot<_annoEntryPair, _expr, ? extends $expr> value =
            $featureBot.of(_annoEntryPair.VALUE);

    public static $annoEntryPair of(_annoEntryPair _mv) {
        return new $annoEntryPair(_mv.getName(), _mv.getValue().ast());
    }

    public static $annoEntryPair of(Predicate<_annoEntryPair> matchFn) {
        $annoEntryPair $mv = new $annoEntryPair();
        return $mv.$and(matchFn);
    }

    public static $annoEntryPair of(Expression value) {
        return new $annoEntryPair("value", value);
    }

    public static $annoEntryPair of(String key, int value) {
        return new $annoEntryPair(key, _intExpr.of(value));
    }

    public static $annoEntryPair of(String key, boolean value) {
        return new $annoEntryPair(key, _booleanExpr.of(value));
    }

    public static $annoEntryPair of(String key, long value) {
        return new $annoEntryPair(key, _longExpr.of(value));
    }

    public static $annoEntryPair of(String key, float value) {
        return new $annoEntryPair(key, _doubleExpr.of(value));
    }

    public static $annoEntryPair of(String key, double value) {
        return new $annoEntryPair(key, _doubleExpr.of(value));
    }

    public static $annoEntryPair of(String key, Class value) {
        return new $annoEntryPair(key, _classExpr.of(value));
    }

    public static $annoEntryPair of(String key, String value) {
        return new $annoEntryPair(key, value);
    }

    public static $annoEntryPair of(String key, Expression exp) {
        return new $annoEntryPair(key, exp);
    }

    public static $annoEntryPair of() {
        return new $annoEntryPair( );
    }

    public $annoEntryPair copy() {
        $annoEntryPair $copy = of();
        $copy.key = key.copy();
        $copy.value = value.copy(); //(($e)value).copy();
        return $copy;
    }

    @Override
    public Select<_annoEntryPair> select(Node n) {
        if (n instanceof MemberValuePair) {
            return select((MemberValuePair) n);
        }
        return null;
    }

    public String compose(Translator translator, Map<String, Object> keyValues) {
        String k = null;
        if (!key.isMatchAny()) {
            k = key.draft(translator, keyValues).toString();
        }
        Expression v = ((_expr) value.draft(translator, keyValues)).ast();
        if (k == null || k.length() == 0) {
            return v.toString();
        }
        return k + "=" + v;
    }

    private $annoEntryPair() { }

    public $annoEntryPair(String name, String value) {
        this(name, Expr.of(value));
    }

    public $annoEntryPair(String name, Expression value) {
        if( name == null || name.length() == 0 ){
            this.key.setBot($id.or( $id.of("value"), $id.of().$and(n-> n== null) ) );
            this.value.setBot($expr.of(value));
        } else {
            this.key.setBot($id.of(name));
            this.value.setBot($expr.of(value));
        }
    }

    public $annoEntryPair(String name, _expr value) {
        if( name == null || name.length() == 0 ){
            this.key.setBot($id.or( $id.of("value"), $id.of().$and(n-> n== null) ) );
            this.value.setBot($expr.of(value));
        }else {
            this.key.setBot($id.of(name));
            this.value.setBot($expr.of(value));
        }
    }

    public $annoEntryPair $not($annoEntryPair... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    @Override
    public _annoEntryPair draft(Translator translator, Map<String, Object> keyValues) {
        _expr _ex = (_expr) this.value.draft(translator, keyValues);
        String key = this.key.draft(translator, keyValues).toString();
        return _annoEntryPair.of(new MemberValuePair(key, _ex.ast()));
    }

    /**
     * @param pairs
     * @return
     */
    public Select selectFirst(List<_annoEntryPair> pairs) {
        for (int i = 0; i < pairs.size(); i++) {
            Select sel = select(pairs.get(i));
            if (sel != null) {
                return sel;
            }
        }
        return null;
    }

    public boolean match(Node node) {
        if (node instanceof MemberValuePair) {
            return matches((MemberValuePair) node);
        }
        if( node instanceof Expression && node.getParentNode().isPresent() && node.getParentNode().get() instanceof SingleMemberAnnotationExpr ){
            return matches( _annoEntryPair.of( (SingleMemberAnnotationExpr) node.getParentNode().get() ));
        }
        return false;
    }

    public String toString(){
        return "$entryPair{" + System.lineSeparator()+
                Text.indent(this.key.toString())+ System.lineSeparator()+
                Text.indent(this.value.toString())+System.lineSeparator()+
                "}";
    }

    @Override
    public Select<_annoEntryPair> selectFirstIn(Node astNode, Predicate<Select<_annoEntryPair>> predicate) {
        Optional<Node> on = astNode.stream().filter(n -> {
            if (n instanceof MemberValuePair) {
                _annoEntryPair _mv = _annoEntryPair.of((MemberValuePair) n);
                Select<_annoEntryPair> smv = select(_mv);
                if (smv != null && predicate.test(smv)) {
                    return true;
                }
            }
            return false;
        }).findFirst();
        if (on.isPresent()) {
            return select((MemberValuePair) on.get());
        }
        return null;
    }

    /**
     * @param mvp
     * @return
     */
    public boolean matches(MemberValuePair mvp) {
        return select(mvp) != null;
    }

    /**
     * @param onlyValueExpression
     * @return
     */
    public Tokens parse(Expression onlyValueExpression) {
        if (predicate.test(_annoEntryPair.of((String)onlyValueExpression.toString()))) {
            //because values can be arrays we dont want to test for direct equality of the
            //expression, but rather whether we can select the expression from the Expression value
            // for example,
            // if I have the value $ex = $ex.of(1);
            // it SHOULD match against Ex.of( "{0,1,2,3}" );
            //System.out.println( value );
            //
            Select sel = value.getBot().selectFirstIn(onlyValueExpression);
            if (sel == null) {
                return null;
            }
            return sel.tokens;
        }
        return null;
    }

    /**
     * When we have an anno like
     *
     * @param onlyValueExpression
     * @return
     * @count(100) (a SingleMemberAnnotationExpr)
     */
    public Select select(Expression onlyValueExpression) {
        //System.out.println( "Selecting Only Value");
        MemberValuePair mvp = new MemberValuePair("", onlyValueExpression);
        if (predicate.test(_annoEntryPair.of(mvp))) {
            //because values can be arrays we dont want to test for direct equality of the
            //expression, but rather whether we can select the expression from the Expression value
            // for example,
            // if I have the value $ex = $ex.of(1);
            // it SHOULD match against Ex.of( "{0,1,2,3}" );
            //System.out.println( value );
            //System.out.println( mvp.getValue() );

            Select sel = value.getBot().selectFirstIn(mvp.getValue());

            if (sel != null) {
                return new Select(mvp, sel.tokens);
            }
        }
        return null;
    }

    /**
     * @param mvp
     * @return
     */
    public Select<_annoEntryPair> select(MemberValuePair mvp) {
        return select(_annoEntryPair.of(mvp));
    }

    @Override
    public boolean matches(String candidate) {
        return matches(_annoEntryPair.of(candidate));
    }

    @Override
    public List<$feature<_annoEntryPair, ?, ?>> $listFeatures() {
        return Stream.of(this.key, this.value).collect(Collectors.toList());
    }
}
