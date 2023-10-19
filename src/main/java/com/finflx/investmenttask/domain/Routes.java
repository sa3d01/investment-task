package com.finflx.investmenttask.domain;

public final class Routes {

    private static final String BASE_URL = "/api/v1";

    public static final String AUTH_URL = BASE_URL + "/auth";
    public static final String ACCOUNT_URL = BASE_URL + "/investment-accounts";
    public static final String ORDER_URL = BASE_URL + "/investment-orders";

    private Routes() {
    }
}
