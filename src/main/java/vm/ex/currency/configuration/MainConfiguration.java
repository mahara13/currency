package vm.ex.currency.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.netty.http.server.HttpServer;
import vm.ex.currency.handler.CurrencyConverterHandler;
import vm.ex.currency.model.provider.ProviderInfo;
import vm.ex.currency.model.provider.ProviderResponse1;
import vm.ex.currency.model.provider.ProviderResponse2;
import vm.ex.currency.service.ExternalProviderService;

import static vm.ex.currency.model.provider.Constants.PROVIDER1_URL;
import static vm.ex.currency.model.provider.Constants.PROVIDER2_URL;

@Configuration
@EnableAutoConfiguration
public class MainConfiguration {

	@Bean
	ProviderInfo[] providerInfos(){
		return  new ProviderInfo[]{new ProviderInfo(PROVIDER1_URL, ProviderResponse1.class),
				new ProviderInfo(PROVIDER2_URL, ProviderResponse2.class)};
	}

	@Bean
	WebClient webClient() {
		return WebClient.builder().build();
	}

	@Bean
	ExternalProviderService conversionProviderService(WebClient webClient, ProviderInfo[] providerInfos) {
		return new ExternalProviderService(webClient, providerInfos);
	}

	@Bean
	RouterFunction<?> router(ExternalProviderService externalProviderService) {
		CurrencyConverterHandler handler = new CurrencyConverterHandler(externalProviderService);

		return RouterFunctions.route(RequestPredicates.POST("/currency/convert").
				and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), handler::getConversion
		);
	}

	@Bean
	HttpServer server(RouterFunction<?> router) {
		HttpHandler handler = RouterFunctions.toHttpHandler(router);
		HttpServer httpServer = HttpServer.create();
		httpServer.port(8080);
		httpServer.host("localhost");
		httpServer.handle(new ReactorHttpHandlerAdapter(handler));
		httpServer.bindNow();
		return  httpServer;
	}
}
