package hdygxsj.mcp.email.entity;

import lombok.Data;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;

@Data
public class EmailRequest {

    @ToolParam(description = "收件人邮箱地址")
    private String to;

    @ToolParam(description = "发件人邮箱地址")
    private String from;

    @ToolParam(description = "抄送人邮箱地址")
    private String cc;

    @ToolParam(description = "发件人用户名")
    private String username;

    @ToolParam(description = "发件人密码")
    private String password;

    @ToolParam(description = "邮件主题")
    private String subject;

    @ToolParam(description = "邮件内容")
    private String content;

    @ToolParam(description = "邮件附件")
    private List<String> attachments;

    @ToolParam(description = "邮件服务器地址")
    private String host;

    @ToolParam(description = "邮件服务器端口")
    private int port;

    @ToolParam(description = "是否使用SSL")
    private boolean useSsl = true;
}
