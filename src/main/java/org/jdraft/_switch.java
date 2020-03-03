package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.SwitchNode;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface _switch<_S extends _switch> {

    SwitchNode ast();

    boolean hasMultiLabelSwitchEntry();

    boolean isSwitchSelector(String... selectorExpression);

    boolean isSwitchSelector(_expression e);

    boolean isSwitchSelector(Expression e);

    _expression getSwitchSelector();

    _S setSwitchSelector(_expression _selectorExpression);

    _S setSwitchSelector(Expression switchSelector);

    _S setSwitchSelector(String... switchSelector);

    List<_switchEntry> listSwitchEntries();

    List<_switchEntry> listSwitchEntries(Predicate<_switchEntry> matchFn);

    _switchEntry getSwitchEntry(int index);

    int countSwitchEntries();

    default _S forSwitchEntries(Consumer<_switchEntry> cs ){
        listSwitchEntries().forEach(cs);
        return (_S)this;
    }

    default _S forSwitchEntries(Predicate<_switchEntry> matchFn, Consumer<_switchEntry> cs ){
        listSwitchEntries(matchFn).stream().forEach(cs);
        return (_S)this;
    }

    List<_caseGroup> listCaseGroups();

    List<_caseGroup> listCaseGroups(Predicate<_caseGroup>matchFn);

    default _S forCaseGroups(Consumer<_caseGroup> cs ){
        listCaseGroups().forEach(cs);
        return (_S)this;
    }

    default _S forCaseGroups(Predicate<_caseGroup> matchFn, Consumer<_caseGroup> cs ){
        listCaseGroups(matchFn).stream().forEach(cs);
        return (_S)this;
    }

    boolean hasDefault();

    _switchEntry getDefault();

    default boolean isDefault(_switchEntry _se){
        _switchEntry _fse = getDefault();
        return Objects.equals(_fse, _se);
    }

    default boolean isDefault(Predicate<_switchEntry> matchFn){
        _switchEntry _fse = getDefault();
        if( _fse == null ){
            return matchFn == null;
        }
        return matchFn.test( _fse);
    }

}
