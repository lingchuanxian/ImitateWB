package cn.fjlcx.android.imitatewb.bean;

import java.io.Serializable;

/**
 * Api返回结果的格式统一封装
 * @author ling_cx
 * @date 2017/5/4.
 */
public class HttpResult<T> implements Serializable{
	private int ReturnCode;
	private String Description;
	private T DetailInfo;

	public int getReturnCode() {
		return ReturnCode;
	}

	public void setReturnCode(int returnCode) {
		ReturnCode = returnCode;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public T getDetailInfo() {
		return DetailInfo;
	}

	public void setDetailInfo(T detailInfo) {
		DetailInfo = detailInfo;
	}

	@Override
	public String toString() {
		return "HttpResult{" +
				"ReturnCode=" + ReturnCode +
				", Description='" + Description + '\'' +
				", DetailInfo=" + DetailInfo +
				'}';
	}
}
