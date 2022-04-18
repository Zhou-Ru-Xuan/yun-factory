package org.zhouruxuan.service.impl;

import org.zhouruxuan.entity.Product;
import org.zhouruxuan.mapper.ProductMapper;
import org.zhouruxuan.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**

 *
 * @author zhouruxuan
 * @since 2021-07-13
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
