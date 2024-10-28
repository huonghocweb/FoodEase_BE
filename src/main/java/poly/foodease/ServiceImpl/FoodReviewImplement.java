package poly.foodease.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import poly.foodease.Mapper.FoodReviewMapper;
import poly.foodease.Model.Entity.FoodReview;
import poly.foodease.Model.Response.FoodReviewResponse;
import poly.foodease.Repository.FoodReviewDao;
import poly.foodease.Service.FoodReviewService;
import poly.foodease.Service.UploadFileService;


@Service
public class FoodReviewImplement implements FoodReviewService {
		@Autowired
		FoodReviewDao foodReviewDao;
		@Autowired FoodReviewMapper foodReviewMapper;
		@Autowired UploadFileService uploadFileService;
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
