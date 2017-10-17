package cn.fjlcx.android.imitatewb.api;

/**
 * Api异常的处理
 * @author ling_cx
 * @date 2017/6/7.
 */

public class ApiException extends RuntimeException{

	public static final int LOGINFAIL = 1;				//	登录失败
	public static final int UNLOGIN = 2;  				//	用户未登录
	public static final int INTERFACEERROR = 3;  		//	子接口不存在或初始化异常
	public static final int NOPERMISSION = 4;			//	权限不合法
	public static final int PARAMSERROR = 5;			//	参数格式不正确
	public static final int OTHER = 99;					//  其他原因
	private static String message;

	public ApiException(int resultCode) {
		this(getApiExceptionMessage(resultCode));
	}

	public ApiException(String detailMessage) {
		super(detailMessage);
		message = detailMessage;
	}

	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
	 * 需要根据错误码对错误信息进行一个转换，在显示给用户
	 * @param code
	 * @return
	 */
	private static String getApiExceptionMessage(int code){
		switch (code) {
			case LOGINFAIL:
				message = "登录失败";
				break;
			case UNLOGIN:
				message = "用户未登录";
				break;
			case INTERFACEERROR:
				message = "子接口不存在或初始化异常";
				break;
			case NOPERMISSION:
				message = "权限不合法";
				break;
			case PARAMSERROR:
				message = "参数格式不正确";
				break;
			case OTHER:
				message = "其他原因";
				break;
			default:
				message = "未知错误";
		}
		return message;
	}
}
