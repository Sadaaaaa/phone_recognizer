package com.example.phone_recognizer.dao;

import com.example.phone_recognizer.entity.PhoneCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneCodeRepository extends JpaRepository<PhoneCode, Long> {

    @Query("SELECT p FROM PhoneCode p WHERE p.code like concat(:text, '%')")
    List<PhoneCode> findByText(String text);
}
