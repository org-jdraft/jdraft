package test.othertools;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.stmt.ReturnStmt;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TraversalTest extends TestCase {
    public void testTraverse(){
        CompilationUnit cu = StaticJavaParser.parse(
                "public class C{\n"+
                "    public int m(){\n"+
                "        int i = 100;\n"+
                "        i++;\n"+
                "        return i;\n"+ //<-- the i is here
                "    }\n"+
                "}");

        ReturnStmt rs = cu.findFirst(ReturnStmt.class).get(); //get "return i;" Statement
        Expression iInReturn = rs.getExpression().get(); //here I am getting "i" from "return i;" statement

        List<VariableDeclarator> declarations = new ArrayList<>();

        iInReturn.walk(Node.TreeTraversal.PARENTS, n -> {
            if(n instanceof BodyDeclaration){
                VariableDeclarator vd = findDeclaration("i", n);
                if( vd != null ){
                    if( !declarations.contains(vd) ){
                        System.out.println( "Found the declaration of i : "+ vd );
                        declarations.add(vd);
                    }
                }
            }
        });
        System.out.println(declarations);
    }


    /**
     * walks within the node to find the Declaration of a variable with the name var
     * @param varName
     * @param n
     * @return
     */
    public static VariableDeclarator findDeclaration(String varName, Node n){
        Optional<VariableDeclarator> ovd = n.findFirst(VariableDeclarator.class,
                vd-> vd.getNameAsString().equals(varName));
        if( ovd.isPresent() ){
            return ovd.get();
        }
        return null;
    }

    //track modifications to a variable
    //declaration
    //assignment
    //increment
    // decrement
    //passing to method??
}
