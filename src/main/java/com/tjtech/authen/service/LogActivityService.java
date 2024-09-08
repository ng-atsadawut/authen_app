package com.tjtech.authen.service;

import com.tjtech.authen.entity.LogActivity;
import com.tjtech.authen.repository.LogActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;

import static java.time.LocalDateTime.now;

@Service
public class LogActivityService {

    @Autowired
    private LogActivityRepository logActivityRepository;

    public void logActivity(String method, String url, String headers, Integer status,
                            String requestBody, String responseBody, String ipAddress, String user) {
        LogActivity logActivity = new LogActivity();
        logActivity.setMethod(method);
        logActivity.setUrl(url);
        logActivity.setHeaders(headers);
        logActivity.setStatus(status);
        logActivity.setRequestBody(requestBody);
        logActivity.setResponseBody(responseBody);
        logActivity.setIpAddress(ipAddress);
        logActivity.setUsername(user);
        logActivity.setCreateDate(now());
        logActivity.setUpdateDate(now());

        logActivityRepository.save(logActivity);
    }
}
