package org.jdraft.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.BiConsumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Given a url to an archive file (.zip, or .jar)
 * reads the archive as an InputStream and perform some user defined action based on a
 * BiConsumer<ZipEntry,InputStream> consumer
 * provided
 *
 * for example:
 *
 * //to read a zip archive from github, and build the _jdraft sources from all .java files within the downloaded archive:
 * _sources _src = _sources.of();
 *
 * _downloadArchiveConsumer.of("https://github.com/edefazio/bincat/archive/master.zip",(ZipEntry ze, InputStream is)->{
 *     if(ze.getName().endsWith(".java")) {
 *         try {
 *             _src.add(_codeUnit.of(is));
 *         }catch(Exception e){
 *             System.out.println( "Couldn't parse \""+ ze.getName());
 *         }
 *     }
 * });
 * //now sources is populated with all of the
 *
 */
public class _downloadArchiveConsumer {

    /**
     * Download the archive and based on the url and using the consumer
     * @param url
     * @param consumer
     * @return
     */
    public static void of(String url, BiConsumer<ZipEntry,InputStream> consumer){
        try{
            of( new URL(url), consumer);
        } catch (MalformedURLException e) {
            throw new _ioException("unable to build URL from \""+url+"\"");
        }
    }

    public static void of(URL url, BiConsumer<ZipEntry,InputStream> consumer){
        try {
            _downloadArchiveConsumer.readZip(url.openStream(), consumer);
        }catch(IOException ioe){
            throw new _ioException("error opening url stream "+ url.toString());
        }
    }

    private static void readZip(InputStream is, BiConsumer<ZipEntry,InputStream> consumer) throws IOException{
        try (ZipInputStream zipFile = new ZipInputStream(is);) {
            ZipEntry entry;
            while((entry = zipFile.getNextEntry()) != null){
                consumer.accept(entry, new FilterInputStream(zipFile) {
                    @Override
                    public void close() throws IOException {
                        zipFile.closeEntry();
                    }
                });
            }
        }
    }

    private _downloadArchiveConsumer(){}
}
