package vm.ex.currency.model.provider;

public class ProviderInfo {
    private final String url;
    private String base;
    private final Class clazz;
    private String authorizationHeader;

    public ProviderInfo(String url, Class clazz) {
        this.url = url;
        this.clazz = clazz;
    }

    public ProviderInfo(String url, Class clazz, String authorizationHeader) {
        this.url = url;
        this.clazz = clazz;
        this.authorizationHeader = authorizationHeader;
    }

    public String getUrl() {
        return url;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getAuthorizationHeader() {
        return authorizationHeader;
    }

    public boolean requireAuthorization() {
        return authorizationHeader != null && authorizationHeader.trim().length() != 0;
    }
}
