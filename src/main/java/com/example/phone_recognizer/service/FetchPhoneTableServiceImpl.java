package com.example.phone_recognizer.service;

import com.example.phone_recognizer.dao.PhoneCodeRepository;
import com.example.phone_recognizer.entity.PhoneCode;
import com.example.phone_recognizer.helpers.PhoneCodeEntityHelper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FetchPhoneTableServiceImpl implements FetchPhoneTableService {

    @Value("${wikiLink}")
    private String wikiLink;
    private final List<String> countriesWithGroups = PhoneCodeEntityHelper.getCountriesWithGroup();
    private final PhoneCodeRepository phoneCodeRepository;

    public List<String> getCountriesWithGroups() {
        return countriesWithGroups;
    }

    @Autowired
    public FetchPhoneTableServiceImpl(PhoneCodeRepository phoneCodeRepository) {
        this.phoneCodeRepository = phoneCodeRepository;
    }

    @PostConstruct
    public void fetchPhoneCodes() throws IOException {
        // fetching markup
        Document document = Jsoup.connect(wikiLink).get();
        // select table
        Element table = document.selectFirst("table.wikitable.sortable.sticky-header-multi");
        List<PhoneCode> phoneCodes = new ArrayList<>();

        if (table != null) {
            Elements rows = table.select("tr");
            for (Element row : rows) {
                Elements cells = row.select("td");
                if (cells.size() >= 2) {
                    String country = cells.get(0).text();
                    String phoneCode = cells.get(1).text();

                    phoneCodes.addAll(this.phoneCodesParser(country, phoneCode));
                }
            }
        }

        // save to db
        List<PhoneCode> savedToDb = phoneCodeRepository.saveAll(phoneCodes);

        log.warn("Entities saved: {}, from {}", savedToDb.size(), phoneCodes.size());
        if (savedToDb.size() == phoneCodes.size()) {
            log.warn("All data saved to db!");
        } else {
            log.error("Error saving data to db!");
        }
    }

    public List<PhoneCode> phoneCodesParser(String country, String phoneCode) {
        List<PhoneCode> result = new ArrayList<>();
        int bracketIndex = phoneCode.indexOf('(');

        if (bracketIndex != -1) {
            String[] primaryCodes = phoneCode.substring(0, bracketIndex).trim().split(",\\s*");

            String areaCodeSubstring = phoneCode.substring(bracketIndex + 1, phoneCode.indexOf(')', bracketIndex)).trim();
            if (!areaCodeSubstring.isEmpty()) {
                String[] areaCodes = areaCodeSubstring.split(",\\s*");

                for (String primaryCode : primaryCodes) {
                    for (String areaCode : areaCodes) {
                        result.add(this.buildPhoneCode(country, primaryCode + areaCode, phoneCode));
                    }
                }
            }
        } else {
            String[] items = phoneCode.split(",\\s*");
            for (String item : items) {
                result.add(this.buildPhoneCode(country, item, phoneCode));
            }
        }

        return result;
    }

    public PhoneCode buildPhoneCode(String country, String countryCode, String phoneCode) {
        return PhoneCode.builder()
                .country(country)
                .code(countryCode)
                .defaultCountry(PhoneCodeEntityHelper.getDefaultCountry(countryCode))
                .description(phoneCode)
                .isHasGroup(countriesWithGroups.contains(country))
                .build();
    }
}
