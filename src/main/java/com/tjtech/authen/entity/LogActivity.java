package com.tjtech.authen.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "log_activity")
public class LogActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String url;

    @Lob
    private String headers;

    @Column(nullable = false)
    private Integer status;

    @Lob
    private String requestBody;

    @Lob
    private String responseBody;

    @Column(name = "ip_address")
    private String ipAddress;

    private String username;

    @Column(name = "create_date", updatable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Column(name = "update_date")
    @LastModifiedDate
    private LocalDateTime updateDate;

}
