package org.jdraft;

import com.github.javaparser.ast.modules.*;
import org.jdraft.text.Text;

public interface _moduleDirective<N extends ModuleDirective, _N extends _moduleDirective> extends _java._domain {

    /**
     * Builds and returns the appropriate _moduleDirective based on the string provided
     * @param str
     * @return
     */
    public static _moduleDirective of( String...str ){
        String code = Text.combine(str);
        if( code.startsWith("exports") ){
            return _moduleExports.of(code);
        }
        if( code.startsWith("opens")){
            return _moduleOpens.of(code);
        }
        if( code.startsWith("provides")){
            return _moduleProvides.of(code);
        }
        if( code.startsWith("requires")){
            return _moduleRequires.of(code);
        }
        if( code.startsWith("uses")){
            return _moduleUses.of(code);
        }
        throw new _jdraftException("Unable to parse \""+code+" does not appear to be a module directive");
    }

    public static _moduleDirective of( ModuleDirective md ){
        if( md instanceof ModuleExportsDirective ){
            return _moduleExports.of( (ModuleExportsDirective)md);
        }
        if( md instanceof ModuleOpensDirective ){
            return _moduleOpens.of( (ModuleOpensDirective)md);
        }
        if( md instanceof ModuleProvidesDirective ){
            return _moduleProvides.of( (ModuleProvidesDirective)md);
        }
        if( md instanceof ModuleRequiresDirective ){
            return _moduleRequires.of( (ModuleRequiresDirective)md);
        }
        if( md instanceof ModuleUsesDirective ){
            return _moduleUses.of( (ModuleUsesDirective)md);
        }
        throw new _jdraftException("unable to match "+md.getClass()+" to _moduleDirective");
    }
}
