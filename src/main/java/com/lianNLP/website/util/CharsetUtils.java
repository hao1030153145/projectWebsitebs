package com.lianNLP.website.util;

import com.jeeframework.util.validate.Validate;
import org.apache.commons.lang3.CharUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 包: com.transing.crawl.util
 * 源文件:CharsetUtils.java
 * 自动编码转换器
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年11月16日
 */
public class CharsetUtils
{
    private static final int errorCount=10;

    private static final String META_CHARSET="<meta.*?charset=[\\'|\\\"]?([[a-z]|[A-Z]|[0-9]|-]*).*?>";

    private static String [] charsetArr=new String[]{"utf-8","GBK","unicode"};


    public String CharsetCheckEntrance(byte [] contentByte,String currentCharset,String respCharSet)
            throws Exception
    {
        String content=new String(contentByte,currentCharset);
        if(isNormalChinseCode(content))
            return content;
        String newCharset=RegexUtil.matchRegxWithPrefix(content,META_CHARSET,false);
        if(!Validate.isEmpty(newCharset))
            return new String(contentByte,newCharset);
        if(!Validate.isEmpty(respCharSet))
            return new String(contentByte,respCharSet);
        List<String> users=new ArrayList<String>();
        content=customUseCharSet(contentByte,currentCharset,users);
        if(Validate.isEmpty(content))
            content=new String(contentByte,currentCharset);
        return content;
    }


    private String customUseCharSet(byte[] contentByte,String currentCharset,List<String> users)
            throws Exception
    {
        users.add(currentCharset);
        String content ="";
        String newCharSet =getNextCharset(users);
        if (newCharSet==null)
        {
            return content;
        }else{
            content=new String(contentByte,newCharSet);
            if(!isNormalChinseCode(content))
                customUseCharSet(contentByte,newCharSet,users);
            else
                return content;
        }
        return content;
    }


    /**
     * 判定是否是正常的中文
     * 先去除英文字母，数字，制表符以及中文字符
     * @return
     */
    private boolean isNormalChinseCode(String content) throws Exception
    {
        int messys=0;
        List list= ParseUtil.parseValue(content,"//body//text()","xpath",null);
        if (list.size()>0)
            content=list.get(0).toString();
        content=clearContent(content);
        char[] contentChars=content.toCharArray();
        for (char c:contentChars){
            Character.UnicodeBlock block=Character.UnicodeBlock.of(c);
            if (!CharUtils.isAscii(c)&&
                    block != Character.UnicodeBlock.GENERAL_PUNCTUATION    //百分号，千分号，单引号，双引号等)
                    && block != Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION //顿号，句号，书名号，〸，〹，〺 等
                    && block != Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS  // 大于，小于，等于，括号，感叹号，加，减，冒号，分号等等
                    && block != Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS //主要是给竖写方式使用的括号，以及间断线﹉，波浪线﹌等
                    && block != Character.UnicodeBlock.VERTICAL_FORMS  //主要是一些竖着写的标点符号，    等等
                    &&block!=Character.UnicodeBlock.LATIN_1_SUPPLEMENT){
                if (!isChinses(c))
                    messys++;
            }
            if (messys>errorCount)
                return false;
        }
        return true;
    }

    private String clearContent(String content){
        return content.replaceAll("([\n\r\t\\s]+)","").trim();
    }

    private boolean isChinses(char c){
        return String.valueOf(c).matches("[\\u4E00-\\u9FA5]+");
    }


    private String getNextCharset(List<String> usedList){
        String newCharSet="utf-8";
        for (String chset:charsetArr){
            if (!usedList.contains(chset)){
                newCharSet=chset;
                return newCharSet;
            }
        }
        if(usedList.size()==charsetArr.length)
            return null;
        return newCharSet;
    }



}
