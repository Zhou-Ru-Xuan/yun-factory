package org.zhouruxuan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**

 *
 * @author zhouruxuan
 * @since 2021-07-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OrderItem对象", description="")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    @TableField(fill = FieldFill.INSERT)
    private String orderNo;

    private String productName;

    private Integer quantity;

    private String winner;

    private Date endTime;

    private Date bidEndTime;

    @ApiModelProperty(value = "1-正常2-已发布3-投标结束 4-已排产 5-已完工 6-已发货 7-已收货 8-已完成")
    private Integer orderState;

    private String belong;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")@TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")@TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")@TableField(fill = FieldFill.INSERT_UPDATE)private Date gmtModified;


}
