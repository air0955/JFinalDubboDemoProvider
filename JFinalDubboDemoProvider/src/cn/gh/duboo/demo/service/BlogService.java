package cn.gh.duboo.demo.service;

import cn.gh.duboo.demo.model.Blog;

import com.jfinal.plugin.activerecord.Page;

public interface BlogService {
	Page<Blog> paginate(int pageNumber, int pageSize);

	void update(Blog blog);

	Blog save(Blog blog);

	Blog findById(String id);

	void deleteById(String id);
}
