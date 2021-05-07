package com.jay.CWOrderService.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Washer {

    private int washerId;
    private String name;
    private List<String> address;

}
