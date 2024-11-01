package poly.foodease.ServiceImpl;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.foodease.Mapper.UserMapper;
import poly.foodease.Model.Entity.PasswordResetToken;
import poly.foodease.Model.Entity.Role;
import poly.foodease.Model.Entity.User;
import poly.foodease.Model.Request.UserRequest;
import poly.foodease.Model.Response.UserResponse;
import poly.foodease.Repository.PasswordResetTokenRepo;
import poly.foodease.Repository.RoleRepo;
import poly.foodease.Repository.UserRepo;
import poly.foodease.Service.MailService;
import poly.foodease.Service.UserService;
import poly.foodease.Model.Response.UserResponse; // Đảm bảo bạn đã nhập đúng

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
    @Autowired
    private MailService mailService;
    private final PasswordResetTokenRepo passwordResetTokenRepo;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepo roleRepo;
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
        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepo.save(user);
        return userMapper.convertEnToRes(savedUser);
    }

    @Override
    public Optional<UserResponse> updateUser(UserRequest userRequest, Integer id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUserName(userRequest.getUserName());
            user.setFullName(userRequest.getFullName());
            user.setEmail(userRequest.getEmail());
            user.setAddress(userRequest.getAddress());
            user.setPhoneNumber(userRequest.getPhoneNumber());
            user.setGender(userRequest.getGender());
            user.setBirthday(userRequest.getBirthday());
            user.setRoles(userRequest.getRoleIds().stream()
                    .map(roleId -> roleRepo.findById(roleId)
                            .orElseThrow(() -> new EntityNotFoundException("Role not found for ID: " + roleId)))
                    .collect(Collectors.toList()));
            userRepo.save(user);
            return Optional.of(userMapper.convertEnToRes(user));
        } else {
            return Optional.empty();
        }
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

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 10000 + random.nextInt(90000); // Mã số từ 10000 đến 99999
        return String.valueOf(code);
    }

    @Override
    @Transactional
    public String requestPasswordReset(String email) throws MessagingException {
        User user = userRepo.findTopByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Tạo mã số xác nhận gồm 5 chữ số
        String verificationCode = generateVerificationCode();

        // Gửi email chứa mã xác nhận
        mailService.sendResetCodeEmail(email, verificationCode);

        // Lưu mã số xác nhận vào cơ sở dữ liệu (PasswordResetToken)
        PasswordResetToken resetToken = new PasswordResetToken(verificationCode, user, LocalDateTime.now().plusMinutes(10));
        passwordResetTokenRepo.save(resetToken);

        return "Verification code sent to your email!";
    }

    @Override
    @Transactional
    public String resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        // Kiểm tra thời gian hết hạn của token
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        // Mã hóa mật khẩu mới
        User user = resetToken.getUser();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword); // Chỉ mã hóa một lần
        userRepo.save(user);

        // Xóa token sau khi đặt lại mật khẩu thành công
        passwordResetTokenRepo.delete(resetToken);

        return "Password reset successful!";
    }

}