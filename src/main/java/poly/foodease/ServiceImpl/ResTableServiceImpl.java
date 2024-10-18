package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.foodease.Model.Entity.ResTable;
import poly.foodease.Model.Request.ResTableRequest;
import poly.foodease.Model.Response.ResTableResponse;
import poly.foodease.Repository.ResTableRepo;
import poly.foodease.Service.ResTableService;

@Service
public class ResTableServiceImpl implements ResTableService {

    @Autowired
    private ResTableRepo restaurantTableRepository;

    @Override
    public List<ResTable> findAll() {
        return restaurantTableRepository.findAll();
    }

    @Override
    public List<ResTable> getAvailableTables() {
        return restaurantTableRepository.findByIsAvailableTrue(); 
    }

    @Override
    public ResTable findTableById(Integer id) {
        Optional<ResTable> optionalTable = restaurantTableRepository.findById(id);
        return optionalTable.orElse(null); // Trả về null nếu không tìm thấy
    }

    @Override
    public List<ResTable> findAvailableTablesByCapacity(int guests) {
        // Giả sử bạn có một dung lượng tối thiểu và tối đa
        int minCapacity = guests;
        int maxCapacity = guests + 1; // hoặc theo cách bạn muốn

        return restaurantTableRepository.findByCapacityBetween(minCapacity, maxCapacity);
    }
    @Override
    public ResTableResponse createTable(ResTableRequest table) {

        if(restaurantTableRepository.existsByTableName(table.getTableName())){
            return ResTableResponse.builder()
                    .tableId(null)
                    .build();
        }

        ResTable newTable = ResTable.builder()
                .tableName(table.getTableName())
                .capacity(table.getCapacity())
                .isAvailable(true)
                .build();

        ResTable saveTable = restaurantTableRepository.save(newTable);

        return ResTableResponse.builder()
                .tableId(saveTable.getTableId())
                .tableName(saveTable.getTableName())
                .capacity(saveTable.getCapacity())
                .isAvailable(saveTable.getIsAvailable())
                .build();
    }

    @Override
    public ResTableResponse updateTable(int tableId, ResTableRequest resTableRequest) {

        // Kiểm tra xem bảng có tồn tại hay không
        if (!restaurantTableRepository.existsById(tableId)) {
            return ResTableResponse.builder()
                    .tableId(null)
                    .build();
        }

        // Lấy bảng hiện tại từ cơ sở dữ liệu
        ResTable existingTable = restaurantTableRepository.findById(tableId).orElseThrow(() ->
                new RuntimeException("Unknown"));

        // Cập nhật thông tin bảng
        existingTable.setTableName(resTableRequest.getTableName());
        existingTable.setCapacity(resTableRequest.getCapacity());
        // Giữ nguyên trạng thái isAvailable

        // Lưu bảng đã cập nhật vào cơ sở dữ liệu
        ResTable updatedTable = restaurantTableRepository.save(existingTable);

        // Trả về thông tin bảng đã cập nhật
        return ResTableResponse.builder()
                .tableId(updatedTable.getTableId())
                .tableName(updatedTable.getTableName())
                .capacity(updatedTable.getCapacity())
                .isAvailable(updatedTable.getIsAvailable())
                .build();
    }
}
