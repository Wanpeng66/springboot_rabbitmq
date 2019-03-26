package com.wp.Consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: wp
 * @Title: consumer1
 * @Description: TODO
 * @date 2019/3/20 22:34
 */
@Component
public class consumer1 {

   @RabbitListener(queues = "springboot_rabbitmq_queue_01",containerFactory = "simple")
    public void receive1( String map, Channel channel, Message message) throws IOException {
        try {
            System.out.println("[1]收到消息: msg->"+map);
            try {
                TimeUnit.SECONDS.sleep( 1 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了
            // 否则消息服务器以为这条消息没处理掉 后续还会在发
            System.out.println("[1] receiver success");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

        } catch (IOException e) {
            e.printStackTrace();
            //第三个从参数为true，将这条消息重新放入queue,否则丢弃这个消息
            System.out.println("[1] receiver fail");
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,true);

        }

    }

   //@RabbitListener(queues = "springboot_rabbitmq_queue_01",containerFactory = "simple")
    public void receive2(String[] map, Channel channel, Message message) throws IOException {
        try {
            System.out.println("[2]收到消息: msg->"+map);
            try {
                TimeUnit.SECONDS.sleep( 2 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了
            // 否则消息服务器以为这条消息没处理掉 后续还会在发
            System.out.println("[2] receiver success");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

        } catch (Exception e) {
            e.printStackTrace();
            //第三个从参数为true，将这条消息重新放入queue,否则丢弃这个消息
            System.out.println("[2] receiver fail");
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,true);

        }
    }

}