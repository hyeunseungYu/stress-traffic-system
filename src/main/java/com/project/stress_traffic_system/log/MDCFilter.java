/*
package com.project.stress_traffic_system.log;

import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;


@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MDCFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final String trxId = UUID.randomUUID().toString().substring(0,8);
        MDC.put("request_id", trxId);

        filterChain.doFilter(servletRequest, servletResponse);

        MDC.clear();
    }
}
*/
