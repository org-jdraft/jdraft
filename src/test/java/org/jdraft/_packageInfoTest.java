/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _packageInfoTest extends TestCase {

    public void testPIs(){
        _packageInfo _pi = _packageInfo.of();

    }
    public void testPI(){
        //you can have a completely empty compilation Unit (i.e. packageinfo)
        CompilationUnit ast = Ast.of("");
        
        ast = Ast.of("package aaaa.bbbb;");
        
        List<Comment> comms = ast.getComments();
    }
    
}
