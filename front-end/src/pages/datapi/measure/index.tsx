import React from 'react';
import { PageContainer } from '@/components';
import SplitPane from 'react-split-pane';
import type { FC } from 'react';
import styles from './index.less';

import FolderTree from './components/FolderTree';
import Workbench from './components/Workbench';

const measure: FC = () => {
  return (
    <PageContainer contentClassName={styles['kpi-system']}>
      <SplitPane className={styles.board} defaultSize={300} style={{ position: 'relative' }}>
        <div className={styles.left}>
          <FolderTree />
        </div>
        <div className={styles.right}>
          <Workbench />
        </div>
      </SplitPane>
    </PageContainer>
  );
};

export default measure;
