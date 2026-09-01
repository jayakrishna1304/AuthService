package com.example.AuthService;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class Retailerdto {
    private int retailer_id;
    private String retailer_name;
    private String email;
    private Date createdDate;
}
