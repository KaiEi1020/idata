import React, { Fragment, useEffect, useState } from 'react';
import { Button, Modal, message } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from '../../index.less';

import ViewDIM from './ViewDIM';
import EditDIM from './EditDIM';
import { createDimension, getDimension } from '@/services/kpisystem';
import { Dimension } from '@/types/kpisystem';

export interface TabDIMProps {
  initialMode: 'view' | 'edit';
  fileCode: string;
}

const { confirm } = Modal;

const TabDIM: FC<TabDIMProps> = ({ initialMode = 'view', fileCode }) => {
  const [mode, setMode] = useState<'view' | 'edit'>('edit');
  const [loading, setLoading] = useState<boolean>(false);

  const [data, setData] = useState<Dimension>();

  const { getTree, removeTab } = useModel('kpisystem', (_) => ({
    getTree: _.getTree,
    removeTab: _.removeTab,
  }));

  useEffect(() => {
    setMode(initialMode);
    fileCode !== 'newDIM' && getDIMInfo(fileCode);
  }, []);

  const getDIMInfo = (dimensionId: string) => {
    getDimension({ dimensionId })
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {});
  };

  const onSubmit = () => {
    setLoading(true);
    createDimension({})
      .then((res) => {})
      .catch((err) => {})
      .finally(() => setLoading(false));
    getTree('DIMENSION_LABEL');
  };

  const onDelete = () =>
    confirm({
      title: '您确定要删除该维度吗？',
      onOk: () => {},
    });

  const onCancel = () => {
    if (fileCode === 'newTable') {
      removeTab('newTable');
    } else {
      setMode('view');
      getDIMInfo(fileCode);
    }
  };

  return (
    <Fragment>
      {mode === 'view' && <ViewDIM />}
      {mode === 'edit' && <EditDIM />}
      <div className={styles.submit}>
        {mode === 'view' && [
          <Button key="edit" type="primary" onClick={() => setMode('edit')}>
            编辑
          </Button>,
          <Button onClick={onDelete}>删除</Button>,
        ]}
        {mode === 'edit' && [
          <Button key="save" type="primary" onClick={onSubmit} loading={loading}>
            保存
          </Button>,
          <Button key="cancel" onClick={onCancel}>
            取消
          </Button>,
        ]}
      </div>
    </Fragment>
  );
};

export default TabDIM;
