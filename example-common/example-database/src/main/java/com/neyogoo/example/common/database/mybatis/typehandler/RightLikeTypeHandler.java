package com.neyogoo.example.common.database.mybatis.typehandler;

import com.baomidou.mybatisplus.core.enums.SqlLike;
import org.apache.ibatis.type.Alias;

/**
 * 仅仅用于like查询
 */
@Alias("rightLike")
public class RightLikeTypeHandler extends BaseLikeTypeHandler {

    public RightLikeTypeHandler() {
        super(SqlLike.RIGHT);
    }
}

