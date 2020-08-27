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

public class InterfaceAutomatic {
    private static final String rootPath = "D:\\ECNU\\DataBaseSystem\\Laboratory\\project\\works\\RedCrossSystem\\src\\main\\java\\com\\redCross";

    public static void main(String args[]) throws IOException {
        List<InterfaceDetail> interfaceDetails = getAllInterfaceDetail();
        generate(interfaceDetails);
    }

    private static List<InterfaceDetail> getAllInterfaceDetail() throws IOException {
        List<InterfaceDetail> interfaceDetails = new ArrayList<>();
        String path = rootPath + "/controller";
        File file = new File(path);
        File[] array = file.listFiles();

        String pathType = rootPath + "/constants";
        File fileType = new File(pathType);
        File[] arrayType = fileType.listFiles();
        List<String> types = new ArrayList<>();
        for (File file1 : arrayType) {
            String fileName = file1.getName().replace(".java", "");
            types.add(fileName);
        }

        for (int i = 0; i < array.length; i++) {
            String fileName = array[i].getName().replace(".java", "");
            if (!fileName.equals("BaseController") && !fileName.equals("FileController")) {
                String pathName = "/api/" + fileName.replaceAll("Info", "").replaceAll("Controller", "").toLowerCase();
                StringBuffer all = new StringBuffer();

                BufferedReader br = new BufferedReader(new FileReader(array[i]));
                String s = null;
                while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                    all.append(s).append("\n");
                }
                br.close();

                String[] chineseNames = all.toString().split("public");
                String[] split0 = chineseNames[0].split("\"");
                String chineseName = split0[split0.length - 2];

                InterfaceDetail interfaceSplit = new InterfaceDetail();
                interfaceSplit.setIterfaceScene(chineseName);
                interfaceSplit.setIterfacePath(pathName);
                interfaceDetails.add(interfaceSplit);

                String[] interfaceStrs = all.toString().split("@ApiOperation");
                for (int j = 1; j < interfaceStrs.length; j++) {
                    String interfaceStr = interfaceStrs[j];
                    String[] splitStrs = interfaceStr.split("\\)");
                    String[] split1 = interfaceStr.split("\"");
                    String iterfaceName = split1[1];
                    String iterfacePath1 = splitStrs[1];

                    String iterfaceMethod = iterfacePath1.substring(2, iterfacePath1.indexOf("Mapping"));
                    String iterfacePath = null;
                    if (iterfacePath1.indexOf(("\"")) != -1) {
                        String[] split2 = iterfacePath1.split("\"");
                        iterfacePath = split2[1];
                    }

                    String str1 = interfaceStr.substring(interfaceStr.indexOf("public"));
                    String[] split2 = str1.split("\\{");
                    String inputParam1 = split2[0].substring(split2[0].indexOf("(") + 1, split2[0].lastIndexOf(")")).replaceAll("\n", "").replaceAll("\t", "").replaceAll("@PathVariable", "").replaceAll("@RequestBody", "").replaceAll("@RequestParam", "");
                    String inputParam2 = inputParam1.replaceAll("\\(required = false, defaultValue = \"0\"\\)", "").replaceAll("\\(required = false\\)", "").replaceAll("\\(required = false, defaultValue = Integer_MAX_VALUE\\)", "").replaceAll("                               ", "");
                    String inputParam = inputParam2.replaceAll("boolean", "").replaceAll("Long", "").replaceAll("Double", "").replaceAll("String", "").replaceAll("double", "").replaceAll("int", "").replaceAll(" ", "");

                    for (String type : types) {
                        inputParam = inputParam.replaceAll(type, "");
                    }

                    InterfaceDetail interfaceDetail = new InterfaceDetail();
                    interfaceDetail.setIterfaceScene(iterfaceName);
                    interfaceDetail.setIterfaceMethod(iterfaceMethod);
                    interfaceDetail.setIterfacePath(iterfacePath);
                    interfaceDetail.setInputParam(inputParam);
                    interfaceDetails.add(interfaceDetail);
                }
            }
        }
        return interfaceDetails;
    }


    private static void generate(List<InterfaceDetail> interfaceDetails) {
        LinkedHashMap<String, String> headerMap = new LinkedHashMap<>();
        headerMap.put("iterfaceScene", "应用场景");
        headerMap.put("iterfacePath", "接口路径");
        headerMap.put("iterfaceMethod", "请求方法");
        headerMap.put("inputParam", "输入参数");
        headerMap.put("inputJson", "输入json");
        headerMap.put("inputRemarks", "输入备注");
        headerMap.put("output", "输出");
        headerMap.put("outputRemarks", "输出备注");
        String fileName = "后端接口文档";
        ExcelUtil.exportExcelX(fileName, "后端接口文档", headerMap, JSONArray.parseArray(JSON.toJSONString(interfaceDetails)), "yyyy-MM-dd", 17,
                "/Users/lli.chen/IdeaProjects/Pension/相关文档", null);
    }
}
