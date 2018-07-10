package com.binance.api.client;

import com.binance.api.client.impl.FcoinApiClientImpl;

/**
 * A factory for creating FcoinApi client objects.
 */
public class FcoinApiClientFactory {

    /**
     * API Key
     */
    private String apiKey;

    /**
     * Secret.
     */
    private String secret;

    /**
     * Instantiates a new fcoin api client factory.
     *
     * @param apiKey the API key
     * @param secret the Secret
     */
    private FcoinApiClientFactory(String apiKey, String secret) {
        this.apiKey = apiKey;
        this.secret = secret;
    }

    /**
     * New instance.
     *
     * @param apiKey the API key
     * @param secret the Secret
     * @return the fcoin api client factory
     */
    public static FcoinApiClientFactory newInstance(String apiKey, String secret) {
        return new FcoinApiClientFactory(apiKey, secret);
    }

    /**
     * New instance without authentication.
     *
     * @return the fcoin api client factory
     */
    public static FcoinApiClientFactory newInstance() {
        return new FcoinApiClientFactory(null, null);
    }

    /**
     * Creates a new web socket client used for handling data streams.
     */
    public FcoinApiClient newClient() {
        return new FcoinApiClientImpl();
    }
}
