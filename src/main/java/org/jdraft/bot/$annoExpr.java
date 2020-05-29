package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.expr.*;
import org.jdraft.*;
import org.jdraft.pattern.*;
import org.jdraft.text.*;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * pattern for an annotation expression
 * i.e. @Deprecated, @Generated("12/14/2019")
 *
 * @author Eric
 */
public class $annoExpr
        extends $botEnsemble<_annoExpr, $annoExpr>
        implements Template<_annoExpr>,
        $bot.$node<AnnotationExpr, _annoExpr, $annoExpr>,
        $selector.$node<_annoExpr, $annoExpr>,
        $constructor.$part, $method.$part,
        $field.$part, $typeParameter.$part, $class.$part, $interface.$part, $enum.$part, $annotation.$part,
        $enumConstant.$part, $type.$part {

    public static $annoExpr of() {
        return new $annoExpr($id.of());
    }

    public static $annoExpr of($id name, $entryPair... memberValues) {
        return new $annoExpr(name, memberValues);
    }

    public static $annoExpr of(String codePattern) {
        return new $annoExpr(_annoExpr.of(codePattern));
    }

    public static $annoExpr of(String... codePattern) {
        return new $annoExpr(_annoExpr.of(codePattern));
    }

    public static $annoExpr of(Predicate<_annoExpr> constraint) {
        return of().$and(constraint);
    }

    public static $annoExpr of(String codePattern, Predicate<_annoExpr> constraint) {
        return new $annoExpr(_annoExpr.of(codePattern)).$and(constraint);
    }

    public static $annoExpr of(_annoExpr _an) {
        return new $annoExpr(_an);
    }

    public static $annoExpr of(_annoExpr _an, Predicate<_annoExpr> constraint) {
        return new $annoExpr(_an).$and(constraint);
    }

    public static $annoExpr of(Class<? extends Annotation> sourceAnnoClass) {
        return new $annoExpr(_annoExpr.of(sourceAnnoClass));
    }

    public static $annoExpr of(Class<? extends Annotation> sourceAnnoClass, Predicate<_annoExpr> constraint) {
        return of(sourceAnnoClass).$and(constraint);
    }

    /**
     * @param anonymousObjectWithAnnotation
     * @return
     */
    public static $annoExpr of(Object anonymousObjectWithAnnotation) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Exprs.newExpr(ste);
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty()).findFirst().get();
        return of(_annoExpr.of(bd.getAnnotation(0)));
    }

    public static $annoExpr.Or or(Class<? extends Annotation>... annClasses) {
        $annoExpr[] $ac = new $annoExpr[annClasses.length];
        for (int i = 0; i < annClasses.length; i++) {
            $ac[i] = $annoExpr.of(annClasses[i]);
        }
        return or($ac);
    }

    public static $annoExpr.Or or(_annoExpr... anns) {
        $annoExpr[] $ac = new $annoExpr[anns.length];
        for (int i = 0; i < anns.length; i++) {
            $ac[i] = $annoExpr.of(anns[i]);
        }
        return or($ac);
    }

    public static $annoExpr.Or or($annoExpr... $as) {
        return new Or($as);
    }

    public static $annoExpr as(String... codePattern) {
        return as(_annoExpr.of(codePattern));
    }

    public static $annoExpr as(_annoExpr _an) {
        $annoExpr $a = of(_an);
        //add a constraint to verify there are EXACTLY only the same
        $a.$and(_a -> _an.listPairs().size() == _a.listPairs().size());
        return $a;
    }

    public $featureBot<_annoExpr, String, $id> name =
            $featureBot.of(_annoExpr.NAME);

    public $featureBotList<_annoExpr, _entryPair, $entryPair> entryPairs =
            $featureBotList.of(_annoExpr.ENTRY_PAIRS);

    public $annoExpr copy() {
        $annoExpr $copy = of(this.predicate.and(t -> true));
        $copy.name = this.name.copy();
        $copy.entryPairs = this.entryPairs.copy();
        return $copy;
    }

    public $annoExpr $not( $annoExpr... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    /**
     * @param name
     * @param mvs
     */
    private $annoExpr($id name, $entryPair... mvs) {
        this.name.setBot(name);
        this.entryPairs.setBotList(Stream.of(mvs).collect(Collectors.toList()));
    }

    //internal (or) constructor
    protected $annoExpr() { }

    /**
     * @param proto
     */
    public $annoExpr(_annoExpr proto) {
        this.name.setBot($id.of(proto.getName()));
        AnnotationExpr astAnn = proto.ast();
        if (astAnn instanceof NormalAnnotationExpr) {
            NormalAnnotationExpr na = (NormalAnnotationExpr) astAnn;

            na.getPairs().forEach(mv -> entryPairs.add($entryPair.of(mv.getNameAsString(), mv.getValue())));
        } else if (astAnn instanceof SingleMemberAnnotationExpr) {

            SingleMemberAnnotationExpr sa = (SingleMemberAnnotationExpr) astAnn;
            Stencil st = Stencil.of(sa.getMemberValue().toString());
            if (st.isMatchAny()) { //i.e. @A($any$) which matches @A, @A(1), @A(k=1), @A(k=1v=2)...
                //System.out.println( "Setting match ALL to :\""+st.$list().get(0)+"\"");
                entryPairs.setMatchAll(st.$list().get(0));
            } else {
                entryPairs.add($entryPair.of(sa.getMemberValue()));
            }
        }
    }

    /**
     * @return
     */
    public $annoExpr $name() {
        this.name.setBot(null);
        return this;
    }

    /**
     * updates the name of the prototype for matching and constructing
     *
     * @param name
     * @return
     */
    public $annoExpr $name($id name) {
        this.name.setBot(name);
        return this;
    }

    /**
     * Updates the name prototype for matching and constructing
     *
     * @param name
     * @return
     */
    public $annoExpr $name(String name) {
        this.name.setBot($id.of(name));
        return this;
    }

    public $annoExpr $entryPairs(List<$entryPair> $keyValuePairs) {
        this.entryPairs.setBotList($keyValuePairs);
        return this;
    }

    /**
     * Adds an entryPair to the annotation (A KeyValue pair)
     * i.e.
     * <PRE>
     * $anno $a = $anno.of("A"); // @A
     * $a.$memberValue(new $memberValue("Name", "EE") );  //@A("name", "EE")
     * </PRE>
     *
     * @param $keyValuePair
     * @return
     */
    public $annoExpr $entryPair($entryPair $keyValuePair) {
        this.entryPairs.add($keyValuePair);
        return this;
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public $annoExpr $entryPair(String key, Expression value) {
        this.entryPairs.add(new $entryPair(key, value));
        return this;
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public $annoExpr $entryPair(String key, String value) {
        this.entryPairs.add(new $entryPair(key, value));
        return this;
    }

    @Override
    public $annoExpr $hardcode(Translator translator, Tokens kvs) {

        this.name.$hardcode(translator, kvs);

        Object val = kvs.get(this.entryPairs.getMatchAllName());
        if (val != null) {
            this.entryPairs.add($entryPair.of(Exprs.of(val.toString())));
            this.entryPairs.setMatchAll(false);
        }
        this.entryPairs.$hardcode(translator, kvs);
        return this;
    }

    @Override
    public List<$feature<_annoExpr, ?, ?>> $listFeatures() {
        return Stream.of(this.name, this.entryPairs).collect(Collectors.toList());
    }

    public boolean matches(AnnotationExpr astAnno) {
        return select(astAnno) != null;
    }

    public boolean matches(String... anno) {
        return matches(_annoExpr.of(anno));
    }

    public boolean matches(_annoExpr _a) {
        return select(_a) != null;
    }

    public String draftToString(Object... values) {
        return draftToString(Translator.DEFAULT_TRANSLATOR, Tokens.of(values));
    }

    public String draftToString(Translator translator, Map<String, Object> keyValues) {
        if (keyValues.get("$anno") != null) {
            //override parameter passed in
            $annoExpr $a = $annoExpr.of(keyValues.get("$anno").toString());
            Map<String, Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$anno"); //remove to avoid stackOverflow
            return $a.draftToString(translator, kvs);
        }
        _annoExpr _a = _annoExpr.of();
        _a.setName(name.draft(translator, keyValues).toString());
        _a.setPairs(entryPairs.draft(translator, keyValues));
        return _a.toString();
    }

    @Override
    public _annoExpr draft(Translator translator, Map<String, Object> keyValues) {
        if (keyValues.get("$anno") != null) {
            //override parameter passed in
            $annoExpr $a = $annoExpr.of(keyValues.get("$anno").toString());
            Map<String, Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$anno"); //remove to avoid stackOverflow
            return $a.draft(translator, kvs);
        }
        return _annoExpr.of(draftToString(translator, keyValues));
    }

    public boolean match(Node node) {
        if (node instanceof AnnotationExpr) {
            return matches((AnnotationExpr) node);
        }
        return false;
    }

    public Select<_annoExpr> select(String str) {
        try {
            return select(Ast.annotationExpr(str));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_annoExpr> select(Node n) {
        if (n instanceof AnnotationExpr) {
            return select((AnnotationExpr) n);
        }
        return null;
    }

    public Select<_annoExpr> select(AnnotationExpr astAnn) {
        return select(_annoExpr.of(astAnn));
    }

    public Select<_annoExpr> select(String... anno) {
        return select(_annoExpr.of(anno));
    }

    /**
     * Return a
     *
     * @param clazz
     * @param annoType
     * @return
     */
    public _type replaceIn(Class clazz, Class<? extends Annotation> annoType) {
        return replaceIn(clazz, $annoExpr.of(annoType));
    }

    /**
     * @param <_J>
     * @param _j
     * @param annoType
     * @return
     */
    public <_J extends _java._node> _J replaceIn(_J _j, Class<? extends Annotation> annoType) {
        return (_J) replaceIn(_j, (Template) $annoExpr.of(annoType));
    }

    /**
     * @param <N>
     * @param astNode
     * @param annoType
     * @return
     */
    public <N extends Node> N replaceIn(N astNode, Class<? extends Annotation> annoType) {
        return replaceIn(astNode, $annoExpr.of(annoType));
    }

    /**
     * builds and returns a toString representation of the $annoExpr
     */
    @Override
    public String toString() {
        if (this.isMatchAny()) {
            return "$annoExpr{ [MATCH ANY] }";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("@");
        sb.append(this.name.bot.stencil);
        if (this.entryPairs.isMatchAll()) {
            return "$annoExpr{ " + sb.toString() + "([MATCH ALL]) }";
        }
        if (this.entryPairs.botList.isEmpty()) {
            return "$annoExpr{ " + sb.toString() + " }";
        }

        sb.append("( " + this.entryPairs.toString() + ")");
        return "$annoExpr{" + System.lineSeparator() + sb.toString() + "}";
    }

    /**
     * An Or entity that can match against any of the $bot instances provided
     * NOTE: template features (draft/fill) are suppressed.
     */
    public static class Or extends $annoExpr {

        final List<$annoExpr> $annoExprBots = new ArrayList<>();

        public Or($annoExpr... $as) {
            super();
            this.name.setBot(null);
            //this.entryPairs = new Select.$botSetSelect(_annoExpr.class, _entryPair.class, "pairs", _ae -> ((_annoExpr) _ae).listPairs());
            Arrays.stream($as).forEach($a -> $annoExprBots.add($a));
        }

        /**
         * Build and return a copy of this or bot
         *
         * @return
         */
        public Or copy() {
            List<$annoExpr> copyBots = new ArrayList<>();
            this.$annoExprBots.forEach(a -> copyBots.add(a.copy()));
            Or theCopy = new Or(copyBots.toArray(new $annoExpr[0]));

            //now copy the predicate and all underlying bots on the baseBot
            theCopy.predicate = this.predicate.and(t -> true);
            theCopy.name = this.name.copy();
            theCopy.entryPairs = this.entryPairs.copy();
            return theCopy;
        }

        public List<String> $list() {
            List<String> list = new ArrayList<>();
            list.addAll(super.$list());
            this.$annoExprBots.forEach($ar -> list.addAll($ar.$list()));
            return list;
        }

        public List<String> $listNormalized() {
            List<String> list = new ArrayList<>();
            list.addAll(super.$listNormalized());
            this.$annoExprBots.forEach($ar -> list.addAll($ar.$listNormalized()));
            return list.stream().distinct().collect(Collectors.toList());
        }

        @Override
        public _annoExpr fill(Object... vals) {
            throw new _jdraftException("Cannot draft/fill " + getClass() + " pattern" + this);
        }

        @Override
        public _annoExpr fill(Translator tr, Object... vals) {
            throw new _jdraftException("Cannot draft/fill " + getClass() + " pattern" + this);
        }

        @Override
        public _annoExpr draft(Translator tr, Map<String, Object> map) {
            throw new _jdraftException("Cannot draft " + getClass() + " pattern" + this);
        }

        @Override
        public String draftToString(Object... vals) {
            throw new _jdraftException("Cannot draft " + getClass() + " pattern" + this);
        }

        @Override
        public String draftToString(Translator tr, Map<String, Object> map) {
            throw new _jdraftException("Cannot draft " + getClass() + " pattern" + this);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("$annoExpr.Or{");
            sb.append(System.lineSeparator());
            $annoExprBots.forEach($a -> sb.append(Text.indent($a.toString())));
            sb.append("}");
            return sb.toString();
        }

        /**
         * @param _a
         * @return
         */
        public Select<_annoExpr> select(_annoExpr _a) {
            Select commonSelect = super.select(_a);
            if (commonSelect == null) {
                return null;
            }
            $annoExpr $whichBot = whichMatch(_a);
            if ($whichBot == null) {
                return null;
            }
            Select whichSelect = $whichBot.select(_a);
            if (!commonSelect.tokens.isConsistent(whichSelect.tokens)) {
                return null;
            }
            whichSelect.tokens.putAll(commonSelect.tokens);
            return whichSelect;
        }

        public boolean isMatchAny() {
            return false;
        }

        public List<$annoExpr> $listOrSelectors() {
            return this.$annoExprBots;
        }

        public $annoExpr.Or $hardcode(Translator tr, Tokens ts) {
            super.$hardcode(tr, ts);
            this.$annoExprBots.forEach($ar -> $ar.$hardcode(tr, ts));
            return this;
        }


        public $annoExpr whichMatch(_annoExpr _a) {
            return whichMatch(_a.ast());
        }

        /**
         * Return the underlying $anno that matches the AnnotationExpr or null if none of the match
         *
         * @param ae
         * @return
         */
        public $annoExpr whichMatch(AnnotationExpr ae) {
            if (!this.predicate.test(_annoExpr.of(ae))) {
                return null;
            }
            Optional<$annoExpr> orsel = this.$annoExprBots.stream().filter($p -> $p.match(ae)).findFirst();
            if (orsel.isPresent()) {
                return orsel.get();
            }
            return null;
        }

        /*
        public Tokens parse(_annoExpr _a) {
            $annoExpr $a = whichMatch(_a);
            if ($a != null) {
                return $a.parse(_a);
            }
            return null;
        }
         */
    }
}
