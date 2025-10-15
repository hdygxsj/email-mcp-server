package hdygxsj.mcp.email.config;

import hdygxsj.mcp.email.tools.ReadEmail;
import hdygxsj.mcp.email.tools.SendEmail;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolsRegister {

    @Bean
    public ToolCallbackProvider tools(ReadEmail readEmail,
                                      SendEmail sendEmail){
        return MethodToolCallbackProvider.builder()
                .toolObjects(readEmail,sendEmail
                        )
                .build();
    }

}
