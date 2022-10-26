package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.*;

@Data
public class User {
    private Set<Integer> likes = new HashSet<>();
    private Set<Integer> friends = new HashSet<>();
    @NotNull
    private int id;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @Past
    private Date birthday;
    public User(){

    }

    public User(int id, String email, String login, Date birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }

    public User(int id, String email, String login, String name, Date birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
        this.name = name;
    }

    public Map<String,Object> toMap() {

            Map<String, Object> values = new HashMap<>();
            values.put("email", this.email);
            values.put("login", this.login);
            if(this.name.equals("")){
                values.put("name",this.login);
            }else{
                values.put("name",this.name);
            }
            values.put("birthday", this.birthday);
            return values;

    }
}
