package com.example.phone_recognizer.service;

import com.example.phone_recognizer.dao.PhoneCodeRepository;
import com.example.phone_recognizer.entity.PhoneCode;
import com.example.phone_recognizer.helpers.PhoneCodeEntityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckPhoneServiceImpl implements CheckPhoneService {

    private final PhoneCodeRepository phoneCodeRepository;

    @Autowired
    public CheckPhoneServiceImpl(PhoneCodeRepository phoneCodeRepository) {
        this.phoneCodeRepository = phoneCodeRepository;
    }

    @Override
    public ResponseEntity<?> checkPhone(String phone) {
        String phoneCode = phone.substring(0, phone.length() - 7);
        String searchText = "";

        List<PhoneCode> countriesWithGroups = new ArrayList<>();
        for (int i = 0; i < phoneCode.length(); i++) {
            String character = String.valueOf(phone.charAt(i));

            searchText = searchText + character;
            List<PhoneCode> entities = phoneCodeRepository.findByText(searchText);

            if (entities.stream().anyMatch(PhoneCode::getIsHasGroup)) {
                countriesWithGroups = entities.stream().filter(PhoneCode::getIsHasGroup).collect(Collectors.toList());
            }

            if (entities.size() == 1 && i == entities.get(0).getCode().length() - 1) {
                return ResponseEntity.ok().body(entities.get(0).getCountry());
            } else if (entities.size() == 0 || i == phoneCode.length() - 1) {
                return this.findCountryWithGroup(countriesWithGroups, phone);
            }
        }

        return ResponseEntity.internalServerError().body("Error checking phone number!");
    }

    public ResponseEntity<?> findCountryWithGroup(List<PhoneCode> countriesWithGroups, String phone) {
        if (countriesWithGroups.size() >= 1) {
            String countriesString = countriesWithGroups.stream()
                    .map(PhoneCode::getCountry)
                    .distinct()
                    .collect(Collectors.joining(" or "));
            return ResponseEntity.ok().body(countriesString);
        }

        return ResponseEntity.ok().body(PhoneCodeEntityHelper.getDefaultArea(phone));
    }
}
