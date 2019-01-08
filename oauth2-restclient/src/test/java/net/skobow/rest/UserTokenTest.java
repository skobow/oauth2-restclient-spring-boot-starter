package net.skobow.rest;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTokenTest {

    private static final int ONY_DAY = 1;
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";

    @Test
    @SuppressWarnings("squid:S00100")
    public void is_expired_should_work_as_expected() {
        final LocalDateTime validDate = LocalDateTime.now().plusDays(ONY_DAY);
        final UserToken validToken = new UserToken(ACCESS_TOKEN, REFRESH_TOKEN, "", validDate);
        assertThat(validToken.isExpired()).isFalse();

        final LocalDateTime expiredDate = LocalDateTime.now().minusDays(ONY_DAY);
        final UserToken expiredToken = new UserToken(ACCESS_TOKEN, REFRESH_TOKEN, "", expiredDate);

        assertThat(expiredToken.isExpired()).isTrue();
    }
}