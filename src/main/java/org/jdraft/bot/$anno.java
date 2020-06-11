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
public class $anno
        extends $botEnsemble<_anno, $anno>
        implements Template<_anno>,
        $bot.$node<AnnotationExpr, _anno, $anno>,
        $selector.$node<_anno, $anno>,
        $constructor.$part, $method.$part,
        $field.$part, $typeParameter.$part, $class.$part, $interface.$part, $enum.$part, $annotation.$part,
        $enumConstant.$part, $type.$part {

    public static $anno of() {
        return new $anno($id.of());
    }

    public static $anno of($id name, $annoEntryPair... memberValues) {
        return new $anno(name, memberValues);
    }

    public static $anno of(String codePattern) {
        return new $anno(_anno.of(codePattern));
    }

    public static $anno of(String... codePattern) {
        return new $anno(_anno.of(codePattern));
    }

    public static $anno of(Predicate<_anno> constraint) {
        return of().$and(constraint);
    }

    public static $anno of(String codePattern, Predicate<_anno> constraint) {
        return new $anno(_anno.of(codePattern)).$and(constraint);
    }

    public static $anno of(_anno _an) {
        return new $anno(_an);
    }

    public static $anno of(_anno _an, Predicate<_anno> constraint) {
        return new $anno(_an).$and(constraint);
    }

    public static $anno of(Class<? extends Annotation> sourceAnnoClass) {
        return new $anno(_anno.of(sourceAnnoClass));
    }

    public static $anno of(Class<? extends Annotation> sourceAnnoClass, Predicate<_anno> constraint) {
        return of(sourceAnnoClass).$and(constraint);
    }

    /**
     * @param anonymousObjectWithAnnotation
     * @return
     */
    public static $anno of(Object anonymousObjectWithAnnotation) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expr.newExpr(ste);
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty()).findFirst().get();
        return of(_anno.of(bd.getAnnotation(0)));
    }

    public static $anno.Or or(Class<? extends Annotation>... annClasses) {
        $anno[] $ac = new $anno[annClasses.length];
        for (int i = 0; i < annClasses.length; i++) {
            $ac[i] = $anno.of(annClasses[i]);
        }
        return or($ac);
    }

    public static $anno.Or or(_anno... anns) {
        $anno[] $ac = new $anno[anns.length];
        for (int i = 0; i < anns.length; i++) {
            $ac[i] = $anno.of(anns[i]);
        }
        return or($ac);
    }

    public static $anno.Or or($anno... $as) {
        return new Or($as);
    }

    public static $anno as(String... codePattern) {
        return as(_anno.of(codePattern));
    }

    public static $anno as(_anno _an) {
        $anno $a = of(_an);
        //add a constraint to verify there are EXACTLY only the same
        $a.$and(_a -> _an.listEntryPairs().size() == _a.listEntryPairs().size());
        return $a;
    }

    public $featureBot<_anno, String, $id> name =
            $featureBot.of(_anno.NAME);

    public $featureBotList<_anno, _annoEntryPair, $annoEntryPair> entryPairs =
            $featureBotList.of(_anno.ENTRY_PAIRS);

    public $anno copy() {
        $anno $copy = of(this.predicate.and(t -> true));
        $copy.name = this.name.copy();
        $copy.entryPairs = this.entryPairs.copy();
        return $copy;
    }

    public $anno $not($anno... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    /**
     * @param name
     * @param mvs
     */
    private $anno($id name, $annoEntryPair... mvs) {
        this.name.setBot(name);
        this.entryPairs.setBotList(Stream.of(mvs).collect(Collectors.toList()));
    }

    protected $anno() { }

    /**
     * @param proto
     */
    public $anno(_anno proto) {
        this.name.setBot($id.of(proto.getName()));
        AnnotationExpr astAnn = proto.ast();
        if (astAnn instanceof NormalAnnotationExpr) {
            NormalAnnotationExpr na = (NormalAnnotationExpr) astAnn;

            na.getPairs().forEach(mv -> entryPairs.add($annoEntryPair.of(mv.getNameAsString(), mv.getValue())));
        } else if (astAnn instanceof SingleMemberAnnotationExpr) {

            SingleMemberAnnotationExpr sa = (SingleMemberAnnotationExpr) astAnn;
            Stencil st = Stencil.of(sa.getMemberValue().toString());
            if (st.isMatchAny()) { //i.e. @A($any$) which matches @A, @A(1), @A(k=1), @A(k=1v=2)...
                entryPairs.setMatchAll(st.$list().get(0));
            } else {
                entryPairs.add($annoEntryPair.of(sa.getMemberValue()));
            }
        }
    }

    /**
     * @return
     */
    public $anno $name() {
        this.name.setBot(null);
        return this;
    }

    /**
     * updates the name of the prototype for matching and constructing
     *
     * @param name
     * @return
     */
    public $anno $name($id name) {
        this.name.setBot(name);
        return this;
    }

    /**
     * Updates the name prototype for matching and constructing
     *
     * @param name
     * @return
     */
    public $anno $name(String name) {
        this.name.setBot($id.of(name));
        return this;
    }

    /**
     *
     * @param $keyValuePairs
     * @return
     */
    public $anno $entryPairs(List<$annoEntryPair> $keyValuePairs) {
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
    public $anno $entryPair($annoEntryPair $keyValuePair) {
        this.entryPairs.add($keyValuePair);
        return this;
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public $anno $entryPair(String key, Expression value) {
        this.entryPairs.add(new $annoEntryPair(key, value));
        return this;
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public $anno $entryPair(String key, String value) {
        this.entryPairs.add(new $annoEntryPair(key, value));
        return this;
    }

    @Override
    public $anno $hardcode(Translator translator, Tokens kvs) {

        this.name.$hardcode(translator, kvs);

        Object val = kvs.get(this.entryPairs.getMatchAllName());
        if (val != null) {
            this.entryPairs.add($annoEntryPair.of(Expr.of(val.toString())));
            this.entryPairs.setMatchAll(false);
        }
        this.entryPairs.$hardcode(translator, kvs);
        return this;
    }

    @Override
    public List<$feature<_anno, ?, ?>> $listFeatures() {
        return Stream.of(this.name, this.entryPairs).collect(Collectors.toList());
    }

    public boolean matches(AnnotationExpr astAnno) {
        return select(astAnno) != null;
    }

    public boolean matches(String... anno) {
        return matches(_anno.of(anno));
    }

    public boolean matches(_anno _a) {
        return select(_a) != null;
    }

    public String draftToString(Object... values) {
        return draftToString(Translator.DEFAULT_TRANSLATOR, Tokens.of(values));
    }

    public String draftToString(Translator translator, Map<String, Object> keyValues) {
        if (keyValues.get("$annoExpr") != null) {
            //override parameter passed in
            $anno $a = $anno.of(keyValues.get("$annoExpr").toString());
            Map<String, Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$annoExpr"); //remove to avoid stackOverflow
            return $a.draftToString(translator, kvs);
        }
        _anno _a = _anno.of();
        _a.setName(name.draft(translator, keyValues).toString());
        _a.setEntryPairs(entryPairs.draft(translator, keyValues));
        return _a.toString();
    }

    @Override
    public _anno draft(Translator translator, Map<String, Object> keyValues) {
        if (keyValues.get("$annoExpr") != null) {
            //override parameter passed in
            $anno $a = $anno.of(keyValues.get("$annoExpr").toString());
            Map<String, Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$annoExpr"); //remove to avoid stackOverflow
            return $a.draft(translator, kvs);
        }
        return _anno.of(draftToString(translator, keyValues));
    }

    public boolean match(Node node) {
        if (node instanceof AnnotationExpr) {
            return matches((AnnotationExpr) node);
        }
        return false;
    }

    public Select<_anno> select(String str) {
        try {
            return select(Ast.annotationExpr(str));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_anno> select(Node n) {
        if (n instanceof AnnotationExpr) {
            return select((AnnotationExpr) n);
        }
        return null;
    }

    public Select<_anno> select(AnnotationExpr astAnn) {
        return select(_anno.of(astAnn));
    }

    public Select<_anno> select(String... anno) {
        return select(_anno.of(anno));
    }

    /**
     * Return a
     *
     * @param clazz
     * @param annoType
     * @return
     */
    public _type replaceIn(Class clazz, Class<? extends Annotation> annoType) {
        return replaceIn(clazz, $anno.of(annoType));
    }

    /**
     * @param <_J>
     * @param _j
     * @param annoType
     * @return
     */
    public <_J extends _tree._node> _J replaceIn(_J _j, Class<? extends Annotation> annoType) {
        return (_J) replaceIn(_j, (Template) $anno.of(annoType));
    }

    /**
     * @param <N>
     * @param astNode
     * @param annoType
     * @return
     */
    public <N extends Node> N replaceIn(N astNode, Class<? extends Annotation> annoType) {
        return replaceIn(astNode, $anno.of(annoType));
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
    public static class Or extends $anno {

        final List<$anno> $annoBots = new ArrayList<>();

        public Or($anno... $as) {
            super();
            this.name.setBot(null);
            //this.entryPairs = new Select.$botSetSelect(_annoExpr.class, _entryPair.class, "pairs", _ae -> ((_annoExpr) _ae).listPairs());
            Arrays.stream($as).forEach($a -> $annoBots.add($a));
        }

        /**
         * Build and return a copy of this or bot
         *
         * @return
         */
        public Or copy() {
            List<$anno> copyBots = new ArrayList<>();
            this.$annoBots.forEach(a -> copyBots.add(a.copy()));
            Or theCopy = new Or(copyBots.toArray(new $anno[0]));

            //now copy the predicate and all underlying bots on the baseBot
            theCopy.predicate = this.predicate.and(t -> true);
            theCopy.name = this.name.copy();
            theCopy.entryPairs = this.entryPairs.copy();
            return theCopy;
        }

        public List<String> $list() {
            List<String> list = new ArrayList<>();
            list.addAll(super.$list());
            this.$annoBots.forEach($ar -> list.addAll($ar.$list()));
            return list;
        }

        public List<String> $listNormalized() {
            List<String> list = new ArrayList<>();
            list.addAll(super.$listNormalized());
            this.$annoBots.forEach($ar -> list.addAll($ar.$listNormalized()));
            return list.stream().distinct().collect(Collectors.toList());
        }

        @Override
        public _anno fill(Object... vals) {
            throw new _jdraftException("Cannot draft/fill " + getClass() + " pattern" + this);
        }

        @Override
        public _anno fill(Translator tr, Object... vals) {
            throw new _jdraftException("Cannot draft/fill " + getClass() + " pattern" + this);
        }

        @Override
        public _anno draft(Translator tr, Map<String, Object> map) {
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
            $annoBots.forEach($a -> sb.append(Text.indent($a.toString())));
            sb.append("}");
            return sb.toString();
        }

        /**
         * @param _a
         * @return
         */
        public Select<_anno> select(_anno _a) {
            Select commonSelect = super.select(_a);
            if (commonSelect == null) {
                return null;
            }
            $anno $whichBot = whichMatch(_a);
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

        public List<$anno> $listOrSelectors() {
            return this.$annoBots;
        }

        public $anno.Or $hardcode(Translator tr, Tokens ts) {
            super.$hardcode(tr, ts);
            this.$annoBots.forEach($ar -> $ar.$hardcode(tr, ts));
            return this;
        }


        public $anno whichMatch(_anno _a) {
            return whichMatch(_a.ast());
        }

        /**
         * Return the underlying $anno that matches the AnnotationExpr or null if none of the match
         *
         * @param ae
         * @return
         */
        public $anno whichMatch(AnnotationExpr ae) {
            if (!this.predicate.test(_anno.of(ae))) {
                return null;
            }
            Optional<$anno> orsel = this.$annoBots.stream().filter($p -> $p.match(ae)).findFirst();
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
