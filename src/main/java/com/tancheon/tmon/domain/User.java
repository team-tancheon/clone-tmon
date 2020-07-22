package com.tancheon.tmon.domain;

import javafx.scene.NodeBuilder;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Temporal(TemporalType.DATE)
    private Calendar signUpTime;
    @Temporal(TemporalType.DATE)
    private Calendar lastLoginTime;
    @Temporal(TemporalType.DATE)
    private Calendar passwordChangeTime;
    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean emailAuthorized;

}
