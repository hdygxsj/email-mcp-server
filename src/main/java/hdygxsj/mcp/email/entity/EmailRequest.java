package hdygxsj.mcp.email.entity;

import lombok.Data;

import java.util.List;

@Data
public class EmailRequest {
    private String to;
    private String from;
    private String cc;
    private String username;
    private String password;
    private String subject;
    private String content;
    private List<String> attachments;
    private String host;
    private int port;
    private boolean useSsl;
}
