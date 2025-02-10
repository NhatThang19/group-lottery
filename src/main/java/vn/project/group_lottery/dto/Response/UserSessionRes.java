package vn.project.group_lottery.dto.Response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSessionRes implements Serializable {
    private long id;
    private String username;
    private String avatar;
    private long balance;
}
