package helpers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import entities.UserList;
import jakarta.servlet.ServletContext;

public abstract class UserListHelper {
    // Относительный путь к файлу, в котором хранятся данные о пользователях
    private static final String USERS_FILENAME = "WEB-INF/users.dat";
    // Полный путь к файлу, в котором хранятся данные о пользователях
    private static String USERS_PATH = null;

    // Читает данные пользователей из файла хранилища и формирует на их основе
    // объект UserList.
    public static UserList readUserList(ServletContext context) {
        try {
            // Определяем физический путь к файлу
            USERS_PATH = context.getRealPath(USERS_FILENAME);
            // Создаем объектный поток ввода на основе файлового потока
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(USERS_PATH));
            UserList userList = (UserList) in.readObject();
            in.close();
            return userList;
        } catch (Exception e) {
            // Если возникли проблемы с чтением из файла, возвращаем пустой список
            return new UserList();
        }
    }

    // Сохраняет в файле хранилища содержимое списка пользователей
    public static void saveUserList(UserList users) {
        // Путь к файлу с данными уже находится в переменной USERS_PATH
        // Она была инициализирована при загрузке данных в процессе инициализации
        // приложения
        synchronized (users) {
            try {
                // Создаем объектный поток вывода на основе файлового потока
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USERS_PATH));
                // Записываем содержимое объекта в поток
                out.writeObject(users);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}