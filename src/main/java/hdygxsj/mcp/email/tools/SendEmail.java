package hdygxsj.mcp.email.tools;

import hdygxsj.mcp.email.commons.EmailService;
import hdygxsj.mcp.email.entity.EmailRequest;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class SendEmail {
    
    @Autowired
    private EmailService emailService;
    
    @PostMapping("/send")
    public void sendEmail(@RequestBody EmailRequest request) throws Exception {
        emailService.sendEmail(request);
    }
}
