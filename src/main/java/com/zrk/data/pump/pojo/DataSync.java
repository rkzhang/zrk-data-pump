package com.zrk.data.pump.pojo;

import java.util.Date;
import lombok.ToString;

/**
 * 
 *
 * @author rkzhang
 * @date 2020/04/20
 */
@ToString
public class DataSync {
    private Long id;

    /**
     * 表名
     */
    private String tableName;

    /**
     * key
     */
    private String keyName;

    /**
     * value
     */
    private String keyValue;

    /**
     * 操作类型
     */
    private String optType;

    /**
     * 执行动作
     */
    private String action;

    /**
     * 执行次数
     */
    private Integer executionCount;

    /**
     * 下次执行时间
     */
    private Date nextExecutionTime;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * redis的key组成表达式
     */
    private String keyExpression;

    /**
     * 错误信息
     */
    private String errInfo;

    private Date createTime;

    private Date updateTime;

    private Integer stat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(Integer executionCount) {
        this.executionCount = executionCount;
    }

    public Date getNextExecutionTime() {
        return nextExecutionTime;
    }

    public void setNextExecutionTime(Date nextExecutionTime) {
        this.nextExecutionTime = nextExecutionTime;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getKeyExpression() {
        return keyExpression;
    }

    public void setKeyExpression(String keyExpression) {
        this.keyExpression = keyExpression;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStat() {
        return stat;
    }

    public void setStat(Integer stat) {
        this.stat = stat;
    }
}