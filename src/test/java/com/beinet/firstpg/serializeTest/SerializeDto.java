package com.beinet.firstpg.serializeTest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class SerializeDto {
    private long id;
    private String name;
    //@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime birthday;
    private float money;

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s",
                id,
                name,
                birthday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                money);
    }
}
