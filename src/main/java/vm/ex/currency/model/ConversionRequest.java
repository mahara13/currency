package vm.ex.currency.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConversionRequest{
    private String from;
    private String to;
    private Double amount;

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Double getAmount() {
        return amount;
    }

    public ConversionRequest(String from, String to, Double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public boolean validate() {
        return isCorrectCurrency(from) && isCorrectCurrency(to);
    }

    private boolean isCorrectCurrency(String currency) {
        return currency != null && currency.length() == 3;
    }

    public boolean isFromEqualsTo() {
        return validate() && from.equals(to);
    }
}
