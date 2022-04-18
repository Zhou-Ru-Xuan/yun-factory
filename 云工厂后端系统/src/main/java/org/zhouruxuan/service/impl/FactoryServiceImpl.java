package org.zhouruxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.zhouruxuan.entity.Factory;
import org.zhouruxuan.entity.User;
import org.zhouruxuan.mapper.FactoryMapper;
import org.zhouruxuan.service.FactoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**

 *
 * @author zhouruxuan
 * @since 2021-07-13
 */
@Service
public class FactoryServiceImpl extends ServiceImpl<FactoryMapper, Factory> implements FactoryService {

    @Override
    public Boolean existFactoryName(String factoryName) {
        QueryWrapper<Factory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("factoryName", factoryName);
        Integer cnt = baseMapper.selectCount(queryWrapper);
        return cnt>0;
    }
}
