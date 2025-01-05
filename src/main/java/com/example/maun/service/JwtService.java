package com.example.maun.service;

import com.example.maun.entity.User;
import org.springframework.stereotype.Service;
import java.util.Map;


@Service
public interface JwtService {

 String generateToken(User user, Map<String, Object> extraClaims) ;
 void saveToken(String key, String token) ;
 String getToken(String key) ;

}
