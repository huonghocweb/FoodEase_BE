package poly.foodease.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.foodease.Model.Entity.BlogComment;

public interface BlogCommentRepo extends JpaRepository<BlogComment, Integer> {
        List<BlogComment> findByBlog_BlogId(Integer blogId);
}
