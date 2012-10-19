package test;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;
import halo.query.model.BaseModel;
import halo.query.model.HaloModel;
import java.util.Date;

@HaloModel
@Table(name = "ewallet.merchant_template")
public class MerchantTemplate extends BaseModel {

	/**
	 * ID
	 */
	@Id
	@Column("id")
	private int id;

	/**
	 * 商户code
	 */
	@Column("merchant_code")
	private int merchant_code;

	/**
	 * 费率类型 0 支付 1退款
	 */
	@Column("rate_type")
	private int rate_type;

	/**
	 * 商品编号
	 */
	@Column("mcc_id")
	private int mcc_id;

	/**
	 * 支付渠道 号 web端，手机端等
	 */
	@Column("channel")
	private int channel;

	/**
	 * 支付类型 钱包 or 卡
	 */
	@Column("pay_type")
	private int pay_type;

	/**
	 * 1.单笔固定金额 2.单笔费率，支持上下线
	 */
	@Column("bill_type")
	private int bill_type;

	/**
	 * 费率内容 1.如果是单笔固定金额，直接数字金额 2.如果是单笔费率格式5%#100#500 ,上下线在100-500元之间
	 */
	@Column("bill")
	private String bill;

	/**
	 * 模板有效期开始时间
	 */
	@Column("start_time")
	private Date start_time;

	/**
	 * 模板有效期结束时间
	 */
	@Column("end_time")
	private Date end_time;

	/**
	 * 创建时间
	 */
	@Column("create_time")
	private Date create_time;

	/**
	 * 最后更新时间
	 */
	@Column("update_time")
	private Date update_time;

	/**
	 * 是否为默认模板，默认为0,否
	 */
	@Column("is_defalut")
	private int is_defalut;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMerchant_code() {
		return merchant_code;
	}

	public void setMerchant_code(int merchantCode) {
		merchant_code = merchantCode;
	}

	public int getRate_type() {
		return rate_type;
	}

	public void setRate_type(int rateType) {
		rate_type = rateType;
	}

	public int getMcc_id() {
		return mcc_id;
	}

	public void setMcc_id(int mccId) {
		mcc_id = mccId;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public int getPay_type() {
		return pay_type;
	}

	public void setPay_type(int payType) {
		pay_type = payType;
	}

	public int getBill_type() {
		return bill_type;
	}

	public void setBill_type(int billType) {
		bill_type = billType;
	}

	public String getBill() {
		return bill;
	}

	public void setBill(String bill) {
		this.bill = bill;
	}

	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date startTime) {
		start_time = startTime;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date endTime) {
		end_time = endTime;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date createTime) {
		create_time = createTime;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date updateTime) {
		update_time = updateTime;
	}

	public int getIs_defalut() {
		return is_defalut;
	}

	public void setIs_defalut(int isDefalut) {
		is_defalut = isDefalut;
	}

	/**
	 * 查询
	 * 
	 * @param id
	 * @return
	 */
	public MerchantTemplate queryById(int id) {
		return query.objById(MerchantTemplate.class, id);
	}
}
