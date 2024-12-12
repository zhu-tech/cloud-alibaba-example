package com.neyogoo.example.admin.test3.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.neyogoo.example.common.database.model.IdEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@TableName("t_dict")
public class TDict extends IdEntity {

    private String dictCode;

    private String dictName;
}
