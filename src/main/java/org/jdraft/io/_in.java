package org.jdraft.io;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * Input sources to be read from (i.e. .java source code) and resolvers
 * for finding and returning the data.
 *
 * A specific source for input data (i.e. File, etc). maintaining it's original
 * sourceId and a description of where it originates from
 *
 * @author Eric
 */
public interface _in {

    /**
     * @return description of originating source(FilePath, the ClassPath, URL)
     */
    String describe();

    /**
     * How is this source identified:
     * i.e. for the Java Map class: "java.lang.Map"
     *
     * @return the sourceId requested
     */
    String getSourceId();

    /**
     * @return source input walk
     */
    InputStream getInputStream();
    
    Path getPath();

    /**
     * Implementation of an _in (input source)
     */
    class _source implements _in {

        /**
         * @param sourceId
         * @param description
         * @param is
         * @return
         */
        public static _source of(String sourceId, String description, InputStream is ) {
            return new _source( sourceId, description, is );
        }

        /**
         * @param sourceId
         * @param description
         * @param is
         * @return
         */
        public static _source of(Path path, String sourceId, String description, InputStream is ) {
            _source _s = new _source( sourceId, description, is );
            _s.path = path;
            return _s;
        }
        
        /** the (optional) path to where the file loaded it*/
        public Path path;
        
        private final String description;

        private final String sourceId;

        private final InputStream is;

        public _source( Path path, String sourceId, String description, InputStream is ) {
            this.description = description;
            this.sourceId = sourceId;
            this.is = is;
        }
        
        public _source( String sourceId, String description, InputStream is ) {
            this( null, sourceId, description, is);
        }

        
        @Override
        public String describe() {
            return description;
        }

        @Override
        public String getSourceId() {
            return sourceId;
        }

        @Override
        public InputStream getInputStream() {
            return this.is;
        }

        @Override
        public Path getPath(){
            return this.path;
        }
        
        @Override
        public String toString(){
            return getSourceId()+" "+ describe();
        }
    }

    /**
     * Returns an _in(put) given a String sourceId
     * (i.e. "java.lang.String.java")
     * or Class
     * (i.e. Map.class ) (to return the .java source of said class)
     *
     */
    interface _resolver{

        /**
         * in and return the In for the sourceId (or null if not resolved)
         *
         * @param sourceId identity of the source (i.e. "com/myapp/Afile.html", "java/util/Map.java")
         * @return the _in
         */
        _in resolve(String sourceId);

        /**
         * @param clazz
         * @return In based on the clazz
         */
        _in resolve(Class clazz);

        /**
         * describe How the resolver resolves source
         * @return s
         */
        String describe();
    }
}
