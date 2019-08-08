package org.jdraft.proto;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalBlockStmt;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.Type;
import org.jdraft._draftException;
import org.jdraft.Expr;
import org.jdraft.*;
import org.jdraft._anno._annos;
import org.jdraft._parameter._parameters;
import java.lang.annotation.Annotation;
import java.util.function.*;
import org.jdraft._typeParameter._typeParameters;
import org.jdraft.macro._macro;
import org.jdraft.macro._remove;

/**
 * This abstraction is a shortcut to unify all of the $prototypes in a single
 * API that is easy / convenient to use (since modern IDES will autocomplete, this
 * will help simplify getting up to speed/ using the protyotype API)
 * 
 * @author Eric
 */
public final class $ {
    /** cant construct one of these */
    private $(){}
    
    public static $anno anno(){
        return $anno.any();
    }
    
    public static $anno anno( $id name, $anno.$memberValue...memberValues ){
        return $anno.of(name, memberValues);
    }
    
    public static $anno anno(Predicate<_anno> constraint){
        return $anno.any().addConstraint(constraint);
    }
    
    public static $anno anno(String pattern){
        return $anno.of(pattern);
    }
    
    public static $anno anno(String pattern, Predicate<_anno> constraint){
        return $anno.of(pattern).addConstraint(constraint);
    }
    
    public static $anno anno( Class<? extends Annotation> clazz ){
        return $anno.of(clazz);
    }
    
    public static $anno anno( Class<? extends Annotation> clazz, Predicate<_anno> constraint){
        return $anno.of(clazz).addConstraint(constraint);
    }
    
    public static $annos annos(){
        return $annos.any();
    }
    
    public static $annos annos( Predicate<_annos> constraint ){
        return $annos.of().addConstraint(constraint);
    }
    
    public static $annos annos( _annos _anns ){
        return $annos.of(_anns);
    }
    
    public static $annos annos( _annos _anns, Predicate<_annos> constraint){
        return $annos.of(_anns).addConstraint(constraint);
    }
    
    public static $annos annos($anno... annos){
        return $annos.of(annos);
    }
    
    public static $body body(){
        return $body.any();
    }
    
    public static $body body( Predicate<_body> constraint){
        return $body.of().addConstraint(constraint);
    }
    
    public static $body body(String...body){
        return $body.of(body);
    }
    
    public static $body body( _body _bd ){
        return $body.of(_bd);
    }
    
    public static $body body( _body _bd, Predicate<_body> constraint){
        return $body.of(_bd);
    }
    
    public static $body body( NodeWithBlockStmt astNodeWithBlock ){
        return $body.of(_body.of(astNodeWithBlock));
    }
    
    public static $body body( NodeWithOptionalBlockStmt astNodeWithBlock ){
        return $body.of(_body.of(astNodeWithBlock));
    }
    
    public static $body body(Expr.Command commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }
    
    public static $body body(Consumer commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }
    
    public static $body body(BiConsumer commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }
    
    public static $body body(Expr.TriConsumer commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }
    
    public static $body body(Expr.QuadConsumer commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }
    
    public static <A extends Object, B extends Object> $body body(Function<A,B> commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }
    
    public static <A extends Object, B extends Object, C extends Object> $body body(BiFunction<A,B,C> commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }
    
    public static <A extends Object, B extends Object, C extends Object, D extends Object> $body body(Expr.TriFunction<A,B,C,D> commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }
    
    public static <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object>  $body body(Expr.QuadFunction<A,B,C,D,E> commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return new $body ( le );
    }
    
    public static $comment comment(){
        return $comment.any();
    }
    
    public static $comment comment( Predicate<Comment> constraint ){
        return $comment.of(constraint);
    }
    
    public static $comment comment( String commentPattern ){
        return $comment.of(commentPattern);
    }
    
    public static $comment<JavadocComment> javadoc( String contentPattern){
        return $comment.javadocComment(contentPattern);
    } 
    
    public static $comment<JavadocComment> javadoc( Predicate<JavadocComment> constraint ){
        return $comment.javadocComment(constraint);
    } 
    
    public static $constructor constructor(){
        return $constructor.any();
    }

    public static $constructor constructor( Predicate<_constructor> constraint){
        return $constructor.of().addConstraint(constraint);
    }
    
    public static $constructor constructor( _constructor _proto ){
        return $constructor.of(_proto);
    }
    
