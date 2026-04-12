package namespace.stedd.messaging.rabbit;

/**
 * Пункт управления обменниками.
 * @author Namespace Stedd
 */
public interface ExchangeControl {
    Queue getQueue();   // Получение названия очереди
    Exchange getExchange();   // Получение названия обменника
    Binding getBinding();   // Получение ключа связи очереди и обменника
    String getOnStartMessage();   // Получение сообщения при запуске обменника в работу
}
