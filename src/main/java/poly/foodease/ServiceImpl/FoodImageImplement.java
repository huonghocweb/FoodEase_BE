package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.foodease.Mapper.FoodImageMapper;
import poly.foodease.Model.Entity.FoodImage;
import poly.foodease.Model.Response.FoodImageResponse;
import poly.foodease.Repository.FoodImageDao;
import poly.foodease.Service.FoodImageService;


@Service
public class FoodImageImplement implements FoodImageService {
	@Autowired
	FoodImageDao foodImageDao;
	@Autowired FoodImageMapper foodImageMapper;
	@Override
	public List<FoodImageResponse> findFoodImageByFoodId(Integer id) {
		// TODO Auto-generated method stub
		List<FoodImage> list=foodImageDao.findFoodImageByFoodId(id);
		return list.stream()
				.map(foodImageMapper :: converEntoResponse)
				.collect(Collectors.toList());
				
	}
	
	

}
