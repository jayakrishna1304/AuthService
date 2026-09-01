package com.example.AuthService;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class Requestdto {
    private String user_name;
    private String user_password;
    private String user_email;
    private String user_role;
}
