package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.SwitchNode;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface _switch<_S extends _switch> extends _java._domain {

    SwitchNode ast();

    boolean hasMultiLabelSwitchEntry();

    boolean isSwitchSelector(String... selectorExpression);

    boolean isSwitchSelector(_expr e);

    boolean isSwitchSelector(Expression e);

    _expr getSwitchSelector();

    _S setSwitchSelector(_expr _selectorExpression);

    _S setSwitchSelector(Expression switchSelector);

    _S setSwitchSelector(String... switchSelector);

    List<_switchCase> listSwitchEntries();

    List<_switchCase> listSwitchEntries(Predicate<_switchCase> matchFn);

    _switchCase getSwitchEntry(int index);

    int countSwitchEntries();

    default _S forSwitchEntries(Consumer<_switchCase> cs ){
        listSwitchEntries().forEach(cs);
        return (_S)this;
    }

    default _S forSwitchEntries(Predicate<_switchCase> matchFn, Consumer<_switchCase> cs ){
        listSwitchEntries(matchFn).stream().forEach(cs);
        return (_S)this;
    }

    List<_switchCases> listCaseGroups();

    List<_switchCases> listCaseGroups(Predicate<_switchCases>matchFn);

    default _S forCaseGroups(Consumer<_switchCases> cs ){
        listCaseGroups().forEach(cs);
        return (_S)this;
    }

    default _S forCaseGroups(Predicate<_switchCases> matchFn, Consumer<_switchCases> cs ){
        listCaseGroups(matchFn).stream().forEach(cs);
        return (_S)this;
    }

    boolean hasDefault();

    _switchCase getDefault();

    default boolean isDefault(_switchCase _se){
        _switchCase _fse = getDefault();
        return Objects.equals(_fse, _se);
    }

    default boolean isDefault(Predicate<_switchCase> matchFn){
        _switchCase _fse = getDefault();
        if( _fse == null ){
            return matchFn == null;
        }
        return matchFn.test( _fse);
    }

}
