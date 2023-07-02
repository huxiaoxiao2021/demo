package com.example.demo.utils;

import com.example.demo.jsf.AA;
import com.example.demo.jsf.BB;
import com.google.common.collect.Lists;
import com.jd.jsf.gd.GenericService;
import com.jd.jsf.gd.config.ConsumerConfig;
import com.jd.jsf.gd.config.RegistryConfig;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 类的描述
 *
 * @author hujiping
 * @date 2022/10/13 5:13 PM
 */
public class JsfHelper {

    private final static Logger logger = LoggerFactory.getLogger(JsfHelper.class);

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {

//        String interfaceClass = "com.jd.bluedragon.distribution.sendCode.DMSSendCodeJSFService";
//        String jsfAlias = "DMS-MANUAL-TEST-LOCAL";
//        String methodName = "queryBigInfoBySendCode";
//        String[] paramTypes = new String[]{"java.lang.String"};
//        Object[] paramContents = new Object[]{"FM00001001-1-1-W"};
//        syncInvoke(interfaceClass, jsfAlias, methodName, paramTypes, paramContents);

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//
//        sdf.format(new Date(1665982801211L));
        List<BB> list = Lists.newArrayList();
        BB bb = new BB();
        bb.setSize2(11);
        list.add(bb);
        BB bb1 = new BB();
        bb.setSize2(22);
        list.add(bb1);

        AA aa = new AA();
        aa.setSize1(1);
        aa.setBb(list);


        com.example.demo.jsf.AA aaa = new com.example.demo.jsf.AA();
        BeanUtils.copyProperties(aaa, aa);





        System.out.println();
    }

    @Data
    static class AA{
        private Integer size1;
        private List<BB> bb;
    }

    @Data
    static class BB{
        private Integer size2;
    }


    private static void syncInvoke(String interfaceClass, String jsfAlias, String methodName, String[] paramTypes, Object[] paramContents){

        // 注册中心实现（必须）
        RegistryConfig jsfRegistry = new RegistryConfig();
        jsfRegistry.setIndex("i.jsf.jd.com");
        logger.info("实例RegistryConfig");
        // 服务消费者连接注册中心，设置属性
        ConsumerConfig<GenericService> consumerConfig = new ConsumerConfig<GenericService>();
        consumerConfig.setInterfaceId(interfaceClass);// 这里写真实的类名
        consumerConfig.setRegistry(jsfRegistry);
        consumerConfig.setProtocol("jsf");
        consumerConfig.setAlias(jsfAlias);
        consumerConfig.setParameter(".token","123456"); // 设置token
        consumerConfig.setGeneric(true); // 需要指定是Generic调用true
        // consumerConfig.setAsync(true); // 如果异步
        logger.info("实例ConsumerConfig");
        // 得到泛化调用实例，此操作很重，请缓存consumerConfig或者service对象！！！！（用map或者全局变量）
        GenericService service = consumerConfig.refer();
        try {
            // 传入方法名，参数类型，参数值
            Object result = service.$invoke(methodName, paramTypes, paramContents);
            // 如果异步
            // ResponseFuture future = RpcContext.getContext().getFuture();
            // result = future.get();
            logger.info("result :{}", result);

//                // 如果传递对象的, 如果没有对象类，可以通过map来描述一个对象
//                Map map = new HashMap();
//                map.put("id",1);
//                map.put("name","zhangg21genericobj");
//                map.put("class", "com.jd.testjsf.ExampleObj"); // class属性就传真实类名
//                // 或者用json转map，而不是一个个put
//                // map = JSON.parseObject("{\"id\":1,\"name\":\"zzzzzz\",\"class\":\"com.jd.testjsf.ExampleObj\", Map.class}");
//                Object objresult = service.$invoke("echoObject", new String[]{"com.jd.testjsf.ExampleObj"},
//                        new Object[]{map});
//                logger.info("obj result :{}", objresult);
//
//                //如果参数传递list对象。接口方法是：public List<ExampleObj> echoObjectList(List<ExampleObj> list)
//                List list = new ArrayList();
//                Map map1 = new HashMap();
//                map1.put("id",1);
//                map1.put("name","polly");
//                map1.put("class", "com.jd.testjsf.ExampleObj");
//                list.add(map1);
//                Object result1 = service.$invoke("echoObjectList", new String[]{"java.util.List"}, new Object[]{list});
//                logger.info("obj result :{}", result1);
//
//                /**
//                 * public String getTestObject(TestObject testObject)
//                 * 方法名：getTestObject
//                 * 入参：TestObject, 自定义POJO,含枚举类型、List<Domain>
//                 * 返回：String
//                 */
//                List list = new ArrayList<>();
//                for(int j=0 ; j < 10 ; j++){
//                    Map domain = new HashMap();
//                    domain.put("name","polly");
//                    domain.put("sex","girl");
//                    domain.put("class","com.jd.testjsf.vo.Domain");
//                    list.add(domain);
//                }
//                Map map = new HashMap();
//                map.put("name","1");
//                map.put("type","YELLO");// EnumType
//                map.put("values",list);//List<Domain>
//                map.put("class", "com.jd.testjsf.vo.TestObject");
//                Object message = service.$invoke("getTestObject", new String[]{"com.jd.testjsf.vo.TestObject"},
//                        new Object[]{map});
//                logger.info("response msg from server :{}",message );
//
//                /**
//                 * public List<String> changeColorType(String str,Set<ColorType> colorTypes);
//                 * 方法名：changeColorType
//                 * 入参：String,Set<ColorType>
//                 * 返回:List<String>
//                 */
//                Set set = new HashSet();
//                set.add("BLANK");//自定义枚举
//                set.add("GREEN");//自定义枚举
//                set.add("RED");//自定义枚举
//                Object listObject = service.$invoke("changeColorType", new String[]{"java.lang.String", "java.util.Set"},
//                        new Object[]{"hello", set});
//                logger.info("response listObject from server :"+listObject );//response listObject from server :[BLANK, GREEN, RED]


        } catch (Exception e) {
            logger.error("", e);
        }
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }
    }

}
