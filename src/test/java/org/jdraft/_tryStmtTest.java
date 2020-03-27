package org.jdraft;

import junit.framework.TestCase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

public class _tryStmtTest extends TestCase {


    public void testGetCatchWithUnionType(){

        _tryStmt _ts = _tryStmt.of( ()->{
            try {
                if( true) {
                    throw new IOException();
                }
                throw new URISyntaxException("A", "B", 1);
            } catch (IOException | URISyntaxException fnfe) {
                System.out.println("Union Type Block");
            }
         });
        _catch _c = _ts.getCatch(IOException.class);
        assertNotNull(_c);
        _c = _ts.getCatch(URISyntaxException.class);
        assertNotNull(_c);

        //we can check if it catches a Class, a Type, or a String
        assertTrue( _ts.catches(IOException.class));
        assertTrue( _ts.catches(URISyntaxException.class));
        assertTrue( _ts.catches( Ast.typeRef(IOException.class)) );
        assertTrue( _ts.catches( _typeRef.of(IOException.class)) );

        assertTrue( _ts.catches(IOException.class.getSimpleName()));
        assertTrue( _ts.catches(IOException.class.getCanonicalName()));

        assertTrue( _ts.catches(URISyntaxException.class.getSimpleName()));
        assertTrue( _ts.catches(URISyntaxException.class.getCanonicalName()));

        System.out.println( _c);
    }

    public void testBuildFromEmpty() throws FileNotFoundException {
        _tryStmt _ts = _tryStmt.of();
        _ts.addWithResources("FileInputStream f = new FileInputStream(\"C:\\temp\");");
        System.out.println( _ts);
        _ts.addCatch("catch(IOException e){ e.printStackTrace();}");
        System.out.println( _ts);
        _ts.setTryBody(_body.of( ()->{System.out.println(1);}));
        System.out.println( _ts);
        _ts.setFinallyBody(_body.of( ()->{System.out.println("finally"); return 3;}));

        _ts.addTry("System.out.println(1);");
        _ts.addTry( ()-> System.out.println("Hello in Lambda"));
        _ts.addFinally("System.out.println(\"fin\");");

        System.out.println( _ts );

        System.out.println( _ts.listCatches().stream().filter( _c-> _c.getParameter().isTypeRef(IOException.class)).findFirst().get() );

        //gets the catch block for IOException & adds code
        _ts.getCatch(IOException.class).add(()->System.out.println("Badness"));
        System.out.println( _ts);

        /*
        try (FileInputStream f = new FileInputStream("C:\temp")) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        /*
        _ts.setResources(()->{
            String zipFileName = "C:\\temp\\zip.zip";
            java.nio.charset.Charset charset =
                    java.nio.charset.StandardCharsets.US_ASCII;
            java.nio.file.Path outputFilePath =
                    java.nio.file.Paths.get("C:\\temp\\output");

            java.util.zip.ZipFile zf =
                    new java.util.zip.ZipFile(zipFileName);
            java.io.BufferedWriter writer =
                    java.nio.file.Files.newBufferedWriter(outputFilePath, charset)
        });

         */
    }
    public void testEmpty(){
        _tryStmt _ts = _tryStmt.of();
        //List<_expression> resources = ;
        assertTrue( _ts.listWithResources().isEmpty());
        assertFalse( _ts.hasWithResources() );
        assertFalse( _ts.hasCatch() );
        assertFalse( _ts.hasFinallyBody() );
        assertTrue(_ts.getTryBody().isEmpty());
        //assertTrue(_ts.getFinallyBody().isEmpty());
        assertNull(_ts.getFinallyBody());
        assertTrue(_ts.listCatches().isEmpty());

        _ts.ast().getTryBlock();
        _ts.ast().getFinallyBlock();
        _ts.ast().getResources();
        _ts.ast().getCatchClauses();
        //System.out.println( _ts.ast().getMetaModel().toString() );
        //catchClausesPropertyMetaModel
        //System.out.println( _ts.ast().getMetaModel().catchClausesPropertyMetaModel );

        //System.out.println( _ts.ast().getParsed().toString() );

    }

