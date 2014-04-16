package ru.xeroxp.launcher;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

import static ru.xeroxp.launcher.xUtils.getDirectory;

@SuppressWarnings("SameParameterValue")
public class xTheme extends JPanel {
    public static boolean gameOffline = false;
    public final JButton[] buttons = new JButton[xSettingsOfTheme.Buttons.length];
    private final JPasswordField passwordBar = new JPasswordField();
    private final JTextField loginBar = new JTextField();
    private final JTextField xSliderValue = new JTextField();
    public BufferedImage background;
    private final ActionListener JoinListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            xTheme.this.startAuth();
        }
    };
    private final ActionListener RememberMemListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                public void run() {
                    String mem = xSliderValue.getText();
                    xAuth.rememberMemory((Integer.parseInt(mem) < 128) ? "128" : mem);
                }
            }).start();
        }
    };

    private final KeyListener JoinKListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 10) {
                xTheme.this.startAuth();
            }
        }
    };

    private BufferedImage logo;
    private BufferedImage loginField;
    private BufferedImage passField;
    private BufferedImage memoryField;
    private final JLabel percent = new JLabel();
    private JPanel nPanel;
    private JPanel bPanel;
    private JScrollPane scrollPane;
    private JButton newsButton;
    private boolean newsOpened = false;
    private final JLabel error = new JLabel();
    private final Pattern pattern = Pattern.compile("^[A-Za-z0-9_-]*$");
    private boolean remember = false;
    private String savedPassword = null;
    private boolean lockAuth = false;
    private Clip clip = null;
    private Font arial = null;
    private Font arial2 = null;

    public xTheme() {
        setLayout(null);
        setMinimumSize(new Dimension(xSettingsOfTheme.LauncherSize[0], xSettingsOfTheme.LauncherSize[1]));
        setSize(xSettingsOfTheme.LauncherSize[0], xSettingsOfTheme.LauncherSize[1]);
        setBackground(new Color(0, 0, 0, 0));
        setBorder(null);
        setOpaque(false);

        InputStream is = xTheme.class.getResourceAsStream("/font/" + xSettingsOfTheme.FontFile1);

        try {
            this.arial = Font.createFont(0, is);
            this.arial = this.arial.deriveFont(0, xSettingsOfTheme.MainFonts[0]);
            this.arial2 = this.arial.deriveFont(Font.PLAIN, xSettingsOfTheme.MainFonts[1]);
        } catch (FontFormatException e2) {
            System.out.println("Failed load font");
            System.out.println(e2.getMessage());
        } catch (IOException e2) {
            System.out.println("Failed load font");
            System.out.println(e2.getMessage());
        }

        try {
            this.background = ImageIO.read(xTheme.class.getResource("/images/" + xSettingsOfTheme.MainPanelBackgroundImage));
            this.logo = ImageIO.read(xTheme.class.getResource("/images/" + xSettingsOfTheme.Logo));
        } catch (IOException e) {
            System.out.println("Failed load Theme images");
            System.out.println(e.getMessage());
        }

        if (xLauncher.getLauncher().getSound()) {
            try {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(xTheme.class.getResource("/sound/" + xSettingsOfTheme.ClickButtonSound));
                this.clip = AudioSystem.getClip();
                this.clip.open(audioIn);
            } catch (UnsupportedAudioFileException e2) {
                System.out.println("Unsupported Audio Format");
                System.out.println(e2.getMessage());
            } catch (IOException e2) {
                System.out.println("Failed load sound");
                System.out.println(e2.getMessage());
            } catch (LineUnavailableException e) {
                System.out.println("Failed load clip");
                System.out.println(e.getMessage());
            }
        }

        JLabel header = new JLabel(xSettings.LauncherName + " v" + xMain.getVersion());
        header.setForeground(xSettingsOfTheme.HeaderColor);
        header.setBounds(xSettingsOfTheme.HeaderBounds[0], xSettingsOfTheme.HeaderBounds[1], xSettingsOfTheme.HeaderBounds[2], xSettingsOfTheme.HeaderBounds[3]);
        header.setFont(this.arial);

        this.percent.setBounds(xSettingsOfTheme.PercentLabelBounds[0], xSettingsOfTheme.PercentLabelBounds[1], xSettingsOfTheme.PercentLabelBounds[2], xSettingsOfTheme.PercentLabelBounds[3]);
        this.percent.setForeground(xSettingsOfTheme.PercentLabelColor);

        this.error.setBounds(xSettingsOfTheme.ErrorLabelBounds[0], xSettingsOfTheme.ErrorLabelBounds[1], xSettingsOfTheme.ErrorLabelBounds[2], xSettingsOfTheme.ErrorLabelBounds[3]);
        this.error.setForeground(xSettingsOfTheme.ErrorLabelColor);
        this.error.setFont(this.arial2);
        this.error.setHorizontalTextPosition(JLabel.CENTER);
        this.error.setHorizontalAlignment(JLabel.CENTER);

        String readFile = readLogin();

        if (readFile != null) {
            String[] args = readFile.split(":");
            if (args.length != 1) {
                this.savedPassword = args[1];
            }
        }

        JLabel mb = new JLabel(xSettingsOfTheme.MemoryLabelText);
        mb.setOpaque(false);
        mb.setBorder(null);
        mb.setBounds(xSettingsOfTheme.MemoryLabelBounds[0], xSettingsOfTheme.MemoryLabelBounds[1], xSettingsOfTheme.MemoryLabelBounds[2], xSettingsOfTheme.MemoryLabelBounds[3]);
        mb.setFont(this.arial);
        mb.setForeground(xSettingsOfTheme.MemoryLabelColor);

        add(mb);
        xButton.loadButtons();
        addButtons();
        xCheckbox.loadCheckboxes();
        addCheckboxes();
        xTextField.loadFields();
        addFields();
        add(header);
        xLabel.loadLabels();
        addLabels();
        getUpdateNews();
        animationPanels();
        xHeaderButton.loadButtons();
        addHeaderButtons();
        add(this.percent);
        add(this.error);
    }

    private static void openLink(URI uri) {
        try {
            Object o = Class.forName("java.awt.Desktop").getMethod("getDesktop", new Class[0]).invoke(null);
            o.getClass().getMethod("browse", new Class[]{URI.class}).invoke(o, uri);
        } catch (Throwable e) {
            System.out.println("Failed to open link " + uri.toString());
        }
    }

    public static String readMemory() {
        File dir = getDirectory();
        File versionFile = new File(dir, "memory");

        if (versionFile.exists()) {
            DataInputStream dis;
            try {
                dis = new DataInputStream(new FileInputStream(versionFile));
                String readMemory = dis.readUTF();
                dis.close();

                return readMemory;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(this.background, 0, 0, this);
        g.drawImage(this.logo, xSettingsOfTheme.LogoBounds[0], xSettingsOfTheme.LogoBounds[1], this);
        g.drawImage(this.loginField, xSettingsOfTheme.LoginFieldBounds[0], xSettingsOfTheme.LoginFieldBounds[1], this);
        g.drawImage(this.passField, xSettingsOfTheme.PasswordFieldBounds[0], xSettingsOfTheme.PasswordFieldBounds[1], this);
        g.drawImage(this.memoryField, xSettingsOfTheme.MemoryFieldBounds[0], xSettingsOfTheme.MemoryFieldBounds[1], this);
    }

    public void updatePercent(int done) {
        if (done < 99) {
            this.percent.setText("Обновление " + done + "%");
            this.percent.setVisible(true);
        } else {
            this.percent.setVisible(false);
        }
    }

    public JScrollPane getUpdateNews() {
        if (scrollPane != null) {
            return scrollPane;
        }

        try {
            final JTextPane editorPane = new JTextPane();

            editorPane.setContentType("text/html");
            editorPane.setText("<html><body><font color=\"#808080\"><br><br><br><br><center>Loading update news..</center></font></body></html>");
            editorPane.addHyperlinkListener(new HyperlinkListener() {
                public void hyperlinkUpdate(HyperlinkEvent he) {
                    if (he.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                        try {
                            openLink(he.getURL().toURI());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            });

            new Thread(new Runnable() {
                public void run() {
                    try {
                        editorPane.setPage(new URL(xSettings.newsUrl));
                    } catch (Exception e) {
                        e.printStackTrace();
                        editorPane.setText("<html><body><font color=\"#808080\"><br><br><br><br><center>Failed to update news<br></center></font></body></html>");
                    }
                }
            }).start();

            editorPane.setOpaque(false);
            editorPane.setEditable(false);
            scrollPane = new JScrollPane(editorPane);
            scrollPane.setBorder(null);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            JScrollBar s_bar = new JScrollBar();
            JScrollPane sp = this.scrollPane;
            s_bar.setUI(new xScrollBar.MyScrollbarUI());
            Dimension dim = new Dimension(xSettingsOfTheme.NewsScrollBarSize[0], xSettingsOfTheme.NewsScrollBarSize[1]);
            s_bar.setPreferredSize(dim);
            s_bar.setBackground(new Color(0, 0, 0, 0));
            s_bar.setForeground(new Color(0, 0, 0, 0));
            s_bar.setOpaque(false);
            sp.setVerticalScrollBar(s_bar);
            editorPane.setMargin(null);
        } catch (Exception e2) {
            e2.printStackTrace();
        }

        return scrollPane;
    }

    public void setAuth(String text) {
        this.error.setText(text);
        System.out.println(text);
    }

    public void setError(String text) {
        this.error.setText(text);
        System.out.println(text);
        lockAuth(false);
    }

    public boolean getRemember() {
        return this.remember;
    }

    String readLogin() {
        File dir = getDirectory();
        File versionFile = new File(dir, "login");

        if (versionFile.exists()) {
            DataInputStream dis;
            try {
                dis = new DataInputStream(new FileInputStream(versionFile));
                String readLogin = dis.readUTF();
                dis.close();

                return readLogin;
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void lockAuth(boolean status) {
        this.lockAuth = status;
    }

    void startAuth() {
        if (this.clip != null) {
            this.clip.start();
        }

        String login = this.loginBar.getText();
        String password = new String(this.passwordBar.getPassword());

        if (login.isEmpty()) {
            setError("Вы не указали логин");
            return;
        }

        if (gameOffline) {
            xLauncher.getLauncher().drawMinecraft(login);
        } else if (!this.lockAuth) {
            if (password.isEmpty()) {
                setError("Вы не указали пароль");
                return;
            }

            if (!this.pattern.matcher(login).matches()) {
                setError("Недопустимый логин");
                return;
            }

            if (!this.pattern.matcher(password).matches()) {
                setError("Недопустимый пароль");
                return;
            }

            lockAuth(true);

            if ((this.savedPassword != null) && (password.equals("password"))) {
                Thread authThread = new Thread(new xAuth(login, this, this.savedPassword));
                authThread.start();
            } else {
                Thread authThread = new Thread(new xAuth(login, password, this));
                authThread.start();
            }
        }
    }

    void addHeaderButtons() {
        for (final xHeaderButton headerButton : xHeaderButton.getButtons()) {
            final JLabel headerButtons = new JLabel();
            headerButtons.setBounds(headerButton.getImageX(), headerButton.getImageY(), headerButton.getImageSizeX(), headerButton.getImageSizeY());
            headerButtons.setIcon(new ImageIcon(xTheme.class.getResource("/images/" + headerButton.getImage())));
            headerButtons.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (headerButton.getButtonName().equals("exit")) {
                        System.exit(0);
                    } else if (headerButton.getButtonName().equals("minimize")) {
                        xLauncher.getLauncher().iconified();
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    headerButtons.setIcon(new ImageIcon(xTheme.class.getResource("/images/" + headerButton.getOnMouseImage())));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    headerButtons.setIcon(new ImageIcon(xTheme.class.getResource("/images/" + headerButton.getImage())));
                }
            });

            add(headerButtons);
        }
    }

    void addButtons() {
        for (final xButton button : xButton.getButtons()) {

            buttons[button.getId()] = new JButton();
            buttons[button.getId()].setBounds(button.getImageX(), button.getImageY(), button.getImageSizeX(), button.getImageSizeY());
            buttons[button.getId()].setIcon(new ImageIcon(xTheme.class.getResource("/images/" + button.getImage())));
            buttons[button.getId()].setPressedIcon(new ImageIcon(xTheme.class.getResource("/images/" + button.getPressedImage())));
            buttons[button.getId()].setDisabledIcon(new ImageIcon(xTheme.class.getResource("/images/" + button.getDisabledImage())));
            buttons[button.getId()].setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttons[button.getId()].setOpaque(false);
            buttons[button.getId()].setBorder(null);
            buttons[button.getId()].setContentAreaFilled(false);

            switch (button.getId()) {
                case xButton.UPDATE_ID:
                    buttons[button.getId()].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                public void run() {
                                    xMain.xWebThread.updater.checkClientUpdate(true);
                                    revalidate();
                                    repaint();
                                }
                            }).start();
                        }
                    });
                    break;
                case xButton.AUTH_ID:
                    buttons[button.getId()].addKeyListener(JoinKListener);
                    buttons[button.getId()].addActionListener(JoinListener);
                    break;
                case xButton.RAM_ID:
                    buttons[button.getId()].addActionListener(RememberMemListener);
                    break;
            }

            add(buttons[button.getId()]);
        }
    }

    void addCheckboxes() {
        for (final xCheckbox checkbox : xCheckbox.getCheckboxes()) {
            final JLabel labels = new JLabel(checkbox.getCheckboxLabel());
            labels.setBounds(checkbox.getLabelX(), checkbox.getLabelY(), checkbox.getLabelSizeX(), checkbox.getLabelSizeY());
            labels.setForeground(checkbox.getLabelColor());
            labels.setFont(this.arial2);
            final JCheckBox checkboxes = new JCheckBox();
            checkboxes.setBounds(checkbox.getImageX(), checkbox.getImageY(), checkbox.getImageSizeX(), checkbox.getImageSizeY());
            checkboxes.setContentAreaFilled(false);
            checkboxes.setBackground(new Color(0, 0, 0, 0));
            checkboxes.setFocusPainted(false);
            checkboxes.setBorder(null);
            checkboxes.setOpaque(false);
            checkboxes.setIcon(new ImageIcon(xTheme.class.getResource("/images/" + checkbox.getImage())));
            checkboxes.setSelectedIcon(new ImageIcon(xTheme.class.getResource("/images/" + checkbox.getSelectedImage())));

            if (checkbox.getId() == xCheckbox.REMEMBER_PASS_ID) {
                checkboxes.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        xTheme.this.remember = checkboxes.isSelected();
                    }
                });

                String readFile = readLogin();

                if (readFile != null) {
                    String[] args = readFile.split(":");

                    if (args.length != 1) {
                        checkboxes.setSelected(true);
                    }
                }
            } else if (checkbox.getId() == xCheckbox.OFFLINE_MODE_ID) checkboxes.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    gameOffline = checkboxes.isSelected();
                }
            });

            add(labels);
            add(checkboxes);
        }
    }

    void addLabels() {
        for (final xLabel label : xLabel.getLabels()) {
            final JLabel labels = new JLabel(label.getName().toUpperCase());
            labels.setForeground(label.getColor());
            labels.setBounds(label.getLabelX(), label.getLabelY(), label.getLabelSizeX(), label.getLabelSizeY());
            labels.setCursor(new Cursor(Cursor.HAND_CURSOR));
            labels.setFont(this.arial2);
            labels.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    try {
                        try {
                            Desktop.getDesktop().browse(new URI(label.getLabelLink()));
                        } catch (URISyntaxException e1) {
                            e1.printStackTrace();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            add(labels);
        }
    }

    void addFields() {
        String readFile = readLogin();
        for (final xTextField field : xTextField.getFields()) {
            if (field.getId() == xTextField.PASS_ID) {
                passwordBar.setDocument(new xTextFieldLimit(field.getFieldLimit()));
                passwordBar.setBounds(field.getFieldX(), field.getFieldY(), field.getFieldSizeX(), field.getFieldSizeY());
                passwordBar.setOpaque(false);
                passwordBar.setBorder(null);
                passwordBar.setFont(this.arial);
                passwordBar.setForeground(field.getFieldColor());
                passwordBar.setEchoChar('\u25CF');
                passwordBar.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        String passToString = new String(passwordBar.getPassword());
                        if (passToString.equals(field.getFieldName())) {
                            passwordBar.setEchoChar('\u25CF');
                            passwordBar.setText("");
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if (passwordBar.getPassword().length == 0) {
                            passwordBar.setEchoChar((char) 0);
                            passwordBar.setText(field.getFieldName());
                        }
                    }
                });

                passwordBar.addKeyListener(JoinKListener);
                if (readFile != null) {
                    String[] args = readFile.split(":");
                    if (args.length != 1) {
                        passwordBar.setText("password");
                    }
                }

                if (passwordBar.getPassword().length == 0) {
                    passwordBar.setEchoChar((char) 0);
                    passwordBar.setText(field.getFieldName());
                }

                try {
                    this.passField = ImageIO.read(xTheme.class.getResource("/images/" + field.getImage()));
                } catch (IOException e) {
                    System.out.println("Failed load password field image");
                    System.out.println(e.getMessage());
                }

                add(passwordBar);
            } else if (field.getId() == xTextField.LOGIN_ID) {
                loginBar.setDocument(new xTextFieldLimit(field.getFieldLimit()));
                loginBar.setBounds(field.getFieldX(), field.getFieldY(), field.getFieldSizeX(), field.getFieldSizeY());
                loginBar.setOpaque(false);
                loginBar.setBorder(null);
                loginBar.setFont(this.arial);
                loginBar.setForeground(field.getFieldColor());
                loginBar.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if (loginBar.getText().equals(field.getFieldName())) loginBar.setText("");
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if (loginBar.getText().length() == 0) loginBar.setText(field.getFieldName());
                    }
                });

                loginBar.addKeyListener(JoinKListener);
                if (readFile != null) {
                    String[] args = readFile.split(":");

                    if (args.length == 1) {
                        loginBar.setText(args[0]);
                    } else {
                        loginBar.setText(args[0]);
                    }
                }

                if (loginBar.getText().length() == 0) {
                    loginBar.setText(field.getFieldName());
                }

                try {
                    this.loginField = ImageIO.read(xTheme.class.getResource("/images/" + field.getImage()));
                } catch (IOException e) {
                    System.out.println("Failed load login field image");
                    System.out.println(e.getMessage());
                }

                add(loginBar);
            } else if (field.getId() == xTextField.RAM_ID) {
                xSliderValue.setDocument(new xTextFieldLimit(field.getFieldLimit()));
                xSliderValue.setBounds(field.getFieldX(), field.getFieldY(), field.getFieldSizeX(), field.getFieldSizeY());
                xSliderValue.setOpaque(false);
                xSliderValue.setBorder(null);
                xSliderValue.setFont(this.arial);
                xSliderValue.setForeground(field.getFieldColor());
                String memory = xTheme.readMemory();

                if (memory != null) {
                    xSliderValue.setText(memory);
                }

                if (xSliderValue.getText().length() == 0) {
                    xSliderValue.setText("512");
                }

                xSliderValue.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if (xSliderValue.getText().length() == 0) xSliderValue.setText("512");
                    }
                });

                try {
                    this.memoryField = ImageIO.read(xTheme.class.getResource("/images/" + field.getImage()));
                } catch (IOException e) {
                    System.out.println("Failed load memory field image");
                    System.out.println(e.getMessage());
                }

                add(xSliderValue);
            }
        }
    }

    void animationPanels() {
        JPanel animPanel = new JPanel();
        animPanel.setLayout(null);
        animPanel.setOpaque(false);

        if (xSettings.animatedNews) {
            bPanel = new JPanel();
            bPanel.setLayout(null);
            bPanel.setBorder(null);
            bPanel.setOpaque(false);
            bPanel.setBounds(0, 0, xSettingsOfTheme.NewsButtonBounds[2], xSettingsOfTheme.NewsPanelHeight1);
            newsButton = new JButton();
            newsButton.setIcon(new ImageIcon(xTheme.class.getResource("/images/" + xSettingsOfTheme.NewsButtonIcons[0])));
            newsButton.setPressedIcon(new ImageIcon(xTheme.class.getResource("/images/" + xSettingsOfTheme.NewsButtonIcons[1])));
            newsButton.setDisabledIcon(new ImageIcon(xTheme.class.getResource("/images/" + xSettingsOfTheme.NewsButtonIcons[2])));
            newsButton.setSize(new Dimension(xSettingsOfTheme.NewsButtonBounds[2], xSettingsOfTheme.NewsButtonBounds[3]));
            newsButton.setOpaque(false);
            newsButton.setBackground(new Color(0, 0, 0, 0));
            newsButton.setFocusPainted(false);
            newsButton.setBorder(null);
            newsButton.setContentAreaFilled(false);
            newsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            animPanel.setBounds(-1, xSettingsOfTheme.NewsPanelY1, xSettingsOfTheme.LauncherSize[0] + 1, xSettingsOfTheme.NewsPanelHeight1);
            animPanel.setSize(xSettingsOfTheme.LauncherSize[0] + 1, xSettingsOfTheme.NewsPanelHeight1);
        } else {
            animPanel.setBounds(-1, xSettingsOfTheme.NewsPanelY2, xSettingsOfTheme.LauncherSize[0] + 1, xSettingsOfTheme.NewsPanelHeight2);
            animPanel.setSize(xSettingsOfTheme.LauncherSize[0] + 1, xSettingsOfTheme.NewsPanelHeight2);
        }

        animPanel.add(getNPane());

        if (xSettings.animatedNews) {
            bPanel.add(newsButton);
            animPanel.add(bPanel);
            newsButton.setBounds(xSettingsOfTheme.NewsButtonBounds[0], xSettingsOfTheme.NewsButtonBounds[1], xSettingsOfTheme.NewsButtonBounds[2], xSettingsOfTheme.NewsButtonBounds[3]);
            newsButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!newsOpened) {
                        xAnimation anim = new xAnimation(bPanel, getNPane(), 0, xAnimation.AnimationType.LEFT_TO_RIGHT_SLIDE);
                        anim.start();
                        newsOpened = true;
                    } else {
                        xAnimation anim2 = new xAnimation(getNPane(), bPanel, -1, xAnimation.AnimationType.RIGHT_TO_LEFT_SLIDE);
                        anim2.start();
                        newsOpened = false;
                    }
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                newsButton.setEnabled(false);

                                while (true) {
                                    if (newsOpened) {
                                        if (nPanel.getX() == -1) {
                                            break;
                                        }

                                        Thread.sleep(100);
                                    } else {
                                        if (nPanel.getX() == -xSettingsOfTheme.NewsPanelWidth1) {
                                            break;
                                        }

                                        Thread.sleep(100);
                                    }
                                }

                                newsButton.setEnabled(true);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
        }

        add(animPanel);
    }

    void buildNPane() {
        nPanel = new JPanel();
        nPanel.setLayout(null);
        nPanel.setOpaque(false);
        JPanel newsPanel = new JPanel();
        JPanel gPanel = new JPanel();
        newsPanel.setOpaque(false);
        JPanel newsBackground;

        if (xSettings.animatedNews) {
            getUpdateNews().setPreferredSize(new Dimension(xSettingsOfTheme.NewsPanelWidth1, xSettingsOfTheme.NewsPanelHeight1 - 25));
            gPanel.setSize(new Dimension(xSettingsOfTheme.NewsPanelWidth1, xSettingsOfTheme.NewsPanelHeight1));
            gPanel.setBackground(xSettingsOfTheme.NewsPanelBgColor2);
            newsBackground = new BgPanel();
            newsBackground.setSize(new Dimension(xSettingsOfTheme.NewsPanelWidth1, xSettingsOfTheme.NewsPanelHeight1));
            newsPanel.setBounds(15, 12, xSettingsOfTheme.NewsPanelWidth1, xSettingsOfTheme.NewsPanelHeight1 - 15);
            nPanel.setBounds(-xSettingsOfTheme.NewsPanelWidth1, 0, xSettingsOfTheme.NewsPanelWidth1, xSettingsOfTheme.NewsPanelHeight1);
        } else {
            getUpdateNews().setPreferredSize(new Dimension(xSettingsOfTheme.NewsPanelWidth2 - 25, xSettingsOfTheme.NewsPanelHeight2 - 25));
            gPanel.setSize(new Dimension(xSettingsOfTheme.NewsPanelWidth2, xSettingsOfTheme.NewsPanelHeight2));
            gPanel.setBackground(xSettingsOfTheme.NewsPanelBgColor2);
            newsBackground = new JPanel();
            newsBackground.setOpaque(false);
            newsBackground.setBackground(new Color(0, 0, 0, 0));
            newsBackground.setSize(new Dimension(xSettingsOfTheme.NewsPanelWidth2, xSettingsOfTheme.NewsPanelHeight2));
            newsPanel.setBounds(5, 12, xSettingsOfTheme.NewsPanelWidth2, xSettingsOfTheme.NewsPanelHeight2 - 15);
            nPanel.setBounds(xSettingsOfTheme.NewsPanelX2, 0, xSettingsOfTheme.NewsPanelWidth2, xSettingsOfTheme.NewsPanelHeight2);
        }

        newsPanel.add(getUpdateNews());
        nPanel.add(newsPanel);
        nPanel.add(newsBackground);
        nPanel.add(gPanel);
    }

    JPanel getNPane() {
        if (nPanel == null) {
            buildNPane();
        }

        return nPanel;
    }

    public class xTextFieldLimit extends PlainDocument {
        private final int limit;

        public xTextFieldLimit(int limit) {
            this.limit = limit;
        }

        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str != null) {
                super.insertString(offset, str, attr);

                if (this.getLength() > this.limit) {
                    super.remove(this.limit, this.getLength() - this.limit);
                }
            }
        }
    }

    class BgPanel extends JPanel {
        final Image bg = new ImageIcon(xTheme.class.getResource("/images/" + xSettingsOfTheme.NewsBgImage)).getImage();

        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
    }
}