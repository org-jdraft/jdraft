package org.jdraft.proto;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import org.jdraft.Tokens;
import org.jdraft.Translator;
import org.jdraft._class;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Note... at the moment this is NOT a template... should it be??
 */
public final class $class
        implements $proto<_class, $class>, $proto.$java<_class,$class> {

    public Predicate<_class> constraint;

    public $comment<BlockComment> headerComment = $comment.blockComment();
    public $package packageDecl = $package.of();
    public List<$import> imports = new ArrayList<>();
    public $comment<JavadocComment>javadoc = $comment.javadocComment();
    public $annos annos = $annos.of();

    public $modifiers modifiers = $modifiers.of();
    public $typeParameters typeParameters = $typeParameters.of();
    public $id name = $id.of("$name$"); //name required

    //body parts
    public List<$constructor> ctors = new ArrayList<>();
    public List<$field> fields = new ArrayList<>();
    public List<$method> methods = new ArrayList<>();
    public List<$initBlock> initBlocks = new ArrayList<>();

    //nested types???

    /** marker interface for member entities that are part of the class */
    public interface $part{ }

    @Override
    public $class $and(Predicate<_class> constraint) {
        return null;
    }

    @Override
    public $class hardcode$(Translator translator, Tokens kvs) {
        return null;
    }

    @Override
    public boolean match(Node candidate) {
        return false;
    }

    @Override
    public _class firstIn(Node astStartNode, Predicate<_class> nodeMatchFn) {
        return null;
    }

    @Override
    public <S extends selected> S select(_class instance) {
        return null;
    }

    @Override
    public <S extends selected> S selectFirstIn(Node astNode) {
        return null;
    }

    @Override
    public <S extends selected> List<S> listSelectedIn(Node astNode) {
        return null;
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_class> nodeMatchFn, Consumer<_class> nodeActionFn) {
        return null;
    }

    @Override
    public Class<_class> javaType() {
        return _class.class;
    }

    public static class Select implements $proto.select_java<_class>{
        public _class selected;
        public $tokens tokens;

        @Override
        public $tokens tokens() {
            return null;
        }

        @Override
        public _class _node() {
            return selected;
        }
    }
}
