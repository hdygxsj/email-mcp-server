package hdygxsj.mcp.email.tools;

import hdygxsj.mcp.email.commons.EmailService;
import hdygxsj.mcp.email.entity.EmailContent;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/email")
public class ReadEmail {

    @Autowired
    private EmailService readerService;
    @GetMapping("/read")
    public List<EmailContent> read(@RequestParam("host") String host,
                                   @RequestParam("num") int num,
                                   @RequestParam("port") int port,
                                   @RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   @RequestParam("useSsl") boolean useSsl) throws MessagingException {
        return readerService.readEmails(host, port, username, password, useSsl,num);
    }
}
