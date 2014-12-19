package com.camera360.auto.ci.analyser;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tangsong on 12/2/14.
 */
public class CiResultSet {

    public LinkedList<CiResult> resultList = new LinkedList<CiResult>();
    public Date date;
    public boolean success = false;
    public List<TsAbsAnalyse> analyseList;

    public void analyse() {
        for (TsAbsAnalyse analyse : analyseList) {
            CiResult r = analyse.analyse();
            if (analyse.mType == CiResult.CheckType.Log) {
                this.success = r.count > 0;
            }
            resultList.add(r);
        }
    }

    @Override
    public String toString() {
        return "CiResultSet{" +
                "date=" + date +
                ", resultList=" + resultList +
                '}';
    }
}
