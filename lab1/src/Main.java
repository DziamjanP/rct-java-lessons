package src;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
    static Food[] breakfast = new Food[20];
    static int breakfast_index = 0;
    static boolean sort = false;
    static boolean calories = false;

    public static void main(String[] args) {
        parseArgs(args);
        calcCalories();
        sortBreakfast();
    }

    static void parseArgs(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-calories"))
                calories = true;
            else if (args[i].equals("-sort"))
                sort = true;
            else {
                String[] argsplit = args[i].split("/");
                String classtype = argsplit[0];
                // Object[] каб прыбраць warning пры выкліку newInstance
                Object[] params = new Object[argsplit.length - 1];
                for (int j = 0; j < argsplit.length - 1; j++) {
                    params[j] = argsplit[j + 1];
                }

                try {
                    // Class<?> каб прыбраць warning аб невызначаным тыпе класса
                    Class<?> newClass = Class.forName("src." + classtype);
                    breakfast[breakfast_index] = (Food) newClass
                            .getConstructor(newClass.getConstructors()[0].getParameterTypes()).newInstance(params);
                    breakfast_index++;
                } catch (ClassNotFoundException ex) {
                    System.out.println("Class not found");
                } catch (NoSuchMethodException ex) {
                    System.out.println("Wrong class parameters");
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | SecurityException e) {
                    System.err.println("Exception! Stack trace:");
                    e.printStackTrace();
                }

            }
        }

        if (calories) {
            System.out.print("Overall calories of breakfast are: ");
            System.out.println(calcCalories());
        }

        if (sort){
            sortBreakfast();
        }

        System.out.println("Products are: ");
        for (int i = 0; i < breakfast_index; i++){
            System.out.println(breakfast[i].toString());
        }

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    static void sortBreakfast() {
        Arrays.sort(breakfast, new Comparator() {
            public int compare(Object f1, Object f2) {
                if (f1 == null)
                    return 1;
                else if (f2 == null)
                    return -1;
                else if (((Food) f1).calculateCalories() == ((Food) f2).calculateCalories())
                    return 0;
                else if (((Food) f1).calculateCalories() > ((Food) f2).calculateCalories())
                    return -1;
                return 1;
            }
        });
    }

    static int calcCalories() {
        int sum = 0;
        for (int i = 0; i < breakfast_index; i++){
            sum += breakfast[i].calculateCalories();
        }
        return sum;
    }
}