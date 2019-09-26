/**
 * <P>Bottom up Prototypes that can be used for constructing, querying, removing
 * intercepting, and replacing code.</ P>
 * 
 * While the core draft API has the tools to define a proper Data Definition Language 
 * <PRE>
 * Data Definition Language(DDL) 
 * (CREATE, DROP, TRUNCATE, COMMENT, RENAME)
 * </PRE>
 * 
 * NOTE: the "p_" prefix identifies prototype classes, which gives a nice
 * visual mnemonic for identifying and differentiating traditional "code" from
 * prototype code.
 * 
 * <P>draft.java.proto provides an additional API that provides tools similar to
 * a Data Manipulation Language:</P>
 * 
 * <P>Data Manipulation Language (DML)
 * (SELECT, INSERT, UPDATE, DELETE)</P>
 * 
 * <P>Unlike Top-Down (DML) Data Manipulation Languages (like SQL) start with the 
 * premise that data is structured in a hierarchial top down fashion 
 * (schemas, tables, columns, rows)</P>
 *
 * (i.e. a "top down query" in SQL
 * <PRE>
 * SELECT * 
 * FROM SCHEMA.TABLE <-- Top Level
 * WHERE ROW = 1     <-- Detail
 * </PRE>
 * 
 * <P>Prototypes are a "bottom up" way of representing patterns in the 
 * code, since the code is more a "network of nodes" than a purely top down 
 * structure. (i.e. the code hierarchy has indeterminate depth).</P>  
 * <PRE>
 * 
 * //Querying and modifying existing code
 * _class _c = _class.of(SomeClass.class);
 * 
 * //we can directly QUERY into the class in a single static method call
 * 
 * // return a list of all println Statements
 * List<Statement> printlns = p_stmt.list(_c, "System.out.println($any$);");
 * 
 * // prints out each return statement
 * p_stmt.forEach(_c, "return $any$;", s -> System.out.println( s ));
 * 
 * //gets the first ReturnSt
 * ReturnStmt rs = p_stmt.first(_c, Ast.RETURN_STMT)
 * 
 * //or you can build a prototype instance (like a stored procedure) that
 * // can be invoked when needed
 * 
 * //With Prototypes, we start bottom up (here a Statement)
 * p_stmt $println =  p_stmt.of("System.out.println($any$);");
 * 
 * // the prototype will "walk" the code and SELECT, DELETE or REPLACE 
 * // the code as it is walked
 * 
 * //SELECT list any System.out.println methods that occur in SomeClass.class
 * $println.listIn(_c); 
 * 
 * //DELETE remove any System out println calls in the SomeClass.class
 * $println.removeIn(_c);
 * 
 * //UPDATE replace all System.out.println with LOG.debug statements
 * $println.replaceIn(_c, "LOG.debug($any$);");
 * </PRE>
 */
package org.jdraft.pattern;
