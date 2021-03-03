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
package cn.zhengcaiyun.idata.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author shiyin
 * @date 2021-03-03 00:23
 */
public class Page<T> {
    @ApiModelProperty(required = true)
    private List<T> content;
    @ApiModelProperty(required = true)
    private int total;

    public static <T> Page newOne(List<T> content, int total) {
        Page<T> p = new Page<>();
        p.setContent(content);
        p.setTotal(total);
        return p;
    }

    // GaS
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
