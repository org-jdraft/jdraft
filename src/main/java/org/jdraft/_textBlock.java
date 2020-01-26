package org.jdraft;

import com.github.javaparser.ast.expr.TextBlockLiteralExpr;
import org.jdraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class _textBlock implements _expression<TextBlockLiteralExpr, _textBlock> {

    public static _textBlock of(){
        return new _textBlock( new TextBlockLiteralExpr());
    }
    public static _textBlock of( String...code){
        return new _textBlock(Ex.textBlockEx( code));
    }

    public TextBlockLiteralExpr ile;

    public _textBlock(TextBlockLiteralExpr ile){
        this.ile = ile;
    }

    @Override
    public _textBlock copy() {
        return new _textBlock(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        return is( new TextBlockLiteralExpr(Text.combine(stringRep)));
    }

    @Override
    public boolean is(TextBlockLiteralExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public TextBlockLiteralExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.LITERAL, this.ile.getValue());
        return comps;
    }

    public String getText(){
        return this.ile.getValue();
    }

    public String toString(){
        return this.ile.toString();
    }
}
