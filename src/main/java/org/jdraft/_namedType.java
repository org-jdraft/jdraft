package org.jdraft;

import com.github.javaparser.ast.type.Type;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Entity with TYPE/NAME pair
 * <UL>
 *     <LI>{@link _field},
 *     <LI>{@link _parameter},
 *     <LI>{@link _method},
 *     <LI>{@link _annotation._element},
 *     <LI>{@link _receiverParameter}
 * </UL>
 * @param <T> the specialized entity that is a named TYPE
 */
public interface _namedType<T extends _namedType> extends _named<T> {

    /**
     * @return they TYPE
     */
    _typeRef getType();

    default _typeRef getElementType() {
        return _typeRef.of(getType().ast().getElementType());
    }

    /**
     * Set the TYPE and return the modified entity
     * @param _tr the _typeRef object
     * @return the modified entity after setting the TYPE
     */
    T type(Type _tr);

    /**
     * set the TYPE and return
     * @param t
     * @return
     */
    default T type(_typeRef t) {
        return type(t.ast());
    }

    /**
     * Set the TYPE and return the modified entity
     * @param typeRef the String representation of the TYPE
     * @return the modified entity after setting the TYPE
     */
    default T type(String typeRef) {
        return type(Ast.typeRef(typeRef));
    }

    /**
     * Set the TYPE and return the modified entity
     * @param clazz the class of the TYPE to set
     * @return the modified entity after setting the TYPE
     */
    default T type(Class clazz) {
        return type(Ast.typeRef(clazz.getCanonicalName()));
    }

    /**
     * Perform the action is the entity is of the TYPE clazz and return it
     * @param clazz the class to perform the action
     * @param _isTypeActionFn the consumer function to run if the TYPE is correct
     * @return the T
     */
    default T ifType(Class clazz, Consumer<T> _isTypeActionFn) {
        if (isType(clazz)) {
            _isTypeActionFn.accept((T) this);
        }
        return (T) this;
    }

    /**
     * Perform the action is the entity is of the TYPE clazz and return it
     * @param clazz the class to perform the action
     * @param _isTypeActionFn the consumer function to run if the TYPE matches
     * @param _isNotTypeActionFn the consumer function to run if the TYPE does not match
     * @return the T
     */
    default T ifType(Class clazz, Consumer<T> _isTypeActionFn, Consumer<T> _isNotTypeActionFn) {
        if (isType(clazz)) {
            _isTypeActionFn.accept((T) this);
        } else {
            _isNotTypeActionFn.accept((T) this);
        }
        return (T) this;
    }

    /**
     * is the type void
     * @return true if void
     */
    default boolean isVoid() {
        return getType().ast().isVoidType();
    }

    /**
     * Is the TYPE of this entity the same as represented where the _typeRef TYPE
     * @param type the _typeRef representation of the TYPE
     * @return true if the TYPE is the same
     */
    default boolean isType(Type type) {
        return getType().ast().equals(type);
    }

    /**
     * Is the TYPE of this entity the same as represented where the _typeRef TYPE
     * @param type the _typeRef representation of the TYPE
     * @return true if the TYPE is the same
     */
    default boolean isType(_typeRef type) {
        return getType().equals(type);
    }

    /**
     * Is the TYPE of this entity the same as represented where the _typeRef TYPE
     * @param clazz the Class representation of the TYPE
     * @return true if the TYPE is the same
     */
    default boolean isType(Class clazz) {
        try {
            return isType(clazz.getCanonicalName()) || isType(clazz.getSimpleName());
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * Does the type of this named typed comply with the lambda
     * @param typeMatchFn lambda matcher function
     * @return true if the type
     */
    default boolean isType(Predicate<_typeRef> typeMatchFn) {
        return typeMatchFn.test(getType());
    }

    /**
     * Is the TYPE of this entity the same as represented where the String TYPE
     * @param type the String representation of the TYPE
     * @return true if the TYPE is the same
     */
    default boolean isType(String type) {
        try {
            return isType(Ast.typeRef(type));
        } catch (Exception e) {
        }
        return false;
    }
}
