package cn.zhengcaiyun.idata.map.dal.model;

import java.util.Date;
import javax.annotation.Generated;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table map_view_count
 */
public class ViewCount {
    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.id")
    private Long id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.del")
    private Integer del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.creator")
    private String creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.create_time")
    private Date createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.editor")
    private String editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.edit_time")
    private Date editTime;

    /**
     * Database Column Remarks:
     *   实体记录唯一标识
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.entity_code")
    private String entityCode;

    /**
     * Database Column Remarks:
     *   实体记录数据源：数仓表（table） or 数据指标（indicator）
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.entity_source")
    private String entitySource;

    /**
     * Database Column Remarks:
     *   用户编号，0表示全局所有用户的统计数据
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.user_id")
    private Long userId;

    /**
     * Database Column Remarks:
     *   浏览次数
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.view_count")
    private Long viewCount;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.del")
    public Integer getDel() {
        return del;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.del")
    public void setDel(Integer del) {
        this.del = del;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.creator")
    public String getCreator() {
        return creator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.create_time")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.create_time")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.editor")
    public String getEditor() {
        return editor;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.editor")
    public void setEditor(String editor) {
        this.editor = editor;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.edit_time")
    public Date getEditTime() {
        return editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.edit_time")
    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.entity_code")
    public String getEntityCode() {
        return entityCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.entity_code")
    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.entity_source")
    public String getEntitySource() {
        return entitySource;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.entity_source")
    public void setEntitySource(String entitySource) {
        this.entitySource = entitySource;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.user_id")
    public Long getUserId() {
        return userId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.user_id")
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.view_count")
    public Long getViewCount() {
        return viewCount;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: map_view_count.view_count")
    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
}