package org.jdraft.io;

import com.github.javaparser.JavaParser;
import org.jdraft._codeUnit;
import org.jdraft._codeUnits;
import org.jdraft.text.Stencil;
import org.jdraft.text.Tokens;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;

/**
 * resolves from github project source code
 * "https://raw.githubusercontent.com/org-jdraft/jdraft/master/src/main/java/org/jdraft/Ast.java"
 *
 */
public class _githubProject implements _batch {

    public static final Stencil PROJECT_HOME_URL = Stencil.of( "https://github.com/$owner$/$project$/" );
    public static final Stencil VIEW_JAVA_FILE_URL = Stencil.of( "https://github.com/$owner$/$project$/blob/$branch$/$pathToJavaFile$.java");
    //public static final Stencil VIEW_DIRECTORY_URL = Stencil.of( "https://github.com/$owner$/$project$/tree/$branch$/$directoryPath$");
    public static final Stencil DOWNLOAD_JAVA_FILE_URL = Stencil.of("https://raw.githubusercontent.com/$owner$/$project$/$branch$/$pathToJavaFile$.java");
    //public static final String RAW_DOWNLOAD_BASE_URL = "https://raw.githubusercontent.com/";

    public static final Stencil DOWNLOAD_PROJECT_ZIP_URL = Stencil.of("https://github.com/$owner$/$project$/archive/$branch$.zip");

    /**
     * ex.
     * _githubProject.of("https://github.com/org-jdraft/jdraft/")
     *     .setBranch("1024").downloadProjectZip();
     *
     * @param projectUrl i.e. "https://github.com/org-jdraft/jdraft/"
     * @return a _gitHubRepo object representing the url
     */
    public static _githubProject of(String projectUrl){

        Tokens ts = null;
        if( projectUrl.endsWith("/") ){
            ts = PROJECT_HOME_URL.parse(projectUrl);
        } else {
            ts = PROJECT_HOME_URL.parse(projectUrl+"/");
        }
        //try VIEW_FILE_URL
        if( ts != null ){
            return new _githubProject((String)ts.get("owner"), (String)ts.get("project"));
        }
        ts = DOWNLOAD_PROJECT_ZIP_URL.parse(projectUrl);
        if( ts != null ){
            return  new _githubProject( (String)ts.get("owner"), (String)ts.get("project")).setBranch((String)ts.get("branch"));
        }
        throw new _ioException("unable to extract github owner and project name from url "+ projectUrl);
    }

    //String owner, name
    public static _githubProject of(String owner, String name ){
        return new _githubProject( owner, name);
    }

    public static _githubProject of(String owner, String name, String branch){
        return new _githubProject( owner, name).setBranch(branch);
    }

    public _projectDetails projectDetail;

    public _githubProject(String owner, String name ){
        projectDetail = new _projectDetails();
        projectDetail.owner = owner;
        projectDetail.name = name;
    }

    public static class _projectDetails {

        public String owner; /** "org-jdraft" */
        public String name; /** "jdraft" */

        /** by default "master" branch */
        public String branch = "master";

        /** by default "src/main/java" source root */
        public String sourceRoot = "src/main/java/";
    }

    /**
     * set the branch (default is "master")
     * @param branch
     * @return
     */
    public _githubProject setBranch(String branch ){
        this.projectDetail.branch = branch;
        return this;
    }

    /**
     * the source root i.e.
     * "src/main/java" in "https://raw.githubusercontent.com/org-jdraft/jdraft/master/src/main/java/org/jdraft/Ast.java"
     *
     * @param sourceRoot
     * @return
     */
    public _githubProject setSourceRoot(String sourceRoot ){
        this.projectDetail.sourceRoot = sourceRoot;
        return this;
    }

    @Override
    public _codeUnits load(JavaParser javaParser) {
        URL downloadUrl = downloadProjectZipURL();
        _codeUnits _cus = new _codeUnits();
        _downloadArchiveConsumer.of( downloadUrl, (ZipEntry ze, InputStream is)-> {
            if (ze.getName().endsWith(".java")) {
                try {
                    _cus.add( _codeUnit.of(javaParser, is) );
                }catch(Exception e){
                    System.err.println( "Couldn't parse zip entry \""+ ze.getName()+"\"");
                }
            }
        });
        return _cus;
    }

