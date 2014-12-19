package com.camera360.auto.ci.analyser;

import java.util.LinkedList;

/**
 * Created by tangsong on 12/4/14.
 */
public class JenkinsResult {
    public LinkedList<CiResultSet> result;

    public String projectName;

    @Override
    public String toString() {
        return "JenkinsResult{" +
                "projectName=" + projectName +
                "result=" + result +
                '}';
    }

    public void analyse() {
        for (CiResultSet set : result) {
            set.analyse();
        }
    }
}
