package com.tuling.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 自定义的随机策略
 * Created by smlz on 2019/4/8.
 */
public class TulingRandomRule extends AbstractLoadBalancerRule {

    Random rand;

    public TulingRandomRule() {
        rand = new Random();
    }

    private int currentIndex = 0;

    private List<Server> currentChooseList = new ArrayList<Server>();

    /**
     * Randomly choose from all living servers
     */
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        Server server = null;

        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers();
            List<Server> allList = lb.getAllServers();

            int serverCount = allList.size();
            if (serverCount == 0) {
                return null;
            }

            //第一次进来 随机选取一个下标
            int index = rand.nextInt(serverCount);

            //当前轮询的次数小于等于5
            if(currentIndex<5) {
                //保存当前选择的服务列表ip
                if(currentChooseList.isEmpty()) {
                    currentChooseList.add(upList.get(index));
                }
                //当前的++
                currentIndex++;
                //返回保存的
                return currentChooseList.get(0);
            }else {
                currentChooseList.clear();
                currentChooseList.add(0,upList.get(index));
                currentIndex=0;
            }


            if (server == null) {
                Thread.yield();
                continue;
            }

            if (server.isAlive()) {
                return (server);
            }

            server = null;
            Thread.yield();
        }

        return server;

    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        // TODO Auto-generated method stub

    }
}
