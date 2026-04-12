package namespace.stedd.messaging.rabbit;

import com.rabbitmq.client.AMQP;
import namespace.stedd.data.type.ExoString;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * конструктор заголовков Rabbit-сообщений.
 * @author Namespace Stedd
 */
public class HeaderBuilder {

    private final Map<String, Object> header;   // Карта заголовков

    /**
     * Создание конструктора заголовков.
     * @author Namespace Stedd
     */
    public HeaderBuilder() {
        this.header = new HashMap<>();
    }

    /**
     * Создание конструктора заголовков.
     * @author Namespace Stedd
     * @return конструктор заголовков Rabbit-сообщений
     */
    public static HeaderBuilder create() {
        return new HeaderBuilder();
    }

    /**
     * Добавление заголовка.
     * @author Namespace Stedd
     * @param key ключ заголовка
     * @param value значение заголовка
     * @return конструктор заголовков для последующей точечной адресации
     */
    public HeaderBuilder add(String key, Object value) {
        this.header.put(key, ExoString.parseString(value, null));
        return this;
    }

    /**
     * Добавление Временного заголовка.
     * @author Namespace Stedd
     * @return конструктор заголовков для последующей точечной адресации
     */
    public HeaderBuilder withTime() {
        return this.add("time", LocalDateTime.now());
    }

    /**
     * Конструирование заголовка.
     * @author Namespace Stedd
     * @return сконструированный заголовок
     */
    public AMQP.BasicProperties build() {
        return new AMQP.BasicProperties().builder().headers(this.header).build();
    }

}
