package org.jdraft.pattern;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jdraft.*;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

/**
 *
 */
public class $type implements $pattern<_type, $type>, $declared<_type, $type> {

    public interface $part {
    }

    public Predicate<_type> constraint = (_t) -> true;

    public List<$typeRef> extend = new ArrayList<>();
    public List<$typeRef> implement = new ArrayList<>();
    public List<$import> imports = new ArrayList<>();
    public List<$member> members = new ArrayList<>();

    public $typeParameters typeParameters = $typeParameters.of();
    public $package packageDecl = $package.of();
    public $comment<JavadocComment> javadoc = $comment.javadocComment();
    public $annoRefs annos = $annoRefs.of();
    public $modifiers modifiers = $modifiers.of();
    public $name name = $name.of("$typeName$"); //name required

    public static $type of( Predicate<_type> matchFn){
        $type $t = of().$and(matchFn);
        return $t;
    }

    public static $type of( String name, $type.$part...parts ){
        $type $t = of( parts );
        if( name.contains("." ) ){
            //(the package is the first part)
            $package $p = $package.of( name.substring(0, name.lastIndexOf(".")) );
            $name $n = $name.of(name.substring(name.lastIndexOf(".")+1));
            $t.$name($n);
            $t.packageDecl = $p;
        } else{
            $name $n = $name.of(name);
            $t.$name($n);
        }
        return $t;
    }

    public static $type of($type.$part... parts) {
        return new $type(parts);
    }

    public static $type not( $type.$part...parts){
        return new $type().$not(parts);
    }

    public $type($type.$part... parts) {
        for (int i = 0; i < parts.length; i++) {
            if (parts[i] instanceof $member) {
                members.add(($member) parts[i]);
            } else if (parts[i] instanceof $name) {
                this.name = ($name) parts[i];
            } else if (parts[i] instanceof $package) {
                this.name = ($name) parts[i];
            } else if (parts[i] instanceof $typeParameters) {
                this.name = ($name) parts[i];
            } else if (parts[i] instanceof $typeParameter) {
                this.typeParameters.$add(($typeParameter) parts[i]);
            } else if (parts[i] instanceof $modifiers) {
                this.modifiers = $modifiers.of(this.modifiers, ($modifiers) parts[i]);
            } else if (parts[i] instanceof $import) {
                this.imports.add(($import) parts[i]);
            } else if (parts[i] instanceof $annoRefs) {
                this.annos.add(($annoRefs) parts[i]);
            } else if (parts[i] instanceof $annoRef) {
                this.annos.add(($annoRef) parts[i]);
            } else if (parts[i] instanceof $comment) {
                this.javadoc = ($comment<JavadocComment>) parts[i];
            }
        }
    }

