package org.jdraft.runtime;

import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.utils.Log;
//import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import org.jdraft.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Captures the extends/implements class relationships within draft source code and Classes.
 *
 * <CODE>
 * _typeTree _tt = _typeTree.of(_class.of("A"), _class.of("B").extend(TestCase.class), _class.of("C").extend("B") );
 * assertEquals(1, _tt.listDirectChildren(TestCase.class)); //"B"
 * assertEquals(2, _tt.listAllChildren(TestCase.class)); //"B", "C" (b/c "C" extends "B")
 * </CODE>
 * the above is "visually"
 * <PRE>
 * - "A"
 * - TestCase
 *     \
 *     "B"
 *       \
 *       "C"
 *  </PRE>
 * Assumption: the other classes
 * Note: there are some examples where we have two
 */
public class _typeTree {

    /**
     * Builds a _typeTree from one or more _code._provider (s)
     * @param _codeProviders where the code comes from
     * @return the _typeTree
     */
    public static _typeTree of(_compilationUnit._provider..._codeProviders){
        List<_type>codeList = new ArrayList<>();
        Arrays.stream(_codeProviders).forEach(_cp -> codeList.addAll(_cp.list_types()));
        System.out.println( codeList.size() );
        return of( codeList );
    }

    /**
     * Build an return the _typeTree relationships among the types
     * @param types the top level types to build relationships for
     * @return a _typeTree to perform queries
     */
    public static _typeTree of(_type...types){
        return new _typeTree(Arrays.asList(types));
    }

    /**
     * Build an return the _typeTree relationships among the types
     * @param types the top level types to build relationships for
     * @return a _typeTree to perform queries
     */
    public static _typeTree of(List<_type> types ){
        return new _typeTree(types);
    }

    /** Parent to child relationships between _types (each entry is a _typeNode) */
    public final Map<_typeNode, Set<_typeNode>> parentChild;

    /**
     *
     * @param types
     */
    public _typeTree(List<_type> types){
        parentChild = buildTypeHierarchy(types);
    }

    /**
     *
     * @param clazz
     * @return
     */
    public _typeNode node (Class clazz){
        Optional<_typeNode> tn =
                this.parentChild.keySet().stream().filter(t -> Objects.equals( t.clazz, clazz)).findFirst();
        if( tn.isPresent() ){
            return tn.get();
        }
        tn = this.parentChild.keySet().stream().filter(t -> Objects.equals( t.fullyQualifiedClassName, clazz.getCanonicalName())).findFirst();
        if( tn.isPresent() ){
            return tn.get();
        }
        return null;
    }

    /**
     *
     * @param _t
     * @return
     */
    public _typeNode node( _type _t ){
        Optional<_typeNode> tn =
                this.parentChild.keySet().stream().filter(t -> Objects.equals( t.type, _t)).findFirst();
        if( tn.isPresent() ){
            return tn.get();
        }
        tn = this.parentChild.keySet().stream().filter(t -> Objects.equals( t.fullyQualifiedClassName, _t.getFullName())).findFirst();
        if( tn.isPresent() ){
            return tn.get();
        }
        return null;
    }

    /**
     *
     * @return
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();

        //lets put the high instances on top and the no children at the bottom
        Map<_typeNode, Integer> instanceMap = new HashMap<>();
        this.parentChild.forEach( (p,c) -> instanceMap.put(p, c.size()));

        instanceMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()) )
                .forEachOrdered(x -> {
                        _typeNode _tn = x.getKey();
                        Set<_typeNode> c = parentChild.get(_tn);
                        if( c.isEmpty() ){ //"parent nodes that have no children are "terminal nodes"
                            sb.append("  ").append(_tn.fullyQualifiedClassName).append(System.lineSeparator());
                        } else {
                            sb.append(_tn.fullyQualifiedClassName).append("->").append(c).append(System.lineSeparator());
                        }
                });
        return sb.toString();
    }

    /**
     * resolves a node for a _type
     * @param typeName
     * @return
     */
    public _typeNode node(String typeName ){
        Optional<_typeNode> tn =
                this.parentChild.keySet().stream().filter(t -> t.fullyQualifiedClassName.equals(typeName)).findFirst();
        if( tn.isPresent() ){
            return tn.get();
        }
        tn = this.parentChild.keySet().stream().filter(t -> t.isName(typeName)).findFirst();
        if( tn.isPresent() ){
            return tn.get();
        }
        return null;
    }

    /**
     *
     * @param typeName
     * @return
     */
    public List<_typeNode> listAllAncestors( String typeName ){
        _typeNode tn = node(typeName);
        return listAllAncestors(tn);
    }

