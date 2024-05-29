package com.example.phone_recognizer;

import com.example.phone_recognizer.dao.PhoneCodeRepository;
import com.example.phone_recognizer.entity.PhoneCode;
import com.example.phone_recognizer.helpers.PhoneCodeEntityHelper;
import com.example.phone_recognizer.service.FetchPhoneTableServiceImpl;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FetchPhoneTableServiceImplTest {

    @Mock
    private PhoneCodeRepository phoneCodeRepository;

    @InjectMocks
    private FetchPhoneTableServiceImpl phoneCodeService;

    @Value("${wikiLink}")
    private String wikiLink;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(phoneCodeService, "wikiLink", wikiLink);
    }

    @Test
    void fetchPhoneCodesTest() throws IOException {
        PhoneCodeRepository mockPhoneCodeRepository = mock(PhoneCodeRepository.class);
        FetchPhoneTableServiceImpl phoneCodeService = new FetchPhoneTableServiceImpl(mockPhoneCodeRepository);

        Connection mockConnection = mock(Connection.class);
        Document mockDocument = mock(Document.class);
        Element mockTable = mock(Element.class);
        Elements mockRows = new Elements();
        Element mockRow = mock(Element.class);
        Elements mockCells = new Elements();

        Element mockCell1 = mock(Element.class);
        when(mockCell1.text()).thenReturn("Country");

        Element mockCell2 = mock(Element.class);
        when(mockCell2.text()).thenReturn("1");

        mockCells.add(mockCell1);
        mockCells.add(mockCell2);
        when(mockRow.select("td")).thenReturn(mockCells);
        mockRows.add(mockRow);

        when(mockTable.select("tr")).thenReturn(mockRows);
        when(mockDocument.selectFirst("table.wikitable.sortable.sticky-header-multi")).thenReturn(mockTable);
        when(mockConnection.get()).thenReturn(mockDocument);

        try (MockedStatic<Jsoup> jsoupMockedStatic = mockStatic(Jsoup.class)) {
            jsoupMockedStatic.when(() -> Jsoup.connect(wikiLink)).thenReturn(mockConnection);
            List<PhoneCode> mockPhoneCodes = new ArrayList<>();
            mockPhoneCodes.add(PhoneCode.builder()
                    .country("United States")
                    .code("1")
                    .defaultCountry("probably North America")
                    .description("1")
                    .isHasGroup(true)
                    .build());

            when(mockPhoneCodeRepository.saveAll(any())).thenReturn(mockPhoneCodes);

            phoneCodeService.fetchPhoneCodes();

            verify(mockPhoneCodeRepository, times(1)).saveAll(any());
        }
    }


    @Test
    void phoneCodesParserTest() {
        String country = "United States";
        String phoneCode = "1, 2 (123, 456)";

        List<PhoneCode> result = phoneCodeService.phoneCodesParser(country, phoneCode);

        assertEquals(4, result.size());
        assertEquals("1123", result.get(0).getCode());
        assertEquals("1456", result.get(1).getCode());
        assertEquals("2123", result.get(2).getCode());
        assertEquals("2456", result.get(3).getCode());
    }

    @Test
    void buildPhoneCodeTest() {
        String country = "United States";
        String countryCode = "1";
        String phoneCode = "1, 2 (123, 456)";

        PhoneCode result = phoneCodeService.buildPhoneCode(country, countryCode, phoneCode);

        assertEquals(country, result.getCountry());
        assertEquals(countryCode, result.getCode());
        assertEquals(phoneCode, result.getDescription());
        assertEquals(PhoneCodeEntityHelper.getDefaultArea(countryCode), result.getDefaultCountry());
        assertEquals(phoneCodeService.getCountriesWithGroups().containsKey(country), result.getIsHasGroup());
    }

}