    public URL downloadProjectZipURL( ) {
        return downloadProjectZipURL(this.projectDetail.branch);
    }

    public URL downloadProjectZipURL(String branch) {
        //build the URL from the Project zip
        String url = DOWNLOAD_PROJECT_ZIP_URL.draft(
                "owner", this.projectDetail.owner,
                "project", this.projectDetail.name,
                "branch", branch);
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new _ioException("could not build build URL from \""+ url+"\"");
        }
    }

    public URL downloadJavaFileURL(Class clazz) {
        //is inner class, etc.
        return downloadJavaFileURL(clazz.getCanonicalName() );
    }

    /**
     * Given the qualified name of a Java type:
     * i.e. "java.util.Map" //without .java extension
     * or "java.util.Map.java" //with .java source file extension
     *
     * Creates the URL to download this source file within the github repository
     * i.e.
     * "https://raw.githubusercontent.com/org-jdraft/jdraft/master/src/main/java/org/jdraft/Ast.java"
     *
     * @param qualifiedName qualifiedName (i.e. "java.util.Map", "java.util.Map.java")
     * @return
     */
    public URL downloadJavaFileURL(String qualifiedName ) {
        return downloadJavaFileURL(projectDetail.sourceRoot, qualifiedName );
    }

    /**
     * Given the qualified name of a Java type:
     * i.e. "java.util.Map" //without .java extension
     * or "java.util.Map.java" //with .java source file extension
     *
     * Creates the URL to download this source file within the github repository
     * i.e.
     * "https://raw.githubusercontent.com/org-jdraft/jdraft/master/src/main/java/org/jdraft/Ast.java"
     *
     * @param qualifiedName qualifiedName (i.e. "java.util.Map", "java.util.Map.java")
     * @return
     */
    public URL downloadJavaFileURL(String sourceRoot, String qualifiedName ) {
        return downloadJavaFileURL(projectDetail.branch, sourceRoot, qualifiedName );
    }

    /**
     * Given the qualified name of a Java type:
     * i.e. "java.util.Map" //without .java extension
     * or "java.util.Map.java" //with .java source file extension
     *
     * Creates the URL to download this source file within the github repository
     * i.e.
     * "https://raw.githubusercontent.com/org-jdraft/jdraft/master/src/main/java/org/jdraft/Ast.java"
     *
     * @param qualifiedName qualifiedName (i.e. "java.util.Map", "java.util.Map.java")
     * @return
     */
    public URL downloadJavaFileURL(String branch, String sourceRoot, String qualifiedName ) {
        return buildURL(DOWNLOAD_JAVA_FILE_URL, this.projectDetail, branch, sourceRoot, qualifiedName);
    }

    /**
     * Given the qualified name of a Java type:
     * i.e. "java.util.Map" //without .java extension
     * or "java.util.Map.java" //with .java source file extension
     *
     * Creates the URL to view this source file on the github repository
     * i.e.
     * "https://raw.githubusercontent.com/org-jdraft/jdraft/master/src/main/java/org/jdraft/Ast.java"
     *
     * @param qualifiedName qualifiedName (i.e. "java.util.Map", "java.util.Map.java")
     * @return
     */
    public URL javaSourceFileViewURL(String branch, String sourceRoot, String qualifiedName ) {
        return buildURL(VIEW_JAVA_FILE_URL, this.projectDetail, branch, sourceRoot, qualifiedName);
    }

    private static URL buildURL(Stencil stencil, _projectDetails projectDetail, String branch, String sourceRoot, String qualifiedName){
        String pathToJavaFile = qualifiedName;
        if( pathToJavaFile.endsWith(".java") ){
            pathToJavaFile = pathToJavaFile.substring(0, pathToJavaFile.length() - ".java".length());
        }
        pathToJavaFile = pathToJavaFile.replace('.', '/');
        Tokens ts = Tokens.of("owner", projectDetail.owner,
                "project", projectDetail.name,
                "branch", branch,
                "pathToJavaFile", sourceRoot + pathToJavaFile);
        String url = stencil.draft(ts);
        try {
            return new URL( url );
        }catch(MalformedURLException mue){
            throw new _ioException("Unable to build url \""+url+"\"");
        }
    }
}
