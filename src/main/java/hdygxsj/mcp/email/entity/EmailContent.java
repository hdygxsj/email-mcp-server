package hdygxsj.mcp.email.entity;

import lombok.Data;

import java.util.Date;

@Data
public class EmailContent {
    private String subject;
    private String from;
    private Date sentDate;
    private String content;

    public EmailContent(String subject, String from, Date sentDate, String content) {
        this.subject = subject;
        this.from = from;
        this.sentDate = sentDate;
        this.content = content;
    }

}
