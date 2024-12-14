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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    //    Hòa
    @Autowired
    private RoleRepo roleRepo;
    //    Hòa
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public Page<UserResponse> getAllUserByPage(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy) {
    logger.debug("Fetching users with pageNumber={}, pageSize={}, sortOrder={}, sortBy={}", pageNumber, pageSize, sortOrder, sortBy);
    Sort sort = Sort.by(new Sort.Order(Objects.equals(sortOrder, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
    Page<User> usersPage = userRepo.findAll(pageable);
    List<UserResponse> userResponses = usersPage.getContent().stream()
            .map(userMapper::convertEnToRes)
            .collect(Collectors.toList());
    logger.info("Successfully fetched {} users", userResponses.size());
    return new PageImpl<>(userResponses, pageable, usersPage.getTotalElements());
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
    logger.debug("Retrieving user with ID={}", id);
    Optional<User> user = userRepo.findById(id);
    return Optional.of(user.map(userMapper::convertEnToRes)
            .orElseThrow(() -> {
                logger.error("User with ID={} not found", id);
                return new EntityNotFoundException("Not Found");
            }));
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        logger.debug("Creating user with username={}", userRequest.getUserName());
        User user = userMapper.convertReqToEn(userRequest);
        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User userUpdate = userRepo.save(user);
        logger.info("User with username={} created successfully", userRequest.getUserName());
        return userMapper.convertEnToRes(userUpdate);
    }
    
    @Override
    public Optional<UserResponse> updateUser(UserRequest userRequest, Integer id) {
        logger.debug("Updating user with ID={}", id);
        return Optional.of(userRepo.findById(id).map(us -> {
            User user = userMapper.convertReqToEn(userRequest);
            user.setUserId(us.getUserId());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User userUpdate = userRepo.save(user);
            logger.info("User with ID={} updated successfully", id);
            return userMapper.convertEnToRes(userUpdate);
        }).orElseThrow(() -> {
            logger.error("User with ID={} not found for update", id);
            return new EntityNotFoundException("Not Found User By Id");
        }));
    }
    
    @Override
    public Optional<Void> deleteUserById(Integer id) {
        logger.debug("Deleting user with ID={}", id);
        Optional<User> user = userRepo.findById(id);
        user.ifPresentOrElse(
            u -> {
                userRepo.delete(u);
                logger.info("User with ID={} deleted successfully", id);
            },
            () -> logger.error("User with ID={} not found for deletion", id)
        );
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
    //    Hòa
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 10000 + random.nextInt(90000); // Mã số từ 10000 đến 99999
        return String.valueOf(code);
    }

    @Override
    @Transactional
    public String requestPasswordReset(String email) throws MessagingException {
        // Tìm người dùng theo email
        User user = userRepo.findTopByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Tạo mã số xác nhận gồm 5 chữ số
        String verificationCode = generateVerificationCode();

        // Gửi email chứa mã xác nhận
        mailService.sendResetCodeEmail(email, verificationCode);
        // Lưu mã số xác nhận vào cơ sở dữ liệu (PasswordResetToken) và liên kết với người dùng
        PasswordResetToken resetToken = new PasswordResetToken(verificationCode, user, email, LocalDateTime.now().plusMinutes(10));
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
    @Override
    @Transactional
    public String requestRegisterCode(String email) throws MessagingException {
        Optional<User> existingUser = userRepo.findTopByEmail(email);

        if (existingUser.isPresent()) {
            throw new RuntimeException("An account with this email already exists. Please use 'Forgot Password' instead.");
        }

        // Tạo mã xác nhận 5 chữ số
        String verificationCode = generateVerificationCode();

        // Gửi email chứa mã xác nhận
        mailService.sendResetCodeEmail(email, verificationCode);

        // Lưu mã xác nhận vào cơ sở dữ liệu
        PasswordResetToken resetToken = new PasswordResetToken(verificationCode, null, email, LocalDateTime.now().plusMinutes(10));
        resetToken.setEmail(email); // Lưu email trong token để tạo tài khoản
        passwordResetTokenRepo.save(resetToken);

        return "Verification code sent to your email!";
    }

	@Override
	public User findById(Integer id) {
		// TODO Auto-generated method stub
		User user =userRepo.findById(id).get();
		return user;
	}
//Hòa
}