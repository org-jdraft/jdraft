package org.jdraft.refactor;

import org.jdraft.pattern.$stmt;
import org.jdraft.pattern.$stmts;
import org.jdraft.pattern.$field;
import org.jdraft._field;
import org.jdraft._type;
import org.jdraft.Ast;
import com.github.javaparser.ast.ImportDeclaration;
import org.jdraft.*;
import org.jdraft.text.Stencil;

import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * An Automated Refactoring that will replace System.out.print/ln() with log.XXX() statements
 * A Macro that can be applied to _types that will
 * 1) check if there are any System.out.println statements in the code
 *    ...IF there are
 *    A) check if there is a static Logger implementation (if not will add a Logger)
 * 2) import the classes needed for the logger
 * 3) convert all println statements to log statements
 *
 */
public class _refactorPrintToLog {
    /** find these statements to be replaced */
    static $stmt $anySystemOut = $stmt.of("System.out.println($any$);");

    /** this is the format of the (one or more) Logger Statement(s) used in place of the System out */
    String loggerStatementsFormat;

    /** find the appropriate Logger field (if the TYPE has one) */
    Predicate<_field> preDefinedLoggerMatcher;

    /** IF... I have to create an ad hoc logger field it will be this */
    $field adHocLogger;

    /** IF ... i have to create an ad hoc logger, these are the imports I need*/
    List<ImportDeclaration> adHocLoggerImports;

    public _refactorPrintToLog(Predicate<_field> preDefinedLoggerMatcher,
                               ImportDeclaration[] adHocLoggerImports,
                               $field adHocLogger,
                               String loggerStatementsFormat){
        
        this( preDefinedLoggerMatcher,
                Arrays.stream( adHocLoggerImports).collect(Collectors.toList()),
                adHocLogger,
                loggerStatementsFormat);
    }

    public _refactorPrintToLog(Predicate<_field> preDefinedLoggerMatcher,
                               List<ImportDeclaration> adHocLoggerImports,
                               $field adHocLogger,
                               String loggerStatementsFormat){

        Stencil st = Stencil.of(loggerStatementsFormat);
        List<String> vars = st.$list();
        if( !vars.contains("name")){
            throw new _jdraftException("MISSING $var: \"name\" in : "+ loggerStatementsFormat +System.lineSeparator() +
                    "... used for representing the field NAME of the Logger");
        }
        if( !vars.contains("any")){
            throw new _jdraftException("MISSING $var: \"any\" in : "+ loggerStatementsFormat +System.lineSeparator() +
                    "... used for representing the content to be logged");
        }
        if( !adHocLogger.$list().contains("className")){
            throw new _jdraftException("MISSING $var: \"className\" in : "+ adHocLogger +System.lineSeparator() +
                    "... used for generating the logger for the class");
        }
        this.preDefinedLoggerMatcher = preDefinedLoggerMatcher;
        this.adHocLogger = adHocLogger;
        this.adHocLoggerImports = adHocLoggerImports;
        this.loggerStatementsFormat = loggerStatementsFormat;
    }

    public void expand(_type _t) {
        if( $anySystemOut.listIn(_t).size() > 0 ) {
            _field _f = _t.firstField(preDefinedLoggerMatcher);
            if( _f == null ){ /* we didnt find a matching logger, create & add a new one*/
                adHocLoggerImports.forEach(i -> _t.addImports(i) ); /* add all Logger imports */
                _f = adHocLogger.draft("className", _t.getFullName()); /* create a clone/copy for this _field */
                _t.addField( _f ); /* add logger field to the TYPE */
            }
            /** add the actual log statement */
            $anySystemOut.replaceIn(_t, $stmts.of( loggerStatementsFormat )
                    .$hardcode("name", _f.getName() ) );
        }
    }
    
    /** (HERE IS HOW WE CREATE A SIMPLE IMPLEMENTATION)
     * A specific implementation that will replace System.out.printlns 
     * with java.util.Logger Log.fine()...statements of the macro
     */
    public static _refactorPrintToLog JavaLoggerFine = new _refactorPrintToLog(
            (_field f)->f.isStatic() && f.isType(Logger.class),
            new ImportDeclaration[] { Ast.importDeclaration( Logger.class ),Ast.importDeclaration( Level.class )},
            $field.of("public static final Logger LOG = Logger.getLogger($className$.class.getCanonicalName());"),
        "if($name$.isLoggable(Level.FINER)){ $name$.fine($any$ + \"\"); }" );
}