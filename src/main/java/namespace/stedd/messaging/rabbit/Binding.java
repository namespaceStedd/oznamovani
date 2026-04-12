package namespace.stedd.messaging.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import namespace.stedd.data.type.ExoString;

/**
 * Связь обменника с очередью Rabbit.
 * @author Namespace Stedd
 */
public class Binding {

    private final String queueName;   // Название очереди
    private final String exchangeName;   // Название обменника
    private final String routingKey;   // Ключ связывания очереди и обменника

    /**
     * Создание связи обменника с очередью.
     * @author Namespace Stedd
     * @param queueName название очереди
     * @param exchangeName название обменника
     * @param routingKey ключ связывания очереди и обменника
     */
    public Binding(String queueName, String exchangeName, String routingKey) {
        this.queueName = queueName;
        this.exchangeName = exchangeName;
        this.routingKey = ExoString.parseString(routingKey, "");
    }

    /**
     * Создание связи обменника с очередью.
     * @author Namespace Stedd
     * @param queueName название очереди
     * @param exchangeName название обменника
     * @param routingKey ключ связывания очереди и обменника
     */
    public static Binding create(String queueName, String exchangeName, String routingKey) {
        return new Binding(queueName, exchangeName, routingKey);
    }

    /**
     * Получение ключа связи очереди и обменника.
     * @author Namespace Stedd
     * @return ключ связи очереди и обменника
     */
    public String getRoutingKey() {
        return this.routingKey;
    }

    /**
     * Объявление обменника.
     * @author Namespace Stedd
     * @param configuration настройка Rabbit-общения
     */
    public void bind(RabbitConfiguration configuration) {
        try {
            // noinspection resource
            Connection connection = configuration.getFactory().newConnection();
            Channel channel = connection.createChannel();
            channel.queueBind(this.queueName, this.exchangeName, this.routingKey);
        }
        catch (Exception exception) {
            configuration.getOutputSystem().write(ExoString.toExceptionString(exception));   // TODO: , "При создании связи обменника с очередью Канала произошла ошибка!"));
        }
    }


}
