package cn.zhengcaiyun.idata.label.compute.sql.model;

import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 14:49
 **/
public class GroupByModel implements ModelRender {
    private final List<BaseColumn> columnList;

    private GroupByModel() {
        columnList = Lists.newArrayList();
    }

    public static GroupByModel of() {
        return new GroupByModel();
    }

    public GroupByModel addColumn(BaseColumn... columns) {
        if (columns != null && columns.length > 0) {
            for (BaseColumn column : columns) {
                columnList.add(column);
            }
        }
        return this;
    }

    @Override
    public String renderSql() {
        checkArgument(!CollectionUtils.isEmpty(this.columnList), "columnList is empty.");
        StringBuilder builder = new StringBuilder();
        builder.append("group by ");
        int count = 0;
        for (BaseColumn column : this.columnList) {
            if (count > 0) {
                builder.append(", ");
            }
            builder.append(column.renderSqlWithAlias());
        }
        return builder.toString();
    }
}
