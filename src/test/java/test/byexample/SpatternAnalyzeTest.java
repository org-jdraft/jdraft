package test.byexample;

import com.github.javaparser.utils.Log;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._codeUnit;
import org.jdraft._project;
import org.jdraft._method;
import org.jdraft.io._archive;
import org.jdraft.io._io;
import org.jdraft.pattern.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpatternAnalyzeTest extends TestCase {

    /**
     * This is how I might
     *
     * how about MODIFYING the setups methods to also have teardown methods

    $class $junit4TestClasses = $class.of().$extend(TestCase.class);
    $method $setUp    = $method.of($.PUBLIC, $.VOID, $name.of("setUp"),    $parameters.none() );//.$isParentMember($junit4TestClasses);
    $method $tearDown = $method.of($.PUBLIC, $.VOID, $name.of("tearDown"), $parameters.none() );//.$isParentMember($junit4TestClasses);
    $stmt $logSetupStmt = $stmt.of( ()-> Log.setAdapter(new Log.StandardOutStandardErrorAdapter()) );
    $stmt $logSilent = $stmt.of( ()-> Log.setAdapter(new Log.SilentAdapter()) );
    */

    public void test$methodShorthand(){

        $method $m = $method.of( $body.of(()->Log.setAdapter( new Log.StandardOutStandardErrorAdapter())));
        assertTrue( $m.matches("public void m(){ Log.setAdapter( new Log.StandardOutStandardErrorAdapter()); }") );
        assertTrue( $m.matches(_method.of("public void m(){ Log.setAdapter( new Log.StandardOutStandardErrorAdapter()); }")) );
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
        assertTrue( $m.matches("public void m(){ assert(true); Log.setAdapter( new Log.StandardOutStandardErrorAdapter()); }") );
        assertTrue( $m.matches(_method.of("public void m(){ assert(true); Log.setAdapter( new Log.StandardOutStandardErrorAdapter()); }")) );
        Log.setAdapter(new Log.SilentAdapter());
    }

    public void testTD(){
        $method $tearDown = $method.of(new Object(){
            public void tearDown(){
                Log.setAdapter( new Log.SilentAdapter() );
            }
        });
        $class $wTearDown = $class.of($tearDown);

        $class $noTearDown = $class.of().$not($tearDown);
        _class _wTearDown = _class.of("C", new Object(){
            public void tearDown(){
                Log.setAdapter(new Log.SilentAdapter());
            }
        });
        _class _noTearDownStmt = _class.of("C", new Object(){
            public void tearDown(){
                //Log.setAdapter(new Log.SilentAdapter());
            }
        });

        _class _noTearDownMethod = _class.of("C", new Object(){
        });

        assertTrue( $wTearDown.matches(_wTearDown));
        assertFalse( $wTearDown.matches(_noTearDownStmt));
        assertFalse( $wTearDown.matches(_noTearDownMethod));

        assertFalse( $noTearDown.matches(_wTearDown));
        assertTrue( $noTearDown.matches(_noTearDownStmt));
        assertTrue( $noTearDown.matches(_noTearDownMethod));
    }

    public void testULShortHand(){

        $class $c = $class.of( new TestCase(){
            public void setUp(){
                Log.setAdapter( new Log.StandardOutStandardErrorAdapter());
            }

            @_$not
            public void tearDown(){
                Log.setAdapter( new Log.SilentAdapter() );
            }
        });
        System.out.println($c);

        assertTrue( $c.matches( _class.of("aaaa.bbbb.C", new TestCase(){
            public void setUp(){
                System.out.println( "Here");
                Log.setAdapter( new Log.StandardOutStandardErrorAdapter());
            }
        })));


        assertFalse( $c.matches( _class.of("aaaa.bbbb.C", new TestCase(){
            public void setUp(){
                Log.setAdapter( new Log.StandardOutStandardErrorAdapter());
            }
            public void tearDown(){
                Log.setAdapter( new Log.SilentAdapter() );
            }
        })));
    }

    public void testUL(){
        $class $c = $class.of( _class.of("$name$", new TestCase(){
            //@_$and
            public void setUp(){
                Log.setAdapter( new Log.StandardOutStandardErrorAdapter());
                //Documented.class
            }

            //@_$not
            public void tearDown(){
                Log.setAdapter( new Log.SilentAdapter() );
            }
        }));
        System.out.println($c);
    }

    public void testSimpleFindTestClassesThatInitializeLogButDontSilenceIt(){
        //find a class containing a setUp method with a body containing a statement that
        //sets up the logger
        $class $unsilencedLog = $class.of(        //find a class
                $method.of("setUp",        //...containing a "setUp" method
                    $body.of(                     //...that contains a body with the Log.setAdapter(...) statement
                            $stmt.of( ()-> Log.setAdapter(new Log.StandardOutStandardErrorAdapter())))))
                .$not(                            //...that does NOT have
                    $method.of("tearDown", //...a method named "tearDown"
                        $body.of(                 //...that contains a body with the Log.setAdapter(...) statement
                            $stmt.of( ()-> Log.setAdapter(new Log.SilentAdapter())))) )
                .$extends(TestCase.class);        //the class extends TestCase

        /* Commented out ... WORKS BUT SLOW
        long start = System.currentTimeMillis();
        _code._cache _cc = _code._cache.of(_batch.of("C:\\jdraft\\project\\jdraft\\src\\test\\java"));
        long end = System.currentTimeMillis();
        //$setUp.printIn(_cc);

        $unsilencedLog.forEachIn(_cc, c-> System.out.println( c.astCompilationUnit().getStorage().get().getPath() ));
        long atEnd = System.currentTimeMillis(); 
        System.out.println( "Took "+ (end - start)); //4764millis  5164                 2352millis (only test src directory)
        System.out.println( "Took "+ (atEnd - end)); //945millis   670 (added TestCase) 228 (only test src directory)
         */
    }

    /**
     * Here is the "More specific variant" that
     * So there I was... just minding my own business
     *
     */
    public void testFindTestClassesThatInitializeTheLogButDontSilenceIt(){
        //heres a simplified version
        //a class that has a setUp method that sets the log appender
        $class.of( $method.of("setUp",
            $body.of( $stmt.of( ()-> Log.setAdapter(new Log.StandardOutStandardErrorAdapter())))))
            //that does not
            .$not( $method.of("tearDown",
            $body.of( $stmt.of( ()-> Log.setAdapter(new Log.SilentAdapter())))) );

        //Here is a MORE SPECIFIC variant... more specific in a sense
        //heres the full one... You probably dont need to get this
        $class.of(
                $method.of("setUp", $.PUBLIC,$.VOID,$parameters.none(),
                        $body.of($stmt.of( ()-> Log.setAdapter(new Log.StandardOutStandardErrorAdapter())) ) ) )
                .$not($method.of( "tearDown", $.PUBLIC, $.VOID, $parameters.none(),
                        $body.of($stmt.of( ()-> Log.setAdapter(new Log.SilentAdapter()) )) ))
                .$extends(TestCase.class);

        //break down each into further components
        $stmt $logSetupStmt = $stmt.of( ()-> Log.setAdapter(new Log.StandardOutStandardErrorAdapter()) );
        $stmt $logSilent = $stmt.of( ()-> Log.setAdapter(new Log.SilentAdapter()) );

        $method $setUp = $method.of($.PUBLIC, $.VOID, $name.of("setUp"), $parameters.none(), $body.of($logSetupStmt) );
        $method $tearDown = $method.of($.PUBLIC, $.VOID, $name.of("tearDown"), $parameters.none(), $body.of($logSilent) );

        //this means find all classes that extend TestCase
        // that have a setupMethod containing the log setup statement
        // ...and DO NOT have a tearDown Method containing the logSilent statement
        //containing the logSilent
        $class $c = $class.of($setUp).$extends(TestCase.class).$not($tearDown);

        /***** THIS IS SLOW BUT IT WORKS
        _code._cache _cc = _code._cache.of(_batch.of("C:\\jdraft\\project\\jdraft\\src"));
        //$setUp.printIn(_cc);

        $c.forEachIn(_cc, c-> System.out.println( c.astCompilationUnit().getStorage().get().getPath() ));
        //$c.printIn(_cc);
        */
    }

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

    public void tearDown(){

    }

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

    public void anaylzeArchive(){
        //read all the source code in this .jar file
        _project _a2 = _archive.of("C:\\temp\\gen-source.jar").load();

        //what I might want to do is to

        //replace All @annotations with @Deprecated and return all of the _code files
        List<_codeUnit> _ts = $.anno().replaceIn(_a2, $annoRef.of("@Deprecated") );

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