    public static $constructor constructor( _constructor _proto, Predicate<_constructor> constraint ){
        return $constructor.of(_proto).addConstraint(constraint);
    }
    
    public static $constructor constructor( String pattern){
        return $constructor.of(pattern);
    }
    
    public static $constructor constructor( String pattern, Predicate<_constructor> constraint){
        return $constructor.of(pattern).addConstraint(constraint);
    }
    
    public static $constructor constructor( String...pattern){
        return $constructor.of(pattern);
    }
    
    public static $constructor constructor($constructor.$part part){
        return $constructor.of(part);
    }
    
    public static $constructor constructor($constructor.$part...parts){
        return $constructor.of(parts);
    }
    
    public static $constructor constructor(Object anonymousObjectContainingMethod){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return $constructor.of( ste, anonymousObjectContainingMethod);
    }

    public static $method method(){
        return $method.any();
    }

    public static $method method(String methodPrototype){
        return $method.of(methodPrototype);
    }

    public static $method method(String...ms){
        return $method.of(ms);
    }

    public static $method method(Object anonymousObjectContainingMethod){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expr.anonymousObject(ste);
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        //removeIn all things that aren't METHODS or METHODS WITH @_remove
        bds.removeIf(b -> b.isAnnotationPresent(_remove.class) || (!(b instanceof MethodDeclaration)));
        //there should be only (1) method left, if > 1 take the first method
        MethodDeclaration md = (MethodDeclaration) bds.get(0);
        _method _m =  _macro.to(anonymousObjectContainingMethod.getClass(), _method.of(md));

        return $method.of( _m);
    }

    public static $method method(Predicate<_method> constraint){
        return $method.any().addConstraint(constraint);
    }

    public static $method method(_method ms){
        return $method.of(ms);
    }

    public static $method method(_method ms, Predicate<_method> _methodMatchFn){
        return $method.of(ms).addConstraint(_methodMatchFn);
    }

    public static $method method($method.$part part){
        return $method.of(part);
    }

    public static $method method($method.$part...parts){
        return $method.of(parts);
    }

    public static $expr expr(){
        return $expr.any();
    }
    
    public static $expr expr(Predicate<Expression> constraint){
        return $expr.any().addConstraint(constraint);
    }
    
    public static $expr expr(String... pattern){
        return $expr.of(pattern);
    }
    
    public static <T extends Expression> $expr<T> of(T protoExpr ){
        return $expr.of(protoExpr);
    }
    
    public static <T extends Expression> $expr<T> of(T protoExpr, Predicate<T> constraint){
        return $expr.of(protoExpr).addConstraint(constraint);
    }
   
    public static $expr<StringLiteralExpr> of( String stringLiteral ){
        return $expr.stringLiteral(stringLiteral);
    }
    
    public static $expr<CharLiteralExpr> of(char c){
        return $expr.of(c);
    }
    
    public static $expr<IntegerLiteralExpr> of(int i){
        return $expr.of(i);
    }
    
    /**
     * Note: this can be finniky
     * @param f
     * @return 
     */
    public static $expr<DoubleLiteralExpr> of(float f){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        Class clazz = null;
        try{
            clazz = Class.forName(ste.getClassName() );
        }catch(Exception e){
            throw new _draftException("unable to resolve calling class "+ ste.getClassName());
        }
        $expr.Select s = $expr.of("$.of($val$)").selectFirstIn(
                clazz, se-> {
            try{
                if( se.astExpression.getBegin().get().line == ste.getLineNumber() ){
                    Float.parseFloat( se.get("val").toString() );
                    return true;
                }
                return false;
            } catch(Exception ex ){
                return false;
            }
        });
        return $expr.doubleLiteral( s.get("val").toString() );        
    }
    
    public static $expr<DoubleLiteralExpr> of(double d){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        Class clazz = null;
        try{
            clazz = Class.forName(ste.getClassName() );
        }catch(Exception e){
            throw new _draftException("unable to resolve calling class "+ ste.getClassName());
        }
        $expr.Select s = $expr.of("$.of($val$)").selectFirstIn(
                clazz, se-> {
            try{
                if( se.astExpression.getBegin().get().line == ste.getLineNumber() ){
                    Float.parseFloat( se.get("val").toString() );
                    return true;
                }
                return false;
            } catch(Exception ex ){
                return false;
            }
        });
        return $expr.doubleLiteral( s.get("val").toString() );        
    }
     
    
    public static $expr<LongLiteralExpr> of(long l){
        return $expr.of(l);
    }
    
