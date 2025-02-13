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
package cn.zhengcaiyun.idata.user.service;

import cn.zhengcaiyun.idata.system.dto.ConfigDto;
import cn.zhengcaiyun.idata.system.service.SystemConfigService;
import cn.zhengcaiyun.idata.user.dto.UserInfoDto;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shiyin
 * @date 2021-03-11 17:26
 */
@Service
public class LdapService implements InitializingBean {

    private static final String LDAP_CONFIG_KEY = "ldap-config";
    private static final String LDAP_DOMAIN_KEY = "ldap.domain";
    private static final String LDAP_URL_KEY = "ldap.url";
    private static final String LDAP_BASE_KEY = "ldap.base";
    private static final String LDAP_USER_DN_KEY = "ldap.userDn";
    private static final String LDAP_PASSWORD_KEY = "ldap.password";

    private LdapTemplate ldapTemplate;

    @Autowired
    private SystemConfigService systemConfigService;

    @Override
    public void afterPropertiesSet() throws Exception {
        ConfigDto ldapConfig = systemConfigService.getSystemConfigByKey(LDAP_CONFIG_KEY);
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(ldapConfig.getValueOne().get(LDAP_URL_KEY).getConfigValue());
        contextSource.setBase(ldapConfig.getValueOne().get(LDAP_BASE_KEY).getConfigValue());
        contextSource.setUserDn(ldapConfig.getValueOne().get(LDAP_USER_DN_KEY).getConfigValue());
        contextSource.setPassword(ldapConfig.getValueOne().get(LDAP_PASSWORD_KEY).getConfigValue());
        contextSource.afterPropertiesSet();
        this.ldapTemplate = new LdapTemplate(contextSource);
        ldapTemplate.setIgnorePartialResultException(true);
    }

    public boolean checkUser(String username, String password) {
        ConfigDto ldapConfig = systemConfigService.getSystemConfigByKey(LDAP_CONFIG_KEY);
        DirContext ctx = null;
        try {
            ctx = ldapTemplate.getContextSource().getContext(username
                    + ldapConfig.getValueOne().get(LDAP_DOMAIN_KEY).getConfigValue(), password);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            LdapUtils.closeContext(ctx);
        }
        return true;
    }

    public UserInfoDto findUserByUsername(String userName) {
        List<UserInfoDto> users = ldapTemplate.search(LdapQueryBuilder.query()
                        .where("objectclass").is("person")
                        .and("sAMAccountName").is(userName),
                this::toUser);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        }
        return users.get(0);
    }

    public List<UserInfoDto> searchUserByDisplayName(String key) {
        List<UserInfoDto> users = ldapTemplate.search(LdapQueryBuilder.query()
                        .where("objectclass").is("person")
                        .and("displayName").like(String.format("*%s*", key)),
                this::toUser);
        if (users == null) {
            return new ArrayList<>();
        }
        if (users.size() == 1 && users.get(0) == null) {
            return new ArrayList<>();
        }
        return users;
    }

    private UserInfoDto toUser(Attributes attributes) throws NamingException {
        try {
            UserInfoDto user = new UserInfoDto();
            user.setUsername(attributes.get("name").get().toString());
            user.setNickname(attributes.get("displayName").get().toString());
            user.setEmployeeId(attributes.get("employeeID").get().toString());
            user.setRealName(attributes.get("displayNamePrintable").get().toString());
            user.setAvatar(attributes.get("url").get().toString());
            user.setEmail(attributes.get("mail").get().toString());
            user.setMobile(attributes.get("mobile").get().toString());
//            user.setDepartment(attributes.get("ou").get().toString());
            return user;
        } catch (NullPointerException e) {
            return null;
        }
    }

}
