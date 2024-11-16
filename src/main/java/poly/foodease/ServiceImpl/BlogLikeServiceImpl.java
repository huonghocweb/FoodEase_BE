package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Mapper.BlogLikeMapper;
import poly.foodease.Model.Entity.BlogLike;
import poly.foodease.Model.Request.BlogLikeRequest;
import poly.foodease.Model.Response.BlogLikeResponse;
import poly.foodease.Repository.BlogLikeRepo;
import poly.foodease.Repository.BlogRepo;
import poly.foodease.Repository.UserRepo;
import poly.foodease.Service.BlogLikeService;
import poly.foodease.Service.UserService;

@Service
public class BlogLikeServiceImpl implements BlogLikeService {

    @Autowired
    private BlogLikeRepo blogLikeRepo;

    @Autowired
    private BlogRepo blogRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BlogLikeMapper blogLikeMapper;
    @Autowired
    private UserService userService;

    @Override
    public BlogLikeResponse createBlogLike(BlogLikeRequest blogLikeRequest) {
        // Kiểm tra tồn tại Blog và User trước khi ánh xạ
        var blog = blogRepo.findById(blogLikeRequest.getBlogId())
                .orElseThrow(() -> new EntityNotFoundException("Blog not found"));
        var user = userRepo.findById(blogLikeRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Sử dụng mapper để chuyển từ Request sang Entity
        BlogLike blogLike = blogLikeMapper.convertReqToEn(blogLikeRequest);

        // Thiết lập Blog và User cho BlogLike
        blogLike.setBlog(blog);
        blogLike.setUser(user);

        // Lưu BlogLike và chuyển đổi kết quả sang Response
        blogLike = blogLikeRepo.save(blogLike);
        return blogLikeMapper.convertEnToRes(blogLike);
    }
    // @Override
    // public Optional<ResTableResponse> updateResTableNew(Integer tableId,
    // ResTableRequest resTableRequest) {
    // return Optional.of(resTableRepo.findById(tableId)
    // .map(existingTable -> {
    // ResTable resTable = resTableMapper.convertReqToEn(resTableRequest);
    // resTable.setTableId(existingTable.getTableId());
    // ResTable updatedTable = resTableRepo.save(resTable);
    // return resTableMapper.convertEnToRes(updatedTable);
    // })
    // .orElseThrow(() -> new EntityNotFoundException("not found Coupon")));
    // }

    @Override
    public BlogLikeResponse likeOrDislikeBlog(BlogLikeRequest blogLikeRequest) {
        // Kiểm tra nếu người dùng đã like blog
        BlogLike existingBlogLike = blogLikeRepo
                .findByBlog_BlogIdAndUser_UserId(blogLikeRequest.getBlogId(), blogLikeRequest.getUserId())
                .orElse(null);

        if (existingBlogLike != null) {
            // Nếu đã tồn tại, cập nhật trạng thái
            existingBlogLike.setIsLike(blogLikeRequest.getIsLike());
            blogLikeRepo.save(existingBlogLike);
            return blogLikeMapper.convertEnToRes(existingBlogLike);
        } else {
            // Nếu chưa tồn tại, tạo mới
            BlogLike newBlogLike = blogLikeMapper.convertReqToEn(blogLikeRequest);
            // Gán blog và user
            var blog = blogRepo.findById(blogLikeRequest.getBlogId())
                    .orElseThrow(() -> new EntityNotFoundException("Blog not found"));
            var user = userRepo.findById(blogLikeRequest.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            newBlogLike.setBlog(blog);
            newBlogLike.setUser(user);

            blogLikeRepo.save(newBlogLike);
            return blogLikeMapper.convertEnToRes(newBlogLike);
        }
    }

    @Override
    public BlogLikeResponse getBlogLikeById(Integer likeId) {
        BlogLike blogLike = blogLikeRepo.findById(likeId)
                .orElseThrow(() -> new EntityNotFoundException("BlogLike not found"));
        return blogLikeMapper.convertEnToRes(blogLike);
    }

    // @Override
    // public Optional<ResTableResponse> getResTableByIdNew(Integer resTableId) {
    // ResTable resTable = resTableRepo.findById(resTableId)
    // .orElseThrow(() -> new EntityNotFoundException("Not found ResTable"));
    // return Optional.of(resTableMapper.convertEnToRes(resTable));
    // }

    @Override
    public void deleteBlogLike(Integer likeId) {
        BlogLike blogLike = blogLikeRepo.findById(likeId)
                .orElseThrow(() -> new EntityNotFoundException("Table not found"));
        blogLikeRepo.delete(blogLike);
    }

    @Override
    public List<BlogLikeResponse> getAllBlogLike() {
        return blogLikeRepo.findAll().stream()
                .map(blogLikeMapper::convertEnToRes)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogLikeResponse> getLikeByBlogId(Integer blogId) {
        return blogLikeRepo.findByBlog_BlogId(blogId)
                .stream()
                .map(blogLikeMapper::convertEnToRes)
                .collect(Collectors.toList());
    }
}
