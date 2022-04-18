package org.zhouruxuan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.zhouruxuan.entity.Factory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zhouruxuan.mapper.FactoryMapper;

/**

 *  服务类

 *
 * @author zhouruxuan
 * @since 2021-07-13
 */
public interface FactoryService extends IService<Factory> {


    Boolean existFactoryName(String factoryName);
}
