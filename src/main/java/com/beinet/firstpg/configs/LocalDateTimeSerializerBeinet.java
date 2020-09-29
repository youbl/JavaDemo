package com.beinet.firstpg.configs;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTimeDeserializer 不支持 yyyy-MM-dd
 * 所以增加这个类，以支付这种格式
 */
public class LocalDateTimeSerializerBeinet extends LocalDateTimeDeserializer {

    public LocalDateTimeSerializerBeinet(DateTimeFormatter formatter) {
        super(formatter);
    }

    // 增加 yyyy-MM-dd 的支持
    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.hasTokenId(JsonTokenId.ID_STRING)) {
            String string = fixMonthAndDay(parser.getText().trim());
            if (!string.isEmpty()) {
                return LocalDateTime.parse(string, this._formatter);
            }
        }
        return super.deserialize(parser, context);
    }

    /**
     * 如果月份只有1位，或日期只有1位，补零
     *
     * @param ymd 年月日
     * @return 带时间的完整日期字符串
     */
    private String fixMonthAndDay(String ymd) {
        int monthIdx = ymd.indexOf('-');
        if (monthIdx < 0)
            return "";
        int dayIdx = ymd.indexOf('-', monthIdx + 1);
        if (dayIdx < 0)
            return "";

        String year = ymd.substring(0, monthIdx);
        if (year.length() != 4)
            return ""; // 不是yyyy-MM-dd返回不处理

        String month = ymd.substring(monthIdx + 1, dayIdx);
        if (month.length() < 1 || month.length() > 2)
            return ""; // 不是yyyy-MM-dd返回不处理
        if (month.length() < 2)
            month = "0" + month;

        String day;

        String otherStr = ymd.substring(dayIdx + 1);
        if (otherStr.length() <= 1) {
            day = otherStr;
            otherStr = "";
        } else if ('1' <= otherStr.charAt(1) && otherStr.charAt(1) <= '9') {
            day = otherStr.substring(0, 2);
            otherStr = otherStr.substring(2);
        } else {
            day = otherStr.substring(0, 1);
            otherStr = otherStr.substring(1);
        }

        if (day.length() < 1 || day.length() > 2)
            return ""; // 不是yyyy-MM-dd返回不处理
        if (day.length() < 2)
            day = "0" + day;

        if (otherStr.isEmpty())
            otherStr = " 00:00:00";
        return String.format("%s-%s-%s%s", year, month, day, otherStr);
    }
}

