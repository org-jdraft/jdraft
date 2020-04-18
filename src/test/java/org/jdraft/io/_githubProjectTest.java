package org.jdraft.io;

import junit.framework.TestCase;
import org.jdraft.bot.$int;
import org.jdraft.bot.$methodCall;

public class _githubProjectTest extends TestCase {

    public void testNothing(){}

    //_url.of("...some github gist")
    public static void main(String[] args) {
        //I'd like to make this happen, where it'll point it to a github project
        // download the code
        // extract all the .java source files
        // create _jdraft models for each and store them in memory
        // return a _sources containing all the _jdraft models

        // then I can query by simple name, etc.

        _sources _sc = _sources.of(_githubProject.of("https://github.com/edefazio/bincat"));
        //Print.tree(_sc.get_class("SourcePathEx"));
        System.out.println( $int.of(65).firstIn(_sc) ); //here

        System.out.println( _sc.size() );

        //_type _t = _type.of(_gitHubProject.of("https://github.com/org-jdraft/jdraft").downloadJavaFileURL(Ast.class));
        //Print.tree(_t);
    }
}
