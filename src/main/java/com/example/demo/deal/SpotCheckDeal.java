package com.example.demo.deal;

import com.example.demo.utils.ExcelHelper;
import com.example.demo.utils.JsonHelper;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 类的描述
 *
 * @author hujiping
 * @date 2022/9/30 5:51 PM
 */
@Slf4j
public class SpotCheckDeal {

    private static final String cookie = "__jdu=16589911404841688144634; shshshfpa=093e4dee-f83f-8d82-f904-00e4569ed442-1660878407; shshshfpb=mY5k9d7H3rV9YSA4uNaAi_A; jd.erp.lang=zh_CN; jdd69fo72b8lfeoe=CSLSOFI6JQHWIGR5JQHDHLTX4PP5TARUQ76TNBNA3WAROKQGRRXZ2ZRT3DLC2G6J3AJ7IYOOASHN6D2ESMKXZN646Q; shshshfp=d091b0070481689ea978cf3f3b218be9; fp=26d0ecce19b55e0011fb955a59d54c87; areaId=1; ipLoc-djd=1-2802-0-0; token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InBobEs1WTlRVnRFV1Zza1dKQU45IiwiaWF0IjoxNjY0MDc5NDU3LCJleHAiOjE2NjQ2ODQyNTd9.5UojMYH8GaFL_5p55bGN-XCP3JU2MbromtOJ4tdD5lw; __jdv=230157721|direct|-|none|-|1664333762259; sso.jd.com=BJ.BF4EC349B22296A062E55F6695869AEE.0220220929183331; logbook_u=hujiping1; X-JACP-TOKEN=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJtb2JpbGUiOiIxNTYxMTcyNzE5NiIsInVzZXJJZCI6Imh1amlwaW5nMSIsImVtYWlsIjoiaHVqaXBpbmdAamQuY29tIiwidXNlcm5hbWUiOiJodWppcGluZzEiLCJpYXQiOjE2NjQ1MDIzNDksImlzcyI6Inhpbmd5dW4uamQuY29tIiwic3ViIjoic3NvIiwiZXhwIjoxNjY0NTg4NzQ5fQ.nMk5T6GW3WAkvpF4Og9EQ9Ci0VPtn373YQjc6fxLYETE1sRz0INI7S3kY5fEE_70Q5VByzlFACWiJYpPQ3Zqkv3ZLe2EHNKRI5rsdOEwwWBUSpHXnsGzrAeBDAJi0c9TZwjUDTv_00ukk-_62Lw67v-f-h2xUbEJIrpdTlzXh9-moGTbrX3vHLQvKpium98GQ8erlOTfl9uQZLk5hKLulD06_TPd8stKl8jwhrxG8FZ0okE5lK-Ifo-kIP_WlFBGXRtqqqkmPxvJv76JRM5-WqJv8g4sFK90JXpdBBwm-589fbphPEQG7TSs9CPv1Q_4WzKEcuCSqWbo-cwjvYd89w; 3AB9D23F7A4B3C9B=QAYROITPERMAVPRWJQFKQGAQRVBEZ6UOAHI7OQZVOXVWOJMC4YTEGPUSU3WLETUD3OY2L5IDS3W5IAAY5GCGYGDLAU; focus-team-id=00046419; focus-client=WEB; focus-token=6b530205bc998171b7a1d4572dcdce5d; RT=\"z=1&dm=jd.com&si=7p49ntsayc2&ss=l8o2gekr&sl=4&tt=9ef&ld=64eq\"; __jda=230157721.16589911404841688144634.1658991140.1664526074.1664529657.198; __jdb=230157721.2.16589911404841688144634|198.1664529657; __jdc=230157721";


    private static String template_path = "/Users/hujiping/project/tools/demo/src/main/resources/template/";

    private static String template_name = "抽检数据";

    private static String file_Suffix = ".xlsx";
    private static String generate_path = "/Users/hujiping/project/tools/demo/src/main/resources/out/";


    private static Lock lock = new ReentrantLock();
    private static Condition c1 = lock.newCondition();
    private static Condition c2 = lock.newCondition();
    private static Condition c3 = lock.newCondition();
    private static int turn = 1;
    
    public static void print(Condition myCondition, Condition nextCondition, int myTurn, String message){
        while (true){
            lock.lock();
            try {
                while (turn != myTurn) {
                    myCondition.await();
                }
                if(Objects.equals(message, "home")){
                    System.out.println(message);
                }else {
                    System.out.print(message + " ");
                }
                
                turn = myTurn % 3 + 1;
                nextCondition.signal();
            }catch (Exception e){
                Thread.currentThread().interrupt();
            }finally {
                lock.unlock();
            }
        }
    }
    
    public static void main(String[] args) {

        try{

            
            

            Thread t1 = new Thread(() -> print(c1, c2, 1,"welcome"));
            Thread t2 = new Thread(() -> print(c2, c3,  2,"to"));
            Thread t3 = new Thread(() -> print(c3, c1, 3,"home"));
            t1.start();
            t2.start();
            t3.start();
            
            
            

            CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
                System.out.println("welcome");
                return null;
            });
            
            CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
                System.out.println("to");
                return null;
            });
            CompletableFuture<String> combineResult =future1.thenCombine(future2, (result1, result2) -> {
                System.out.println("home");
                return null;
            });
            combineResult.get();

            // 异常eclp_mark start
            List<String> fieldListV1 = Lists.newArrayList();
            fieldListV1.add("packageCode");
            List<SheetV1> sheetV1List = ExcelHelper.readFromExcel("/Users/hujiping/Downloads/集包下线场地.xlsx", SheetV1.class, fieldListV1);

            NeedUpdateEclpMarkBarCodeVO needUpdateEclpMarkBarCodeVO = new NeedUpdateEclpMarkBarCodeVO();
            needUpdateEclpMarkBarCodeVO.setPackCodeList(
                    sheetV1List.stream().map(SheetV1::getPackageCode).collect(Collectors.toList())
            );
            JsonHelper.toJson(needUpdateEclpMarkBarCodeVO);
            // 异常eclp_mark end
            
            
            List<String> fieldList1 = Lists.newArrayList();
            fieldList1.add("waybillCode");
            fieldList1.add("createSiteCode");
            fieldList1.add("receiveSiteCode");
            fieldList1.add("createUserName");
            List<Sheet1Row> list1 = ExcelHelper.readFromExcel("/Users/hujiping/Downloads/sheet1-2023-06-07.xlsx", Sheet1Row.class, fieldList1);

            List<String> fieldList2 = Lists.newArrayList();
            fieldList2.add("waybillCode");
            fieldList2.add("orderId");
            fieldList2.add("packListStr");
            fieldList2.add("goodsListStr");
            List<Sheet2Row> list2 = ExcelHelper.readFromExcel("/Users/hujiping/Downloads/sheet2-2023-06-07.xlsx", Sheet2Row.class, fieldList2);

            System.out.println();

            List<String> sheet1WaybillList = list1.stream().map(Sheet1Row::getWaybillCode).collect(Collectors.toList());
            List<String> sheet2WaybillList = list2.stream().map(Sheet2Row::getWaybillCode).collect(Collectors.toList());

            List<String> notExistList = Lists.newArrayList();
            for (String item1 : sheet1WaybillList) {
                if(!sheet2WaybillList.contains(item1)){
                    notExistList.add(item1);
                }
            }

            List<Sheet1Row> sheet1List = JsonHelper.jsonToList(JsonHelper.toJson(list1), Sheet1Row.class);
            
//            List<Sheet1Row> proList = Lists.newArrayList();
//            int count = 0;
//            for (Sheet1Row sheet1Row : list1) {
//                for (Sheet2Row sheet2Row : list2) {
//                    if(Objects.equals(sheet1Row.getWaybillCode(), sheet2Row.getWaybillCode())
//                            && Objects.equals(Double.parseDouble(sheet1Row.getReviewWeight()), Double.parseDouble(sheet2Row.getJfWeight())) 
//                            && Objects.equals(sheet2Row.getYn(), "0")){
//                        proList.add(sheet1Row);
//                        count ++;
//                        break;
//                    }
//                }
//            }
//            System.out.println(count);


//            String templateFile = "/Users/hujiping/project/tools/demo/src/main/resources/template/抽检异常数据模板.xlsx";
//            String generateFile = "/Users/hujiping/project/tools/demo/src/main/resources/out/抽检异常数据结果.xlsx";
//
//            Map<String, Object> params = new HashMap<>();
//            Vector<Map<String, String>> items = new Vector<>();
//            for (Sheet1Row sheet1Row : proList) {
//                Map<String, String> item = new HashMap<>();
//                item.put("waybillCode", sheet1Row.getWaybillCode());
//                item.put("reviewWeight", sheet1Row.getReviewWeight());
//                item.put("reviewVolume", sheet1Row.getReviewVolume());
//                item.put("contrastWeight", sheet1Row.getContrastWeight());
//                item.put("contrastVolume", sheet1Row.getContrastVolume());
//                item.put("contrastSource", sheet1Row.getContrastSource());
//                items.add(item);    
//            }
//            params.put("printList", items);
//            ExcelHelper.generateExcel(templateFile, generateFile, params);

        }catch (Exception e) {
            log.error("服务异常!", e);
        }

    }

    @Data
    public static class Sheet1Row {
        private String waybillCode;
        private String createSiteCode;
        private String receiveSiteCode;
        private String createUserName;
    }

    @Data
    public static class Sheet2Row {
        private String waybillCode;
        private String orderId;
        private String packListStr;
        private String goodsListStr;
    }

    @Data
    public static class SheetV1 {
        private String packageCode;
    }

    @Data
    public static class NeedUpdateEclpMarkBarCodeVO {
        private List<String> packCodeList;
    }
}
