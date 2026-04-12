package namespace.stedd.messaging.http;

/**
 * Код ответа на API-запрос.
 * @author Namespace Stedd
 */
public enum HttpResponseCode {

    OK(200),   // Запрос успешно обработан
    Created(201),   // Запрос успешно обработан
    BadRequest(400),   // Допущены ошибки в структуре запроса
    Forbidden(403),   // Операция с текущим идентификатором не может прервана
    NotFound(404),   // Данный способ оплаты не поддерживается или отключён
    NotAllowed(405),   // Данная функциональность не поддерживается устройством или отключена
    Conflict(409),   // Операция с текущим идентификатором уже существует
    InternalError(500);   // Внутренняя ошибка обработки запроса или ошибка конфигурации терминала

    private final int statusCode;   // Код статуса ответа

    /**
     * Создание кода ответа на API-запрос.
     * @author Namespace Stedd
     * @param statusCode код статуса ответа
     */
    HttpResponseCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Получение кода статуса ответа.
     * @author Namespace Stedd
     * @return код статуса ответа
     */
    public int getStatusCode() {
        return this.statusCode;
    }

}
