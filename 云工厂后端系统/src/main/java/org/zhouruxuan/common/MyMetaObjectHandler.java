package org.zhouruxuan.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 属性自动注入的配置类
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("gmtCreate", new Date(), metaObject);
        this.setFieldValByName("gmtModified", new Date(), metaObject);
        this.setFieldValByName("productNo","PNO"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),metaObject);
        this.setFieldValByName("deviceNo","DNO"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),metaObject);
        this.setFieldValByName("orderNo","ONO"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified", new Date(), metaObject);

    }
}