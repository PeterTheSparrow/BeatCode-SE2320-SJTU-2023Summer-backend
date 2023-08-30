package team.beatcode.qbank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "problem_mongo")
public class Problem {
    @Id
    private String id;

    @Data
    public static class Title {
        @Field("name")
        private String name;
        @Field("id")
        private Integer id;
    }
    @Field("title")
    private Title title;

    @Field("description")
    private String detail;
    @Field("difficulty")
    private String difficulty;

    @Field("version")
    private Integer version;

    @Data
    public static class Tag {
        @Field("name")
        private String tag;
        @Field("description")
        private String caption;
        @Field("color")
        private String color;
    }
    @Field("tags")
    private List<Tag> tags;

    @Data
    @NoArgsConstructor
    public static class Config {
        @Field("test_num")
        private Integer tests;
        /**
         * 单位是毫秒，但评测机用的是秒+三位小数
         */
        @Field("time_limit")
        private Integer tLimit;
        /**
         * 单位是MB
         */
        @Field("memory_limit")
        private Integer mLimit;
        @Field("output_limit")
        private Integer oLimit;
    }
    @Field("config")
    private Config config;

    @JsonIgnore
    @Field("locked")
    private Boolean locked;
}
