package namespace.stedd.messaging.rabbit;

import com.rabbitmq.client.*;
import namespace.stedd.data.console.OutputSystem;
import namespace.stedd.data.type.ExoString;

import java.nio.charset.StandardCharsets;

/**
 * Настройка Rabbit-общения.
 * @author Namespace Stedd
 */
public class RabbitConfiguration {

    private final ConnectionFactory connectionFactory;   // Фабрика подключения
    private final OutputSystem outputSystem;   // Система вывода информации

    /**
     * Создание настройки Rabbit-общения.
     * @author Namespace Stedd
     * @param host адрес подключения
     * @param user пользователь
     * @param password пароль
     * @param outputSystem система вывода информации
     */
    public RabbitConfiguration(String host, String user, String password, OutputSystem outputSystem) {
        this.connectionFactory = getFactory(host, user, password);
        this.outputSystem = outputSystem;
    }

    /**
     * Создание настройки Rabbit-общения.
     * @author Namespace Stedd
     * @param host адрес подключения
     * @param user пользователь
     * @param password пароль
     * @param outputSystem система вывода информации
     */
    public static RabbitConfiguration create(String host, String user, String password, OutputSystem outputSystem) {
        return new RabbitConfiguration(host, user, password, outputSystem);
    }

    /**
     * Получение стандартной фабрики подключения.
     * @author Namespace Stedd
     * @return фабрика подключения
     */
    public static ConnectionFactory getDefaultFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setConnectionTimeout(3000);
        connectionFactory.setRequestedHeartbeat(30);
        return connectionFactory;
    }

    /**
     * Получение фабрики подключения.
     * @author Namespace Stedd
     * @param host адрес подключения
     * @param user пользователь
     * @param password пароль
     * @return фабрика подключения
     */
    public static ConnectionFactory getFactory(String host, String user, String password) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setUsername(user);
        connectionFactory.setPassword(password);
        connectionFactory.setConnectionTimeout(3000);
        connectionFactory.setRequestedHeartbeat(30);
        return connectionFactory;
    }

    /**
     * Получение фабрики подключения.
     * @author Namespace Stedd
     * @return фабрика подключения
     */
    public ConnectionFactory getFactory() {
        return this.connectionFactory;
    }

    /**
     * Получение системы вывода информации.
     * @author Namespace Stedd
     * @return система вывода информации
     */
    public OutputSystem getOutputSystem() {
        return this.outputSystem;
    }

    /**
     * Запуск работы обменника.
     * @author Namespace Stedd
     * @param exchangeControl пункт управления обменниками
     */
    public void start(ExchangeControl exchangeControl) {
        exchangeControl.getQueue().declare(this);
        exchangeControl.getExchange().declare(this);
        exchangeControl.getBinding().bind(this);
        this.outputSystem.write(exchangeControl.getOnStartMessage());
    }

    /**
     * Запуск работы всех указанных обменников.
     * @author Namespace Stedd
     * @param exchangeControls пункты управления обменниками
     */
    public void startAll(ExchangeControl... exchangeControls) {
        for (ExchangeControl exchangeControl : exchangeControls) {
            this.start(exchangeControl);
        }
    }

    /**
     * Запуск прослушивания очереди.
     * @author Namespace Stedd
     * @param exchangeControl пункт управления обменниками
     * @param deliverCallback интерфейс реакции на пришедшее сообщение
     */
    public void listen(ExchangeControl exchangeControl, DeliverCallback deliverCallback) {
        new Thread(() -> exchangeControl.getQueue().listen(this, deliverCallback)).start();
    }

    /**
     * Отправка сообщения в очередь.
     * @author Namespace Stedd
     * @param exchangeControl пункт управления обменниками
     * @param message отправляемое сообщение
     */
    public void send(ExchangeControl exchangeControl, String message) {
        this.send(exchangeControl, "", message, null);
    }

    /**
     * Отправка сообщения в очередь.
     * @author Namespace Stedd
     * @param exchangeControl пункт управления обменниками
     * @param routingKey ключ связывания очереди и обменника
     * @param message отправляемое сообщение
     */
    public void send(ExchangeControl exchangeControl, String routingKey, String message) {
        this.send(exchangeControl, routingKey, message, null);
    }

    /**
     * Отправка сообщения в очередь.
     * @author Namespace Stedd
     * @param exchangeControl пункт управления обменниками
     * @param message отправляемое сообщение
     * @param headerBuilder конструктор заголовков сообщений
     */
    public void send(ExchangeControl exchangeControl, String message, HeaderBuilder headerBuilder) {
        this.send(exchangeControl, "", message, headerBuilder);
    }

    /**
     * Отправка сообщения в очередь.
     * @author Namespace Stedd
     * @param exchangeControl пункт управления обменниками
     * @param routingKey ключ связывания очереди и обменника
     * @param message отправляемое сообщение
     * @param headerBuilder конструктор заголовков сообщений
     */
    public void send(ExchangeControl exchangeControl, String routingKey, String message, HeaderBuilder headerBuilder) {
        try (Connection connection = this.connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            AMQP.BasicProperties properties = headerBuilder != null ? headerBuilder.build() : null;
            String exchange = exchangeControl.getExchange().getName();
            channel.basicPublish(
                    exchange,
                    routingKey,
                    properties,
                    message.getBytes(StandardCharsets.UTF_8)
            );
            this.outputSystem.write("Сообщение \"" + message + "\" успешно отправлено!");
        }
        catch (Exception exception) {
            this.outputSystem.write(ExoString.toExceptionString(exception));   // TODO: , "При отправки сообщения в Обменник произошла ошибка!"));
        }
    }

}
