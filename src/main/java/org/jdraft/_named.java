package org.jdraft;

import java.util.Objects;

/**
 * Named java entities
 * {@link _type} {@link _class} {@link _enum} {@link _interface} {@link _annotation}
 * {@link _method}
 * {@link _field}
 * {@link _parameter}
 * {@link _anno}
 * {@link _enum._constant}
 * {@link _annotation._element}
 * {@link _typeRef}
 * {@link _typeParameter}
 *
 * @author Eric
 * @param <_N>
 */
public interface _named<_N extends _named> extends _model {

    /**
     * @param name set the name on the entity and return the modified entity
     * @return the modified entity
     */
    _N name(String name);

    /**
     * gets the name of the entity
     * @return the name of the entity
     */
    String getName();

    /**
     *
     * @param name
     * @return
     */
    default boolean isNamed(String name) {
        return Objects.equals(getName(), name);
    }
}