    /**
     *
     * @param clazz
     * @return
     */
    public List<_typeNode> listAllAncestors( Class clazz){
        return listAllAncestors(node(clazz));
    }

    /**
     *
     * @param _t
     * @return
     */
    public List<_typeNode> listAllAncestors( _type _t){
        return listAllAncestors(node(_t) );
    }

    /**
     *
     * @param tn
     * @return
     */
    public List<_typeNode> listAllAncestors( _typeNode tn ){
        Set<_typeNode> ancestors = new HashSet<>();
        this.parentChild.forEach( (n,s)-> {
            if(s.contains(tn)){
                ancestors.add(n);
            }
        });
        Set<_typeNode> aa = new HashSet<>();
        ancestors.stream().forEach(a -> aa.addAll( listAllAncestors(a)));
        ancestors.addAll(aa);
        return ancestors.stream().collect(Collectors.toList());
    }

    /**
     *
     * @param _t
     * @return
     */
    public List<_typeNode> listDirectChildren( _type _t ){
        return listDirectChildren(_t.getFullName() );
    }

    /**
     *
     * @param clazz
     * @return
     */
    public List<_typeNode> listDirectChildren( Class clazz){
        return listDirectChildren( node(clazz) );
    }

    /**
     * find all classes that directly extend or implement TestCase.class
     * <PRE>
     * List<_typeNode> tns = _typeTree.of(_a,_b,_c).listDirectChildren("TestCase");
     * </PRE>
     * @param typeName
     * @return
     */
    public List<_typeNode> listDirectChildren(String typeName){
        typeName = Ast.typeRef(typeName).asClassOrInterfaceType().getName().toString();
        _typeNode _tn = node(typeName);
        if( _tn == null ){
            throw new _runtimeException("Could not find typeNode for "+typeName);
        }
        return listDirectChildren(_tn);
    }

    /**
     *
     * @param _tn
     * @return
     */
    public List<_typeNode> listDirectChildren( _typeNode _tn ){
        List<_typeNode> otn =
                this.parentChild.keySet().stream().filter( tn-> tn.fullyQualifiedClassName.equals(_tn.fullyQualifiedClassName))
                        .collect(Collectors.toList());
        if( otn.size() == 0 ){
            throw new _runtimeException("Could not find matching _typenode for "+ _tn );
        }
        if( otn.size() == 1 ){
            return this.parentChild.get(otn.get(0)).stream().collect(Collectors.toList());
        }
        //more than one match, take the one that is exact equals first
        if( otn.contains(_tn) ){
            return this.parentChild.get(_tn).stream().collect(Collectors.toList());
        }
        throw new _runtimeException("ambiguous node to resolve"+_tn+ " "+ otn);
    }

    /**
     * Does the _type _t implement the type defined by the fullyQualified Type Name
     * @param _t the type to check
     * @param fullyQualifiedTypeName fully qualified type name (no generics)
     * @return true
     */
    public boolean isImplements( _type _t, String fullyQualifiedTypeName){
        return listAllDescendants(fullyQualifiedTypeName).stream().filter(tn -> tn.is(_t)).findFirst().isPresent();
    }

    /**
     * Does the _type _t implement the type defined by the fullyQualified Type Name
     * @param _t the type to check
     * @param _t the interface (or class)
     * @return true
     */
    public boolean isImplements( _type _t, _type _i){
        return listAllDescendants(_i).stream().filter(tn -> tn.is(_t)).findFirst().isPresent();
    }

    /**
     *
     * @param _t
     * @param clazz
     * @return
     */
    public boolean isImplements( _type _t, Class clazz){
        return listAllDescendants(clazz).stream().filter(tn -> tn.is(_t)).findFirst().isPresent();
    }

    /**
     *
     * @param _t
     * @return
     */
    public List<_typeNode> listAllDescendants(_type _t ){
        return listDirectChildren(_t.getFullName() );
    }



    /**
     *
     * @param clazz
     * @return
     */
    public List<_typeNode> listAllDescendants(Class clazz){
        return listAllDescendants(clazz.getCanonicalName());
    }

    /**
     *
     * @param typeName
     * @return
     */
    public List<_typeNode> listAllDescendants(String typeName){
        _typeNode _tn = node(typeName);
        if( _tn == null ){
            throw new _runtimeException("Could not find typeNode for "+typeName);
        }
        return listAllDescendants(_tn);
    }

    /**
     * Depp listing of the children of the typeNode provided
     * @param _tn
     * @return
     */
    public List<_typeNode> listAllDescendants(_typeNode _tn ){
        Set<_typeNode>coll = this.parentChild.get(_tn);
        _typeNode[] found = coll.toArray(new _typeNode[0]);

        for(int i=0;i< found.length;i++){
            //deeply follow children
            List<_typeNode> ch = listAllDescendants(found[i].fullyQualifiedClassName);
            coll.addAll(ch);
            if( !ch.isEmpty() ){
                ch.forEach( c -> coll.addAll(listAllDescendants(c.fullyQualifiedClassName)));
            }
        }
        return coll.stream().collect(Collectors.toList());
    }

