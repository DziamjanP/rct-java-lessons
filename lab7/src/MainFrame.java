package src;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {
    private static final String FRAME_TITLE = "Клиент мгновенных сообщений";
    private static final int FRAME_MINIMUM_WIDTH = 500;
    private static final int FRAME_MINIMUM_HEIGHT = 500;
    private static final int FROM_FIELD_DEFAULT_COLUMNS = 10;
    private static final int TO_FIELD_DEFAULT_COLUMNS = 20;
    private static final int INCOMING_AREA_DEFAULT_ROWS = 10;
    private static final int OUTGOING_AREA_DEFAULT_ROWS = 5;
    private static final int SMALL_GAP = 5;
    private static final int MEDIUM_GAP = 10;
    private static final int LARGE_GAP = 15;
    private static final int SERVER_PORT = 4567;
    private static int SENDER_PORT = SERVER_PORT;
    private static int RECEIVER_PORT = SERVER_PORT;
    private final JTextField textFieldFrom;
    private final JTextField textFieldTo;
    private final JTextArea textAreaIncoming;
    private final JTextArea textAreaOutgoing;
    private final JLabel fileLabel;
    private final JButton fileButton;
    private JFileChooser fileChooser;
    private File selectedFile;

    public MainFrame() {
        super(FRAME_TITLE + " " + Integer.toString(RECEIVER_PORT));
        setMinimumSize(
                new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));
        // Центрирование окна
        final Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - getWidth()) / 2,
                (kit.getScreenSize().height - getHeight()) / 2);
        // Текстовая область для отображения полученных сообщений
        textAreaIncoming = new JTextArea(INCOMING_AREA_DEFAULT_ROWS, 0);
        textAreaIncoming.setEditable(false);
        // Контейнер, обеспечивающий прокрутку текстовой области
        final JScrollPane scrollPaneIncoming = new JScrollPane(textAreaIncoming);
        // Подписи полей
        final JLabel labelFrom = new JLabel("Подпись");
        final JLabel labelTo = new JLabel("Получатель");
        // Поля ввода имени пользователя и адреса получателя
        textFieldFrom = new JTextField(FROM_FIELD_DEFAULT_COLUMNS);
        textFieldTo = new JTextField(TO_FIELD_DEFAULT_COLUMNS);
        // Текстовая область для ввода сообщения
        textAreaOutgoing = new JTextArea(OUTGOING_AREA_DEFAULT_ROWS, 0);
        // Контейнер, обеспечивающий прокрутку текстовой области
        final JScrollPane scrollPaneOutgoing = new JScrollPane(textAreaOutgoing);
        // Панель ввода сообщения
        final JPanel messagePanel = new JPanel();
        messagePanel.setBorder(
                BorderFactory.createTitledBorder("Сообщение"));
        // Кнопка отправки сообщения
        final JButton sendButton = new JButton("Отправить");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        fileLabel = new JLabel("");
        fileButton = new JButton("Выбрать файл");
        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser==null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (selectedFile != null) {
                    unselectFile();
                }
                else {
                    if (fileChooser.showOpenDialog(MainFrame.this) ==
                            JFileChooser.APPROVE_OPTION) ;
                    selectFile(fileChooser.getSelectedFile());
                }
            }
        });
        // Компоновка элементов панели "Сообщение"
        final GroupLayout layout2 = new GroupLayout(messagePanel);
        messagePanel.setLayout(layout2);
        layout2.setHorizontalGroup(layout2.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout2.createParallelGroup(Alignment.TRAILING)
                        .addGroup(layout2.createSequentialGroup()
                                .addComponent(labelFrom)
                                .addGap(SMALL_GAP)
                                .addComponent(textFieldFrom)
                                .addGap(LARGE_GAP)
                                .addComponent(labelTo)
                                .addGap(SMALL_GAP)
                                .addComponent(textFieldTo))
                        .addComponent(scrollPaneOutgoing)
                        .addGroup(layout2.createSequentialGroup()
                        .addComponent(fileLabel)
                        .addGap(LARGE_GAP)
                        .addComponent(fileButton)
                        .addGap(LARGE_GAP)
                        .addComponent(sendButton)
                        ))
                .addContainerGap());
        layout2.setVerticalGroup(layout2.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout2.createParallelGroup(Alignment.BASELINE)
                        .addComponent(labelFrom)
                        .addComponent(textFieldFrom)
                        .addComponent(labelTo)
                        .addComponent(textFieldTo))
                .addGap(MEDIUM_GAP)
                .addComponent(scrollPaneOutgoing)
                .addGap(MEDIUM_GAP)
                .addGroup(layout2.createParallelGroup(Alignment.BASELINE)
                .addComponent(fileLabel)
                .addComponent(fileButton)
                .addComponent(sendButton))
                .addContainerGap());
        // Компоновка элементов фрейма
        final GroupLayout layout1 = new GroupLayout(getContentPane());
        setLayout(layout1);
        layout1.setHorizontalGroup(layout1.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout1.createParallelGroup()
                        .addComponent(scrollPaneIncoming)
                        .addComponent(messagePanel))
                .addContainerGap());
        layout1.setVerticalGroup(layout1.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPaneIncoming)
                .addGap(MEDIUM_GAP)
                .addComponent(messagePanel)
                .addContainerGap());
        // Создание и запуск потока-обработчика запросов
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try (ServerSocket serverSocket = new ServerSocket(RECEIVER_PORT)) {
                        while (!Thread.interrupted()) {
                            final Socket socket = serverSocket.accept();
                            final DataInputStream in = new DataInputStream(
                                    socket.getInputStream());
                            // Читаем имя отправителя
                            final String senderName = in.readUTF();

                            final boolean hasFile = in.readBoolean();
                            String fileStr = "";
                            if (hasFile) {
                                final String fileName = in.readUTF();
                                final int fileSize = in.readInt();
                                final byte[] fileData = in.readNBytes(fileSize);
                                DataOutputStream out = new DataOutputStream(new FileOutputStream("Downloads/"+fileName));
                                out.write(fileData);
                                out.close();
                                DecimalFormat df = new DecimalFormat("#.#");
                                fileStr = "<attached file ["
                                        + df.format((double) fileSize / 1024.0) + " KiB" + "]: "
                                        + fileName + ">\n";
                            }
                            // Читаем сообщение
                            final String message = in.readUTF();
                            // Закрываем соединение
                            socket.close();
                            // Выделяем IP-адрес
                            final String address = ((InetSocketAddress) socket
                                    .getRemoteSocketAddress())
                                    .getAddress()
                                    .getHostAddress();
                            // Выводим сообщение в текстовую область
                            textAreaIncoming.append(senderName +
                                    " (" + address + "): " +
                                    message + "\n" + fileStr);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в работе сервера", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();
    }

    private void selectFile(File file) {
        selectedFile = file;
        DecimalFormat df = new DecimalFormat("#.#");
        fileLabel.setText("Selected " + selectedFile.getName() + " " + df.format((double) selectedFile.length() / 1024.0) + " KiB");
        fileButton.setText("Отменить выбор");
    }

    private void unselectFile() {
        selectedFile = null;
        fileLabel.setText("");
        fileButton.setText("Выбрать файл");
    }

    private void sendMessage() {
        try {
            // Получаем необходимые параметры
            final String senderName = textFieldFrom.getText();
            final String destinationAddress = textFieldTo.getText().split(":")[0];
            int port = SENDER_PORT;
            if (textFieldTo.getText().split(":").length > 1)
                port = Integer.parseInt(textFieldTo.getText().split(":")[1]);
            final String message = textAreaOutgoing.getText();
            // Убеждаемся, что поля не пустые
            if (senderName.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Введите имя отправителя", "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (destinationAddress.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Введите адрес узла-получателя", "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (message.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Введите текст сообщения", "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Создаем сокет для соединения
            final Socket socket = new Socket(destinationAddress, port);
            // Открываем поток вывода данных
            final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            // Записываем в поток имя
            out.writeUTF(senderName);
            out.writeBoolean(selectedFile != null);
            if (selectedFile != null) {
                out.writeUTF(selectedFile.getName());
                out.writeInt((int) selectedFile.length());
                DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));
                out.write(in.readAllBytes());
                in.close();
            }
            // Записываем в поток сообщение
            out.writeUTF(message);
            // Закрываем сокет
            socket.close();
            // Помещаем сообщения в текстовую область вывода
            textAreaIncoming.append("Я -> " + destinationAddress + ": "
                    + message + "\n");
            if (selectedFile != null) {
                DecimalFormat df = new DecimalFormat("#.#");
                textAreaIncoming.append("<attached file ["
                + df.format((double) selectedFile.length() / 1024.0) + " KiB" + "]: "
                + selectedFile.getName() + ">\n");
                unselectFile();
            }
            // Очищаем текстовую область ввода сообщения
            textAreaOutgoing.setText("");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Не удалось отправить сообщение: узел-адресат не найден",
                    e.getMessage(), JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Не удалось отправить сообщение", "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        if (args.length >= 2){
            if (args[0].equals("-pr")){
                RECEIVER_PORT = Integer.parseInt(args[1]);
            }
            else if (args[0].equals("-ps")){
                SENDER_PORT = Integer.parseInt(args[1]);
            }
        }
        if (args.length == 4) {
            if (args[2].equals("-pr")){
                RECEIVER_PORT = Integer.parseInt(args[3]);
            }
            else if (args[2].equals("-ps")){
                SENDER_PORT = Integer.parseInt(args[3]);
            }
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final MainFrame frame = new MainFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}