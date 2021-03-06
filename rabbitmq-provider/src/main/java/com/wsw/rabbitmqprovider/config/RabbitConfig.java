package com.wsw.rabbitmqprovider.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author WangSongWen
 * @Date: Created in 14:55 2020/7/13
 * @Description: 生产者推送消息的消息确认 回调函数的使用。可以在回调函数根据需求做对应的扩展或者业务数据处理
 */
//@Configuration
public class RabbitConfig {
    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        //消息是否发送到交换器的回调函数
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("ConfirmCallback: " + "相关数据：" + correlationData);
                System.out.println("ConfirmCallback: " + "确认情况：" + ack);
                System.out.println("ConfirmCallback: " + "原因：" + cause);
            }
        });

        //消息是否发送到队列的回调函数
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("ReturnCallback: " + "消息：" + message);
                System.out.println("ReturnCallback: " + "回应码：" + replyCode);
                System.out.println("ReturnCallback: " + "回应信息：" + replyText);
                System.out.println("ReturnCallback: " + "交换机：" + exchange);
                System.out.println("ReturnCallback: " + "路由键：" + routingKey);
            }
        });

        return rabbitTemplate;
    }
}
