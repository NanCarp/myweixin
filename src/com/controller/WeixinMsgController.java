package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.CustomServiceApi;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import com.jfinal.weixin.sdk.api.TemplateData;
import com.jfinal.weixin.sdk.api.TemplateMsgApi;
import com.jfinal.weixin.sdk.api.CustomServiceApi.Articles;
import com.jfinal.weixin.sdk.jfinal.MsgControllerAdapter;
import com.jfinal.weixin.sdk.msg.in.InImageMsg;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMenuEvent;
import com.jfinal.weixin.sdk.msg.out.OutImageMsg;
import com.jfinal.weixin.sdk.msg.out.OutNewsMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;

public class WeixinMsgController extends MsgControllerAdapter {
	static Log logger = Log.getLog(WeixinMsgController.class);
	private static final String helpStr = "\t你的品位不错哦  么么哒。";

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

	@Override
	protected void processInFollowEvent(InFollowEvent inFollowEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processInTextMsg(InTextMsg inTextMsg) {
		String msgContent = inTextMsg.getContent().trim();
		// 帮助提示
		if ("help".equalsIgnoreCase(msgContent) || "帮助".equals(msgContent)) {
			OutTextMsg outMsg = new OutTextMsg(inTextMsg);
			outMsg.setContent(helpStr);
			render(outMsg);
		} else if (msgContent.equals("1") || msgContent.equals("人脸识别")) {
			msgContent = "请发一张清晰的照片！";
			renderOutTextMsg(msgContent);
		} else if (msgContent.equals("9") || "QQ咨询".equalsIgnoreCase(msgContent)) {
			String url = "http://wpa.qq.com/msgrd?v=3&uin=1472405080&site=qq&menu=yes";
			String urlStr = "<a href=\"" + url + "\">点击咨询</a>";
			renderOutTextMsg("QQ在线咨询" + urlStr);
		} else if (msgContent.equals("微信支付")) {
			String url = "http://javen.ngrok.natapp.cn/pay?openId=o_pncsidC-pRRfCP4zj98h6slREw";
			String urlStr = "<a href=\"" + url + "\">微信支付测试</a>";
			renderOutTextMsg(urlStr);
		} else if (msgContent.equals("微信支付测试")) {
			String url = "http://javen.ngrok.natapp.cn/paytest?openId=o_pncsidC-pRRfCP4zj98h6slREw";
			String urlStr = "<a href=\"" + url + "\">微信支付测试</a>";
			renderOutTextMsg(urlStr);
		} else if (msgContent.equals("8")) {
			String calbackUrl = PropKit.get("domain") + "/oauth";
			String url = SnsAccessTokenApi.getAuthorizeURL(PropKit.get("appId"), calbackUrl, "111", false);
			String urlStr = "<a href=\"" + url + "\">点击我授权</a>";
			System.out.println("urlStr " + urlStr);
			renderOutTextMsg("授权地址" + urlStr);
		} else if ("jssdk".equalsIgnoreCase(msgContent)) {
			String url = PropKit.get("domain") + "/jssdk";
			String urlStr = "<a href=\"" + url + "\">JSSDK</a>";
			renderOutTextMsg("地址" + urlStr);
		} else if ("模板消息".equalsIgnoreCase(msgContent)) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
			String time = sdf.format(new Date());
			String json = TemplateData.New().setTouser(inTextMsg.getFromUserName())
					.setTemplate_id("BzC8RvHu1ICOQfO4N7kp6EWz9VAbISJjV2fO5t7MiXE").setTopcolor("#743A3A")
					.setUrl("http://www.cnblogs.com/zyw-205520/tag/%E5%BE%AE%E4%BF%A1/")
					.add("first", "您好,你已购买课程成功", "#743A3A").add("keyword1", "微信公众号开发公开课", "#0000FF")
					.add("keyword2", "免费", "#0000FF").add("keyword3", "Javen205", "#0000FF")
					.add("keyword4", time, "#0000FF").add("remark", "请点击详情直接看课程直播，祝生活愉快", "#008000").build();
			System.out.println(json);
			ApiResult result = TemplateMsgApi.send(json);

			System.out.println(result.getJson());

			renderNull();
		} else if ("异步回复多个消息".equals(msgContent)) {
			final String toUser = inTextMsg.getFromUserName();
			new Thread(new Runnable() {

				@Override
				public void run() {

					ApiConfigKit.setThreadLocalApiConfig(getApiConfig());

					ApiResult sendText = CustomServiceApi.sendText(toUser, "客服消息");

					System.out.println(sendText.getJson());

					List<Articles> list = new ArrayList<Articles>();

					Articles articles1 = new Articles();
					articles1.setTitle("测试异步回复多个消息");
					articles1.setDescription("客服多图文消息");
					articles1.setPicurl(
							"http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1609/27/c0/27587202_1474952311163_800x600.jpg");
					articles1.setUrl("http://www.cnblogs.com/zyw-205520/tag/%E5%BE%AE%E4%BF%A1/");

					Articles articles2 = new Articles();
					articles2.setTitle("微信买单、刷卡、扫码、公众号支付");
					articles2.setDescription("微信支付教程");
					articles2.setPicurl(
							"http://desk.fd.zol-img.com.cn/t_s960x600c5/g4/M01/0D/04/Cg-4WVP_npmIY6GRAKcKYPPMR3wAAQ8LgNIuTMApwp4015.jpg");
					articles2.setUrl("http://www.jianshu.com/notebooks/2736169/latest");

					list.add(articles2);
					list.add(articles1);

					CustomServiceApi.sendNews(toUser, list);

				}
			}).start();

			// 回复被动响应消息
			renderOutTextMsg("你发的内容为：" + msgContent);

		}

		else {
			renderOutTextMsg("你发的内容为：" + msgContent);
			// 转发给多客服PC客户端
			// OutCustomMsg outCustomMsg = new OutCustomMsg(inTextMsg);
			// render(outCustomMsg);
		}

	}

	protected void processInImageMsg(InImageMsg inImageMsg) {
		OutImageMsg outMsg = new OutImageMsg(inImageMsg);
		// 将刚发过来的图片再发回去
		outMsg.setMediaId(inImageMsg.getMediaId());
		render(outMsg);
	}

	@Override
	protected void processInMenuEvent(InMenuEvent inMenuEvent) {
		// TODO Auto-generated method stub

	}

}
