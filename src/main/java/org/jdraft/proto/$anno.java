package org.jdraft.proto;

import org.jdraft._code;
import org.jdraft._java;
import org.jdraft._anno;
import org.jdraft._type;
import org.jdraft.Expr;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.expr.*;
import org.jdraft.*;
import org.jdraft._node;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.jdraft.Template;

/**
 * prototype for an annotation
 *
 * @author Eric
 */
public class $anno
    implements Template<_anno>, $proto<_anno>, $constructor.$part, $method.$part, 
        $field.$part, $parameter.$part {
    
    public static $anno any(){
        return of();
    }
    
    public static $anno of(){
        return new $anno( $id.of() );
    }
    
    public static $anno of( $id name, $memberValue...memberValues ){
        return new $anno(name, memberValues);
    }
    
    public static $anno of(String pattern) {
        return new $anno(_anno.of(pattern));
    }
    
    public static $anno of(String... pattern) {
        return new $anno(_anno.of(pattern));
    }
    
    public static $anno of( Predicate<_anno> constraint ){
        return of().and(constraint);
    }
    
    public static $anno of(String pattern, Predicate<_anno>constraint) {
        return new $anno(_anno.of(pattern)).and(constraint);
    }
    
    public static $anno of(_anno _an) {
        return new $anno(_an);
    }

    public static $anno of(_anno _an, Predicate<_anno>constraint) {
        return new $anno(_an).and(constraint);
    }
        
    public static $anno of(Class<? extends Annotation>sourceAnnoClass) {
        return new $anno(_anno.of(sourceAnnoClass));
    }
    
    public static $anno of (Class<? extends Annotation>sourceAnnoClass, Predicate<_anno> constraint) {
        return of( sourceAnnoClass).and(constraint);
    }
    
    /**
     * 
     * @param anonymousObjectWithAnnotation
     * @return 
     */
    public static $anno of( Object anonymousObjectWithAnnotation ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expr.anonymousObject( ste );
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty() ).findFirst().get();
        return of( _anno.of(bd.getAnnotation(0) ) );        
    }

    /** Default Matching constraint (by default ALWAYS Match)*/
    public Predicate<_anno> constraint = a -> true;

    /** the id of the annotation */
    public $id name;

    /** the member values of the annotation */
    public List<$memberValue> $mvs = new ArrayList<>();

    /**
     * 
     * @param name
     * @param mvs
     */
    private $anno( $id name, $memberValue...mvs ){
       this.name = name;
       Arrays.stream(mvs).forEach( mv -> this.$mvs.add(mv));       
    }

    /**
     *
     * @param proto
     */
    public $anno(_anno proto) {
        this.name = $id.of(proto.getName());
        AnnotationExpr astAnn = proto.ast();
        if (astAnn instanceof NormalAnnotationExpr) {
            NormalAnnotationExpr na = (NormalAnnotationExpr) astAnn;
            na.getPairs().forEach(mv -> $mvs.add($memberValue.of(mv.getNameAsString(), mv.getValue())));
        } else if (astAnn instanceof SingleMemberAnnotationExpr) {            
            SingleMemberAnnotationExpr sa = (SingleMemberAnnotationExpr) astAnn;
            $mvs.add($memberValue.of(sa.getMemberValue()));
        }
    }

    /**
     *
     * @return
     */
    public $anno $name(){
        this.name = $id.any();
        return this;
    }
    
    /**
     * updates the name of the prototype for matching and constructing
     * @param name
     * @return 
     */
    public $anno $name($id name){
        this.name = name;
        return this;
    }
    
    /**
     * Updates the name prototype for matching and constructing
     * @param name
     * @return 
     */
    public $anno $name(String name){
        this.name = $id.of(name);
        return this;
    } 
    
    /**
     * 
     * @param mv
     * @return 
     */
    public $anno $memberValue( $memberValue mv ){
        this.$mvs.add(mv);
        return this;
    }
    
    /**
     * 
     * @param key
     * @param value
     * @return 
     */
    public $anno $memberValue(String key, Expression value){
        this.$mvs.add( new $memberValue(key, value) );
        return this;
    }
    
    /**
     * 
     * @param key
     * @param value
     * @return 
     */
    public $anno $memberValue(String key, String value){
        this.$mvs.add( new $memberValue(key, value) );
        return this;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isMatchAny(){
        return name.isMatchAny() && ( this.$mvs.isEmpty() || (this.$mvs.size() ==1 && this.$mvs.get(0).isMatchAny() ));
    }
    
    /**
     * ADDS an additional matching constraint to the prototype
     *
     * @param constraint a constraint to be added
     * @return the modified prototype
     */
    public $anno and(Predicate<_anno> constraint) {
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    public Tokens decompose(_anno _a) {
        //Tokens ts = $name.decompose(_a.getName());
        if( ! this.constraint.test(_a)){
            //System.out.println( "Didnt pass MVP Constraint");
            return null;
        }
        
        Tokens ts = name.decompose(_a.getName());
        if( ts == null ){
            //System.out.println( "Decompose null for name "+name+" for \""+_a.getName()+"\"");
            return null;
        }
        if ($mvs.isEmpty() ) {
            //System.out.println( "Returning "+ts+" for name \""+_a.getName()+"\"");
            return ts;
        }
        AnnotationExpr astAnn = _a.ast();
        if (astAnn instanceof MarkerAnnotationExpr) {
            //System.out.println( "Marketer");            
            if ($mvs.size() == 1) {
                if ($mvs.get(0).isMatchAny()) {
                    return ts;
                }
                return null;
            }            
            return null;
        }
        if (astAnn instanceof SingleMemberAnnotationExpr) {
            //System.out.println( "SingleMember");
            if ($mvs.size() == 1) {
                SingleMemberAnnotationExpr sme = (SingleMemberAnnotationExpr) astAnn;
                Tokens props = $mvs.get(0).decompose(sme.getMemberValue());
                if( props == null ){
                    return null;
                }
                if (ts.isConsistent(props)) {                    
                    ts.putAll(props);
                    return ts;
                } 
            }
            return null;
        }
        if (astAnn instanceof NormalAnnotationExpr) {
            NormalAnnotationExpr astNa = (NormalAnnotationExpr) astAnn;
            //System.out.println( "Normal "+ astAnn +" mvs size "+ $mvs.size()+" "+astNa+" "+astNa.getPairs());
            if( astNa.getPairs().isEmpty() ){
                //System.out.println( "Empty parameters ");
                if( $mvs.isEmpty() ){
                    //System.out.println( "returning "+ ts );
                    return ts;
                }
                if( $mvs.size() == 1 && $mvs.get(0).isMatchAny() ){
                    //System.out.println( "returning "+ ts );
                    return ts;
                }
                return null;
            }
            if( $mvs.size() == 0 ){
                return ts;
            }
            
            if( $mvs.size() == 1 && astNa.getPairs().isEmpty() && $mvs.get(0).isMatchAny() ){
                return ts;
            }
            if ($mvs.size() <= astNa.getPairs().size()) {
                //System.out.println( "Checking pairs"+astNa.getPairs());
                List<MemberValuePair> mvpsC = new ArrayList<>();
                mvpsC.addAll(astNa.getPairs());
                for (int i = 0; i < $mvs.size(); i++) {
                    $memberValue.Select sel = $mvs.get(i).selectFirst(mvpsC);
                    if (sel == null) {
                        return null;
                    } else {
                        mvpsC.remove(sel.astMvp); //whenever I find a match, I remove the matcher
                        if( !ts.isConsistent(sel.args.asTokens())){
                            return null;
                        }
                        //System.out.println( "    Adding Tokens "+ ts );
                        ts.putAll(sel.args.asTokens());
                        //System.out.println( "    Added Tokens "+ ts );
                    }
                }
                return ts;                 
            }
            return null;
        }
        return null;
    }

    public boolean matches( AnnotationExpr astAnno){
        return select( astAnno ) != null;
    }
    
    public boolean matches(String... anno) {
        return matches(_anno.of(anno));
    }

    public boolean matches(_anno _a) {
        return select(_a) != null;
    }

    public String composeToString( Object...values ){
        return composeToString( Translator.DEFAULT_TRANSLATOR, Tokens.of(values ));
    }
    
    public String composeToString(Translator translator, Map<String, Object> keyValues) {
        if( keyValues.get("$anno") != null ){
            //override parameter passed in
            $anno $a = $anno.of( keyValues.get("$anno").toString() );
            Map<String,Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$anno"); //remove to avoid stackOverflow
            return $a.composeToString(translator, kvs);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("@");
        sb.append(name.compose(translator, keyValues));
        String properties = "";
        for (int i = 0; i < $mvs.size(); i++) {
            if ((properties.length() > 0) && (!properties.endsWith(","))) {
                properties = properties + ",";
            }
            properties += $mvs.get(i).compose(translator, keyValues);
        }
        if (properties.length() == 0) {
            return sb.toString();
        }
        return sb.toString() + "(" + properties + ")";
    }
    
    @Override
    public _anno compose(Translator translator, Map<String, Object> keyValues) {
        if( keyValues.get("$anno") != null ){
            //override parameter passed in
            $anno $a = $anno.of( keyValues.get("$anno").toString() );
            Map<String,Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$anno"); //remove to avoid stackOverflow
            return $a.compose(translator, kvs);
        }
        //return _anno.of(compose(translator, keyValues));
        return _anno.of(composeToString(translator, keyValues));
    }

    @Override
    public $anno $(String target, String $Name) {
        name.$(target, $Name);
        $mvs.forEach(mv -> mv.key.pattern = mv.key.pattern.$(target, $Name));
        return this;
    }
    
    public $anno hardcode$( Translator tr, Object...keyValues){
        name.hardcode$(tr, keyValues);
        $mvs.forEach(mv -> mv.key.pattern = mv.key.pattern.hardcode$(tr, keyValues));
        return this;
    }

    public boolean match( Node node ){
        if( node instanceof AnnotationExpr ){
            return matches( (AnnotationExpr)node);
        }
        return false;
    }

    public Select select( AnnotationExpr astAnn){
        return select(_anno.of(astAnn));
    }
    
    public Select select(String... anno){
        return select(_anno.of(anno));
    }
    
    public Select select(_anno _a){
        //System.out.println( "testing "+ _a);
        if(this.constraint.test(_a)){
            //System.out.println( "passed constraint "+ _a);
            Tokens ts = decompose(_a);            
            if( ts != null ){
                return new Select(_a, ts);
            }
        }
        return null;
    }
    
    @Override
    public List<String> list$() {
        List<String> params = new ArrayList<>();
        params.addAll( this.name.list$() );
        this.$mvs.forEach(m -> {
            params.addAll( m.key.pattern.list$() ); 
            params.addAll( m.value.list$() ); 
        });
        return params;
    }

    @Override
    public List<String> list$Normalized() {
        List<String> params = new ArrayList<>();
        params.addAll( this.name.list$() );
        this.$mvs.forEach(m -> {
            params.addAll( m.key.pattern.list$Normalized() ); 
            params.addAll( m.value.list$Normalized() ); 
        });
        return params.stream().distinct().collect(Collectors.toList() );
    }

    /**
     * Return a
     * @param clazz
     * @param annoType
     * @return
     */
    public _type replaceIn(Class clazz, Class<? extends Annotation> annoType ){
        return replaceIn(clazz, $anno.of(annoType) );
    }
    
    /**
     * Replace all occurrences of the template in the code with the replacement
     * (composing the replacement from the constructed tokens in the source)
     *
     * @param clazz 
     * @param a the template to be constructed as the replacement
     * @return
     */
    public _type replaceIn(Class clazz, $anno a ){
        return replaceIn(_java.type(clazz), a);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param annoType
     * @return 
     */
    public <_J extends _java> _J replaceIn(_J _j, Class<? extends Annotation> annoType ){
        return replaceIn(_j, $anno.of(annoType) );
    }
     
    /**
     * Replace all occurrences of the template in the code with the replacement
     * (composing the replacement from the constructed tokens in the source)
     *
     * @param _j the model to find replacements
     * @param a the template to be constructed as the replacement
     * @param <_J> the TYPE of model
     * @return
     */
    public <_J extends _java> _J replaceIn(_J _j, $anno a ){
        if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                replaceIn(_c.astCompilationUnit(), a);
                return _j;
            }
            _type _t = (_type) _j; //only possible
            replaceIn(_t.ast(), a); //return the TypeDeclaration, not the CompilationUnit
            return _j;
        }
        replaceIn(((_node) _j).ast(), a);
        return _j;
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param annoType
     * @return 
     */
    public <N extends Node> N replaceIn(N astNode, Class<? extends Annotation> annoType ){
        return replaceIn(astNode, $anno.of(annoType));
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param a
     * @return 
     */
    public <N extends Node> N replaceIn(N astNode, $anno a ){
        astNode.walk(AnnotationExpr.class, e-> {
            Select sel = select( e );
            if( sel != null ){
                sel._ann.ast().replace(a.compose(sel.args).ast() );
            }
        });
        return astNode;
    }
    
    @Override
    public _anno firstIn(Node astStartNode, Predicate<_anno> _annoMatchFn) {
        Optional<Node>on = 
            astStartNode.stream().filter(
                n -> {
                    if( n instanceof AnnotationExpr){
                        Select sel = select((AnnotationExpr)n); 
                        return ( sel != null && _annoMatchFn.test(sel._ann));
                    } 
                    return false;
                }).findFirst();
        if( on.isPresent() ){
            return _anno.of( (AnnotationExpr)on.get());
        }
        return null;
    }

    /**
     * 
     * @param astNode
     * @return 
     */
    @Override
    public Select selectFirstIn(Node astNode) {
        Optional<Node>on = 
            astNode.stream().filter(
                n -> n instanceof AnnotationExpr 
                    && select((AnnotationExpr)n) != null )
                .findFirst();
        if( on.isPresent() ){
            return select( (AnnotationExpr)on.get());
        }
        return null;        
    }
    
    /**
     * 
     * @param clazz
     * @return 
     */
    @Override
    public Select selectFirstIn( Class clazz){
        return selectFirstIn( _java.type(clazz));
    } 
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn( Class clazz, Predicate<Select> selectConstraint ){
        return selectFirstIn(_java.type(clazz), selectConstraint );
    }
    
    /**
     * 
     * @param astRootNode
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn(Node astRootNode, Predicate<Select> selectConstraint) {
        Optional<Node>on = 
            astRootNode.stream().filter(
                n -> {
                    if( n instanceof AnnotationExpr){
                        Select sel = select( (AnnotationExpr)n);
                        return sel != null && selectConstraint.test(sel);
                    }
                    return false;
                }).findFirst();
        
        if( on.isPresent() ){
            return select( (AnnotationExpr)on.get());
        }
        return null;        
    }

    /**
     * 
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn(_java _j, Predicate<Select>selectConstraint) {
        if( _j instanceof _code ){
            _code _c = (_code)_j;
            if( _c.isTopLevel() ){
                return selectFirstIn(_c.astCompilationUnit(), selectConstraint);
            }
            _type _t = (_type)_j; //only possible
            return selectFirstIn(_t.ast(), selectConstraint); //return the TypeDeclaration, not the CompilationUnit            
        }
        return selectFirstIn(((_node)_j).ast(), selectConstraint);
    }
    
    @Override
    public List<Select> listSelectedIn(Node astNode) {
        List<Select> found = new ArrayList<>();
        astNode.walk(AnnotationExpr.class, a-> {
            Select sel = select(_anno.of(a));
            if( sel != null ){
                found.add( sel  );
            }
        });
        return found;
    }

    /**
     * 
     * @param astRootNode
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(Node astRootNode, Predicate<Select> selectConstraint) {
        List<Select> found = new ArrayList<>();
        astRootNode.walk(AnnotationExpr.class, a-> {
            Select sel = select(_anno.of(a));
            if( sel != null && selectConstraint.test(sel)){
                found.add( sel  );
            }
        });
        return found;
    }

    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(Class clazz, Predicate<Select> selectConstraint) {
        return listSelectedIn(_java.type(clazz), selectConstraint);
    }
    
    /**
     * 
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(_java _j, Predicate<Select> selectConstraint) {
         if( _j instanceof _code ){
            _code _c = (_code)_j;
            if( _c.isTopLevel() ){
                return listSelectedIn(_c.astCompilationUnit(), selectConstraint);
            }
            _type _t = (_type)_j; //only possible
            return listSelectedIn(_t.ast(), selectConstraint); //return the TypeDeclaration, not the CompilationUnit            
        }
        return listSelectedIn(((_node)_j).ast(), selectConstraint);
    }    
    
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_anno> _nodeMatchFn, Consumer<_anno> _nodeActionFn) {
        astNode.walk(AnnotationExpr.class, a-> {
            Select sel = select(_anno.of(a));
            if( sel != null && _nodeMatchFn.test(sel._ann)){
                _nodeActionFn.accept(sel._ann);
            }
        });
        return astNode;
    }

    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param selectActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode, Consumer<Select> selectActionFn) {
        astRootNode.walk(AnnotationExpr.class, a-> {
            Select sel = select(_anno.of(a));
            if( sel != null ){
                selectActionFn.accept(sel);
            }
        });
        return astRootNode;
    }
    
    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        astRootNode.walk(AnnotationExpr.class, a-> {
            Select sel = select(_anno.of(a));
            if( sel != null && selectConstraint.test(sel)){
                selectActionFn.accept(sel);
            }
        });
        return astRootNode;
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectActionFn
     * @return 
     */
    public <_J extends _java> _J forSelectedIn(_J _j, Consumer<Select> selectActionFn) {
         if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                forSelectedIn(_c.astCompilationUnit(), selectActionFn);
                return _j;
            }
            _type _t = (_type) _j; //only possible
            forSelectedIn(_t.ast(), selectActionFn); //return the TypeDeclaration, not the CompilationUnit            
            return _j;
        }
        forSelectedIn(((_node) _j).ast(), selectActionFn);
        return _j;
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public <_J extends _java> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
         if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                forSelectedIn(_c.astCompilationUnit(), selectConstraint, selectActionFn);
                return _j;
            }
            _type _t = (_type) _j; //only possible
            forSelectedIn(_t.ast(), selectConstraint, selectActionFn); //return the TypeDeclaration, not the CompilationUnit            
            return _j;
        }
        forSelectedIn(((_node) _j).ast(), selectActionFn);
        return _j;
    }
    
    /**
     * 
     * @param clazz
     * @param selectActionFn
     * @return 
     */
    public _type forSelectedIn(Class clazz, Consumer<Select> selectActionFn) {
        return forSelectedIn(_java.type(clazz), selectActionFn);         
    }

    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public _type forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        return forSelectedIn(_java.type (clazz), selectConstraint, selectActionFn);         
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("@");
        sb.append(this.name.pattern);
        if( this.$mvs.isEmpty() ){
            return sb.toString();
        }
        sb.append("(");
        for(int i=0;i<this.$mvs.size();i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append( $mvs.toString() );            
        }
        sb.append(")");
        sb.append(System.lineSeparator());
        return sb.toString();        
    }
    
    /**
     * prototype for member values (i.e. the key values inside the annotation)
     * i.e. @A(key="value")
     */
    public static class $memberValue {

        public $id key = $id.any();

        public $expr value = new $expr(Expression.class, "$value$");

        public Predicate<MemberValuePair> constraint = t -> true;

        public static $memberValue of(Expression value) {
            return new $memberValue("$_$", value);
        }

        public static $memberValue of(String key, String value) {
            return new $memberValue(key, value);
        }

        public String compose(Translator translator, Map<String, Object> keyValues) {
            String k = null;
            if( !key.isMatchAny() ){
                k = key.compose(translator, keyValues);
            }
            Expression v = value.compose(translator, keyValues);
            if (k == null || k.length() == 0) {                
                return v.toString();
            }
            return k + "=" + v;
        }

        public boolean isMatchAny() {
            boolean k = key.isMatchAny();
            boolean v = value.isMatchAny();
            return   k && v;
        }

        public static $memberValue of(String key, Expression exp) {
            return new $memberValue(key, exp);
        }

        public static $memberValue any() {
            return new $memberValue("$key$", "$value$");
        }

        public $memberValue(String name, String value) {
            this(name, Expr.of(value));
        }

        public $memberValue(String name, Expression value) {
            this.key.pattern = Stencil.of(name);
            Stencil st = Stencil.of(value.toString());
            if( st.isMatchAny() ){
                this.value = new $expr(Expression.class, value.toString() );   
            } else {
                this.value = $expr.of(value);
            }
        }

        /**
         *
         * @param target
         * @param $name
         * @return
         */
        public $memberValue $(String target, String $name) {
            this.key.pattern = this.key.pattern.$(target, $name);
            this.value = this.value.$(target, $name);
            return this;
        }

        /**
         *
         * @param pairs
         * @return
         */
        public Select selectFirst( List<MemberValuePair> pairs ){
            for(int i=0;i<pairs.size();i++){
                Select sel = select(pairs.get(i) );
                if( sel != null ){
                    return sel;
                }
            }            
            return null;
        }

        public boolean match( Node node ){
            if( node instanceof MemberValuePair ){
                return matches( (MemberValuePair) node);
            }
            return false;
        }

        /**
         * 
         * @param mvp
         * @return 
         */
        public boolean matches(MemberValuePair mvp) {
            return select(mvp) != null;
        }

        /**
         * 
         * @param onlyValueExpression
         * @return 
         */
        public Tokens decompose(Expression onlyValueExpression){            
            if( constraint.test( new MemberValuePair("_", onlyValueExpression) ) ) {
                $expr.Select sel = value.select(onlyValueExpression.toString());
                if( sel == null ){
                    return null;
                }
                return sel.args.asTokens();
            }
            return null;
        }
        
        /**
         * 
         * @param mvp
         * @return 
         */
        public Tokens decompose(MemberValuePair mvp ){
            if (mvp == null) {
                return null;
            }
            if (constraint.test(mvp)) {
                Tokens ts = key.decompose(mvp.getNameAsString());
                $expr.Select sel = value.select(mvp.getValue());
                if( sel == null || !ts.isConsistent(sel.args.asTokens())){
                    return null;
                }
                ts.putAll(sel.args.asTokens());
                //ts = value.decomposeTo(mvp.getValue().toString(), ts);
                return ts;
            }
            return null;
        }
        
        /**
         * When we have an anno like
         * @param onlyValueExpression
         * @return 
         * @count(100)
         * (a SingleMemberAnnotationExpr)
         * 
         */
        public Select select (Expression onlyValueExpression){            
            MemberValuePair mvp = new MemberValuePair("", onlyValueExpression); 
            if( constraint.test( mvp ) ) {
                $expr.Select sel = value.select(onlyValueExpression);
                if( sel != null ){
                    return new Select(mvp, sel.args.asTokens());
                }
            }
            return null;
        }
        
        /**
         * 
         * @param mvp
         * @return 
         */
        public Select select(MemberValuePair mvp) {
            if (mvp == null) {
                return null;
            }
            if (constraint.test(mvp)) {
                Tokens ts = key.decompose(mvp.getNameAsString());
                if( ts == null ){
                    return null;
                }
                $expr.Select sel = value.select(mvp.getValue());
                if( sel == null || !ts.isConsistent(sel.args.asTokens())){
                    return null;
                }
                ts.putAll(sel.args.asTokens());
                return new Select(mvp, ts);
            }
            return null;
        }

        public static class Select
            implements $proto.selected, selectedAstNode<MemberValuePair> {

            public final MemberValuePair astMvp;
            public final $args args;

            public Select(MemberValuePair astMvp, Tokens tokens) {
                this.astMvp = astMvp;
                this.args = $args.of(tokens);
            }

            @Override
            public $args args() {
                return args;
            }

            @Override
            public String toString() {
                return "$anno.$memberValue.Select {" + System.lineSeparator()
                        + Text.indent(astMvp.toString()) + System.lineSeparator()
                        + Text.indent("$args : " + args) + System.lineSeparator()
                        + "}";
            }

            /**
             * It's not a "true" member value pair, but rather a synthesized
             * MemberValue Pair with no name... this is for cases where we have an 
             * annotation like 
             * @name("Eric")
             * as apposed to cases where the key and value are spelled out
             * @name(value="Eric")
             * 
             * @return true if its a "value only" member value pair
             */
            public boolean isValueOnly(){
                return astMvp.getNameAsString().equals("_");
            }
            
            @Override
            public MemberValuePair ast() {
                return astMvp;
            }

            public String getName() {
                return astMvp.getNameAsString();
            }

            public Expression getValue() {
                return astMvp.getValue();
            }
        }
    }
    
    /**
     * A Matched Selection result returned from matching a prototype $a
     * inside of some Node or _node
     */
    public static class Select
        implements $proto.selected, selected_model<_anno>, selectedAstNode<AnnotationExpr> {

        public final _anno _ann;
        public final $args args;

        public Select(_anno _a, Tokens tokens) {
            this(_a, $args.of(tokens));
        }

        public Select(_anno _a, $args tokens) {
            this._ann = _a;
            args = tokens;
        }

        public Select(AnnotationExpr astAnno, $args tokens) {
            this._ann = _anno.of( astAnno );
            this.args = tokens;
        }

        @Override
        public $args args() {
            return args;
        }

        @Override
        public String toString() {
            return "$anno.Select {" + System.lineSeparator()
                + Text.indent(_ann.toString()) + System.lineSeparator()
                + Text.indent("$args : " + args) + System.lineSeparator()
                + "}";
        }

        /**
         * Is the name of the selected _anno the name provided
         * @param name
         * @return 
         */
        public boolean isNamed(String name) {
            return _ann.isNamed(name);
        }

        public boolean isSingleValue() {
            return _ann.ast() instanceof SingleMemberAnnotationExpr;
        }

        public boolean hasKeyValues() {
            return _ann.ast() instanceof NormalAnnotationExpr;
        }

        public boolean hasNoValues() {
            return _ann.ast() instanceof MarkerAnnotationExpr;
        }

        @Override
        public AnnotationExpr ast() {
            return _ann.ast();
        }

        @Override
        public _anno _node() {
            return _ann;
        }
    }
}
