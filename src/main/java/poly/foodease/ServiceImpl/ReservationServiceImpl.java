package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import poly.foodease.Repository.ReservationRepo;
import poly.foodease.Service.EmailService;
import poly.foodease.Service.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepo reservationRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailService emailService;

    @Override
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation updateReservationStatus(Integer reservationId, String status) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.setStatus(status);
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation getReservationById(Integer id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    @Override
    public Reservation findById(Integer id) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);

        if (reservationOptional.isPresent()) {
            return reservationOptional.get(); // Trả về đối tượng nếu tìm thấy
        } else {
            throw new RuntimeException("Reservation not found with id: " + id);
        }
    }

    private void sendConfirmationEmail(Reservation reservation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(reservation.getEmail());
        message.setSubject("Reservation Status Update");
        message.setText("Your reservation for " + reservation.getGuests() + " people on " +
                reservation.getReservationDate() + " at " + reservation.getReservationTime() +
                " has been " + reservation.getStatus() + ".");
        mailSender.send(message);
    }

    @Override
    public void acceptReservation(Reservation reservation) {
        Integer reservationId = reservation.getReservationId();

        Reservation existingReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        existingReservation.setStatus("Accepted");
        reservationRepository.save(existingReservation);

        String subject = "Reservation Confirmed";
        String body = "Dear " + existingReservation.getName() + ",\n\nYour reservation has been confirmed.\nDetails:\n"
                + "Date: " + existingReservation.getReservationDate() + "\n"
                + "Time: " + existingReservation.getReservationTime() + "\n"
                + "Guests: " + existingReservation.getGuests() + "\n\nThank you!";
        emailService.sendEmail(existingReservation.getEmail(), subject, body);
    }

    @Override
    public void cancelReservation(Reservation reservation) {
        Integer reservationId = reservation.getReservationId();

        Reservation existingReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        existingReservation.setStatus("Cancelled");
        reservationRepository.save(existingReservation);

        String subject = "Reservation Cancelled";
        String body = "Dear " + existingReservation.getName() + ",\n\nYour reservation has been cancelled.\n"
                + "If you have any questions, feel free to contact us.\n\nThank you!";
        emailService.sendEmail(existingReservation.getEmail(), subject, body);
    }

    @Override
    public Reservation save(Reservation reservation) {
        // Kiểm tra thông tin hợp lệ (nếu cần)
        // Ví dụ: kiểm tra xem số khách có phù hợp không
        if (reservation.getGuests() <= 0) {
            throw new IllegalArgumentException("Number of guests must be greater than 0");
        }

        // Lưu đối tượng reservation vào cơ sở dữ liệu
        return reservationRepository.save(reservation);
    }

}
