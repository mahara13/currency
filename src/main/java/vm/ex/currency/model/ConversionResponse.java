package vm.ex.currency.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConversionResponse {
    public String from;
    public String to;
    private Double amount;
    private Double converted;

    public ConversionResponse(String from, String to, Double amount, Double converted) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.converted = converted;
    }

    public ConversionResponse(ConversionRequest request, double conversionRate) {
        this.from = request.getFrom();
        this.to = request.getTo();
        this.amount = request.getAmount();

        converted = amount * conversionRate;
    }

    public boolean validate() {
        return isCorrectCurrency(from) && isCorrectCurrency(to);
    }

    private boolean isCorrectCurrency(String currency) {
        return currency != null && currency.length() == 3;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getConverted() {
        return converted;
    }
}
