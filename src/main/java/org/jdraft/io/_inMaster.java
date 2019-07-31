package org.jdraft.io;

/**
 * A master input router...
 *
 * tries to accomplish:
 * 1) good defaults (should work out of the box for most projects)
 * 2) easily configurable out of the box (swap out via Thread local or system properties)
 *
 * ..when resolving
 * 1) check threadLocal to see if there is a resolver
 *     1b) if Class (check in memory via _classLoader)
 * 2) check System properties for In Projects
 * 3) check System properties for In FilePaths
 * 4) check in local classpath
 * 5) check In localProject with (user.dir) property
 *
 *
 * @author Eric
 */
public final class _inMaster implements _in._resolver {

    public static final _inMaster INSTANCE = new _inMaster();

    private _inMaster(){
    }

    private _inMaster( _io._inConfig ic ){

    }

    /** ThreadLocalResolver, we check this for a resolver EACH TIME*/
    private static final ThreadLocal<_in._resolver> THREAD_LOCAL_RESOLVER =
            new ThreadLocal<_in._resolver>() {};

    public static final _inProject IN_CURRENT_PROJECT = new _inProject();

    // Returns the _in.resolver from ThreadLocal storage
    public static _in._resolver getThreadLocalInResolver() {
        return THREAD_LOCAL_RESOLVER.get();
    }

    //Sets the _in.resolver in ThreadLocal storage
    public static void setThreadLocalInResolver( _in._resolver jsh ){
        THREAD_LOCAL_RESOLVER.set( jsh );
    }

    public static void clearThreadLocalInResolver( ){
        THREAD_LOCAL_RESOLVER.set( null );
    }

    public static _in resolve( _io._inConfig ic, String sourceId ){
        //1) check thread local in resolver
        _in._resolver intl = getThreadLocalInResolver();
        if( intl != null ){
            //fully delegate the resolver to the thread local version
            return intl.resolve(sourceId);
        }
        //2) check for System properties for projects IN PROJECTS PATHS
        String projectsPaths = ic.inProjectsPath;
        if( projectsPaths != null && projectsPaths.length() > 0 ){
            String[] projs = projectsPaths.split(";");
            for(int i=0;i<projs.length;i++) {
                _in in = new _inProject(projs[i]).resolve(sourceId);
                if (in != null) {
                    return in;
                }
            }
        }
        //3) check for System properties for projects IN FILES PATH
        String filePaths = ic.inFilesPath;
        if( filePaths != null && filePaths.length() > 0 ){
            String[] fps = filePaths.split(";");
            for(int i=0;i<fps.length;i++) {
                _in in = _inFilePath.of(fps[i]).resolve(sourceId);
                if (in != null) {
                    return in;
                }
            }
        }
        // 4) check if it's in the current classPath
        _in in = _inClassPath.INSTANCE.resolve(sourceId);
        if( in != null ){
            return in;
        }

        // 5) check if it's in the current Project
        return IN_CURRENT_PROJECT.resolve(sourceId);
    }

    @Override
    public _in resolve(String sourceId) {
        return resolve( new _io._inConfig(), sourceId );        
    }

    @Override
    public _in resolve(Class clazz) {
        return resolve( new _io._inConfig(), clazz);
    }

    public static _in resolve( _io._inConfig config, Class clazz ){
        //1) check thread local in resolver
        _in._resolver intl = getThreadLocalInResolver();
        if( intl != null ){
            //fully delegate the resolver to the thread local version
            return intl.resolve(clazz);
        }

        //1B) (ONLY for in(Class) check if the Class
        _in in = _in_classLoader.INSTANCE.resolve(clazz);
        if( in != null ){
            return in;
        }

        //2) check for System properties for projects IN PROJECTS PATHS
        String projectsPaths = config.inProjectsPath; //System.getProperty( _io.IN_PROJECTS_PATH );
        if( projectsPaths != null && projectsPaths.length() > 0 ){
            String[] projs = projectsPaths.split(";");
            for(int i=0;i<projs.length;i++) {
                in = new _inProject(projs[i]).resolve(clazz);
                if (in != null) {
                    return in;
                }
            }
        }
        //3) check for System properties for projects IN FILES PATH
        String filePaths = config.inFilesPath; //System.getProperty( _io.IN_FILES_PATH );
        if( filePaths != null && filePaths.length() > 0 ){
            String[] fps = filePaths.split(";");
            for(int i=0;i<fps.length;i++) {
                in = _inFilePath.of(fps[i]).resolve(clazz);
                if (in != null) {
                    return in;
                }
            }
        }
        // 4) check if it's in the current classPath
        in = _inClassPath.INSTANCE.resolve(clazz);
        if( in != null ){
            return in;
        }

        // 5) check if it's in the current Project
        return IN_CURRENT_PROJECT.resolve(clazz);
    }

    @Override
    public String describe() {
        //describe the strategy for how files ( i.e. .java source files) will be resolved )
        _in._resolver _r = _io.getThreadLocalInResolver();
        if( _r == null ) {
            String resolveStrategy =  "1) *NO* ThreadLocal override resolver Set via _io.setThreadLocalInResolver" + System.lineSeparator() +
                    "2) InMemory check if the Class is loaded via a _classLoader" + System.lineSeparator();
            String pp = _io.getInProjectsPath();
            if( pp != null ){
                resolveStrategy += "3) InProjects: \"" + _io.IN_PROJECTS_PATH + "\" " + pp + " for projects to read from " + System.lineSeparator();
            } else{
                resolveStrategy += "3) *NO* InProjects set via System property: \"" + _io.IN_PROJECTS_PATH + "\"" + System.lineSeparator();
            }

            String fp = _io.getInFilesPath();
            if( fp != null ){
                resolveStrategy += "4) FilePath: \"" + _io.IN_FILES_PATH + "\" " + fp + " to read from " + System.lineSeparator();
            } else{
                resolveStrategy += "4) *NO* FilePath set via System property: \"" + _io.IN_FILES_PATH + "\" " + System.lineSeparator();
            }
            resolveStrategy += "5) "+ _inClassPath.INSTANCE.describe() + System.lineSeparator();
            String userPath = System.getProperty( "user.dir" );

            resolveStrategy += "6) "+ new _inProject().describe();
                    //_inProject.projectSourcePaths( ) + System.lineSeparator() +
                    //_inProject.projectTestPaths( System.getProperty( "user.dir" ))+System.lineSeparator();
            //String classPath = System.getProperty("java.class.path");
            return resolveStrategy;

            /*
            return "1) ThreadLocal override resolver is Not Set " + System.lineSeparator() +
                    "1b) (for class) check if the Class is loaded into a _classLoader inMemory" + System.lineSeparator() +
                    "2) check System property: \"" + _io.IN_PROJECTS_PATH + "\" " + _io.getInProjectsPath() + " for projects to read from " + System.lineSeparator() +
                    "3) check System property: \"" + _io.IN_FILES_PATH + "\" " + _io.getInFilesPath() + " for file filePaths to read from " + System.lineSeparator() +
                    "4) check System classPath : " + System.getProperty("CLASSPATH") + " " + System.lineSeparator() +
                    "5) check Current Project : " + System.lineSeparator();
                    */
        }
        return "ThreadLocalInResolver is set to : "+ _r.describe()+ System.lineSeparator();
    }


}