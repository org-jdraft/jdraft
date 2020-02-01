package org.jdraft;

import junit.framework.TestCase;

public class _tryStmtTest extends TestCase {

    public void buildFromEmpty(){
        _tryStmt _ts = _tryStmt.of();
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
        assertFalse( _ts.hasTryBody() );
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

        _ts.ast().setTryBlock( Stmt.blockStmt(()->{
            System.out.println(1);
        }));
    }

    public void testParseExistingCode(){
        _method _m =
                _method.of(this.getClass(), "writeToFileZipFileContents");

        System.out.println( _m );

        _java.describe(_m);
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
