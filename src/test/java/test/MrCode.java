package test;

import org.jdraft._class;
import org.jdraft._project;
import org.jdraft.io._path;
import org.jdraft.io._io;
import org.jdraft.macro._packageName;
import org.jdraft.macro._public;
import org.jdraft.pattern.$;
import org.jdraft.pattern.$token;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MrCode {

    public static void refactorPackageReferences(_project _cc){
        $token.of("org.jdraft.text").replaceIn(_cc, "org.mrcode");

        $token.of("org.jdraft.diff").replaceIn(_cc, "org.mrcode.java.diff");
        $token.of("org.jdraft.io").replaceIn(_cc, "org.mrcode.java.io");
        $token.of("org.jdraft.macro").replaceIn(_cc, "org.mrcode.java.macro");
        $token.of("org.jdraft.pattern").replaceIn(_cc, "org.mrcode.java.pattern");
        $token.of("org.jdraft.runtime").replaceIn(_cc, "org.mrcode.java.runtime");
        $token.of("org.jdraft").replaceIn(_cc, "org.mrcode.java");
    }

    public static void refactorCommentReferences(_project _cc){
        //update javadoc references
        Map<String,String> targetToReplacement = new HashMap<>();
        targetToReplacement.put("org.jdraft.text", "org.mrcode");
        targetToReplacement.put("org.jdraft.diff", "org.mrcode.diff");
        targetToReplacement.put("org.jdraft.io", "org.mrcode.java.io");
        targetToReplacement.put("org.jdraft.macro", "org.mrcode.java.pattern");
        targetToReplacement.put("org.jdraft.pattern", "org.mrcode.java.pattern");
        targetToReplacement.put("org.jdraft.runtime", "org.mrcode.java.runtime");
        targetToReplacement.put("org.jdraft", "org.mrcode");
        targetToReplacement.put("jdraft", "mrcode-java");

        $.comment().findAndReplace(_cc, targetToReplacement);
    }

    /**
     * Instead of _jdraftException
     * we want _javaException which extends _mrCodeException
     *
     * so I need to:
     * 1) add org.mrcode._mrCodeException
     * 2) rename _jdraftException to _javaException
     * 3) change _javaException from extends RuntimeException to extends _mrCodeException
     *    a) add import in _javaException to _mrCodeException
     * 4) change all old-references from _jdraftException to _javaException
     *
     * @param _cc
     */
    public static void refactorExceptionHierarchy(_project _cc){
        //here we build the base-exception for

        /**
         *  Base Exception for mrCode (Meta Representation of Code)
         *  regardless of the target language (i.e. java, html)
         *
         *  @author Eric
         */
        @_packageName("org.mrcode")
        @_public class MrCodeException extends RuntimeException {

            private static final long serialVersionUID = 8632925250693789277L;

            public MrCodeException(String message, Throwable throwable) {
                super(message, throwable);
            }

            public MrCodeException(String message) {
                super(message);
            }

            public MrCodeException(Throwable throwable) {
                super(throwable);
            }
        }
        //adds the class to the _cache
        //1) add the _mrCodeException
        _class _mre = _class.of(MrCodeException.class);
        _cc.add( _mre );

        //2) rename _jdraftException to _javaException
        //TODO this will ALSO rename the constructors within the _class
        _class _je = _cc.get_class("_jdraftException")
            //.name( "_javaException" )
            .addExtend(_mre ) //3) extends _mrCodeException
            .addImports( _mre ); //3a) add import of _mrCodeException

        //4) change all references from _jdraftException to _javaException
        $.token("_jdraftException").replaceIn(_cc, "_javaException");
        $.token("org.mrcode.java._jdraftException").replaceIn(_cc, "org.mrcode.java._javaException");
    }

    public static void refactor_draft_to_mrjava(_project _sf){
        $.token("_draft").replaceIn(_sf,"_mrjava");
        $.comment().findAndReplace(_sf, "_draft", "_mrjava"); //update all comments
    }

    public static void main(String[] args) throws IOException {
        Path inSourcePath = Paths.get("C:\\jdraft\\project\\jdraft\\src\\main\\java");

        Path outSourcePath = Paths.get("C:\\dev\\eclipse\\workspaces\\mrcode-java\\src\\main\\java");

        //cleanup the out source path (remove source code created from previous runs)
        boolean isClean = _io.deleteDirectoryRecursive(outSourcePath);
        if(!isClean ){
            System.err.println("Unable to delete everything under "+outSourcePath);
        }
        long start = System.currentTimeMillis();
        //_sources _baseCode = _sources.of(inSourcePath);
        _project _baseCode = _path.of(inSourcePath).load();
        long parsed = System.currentTimeMillis();
        refactorPackageReferences(_baseCode);
        refactorExceptionHierarchy(_baseCode);
        refactorCommentReferences(_baseCode);
        refactor_draft_to_mrjava(_baseCode);
        List<Path> paths = _io.out(outSourcePath, _baseCode );
        long done = System.currentTimeMillis();
        System.out.println( paths );
        System.out.println( "Parsing took  "+ (parsed - start));
        System.out.println( "Refactor took "+ (done - parsed));
        System.out.println( "Total took    "+ (done - start));
    }
}
