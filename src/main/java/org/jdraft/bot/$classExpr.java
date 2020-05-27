package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.ClassExpr;
import org.jdraft.Exprs;
import org.jdraft._classExpr;
import org.jdraft._java._domain;
import org.jdraft._typeRef;
import org.jdraft.text.Template;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class $classExpr implements $bot.$node<ClassExpr, _classExpr, $classExpr>,
        $selector.$node<_classExpr, $classExpr>,
        $expr<ClassExpr, _classExpr, $classExpr>, Template<_classExpr> {

    public static $classExpr of() {
        return new $classExpr();
    }

    public static $classExpr of(ClassExpr aae) {
        return new $classExpr(aae);
    }

    public static $classExpr of(_classExpr _aa) {
        return new $classExpr(_aa);
    }

    public static $classExpr of(String... code) {
        return new $classExpr(_classExpr.of(code));
    }

    public Predicate<_classExpr> getPredicate(){
        return this.predicate;
    }

    public $classExpr setPredicate(Predicate<_classExpr> predicate){
        this.predicate = predicate;
        return this;
    }

    public $classExpr $not( $classExpr... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    @Override
    public $classExpr $hardcode(Translator translator, Tokens kvs) {
        this.type.$hardcode(translator, kvs);
        return this;
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $classExpr copy(){
        $classExpr $c = of( ).$and(this.predicate.and(t->true) );
        $c.type = this.type.copy();
        return $c;
    }

    public boolean isMatchAny() {
        if (this.type.isMatchAny()) {
            try {
                return this.predicate.test(null);
            } catch (Exception e) {
            }
        }
        return false;
    }

    public _classExpr instance(String... s) {
        return _classExpr.of(s);
    }

    public boolean matches(Node node) {
        return select(node) != null;
    }

    public boolean matches(_domain _j) {
        return _j instanceof _classExpr && matches((_classExpr) _j);
    }

    public Select<_classExpr> select(String... str) {
        return select(Text.combine(str));
    }

    public Select<_classExpr> select(String str) {
        try {
            return select(Exprs.classExpr(str));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_classExpr> select(Node n) {
        if (n instanceof ClassExpr) {
            return select(_classExpr.of((ClassExpr) n));
        }
        return null;
    }

    public Select<_classExpr> select(_classExpr _aa) {
        if (this.predicate.test(_aa)) {
            Select s = this.type.select(_aa.getTypeRef());
            if (s == null) {
                return null;
            }
            return new Select<>(_aa, s.tokens);
        }
        return null;
    }

    public $classExpr $and(Predicate<_classExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $classExpr $not(Predicate<_classExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public $classExpr $(String target, String $name) {
        this.type.$(target, $name);
        return this;
    }

    @Override
    public $classExpr $hardcode(Translator translator, Map<String, Object> keyValues) {
        return this.$hardcode(translator, Tokens.of(keyValues));
    }

    public List<String> $list() {
        return this.type.$list();
    }

    public List<String> $listNormalized() {
        return this.type.$listNormalized();
    }

    public $classExpr $type($typeRef $t) {
        this.type = $t;
        return this;
    }

    public $classExpr $type(_typeRef _e) {
        this.type = $typeRef.of(_e);
        return this;
    }

    public $classExpr $type(String... astE) {
        this.type = $typeRef.of(Text.combine(astE));
        return this;
    }

    @Override
    public _classExpr draft(Translator translator, Map<String, Object> keyValues) {
        _classExpr _e = _classExpr.of();
        _e.setTypeRef(this.type.draft(translator, keyValues));
        return _e;
    }

    public _classExpr instance(ClassExpr astNode) {
        return _classExpr.of(astNode);
    }

    public String toString() {
        return "$classExpr{" + System.lineSeparator() + "    " + this.type.toString() + System.lineSeparator() + "}";
    }

    Predicate<_classExpr> predicate = (a) -> true;

    $typeRef type = $typeRef.of();

    public $classExpr() {
    }

    public $classExpr(ClassExpr e) {
        this.type = $typeRef.of(e.getType());
    }

    public $classExpr(_classExpr _e) {
        this.type = $typeRef.of(_e.getTypeRef());
    }
}
