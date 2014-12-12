package com.camera360.auto.ci.analyser;

import java.util.Date;
import java.util.LinkedList;

/**
 * Created by tangsong on 12/2/14.
 */
public class CiResultSet {

    public LinkedList<CiResult> resultList = new LinkedList<CiResult>();
    public Date date;
    public boolean success = false;

    @Override
    public String toString() {
        return "CiResultSet{" +
                "date=" + date +
                ", resultList=" + resultList +
                '}';
    }
}
