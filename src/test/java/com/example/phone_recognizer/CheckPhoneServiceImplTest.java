package com.example.phone_recognizer;


import com.example.phone_recognizer.dao.PhoneCodeRepository;
import com.example.phone_recognizer.entity.PhoneCode;
import com.example.phone_recognizer.helpers.PhoneCodeEntityHelper;
import com.example.phone_recognizer.service.CheckPhoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CheckPhoneServiceImplTest {

    @Mock
    private PhoneCodeRepository phoneCodeRepository;

    @InjectMocks
    private CheckPhoneServiceImpl checkPhoneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkPhone_singleEntityFound() {
        String phone = "71234567890";
        String phoneCode = "1123";

        List<PhoneCode> mockEntities = new ArrayList<>();
        PhoneCode mockPhoneCode = new PhoneCode();
        mockPhoneCode.setCountry("Russia");
        mockPhoneCode.setCode(phoneCode);
        mockPhoneCode.setIsHasGroup(false);
        mockEntities.add(mockPhoneCode);

        when(phoneCodeRepository.findByText(anyString())).thenReturn(mockEntities);

        ResponseEntity<?> response = checkPhoneService.checkPhone(phone);

        assertEquals(ResponseEntity.ok().body("Russia"), response);
    }

    @Test
    void checkPhone_noEntitiesFound() {
        String phone = "11234567890";

        when(phoneCodeRepository.findByText(anyString())).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = checkPhoneService.checkPhone(phone);

        assertEquals(ResponseEntity.ok().body(PhoneCodeEntityHelper.getDefaultCountry(phone)), response);
    }

    @Test
    void checkPhone_multipleEntitiesWithGroupFound() {
        String phone = "11234567890";
        String phoneCode = "1123";

        List<PhoneCode> mockEntities = new ArrayList<>();
        PhoneCode mockPhoneCode1 = new PhoneCode();
        mockPhoneCode1.setCountry("United States");
        mockPhoneCode1.setCode(phoneCode);
        mockPhoneCode1.setIsHasGroup(true);
        mockEntities.add(mockPhoneCode1);

        PhoneCode mockPhoneCode2 = new PhoneCode();
        mockPhoneCode2.setCountry("Canada");
        mockPhoneCode2.setCode(phoneCode);
        mockPhoneCode2.setIsHasGroup(true);
        mockEntities.add(mockPhoneCode2);

        when(phoneCodeRepository.findByText(anyString())).thenReturn(mockEntities);

        ResponseEntity<?> response = checkPhoneService.checkPhone(phone);

        assertEquals(ResponseEntity.ok().body("United States or Canada"), response);
    }

    @Test
    void findCountryWithGroup_multipleGroups() {
        String phone = "11234567890";

        List<PhoneCode> countriesWithGroups = new ArrayList<>();
        PhoneCode mockPhoneCode1 = new PhoneCode();
        mockPhoneCode1.setCountry("United States");
        countriesWithGroups.add(mockPhoneCode1);

        PhoneCode mockPhoneCode2 = new PhoneCode();
        mockPhoneCode2.setCountry("Canada");
        countriesWithGroups.add(mockPhoneCode2);

        ResponseEntity<?> response = checkPhoneService.findCountryWithGroup(countriesWithGroups, phone);

        assertEquals(ResponseEntity.ok().body("United States or Canada"), response);
    }

    @Test
    void findCountryWithGroup_singleGroup() {
        String phone = "11234567890";

        List<PhoneCode> countriesWithGroups = new ArrayList<>();
        PhoneCode mockPhoneCode = new PhoneCode();
        mockPhoneCode.setCountry("United States");
        countriesWithGroups.add(mockPhoneCode);

        ResponseEntity<?> response = checkPhoneService.findCountryWithGroup(countriesWithGroups, phone);

        assertEquals(ResponseEntity.ok().body("United States"), response);
    }

    @Test
    void findCountryWithGroup_noGroups() {
        String phone = "11234567890";
        List<PhoneCode> countriesWithGroups = new ArrayList<>();

        ResponseEntity<?> response = checkPhoneService.findCountryWithGroup(countriesWithGroups, phone);

        assertEquals(ResponseEntity.ok().body(PhoneCodeEntityHelper.getDefaultCountry(phone)), response);
    }
}
