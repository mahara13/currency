package vm.ex.currency.configuration;

import okhttp3.mockwebserver.MockWebServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import vm.ex.currency.model.provider.ProviderInfo;
import vm.ex.currency.model.provider.ProviderResponse1;
import vm.ex.currency.model.provider.ProviderResponse2;

@TestConfiguration
public class MockedWebClientConfiguration {

    @Bean
    MockWebServer mockWebServer() {
        return new MockWebServer();
    }

    @Bean
    ProviderInfo[] providerInfos(){
        return  new ProviderInfo[]{new ProviderInfo("/{base}", ProviderResponse1.class),
                new ProviderInfo("/{base}", ProviderResponse2.class)};
    }

    @Bean
    WebClient webClient(MockWebServer mockWebServer) {
        return WebClient.create(mockWebServer.url("/").toString());
    }

    @Bean
    WebTestClient webTestClient(ApplicationContext context) {
        return WebTestClient.bindToApplicationContext(context).build();
    }
}
