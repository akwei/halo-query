package test;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;

import java.util.Date;

@Table(name = "role")
public class Role extends BaseRole {

    @Id
    @Column("role_id")
    private int roleId;

    @Column("role_name")
    private String roleName = "";

    @Column("role_desc")
    private String roleDesc = "";

    @Column("parent_id")
    private int parentId;

    @Column
    private String hierarchy = "";

    @Column("leaf_flag")
    private int leafFlag;

    @Column("creater_id")
    private int createrId;

    @Column("create_time")
    private Date createTime;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(String hierarchy) {
        this.hierarchy = hierarchy;
    }

    public int getLeafFlag() {
        return leafFlag;
    }

    public void setLeafFlag(int leafFlag) {
        this.leafFlag = leafFlag;
    }

    public int getCreaterId() {
        return createrId;
    }

    public void setCreaterId(int createrId) {
        this.createrId = createrId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
