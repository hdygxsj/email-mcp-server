package hdygxsj.mcp.email.tools;

import hdygxsj.mcp.email.commons.EmailService;
import hdygxsj.mcp.email.entity.EmailContent;
import jakarta.mail.MessagingException;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
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

    /**
     * 读取邮件
     *
     * @param host     邮箱 IMAP 服务器（如 imap.qq.com）
     * @param port     端口（如 993）
     * @param username 邮箱地址（如 xxx@qq.com）
     * @param password 授权码（QQ 邮箱）或应用密码（Gmail）
     * @param useSsl   是否使用 SSL（通常为 true）
     */
    @Tool(description = "读取邮件")
    @GetMapping("/read")
    public List<EmailContent> read(@RequestParam("host") @ToolParam(description = "邮箱 IMAP 服务器") String host,
                                   @RequestParam("num") @ToolParam(description = "读取邮件数量") int num,
                                   @RequestParam("port") @ToolParam(description = "端口") int port,
                                   @RequestParam("username") @ToolParam(description = "收件人用户名") String username,
                                   @RequestParam("password") @ToolParam(description = "收件人密码") String password,
                                   @RequestParam("useSsl") @ToolParam(description = "是否使用 SSL") boolean useSsl) throws MessagingException {
        return readerService.readEmails(host, port, username, password, useSsl,num);
    }
}