    public static $expr<BooleanLiteralExpr> of(boolean b){        
        return $expr.of(b);
    }
    
    /**
     * index Expression 
     * @return 
     */
    public static $expr<ArrayAccessExpr> arrayAccess(){
        return $expr.arrayAccess();
    }

    public static $expr<ArrayAccessExpr> arrayAccess(String ae){
        return $expr.arrayAccess(ae);
    }

    public static $expr<ArrayAccessExpr> arrayAccess(ArrayAccessExpr aae){
        return $expr.of(aae);
    }

    public static $expr<ArrayAccessExpr> arrayAccess(Predicate<ArrayAccessExpr> aae){
        return $expr.arrayAccess(aae);
    }
    
    public static $expr<ArrayCreationExpr> arrayCreation(){
        return $expr.arrayCreation();    
    }

    public static $expr<ArrayCreationExpr> arrayCreation(String ac){
        return $expr.arrayCreation(ac);
    }

    public static $expr<ArrayCreationExpr> arrayCreation(ArrayCreationExpr ace){
        return $expr.of(ace);
    }

    public static $expr<ArrayCreationExpr> arrayCreation(Predicate<ArrayCreationExpr> ace){
        return $expr.arrayCreation(ace);
    }
    
    /**
     * new int[][]{{1, 1}, {2, 2}};
     * @return 
     */
    public static $expr<ArrayInitializerExpr> arrayInit(){
        return $expr.arrayInitializer();
    }

    public static $expr<ArrayInitializerExpr> arrayInit(String ae){
        return $expr.arrayInitializer(ae);
    }

    public static $expr<ArrayInitializerExpr> arrayInit( ArrayInitializerExpr aei){
        return $expr.of(aei);
    }

    public static $expr<ArrayInitializerExpr> arrayInit(Predicate<ArrayInitializerExpr> aei){
        return $expr.arrayInitializer(aei);
    }
    
    public static $expr<AssignExpr> assign(){
        return $expr.assign();
    }

    public static $expr<AssignExpr> assign(String a){
        return $expr.assign(a);
    }

    public static $expr<AssignExpr> assign(AssignExpr ae){
        return $expr.of(ae);
    }

    public static $expr<AssignExpr> assign(Predicate<AssignExpr> ae){
        return $expr.assign(ae);
    }
    
    public static $expr<BinaryExpr> binaryExpr(){
        return $expr.binary();
    }

    public static $expr<BinaryExpr> binaryExpr(String be){
        return $expr.binary(be);
    }

    public static $expr<BinaryExpr> binaryExpr(BinaryExpr be){
        return $expr.of(be);
    }

    public static $expr<BinaryExpr> binaryExpr(Predicate<BinaryExpr> be){        
        return $expr.binary(be);
    }
    
    public static $expr<BooleanLiteralExpr> booleanLiteral(){
        return $expr.booleanLiteral();
    }
    
    public static $expr<BooleanLiteralExpr> booleanLiteral(boolean bool){
        return $expr.booleanLiteral(bool);
    }
    
    public static $expr<BooleanLiteralExpr> booleanLiteral(Predicate<BooleanLiteralExpr> bl){
        return $expr.booleanLiteral(bl);
    }
        
    public static $expr<CastExpr> cast(){
        return $expr.cast();
    }

    public static $expr<CastExpr> cast(String ce){
        return $expr.cast(ce);
    }

    public static $expr<CastExpr> cast(CastExpr ce){
        return $expr.of(ce);
    }

    public static $expr<CastExpr> cast(Predicate<CastExpr> ce){
        return $expr.cast(ce);
    }
    
    public static $expr<CharLiteralExpr> charLiteral(){
        return $expr.charLiteral();
    }
    
    public static $expr<CharLiteralExpr> charLiteral(char c){
        return $expr.charLiteral(c);
    }
    
    public static $expr<CharLiteralExpr> charLiteral(Predicate<CharLiteralExpr> pc){
        return $expr.charLiteral(pc);
    }
    
    public static $expr<ClassExpr> classExpr(){
        return $expr.classExpr();
    }

    public static $expr<ClassExpr> classExpr(String ce){
        return $expr.classExpr(ce);
    }

    public static $expr<ClassExpr> classExpr( ClassExpr ce){
        return $expr.of(ce);
    }

    public static $expr<ClassExpr> classExpr(Predicate<ClassExpr> ce){
        return $expr.classExpr(ce);
    }
    
