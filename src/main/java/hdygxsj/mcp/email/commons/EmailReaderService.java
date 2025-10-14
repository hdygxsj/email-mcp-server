package hdygxsj.mcp.email.commons;

import hdygxsj.mcp.email.config.EmailConfig;
import hdygxsj.mcp.email.entity.EmailContent;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class EmailReaderService {

    @Autowired
    private EmailConfig emailConfig;

    /**
     * 动态读取指定邮箱的最新邮件
     *
     * @param host     邮箱 IMAP 服务器（如 imap.qq.com）
     * @param port     端口（如 993）
     * @param username 邮箱地址（如 xxx@qq.com）
     * @param password 授权码（QQ 邮箱）或应用密码（Gmail）
     * @param useSsl   是否使用 SSL（通常为 true）
     */
    public List<EmailContent> readEmails(String host, int port, String username, String password, boolean useSsl, int num)
            throws MessagingException {
        List<EmailContent> results = new ArrayList<>();
        Properties props = new Properties();
        props.put("mail.store.protocol", emailConfig.getProtocol());
        props.put("mail.imaps.host", host);
        props.put("mail.imaps.port", port);
        props.put("mail.imaps.ssl.enable", String.valueOf(useSsl));
        // 信任所有证书（仅开发测试用，生产环境应配置证书）
        if (useSsl) {
            props.put("mail.imaps.ssl.trust", "*");
        }

        Session session = Session.getInstance(props);
        try (Store store = session.getStore("imaps")) {
            store.connect(username, password); // 动态认证

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // 读取最近5封邮件
            Message[] messages = inbox.getMessages();
            int start = Math.max(messages.length - num, 0);
            for (int i = messages.length - 1; i >= start; i--) {
                Message msg = messages[i];
                EmailContent msgContent = new EmailContent(
                        msg.getSubject(),
                        msg.getFrom()[0].toString(),
                        msg.getSentDate(),
                        getTextFromMessage(msg)
                );
                results.add(msgContent);
            }
            inbox.close(false);
        } catch (MessagingException e) {
            throw new MessagingException("邮箱连接或读取失败: " + e.getMessage(), e);
        }
        return results;
    }

    private String getTextFromMessage(Message message) {
        try {
            if (message.isMimeType("text/plain")) {
                return message.getContent().toString();
            } else if (message.isMimeType("text/html")) {
                return message.getContent().toString();
            } else if (message.isMimeType("multipart/*")) {
                MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                return getTextFromMimeMultipart(mimeMultipart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[无法解析内容]";
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            var bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent());
                break;
            } else if (bodyPart.isMimeType("text/html")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }
}
