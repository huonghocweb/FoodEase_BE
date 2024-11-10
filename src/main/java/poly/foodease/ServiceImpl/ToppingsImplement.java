package poly.foodease.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Entity.Toppings;
import poly.foodease.Repository.ToppingDao;
import poly.foodease.Service.ToppingService;

@Service
public class ToppingsImplement implements ToppingService {
	@Autowired
	ToppingDao toppingDao;
	@Override
	public List<Toppings> findAll() {
		// TODO Auto-generated method stub
		return toppingDao.findAll();
	}

	
}
