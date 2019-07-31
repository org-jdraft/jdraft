package org.jdraft.diff;

import java.util.*;

import org.jdraft.*;

/**
 * Defines the Path through the hierarchial member components ( _types, methods,
 * etc.) a specific member or property
 *
 * (the notation we use is [xxxx] represents a name, and the other names
 * represent components for example:
 * <PRE>
 * class[MyClass].name                          : the name of class MyClass
 * interface[I].extends                         : the extends on the interface I
 * enum[Scope].implements                       : the implements on the enum Scope
 * annotation[Retain].field[hops].init          : init of a field hops on the annotation Retain
 * class[MyClass].method[m(int)].parameter[0]   : the first parameter on method m(int) in class MyClass
 *
 * //if we have nested components it can get interesting (omit Component for brevity)
 * enum[E].nest.class[inner].method[m()].body   : (the method body on a nested class within an enum)
 * </PRE>
 */
public class _path {

    public static _path of(Object... pathAsTokens) {
        _path _p = new _path();
        for (int i = 0; i < pathAsTokens.length; i += 2) {
            if (!(pathAsTokens[i] instanceof _java.Component) && !(pathAsTokens[i] instanceof Class)) {
                throw new _jDraftException("element [" + i + "] MUST be a Component or _node Class ");
            }
            if ((pathAsTokens.length > i + 1) && pathAsTokens[i + 1] instanceof String) {
                if( pathAsTokens[i] instanceof _java.Component){
                    _p = _p.in((_java.Component) pathAsTokens[i], (String) pathAsTokens[i + 1]);
                } else{
                    _p = _p.in(_java.Component.of( (Class<_node>)pathAsTokens[i]), (String) pathAsTokens[i + 1]);
                }
            } else {
                if( pathAsTokens[i] instanceof _java.Component){
                    _p = _p.in((_java.Component) pathAsTokens[i]);
                } else{
                    _p = _p.in( _java.Component.of( (Class<_node>)pathAsTokens[i]));
                }
            }
        }
        return _p;
    }

    /**
     * the types of components that identify an entity for example:
     * <PRE>
     * Component.CLASS, Component.NAME (the class Name)
     * Component.INTERFACE, Component.EXTENDS (the extends on the interface)
     * Component.ENUM, Component.IMPLEMENTS ( the implements on the enum)
     * Component.ANNOTATION. Component.FIELD, Component.INIT (init of a field on the annotation)
     *
     * //if we have nested components it can get interesting (omit Component for brevity)
     * ENUM, CLASS, METHOD, BODY (the method body on a nested class within an enum)
     * </PRE>
     *
     * we build this as we traverse the class (when identifying diffs)
     */
    public List<_java.Component> componentPath;

    /**
     * The identifying String of a member component (usually the name of the
     * member) (i.e. for a _field, _type, _method, _annotation._element, or
     * _enum._constant the name) (for a constructor, the parameter types)
     *
     * //NOTE: can be empty for non-named components (i.e. EXTENDS, IMPLEMENTS,
     * etc.)
     */
    public List<String> idPath;

    /**
     * build a new empty path
     */
    public _path() {
        componentPath = new ArrayList<>();
        idPath = new ArrayList<>();
    }

    /**
     * create a new path containing all nodes as the original
     *
     * @param original
     */
    public _path(_path original) {
        componentPath = new ArrayList();
        componentPath.addAll(original.componentPath);
        idPath = new ArrayList();
        idPath.addAll(original.idPath);
    }

    /**
     * Build and return a new _path that follows the current path down one
     * component
     *
     * @param component
     * @return a new _path that has a leaf node at the component
     */
    public _path in(_java.Component component) {
        return in(component, "");
    }

    /**
     * How many "nodes" are in the path
     *
     * @return the number of nodes in the path
     */
    public int size() {
        return this.componentPath.size();
    }

    /**
     * get the leaf (last component) component in the path
     *
     * @return the last component in the path (null if the path is empty)
     */
    public _java.Component leaf() {
        if (!this.componentPath.isEmpty()) {
            return this.componentPath.get(this.componentPath.size() - 1);
        }
        return null;
    }

    /**
     * Gets the last id in the path (note: may be empty string, null if the path
     * is empty)
     *
     * @return the last id of the path or null if path is empty
     */
    public String leafId() {
        if (!this.idPath.isEmpty()) {
            return this.idPath.get(this.idPath.size() - 1);
        }
        return null;
    }

    /**
     * does the path at index have the id provided?
     *
     * @param index the index of the path (0 based)
     * @param id the id
     * @return true if the same, false otherwise
     */
    public boolean is(int index, String id) {
        if (index <= this.size() && index >= 0) {
            return this.idPath.get(index).equals(id);
        }
        return false;
    }

    /**
     * does the path at index have the component provided
     *
     * @param index the index from the start of the path (0-based)
     * @param component the component type expected
     * @return true if the component at
     */
    public boolean is(int index, _java.Component component) {
        if (index <= this.size() && index >= 0) {
            return this.componentPath.get(index).equals(component);
        }
        return false;
    }

