package namespace.stedd.messaging.http;

import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;

/**
 * Интерфейс тела HTTP-запроса.
 * @author Namespace Stedd
 */
public interface HttpBody {

    /**
     * Преобразование интерфейса в тело HTTP-запроса.
     * @author Namespace Stedd
     * @return тело HTTP-запроса
     */
    default BodyPublisher toBody() {
        return BodyPublishers.ofString("");
    }

}
