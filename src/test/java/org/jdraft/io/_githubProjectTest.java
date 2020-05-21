package org.jdraft.io;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._project;

public class _githubProjectTest extends TestCase {

    public void testNothing(){}

    //_url.of("...some github gist")
    public static void main(String[] args) {
        // point it to a github project (via URL)
        // download the zip project archive
        // extract all the .java source files from the Zip
        // create _jdraft models for each .java file and store them in _sources
        // return a _sources containing all the _jdraft models
        JavaParser jp = new JavaParser();
        jp.getParserConfiguration().setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_7);
        _githubProject _bc  = _githubProject.of("https://github.com/edefazio/bincat").setJavaParser( jp );
        _project _pro = _bc.load();
        //_codeUnits _cus = _githubProject.of("https://github.com/edefazio/bincat").load(jp);
        //_codeUnits _cus = _githubProject.of("https://github.com/TIBCOSoftware/jasperreports").load();
        //_codeUnits _cus = _githubProject.of("https://github.com/wmdietl/javaparser-module-bug").load();
        //Print.tree(_sc.get_class("SourcePathEx"));
        //System.out.println( $int.of(65).firstIn(_cus) ); //here

        System.out.println( _pro.size() );
        System.out.println( _pro.getOrigin() );


        //verify that all of the compilationUnits are able to
        assertTrue(_pro.list(_cu -> !_cu.getParentProject().equals(_pro)).isEmpty() );

        //verify that the origin is
        assertNotNull( _pro.first(_class.class).getOrigin() );





        //_type _t = _type.of(_gitHubProject.of("https://github.com/org-jdraft/jdraft").downloadJavaFileURL(Ast.class));
        //Print.tree(_t);
    }
}
