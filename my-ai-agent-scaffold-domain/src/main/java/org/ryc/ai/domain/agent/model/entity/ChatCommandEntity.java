package org.ryc.ai.domain.agent.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ChatCommandEntity
 * @Description
 * @Author admin
 * @Time 2026/7/14 10:50
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatCommandEntity {

    private String userId ;

    private String sessionId;

    private String agentId;

    private String userMessage;

    private List<Content.File> files;

    private List<Content.Text> texts;

    private List<Content.InlineData> inlineDatas;


    @Data
    public static class Content{

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Text {
            private String message;

        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class File {
            private String fileUri;
            private String mimeType;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class InlineData {
            private byte[] bytes;
            private String mimeType;
        }

    }

}
