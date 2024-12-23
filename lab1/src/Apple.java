package src;
public class Apple extends Food {
    private String size;
    
    public Apple(String size) {
        super("Apple");
        this.size = size;
    }

    public void consume() {
        System.out.println(this + " consumed");
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object arg0) {
        if (super.equals(arg0)) {
            if (!(arg0 instanceof Apple))
                return false;
            return size.equals(((Apple) arg0).size);
        } else
            return false;
    }

    @Override
    public String toString() {
        return super.toString() + " of size '" + size + "'";
    }

    public int calculateCalories(){
            if(size.equals("big")) {
                return 50;
            } else if(size.equals("small")){
                return 10;
            }else{
                return 20;
            }
    }
}