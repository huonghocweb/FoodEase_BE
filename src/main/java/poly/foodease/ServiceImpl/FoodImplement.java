package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.foodease.Mapper.FoodMapper;
import poly.foodease.Model.Entity.Foods;
import poly.foodease.Model.Response.FoodResponse;
import poly.foodease.Repository.FoodsDao;
import poly.foodease.Service.FoodsService;


@Service
public class FoodImplement implements FoodsService {
	
	@Autowired
	FoodsDao foodsDao;
	
	@Autowired FoodMapper foodMapper;
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
	public void deleteFood(int foodId) {
		Foods food = foodsDao.findById(foodId).get();
		foodsDao.deleteById(food.getFoodId());
	}

}
