package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import org.jdraft._project;
import org.jdraft._java;
import org.jdraft._type;
import org.jdraft.io._batch;

import java.util.function.Consumer;

/**
 * a pre-programmed interaction for $bot(s) where we store the target $bot and the underlying
 * "action" {@link Consumer <Select<>>} so it can be predefined and reused against different sources
 *
 * (i.e. to convert all System.out.printlns to Log.debugs:
 * $refactoring $printToLog = $statement.refactor("System.out.println($any$);", "Log.debug($any$);");
 *
 * //given the code of MyClass.class convert all
 * //System.out.println(...) statements to
 * //Log.debug(...) statements
 * // and return the modified _type (containing the code)
 * _type _t = $printToLog.in(MyClass.class);
 *
 * @author Eric
 */
public interface $refactoring {

    /**
     * perform the refactoring on the source code of this clazz abnd return the modified _type
     * @param clazz the class for the source (NOTE: the .java source file must be on the classpath)
     * @return the modified source after the refactoring
     */
    _type in(Class  clazz);

    /**
     * perform the refactoring on the source code of this clazz and return the modified _type
     * @param clazzes the class for the source (NOTE: the .java source file must be on the classpath)
     * @return the modified source after the refactoring
     */
    _project in(Class... clazzes);

    /**
     * perform the refactoring on the source code of this clazz and return the modified astNode
     * @param astNode
     * @param <N>
     * @return
     */
    <N extends Node> N in(N astNode);

    /**
     * perform the refactoring on the source code of this _n and return the modified _node
     * @param _n
     * @param <_N>
     * @return
     */
    <_N extends _java._node> _N in(_N _n);

    /**
     * perform the refactoring on the source code within the given _batches and return all of the _codeUnits of the _batches
     * @param _batches the batches of source code
     * @return the _codeUnits for all of the java source from the batches
     */
    _project in(_batch... _batches);

    /**
     * perform the refactoring on the source code within the given _batches and return all of the _codeUnits of the _batches
     * @param _cus the array of _codeUnits to refactor
     * @return all of the codeUnits (including the refactored code)
     */
    _project in(_project... _cus);
}
