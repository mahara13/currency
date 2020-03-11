package vm.ex.currency.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import vm.ex.currency.model.*;
import vm.ex.currency.service.ExternalProviderService;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class CurrencyConverterHandler {
    ExternalProviderService externalProviderService;

    public CurrencyConverterHandler(ExternalProviderService externalProviderService) {
        this.externalProviderService = externalProviderService;
    }

    public Mono<ServerResponse> getConversion(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ConversionRequest.class)
                .flatMap(conversionRequest ->
                {
                    if (conversionRequest.validate() == false) {
                        return ServerResponse.badRequest().body(fromValue("Please provide valid conversion request"));
                    } else if (conversionRequest.isFromEqualsTo()) {
                        return ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(new ConversionResponse(conversionRequest, 1d)));
                    }

                    return externalProviderService.getProviderResponseMono(conversionRequest.getFrom())
                            .flatMap(providerResponse -> {
                                Double conversionRate = providerResponse.getRates().get(conversionRequest.getTo());
                                if (conversionRate != null) {
                                    return ServerResponse.ok()
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .body(fromValue(new ConversionResponse(conversionRequest, conversionRate)));
                                } else {
                                    return ServerResponse.notFound().build();
                                }
                            }).switchIfEmpty(ServerResponse.notFound().build());
                }).switchIfEmpty(ServerResponse.notFound().build());
    }
}
