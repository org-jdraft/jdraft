/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.Ast;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class ReplaceLabeledStatementsTest extends TestCase {
    public void testLS(){
        class F{
            void m(int i){
                $log:{/*System.out.println("optional code running");*/}
                $log2:{
                    /*System.out.println(1);*/
                    /*System.out.println(2);*/
                }
            }            
        }
        
        TypeDeclaration td = Ast.type(F.class);
        td.walk(LabeledStmt.class, ls-> {
            if( ls.getLabel().asString().startsWith("$") && ls.getStatement().isBlockStmt()){
                BlockStmt bs = (BlockStmt)ls.getStatement();
                List<Comment> comments = bs.getOrphanComments();
                List<Statement> sts = new ArrayList<>();
                comments.forEach(c -> sts.add( StaticJavaParser.parseStatement(c.getContent())) );
                if( sts.size() == 1 ){
                    ls.replace( sts.get(0) );
                } else{
                    BlockStmt bsn = new BlockStmt();
                    sts.forEach(s -> bsn.addStatement(s));
                    ls.replace( bsn );
                }
            } 
        });
        
        //assertTrue( td.getMethodsByName("m").get(0). )
        System.out.println(td);        
    }
}
