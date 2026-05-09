package namespace.stedd.messaging.http;

import namespace.stedd.data.Converter;
import namespace.stedd.data.type.KeyPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Структура параметров HTTP-запроса.
 * @author Namespace Stedd
 */
public class HttpParameter extends KeyPair {

    /**
     * Создание структуры параметров HTTP-запроса.
     * @author Namespace Stedd
     * @param key ключ параметра
     * @param value объект-значение параметра
     */
    public HttpParameter(String key, Object value) {
        super(key, value);
    }

    /**
     * Создание структуры параметров HTTP-запроса.
     * @author Namespace Stedd
     * @param key ключ параметра
     * @param value объект-значение параметра
     * @return структура параметров HTTP-запроса
     */
    public static HttpParameter create(String key, Object value) {
        return new HttpParameter(key, value);
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

    /**
     * Преобразование строки HTTP-параметров в массив параметров HTTP-запроса.
     * @author Namespace Stedd
     * @param httpParametersString строка HTTP-параметров
     * @return массив параметров HTTP-запроса
     */
    public static HttpParameter[] fromHttpParametersString(String httpParametersString) {
        String[] keyPairs = httpParametersString.split("&");
        List<HttpParameter> httpParameters = new ArrayList<>();
        // TODO: HP.getValue(T)
        for (String pair : keyPairs) {
            String[] keyPair = pair.split("=");
            if (keyPair.length > 0) {
                String key = keyPair[0];
                String value = keyPair.length > 1 ? keyPair[1] : null;
                httpParameters.add(HttpParameter.create(key, value));
            }
        }
        return Converter.toArray(httpParameters, HttpParameter.class);
    }

    /**
     * Преобразование массива параметров HTTP-запроса в карту.
     * @author Namespace Stedd
     * @param httpParameters массив параметров HTTP-запроса
     * @return карта параметров HTTP-запроса
     */
    public static Map<String, Object> toMap(HttpParameter... httpParameters) {
        Map<String, Object> httpParametersMap = new HashMap<>();
        for (HttpParameter httpParameter : httpParameters) {
            httpParametersMap.put(httpParameter.getKey(), httpParameter.getValue());
        }
        return httpParametersMap;
    }

}