    public static $expr<ConditionalExpr> conditionalExpr(){
        return $expr.conditional();
    }

    public static $expr<ConditionalExpr> conditionalExpr(String ce){
        return $expr.conditional(ce);
    }

    public static $expr<ConditionalExpr> conditionalExpr(ConditionalExpr ce){
        return $expr.of(ce);
    }

    public static $expr<ConditionalExpr> conditionalExpr(Predicate<ConditionalExpr> ce){
        return $expr.conditional(ce);
    }
    
    public static $expr<DoubleLiteralExpr> doubleLiteral(){
        return $expr.doubleLiteral();
    }
    
    public static $expr<DoubleLiteralExpr> doubleLiteral(Predicate<DoubleLiteralExpr> dl){
        return $expr.doubleLiteral(dl);
    }
    
    public static $expr<DoubleLiteralExpr> doubleLiteral(String pattern){
        return $expr.doubleLiteral(pattern);
    }
    
    public static $expr<DoubleLiteralExpr> doubleLiteral(double dbl){
        return $expr.doubleLiteral(dbl);
    }
    
    public static $expr<EnclosedExpr> enclosedExpr(){
        return $expr.enclosedExpr();
    }

    public static $expr<EnclosedExpr> enclosedExpr(String ee){
        return $expr.enclosedExpr(ee);
    }

    public static $expr<EnclosedExpr> enclosedExpr( EnclosedExpr ee){
        return $expr.of(ee);
    }

    public static $expr<EnclosedExpr> enclosedExpr(Predicate<EnclosedExpr> ee){
        return $expr.enclosedExpr(ee);
    }
    
    public static $expr<FieldAccessExpr> fieldAccessExpr(){
        return $expr.fieldAccess();
    }

    public static $expr<FieldAccessExpr> fieldAccessExpr(String fae){
        return $expr.fieldAccess(fae);
    }

    public static $expr<FieldAccessExpr> fieldAccessExpr(FieldAccessExpr fae){
        return $expr.of(fae);
    }

    public static $expr<FieldAccessExpr> fieldAccessExpr(Predicate<FieldAccessExpr> fae){
        return $expr.fieldAccess(fae);
    }
    
    public static $expr<InstanceOfExpr> instanceOf(){
        return $expr.instanceOf();
    }

    public static $expr<InstanceOfExpr> instanceOf(String io){
        return $expr.instanceOf(io);
    }

    public static $expr<InstanceOfExpr> instanceOf(InstanceOfExpr io){
        return $expr.of(io);
    }

    public static $expr<InstanceOfExpr> instanceOf(Predicate<InstanceOfExpr> io){
        return $expr.instanceOf(io);
    }
    
    public static $expr<IntegerLiteralExpr> intLiteral(){
        return $expr.intLiteral();
    }
    
    public static $expr<IntegerLiteralExpr> intLiteral(Predicate<IntegerLiteralExpr> il){
        return $expr.intLiteral(il);
    }
        
    public static $expr<IntegerLiteralExpr> intLiteral(int val){
        return $expr.intLiteral(val);
    }
    
    public static $expr<LambdaExpr> lambda(){
        return $expr.lambda();
    }

    public static $expr<LambdaExpr> lambda(String lm){
        return $expr.lambda(lm);
    }

    public static $expr<LambdaExpr> lambda( LambdaExpr le){
        return $expr.of(le);
    }

    public static $expr<LambdaExpr> lambda(Predicate<LambdaExpr> le){
        return $expr.lambda(le);
    }
        
    public static $expr<LongLiteralExpr> longLiteral(){
        return $expr.longLiteral();
    }
    
    public static $expr<LongLiteralExpr> longLiteral(long lit){
        return $expr.longLiteral(lit);
    }
    
    public static $expr<LongLiteralExpr> longLiteral(Predicate<LongLiteralExpr> lle){
        return $expr.longLiteral(lle);
    }
    
    public static $expr<MethodCallExpr> methodCall(){
        return $expr.methodCall();
    }

    public static $expr<MethodCallExpr> methodCall(String mc){
        return $expr.methodCall(mc);
    }

    public static $expr<MethodCallExpr> methodCall(MethodCallExpr mce){
        return $expr.of(mce);
    }

    public static $expr<MethodCallExpr> methodCall(Predicate<MethodCallExpr> mce){
        return $expr.methodCall(mce);
    }
        
