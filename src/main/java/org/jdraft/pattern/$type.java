package org.jdraft.pattern;

import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.*;

import org.jdraft.*;

public interface $type { //<T extends TypeDeclaration, _T extends _type> {

    /** this is a $type representation */
    public static class $impl{

    }

    /* These are methods shared/used by all $type implementations */
    static $pattern.$tokens selectImplements(List<$typeRef> $protoTypes, _type._hasImplements _hi){
        Map<$typeRef, List<$typeRef.Select>> selectMap = new HashMap<>();

        for(int i=0;i<$protoTypes.size(); i++) {
            final $typeRef t = $protoTypes.get(i);
            List<$typeRef.Select>matches = new ArrayList<>();
            _hi.listImplements().forEach( c ->{
                $typeRef.Select sel = t.select( _typeRef.of( (ClassOrInterfaceType)c) );
                if( sel != null ){
                    matches.add(sel);
                }
            } );
            if( matches.isEmpty()){
                return null; //couldnt match a $constructor to ANY constructors
            } else{
                selectMap.put(t, matches); //associated the matches with
            }
        }
        //Now create a map with ALL tokens
        $pattern.$tokens all = $pattern.$tokens.of();
        selectMap.values().forEach( ls -> ls.forEach( s-> all.putAll(s.tokens()) ));
        all.remove("type");
        all.remove("name");
        return all;
    }

    static $pattern.$tokens selectExtends($typeRef $protoType, _type._hasExtends _he ){
        if( !_he.hasExtends() && $protoType.isMatchAny() ){
            return $pattern.$tokens.of();
        }
        List<$typeRef> lt = new ArrayList<>();
        lt.add($protoType);
        return selectExtends(lt, _he);
    }

    static $pattern.$tokens selectExtends(List<$typeRef> $protoTypes, _type._hasExtends _he ){

        Map<$typeRef, List<$typeRef.Select>> selectMap = new HashMap<>();

        for(int i=0;i<$protoTypes.size(); i++) {
            final $typeRef t = $protoTypes.get(i);
            List<$typeRef.Select>matches = new ArrayList<>();
            _he.listExtends().forEach( c ->{
                $typeRef.Select sel = t.select( _typeRef.of( (ClassOrInterfaceType)c) );
                if( sel != null ){
                    matches.add(sel);
                }
            } );
            if( matches.isEmpty()){
                return null; //couldnt match a $constructor to ANY constructors
            } else{
                selectMap.put(t, matches); //associated the matches with
            }
        }
        //Now create a map with ALL tokens
        $pattern.$tokens all = $pattern.$tokens.of();
        selectMap.values().forEach( ls -> ls.forEach( s-> all.putAll(s.tokens()) ));

        all.remove("type");
        all.remove("name");
        return all;
    }

    static $pattern.$tokens selectConstructors(List<$constructor> $protoCtors, _constructor._hasConstructors _hcs ){
        Map<$constructor, List<$constructor.Select>> selectMap = new HashMap<>();

        for(int i=0;i<$protoCtors.size(); i++) {
            final $constructor t = $protoCtors.get(i);
            List<$constructor.Select>matches = new ArrayList<>();
            _hcs.listConstructors().forEach( c ->{
                $constructor.Select sel = t.select( (_constructor)c);
                if( sel != null ){
                    matches.add(sel);
                }
            } );
            if( matches.isEmpty()){
                return null; //couldnt match a $constructor to ANY constructors
            } else{
                selectMap.put(t, matches); //associated the matches with
            }
        }
        //Now create a map with ALL tokens
        $pattern.$tokens all = $pattern.$tokens.of();
        selectMap.values().forEach( ls -> ls.forEach( s-> all.putAll(s.tokens()) ));

        all.remove("type");
        all.remove("name");
        return all;
    }


