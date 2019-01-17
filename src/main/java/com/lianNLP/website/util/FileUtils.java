package com.lianNLP.website.util;

import com.jeeframework.util.httpclient.HttpClientHelper;
import com.jeeframework.util.httpclient.HttpResponse;
import com.jeeframework.util.httpclient.proxy.SiteProxyIp;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {



    private static void downloadPicture(String urlList, String path) {
        URL url = null;
        try {
            urlList = "http:" + urlList;
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeImg(byte[] imgBytes, String path) {
        try {
            File file = new File(path);
            if (!file.exists())
                file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(imgBytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> readFile(String path)
            throws IOException
    {
        File file=new File(path);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        List<String> urls=new ArrayList<>();
        String contentLine ="";
        while ((contentLine = bufferedReader.readLine())!=null){
            urls.add(contentLine);
        }
        bufferedReader.close();
        return urls;

    }

    public static Object [] xpath(String xpath,String content){
        try
        {
            HtmlCleaner htmlCleaner = new HtmlCleaner();
            TagNode tagNode = htmlCleaner.clean(content);
            Object[] boj = tagNode.evaluateXPath(xpath);
            return boj;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 正则处理
     * @param regx
     * @param html
     * @return
     */
    public static List regexMatcher(String regx,String html){
        List ret=new ArrayList<>();
        Integer curPattern = Pattern.DOTALL;
        Pattern p = Pattern.compile(regx, curPattern);

        Matcher m = p.matcher(html);// 开始编译
        while (m.find())
        {
            String retContent = m.group(1);
            ret.add(retContent);
        }
        return ret;
    }

    public static String callRemoteService(String serviceURL, String method, Map<String, String> postData, String encode)
            throws Exception
    {
        HttpClientHelper httpClientHelper=new HttpClientHelper();
        httpClientHelper.setSoTimeout(5*60000);
        httpClientHelper.setConnectionTimeout(5*60000);
        try{
            HttpResponse getTermListResponse = null;

            if (method.equalsIgnoreCase("get")) {
                getTermListResponse = httpClientHelper
                        .doGetAndRetBytes(serviceURL,encode, encode, null,
                                null);
            } else {
                getTermListResponse = httpClientHelper
                        .doPostAndRetBytes(serviceURL, postData, encode, encode,
                                null, null);
            }
            return  new String(getTermListResponse.getContentBytes(),encode);
        }catch (Exception e){
            e.printStackTrace();

            throw  e;
        }finally
        {
            //httpClientHelper.get
        }
    }
    public static byte[] callRemoteService(String serviceURL, String encode, SiteProxyIp siteProxyIp)
            throws Exception
    {
        HttpClientHelper httpClientHelper=new HttpClientHelper();
        httpClientHelper.setSoTimeout(5*60000);
        httpClientHelper.setConnectionTimeout(5*60000);
        try{
            HttpResponse getTermListResponse = null;

            getTermListResponse = httpClientHelper
                    .doGetAndRetBytes(serviceURL,encode, encode, null,
                            siteProxyIp);
            return  getTermListResponse.getContentBytes();
        }catch (Exception e){
            e.printStackTrace();

            throw  e;
        }finally
        {
            //httpClientHelper.get
        }
    }

}
