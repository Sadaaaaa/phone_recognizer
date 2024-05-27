package com.example.phone_recognizer.service;

import org.springframework.http.ResponseEntity;

public interface CheckPhoneService {
    ResponseEntity<?> checkPhone(String phone);
}
