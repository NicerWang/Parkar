package nk.parkar.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    int id;
    String name;
    String pwd;

    public Admin(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }
}
