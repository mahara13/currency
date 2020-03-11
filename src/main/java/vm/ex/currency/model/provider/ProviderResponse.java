package vm.ex.currency.model.provider;

import java.util.Map;

public interface ProviderResponse {
    Map<String, Double> getRates();
}
