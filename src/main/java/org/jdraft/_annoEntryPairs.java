package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
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
import java.util.stream.Stream;

/**
 * _tree._view of the grouping of all _annoEntryPairs on an _anno
 * <PRE>
 * @Anno(k=1, val="String", count=4)
 *       -------------------------- "k=1, val="String", count=4" (3) {@link _annoEntryPair}s
 * </PRE>
 */
public class _annoEntryPairs implements _tree._view<_annoEntryPairs>,
        _tree._group<MemberValuePair, _annoEntryPair, _annoEntryPairs> {

    public static _annoEntryPairs of( ){
        return new _annoEntryPairs(new MarkerAnnotationExpr("Empty"));
    }

    public static _annoEntryPairs of(String...code ){
        return PARSER.apply(Text.combine(code));
    }

    public static _annoEntryPairs of( MemberValuePair... mvps){
        NormalAnnotationExpr nae = new NormalAnnotationExpr(new Name("UNKNOWN"), new NodeList<>());
        NodeList<MemberValuePair> mvs = new NodeList<>();
        Stream.of(mvps).forEach(m ->mvs.add(m));
        nae.setPairs(mvs);
        return new _annoEntryPairs(nae);
    }

    public static _annoEntryPairs of( _annoEntryPair... ps){
        if( ps.length == 0 ){
            return _annoEntryPairs.of();
        }
        if( ps.length == 1 && ps[0].isValueOnly() ){
            return new _annoEntryPairs( new SingleMemberAnnotationExpr(new Name("UNKNOWN"), ps[0].getValue().node()));
        }
        NormalAnnotationExpr nae = new NormalAnnotationExpr(new Name("UNKNOWN"), new NodeList<>());
        NodeList<MemberValuePair> mvs = new NodeList<>();
        Stream.of(ps).forEach(m ->mvs.add(m.node()));
        nae.setPairs(mvs);
        return new _annoEntryPairs(nae);
    }

    public static _annoEntryPairs of( AnnotationExpr parentAnno ){
        return new _annoEntryPairs(parentAnno);
    }

    /** The parent AST node that is interpreted*/
    public AnnotationExpr parentNode;

    public AnnotationExpr anchorNode(){
        return parentNode;
    }

    public static final Function<String,_annoEntryPairs> PARSER = (s)->{
        String full = "@UNKNOWN("+s+")";
        _anno _a = _anno.of(full);
        return of( _a.node());
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

    private _annoEntryPairs(AnnotationExpr parentNode){
        this.parentNode = parentNode;
    }

    @Override
    public _annoEntryPairs copy() {
        return new _annoEntryPairs( this.parentNode);
    }

    public boolean isValueOnly(){
        return this.parentNode instanceof SingleMemberAnnotationExpr;
    }

    @Override
    public List<_annoEntryPair> list() {
        if( this.parentNode.isSingleMemberAnnotationExpr()){
            SingleMemberAnnotationExpr sm = this.parentNode.asSingleMemberAnnotationExpr();
            List<_annoEntryPair> lae = new ArrayList<>();
            lae.add( _annoEntryPair.of(sm) );
            return lae;
        }
        if( this.parentNode.isNormalAnnotationExpr() ){
            return this.parentNode.asNormalAnnotationExpr().getPairs().stream().map(ae-> _annoEntryPair.of(ae)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public NodeList<MemberValuePair> astList() {
        if( this.parentNode.isSingleMemberAnnotationExpr()){
            SingleMemberAnnotationExpr sm = this.parentNode.asSingleMemberAnnotationExpr();
            NodeList<MemberValuePair> mvps = new NodeList<>();
            mvps.add( new MemberValuePair("value", sm.getMemberValue()));
            return mvps;
        }
        if( this.parentNode.isNormalAnnotationExpr() ){
            return this.parentNode.asNormalAnnotationExpr().getPairs();
        }
        return new NodeList<>();
    }

    @Override
    public String toString(PrettyPrinterConfiguration prettyPrinter) {
        if( this.parentNode.isSingleMemberAnnotationExpr()){
            SingleMemberAnnotationExpr sm = this.parentNode.asSingleMemberAnnotationExpr();
            return "("+sm.asSingleMemberAnnotationExpr().getMemberValue().toString(prettyPrinter)+")";
        }
        if( this.parentNode.isNormalAnnotationExpr() ){
            NodeList<MemberValuePair> mvps = this.parentNode.asNormalAnnotationExpr().getPairs();
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
        if(this.parentNode.isSingleMemberAnnotationExpr() ){
            return this.parentNode.asSingleMemberAnnotationExpr().getMemberValue().hashCode() * 31;
        } else if( this.parentNode.isNormalAnnotationExpr() ){
            Set<MemberValuePair> mvps = new HashSet<>();
            mvps.addAll(this.parentNode.asNormalAnnotationExpr().getPairs() );
            return mvps.hashCode() * 31;
        } else{
            return 31;
        }
    }
}
