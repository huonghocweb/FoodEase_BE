package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Mapper.TableCategoryMapper;
import poly.foodease.Model.Entity.TableCategory;
import poly.foodease.Model.Response.TableCategoryResponse;
import poly.foodease.Repository.TableCategoryRepo;
import poly.foodease.Service.TableCategoryService;

@Service
public class TableCategoryServiceImpl implements TableCategoryService {

    @Autowired
    private TableCategoryRepo tableCategoryRepo;

    @Autowired
    private TableCategoryMapper tableCategoryMapper;

    @Override
    public TableCategoryResponse getTableCategoryById(Integer tableCategoryId) {
        TableCategory tableCategory = tableCategoryRepo.findById(tableCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Table category not found"));
        return tableCategoryMapper.convertEnToRes(tableCategory);
    }

    @Override
    public List<TableCategoryResponse> getAllTableCategory() {
        return tableCategoryRepo.findAll().stream()
                .map(tableCategoryMapper::convertEnToRes)
                .collect(Collectors.toList());
    }
}
