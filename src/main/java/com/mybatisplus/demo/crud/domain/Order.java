package com.mybatisplus.demo.crud.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

/**
 * @author Fangheng Sun on 2021/8/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_order")
public class Order {

    @TableField("id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @TableField("order_oid")
    private String orderOID;
}
