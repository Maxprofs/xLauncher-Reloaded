package ru.xeroxp.launcher;

import java.awt.*;

class xSettingsOfTheme {
    public static final xServer[] Servers = {
            new xServer(0, "MagicWars", "s2.high-sky.ru", "27897", "client", "Forge1.6.4.jar", "1.6.4", 100, 0, 0, 724, 110, 6, 6, 180, 98, 500, 30, 200, 24, new Color(114, 114, 114), 450, 78, 70, 14, new Color(114, 114, 114), 231, 71, 484.0D, 29),
            new xServer(1, "Hitech", "localhost", "25565", "sandbox", "minecraft.jar", "1.4.7", 100, 0, 120, 724, 110, 6, 126, 180, 98, 500, 150, 200, 24, new Color(114, 114, 114), 450, 198, 70, 14, new Color(114, 114, 114), 231, 191, 484.0D, 29),
            new xServer(2, "Rpg", "localhost", "25565", "new", "minecraft.jar", "1.6.0", 100, 0, 240, 724, 110, 6, 246, 180, 98, 500, 270, 200, 24, new Color(114, 114, 114), 450, 318, 70, 14, new Color(114, 114, 114), 231, 311, 484.0D, 29),
            new xServer(3, "Sandbox", "localhost", "25565", "", "minecraft.jar", "1.4.7", 80, 0, 360, 724, 110, 6, 366, 180, 98, 500, 390, 200, 24, new Color(114, 114, 114), 450, 438, 70, 14, new Color(114, 114, 114), 231, 431, 484.0D, 29),
            new xServer(4, "Rpg", "localhost", "25565", "", "minecraft.jar", "1.4.7", 100, 0, 480, 724, 110, 6, 486, 180, 98, 500, 510, 200, 24, new Color(114, 114, 114), 450, 558, 70, 14, new Color(114, 114, 114), 231, 551, 484.0D, 29),
            new xServer(5, "Classic", "localhost", "25565", "", "minecraft.jar", "1.4.7", 100, 0, 600, 724, 110, 6, 606, 180, 98, 500, 630, 200, 24, new Color(114, 114, 114), 450, 678, 70, 14, new Color(114, 114, 114), 231, 671, 484.0D, 29),
            new xServer(6, "Hitech", "localhost", "25565", "", "minecraft.jar", "1.4.7", 100, 0, 720, 724, 110, 6, 726, 180, 98, 500, 750, 200, 24, new Color(114, 114, 114), 450, 798, 70, 14, new Color(114, 114, 114), 231, 791, 484.0D, 29),
            new xServer(7, "Classic", "localhost", "25565", "", "minecraft.jar", "1.4.7", 80, 0, 840, 724, 110, 6, 846, 180, 98, 500, 870, 200, 24, new Color(114, 114, 114), 450, 918, 70, 14, new Color(114, 114, 114), 231, 911, 484.0D, 29),
            new xServer(8, "Sandbox", "localhost", "25565", "", "minecraft.jar", "1.4.7", 100, 0, 960, 724, 110, 6, 966, 180, 98, 500, 990, 200, 24, new Color(114, 114, 114), 450, 1038, 70, 14, new Color(114, 114, 114), 231, 1031, 484.0D, 29)
    }; // id, ��������(����� � �������� ��������), ip, port, ��������, ������, ������, ������������ ���������� ������ �������, ���������� � �������, ����� �������

    public static final xLabel[] Labels = {
            new xLabel("���� ������", new Color(63, 63, 63), 593, 65, 100, 14, "http://magicwars.high-sky.ru/"),
            new xLabel("������ �������", new Color(63, 63, 63), 593, 98, 150, 14, "http://magicwars.high-sky.ru/cabinet/"),
            new xLabel("��� ������", new Color(63, 63, 63), 593, 131, 150, 14, "http://magicwars.high-sky.ru/"),
            new xLabel("� ���", new Color(63, 63, 63), 593, 164, 100, 14, "http://magicwars.high-sky.ru/")
    }; // ��������, ����, ���������� � ������� ������, ���� ������

    public static final xButton[] Buttons = {
            new xButton(0, "update_button.png", "update_button_pressed.png", "update_button_disabled.png", 730, 448, 39, 39, "UL", ""),
            new xButton(1, "auth_button.png", "auth_button_pressed.png", "auth_button.png", 545, 448, 178, 39, "JL", "JKL"),
            new xButton(2, "save_button.png", "save_button_pressed.png", "save_button.png", 730, 400, 39, 39, "RML", "")
    }; // id, ��������, �������� ��� �������, �������� ��� ����������� ������, ���������� � �������, ����������(�� ������!!!)

    public static final xCheckbox[] Checkboxes = {
            new xCheckbox("��������� ������", 573, 331, 120, 14, new Color(63, 63, 63), "checkbox.png", "checkbox_select.png", 549, 331, 16, 16, "RPL"),
            new xCheckbox("����� �������", 573, 364, 120, 14, new Color(63, 63, 63), "checkbox.png", "checkbox_select.png", 549, 364, 16, 16, "GML")
    }; // ��������(��������� ���� ������), ���������� � �������, ����, ��������, �������������� ��������, ���������� � �������, ����������(�� ������!!!)

