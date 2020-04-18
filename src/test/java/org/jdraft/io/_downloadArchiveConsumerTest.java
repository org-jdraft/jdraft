package org.jdraft.io;

import junit.framework.TestCase;
import org.jdraft._codeUnit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;

public class _downloadArchiveConsumerTest extends TestCase {
    public void testNothing(){

    }

    public static void main(String[] args) throws IOException {
        //URL url = new URL("https://github.com/edefazio/bincat/archive/master.zip");

        _sources _sc = _sources.of();
        //reads from a maven-central jar file
        _downloadArchiveConsumer.of("https://search.maven.org/remotecontent?filepath=com/github/javaparser/javaparser-core/3.15.18/javaparser-core-3.15.18-sources.jar",
                (ZipEntry ze, InputStream is)-> {
                if( ze.getName().endsWith(".java")){
                    _sc.add( _codeUnit.of(is) );
                }
            });
         System.out.println( _sc.size() );

        _sources _src = _sources.of(_githubProject.of("https://github.com/edefazio/bincat") );
        //_sources _src = _sources.of();
        //_downloadArchive _da = _downloadArchive.of(url, (ZipEntry ze,InputStream is)-> {
        //    if( ze.getName().endsWith(".java")){
        //        _src.add( _codeUnit.of(is) );
        //    }
        //});
        /*
        try( InputStream inputStream = url.openStream();
             ZipInputStream zis = new ZipInputStream(inputStream) ) {

             byte[] buffer = new byte[2048];

             while (zis.available() > 0) {
                 ZipEntry ze = zis.getNextEntry();
                 System.out.println("reading " + ze.getName());

                 if( ze.isDirectory() ){
                     ze.
                 } else if( ze.)

                     if (!ze.isDirectory() && ze.getName().endsWith(".java")) {
                         ByteArrayOutputStream baos = new ByteArrayOutputStream();
                         int len;
                         while ((len = zis.read(buffer)) > 0) {
                             baos.write(buffer, 0, len);
                         }
                         try {
                             System.out.println("adding" + ze.getName());
                             _src.add(_codeUnit.of(baos.toString()));
                         } catch (Exception e) {
                             throw new _ioException("could not read from entry " + ze.getName());
                         }
                     }
                 }
             } catch (IOException e) {
                e.printStackTrace();
            }
            */

        System.out.println( "finished reading "+ _src.size());
    }
}
