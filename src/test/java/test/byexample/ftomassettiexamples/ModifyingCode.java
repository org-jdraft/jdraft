package test.byexample.ftomassettiexamples;

import org.jdraft._class;

import java.nio.file.Paths;

//https://github.com/ftomassetti/analyze-java-code-examples/blob/master/src/main/java/me/tomassetti/examples/ModifyingCode.java
public class ModifyingCode {

    public static void main(String[] args){
        //if the file is local, just read a .java file from sources
        _class _c = _class.of(
                Paths.get( "C:\\jdraft\\project\\jdraft\\src\\test\\java\\org\\jdraft\\text\\ClasstoStringTranslateTest.java")
                //Paths.get("C:\\jdraft\\project\\jdraft\\src\\main\\java\\name\\fraser\\neil\\plaintext\\diff-match-path.java")
        );

        _c.setName("RenamedClass");

        System.out.println("Renamed class"+ _c );

        _c.addMethod( new Object(){
            boolean aField;
            public void setAField(boolean aField){
                this.aField = aField;
            }
        });
        System.out.println("Added field class"+ _c );
    }
}
