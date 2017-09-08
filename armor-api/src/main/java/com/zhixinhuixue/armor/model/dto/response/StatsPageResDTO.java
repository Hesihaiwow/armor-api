package com.zhixinhuixue.armor.model.dto.response;

/**
 * Created by Lang on 2017/9/4 0004.
 */
public class StatsPageResDTO {

    private Long id;

    private String name;

    private int quantity;//用户任务数量

    private int inProcess;//任务进行中

    private int complete;//任务完成数

    private int delay;//任务超时数

    private int sum;
    private int hours;//正在进行任务时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getInProcess() {
        return inProcess;
    }

    public void setInProcess(int inProcess) {
        this.inProcess = inProcess;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getHours() {
        return hours;
    }
}
