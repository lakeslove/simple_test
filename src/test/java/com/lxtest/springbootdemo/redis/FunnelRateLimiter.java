package com.lxtest.springbootdemo.redis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class FunnelRateLimiter {
  static class Funnel {
    int capacity;
    float leakingRate;
    int leftQuota;
    long leakingTs;
    int quota = 1;
    int minStartQuota = 5;

    public Funnel(int capacity, int count, float time) {
      this.capacity = capacity;
      this.leakingRate = count/time;
      this.leftQuota = capacity;
      this.leakingTs = System.currentTimeMillis();
      this.minStartQuota = count;
    }

    void makeSpace() {
      long nowTs = System.currentTimeMillis();
      long deltaTs = nowTs - leakingTs;
      int deltaQuota = (int) (deltaTs * leakingRate);
      if (deltaQuota < 0) { // 间隔时间太长，整数数字过大溢出
        this.leftQuota = capacity;
        this.leakingTs = nowTs;
        return;
      }
      if (deltaQuota < minStartQuota) { // 腾出空间太小，最小单位是1
        return;
      }
      this.leftQuota += deltaQuota;
      this.leakingTs = nowTs;
      if (this.leftQuota > this.capacity) {
        this.leftQuota = this.capacity;
      }
    }

    boolean watering() {
      makeSpace();
      if (this.leftQuota >= quota) {
        this.leftQuota -= quota;
        return true;
      }
      return false;
    }

    public boolean isActionAllowed() {
      return watering(); // 需要1个quota
    }
  }

  private static Map<String, Funnel> funnels = new HashMap<>();

  private static Funnel getFunnel(String userId, String actionKey, int capacity, int count, float time) {
    String key = String.format("%s:%s", userId, actionKey);
    Funnel funnel = funnels.get(key);
    if (funnel == null) {
      funnel = new Funnel(capacity, count, time);
      funnels.put(key, funnel);
    }
    return funnel;
  }

  public static void main(String[] args){
    int count = 40;
    Funnel funnel = getFunnel("lx", "test", 10, 5, 5f);
    long start = System.currentTimeMillis();
    while(count>0){
      if(funnel.isActionAllowed()){
        count--;
        System.out.println(System.currentTimeMillis() + ",剩余个数"+count);
      }
    }
    long end = System.currentTimeMillis();
    System.out.println("耗费时间："+(end-start));
  }
}