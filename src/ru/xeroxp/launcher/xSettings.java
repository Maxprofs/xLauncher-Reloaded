package ru.xeroxp.launcher;

public class xSettings {
    public static final String siteLink = "http://magicwars.high-sky.ru/"; // ������ �� ������� �������� �����
    public static final String mineFolder = "magicwars"; // ����� � ������� ��������� ��������� (.minecraft) ��� �����
    public static final String downLauncherLink = "http://localhost/launcher/"; // ���� �� �����, � ������� ����� ������� (xLauncher.jar � xLauncher.exe)
    public static final String downClientLink = "http://localhost/client/"; // ���� �� �����, � ������� �������� client.zip (� ��� �������� ��� ����� ��������)
    public static final String mainInfoFile = "http://localhost/maininfo.php"; // ���� �� ����� ����������
    public static final String LauncherName = "MagicWars Launcher"; // �������� ��������
    public static final String monitorLink = "http://localhost/data/monitor.txt"; // ������ �� ���� �����������
    public static final int checkTime = 70000; // ���������� �������� �������
    public static final String gameName = "MagicWars"; // �������� ���� ����
    public static final String launcherVersion = "1.0.0"; // ������ ��������
    public static final String[] offlineClient = {"magicwars", "Forge1.6.4.jar", "1.6.4"}; // ������, ������� ����� ����������� � ������� ������
    public static final String newsUrl = "http://localhost/MineCraft/news.html"; // ������ �� ���� � ���������
    public static final String passIdKey = "QWERTY1"; // ���� ������ ������ (����� �� ������ ���� � �������)
    public static final int localPort = 6565; // ���� �������� �������� �� �������
    public static final byte[] key = {
            0x74, 0x68, 0x68, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
    }; // ���� ������ ����������� ����� ��������� � �����-�������� (� �����-������� ������ ���� ����� ��)
    public static final boolean animatedNews = false; // ������������ ���� ��������
    public static final xServerConnect[] connectServers = {
            new xServerConnect("192.168.10.3", 4444)
    }; // id, ip ����� � port �����-��������
    public static final boolean patchDir = true; //������������ �������������� ������ ���������� ����
    public static final String mcClass = "net.minecraft.client.Minecraft";
    public static final String[] mcVersions = {
            "1.2.5::aj", "1.3.x::am", "1.4.x::an", "1.5.x::an", "1.6.x::an"
    }; // ������ � ������ ��� ���������� ����������� ����
}
