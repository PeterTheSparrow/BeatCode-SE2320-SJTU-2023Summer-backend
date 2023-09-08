package team.beatcode.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
public class Problem {
    private String id;

    @Data
    public static class Title {
        private String name;
        private Integer id;
    }
    private Title title;

    private String detail;
    private String difficulty;

    private Integer version;

    @Data
    public static class Tag {
        private String tag;
        private String caption;
        private String color;
    }
    private List<Tag> tags;

    @Data
    @NoArgsConstructor
    public static class Config {
        private Integer tests;
        /**
         * 单位是毫秒，但评测机用的是秒+三位小数
         */
        private Integer tLimit;
        /**
         * 单位是MB
         */
        private Integer mLimit;
        private Integer oLimit;
    }
    private Config config;

    @JsonIgnore
    private Boolean locked;
}
