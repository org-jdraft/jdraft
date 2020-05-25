package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MemberValuePair;
import org.jdraft.*;
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
public class $entryPair extends $baseBot<_entryPair, $entryPair>
        implements $bot<MemberValuePair, _entryPair, $entryPair> {

    /** key of the key/value pair*/
    public Select.$botSelect<$name, _entryPair, _name> key =
            Select.$botSelect.of(_entryPair.class, _name.class, "key", p -> _name.of(p.getNameNode()));

    /** value of the key/value pair */
    public Select.$botSelect<$expr, _entryPair, _expr> value =
            Select.$botSelect.of(_entryPair.class, _expr.class, "value", p -> p.getValue());

    public static $entryPair of(_entryPair _mv) {
        return new $entryPair(_mv.getName(), _mv.getValue().ast());
    }

    public static $entryPair of(Predicate<_entryPair> matchFn) {
        $entryPair $mv = new $entryPair();
        return $mv.$and(matchFn);
    }

    public static $entryPair of(Expression value) {
        return new $entryPair("value", value);
    }

    public static $entryPair of(String key, int value) {
        return new $entryPair(key, _intExpr.of(value));
    }

    public static $entryPair of(String key, boolean value) {
        return new $entryPair(key, _booleanExpr.of(value));
    }

    public static $entryPair of(String key, long value) {
        return new $entryPair(key, _longExpr.of(value));
    }

    public static $entryPair of(String key, float value) {
        return new $entryPair(key, _doubleExpr.of(value));
    }

    public static $entryPair of(String key, double value) {
        return new $entryPair(key, _doubleExpr.of(value));
    }

    public static $entryPair of(String key, Class value) {
        return new $entryPair(key, _classExpr.of(value));
    }

    public static $entryPair of(String key, String value) {
        return new $entryPair(key, value);
    }

    public static $entryPair of(String key, Expression exp) {
        return new $entryPair(key, exp);
    }

    public static $entryPair of() {
        return new $entryPair( );
    }

    public $entryPair copy() {
        $entryPair $copy = of();
        $copy.key = key.copy();
        $copy.value = value.copy(); //(($e)value).copy();
        return $copy;
    }

    @Override
    public Select<_entryPair> select(Node n) {
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

    @Override
    public List<Select.$feature<_entryPair, ?>> $listSelectors() {
        return Stream.of(this.key, this.value).collect(Collectors.toList());
    }

    private $entryPair() {
    }

    public $entryPair(String name, String value) {
        this(name, Exprs.of(value));
    }

    public $entryPair(String name, Expression value) {
        this.key.setBot($name.of(name));
        this.value.setBot($expr.of(value));
    }

    public $entryPair(String name, _expr value) {
        this.key.setBot($name.of(name));
        this.value.setBot($expr.of(value));
    }

    public $entryPair $not( $entryPair... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }


    @Override
    public _entryPair draft(Translator translator, Map<String, Object> keyValues) {
        _expr _ex = (_expr) this.value.draft(translator, keyValues);
        String key = this.key.draft(translator, keyValues).toString();
        return _entryPair.of(new MemberValuePair(key, _ex.ast()));
    }

    /**
     * @param pairs
     * @return
     */
    public Select selectFirst(List<_entryPair> pairs) {
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
        return false;
    }

    @Override
    public Select<_entryPair> selectFirstIn(Node astNode, Predicate<Select<_entryPair>> predicate) {
        Optional<Node> on = astNode.stream().filter(n -> {
            if (n instanceof MemberValuePair) {
                _entryPair _mv = _entryPair.of((MemberValuePair) n);
                Select<_entryPair> smv = select(_mv);
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
        if (predicate.test(_entryPair.of((String)onlyValueExpression.toString()))) {
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
        if (predicate.test(_entryPair.of(mvp))) {
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
    public Select<_entryPair> select(MemberValuePair mvp) {
        return select(_entryPair.of(mvp));
    }

    @Override
    public boolean matches(String candidate) {
        return matches(_entryPair.of(candidate));
    }
}
