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
                _annoExpr.of(),
                _annoExprs.of(),
                _annotation.of(),
                _arguments.of(),
                _arrayAccessExpr.of(),
                _arrayCreateExpr.of(),
                _arrayDimension.of(),
                _arrayInitializeExpr.of(),
                _assertStmt.of(),
                _assignExpr.of(),
                _binaryExpr.of(),
                _blockStmt.of(),
                _blockComment.of(),
                _body.of(),
                _booleanExpr.of(),
                _breakStmt.of(),
                _caseGroup.of(),
                _castExpr.of(),
                _catch.of(),
                _charExpr.of(), // or "'c'"
                _class.of(),
                _classExpr.of(),
                _conditionalExpr.of(), //also called "ternary"
                _constant.of(), //enum constant declaration
                _constructor.of(),
                _constructorCallStmt.of(),
                _continueStmt.of(),
                _doStmt.of(),
                _doubleExpr.of(), //this is for double precision doubles
                _emptyStmt.of(), // empty statement placeholders i.e. for(;;){}
                _enclosedEx.of(),
                _enum.of(),
                _exprStmt.of(),
                _field.of(),
                _fieldAccessExpr.of(),
                _forEachStmt.of(),
                _forStmt.of(),
                _ifStmt.of(),
                _import.of(),
                _imports.of(),
                _initBlock.of(),
                _instanceOfExpr.of(),
                _intExpr.of(),
                _interface.of(),
                _javadocComment.of(),
                _labeledStmt.of(),
                _lambdaExpr.of(),
                _lineComment.of(),
                _localClassStmt.of(),
                _variablesExpr.of(),
                _longExpr.of(),
                _method.of(),
                _methodCallExpr.of(),
                _methodRefExpr.of(),
                //_modifier.of(),
                _modifiers.of(),
                _moduleInfo.of(),
                _name.of(),
                _nameExpr.of(),
                _newExpr.of(),
                _nullExpr.of(),
                _package.of(),
                _packageInfo.of(),
                _parameter.of(),
                _parameters.of(),
                //_qualifiedName.of(),
                _receiverParameter.of(),
                _returnStmt.of(),
                _stringExpr.of(),
                _superExpr.of(),
                _switchEntry.of(),
                _switchExpr.of(),
                _switchStmt.of(),
                _synchronizedStmt.of(),
                _textBlockExpr.of(),
                _thisExpr.of(),
                _throws.of(),
                _throwStmt.of(),
                _tryStmt.of(),
                _typeArguments.of(),
                _typeExpr.of(), //In <code>World::greet</code> the "World" is a TypeExpr
                _typeParameter.of(),
                _typeParameters.of(),
                //_typeRef.of(),
                _unaryExpr.of(),
                _variable.of(),
                _whileStmt.of(),
                _yieldStmt.of()
        };
    }
}
