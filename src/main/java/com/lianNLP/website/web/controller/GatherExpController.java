package com.lianNLP.website.web.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chainNLP.TextNLPHelper;
import com.jeeframework.util.httpclient.proxy.SiteProxyIp;
import com.jeeframework.util.validate.Validate;
import com.jeeframework.webframework.exception.WebException;
import com.lianNLP.website.biz.service.GatherExpService;
import com.lianNLP.website.util.*;
import com.lianNLP.website.util.client.HttpClientHelper;
import com.lianNLP.website.web.exception.MySystemCode;
import com.lianNLP.website.web.po.CommonPO;
import com.lianNLP.website.web.po.RequireListPO;
import com.lianNLP.website.web.po.RequirePO;
import com.parseFrame.ParserHelper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller("gatherExpController")
@RequestMapping("/gatherExp")
@Api(value = "采集体验管理", description = "采集体验相关的访问接口", position = 2)
public class GatherExpController {

    @Resource
    private GatherExpService gatherExpService;

    @RequestMapping(value = "/getUrlCrawlDataList.json", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "获得url的列表抓取数据")
    public Map getUrlCrawlDataList(@RequestParam(value = "crawlUrl", required = true) @ApiParam(value = "抓取网址", required = true) String crawlUrl,
                                   @RequestParam(value = "isDepth", required = true) @ApiParam(value = "深入抓取 1为深入 0为不深入", required = true) String isDepth,
                                   HttpServletRequest request, HttpServletResponse response) {

        Map map = new HashMap();
        try {
            SiteProxyIp siteProxyIp = null;
            HttpClientHelper httpClientHelper = new HttpClientHelper();
            Map<String, String> headMap = new HashMap<>();
            // 根据网址获得源码文本
            String contents = httpClientHelper.doGet(crawlUrl, "utf-8", "utf-8", headMap, siteProxyIp).getContent();

            // 使用字符转换工具类，对源码进行转码
            CharsetUtils charsetUtils = new CharsetUtils();
            //contents = charsetUtils.CharsetCheckEntrance(contents.getBytes(), "utf-8", null);

            ParserHelper parserHelper = new ParserHelper();
            map = parserHelper.parse(contents, crawlUrl);
            GatherExpController.getUrlList(map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebException(MySystemCode.CRAWL_DATA_EXCEPTION);
        }
        return map;
    }

    private static void getUrlList(Map mapData) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");

        Map<String, List> mapList = new HashMap<>();

        List<String> titleList = null;
        List<List> dataList = new ArrayList<>();

        List<Map> list = (List<Map>) mapData.get("data");

        for (Map map : list) {
            List<String> bodyList = new ArrayList<>();
            titleList = new ArrayList<>();
            Iterator entries = map.entrySet().iterator();
            if (entries.hasNext()) {
                do {
                    Map.Entry entry = (Map.Entry) entries.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    System.out.println("Key = " + key + ", Value = " + value);
                    try {
                        if (value.length() == 13 && Long.parseLong(value) > 0) {
                            Date date = new Date(Long.parseLong(value));
                            value = sdf.format(date);
                        }
                    } catch (Exception e) {
                        bodyList.add(value);
                        continue;
                    }
                    bodyList.add(value);
                    titleList.add(key);
                } while (entries.hasNext());
            }
            dataList.add(bodyList);
        }
        mapList.put("title", titleList);
        mapList.put("data", dataList);
        mapData.remove("data");
        mapData.put("data", mapList);
    }

