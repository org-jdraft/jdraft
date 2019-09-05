package org.jdraft.proto;

import org.jdraft.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class $type {

    public static $proto.$tokens selectConstructors(List<$constructor> $protoCtors, _constructor._hasConstructors _hcs ){
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
        $proto.$tokens all = $proto.$tokens.of();
        selectMap.values().forEach( ls -> ls.forEach( s-> all.putAll(s.tokens()) ));
        return all;
    }


    public static $proto.$tokens selectMethods(List<$method> $protoMethods, _method._hasMethods _hcs ){
        Map<$method, List<$method.Select>> selectMap = new HashMap<>();

        for(int i=0;i<$protoMethods.size(); i++) {
            final $method t = $protoMethods.get(i);
            List<$method.Select>matches = new ArrayList<>();
            _hcs.listMethods().forEach( c ->{
                $method.Select sel = t.select( (_method)c);
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
        $proto.$tokens all = $proto.$tokens.of();
        selectMap.values().forEach( ls -> ls.forEach( s-> all.putAll(s.tokens()) ));
        return all;
    }

    public static $proto.$tokens selectFields(List<$field> $protoFields, _field._hasFields _hcs ){
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
        $proto.$tokens all = $proto.$tokens.of();
        selectMap.values().forEach( ls -> ls.forEach( s-> all.putAll(s.tokens()) ));
        return all;
    }


    public static $proto.$tokens selectInitBlocks(List<$initBlock> $protoInitBlocks, _initBlock._hasInitBlocks _hcs ){
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
        $proto.$tokens all = $proto.$tokens.of();
        selectMap.values().forEach( ls -> ls.forEach( s-> all.putAll(s.tokens()) ));
        return all;
    }

    public static $proto.$tokens selectImports(List<$import> $protoImports, _code _hcs ){
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
        $proto.$tokens all = $proto.$tokens.of();
        selectMap.values().forEach( ls -> ls.forEach( s-> all.putAll(s.tokens()) ));
        return all;
    }
}
