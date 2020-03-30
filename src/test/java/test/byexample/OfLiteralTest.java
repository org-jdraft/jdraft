package test.byexample;

import junit.framework.TestCase;
import org.jdraft.*;

public class OfLiteralTest extends TestCase {

    /**
     * the model for Literals can be built using values
     */
    public void testBuildOfLiteral(){
        _expression._literal[] _literals = {
                _expression.of(1),
                _expression.of(1.2f),
                _expression.of(1.2d),
                _expression.of(true),
                _expression.of('c'),

                _int.of(1),
                _double.of(1.2d),
                _double.of(1.5f),
                _boolean.of(true),
                _char.of('c'),
                _null.of(),
                _string.of("String"),
                _textBlock.of("multi", "line", "Text", "Block")
        };
    }
}
