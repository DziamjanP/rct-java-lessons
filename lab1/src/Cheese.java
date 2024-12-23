package src;
public class Cheese extends Food {
    public Cheese() {
        super("Cheese");
    }

    public int calculateCalories(){
        return 100;
    }

    public void consume() {
        System.out.println(this + " consumed");
    }
}