package org.jdraft.io;

import org.jdraft._codeUnits;
import org.jdraft.pattern.$parameter;

import java.nio.file.Path;
import java.nio.file.Paths;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class BatchModifySourceFiles extends TestCase {
    
    public void testMakeAllParametersFinal(){
        
        //_batch bulk = _batch.of(Paths.get("C:\\rn"), p-> !p.endsWith("TestFileIso88591.java"));

        Path pp = Paths.get(System.getProperty("java.io.tmpdir"), "temp");

        pp.toFile().mkdirs();

        //_path batch = _path.of(pp, p-> !p.endsWith("TestFileIso88591.java"));
        $parameter $anyParameter = $parameter.of();
        
        _codeUnits batch = _path.of(pp, p-> !p.endsWith("TestFileIso88591.java")).load();
        
        //for all java files 
        batch.for_code(
            _c-> {
                try{
                    //_code _c = _java.of(p);
                    $anyParameter.forEachIn(_c, param-> param.setFinal());                
                    if( Math.random() > 0.98d ){
                        //System.out.println(_c); //print a sample
                    }
                }catch(Exception e){
                    fail("exception"+e);
                    //some of them wont parse
                    //System.err.println("failed processing "+ e);
                }
        });
    }
}
