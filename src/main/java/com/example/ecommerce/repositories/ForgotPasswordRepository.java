package com.example.ecommerce.repositories;

import com.example.ecommerce.entity.ForgotPassword;
import com.example.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword,Integer> {

    @Query("select fp from ForgotPassword fp where fp.otp= ?1 and fp.user= ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);
}