    @Override
    public boolean isMatchAny() {
        try {
            return this.constraint.test(null) && this.annos.isMatchAny() && this.extend.isEmpty()
                    && this.implement.isEmpty() && this.imports.isEmpty() && this.javadoc.isMatchAny()
                    && this.members.isEmpty() && this.modifiers.isMatchAny() && this.name.isMatchAny()
                    && this.packageDecl.isMatchAny() && this.typeParameters.isMatchAny();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public $type $(String target, String $paramName) {
        this.members.forEach(p -> (($pattern) p).$(target, $paramName));
        this.annos.$(target, $paramName);
        this.extend.forEach(p -> (($pattern) p).$(target, $paramName));
        this.implement.forEach(p -> (($pattern) p).$(target, $paramName));
        this.imports.forEach(p -> (($pattern) p).$(target, $paramName));
        this.modifiers.$(target, $paramName);
        this.name = this.name.$(target, $paramName);
        this.packageDecl.$(target, $paramName);
        this.typeParameters.$(target, $paramName);
        return this;
    }

    public $type $and($type.$part...parts){
        for (int i = 0; i < parts.length; i++) {
            if( parts[i] instanceof $annoRef){
                final $annoRef $fa = (($annoRef)parts[i]);
                Predicate<_type> pf = an-> an.getAnnoRef(a -> $fa.match( (_annoExpr)a) ) != null;
                $and( pf  );
            }
            else if( parts[i] instanceof $annoRefs){
                final $annoRefs $fa = (($annoRefs)parts[i]);
                Predicate<_type> pf = an-> $fa.matches(an.getAnnoRefs());
                $and( pf );
            }
            else if( parts[i] instanceof $modifiers ) {
                final $modifiers $fa = (($modifiers) parts[i]);
                Predicate<_type> pf = f -> $fa.matches(f.getModifiers());
                $and(pf );
            }
            else if(parts[i] instanceof $field ){
                final $field $fj = (($field)parts[i]);
                Predicate<_type> aFn = a-> a.getField(e->$fj.match( (_field)e)) != null; //found one
                $and( aFn  );
            }
            else if( parts[i] instanceof $import) {
                final $import $fj = (($import)parts[i]);
                Predicate<_type> aFn = a-> a.getImport(im->$fj.match((_import)im)) != null; //found one
                $and( aFn.negate() );
            }
            else if( parts[i] instanceof $package ) {
                final $package $fa = (($package) parts[i]);
                Predicate<_type> pf = f -> $fa.matches(f.getPackageName());
                $and(pf );
            }
            else if( parts[i] instanceof $name){
                final $name $fn = (($name)parts[i]);
                Predicate<_type> pf = f-> $fn.matches(f.getName());
                $and( pf  );
            }
            else if(parts[i] instanceof $comment ){
                final $comment $fj = (($comment)parts[i]);
                Predicate<_type> pf = f-> $fj.matches(f.getJavadoc());
                $and( pf  );
            }
            else if(parts[i] instanceof $method ){
                final $method $fj = (($method)parts[i]);
                Predicate<_type> aFn = a-> {
                    if( a instanceof _method._withMethods){
                        return ((_method._withMethods)a).getMethod(e->$fj.match((_method)e)) != null; //found one
                    }
                    return false;
                };
                $and( aFn  );
            }
            else if(parts[i] instanceof $constructor ){
                final $constructor $fj = (($constructor)parts[i]);
                Predicate<_type> aFn = a->{
                    if( a instanceof _constructor._withConstructors){
                        return ((_constructor._withConstructors)a).getConstructor(e->$fj.match((_constructor)e)) != null; //found one
                    }
                    return false;
                };
                $and( aFn );
            }
            else if(parts[i] instanceof $initBlock){
                final $initBlock $fj = (($initBlock)parts[i]);
                Predicate<_type> aFn = a-> {
                    if( a instanceof _initBlock._withInitBlocks) {
                        return ((_initBlock._withInitBlocks)a).getInitBlock(e -> $fj.match((_initBlock)e)) != null; //found one
                    }
                    return false;
                };
                $and( aFn );
            }
            else if(parts[i] instanceof $annotationEntry){
                final $annotationEntry $fj = (($annotationEntry)parts[i]);
                Predicate<_type> aFn = a-> {
                    if( a instanceof _annotation ) {
                        return ((_annotation)a).getEntry(e -> $fj.match(e)) != null;
                    }
                    return false;
                };
                $and( aFn  );
            }
            else if(parts[i] instanceof $enumConstant ){
                final $enumConstant $fj = (($enumConstant)parts[i]);
                Predicate<_type> aFn = a-> {
                    if( a instanceof _enum ) {
                        return ((_enum)a).getConstant(e -> $fj.match(e)) != null;
                    }
                    return false;
                };
                $and( aFn  );
            }
        }
        return this;
    }

    public $type $not($type.$part...parts){
        for (int i = 0; i < parts.length; i++) {
            if( parts[i] instanceof $annoRef){
                final $annoRef $fa = (($annoRef)parts[i]);
                Predicate<_type> pf = an-> an.getAnnoRef(a -> $fa.match( (_annoExpr)a) ) != null;
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $annoRefs){
                final $annoRefs $fa = (($annoRefs)parts[i]);
                Predicate<_type> pf = an-> $fa.matches(an.getAnnoRefs());
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $modifiers ) {
                final $modifiers $fa = (($modifiers) parts[i]);
                Predicate<_type> pf = f -> $fa.matches(f.getModifiers());
                $and(pf.negate());
            }
            else if(parts[i] instanceof $field ){
                final $field $fj = (($field)parts[i]);
                Predicate<_type> aFn = a-> a.getField(e->$fj.match( (_field)e)) != null; //found one
                $and( aFn.negate() );
            }
            else if( parts[i] instanceof $import) {
                final $import $fj = (($import)parts[i]);
                Predicate<_type> aFn = a-> a.getImport(im->$fj.match((_import)im)) != null; //found one
                $and( aFn.negate() );
            }
            else if( parts[i] instanceof $package ) {
                final $package $fa = (($package) parts[i]);
                Predicate<_type> pf = f -> $fa.matches(f.getPackageName());
                $and(pf.negate());
            }
            else if( parts[i] instanceof $name){
                final $name $fn = (($name)parts[i]);
                Predicate<_type> pf = f-> $fn.matches(f.getName());
                $and( pf.negate() );
            }
            else if(parts[i] instanceof $comment ){
                final $comment $fj = (($comment)parts[i]);
                Predicate<_type> pf = f-> $fj.matches(f.getJavadoc());
                $and( pf.negate() );
            }
            else if(parts[i] instanceof $method ){
                final $method $fj = (($method)parts[i]);
                Predicate<_type> aFn = a-> {
                    if( a instanceof _method._withMethods){
                        return ((_method._withMethods)a).getMethod(e->$fj.match((_method)e)) != null; //found one
                    }
                    return false;
                };
                $and( aFn.negate() );
            }
            else if(parts[i] instanceof $constructor ){
                final $constructor $fj = (($constructor)parts[i]);
                Predicate<_type> aFn = a->{
                    if( a instanceof _constructor._withConstructors){
                        return ((_constructor._withConstructors)a).getConstructor(e->$fj.match((_constructor)e)) != null; //found one
                    }
                    return false;
                };
                $and( aFn.negate() );
            }
            else if(parts[i] instanceof $initBlock){
                final $initBlock $fj = (($initBlock)parts[i]);
                Predicate<_type> aFn = a-> {
                    if( a instanceof _initBlock._withInitBlocks) {
                        return ((_initBlock._withInitBlocks)a).getInitBlock(e -> $fj.match((_initBlock)e)) != null; //found one
                    }
                    return false;
                };
                $and( aFn.negate() );
            }
            else if(parts[i] instanceof $annotationEntry){
                final $annotationEntry $fj = (($annotationEntry)parts[i]);
                Predicate<_type> aFn = a-> {
                    if( a instanceof _annotation ) {
                        return ((_annotation)a).getEntry(e -> $fj.match(e)) != null;
                    }
                    return false;
                };
                $and( aFn.negate() );
            }
            else if(parts[i] instanceof $enumConstant ){
                final $enumConstant $fj = (($enumConstant)parts[i]);
                Predicate<_type> aFn = a-> {
                    if( a instanceof _enum ) {
                        return ((_enum)a).getConstant(e -> $fj.match(e)) != null;
                    }
                    return false;
                };
                $and( aFn.negate() );
            }
        }
        return this;
    }

    /**
     * Add a member ($method, $field, $constructor, $initBlock, $enumConstant, $annotationElement)
     * @param $ms the member patterns to be added
     * @return the type
     */
    public $type $member($member...$ms){
        Arrays.stream($ms).forEach($m -> this.members.add($m));
        return this;
    }

    public $type $modifiers( $modifiers $mods ){
        this.modifiers = $mods;
        return this;
    }

    public $type $imports( Class...imports){
        Arrays.stream(imports).forEach( t-> this.imports.add($import.of(t)) );
        return this;
    }

    public $type $imports( _import...imports){
        Arrays.stream(imports).forEach( t-> this.imports.add($import.of(t)) );
        return this;
    }

    public $type $imports( $import...imports){
        Arrays.stream(imports).forEach( t-> this.imports.add(t) );
        return this;
    }

    public $type $imports( List<_import> _is){
        _is.forEach( i -> this.imports.add($import.of(i)));
        return this;
    }

    public $type $implement( $typeRef... types ){
        Arrays.stream(types).forEach( t-> this.implement.add(t) );
        return this;
    }

    public $type $implement( Class... interfaces ){
        Arrays.stream(interfaces).forEach( i-> this.implement.add($typeRef.of(i)) );
        return this;
    }

    public $type $implement( String... interfaces ){
        Arrays.stream(interfaces).forEach( i-> this.implement.add($typeRef.of(i)) );
        return this;
    }

    public $type $extend( $typeRef... types ){
        Arrays.stream(types).forEach( t-> this.extend.add(t) );
        return this;
    }

    public $type $extend( Class... baseClass ){
        Arrays.stream(baseClass).forEach( c-> this.extend.add($typeRef.of(c)) );
        return this;
    }

    public $type $extend( String... baseClass ){
        Arrays.stream(baseClass).forEach( c-> this.extend.add($typeRef.of(c)) );
        return this;
    }

    public $annoRefs get$Annos(){
        return this.annos;
    }

    public $type $annos(Predicate<_annoExprs> annosMatchFn){
        this.annos.$and(annosMatchFn);
        return this;
    }

    public $type $annos( $annoRefs $as ){
        this.annos = $as;
        return this;
    }

    public $type $annos( $annoRef... $a){
        this.annos.add($a);
        return this;
    }

    public $type $typeParameters( $typeParameters $tps ){
        this.typeParameters = $tps;
        return this;
    }

    public $type $typeParameters( $typeParameter... $tps ){
        Arrays.stream($tps).forEach(tp-> this.typeParameters.$add(tp));
        return this;
    }

    @Override
    public $type $and(Predicate<_type> constraint) {
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    @Override
    public $name get$Name() {
        return name;
    }

    @Override
    public $type $name(String name) {
        this.name = $name.of(name);
        return this;
    }

    @Override
    public $type $name($name name) {
        this.name = name;
        return this;
    }

    @Override
    public $type $name(Predicate<String> nameMatchFn) {
        this.name.and(nameMatchFn);
        return this;
    }

    @Override
    public $comment<JavadocComment> get$javadoc() {
        return this.javadoc;
    }

    @Override
    public $type $javadoc($comment<JavadocComment> javadocComment) {
        this.javadoc = javadocComment;
        return this;
    }

    @Override
    public $type $javadoc(_javadocComment _jd) {
        this.javadoc = $comment.javadocComment(_jd);
        return this;
    }

    @Override
    public $type $javadoc(Predicate<JavadocComment> javadocMatchFn) {
        this.javadoc.$and(javadocMatchFn);
        return this;
    }

    @Override
    public $type $hardcode(Translator translator, Tokens kvs) {
        this.members.forEach(p -> (($pattern) p).$hardcode(translator, kvs));
        this.annos.$hardcode(translator, kvs);
        this.extend.forEach(p -> (($pattern) p).$hardcode(translator, kvs));
        this.implement.forEach(p -> (($pattern) p).$hardcode(translator, kvs));
        this.imports.forEach(p -> (($pattern) p).$hardcode(translator, kvs));
        this.modifiers.$hardcode(translator, kvs);
        this.name = this.name.hardcode$(translator, kvs);
        this.packageDecl.$hardcode(translator, kvs);
        this.typeParameters.$hardcode(translator, kvs);
        return this;
    }

    @Override
    public boolean match(Node candidate) {
        if (candidate instanceof TypeDeclaration) {
            return select((TypeDeclaration) candidate) != null;
        }
        return false;
    }

    public boolean matches(String... code) {
        try {
            return matches(Ast.of(code));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean matches(Class clazz) {
        try {
            return matches(Ast.typeDecl(clazz));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean matches(TypeDeclaration ast) {
        return select(ast) != null;
    }

    public boolean matches(_type _t) {
        return select(_t) != null;
    }

    public boolean matches(CompilationUnit cu) {
        if (cu != null) {
            if (cu.getTypes().size() == 1) {
                return matches(cu.getType(0));
            }
            if (cu.getPrimaryType().isPresent()) {
                return matches(cu.getPrimaryType().get());
            }
        }
        return false;
    }

    @Override
    public _type firstIn(Node astStartNode, Predicate<_type> nodeMatchFn) {
        Optional<Node> ot = astStartNode.stream().filter(n -> match(n)).findFirst();
        if (ot.isPresent()) {
            return _type.of((TypeDeclaration) ot.get());
        }
        return null;
    }

    public Select select(TypeDeclaration astType) {
        return select((_type) _type.of(astType));
    }

    public Select select( CompilationUnit cu ){
        return select( (_type) _type.of(cu));
    }

    @Override
    public Select select(_type instance) {
        if (!this.constraint.test(instance)) {
            return null;
        }
        $tokens tokens = $type.selectImports(this.imports, instance);
        //$tokens tokens = this.packageDecl.parse(instance.astCompilationUnit() );
        tokens = $tokens.to(tokens, () -> this.packageDecl.parse(instance.astCompilationUnit()));

        tokens = $tokens.to(tokens, () -> this.annos.parse(instance));
        tokens = $tokens.to(tokens, () -> this.javadoc.parse(instance));
        tokens = $tokens.to(tokens, () -> this.modifiers.parse(instance));
        tokens = $tokens.to(tokens, () -> this.name.parse(instance.getName()));

        if (instance instanceof _typeParameters._withTypeParameters) {
            tokens = $tokens.to(tokens, () -> this.typeParameters.parse(((_typeParameters._withTypeParameters) instance).getTypeParameters()));
        } else {
            if (!this.typeParameters.isMatchAny()) {
                return null;
            }
        }
        if (instance instanceof _type._withExtends) {
            tokens = $tokens.to(tokens, () -> $type.selectExtends(this.extend, (_type._withExtends) instance));
        } else {
            if (!this.extend.isEmpty()) {
                return null;
            }
        }
        if (instance instanceof _type._withImplements) {
            tokens = $tokens.to(tokens, () -> $type.selectImplements(this.implement, (_type._withImplements) instance));
        } else {
            if (!this.implement.isEmpty()) {
                return null;
            }
        }
        List<$field> $fs = new ArrayList<>();
        this.members.stream().filter(f -> f instanceof $field).forEach(f -> $fs.add(($field) f));
        tokens = $tokens.to(tokens, () -> $type.selectFields($fs, (_field._withFields) instance));

        List<$initBlock> $ibs = new ArrayList<>();
        this.members.stream().filter(f -> f instanceof $initBlock).forEach(f -> $ibs.add(($initBlock) f));
        if (instance instanceof _initBlock._withInitBlocks) {
            tokens = $tokens.to(tokens, () -> $type.selectInitBlocks($ibs, (_initBlock._withInitBlocks) instance));
        } else {
            if (!$ibs.isEmpty()) {
                return null;
            }
        }

        List<$constructor> $cts = new ArrayList<>();
        this.members.stream().filter(f -> f instanceof $constructor).forEach(f -> $cts.add(($constructor) f));
        if (instance instanceof _constructor._withConstructors) {
            tokens = $tokens.to(tokens, () -> $type.selectConstructors($cts, (_constructor._withConstructors) instance));
        } else {
            if (!$cts.isEmpty()) {
                return null;
            }
        }

        List<$method> $ms = new ArrayList<>();
        this.members.stream().filter(f -> f instanceof $method).forEach(f -> $ms.add(($method) f));
        if (instance instanceof _method._withMethods) {
            tokens = $tokens.to(tokens, () -> $type.selectMethods($ms, (_method._withMethods) instance));
        } else {
            if (!$ms.isEmpty()) {
                return null;
            }
        }

        List<$enumConstant> $ecs = new ArrayList<>();
        this.members.stream().filter(f -> f instanceof $enumConstant).forEach(f -> $ecs.add(($enumConstant) f));
        if (instance instanceof _enum) {
            tokens = $tokens.to(tokens, () -> $enum.selectEnumConstants($ecs, (_enum) instance));
        } else {
            if (!$ecs.isEmpty()) {
                return null;
            }
        }

        List<$annotationEntry> $aes = new ArrayList<>();
        this.members.stream().filter(f -> f instanceof $annotationEntry).forEach(f -> $aes.add(($annotationEntry) f));
        if (instance instanceof _annotation) {
            tokens = $tokens.to(tokens, () -> $annotation.selectAnnotationElements($aes, (_annotation) instance));
        } else {
            if (!$ecs.isEmpty()) {
                return null;
            }
        }

        //TODO nests
        //tokens = $tokens.to( tokens, ()-> $type.selectConstructors(this.ctors, instance ) );
        //tokens = $tokens.to( tokens, ()-> $type.selectMethods(this.methods, instance ) );

        //nests
        if (tokens != null) {
            return new Select(instance, tokens);
        }
        return null;
    }

    @Override
    public Select selectFirstIn(Node astNode) {
        Optional<Node> on = astNode.stream().filter(n -> match(n)).findFirst();
        if (on.isPresent()) {
            return select((TypeDeclaration) on.get());
        }
        return null;
    }

    @Override
    public List<Select> listSelectedIn(Node astNode) {
        List<Select> selected = new ArrayList<>();
        astNode.walk(TypeDeclaration.class, td -> {
            Select s = select(td);
            if (s != null) {
                selected.add(s);
            }
        });
        return selected;
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_type> nodeMatchFn, Consumer<_type> nodeActionFn) {
        astNode.walk(TypeDeclaration.class, td -> {
            Select s = select(td);
            if (s != null && nodeMatchFn.test(s.selected)) {
                nodeActionFn.accept(s.selected);
            }
        });
        return astNode;
    }

    public <N extends Node> N forSelectedIn(N astNode, Consumer<Select> selectActionFn) {
        return forSelectedIn(astNode, t -> true, selectActionFn);
    }

    public <N extends Node> N forSelectedIn(N astNode, Predicate<Select> selectMatchFn, Consumer<Select> selectActionFn) {
        astNode.walk(TypeDeclaration.class, td -> {
            Select s = select(td);
            if (s != null && selectMatchFn.test(s)) {
                selectActionFn.accept(s);
            }
        });
        return astNode;
    }

    /**
     * The selected Class
     */
    public static class Select implements select_java<_type> {
        public _type selected;
        public $tokens tokens;

        public Select(_type _t, $tokens tokens) {
            this.selected = _t;
            this.tokens = tokens;
        }

        @Override
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public _type _node() {
            return selected;
        }
    }

    /* These are methods shared/used by all $type implementations */
    public static $pattern.$tokens selectImplements(List<$typeRef> $protoTypes, _type._withImplements _hi) {
        Map<$typeRef, List<$typeRef.Select>> selectMap = new HashMap<>();

        for (int i = 0; i < $protoTypes.size(); i++) {
            final $typeRef t = $protoTypes.get(i);
            List<$typeRef.Select> matches = new ArrayList<>();
            _hi.listImplements().forEach(c -> {
                $typeRef.Select sel = t.select(_typeRef.of((ClassOrInterfaceType) c));
                if (sel != null) {
                    matches.add(sel);
                }
            });
            if (matches.isEmpty()) {
                return null; //couldnt match a $constructor to ANY constructors
            } else {
                selectMap.put(t, matches); //associated the matches with
            }
        }
        //Now create a map with ALL tokens
        $pattern.$tokens all = $pattern.$tokens.of();
        selectMap.values().forEach(ls -> ls.forEach(s -> all.putAll(s.tokens())));
        all.remove("type");
        all.remove("name");
        return all;
    }

    public static $pattern.$tokens selectExtends($typeRef $protoType, _type._withExtends _he) {
        if (!_he.hasExtends() && $protoType.isMatchAny()) {
            return $pattern.$tokens.of();
        }
        List<$typeRef> lt = new ArrayList<>();
        lt.add($protoType);
        return selectExtends(lt, _he);
    }

    public static $pattern.$tokens selectExtends(List<$typeRef> $protoTypes, _type._withExtends _he) {

        Map<$typeRef, List<$typeRef.Select>> selectMap = new HashMap<>();

        for (int i = 0; i < $protoTypes.size(); i++) {
            final $typeRef t = $protoTypes.get(i);
            List<$typeRef.Select> matches = new ArrayList<>();
            _he.listExtends().forEach(c -> {
                $typeRef.Select sel = t.select(_typeRef.of((ClassOrInterfaceType) c));
                if (sel != null) {
                    matches.add(sel);
                }
            });
            if (matches.isEmpty()) {
                return null; //couldnt match a $constructor to ANY constructors
            } else {
                selectMap.put(t, matches); //associated the matches with
            }
        }
        //Now create a map with ALL tokens
        $pattern.$tokens all = $pattern.$tokens.of();
        selectMap.values().forEach(ls -> ls.forEach(s -> all.putAll(s.tokens())));

        all.remove("type");
        all.remove("name");
        return all;
    }

    public static $pattern.$tokens selectConstructors(List<$constructor> $protoCtors, _constructor._withConstructors _hcs) {
        Map<$constructor, List<$constructor.Select>> selectMap = new HashMap<>();

        for (int i = 0; i < $protoCtors.size(); i++) {
            final $constructor t = $protoCtors.get(i);
            List<$constructor.Select> matches = new ArrayList<>();
            _hcs.listConstructors().forEach(c -> {
                $constructor.Select sel = t.select((_constructor) c);
                if (sel != null) {
                    matches.add(sel);
                }
            });
            if (matches.isEmpty()) {
                return null; //couldnt match a $constructor to ANY constructors
            } else {
                selectMap.put(t, matches); //associated the matches with
            }
        }
        //Now create a map with ALL tokens
        $pattern.$tokens all = $pattern.$tokens.of();
        selectMap.values().forEach(ls -> ls.forEach(s -> all.putAll(s.tokens())));

        all.remove("type");
        all.remove("name");
        return all;
    }


    public static $pattern.$tokens selectNests(List<$type> $protoTypes, _type _t){
        Map<$type, List<$type.Select>> selectMap = new HashMap<>();

        for (int i = 0; i < $protoTypes.size(); i++) {
            final $type t = $protoTypes.get(i);
            List<$type.Select> matches = new ArrayList<>();
            _t.listInnerTypes().forEach(m -> {
                $type.Select sel = t.select((_type) m);
                if (sel != null) {
                    matches.add(sel);
                    //System.out.println( "FOUND "+ sel+" "+ matches);
                }
            });
            if (matches.isEmpty()) {
                return null; //couldnt match a $method to ANY constructors
            } else {
                selectMap.put(t, matches); //associated the matches with
            }
        }
        //Now create a map with ALL tokens
        $pattern.$tokens all = $pattern.$tokens.of();
        selectMap.values().forEach(ls -> ls.forEach(s -> all.putAll(s.tokens())));
        return all;
    }

    public static $pattern.$tokens selectMethods(List<$method> $protoMethods, _method._withMethods _hcs) {
        Map<$method, List<$method.Select>> selectMap = new HashMap<>();

        for (int i = 0; i < $protoMethods.size(); i++) {
            final $method t = $protoMethods.get(i);
            List<$method.Select> matches = new ArrayList<>();
            _hcs.listMethods().forEach(m -> {
                $method.Select sel = t.select((_method) m);
                if (sel != null) {
                    matches.add(sel);
                    //System.out.println( "FOUND "+ sel+" "+ matches);
                }
            });
            if (matches.isEmpty()) {
                return null; //couldnt match a $method to ANY constructors
            } else {
                selectMap.put(t, matches); //associated the matches with
            }
        }
        //Now create a map with ALL tokens
        $pattern.$tokens all = $pattern.$tokens.of();
        selectMap.values().forEach(ls -> ls.forEach(s -> all.putAll(s.tokens())));
        //we have to remove these from method so they dont "trickle up"
        all.remove("type");
        all.remove("name");
        return all;
    }

    public static $pattern.$tokens selectFields(List<$field> $protoFields, _field._withFields _hcs) {
        Map<$field, List<$field.Select>> selectMap = new HashMap<>();

        for (int i = 0; i < $protoFields.size(); i++) {
            final $field t = $protoFields.get(i);
            List<$field.Select> matches = new ArrayList<>();
            _hcs.listFields().forEach(c -> {
                $field.Select sel = t.select((_field) c);
                if (sel != null) {
                    matches.add(sel);
                }
            });
            if (matches.isEmpty()) {
                return null; //couldnt match a $constructor to ANY constructors
            } else {
                selectMap.put(t, matches); //associated the matches with
            }
        }
        //Now create a map with ALL tokens
        $pattern.$tokens all = $pattern.$tokens.of();
        selectMap.values().forEach(ls -> ls.forEach(s -> all.putAll(s.tokens())));
        all.remove("type");
        all.remove("name");
        return all;
    }


    public static $pattern.$tokens selectInitBlocks(List<$initBlock> $protoInitBlocks, _initBlock._withInitBlocks _hcs) {
        Map<$initBlock, List<$initBlock.Select>> selectMap = new HashMap<>();

        for (int i = 0; i < $protoInitBlocks.size(); i++) {
            final $initBlock t = $protoInitBlocks.get(i);
            List<$initBlock.Select> matches = new ArrayList<>();
            _hcs.listInitBlocks().forEach(c -> {
                $initBlock.Select sel = t.select((_initBlock) c);
                if (sel != null) {
                    matches.add(sel);
                }
            });
            if (matches.isEmpty()) {
                return null; //couldnt match a $constructor to ANY constructors
            } else {
                selectMap.put(t, matches); //associated the matches with
            }
        }
        //Now create a map with ALL tokens
        $pattern.$tokens all = $pattern.$tokens.of();
        selectMap.values().forEach(ls -> ls.forEach(s -> all.putAll(s.tokens())));
        all.remove("type");
        all.remove("name");
        return all;
    }

    public static $pattern.$tokens selectImports(List<$import> $protoImports, _codeUnit _hcs) {
        Map<$import, List<$import.Select>> selectMap = new HashMap<>();

        for (int i = 0; i < $protoImports.size(); i++) {
            final $import t = $protoImports.get(i);
            List<$import.Select> matches = new ArrayList<>();
            _hcs.listImports().forEach(c -> {
                $import.Select sel = t.select((_import) c);
                if (sel != null) {
                    matches.add(sel);
                }
            });
            if (matches.isEmpty()) {
                return null; //couldnt match a $constructor to ANY constructors
            } else {
                selectMap.put(t, matches); //associated the matches with
            }
        }
        //Now create a map with ALL tokens
        $pattern.$tokens all = $pattern.$tokens.of();
        selectMap.values().forEach(ls -> ls.forEach(s -> all.putAll(s.tokens())));
        all.remove("type");
        all.remove("name");
        return all;
    }
}
