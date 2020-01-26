package org.jdraft;

import com.github.javaparser.ast.stmt.Statement;

public interface _statement<S extends Statement, _S extends _statement> extends _node<S, _S>{

    /**
     * Refine the ast() method to be more strict (only return Statements)
     * @return
     */
    S ast();
}
