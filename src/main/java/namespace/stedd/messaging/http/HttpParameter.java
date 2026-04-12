package namespace.stedd.messaging.http;

import java.util.ArrayList;
import java.util.List;

/**
 * Структура параметров HTTP-запроса.
 * // TODO: Unif Impl
 * @author Namespace Stedd
 */
public class HttpParameter {

    private String key;   // Ключ параметра
    private Object value;   // Объект-значение параметра

    /**
     * Создание структуры параметров HTTP-запроса.
     * @author Namespace Stedd
     * @param key ключ параметра
     * @param value объект-значение параметра
     */
    public HttpParameter(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Создание структуры параметров HTTP-запроса.
     * @author Namespace Stedd
     * @param key ключ параметра
     * @param value объект-значение параметра
     */
    public static HttpParameter create(String key, Object value) {
        return new HttpParameter(key, value);
    }

    /**
     * Получение ключа параметра.
     * @author Namespace Stedd
     * @return ключ параметра
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Обновление ключа параметра.
     * @author Namespace Stedd
     * @return ключ параметра
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Получение объект-значения параметра.
     * @author Namespace Stedd
     * @return объект-значение параметра
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Обновление объект-значения параметра.
     * @author Namespace Stedd
     * @return объект-значение параметра
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Преобразование структуры параметров HTTP-запроса в строку.
     * @author Namespace Stedd
     * @return строчная структура параметров HTTP-запроса
     */
    @Override
    public String toString() {
        return this.key != null && this.value != null ? this.key + '=' + this.value : "";
    }

    /**
     * Создание Конструктора параметров HTTP-запроса.
     * @author Namespace Stedd
     * @return Конструктор параметров HTTP-запроса
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Конструктор параметров HTTP-запроса.
     * @author Namespace Stedd
     */
    public static class Builder {

        private final List<HttpParameter> httpParameters;   // Указанные HTTP-параметры

        /**
         * Создание Конструктора параметров HTTP-запроса.
         * @author Namespace Stedd
         */
        public Builder() {
            this.httpParameters = new ArrayList<>();
        }

        /**
         * Создание Конструктора параметров HTTP-запроса.
         * @author Namespace Stedd
         * @return Конструктор параметров HTTP-запроса
         */
        public static Builder newBuilder() {
            return new Builder();
        }

        /**
         * Добавление параметра HTTP-запроса.
         * @author Namespace Stedd
         * @param key ключ параметра
         * @param value объект-значение параметра
         * @return Конструктор параметров HTTP-запроса
         */
        public Builder addHttpParameter(String key, Object value) {
            this.httpParameters.add(HttpParameter.create(key, value));
            return this;
        }

        /**
         * Формирование строки параметров к HTTP-запросу.
         * @author Namespace Stedd
         * @return строка параметров HTTP-запроса
         */
        public String build() {
            if (this.httpParameters.isEmpty()) {
                return "";
            }
            StringBuilder httpParameters = new StringBuilder();
            for (HttpParameter httpParameter : this.httpParameters) {
                String parameter = httpParameter.toString();
                // TODO: Conv
                httpParameters.append(parameter != null && !parameter.isEmpty() ? parameter + "&" : "");
            }
            // TODO: Conv
            return !httpParameters.isEmpty() ? "?" + httpParameters.deleteCharAt(httpParameters.length() - 1) : "";
        }

        /**
         * Формирование полной строки HTTP-запроса.
         * @author Namespace Stedd
         * @param withLink HTTP-ссылка
         * @return строка HTTP-запроса
         */
        public String build(String withLink) {
            return withLink + this.build();
        }

    }

}
