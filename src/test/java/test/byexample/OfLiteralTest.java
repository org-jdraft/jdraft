package test.byexample;

import junit.framework.TestCase;
import org.jdraft.*;

public class OfLiteralTest extends TestCase {

    /**
     * the model for Literals can be built using values
     */
    public void testBuildOfLiteral(){
        _expr._literal[] _literals = {
                _expr.of(1),
                _expr.of(1.2f),
                _expr.of(1.2d),
                _expr.of(true),
                _expr.of('c'),

                _intExpr.of(1),
                _doubleExpr.of(1.2d),
                _doubleExpr.of(1.5f),
                _booleanExpr.of(true),
                _charExpr.of('c'),
                _nullExpr.of(),
                _stringExpr.of("String"),
                _textBlockExpr.of("multi", "line", "Text", "Block")
        };
    }
}
