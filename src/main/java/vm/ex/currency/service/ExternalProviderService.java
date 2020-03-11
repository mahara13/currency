package vm.ex.currency.service;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import vm.ex.currency.model.provider.ProviderInfo;
import vm.ex.currency.model.provider.ProviderResponse;

import java.util.Random;

public class ExternalProviderService {
    WebClient webClient;
    public ProviderInfo[] providerInfos;

    public ExternalProviderService(WebClient webClient, ProviderInfo[] providerInfos) {
        this.webClient = webClient;
        this.providerInfos = providerInfos;
    }

    public Mono<ProviderResponse> getProviderResponseMono(String base) {
        boolean isFirst = new Random().nextBoolean();
        ProviderInfo firstProviderInfo = isFirst ? providerInfos[0] : providerInfos[1];
        firstProviderInfo.setBase(base);
        ProviderInfo secondProviderInfo = !isFirst ? providerInfos[0] : providerInfos[1];
        secondProviderInfo.setBase(base);

        return getProviderResponseMono(firstProviderInfo,
                getProviderResponseMono(secondProviderInfo, Mono.empty()));
    }

    private Mono<ProviderResponse> getProviderResponseMono(ProviderInfo providerInfo, Mono<ProviderResponse> providerResponseMono) {

        return webClient.get()
                .uri(providerInfo.getUrl(), providerInfo.getBase())
                .headers(httpHeaders -> {
                    if (providerInfo.requireAuthorization()) {
                        httpHeaders.add("Authorization", providerInfo.getAuthorizationHeader());
                    }
                })
                .exchange()
                .flatMap(clientResponse -> {
                    if (clientResponse.statusCode().isError()) {
                        return providerResponseMono;
                    } else {
                        return clientResponse.bodyToMono(providerInfo.getClazz());
                    }
                });
    }
}
