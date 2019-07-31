/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.subpkg;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import junit.framework.TestCase;

/**
 * // should I have the ability to do simple queries through the 
     * // project:
     * 
     * at(type[*])      //all types
     * at(class[*])     //all classes
     * at(class[Scope]) //all classes named Scope   
     * 
     * at type *
     * at class *
     * at class *
     * at Scope everything named Scope
     * 
     * on t *
     * on t Scope
     * on t 
     * @T Scope
     * @C Scope
     * @E 
     * @F 
     * @CU ClassUse
     * 
     * @T*
     * @C*
     * @T SCOPE
     * 
     * 
     * at(field[*])     //all fields
     * at(field[count]) //all fields named count
     * at(method[*])    //all methods
     * at(method[get*()]) //all get methods
     * at(
     * 
     * 
     * on(type[*])      //all members directly on a type
     * on(class[*])     //all members directly on the a class (i.e. not enum, interface or annotation members)
     * 
     * in(type[*])      //all types and all members
     * in(class[*])     //all class members
     * 



@  
at
on  
in



any name
"name"

t ype
ts
c lass
co nstant
cs 
e num
es
i nterface
is interfaces

a  (Both annotation and Annotation use)
as
m  method
ms methods
mo modifier
mos modifiers
th throw
ths throws
r receiver parameter
s staticblock
ss 
p aram
pa ram
pk package
v ararg
n ame
ne st
it   init
init fieldInit
ins  field inits
im port
ims imports
* 
 */
public class TestUrlEncode extends TestCase {
    
    public static String getQueryParams(String proxyRequestParam) 
            throws UnsupportedEncodingException{
        
        return URLDecoder.decode(proxyRequestParam.replace("+", "%2B"), "UTF-8")
          .replace("%2B", "+");
    }
    
    //something?alias=pos&FirstName=Foo+A%26B%3DC&LastName=Bar 
    //URLDecoder.decode(proxyRequestParam.replace("+", "%2B"), "UTF-8").replace("%2B", "+")
    public void testA() throws UnsupportedEncodingException{
        System.out.println( getQueryParams("?alias=pos&FirstName=Foo+A%26B%3DC&LastName=Bar") );
        
    }
}