    /**
     * Given a _type, map all of it's ancestors by updating the parentToChildMap
     * @param _t the _type to add ancestors
     * @param typeNodes all of the _typeNodes used
     * @param parentToChildMap the map to be updated with ancestor relationships
     */
    private static void mapAncestors(_type _t, List<_typeNode> typeNodes, Map<_typeNode, Set<_typeNode>>parentToChildMap){
        _typeNode cNode = typeNodes.stream().filter(n -> n.isName(_t.getFullName())).findFirst().get();
        if( _t instanceof _type._withExtends){
            ((_type._withExtends)_t).listExtends().forEach(e-> {
                String name = ((ClassOrInterfaceType )e).getNameAsString();
                //find (or create) the appropriate _typeNode
                Log.info(" mapping extends for %s", ()->_t.getFullName());
                _typeNode parent = _typeNode.findOrCreate(typeNodes, name, _t.getImports());
                Set<_typeNode> children = parentToChildMap.get(parent);
                if (children == null) {
                    children = new HashSet<>();
                    parentToChildMap.put(parent, children);
                }
                children.add( cNode);
            });
        }
        if( _t instanceof _type._withImplements){
            ((_type._withImplements)_t).listImplements().forEach(e-> {
                Log.info(" mapping implements for %s", ()->_t.getFullName());
                String name = ((ClassOrInterfaceType )e).getNameAsString();
                //find (or create) the appropriate _typeNode
                _typeNode parent = _typeNode.findOrCreate(typeNodes, name, _t.getImports());
                Set<_typeNode> children = parentToChildMap.get(parent);
                if (children == null) {
                    children = new HashSet<>();
                    parentToChildMap.put(parent, children);
                }
                children.add( cNode);
            });
        }
    }

    /**
     * Find the first ancestor of this type that matches the ancestorMatchFn
     * @param _t
     * @param ancestorMatchFn
     * @return
     */
    public _typeNode findFirstAncestor( _type _t, List<_typeNode> typeNodes, Predicate<_typeNode> ancestorMatchFn){
        List<_typeNode> parents = new ArrayList<>();
        if( _t instanceof _type._withExtends){
            _type._withExtends he =((_type._withExtends)_t);
            List<ClassOrInterfaceType> cit = he.listExtends();

            List<_typeNode> extendParent = cit.stream().map(e -> _typeNode.findOrCreate(typeNodes, e.getNameAsString(), _t.getImports()))
                    .collect(Collectors.toList());

            parents.addAll(extendParent);
            //check the direct parent
            Optional<_typeNode> foundParent = extendParent.stream()
                    .filter(t-> ancestorMatchFn.test(t)).findFirst();
            if( foundParent.isPresent() ){
                return foundParent.get();
            }
        }
        if( _t instanceof _type._withImplements){
            _type._withImplements he =((_type._withImplements)_t);
            List<ClassOrInterfaceType> cit = he.listImplements();

            List<_typeNode> implParent = cit.stream().map(e -> _typeNode.findOrCreate(typeNodes, e.getNameAsString(), _t.getImports()))
                    .collect(Collectors.toList());

            parents.addAll(implParent);
            //check the direct parent
            Optional<_typeNode> foundParent = implParent.stream()
                    .filter(t-> ancestorMatchFn.test(t)).findFirst();
            if( foundParent.isPresent() ){
                return foundParent.get();
            }
        }
        // I need to look for grandparents
        List<_typeNode> ps = parents.stream().filter( c ->c.type != null ).collect(Collectors.toList());

        Optional<_typeNode> otn = ps.stream().filter(e -> findFirstAncestor(e.type, typeNodes, ancestorMatchFn) != null).findFirst();
        if( otn.isPresent() ){
            otn.get();
        }
        return null;
    }

    /**
     *
     * @param types
     * @return
     */
    private Map<_typeNode, Set<_typeNode>> buildTypeHierarchy( List<_type> types ){
        //make a map of typeNodes
        List<_typeNode> typeNodes = new ArrayList<>();

        //this is a "flat" parent to all descendents map
        Map<_typeNode, Set<_typeNode>> parentToChildMap = new HashMap();

        //make all the typeNodes for these nodes
        types.stream().forEach(t-> Walk.in(t, _type.class, tt-> typeNodes.add( new _typeNode( tt ))) );
        typeNodes.forEach( tn -> parentToChildMap.put( tn, new HashSet<>()));

        //traverse the map and update the map with typeNodes
        types.stream().forEach(t-> {
            Walk.in(t, _type.class, n-> mapAncestors(n, typeNodes, parentToChildMap));
        });
        return parentToChildMap;
    }

