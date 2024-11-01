package poly.foodease.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import poly.foodease.Mapper.FoodReviewMapper;
import poly.foodease.Model.Entity.FoodReview;
import poly.foodease.Model.Entity.Foods;
import poly.foodease.Model.Entity.User;
import poly.foodease.Model.Request.FoodReviewRequest;
import poly.foodease.Model.Response.FoodResponse;
import poly.foodease.Model.Response.FoodReviewResponse;
import poly.foodease.Repository.FoodReviewDao;
import poly.foodease.Repository.FoodsDao;
import poly.foodease.Repository.UserRepo;
import poly.foodease.Service.CloudinaryService;
import poly.foodease.Service.FoodReviewService;
import poly.foodease.Service.UploadFileService;
import poly.foodease.Utils.FileManageUtils;


@Service
public class FoodReviewImplement implements FoodReviewService {
		@Autowired
		FoodReviewDao foodReviewDao;
		@Autowired FoodReviewMapper foodReviewMapper;
		@Autowired UploadFileService uploadFileService;

		@Autowired
	CloudinaryService cloudinaryService;

		@Autowired
	FileManageUtils fileManageUtils;

		@Autowired
	FoodsDao foodRepository;

		@Autowired
	UserRepo userRepository;
		@Override
	public List<FoodReviewResponse> findFoodReviewByFoodId(Integer id) {
		// TODO Auto-generated method stub
			List<FoodReview> foodReview =foodReviewDao.findFoodReviewByFoodId(id);
		return foodReview.stream().map(foodReviewMapper :: converEnToRespon)
				.collect(Collectors.toList());
	}


	@Override
		public FoodReviewResponse save(MultipartFile file, Integer rating, String review, Integer foodId) {
		    try {
		        FoodReview foodReview = new FoodReview();
		        foodReview.setRating(rating);
		        foodReview.setReview(review);
		        foodReview.setReviewDate(new Date());
		        
		        if (!file.isEmpty()) {
		            String fileName = uploadFileService.uploadFile(file);
		            foodReview.setImageUrl(fileName);
		            System.out.println("Tên file là: " + fileName);
		        }
		        
		        foodReview.setUserId(2); 
		        foodReview.setFoodId(foodId);
		        
		        FoodReview savedFoodReview = foodReviewDao.save(foodReview);
		        System.out.println("Đã thực hiện comment");
		        
		        // Chuyển đổi savedFoodReview sang FoodReviewResponse
		        FoodReviewResponse response = foodReviewMapper.converEnToRespon(savedFoodReview) ;
		        return response;
		    } catch (Exception e) {
		        System.out.println("Không thành công comment");
		        e.printStackTrace();
		        throw new RuntimeException("Lỗi khi lưu review", e);
		    }
		}

	@Override
	public FoodReview createReview(FoodReviewRequest request) throws IOException {
		FoodReview review = new FoodReview();
		review.setRating(request.getRating());
		review.setReview(request.getReview());
		review.setFoodId(request.getFoodId());
		review.setUserId(request.getUserId());

		if (request.getImage() != null && !request.getImage().isEmpty()) {
			MultipartFile file = request.getImage();

			// Tải ảnh lên Cloudinary và nhận lại URL
			List<String> uploadedUrls = cloudinaryService.uploadFile(new MultipartFile[]{file}, "reviews");

			if (!uploadedUrls.isEmpty()) {
				review.setImageUrl(uploadedUrls.get(0)); // Lưu URL của hình ảnh đầu tiên
			}
		}
		return foodReviewDao.save(review);

	}

//	@Override
//	public FoodReviewResponse createReview(FoodReviewRequest request) throws IOException {
//		// Khởi tạo review mới
//		FoodReview review = new FoodReview();
//		review.setRating(request.getRating());
//		review.setReview(request.getReview());
//
//		// Lấy `Food` và `User` từ repository dựa trên `foodId` và `userId` trong request
//		Foods food = foodRepository.findById(request.getFoodId()).orElseThrow(() -> new IllegalArgumentException("Not found"));
//		User user = userRepository.findById(request.getUserId())
//				.orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//		review.setFood(food); // Đặt food cho review
//		review.setUser(user); // Đặt user cho review
//
//		// Xử lý upload ảnh nếu có
//		if (request.getImage() != null && !request.getImage().isEmpty()) {
//			MultipartFile file = request.getImage();
//			String filename = fileManageUtils.save("reviews", new MultipartFile[]{file}).get(0);
//			review.setImageUrl(filename);
//		}
//
//		// Lưu review vào database
//		FoodReview savedReview = foodReviewDao.save(review);
//
//		// Tạo phản hồi `FoodReviewResponse` bao gồm thông tin của user và food
//		FoodReviewResponse response = new FoodReviewResponse();
//		response.setReviewId(savedReview.getReviewId());
//		response.setRating(savedReview.getRating());
//		response.setReview(savedReview.getReview());
//		response.setReviewDate(savedReview.getReviewDate());
//		response.setImageUrl(savedReview.getImageUrl());
//		response.setUserId(user.getUserId());
//		response.setFoodId(food.getFoodId());
//
//		// Ánh xạ thông tin `User` và `Food` vào phản hồi
//		response.setUser(User.builder()
//						.userId(user.getUserId())
//						.userName(user.getUserName())
//						.fullName(user.getFullName())
//				.build());
//		response.setFood(Foods.builder()
//						.foodId(food.getFoodId())
//						.foodName(food.getFoodName())
//				.build());
//
//		return response;
//	}


	// Thêm phương thức save
    @Override
    public FoodReview save(FoodReview foodReview) {
        return foodReviewDao.save(foodReview);
    }

    // Thêm phương thức findByFilter
    @Override
    public List<FoodReview> findByFilter(Integer rating, Integer month, Integer year) {
        return foodReviewDao.findByFilter(rating, month, year);
    }	

}
