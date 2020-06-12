/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.othertools;

import org.jdraft.Expr;
import org.jdraft._type;
import junit.framework.TestCase;
import org.jdraft.bot.$intExpr;
import org.jdraft.bot.$refactoring;

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
public class SwiftSyntaxTest extends TestCase {

    /** This is all the code you need for refactoring */
    $refactoring $addOneToAllInts =
            //1) find all int literals
            $intExpr.of()
                    // for each selected get its value and update it by 1
                    .refactor(s-> s.select.setValue( s.select.getValue() + 1));


    public void testUseRefactoring(){

        //heres an example with a bunch of ints
        class C{
           int x=2, y=3_000;  //_ separators
           int b = 0b01; //binary
           int hex = 0xDEAD; //hex
        }
        //here apply the refactoring to all ints in the class
        _type _t = $addOneToAllInts.in(C.class);

        assertTrue( _t.fieldNamed("x").is("int x = 3") );
        assertTrue( _t.fieldNamed("y").is("int y = 3001") );
        
        assertTrue( _t.fieldNamed("b").is("int b = 2") );
        assertEquals( Expr.parseInt( _t.fieldNamed("hex").getInitNode().asIntegerLiteralExpr().getValue() ),
            new Integer(0xDEAE) );
    }
    
}
