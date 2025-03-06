package tags;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import entities.User;
import entities.UserList;
import entities.UserList.UserExistsException;
import helpers.UserListHelper;

public class AddUser extends SimpleTagSupport {
    // Поле данных для атрибута user
    private User user;

    // Метод-сеттер для установки атрибута (вызывается контейнером)
    public void setUser(User user) {
        this.user = user;
    }

    private boolean checkCaptcha(int checkId, String captcha) {
        String query;
        try {
            query = String.format("id=%s&captcha=%s", 
                 URLEncoder.encode(Integer.toString(checkId), "UTF-8"),
                 URLEncoder.encode(captcha, "UTF-8"));
                 
            URL url = new URL("http://localhost:8080/adboard/captcha" + "?" + query);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            Map<String, List<String>> header = conn.getHeaderFields();
            int responseCode = conn.getResponseCode();
            System.out.println("Headers : "+header);
            System.out.println("Response Code "+responseCode);
            DataInputStream in = new DataInputStream(conn.getInputStream());
            boolean res = in.readBoolean();
            System.out.println("CAPTCHA RESULT " + res);
            return res;
        } catch (UnsupportedEncodingException e) {
            
            e.printStackTrace();
        } catch (MalformedURLException e) {
            
            e.printStackTrace();
        } catch (ProtocolException e) {
            
            e.printStackTrace();
        } catch (IOException e) {
            
            e.printStackTrace();
        }
        return false;
    }

    public void doTag() throws JspException, IOException {
        // Изначально описание ошибки = null (т.е. ошибки нет)
        String errorMessage = null;
        // Извлечь из контекста приложения общий список пользователей
        UserList userList = (UserList) getJspContext().getAttribute("users", PageContext.APPLICATION_SCOPE);
        // Проверить, что логин не пустой
        if (user.getLogin() == null || user.getLogin().equals("")) {
            errorMessage = "Логин не может быть пустым!";
            // Проверить, что имя не пустое
        } else if (user.getName() == null || user.getName().equals("")) {
            errorMessage = "Имя пользователя не может быть пустым!";
        } else if (user.getCaptcha() == null || user.getCaptcha().equals("")) {
            errorMessage = "Заполните каптчу!";
        } else if (!checkCaptcha(user.getCheckId(), user.getCaptcha())) {
            errorMessage = "Неверное заполнена каптча! Попробуйте ещё раз.";
        }
        // Если ошибки не было - добавить пользователя
        if (errorMessage == null) {
            try {
                // Непосредственное добавление пользователя делает UserList
                userList.addUser(user);
                // Записать обновлѐнный список пользователей в файл
                UserListHelper.saveUserList(userList);
            } catch (UserExistsException e) {
                // Ошибка - пользователь с таким логином уже существует
                errorMessage = "Пользователь с таким логином уже существует!";
            }
        }
        // Сохранить описание ошибки (текст или null) в сессии
        getJspContext().setAttribute("errorMessage", errorMessage, PageContext.SESSION_SCOPE);
    }
}