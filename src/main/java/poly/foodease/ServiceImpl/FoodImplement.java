package poly.foodease.ServiceImpl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import poly.foodease.Mapper.FoodMapper;
import poly.foodease.Model.Entity.Foods;
import poly.foodease.Model.Request.FoodRequest;
import poly.foodease.Model.Response.FoodResponse;
import poly.foodease.Repository.FoodsDao;
import poly.foodease.Service.CloudinaryService;
import poly.foodease.Service.FoodsService;
import poly.foodease.Service.UploadFileService;


@Service
public class FoodImplement implements FoodsService {
	
	@Autowired
	FoodsDao foodsDao;
	
	@Autowired FoodMapper foodMapper;
	@Autowired UploadFileService uploadFileService;
	@Autowired  CloudinaryService cloudinaryService;
	@Override
	public List<FoodResponse> findAll() {
		List<Foods> list=foodsDao.findAll();
		return list.stream()
				.map(foodMapper :: converEntoResponse)
				.collect(Collectors.toList());
	}
	@Override
	public List<Foods> findByCategoryMainDishes() {
		return null;
	}

	@Override
	public List<Foods> findByCategoryDrink() {
		return null;
	}

	@Override
	public void deleteFood(Integer foodId) {
		Foods food = foodsDao.findById(foodId).get();
		foodsDao.deleteById(food.getFoodId());
	}
	@Override
	public FoodResponse save(String name, String deception, Double basePrice, MultipartFile[] file, Integer discout,
			Integer categoryId) {
		try {
			Foods food=new Foods();
			food.setFoodName(name);
			food.setDescription(deception);
			food.setBasePrice(basePrice);
			 
			  if (file != null) {
				  food.setImageUrl(cloudinaryService.uploadFile(file, "restables").get(0));
		            // couponRequest.setImageUrl(fileManageUtils.save(folder,files).get(0));
				  System.out.println(food.getImageUrl());
		        } else {
		            System.out.println("file null");
		            food.setImageUrl(" ");
		        }
			food.setDiscount(discout);
			
			food.setCreatedAt(LocalDate.now());
			food.setUpdatedAt(LocalDate.now());
			food.setCategoryId(categoryId);			
			Foods foodRequest =foodsDao.save(food);
			System.out.println("thêm food thành công");
			FoodResponse response=foodMapper.converEntoResponse(foodRequest);
			return response;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
//	
	}
	@Override
	public Page<FoodResponse> findAddFoodAll(Pageable page) {
		// TODO Auto-generated method stub
		Page<Foods> list=foodsDao.findAll(page);
		return list.map(foodMapper:: converEntoResponse);
	}
	@Override
	public FoodResponse update(Integer id, String foodName, String description, Double basePrice, MultipartFile[] file,
			Integer discount, Integer categoriesId) {
		// TODO Auto-generated method stub
		try {
			Foods food=new Foods();
			food.setFoodId(id);
			food.setFoodName(foodName);
			food.setDescription(description);
			food.setBasePrice(basePrice);
			if (file != null) {
				  food.setImageUrl(cloudinaryService.uploadFile(file, "restables").get(0));
		            // couponRequest.setImageUrl(fileManageUtils.save(folder,files).get(0));
				  System.out.println(food.getImageUrl());
		        } else {
		            System.out.println("file null");
		            food.setImageUrl(" ");
		        }
			food.setDiscount(discount);
			
			food.setCreatedAt(LocalDate.now());
			food.setUpdatedAt(LocalDate.now());
			food.setCategoryId(categoriesId);			
			Foods foodRequest =foodsDao.save(food);
			FoodResponse response=foodMapper.converEntoResponse(foodRequest);
			System.out.println("update thành công");
			return response;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("update không thành công");
			return null;
		}
	
	}



	//Huong
	@Override
	public Page<FoodResponse> fillAllFoodByHuong(Pageable pageable) {
		Page<Foods> foodsPage = foodsDao.findAll(pageable);
		List<FoodResponse> foodResponses = foodsPage.getContent().stream()
				.map(foodMapper :: converEntoResponse)
				.collect(Collectors.toList());
		return new PageImpl<>(foodResponses,pageable , foodsPage.getTotalElements());
	}


	@Override
	public Page<FoodResponse> findFoodsByFoodName(String foodName, Pageable pageable) {
		Page<Foods> foodsByFoodName = foodsDao.findFoodsByFoodName(foodName, pageable);
		List<FoodResponse> foodResponses = foodsByFoodName.getContent().stream()
				.map(foodMapper :: converEntoResponse)
				.collect(Collectors.toList());
		return new PageImpl<>(foodResponses,pageable,foodsByFoodName.getTotalElements());
	}
	//Huong

}
