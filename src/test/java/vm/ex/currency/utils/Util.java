package vm.ex.currency.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import vm.ex.currency.model.ConversionRequest;
import vm.ex.currency.model.provider.ProviderResponse1;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Util {
    public static String EUR_CURRENCY = "EUR";
    public static String USD_CURRENCY = "USD";
    public static double EUR_CURRENCY_RATE = 1.4;

    public static MockResponse createNotFoundResponse() {
        return new MockResponse()
                .setResponseCode(404)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("");
    }

    public static MockResponse createSuccessResponse() throws JsonProcessingException {
        return createSuccessResponse(USD_CURRENCY, EUR_CURRENCY, EUR_CURRENCY_RATE);
    }

    public static MockResponse createSuccessResponse(String baseCurrency, String toCurrency, Double rate) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Double> map = new HashMap<>();
        map.put(toCurrency, rate);
        ProviderResponse1 providerResponse1 = new ProviderResponse1(map, baseCurrency, new Date());
        String body = objectMapper.writeValueAsString(providerResponse1);
        return new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(body);
    }

    public static String createConversionRequest(String from, String to, Double amount) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(new ConversionRequest(from, to, amount));
    }
}
