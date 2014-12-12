package com.camera360.auto.ci.excel;

import com.camera360.auto.ci.analyser.CiResult;
import com.camera360.auto.ci.analyser.CiResultSet;
import com.camera360.auto.ci.analyser.JenkinsResult;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * Camera360
 *           lint
 *           check
 *           style
 *           pmd
 *
 * Baby360
 *           lint
 *           ...
 *
 * Created by tangsong on 12/3/14.
 */

public class ExcelWriteAllModel {

    private static final int N = 4;
    private String mPath;

    private int mRow = 0;

    public void write(LinkedList<JenkinsResult> list) {
        // create a new workbook
        HSSFWorkbook wb = new HSSFWorkbook();
        // create a new sheet
        HSSFSheet thisWeek = wb.createSheet();
        wb.createSheet("all");

        HSSFRow head = thisWeek.createRow(mRow++);
        head.createCell(0).setCellValue("编译记录");

        for (int i = 0; i < list.size(); i++) {

            JenkinsResult project = list.get(i);

            HSSFRow title = thisWeek.createRow(mRow++);
            //写入工程名称
            title.createCell(0).setCellValue(project.projectName);


            LinkedList<CiResultSet> allResults = new LinkedList<CiResultSet>(project.result);
            if (allResults.size() <= 0) {
                continue;
            }

            int changeCount = 0;
            int reuseRow = mRow;
            for (int j = 0; j < allResults.size(); j ++) {

                mRow = reuseRow;

                final CiResultSet oneBuild = allResults.get(j);

                for (CiResult check : oneBuild.resultList) {
                    switch (check.type) {
                        case CheckStyle:
                        case Lint:
                        case PMD:
                        case CPD: {
                            if (j == 0) {

                                thisWeek.createRow(mRow++).createCell(j + 1).setCellValue(check.type.toString());
                                thisWeek.getRow(mRow - 1).createCell(j + 2).setCellValue(check.count);
                            } else {
                                thisWeek.getRow(mRow++).createCell(j + 2).setCellValue(check.count);
                            }
                        }
                        break;
                        case Commits:
                            //统计提交次数
                            changeCount += check.count;
                            break;
                    }
                }

            }

            mRow++;
        }

        try {
            // create a new file
            FileOutputStream out = new FileOutputStream("report_all_build.xls");
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
