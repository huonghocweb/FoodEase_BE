package poly.foodease.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import poly.foodease.Model.Entity.Role;
import poly.foodease.Repository.RoleRepo;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleApi {
    @Autowired
    private RoleRepo roleRepo;

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleRepo.findAll();
        if (roles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Trả về 204 nếu không có role
        }
        return ResponseEntity.ok(roles); // Trả về danh sách roles
    }
}