    public static $expr<MethodReferenceExpr> methodReference(){
        return $expr.methodReference();
    }

    public static $expr<MethodReferenceExpr> methodReference(String mr){
        return $expr.methodReference(mr);
    }

    public static $expr<MethodReferenceExpr> methodReference( MethodReferenceExpr mre){
        return $expr.of(mre);
    }

    public static $expr<MethodReferenceExpr> methodReference(Predicate<MethodReferenceExpr> mre){
        return $expr.methodReference(mre);
    }
    
    public static $expr<NameExpr> nameExpr(){
        return $expr.name();
    }

    public static $expr<NameExpr> nameExpr(String name){
        return $expr.name(name);
    }

    public static $expr<NameExpr> nameExpr( NameExpr ne){
        return $expr.of(ne);
    }

    public static $expr<NameExpr> nameExpr(Predicate<NameExpr> ne){
        return $expr.name(ne);
    }
        
    public static $expr<NullLiteralExpr> nullExpr(){
        return $expr.nullExpr();
    }

    public static $expr<NullLiteralExpr> nullExpr(NullLiteralExpr nle){
        return $expr.of(nle);
    }

    public static $expr<NullLiteralExpr> nullExpr(Predicate<NullLiteralExpr> nle){
        return $expr.nullExpr(nle);
    }
    
    public static $expr<ObjectCreationExpr> objectCreation(){
        return $expr.objectCreation();
    }

    public static $expr<ObjectCreationExpr> objectCreation(String oc){
        return $expr.objectCreation(oc);
    }

    public static $expr<ObjectCreationExpr> objectCreation( ObjectCreationExpr oce){
        return $expr.of(oce);
    }

    public static $expr<ObjectCreationExpr> objectCreation(Predicate<ObjectCreationExpr> oce){
        return $expr.objectCreation(oce);
    }
    
    public static $expr<StringLiteralExpr> stringLiteral(){
        return $expr.stringLiteral();
    }
    
    public static $expr<StringLiteralExpr> stringLiteral(String lit){
        return $expr.stringLiteral(lit);
    }

    public static $expr<StringLiteralExpr> stringLiteral(StringLiteralExpr sl){
        return $expr.of(sl);
    }

    public static $expr<StringLiteralExpr> stringLiteral(Predicate<StringLiteralExpr> sl){
        return $expr.stringLiteral(sl);
    }
        
    public static $expr<SuperExpr> superExpr(){
        return $expr.superExpr();
    }

    public static $expr<SuperExpr> superExpr(String se){
        return $expr.superExpr(se);
    }

    public static $expr<SuperExpr> superExpr(SuperExpr se){
        return $expr.superExpr(se);
    }

    public static $expr<SuperExpr> superExpr(Predicate<SuperExpr> se){
        return $expr.superExpr(se);
    }
    
    public static $expr<ThisExpr> thisExpr(){
        return $expr.thisExpr();
    }

    public static $expr<ThisExpr> thisExpr(String se){
        return $expr.thisExpr(se);
    }

    public static $expr<ThisExpr> thisExpr( ThisExpr te){
        return $expr.thisExpr(te);
    }

    public static $expr<ThisExpr> thisExpr(Predicate<ThisExpr> te){
        return $expr.thisExpr(te);
    }
    
    public static $expr<UnaryExpr> unary(){
        return $expr.unary();
    }

    public static $expr<UnaryExpr> unary(String ue){
        return $expr.unary(ue);
    }

    public static $expr<UnaryExpr> unary( UnaryExpr ue){
        return $expr.of(ue);
    }

    public static $expr<UnaryExpr> unary(Predicate<UnaryExpr> ue){
        return $expr.unary();
    }
    
    public static $expr<VariableDeclarationExpr> varDeclaration(){
        return $expr.varDecl();
    }

    public static $expr<VariableDeclarationExpr> varDeclaration(String ve){
        return $expr.varDecl(ve);
    }

    public static $expr<VariableDeclarationExpr> varDeclaration( VariableDeclarationExpr vde){
        return $expr.of(vde);
    }

    public static $expr<VariableDeclarationExpr> varDeclaration(Predicate<VariableDeclarationExpr> vde){
        return $expr.varDecl(vde);
    }
        
    public static $case switchCase(){
        return $case.any();
    }

    public static $case switchCase(String... sc){
        return $case.of(sc);
    }

    public static $case switchCase( SwitchEntry se){
        return $case.of(se);
    }

