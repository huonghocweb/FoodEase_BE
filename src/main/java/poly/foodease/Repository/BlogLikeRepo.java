package poly.foodease.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.foodease.Model.Entity.BlogLike;

public interface BlogLikeRepo extends JpaRepository<BlogLike, Integer> {
        List<BlogLike> findByBlog_BlogId(Integer blogId);
        Optional<BlogLike> findByBlog_BlogIdAndUser_UserId(Integer blogId, Integer userId);
}
