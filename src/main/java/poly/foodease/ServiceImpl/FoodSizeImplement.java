package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.foodease.Mapper.FoodSizeMapper;
import poly.foodease.Model.Entity.FoodSize;
import poly.foodease.Model.Response.FoodSizeResponse;
import poly.foodease.Repository.FoodSizeDao;
import poly.foodease.Service.FoodSizeService;


@Service
public class FoodSizeImplement implements FoodSizeService {
@Autowired
FoodSizeDao foodSizeDao;
@Autowired FoodSizeMapper foodSizeMapper;
	@Override
	public FoodSizeResponse findFoodSizeBySize(String foodSizeName) {
		// TODO Auto-generated method stub
		FoodSize foodSize=foodSizeDao.findFoodSizeBySize(foodSizeName);
		return   foodSizeMapper.converEnToRespon(foodSize);
	}

	@Override
	public List<FoodSizeResponse> findAll() {
		// TODO Auto-generated method stub
		List<FoodSize> list= foodSizeDao.findAll();
		return list.stream()
				.map(foodSizeMapper :: converEnToRespon)
				.collect(Collectors.toList());
	}

	@Override
	public List<FoodSizeResponse> findFoodSizeByFoodId(Integer id) {
		// TODO Auto-generated method stub
		List<FoodSize> list =foodSizeDao.findSizeByFoodId(id);
		return  list.stream().map(foodSizeMapper :: converEnToRespon)
				.collect(Collectors.toList());
	}

	

}
