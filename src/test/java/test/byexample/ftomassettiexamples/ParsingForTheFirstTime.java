package test.byexample.ftomassettiexamples;

import org.jdraft.*;

import java.util.stream.Collectors;

/**
 * https://github.com/ftomassetti/analyze-java-code-examples/blob/master/src/main/java/me/tomassetti/examples/ParsingForFirstTime.java
 */
public class ParsingForTheFirstTime {

    public static void main(String[] args){
        _expr _e = _expr.of("1 + 2");

        _method _m = _method.of("boolean invert(boolean aFlag) { return !p; }");

        _class _c = _class.of( "class A { int aField;} ");

        _binaryExpr _be = (_binaryExpr)_e;//you can cast from _expression to impl type
        _binaryExpr _bb = _binaryExpr.of("1 + 2"); //if you already know the type, just create directly

        System.out.println(String.format("Binary expression: left=%s, right=%s, operator=%s",
                _bb.getLeft(), _bb.getRight(), _bb.getOperator()));

        System.out.println(String.format("Method declaration: modifiers=%s, name=%s, parameters=%s, returnType=%s",
                _m.getModifiers(), _m.getName(),
                _m.getParameters().list().stream().map(p -> p.getName()).collect(Collectors.toList()),
                _m.getTypeRef()));

        System.out.println(String.format("Class declaration: name=%s, nMembers=%s",
                _c.getName(), _c.listMembers().size()));
    }
}
