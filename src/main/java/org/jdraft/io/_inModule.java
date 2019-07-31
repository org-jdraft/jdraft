package org.jdraft.io;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * API is good
 * piecewise refinement & feedback
 *
 * API is bad
 * ONLY a Large configuration file
 *
 * Digest
 * One bite at a time constant reinforcement you are moving in the right direction
 *
 * vs parachuting into the middle of complexity
 * & if you touch anything it could break something
 *
 * Represents a module root and the contained source code within
 *
 * @author Eric
 */
public final class _inModule implements _in._resolver {

    public final String moduleRoot;
    public final _inFilePath classes;
    public final _in._resolver tests;

    public _inModule( String moduleDir ){
        Path p = Paths.get( moduleDir );
        this.moduleRoot = moduleDir;
        this.classes = _inFilePath.of(moduleDir);

        Path testsPath = p.resolveSibling( "tests" );
        if( testsPath.toFile().isDirectory() ){
            //System.out.println( "found tests directory: "+ testsPath.toFile() );
            this.tests = _inFilePath.of( testsPath.toFile() );
        } else{
            this.tests = new _in._resolver() {
                @Override
                public _in resolve( String sourceId ) {
                    return null;
                }

                @Override
                public _in resolve( Class clazz ) {
                    return null;
                }

                @Override
                public String describe() {
                    return "[EMPTY]";
                }
            };
        }
    }

    public static _inModule of( File moduleRoot ){
        return new _inModule( moduleRoot.getAbsolutePath()+"\\" );
    }

    @Override
    public _in resolve(String sourceId) {
        //System.out.println( "resolving from "+classes.describe() +" Of "+ sourceId);
        _in _i = classes.resolve( sourceId );
        if( _i != null ){
            return _i;
        }
        return this.tests.resolve( sourceId );
    }

    @Override
    public String describe() {
        return "[Module "+this.moduleRoot+"]";
    }

    @Override
    public _in resolve(Class clazz) {
        //System.out.println( "resolving from "+classes.describe() +" Of "+ clazz);
        _in _i = classes.resolve( clazz );
        if( _i != null ){
            return _i;
        }
        return this.tests.resolve( clazz );
    }
}



