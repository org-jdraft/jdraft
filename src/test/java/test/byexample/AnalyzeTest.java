package test.byexample;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._code;

import java.util.Collections;
import java.util.List;

public class AnalyzeTest extends TestCase {

    public void testAnalyze(){
        //simple query

        //_batch should be a _code._hasCode
        _code _c = _class.of("aaaa.bbbb.CC");

        //query a github project, and find the number


    }

    public interface _codeProvider{
        List<_code> list_code();
    }

    /**
     *
     */
    public static class GitHubJavaProject implements _codeProvider{
        public String projectUrl; //make this a URL instead
        public long downloadTimestamp;
        public List<_code> lazyCachedCode;
        public List<String> cachedFiles;

        public GitHubJavaProject (String projectUrl){
            this( projectUrl, true);
        }

        private GitHubJavaProject(String projectUrl, boolean lazyLoad){
            this.projectUrl = projectUrl;
        }


        /**
         * Lazily read from the github project
         * @return
         */
        public List<_code> list_code(){
            //here read from github
            return Collections.EMPTY_LIST;
        }
    }

}
