package ru.xeroxp.launcher;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.NumberFormatter;
import javax.swing.text.PlainDocument;

public class xTheme extends JPanel
{
    private static final long serialVersionUID = 1821411684598791934L;
    public BufferedImage background;
    private BufferedImage logo;
    private BufferedImage loginfield;
    private BufferedImage passfield;
    private BufferedImage memoryfield;
    private JLabel percent = new JLabel();
    private JPanel animpanel;
    private JPanel npanel;
    private JPanel bpanel;
    private JPanel newspanel;
    private JPanel gpanel;
    private JScrollPane scrollPane;
    private JButton newsButton;
    public final JButton[] buttons = new JButton[xSettingsOfTheme.Buttons.length];
    private boolean newsOpened = false;
    private JLabel error = new JLabel();
    private Pattern pattern = Pattern.compile("^[A-Za-z0-9_-]*$");
    public static boolean gameOffline = false;
    private boolean remember = false;
    private String savedPassword = null;
    private boolean lockAuth = false;
    private Clip clip = null;
    public boolean newsLoaded = true;
    private Font arial = null;
    private Font arial2 = null;
    private final JPasswordField passwordBar = new JPasswordField();
    private final JTextField loginBar = new JTextField();
    private final JTextField xSliderValue = new JTextField();
    public static xTheme theme;
    
