package namespace.stedd.messaging.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import namespace.stedd.data.type.ExoString;

/**
 * Очередь Rabbit.
 * @author Namespace Stedd
 */
public class Queue {

    private final String name;   // Название очереди
    private final boolean durable;   // Долговечность
    private final boolean exclusive;   // Привилегированность
    private final boolean autoDelete;   // Авто-удаление

    /**
     * Создание Rabbit-очереди.
     * @author Namespace Stedd
     * @param name название очереди
     * @param durable долговечность
     * @param exclusive привилегированность
     * @param autoDelete авто-удаление
     */
    public Queue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
        this.name = name;
        this.durable = durable;
        this.exclusive = exclusive;
        this.autoDelete = autoDelete;
    }

    /**
     * Создание Rabbit-очереди.
     * @author Namespace Stedd
     * @param name название очереди
     * @param durable долговечность
     * @param exclusive привилегированность
     * @param autoDelete авто-удаление
     */
    public static Queue create(String name, boolean durable, boolean exclusive, boolean autoDelete) {
        return new Queue(name, durable, exclusive, autoDelete);
    }

    /**
     * Создание Rabbit-очереди.
     * @author Namespace Stedd
     * @param name название очереди
     */
    public static Queue create(String name) {
        return new Queue(name, true, false, true);
    }

    /**
     * Получение названия очереди.
     * @author Namespace Stedd
     * @return название очереди
     */
    public String getName() {
        return this.name;
    }

    /**
     * Объявление очереди.
     * @author Namespace Stedd
     * @param configuration настройка Rabbit-общения
     */
    public void declare(RabbitConfiguration configuration) {
        try {
            // noinspection resource
            Connection connection = configuration.getFactory().newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(this.name, this.durable, this.exclusive, this.autoDelete, null);
        }
        catch (Exception exception) {
            configuration.getOutputSystem().write(ExoString.toExceptionString(exception));   // TODO: , "При создании очереди Канала произошла ошибка!"));
        }
    }

    /**
     * Объявление прослушивателя очереди.
     * @author Namespace Stedd
     * @param configuration настройка Rabbit-общения
     * @param deliverCallback интерфейс реакции на пришедшее сообщение
     */
    public void listen(RabbitConfiguration configuration, DeliverCallback deliverCallback) {
        configuration.getOutputSystem().write("Очередь " + this.name + " теперь прослушивается...");
        this.declare(configuration);
        try {
            // noinspection resource
            Connection connection = configuration.getFactory().newConnection();
            Channel channel = connection.createChannel();
            channel.basicConsume(this.name, true, deliverCallback, consumerTag -> {
            });
        }
        catch (Exception exception) {
            configuration.getOutputSystem().write(ExoString.toExceptionString(exception));   // TODO: , "При создании очереди Канала произошла ошибка!"));
        }
    }

}
