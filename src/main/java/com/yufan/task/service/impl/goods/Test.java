package com.yufan.task.service.impl.goods;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @description:
 * @author: lirf
 * @time: 2021/2/2
 */
public class Test {

    private Logger LOG = Logger.getLogger(QuerySingleGoodsList.class);
    private final static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        System.out.println("===================begin===========================");
        List<Future<Integer>> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            FindGoodsList1 f = new FindGoodsList1(i);
            Future<Integer> future = executorService.submit(f);
            list.add(future);
        }

        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            count = count+ list.get(i).get();
        }
        System.out.println("count=" + count);
        System.out.println("===================end===========================");
    }

}

class FindGoodsList1 implements Callable<Integer> {
    private int index;

    public FindGoodsList1(int index) {
        this.index = index;
    }


    @Override
    public Integer call() throws Exception {
        System.out.println("-----------------------" + index);
        if (index == 0) {
//            Thread.sleep(2000);
            return 2;
        } else if (index == 1) {
//            Thread.sleep(6000);
            return 6;
        } else if (index == 2) {
//            Thread.sleep(3000);
            return 3;
        }
        return 1;
    }
}