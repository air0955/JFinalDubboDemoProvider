package cn.gh.duboo.demo.provider.config;

import cn.gh.duboo.demo.controller.IndexController;
import cn.gh.duboo.demo.model.Blog;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.spring.SpringPlugin;
import com.jfinal.render.ViewType;

public class DemoProviderConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		loadPropertyFile("conn.properties");
		me.setDevMode(getPropertyToBoolean("devMode", false));
		me.setViewType(ViewType.JSP);
	}

	
	@Override
	public void configHandler(Handlers me) {
		// 声明Druid监控页面URL
		me.add(new DruidStatViewHandler("/druid"));
	}

	@Override
	public void configInterceptor(Interceptors me) {
	}

	@Override
	public void configPlugin(Plugins me) {
		// 配置Druid数据库连接池插件
		DruidPlugin dp = new DruidPlugin(getProperty("jdbcUrl"),
				getProperty("user"), getProperty("password").trim());

		dp.setDriverClass(getProperty("jdbcdrive"));
		StatFilter stat = new StatFilter();
		stat.setMergeSql(true);
		dp.addFilter(stat);

		WallFilter wall = new WallFilter();
		wall.setDbType(JdbcConstants.ORACLE);

		dp.addFilter(wall);
		me.add(dp);

		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);

		arp.setShowSql(getPropertyToBoolean("devMode", false));
		arp.setDevMode(getPropertyToBoolean("devMode", false));
		
//		  C3p0Plugin cp = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
//		  me.add(cp);
//		  cp.setDriverClass(getProperty("jdbcdrive"));
//		  ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
//		  me.add(arp);
//		  arp.setDialect(new OracleDialect());
//		  arp.setContainerFactory(new CaseInsensitiveContainerFactory());
		
		arp.addMapping("blog", Blog.class); // 映射blog 表到 Blog模型
		arp.setDialect(new OracleDialect());
		arp.setContainerFactory(new CaseInsensitiveContainerFactory());
		me.add(arp);
		// 配置Spring插件
		SpringPlugin sp = new SpringPlugin();

		// 加入各插件到Config
		me.add(sp);
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/", IndexController.class);
	}

	@Override
	public void afterJFinalStart() {
		System.out.println("Demo provider for Dubbo启动完成");
	}
}
