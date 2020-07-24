package com.beinet.firstpg.serializeTest;

import com.beinet.firstpg.BaseTest;
import com.beinet.firstpg.regexDemo.StrHelper;
import com.beinet.firstpg.serializeDemo.SerializeHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
//@RunWith(SpringRunner.class)
public class SerializeHelperTest extends BaseTest {

    @Test
    public void Test() throws Exception {
        SerializeDto dto = new SerializeDto();
        dto.setId(11);
        dto.setName("张三dd");
        dto.setBirthday(LocalDateTime.now().minusDays(30));
        dto.setMoney(123.45F);

        String json = SerializeHelper.serializeToStr(dto);
        SerializeDto dtoFromJson = SerializeHelper.deserialize(json, SerializeDto.class);

        Assert.assertEquals(dto.toString(), dtoFromJson.toString());
        // boolean aa = dto.equals(dtoFromJson);
        Assert.assertNotEquals(dto, dtoFromJson);
    }
}
