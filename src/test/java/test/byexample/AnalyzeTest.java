package test.byexample;

import junit.framework.TestCase;
import org.jdraft._code;
import org.jdraft._type;
import org.jdraft.io._archive;
import org.jdraft.io._io;
import org.jdraft.proto.$;
import org.jdraft.proto.$anno;
import org.jdraft.proto.$id;
import org.jdraft.proto.$typeRef;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class AnalyzeTest extends TestCase {

    /*
    Heres how I created the archive... it's just some source (containing the letter z in the name) from a big
    jar... I did this to speed up test time
    public void setUp(){
        _archive _a = _archive.of("C:\\Users\\Eric\\Downloads\\javaparser-core-3.6.7-sources.jar", p-> p.toString().contains("z"));
        //System.out.println( _a.list_types() );
        //let me create a smaller jar with only a few types
        _archive _a2 = _a.toJar("C:\\temp\\gen-source.jar");

        _a2.for_types( t-> System.out.println(t.getFullName()));
    }
     */


    public void testAnaylzeArchive(){
        //read all the source code in this .jar file
        _archive _a2 = _archive.of("C:\\temp\\gen-source.jar");

        //what I might want to do is to

        //replace All @annotations with @Deprecated and return all of the _code files
        List<_code> _ts = $.anno().replaceIn(_a2, $anno.of("@Deprecated") );

        System.out.println( _io.out("C:\\temp\\refactored", _ts));
        //_ts.forEach( t -> System.out.println( t ) );
    }

    /** Slow
    public void testAnalyze(){
        //really, most of the time is spent parsing (creating the _code from java files)
        long start = System.currentTimeMillis();
        _code._cache _cache = _code._cache.of( _archive.of("C:\\Users\\Eric\\Downloads\\javaparser-core-3.6.7-sources.jar") );
        long end = System.currentTimeMillis();

        $.method($.PUBLIC, $id.of("get$Name$"),$typeRef.of( t->!t.isVoid()) ).printIn( _cache );
        long fin = System.currentTimeMillis();

        System.out.println( "Parsing took "+ (end - start)); //4911, 5697, 4750
        System.out.println( "Query took   "+ (fin - end));   //157, 258, 497
        //query a github project, and find the number
    }
     */

    /**
     *

    public static class GitHubJavaProject implements _code._provider{
        public String projectUrl; //make this a URL instead
        public long downloadTimestamp;
        public List<_code> lazyCachedCode;
        public List<String> cachedFiles;

        public GitHubJavaProject (String projectUrl){
            this( projectUrl, true);
        }

        private GitHubJavaProject(String projectUrl, boolean lazyLoad){
            this.projectUrl = projectUrl;
        }


        /**
         * Lazily read from the github project
         * @return

        public List<_code> list_code(){
            //here read from github
            return Collections.EMPTY_LIST;
        }
    }
    */

}
