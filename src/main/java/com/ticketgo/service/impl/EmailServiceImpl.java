package com.ticketgo.service.impl;

import com.ticketgo.dto.BookingInfoDTO;
import com.ticketgo.service.BookingService;
import com.ticketgo.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;
    private final BookingService bookingService;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.fromName}")
    private String fromName;

    @Value("${frontend.url}")
    private String feUrl;

    @Async
    @Override
    public CompletableFuture<Boolean> sendActivationEmail(String email, String token) {
        try {
            MimeMessage message = createActivationEmail(email, token);
            emailSender.send(message);
            return CompletableFuture.completedFuture(true);
        } catch (UnsupportedEncodingException | MessagingException e) {
            log.error("Failed to send activation email to {}: {}", email, e.getMessage());
            return CompletableFuture.completedFuture(false);
        }
    }

    @Override
    @Async
    @Transactional
    public void sendBookingInfo(long bookingId) {
        List<BookingInfoDTO> bookingInfoList = bookingService.getBookingInfoList(bookingId);
        if (!bookingInfoList.isEmpty()) {
            String contactEmail = bookingInfoList.get(0).getContactEmail();

            StringBuilder emailContent = new StringBuilder();
            emailContent.append("<html><body>");
            emailContent.append("<div style='font-family: Arial, sans-serif; padding: 20px; background-color: #f4f4f4;'>");

            emailContent.append("<h2 style='color: #1E90FF; text-align: center; font-size: 30px; font-weight: bold;'>Thông tin đặt vé của bạn</h2>");

            for (BookingInfoDTO info : bookingInfoList) {
                emailContent.append("<div style='background-color: white; padding: 15px; margin-bottom: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);'>");
                emailContent.append("<p><b style='color: #333;'>Mã vé:</b> <span style='color: #1E90FF; font-weight: bold;'>").append(info.getTicketCode()).append("</span></p>");
                emailContent.append("<p><b style='color: #333;'>Tên khách hàng:</b> ").append(info.getContactName()).append("</p>");
                emailContent.append("<p><b style='color: #333;'>Tuyến đường:</b> ").append(info.getRouteName()).append("</p>");
                emailContent.append("<p><b style='color: #333;'>Ngày khởi hành:</b> ").append(info.getDepartureDate()).append("</p>");
                emailContent.append("<p><b style='color: #333;'>Số ghế:</b> ").append(info.getSeatNumber()).append("</p>");
                emailContent.append("<p><b style='color: #333;'>Biển số xe:</b> ").append(info.getLicensePlate()).append("</p>");
                emailContent.append("<p><b style='color: #333;'>Thời gian đón dự kiến:</b> ").append(info.getPickupTime()).append("</p>");
                emailContent.append("<p><b style='color: #333;'>Địa điểm đón:</b> ").append(info.getPickupLocation()).append("</p>");
                emailContent.append("<p><b style='color: #333;'>Địa điểm trả:</b> ").append(info.getDropoffLocation()).append("</p>");
                emailContent.append("</div>");
            }

            emailContent.append("<p style='text-align: center; font-size: 14px;'>Nếu có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi qua email này.</p>");
            emailContent.append("</div>");
            emailContent.append("</body></html>");

            try {
                MimeMessage message = emailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(fromEmail, fromName);
                helper.setTo(contactEmail);
                helper.setSubject("Thông tin đặt vé của bạn");
                helper.setText(emailContent.toString(), true);

                emailSender.send(message);
                log.info("Đã gửi email thông tin đặt vé tới: {}", contactEmail);
            } catch (MessagingException | UnsupportedEncodingException e) {
                log.error("Không thể gửi email thông tin đặt vé tới {}: {}", contactEmail, e.getMessage());
            }
        }
    }

    private MimeMessage createActivationEmail(String email, String token)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail, fromName);
        helper.setTo(email);
        helper.setSubject(getActivationEmailSubject());
        helper.setText(getActivationEmailContent(token), true);

        return message;
    }

    private String getActivationEmailSubject() {
        return "Xác nhận tài khoản của bạn";
    }

    private String getActivationEmailContent(String token) {
        String activationLink = String.format("%s/activate?token=%s", feUrl, token);
        return """
               <p>Xin chào,</p>
               <p>Cảm ơn bạn đã đăng ký với Ticket Go.</p>
               <p>Vui lòng nhấn vào liên kết bên dưới để xác nhận tài khoản của bạn:</p>
               <p><a href="%s">Xác nhận tài khoản của tôi</a></p>
               """.formatted(activationLink);
    }
}