    public static $case switchCase(Predicate<SwitchEntry> se){
        return $case.any().addConstraint(se);
    }
    
    public static $catch catchClause(){
        return $catch.any();
    }

    public static $catch catchClause(String cc){
        return $catch.of(cc);
    }

    public static $catch catchClause(CatchClause cc ){
        return $catch.of(cc);
    }

    public static $catch catchClause(Predicate<CatchClause> cc ){
        return $catch.any().addConstraint(cc);
    }
        
    public static $field field(){
        return $field.any();
    }

    public static $field field(String fc){
        return $field.of(fc);
    }
    
    public static $field field(Predicate<_field> constraint){
        return $field.any().addConstraint(constraint);
    }

    public static $field field(_field f){
        return $field.of(f);
    }
    
    public static $import importStmt(){
        return $import.any();
    }

    public static $import importStmt(String pattern){
        return $import.of(pattern);
    }
    
    public static $import importStmt(Predicate<_import> constraint){
        return $import.any().addConstraint(constraint);
    }

    public static $import importStmt( _import imp){
        return $import.of(imp);
    }

    public static $modifiers modifiers(){
        return $modifiers.any();
    }

    public static $modifiers modifiers(String...ms){
        return $modifiers.of(ms);
    }
    
    public static $modifiers modifiers(Predicate<_modifiers> constraint){
        return $modifiers.any().addConstraint(constraint);
    }

    public static $modifiers modifiers(_modifiers ms){
        return $modifiers.of(ms);
    }

    public static $parameters parameters(){
        return $parameters.any();
    }

    public static $parameters parameters(String... parameters){
        return $parameters.of(parameters);
    }
    
    public static $parameters parameters(Predicate<_parameters> constraint){
        return $parameters.any().addConstraint(constraint);
    }
    
    public static $parameter parameter(){
        return $parameter.any();
    }

    public static $parameter parameter(String p){
        return $parameter.of(p);
    }

    public static $parameter parameter( _parameter p){
        return $parameter.of(p);
    }

    public static $parameter parameter(Predicate<_parameter> constraint){
        return $parameter.any().addConstraint(constraint);
    }
    
    public static $stmt stmt(){
        return $stmt.any();
    }

    public static $stmt stmt(String...pattern){
        return $stmt.of(pattern);
    }
    
    public static $stmt<AssertStmt> assertStmt(){
        return $stmt.assertStmt();
    }

    public static $stmt<AssertStmt> assertStmt(String...as){
        return $stmt.assertStmt(as);
    }

    public static $stmt<AssertStmt> assertStmt( AssertStmt as){
        return $stmt.of(as);
    }

    public static $stmt<AssertStmt> assertStmt(Predicate<AssertStmt> as){
        return $stmt.assertStmt().addConstraint(as);
    }
    
    public static $stmt<BlockStmt> blockStmt(){
        return $stmt.blockStmt();
    }

    public static $stmt<BlockStmt> blockStmt(String... bs){
        return $stmt.blockStmt(bs);
    }
    
    public static $stmt<BlockStmt> blockStmt(Predicate<BlockStmt> bs){
        return $stmt.blockStmt(bs);
    }

    public static $stmt<BlockStmt> blockStmt( BlockStmt bs){
        return $stmt.of(bs);
    }
    
    public static $stmt<BreakStmt> breakStmt(){
        return $stmt.breakStmt();
    }

    public static $stmt<BreakStmt> breakStmt(String...breakStmt){
        return $stmt.breakStmt(breakStmt);
    }

    public static $stmt<BreakStmt> breakStmt(BreakStmt bs){
        return $stmt.of(bs);
    }

    public static $stmt<BreakStmt> breakStmt(Predicate<BreakStmt> bs){
        return $stmt.breakStmt().addConstraint(bs);
    }
    
    public static $stmt<ExplicitConstructorInvocationStmt> constructorInvocationStmt(){
        return $stmt.ctorInvocationStmt();
    }

    public static $stmt<ExplicitConstructorInvocationStmt> constructorInvocationStmt(String...ctor){
        return $stmt.ctorInvocationStmt(ctor);
    }

    public static $stmt<ExplicitConstructorInvocationStmt> constructorInvocationStmt( ExplicitConstructorInvocationStmt cs){
        return $stmt.of(cs);
    }

    public static $stmt<ExplicitConstructorInvocationStmt> constructorInvocationStmt(Predicate<ExplicitConstructorInvocationStmt> cs){
        return $stmt.ctorInvocationStmt(cs);
    }
    
