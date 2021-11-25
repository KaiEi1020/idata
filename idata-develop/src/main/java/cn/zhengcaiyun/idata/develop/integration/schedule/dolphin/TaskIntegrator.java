/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.zhengcaiyun.idata.develop.integration.schedule.dolphin;

import cn.zhengcaiyun.idata.commons.exception.ExternalIntegrationException;
import cn.zhengcaiyun.idata.commons.rpc.HttpInput;
import cn.zhengcaiyun.idata.develop.constant.enums.JobPriorityEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.RunningStateEnum;
import cn.zhengcaiyun.idata.develop.dal.model.integration.DSDependenceNode;
import cn.zhengcaiyun.idata.develop.dal.model.integration.DSEntityMapping;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.integration.DSDependenceNodeRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.integration.DSEntityMappingRepo;
import cn.zhengcaiyun.idata.develop.integration.schedule.IJobIntegrator;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.ResultDto;
import cn.zhengcaiyun.idata.develop.util.DagJobPair;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-03 17:41
 **/
@Component
public class TaskIntegrator extends DolphinIntegrationAdapter implements IJobIntegrator {

    @Autowired
    public TaskIntegrator(DSEntityMappingRepo dsEntityMappingRepo,
                          DSDependenceNodeRepo dsDependenceNodeRepo) {
        super(dsEntityMappingRepo, dsDependenceNodeRepo);
    }

