package com.camera360.auto.ci;

import com.camera360.auto.ci.analyser.CiResultSet;
import com.camera360.auto.ci.analyser.JenkinsAnalyser;
import com.camera360.auto.ci.analyser.JenkinsResult;
import com.camera360.auto.ci.excel.ExcelWriteAllModel;
import com.camera360.auto.ci.utils.L;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Locale;

/***
 *
 */
public class Main {


    /**
     * the options to the command line
     */
    private static final Options OPTS = new Options();

    static {
        OPTS.addOption("t", true, "the report type : this_week all every_week");
        OPTS.addOption("r", true, "Traverse the directory for source files");
        OPTS.addOption("o", true, "Sets the output file. Defaults to stdout");
        OPTS.addOption("p", true, "Set the jenkins root dir");
        OPTS.addOption(
                "f",
                true,
                "Sets the output format. (plain|xml). Defaults to plain");
        OPTS.addOption("v", false, "Print product version and exit");
        OPTS.addOption("h", false, "Print the help information");
    }

    /**
     * Stop instances being created.
     */
    private Main() {
    }

    public static void main(String[] args) throws IOException {

//        L.i("is the week : " + TimeUtils.isInThisWeek(System.currentTimeMillis() - 3600 * 24 * 5 * 1000));
//        L.i("is the week : " + TimeUtils.isInThisWeek(TimeUtils.getTimeStamp("2014-12-03_10-23-02", JenkinsAnalyser.DATA_FORMAT_JENKINS_BUILD)));
//
//        Date date = new Date(TimeUtils.getTimeStamp("2014-12-03_10-23-02", JenkinsAnalyser.DATA_FORMAT_JENKINS_BUILD));
//        L.i("date = " + date.toString());
        doTask(args);
    }

    private static void doTask(String[] args) {

        // parse the parameters
        final CommandLineParser clp = new PosixParser();
        CommandLine line = null;
        try {
            line = clp.parse(OPTS, args);
        } catch (final ParseException e) {
            e.printStackTrace();
            //TODO
            L.i("print the help info ");
            return;
        }

        assert line != null;

        JenkinsAnalyser jenkinsAnalyser = new JenkinsAnalyser();


        JenkinsAnalyser.TaskType taskType = JenkinsAnalyser.TaskType.THIS_WEEK;
        if (line.hasOption("t")) {
            final String tValue = line.getOptionValue("t");
            try {
                taskType = JenkinsAnalyser.TaskType.valueOf(tValue.toUpperCase(Locale.US));
            } catch (final IllegalArgumentException e) {
                L.i("unknown task type : '" + tValue);
                return;
            }
        }

        if (line.hasOption("h")) {
            //TODO
            L.i("print the help info ");
            return;
        }

        jenkinsAnalyser.setTaskType(taskType);

        if (line.hasOption("p")) {
            jenkinsAnalyser.setHomePath(line.getOptionValue("p"));
        } else {
            try {
                String path = new File(jenkinsAnalyser.getClass().getResource("").getPath()).getAbsolutePath().split("!")[0].split("file:")[1];
                jenkinsAnalyser.setHomePath(new File(path).getParent());
            } catch (ArrayIndexOutOfBoundsException e) {
                L.e("set the debug path: ");
                jenkinsAnalyser.setHomePath("/home/ci-android/.jenkins/jobs");
            }
        }

        jenkinsAnalyser.init();

        LinkedList<JenkinsResult> list = jenkinsAnalyser.analyse();

//        Collections.sort(list, new MySort());
        for (JenkinsResult item : list) {
            if (item.result == null || item.result.size() == 0) {
                continue;
            }
            Collections.sort(item.result, new CiResultSetSort());
        }

//        ExcelWriteModel excelWriteModel = new ExcelWriteModel();
//        excelWriteModel.write(list);

        ExcelWriteAllModel excelWriteModel = new ExcelWriteAllModel();
        excelWriteModel.write(list);

        L.i(" all task finish..");
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
