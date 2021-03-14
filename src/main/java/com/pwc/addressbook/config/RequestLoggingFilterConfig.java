package com.pwc.addressbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class RequestLoggingFilterConfig {

    @Bean
    public RequestLoggingFilter logFilter() {
        final RequestLoggingFilter filter = new RequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST : ");

        return filter;
    }
}

class RequestLoggingFilter extends CommonsRequestLoggingFilter {
    @Override
    protected  void beforeRequest(final HttpServletRequest request, final String message) {

    }
}