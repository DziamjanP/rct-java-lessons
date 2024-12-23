package src;
public class Sandwich extends Food{
    String name;

    String filling1;
    String filling2;

    public Sandwich(String filling1, String filling2){
        super("Sandwich");
        this.filling1 = filling1;
        this.filling2 = filling2;
    }

    @Override
    public String toString(){
        return super.toString() + " with fillings " + filling1 + ", " + filling2;
    }

    public void consume(){
        System.out.println(this + " consumed");
    }

    private int calcFillingCal(String filling){
        if (filling == "ham")
            return 100;
        else if (filling == "chicken")
            return 150;
        else if (filling == "cheese")
            return 30;
        else if (filling == "salad")
            return 10;
        else if (filling == "mayo")
            return 130;
        else 
            return 50;
    }

    public int calculateCalories(){
        return calcFillingCal(filling1) + calcFillingCal(filling2);
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (!(obj instanceof Sandwich)) return false;
        
        if (((Sandwich) obj).name.length() == name.length())
            return name.equals(((Sandwich) obj).name);
        return ((Sandwich) obj).name.length() > name.length();
    }

}