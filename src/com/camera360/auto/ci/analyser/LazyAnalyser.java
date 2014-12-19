package com.camera360.auto.ci.analyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by tangsong on 12/2/14.
 */
public class LazyAnalyser extends TsAbsAnalyse {

    public void setKeyWord(String keyWord) {
      mKey = keyWord;
    }

    public void setCheckType(CiResult.CheckType type) {
        mType = type;
    }

    @Override
    public CiResult analyse() {

        CiResult result = new CiResult();
        result.type = mType;

        File file = new File(mResultPath);
        if (file.exists()) {
            BufferedReader reader = null;
            int count = 0;
            try {
                reader = new BufferedReader(new FileReader(file));
                String tempString = null;
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader.readLine()) != null) {
                    if (tempString.indexOf(mKey) >= 0) {
                        count++;
                    }
                }

                result.count = count;
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
        } else {

        }

        return result;
    }
}
