package com.github.javaparser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;

/**
 * A Post processor attached to the JavaParser to condense negative UnaryExpr + Number literals
 * to a single {@link IntegerLiteralExpr} or {@link DoubleLiteralExpr}
 *
 * Find all cases of a {@link UnaryExpr} with a {@link UnaryExpr.Operator#MINUS} as
 * parent of {@link IntegerLiteralExpr} or {@link DoubleLiteralExpr}
 * and replace with:
 * a single {@link IntegerLiteralExpr} with a negative number
 * a single {@link DoubleLiteralExpr} with a negative number
 *
 *  For example:
 *  <PRE>
 *      ...
 *       └─"-10000" UnaryExpr : (1,18)-(1,25)
 *         └─"10000" IntegerLiteralExpr : (1,21)-(1,25)
 *  </PRE>
 *  will be converted to:
 *  <PRE>
 *       └─"-10000" IntegerLiteralExpr : (1,28)-(1,25)
 *  </PRE>
 * //double negative?
 *              - -2 (will leave as is)
 *
 * //multi line -
 *               100
 *               number (will take the start position of the - and the end position of the number (100)
 */
public class NegativeLiteralNumberPostProcessor implements ParseResult.PostProcessor{

    @Override
    public void process(ParseResult<? extends Node> result, ParserConfiguration configuration) {
        doProcess(result, configuration);
    }

    /**
     * Since this method is stateless, made it a static method to enhance testability
     * @param result the result of the parse
     */
    public static void doProcess(ParseResult<? extends Node> result){
        doProcess(result, null);
    }

    /**
     * Since this method is stateless, made it a static method to enhance testability
     * @param result the result of the parse
     * @param configuration the parser configuration
     */
    public static void doProcess(ParseResult<? extends Node> result, ParserConfiguration configuration) {

        if( result.getResult().isPresent() ){
            Node top = result.getResult().get();
            if( top instanceof CompilationUnit){
                CompilationUnit cu = (CompilationUnit)top;
                if( !cu.getModule().isPresent() && !cu.getPackageDeclaration().isPresent() && cu.getTypes().isEmpty()){
                    return; //we cant process empty Modules (there are no nodes)
                }
            }
            top.stream(Node.TreeTraversal.POSTORDER)
                    .filter(n -> n instanceof UnaryExpr)
                    .forEach(e -> replaceUnaryWithNegativeLiteral((UnaryExpr) e));
        }
    }

    /**
     * find
     * @param ue
     * @return
     */
    public static void replaceUnaryWithNegativeLiteral( UnaryExpr ue ){
        if(ue.getOperator() == UnaryExpr.Operator.MINUS ){
            //if we have nested minuses (for some reason which isnt clear), then disregard this & just use
            // nested UnaryExpressions
            // i.e. "- - 2;"
            if( ue.getParentNode().isPresent()
                    && ue.getParentNode().get() instanceof UnaryExpr
                    && ((UnaryExpr) ue.getParentNode().get()).getOperator() == UnaryExpr.Operator.MINUS ){
                return;
            }
            if(ue.getExpression() instanceof IntegerLiteralExpr){
                IntegerLiteralExpr ile = ue.getExpression().asIntegerLiteralExpr();
                String intValue = ile.getValue();
                if( !intValue.startsWith("-")){
                    intValue = "-"+intValue;
                    IntegerLiteralExpr replacementInt = new IntegerLiteralExpr(intValue);
                    if( ile.getRange().isPresent() && ue.getRange().isPresent() ) {
                        Range oldUnaryRange = ue.getRange().get();
                        Range oldNumRange = ile.getRange().get();
                        //we "combine" the ranges from the start of the parent UnaryExpression to the End of the
                        // child (int number) end range (because the - may be on the previous line)
                        Range newRange = Range.range(oldUnaryRange.begin.line, oldUnaryRange.begin.column, oldNumRange.end.line, oldNumRange.end.column);
                        replacementInt.setRange(newRange);
                        ue.replace(replacementInt);
                    }
                } else{ ////double negative??
                    //in this case, leave it as a double unaryExpr
                }
            }
            else if(ue.getExpression() instanceof DoubleLiteralExpr) {
                DoubleLiteralExpr ile = ue.getExpression().asDoubleLiteralExpr();
                String doubleValue = ile.getValue();
                if( !doubleValue.startsWith("-")){
                    doubleValue = "-"+doubleValue;
                    DoubleLiteralExpr replacementDouble = new DoubleLiteralExpr(doubleValue);
                    if( ile.getRange().isPresent() && ue.getRange().isPresent() ) {
                        Range oldUnaryRange = ue.getRange().get();
                        Range oldRange = ile.getRange().get();
                        //we "combine" the ranges from the start of the parent UnaryExpression to the End of the
                        // child (int number) end range (because the - may be on the previous line)
                        Range newRange = Range.range(oldUnaryRange.begin.line, oldUnaryRange.begin.column, oldRange.end.line, oldRange.end.column);
                        replacementDouble.setRange(newRange);
                        ue.replace(replacementDouble);
                    }
                } else{ ////double negative??
                    //leave it as a double negate
                }
            }
        }
    }
}
