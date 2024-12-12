package com.neyogoo.example.admin.test2.dao;

import com.neyogoo.example.admin.test2.model.TOrder;
import com.neyogoo.example.common.database.mvc.dao.SuperMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TOrderMapper extends SuperMapper<TOrder> {

    List<Map<String, Object>> listOrder();
}
