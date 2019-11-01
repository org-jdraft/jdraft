package test.othertools;

import junit.framework.TestCase;

     /*
             * // this is a very SIMPLE way of calculating complexity, I'd argue itself is not a very good metric in this context
             * // IMHO we should take into account super.XXX this.XXX method calls (these add complexity)
             * // each method that can split control flow should take into account it's current depth
             * // and there should be a factor applied (a top level IF statement much less complex than a nested If statement)
             * // multiple return statements should add complexity
             * // thrown exceptions like return statements should add to complexity
             * // the amount of parameters and local variables adds to complexity
             *
 this code emulates it's behavior as a SIMPLE way ot testing code complexity
     *
     * int cyclomaticComplexity(MethodDec m) {
     *   result = 1;
     *   visit (m) {
     *     case (Stm)`do <Stm _> while (<Expr _>);`: result += 1;
     *     case (Stm)`while (<Expr _>) <Stm _>`: result += 1;
     *     case (Stm)`if (<Expr _>) <Stm _>`: result +=1;
     *     case (Stm)`if (<Expr _>) <Stm _> else <Stm _>`: result +=1;
     *     case (Stm)`for (<{Expr ","}* _>; <Expr? _>; <{Expr ","}*_>) <Stm _>` : result += 1;
     *     case (Stm)`for (<LocalVarDec _> ; <Expr? e> ; <{Expr ","}* _>) <Stm _>`: result += 1;
     *     case (Stm)`for (<FormalParam _> : <Expr _>) <Stm _>` : result += 1;
     *     case (Stm)`switch (<Expr _> ) <SwitchBlock _>`: result += 1;
     *     case (SwitchLabel)`case <Expr _> :` : result += 1;
     *     case (CatchClause)`catch (<FormalParam _>) <Block _>` : result += 1;
     *   }
     *   return result;
     * }
*/
public class EricMetricsTest extends TestCase {

         /**
          * I didbnt want to loose information about my frustration with how code complexity is calculated
          * so I created this class to store some comments
          */
    public void testNothing(){

    }
}