    public static final xHeaderButton[] HeaderButtons = {
            new xHeaderButton("exit", "exit_button.png", "exit_button.png", 775, 6, 14, 14),
            new xHeaderButton("minimize", "minimize_button.png", "minimize_button.png", 745, 6, 14, 14)
    }; // ��������(�� ������!!!), ��������, �������� ��� ���������, ���������� � �������

    public static final xTextField[] Fields = {
            new xTextField("�����", new Color(114, 114, 114), 20, 560, 232, 200, 14, "login_field.png"),
            new xTextField("������", new Color(114, 114, 114), 26, 560, 281, 200, 14, "password_field.png"),
            new xTextField("������", new Color(114, 114, 114), 6, 560, 411, 120, 20, "memory_field.png")
    }; // ��������(�� ������!!!), ����, ������������ ���������� ��������, ���������� � �������, ��������

    public static final int[] LoginFieldBounds = {549, 225}; // ���������� �������� ��� ������
    public static final int[] PasswordFieldBounds = {549, 273}; // ���������� �������� ��� ������
    public static final int[] MemoryFieldBounds = {547, 405}; // ���������� �������� ��� ������

    public static final int[] LauncherSize = {800, 599}; // ������ ��������
    public static final String Favicon = "favicon.png"; // ������
    public static final String FontFile1 = "arial.ttf"; // �������� ����� ������� ������
    public static final String FontFile2 = "MyriadPro.otf"; // �������� ����� ������� ������
    public static final String ClickButtonSound = "click.wav"; // �������� ����� ����� �����
    public static final String MainPanelBackgroundImage = "launcher_bg.png"; // �������� ���� ������� ������ ��������
    public static final float[] MainFonts = {12.0F, 12.0F}; // ������� ������� � ������� ������

    public static final String Logo = "logo.png"; // ��������-����
    public static final int[] LogoBounds = {50, 70}; // ���������� ����

    public static final int[] HeaderBounds = {20, 5, 200, 14}; // ���������� ������ �� ����� (������)
    public static final Color HeaderColor = new Color(114, 114, 114); // ���� ������ �� ����� (������)

    public static final int[] ErrorLabelBounds = {485, 513, 300, 14}; // ���������� ������ ���������� � ����� (���, ��� ������� "�����������...")
    public static final Color ErrorLabelColor = new Color(63, 63, 63); // ���� ����� ������ ���������� � �����

    public static final int[] PercentLabelBounds = {360, 542, 200, 15}; // ���������� ������ � ���������� (�� ������ ��������)
    public static final Color PercentLabelColor = new Color(114, 114, 114); // ���� ������ ������ � ����������

    public static final String MemoryLabelText = "mb"; // ������� � ������ ������ ������
    public static final int[] MemoryLabelBounds = {690, 410, 20, 20}; // ���������� ������� � ������ ������ ������
    public static final Color MemoryLabelColor = new Color(114, 114, 114); // ���� ������ ������� � ������ ������ ������

    public static final String[] ScrollbarImages = {"scrollbar.png", "scrollbar_bg.png"}; // �������� ������ ���������

    public static final int[] NewsScrollBarSize = {12, 30}; // ������� ������ ��������� ��������
    // ���� �������� ������������� �������
    public static final String[] NewsButtonIcons = {"news_button.png", "news_button.png", "news_button_disabled.png"}; //�������� ������ ��������
    public static final int[] NewsButtonBounds = {0, 0, 50, 279}; // ���������� � ������� ������ ��������
    public static final int NewsPanelHeight1 = 279; // ������ ������ ��������
    public static final int NewsPanelWidth1 = 330; // ������ ������ ��������
    public static final int NewsPanelY1 = 180; // ��������� ������ �������� �� ������
    public static final String NewsBgImage = "news_panel.png"; // �������� ���� ������ ��������
    // ���� ��������� ������������� �������
    public static final int NewsPanelHeight2 = 305; // ������ ������ ��������
    public static final int NewsPanelWidth2 = 430; // ������ ������ ��������
    public static final int NewsPanelY2 = 180; // ��������� ������ �������� �� ������
    public static final int NewsPanelX2 = 50; // ��������� ������ �������� �� ������
    public static final Color NewsPanelBgColor2 = new Color(0, 0, 0, 50); // ���� ���� ������ ��������

    public static final String ServersPanelBackgroundImage = "servers_bg.png"; // �������� ������ ������ ��������
    public static final String[] ServersImages = {"serverinfobox.png", "serverinfobox_pressed.png"}; // �������� ������� ��������
    public static final String[] ServersBarImages = {"serverbar_on.png", "serverbar_off.png", "serverbar_bg.png"}; // �������� ������ ������������� ��������
    public static final float[] ServersFonts = {12.0F, 16.0F, 24.0F}; // ������� ������� ������ ��������
    public static final int[] ServersScrollPanelBounds = {20, 65, 760, 495}; // ���������� ������ � ��������� � ������ ������ ��������
    public static final int[] ServersInScrollPanelSize = {740, xSettingsOfTheme.Servers.length * 120}; // ������� ����� � ���������
    public static final int[] ServersScrollBarSize = {17, 479}; // ������� ������ ��������� ������ ��������

    public static final int[] ErrorPanelSize = {500, 200}; //������� ���� ������ (��������� � ����� ����)
    public static final Color ErrorPanelTextColor = Color.red; //���� ������ � ���� ������
    public static final float ErrorPanelTextSize = 20.0F; //������ ������ � ���� ������
    public static final String ErrorPanelImage = "errorpanel.png"; //��� � ���� ������
}