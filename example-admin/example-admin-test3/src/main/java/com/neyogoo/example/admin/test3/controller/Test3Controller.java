package com.neyogoo.example.admin.test3.controller;

import com.baidu.fsg.uid.UidGenerator;
import com.neyogoo.example.admin.test3.dao.TDictMapper;
import com.neyogoo.example.admin.test3.dao.TOrderItemMapper;
import com.neyogoo.example.admin.test3.dao.TOrderMapper;
import com.neyogoo.example.admin.test3.dao.TUserMapper;
import com.neyogoo.example.admin.test3.model.TDict;
import com.neyogoo.example.admin.test3.model.TOrder;
import com.neyogoo.example.admin.test3.model.TOrderItem;
import com.neyogoo.example.admin.test3.model.TUser;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.exception.SilenceException;
import com.neyogoo.example.common.core.model.R;
import com.neyogoo.example.common.core.util.JsonUtils;
import com.neyogoo.example.common.database.mybatis.conditions.Wraps;
import io.seata.core.context.RootContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/test3")
public class Test3Controller {

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

    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/anno/test/commit")
    public R<Boolean> testCommit() {
        System.out.println("请求 test commit");
        System.out.println("XID = " + RootContext.getXID());
        return R.success(true);
    }

    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/anno/test/rollback")
    public R<Boolean> testRollback() {
        System.out.println("请求 test rollback");
        System.out.println("XID = " + RootContext.getXID());
        throw SilenceException.wrap("rollback exception");
    }

    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/anno/add/user")
    public R<Boolean> addUser(@RequestParam("userId") Long userId) {
        System.out.println("请求 add user");
        System.out.println("XID = " + RootContext.getXID());
        TUser user = new TUser();
        user.setId(userId);
        user.setName("from test3");
        userMapper.insert(user);
        throw SilenceException.wrap("rollback exception");
        //return R.success(true);
    }

    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/anno/list/user")
    public R<List<TUser>> listUser() {
        System.out.println("请求 list user");
        return R.success(userMapper.selectList(null));
    }

    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/anno/add/order")
    public R<Boolean> addOrder(@RequestParam("userId") Long userId,
                               @RequestParam("orderId") Long orderId) {
        System.out.println(JsonUtils.toJson(ContextUtils.getLocalMap()));
        if (StringUtils.isNotBlank(ContextUtils.getXTransactionId())) {
            RootContext.bind(ContextUtils.getXTransactionId());
        }
        System.out.println("请求 add order");
        System.out.println("XID = " + RootContext.getXID());
        TOrder order = new TOrder();
        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setCreateDate(LocalDateTime.now());
        orderMapper.insert(order);

        TOrderItem orderItem = new TOrderItem();
        orderItem.setId(uidGenerator.getUid());
        orderItem.setOrderId(orderId);
        orderItem.setProductId(uidGenerator.getUid());
        orderItem.setUserId(userId);
        orderItemMapper.insert(orderItem);

        throw new RuntimeException("aaa");
    }

    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/anno/add/order/new")
    public R<Boolean> addOrderNew() {
        System.out.println("请求 add order new");
        TOrder order = new TOrder();
        order.setOrderId(uidGenerator.getUid());
        order.setUserId(uidGenerator.getUid());
        order.setCreateDate(LocalDateTime.now());
        orderMapper.insert(order);
        return R.success(true);
    }

    @GetMapping("/anno/list/order/oderId")
    public R<List<TOrder>> listOrderByOrderId(@RequestParam(value = "oderId") Long oderId) {
        System.out.println("请求 list order");
        return R.success(orderMapper.selectList(Wraps.<TOrder>lbQ()
                .eq(TOrder::getOrderId, oderId)));
    }

    @GetMapping("/anno/list/order/date")
    public R<List<TOrder>> listOrderByDate(@RequestParam(value = "createDate", required = false) LocalDate createDate) {
        System.out.println("请求 list order");
        return R.success(orderMapper.selectList(Wraps.<TOrder>lbQ()
                .between(TOrder::getCreateDate, createDate.atTime(LocalTime.MIN), createDate.atTime(LocalTime.MAX))));
    }

    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/anno/add/dict")
    public R<Boolean> addDict(@RequestParam("dictCode") String dictCode,
                              @RequestParam("dictName") String dictName) {
        System.out.println("请求 add dict");
        System.out.println("XID = " + RootContext.getXID());
        TDict dict = new TDict();
        dict.setId(uidGenerator.getUid());
        dict.setDictCode(dictCode);
        dict.setDictName(dictName);
        dictMapper.insert(dict);
        return R.success(true);
    }

    @GetMapping("/anno/list/dict")
    public R<List<TDict>> listDict() {
        System.out.println("请求 list dict");
        return R.success(dictMapper.selectList(null));
    }

    @GetMapping("/id")
    public R<Long> id() {
        long uid = uidGenerator.getUid();
        System.out.println(uid);
        return R.success(uid);
    }
}
