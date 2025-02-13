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
package cn.zhengcaiyun.idata.portal.controller.dev;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDefineDto;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.system.dto.ConfigDto;
import cn.zhengcaiyun.idata.user.service.TokenService;
import cn.zhengcaiyun.idata.user.service.UserAccessService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * dev-label-controller
 * @author caizhedong
 * @date 2021-05-18 20:05
 */

@RestController
@RequestMapping(path = "/p1/dev")
public class LabelController {

    @Autowired
    private LabelService labelService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserAccessService userAccessService;

    private final String CONFIG_METADATA_ACCESS_CODE = "F_MENU_METADATA_CONFIG";

    @GetMapping("labelDefine")
    public RestResult<LabelDefineDto> findDefine(@RequestParam("labelCode") String labelCode) {
        return RestResult.success(labelService.findDefine(labelCode));
    }

    @GetMapping("labelDefines/{labelDefineId}")
    public RestResult<LabelDefineDto> getLabelDefine(@PathVariable(value = "labelDefineId") Long labelDefineId) {
        String labelCode = labelService.getLabelDefineCode(labelDefineId);
        checkArgument(StringUtils.isNotBlank(labelCode), "标签编号不存在");
        return RestResult.success(labelService.findDefine(labelCode));
    }

    @GetMapping("labelDefines")
    public RestResult<List<LabelDefineDto>> findDefines(@RequestParam(value = "subjectType", required = false) String subjectType,
                                                        @RequestParam(value = "labelTag", required = false) String labelTag) throws IllegalAccessException {
        if (!userAccessService.checkAccess(OperatorContext.getCurrentOperator().getId(), CONFIG_METADATA_ACCESS_CODE)) {
            throw new IllegalAccessException("没有元数据标签配置权限");
        }
        return RestResult.success(labelService.findDefines(subjectType, labelTag));
    }

    @PostMapping("labelDefine")
    public RestResult<LabelDefineDto> defineLabel(@RequestBody LabelDefineDto labelDefineDto,
                                                  HttpServletRequest request) {
        return RestResult.success(labelService.defineLabelAndEnum(labelDefineDto, tokenService.getNickname(request)));
    }

    @DeleteMapping("labelDefine")
    public RestResult deleteDefine(@RequestParam("labelCode") String labelCode,
                                   HttpServletRequest request) {
        return RestResult.success(labelService.deleteDefine(labelCode, tokenService.getNickname(request)));
    }
}
