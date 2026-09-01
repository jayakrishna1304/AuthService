package com.example.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;


@Service
public class AuthService {

    @Autowired
    AuthRepository db;
    @Autowired
    RetailerServiceInterface retailerclient;

    @Autowired
    JwtService jwtbean;
    @Autowired
    AuthserviceInterface customerclient;

    PasswordEncoder passwordEncoder;

    public AuthService(PasswordEncoder passwordEncoder) {

        this.passwordEncoder = passwordEncoder;
    }

    public String register(Requestdto request) {

        if (db.existsByUserEmail(request.getUser_email())) {
            throw new RuntimeException("User already exists!!");
        }
        System.out.println(request.getUser_password());
        String encodedpass =
                passwordEncoder.encode(request.getUser_password());

        AuthEntity auth = new AuthEntity();

        auth.setUserName(request.getUser_name());
        auth.setUserEmail(request.getUser_email());
        auth.setUserPassword(encodedpass);
        auth.setUserRole(request.getUser_role());
        auth.setUserDate(new Date());

        db.save(auth);
        if(auth.getUserRole().equalsIgnoreCase("customer")){
            authtocustomer(auth);
        } else if (auth.getUserRole().equalsIgnoreCase("retailer")) {
            authtoretailer(auth);

        }

        return "Registered Successfully!!";
    }


    public String Login(LoginRequestdto login) {

        Optional<AuthEntity> user =
                db.findByUserEmail(login.getEmail());

        if (user.isEmpty()) {
            throw new RuntimeException("Invalid email or password!!");
        }

        boolean passcheck = passwordEncoder.matches(
                login.getPassword(),
                user.get().getUserPassword()
        );

        System.out.println(passcheck);
        System.out.println("Stored password starts with: " +
                user.get().getUserPassword().substring(0, 4));

        if (!passcheck) {
            throw new RuntimeException("Invalid email or password!!");
        }

        return jwtbean.generateToken(
                user.get().getUserEmail(),
                user.get().getUserRole()
        );
    }
//    public void testPassword() {
//
//        String raw = "chantijk@123";
//
//        String encoded = passwordEncoder.encode(raw);
//
//        System.out.println("New hash = " + encoded);
//
//        System.out.println(
//                "Match = " + passwordEncoder.matches(raw, encoded)
//        );
//    }
    public void authtocustomer(AuthEntity auth){
        Customerdto customer =new Customerdto();
        customer.setCustomer_name(auth.getUserName());
        customer.setCustomer_id(auth.getUserId());
        customer.setEmail(auth.getUserEmail());
        customer.setCreatedDate(auth.getUserDate());
        customerclient.createcustomer(customer);

    }
    public void authtoretailer(AuthEntity auth) {

        Retailerdto retailer = new Retailerdto();

        retailer.setRetailer_id(auth.getUserId());
        retailer.setRetailer_name(auth.getUserName());
        retailer.setEmail(auth.getUserEmail());
        retailer.setCreatedDate(auth.getUserDate());

        retailerclient.createretailer(retailer);
    }
}