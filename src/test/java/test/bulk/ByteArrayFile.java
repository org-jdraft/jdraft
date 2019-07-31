package test.bulk;

import java.io.*;
import java.net.URI;
import java.nio.CharBuffer;
import java.nio.file.*;
import javax.tools.FileObject;

/**
 * <P>Simple {@link FileObject} with data stored in a ByteArrayOutputStream
 * can be used when the user might want to directly manipulate bytes.</P>
 * 
 * <P>The idea behind these implementations of {@link FileObject} is that its a 
 * FILE (A Path & byte[]), and it CAN be an object/abstraction SECOND.
 * In short:
 * <PRE> Everything is a File, some things are Objects/abstractions</PRE>
 * </P>
 * 
 * <P>Its sort of the opposite of abstractions in Java, where: 
 * <PRE>
 * "everything is an object"
 * and
 * "some things are serializable (to files)"
 * </PRE>
 * </P>
 * 
 * The ByteArrayFile (Its a good "fallback" for files... i.e. files that COULD 
 * be textual or binary), or a base class (i.e. SAXFile, which allows querying 
 * via the SAX API but isn't manipulating the bytes)
 */
public class ByteArrayFile implements FileObject {

    /** the relative path (as related to the basePath) to the file */
    public Path relativePath;
    
    /** the file contents */
    public ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    /** The last modified time of the file abstraction */
    public long lastModified = 0L;
    
    /**
     * Read in the File bytes at filePath & find the relativePath by relativizing 
     * the filePath based on the basePath.
     * @param basePath the base Path where all files are read from
     * @param filePath the fully qualified path to the file to read in
     */
    public ByteArrayFile(Path basePath, Path filePath){        
        this.relativePath = basePath.relativize(filePath);
        try{            
            this.bytes.write(Files.readAllBytes(filePath) );
            this.lastModified = filePath.toFile().lastModified();
        }catch(IOException ioe){
            throw new RuntimeException("Unable to read from "+filePath, ioe);
        }
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
        return new ByteArrayInputStream(this.bytes.toByteArray());
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return bytes;
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
        return new String(this.bytes.toByteArray());
    }

    @Override
    public Writer openWriter() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLastModified() {
        return lastModified;
    }

    @Override
    public boolean delete() {
        return false;
    }
}
