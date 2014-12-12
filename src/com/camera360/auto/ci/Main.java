package com.camera360.auto.ci;

import com.camera360.auto.ci.analyser.*;
import com.camera360.auto.ci.excel.ExcelWriteAllModel;
import com.camera360.auto.ci.excel.ExcelWriteModel;
import com.camera360.auto.ci.utils.L;
import com.camera360.auto.ci.utils.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/***
 *
 */
public class Main {


    public static void main(String[] args) throws IOException {
	// write your code here

//        L.i("is the week : " + TimeUtils.isInThisWeek(System.currentTimeMillis() - 3600 * 24 * 5 * 1000));
//        L.i("is the week : " + TimeUtils.isInThisWeek(TimeUtils.getTimeStamp("2014-12-03_10-23-02", JenkinsAnalyser.DATA_FORMAT_JENKINS_BUILD)));
//
//        Date date = new Date(TimeUtils.getTimeStamp("2014-12-03_10-23-02", JenkinsAnalyser.DATA_FORMAT_JENKINS_BUILD));
//        L.i("date = " + date.toString());
        doTask(args);
    }

    private static void doTask(String[] args) {
        JenkinsAnalyser jenkinsAnalyser = new JenkinsAnalyser();

        System.out.println(args);
        System.out.println(" args.length = " + args.length);

        if (args != null && args.length > 0) {

            for (String str : args) {
                
            }

            jenkinsAnalyser.setHomePath(args[0]);
        } else {
//            jenkinsAnalyser.setHomePath(new File("").getCanonicalPath());
            String path = new File(jenkinsAnalyser.getClass().getResource("").getPath()).getAbsolutePath().split("!")[0].split("file:")[1];
            jenkinsAnalyser.setHomePath(new File(path).getParent());
//            jenkinsAnalyser.setHomePath("/Users/tangsong/Dev/open_source/CI_Reporter/test");
        }

        LinkedList<JenkinsResult> list = jenkinsAnalyser.analyse();

//        Collections.sort(list, new MySort());
        for (JenkinsResult item : list) {
            Collections.sort(item.result, new CiResultSetSort());
        }

//        ExcelWriteModel excelWriteModel = new ExcelWriteModel();
//        excelWriteModel.write(list);

        ExcelWriteAllModel excelWriteModel = new ExcelWriteAllModel();
        excelWriteModel.write(list);
    }

    public static class MySort implements Comparator<JenkinsResult> {

        @Override
        public int compare(JenkinsResult o1, JenkinsResult o2) {
            return 0;
        }
    }

    public static class CiResultSetSort implements Comparator<CiResultSet> {

        @Override
        public int compare(CiResultSet o1, CiResultSet o2) {
            if (o1.date.getTime() == o2.date.getTime()) {
                return 0;
            } else if (o1.date.getTime() > o2.date.getTime()) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
