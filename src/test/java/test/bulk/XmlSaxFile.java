package test.bulk;

import java.io.* ;
import java.nio.file.Path;
import javax.xml.parsers.*;
import org.xml.sax.*;

/**
 * Adapts this file to accept SAX {@link ContentHandler}s
 * it simply extends the ByteArrayFile implementation and adds
 * the handle(ContentHandler) 
 * 
 * Great if you want to have files that you READ FROM but don't modify
 *  
 * Instead of storing the File in an Object form, just stores the File as
 * bytes and provides the {@link #handle(org.xml.sax.ContentHandler) } methods
 * to access the data within the file
 * 
 * BUILT FOR READING NOT WRITING
 * 
 * @author Eric
 */
public class XmlSaxFile extends ByteArrayFile {
    
    /** the underlying SAX Parser for calling Handle */
    public SAXParser saxParser;
    
    public XmlSaxFile(Path basePath, Path filePath){
        super(basePath, filePath);
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        try{
            this.saxParser = spf.newSAXParser();
        }catch(ParserConfigurationException e){
            throw new RuntimeException("unable to create SAXParser", e);
        }catch(SAXException sxe){
            throw new RuntimeException("SAXException creating SAXParser", sxe);
        }
    }
    
    public final XmlSaxFile setSAXParser(SAXParser sxp){
        this.saxParser = sxp;
        return this;
    }
    
    /** 
     * Use the embedded SAXParser to handle 
     * @param xmlContentHandler
     */
    public void handle( ContentHandler xmlContentHandler ){
        handle(this.saxParser, xmlContentHandler);
    }
    
    /**
     * Pass in a SAXParser and ContentHandler to query the file
     * @param saxParser
     * @param xmlContentHandler 
     */
    public void handle( SAXParser saxParser, ContentHandler xmlContentHandler){
        try{
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(xmlContentHandler);
            xmlReader.parse( new InputSource( new ByteArrayInputStream(this.bytes.toByteArray() )) );
        }catch(IOException e){
            throw new RuntimeException("unable to read from file "+ this.getName() );
        } catch(SAXException se){
            throw new RuntimeException("SAXException processing file "+ this.getName() );
        }
    }    
}
