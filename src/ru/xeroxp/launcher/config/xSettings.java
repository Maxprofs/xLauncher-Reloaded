package ru.xeroxp.launcher.config;

import ru.xeroxp.launcher.xServerConnect;

public final class xSettings {
    public static final String SITE_LINK = "http://mw.endlessworlds.ru/"; // Ссылка на главную страницу сайта
    public static final String MINE_FOLDER = "magicwars"; // Папка в которой находится майнкрафт (.minecraft) без точки
    public static final String DOWN_LAUNCHER_LINK = "http://localhost/launcher/"; // Путь до папки, в которой лежит лаунчер
    public static final String LAUNCHER_FILE_NAME = "MagicWars"; // Названия файла лаунчера (.jar и .exe)
    public static final String DOWN_CLIENT_LINK = "http://localhost/client/"; // Путь до папки, в которой хранится client.zip (в нем хранятся все файлы клиентов)
    public static final String MAIN_INFO_FILE = "http://localhost/maininfo.php"; // Путь до файла информации
    public static final String LAUNCHER_NAME = "MagicWars Launcher"; // Название лаунчера
    public static final String MONITOR_LINK = "http://localhost/data/monitor.txt"; // Ссылка на файл мониторинга
    public static final int CHECK_TIME = 70000; // Время между проверками клиента
    public static final String GAME_NAME = "MagicWars"; // Название окна игры
    public static final String LAUNCHER_VERSION = "1.0.0"; // Версия лаунчера
    public static final String[] OFFLINE_CLIENT = {"magicwars", "Forge1.6.4.jar", "1.6.4"}; // Клиент, который будет запускаться в оффлайн режиме
    public static final String NEWS_URL = "http://localhost/MineCraft/news.html"; // Ссылка на файл с новостями
    public static final String PASS_ID_KEY = "QWERTY1"; // Ключ защиты пароля (такой же должен быть в сервере)
    public static final int LOCAL_PORT = 6565; // Порт проверки лаунчера из клиента
    public static final byte[] KEY = {
            0x74, 0x68, 0x68, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
    }; // Ключ обмена информацией между лаунчером и сокет-сервером (в сокет-сервере должен быть такой же)
    public static final boolean ANIMATED_NEWS = false; // Переключение вида новостей
    public static final xServerConnect[] SERVER_CONNECTS = {
            new xServerConnect("127.0.0.1", 4444)
    }; // ip адрес и port сокет-сервера
    public static final boolean PATCH_DIR = true; // Использовать автоматическую замену директории игры
    public static final String MC_CLASS = "net.minecraft.client.main.Main";
    public static final String[] MC_VERSIONS = {
            "1.2.5::aj", "1.3.x::am", "1.4.x::an", "1.5.x::an", "1.6.x::an"
    }; // Версии и классы для автозамены дирректории игры

    /**
     * Режим отладки
     */
    public static final boolean DEBUG_MODE = true; // Отключите если не знаете что это
}
