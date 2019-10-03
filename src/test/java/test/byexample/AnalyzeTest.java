package test.byexample;

import com.github.javaparser.ast.body.TypeDeclaration;
import junit.framework.TestCase;
import org.jdraft._code;
import org.jdraft.io._archive;
import org.jdraft.io._io;
import org.jdraft.pattern.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    /************************************************************************** WORKS BUT SLOW

    public void testAnalyzeProjects(){
        _archive _ar =
                //_archive.of("C:\\Users\\Eric\\Downloads\\jackson-core-2.9.9-sources.jar");
                //_archive.of("C:\\Users\\Eric\\Downloads\\jetty-server-9.4.20.v20190813-sources.jar");
                //_archive.of("C:\\Users\\Eric\\Downloads\\guava-28.1-jre-sources.jar");
                //_archive.of("C:\\Users\\Eric\\Downloads\\commons-lang-2.6-sources.jar");
                _archive.of("C:\\Users\\Eric\\Downloads\\commons-lang3-3.9-sources.jar");

        //Cache all of the code so we can do multiple analysis (without reparsing each time)
        _code._cache cc = _code._cache.of(_ar);

        $interface.of().streamIn(cc).forEach( i -> System.out.println(i.getFullName()));

        //print all anonymous classes
        $.objectCreation(oce-> oce.getAnonymousClassBody().isPresent()).printIn(cc);

        //print all local classes that are within methods
        $.localClassStmt().$hasParent($.method()).printIn(cc);

        $.comment(c -> c.getContent().contains("TODO")).printIn(cc);

        //print all lambdas with no parameters
        $.lambda(l-> l.getParameters().isEmpty()).printIn(cc);
    }

    public void testAnalyzeSpringInLocalJar(){

        _archive _ar =
                _archive.of("C:\\Users\\Eric\\Downloads\\spring-core-5.1.9.RELEASE-sources.jar");

        //Cache all of the code so we can do multiple analysis (without reparsing each time)
        _code._cache cc = _code._cache.of(_ar);

        //print all the annotations found in the Spring framework core
        $.anno().printIn(cc);

        //print all comments with "TODO"
        $.comment(c -> c.getContent().contains("TODO")).printIn(cc);

        //print all methods containing synchronized statements
        $.method().$hasDescendant( $.synchronizedStmt() ).printIn(cc);

        //print all local classes that are within methods
        $.localClassStmt().$hasParent($.method()).printIn(cc);

        //print all lambdas with no parameters
        $.lambda(l-> l.getParameters().isEmpty()).printIn(cc);

        //print all nested types
        $.of(TypeDeclaration.class).$hasAncestor(1, $.of(TypeDeclaration.class)).printIn(cc);

        //count all types containing at least one nested type
        System.out.println( $.of(TypeDeclaration.class).$hasDescendant(1, $.of(TypeDeclaration.class)).count(cc) );
    }
     */


    public void testAnalyzeJavaParserInURLJar(){
        Map<String,String> mm = new HashMap();
        //mm.putAll( System.getProperties() );
        System.getProperties().entrySet().stream().filter(e -> e.getValue().toString().contains(".m2"))
                .forEach(e ->System.out.println(e.getKey()+" "+e.getValue()));

        //System.getProperties().entrySet().stream().filter(e -> e.getValue().toString().contains(".m2"))
        //        .forEach(e ->System.out.println(e.getKey()+" "+e.getValue()));
        //System.out.println(  );
        //String JavaParserUrl = "https://repo1.maven.org/maven2/com/github/javaparser/javaparser-core/3.14.11/javaparser-core-3.14.11-sources.jar";
    }

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
