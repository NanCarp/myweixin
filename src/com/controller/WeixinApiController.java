package com.controller;

import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.MenuApi;
import com.jfinal.weixin.sdk.jfinal.ApiController;
import com.utils.Constant;

public class WeixinApiController extends ApiController {
	public void index() {
		render("/api/index.html");
	}

	@Override
	public ApiConfig getApiConfig() {
		ApiConfig ac = new ApiConfig();
		// 配置微信 API 相关常量
		ac.setToken(PropKit.get("token"));
		ac.setAppId(PropKit.get("appId"));
		ac.setAppSecret(PropKit.get("appSecret"));
		ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
		return ac;
	}

	/**
	 * 获取公众号菜单
	 */
	public void getMenu() {
		ApiResult apiResult = MenuApi.getMenu();
		if (apiResult.isSucceed())
			renderText(apiResult.getJson());
		else
			renderText(apiResult.getErrorMsg());
	}

	// 创建自定义菜单
	public void createmenu() {
		String path = Constant.getHost;
		String jsonstr = "{" + "    \"button\": [" + "        {" + "            \"name\": \"鲜花订阅\","
				+ "            \"sub_button\": ["
				+ "			 	{\"name\": \"多品鲜花订阅 | 59.99元\",\"type\": \"view\",\"url\": \"" + path + "/product/2\"},"
				+ "			 	{\"name\": \"双品鲜花订阅 | 39.99元\",\"type\": \"view\",\"url\": \"" + path + "/product/1\"},"
				+ "				{\"name\": \"我要送花| 69.99元\",\"type\": \"view\",\"url\": \"" + path + "/product/3\"},"
				+ "				{\"name\": \"花边好物 | 花瓶 花剪\",\"type\": \"view\",\"url\": \"" + path + "/around\"},"
				+ "				{\"name\": \"轻松赚花票\",\"type\": \"view\",\"url\": \"" + path + "/account/invitefri\"}]"
				+ "        }," + "        {" + "            \"name\": \"小美秘密\"," + "            \"sub_button\": ["
				+ "            	{\"name\": \"生活美学\",\"type\": \"view\",\"url\": \"" + path + "/esthetics\"},"
				+ "				{\"name\": \"养护 | 搭配\",\"type\": \"view\",\"url\": \"" + path + "/knowledge\"},"
				+ "				{\"name\": \"晒 晒 晒\",\"type\": \"view\",\"url\": \"http://www.webei.cn/1b434a4e7d\"},"
				+ "           	{\"name\": \"我要带颜\",\"type\": \"view\",\"url\": \"" + path + "/account/invitefri\"}]"
				+ "        }," + "        {" + "    		 \"name\": \"为您服务\"," + "    		 \"sub_button\": ["
				+ "			 	{\"name\": \"会员中心\",\"type\": \"view\",\"url\": \"" + path + "/account/center\"},"
				+ "			 	{\"name\": \"在线客服\",\"type\": \"click\",\"key\": \"32\"},"
				+ "           	{\"name\": \"联系我们\",\"type\": \"view\",\"url\": \"" + path + "/contactus\"},"
				+ "			 	{\"name\": \"物流查询\",\"type\": \"view\",\"url\": \"" + path + "/logistics_query\"},"
				+ "			 	{\"name\": \"常见问题\",\"type\": \"view\",\"url\": \"" + path + "/question\"}]"
				+ "        }" + "    ]" + "}";
		ApiResult apiResult = MenuApi.createMenu(jsonstr);
		renderText(apiResult.getJson());
	}
}
