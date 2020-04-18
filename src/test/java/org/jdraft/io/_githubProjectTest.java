package org.jdraft.io;

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
        _codeUnits _cus = _githubProject.of("https://github.com/edefazio/bincat").load();

        //Print.tree(_sc.get_class("SourcePathEx"));
        //System.out.println( $int.of(65).firstIn(_cus) ); //here

        System.out.println( _cus.size() );

        //_type _t = _type.of(_gitHubProject.of("https://github.com/org-jdraft/jdraft").downloadJavaFileURL(Ast.class));
        //Print.tree(_t);
    }
}