    static $pattern.$tokens selectMethods(List<$method> $protoMethods, _method._hasMethods _hcs ){
        Map<$method, List<$method.Select>> selectMap = new HashMap<>();

        for(int i=0;i<$protoMethods.size(); i++) {
            final $method t = $protoMethods.get(i);
            List<$method.Select>matches = new ArrayList<>();
            _hcs.listMethods().forEach( m ->{
                $method.Select sel = t.select( (_method)m);
                if( sel != null ){
                    matches.add(sel);
                    //System.out.println( "FOUND "+ sel+" "+ matches);
                }
            } );
            if( matches.isEmpty()){
                return null; //couldnt match a $method to ANY constructors
            } else{
                selectMap.put(t, matches); //associated the matches with
            }
        }
        //Now create a map with ALL tokens
        $pattern.$tokens all = $pattern.$tokens.of();
        selectMap.values().forEach( ls -> ls.forEach( s-> all.putAll(s.tokens()) ));
        //we have to remove these from method so they dont "trickle up"
        all.remove("type");
        all.remove("name");
        return all;
    }

    static $pattern.$tokens selectFields(List<$field> $protoFields, _field._hasFields _hcs ){
        Map<$field, List<$field.Select>> selectMap = new HashMap<>();

        for(int i=0;i<$protoFields.size(); i++) {
            final $field t = $protoFields.get(i);
            List<$field.Select>matches = new ArrayList<>();
            _hcs.listFields().forEach( c ->{
                $field.Select sel = t.select( (_field)c);
                if( sel != null ){
                    matches.add(sel);
                }
            } );
            if( matches.isEmpty()){
                return null; //couldnt match a $constructor to ANY constructors
            } else{
                selectMap.put(t, matches); //associated the matches with
            }
        }
        //Now create a map with ALL tokens
        $pattern.$tokens all = $pattern.$tokens.of();
        selectMap.values().forEach( ls -> ls.forEach( s-> all.putAll(s.tokens()) ));
        all.remove("type");
        all.remove("name");
        return all;
    }


    static $pattern.$tokens selectInitBlocks(List<$initBlock> $protoInitBlocks, _initBlock._hasInitBlocks _hcs ){
        Map<$initBlock, List<$initBlock.Select>> selectMap = new HashMap<>();

        for(int i=0;i<$protoInitBlocks.size(); i++) {
            final $initBlock t = $protoInitBlocks.get(i);
            List<$initBlock.Select>matches = new ArrayList<>();
            _hcs.listInitBlocks().forEach( c ->{
                $initBlock.Select sel = t.select( (_initBlock) c);
                if( sel != null ){
                    matches.add(sel);
                }
            } );
            if( matches.isEmpty()){
                return null; //couldnt match a $constructor to ANY constructors
            } else{
                selectMap.put(t, matches); //associated the matches with
            }
        }
        //Now create a map with ALL tokens
        $pattern.$tokens all = $pattern.$tokens.of();
        selectMap.values().forEach( ls -> ls.forEach( s-> all.putAll(s.tokens()) ));
        all.remove("type");
        all.remove("name");
        return all;
    }

    static $pattern.$tokens selectImports(List<$import> $protoImports, _code _hcs ){
        Map<$import, List<$import.Select>> selectMap = new HashMap<>();

        for(int i=0;i<$protoImports.size(); i++) {
            final $import t = $protoImports.get(i);
            List<$import.Select>matches = new ArrayList<>();
            _hcs.listImports().forEach( c ->{
                $import.Select sel = t.select( (_import) c);
                if( sel != null ){
                    matches.add(sel);
                }
            } );
            if( matches.isEmpty()){
                return null; //couldnt match a $constructor to ANY constructors
            } else{
                selectMap.put(t, matches); //associated the matches with
            }
        }
        //Now create a map with ALL tokens
        $pattern.$tokens all = $pattern.$tokens.of();
        selectMap.values().forEach( ls -> ls.forEach( s-> all.putAll(s.tokens()) ));
        all.remove("type");
        all.remove("name");
        return all;
    }
}
