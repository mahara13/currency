package vm.ex.currency.model.provider;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
public class ProviderResponse1 implements ProviderResponse {
    private Map<String, Double> rates;
    private String base;
    private Date date;

    public Map<String, Double> getRates() {
        return rates;
    }

    public ProviderResponse1(Map<String, Double> rates, String base, Date date) {
        this.rates = rates;
        this.base = base;
        this.date = date;
    }
}
