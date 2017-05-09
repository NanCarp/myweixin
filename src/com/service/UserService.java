package com.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.JsonKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.UserApi;
import com.weixin.user.GetUserInfo;
import com.weixin.user.UserConfig;
import com.weixin.user.UserConfig.LangType;
import com.weixin.user.UserInfo;


public class UserService {
	
	/**
	 * 获取所有的openid
	 * @return
	 */
	public List<String> getAllOpenId() {
		List<String> openIds = getOpenIds(null);
		return openIds;
	}

	private List<String> getOpenIds(String next_openid) {
		List<String> openIdList = new ArrayList<String>();
		ApiResult apiResult = UserApi.getFollowers(next_openid);
		String json = apiResult.getJson();
		//log.error("json:"+json);
		if (apiResult.isSucceed()) {
			JSONObject result = JSON.parseObject(json);
			next_openid = apiResult.getStr("next_openid");
			int count = apiResult.getInt("count");
			JSONObject openIdObject = result.getJSONObject("openid");
			if (count>0){
				JSONArray openids = openIdObject.getJSONArray("openid");
				for (int i=0; i<openids.size(); i++){
					openIdList.add(openids.getString(i));
				}
				// next page
				if (next_openid!=null && !next_openid.equals("")) {
					List<String> list = getOpenIds(next_openid);
					openIdList.addAll(list);
				}
			}
		}
		return openIdList;
	}
	
	private List<UserInfo> getAllUserInfo(List<String> allOpenId) {
		List<UserInfo> userInfos = new ArrayList<UserInfo>();
		int total = allOpenId.size();
		UserConfig[] user_list = null;
		//
		int temp = 100;
		if (total>temp){
			int page = 0;//当前页面
			int count = total/100 + (total%100>0?1:0);//获取次数
			int index = 0;
			while (page<count){
				index = (temp*(page+1)>total?total:(temp*(page+1)));
				System.out.println("////////"+page*temp+" "+index);
				user_list = new UserConfig[index-(page*temp)];// 当前页面第一个
				for (int i = page*temp; i < index; i++) {
					UserConfig config = new UserConfig();
					config.setLang(LangType.zh_CN);
					config.setOpenid(allOpenId.get(i));
					user_list[i-(page*temp)] = config;
				}
				GetUserInfo getUserInfo = new GetUserInfo();
				getUserInfo.setUser_list(user_list);
				String jsonGetUserInfo = JsonKit.toJson(getUserInfo);
				System.out.println("jsonGetUserInfo" + jsonGetUserInfo);
				
				ApiResult apiResult = UserApi.batchGetUserInfo(jsonGetUserInfo);
				
				String jsonResult = apiResult.getJson();
				// json 转化为对象
				List<UserInfo> userInfo = parseJsonToUserInfo(jsonResult);
				userInfos.addAll(userInfo);
				page++;
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		return null;
		
	}
	
	
}
