package org.jdraft;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.stmt.Statement;
import junit.framework.TestCase;

public class _switchExprTest extends TestCase {

    public void testStaticParse(){
        JavaParser jp = new JavaParser(new ParserConfiguration().setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_14));
        ParseResult<Statement> ps = jp.parseStatement("int i = switch(a){ default -> 1; };");
        System.out.println( "PROBS " + ps.getProblems());
        assertTrue(ps.isSuccessful());
        Print.tree(ps.getResult().get());

        //StaticJavaParser.parseStatement("int i = switch(a){ default -> 1; };");
        //StaticJavaParser.parseExpression("switch(a){ default-> 1; }");

        //StaticJavaParser.parseStatement("int i = switch(a){ default: yield 1; };");
        //StaticJavaParser.parseExpression("switch(a){ default: yield 1; }");
    }
    public void testYieldParseAndUpdate(){
        // value breaks are superseded by Java 13 'yield' statements.
        //String[] sy = { "switch(a){ default: yield 1;}" };

        String[] sy ={
                "switch (mode) {",
                "    case \"a\", \"b\":",
                "        yield 1;",
                "    case \"c\":",
                "        yield 2;",
                "    case \"d\", \"e\", \"f\":",
                "        yield 3;",
                "    default:",
                "        yield -1;",
                "}"};


        _switchExpr _se = _switchExpr.of(sy);
        System.out.println( _se );
    }

    public void testWithArrows(){
        String[] sy ={
                "switch (mode) {",
                "    case \"a\", \"b\"-> 1;",
                "    case \"c\"-> 2;",
                "    case \"d\", \"e\", \"f\"-> 3;",
                "    default-> -1;",
                "}"};
        _switchExpr _se = _switchExpr.of(sy);
        assertEquals(4, _se.listCaseGroups().size()); //4 caseGroups

        System.out.println( _se );
    }
}
