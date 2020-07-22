package com.tancheon.tmon.domain;

import javafx.beans.property.adapter.JavaBeanBooleanPropertyBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private String test;
    private long test;
    private Integer num;


}