    /**
     * does the path at index have the component and id provided
     *
     * @param index the index from the start of the path (0-based)
     * @param component the component type expected
     * @param id the expected id
     * @return true if the component at index has the component and id provided,
     * false otherwise
     */
    public boolean is(int index, _java.Component component, String id) {
        if (index <= this.size() && index >= 0) {
            return this.componentPath.get(index).equals(component)
                    && this.idPath.get(index).equals(id);
        }
        return false;
    }

    /**
     *
     * @param <N>
     * @param index
     * @param clazz
     * @param id
     * @return
     */
    public <N extends _node> boolean is(int index, Class<N> clazz, String id) {
        if (index <= this.size() && index >= 0) {
            return this.componentPath.get(index).implementationClass.equals(clazz)
                    && this.idPath.get(index).equals(id);
        }
        return false;
    }

    /**
     * Does the leaf part of the path have the provided component and id
     *
     * @param component the component
     * @param id the id
     * @return true if the path has the leaf at component and id
     */
    public boolean isLeaf(_java.Component component, String id) {
        return component.equals(leaf()) && id.equals(leafId());
    }

    /**
     *
     * @param <N>
     * @param clazz
     * @param id
     * @return
     */
    public <N extends _node> boolean isLeaf(Class<N> clazz, String id) {        
        return isLeaf(clazz) && leafId().equals(id);
    }

    /**
     * @param component the component
     * @return is the last component in the path this component?
     */
    public boolean isLeaf(_java.Component component) {
        return component.equals(leaf());
    }

    /**
     *
     * @param <N>
     * @param clazz
     * @return
     */
    public <N extends _node> boolean isLeaf(Class<N> clazz) {
        return leaf().implementationClass.equals(clazz);
    }

    /**
     * @param id
     * @return is the last id in the path this id?
     */
    public boolean isLeafId(String id) {
        return id.equals(leafId());
    }

    /**
     * does the path contain this id at any node?
     *
     * @param id
     * @return true if
     */
    public boolean has(String id) {
        return idPath.contains(id);
    }

    /**
     * does the path contain ALL of these ids (in ANY order)
     *
     * @param ids the ids to look for in the path
     * @return true if the path contains all ids
     */
    public boolean has(String... ids) {
        Set<String> sids = new HashSet<>();
        Arrays.stream(ids).forEach(i -> sids.add(i));
        return idPath.containsAll(sids);
    }

    /**
     * does this path contain all of these components (in ANY order)?
     *
     * @param components
     * @return true if the path contains all these components in ANY order
     */
    public boolean has(_java.Component... components) {
        Set<_java.Component> s = new HashSet<>();
        Arrays.stream(components).forEach(c -> s.add(c));
        return componentPath.containsAll(s);
    }

    /**
     * does the path contain this component (anywhere?)
     *
     * @param component component to look for
     * @return true if the component occurs anywhere in the path
     */
    public boolean has(_java.Component component) {
        return componentPath.contains(component);
    }

    /**
     * id there a
     *
     * @param <N>
     * @param clazz
     * @return
     */
    public <N extends _node> boolean has(Class<N> clazz) {
        for (int i = 0; i < size(); i++) {
            if (this.componentPath.get(i).implementationClass.equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    /**
     * does the path contain a part that has this exact component and id
     *
     * @param component the component we are looking for
     * @param id the path we are looking for
     * @return true if the path contains part with this component & id, false
     * otherwise
     */
    public boolean has(_java.Component component, String id) {
        for (int i = 0; i < size(); i++) {
            if (this.componentPath.get(i).equals(component)
                    && this.idPath.get(i).equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param <N>
     * @param clazz
     * @param id
     * @return
     */
    public <N extends _node> boolean has(Class<N> clazz, String id) {
        for (int i = 0; i < size(); i++) {
            if (this.componentPath.get(i).implementationClass.equals(clazz)
                    && this.idPath.get(i).equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Build and return a new _path that follows the current path down one
     * component
     *
     * @param component the next component part
     * @param id the id for the component
     * @return a new _path advanced to the next component/id
     */
    public _path in(_java.Component component, String id) {
        _path _p = new _path(this);
        _p.componentPath.add(component);
        _p.idPath.add(id);
        return _p;
    }

    /**
     * @return the component path as a String
     */
    public String componentPathString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.componentPath.size(); i++) {
            if (i > 0) {
                sb.append(".");
            }
            sb.append(this.componentPath.get(i).getName());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.componentPath.size(); i++) {
            if (i > 0) {
                sb.append(".");
            }
            sb.append(this.componentPath.get(i).getName());
            if (this.idPath.get(i).length() > 0) {
                sb.append("[").append(this.idPath.get(i)).append("]");
            }
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.componentPath);
        hash = 79 * hash + Objects.hashCode(this.idPath);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof _path)) {
            return false;
        }
        _path other = (_path) obj;

        return Objects.equals(this.componentPath, other.componentPath)
                && Objects.equals(this.idPath, other.idPath);
    }
}