    @Override
    public void create(JobInfo jobInfo, JobExecuteConfig executeConfig, String environment) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/task-definition", projectCode);
        String req_method = "POST";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildJobReqParam(jobInfo, executeConfig);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("创建DS任务失败：s%", resultDto.getMsg()));
        }

        JSONObject result = resultDto.getData();
        Long taskCode = result.getLong("code");
        //保存job和ds task关系
        DSEntityMapping mapping = new DSEntityMapping();
        mapping.setEntityId(jobInfo.getId());
        mapping.setEnvironment(environment);
        mapping.setDsEntityType(ENTITY_TYPE_TASK);
        mapping.setDsEntityCode(taskCode);
        dsEntityMappingRepo.create(new DSEntityMapping());
    }

    @Override
    public void update(JobInfo jobInfo, JobExecuteConfig executeConfig, String environment) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/task-definition/s%", projectCode, taskCode);
        String req_method = "PUT";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildJobReqParam(jobInfo, executeConfig);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("更新DS任务失败：s%", resultDto.getMsg()));
        }
    }

    @Override
    public void delete(JobInfo jobInfo, String environment) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/task-definition/s%", projectCode, taskCode);
        String req_method = "DELETE";
        String token = getDSToken(environment);

        HttpInput req_input = buildHttpReq(null, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("删除DS任务失败：s%", resultDto.getMsg()));
        }
    }

    @Override
    public void enableRunning(JobInfo jobInfo, String environment) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/task-definition/s%/release", projectCode, taskCode);
        String req_method = "POST";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildJobRunningStatusChangedParam(RunningStateEnum.resume.val);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("上线DS任务失败：s%", resultDto.getMsg()));
        }
    }

    @Override
    public void disableRunning(JobInfo jobInfo, String environment) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/task-definition/s%/release", projectCode, taskCode);
        String req_method = "POST";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildJobRunningStatusChangedParam(RunningStateEnum.pause.val);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("下线DS任务失败：s%", resultDto.getMsg()));
        }
    }

    @Override
    public void publish(JobInfo jobInfo, String environment) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/task-definition/s%/release", projectCode, taskCode);
        String req_method = "POST";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildJobRunningStatusChangedParam(RunningStateEnum.resume.val);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("发布DS任务失败：s%", resultDto.getMsg()));
        }
    }

    @Override
    public void bindDag(JobInfo jobInfo, Long dagId, String environment) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取工作流code
        Long workflowCode = getWorkflowCode(dagId, environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/process-task-relation", projectCode);
        String req_method = "POST";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildBindDagParam(workflowCode, taskCode);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("关联DS工作流失败：s%", resultDto.getMsg()));
        }
    }

    @Override
    public void unBindDag(JobInfo jobInfo, Long dagId, String environment) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取工作流code
        Long workflowCode = getWorkflowCode(dagId, environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/process-task-relation/s%", projectCode, taskCode);
        String req_method = "DELETE";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildUnBindDagParam(workflowCode);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("解除关联DS工作流失败：s%", resultDto.getMsg()));
        }
    }

    @Override
    public void run(JobInfo jobInfo, JobExecuteConfig executeConfig, String environment) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取工作流code
        Long workflowCode = getWorkflowCode(executeConfig.getSchDagId(), environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/executors/start-process-instance", projectCode);
        String req_method = "POST";
        String token = getDSToken(environment);

        Map<String, String> req_param = buildRunningParam(workflowCode, taskCode, executeConfig);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("运行DS任务失败：s%", resultDto.getMsg()));
        }
    }

    @Override
    public void buildJobRelation(JobInfo jobInfo, JobExecuteConfig executeConfig, String environment,
                                 List<DagJobPair> addingPrevRelations, List<DagJobPair> removingPrevRelations) throws ExternalIntegrationException {
        String token = getDSToken(environment);
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取工作流code
        Long workflowCode = getWorkflowCode(executeConfig.getSchDagId(), environment);
        // 获取task code
        Long taskCode = getTaskCode(jobInfo.getId(), environment);

        if (!CollectionUtils.isEmpty(addingPrevRelations)) {
            addPrevRelation(token, projectCode, workflowCode, taskCode,
                    jobInfo, executeConfig, environment, addingPrevRelations);
        }
        if (!CollectionUtils.isEmpty(removingPrevRelations)) {
            removePrevRelation(token, projectCode, workflowCode, taskCode,
                    jobInfo, executeConfig, environment, removingPrevRelations);
        }

    }

    private void removePrevRelation(String token, String projectCode, Long workflowCode, Long taskCode,
                                    JobInfo jobInfo, JobExecuteConfig executeConfig, String environment,
                                    List<DagJobPair> removingPrevRelations) throws ExternalIntegrationException {
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/process-task-relation/s%/upstream", projectCode, taskCode);
        String req_method = "DELETE";

        List<Long> prevTaskCodes = Lists.newArrayList();
        List<DSDependenceNode> deleteDepNodes = Lists.newArrayList();
        for (DagJobPair dagJobPair : removingPrevRelations) {
            List<Long> prevJobIds = dagJobPair.getJobIds();
            if (dagJobPair.getDagId().equals(executeConfig.getSchDagId())) {
                for (Long prevJobId : prevJobIds) {
                    Long prevTaskCode = getTaskCode(prevJobId, environment);
                    prevTaskCodes.add(prevTaskCode);
                }
            } else {
                for (Long prevJobId : prevJobIds) {
                    Long prevTaskCode = getTaskCode(prevJobId, environment);
                    List<DSDependenceNode> dsDependenceNodes = dsDependenceNodeRepo.queryDependenceNodeInWorkflow(taskCode, workflowCode, prevTaskCode);
                    if (!CollectionUtils.isEmpty(dsDependenceNodes)) {
                        prevTaskCodes.add(dsDependenceNodes.get(0).getDependenceNodeCode());
                        deleteDepNodes.addAll(dsDependenceNodes);
                    }
                }
            }
        }

        Map<String, String> req_param = buildDeleteJobRelationParam(prevTaskCodes);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("删除DS任务依赖关系失败：s%", resultDto.getMsg()));
        }

        if (!CollectionUtils.isEmpty(deleteDepNodes)) {
            Set<Long> depNodeCodes = Sets.newHashSet();
            List<Long> ids = Lists.newArrayList();
            for (DSDependenceNode dependenceNode : deleteDepNodes) {
                ids.add(dependenceNode.getId());
                depNodeCodes.add(dependenceNode.getDependenceNodeCode());
            }
            dsDependenceNodeRepo.delete(ids);

            for (Long depNodeCode : depNodeCodes) {
                if (CollectionUtils.isEmpty(dsDependenceNodeRepo.queryByDependenceNode(depNodeCode))) {
                    unBindAndDeleteDepNode(token, projectCode, workflowCode, depNodeCode, environment);
                }
            }
        }
    }

    private void unBindAndDeleteDepNode(String token, String projectCode, Long workflowCode, Long depNodeCode, String environment) throws ExternalIntegrationException {
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/process-task-relation/s%", projectCode, depNodeCode);
        String req_method = "DELETE";

        Map<String, String> req_param = buildUnBindDagParam(workflowCode);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("解除依赖节点关联DS工作流失败：s%", resultDto.getMsg()));
        }
    }

    private Map<String, String> buildDeleteJobRelationParam(List<Long> prevTaskCodes) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("preTaskCodes", Joiner.on(",").join(prevTaskCodes));
        return paramMap;
    }

    private void addPrevRelation(String token, String projectCode, Long workflowCode, Long taskCode,
                                 JobInfo jobInfo, JobExecuteConfig executeConfig, String environment,
                                 List<DagJobPair> addingPrevRelations) throws ExternalIntegrationException {
        for (DagJobPair dagJobPair : addingPrevRelations) {
            if (dagJobPair.getDagId().equals(executeConfig.getSchDagId())) {
                addPrevRelationInSameDAG(token, projectCode, workflowCode, taskCode,
                        jobInfo, executeConfig, environment, dagJobPair);
            } else {
                addPrevRelationInDifferentDAG(token, projectCode, workflowCode, taskCode,
                        jobInfo, executeConfig, environment, dagJobPair);
            }
        }
    }

    private void addPrevRelationInSameDAG(String token, String projectCode, Long workflowCode, Long taskCode,
                                          JobInfo jobInfo, JobExecuteConfig executeConfig, String environment,
                                          DagJobPair dagJobPair) throws ExternalIntegrationException {
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/process-task-relation", projectCode);
        String req_method = "POST";

        List<Long> prevJobIds = dagJobPair.getJobIds();
        for (Long prevJobId : prevJobIds) {
            Long prevTaskCode = getTaskCode(prevJobId, environment);
            Map<String, String> req_param = buildJobRelationParam(workflowCode, taskCode, prevTaskCode);
            HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
            ResultDto<JSONObject> resultDto = sendReq(req_input);
            if (!resultDto.isSuccess()) {
                throw new ExternalIntegrationException(String.format("创建DS任务依赖关系失败：s%", resultDto.getMsg()));
            }
        }
    }

    private void addPrevRelationInDifferentDAG(String token, String projectCode, Long workflowCode, Long taskCode,
                                               JobInfo jobInfo, JobExecuteConfig executeConfig, String environment,
                                               DagJobPair dagJobPair) throws ExternalIntegrationException {
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/process-task-relation", projectCode);
        String req_method = "POST";

        Long prevDagId = dagJobPair.getDagId();
        Long prevWorkflowCode = getWorkflowCode(prevDagId, environment);
        List<Long> prevJobIds = dagJobPair.getJobIds();
        for (Long prevJobId : prevJobIds) {
            Long prevTaskCode = getTaskCode(prevJobId, environment);
            Long dependenceNodeCode;
            // 查询上游依赖任务在当前dag有没有依赖节点
            List<DSDependenceNode> dsDependenceNodes = dsDependenceNodeRepo.queryDependenceNodeInWorkflow(workflowCode, prevTaskCode);
            if (CollectionUtils.isEmpty(dsDependenceNodes)) {
                // 创建依赖节点
                dependenceNodeCode = createDependenceNode(token, projectCode, jobInfo, executeConfig, environment,
                        prevWorkflowCode, prevTaskCode, prevDagId, prevJobId);
            } else {
                dependenceNodeCode = dsDependenceNodes.get(0).getDependenceNodeCode();
            }
            DSDependenceNode node = new DSDependenceNode();
            node.setTaskCode(taskCode);
            node.setWorkflowCode(workflowCode);
            node.setDependenceNodeCode(dependenceNodeCode);
            node.setPrevTaskCode(prevTaskCode);
            node.setPrevWorkflowCode(prevWorkflowCode);
            dsDependenceNodeRepo.create(node);

            // 建立当前任务和依赖节点关系
            Map<String, String> req_param = buildJobRelationParam(workflowCode, taskCode, dependenceNodeCode);
            HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
            ResultDto<JSONObject> resultDto = sendReq(req_input);
            if (!resultDto.isSuccess()) {
                throw new ExternalIntegrationException(String.format("创建DS任务跨工作流依赖关系失败：s%", resultDto.getMsg()));
            }
        }
    }

    private Long createDependenceNode(String token, String projectCode, JobInfo jobInfo, JobExecuteConfig executeConfig, String environment,
                                      Long prevWorkflowCode, Long prevTaskCode, Long prevDagId, Long prevJobId) throws ExternalIntegrationException {
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/task-definition", projectCode);
        String req_method = "POST";

        Map<String, String> req_param = buildDependenceNodeParam(projectCode, prevWorkflowCode,
                prevTaskCode, prevDagId, prevJobId, jobInfo, executeConfig);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("创建DS依赖节点失败：s%", resultDto.getMsg()));
        }

        JSONObject result = resultDto.getData();
        Long depCode = result.getLong("code");
        return depCode;
    }

    private Map<String, String> buildDependenceNodeParam(String projectCode, Long prevWorkflowCode,
                                                         Long prevTaskCode, Long prevDagId, Long prevJobId,
                                                         JobInfo jobInfo, JobExecuteConfig executeConfig) {
        String taskJson = buildDependenceNodeJson(projectCode, prevWorkflowCode,
                prevTaskCode, prevDagId, prevJobId,
                jobInfo, executeConfig);

        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("taskDefinitionJson", taskJson);
        return paramMap;
    }

    private String buildDependenceNodeJson(String projectCode, Long prevWorkflowCode,
                                           Long prevTaskCode, Long prevDagId, Long prevJobId,
                                           JobInfo jobInfo, JobExecuteConfig executeConfig) {
        JSONObject taskJson = JSONObject.parseObject("{\"code\":null,\"name\":\"depend-node-template-1\",\"description\":\"It is a dependent node\",\"delayTime\":0," +
                "\"taskType\":\"DEPENDENT\",\"taskParams\":{\"dependence\":{\"relation\":\"AND\",\"dependTaskList\":[{\"relation\":\"AND\",\"dependItemList\":[{\"projectCode\":811464712822784,\"definitionCode\":3602075624352,\"depTaskCode\":3602064345376,\"cycle\":\"day\",\"dateValue\":\"today\"}]}]}," +
                "\"conditionResult\":{\"successNode\":[],\"failedNode\":[]},\"waitStartTimeout\":{\"strategy\":\"FAILED\",\"interval\":null,\"checkInterval\":null,\"enable\":false},\"switchResult\":{}}," +
                "\"flag\":\"YES\",\"taskPriority\":\"MEDIUM\",\"workerGroup\":\"default\",\"failRetryTimes\":0,\"failRetryInterval\":1,\"timeoutFlag\":\"CLOSE\",\"timeoutNotifyStrategy\":\"\",\"timeout\":0,\"environmentCode\":-1}");

        String name = "dep#to__t-" + Strings.padStart(prevJobId.toString(), 6, '0')
                + "__w-" + Strings.padStart(prevDagId.toString(), 6, '0')
                + "__f-" + Strings.padStart(jobInfo.getId().toString(), 6, '0');
        taskJson.put("name", name);
        taskJson.put("taskType", "DEPENDENT");
        taskJson.put("flag", "YES");
        taskJson.put("taskPriority", "MEDIUM");
        taskJson.put("workerGroup", getDSWorkGroup(executeConfig.getEnvironment()));
        taskJson.put("timeoutFlag", "CLOSE");
        taskJson.put("timeout", "0");
        taskJson.put("timeoutNotifyStrategy", "");

        JSONObject depItemJson = taskJson.getJSONObject("taskParams")
                .getJSONObject("dependence")
                .getJSONArray("dependTaskList").getJSONObject(0)
                .getJSONArray("dependItemList").getJSONObject(0);
        depItemJson.put("projectCode", projectCode);
        depItemJson.put("definitionCode", prevWorkflowCode.toString());
        depItemJson.put("depTaskCode", prevTaskCode.toString());
        depItemJson.put("cycle", "day");
        depItemJson.put("dateValue", "today");

        JSONArray taskJsonArray = new JSONArray();
        taskJsonArray.add(taskJson);
        return taskJsonArray.toJSONString();
    }

    private Map<String, String> buildRunningParam(Long workflowCode, Long taskCode, JobExecuteConfig executeConfig) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("processDefinitionCode", workflowCode.toString());
        paramMap.put("scheduleTime", null);
        paramMap.put("failureStrategy", "END");
        paramMap.put("warningType", "NONE");
        paramMap.put("warningGroupId", null);
        paramMap.put("execType", null);
        paramMap.put("taskDependType", "TASK_ONLY");
        paramMap.put("runMode", "RUN_MODE_SERIAL");
        paramMap.put("processInstancePriority", "HIGHEST");
        paramMap.put("workerGroup", getDSWorkGroup(executeConfig.getEnvironment()));
        paramMap.put("environmentCode", null);
        paramMap.put("expectedParallelismNumber", null);
        paramMap.put("dryRun", "0");
        paramMap.put("timeout", null);
        paramMap.put("startParams", null);
        paramMap.put("startNodeList", taskCode.toString());
        return paramMap;
    }

    private Map<String, String> buildJobRelationParam(Long workflowCode, Long taskCode, Long prevTaskCode) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("processDefinitionCode", workflowCode.toString());
        paramMap.put("preTaskCode", prevTaskCode.toString());
        paramMap.put("postTaskCode", taskCode.toString());
        return paramMap;
    }

    private Map<String, String> buildBindDagParam(Long workflowCode, Long taskCode) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("processDefinitionCode", workflowCode.toString());
        paramMap.put("preTaskCode", "0");
        paramMap.put("postTaskCode", taskCode.toString());
        return paramMap;
    }

    private Map<String, String> buildUnBindDagParam(Long workflowCode) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("processDefinitionCode", workflowCode.toString());
        return paramMap;
    }

    private Map<String, String> buildJobRunningStatusChangedParam(Integer status) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("releaseState", RunningStateEnum.resume.val == status ? "ONLINE" : "OFFLINE");
        return paramMap;
    }

    private Map<String, String> buildJobReqParam(JobInfo jobInfo, JobExecuteConfig executeConfig) {
        String taskJson = buildTaskJson(jobInfo, executeConfig);

        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("taskDefinitionJson", taskJson);
        return paramMap;
    }

    private String buildTaskJson(JobInfo jobInfo, JobExecuteConfig executeConfig) {
        JSONObject taskJson = JSONObject.parseObject("{\"code\":null,\"name\":\"task-template-1\",\"description\":\"It is a task\",\"taskType\":\"SHELL\"," +
                "\"taskParams\":{\"resourceList\":[],\"localParams\":[],\"rawScript\":\"run_etl\",\"dependence\":{},\"conditionResult\":{\"successNode\":[],\"failedNode\":[]},\"waitStartTimeout\":{},\"switchResult\":{}}," +
                "\"flag\":\"NO\",\"taskPriority\":\"MEDIUM\",\"workerGroup\":\"default\",\"failRetryTimes\":\"0\",\"failRetryInterval\":\"1\"," +
                "\"timeoutFlag\":\"OPEN\",\"timeoutNotifyStrategy\":\"WARN,FAILED\",\"timeout\":30,\"delayTime\":\"0\",\"environmentCode\":-1}");
        taskJson.put("name", buildJobName(jobInfo));
        taskJson.getJSONObject("taskParams").put("rawScript", String.format("run_etl jobId=%d env=%s", jobInfo.getId(), executeConfig.getEnvironment()));
        taskJson.put("flag", "NO");
        taskJson.put("taskPriority", getJobPriority(executeConfig));
        taskJson.put("workerGroup", getDSWorkGroup(executeConfig.getEnvironment()));
        taskJson.put("timeoutFlag", "OPEN");
        taskJson.put("timeout", executeConfig.getSchTimeOut());
        taskJson.put("timeoutNotifyStrategy", getJobTimeoutStrategy(executeConfig));

        JSONArray taskJsonArray = new JSONArray();
        taskJsonArray.add(taskJson);
        return taskJsonArray.toJSONString();
    }

    private String getJobTimeoutStrategy(JobExecuteConfig executeConfig) {
        // 超时策略，alarm：超时告警，fail：超时失败，都有时用,号分隔
        String timeOutStrategy = executeConfig.getSchTimeOutStrategy();
        if (StringUtils.isEmpty(timeOutStrategy)) return null;

        timeOutStrategy = timeOutStrategy.replace("alarm", "WARN");
        timeOutStrategy = timeOutStrategy.replace("fail", "FAILED");
        return timeOutStrategy;
    }

    private String getJobPriority(JobExecuteConfig executeConfig) {
        Optional<JobPriorityEnum> priorityEnumOptional = JobPriorityEnum.getEnum(executeConfig.getSchPriority());
        if (priorityEnumOptional.isEmpty()) return "MEDIUM";

        JobPriorityEnum priorityEnum = priorityEnumOptional.get();
        if (JobPriorityEnum.low == priorityEnum) {
            return "LOW";
        } else if (JobPriorityEnum.middle == priorityEnum) {
            return "MEDIUM";
        } else if (JobPriorityEnum.high == priorityEnum) {
            return "HIGH";
        }
        return "MEDIUM";
    }

    private String buildJobName(JobInfo jobInfo) {
        return jobInfo.getName() + NAME_DELIMITER + Strings.padStart(jobInfo.getId().toString(), 6, '0');
    }

}
