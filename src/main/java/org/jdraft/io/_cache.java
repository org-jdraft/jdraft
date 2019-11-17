package org.jdraft.io;

import org.jdraft._code;
import org.jdraft._java;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * _code._provider as List of local in memory _code objects
 *
 */
public class _cache <_C extends _code> implements _code._provider {

    public static _cache of(_code._provider..._providers ){
        List<_code> all = new ArrayList<>();
        Arrays.stream(_providers).forEach(_p -> all.addAll(_p.list_code()));
        return of(all);
    }

    public static _cache of(_code._provider _p ){
        return of(_p.list_code());
    }

    /**
     *
     * @param pathNames
     * @return
     */
    public static _cache of( String... pathNames ){
        Path[] paths = new Path[pathNames.length];
        for(int i=0;i<pathNames.length;i++){
            paths[i] = Paths.get(pathNames[i]);
        }
        return of (paths);
    }

    /**
     * Builds a code cache based on the code present at these paths (if the path is to a .jar, or .zip file
     * it will use an {@link _archive}, otherwise the Path will be a {@link _path})
     * @param paths the paths to include in the code cache
     * @return
     */
    public static _cache of( Path... paths ){
        _code._provider[] _ps = new _code._provider[paths.length];

        for(int i=0;i<paths.length;i++){
            //what if it's a .zip or .jar
            if( paths[i].toString().endsWith(".jar") ||  paths[i].toString().endsWith(".zip") ){
                _ps[i] = _archive.of(paths[i]);
            } else if( paths[i].toString().endsWith(".java") ){
                _ps[i] = _cache.of(_java.code( paths[i]  ) ); //add a cache with a single .java file
            } else{
                _ps[i] = _path.of(paths[i]); //Paths.get(pathNames[i]);
            }
        }
        return of (_ps);
    }

    public static <_C extends _code> _cache of( _C... _codeToCache){
        return of(Arrays.stream(_codeToCache).collect(Collectors.toList()));
    }

    public static <_C extends _code> _cache of( List<_C> _codeToCache){
        return new _cache( _codeToCache );
    }

    List<_C> codeList;

    public _cache( List<_C> _codeToCache ){
        this.codeList = _codeToCache;
    }

    @Override
    public <_C extends _code> List<_C> for_code(Class<_C> codeClass, Predicate<_C> _codeMatchFn, Consumer<_C> _codeActionFn) {
        List<_C> acted = new ArrayList<>();
        codeList.forEach( c -> {
            if(codeClass.isAssignableFrom( c.getClass() ) && _codeMatchFn.test( (_C)c) ){
                _codeActionFn.accept( (_C)c );
                acted.add((_C)c);
            }
        });
        return acted;
    }

    /**
     * Build & return a copy of the _code
     * @return
     */
    public _cache<_C> copy(){
        List<_C>copyList = new ArrayList<>();
        this.codeList.forEach(c -> copyList.add( (_C)c.copy() ) );
        return new _cache<_C>(copyList);
    }
}
