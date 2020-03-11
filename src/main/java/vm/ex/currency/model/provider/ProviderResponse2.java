package vm.ex.currency.model.provider;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
public class ProviderResponse2 implements ProviderResponse {
    private Map<String, Double> rates;
    private String base;
    private Date date;
    private Long time_last_updated;

    public Map<String, Double> getRates() {
        return rates;
    }

    public ProviderResponse2(Map<String, Double> rates, String base, Date date, Long time_last_updated) {
        this.rates = rates;
        this.base = base;
        this.date = date;
        this.time_last_updated = time_last_updated;
    }
}
