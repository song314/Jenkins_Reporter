package com.camera360.auto.ci.analyser;

import com.camera360.auto.ci.utils.L;
import com.camera360.auto.ci.utils.StringUtils;
import com.camera360.auto.ci.utils.TimeUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

/**
 *
 *
 *  Jekins目录结构
 *      jobs
 *          job1
 *              build
 *                  build1
 *                  build2
 *          job2
 *
 * how to use:
 *  1.new object
 *  2.setHomePath()
 *  3.analyse
 * Created by tangsong on 12/3/14.
 * todo 本周第一次或者最后一次构建失败时，不能获取正常的检查结果
 */
public class JenkinsAnalyser {

    /**
     * Jenkins
     */
    public static final String DATA_FORMAT_JENKINS_BUILD = "yyyy-MM-dd_HH-mm-ss";

    public enum TaskType {
        ALL, THIS_WEEK
    }

    private TaskType mTaskType = TaskType.ALL;

    private String mHomePath;
    private LinkedList<JenkinsResult> mJenkinsResult;

    public void setHomePath(String path) {
        System.out.println("set home path : " + path);
        mHomePath = path;
    }

    public void setTaskType(TaskType type) {
        mTaskType = type;
    }

    /**
     * 根据任务类型继续初始化
     */
    public void init() {
        final File fJobs = new File(mHomePath);

        File[] ciJobs = fJobs.listFiles(new CiJob());
        mJenkinsResult = new LinkedList<JenkinsResult>();

        for (File job : ciJobs) {
            L.i("to analyse job " + job.getName());
            JenkinsResult jenkinsResult = new JenkinsResult();
            jenkinsResult.result = initJobAnalysers(job);
            jenkinsResult.projectName = job.getName();
            mJenkinsResult.add(jenkinsResult);
        }
    }

    public LinkedList<JenkinsResult> analyse() {
        for (JenkinsResult jr : mJenkinsResult) {
            jr.analyse();
        }
        return mJenkinsResult;
    }

    public LinkedList<CiResultSet> initJobAnalysers(File dirJob) {
        final File dirBuild = new File(dirJob.getAbsolutePath() + File.separator + "builds");

        FilenameFilter filter;

        switch (mTaskType) {
            case THIS_WEEK:
                filter = new ThisWeekDirs();
                break;
            default:
                filter = new DateDir();
        }
        File[] builds = dirBuild.listFiles(filter);

        //过滤build，每天只扫描最后一次成功构建的结果

        LinkedList<CiResultSet> list = new LinkedList<CiResultSet>();
        for (File b : builds) {
            if (b.isDirectory()) {
                L.i("    to analyse build " + b.getName());
                CiResultSet ciResultSet = new CiResultSet();
                ciResultSet.analyseList = initBuildAnalysers(b);
                list.add(ciResultSet);
            }
        }
        return list;
    }

    private List<TsAbsAnalyse> initBuildAnalysers(File buildDir) {
        LinkedList<TsAbsAnalyse> list = new LinkedList<TsAbsAnalyse>();

        CiResultSet resultSet = new CiResultSet();
        resultSet.date = TimeUtils.getData(buildDir.getName(), DATA_FORMAT_JENKINS_BUILD, TimeZone.getDefault());

        LazyAnalyser la = new LazyAnalyser();

        la.setResultPath(buildDir.getAbsolutePath() + File.separator + "log");
        la.setCheckType(CiResult.CheckType.Log);
        la.setKeyWord("Finished: SUCCESS");
//        resultSet.success = la.analyse().count > 0;
        list.add(la);

        la = new LazyAnalyser();
        la.setResultPath(buildDir.getAbsolutePath() + File.separator + "changelog.xml");
        la.setCheckType(CiResult.CheckType.Commits);
        la.setKeyWord("commit ");
        resultSet.resultList.add(la.analyse());

        la = new LazyAnalyser();
        la.setResultPath(buildDir.getAbsolutePath() + File.separator + "checkstyle-warnings.xml");
        la.setCheckType(CiResult.CheckType.CheckStyle);
        la.setKeyWord("</warning>");
        list.add(la);

        la = new LazyAnalyser();
        la.setResultPath(buildDir.getAbsolutePath() + File.separator + "android-lint-issues.xml");
        la.setCheckType(CiResult.CheckType.Lint);
        la.setKeyWord("</issue>");
        list.add(la);

        la = new LazyAnalyser();
        la.setResultPath(buildDir.getAbsolutePath() + File.separator + "/pmd-warnings.xml");
        la.setCheckType(CiResult.CheckType.PMD);
        la.setKeyWord("</bug>");
        list.add(la);

        la = new LazyAnalyser();
        la.setResultPath(buildDir.getAbsolutePath() + File.separator + "/pmd-warnings.xml");
        la.setCheckType(CiResult.CheckType.CPD);
        la.setKeyWord("<dry plugin");
        list.add(la);

        return list;
    }

    private static class CiJob implements FileFilter {

        @Override
        public boolean accept(File pathname) {
            String name = pathname.getName();
            return name != null && name.endsWith("_CI") && pathname.isDirectory();
        }
    }

    /**
     * just list one build in each day, which must be built successfully &
     * the last build in the day
     */
    private static class OneBuildInDay extends DateDir {

        private File[] mBuild;

        public OneBuildInDay(File[] allBuild) {
            mBuild = allBuild;
            Arrays.sort(mBuild);
        }

        @Override
        public boolean accept(File dir, String name) {
            return super.accept(dir, name);
        }
    }

    private static class DateDir implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return StringUtils.isNumeric(name);
        }


    }

    private static class ThisWeekDirs extends DateDir {
        @Override
        public boolean accept(File dir, String name) {
            if (super.accept(dir, name)) {
                return TimeUtils.isInThisWeek(TimeUtils.getTimeStamp(name, DATA_FORMAT_JENKINS_BUILD));
            } else {
                return false;
            }
        }
    }

}
