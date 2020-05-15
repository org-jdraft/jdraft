package org.jdraft.bot;

import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import junit.framework.TestCase;
import org.jdraft._doubleExpr;
import org.jdraft._nullExpr;
import org.jdraft.io._io;
import org.jdraft.runtime._runtime;
import org.jdraft.text.Stencil;

public class buildTest extends TestCase {

    public void testBuildUsingTemplate() {
        //Template.replace
        //just read the source of the .class file Raw (Dont bother with creating the AST, just gimme a String)
        Stencil classStencil = Stencil.of( _io.in($nullExpr.class).getPath())
                .$(NullLiteralExpr.class.getSimpleName(), "astName",
                        _nullExpr.class.getSimpleName(), "_nodeName",
                        $nullExpr.class.getSimpleName(), "protoName");
        String drafted = classStencil.draft(
                "astName", DoubleLiteralExpr.class.getSimpleName(),
                "_nodeName", _doubleExpr.class.getSimpleName(),
                "protoName", "$double");

        _runtime.compile(drafted);
    }

    /*
    public void testBuildUsingPrototype(){
        $class $c = $class.of($null.class).$(
                NullLiteralExpr.class.getSimpleName(), "astName",
                _null.class.getSimpleName(), "_nodeName",
                $null.class.getSimpleName(), "protoName");

        _class _c = $c.draft(
                "astName", DoubleLiteralExpr.class.getSimpleName(),
                "_nodeName", _double.class.getSimpleName(),
                "protoName", "$double");
        //_c.addImports(Selected)
        //verify I can compile the class
        _runtime.compile(_c);
    }
     */

}
