package com.camera360.auto.ci.analyser;

/**
 * Created by tangsong on 12/2/14.
 */
public class CiResult {

    public enum CheckType {
        Lint, CheckStyle, PMD, CPD, Commits, Log
    }

    public CheckType type;
    public long count = 0;

    @Override
    public String toString() {
        return "CiResult{" +
                "type='" + type + '\'' +
                ", count=" + count +
                '}';
    }
}
