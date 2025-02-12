package vn.project.group_lottery.dto.Request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOSession implements Serializable {
    private long id;
    private String username;
    private String avatar;
    private long balance;
}
