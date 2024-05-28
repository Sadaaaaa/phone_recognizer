package com.example.phone_recognizer;

import com.example.phone_recognizer.dao.PhoneCodeRepository;
import com.example.phone_recognizer.entity.PhoneCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PhoneCodeRepositoryTest {

    @Autowired
    private PhoneCodeRepository phoneCodeRepository;

    @Test
    public void testFindByText() {
        List<PhoneCode> result = phoneCodeRepository.findByText("441624");

        Assertions.assertEquals(result.get(0).getCountry(), "Isle of Man");
    }

    @Test
    public void testFindByText_NoResults() {
        List<PhoneCode> result = phoneCodeRepository.findByText("999");

        Assertions.assertTrue(result.isEmpty());
    }
}

