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
public final class _nodePath {

    public static _nodePath of(Object... pathAsTokens) {
        _nodePath _p = new _nodePath();
        for (int i = 0; i < pathAsTokens.length; i += 2) {
            if (!(pathAsTokens[i] instanceof _java.Feature) && !(pathAsTokens[i] instanceof Class)) {
                throw new _jdraftException("element [" + i + "] MUST be a Component or _node Class ");
            }
            if ((pathAsTokens.length > i + 1) && pathAsTokens[i + 1] instanceof String) {
                if( pathAsTokens[i] instanceof _java.Feature){
                    _p = _p.in((_java.Feature) pathAsTokens[i], (String) pathAsTokens[i + 1]);
                } else{
                    _p = _p.in(_java.Feature.of( (Class<_java._multiPart>)pathAsTokens[i]), (String) pathAsTokens[i + 1]);
                }
            } else {
                if( pathAsTokens[i] instanceof _java.Feature){
                    _p = _p.in((_java.Feature) pathAsTokens[i]);
                } else{
                    _p = _p.in( _java.Feature.of( (Class<_java._multiPart>)pathAsTokens[i]));
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
    public List<_java.Feature> featurePath;

    /**
     * The identifying String of a member component (usually the name of the
     * member) (i.e. for a _field, _type, _method, _annotation._element, or
     * _enum._constant the name) (for a constructor, the parameter types)
     *
     * //NOTE: can be empty for non-named components (i.e. EXTENDS, IMPLEMENTS, etc.)
     */
    public List<String> idPath;

    /**
     * build a new empty path
     */
    public _nodePath() {
        featurePath = new ArrayList<>();
        idPath = new ArrayList<>();
    }

    /**
     * create a new path containing all nodes as the original
     *
     * @param original
     */
    public _nodePath(_nodePath original) {
        featurePath = new ArrayList();
        featurePath.addAll(original.featurePath);
        idPath = new ArrayList();
        idPath.addAll(original.idPath);
    }

    /**
     * Build and return a new _path that follows the current path down one
     * component
     *
     * @param feature
     * @return a new _path that has a leaf node at the component
     */
    public _nodePath in(_java.Feature feature) {
        return in(feature, "");
    }

    /**
     * How many "nodes" are in the path
     *
     * @return the number of nodes in the path
     */
    public int size() {
        return this.featurePath.size();
    }

    /**
     * get the leaf (last component) component in the path
     *
     * @return the last component in the path (null if the path is empty)
     */
    public _java.Feature leaf() {
        if (!this.featurePath.isEmpty()) {
            return this.featurePath.get(this.featurePath.size() - 1);
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
     * @param feature the component type expected
     * @return true if the component at
     */
    public boolean is(int index, _java.Feature feature) {
        if (index <= this.size() && index >= 0) {
            return this.featurePath.get(index).equals(feature);
        }
        return false;
    }

    /**
     * does the path at index have the component and id provided
     *
     * @param index the index from the start of the path (0-based)
     * @param feature the component type expected
     * @param id the expected id
     * @return true if the component at index has the component and id provided,
     * false otherwise
     */
    public boolean is(int index, _java.Feature feature, String id) {
        if (index <= this.size() && index >= 0) {
            return this.featurePath.get(index).equals(feature)
                    && this.idPath.get(index).equals(id);
        }
        return false;
    }

    /**
     *
     * @param <_N>
     * @param index
     * @param clazz
     * @param id
     * @return
     */
    public <_N extends _java._multiPart> boolean is(int index, Class<_N> clazz, String id) {
        if (index <= this.size() && index >= 0) {
            return this.featurePath.get(index).implementationClass.equals(clazz)
                    && this.idPath.get(index).equals(id);
        }
        return false;
    }

    /**
     * Does the leaf part of the path have the provided component and id
     *
     * @param feature the component
     * @param id the id
     * @return true if the path has the leaf at component and id
     */
    public boolean isLeaf(_java.Feature feature, String id) {
        return feature.equals(leaf()) && id.equals(leafId());
    }

    /**
     *
     * @param <_N>
     * @param clazz
     * @param id
     * @return
     */
    public <_N extends _java._multiPart> boolean isLeaf(Class<_N> clazz, String id) {
        return isLeaf(clazz) && leafId().equals(id);
    }

    /**
     * @param feature the component
     * @return is the last component in the path this component?
     */
    public boolean isLeaf(_java.Feature feature) {
        return feature.equals(leaf());
    }

    /**
     *
     * @param <_N>
     * @param clazz
     * @return
     */
    public <_N extends _java._multiPart> boolean isLeaf(Class<_N> clazz) {
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
     * @param features
     * @return true if the path contains all these components in ANY order
     */
    public boolean has(_java.Feature... features) {
        Set<_java.Feature> s = new HashSet<>();
        Arrays.stream(features).forEach(c -> s.add(c));
        return featurePath.containsAll(s);
    }

    /**
     * does the path contain this component (anywhere?)
     *
     * @param feature component to look for
     * @return true if the component occurs anywhere in the path
     */
    public boolean has(_java.Feature feature) {
        return featurePath.contains(feature);
    }

    /**
     * id there a
     *
     * @param <_N>
     * @param clazz
     * @return
     */
    public <_N extends _java._multiPart> boolean has(Class<_N> clazz) {
        for (int i = 0; i < size(); i++) {
            if (this.featurePath.get(i).implementationClass.equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    /**
     * does the path contain a part that has this exact component and id
     *
     * @param feature the component we are looking for
     * @param id the path we are looking for
     * @return true if the path contains part with this component & id, false
     * otherwise
     */
    public boolean has(_java.Feature feature, String id) {
        for (int i = 0; i < size(); i++) {
            if (this.featurePath.get(i).equals(feature)
                    && this.idPath.get(i).equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param <_N>
     * @param clazz
     * @param id
     * @return
     */
    public <_N extends _java._multiPart> boolean has(Class<_N> clazz, String id) {
        for (int i = 0; i < size(); i++) {
            if (this.featurePath.get(i).implementationClass.equals(clazz)
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
     * @param feature the next component part
     * @param id the id for the component
     * @return a new _path advanced to the next component/id
     */
    public _nodePath in(_java.Feature feature, String id) {
        _nodePath _p = new _nodePath(this);
        _p.featurePath.add(feature);
        _p.idPath.add(id);
        return _p;
    }

    /**
     * @return the component path as a String
     */
    public String componentPathString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.featurePath.size(); i++) {
            if (i > 0) {
                sb.append(".");
            }
            sb.append(this.featurePath.get(i).getName());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.featurePath.size(); i++) {
            if (i > 0) {
                sb.append(".");
            }
            sb.append(this.featurePath.get(i).getName());
            if (this.idPath.get(i).length() > 0) {
                sb.append("[").append(this.idPath.get(i)).append("]");
            }
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.featurePath);
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
        if (!(obj instanceof _nodePath)) {
            return false;
        }
        _nodePath other = (_nodePath) obj;

        return Objects.equals(this.featurePath, other.featurePath)
                && Objects.equals(this.idPath, other.idPath);
    }
}