    public static $stmt<ContinueStmt> continueStmt(){
        return $stmt.continueStmt();
    }

    public static $stmt<ContinueStmt> continueStmt(String...continueStmt){
        return $stmt.continueStmt(continueStmt);
    }

    public static $stmt<ContinueStmt> continueStmt(ContinueStmt cs){
        return $stmt.of(cs);
    }

    public static $stmt<ContinueStmt> continueStmt(Predicate<ContinueStmt> cs){
        return $stmt.continueStmt(cs);
    }
        
    public static $stmt<DoStmt> doStmt(){
        return $stmt.doStmt();
    }

    public static $stmt<DoStmt> doStmt(String...doStmt){
        return $stmt.doStmt(doStmt);
    }
    
    public static $stmt<DoStmt> doStmt(Predicate<DoStmt> ds){
        return $stmt.doStmt(ds);
    }

    public static $stmt<DoStmt> doStmt(DoStmt ds){
        return $stmt.doStmt(ds);
    }
    
    public static $stmt<EmptyStmt> emptyStmt(){
        return $stmt.emptyStmt();
    }
    
    public static $stmt<ExpressionStmt> expressionStmt(){
        return $stmt.expressionStmt();
    }

    public static $stmt<ExpressionStmt> expressionStmt(String ex){
        return $stmt.expressionStmt(ex);
    }

    public static $stmt<ExpressionStmt> expressionStmt( ExpressionStmt es){
        return $stmt.expressionStmt(es);
    }

    public static $stmt<ExpressionStmt> expressionStmt(Predicate<ExpressionStmt> es){
        return $stmt.expressionStmt(es);
    }
    
    public static $stmt<ForStmt> forStmt(){
        return $stmt.forStmt();
    }

    public static $stmt<ForStmt> forStmt(String...fs){
        return $stmt.forStmt(fs);
    }

    public static $stmt<ForStmt> forStmt(ForStmt fs){
        return $stmt.of(fs);
    }

    public static $stmt<ForStmt> forStmt(Predicate<ForStmt> fs){
        return $stmt.forStmt().addConstraint(fs);
    }
    
    public static $stmt<ForEachStmt> forEachStmt(){
        return $stmt.forEachStmt();
    }

    public static $stmt<ForEachStmt> forEachStmt(String forEachStmt){
        return $stmt.forEachStmt(forEachStmt);
    }

    public static $stmt<ForEachStmt> forEachStmt(ForEachStmt fes){
        return $stmt.forEachStmt(fes);
    }

    public static $stmt<ForEachStmt> forEachStmt(Predicate<ForEachStmt> fes){
        return $stmt.forEachStmt(fes);
    }
    
    public static $stmt<IfStmt> ifStmt(){
        return $stmt.ifStmt();
    }

    public static $stmt<IfStmt> ifStmt(String...is){
        return $stmt.ifStmt(is);
    }
    
    public static $stmt<IfStmt> ifStmt(Predicate<IfStmt> is){
        return $stmt.ifStmt(is);
    }

    public static $stmt<IfStmt> ifStmt( IfStmt is){
        return $stmt.ifStmt(is);
    }
    
    public static $stmt<LabeledStmt> labeledStmt(){
        return $stmt.labeledStmt();
    }

    public static $stmt<LabeledStmt> labeledStmt(String ls){
        return $stmt.labeledStmt(ls);
    }
    
    public static $stmt<LabeledStmt> labeledStmt(Predicate<LabeledStmt> ls){
        return $stmt.labeledStmt(ls);
    }

    public static $stmt<LabeledStmt> labeledStmt(LabeledStmt ls){
        return $stmt.labeledStmt(ls);
    }
    
    public static $stmt<LocalClassDeclarationStmt> localClassStmt(){
        return $stmt.localClassStmt();
    }

    public static $stmt<LocalClassDeclarationStmt> localClassStmt(String... str){
        return $stmt.localClassStmt(str);
    }

    public static $stmt<LocalClassDeclarationStmt> localClassStmt( LocalClassDeclarationStmt lcds){
        return $stmt.localClassStmt(lcds);
    }

    public static $stmt<LocalClassDeclarationStmt> localClassStmt(Predicate<LocalClassDeclarationStmt> lcds){
        return $stmt.localClassStmt().addConstraint(lcds);
    }
    
    public static $stmt<SwitchStmt> switchStmt(){
        return $stmt.switchStmt();
    }

