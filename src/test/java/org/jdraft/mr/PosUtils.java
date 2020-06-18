package org.jdraft.mr;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import junit.framework.TestCase;
import org.jdraft._field;
import org.jdraft._method;

import java.util.Optional;
import java.util.stream.StreamSupport;

public class PosUtils extends TestCase {

    public void testFF() {
        String s = "public class A{\n\n\n"+ "}";
        CompilationUnit cu = StaticJavaParser.parse(s);
        //cu.recalculatePositions();

        //cu.walk(Node.class, n-> System.out.println(n.getClass()+ " "+ n.getRange().get()));

        cu.getType(0).addMember( _field.of("public static final int ID=100;").getFieldDeclaration() );
        cu.getType(0).addMember( _method.of("public static void main(String[] args){ System.out.println(1); }").node() );
        cu.recalculatePositions();

        //cu.walk(Node.class, n-> System.out.println(n.getClass()+ " "+ n.getRange().get()));
        //cu.recalculatePositions();
        //System.out.println( "***** RECALC");
        //cu.walk(Node.class, n-> System.out.println(n.getClass()+ " "+ n.getRange().get()));
        //PosUtil.recalcPositions(cu);
        System.out.println( "***** RECALC PosUtil ");
        cu.walk(Node.class, n-> System.out.println(n.getClass()+ " "+ n.getRange().get()));

        System.out.println( cu );

        CompilationUnit cuuf = StaticJavaParser.parse(cu.toString());

        cuuf.walk(Node.class, n-> System.out.println(n.getClass()+ " "+ n.getRange().get()));
    }

    public static class PosUtil {

        public static void recalcPositions(CompilationUnit cu) {
            recalcPositions(cu, new Position(1, 1));
        }

        public static void recalcPositions( Node n ){
            recalcPositions(n, new Position(1, 1));
        }

        private static Position recalcPositions(Node node, Position currentPosition) {
            Optional<TokenRange> tr = node.getTokenRange();
            if (!tr.isPresent() || currentPosition == null)
                return null;
            Position end = determineTokenRanges(currentPosition, tr.get());
            node.setRange(new Range(currentPosition, end));
            for (Node childNode : node.getChildNodes()) {
                currentPosition = recalcPositions(childNode, determineStartPosition(node, tr.get(), currentPosition));
            }
            return currentPosition;
        }

        private static Position determineStartPosition(Node node, TokenRange tokenRange, Position currentPosition) {
            Optional<TokenRange> tr = node.getTokenRange();
            if (!tr.isPresent())
                return null;
            JavaToken firstToken = tr.get().getBegin();
            return StreamSupport.stream(tr.get().spliterator(), false)
                    .filter(token -> token == firstToken).map(token -> token.getRange())
                    .filter(e -> e.isPresent()).map(e -> e.get().begin).findAny().orElse(null);
        }

        private static Position determineTokenRanges(Position currentPosition, TokenRange tokenRange) {
            for (JavaToken token : tokenRange) {
                Position end = token.getCategory() == JavaToken.Category.EOL ?
                        new Position(currentPosition.line + 1, 1) :
                        new Position(currentPosition.line, currentPosition.column + token.toString().length());
                token.setRange(new Range(currentPosition, end));
                currentPosition = end;
            }
            return currentPosition;
        }
    }

}
