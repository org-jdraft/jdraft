package org.jdraft;

import com.github.javaparser.ast.PackageDeclaration;
import org.jdraft.text.Text;

import java.util.Objects;

public class _package implements _java._node<PackageDeclaration, _package> {

    public static _package of(){
        return of( new PackageDeclaration());
    }

    public static _package of( String packageName ){
        return of( Ast.packageDeclaration(packageName));
    }

    public static _package of( PackageDeclaration astPackage){
        return new _package(astPackage);
    }

    public PackageDeclaration astPackage;

    public _package( PackageDeclaration astPd){
        this.astPackage = astPd;
    }

    @Override
    public _package copy() {
        return of( astPackage.clone() );
    }

    @Override
    public PackageDeclaration ast() {
        return this.astPackage;
    }

    @Override
    public boolean is(String... stringRep) {
        return Objects.equals( this.astPackage, Ast.packageDeclaration(Text.combine(stringRep)));
    }

    public int hashCode(){
        return 31 * this.astPackage.hashCode();
    }

    public boolean equals(Object o){
        if( o instanceof _package ){
            _package _p = (_package)o;
            return Objects.equals( this.astPackage, _p.astPackage);
        }
        return false;
    }
}
