package org.jdraft;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Any unordered mutable aggregate of {@link _type}s
 * 
 * @author Eric
 * @param <_T> the implementation class of the type aggregate
 */
public interface _types <_T extends _types>
    extends  _java {
    
    /**
     * Build and return an implementation given this collection of {@link _type}s
     * @param types
     * @return 
     */
    static _impl of( Collection<_type> types ){
        return _impl.of(types);
    }
    
    static _impl of( _type... types ){
        return _impl.of(types);
    }
    
    int size();
    
    /**
     * Adds this type to the 
     * @param type
     * @return the modified T
     */
    _T add(_type type );
    
    /**
     * Adds
     * @param types
     * @return 
     */
    default _T add(_type... types ){
        Arrays.stream(types).forEach(t-> add(t) );
        return (_T)this;
    }

    List<_type> list();

    default List<_type> list( Predicate<_type> _typeMatchFn ){
        return list().stream().filter(_typeMatchFn).collect(Collectors.toList());
    }

    /**
     * 
     * @param type
     * @return 
     */
    _T remove(_type type );
    
    /**
     * 
     * @param types
     * @return 
     */
    default _T remove(_type...types ){
        Arrays.stream(types).forEach(t-> remove(t) );
        return (_T)this;
    }
    
    /**
     * remove all types that pass the _typePredicate
     * @param _typePredicate
     * @return 
     */
    default _T remove(Predicate<_type> _typePredicate ){
        list(_typePredicate).forEach(t -> remove(t));
        return (_T)this;
    }

    /**
     * Simple List implementation of {@link _type}s
     * 
     */
     class _impl {

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

        public int size(){
            return types.size();
        }

        public _impl add(_type type) {
            types.add( type );
            return this;
        }

        public _impl remove(_type type) {
            list( (_type t) -> t.equals( type) ).forEach(t -> types.remove(t));
            return this;
        }

        public List<_type> list( Predicate<_type> _typeMatchFn ){
            return list().stream().filter(_typeMatchFn).collect(Collectors.toList());
        }

        public List<_type> list() {
            return types;
        }

        public boolean isEmpty() {
            return types.isEmpty();
        }        
    }
}
