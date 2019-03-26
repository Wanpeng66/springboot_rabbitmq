package com.wp.ListenerAdpter;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.io.IOException;

/**
 * @author: wp
 * @Title: messageHandle
 * @Description: TODO
 * @date 2019/3/26 10:26
 */
public class messageHandle {

    public void add( Message message, Channel channel){
        try {
            System.out.println(
                    "消费端接收到消息:" + message.getMessageProperties() + ":" + new String(message.getBody()));
            System.out.println("routingKey:"+message.getMessageProperties().getReceivedRoutingKey()+"" +
                    ";deliveryTag:"+message.getMessageProperties().getDeliveryTag());
            // false只确认当前一个消息收到，true确认所有consumer获得的消息

                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            try {
                if (message.getMessageProperties().getRedelivered()) {
                    System.out.println("消息已重复处理失败,拒绝再次接收...");
                    // 拒绝消息

                        channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);

                } else {
                    System.out.println("消息即将再次返回队列处理...");
                    // 第三个参数 requeue为是否重新回到队列
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
