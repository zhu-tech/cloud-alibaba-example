package com.neyogoo.example.admin.test2.controller;

import com.baidu.fsg.uid.UidGenerator;
import com.neyogoo.example.admin.test2.dao.TDictMapper;
import com.neyogoo.example.admin.test2.dao.TOrderItemMapper;
import com.neyogoo.example.admin.test2.dao.TOrderMapper;
import com.neyogoo.example.admin.test2.dao.TUserMapper;
import com.neyogoo.example.admin.test2.model.TDict;
import com.neyogoo.example.admin.test2.model.TOrder;
import com.neyogoo.example.admin.test2.model.TOrderItem;
import com.neyogoo.example.admin.test2.model.TUser;
import com.neyogoo.example.common.core.model.R;
import io.seata.core.context.RootContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/test2")
public class Test2Controller {

    @Autowired
    private UidGenerator uidGenerator;
    @Autowired
    private TUserMapper userMapper;
    @Autowired
    private TOrderMapper orderMapper;
    @Autowired
    private TOrderItemMapper orderItemMapper;
    @Autowired
    private TDictMapper dictMapper;

    @GetMapping("/anno/add/user")
    public void addUser(@RequestParam("userId") Long userId) {
        System.out.println("请求 add user");
        TUser user = new TUser();
        user.setId(userId);
        user.setName("用户" + userId);
        userMapper.insert(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/anno/list/user")
    public R<List<TUser>> listUser() {
        System.out.println("请求 list user");
        return R.success(userMapper.selectList(null));
    }

    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/anno/add/order")
    public void addOrder(@RequestParam("userId") Long userId,
                         @RequestParam("orderId") Long orderId) {
        System.out.println("请求 add order");
        System.out.println("XID = " + RootContext.getXID());
        TOrder order = new TOrder();
        order.setOrderId(orderId);
        order.setUserId(userId);
        orderMapper.insert(order);

        TOrderItem orderItem = new TOrderItem();
        orderItem.setId(uidGenerator.getUid());
        orderItem.setOrderId(orderId);
        orderItem.setProductId(uidGenerator.getUid());
        orderItem.setUserId(userId);
        orderItemMapper.insert(orderItem);
    }

    @GetMapping("/anno/list/order")
    public R<List<Map<String, Object>>> listOrder() {
        System.out.println("请求 list order");
        return R.success(orderMapper.listOrder());
    }

    @GetMapping("/anno/add/dict")
    public void addDict(@RequestParam("dictCode") String dictCode,
                        @RequestParam("dictName") String dictName) {
        System.out.println("请求 add dict");
        TDict dict = new TDict();
        dict.setId(uidGenerator.getUid());
        dict.setDictCode(dictCode);
        dict.setDictName(dictName);
        dictMapper.insert(dict);
    }

    @GetMapping("/anno/list/dict")
    public R<List<TDict>> listDict() {
        System.out.println("请求 list dict");
        return R.success(dictMapper.selectList(null));
    }
}
