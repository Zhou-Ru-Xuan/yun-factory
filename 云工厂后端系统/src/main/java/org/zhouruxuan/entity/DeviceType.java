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
@ApiModel(value="DeviceType对象", description="")
public class DeviceType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String typeName;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")@TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")@TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")@TableField(fill = FieldFill.INSERT_UPDATE)private Date gmtModified;


}
