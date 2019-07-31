/**
 * draft defines a new representation for program source code.  
 * Normally, the programmer: 
 * <OL>
 *  <LI>creates/modifies the source code as a plain-text file(s) through an IDE
 *  <LI>passes the plain text files to the compiler
 *  <LI>the compiler outputs a binary (bytecode/ etc.) for a machine or VM to run
 * </OL>
 * For example: 
 * to create a new Java class "Hello" in the "my.proj" package,
 * <OL>
 * <LI> we'd create a new file named "Hello.java" in the "my.proj" directory
 * <LI> in plain text write out the file:
 * <PRE>
 * package my.proj;
 * 
 * public class Hello{
 * }
 * </PRE>
 * 
 * <LI> call the javac command to produce the "my.proj.Hello.class" file
 * </OL>
 * 
 *<PRE> 
 * you
 *   |---------->---------->
 * source    compiler    binary
 *</PRE>
 * 
 * <P>with draft, the developer gets a new entry point into source code... 
 * In addition to being able to manipulate the code as plain text in files, 
 * the source code can be created or loaded as a draft domain object and 
 * directly manipulated.</P>  
 * 
 * <P>with draft, you can create a new _class instance representing
 * the source code for "my.proj.Hello" by calling a method:</P>
 * 
 * <PRE>_class _c = _class.of("my.proj.Hello");</PRE>
 * 
 * <P>..the draft domain object representing the source does NOT exist in the 
 * fileSystem (we CAN export it there easily)</P>
 * <PRE>
 * _io.out("C:\\temp\\", _c); // will export" "C:\\temp\\my\\proj\\Hello.java")
 * 
 * //we can convert the _c instance into a String, write it to System.out
 * System.out.println( _c );
 * </PRE>
 *
 * <P>...but, more importantly we can manipulate it (add fields, methods, etc)</P>
 * 
 * <PRE>
 * //add main method
 * _c.main("System.out.println(1);"); 
 * //add field
 * _c.field("public static final int ID = 102;");
 * //set the class to be final
 * _c.setFinal();
 * <PRE> 
 * <P>...underlying the _class domain object is actually an AST, we can directly
 * access it  and (optionally modify it) from the </P>
 * <PRE>
 * //get the JavaParser AST implementation from the _c
 * ClassOrInterfaceDeclaration astCoid = _c.astClass();
 * astCoid.addImplement( Serializable.class)
 * </PRE>
 * 
 * ...and we can compile and use the _c directly by the model
 * <PRE>
 * // compile and load the _c domain object, then call the main method 
 * _project.of(_c).main();
 * </PRE>
 * 
 * <PRE> 
 * you__________
 *   |          |
 *   |    ----draft---------------- 
 *   |   /      |       \          \
 *   |  /------AST       \          \
 *   | /        |         \          \
 *   |---------->---------->---------->
 * .java     [javac]     .class     [JVM]
 * </PRE>
 */
package org.jdraft;
