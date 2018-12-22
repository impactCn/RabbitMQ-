package com.rabbitmq;

import org.seimicrawler.xpath.JXDocument;

import java.io.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/12/14.
 */
public class spider {


    public static void main(String[] args) throws IOException {

        // 读取文件
        File file = new File("C:/Users/Administrator/Desktop/courseTable/courseTable.html");

        // 输入流
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        // 转成String类型
        String s;
        String allContent = "";
        while ((s = bufferedReader.readLine()) != null) {
            allContent = allContent + s;
        }

        // 解析页面
        JXDocument jxDocument = JXDocument.create(allContent);

        // 课程数据存储
        List list1 = jxDocument.selN("//*[@id=\"table6\"]/tbody/tr[3]/td/text()");
        List copyList1 = list1.subList(2, list1.size());
        List list2 = jxDocument.selN("//*[@id=\"table6\"]/tbody/tr[5]/td/text()");
        List copylist2 = list2.subList(1, list2.size());
        List list3 = jxDocument.selN("//*[@id=\"table6\"]/tbody/tr[8]/td/text()");
        List copylist3 = list3.subList(2, list3.size());
        List list4 = jxDocument.selN("//*[@id=\"table6\"]/tbody/tr[10]/td/text()");
        List copylist4 = list4.subList(1, list4.size());

        // 日期数据存储
        List days = jxDocument.selN("//*[@id=\"table6\"]/tbody/tr[1]/td/text()").subList(1, 8);

        HashMap<String, HashMap<String, Object>> a = getList(list1,copyList1, days);
        HashMap<String, HashMap<String, Object>> b = getList(list2,copylist2, days);
        HashMap<String, HashMap<String, Object>> c = getList(list3,copylist3, days);
        HashMap<String, HashMap<String, Object>> d = getList(list4,copylist4, days);




        System.out.println(mergicMap(a, b, c, d, days));
    }
    // 数据清洗与重新分配
    public static HashMap<String, HashMap<String, Object>> getList(List arrs, List copyArrs, List days) {


        // 返回的map
        HashMap<String, HashMap<String, Object>> returnHashMap = new HashMap();

        int flag = 0;

        for (int i = 0; i < copyArrs.size(); i++) {
            String a = copyArrs.get(i).toString().split(", ")[0];

            // 控制课堂
            if (arrs.get(1).toString().equals("第一节")) {
                flag = 1;
            }
            if (arrs.get(0).toString().equals("第三节")) {
                flag = 3;
            }
            if (arrs.get(1).toString().equals("第五节")) {
                flag = 5;
            }
            if (arrs.get(0).toString().equals("第七节")) {
                flag = 7;
            }

            if (a.equals(" ")) {
                continue;
            } else {
                HashMap<String, Object> hashMap = new HashMap<>();
                if (a.split(" ")[0].isEmpty()) {
                    continue;
                }
                hashMap.put("课程名", a.split(" ")[0]);
                hashMap.put("时长", a.split(" ")[1]);
                hashMap.put("老师", a.split(" ")[2]);
                hashMap.put("教室", a.split(" ")[3]);
                hashMap.put("周期", a.split(" ")[4]);
                hashMap.put("上课节数", flag);
                hashMap.put("下课节数", flag + 1);
                returnHashMap.put(days.get(i).toString(), hashMap);
            }
        }
        return returnHashMap;
    }

    public static HashMap<String, String> mergicMap(HashMap<String, HashMap<String, Object>> a,
                                                    HashMap<String, HashMap<String, Object>> b,
                                                    HashMap<String, HashMap<String, Object>> c,
                                                    HashMap<String, HashMap<String, Object>> d,
                                                    List days) {


        HashMap<String, Object> nullHashMap = new HashMap<>();
        nullHashMap.put(" ", " ");

        HashMap<String, String> json = new HashMap<>();

        for (int i = 0; i < days.size(); i++) {
            String str = days.get(i).toString();

            if (!a.containsKey(str) || !b.containsKey(str) || !c.containsKey(str) || !d.containsKey(str)) {
                a.put(str, nullHashMap);
                b.put(str, nullHashMap);
                c.put(str, nullHashMap);
                d.put(str, nullHashMap);
            }


            json.put(str, a.get(str).toString() + b.get(str).toString() + c.get(str).toString()+d.get(str).toString());

        }

        return json;
    }

}



