import React, { useEffect } from 'react';
import { Form } from 'antd';
import { history } from 'umi';
import { createRole } from '@/services/role';
// import { saveFn } from '@/utils/utils';
import useSave from '@/hooks/useSave';
import useAuthSetting from '../../hooks/useAuthSetting';
import RoleConf from '../../components/RoleConf';

const Create = () => {
  const [form] = Form.useForm();
  const { fetchData, authSettingProps } = useAuthSetting();
  useEffect(() => {
    fetchData();
  }, []);
  const { btnProps } = useSave(
    async () => {
      const values = await form.validateFields();
      const { folderTree, origFeatureTree } = authSettingProps;
      return createRole({ folderTree, featureTree: origFeatureTree, roleName: values.roleName });
    },
    () => history.push('/authority/role/list'),
  );

  const roleConfProps = { authSettingProps, form, saveBtnProps: btnProps  };
  return <RoleConf {...roleConfProps} />;
};

export default Create;
