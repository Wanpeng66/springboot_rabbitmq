package com.wp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootRabbitmqApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @Test
    public void contextLoads() {
            String[] array = new String[]{"hello world"};
           rabbitTemplate.setMessageConverter( new Jackson2JsonMessageConverter(  ) );
            setCallback(rabbitTemplate);

            for(int i=0;i<10;i++){
                MessageProperties messageProperties = new MessageProperties();
                messageProperties.setContentEncoding( "utf-8" );
                messageProperties.setDeliveryMode( MessageDeliveryMode.NON_PERSISTENT );
                Message message = new Message( "hello world".getBytes(),messageProperties );
                CorrelationData correlationData = new CorrelationData( UUID.randomUUID().toString());
                rabbitTemplate.convertAndSend("springboot_rabbitmq_queue_01",message,correlationData);
            }

        }

    private void setCallback(RabbitTemplate rabbitTemplate) {
        rabbitTemplate.setConfirmCallback( new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm( CorrelationData correlationData, boolean ack, String cause ) {
                //correlationData是生产者发送消息时一并传递的标识
               // System.out.println("消息id:" + correlationData.getId());
                if (ack) {
                    System.out.println("消息id:" + correlationData.getId()+"消息发送到服务器成功");
                } else {
                    System.out.println("消息id:" + correlationData.getId()+"消息发送到服务器失败:" + cause);
                }

            }
        } );
        rabbitTemplate.setReturnCallback( new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage( Message message, int replyCode, String replyText, String exchange, String routingKey ) {
                try {
                    System.out.println("return--message:" + new String(message.getBody(),"utf-8") + ",replyCode:" + replyCode
                            + ",replyText:" + replyText + ",exchange:" + exchange + ",routingKey:" + routingKey);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        } );
    }

    @Test
    public void test_recieve(){
        Object msg = rabbitTemplate.receiveAndConvert( "springboot_rabbitmq_queue_01" );
        System.out.println(msg.getClass());
        System.out.println(msg.toString());

    }

    @Test
    public void creat(){
        amqpAdmin.declareQueue( new Queue( "springboot_rabbitmq_queue_01" ) );
       // amqpAdmin.declareExchange( new DirectExchange( "test_direct_exchange" ) );
        //amqpAdmin.declareBinding( new Binding( "test",Binding.DestinationType.QUEUE,"test_direct_exchange","test",null ) );

    }

    @Test
    public void testDirect(){
        setCallback(rabbitTemplate);
        CorrelationData correlationData = new CorrelationData( UUID.randomUUID().toString() );
        rabbitTemplate.convertAndSend( "direct_exchange","queue01","direct",correlationData );
    }

    @Test
    public void testFanout(){
        setCallback(rabbitTemplate);
        CorrelationData correlationData = new CorrelationData( UUID.randomUUID().toString() );
        rabbitTemplate.convertAndSend( "fanout_exchange","","fanout",correlationData );
    }

    @Test
    public void testTopic() throws InterruptedException {
        //TimeUnit.SECONDS.sleep( 2 );
        setCallback(rabbitTemplate);
        CorrelationData correlationData = new CorrelationData( UUID.randomUUID().toString() );
        rabbitTemplate.convertAndSend( "topic_exchange","queue.01.0244","topic",correlationData );
    }


}
