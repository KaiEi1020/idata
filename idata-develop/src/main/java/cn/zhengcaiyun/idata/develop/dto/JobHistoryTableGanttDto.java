package cn.zhengcaiyun.idata.develop.dto;

import cn.zhengcaiyun.idata.develop.dto.job.JobInfoDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 表单格式甘特图
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table dev_job_history
 */
public class JobHistoryTableGanttDto {

    /**
     * Database Column Remarks:
     *   作业id
     */
    private Long jobId;

    /**
     * Database Column Remarks:
     *   作业id
     */
    private String jobName;

    /**
     * y轴数据
     */
    private List<Data> children = new ArrayList();

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public List<Data> getChildren() {
        return children;
    }

    public void setChildren(List<Data> children) {
        this.children = children;
    }

    public static class Data {

        /**
         * Database Column Remarks:
         *   作业id
         */
        private Long jobId;

        /**
         * Database Column Remarks:
         *   作业id
         */
        private String jobName;

        /**
         * Database Column Remarks:
         *   作业开始时间
         */
        private Date startTime;

        /**
         * Database Column Remarks:
         *   作业结束时间
         */
        private Date finishTime;

        /**
         * Database Column Remarks:
         *   作业最终状态
         */
        private String finalStatus;

        /**
         * 作业最终状态（系统内部展示的状态）
         * 1:等待运行 2：运行中 6：失败 7：成功 -1：其他
         */
        private Integer businessStatus;

        /**
         *   application master container url地址
         */
        private String amContainerLogsUrl;

        /**
         * 业务方需要的日志地址逻辑
         * http://bigdata-master3.cai-inc.com:8088/cluster/app/application_1636461038777_141467     killed  failed finished
         * http://bigdata-master3.cai-inc.com:8088/proxy/application_1636461038777_145072/       running
         */
        private String businessLogsUrl;

        public Long getJobId() {
            return jobId;
        }

        public void setJobId(Long jobId) {
            this.jobId = jobId;
        }

        public String getJobName() {
            return jobName;
        }

        public void setJobName(String jobName) {
            this.jobName = jobName;
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

        public String getFinalStatus() {
            return finalStatus;
        }

        public void setFinalStatus(String finalStatus) {
            this.finalStatus = finalStatus;
        }

        public String getAmContainerLogsUrl() {
            return amContainerLogsUrl;
        }

        public void setAmContainerLogsUrl(String amContainerLogsUrl) {
            this.amContainerLogsUrl = amContainerLogsUrl;
        }

        public Integer getBusinessStatus() {
            return businessStatus;
        }

        public void setBusinessStatus(Integer businessStatus) {
            this.businessStatus = businessStatus;
        }

        public String getBusinessLogsUrl() {
            return businessLogsUrl;
        }

        public void setBusinessLogsUrl(String businessLogsUrl) {
            this.businessLogsUrl = businessLogsUrl;
        }
    }
}