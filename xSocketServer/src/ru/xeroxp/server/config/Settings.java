package ru.xeroxp.server.config;

public class Settings {
    public static final int PORT_WORK = 4444; // Порт сокет-сервера для клиентов
    public static final int PORT_STOP = 4445; // Порт сокет-сервера для его отключения
    public static final String STOP_IP = "127.0.0.1"; // ip адрес, сокет-сервера
    public static final String MAIN_FILE = "http://localhost/3/mainfile.php"; // Ссылка до главного файла
    public static final String JOIN_SERVER = "http://localhost/3/joinserver.php"; // Ссылка на joinserver.php
    public static final int MONITOR_TIME_UPDATE = 120000; //Время обновления мониторинга в миллисекундах (1000ms = 1sec) [ставить не меньше 1 минуты = 60000 миллисекунд]
    public static final int SYMBOLS_COUNT = 5; // Количество символов в соли
    public static final String SESSION_ID_KEY = "QWERTY"; // Ключ защиты сессии (такой же должен быть указан в web-части)
    public static final String PASS_ID_KEY = "QWERTY1"; // Ключ защиты пароля (такой же должен быть в лаунчере)
    public static final String LAUNCHER_FILE_NAME = "xLauncher"; // Имя файла лаунчера

    public static final byte[] KEY_1 = {
            0x74, 0x68, 0x69, 0x73, 0x49, 0x65, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
    }; // Ключ сессии сервера
    public static final byte[] KEY_2 = {
            0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
    }; // Ключ сессии клиента (в клиенте должен быть такой же)
    public static final byte[] KEY_3 = {
            0x74, 0x68, 0x68, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
    }; // Ключ обмена информацией между лаунчером и сокет-сервером (в лаунчере должен быть такой же)

    public static final String[] CHECK_FORMATS = {
            ".zip", ".jar", ".class", ".dll", ".exe", ".bat", ".cmd", ".sh"
    }; // Проверяемые форматы файлов

    /**
     * Режим отладки
     */
    public static final boolean DEBUG_MODE = true; // Отключите если не знаете что это
}
