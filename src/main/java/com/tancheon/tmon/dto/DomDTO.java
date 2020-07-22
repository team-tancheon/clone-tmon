package com.tancheon.tmon.dto;

import com.tancheon.tmon.domain.Dom;
import com.tancheon.tmon.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DomDTO {
    private long test;
//    private String test;
    private int num;

    public Dom toEntity(){
        return Dom.builder()
                .test(test)
                .num(num)
                .build();
    }

}
