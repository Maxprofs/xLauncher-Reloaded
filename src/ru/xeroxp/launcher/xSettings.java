package ru.xeroxp.launcher;

import java.awt.Color;

public class xSettings {
   public static String siteLink = "http://author-xeroxp.ru/"; //Ссылка на главную страницу сайта 
   public static String mineFolder = "xeroxp_socket"; //Папка в которой находится майнкрафт (.minecraft) без точки
   public static String downLauncherLink = "http://localhost/3/launcher/"; //Путь до папки, в которой лежит лаунчер (xLauncher.jar и xLauncher.exe)
   public static String downClientLink = "http://localhost/3/client/"; //Путь до папки, в которой хранится client.zip (в нем хранятся все файлы клиентов)
   public static String maininfofile = "http://localhost/3/maininfo.php"; //Путь до файла информации
   public static String LauncherName = "XeroXP Launcher"; //Название лаунчера
   public static String monitorLink = "http://localhost/3/data/monitor.txt"; //Ссылка на файл мониторинга
   public static int checkTime = 70000; //промежутки проверки клиента
   public static String gameName = "XeroXP Minecraft"; //Название окна игры
   public static String launcherVersion = "1.0.0"; //версия лаунчера
   public static String[] offlineClient = {"", "minecraft.jar", "1.4.7"}; //Клиент, который будет запускаться в оффлайн режиме
   public static String newsUrl = "http://localhost/3/news.html"; //Ссылка на файл с новостями
   public static String passidkey = "QWERTY1"; //Ключ защиты пароля (такой же должен быть в сервере)
   public static int localport = 6565; //порт проверки лаунчера из клиента
   public static byte[] key = {
       0x74, 0x68, 0x68, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
   }; //Ключ обмена информацией между лаунчером и сокет-сервером (в сокет-сервере должен быть такой же)
   public static boolean animatednews = true; //Переключение вида новостей
   public static xServerConnect[] connectServers = {
       new xServerConnect(0, "192.168.10.3", 4444)
   }; //id, ip адрес и port сокет-серверов
   public static boolean patchDir = true; //Использовать автоматическую замену директории игры
   public static String mcclass = "net.minecraft.client.Minecraft";
   public static String[] mcversions = {
       "1.2.5::aj", "1.3.x::am", "1.4.x::an", "1.5.x::an"
   }; //версии и переменные для версий для автозамены дирректории игры
}
