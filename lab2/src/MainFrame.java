package src;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.log;
import static java.lang.Math.log10;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import java.awt.*;
import java.awt.image.BufferedImage;


public class MainFrame extends JFrame {

    private int activeMemoryCell = 0;

    private int activeFormula = 1;

    private double memoryCells[] = new double[3];

    private JFrame mainFrame = new JFrame();

    private JLabel resultLabel = new JLabel();

    private JLabel formulaImageLabel = new JLabel();

    private JLabel memoryTextLabel = new JLabel("MEMORY:");

    private JLabel memoryTextLabel1 = new JLabel("0");
    private JLabel memoryTextLabel2 = new JLabel("0");
    private JLabel memoryTextLabel3 = new JLabel("0");

    private JLabel formulaTextLabel = new JLabel("Formula:");

    private JButton buttonClearVariables = new JButton("Clear");

    private static final int WIDTH = 600;
    private static final int HEIGHT = 320;

    MainFrame(){
        super("Разлік формул");
        
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        
        setLocation((kit.getScreenSize().width - WIDTH)/2,
        (kit.getScreenSize().height - HEIGHT)/2);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth() * 480 / 1920;
        int height = gd.getDisplayMode().getHeight() * 400 / 1080;


        mainFrame.setSize(width, height);

        JRadioButton rbMem1 = new JRadioButton("1");
        JRadioButton rbMem2 = new JRadioButton("2");
        JRadioButton rbMem3 = new JRadioButton("3");

        rbMem1.setSelected(true);

        rbMem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.this.activeMemoryCell = 0;
            }
        });

        rbMem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.this.activeMemoryCell = 1;
            }
        });

        rbMem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.this.activeMemoryCell = 2;
            }
        });

        ButtonGroup memButtonGroup = new ButtonGroup();
        memButtonGroup.add(rbMem1);
        memButtonGroup.add(rbMem2);
        memButtonGroup.add(rbMem3);

        JButton buttonMemoryPlus = new JButton("M+");
        buttonMemoryPlus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int activeCell = MainFrame.this.activeMemoryCell;
                memoryCells[activeCell] = Double.parseDouble(resultLabel.getText());
                updateMemoryLabels();
            }
        });

        JButton buttonMemoryClear = new JButton("MC");
        buttonMemoryClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int activeCell = MainFrame.this.activeMemoryCell;
                memoryCells[activeCell] = 0;
                updateMemoryLabels();

            }
        });


        JRadioButton rbFormula1 = new JRadioButton("1");
        JRadioButton rbFormula2 = new JRadioButton("2");

        rbFormula1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.this.activeFormula = 1;
                drawFormula("https://latex.codecogs.com/png.image?F(x,y,z)=(\\ln(1+x)^2+\\cos\\pi%20z^3)^{\\sin{y}}+(e^{x^{2}}+\\cos(e^{z})+\\sqrt{\\frac{1}{y}})^\\frac{1}{x}");
            }
        });

        rbFormula2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.this.activeFormula = 2;
                drawFormula("https://latex.codecogs.com/png.image?F(x,y,z)=y\\cdot\\frac{x^2}{\\lg{z^y}+\\cos^2{\\sqrt[3]{x}}}");
            }
        });

        rbFormula1.setSelected(true);

        ButtonGroup formulaButtonGroup = new ButtonGroup();
        formulaButtonGroup.add(rbFormula1);
        formulaButtonGroup.add(rbFormula2);

        JTextField textVariableX = new JTextField("0", 6);
        JTextField textVariableY = new JTextField("0", 6);
        JTextField textVariableZ = new JTextField("0", 6);

        JButton buttonCalculate = new JButton("Calculate");

        buttonCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Double result = null;
                System.out.println(MainFrame.this.activeFormula);
                double x = Double.parseDouble(textVariableX.getText());
                double y = Double.parseDouble(textVariableY.getText());
                double z = Double.parseDouble(textVariableZ.getText());
                if (MainFrame.this.activeFormula == 1) {
                    result = calculateFirstFormula(x, y, z);
                }
                else if (MainFrame.this.activeFormula == 2) {
                    result = calculateSecondFormula(x, y, z);
                }
                System.out.println(result);

                MainFrame.this.resultLabel.setText(result.toString());
            }
        });

        buttonClearVariables.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textVariableX.setText("0");
                textVariableY.setText("0");
                textVariableZ.setText("0");
                resultLabel.setText("0");
            }
        });


        Box hboxFormulaChoice = Box.createHorizontalBox();
        hboxFormulaChoice.add(Box.createHorizontalGlue());
        hboxFormulaChoice.add(formulaTextLabel);
        hboxFormulaChoice.add(rbFormula1);
        hboxFormulaChoice.add(rbFormula2);
        hboxFormulaChoice.add(Box.createHorizontalGlue());

        Box hboxFormulaImage = Box.createHorizontalBox();
        hboxFormulaImage.add(Box.createHorizontalGlue());
        hboxFormulaImage.add(Box.createVerticalStrut(80));
        hboxFormulaImage.add(formulaImageLabel);
        hboxFormulaImage.add(Box.createVerticalStrut(80));
        hboxFormulaImage.add(Box.createHorizontalGlue());

        Box hboxMemory = Box.createHorizontalBox();
        hboxMemory.add(Box.createHorizontalGlue());
        hboxMemory.add(memoryTextLabel);
        hboxMemory.add(rbMem1);
        hboxMemory.add(rbMem2);
        hboxMemory.add(rbMem3);
        hboxMemory.add(buttonMemoryPlus);
        hboxMemory.add(buttonMemoryClear);
        hboxMemory.add(Box.createHorizontalGlue());

        Box hboxMemoryLabels = Box.createHorizontalBox();
        hboxMemoryLabels.add(Box.createHorizontalGlue());
        hboxMemoryLabels.add(memoryTextLabel1);
        hboxMemoryLabels.add(Box.createHorizontalStrut(20));
        hboxMemoryLabels.add(memoryTextLabel2);
        hboxMemoryLabels.add(Box.createHorizontalStrut(20));
        hboxMemoryLabels.add(memoryTextLabel3);
        hboxMemoryLabels.add(Box.createHorizontalGlue());
        hboxMemoryLabels.setMaximumSize(new Dimension(width, 30));
        
        Box hboxVariables = Box.createHorizontalBox();
        hboxVariables.add(Box.createHorizontalStrut(width/5));
        Box hboxFunc = Box.createHorizontalBox();

        hboxFunc.add(new JLabel("F( "));
        hboxFunc.add(textVariableX);
        hboxFunc.add(new JLabel(" ; "));
        hboxFunc.add(textVariableY);
        hboxFunc.add(new JLabel(" ; "));
        hboxFunc.add(textVariableZ);
        hboxFunc.add(new JLabel(") = "));
        hboxFunc.setMaximumSize(new Dimension(30,20));

        hboxVariables.add(hboxFunc);
        hboxVariables.add(Box.createHorizontalStrut(3));
        hboxVariables.add(resultLabel);
        hboxVariables.setMaximumSize(new Dimension(1000,30));
        hboxVariables.add(Box.createHorizontalGlue());


        Box hboxCalculate = Box.createHorizontalBox();
        hboxCalculate.add(buttonClearVariables);
        hboxCalculate.add(Box.createHorizontalGlue());
        hboxCalculate.add(buttonCalculate);
        hboxCalculate.setMaximumSize(new Dimension(500,30));


        Box contentBox = Box.createVerticalBox();
        contentBox.add(hboxFormulaChoice);
        contentBox.add(hboxFormulaImage);
        contentBox.add(hboxMemory);
        contentBox.add(hboxMemoryLabels);
        contentBox.add(Box.createVerticalStrut(10));
        contentBox.add(hboxVariables);
        contentBox.add(Box.createVerticalStrut(10));
        contentBox.add(hboxCalculate);
        contentBox.add(Box.createVerticalGlue());

        getContentPane().add(contentBox);
    }

    private Double calculateFirstFormula(Double x, Double y, Double z) {
        if (x == 0 || y == 0 || y > 1) {
            return null;
        }
        return pow(log(pow(1 + x, 2)) + cos(PI * pow(z, 3)), sin(y)) +
                pow(pow(java.lang.Math.E, x * x) + cos(pow(java.lang.Math.E, z)) + sqrt(1.f / y), 1.f / x);
    }

    private Double calculateSecondFormula(Double x, Double y, Double z) {
        if (y == 0 || x == -1) {
            return null;
        }

        double divider = (log10(pow(z, y)) + pow(cos(pow(x, 1.f / 3.f)), 2));
        if (divider == 0) {
            return null;
        }

        return y * x * x / divider;
    }

    private void updateMemoryLabels() {
        memoryTextLabel1.setText(Double.toString(memoryCells[0]));
        memoryTextLabel2.setText(Double.toString(memoryCells[1]));
        memoryTextLabel3.setText(Double.toString(memoryCells[2]));
    }

    private void drawFormula(String strUrl) {
        URL url = null;
        try {
            url = new URL(strUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        BufferedImage image = null;
        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        formulaImageLabel.setIcon(new ImageIcon(image));
    }

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.drawFormula("https://latex.codecogs.com/png.image?F(x,y,z)=(\\ln(1+x)^2+\\cos\\pi%20z^3)^{\\sin{y}}+(e^{x^{2}}+\\cos(e^{z})+\\sqrt{\\frac{1}{y}})^\\frac{1}{x}");
        mainFrame.setVisible(true);
    }
}