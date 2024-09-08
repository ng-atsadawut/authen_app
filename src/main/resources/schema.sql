CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    countLogin INT DEFAULT 0,
    lastLoginSuccess TIMESTAMP,
    role VARCHAR(20) DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE log_activity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    method VARCHAR(10) NOT NULL,              -- HTTP method (GET, POST, PUT, DELETE, etc.)
    url VARCHAR(255) NOT NULL,                -- The URL of the API request
    headers TEXT,                            -- Headers of the request
    status INT NOT NULL,                     -- HTTP status code
    request_body TEXT,                       -- Request body in JSON format or as a string
    response_body TEXT,                      -- Response body in JSON format or as a string
    ip_address VARCHAR(45),                  -- IP address of the requester (supports IPv4 and IPv6)
    username VARCHAR(255),                       -- User ID or username who made the request
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of when the log entry was created
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Timestamp of last update
);
