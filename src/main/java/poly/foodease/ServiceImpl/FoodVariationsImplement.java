package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import poly.foodease.Mapper.FoodVariationMapper;
import poly.foodease.Model.Entity.FoodVariations;
import poly.foodease.Model.Response.FoodVariationResponse;
import poly.foodease.Repository.FoodVariationsDao;
import poly.foodease.Service.FoodVariationsService;


@Service
public class FoodVariationsImplement implements FoodVariationsService {
@Autowired
FoodVariationsDao foodVariationsDao;
@Autowired FoodVariationMapper foodVariationMapper;
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



}
