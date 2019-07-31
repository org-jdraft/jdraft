package test.bulk;

import java.io.*;
import java.net.URI;
import java.nio.CharBuffer;
import java.nio.file.Path;
import java.util.Properties;
import javax.tools.FileObject;

/**
 * Stores the contents of the File as Properties
 * 
 * @author Eric
 */
public class PropertiesFile implements ModelFile<Properties> {

    public static Properties properties( Path filePath ){
        try {
            Properties properties = new Properties();
            properties.load( new FileInputStream( filePath.toFile()));            
            return properties;
        } catch (IOException ioe) {
            throw new RuntimeException("unable to write bytes", ioe);
        }
    }
    
    public static Properties properties( String...lines ){
        Properties properties = new Properties();
        try{
            properties.load( new ByteArrayInputStream( ModelFile.combine(lines).getBytes() ) );
            return properties;
        } catch (IOException ioe) {
            throw new RuntimeException("unable to write properties file", ioe);
        }
    }
    
    public static PropertiesFile of( Path basePath, Path filePath){
        return new PropertiesFile(basePath, filePath);
    }
    
    /** Relative Path to the file */
    public Path relativePath;
    
    /** We store the file as a Properties (not as byte[]) */
    private Properties properties;
    
    public long lastModified;

    public PropertiesFile(Path basePath, Path filePath){
        this.relativePath = basePath.relativize(filePath);
        this.properties = properties( filePath );        
    }
    
    @Override
    public URI toUri() {
        return ModelFile.fileUri(relativePath);
    }

    @Override
    public String getName() {
        return this.relativePath.toString();
    }

    @Override
    public InputStream openInputStream() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.properties.store(baos, null);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    @Override
    public OutputStream openOutputStream() throws IOException {        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Wraps the result of {@linkplain #getCharContent} in a Reader. Subclasses
     * can change this behavior as long as the contract of {@link FileObject} is
     * obeyed.
     *
     * @param ignoreEncodingErrors {@inheritDoc}
     * @return a Reader wrapping the result of getCharContent
     * @throws IllegalStateException {@inheritDoc}
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public Reader openReader(boolean ignoreEncodingErrors) throws IOException {

        CharSequence charContent = getCharContent(ignoreEncodingErrors);
        if (charContent == null) {
            throw new UnsupportedOperationException();
        }
        if (charContent instanceof CharBuffer) {
            CharBuffer buffer = (CharBuffer) charContent;
            if (buffer.hasArray()) {
                return new CharArrayReader(buffer.array());
            }
        }
        return new StringReader(charContent.toString());
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.properties.store(baos, null);
        return baos.toString();
    }

    @Override
    public Writer openWriter() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLastModified() {
        return 0L;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public Properties getModel() {
        return this.properties;
    }

    @Override
    public PropertiesFile setModel(Properties model) {
        this.properties = model;
        this.lastModified = System.currentTimeMillis();
        return this;
    }
}
