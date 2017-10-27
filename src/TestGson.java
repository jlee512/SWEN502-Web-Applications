import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julian on 27/10/2017.
 */
public class TestGson {

    static class User {
        String name;
        int age;
        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    public static void main(String args[]) throws Exception {
        List<String> names = new ArrayList<String>();
        names.add("a");
        names.add("b");
        names.add("c");
        System.out.println(new Gson().toJson(names));

        User u = new User("one", 20);
        String _user = new Gson().toJson(u);
        User _u = new Gson().fromJson(_user, User.class);
        System.out.println(_u.name);
    }

}
