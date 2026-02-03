package com.zerti.utilities.log.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KpiValues {

    private String service;
    private String flow;
    private String domain;
    private String step;
    private String dependency;
}
