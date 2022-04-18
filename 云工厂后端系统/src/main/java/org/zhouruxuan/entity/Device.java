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
@ApiModel(value = "Device对象", description = "")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String deviceName;

    private String norms;

    private String des;

    @ApiModelProperty(value = "0-已关闭，1-生产中，2-闲置中")
    private Integer deviceState;

    @ApiModelProperty(value = "0-工厂设备，1-已被租用，2-未被租用")
    private Integer hireState;

    @ApiModelProperty(value = "0-自有设备，1-租用设备")
    private Integer deviceSource;

    private String belong;
    @TableField(fill = FieldFill.INSERT)
    private String deviceNo;

    private String typeName;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
