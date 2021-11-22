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

package cn.zhengcaiyun.idata.develop.service.job;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.develop.dto.job.JobDryRunDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobInfoDto;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-26 17:06
 **/
public interface JobInfoService {

    Long addJobInfo(JobInfoDto dto, Operator operator);

    Boolean editJobInfo(JobInfoDto dto, Operator operator);

    JobInfoDto getJobInfo(Long id);

    Boolean removeJobInfo(Long id, Operator operator);

    Boolean enableJobInfo(Long id, Operator operator);

    Boolean disableJobInfo(Long id, Operator operator);

    Boolean runJob(Long id, Operator operator);

    JobDryRunDto dryRunJob(Long jobId, Integer version);

}
