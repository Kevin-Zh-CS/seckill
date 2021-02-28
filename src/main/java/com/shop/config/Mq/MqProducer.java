package com.shop.config.Mq;

import com.shop.dao.ItemStockDataObjMapper;
import com.shop.service.OrderService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Map;

public class MqProducer {
    private DefaultMQProducer producer;
    private TransactionMQProducer transactionMQProducer;
    @Value("${mq.nameserver.addr}")
    private String nameAddr;
    @Value("${mq.topicname}")
    private String topicName;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ItemStockDataObjMapper stockLogDOMapper;

    @PostConstruct
    public void init() throws MQClientException {
        //Producer初始化，Group没有意义，但是消费者有意义
        producer = new DefaultMQProducer("producer_group");
        producer.setNamesrvAddr(nameAddr);
        producer.start();
        transactionMQProducer = new TransactionMQProducer("transaction_producer_group");
        transactionMQProducer.setNamesrvAddr(nameAddr);
        transactionMQProducer.start();

//        transactionMQProducer.setTransactionListener(new TransactionListener() {
//            @Override
//            public LocalTransactionState executeLocalTransaction(Message message, Object args) {
//                //真正要做的事
//                Integer itemId = (Integer) ((Map) args).get("itemId");
//                Integer promoId = (Integer) ((Map) args).get("promoId");
//                Integer userId = (Integer) ((Map) args).get("userId");
//                Integer amount = (Integer) ((Map) args).get("amount");
//                String stockLogId = (String) ((Map) args).get("stockLogId");
//                try {
//                    orderService.createOrder(userId, itemId, promoId, amount, stockLogId);
//                } catch (BizException e) {
//                    e.printStackTrace();
//                    //如果发生异常，createOrder已经回滚，此时要回滚事务型消息。
//                    //设置stockLog为回滚状态
//                    StockLogDO stockLogDO = stockLogDOMapper.selectByPrimaryKey(stockLogId);
//                    stockLogDO.setStatus(3);
//                    stockLogDOMapper.updateByPrimaryKeySelective(stockLogDO);
//                    return LocalTransactionState.ROLLBACK_MESSAGE;
//                }
//                return LocalTransactionState.COMMIT_MESSAGE;
//            }
//
//            @Override
//            public LocalTransactionState checkLocalTransaction(MessageExt message) {
//                //根据是否扣减库存成功，来判断要返回COMMIT，ROLLBACK还是UNKNOWN
//                String jsonString = new String(message.getBody());
//                Map<String, Object> map = JSON.parseObject(jsonString, Map.class);
//                String stockLogId = (String) map.get("stockLogId");
//                StockLogDO stockLogDO = stockLogDOMapper.selectByPrimaryKey(stockLogId);
//                if (stockLogDO == null)
//                    return LocalTransactionState.UNKNOW;
//                //订单操作已经完成，等着异步扣减库存，那么就提交事务型消息
//                if (stockLogDO.getStatus() == 2) {
//                    return LocalTransactionState.COMMIT_MESSAGE;
//                    //订单操作还未完成，需要执行下单操作，那么就维持为prepare状态
//                } else if (stockLogDO.getStatus() == 1) {
//                    return LocalTransactionState.UNKNOW;
//                }
//                return LocalTransactionState.ROLLBACK_MESSAGE;
//            }
//        });
    }


}
