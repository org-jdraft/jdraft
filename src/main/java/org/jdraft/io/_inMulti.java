package org.jdraft.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Checks multiple {@link _in._resolver}s for a specific input source
 * (similar to a ClassPath resolution for Classes)
 *
 * @author Eric
 */
public final class _inMulti {

    private final List<_in._resolver> inpaths;

    public static _inMulti of(List<_in._resolver> paths ){
        return new _inMulti( paths );
    }

    public static _inMulti of( _in._resolver... paths ){
        return new _inMulti( paths );
    }

    public _inMulti( List<_in._resolver> paths ){
        this.inpaths = new ArrayList<_in._resolver>();
        inpaths.addAll( paths );
    }

    public _inMulti( _in._resolver... paths ){
        this.inpaths = new ArrayList<>();
        Collections.addAll(inpaths, paths);
    }

    public _inMulti add( List<_in._resolver> paths ){
        inpaths.addAll( paths );
        return this;
    }

    public _inMulti add( _in._resolver...ir ){
        inpaths.addAll(Arrays.asList(ir));
        return this;
    }

    public _in resolve(String sourceId ) {
        for(int i=0;i<this.inpaths.size();i++){
            _in in = this.inpaths.get(i).resolve(sourceId);
            if( in != null ){
                return in;
            }
        }
        return null;
    }

    public _in resolve(Class clazz ) {
        for(int i=0;i<this.inpaths.size();i++){
            _in in = this.inpaths.get(i).resolve( clazz );
            if( in != null ){
                return in;
            }
        }
        return null;
    }

    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append( "MultiPath" );
        sb.append( System.lineSeparator() );
        for(int i=0;i<this.inpaths.size();i++){
            sb.append( "    " );
            sb.append( this.inpaths.get( i ).describe() );
            sb.append( System.lineSeparator() );
        }
        return sb.toString();
    }
}
