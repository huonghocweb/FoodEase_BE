package poly.foodease.ServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import poly.foodease.Mapper.FoodVariationMapper;
import poly.foodease.Model.Entity.FoodVariations;
import poly.foodease.Model.Response.FoodResponse;
import poly.foodease.Model.Response.FoodVariationResponse;
import poly.foodease.Repository.FoodVariationsDao;
import poly.foodease.Service.CloudinaryService;
import poly.foodease.Service.FoodVariationsService;


@Service
public class FoodVariationsImplement implements FoodVariationsService {
@Autowired
FoodVariationsDao foodVariationsDao;
@Autowired FoodVariationMapper foodVariationMapper;
@Autowired  CloudinaryService cloudinaryService;
@Autowired FoodVariationMapper mapper;
//	@Override
//	public List<FoodVariationResponse> findByCategoryMainDishes() {
//		// TODO Auto-generated method stub
//		List<FoodVariations> list= foodVariationsDao.findByCategoryMainDishes();
//		return list.stream()
//				.map(foodVariationMapper :: converEnToReponse)
//				.collect(Collectors.toList());
//				
//	}

	@Override
	public List<FoodVariationResponse> findByCategoryDrink() {
		// TODO Auto-generated method stub
		List<FoodVariations>  list=foodVariationsDao.findByCategoryDrink();
		return list.stream()
				.map(foodVariationMapper :: converEnToReponse)
				.collect(Collectors.toList());
	}
	@Override
	public Optional<FoodVariationResponse> findById1(Integer Id) {
		// TODO Auto-generated method stub
		Optional<FoodVariations> foodVariationByid=foodVariationsDao.findById(Id);
		return foodVariationByid.map(foodVariationMapper :: converEnToReponse);
	}
	@Override
	public FoodVariationResponse findFoodVariationBySize(Integer id, String sizeName) {
		// TODO Auto-generated method stub
		FoodVariations foodVariation=foodVariationsDao.findFoodVariationBySize(id, sizeName);
		 
		return foodVariationMapper.converEnToReponse(foodVariation);
	}

	@Override
	public Optional<FoodVariations> findById(Integer id) {
		// TODO Auto-generated method stub
		return foodVariationsDao.findById(id);
	}
	@Override
	public List<FoodVariationResponse> findFoodVariationByFoodName(String foodName) {
		// TODO Auto-generated method stub
		List<FoodVariations> list=foodVariationsDao.findFoodVariationByFoodName(foodName);
		return list.stream()
				.map(foodVariationMapper :: converEnToReponse)
				.collect(Collectors.toList());
				
	}
	@Override
	public Page<FoodVariationResponse> findByCategoryMainDishes(Pageable pageable) {
		// TODO Auto-generated method stub
		 Page<FoodVariations> list= foodVariationsDao.findByCategoryMainDishes(pageable);
		return  list.map(foodVariationMapper:: converEnToReponse);
	}
	@Override
	public List<FoodVariationResponse> findFoodVariationByCategoryId(Integer id) {
		// TODO Auto-generated method stub
		List<FoodVariations> list= foodVariationsDao.findFoodVariationByCategoryId(id);
		return list.stream()
				.map(foodVariationMapper :: converEnToReponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<FoodVariationResponse> findFoodVariationByUserId(Integer id) {
		// TODO Auto-generated method stub
		List<FoodVariations> list=foodVariationsDao.findFoodVariationByUserId(id);
		return list.stream()
				.map(foodVariationMapper :: converEnToReponse)
				.collect(Collectors.toList());
	}


	@Override
	public Page<FoodVariationResponse> findAll(Pageable pageple) {
		// TODO Auto-generated method stub
		Page<FoodVariations> list =foodVariationsDao.findAll(pageple);
		return list.map(foodVariationMapper:: converEnToReponse);
	}
	@Override
	public List<FoodVariationResponse> findFoodVariationByFoodId(Integer id) {
		// TODO Auto-generated method stub
		List<FoodVariations> list=foodVariationsDao.findFoodVariationByFoodId(id);
		return list.stream()
				.map(foodVariationMapper :: converEnToReponse)
				.collect(Collectors.toList());
	}
	@Override
	public FoodVariationResponse Save(MultipartFile[] file, Integer quantityStock, Integer FoodSizeId,Integer foodId) {
		// TODO Auto-generated method stub
		try {
			FoodVariations foodVariations =new FoodVariations();
			foodVariations.setCreatedAt(LocalDate.now());
			foodVariations.setUpdatedAt(LocalDate.now());
			foodVariations.setQuantityStock(quantityStock);
			foodVariations.setFoodSizeId(FoodSizeId);
			foodVariations.setFoodId(foodId);
			
			  if (file != null) {
				  foodVariations.setImageUrl(cloudinaryService.uploadFile(file, "restables").get(0));
		            // couponRequest.setImageUrl(fileManageUtils.save(folder,files).get(0));
				  System.out.println(foodVariations.getImageUrl());
		        } else {
		            System.out.println("file null");
		            foodVariations.setImageUrl(" ");
		        }
			  FoodVariations Save = foodVariationsDao.save(foodVariations);
			  FoodVariationResponse response=mapper.converEnToReponse(Save);
			  System.out.println("lưu thành công");
			  return response;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			 System.out.println("lưu thất bại");
			return null;
		}
		
	}
	@Override
	public void deletebyId(Integer id) {
		// TODO Auto-generated method stub
		FoodVariations foodVariations=foodVariationsDao.findById(id).get();
		foodVariationsDao.deleteById(foodVariations.getFoodVariationId());	
		
	}
	@Override
	public FoodVariationResponse Update(Integer foodVariationId,MultipartFile[] file, Integer quantityStock, Integer FoodSizeId,
			Integer foodId) {
		try {
			FoodVariations foodVariations =new FoodVariations();
			foodVariations.setFoodVariationId(foodVariationId);
			foodVariations.setCreatedAt(LocalDate.now());
			foodVariations.setUpdatedAt(LocalDate.now());
			foodVariations.setQuantityStock(quantityStock);
			foodVariations.setFoodSizeId(FoodSizeId);
			foodVariations.setFoodId(foodId);
			
			  if (file != null) {
				  foodVariations.setImageUrl(cloudinaryService.uploadFile(file, "restables").get(0));
		            // couponRequest.setImageUrl(fileManageUtils.save(folder,files).get(0));
				  System.out.println(foodVariations.getImageUrl());
		        } else {
		            System.out.println("file null");
		            foodVariations.setImageUrl(" ");
		        }
			  FoodVariations Save = foodVariationsDao.save(foodVariations);
			  FoodVariationResponse response=mapper.converEnToReponse(Save);
			  System.out.println("update thành công");
			  return response;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			 System.out.println("update thất bại");
			return null;
		}
	}



}
