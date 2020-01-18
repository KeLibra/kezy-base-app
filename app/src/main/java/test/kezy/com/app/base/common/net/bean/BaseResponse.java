package test.kezy.com.app.base.common.net.bean;


import test.kezy.libs.common.base.bean.BaseBean;

/**
 */
public class BaseResponse<T>  extends BaseBean {

	public T data;
	public String code; // 状态码
	public String message; // 状态消息
}
