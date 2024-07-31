package com.example.ecommerce.controller;


import com.example.ecommerce.entity.ForgotPassword;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repositories.ForgotPasswordRepository;
import com.example.ecommerce.repositories.UserRepository;
import com.example.ecommerce.services.EmailService;
import com.example.ecommerce.utils.ChangePassword;
import com.example.ecommerce.utils.MailBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("forgotPassword")
public class ForgotPasswordController {

    private final UserRepository userRepository;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordController(UserRepository userRepository, ForgotPasswordRepository forgotPasswordRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("sendOTP/{email}")
    public ResponseEntity<String> sentOTP(@PathVariable("email") String email) {

         User user=userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User name not found with the mentioned email"));

         Integer otp=otpGenerator();
        MailBody mailBody= MailBody.builder()
                .to(email)
                .subject("OTP for forgot password")
                .text("The OTP for you forgot password is : " + otp)
                .build();

        ForgotPassword fp= ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 70*1000) )
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);

        return ResponseEntity.ok("Email sent for verification successfully");
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOTP(@PathVariable("email") String email, @PathVariable("otp") Integer otp) {

        User user=userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found with the mentioned email"));

        ForgotPassword fp=forgotPasswordRepository.findByOtpAndUser(otp,user)
                .orElseThrow(()->new RuntimeException("Invalid OTP for email :"+email));

        if(fp.getExpirationTime().before(Date.from(Instant.now()))){

            forgotPasswordRepository.delete(fp);
            return new ResponseEntity<>("OTP has expired", HttpStatus.EXPECTATION_FAILED);

        }

        return ResponseEntity.ok("OTP has been verified successfully");

    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword,@PathVariable("email") String email){

        if(!Objects.equals(changePassword.password(),changePassword.repeatPassword())){
            return new ResponseEntity<>("Please enter thye password again",HttpStatus.EXPECTATION_FAILED);
        }

        String encodedPassword=passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(email, encodedPassword);

        return ResponseEntity.ok("Password changed successfully");
    }




    public Integer otpGenerator(){
        Random random = new Random();
        return random.nextInt(100000,999999);
    }

}