    @RequestMapping(value = "/downloadCrawlData.json", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "下载抓取的数据")
    public void downloadCrawlData(@RequestParam(value = "bodyData", required = true) @ApiParam(value = "抓取数据", required = true) String bodyData,
                                  @RequestParam(value = "type", required = true) @ApiParam(value = "下载类型 excel,csv,txt", required = true) String type,
                                  HttpServletRequest request, HttpServletResponse response) {

        //将传入的jsonString转为JSON
        JSONObject jsonObjectParam = JSONObject.parseObject(bodyData);

        JSONArray dataArray = jsonObjectParam.getJSONArray("data");
        JSONArray dataTitleArray = jsonObjectParam.getJSONArray("title");

        List<String> fieldNameList = new ArrayList<>();
        for (int i = 0; i < dataTitleArray.size(); i++) {
            String key = dataTitleArray.getString(i);
            fieldNameList.add(key);
        }
        String baseUrl = this.getClass().getResource("/").getPath();
        String fileName = "采集数据";

        if (Validate.isEmpty(type)) {
            throw new WebException(MySystemCode.SYS_REQUEST_EXCEPTION);
        }
        Map<String, String> fieldTypeMap = new HashMap<>();
        fieldTypeMap.put("text", "文本");
        String fileNameReal;
        File tempFile;
        try {
            if ("excel".equalsIgnoreCase(type)) {
                fileNameReal = fileName + "_" + new Date().getTime() + ".xlsx";
                fileName = baseUrl + fileNameReal;
                ExcelUtils.createExcelFile(fieldTypeMap, fileName, fieldNameList, dataArray);
                tempFile = new File(fileName);
            } else if ("csv".equalsIgnoreCase(type)) {
                fileNameReal = fileName + "_" + new Date().getTime() + ".csv";
                fileName = baseUrl + fileNameReal ;
                CSVFileUtil.createCsvFile(baseUrl, fileNameReal, fieldNameList, dataArray);
                tempFile = new File(fileName);
            } else if ("txt".equalsIgnoreCase(type)) {
                fileNameReal = fileName + "_" + new Date().getTime() + ".txt";
                fileName = baseUrl + fileNameReal;
                TxtUtil.creatTxtFile(fieldTypeMap, fileName, fieldNameList, dataArray);
                tempFile = new File(fileName);
            } else {
                fileNameReal = fileName + "_" + new Date().getTime() + ".xml";
                fileName = baseUrl + fileNameReal ;
                XmlUtils.createXmlFile(fieldTypeMap, fileName, fieldNameList, dataArray);
                tempFile = new File(fileName);
            }

            InputStream fis = new BufferedInputStream(new FileInputStream(fileName));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileNameReal.getBytes("gb2312"), "ISO8859-1"));
            response.addHeader("Content-Length", "" + tempFile.length());
            //response.setContentType("application/octet-stream");
            response.setContentType("multipart/form-data");
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(buffer);
            outputStream.flush();
            outputStream.close();
            tempFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/textAnalysis.json", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "文本分析")
    public Map<String, Object> textAnalysis(@RequestParam(value = "textData", required = true) @ApiParam(value = "分析文本", required = true) String textData,
                                            HttpServletRequest request, HttpServletResponse response) {

        TextNLPHelper textNLPHelper = new TextNLPHelper();
        return textNLPHelper.parse(textData,System.getProperty("path"));
    }

    @RequestMapping(value = "/saveRequire.json", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "保存需求")
    public CommonPO saveRequire(@RequestParam(value = "require", required = true) @ApiParam(value = "需求", required = true) String require,
                                @RequestParam(value = "nickName", required = true) @ApiParam(value = "昵称", required = true) String nickName,
                                @RequestParam(value = "phone", required = true) @ApiParam(value = "电话", required = true) String phone,
                                @RequestParam(value = "weixin", required = false) @ApiParam(value = "微信", required = false) String weixin,
                                HttpServletRequest request, HttpServletResponse response) {

        if (Validate.isEmpty(require) || Validate.isEmpty(nickName) || Validate.isEmpty(phone)) {
            throw new WebException(MySystemCode.SYS_REQUEST_EXCEPTION);
        }

        RequirePO requirePO = new RequirePO(require, nickName, phone, weixin);

        // 保存需求
        gatherExpService.saveRequire(requirePO);

        CommonPO commonPO = new CommonPO();
        commonPO.setCode(0);
        return commonPO;
    }

    @RequestMapping(value = "/getRequire.json", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "获取需求")
    public RequireListPO getRequire(@RequestParam(value = "size", required = false) @ApiParam(value = "查询记录数 默认 10 条", required = false) String size,
                                    @RequestParam(value = "page", required = false) @ApiParam(value = "查询页数 默认第 1 页", required = false) String page,
                                    HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> param = new HashMap<>();

        int sizeInt = 10;
        if (!Validate.isEmpty(size) && size.matches("\\d+")) {
            sizeInt = Integer.parseInt(size);
        }
        int pageInt = 0;
        if (!Validate.isEmpty(page) && page.matches("\\d+")) {
            pageInt = Integer.parseInt(page);
            pageInt = (pageInt - 1) * sizeInt;
        }
        param.put("size", sizeInt);
        param.put("page", pageInt);

        // 根据分页条件获得需求
        List<RequirePO> requirePOList = gatherExpService.getRequire(param);
        int count = gatherExpService.getRequireCount();
        if (requirePOList.size() == 0) {
            throw new WebException(MySystemCode.GATHER_QUERY_EXCEPTION);
        }
        RequireListPO requireListPO = new RequireListPO();
        requireListPO.setRequire(requirePOList);
        requireListPO.setCount(count);
        return requireListPO;
    }


}
