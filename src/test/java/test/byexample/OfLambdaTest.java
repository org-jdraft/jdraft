package test.byexample;

import junit.framework.TestCase;
import org.jdraft.*;

import java.io.IOException;

public class OfLambdaTest extends TestCase {

    /** of( ()-> ) */
    public void testBuildOfLambda(){
        System.out.println( _binaryExpression.of( ()-> 1 +7 + 11 ) );

        //create a lambda version of this
        //_expression.of()

        _java._domain[] ol = {
                //expressions
                _assign.of( (Object a, Object b) -> a = b ),
                _arrayCreate.of( ()-> { String[][] s = new String[1][3];} ),
                _arrayInitialize.of( ()-> { int[] o = {1,3,5,7,9}; } ),
                _binaryExpression.of( ()-> 1 + 4 ),
                _body.of( (a)->{ System.out.println(a); }),
                _cast.of( o-> (String)o ),
                _caseGroup.of( (Integer o)-> {
                    switch(o){
                        case 1: case 3: case 5: return "Odd";
                    }
                    return "Even";
                }),
                _catch.of( ()-> {
                    try{
                        throw new IOException();
                    }
                    catch(IOException ioe){
                        throw new RuntimeException(ioe);
                    }
                }),


                //generic statements
                _statement.of( (a) -> System.out.println(a)),

                _assertStmt.of( ()-> {assert (1==1);} ),
                _parameters.of( (final Integer a, String... s)->{} ),
                _arguments.of( ()-> new Object[]{1, "Value", System.currentTimeMillis()} )
        };
    }
}
