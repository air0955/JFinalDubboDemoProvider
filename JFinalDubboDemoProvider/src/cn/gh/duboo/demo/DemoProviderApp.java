package cn.gh.duboo.demo;

import cn.gh.duboo.demo.model.Blog;

import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.spring.SpringPlugin;

public class DemoProviderApp {

	public static void main(String[] args) throws InterruptedException {
		// 读取配置文件
		Prop p = PropKit.use("duboo_demo_provider_config.txt", "utf-8");

		// 配置Druid数据库连接池插件
		DruidPlugin dp = new DruidPlugin(p.get("jdbcUrl"), p.get("user"), p
				.get("password").trim());

		WallFilter wall = new WallFilter();
		wall.setDbType(JdbcConstants.MYSQL);
		dp.addFilter(wall);

		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);

		arp.addMapping("blog", Blog.class); // 映射blog 表到 Blog模型
		arp.setDialect(new MysqlDialect());
		arp.setShowSql(p.getBoolean("devMode", false));
		arp.setDevMode(p.getBoolean("devMode", false));

		// 配置Spring插件
		SpringPlugin sp = new SpringPlugin(
				"src/main/webapp/WEB-INF/applicationContext.xml");

		// 手动启动各插件
		dp.start();
		arp.start();
		sp.start();

		System.out.println("Demo provider for Dubbo启动完成。");
		
		// 没有这一句，启动到这服务就退出了
		Thread.currentThread().join();
	}
}
