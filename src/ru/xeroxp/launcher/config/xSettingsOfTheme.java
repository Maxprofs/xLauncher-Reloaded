package ru.xeroxp.launcher.config;

import ru.xeroxp.launcher.gui.elements.*;
import ru.xeroxp.launcher.gui.xHeaderButton;

import java.awt.*;

public class xSettingsOfTheme {
    public static final xServer[] SERVERS = {
            new xServer(0, "MagicWars", "localhost", "25565", "client", "Forge1.6.4.jar", "1.6.4", 100, 0, 0, 724, 110, 6, 6, 180, 98, 500, 30, 200, 24, new Color(114, 114, 114), 450, 78, 70, 14, new Color(114, 114, 114), 231, 71, 484.0D, 29),
            new xServer(1, "Hitech", "localhost", "25565", "sandbox", "minecraft.jar", "1.4.7", 100, 0, 120, 724, 110, 6, 126, 180, 98, 500, 150, 200, 24, new Color(114, 114, 114), 450, 198, 70, 14, new Color(114, 114, 114), 231, 191, 484.0D, 29),
            new xServer(2, "Rpg", "localhost", "25565", "new", "minecraft.jar", "1.6.0", 100, 0, 240, 724, 110, 6, 246, 180, 98, 500, 270, 200, 24, new Color(114, 114, 114), 450, 318, 70, 14, new Color(114, 114, 114), 231, 311, 484.0D, 29),
            new xServer(3, "Sandbox", "localhost", "25565", "", "minecraft.jar", "1.4.7", 80, 0, 360, 724, 110, 6, 366, 180, 98, 500, 390, 200, 24, new Color(114, 114, 114), 450, 438, 70, 14, new Color(114, 114, 114), 231, 431, 484.0D, 29),
            new xServer(4, "Rpg", "localhost", "25565", "", "minecraft.jar", "1.4.7", 100, 0, 480, 724, 110, 6, 486, 180, 98, 500, 510, 200, 24, new Color(114, 114, 114), 450, 558, 70, 14, new Color(114, 114, 114), 231, 551, 484.0D, 29),
            new xServer(5, "Classic", "localhost", "25565", "", "minecraft.jar", "1.4.7", 100, 0, 600, 724, 110, 6, 606, 180, 98, 500, 630, 200, 24, new Color(114, 114, 114), 450, 678, 70, 14, new Color(114, 114, 114), 231, 671, 484.0D, 29),
            new xServer(6, "Hitech", "localhost", "25565", "", "minecraft.jar", "1.4.7", 100, 0, 720, 724, 110, 6, 726, 180, 98, 500, 750, 200, 24, new Color(114, 114, 114), 450, 798, 70, 14, new Color(114, 114, 114), 231, 791, 484.0D, 29),
            new xServer(7, "Classic", "localhost", "25565", "", "minecraft.jar", "1.4.7", 80, 0, 840, 724, 110, 6, 846, 180, 98, 500, 870, 200, 24, new Color(114, 114, 114), 450, 918, 70, 14, new Color(114, 114, 114), 231, 911, 484.0D, 29),
            new xServer(8, "Sandbox", "localhost", "25565", "", "minecraft.jar", "1.4.7", 100, 0, 960, 724, 110, 6, 966, 180, 98, 500, 990, 200, 24, new Color(114, 114, 114), 450, 1038, 70, 14, new Color(114, 114, 114), 231, 1031, 484.0D, 29)
    }; // id, название(текст и названия картинки), ip, port, подпапка, клиент, версия, максимальное количество слотов сервера, координаты и размеры, цвета текстов

    public static final xLabel[] LABELS = {
            new xLabel("База знаний", new Color(63, 63, 63), 593, 65, 100, 14, "http://magicwars.high-sky.ru/"),
            new xLabel("Личный кабинет", new Color(63, 63, 63), 593, 98, 150, 14, "http://magicwars.high-sky.ru/cabinet/"),
            new xLabel("Еще ссылка", new Color(63, 63, 63), 593, 131, 150, 14, "http://magicwars.high-sky.ru/"),
            new xLabel("И еще", new Color(63, 63, 63), 593, 164, 100, 14, "http://magicwars.high-sky.ru/")
    }; // Название, цвет, координаты и размеры ссылки, сама ссылка