    /**
     * A _typeNode within a type hierarchy
     * it stores the fullyQualifiedClassName
     * and EITHER
     * the (Runtime) Class ( a runtime class)
     * or the (source) _type (a model of the source code)
     */
    public static class _typeNode {
        public String fullyQualifiedClassName;
        public Class clazz; //runtime class (if we have it)... i.e. we use this if
        public _type type; //we use this if we have a resolved type

        public _typeNode(_type _t){
            this.type = _t;
            this.fullyQualifiedClassName = _t.getFullName();
        }

        public _typeNode(String fullyQualifiedTypeName){
            this.fullyQualifiedClassName = fullyQualifiedTypeName;
        }

        public _typeNode(Class clazz ){
            this.fullyQualifiedClassName = clazz.getCanonicalName();
            this.clazz = clazz;
        }

        public boolean is( _type _t ){
            if( Objects.equals(_t, this.type) ){
                return true;
            }
            return _t.getFullName().equals( fullyQualifiedClassName);
        }

        public boolean is( Class clazz ){
            if( Objects.equals(this.clazz, clazz) ){
                return true;
            }
            return this.fullyQualifiedClassName.equals(clazz.getCanonicalName());
        }

        public boolean isName(String name){
            return this.fullyQualifiedClassName.equals(name)
                    || (this.type != null && this.type.getName().equals(name))
                    || (this.fullyQualifiedClassName.endsWith("."+name));
        }

        @Override
        public String toString() {
            //return "_typeNode[" + fullyQualifiedClassName + ']';
            return "[" + fullyQualifiedClassName + ']';
        }

        public boolean equals(Object obj){
            if( obj == this  ){
                return true;
            }
            if( !(obj instanceof _typeNode)){
                return false;
            }
            _typeNode other = (_typeNode)obj;
            return Objects.equals( this.fullyQualifiedClassName, other.fullyQualifiedClassName) &&
                    Objects.equals( this.type, other.type) &&
                    Objects.equals( this.clazz, other.clazz);
        }

        @Override
        public int hashCode() {

            return Objects.hash(fullyQualifiedClassName, clazz, type);
        }

        /**
         * finds or creates & returns a _typeNode that represents this name(identifier)
         * within a class with these imports
         *
         * @param tns
         * @param name
         * @param _is
         * @return
         */
        public static _typeNode findOrCreate(List<_typeNode> tns, String name, _imports _is) {
            Log.info("Finding %s", ()->name);
            Optional<_typeNode> tn = tns.stream().filter(t-> t.isName(name)).findFirst();
            if( tn.isPresent() ){
                return tn.get();
            }
            //check if the type I'm looking for is "directly" imported
            List<_import> ii = _is.list(i-> i.getName().equals( name) || i.getName().endsWith("."+name) );
            if( !ii.isEmpty() ){
                Log.info("   FOUND Direct imports to find %s type %s", ()->name, ()->ii );
                _typeNode ntn = new _typeNode(ii.get(0).getName());
                tns.add( ntn );
                return ntn;
            }
            //ok the best I can do is to expect to find a wildcard import that completes the name
            List<_import> wcs = _is.list(_import::isWildcard);

            for(int i=0;i<wcs.size();i++){
                String fcn = wcs.get(i).getName().replace(".*", "."+name);
                Log.info("  try wildcard imports to find type %s"+wcs.get(i).getName(), ()->name);
                try {

                    Class clazz = Class.forName(fcn);
                    _typeNode ntn = new _typeNode(clazz);
                    tns.add( ntn );
                    return ntn;
                }catch(ClassNotFoundException cnfe){
                    //expected
                }
            }
            //last ditch effort... it's a "java.lang.*" type
            try{
                Class clazz = Class.forName("java.lang."+name);
                _typeNode ntn = new _typeNode(clazz);
                tns.add( ntn );
                return ntn;
            } catch( ClassNotFoundException cnfe){

            }
            Log.error("Could not find type %s with %s", ()->name, ()->_is);
            if( name.equals("Objenesis")) {
                return new _typeNode("org.objenesis.Objenesis");
            }
            if( name.equals("ClassGenerator") ){
                return new _typeNode("org.springframework.cglib.core.ClassGenerator"); //org.springframework.ClassGenerator.class);
            }
            if( name.equals("DefaultNamingPolicy")){
                return new _typeNode("net.sf.cglib.core.DefaultNamingPolicy");
            }
            throw new _runtimeException("type not found: " + name);
        }
    }
}
