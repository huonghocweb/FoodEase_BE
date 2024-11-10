package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.foodease.Mapper.FoodCategoryMapper;
import poly.foodease.Model.Entity.FoodCategories;
import poly.foodease.Model.Response.FoodCategoriesReponse;
import poly.foodease.Repository.FoodCategoryDao;
import poly.foodease.Service.FoodCategoryService;


@Service
public class FoodCategoryIplement implements FoodCategoryService {
	@Autowired
	FoodCategoryDao foodCategoryDao;
	@Autowired FoodCategoryMapper foodCategoryMapper;
	@Override
	public void deleteCategories(Integer id) {
		FoodCategories foodCategories = foodCategoryDao.findById(id).get();
		foodCategoryDao.deleteById(foodCategories.getCategoryId());
	}

	@Override
	public List<FoodCategoriesReponse> findAll() {
		// TODO Auto-generated method stub
	List<FoodCategories> list=foodCategoryDao.findAll();
	
		return list.stream()
				.map(foodCategoryMapper :: converEnToRes)
				.collect(Collectors.toList());
	}

	@Override
	public FoodCategoriesReponse save(String categoryName) {
		try {
			FoodCategories foodCategories = new FoodCategories();
			foodCategories.setCartegoryName(categoryName);
			FoodCategories foodCategoriesRequest = foodCategoryDao.save(foodCategories);
			FoodCategoriesReponse request=foodCategoryMapper.converEnToRes(foodCategoriesRequest);
			return request;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		// TODO Auto-generated method stub
		
	}


}
