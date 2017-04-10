package com.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import com.vo.AjaxResult;

public class IndexController extends Controller {
	private AjaxResult result = new AjaxResult();

	public void toOauth() {
		String calbackUrl = PropKit.get("domain") + "/oauth";
		String url = SnsAccessTokenApi.getAuthorizeURL(PropKit.get("appId"), calbackUrl, "111", false);
		redirect(url);
	}

	public void index() {
		String openId = getSessionAttr("openId");

		setAttr("openId", openId);
		// 查询最新课程
		// List<Coures> list = Coures.dao.getCouresByTop(2);
		// setAttr("coures", list);
		render("index.html");
	}
	
	public void login() {
		String openId = getSessionAttr("openId");
		if (StrKit.isBlank(openId)) {
			
		}
		System.out.println("login openId: " + openId);
	}
	
	
	
	
	
}
