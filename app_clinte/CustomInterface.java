import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import java.awt.*;

public class CustomInterface extends JFrame {
    static String fullName;
    static JLabel statusLabel;
    static JTextField messageField;
    private JButton sendMessageButton;
    private JButton ServerConnectButton;
    static JTextPane messagePane;
    private JLabel imageLabel;

    public void showInterface() {
        Client client = new Client();
        setTitle(Constant.kAppName);
        setSize(450, 450);
        getContentPane().setBackground(Color.white);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // center form
        setLocationRelativeTo(null);
        setResizable(false);

        // Create components
        messagePane = new JTextPane();
        messageField = new JTextField(20);
        sendMessageButton = new JButton("Send message");
        ServerConnectButton = new JButton("Connect to the server");

        Font font = new Font("Arial", Font.PLAIN, 18);

        messagePane.setFont(font);

        JScrollPane scrollPane = new JScrollPane(messagePane);

        sendMessageButton.setBackground(Color.GREEN);
        sendMessageButton.setForeground(Color.white);
        ServerConnectButton.setForeground(Color.white);
        ServerConnectButton.setBackground(Color.red);

        JPanel messagePanel = new JPanel(new BorderLayout());

        sendMessageButton.setMargin(new Insets(10, 10, 10, 10));
        ServerConnectButton.setMargin(new Insets(10, 10, 10, 10));
        messageField.setMargin(new Insets(10, 10, 10, 10));

        // Set layout manager to BorderLayout
        setLayout(new BorderLayout());

        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendMessageButton, BorderLayout.EAST);
        messagePanel.add(ServerConnectButton, BorderLayout.SOUTH);

        // Add components to the frame
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(messagePanel, BorderLayout.SOUTH);

        ServerConnectButton.addActionListener(e -> {
            ServerConnectButton.setBackground(Color.blue);
            ServerConnectButton.setEnabled(false);
            client.ConnectToTheServer();
        });

        sendMessageButton.addActionListener(e -> {
            client.sendMessageButton();
        });
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        JPanel textPanel = new JPanel();

        // Set the layout of textPanel to BoxLayout vertically
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        ImageIcon imageIcon = new ImageIcon(Constant.kDefulteCircleAvatar);

        // Resize the image
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(60, 60, Image.SCALE_SMOOTH);

        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

        imageLabel = new JLabel(scaledImageIcon);
        JLabel textLabel = new JLabel(fullName);
        statusLabel = new JLabel("No active");

        // Set margins
        imageLabel.setBorder(new EmptyBorder(5, 10, 5, 5));
        textLabel.setBorder(new EmptyBorder(15, 10, 0, 0));
        statusLabel.setBorder(new EmptyBorder(7, 10, 0, 0));

        // Add textLabel and statusLabel to textPanel
        textPanel.add(textLabel);
        textPanel.add(statusLabel);

        // Add imageLabel and textPanel to headerPanel
        headerPanel.add(imageLabel, BorderLayout.WEST);
        headerPanel.add(textPanel, BorderLayout.CENTER);
        return headerPanel;
    }

    public void setProfileImage(ImageIcon imageIcon) {
        // Resize the image
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(60, 60, Image.SCALE_SMOOTH);

        // Create a circular mask for the image
        BufferedImage bufferedImage = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new Ellipse2D.Float(0, 0, 60, 60));
        g2.drawImage(scaledImage, 0, 0, 60, 60, null);
        g2.dispose();

        ImageIcon scaledImageIcon = new ImageIcon(bufferedImage);
        imageLabel.setIcon(scaledImageIcon);
    }
}
