package namespace.stedd.messaging.rabbit;

/**
 * Перечисление типов Rabbit-обменников.
 * @author Namespace Stedd
 */
public enum ExchangeType {
    direct,   // Прямой обменник конечному пользователю
    topic,   // Тематическая рассылка по ключу / шаблону
    headers,   // Рассылка на основе атрибутов заголовка
    fanout,   // Веерная рассылка всем очередям
    ;
}
