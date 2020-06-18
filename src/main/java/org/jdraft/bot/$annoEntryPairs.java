package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import org.jdraft.*;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class $annoEntryPairs
        extends $botEnsemble<_annoEntryPairs, $annoEntryPairs>
        implements $bot<_annoEntryPairs, $annoEntryPairs> {

    public static $annoEntryPairs of() {
        return new $annoEntryPairs();
    }

    public static $annoEntryPairs of($annoEntryPair $ar) {
        return new $annoEntryPairs($ar);
    }

    public static $annoEntryPairs of($annoEntryPair... $ars) {
        return new $annoEntryPairs($ars);
    }

    public static $annoEntryPairs of(String annos) {
        return of(_annos.of(annos));
    }

    public static $annoEntryPairs of(String... annos) {
        return of(_annos.of(annos));
    }

    public static $annoEntryPairs of(_anno _ar) {
        return of(_annos.of(_ar));
    }

    public static $annoEntryPairs of(_anno... _ars) {
        return of(_annos.of(_ars));
    }

    public static $annoEntryPairs of(_annos _annos) {
        $anno[] $ars = new $anno[_annos.size()];
        for (int i = 0; i < _annos.size(); i++) {
            $ars[i] = $anno.of(_annos.getAt(i));
        }
        return of($ars);
    }

    public static $annoEntryPairs of(NodeWithAnnotations nwa) {
        return of(_annos.of(nwa));
    }

    /**
     * @param anonymousObjectWithAnnotations
     * @return
     */
    public static $annoEntryPairs of(Object anonymousObjectWithAnnotations) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expr.newExpr(ste);
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty()).findFirst().get();
        return of(_annos.of(bd));
    }

    /**
     * I want EXACTLY these annotations (no more, no less)
     *
     * @param $ars
     * @return
     */
    public static $annoEntryPairs as($annoEntryPair... $ars) {
        return of($ars).$and(as -> as.size() == $ars.length);
    }

    public static $annoEntryPairs.Or or($annoEntryPairs... $as) {
        return new $annoEntryPairs.Or($as);
    }

    public $featureBotList<_annoEntryPairs, _annoEntryPair, $annoEntryPair> annosList =
            $featureBotList.of(_annos.ANNOS);

    public $annoEntryPairs($annoEntryPair... $ars) {
        Arrays.stream($ars).forEach($a -> annosList.add($a));
    }

    @Override
    public $annoEntryPairs copy() {
        $annoEntryPairs copy = of();
        copy.annosList = annosList.copy();
        copy.predicate = predicate.and(t -> true);
        return copy;
    }

    public $annoEntryPairs $not($annoEntryPairs... $sels) {
        return $not(t -> Stream.of($sels).anyMatch($s -> (($bot) $s).matches(t)));
    }

    @Override
    public Select<_annoEntryPairs> selectFirstIn(Node astNode, Predicate<Select<_annoEntryPairs>> predicate) {
        Optional<Node> on = astNode.stream().filter(nwa -> {
            if (nwa instanceof AnnotationExpr) {
                Select s = select(nwa);
                return s != null;
            }
            return false;
        }).findFirst();
        if (on.isPresent()) {
            return select((AnnotationExpr) on.get());
        }
        return null;
    }

    @Override
    public Select<_annoEntryPairs> select(Node n) {
        if (n instanceof AnnotationExpr) {
            return select(_annoEntryPairs.of((AnnotationExpr) n));
        }
        return null;
    }

    public Select<_annoEntryPairs> select(AnnotationExpr nwa) {
        return select(_annoEntryPairs.of(nwa));
    }

    public Select<_annoEntryPairs> select(String... candidate) {
        try {
            return select(_annoEntryPairs.of(candidate));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean matches(String candidate) {
        return select(candidate) != null;
    }

    @Override
    public List<$feature<_annoEntryPairs, ?, ?>> $listFeatures() {
        return Stream.of(this.annosList).collect(Collectors.toList());
    }

    @Override
    public boolean isMatchAny() {
        try {
            return this.annosList.isMatchAny() && this.predicate.test(null);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public _annoEntryPairs draft(Translator translator, Map<String, Object> keyValues) {
        List<_annoEntryPair> _as = annosList.draft(translator, keyValues);
        _annoEntryPairs _ars = _annoEntryPairs.of(_as.toArray(new _annoEntryPair[0]));

        if (this.predicate.test(_ars)) {
            return _ars;
        }
        return null;
    }

    /**
     * An Or entity that can match against any of the $bot instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $annoEntryPairs {

        final List<$annoEntryPairs> $annosBots = new ArrayList<>();

        public Or($annoEntryPairs... $as) {
            super();
            Arrays.stream($as).forEach($a -> $annosBots.add($a));
        }

        /**
         * Build and return a copy of this or bot
         *
         * @return
         */
        public Or copy() {
            List<$annoEntryPairs> copyBots = new ArrayList<>();
            this.$annosBots.forEach(a -> copyBots.add(a.copy()));
            Or theCopy = new Or(copyBots.toArray(new $annoEntryPairs[0]));

            //now copy the predicate and all underlying bots on the baseBot
            theCopy.predicate = this.predicate.and(t -> true);
            return theCopy;
        }

        @Override
        public List<String> $list() {
            return $annosBots.stream().map($a -> $a.$list()).flatMap(Collection::stream).collect(Collectors.toList());
        }

        @Override
        public List<String> $listNormalized() {
            return $annosBots.stream().map($a -> $a.$listNormalized()).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        }

        @Override
        public $annoEntryPairs.Or $hardcode(Translator translator, Tokens tokens) {
            $annosBots.stream().forEach($a -> $a.$hardcode(translator, tokens));
            return this;
        }

        @Override
        public _annoEntryPairs draft(Translator tr, Map<String, Object> map) {
            throw new _jdraftException("Cannot draft " + getClass() + " pattern" + this);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("$annoEntryPairs.Or{");
            sb.append(System.lineSeparator());
            $annosBots.forEach($a -> sb.append(Text.indent($a.toString())));
            sb.append("}");
            return sb.toString();
        }

        /**
         * @param _a
         * @return
         */
        public Select<_annoEntryPairs> select(_annoEntryPairs _a) {
            Select commonSelect = super.select(_a);
            if (commonSelect == null) {
                return null;
            }
            $annoEntryPairs $whichBot = whichMatch(_a);
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

        public List<$annoEntryPairs> $listOrSelectors() {
            return this.$annosBots;
        }

        public $annoEntryPairs whichMatch(_annoEntryPairs _a) {
            if (!this.predicate.test(_a)) {
                return null;
            }
            Optional<$annoEntryPairs> orsel = this.$annosBots.stream().filter($p -> $p.matches(_a)).findFirst();
            if (orsel.isPresent()) {
                return orsel.get();
            }
            return null;
        }
    }
}

