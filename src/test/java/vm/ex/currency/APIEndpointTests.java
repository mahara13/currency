package vm.ex.currency;

import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import vm.ex.currency.configuration.MainConfiguration;
import vm.ex.currency.configuration.MockedWebClientConfiguration;
import vm.ex.currency.handler.CurrencyConverterHandler;
import vm.ex.currency.model.ConversionResponse;
import vm.ex.currency.utils.Util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static vm.ex.currency.utils.Util.*;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MainConfiguration.class, CurrencyConverterHandler.class , MockedWebClientConfiguration.class})
@WebFluxTest
class APIEndpointTests {
	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private MockWebServer mockWebServer;
	private final String endPoint = "/currency/convert";;

	@Test
	void successConversion() throws JsonProcessingException {
		mockWebServer.enqueue(Util.createSuccessResponse());
		webTestClient.post().uri(endPoint)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(fromValue(createConversionRequest(USD_CURRENCY, EUR_CURRENCY, 10D)))
				.exchange()
				.expectStatus().isOk()
				.expectBody(ConversionResponse.class)
				.value(conversionResponse -> {
					assertEquals(conversionResponse.getFrom(), USD_CURRENCY);
					assertEquals(conversionResponse.getTo(), EUR_CURRENCY);
					assertEquals(conversionResponse.getAmount(), 10D);
					assertEquals(conversionResponse.getConverted(), 14D);
				});
	}

	@Test
	void badFormattedConversionRequest() throws JsonProcessingException {
		webTestClient.post().uri(endPoint)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(fromValue(createConversionRequest(USD_CURRENCY, "BadCurrency", 10D)))
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	void notFoundConversion() throws JsonProcessingException {
		mockWebServer.enqueue(Util.createSuccessResponse());
		webTestClient.post().uri(endPoint)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(fromValue(createConversionRequest(USD_CURRENCY, "XYZ", 10D)))
				.exchange()
				.expectStatus().isNotFound();
	}


	@Test
	void fromAndToCurrencyAreEqualsConversion() throws JsonProcessingException {
		webTestClient.post().uri(endPoint)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(fromValue(createConversionRequest(USD_CURRENCY, USD_CURRENCY, 10D)))
				.exchange()
				.expectStatus().isOk()
				.expectBody(ConversionResponse.class)
				.value(conversionResponse -> {
					assertEquals(conversionResponse.getFrom(), USD_CURRENCY);
					assertEquals(conversionResponse.getTo(), USD_CURRENCY);
					assertEquals(conversionResponse.getAmount(), 10D);
					assertEquals(conversionResponse.getConverted(), 10D);
				});
	}
}
