package com.example.demo.utils;

import java.util.concurrent.*;

/**
 * 类的描述
 *
 * @author hujiping
 * @date 2022/9/30 11:13 AM
 */
public class ThreadHelper {

    private static final ExecutorService threadPoolExecutor = new ThreadPoolExecutor(2, 10, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1024));

    public static void main(String[] args) {

        try {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(123);
                }
            });

            Future<?> future = threadPoolExecutor.submit(new FutureTask<String>(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "null";
                }
            }));
            Object o = future.get();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void asyncExecute() {

    }

}
