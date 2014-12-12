package com.camera360.auto.ci.excel;

import com.camera360.auto.ci.analyser.CiResult;
import com.camera360.auto.ci.analyser.CiResultSet;
import com.camera360.auto.ci.analyser.JenkinsResult;
import com.camera360.auto.ci.utils.TimeUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 把分析的CI结果写入到excel表中
 *
 * Month
 *
 *      Week
 *
 *          Day
 *
 *              Build
 *
 *                  lint            Checkstyle pmd cpd   lint  Checkstyle pmd cpd   lint  Checkstyle pmd cpd
 *                  上周本 周 新增
 *  Camera360
 *  Baby360
 *  LiteCamera
 *  EffectSDK
 *
 * 质量表
 * *                  lint    Checkstyle  pmd  cpd
 *  Camera360        (n-0)commit
 *  Baby360
 *  LiteCamera
 *  EffectSDK
 *
 *
 * Created by tangsong on 12/3/14.
 */

public class ExcelWriteModel {

    private static final int N = 4;
    private String mPath;

    public void write(LinkedList<JenkinsResult> list) {
        // create a new workbook
        HSSFWorkbook wb = new HSSFWorkbook();
        // create a new sheet
        HSSFSheet thisWeek = wb.createSheet();
        wb.createSheet("week");
        wb.createSheet("daily");

        HSSFRow head = thisWeek.createRow(0);
        HSSFRow headDetail = thisWeek.createRow(1);
        CiResult.CheckType[] allCheckType = CiResult.CheckType.values();
        for (CiResult.CheckType type : allCheckType) {
            head.createCell(getIndex(type)).setCellValue(type.toString());
            headDetail.createCell(getIndex(type)).setCellValue("本周开始");
            headDetail.createCell(getIndex(type) + 1).setCellValue("本周结束");
            headDetail.createCell(getIndex(type) + 2).setCellValue("累积新增");
            headDetail.createCell(getIndex(type) + 3).setCellValue("提交质量");
        }


        for (int i = 0; i < list.size(); i++) {
            HSSFRow projectRow = thisWeek.createRow(i + 2);
            JenkinsResult project = list.get(i);
            projectRow.createCell(0).setCellValue(project.projectName);

            int changeCount = 0;

            LinkedList<CiResultSet> thisWeekResults = new LinkedList<CiResultSet>(project.result);

            if (thisWeekResults.size() <= 0) {
                continue;
            }

            for (int j = 0; j < thisWeekResults.size(); j ++) {
                CiResultSet oneBuild = thisWeekResults.get(j);
                for (CiResult check : oneBuild.resultList) {
                    switch (check.type) {
                        case Commits:
                            //统计提交次数
                            changeCount += check.count;
                            break;
                        case CheckStyle:
                            break;
                    }
                }
            }

            CiResultSet first = thisWeekResults.getFirst();
            CiResultSet last = thisWeekResults.getLast();

            for (int x = 0; x < first.resultList.size(); x++) {
                recordDelta(last.resultList.get(x), first.resultList.get(x), projectRow);
            }



            projectRow.getCell(getIndex(CiResult.CheckType.Commits)).setCellValue(changeCount);
        }

        try {
            // create a new file
            FileOutputStream out = new FileOutputStream("workbook.xls");
            wb.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void record(CiResult result, HSSFRow projectRow) {
        projectRow.createCell(getIndex(result.type)).setCellValue(result.count);
    }

    private void recordDelta(CiResult last, CiResult old, HSSFRow row) {
        row.createCell(getIndex(old.type)).setCellValue(old.count);
        row.createCell(getIndex(old.type) + 1).setCellValue(last.count);
        row.createCell(getIndex(old.type) + 2).setCellValue(last.count - old.count);
//        row.createCell(getIndex(old.type) + 3).setCellValue(last.count - old.count);
    }

    private int getIndex(CiResult.CheckType type) {
        int n;
        switch (type) {
            case Commits:
                n = 5;
                break;
            case CheckStyle:
                n = 2;
                break;
            case Lint:
                n = 1;
                break;
            case CPD:
                n = 4;
                break;
            case PMD:
                n = 3;
                break;
            default:
                throw new IllegalArgumentException("can not support the new type " + type);

        }

        return n * N - (N - 1);
    }

    public void writeThisWeek(LinkedList<JenkinsResult> list) {

    }
}
