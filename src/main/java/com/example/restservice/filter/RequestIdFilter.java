package com.example.restservice.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * Servlet filter to capture x-request-id from HTTP headers and add it to the
 * Mapped Diagnostic Context (MDC) for logging.
 * If x-request-id is not present in the headers, a new UUID is generated.
 * 
 * @author Application Development Team
 * @since 1.0
 */
@Component
public class RequestIdFilter implements Filter {

    private static final String REQUEST_ID_HEADER = "x-request-id";
    private static final String REQUEST_ID_MDC_KEY = "requestId";

    /**
     * Filters the request to extract or generate a request ID and put it in MDC.
     * The request ID is removed from MDC after the request is processed.
     * 
     * @param request  the servlet request
     * @param response the servlet response
     * @param chain    the filter chain
     * @throws IOException      if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String requestId = extractOrGenerateRequestId(request);
            MDC.put(REQUEST_ID_MDC_KEY, requestId);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(REQUEST_ID_MDC_KEY);
        }
    }

    /**
     * Extracts the request ID from the HTTP header or generates a new UUID if not
     * present.
     * 
     * @param request the servlet request
     * @return the request ID
     */
    private String extractOrGenerateRequestId(ServletRequest request) {
        if (request instanceof HttpServletRequest httpRequest) {
            String requestId = httpRequest.getHeader(REQUEST_ID_HEADER);
            if (requestId != null && !requestId.isEmpty()) {
                return requestId;
            }
        }
        return UUID.randomUUID().toString();
    }
}
