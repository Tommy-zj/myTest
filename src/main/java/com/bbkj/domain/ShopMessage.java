package com.bbkj.domain;

import com.bbkj.common.GetLocalTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/11/11 11:31
 */
@Setter
@Getter
public class ShopMessage {
    private String msg;
    private String ctime = GetLocalTime.localTime();
    private long buyer_id;//下单人id
    private long create_id; //创建时间
    private long timestamp = System.currentTimeMillis();
}
