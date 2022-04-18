package org.zhouruxuan;

import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class Factory05ApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
    }

}
