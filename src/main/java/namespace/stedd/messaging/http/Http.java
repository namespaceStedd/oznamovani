package namespace.stedd.messaging.http;

import namespace.stedd.data.console.OutputSystem;
import namespace.stedd.data.crypto.CryptoProtocol;
import namespace.stedd.data.crypto.MockTrustManager;
import namespace.stedd.data.crypto.SecureKeyType;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Класс управления HTTP-запросами.
 * @author Namespace Stedd
 */
public class Http {

    private HttpClient.Builder clientBuilder;   // Сборщик Клиента HTTP, отправляющий запросы

    /**
     * Создание базового HTTP-управляющего.
     * @author Namespace Stedd
     */
    public Http() {
        this.clientBuilder = HttpClient.newBuilder();
    }

    /**
     * Создание защищённого HTTPS-управляющего.
     * @author Namespace Stedd
     * @param password пароль, использованный для создания сертификата
     * @param certificate защищённое хранилище Криптографических ключей
     * @param outputSystem система вывода информации об исключении
     */
    public Http(String password, KeyStore certificate, OutputSystem outputSystem) {
        try {
            // Получение контекста SSL
            SSLContext sslContext = getSslContext(password, certificate);
            this.clientBuilder = HttpClient.newBuilder().sslContext(sslContext);
        }
        catch (Exception exception) {
            // TODO: -> toExceptionString <-
            outputSystem.write("Exception: " + exception.getMessage());
            outputSystem.write("Created Default Http");
            this.clientBuilder = HttpClient.newBuilder();
        }
    }

    /**
     * Создание защищённого HTTPS-управляющего.
     * @author Namespace Stedd
     * @param password пароль, использованный для создания сертификата
     * @param sslCertificateClassPath классовый путь до SSL-сертификата
     * @param outputSystem система вывода информации об исключении
     */
    public Http(String password, String sslCertificateClassPath, OutputSystem outputSystem) {
        try {
            // Преобразование пароля в массив символов
            char[] charsPassword = password.toCharArray();
            // Объявление защищённого хранилища для Криптографических ключей и сертификатов
            KeyStore certificate = getKeyStoreFromClasspath(sslCertificateClassPath, SecureKeyType.JKS, password);
            // Создание ключевого управляющего
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(certificate, password.toCharArray());
            // Создание контекста использования SSL
            SSLContext sslContext = SSLContext.getInstance(CryptoProtocol.SSL.name());
            sslContext.init(keyManagerFactory.getKeyManagers(), new TrustManager[] { MockTrustManager.create() }, new SecureRandom());
            // Получение контекста SSL
            sslContext = getSslContext(password, certificate);
            this.clientBuilder = HttpClient.newBuilder().sslContext(sslContext);
        }
        catch (Exception exception) {
            // TODO: -> toExceptionString <-
            outputSystem.write("Exception: " + exception.getMessage());
            outputSystem.write("Created Default Http");
            this.clientBuilder = HttpClient.newBuilder();
        }
    }

    /**
     * Получение ответа на HTTP-запрос.
     * @author Namespace Stedd
     * @param request отправляемый HTTP-запрос
     * @return ответ на HTTP-запрос
     * @throws IOException исключение при операции ввода / вывода
     * @throws InterruptedException исключение при выполнении потока
     */
    public String getResponse(HttpRequest request) throws IOException, InterruptedException {
        try (HttpClient client = this.clientBuilder.build()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return response.body();
        }
    }

    /**
     * Получение ответа на HTTP-запрос.
     * @author Namespace Stedd
     * @param request отправляемый HTTP-запрос
     * @param outputSystem система вывода информации об исключении
     * @return ответ на HTTP-запрос
     */
    public String getResponse(HttpRequest request, OutputSystem outputSystem) {
        try (HttpClient client = this.clientBuilder.build()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return response.body();
        }
        catch (IOException exception) {
            // TODO: Выводить информацию об ошибке в ... по выбору (от интерфейса тогда)
            // TODO: Разграничить?
            // TODO: -> toExceptionString <-
            outputSystem.write("IOException: " + exception.getMessage());
            return null;
        }
        catch (InterruptedException exception) {
            outputSystem.write("InterruptedException: " + exception.getMessage());
            return null;
        }
    }

    /**
     * Получение хранилища ключей из классового пути.
     * @author Namespace Stedd
     * @param classpath путь до пользовательских классов
     * @param type тип ключа
     * @param password пароль сертификата
     * @return Хранилище ключей
     * @throws KeyStoreException исключение при использовании хранилища ключей
     * @throws CertificateException исключение при обработке сертификатов
     * @throws IOException исключение при операции ввода / вывода
     * @throws NoSuchAlgorithmException исключение при недоступности Криптографического алгоритма
     */
    public static KeyStore getKeyStoreFromClasspath(String classpath, SecureKeyType type, String password)
            throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
        // Создание временного Хранилища ключей
        KeyStore keyStore = KeyStore.getInstance(type.name());
        // Чтение существующего
        InputStream inputStream = Http.class.getResourceAsStream(classpath);
        // Загрузка в хранилище
        keyStore.load(inputStream, password.toCharArray());
        // Возврат каретки
        return keyStore;
    }

    /**
     * Получение SSL-контекста.
     * @author Namespace Stedd
     * @param password пароль, использованный для создания сертификата
     * @param certificate защищённое хранилище Криптографических ключей
     * @return сформированный SSL-контекст
     * @throws UnrecoverableKeyException исключение при восстановлении Криптографического ключа из хранилища
     * @throws KeyStoreException исключение при использовании хранилища ключей
     * @throws NoSuchAlgorithmException исключение при недоступности Криптографического алгоритма
     * @throws KeyManagementException исключение при использовании ключей
     */
    public static SSLContext getSslContext(String password, KeyStore certificate)
            throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        // Создание ключевого управляющего
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(certificate, password.toCharArray());
        // Создание контекста использования SSL
        SSLContext sslContext = SSLContext.getInstance(CryptoProtocol.SSL.name());
        sslContext.init(keyManagerFactory.getKeyManagers(), new TrustManager[] { MockTrustManager.create() }, new SecureRandom());
        // Возврат каретки
        return sslContext;
    }

}
