package org.jdraft.io;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import junit.framework.TestCase;
import org.jdraft._codeUnits;
import org.jdraft.bot.$int;
import org.jdraft.bot.$methodCall;

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
        _codeUnits _cus = _bc.load();
        //_codeUnits _cus = _githubProject.of("https://github.com/edefazio/bincat").load(jp);
        //_codeUnits _cus = _githubProject.of("https://github.com/TIBCOSoftware/jasperreports").load();
        //_codeUnits _cus = _githubProject.of("https://github.com/wmdietl/javaparser-module-bug").load();
        //Print.tree(_sc.get_class("SourcePathEx"));
        //System.out.println( $int.of(65).firstIn(_cus) ); //here

        System.out.println( _cus.size() );

        //_type _t = _type.of(_gitHubProject.of("https://github.com/org-jdraft/jdraft").downloadJavaFileURL(Ast.class));
        //Print.tree(_t);
    }
}