    public static final xButton[] BUTTONS = {
            new xButton(0, "update_button.png", "update_button_pressed.png", "update_button_disabled.png", 730, 448, 39, 39),
            new xButton(1, "auth_button.png", "auth_button_pressed.png", "auth_button.png", 545, 448, 178, 39),
            new xButton(2, "save_button.png", "save_button_pressed.png", "save_button.png", 730, 400, 39, 39)
    }; // id (не менять!), картинка, картинка при нажатии, картинка при деактивации кнопки, координаты и размеры

    public static final xCheckbox[] CHECKBOXES = {
            new xCheckbox(0, "Запомнить пароль", 573, 331, 120, 14, new Color(63, 63, 63), "checkbox.png", "checkbox_select.png", 549, 331, 16, 16),
            new xCheckbox(1, "Режим оффлайн", 573, 364, 120, 14, new Color(63, 63, 63), "checkbox.png", "checkbox_select.png", 549, 364, 16, 16)
    }; // id (не менять!), название(выполняет роль текста), координаты и размеры, цвет, картинка, активированная картинка, координаты и размеры

    public static final xHeaderButton[] HEADER_BUTTONS = {
            new xHeaderButton("exit", "exit_button.png", "exit_button.png", 775, 6, 14, 14),
            new xHeaderButton("minimize", "minimize_button.png", "minimize_button.png", 745, 6, 14, 14)
    }; // Название (не менять!), картинка, картинка при наведении, координаты и размеры

    public static final xTextField[] FIELDS = {
            new xTextField(0, "Логин", new Color(114, 114, 114), 20, 560, 232, 200, 14, "login_field.png"),
            new xTextField(1, "Пароль", new Color(114, 114, 114), 26, 560, 281, 200, 14, "password_field.png"),
            new xTextField(2, "Память", new Color(114, 114, 114), 6, 560, 411, 120, 20, "memory_field.png")
    }; // id (не менять!), название, цвет, максимальное количество символов, координаты и размеры, картинка

    public static final int[] LOGIN_FIELD_BOUNDS = {549, 225}; // Координаты картинки для логина
    public static final int[] PASSWORD_FIELD_BOUNDS = {549, 273}; // Координаты картинки для пароля
    public static final int[] MEMORY_FIELD_BOUNDS = {547, 405}; // Координаты картинки для памяти

    public static final int[] LAUNCHER_SIZE = {800, 599}; // Размер лаунчера (ширина и высота)
    public static final String FAVICON = "favicon.png"; // Значек
    public static final String MAIN_PANEL_BACKGROUND_IMAGE = "launcher_bg.png"; // Картинка фона главной панели лаунчера
    public static final float[] FONTS_SIZE = {12.0F, 12.0F}; // Размеры шрифтов в главной панели
    public static final String MAIN_FONT_FILE = "arial.ttf"; // Название файла первого шрифта
    public static final String SERVER_FONT_FILE = "MyriadPro.otf"; // Название файла второго шрифта

    public static final String LOGO = "logo.png"; // Картинка-лого
    public static final int[] LOGO_BOUNDS = {50, 70}; // Координаты лого

    public static final int[] HEADER_BOUNDS = {20, 5, 200, 14}; // Координаты текста на рамке (сверху)
    public static final Color HEADER_COLOR = new Color(114, 114, 114); // Цвет текста на рамке (сверху)

    /**
     * Labels
     */
    public static final int[] ERROR_LABEL_BOUNDS = {485, 513, 300, 14}; // Координаты панели информации о входе (там, где надпись "Авторизация...")
    public static final Color ERROR_LABEL_COLOR = new Color(63, 63, 63); // Цвет текта панели информации о входе

