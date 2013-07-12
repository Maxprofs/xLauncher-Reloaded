package ru.xeroxp.launcher;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class xSettingsOfTheme {
    public static xServer[] Servers = {
       new xServer(0, "Classic", "localhost", "25565", "hitech", "minecraft.jar", "1.5.2", 100, 0, 0, 724, 110, 6, 6, 180, 98, 500, 30, 200, 24, new Color(114, 114, 114), 450, 78, 70, 14, new Color(114, 114, 114), 231, 71, 484.0D, 29),
       new xServer(1, "Hitech", "localhost", "25565", "sandbox", "minecraft.jar", "1.4.7", 100, 0, 120, 724, 110, 6, 126, 180, 98, 500, 150, 200, 24, new Color(114, 114, 114), 450, 198, 70, 14, new Color(114, 114, 114), 231, 191, 484.0D, 29),
       new xServer(2, "Rpg", "localhost", "25565", "new", "minecraft.jar", "1.6.0", 100, 0, 240, 724, 110, 6, 246, 180, 98, 500, 270, 200, 24, new Color(114, 114, 114), 450, 318, 70, 14, new Color(114, 114, 114), 231, 311, 484.0D, 29),
       new xServer(3, "Sandbox", "localhost", "25565", "", "minecraft.jar", "1.4.7", 80, 0, 360, 724, 110, 6, 366, 180, 98, 500, 390, 200, 24, new Color(114, 114, 114), 450, 438, 70, 14, new Color(114, 114, 114), 231, 431, 484.0D, 29),
       new xServer(4, "Rpg", "localhost", "25565", "", "minecraft.jar", "1.4.7", 100, 0, 480, 724, 110, 6, 486, 180, 98, 500, 510, 200, 24, new Color(114, 114, 114), 450, 558, 70, 14, new Color(114, 114, 114), 231, 551, 484.0D, 29),
       new xServer(5, "Classic", "localhost", "25565", "", "minecraft.jar", "1.4.7", 100, 0, 600, 724, 110, 6, 606, 180, 98, 500, 630, 200, 24, new Color(114, 114, 114), 450, 678, 70, 14, new Color(114, 114, 114), 231, 671, 484.0D, 29),
       new xServer(6, "Hitech", "localhost", "25565", "", "minecraft.jar", "1.4.7", 100, 0, 720, 724, 110, 6, 726, 180, 98, 500, 750, 200, 24, new Color(114, 114, 114), 450, 798, 70, 14, new Color(114, 114, 114), 231, 791, 484.0D, 29),
       new xServer(7, "Classic", "localhost", "25565", "", "minecraft.jar", "1.4.7", 80, 0, 840, 724, 110, 6, 846, 180, 98, 500, 870, 200, 24, new Color(114, 114, 114), 450, 918, 70, 14, new Color(114, 114, 114), 231, 911, 484.0D, 29),
       new xServer(8, "Sandbox", "localhost", "25565", "", "minecraft.jar", "1.4.7", 100, 0, 960, 724, 110, 6, 966, 180, 98, 500, 990, 200, 24, new Color(114, 114, 114), 450, 1038, 70, 14, new Color(114, 114, 114), 231, 1031, 484.0D, 29)
   }; //id, название(выполняет роль текста и названия картинки), ip, port, подпапка, клиент, версия, максимальное количество слотов сервера, координаты и размеры, цвета текстов
   public static xLabel[] Labels = {
       new xLabel(0, "Сайт", new Color(63, 63, 63), 593, 65, 100, 14, "http://author-xeroxp.ru/"),
       new xLabel(1, "Регистрация", new Color(63, 63, 63), 593, 98, 100, 14, "http://author-xeroxp.ru/registration/"),
       new xLabel(2, "Личный кабинет", new Color(63, 63, 63), 593, 131, 150, 14, "http://author-xeroxp.ru/lk/"),
       new xLabel(3, "Форум", new Color(63, 63, 63), 593, 164, 100, 14, "http://author-xeroxp.ru/forum/")
   }; //id, название, цвет, координаты и размеры ссылки, сама ссылка
    public static xButton[] Buttons = {
        new xButton(0, "Обновить клиент", "update_button.png", "update_button_pressed.png", "update_button_disabled.png", 730, 448, 39, 39, "UL", ""),
        new xButton(1, "Авторизация", "auth_button.png", "auth_button_pressed.png", "auth_button.png", 545, 448, 178, 39, "JL", "JKL"),
        new xButton(2, "Сохранение и перезагрузка с данной памятью", "save_button.png", "save_button_pressed.png", "save_button.png", 730, 400, 39, 39, "RML", "")
    }; //id, название(в данном случае ни на что не влияет), картинка, картинка при нажатии, картинка при деактивации кнопки, координаты и размеры, переменные(не менять!!!)
    public static xCheckbox[] Checkboxes = {
        new xCheckbox(0, "Запомнить пароль", 573, 331, 120, 14, new Color(63, 63, 63), "checkbox.png", "checkbox_select.png", 549, 331, 16, 16, "RPL"),
        new xCheckbox(1, "Режим оффлайн", 573, 364, 120, 14, new Color(63, 63, 63), "checkbox.png", "checkbox_select.png", 549, 364, 16, 16, "GML")
    }; //id, название(выполняет роль текста), координаты и размеры, цвет, картинка, активированная картинка, координаты и размеры, переменные(не менять!!!)
    public static xHeaderButton[] HeaderButtons = {
        new xHeaderButton(0, "exit", "exit_button.png", "exit_button.png", 775, 6, 14, 14),
        new xHeaderButton(1, "minimize", "minimize_button.png", "minimize_button.png", 745, 6, 14, 14)
    }; //id, название(не менять!!!), картинка, картинка при наведении, координаты и размеры
    public static xTextField[] Fields = {
        new xTextField(0, "Логин", new Color(114, 114, 114), 20, 560, 232, 200, 14, "login_field.png"),
        new xTextField(1, "Пароль", new Color(114, 114, 114), 26, 560, 281, 200, 14, "password_field.png"),
        new xTextField(2, "Память", new Color(114, 114, 114), 6, 560, 411, 120, 20, "memory_field.png")
    }; //id, название(не менять!!!), цвет, максимальное количество символов, координаты и размеры, картинка
    public static int[] LoginFieldBounds = {549, 225}; //координаты картинки для логина
    public static int[] PasswordFieldBounds = {549, 273}; //координаты картинки для пароля
    public static int[] MemoryFieldBounds = {547, 405}; //координаты картинки для памяти
    
    public static int[] LauncherSize = {800, 599}; //размер лаунчера
    public static String FontFile1 = "arial.ttf"; //название файла первого шрифта
    public static String FontFile2 = "MyriadPro.otf"; //название файла второго шрифта
    public static String ClickButtonSound = "click.wav"; //название файла звука клика
    public static String MainPanelBackgroundImage = "launcher_bg.png"; //картинка фона главной панели лаунчера
    public static float[] MainFonts = {12.0F, 12.0F}; //размеры шрифтов в главной панели
    
    public static String Logo = "logo.png"; //картинка-лого
    public static int[] LogoBounds = {50, 70}; //координаты лого
    
    public static int[] HeaderBounds = {20, 5, 200, 14}; //координаты текста на рамке (сверху)
    public static Color HeaderColor = new Color(114, 114, 114); //цвет текста на рамке (сверху)
    
    public static int[] ErrorLabelBounds = {485, 513, 300, 14}; //координаты панели информации о входе (там, где надпись "Авторизация...")
    public static Color ErrorLabelColor = new Color(63, 63, 63); //цвет текта панели информации о входе
    
    public static int[] PercentLabelBounds = {360, 542, 200, 15}; //координаты панели с процентами (на полосе загрузки)
    public static Color PercentLabelColor = new Color(114, 114, 114); //цвет текста панели с процентами
    
    public static String MemoryLabelText = "mb"; //надпись в панели выбора памяти
    public static int[] MemoryLabelBounds = {690, 410, 20, 20}; //координаты надписи в панели выбора памяти
    public static Color MemoryLabelColor = new Color(114, 114, 114); //цвет текста надписи в панели выбора памяти
    
    public static String[] ScrollbarImages = {"scrollbar.png", "scrollbar_bg.png"}; //картинки полосы прокрутки
    
    public static int[] NewsScrollBarSize = {12,30}; //размеры полосы прокрутки новостей
    //Если включены анимированные новости
    public static String[] NewsButtonIcons = {"news_button.png", "news_button.png", "news_button_disabled.png"}; //картинки кнопки новостей
    public static int[] NewsButtonBounds = {0, 0, 50, 279}; //координаты и размеры кнопки новостей
    public static int NewsPanelHeight1 = 279; //высота панели новостей
    public static int NewsPanelWidth1 = 330; //ширина панели новостей
    public static int NewsPanelY1 = 180; //положение панели новостей по высоте
    public static Color NewsPanelBgColor1 = new Color(0, 0, 0, 100); //цвет фона пвнели новостей
    public static String NewsBgImage = "news_panel.png"; //картинка фона панели новостей
    //Если выключены анимированные новости
    public static int NewsPanelHeight2 = 305; //высота панели новостей
    public static int NewsPanelWidth2 = 430; //ширина панели новостей
    public static int NewsPanelY2 = 180; //положение панели новостей по высоте
    public static int NewsPanelX2 = 50; //положение панели новостей по ширине
    public static Color NewsPanelBgColor2 = new Color(0, 0, 0, 50); //цвет фона пвнели новостей
    
    public static String ServersPanelBackgroundImage = "servers_bg.png"; //картинка панели выбора серверов
    public static String[] ServersImages = {"serverinfobox.png", "serverinfobox_pressed.png"}; //картинки панелек серверов
    public static String[] ServersBarImages = {"serverbar_on.png", "serverbar_off.png", "serverbar_bg.png"}; //картинки полосы загруженности серверов
    public static float[] ServersFonts = {12.0F, 16.0F, 24.0F}; //размеры шрифтов панели серверов
    public static int[] ServersScrollPanelBounds = {20, 65, 760, 495}; //координаты панели с серверами в панели выбора серверов
    public static int[] ServersInScrollPanelSize = {740, xSettingsOfTheme.Servers.length * 120}; //размеры листа с серверами
    public static int[] ServersScrollBarSize = {17, 479}; //размеры полосы прокрутки панели серверов
    
    public static int[] ErrorPanelSize = {500, 200}; //размеры окна ошибок (выводится в самой игре)
    public static Color ErrorPanelTextColor = Color.red; //цвет текста в окне ошибок
    public static float ErrorPanelTextSize = 20.0F; //размер текста в окне ошибок
    public static String ErrorPanelImage = "errorpanel.png"; //фон в окне ошибок
}