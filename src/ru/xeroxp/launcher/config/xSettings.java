package ru.xeroxp.launcher.config;

import ru.xeroxp.launcher.xServerConnect;

public class xSettings {
    public static final String siteLink = "http://magicwars.high-sky.ru/"; // Ссылка на главную страницу сайта
    public static final String mineFolder = "magicwars"; // Папка в которой находится майнкрафт (.minecraft) без точки
    public static final String downLauncherLink = "http://localhost/launcher/"; // Путь до папки, в которой лежит лаунчер (xLauncher.jar и xLauncher.exe)
    public static final String downClientLink = "http://localhost/client/"; // Путь до папки, в которой хранится client.zip (в нем хранятся все файлы клиентов)
    public static final String mainInfoFile = "http://localhost/maininfo.php"; // Путь до файла информации
    public static final String LauncherName = "MagicWars Launcher"; // Название лаунчера
    public static final String monitorLink = "http://localhost/data/monitor.txt"; // Ссылка на файл мониторинга
    public static final int checkTime = 70000; // Промежутки проверки клиента
    public static final String gameName = "MagicWars"; // Название окна игры
    public static final String launcherVersion = "1.0.0"; // Версия лаунчера
    public static final String[] offlineClient = {"magicwars", "Forge1.6.4.jar", "1.6.4"}; // Клиент, который будет запускаться в оффлайн режиме
    public static final String newsUrl = "http://localhost/MineCraft/news.html"; // Ссылка на файл с новостями
    public static final String passIdKey = "QWERTY1"; // Ключ защиты пароля (такой же должен быть в сервере)
    public static final int localPort = 6565; // Порт проверки лаунчера из клиента
    public static final byte[] key = {
            0x74, 0x68, 0x68, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
    }; // Ключ обмена информацией между лаунчером и сокет-сервером (в сокет-сервере должен быть такой же)
    public static final boolean animatedNews = false; // Переключение вида новостей
    public static final xServerConnect[] connectServers = {
            new xServerConnect("192.168.10.3", 4444)
    }; // id, ip адрес и port сокет-серверов
    public static final boolean patchDir = true; //Использовать автоматическую замену директории игры
    public static final String mcClass = "net.minecraft.client.Minecraft";
    public static final String[] mcVersions = {
            "1.2.5::aj", "1.3.x::am", "1.4.x::an", "1.5.x::an", "1.6.x::an"
    }; // Версии и классы для автозамены дирректории игры

    public static boolean debug = true; // Режим отладки - отключите если не знаете что это
}
