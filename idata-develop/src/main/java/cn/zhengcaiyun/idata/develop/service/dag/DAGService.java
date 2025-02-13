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

package cn.zhengcaiyun.idata.develop.service.dag;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.develop.condition.dag.DAGInfoCondition;
import cn.zhengcaiyun.idata.develop.dto.dag.DAGDto;
import cn.zhengcaiyun.idata.develop.dto.dag.DAGInfoDto;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-26 13:41
 **/
public interface DAGService {

    Long addDAG(DAGDto dto, Operator operator) throws IllegalAccessException;

    Boolean editDAG(DAGDto dto, Operator operator) throws IllegalAccessException;

    DAGDto getDag(Long id);

    Boolean removeDag(Long id, Operator operator) throws IllegalAccessException;

    Boolean online(Long id, Operator operator);

    Boolean offline(Long id, Operator operator);

    Boolean saveDependence(Long id, List<Long> dependenceIds, Operator operator);

    List<DAGInfoDto> getDAGInfoList(DAGInfoCondition condition);

}
