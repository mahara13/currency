package vm.ex.currency.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vm.ex.currency.configuration.MainConfiguration;
import vm.ex.currency.configuration.MockedWebClientConfiguration;
import vm.ex.currency.utils.Util;
import vm.ex.currency.model.provider.ProviderResponse;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MainConfiguration.class, MockedWebClientConfiguration.class})
class ExtyernalProviderServiceTest {

    @Autowired
    private MockWebServer mockWebServer;

    @Autowired
    private ExternalProviderService externalProviderService;

    @Test
    void getProviderResponseMonoIfProviderAvailable() throws JsonProcessingException {
        mockWebServer.enqueue(Util.createSuccessResponse());
        ProviderResponse usdCurrency = externalProviderService.getProviderResponseMono(Util.USD_CURRENCY).block();
        assertNotNull(usdCurrency);
        assertNotNull(usdCurrency.getRates().get(Util.EUR_CURRENCY));
        assertEquals(usdCurrency.getRates().get(Util.EUR_CURRENCY), Util.EUR_CURRENCY_RATE);
    }

    @Test
    void getProviderResponseMonoIfFirstProviderIsAbsentAndSecondProviderAvailable() throws JsonProcessingException {
        mockWebServer.enqueue(Util.createNotFoundResponse());
        mockWebServer.enqueue(Util.createSuccessResponse());
        ProviderResponse usdCurrency = externalProviderService.getProviderResponseMono(Util.USD_CURRENCY).block();
        assertNotNull(usdCurrency);
        assertNotNull(usdCurrency.getRates().get(Util.EUR_CURRENCY));
        assertEquals(usdCurrency.getRates().get(Util.EUR_CURRENCY), Util.EUR_CURRENCY_RATE);
    }

    @Test
    void getEmptyProviderResponseMonoIfBothProvidersNotAvailable() throws JsonProcessingException {
        mockWebServer.enqueue(Util.createNotFoundResponse());
        mockWebServer.enqueue(Util.createNotFoundResponse());
        ProviderResponse noCurrency = externalProviderService.getProviderResponseMono("US").block();
        assertNull(noCurrency);
    }
}
