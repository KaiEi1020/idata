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
package cn.zhengcaiyun.idata.sql.model;

import cn.zhengcaiyun.idata.sql.model.condition.BaseCondition;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 14:48
 **/
public class WhereModel implements ModelRender {
    private final BaseCondition firstConditionOfChain;

    private WhereModel(BaseCondition conditionChain) {
        this.firstConditionOfChain = conditionChain;
    }

    @Override
    public String renderSql() {
        return "where " + firstConditionOfChain.renderSql();
    }

    public static WhereModel of(BaseCondition conditionChain) {
        return new WhereModel(conditionChain);
    }

    public WhereModel and(BaseCondition condition) {
        BaseCondition lastCond = firstConditionOfChain;
        while (lastCond.hasNextCondition()) {
            lastCond = lastCond.nextCondition();
        }
        lastCond.and(condition);
        return this;
    }
}
