package poly.foodease.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.foodease.Mapper.UserMapper;
import poly.foodease.Model.Entity.User;
import poly.foodease.Model.Request.UserRequest;
import poly.foodease.Model.Response.UserResponse;
import poly.foodease.Repository.UserRepo;
import poly.foodease.Service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Page<UserResponse> getAllUserByPage(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy) {
        Sort sort = Sort.by(new Sort.Order(Objects.equals(sortOrder, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC ,sortBy));
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> usersPage= userRepo.findAll(pageable);
        List<UserResponse> userResponses = usersPage.getContent().stream()
                .map(userMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(userResponses, pageable , usersPage.getTotalElements());
    }

    @Override
    public List<UserResponse> getAllUser() {
        List<User> users= userRepo.findAll();
        return users.stream()
                .map(userMapper :: convertEnToRes)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponse> getUserById(Integer id) {
        Optional<User> user = userRepo.findById(id);
        return Optional.of(user.map(userMapper :: convertEnToRes)
                .orElseThrow(() -> new EntityNotFoundException("Not Found")));
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User user = userMapper.convertReqToEn(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userUpdate= userRepo.save(user);
        return userMapper.convertEnToRes(userUpdate);
    }

    @Override
    public Optional<UserResponse> updateUser(UserRequest userRequest, Integer id) {
        Optional<User> userById = userRepo.findById(id);
        return Optional.of(userById.map(us -> {
            User user = userMapper.convertReqToEn(userRequest);
            user.setUserId(us.getUserId());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User userUpdate= userRepo.save(user);
            return userMapper.convertEnToRes(userUpdate);
        }).orElseThrow(() -> new EntityNotFoundException(" Not Found User By Id")));
    }

    @Override
    public Optional<Void> deleteUserById(Integer id) {
        Optional<User> user = userRepo.findById(id);
        user.ifPresent(userRepo::delete);
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserResponse> getUserByUsername(String username) {
        User user = userRepo.findUserByUserName(username)
                .orElseThrow(() -> new EntityNotFoundException("not found User"));
        return Optional.of(userMapper.convertEnToRes(user));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public void saveAll(List<User> users) {
        userRepo.saveAll(users);
    }

	@Override
	public User SaveUser(User user) {
		// TODO Auto-generated method stub
		return userRepo.save(user);
	}
}