    public void testStmt(){
        _tryStmt _ts = _tryStmt.of();
        System.out.println( _ts.ast().getResources() );
        assertTrue( _ts.ast().getResources().isEmpty());

        //simple try catch with no body
        _ts = _tryStmt.of("try{}catch(Exception e){}");
        System.out.println( _ts);

        //try with single resource no catches
        _ts = _tryStmt.of("try(AutoCloseable ac = new AutoCloseable()){}");
        System.out.println( _ts);

        //try with multiple resources
        _ts = _tryStmt.of("try(AutoCloseable ac = new AutoCloseable(); Ex ex = Ex.of();){}");
        System.out.println( _ts);

        //tryFinally -nocatch
        _ts = _tryStmt.of("try(AutoCloseable ac = new AutoCloseable()){} finally{}");
        System.out.println( _ts);

        _ts = _tryStmt.of("try(AutoCloseable ac = new AutoCloseable()){} catch(Exception e){} finally{}");
        System.out.println( _ts);

        /**
        _ts.setTryBlock( ()->{

        });
        _ts.setFinallyBlock( ()->{

        });
         */
        //_ts.ast().setResources()
        //_ts.ast().setFinallyBlock( );

        _ts.ast().setTryBlock( Statements.blockStmt(()->{
            System.out.println(1);
        }));
    }

    public void testWithResourcesUnionTypeAndFinallyBlock(){
        _tryStmt _ts = _tryStmt.of( ()->{
            //withResources
            try( FileInputStream f = new FileInputStream("C:\\temp") ){
               if( true ){
                   throw new URISyntaxException("A", "2");
               }
            }catch(URISyntaxException | IOException fnfe){

            } catch(Exception e){

            }
            finally{
                System.out.println( "in Finally");
            }
        });
        assertTrue( _ts.hasWithResources());
        assertTrue( _ts.hasWithResources(r-> r instanceof _new) ); //has withResources that is a new
        //assertTrue( _ts.hasWithResource(r-> FileInputStream.class) ); //has withResources that is a new

        assertTrue( _ts.hasCatch() );
        assertTrue( _ts.catches(URISyntaxException.class) );
        assertTrue( _ts.catches(IOException.class) );
        assertTrue( _ts.catches(Exception.class) );
        assertTrue( _ts.hasCatch(c-> c.isParameter(p-> p.isNamed("e")))); //has a catch clause with an e parameter
        assertTrue( _ts.hasCatch(c-> c.isParameter(p-> p.isTypeRef(Exception.class)))); //has a catch clause with an Exception type parameter

        assertNotNull(_ts.getCatch(IOException.class));
        assertNotNull(_ts.getCatch(URISyntaxException.class));
        assertNotNull(_ts.getCatch(Exception.class));

        assertTrue( _ts.hasFinally());
        assertTrue( _ts.hasFinallyBody());
    }

    public void testParseExistingCode(){
        _method _m =
                _method.of(this.getClass(), "writeToFileZipFileContents");

        System.out.println( _m );

        Tree.describe(_m);
    }

    public static void writeToFileZipFileContents(String zipFileName,
                                                  String outputFileName)
            throws java.io.IOException {

        java.nio.charset.Charset charset =
                java.nio.charset.StandardCharsets.US_ASCII;
        java.nio.file.Path outputFilePath =
                java.nio.file.Paths.get(outputFileName);

        // Open zip file and create output file with
        // try-with-resources statement

        try (
                java.util.zip.ZipFile zf =
                        new java.util.zip.ZipFile(zipFileName);
                java.io.BufferedWriter writer =
                        java.nio.file.Files.newBufferedWriter(outputFilePath, charset)
        ) {
            // Enumerate each entry
            for (java.util.Enumeration entries =
                 zf.entries(); entries.hasMoreElements();) {
                // Get the entry name and write it to the output file
                String newLine = System.getProperty("line.separator");
                String zipEntryName =
                        ((java.util.zip.ZipEntry)entries.nextElement()).getName() +
                                newLine;
                writer.write(zipEntryName, 0, zipEntryName.length());
            }
        }
    }
}
