package com.camera360.auto.ci.analyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by tangsong on 12/2/14.
 */
public class CommitAnalyse extends TsAbsAnalyse {

    private final static String KEY = "commit ";

    @Override
    public CiResult analyse() {

        File file = new File(mResultPath);
        BufferedReader reader = null;
        int commitCount = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if (tempString.indexOf(KEY) >= 0) {
                    commitCount++;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        CiResult result = new CiResult();
        result.count = commitCount;
        result.type = CiResult.CheckType.Commits;

        return result;
    }
}
