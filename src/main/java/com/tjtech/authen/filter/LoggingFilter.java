package com.tjtech.authen.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjtech.authen.service.LogActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class LoggingFilter implements Filter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LogActivityService logActivityService;

    private static final Set<String> IGNORED_PATH_PREFIXES = new HashSet<>();

    static {
        IGNORED_PATH_PREFIXES.add("/h2-console");  // Example path prefix to ignore
        IGNORED_PATH_PREFIXES.add("/api/health");  // Another example path prefix to ignore
        IGNORED_PATH_PREFIXES.add("/api/status");  // Additional example
        // Add more prefixes as needed
    }

    @Autowired
    private ObjectMapper objectMapper; // Jackson ObjectMapper for JSON conversion

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String method = httpRequest.getMethod();
        String url = httpRequest.getRequestURI();
        String headers = getHeadersAsJson(httpRequest);
        String ipAddress = request.getRemoteAddr();
        String user = httpRequest.getUserPrincipal() != null ? httpRequest.getUserPrincipal().getName() : "unknown";

        boolean shouldIgnore = IGNORED_PATH_PREFIXES.stream().anyMatch(url::startsWith);

        if (shouldIgnore) {
            // Skip logging for requests starting with any of the ignored prefixes
            chain.doFilter(request, response);
            return;
        }

        // Wrap the request and response to cache their content
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        // Proceed with the next filter in the chain
        chain.doFilter(wrappedRequest, wrappedResponse);

        // Log the request and response after processing
        logRequestDetails(wrappedRequest);
        logResponseDetails(wrappedResponse);

        // Copy the cached response body back to the original response output stream
        wrappedResponse.copyBodyToResponse();

        int status = httpResponse.getStatus();

        String requestBody = new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
        String responseBody = new String(wrappedResponse.getContentAsByteArray(), StandardCharsets.UTF_8);

        logActivityService.logActivity(method, url, headers, status, requestBody, responseBody, ipAddress, user);
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }

    private void logRequestDetails(ContentCachingRequestWrapper request) {
        log.info("Incoming Request:");
        log.info("Method: " + request.getMethod());
        log.info("URL: " + request.getRequestURL().toString());
        log.info("Headers: " + Collections.list(request.getHeaderNames()).stream()
                .map(headerName -> headerName + ": " + request.getHeader(headerName))
                .collect(Collectors.joining(", ")));
        String requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);

        Map<String, String[]> parameters = request.getParameterMap();
        StringBuilder logMessage = new StringBuilder();
        parameters.forEach((key, values) -> {
            if ("password".equalsIgnoreCase(key)) {
                logMessage.append(key).append("="); // Mask the password
                logMessage.append(passwordEncoder.encode(key));
            } else {
                logMessage.append(key).append("=").append(values[0]); // Log other parameters
            }
            logMessage.append("&");
        });
        log.info("Request Body: " + logMessage.toString());
    }

    private void logResponseDetails(ContentCachingResponseWrapper response) {
        log.info("Outgoing Response:");
        log.info("Status: " + response.getStatus());
        log.info("Headers: " + response.getHeaderNames().stream()
                .map(headerName -> headerName + ": " + response.getHeader(headerName))
                .collect(Collectors.joining(", ")));
        log.info("Response Body: " + new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
    }

    // Method to convert headers to JSON
    private String getHeadersAsJson(HttpServletRequest request) throws IOException {
        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headersMap.put(headerName, headerValue);
        }

        // Convert the map to a JSON string using ObjectMapper
        return objectMapper.writeValueAsString(headersMap);
    }
}
