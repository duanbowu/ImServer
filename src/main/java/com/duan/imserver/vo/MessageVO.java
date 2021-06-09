package com.duan.imserver.vo;

import lombok.Data;

@Data
public class MessageVO {
    private char type;
    private String from;
    private String to;
    private String content;
    private String time;
}
