package namespace.stedd.messaging.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import namespace.stedd.data.type.ExoString;

/**
 * Обменник Rabbit.
 * @author Namespace Stedd
 */
public class Exchange {

    private final ExchangeType type;   // Тип обменника
    private final String name;   // Название обменника
    private final boolean durable;   // Долговечность
    private final boolean autoDelete;   // Авто-удаление

    /**
     * Создание обменника.
     * @author Namespace Stedd
     * @param type тип обменника
     * @param name название обменника
     * @param durable долговечность
     * @param autoDelete авто-удаление
     */
    public Exchange(ExchangeType type, String name, boolean durable, boolean autoDelete) {
        this.type = type;
        this.name = name;
        this.durable = durable;
        this.autoDelete = autoDelete;
    }

    /**
     * Создание обменника.
     * @author Namespace Stedd
     * @param type тип обменника
     * @param name название обменника
     */
    public static Exchange create(ExchangeType type, String name) {
        return new Exchange(type, name, false, false);
    }

    /**
     * Создание обменника.
     * @author Namespace Stedd
     * @param type тип обменника
     * @param name название обменника
     * @param durable долговечность
     */
    public static Exchange create(ExchangeType type, String name, boolean durable) {
        return new Exchange(type, name, durable, false);
    }

    /**
     * Создание обменника.
     * @author Namespace Stedd
     * @param type тип обменника
     * @param name название обменника
     * @param durable долговечность
     * @param autoDelete авто-удаление
     */
    public static Exchange create(ExchangeType type, String name, boolean durable, boolean autoDelete) {
        return new Exchange(type, name, durable, autoDelete);
    }

    /**
     * Получение типа обменника.
     * @author Namespace Stedd
     * @return тип обменника
     */
    public ExchangeType getType() {
        return this.type;
    }

    /**
     * Получение названия обменника.
     * @author Namespace Stedd
     * @return название обменник
     */
    public String getName() {
        return this.name;
    }

    /**
     * Объявление обменника.
     * @author Namespace Stedd
     * @param configuration настройка Rabbit-общения
     */
    public void declare(RabbitConfiguration configuration) {
        try {
            // noinspection resource
            Connection connection = configuration.getFactory().newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(this.name, this.type.name(), this.durable, this.autoDelete, null);
        }
        catch (Exception exception) {
            configuration.getOutputSystem().write(ExoString.toExceptionString(exception));   // TODO: , "При создании обменника Канала произошла ошибка!"));
        }
    }


}
