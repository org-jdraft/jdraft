package test.bulk;

import java.io.*;
import java.net.URI;
import java.nio.CharBuffer;
import java.nio.file.Path;
import javax.tools.FileObject;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * Stores the contents of the File as an XML DOM object in memory
 * @author Eric
 */
public class XmlDomFile implements ModelFile<Document> {

    public static Document dom( String...xmlFileContents ){
        return dom( new ByteArrayInputStream( ModelFile.combine(xmlFileContents).getBytes()));                 
    }
    
    public static Document dom( Path filePath ){
        try{
            return dom(new FileInputStream(filePath.toFile()));
        }catch(IOException ioe ){
            throw new RuntimeException("unable to create file", ioe);
        }        
    }
    
    public static Document dom( InputStream is){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder.parse(is);
        } catch (ParserConfigurationException pce) {
            throw new RuntimeException("unable to configure parser", pce);
        } catch (IOException ioe) {
            throw new RuntimeException("unable to write bytes", ioe);
        } catch (SAXException se) {
            throw new RuntimeException("Parse XML", se);
        }
    }

    public static XmlDomFile of(Path basePath, Path filePath){
        return new XmlDomFile(basePath, filePath);
    }
    
    /** Relative Path to the file */
    public Path relativePath;
    
    /** We store the file as a DOM document (not as byte[]) */
    private Document document;
    
    public long lastModified;

    public XmlDomFile(Path basePath, Path filePath){
        this.relativePath = basePath.relativize(filePath);
        this.document = dom (filePath);
    }
    
    public XmlDomFile(Path relativePath, String content){
        this.relativePath = relativePath;
        this.document = dom(content);
    }
    
    
    @Override
    public URI toUri() {
        return ModelFile.fileUri(this.relativePath);
    }

    @Override
    public String getName() {
        return this.relativePath.toString();
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return new ByteArrayInputStream(this.document.getTextContent().getBytes());
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
        return this.document.getTextContent();
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
    public Document getModel() {
        return this.document;
    }

    @Override
    public XmlDomFile setModel(Document model) {
        this.document = model;
        return this;
    }
}
