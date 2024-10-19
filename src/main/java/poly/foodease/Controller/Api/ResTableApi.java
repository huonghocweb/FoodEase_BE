package poly.foodease.Controller.Api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import poly.foodease.Service.ResTableService;

@RestController
@RequestMapping("/api/tables")
@CrossOrigin("*")
public class ResTableApi {

    @Autowired
    private ResTableService restaurantTableService;

    @Autowired
    private ResTableMapper restaurantTableMapper;

    @GetMapping
    public List<ResTable> getAllTable(){
        return restaurantTableService.findAll();
    }

    @PostMapping
    public ResTableResponse createTable(@RequestBody ResTableRequest tableRequest) {
        return restaurantTableService.createTable(tableRequest);
    }

    @PutMapping("/{tableId}") // Đường dẫn cho phương thức update
    @ResponseStatus(HttpStatus.OK) // Mã trạng thái trả về
    public ResTableResponse updateTable(
            @PathVariable int tableId, // Lấy ID từ đường dẫn
            @RequestBody ResTableRequest resTableRequest) { // Lấy dữ liệu từ thân yêu cầu
        return restaurantTableService.updateTable(tableId, resTableRequest);
    }

    @GetMapping("/available/{guests}")
    public ResponseEntity<List<ResTable>> getAvailableTables(@PathVariable int guests) {
        List<ResTable> availableTables = restaurantTableService.findAvailableTablesByCapacity(guests);
        return ResponseEntity.ok(availableTables);
    }
}
