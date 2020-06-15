package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * _tree._view of the grouping of all _annoEntryPairs on an _anno
 * <PRE>
 * @Anno(k=1, val="String", count=4)
 *       -------------------------- k=1, val="String", count=4 (3) {@link _annoEntryPair}s
 * </PRE>
 *
 * transient translator object
 * (Im not much for Patterns, but this is a good explanation of why this Object exists)
 * <A HREF="https://wiki.c2.com/?TranslatorPattern">Translator Pattern</A>
 */
public class _annoEntryPairs implements _tree._view<_annoEntryPairs>,
        _tree._group<MemberValuePair, _annoEntryPair, _annoEntryPairs> {

    public static _annoEntryPairs of( ){
        return new _annoEntryPairs(new MarkerAnnotationExpr("Empty"));
    }

    public static _annoEntryPairs of(String...code ){
        return PARSER.apply(Text.combine(code));
    }

    public static _annoEntryPairs of( AnnotationExpr parentAnno ){
        return new _annoEntryPairs(parentAnno);
    }

    /** The parent AST node that is interpreted*/
    public AnnotationExpr annoParentNode;

    public static final Function<String,_annoEntryPairs> PARSER = (s)->{
        String full = "@UNKNOWN("+s+")";
        _anno _a = _anno.of(full);
        return of( _a.ast());
    };

    public _feature._features<_annoEntryPairs> features(){
        return FEATURES;
    }

    public static _feature._many<_annoEntryPairs, _annoEntryPair> ANNO_ENTRY_PAIRS =
            new _feature._many<>(_annoEntryPairs.class, _annoEntryPair.class,
            _feature._id.ENTRY_PAIRS, _feature._id.ENTRY_PAIR,
            as->as.list(),
            (_annoEntryPairs as, List<_annoEntryPair> anns)-> as.set(anns),
            PARSER,
            s-> _annoEntryPair.of(s))
            .setOrdered(false); /* the order of the annos isnt semantically important {@A @B @C === @B @A @C} */

    public static _feature._features<_annoEntryPairs> FEATURES = _feature._features.of(_annoEntryPairs.class,  PARSER, ANNO_ENTRY_PAIRS);

    private _annoEntryPairs(AnnotationExpr annoParentNode){
        this.annoParentNode = annoParentNode;
    }

    @Override
    public _annoEntryPairs copy() {
        return new _annoEntryPairs( this.annoParentNode);
    }

    public boolean isValueOnly(){
        return this.annoParentNode instanceof SingleMemberAnnotationExpr;
    }

    @Override
    public List<_annoEntryPair> list() {
        if( this.annoParentNode.isSingleMemberAnnotationExpr()){
            SingleMemberAnnotationExpr sm = this.annoParentNode.asSingleMemberAnnotationExpr();
            List<_annoEntryPair> lae = new ArrayList<>();
            lae.add( _annoEntryPair.of(sm) );
            return lae;
        }
        if( this.annoParentNode.isNormalAnnotationExpr() ){
            return this.annoParentNode.asNormalAnnotationExpr().getPairs().stream().map( ae-> _annoEntryPair.of(ae)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public NodeList<MemberValuePair> listAstElements() {
        if( this.annoParentNode.isSingleMemberAnnotationExpr()){
            SingleMemberAnnotationExpr sm = this.annoParentNode.asSingleMemberAnnotationExpr();
            NodeList<MemberValuePair> mvps = new NodeList<>();
            mvps.add( new MemberValuePair("value", sm.getMemberValue()));
            return mvps;
        }
        if( this.annoParentNode.isNormalAnnotationExpr() ){
            return this.annoParentNode.asNormalAnnotationExpr().getPairs();
        }
        return new NodeList<>();
    }

    @Override
    public String toString(PrettyPrinterConfiguration prettyPrinter) {
        if( this.annoParentNode.isSingleMemberAnnotationExpr()){
            SingleMemberAnnotationExpr sm = this.annoParentNode.asSingleMemberAnnotationExpr();
            return "("+sm.asSingleMemberAnnotationExpr().getMemberValue().toString(prettyPrinter)+")";
        }
        if( this.annoParentNode.isNormalAnnotationExpr() ){
            NodeList<MemberValuePair> mvps = this.annoParentNode.asNormalAnnotationExpr().getPairs();
            StringBuilder sb = new StringBuilder();
            sb.append("(");

            for(int i=0;i<mvps.size(); i++){
                sb.append( mvps.get(i).toString(prettyPrinter) );
            }
            sb.append(")");
            return sb.toString();
        }
        return "";
    }

    @Override
    public boolean is(String code) {
        return is( new String[]{code});
    }

    public boolean is(String...code){
         String all = Text.combine(code);
         Stencil st = Stencil.of(all);
         if( st.isMatchAny() ){
             return true;
         }
         if( st.isFixedText() ){
             _annoEntryPairs _aeps = of(all);
             return equals(_aeps);
         }
        _annoEntryPairs _aeps = of(all);
        return _aeps.list().stream().allMatch( a-> {
            return list().stream().anyMatch(_a -> _a.is(a.toString(Print.PRINT_NO_COMMENTS)) );
            //return this.has(a.toString(Print.PRINT_NO_COMMENTS));
            //TODO what about if it's a parameter?
        });
    }

    /**
     * Is this a SingleMemberAnnotationExpr with the _expression _e
     * @param _e
     * @return
     */
    public boolean is(_expr _e){
        return( isValueOnly() && getAt(0).isValue(_e) );
    }

    public boolean is(Predicate<_expr> e){
        return (isValueOnly() && getAt(0).isValue(e));
    }

    public <_IE extends _expr> boolean is(Class<_IE> implClass, Predicate<_IE> _matchFn){
        return (isValueOnly() && getAt(0).isValue(implClass, _matchFn));
    }

    public boolean has(String...annoEntryPair ){
        return list().stream().anyMatch(_a -> _a.is(annoEntryPair));
    }

    public boolean equals(Object o){
        if( o instanceof _annoEntryPairs ){
            _annoEntryPairs _o = (_annoEntryPairs)o;
            return this.size() == _o.size() && first( e-> !_o.has(e)) == null;
        }
        return false;
    }

    public int hashCode(){
        if(this.annoParentNode.isSingleMemberAnnotationExpr() ){
            return this.annoParentNode.asSingleMemberAnnotationExpr().getMemberValue().hashCode() * 31;
        } else if( this.annoParentNode.isNormalAnnotationExpr() ){
            Set<MemberValuePair> mvps = new HashSet<>();
            mvps.addAll(this.annoParentNode.asNormalAnnotationExpr().getPairs() );
            return mvps.hashCode() * 31;
        } else{
            return 31;
        }
    }
}
