package ru.xeroxp.launcher.gui;

import ru.xeroxp.launcher.config.xSettings;
import ru.xeroxp.launcher.config.xThemeSettings;
import ru.xeroxp.launcher.gui.elements.*;
import ru.xeroxp.launcher.misc.xConfig;
import ru.xeroxp.launcher.misc.xDebug;
import ru.xeroxp.launcher.xAuth;
import ru.xeroxp.launcher.xLauncher;
import ru.xeroxp.launcher.xLoader;
import ru.xeroxp.launcher.xMain;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.regex.Pattern;

@SuppressWarnings("SameParameterValue")
public class xTheme extends JPanel {
    public static boolean offlineMode = false;
    public final JButton[] buttons = new JButton[xThemeSettings.BUTTONS.length];
    public final xProgressBar progressBar = new xProgressBar(this);
    private final JPasswordField passwordBar = new JPasswordField();
    private final JTextField loginBar = new JTextField();
    private final JTextField xSliderValue = new JTextField();

    private final ActionListener joinListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            xTheme.this.startAuth();
        }
    };

    private final ActionListener rememberMemListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String mem = xSliderValue.getText();

                    try {
                        xConfig config = new xConfig(xConfig.LAUNCHER);
                        config.set("memory", (Integer.parseInt(mem) < 128) ? "128" : mem);
                        xMain.restart();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    };

    private final KeyListener joinKeyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 10) {
                xTheme.this.startAuth();
            }
        }
    };

    private final JLabel error = new JLabel();
    private final Pattern pattern = Pattern.compile("^[A-Za-z0-9_-]*$");
    public BufferedImage background;
    private BufferedImage logo;
    private BufferedImage loginField;
    private BufferedImage passField;
    private BufferedImage memoryField;
    private JPanel nPanel;
    private JPanel bPanel;
    private JScrollPane scrollPane;
    private JButton newsButton;
    private boolean newsOpened = false;
    private boolean remember = false;
    private String savedPassword = null;
    private boolean lockAuth = false;
    private Font arial = null;
    private Font arial2 = null;

    public xTheme() {
        this.setLayout(null);
        this.setMinimumSize(new Dimension(xThemeSettings.LAUNCHER_SIZE[0], xThemeSettings.LAUNCHER_SIZE[1]));
        this.setSize(xThemeSettings.LAUNCHER_SIZE[0], xThemeSettings.LAUNCHER_SIZE[1]);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setBorder(null);
        this.setOpaque(false);

        InputStream is = xTheme.class.getResourceAsStream("/font/" + xThemeSettings.MAIN_FONT_FILE);

        try {
            this.arial = Font.createFont(0, is);
            this.arial = this.arial.deriveFont(0, xThemeSettings.FONTS_SIZE[0]);
            this.arial2 = this.arial.deriveFont(Font.PLAIN, xThemeSettings.FONTS_SIZE[1]);
        } catch (Exception e) {
            xDebug.errorMessage("Failed load font: " + e.getMessage());
        }

        try {
            this.background = ImageIO.read(xTheme.class.getResource("/images/" + xThemeSettings.MAIN_PANEL_BACKGROUND_IMAGE));
            this.logo = ImageIO.read(xTheme.class.getResource("/images/" + xThemeSettings.LOGO));
        } catch (IOException e) {
            xDebug.errorMessage("Failed load Theme images: " + e.getMessage());
        }

        JLabel header = new JLabel(xSettings.LAUNCHER_NAME + " v" + xMain.getVersion());
        header.setForeground(xThemeSettings.HEADER_COLOR);
        header.setBounds(xThemeSettings.HEADER_BOUNDS[0], xThemeSettings.HEADER_BOUNDS[1], xThemeSettings.HEADER_BOUNDS[2], xThemeSettings.HEADER_BOUNDS[3]);
        header.setFont(this.arial);

        this.error.setBounds(xThemeSettings.ERROR_LABEL_BOUNDS[0], xThemeSettings.ERROR_LABEL_BOUNDS[1], xThemeSettings.ERROR_LABEL_BOUNDS[2], xThemeSettings.ERROR_LABEL_BOUNDS[3]);
        this.error.setForeground(xThemeSettings.ERROR_LABEL_COLOR);
        this.error.setFont(this.arial2);
        this.error.setHorizontalTextPosition(JLabel.CENTER);
        this.error.setHorizontalAlignment(JLabel.CENTER);

        String authData = readLogin();
        if (authData != null) {
            String[] args = authData.split(":");

            if (args.length != 1) {
                this.savedPassword = args[1];
            }
        }

        JLabel mb = new JLabel(xThemeSettings.MEMORY_LABEL_TEXT);
        mb.setOpaque(false);
        mb.setBorder(null);
        mb.setBounds(xThemeSettings.MEMORY_LABEL_BOUNDS[0], xThemeSettings.MEMORY_LABEL_BOUNDS[1], xThemeSettings.MEMORY_LABEL_BOUNDS[2], xThemeSettings.MEMORY_LABEL_BOUNDS[3]);
        mb.setFont(this.arial);
        mb.setForeground(xThemeSettings.MEMORY_LABEL_COLOR);

        this.add(mb);
        xButton.loadButtons();
        this.addButtons();
        xCheckbox.loadCheckboxes();
        this.addCheckboxes();
        xTextField.loadFields();
        this.addFields();
        this.add(header);
        xLabel.loadLabels();
        this.addLabels();
        this.getUpdateNews();
        this.animationPanels();
        xHeaderButton.loadButtons();
        this.addHeaderButtons();
        this.add(this.error);
    }

    private static void openLink(URI uri) {
        try {
            /** Что это такое вообще? */
//            Object o = Class.forName("java.awt.Desktop").getMethod("getDesktop", new Class[0]).invoke(null);
//            o.getClass().getMethod("browse", new Class[]{URI.class}).invoke(o, uri);
            Desktop.getDesktop().browse(uri);
        } catch (Exception e) {
            xDebug.errorMessage("Failed to open link " + uri.toString());
        }
    }

    private static String readLogin() {
        try {
            xConfig config = new xConfig(xConfig.LAUNCHER);
            return config.get("auth");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(this.background, 0, 0, this);
        g.drawImage(this.logo, xThemeSettings.LOGO_BOUNDS[0], xThemeSettings.LOGO_BOUNDS[1], this);
        g.drawImage(this.loginField, xThemeSettings.LOGIN_FIELD_BOUNDS[0], xThemeSettings.LOGIN_FIELD_BOUNDS[1], this);
        g.drawImage(this.passField, xThemeSettings.PASSWORD_FIELD_BOUNDS[0], xThemeSettings.PASSWORD_FIELD_BOUNDS[1], this);
        g.drawImage(this.memoryField, xThemeSettings.MEMORY_FIELD_BOUNDS[0], xThemeSettings.MEMORY_FIELD_BOUNDS[1], this);
    }

    public JScrollPane getUpdateNews() {
        if (scrollPane != null) {
            return scrollPane;
        }

        try {
            final JTextPane editorPane = new JTextPane();

            editorPane.setContentType("text/html");
            editorPane.setText("<html><body><font color=\"#808080\"><br><br><br><br><center>Загрузка новостей...</center></font></body></html>");
            editorPane.addHyperlinkListener(new HyperlinkListener() {
                @Override
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
                @Override
                public void run() {
                    try {
                        editorPane.setPage(new URL(xSettings.NEWS_URL));
                    } catch (Exception e) {
                        e.printStackTrace();
                        editorPane.setText("<html><body><font color=\"#808080\"><br><br><br><br><center>Новости не доступны<br></center></font></body></html>");
                    }
                }
            }).start();

            editorPane.setOpaque(false);
            editorPane.setEditable(false);
            this.scrollPane = new JScrollPane(editorPane);
            this.scrollPane.setBorder(null);
            this.scrollPane.setOpaque(false);
            this.scrollPane.getViewport().setOpaque(false);
            JScrollBar scrollBar = new JScrollBar();
            JScrollPane scrollPane = this.scrollPane;
            scrollBar.setUI(new xScrollBar.MyScrollbarUI());
            scrollBar.setPreferredSize(new Dimension(xThemeSettings.NEWS_SCROLL_BAR_SIZE[0], xThemeSettings.NEWS_SCROLL_BAR_SIZE[1]));
            scrollBar.setBackground(new Color(0, 0, 0, 0));
            scrollBar.setForeground(new Color(0, 0, 0, 0));
            scrollBar.setOpaque(false);
            scrollPane.setVerticalScrollBar(scrollBar);
            editorPane.setMargin(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scrollPane;
    }

    public void setAuth(String text) {
        this.error.setText(text);
        xDebug.infoMessage(text);
    }

    public void setError(String text) {
        this.error.setText(text);
        xDebug.errorMessage(text);
        setLockAuth(false);
    }

    public boolean getRemember() {
        return this.remember;
    }

    public void setLockAuth(boolean status) {
        this.lockAuth = status;
    }

    private void startAuth() {
        String login = this.loginBar.getText();
        String password = new String(this.passwordBar.getPassword());

        if (login.isEmpty()) {
            setError("Вы не указали логин");
            return;
        }

        if (offlineMode) {
            xLauncher.getIntsanse().drawMinecraft(login);
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

            setLockAuth(true);

            if ((this.savedPassword != null) && (password.equals("password"))) {
                Thread authThread = new Thread(new xAuth(login, this, this.savedPassword));
                authThread.start();
            } else {
                Thread authThread = new Thread(new xAuth(login, password, this));
                authThread.start();
            }
        }
    }

    private void addHeaderButtons() {
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
                        xLauncher.getIntsanse().iconified();
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

    private void addButtons() {
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
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    xMain.xWebThread.updater.checkClientsUpdate(true);
                                    revalidate();
                                    repaint();
                                }
                            }).start();
                        }
                    });
                    break;
                case xButton.AUTH_ID:
                    buttons[button.getId()].addKeyListener(joinKeyListener);
                    buttons[button.getId()].addActionListener(joinListener);
                    break;
                case xButton.RAM_ID:
                    buttons[button.getId()].addActionListener(rememberMemListener);
                    break;
            }

            add(buttons[button.getId()]);
        }
    }

    private void addCheckboxes() {
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
                    @Override
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
                @Override
                public void itemStateChanged(ItemEvent e) {
                    offlineMode = checkboxes.isSelected();
                }
            });

            add(labels);
            add(checkboxes);
        }
    }

    private void addLabels() {
        for (final xLabel label : xLabel.getLabels()) {
            final JLabel labels = new JLabel(label.getName().toUpperCase());
            labels.setForeground(label.getColor());
            labels.setBounds(label.getLabelX(), label.getLabelY(), label.getLabelSizeX(), label.getLabelSizeY());
            labels.setCursor(new Cursor(Cursor.HAND_CURSOR));
            labels.setFont(this.arial2);
            labels.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI(label.getLabelLink()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            add(labels);
        }
    }

    private void addFields() {
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

                passwordBar.addKeyListener(joinKeyListener);
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
                    xDebug.errorMessage("Failed load password field image: " + e.getMessage());
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

                loginBar.addKeyListener(joinKeyListener);
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
                    xDebug.errorMessage("Failed load login field image: " + e.getMessage());
                }

                add(loginBar);
            } else if (field.getId() == xTextField.RAM_ID) {
                xSliderValue.setDocument(new xTextFieldLimit(field.getFieldLimit()));
                xSliderValue.setBounds(field.getFieldX(), field.getFieldY(), field.getFieldSizeX(), field.getFieldSizeY());
                xSliderValue.setOpaque(false);
                xSliderValue.setBorder(null);
                xSliderValue.setFont(this.arial);
                xSliderValue.setForeground(field.getFieldColor());
                String memory = xLoader.getMemory();

                if (memory != null) {
                    xSliderValue.setText(memory);
                }

                if (xSliderValue.getText().isEmpty()) {
                    xSliderValue.setText("512");
                }

                xSliderValue.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        if (xSliderValue.getText().length() == 0) xSliderValue.setText("512");
                    }
                });

                try {
                    this.memoryField = ImageIO.read(xTheme.class.getResource("/images/" + field.getImage()));
                } catch (IOException e) {
                    xDebug.errorMessage("Failed load memory field image: " + e.getMessage());
                }

                add(xSliderValue);
            }
        }
    }

    private void animationPanels() {
        JPanel animPanel = new JPanel();
        animPanel.setLayout(null);
        animPanel.setOpaque(false);

        if (xSettings.ANIMATED_NEWS) {
            bPanel = new JPanel();
            bPanel.setLayout(null);
            bPanel.setBorder(null);
            bPanel.setOpaque(false);
            bPanel.setBounds(0, 0, xThemeSettings.NEWS_BUTTON_BOUNDS[2], xThemeSettings.NEWS_PANEL_HEIGHT_1);
            newsButton = new JButton();
            newsButton.setIcon(new ImageIcon(xTheme.class.getResource("/images/" + xThemeSettings.NEWS_BUTTON_ICONS[0])));
            newsButton.setPressedIcon(new ImageIcon(xTheme.class.getResource("/images/" + xThemeSettings.NEWS_BUTTON_ICONS[1])));
            newsButton.setDisabledIcon(new ImageIcon(xTheme.class.getResource("/images/" + xThemeSettings.NEWS_BUTTON_ICONS[2])));
            newsButton.setSize(new Dimension(xThemeSettings.NEWS_BUTTON_BOUNDS[2], xThemeSettings.NEWS_BUTTON_BOUNDS[3]));
            newsButton.setOpaque(false);
            newsButton.setBackground(new Color(0, 0, 0, 0));
            newsButton.setFocusPainted(false);
            newsButton.setBorder(null);
            newsButton.setContentAreaFilled(false);
            newsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            animPanel.setBounds(-1, xThemeSettings.TOP_SPACE_1, xThemeSettings.LAUNCHER_SIZE[0] + 1, xThemeSettings.NEWS_PANEL_HEIGHT_1);
            animPanel.setSize(xThemeSettings.LAUNCHER_SIZE[0] + 1, xThemeSettings.NEWS_PANEL_HEIGHT_1);
        } else {
            animPanel.setBounds(-1, xThemeSettings.TOP_SPACE_2, xThemeSettings.LAUNCHER_SIZE[0] + 1, xThemeSettings.NEWS_PANEL_HEIGHT_2);
            animPanel.setSize(xThemeSettings.LAUNCHER_SIZE[0] + 1, xThemeSettings.NEWS_PANEL_HEIGHT_2);
        }

        animPanel.add(getNPanel());

        if (xSettings.ANIMATED_NEWS) {
            bPanel.add(newsButton);
            animPanel.add(bPanel);
            newsButton.setBounds(xThemeSettings.NEWS_BUTTON_BOUNDS[0], xThemeSettings.NEWS_BUTTON_BOUNDS[1], xThemeSettings.NEWS_BUTTON_BOUNDS[2], xThemeSettings.NEWS_BUTTON_BOUNDS[3]);
            newsButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!newsOpened) {
                        xAnimation anim = new xAnimation(bPanel, getNPanel(), 0, xAnimation.AnimationType.LEFT_TO_RIGHT_SLIDE);
                        anim.start();
                        newsOpened = true;
                    } else {
                        xAnimation anim2 = new xAnimation(getNPanel(), bPanel, -1, xAnimation.AnimationType.RIGHT_TO_LEFT_SLIDE);
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
                                        if (nPanel.getX() == -xThemeSettings.NEWS_PANEL_WIDTH_1) {
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

    private void buildNPane() {
        nPanel = new JPanel();
        nPanel.setLayout(null);
        nPanel.setOpaque(false);
        JPanel newsPanel = new JPanel();
        JPanel gPanel = new JPanel();
        newsPanel.setOpaque(false);
        JPanel newsBackground;

        if (xSettings.ANIMATED_NEWS) {
            getUpdateNews().setPreferredSize(new Dimension(xThemeSettings.NEWS_PANEL_WIDTH_1, xThemeSettings.NEWS_PANEL_HEIGHT_1 - 25));
            gPanel.setSize(new Dimension(xThemeSettings.NEWS_PANEL_WIDTH_1, xThemeSettings.NEWS_PANEL_HEIGHT_1));
            gPanel.setBackground(xThemeSettings.NEWS_PANEL_BG_COLOR);
            newsBackground = new BgPanel();
            newsBackground.setSize(new Dimension(xThemeSettings.NEWS_PANEL_WIDTH_1, xThemeSettings.NEWS_PANEL_HEIGHT_1));
            newsPanel.setBounds(15, 12, xThemeSettings.NEWS_PANEL_WIDTH_1, xThemeSettings.NEWS_PANEL_HEIGHT_1 - 15);
            nPanel.setBounds(-xThemeSettings.NEWS_PANEL_WIDTH_1, 0, xThemeSettings.NEWS_PANEL_WIDTH_1, xThemeSettings.NEWS_PANEL_HEIGHT_1);
        } else {
            getUpdateNews().setPreferredSize(new Dimension(xThemeSettings.NEWS_PANEL_WIDTH_2 - 25, xThemeSettings.NEWS_PANEL_HEIGHT_2 - 25));
            gPanel.setSize(new Dimension(xThemeSettings.NEWS_PANEL_WIDTH_2, xThemeSettings.NEWS_PANEL_HEIGHT_2));
            gPanel.setBackground(xThemeSettings.NEWS_PANEL_BG_COLOR);
            newsBackground = new JPanel();
            newsBackground.setOpaque(false);
            newsBackground.setBackground(new Color(0, 0, 0, 0));
            newsBackground.setSize(new Dimension(xThemeSettings.NEWS_PANEL_WIDTH_2, xThemeSettings.NEWS_PANEL_HEIGHT_2));
            newsPanel.setBounds(5, 12, xThemeSettings.NEWS_PANEL_WIDTH_2, xThemeSettings.NEWS_PANEL_HEIGHT_2 - 15);
            nPanel.setBounds(xThemeSettings.LEFT_SPACE, 0, xThemeSettings.NEWS_PANEL_WIDTH_2, xThemeSettings.NEWS_PANEL_HEIGHT_2);
        }

        newsPanel.add(getUpdateNews());
        nPanel.add(newsPanel);
        nPanel.add(newsBackground);
        nPanel.add(gPanel);
    }

    private JPanel getNPanel() {
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
        final Image bg = new ImageIcon(xTheme.class.getResource("/images/" + xThemeSettings.NEWS_BG_IMAGE)).getImage();

        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
    }
}