package servlets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import entities.ChatMessage;
import entities.ChatUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

public class ChatServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // Карта текущих пользователей
    protected HashMap<String, ChatUser> activeUsers;
    // Список сообщений чата
    protected ArrayList<ChatMessage> messages;

    private static ArrayList<ChatServlet> liveServlets = new ArrayList<ChatServlet>();
    private static String dumpPath;

    @SuppressWarnings("unchecked")
    public void init() throws ServletException {
        // Вызвать унаследованную от HttpServlet версию init()
        super.init();
        // Извлечь из контекста карту пользователей и список сообщений
        activeUsers = (HashMap<String, ChatUser>) getServletContext().getAttribute("activeUsers");
        messages = (ArrayList<ChatMessage>) getServletContext().getAttribute("messages");
        // Если карта пользователей не определена ...
        if (activeUsers == null) {
            // Создать новую карту
            activeUsers = new HashMap<String, ChatUser>();
            // Поместить еѐ в контекст сервлета,
            // чтобы другие сервлеты могли до него добраться
            getServletContext().setAttribute("activeUsers",
                    activeUsers);
        }
        // Если список сообщений не определѐн ...
        if (messages == null) {
            // Создать новый список
            messages = new ArrayList<ChatMessage>(100);
            // Поместить его в контекст сервлета,
            // чтобы другие сервлеты могли до него добрать
            getServletContext().setAttribute("messages", messages);
        }
        if (liveServlets.isEmpty()){
            loadMessages();
        }
        liveServlets.add(this);
    }

    void loadMessages() {
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(dumpPath + "/messages.chatdata"));
            ObjectInputStream ois = new ObjectInputStream(in);
            int amount = ois.readInt();
            for (int i = 0; i < amount; i++) {
                messages.add((ChatMessage) ois.readObject());
            }
            ois.close();
            in.close();
        } catch (FileNotFoundException e) {
            // oh no
        } catch (IOException e) {
            // not cool
        } catch (ClassNotFoundException e) {
            // i can do nothing
        }
    }

    public void destroy() {
        super.destroy();
        liveServlets.remove(this);
        if (liveServlets.isEmpty()) {
            dumpData();
        }
    }

    private void dumpData() {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(dumpPath + "/messages.chatdata"));
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeInt(messages.size());
            for (int i = 0; i < messages.size(); i++) {
                oos.writeObject(messages.get(i));
            }
            oos.close();
            out.close();
        } catch (FileNotFoundException e) {
            //well thats sad
        } catch (IOException e) {
            //also sad
        }
    }
}