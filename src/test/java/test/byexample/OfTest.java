package test.byexample;

import junit.framework.TestCase;
import org.jdraft.*;

public class OfTest extends TestCase {
    /**
     * By convention entities are built using the of() static method
     * (rather than using constructors)
     */
    public void testBuildOfNoArg(){
        _java._domain[] _instances = {
                _anno.of(),
                _annos.of(),
                _annotation.of(),
                _arguments.of(),
                _arrayAccess.of(),
                _arrayCreate.of(),
                _arrayDimension.of(),
                _arrayInitialize.of(),
                _assertStmt.of(),
                _assign.of(),
                _binaryExpression.of(),
                _blockStmt.of(),
                _blockComment.of(),
                _body.of(),
                _boolean.of(),
                _breakStmt.of(),
                _caseGroup.of(),
                _cast.of(),
                _catch.of(),
                _char.of(), // or "'c'"
                _class.of(),
                _classExpression.of(),
                _conditionalExpression.of(), //also called "ternary"
                _constant.of(), //enum constant declaration
                _constructor.of(),
                _constructorCallStmt.of(),
                _continueStmt.of(),
                _doStmt.of(),
                _double.of(), //this is for double precision doubles
                _emptyStmt.of(), // empty statement placeholders i.e. for(;;){}
                _enclosedExpression.of(),
                _enum.of(),
                _expressionStmt.of(),
                _field.of(),
                _fieldAccess.of(),
                _forEachStmt.of(),
                _forStmt.of(),
                _ifStmt.of(),
                _import.of(),
                _imports.of(),
                _initBlock.of(),
                _instanceOf.of(),
                _int.of(),
                _interface.of(),
                _javadocComment.of(),
                _labeledStmt.of(),
                _lambda.of(),
                _lineComment.of(),
                _localClassStmt.of(),
                _localVariables.of(),
                _long.of(),
                _method.of(),
                _methodCall.of(),
                _methodRef.of(),
                //_modifier.of(),
                _modifiers.of(),
                _moduleInfo.of(),
                _name.of(),
                _nameExpression.of(),
                _new.of(),
                _null.of(),
                _package.of(),
                _packageInfo.of(),
                _parameter.of(),
                _parameters.of(),
                //_qualifiedName.of(),
                _receiverParameter.of(),
                _returnStmt.of(),
                _string.of(),
                _super.of(),
                _switchEntry.of(),
                _switchExpression.of(),
                _switchStmt.of(),
                _synchronizedStmt.of(),
                _textBlock.of(),
                _this.of(),
                _throws.of(),
                _throwStmt.of(),
                _tryStmt.of(),
                _typeArguments.of(),
                _typeExpression.of(), //In <code>World::greet</code> the "World" is a TypeExpr
                _typeParameter.of(),
                _typeParameters.of(),
                //_typeRef.of(),
                _unary.of(),
                _variable.of(),
                _whileStmt.of(),
                _yieldStmt.of()
        };
    }
}
