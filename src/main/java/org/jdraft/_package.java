package org.jdraft;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.expr.Name;
import org.jdraft.text.Text;

import java.util.Objects;

public final class _package implements _java._node<PackageDeclaration, _package> {

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

    public static _feature._one<_package, Name> NAME = new _feature._one<>(_package.class, Name.class,
            _feature._id.NAME,
            a -> a.getNameNode(),
            (_package p, Name s) -> p.setName(s));

    public static _feature._meta<_package> META = _feature._meta.of(_package.class, NAME);

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

    public Name getNameNode(){
        return this.astPackage.getName();
    }

    public _package setName(Name n){
        this.astPackage.setName(n);
        return this;
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

    public String toString(){
        return this.astPackage.toString();
    }
}
