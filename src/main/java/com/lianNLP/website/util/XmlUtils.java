package com.lianNLP.website.util;

import com.alibaba.fastjson.JSONArray;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.List;
import java.util.Map;

public class XmlUtils {


    public static void createXmlFile(Map<String, String> filedType, String fileName, List<String> fieldNameList, JSONArray dataArray) {
        try {
            // 1、创建document对象
            Document document = DocumentHelper.createDocument();
            // 2、创建根节点rss
            Element rss = document.addElement("geekdatachain");
            // 3、向rss节点添加version属性
            rss.addAttribute("version", "1.0");
            // 4、生成子节点及子节点内容
            int j = 1;
            for (Object o : dataArray) {
                JSONArray dataJson = JSONArray.parseArray(o.toString());
                Element dataBody = rss.addElement("dataBody");
                dataBody.addAttribute("id", String.valueOf(j++));
                for (int i = 0; i < fieldNameList.size(); i++) {
                    String titleEnName = fieldNameList.get(i);
                    String value = dataJson.getString(i);
                    if (titleEnName != null) {
                        Element title = dataBody.addElement(titleEnName);
                        title.setText(value);
                    }
                }
            }
            // 5、设置生成xml的格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            // 设置编码格式
            format.setEncoding("UTF-8");
            // 6、生成xml文件
            File file = new File(fileName);
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            // 设置是否转义，默认使用转义字符
            writer.setEscapeText(false);
            writer.write(document);
            writer.close();
            System.out.println("生成rss.xml成功");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
