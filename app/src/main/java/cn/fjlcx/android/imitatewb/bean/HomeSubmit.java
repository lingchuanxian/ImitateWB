package cn.fjlcx.android.imitatewb.bean;

import java.math.BigInteger;

/**
 * class description here
 *
 * @author ling_cx
 * @date 2017/10/17
 */

public class HomeSubmit {
	private String access_token;
	private BigInteger since_id;
	private BigInteger max_id;
	private int count;
	private int page;
	private int base_app;
	private int feature;
	private int trim_user;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public BigInteger getSince_id() {
		return since_id;
	}

	public void setSince_id(BigInteger since_id) {
		this.since_id = since_id;
	}

	public BigInteger getMax_id() {
		return max_id;
	}

	public void setMax_id(BigInteger max_id) {
		this.max_id = max_id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getBase_app() {
		return base_app;
	}

	public void setBase_app(int base_app) {
		this.base_app = base_app;
	}

	public int getFeature() {
		return feature;
	}

	public void setFeature(int feature) {
		this.feature = feature;
	}

	public int getTrim_user() {
		return trim_user;
	}

	public void setTrim_user(int trim_user) {
		this.trim_user = trim_user;
	}
}
