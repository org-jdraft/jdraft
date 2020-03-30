package test.byexample;

import junit.framework.TestCase;
import org.jdraft.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Illustrates easy ways of building a model
 * Readability - "Eliminate the unnecessary so that the necessary may speak" (Hans Hofmann)
 * Brevity - "...is the soul of wit"
 *
 */
public class CreateModelOfTest extends TestCase {

    static class InnerClass{ static int ID=12345; }

    /**
     * Illustrates the different ways of building a jdraft model
     * in this case the _class model is built with resources "Strings, Classes, etc."
     * that are "internal" meaning WITHIN YOUR CURRENT PROJECT...
     *
     * (** this means the source code can be found on the classpath)
     */
    public void testCreateTypeModelOf(){
        //create a base model with no properties
        _class _c = _class.of(); /** @see OfTest */

        //create a model providing a String/TextBlock (to be parsed)
        _class _cs = _class.of("class Point{ public int x,y; }"); /** @see OfStringTest */

        //** create a model from the source code of a given TOP LEVEL class
        _class _cc = _class.of(CreateModelOfTest.class); /** @see OfClassTest */

        //** create a model from the source code of a given INNER class
        _class _ci = _class.of(InnerClass.class); /** @see OfClassTest */

        class LocalPoint {
            int x, y;
        }

        //** create a model based on the source code from a Local Class
        _class _cl = _class.of(LocalPoint.class); /** @see OfClassTest */

        //** create a model based on the source code within an Anonymous Class**
        _class _ca = _class.of("Point", new Object(){
            public int x,y;
        }); /** @see OfAnonymousClassTest */
    }

    public static void main(String[] args) throws Exception {
        new CreateModelOfTest().createOf_ExternalCode();
    }

    /**
     * Importing the source code from "external" sources
     * (i.e. not necessarily on your classpath)
     * <UL>
     *     <LI>from a Path on the File System</LI>
     *     <LI>from a URL</LI>
     *     <LI>from an InputStream</LI>
     * </UL>
     * @throws MalformedURLException
     */
    public static void createOf_ExternalCode() throws IOException {

        //build a model from the source file at a Path (not on classpath)
        _class _cp = _class.of(Paths.get("C:\\temp\\Point.java"));

        //build a model from the source at the URL (here a github gist)
        _class _curl = _class.of(
                new URL("https://gist.githubusercontent.com/edefazio/bf47f86143d78df36382c68d9fdff6f5/raw/b46d7774013185458c44591dbf0c939fe54e636f/test.othertools.JavaParserLucaAccumulateTest.java") );

        //build a model from an inputStream (here from a url's InputStream)
        try{
            URL u = new URL("https://raw.githubusercontent.com/org-jdraft/jdraft/master/src/main/java/org/jdraft/_boolean.java");
            InputStream inStream = u.openStream();
            _type _ccc = _type.of(inStream);
        } catch(IOException mue){
            throw new RuntimeException("couldnt read from url");
        }
    }

}
