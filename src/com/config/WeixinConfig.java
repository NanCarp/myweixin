package com.config;


import javax.servlet.http.HttpServletRequest;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.controller.IndexController;
import com.controller.WeixinApiController;
import com.controller.WeixinMsgController;
import com.controller.WeixinOauthController;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.druid.IDruidStatViewAuth;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.model.Users;

public class WeixinConfig extends JFinalConfig {
	static Log log = Log.getLog(WeixinMsgController.class);

	@Override
	public void configConstant(Constants me) {
		PropKit.use("config.txt");
		me.setDevMode(true);
		ApiConfigKit.setDevMode(me.getDevMode());
	}

	@Override
	public void configRoute(Routes me) {

		/*************** 微信 **************/
		me.add("/msg", WeixinMsgController.class);
		me.add("/api", WeixinApiController.class);
		me.add("/oauth", WeixinOauthController.class);

		me.add("/", IndexController.class, "/front");
	}

	@Override
	public void configPlugin(Plugins me) {
		// 配置ActiveRecord插件
		DruidPlugin druidPlugin = createDruidPlugin();
		me.add(druidPlugin);

		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		arp.addMapping("users", "id", Users.class);
		arp.setShowSql(PropKit.getBoolean("devMode", false));
		me.add(arp);

/*		// ehcahce插件配置
		me.add(new EhCachePlugin());

		SchedulerPlugin sp = new SchedulerPlugin("job.properties");
		me.add(sp);*/
	}

	public static DruidPlugin createDruidPlugin() {
		String jdbcUrl = PropKit.get("jdbcUrl");
		String user = PropKit.get("user");
		String password = PropKit.get("password");
		log.error(jdbcUrl + " " + user + " " + password);
		// 配置druid数据连接池插件
		DruidPlugin dp = new DruidPlugin(jdbcUrl, user, password);
		// 配置druid监控
		dp.addFilter(new StatFilter());
		WallFilter wall = new WallFilter();
		wall.setDbType("mysql");
		dp.addFilter(wall);
		return dp;
	}
	
	@Override
	public void configInterceptor(Interceptors me) {
	}

	@Override
	public void configHandler(Handlers me) {
		// Druid监控
		DruidStatViewHandler dvh = new DruidStatViewHandler("/druid", new IDruidStatViewAuth() {

			@Override
			public boolean isPermitted(HttpServletRequest request) {
				return true;
			}
		});
		me.add(dvh);
	}

	@Override
	public void afterJFinalStart() {
		// TODO Auto-generated method stub
		// super.afterJFinalStart();
		// timer.schedule(new TaskService(), 1000*60*60*6, 1000*60*60*24*4);
	}

	@Override
	public void beforeJFinalStop() {
		// TODO Auto-generated method stub
		// super.beforeJFinalStop();
		// timer.cancel();
	}
}