package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.foodease.Mapper.FoodVariationToppingMapper;
import poly.foodease.Model.Entity.FoodVariationToppings;
import poly.foodease.Model.Response.FoodVariationToppingResponse;
import poly.foodease.Repository.FoodVariatonToppingDao;
import poly.foodease.Service.FoodVariationToppingService;

@Service
public class FoodVariationToppingIplement implements FoodVariationToppingService {
@Autowired
FoodVariatonToppingDao foodVariatonToppingDao;
@Autowired FoodVariationToppingMapper foodVariationToppingMapper;
	@Override
	public List<FoodVariationToppingResponse> findFoodVariationToppingById(Integer id) {
		// TODO Auto-generated method stub
		List<FoodVariationToppings> list=foodVariatonToppingDao.findFoodVariationToppingById(id);
		return list.stream()
				.map(foodVariationToppingMapper :: converEntoResponse)
				.collect(Collectors.toList());
	}
	@Override
	public FoodVariationToppingResponse Save(Integer foodVariationId, Integer toppingID) {
		// TODO Auto-generated method stub
		FoodVariationToppings foodVariationToppings= new FoodVariationToppings();
		foodVariationToppings.setFoodVariationId(foodVariationId);
		foodVariationToppings.setToppingId(toppingID);
		FoodVariationToppings Save=foodVariatonToppingDao.save(foodVariationToppings);
		FoodVariationToppingResponse response = foodVariationToppingMapper.converEntoResponse(Save);		
		return response;
	}
	
	
}
