/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft.pattern;

import org.jdraft.Expressions;
import org.jdraft._type;
import junit.framework.TestCase;

/**
 * I wanted to look at tools that do something like $pattern work in other languages
 * to get a feel for how intuitive it is to read/ write and build 
 * 
 * https://github.com/apple/swift-syntax#example
 * <PRE>
/// AddOneToIntegerLiterals will visit each token in the Syntax tree, and
/// (if it is an integer literal token) add 1 to the integer and return the
/// new integer literal token.
class AddOneToIntegerLiterals: SyntaxRewriter {
  override func visit(_ token: TokenSyntax) -> Syntax {
    // Only transform integer literals.
    guard case .integerLiteral(let text) = token.tokenKind else {
      return token
    }

    // Remove underscores from the original text.
    let integerText = String(text.filter { ("0"..."9").contains($0) })

    // Parse out the integer.
    let int = Int(integerText)!

    // Return a new integer literal token with `int + 1` as its text.
    return token.withKind(.integerLiteral("\(int + 1)"))
  }
}

let file = CommandLine.arguments[1]
let url = URL(fileURLWithPath: file)
let sourceFile = try SyntaxParser.parse(url)
let incremented = AddOneToIntegerLiterals().visit(sourceFile)
print(incremented)
 *</PRE> 
 * 
 * @author Eric
 */
public class vSwiftSyntaxTest extends TestCase {
    
    public void testUpdate(){
        class C{
           int x=2, y=3_000; 
           int b = 0b01;
           int hex = 0xDEAD;
        }
        _type _t = $.intLiteral().forEachIn(C.class,
            //i-> i.setInt( Integer.parseInt(i.getValue().replace("_", "")) +1) );    
            i-> i.ast().setInt( Expressions.parseInt(i.ast().getValue()) +1) );
        
        assertTrue( _t.getField("x").is("int x = 3") );
        assertTrue( _t.getField("y").is("int y = 3001") );
        
        assertTrue( _t.getField("b").is("int b = 2") );
        assertEquals( Expressions.parseInt( _t.getField("hex").getInit().asIntegerLiteralExpr().getValue() ),
            new Integer(0xDEAE) );
    }
    
}