    public static $stmt<SwitchStmt> switchStmt(String ... ss){
        return $stmt.switchStmt(ss);
    }
    
    public static $stmt<SwitchStmt> switchStmt(Predicate<SwitchStmt> ss){
        return $stmt.switchStmt(ss);
    }

    public static $stmt<SwitchStmt> switchStmt(SwitchStmt ss){
        return $stmt.switchStmt(ss);
    }

    public static $stmt<SynchronizedStmt> synchronizedStmt() { return $stmt.synchronizedStmt(); }

    public static $stmt<SynchronizedStmt> synchronizedStmt(String ... ss){
        return $stmt.synchronizedStmt(ss);
    }

    public static $stmt<SynchronizedStmt> synchronizedStmt(Predicate<SynchronizedStmt> ss){
        return $stmt.synchronizedStmt(ss);
    }

    public static $stmt<SynchronizedStmt> synchronizedStmt(SynchronizedStmt ss){
        return ($stmt<SynchronizedStmt>)$stmt.of(ss);
    }

    public static $stmt<ThrowStmt> throwStmt(){
        return $stmt.throwStmt();
    }

    public static $stmt<ThrowStmt> throwStmt(String... ts){
        return $stmt.throwStmt(ts);
    }

    public static $stmt<ThrowStmt> throwStmt(ThrowStmt ts){
        return $stmt.throwStmt(ts);
    }

    public static $stmt<ThrowStmt> throwStmt(Predicate<ThrowStmt> ts){
        return $stmt.throwStmt(ts);
    }
    
    public static $stmt<TryStmt> tryStmt(){
        return $stmt.tryStmt();
    }

    public static $stmt<TryStmt> tryStmt(String...tryStmt){
        return $stmt.tryStmt(tryStmt);
    }

    public static $stmt<TryStmt> tryStmt(TryStmt ts){
        return $stmt.tryStmt(ts);
    }

    public static $stmt<TryStmt> tryStmt(Predicate<TryStmt> ts){
        return $stmt.tryStmt(ts);
    }
    
    public static $stmt<WhileStmt> whileStmt(){
        return $stmt.whileStmt();
    }

    public static $stmt<WhileStmt> whileStmt(String...whileStmt){
        return $stmt.whileStmt(whileStmt);
    }

    public static $stmt<WhileStmt> whileStmt(WhileStmt ws){
        return $stmt.whileStmt(ws);
    }

    public static $stmt<WhileStmt> whileStmt(Predicate<WhileStmt> ws){
        return $stmt.whileStmt(ws);
    }
    
    public static $throws thrown(){
        return $throws.any();
    }

    public static $throws thrown(String...th){
        return $throws.of(th);
    }

    public static $throws thrown(_throws ts){
        return $throws.of(ts);
    }

    public static $throws thrown(Predicate<_throws> ts){
        return $throws.any().addConstraint( ts);
    }
    
    public static $typeParameter typeParameter(){
        return $typeParameter.any();
    }

    public static $typeParameter typeParameter(String tp){
        return $typeParameter.of(tp);
    }

    public static $typeParameter typeParameter(_typeParameter tp){
        return $typeParameter.of(tp);
    }

    public static $typeParameter typeParameter(Predicate<_typeParameter> tp){
        return $typeParameter.any().addConstraint(tp);
    }
    
    public static $typeParameters typeParameters(){
        return $typeParameters.any();
    }

    public static $typeParameters typeParameters(String...tps){
        return $typeParameters.of(tps);
    }

    public static $typeParameters typeParameters(_typeParameters tps){
        return $typeParameters.of(tps);
    }

    public static $typeParameters typeParameters(Predicate<_typeParameters> tps){
        return $typeParameters.any().addConstraint(tps);
    }
    
    public static $typeRef typeRef(){
        return $typeRef.any();
    }

    public static $typeRef typeRef(String tr){
        return $typeRef.of(tr);
    }

    public static $typeRef typeRef(Type tr){
        return $typeRef.of(tr);
    }

    public static $typeRef typeRef(Predicate<_typeRef> tr){
        return $typeRef.any().addConstraint(tr);
    }
    
    public static $var var(){
        return $var.any();
    }

    public static $var var(String var){
        return $var.of(var);
    }

    public static $var var(VariableDeclarator var){
        return $var.of(var);
    }
    
    public static $var var(Predicate<VariableDeclarator> vd){
        return $var.any().addConstraint(vd);
    }
}
