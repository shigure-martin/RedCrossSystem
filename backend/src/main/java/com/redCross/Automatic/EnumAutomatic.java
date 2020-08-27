package com.redCross.Automatic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.redCross.utils.ExcelUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class EnumAutomatic {
    private static final String rootPath = "/Users/lli.chen/IdeaProjects/celebritiesGathering/backserver/src/main/java/com/celebritiesGathering";

    public static void main(String args[]) throws IOException {
        List<EnumDetail> enumDetails = getAllEnumDetail();
        generate(enumDetails);
    }

    private static List<EnumDetail> getAllEnumDetail() throws IOException {
        List<EnumDetail> enumDetails = new ArrayList<>();
        String path = rootPath + "/constants";
        File file = new File(path);
        File[] array = file.listFiles();
        for (int i = 0; i < array.length; i++) {
            String fileName = array[i].getName().replace(".java", "");
            String enumDetailstr;
            StringBuffer all = new StringBuffer();

            BufferedReader br = new BufferedReader(new FileReader(array[i]));
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                all.append(s).append("\n");
            }
            br.close();
            String str1 = all.substring(0, all.indexOf("{"));
            String str2 = all.substring(str1.length() + 1, all.length());
            enumDetailstr = str2.substring(0, str2.indexOf(";"));

            EnumDetail enumDetail = new EnumDetail();
            enumDetail.setEnumName(fileName);
            enumDetail.setEnumDetail(enumDetailstr);
            enumDetails.add(enumDetail);
        }
        return enumDetails;
    }

    private static void generate(List<EnumDetail> enumDetails) {
        LinkedHashMap<String, String> headerMap = new LinkedHashMap<>();
        headerMap.put("enumName", "枚举类名");
        headerMap.put("enumDetail", "枚举类内容");
        String fileName = "枚举类";
        ExcelUtil.exportExcelX(fileName, "枚举类", headerMap, JSONArray.parseArray(JSON.toJSONString(enumDetails)), "yyyy-MM-dd", 17,
                "/Users/lli.chen/IdeaProjects/celebritiesGathering/相关文档", null);
    }
}
