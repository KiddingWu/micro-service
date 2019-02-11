package n.kidding.user.service;


import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import n.kidding.thrift.message.MessageService;

@Service
public class MessageServiceImpl implements MessageService.Iface {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public boolean sendMobileMessage(String mobile, String message) throws TException {
        System.out.println("sendMobileMessage, mobile: " + mobile + ", message: " + message);
        return true;
    }

    @Override
    public boolean sendEmailMessage(String email, String message) throws TException {
        System.out.println("sendEmailMessage, email: " + email + ", message: " + message);

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            // 设置收件人，寄件人
            simpleMailMessage.setFrom(from);
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("kidding Mail");
            simpleMailMessage.setText("sendEmailMessage，email: " + email + ", message: " + message);

            //发送邮件
            mailSender.send(simpleMailMessage);
            System.out.println("send mail success");
            return true;
        } catch (Exception e) {
            System.out.println("send mail failed!" + e);
            return false;
        }
    }
}
