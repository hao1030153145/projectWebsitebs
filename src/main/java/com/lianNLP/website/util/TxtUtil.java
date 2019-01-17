package com.lianNLP.website.util;

import com.alibaba.fastjson.JSONArray;
import com.jeeframework.logicframework.util.logging.LoggerUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TxtUtil {

    public static void creatTxtFile(Map<String, String> filedType, String fileName, List<String> fieldNameList, JSONArray dataArray) {

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName, true);
            String end = "";
            for (Object o : dataArray) {
                JSONArray datajson = JSONArray.parseArray(o.toString());
                for (int i = 0; i < fieldNameList.size(); i++) {
                    String titleEnName = fieldNameList.get(i);
                    String value = datajson.getString(i);
                    if (titleEnName != null){
                        String add1 = titleEnName.replaceAll(" +", " ");
                        String add2 = value.replaceAll(" +", " ");
                        end = end + add1 + "：" + add2 + "\t";
                    }else {
                        end = end + "";
                    }
                }
                end += "\r\n";
            }
            fileWriter.write(end);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                LoggerUtil.debugTrace("写入完成");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void writeTxt(FileWriter fileWriter, String value, String fileName) {

    }
}