    public static final int[] PERCENT_LABEL_BOUNDS = {360, 542, 200, 15}; // Координаты панели с процентами (на полосе загрузки)
    public static final Color PERCENT_LABEL_COLOR = new Color(114, 114, 114); // Цвет текста панели с процентами

    public static final String MEMORY_LABEL_TEXT = "mb"; // Надпись в панели выбора памяти
    public static final int[] MEMORY_LABEL_BOUNDS = {690, 410, 20, 20}; // Координаты надписи в панели выбора памяти
    public static final Color MEMORY_LABEL_COLOR = new Color(114, 114, 114); // Цвет текста надписи в панели выбора памяти

    /**
     * Scrollbar
     */
    public static final String[] SCROLLBAR_IMAGES = {"scrollbar.png", "scrollbar_bg.png"}; // Картинки полосы прокрутки
    public static final int[] NEWS_SCROLL_BAR_SIZE = {12, 30}; // Размеры полосы прокрутки новостей

    /**
     * News panel
     */
    // Если включены анимированные новости
    public static final String[] NEWS_BUTTON_ICONS = {"news_button.png", "news_button.png", "news_button_disabled.png"}; //картинки кнопки новостей
    public static final int[] NEWS_BUTTON_BOUNDS = {0, 0, 50, 279}; // Координаты и размеры кнопки новостей
    public static final int NEWS_PANEL_HEIGHT_1 = 279; // Высота панели новостей
    public static final int NEWS_PANEL_WIDTH_1 = 330; // Ширина панели новостей
    public static final int TOP_SPACE_1 = 180; // Отступ от верхнего края
    public static final String NEWS_BG_IMAGE = "news_panel.png"; // Картинка фона панели новостей

    // Если выключены анимированные новости
    public static final int NEWS_PANEL_HEIGHT_2 = 305; // Высота панели новостей
    public static final int NEWS_PANEL_WIDTH_2 = 430; // Ширина панели новостей
    public static final int TOP_SPACE_2 = 180; // Отступ от верхнего края
    public static final int LEFT_SPACE = 50; // Отступ от левого края
    public static final Color NEWS_PANEL_BG_COLOR = new Color(0, 0, 0, 50); // Цвет фона пвнели новостей

    /**
     * Servers panel
     */
    public static final String SERVERS_PANEL_BACKGROUND_IMAGE = "servers_bg.png"; // Картинка панели выбора серверов
    public static final String[] SERVERS_IMAGES = {"serverinfobox.png", "serverinfobox_pressed.png"}; // Картинки панелек серверов
    public static final String[] SERVERS_BAR_IMAGES = {"serverbar_on.png", "serverbar_off.png", "serverbar_bg.png"}; // Картинки полосы загруженности серверов
    public static final float[] SERVER_FONTS_SIZE = {12.0F, 16.0F, 24.0F}; // Размеры шрифтов панели серверов
    public static final int[] SERVERS_SCROLL_PANEL_BOUNDS = {20, 65, 760, 495}; // Координаты панели с серверами в панели выбора серверов
    public static final int[] SERVERS_IN_SCROLL_PANEL_SIZE = {740, xSettingsOfTheme.SERVERS.length * 120}; // Размеры листа с серверами
    public static final int[] SERVERS_SCROLL_BAR_SIZE = {17, 479}; // Размеры полосы прокрутки панели серверов

    /**
     * Error panel
     */
    public static final int[] ERROR_PANEL_SIZE = {500, 200}; // Размеры окна ошибок (выводится в самой игре)
    public static final Color ERROR_PANEL_TEXT_COLOR = Color.red; // Цвет текста в окне ошибок
    public static final float ERROR_PANEL_TEXT_SIZE = 20.0F; // Размер текста в окне ошибок
    public static final String ERROR_PANEL_IMAGE = "errorpanel.png"; // Фон в окне ошибок
}