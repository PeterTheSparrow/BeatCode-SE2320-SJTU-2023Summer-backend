package team.beatcode.consumer.utils.context;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import team.beatcode.consumer.utils.Macros;

@Data
public class UserContext {
    @JsonAlias({Macros.USER_CONTEXT_ID})
    private Integer user_id;
    @JsonAlias({Macros.USER_CONTEXT_NAME})
    private String user_name;
    @JsonAlias({Macros.USER_CONTEXT_ROLE})
    private Integer user_role;
}
