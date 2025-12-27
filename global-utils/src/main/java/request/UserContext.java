package request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContext {

    private Long id;
    private String username;
    private String profileImageUrl;
    private String token;

}