    public xTheme()
    {
        setLayout(null);
        setMinimumSize(new Dimension(xSettingsOfTheme.LauncherSize[0], xSettingsOfTheme.LauncherSize[1]));
        setSize(xSettingsOfTheme.LauncherSize[0], xSettingsOfTheme.LauncherSize[1]);
        setBackground(new Color(0, 0, 0, 0));
        setBorder(null);
        setOpaque(false);
        
        InputStream is = xTheme.class.getResourceAsStream("/font/" + xSettingsOfTheme.FontFile1);
        try
        {
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
        try
        {
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
            if (args.length == 1) {
            } else {
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
    
    public void paintComponent(Graphics g)
    {
        g.drawImage(this.background, 0, 0, this);
        g.drawImage(this.logo, xSettingsOfTheme.LogoBounds[0], xSettingsOfTheme.LogoBounds[1], this);
        g.drawImage(this.loginfield, xSettingsOfTheme.LoginFieldBounds[0], xSettingsOfTheme.LoginFieldBounds[1], this);
        g.drawImage(this.passfield, xSettingsOfTheme.PasswordFieldBounds[0], xSettingsOfTheme.PasswordFieldBounds[1], this);
        g.drawImage(this.memoryfield, xSettingsOfTheme.MemoryFieldBounds[0], xSettingsOfTheme.MemoryFieldBounds[1], this);
    }
    
    public void updatePercent(int done) {
        if (done < 99) {
            this.percent.setText("Обновление " + done + "%");
            this.percent.setVisible(true);
        } else {
            this.percent.setVisible(false);
        }
    }
    
    public ActionListener JoinListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            xTheme.this.startAuth();
        }
    };
    
    public ActionListener RememberMemListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                public void run() {
                    String mem = xSliderValue.getText();
                    if (Integer.parseInt(mem) < 128) xAuth.rememberMemory("128");
                    else xAuth.rememberMemory(mem);
                }
            }).start();
        }
    };
    
    public KeyListener JoinKListener = new KeyListener() {
        public void keyTyped(KeyEvent e) {
        }
        public void keyReleased(KeyEvent e) {
        }
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == 10)
                xTheme.this.startAuth();
        }
    };
    
    public JScrollPane getUpdateNews()
    {
        if (scrollPane != null) return scrollPane;
        try
        {
            final JTextPane editorPane = new JTextPane()
            {
                private static final long serialVersionUID = 1L;
            };
            editorPane.setContentType ( "text/html" );
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
    
    public static void openLink(URI uri) {
        try {
            Object o = Class.forName("java.awt.Desktop").getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
            o.getClass().getMethod("browse", new Class[] { URI.class }).invoke(o, new Object[] { uri });
        } catch (Throwable e) {
            System.out.println("Failed to open link " + uri.toString());
        }
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
    
    public String readLogin() {
        xUtils utils = new xUtils();
        File dir = utils.getDirectory();
        File versionFile = new File(dir, "login");
        
        if (versionFile.exists()) {
            DataInputStream dis = null;
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
    
    public static String readMemory() {
        xUtils utils = new xUtils();
        File dir = utils.getDirectory();
        File versionFile = new File(dir, "memory");
        if (versionFile.exists()) {
            DataInputStream dis = null;
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
    
    public void lockAuth(boolean status) {
        this.lockAuth = status;
    }
    
    public void startAuth() {
        if (this.clip != null) {
            this.clip.start();
        }
        
        String login = this.loginBar.getText();
        String password = new String(this.passwordBar.getPassword());
        
        if (login.isEmpty()) {
            setError("Вы не указали логин");
            return;
        }
        
        if ((!this.newsLoaded) && (!this.gameOffline)) {
            setError("Отсутствует подключение к серверу");
            return;
        }
        
        if (this.gameOffline) {
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
    
    public void addHeaderButtons()
    {
        for (final xHeaderButton headerbutton : xHeaderButton.getButtons())
        {
            final JLabel headerbuttons = new JLabel();
            headerbuttons.setBounds(headerbutton.getImageX(), headerbutton.getImageY(), headerbutton.getImageSizeX(), headerbutton.getImageSizeY());
            headerbuttons.setIcon(new ImageIcon(xTheme.class.getResource("/images/" + headerbutton.getImage())));
            if (headerbutton.getButtonName().equals("exit")) {
                headerbuttons.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        System.exit(0);
                    }
                    public void mouseEntered(MouseEvent e) {
                        headerbuttons.setIcon(new ImageIcon(xTheme.class.getResource("/images/" + headerbutton.getOnMouseImage())));
                    }
                    public void mouseExited(MouseEvent e) {
                        headerbuttons.setIcon(new ImageIcon(xTheme.class.getResource("/images/" + headerbutton.getImage())));
                    }
                });
            }
            if (headerbutton.getButtonName().equals("minimize")) {
                headerbuttons.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        xLauncher.getLauncher().iconified();
                    }
                    public void mouseEntered(MouseEvent e) {
                        headerbuttons.setIcon(new ImageIcon(xTheme.class.getResource("/images/" + headerbutton.getOnMouseImage())));
                    }
                    public void mouseExited(MouseEvent e) {
                        headerbuttons.setIcon(new ImageIcon(xTheme.class.getResource("/images/" + headerbutton.getImage())));
                    }
                });
            }
            add(headerbuttons);
        }
    }
    
    public void addButtons()
    {
        for (final xButton button : xButton.getButtons())
        {
            buttons[button.getId()] = new JButton();
            buttons[button.getId()].setBounds(button.getImageX(), button.getImageY(), button.getImageSizeX(), button.getImageSizeY());
            buttons[button.getId()].setIcon(new ImageIcon(xTheme.class.getResource("/images/" + button.getImage())));
            buttons[button.getId()].setPressedIcon(new ImageIcon(xTheme.class.getResource("/images/" + button.getPressedImage())));
            buttons[button.getId()].setDisabledIcon(new ImageIcon(xTheme.class.getResource("/images/" + button.getDisabledImage())));
            buttons[button.getId()].setCursor(new Cursor(12));
            buttons[button.getId()].setOpaque(false);
            buttons[button.getId()].setBorder(null);
            buttons[button.getId()].setContentAreaFilled(false);
            if (button.getKeyListener().equals("JKL")) buttons[button.getId()].addKeyListener(JoinKListener);
            if (button.getActionListener().equals("JL")) buttons[button.getId()].addActionListener(JoinListener);
            if (button.getActionListener().equals("UL")) {
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
            }
            if (button.getActionListener().equals("RML")) buttons[button.getId()].addActionListener(RememberMemListener);
            add(buttons[button.getId()]);
        }
    }
    
    public void addCheckboxes()
    {
        for (final xCheckbox checkbox : xCheckbox.getCheckboxes())
        {
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
            if (checkbox.getItemListener().equals("RPL")) {
                checkboxes.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        if (checkboxes.isSelected()) {
                            xTheme.this.remember = true;
                        } else {
                            xTheme.this.remember = false;
                        }
                    }
                });
                String readFile = readLogin();
                if (readFile != null) {
                    String[] args = readFile.split(":");
                    if (args.length == 1) {
                    } else {
                        checkboxes.setSelected(true);
                    }
                }
            }
            if (checkbox.getItemListener().equals("GML")) checkboxes.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (checkboxes.isSelected()) {
                        xTheme.this.gameOffline = true;
                    } else {
                        xTheme.this.gameOffline = false;
                    }
                }
            });
            add(labels);
            add(checkboxes);
        }
    }
    
    public void addLabels()
    {
        for (final xLabel label : xLabel.getLabels())
        {
            final JLabel labels = new JLabel(label.getName().toUpperCase());
            labels.setForeground(label.getColor());
            labels.setBounds(label.getLabelX(), label.getLabelY(), label.getLabelSizeX(), label.getLabelSizeY());
            labels.setCursor(new Cursor(12));
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
    
    public void addFields()
    {
        String readFile = readLogin();
        for (final xTextField field : xTextField.getFields())
        {
            if (field.getFieldName().equals("Пароль")) {
                passwordBar.setDocument(new xTextFieldLimit(field.getFieldLimit()));
                passwordBar.setBounds(field.getFieldX(), field.getFieldY(), field.getFieldSizeX(), field.getFieldSizeY());
                passwordBar.setOpaque(false);
                passwordBar.setBorder(null);
                passwordBar.setFont(this.arial);
                passwordBar.setForeground(field.getFieldColor());
                passwordBar.setEchoChar('\u25CF');
                passwordBar.addFocusListener(new FocusListener() {
                    public void focusGained(FocusEvent e) {
                        String passToString = new String(passwordBar.getPassword());
                        if (passToString.equals("Пароль")) {
                            passwordBar.setEchoChar('\u25CF');
                            passwordBar.setText("");
                        }
                    }
                    public void focusLost(FocusEvent e) {
                        if (passwordBar.getPassword().length == 0) {
                            passwordBar.setEchoChar((char) 0);
                            passwordBar.setText("Пароль");
                        }
                    }
                });
                passwordBar.addKeyListener(JoinKListener);
                if (readFile != null) {
                    String[] args = readFile.split(":");
                    if (args.length == 1) {
                    } else {
                        passwordBar.setText("password");
                    }
                }
                if (passwordBar.getPassword().length == 0) {
                    passwordBar.setEchoChar((char) 0);
                    passwordBar.setText("Пароль");
                }
                try {
                    this.passfield = ImageIO.read(xTheme.class.getResource("/images/" + field.getImage()));
                } catch (IOException e) {
                    System.out.println("Failed load password field image");
                    System.out.println(e.getMessage());
                }
                add(passwordBar);
            } else if (field.getFieldName().equals("Логин")) {
                loginBar.setDocument(new xTextFieldLimit(field.getFieldLimit()));
                loginBar.setBounds(field.getFieldX(), field.getFieldY(), field.getFieldSizeX(), field.getFieldSizeY());
                loginBar.setOpaque(false);
                loginBar.setBorder(null);
                loginBar.setFont(this.arial);
                loginBar.setForeground(field.getFieldColor());
                loginBar.addFocusListener(new FocusListener() {
                    public void focusGained(FocusEvent e) {
                        if (loginBar.getText().equals("Логин")) loginBar.setText("");
                    }
                    public void focusLost(FocusEvent e) {
                        if (loginBar.getText().length() == 0) loginBar.setText("Логин");
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
                if (loginBar.getText().length() == 0) loginBar.setText("Логин");
                try {
                    this.loginfield = ImageIO.read(xTheme.class.getResource("/images/" + field.getImage()));
                } catch (IOException e) {
                    System.out.println("Failed load login field image");
                    System.out.println(e.getMessage());
                }
                add(loginBar);
            } else if (field.getFieldName().equals("Память")) {
                xSliderValue.setDocument(new xTextFieldLimit(field.getFieldLimit()));
                xSliderValue.setBounds(field.getFieldX(), field.getFieldY(), field.getFieldSizeX(), field.getFieldSizeY());
                xSliderValue.setOpaque(false);
                xSliderValue.setBorder(null);
                xSliderValue.setFont(this.arial);
                xSliderValue.setForeground(field.getFieldColor());
                String memory = xTheme.readMemory();
                if (memory != null) xSliderValue.setText(memory);
                if (xSliderValue.getText().length() == 0) xSliderValue.setText("512");
                xSliderValue.addFocusListener(new FocusListener() {
                    public void focusGained(FocusEvent e) {
                    }
                    public void focusLost(FocusEvent e) {
                        if (xSliderValue.getText().length() == 0) xSliderValue.setText("512");
                    }
                });
                try {
                    this.memoryfield = ImageIO.read(xTheme.class.getResource("/images/" + field.getImage()));
                } catch (IOException e) {
                    System.out.println("Failed load memory field image");
                    System.out.println(e.getMessage());
                }
                add(xSliderValue);
            }
        }
    }
    
    public void animationPanels()
    {
        animpanel = new JPanel();
        animpanel.setLayout(null);
        animpanel.setOpaque(false);
        if (xSettings.animatednews){
            bpanel = new JPanel();
            bpanel.setLayout(null);
            bpanel.setBorder(null);
            bpanel.setOpaque(false);
            bpanel.setBounds(0, 0, xSettingsOfTheme.NewsButtonBounds[2], xSettingsOfTheme.NewsPanelHeight1);
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
            newsButton.setCursor(new Cursor(12));
            animpanel.setBounds(-1, xSettingsOfTheme.NewsPanelY1, xSettingsOfTheme.LauncherSize[0] + 1, xSettingsOfTheme.NewsPanelHeight1);
            animpanel.setSize(xSettingsOfTheme.LauncherSize[0] + 1, xSettingsOfTheme.NewsPanelHeight1);
        } else {
            animpanel.setBounds(-1, xSettingsOfTheme.NewsPanelY2, xSettingsOfTheme.LauncherSize[0] + 1, xSettingsOfTheme.NewsPanelHeight2);
            animpanel.setSize(xSettingsOfTheme.LauncherSize[0] + 1, xSettingsOfTheme.NewsPanelHeight2);
        }
        animpanel.add(getNPane());
        if (xSettings.animatednews){
            bpanel.add(newsButton);
            animpanel.add(bpanel);
            newsButton.setBounds(xSettingsOfTheme.NewsButtonBounds[0], xSettingsOfTheme.NewsButtonBounds[1], xSettingsOfTheme.NewsButtonBounds[2], xSettingsOfTheme.NewsButtonBounds[3]);
            newsButton.addActionListener(new ActionListener() {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!newsOpened){
                        xAnimation anim = new xAnimation(bpanel, getNPane(), 0, animpanel, xAnimation.AnimationType.LEFT_TO_RIGHT_SLIDE);
                        anim.start();
                        newsOpened = true;
                    } else {
                        xAnimation anim2 = new xAnimation(getNPane(), bpanel, -1, animpanel, xAnimation.AnimationType.RIGHT_TO_LEFT_SLIDE);
                        anim2.start();
                        newsOpened = false;
                    }
                    new Thread (new Runnable() {
                        public void run() {
                            try {
                                newsButton.setEnabled(false);
                                while(true) {
                                    if (newsOpened) {
                                        if (npanel.getX() == -1) break;
                                        else Thread.sleep(100);
                                    } else {
                                        if (npanel.getX() == -xSettingsOfTheme.NewsPanelWidth1) break;
                                        else Thread.sleep(100);
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
        add(animpanel);
    }
    
    public void buildNPane() {
        npanel = new JPanel();
        npanel.setLayout(null);
        npanel.setOpaque(false);
        newspanel = new JPanel();
        gpanel = new JPanel();
        newspanel.setOpaque(false);
        JPanel newsbackground;
        if (xSettings.animatednews){
            getUpdateNews().setPreferredSize(new Dimension(xSettingsOfTheme.NewsPanelWidth1, xSettingsOfTheme.NewsPanelHeight1 - 25));
            gpanel.setSize(new Dimension(xSettingsOfTheme.NewsPanelWidth1, xSettingsOfTheme.NewsPanelHeight1));
            gpanel.setBackground(xSettingsOfTheme.NewsPanelBgColor2);
            newsbackground = new BgPanel();
            newsbackground.setSize(new Dimension(xSettingsOfTheme.NewsPanelWidth1, xSettingsOfTheme.NewsPanelHeight1));
            newspanel.setBounds(15, 12, xSettingsOfTheme.NewsPanelWidth1, xSettingsOfTheme.NewsPanelHeight1 - 15);
            npanel.setBounds(-xSettingsOfTheme.NewsPanelWidth1, 0, xSettingsOfTheme.NewsPanelWidth1, xSettingsOfTheme.NewsPanelHeight1);
        } else {
            getUpdateNews().setPreferredSize(new Dimension(xSettingsOfTheme.NewsPanelWidth2 - 25, xSettingsOfTheme.NewsPanelHeight2 - 25));
            gpanel.setSize(new Dimension(xSettingsOfTheme.NewsPanelWidth2, xSettingsOfTheme.NewsPanelHeight2));
            gpanel.setBackground(xSettingsOfTheme.NewsPanelBgColor2);
            newsbackground = new JPanel();
            newsbackground.setOpaque(false);
            newsbackground.setBackground(new Color(0, 0, 0, 0));
            newsbackground.setSize(new Dimension(xSettingsOfTheme.NewsPanelWidth2, xSettingsOfTheme.NewsPanelHeight2));
            newspanel.setBounds(5, 12, xSettingsOfTheme.NewsPanelWidth2, xSettingsOfTheme.NewsPanelHeight2 - 15);
            npanel.setBounds(xSettingsOfTheme.NewsPanelX2, 0, xSettingsOfTheme.NewsPanelWidth2, xSettingsOfTheme.NewsPanelHeight2);
        }
        newspanel.add(getUpdateNews());
        npanel.add(newspanel);
        npanel.add(newsbackground);
        npanel.add(gpanel);
    }
    
    public JPanel getNPane() {
        if (npanel != null)
            return npanel;
        else
            buildNPane();
        return npanel;
    }
    
    public class xTextFieldLimit extends PlainDocument {
        
        private static final long serialVersionUID = 1L;
        private int limit;
        
        public xTextFieldLimit(int limit) {
            this.limit = limit;
        }
        
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if(str != null) {
                super.insertString(offset, str, attr);
                if(this.getLength() > this.limit) {
                    super.remove(this.limit, this.getLength() - this.limit);
                }
            }
        }
    }
    
    class BgPanel extends JPanel {
        Image bg = new ImageIcon(xTheme.class.getResource("/images/" + xSettingsOfTheme.NewsBgImage)).getImage();
        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
    }
}