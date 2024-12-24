import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;
import java.awt.*;

class Server {
    private ServerSocket serverSocket;
    private DataInputStream serverRead;
    private DataOutputStream serverWrite;

    void runServerButton() {
        try {
            serverSocket = new ServerSocket(30000);
            Socket clientSocket = serverSocket.accept();
            CustomInterface.statusLabel.setText("Active Now");

            serverRead = new DataInputStream(clientSocket.getInputStream());
            serverWrite = new DataOutputStream(clientSocket.getOutputStream());

            new Thread(() -> {
                try {
                    String receivedMessage = "";
                    
                    while (!receivedMessage.equals("End")) {
                        receivedMessage = serverRead.readUTF();
                        int newMessageIndex = receivedMessage.indexOf("\n");
                        String newMessageString = receivedMessage.substring(0, newMessageIndex);
                        String newMessageDateString = receivedMessage.substring(newMessageIndex + 1);
                        appendMessage(
                                CustomInterface.fullName + ": " + newMessageString + "\n" + newMessageDateString + "\n",
                                true);
                    }
                } catch (IOException ex) {
                    Constant.kShowErrorException();
                }
            }).start();
        } catch (IOException ex) {
            Constant.kShowErrorException();
        }

    }

    void sendMessageButton() {
        new Thread(() -> {
            try {
                String messageToSend = CustomInterface.messageField.getText();
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                if (!messageToSend.equals("End")) {
                    appendMessage(AddDetailsInterface.fullNameField.getText() +": " + messageToSend + "\n" + sdf.format(cal.getTime()) + "\n", false);
                    serverWrite.writeUTF(messageToSend + "\n" + sdf.format(cal.getTime()));
                    serverWrite.flush();
                }
                CustomInterface.messageField.setText("");
            } catch (IOException ex) {
                Constant.kShowErrorException();
            }
        }).start();
    }

    private void appendMessage(String message, boolean isClient) {
        SwingUtilities.invokeLater(() -> {
            SimpleAttributeSet messageAttributes = new SimpleAttributeSet();
            StyleConstants.setForeground(messageAttributes, isClient ? Color.BLUE : Color.RED);
            StyleConstants.setFontSize(messageAttributes, 14); // Set font size for message

            SimpleAttributeSet dateAttributes = new SimpleAttributeSet();
            StyleConstants.setForeground(dateAttributes, Color.GRAY);
            StyleConstants.setFontSize(dateAttributes, 12); // Set font size for date

            // Create paragraph style
            StyleConstants.setAlignment(messageAttributes,
                    isClient ? StyleConstants.ALIGN_RIGHT : StyleConstants.ALIGN_LEFT);
            StyleConstants.setAlignment(dateAttributes,
                    isClient ? StyleConstants.ALIGN_RIGHT : StyleConstants.ALIGN_LEFT);
            StyleConstants.setSpaceBelow(messageAttributes, 5); // Add some space between paragraphs

            // Apply paragraph style to the message
            AttributeSet paragraphStyle = CustomInterface.messagePane.getParagraphAttributes();
            SimpleAttributeSet newParagraphStyle = new SimpleAttributeSet(paragraphStyle);
            StyleConstants.setAlignment(newParagraphStyle,
                    isClient ? StyleConstants.ALIGN_RIGHT : StyleConstants.ALIGN_LEFT);
            CustomInterface.messagePane.setParagraphAttributes(newParagraphStyle, true);

            // Split message into content and date
            String[] parts = message.split("\n");
            String content = parts[0];
            String date = parts[1];

            // Append message content to the JTextPane
            Document doc = CustomInterface.messagePane.getDocument();
            try {
                doc.insertString(doc.getLength(), content, messageAttributes);
                // Insert new line
                doc.insertString(doc.getLength(), "\n", null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }

            // Append date to the JTextPane with smaller font size
            try {
                doc.insertString(doc.getLength(), date + "\n", dateAttributes);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });

    }
}