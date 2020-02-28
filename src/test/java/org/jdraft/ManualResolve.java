package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import junit.framework.TestCase;
import org.jdraft.pattern.$;
import org.jdraft.pattern.$var;

/**
 *
 * @author Eric
 */
public class ManualResolve extends TestCase {
    
    public static class D{
        public static int aStaticMethod(){
            return 102;
        }
        public void aMethod(){
            
        }
    }
    
    public static class C{
        public void param (D d){
            D.aStaticMethod();
            d.aMethod();
        }
        public void localInst(){
            D d = new D();
            d.aMethod();
        }
    }
    
    public void testMR(){
        _class _c = _class.of(C.class);
        _class _d = _class.of(D.class);
        
        _method _m = _c.getMethod("localInst");
        List<MethodCallExpr> mces = new ArrayList<>();
        Walk.in( _m, Ast.METHOD_CALL_EXPR, mce ->{
        //_m.forExprs(Ast.METHOD_CALL_EXPR, mce-> {
            Optional<Expression> scope = mce.getScope();
            if( scope.isPresent() ){
                System.out.println( "Scope is "+ scope.get());
                
                //I need to "Trace backwards"
                //and look for references to NodeWithParameters
                // variableDeclarator
                //look for a variable
                //look for a field
                //look for a parameter
                mce.walk(Node.TreeTraversal.PARENTS, p->{
                    System.out.println("AT "+ p.getClass() );
                    $var $vd = $.variable(v-> v.getNameAsString().equals(mce.getScope().get().toString()));
                    List<VariableDeclarator> vds = $vd.listIn(p);
                    if( !vds.isEmpty() ){
                        System.out.println( "Found "+ vds );
                    }
                    /*
                    System.out.println("Parent "+ p.getClass() );
                    if( p.getChildNodes().contains(mce )){
                        int ci = p.getChildNodes().indexOf( mce );
                        System.out.println("child index "+ ci);
                        //look at FORMER SIBLINGS 
                        for(int i=0; i<ci; i++ ){
                            Node n = p.getChildNodes().get(i);
                            if
                            if( n instanceof VariableDeclarator ){
                                
                            }
                        }    
                    }
                    if( p instanceof BlockStmt ){
                        
                    }
                    */
                });
            }
        });
    }
   
}
