package com.wp.config;

import com.rabbitmq.client.Channel;
import com.wp.ListenerAdpter.messageHandle;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.ErrorHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: wp
 * @Title: RabbitmqConfig
 * @Description: TODO rabbitmq配置类
 * @date 2019/3/20 22:10
 */
@EnableRabbit
@Configuration
public class RabbitmqConfig {

    @Autowired
    CachingConnectionFactory cachingConnectionFactory;

    /*@Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter(  );
    }*/

    @Bean
    public RabbitAdmin rabbitAdmin(){
       RabbitAdmin rabbitAdmin = new RabbitAdmin( cachingConnectionFactory );
       //rabbitAdmin.setAutoStartup( true );
       return rabbitAdmin;
    }

    @Bean
    /** 因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调类为最后一次设置 */
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(cachingConnectionFactory);
        //设置mandatory=true,则如果消息从exchange到queue失败，会触发template的returnCallback方法，将消息退回
        template.setMandatory(true);
        return template;
    }

    @Bean("simple")
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(){
        SimpleRabbitListenerContainerFactory simple = new SimpleRabbitListenerContainerFactory();
        simple.setConnectionFactory( cachingConnectionFactory );
        simple.setConcurrentConsumers( 3 );
        simple.setMaxConcurrentConsumers( 10 );
        simple.setAcknowledgeMode( AcknowledgeMode.MANUAL );
        simple.setPrefetchCount( 1 );
        simple.setErrorHandler( new ConditionalRejectingErrorHandler( new FatalExceptionStrategy() {
            @Override
            public boolean isFatal( Throwable t ) {
                return false;
            }
        } ) );

        return simple;
    }

    @Bean
    public Queue queue01(){
        return new Queue( "queue01",true );
    }

    @Bean
    public Queue queue02(){
        return new Queue( "queue02",true );
    }

    @Bean
    public Queue queue3(){
        return new Queue( "queue03",true );
    }

    @Bean
    public Queue deadQueue(){
        Map<String,Object> args = new HashMap<>(  );
        args.put( "x-dead-letter-exchange","topic_exchange" );
        args.put( "x-dead-letter-routing-key","queue.01.02" );
        return new Queue( "deadQueue",true,false,false,args );
    }

    @Bean
    public Exchange direct(){
        return new DirectExchange( "direct_exchange",true,false );
    }

    @Bean
    public Exchange fanout(){
        return new DirectExchange( "fanout_exchange",true,false );
    }

    @Bean
    public Exchange topic(){
        return new TopicExchange( "topic_exchange",true,false );
    }

    @Bean
    public Binding binding01(){
        return new Binding( "queue01",Binding.DestinationType.QUEUE,"direct_exchange","queue01",null );
    }

    @Bean
    public Binding deadBinding(){
        return new Binding( "deadQueue",Binding.DestinationType.QUEUE,"direct_exchange","deadQueue",null );
    }

    @Bean
    public Binding binding02(){
        return new Binding( "queue01",Binding.DestinationType.QUEUE,"fanout_exchange","",null );
    }

    @Bean
    public Binding binding03(){
        return new Binding( "queue01",Binding.DestinationType.QUEUE,"topic_exchange","queue.01",null);
    }

    @Bean
    public Binding binding04(){
        return new Binding( "queue02",Binding.DestinationType.QUEUE,"fanout_exchange","",null );
    }

    @Bean
    public Binding binding05(){
        return new Binding( "queue02",Binding.DestinationType.QUEUE,"topic_exchange","queue.*",null );
    }

    @Bean
    public Binding binding06(){
        return new Binding( "queue02",Binding.DestinationType.QUEUE,"topic_exchange","queue.01.02",null);
    }

    @Bean
    public Binding binding07(){
        return new Binding( "queue03",Binding.DestinationType.QUEUE,"topic_exchange","queue.#",null );
    }



    //@Bean(name="simpleML")
   // @Primary
    public SimpleMessageListenerContainer simpleMessageListenerContainer(){
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer( cachingConnectionFactory );
        simpleMessageListenerContainer.setAcknowledgeMode( AcknowledgeMode.MANUAL );
        simpleMessageListenerContainer.setQueueNames( "springboot_rabbitmq_queue_01" );
        simpleMessageListenerContainer.setConnectionFactory( cachingConnectionFactory );
        simpleMessageListenerContainer.setConcurrentConsumers( 2 );
        simpleMessageListenerContainer.setMaxConcurrentConsumers( 10 );
        simpleMessageListenerContainer.setExposeListenerChannel( true );
        simpleMessageListenerContainer.setErrorHandler( new ErrorHandler() {
            @Override
            public void handleError( Throwable throwable ) {
                System.out.println("handleError...."+throwable);
            }
        } );

        simpleMessageListenerContainer.setMessageListener( new ChannelAwareMessageListener() {
            @Override
            public void onMessage( Message message, Channel channel ) throws Exception {
                try {
                    System.out.println(
                            "消费端接收到消息:" + message.getMessageProperties() + ":" + new String(message.getBody()));
                    System.out.println("routingKey:"+message.getMessageProperties().getReceivedRoutingKey()+"" +
                            ";deliveryTag:"+message.getMessageProperties().getDeliveryTag());

                    //中断1秒 模拟处理消息
                    //TimeUnit.SECONDS.sleep( 1 );
                    // deliveryTag是消息传送的次数，我这里是为了让消息队列的第一个消息到达的时候抛出异常，
                    // 处理异常让消息重新回到队列，然后再次抛出异常，处理异常拒绝让消息重回队列
					/*if (message.getMessageProperties().getDeliveryTag() == 1
							|| message.getMessageProperties().getDeliveryTag() == 2) {
						throw new Exception();
					}*/
                    // false只确认当前一个消息收到，true确认所有consumer获得的消息
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                } catch (Exception e) {
                   // e.printStackTrace();

                    if (message.getMessageProperties().getRedelivered()) {
                        System.out.println("消息已重复处理失败,拒绝再次接收...");
                        // 拒绝消息
                        channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
                    } else {
                        System.out.println("消息即将再次返回队列处理...");
                        // 第三个参数 requeue为是否重新回到队列
                        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                    }
                }

            }
        } );

        //还可以给消息容器设置listeneradpter 去适配不同的消息
       /* MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter( new messageHandle() );
        messageListenerAdapter.setDefaultListenerMethod( "handleMessage" );
        Map<String,String> queueOrTagToMethod = new HashMap<>(  );
        queueOrTagToMethod.put( "springboot_rabbitmq_queue_01","add" );
        messageListenerAdapter.setQueueOrTagToMethodName( queueOrTagToMethod );
        simpleMessageListenerContainer.setMessageListener( messageListenerAdapter );*/


        return simpleMessageListenerContainer;
    }




}
