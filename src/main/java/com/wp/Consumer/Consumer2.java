package com.wp.Consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @author: wp
 * @Title: Consumer2
 * @Description: TODO
 * @date 2019/3/26 15:31
 */
@Component
public class Consumer2 {

    @RabbitListener(queues = "queue01",containerFactory = "simple")
    public void queue01( Channel channel, Message message) throws Exception {
        try{
            System.out.println("[queue01]队列收到消息,msg:"+new String(message.getBody(),"utf-8"));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false );
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("[queue01] 队列消息接收失败,丢弃");
            channel.basicNack( message.getMessageProperties().getDeliveryTag(),false,false );
        }


    }

    @RabbitListener(queues = "queue02",containerFactory = "simple")
    public void queue02( Channel channel, Message message) throws Exception {
        try{
            System.out.println("[queue02]队列收到消息,msg:"+new String(message.getBody(),"utf-8"));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false );
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("[queue02] 队列消息接收失败,丢弃");
            channel.basicNack( message.getMessageProperties().getDeliveryTag(),false,false );
        }
    }

    @RabbitListener(queues = "queue03",containerFactory = "simple")
    public void queue03( Channel channel, Message message) throws Exception {
        try{
            System.out.println("[queue03]队列收到消息,msg:"+new String(message.getBody(),"utf-8"));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false );
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("[queue03] 队列消息接收失败,丢弃");
            channel.basicNack( message.getMessageProperties().getDeliveryTag(),false,false );
        }
    }
}
