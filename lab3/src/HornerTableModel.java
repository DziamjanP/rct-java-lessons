package src;

import javax.swing.table.AbstractTableModel;
public class HornerTableModel extends AbstractTableModel {
    private final Double[] coefficients;
    private final Double from;
    private final Double to;
    private final Double step;
    public HornerTableModel(Double from, Double to, Double step,
                            Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }
    public Double getFrom() {
        return from;
    }
    public Double getTo() {
        return to;
    }

    public Double getStep() {
        return step;
    }

    public int getColumnCount() {
        return 4;
    }

    public int getRowCount() {
        return (int)Math.ceil((to-from)/step) + 1;
    }

    public Object getValueAt(int row, int col) {
        double x = from + step*row;
        switch (col) {
            case (0):
              return x;
            case (1): 
              return calculateHorner(x);
            case (2):
              return calculatePow(x);
            case (3):
              return Math.abs(calculatePow(x) - calculateHorner(x));
            default:
              return 0;
        }
    }
    public String getColumnName(int col) {
        switch (col) {
            case (0):
                return "X";
            case (1):
                return "Polinomial";
            case (2):
                return "Power";
            case (3):
                return "Difference";
            default: 
                return "";
        }
    }
    public Class<?> getColumnClass(int col) {
        return Double.class;
    }

    private double calculatePow(double x){
        double res = 0.;

        for (int i = 0; i < coefficients.length; i++) {
            res += coefficients[i] * Math.pow(x, i);
        }

        return res;
    }

    private double calculateHorner(double x){
        Double b = coefficients[coefficients.length-1];
        for (int i = coefficients.length-2; i >= 0; i--){
            b = b*x + coefficients[i];
        }
        return b;
    }
}

