package cn.zhengcaiyun.idata.develop.dto.job;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Generated;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table dev_job_history
 */
public class JobHistoryDto implements Comparable {
    private Long id;

    private Date createTime;

    /**
     *  作业id
     */
    private Long jobId;

    /**
     * 作业名称
     */
    private String jobName;

    /**
     *  作业开始时间
     */
    private Date startTime;

    /**
     *  作业结束时间
     */
    private Date finishTime;

    /**
     *    作业持续时间（ms）
     */
    private Long duration;

    /***   作业最终状态
     */
    private String finalStatus;

    /***   作业平均消耗cpu虚拟核数
     */
    private Double avgVcores;

    /**
     *   作业平均消耗内存（MB）
     */
    private Long avgMemory;

    /**
     *   yarn的application
     */
    private String applicationId;

    private Long avgDuration;

    /**
     *   application master container url地址
     */
    private String amContainerLogsUrl;

    private Long jobInstanceId;

    /**
     * 数仓分层
     */
    private String layer;

    /**
     * Database Column Remarks:
     *   启动应用的user
     */
    private String user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public Double getAvgVcores() {
        return avgVcores;
    }

    public void setAvgVcores(Double avgVcores) {
        this.avgVcores = avgVcores;
    }

    public Long getAvgMemory() {
        return avgMemory;
    }

    public void setAvgMemory(Long avgMemory) {
        this.avgMemory = avgMemory;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Long getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(Long avgDuration) {
        this.avgDuration = avgDuration;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getAmContainerLogsUrl() {
        return amContainerLogsUrl;
    }

    public void setAmContainerLogsUrl(String amContainerLogsUrl) {
        this.amContainerLogsUrl = amContainerLogsUrl;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }

    public Long getJobInstanceId() {
        return jobInstanceId;
    }

    public void setJobInstanceId(Long jobInstanceId) {
        this.jobInstanceId = jobInstanceId;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}