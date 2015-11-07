package test.bean;

import halo.query.annotation.Column;
import halo.query.annotation.Table;
import halo.query.model.BaseModel;

/**
 * Created by akwei on 9/28/14.
 */
@Table(name = "tb_user_seq", dalParser = TbUserIdSeqParser.class)
public class UserSeq extends BaseModel {

    @Column("seq_id")
    private int seq_id;

    public int getSeq_id() {
        return seq_id;
    }

    public void setSeq_id(int seq_id) {
        this.seq_id = seq_id;
    }
}
