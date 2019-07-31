package org.jdraft;

import java.util.*;
import java.util.function.Predicate;

/**
 * Any unordered mutable aggregate of {@link _type}s
 * 
 * @author Eric
 * @param <T> the implementation class of the type aggregate
 */
public interface _types <T extends _types>
    extends _type._hasTypes, _java {
    
    /**
     * Build and return an implementation given this collection of {@link _type}s
     * @param types
     * @return 
     */
    static _types of( Collection<_type> types ){
        return _impl.of(types);
    }
    
    static _types of( _type... types ){
        return _impl.of(types);
    }
    
    int size();
    
    /**
     * Adds this type to the 
     * @param type
     * @return the modified T
     */
    T add( _type type );
    
    /**
     * Adds
     * @param types
     * @return 
     */
    default T add( _type... types ){
        Arrays.stream(types).forEach(t-> add(t) );
        return (T)this;
    }
    
    /**
     * 
     * @param type
     * @return 
     */
    T remove( _type type );
    
    /**
     * 
     * @param types
     * @return 
     */
    default T remove( _type...types ){
        Arrays.stream(types).forEach(t-> remove(t) );
        return (T)this;
    }
    
    /**
     * remove all types that pass the _typePredicate
     * @param _typePredicate
     * @return 
     */
    default T remove( Predicate<_type> _typePredicate ){
        list(_typePredicate).forEach(t -> remove(t));
        return (T)this;
    }

    /**
     * Simple List implementation of {@link _type}s
     * 
     */
     class _impl implements _types{

        List<_type> types;
        
        public static _impl of( Collection<_type> ts ){
            _impl _i = new _impl();
            _i.types.addAll(ts);
            return _i;
        }
        
        public static _impl of( _type... ts ){
            _impl _i = new _impl();
            Arrays.stream(ts).forEach(t -> _i.types.add(t) );            
            return _i;
        }
        
        public _impl(){
            this.types = new ArrayList<>();
        }
        
        @Override
        public int size(){
            return types.size();
        }
        
        @Override
        public _types add(_type type) {
            types.add( type );
            return this;
        }

        @Override
        public _types remove(_type type) {
            list(t -> t.equals( type) ).forEach(t -> types.remove(t));
            return this;
        }

        @Override
        public List<_type> list() {
            return types;
        }

        @Override
        public boolean isEmpty() {
            return types.isEmpty();
        }        
    }
}
