package test.bean;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;
import halo.query.dal.DALStatus;
import halo.query.model.BaseModel;

/**
 * Created by akwei on 9/28/14.
 */
@Table(name = "tb_user", dalParser = TbUserParser.class)
public class TbUser extends BaseModel {

    @Id
    @Column("userid")
    private int userId;

    @Column
    private String name;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void create() {
        DALStatus.addParam("userId", this.userId);
        super.create();
    }
}
