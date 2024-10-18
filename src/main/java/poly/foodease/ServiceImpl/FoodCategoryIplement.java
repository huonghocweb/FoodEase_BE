package poly.foodease.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Entity.FoodCategories;
import poly.foodease.Repository.FoodCategoryDao;
import poly.foodease.Service.FoodCategoryService;


@Service
public class FoodCategoryIplement implements FoodCategoryService {
	@Autowired
	FoodCategoryDao foodCategoryDao;
	@Override
	public List<FoodCategories> findAll() {
		// TODO Auto-generated method stub
		return foodCategoryDao.findAll();
	}
	@Override
	public void deleteCategories(Integer id) {
		FoodCategories foodCategories = foodCategoryDao.findById(id).get();
		foodCategoryDao.deleteById(foodCategories.getCategoryId());
	}


}
