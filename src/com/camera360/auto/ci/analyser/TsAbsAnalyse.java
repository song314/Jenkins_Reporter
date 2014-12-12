package com.camera360.auto.ci.analyser;

/**
 * Created by tangsong on 12/2/14.
 */
public abstract class TsAbsAnalyse {

    protected String mResultPath;

    public void setResultPath(String path) {
        mResultPath = path;
    }

    public abstract CiResult analyse();



}
