import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    static double x1 = 0, y1 = 0, x2 = 0, y2 = 0, x3 = 0, y3 = 0, x4 = 0, y4 = 0, l1 = 100, l2 = 150, m1 = 20, m2 = 10, g = 0.05,
            a1 = 0, a2 = 0, a3 = a1, a4 = a2, x0 = 300, y0 = 300, p1 = 0, p2 = 0, p3 = p1, p4 = p2;

    static int width = 960, height = 750;

    static ArrayList<Double> xs1 = new ArrayList<>(), ys1 = new ArrayList<>(), xs2 = new ArrayList<>(), ys2 = new ArrayList<>();

    static JFrame frame = new JFrame("КТПрВП лабораторная 2");

    static JButton startButton = new JButton("применить");

    static JLabel
            angle1 = new JLabel("угол 1 в градусах"),
            angle2 = new JLabel("угол 2 в градусах"),
            len1 = new JLabel("длина 1 плеча"),
            len2 = new JLabel("длина 2 плеча"),
            mass1 = new JLabel("масса 1 груза"),
            mass2 = new JLabel("масса 2 груза"),
            pulse1 = new JLabel("импульс 1 груза"),
            pulse2 = new JLabel("импульс 2 груза");

    static JTextField
            angle1Field = new JTextField("0"),
            angle2Field = new JTextField("0"),
            len1Field = new JTextField("100"),
            len2Field = new JTextField("150"),
            mass1Field = new JTextField("20"),
            mass2Field = new JTextField("10"),
            pulse1Field = new JTextField("0"),
            pulse2Field = new JTextField("0");


    public static void main(String[] args) {
        if (args.length == 2) {
            width = Integer.parseInt(args[0]);
            height = Integer.parseInt(args[1]);
        }
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(1, 1));
        frame.setVisible(true);

        angle1.setBounds(width - 250, 0, 120, 30);
        angle2.setBounds(width - 250, 30, 120, 30);
        len1.setBounds(width - 250, 60, 120, 30);
        len2.setBounds(width - 250, 90, 120, 30);
        mass1.setBounds(width - 250, 120, 120, 30);
        mass2.setBounds(width - 250, 150, 120, 30);
        pulse1.setBounds(width - 250, 180, 120, 30);
        pulse2.setBounds(width - 250, 210, 120, 30);

        angle1Field.setBounds(width - 135, 0, 120, 30);
        angle2Field.setBounds(width - 135, 30, 120, 30);
        len1Field.setBounds(width - 135, 60, 120, 30);
        len2Field.setBounds(width - 135, 90, 120, 30);
        mass1Field.setBounds(width - 135, 120, 120, 30);
        mass2Field.setBounds(width - 135, 150, 120, 30);
        pulse1Field.setBounds(width - 135, 180, 120, 30);
        pulse2Field.setBounds(width - 135, 210, 120, 30);

        startButton.setBounds(width - 135, 240, 120, 30);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    a1 = Double.parseDouble(angle1Field.getText());
                    a1 /= 180;
                    a1 *= Math.PI;
                    a3 = a1;
                    a2 = Double.parseDouble(angle2Field.getText());
                    a2 /= 180;
                    a2 *= Math.PI;
                    a4 = a2;
                    m1 = Double.parseDouble(mass1Field.getText());
                    m2 = Double.parseDouble(mass2Field.getText());
                    l1 = Double.parseDouble(len1Field.getText());
                    l2 = Double.parseDouble(len2Field.getText());
                    p1 = Double.parseDouble(pulse1Field.getText());
                    p3 = p1;
                    p2 = Double.parseDouble(pulse2Field.getText());
                    p4 = p2;
                    if (m1 <= 0 || m2 <= 0 || l1 <= 0 || l2 <= 0) {
                        throw new Exception();
                    }
                    xs1.clear();
                    ys1.clear();
                    xs2.clear();
                    ys2.clear();
                } catch (Exception ex) {}
            }
        });


        frame.add(angle1);
        frame.add(angle2);
        frame.add(len1);
        frame.add(len2);
        frame.add(mass1);
        frame.add(mass2);
        frame.add(pulse1);
        frame.add(pulse2);

        frame.add(angle1Field);
        frame.add(angle2Field);
        frame.add(len1Field);
        frame.add(len2Field);
        frame.add(mass1Field);
        frame.add(mass2Field);
        frame.add(pulse1Field);
        frame.add(pulse2Field);

        frame.add(startButton);
        frame.getContentPane();
        MyTimerTask timerTask = new MyTimerTask();
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 10);
    }

    public static void update(JFrame frame) {

        double at1, at2, pt1, pt2;
        at1 = (p1 * l2 - p2 * l1 * Math.cos(a1 - a2)) / (l1 * l1 * l2 * (m1 + m2 * Math.pow(Math.sin(a1 - a2), 2)));
        at2 = (p2 * (m1 + m2) * l1 - p1 * m2 * l2 * Math.cos(a1 - a2)) / (m2 * l1 * l2 * l2 * (m1 + m2 * Math.pow(Math.sin(a1 - a2), 2)));
        pt1 = (-(m1 + m2) * g * l1 * Math.sin(a1) - (p1 * p2 * Math.sin(a1 - a2)) / (l1 * l2 * (m1 + m2 * Math.pow(Math.sin(a1 - a2), 2))))
                + (((p1 * p1 * m2 * l2 * l2 - 2 * p1 * p2 * m2 * l1 * l2 * Math.cos(a1 - a2) + p2 * p2 * (m1 + m2) * l1 * l1) * Math.sin(2 * (a1 - a2)))
                / (2 * l1 * l1 * l2 * l2 * Math.pow(m1 + m2 * Math.pow(Math.sin(a1 - a2), 2), 2)));
        pt2 = (-m2 * g * l2 * Math.sin(a2)) + (p1 * p2 * Math.sin(a1 - a2)) / (l1 * l2 * (m1 + m2 * Math.pow(Math.sin(a1 - a2), 2)))
                - (((p1 * p1 * m2 * l2 * l2 - 2 * p1 * p2 * m2 * l1 * l2 * Math.cos(a1 - a2) + p2 * p2 * (m1 + m2) * l1 * l1) * Math.sin(2 * (a1 - a2)))
                / (2 * l1 * l1 * l2 * l2 * Math.pow(m1 + m2 * Math.pow(Math.sin(a1 - a2), 2), 2)));
        a1 += at1;
        a2 += at2;
        p1 += pt1;
        p2 += pt2;
        x1 = l1 * Math.sin(a1);
        y1 = l1 * Math.cos(a1);
        x2 = x1 + l2 * Math.sin(a2);
        y2 = y1 + l2 * Math.cos(a2);
        xs1.add(x2);
        ys1.add(y2);

        a3 += (p3 * l2 - p4 * l1 * Math.cos(a3 - a4)) / (l1 * l1 * l2 * (m1 + m2 * Math.pow(Math.sin(a3 - a4), 2)));
        a4 += (p4 * (m1 + m2) * l1 - p3 * m2 * l2 * Math.cos(a3 - a4)) / (m2 * l1 * l2 * l2 * (m1 + m2 * Math.pow(Math.sin(a3 - a4), 2)));
        p3 += (-(m1 + m2) * g * l1 * Math.sin(a3) - (p3 * p4 * Math.sin(a3 - a4)) / (l1 * l2 * (m1 + m2 * Math.pow(Math.sin(a3 - a4), 2))))
                + (((p3 * p3 * m2 * l2 * l2 - 2 * p3 * p4 * m2 * l1 * l2 * Math.cos(a3 - a4) + p4 * p4 * (m1 + m2) * l1 * l1) * Math.sin(2 * (a3 - a4)))
                / (2 * l1 * l1 * l2 * l2 * Math.pow(m1 + m2 * Math.pow(Math.sin(a3 - a4), 2), 2)));
        p4 += (-m2 * g * l2 * Math.sin(a4)) + (p3 * p4 * Math.sin(a3 - a4)) / (l1 * l2 * (m1 + m2 * Math.pow(Math.sin(a3 - a4), 2)))
                - (((p3 * p3 * m2 * l2 * l2 - 2 * p3 * p4 * m2 * l1 * l2 * Math.cos(a3 - a4) + p4 * p4 * (m1 + m2) * l1 * l1) * Math.sin(2 * (a3 - a4)))
                / (2 * l1 * l1 * l2 * l2 * Math.pow(m1 + m2 * Math.pow(Math.sin(a3 - a4), 2), 2)));
        x3 = l1 * Math.sin(a3);
        x4 = x3 + l2 * Math.sin(a4);
        y3 = l1 * Math.cos(a3);
        y4 = y3 + l2 * Math.cos(a4);
        xs2.add(x4);
        ys2.add(y4);

        if (xs1.size() > 20000) {
            xs1.removeAll(xs1.subList(0, 10000));
            ys1.removeAll(ys1.subList(0, 10000));
            xs2.removeAll(xs2.subList(0, 10000));
            ys2.removeAll(ys2.subList(0, 10000));
        }

        frame.add(new Plane());
        frame.repaint();
    }

}


class MyTimerTask extends TimerTask {

    @Override
    public void run() {
        Main.update(Main.frame);
    }
}

class Plane extends JComponent {
    public void paintComponent(Graphics painter) {
        double s1 = 30 * Main.m1 / (Main.m1 + Main.m2), s2 = 30 * Main.m2 / (Main.m1 + Main.m2);
        painter.setColor(Color.MAGENTA);
        painter.drawString("явный ментод Эйлера", 20, 20);
        for (int i = Math.max(1, Main.xs1.size() - 10000); i < Main.xs1.size(); i++) {
            painter.drawLine((int) (Main.x0 + Main.xs1.get(i - 1)), (int) (Main.y0 + Main.ys1.get(i - 1)),
                    (int) (Main.x0 + Main.xs1.get(i)), (int) (Main.y0 + Main.ys1.get(i)));
        }
        painter.setColor(Color.ORANGE);
        painter.drawString("полуявный ментод Эйлера-Кромера", 20, 40);
        for (int i = Math.max(1, Main.xs1.size() - 10000); i < Main.xs1.size(); i++) {
            painter.drawLine((int) (Main.x0 + Main.xs2.get(i - 1)), (int) (Main.y0 + Main.ys2.get(i - 1)),
                    (int) (Main.x0 + Main.xs2.get(i)), (int) (Main.y0 + Main.ys2.get(i)));
        }
        painter.setColor(Color.BLACK);
        painter.fillOval((int) Main.x0 - 5, (int) Main.y0 - 5, 10, 10);
        painter.setColor(Color.BLUE);
        painter.drawLine((int) Main.x0, (int) Main.y0, (int) (Main.x1 + Main.x0), (int) (Main.y1 + Main.y0));
        painter.fillOval((int) (Main.x1 + Main.x0 - s1 / 2), (int) (Main.y1 + Main.y0 - s1 / 2), (int) s1, (int) s1);
        painter.setColor(Color.CYAN);
        painter.drawLine((int) Main.x0, (int) Main.y0, (int) (Main.x3 + Main.x0), (int) (Main.y3 + Main.y0));
        painter.fillOval((int) (Main.x3 + Main.x0 - s1 / 2), (int) (Main.y3 + Main.y0 - s1 / 2), (int) s1, (int) s1);
        painter.setColor(Color.RED);
        painter.drawLine((int) (Main.x1 + Main.x0), (int) (Main.y1 + Main.y0), (int) (Main.x2 + Main.x0), (int) (Main.y2 + Main.y0));
        painter.fillOval((int) (Main.x2 + Main.x0 - s2 / 2), (int) (Main.y2 + Main.y0 - s2 / 2), (int) s2, (int) s2);
        painter.setColor(Color.PINK);
        painter.drawLine((int) (Main.x3 + Main.x0), (int) (Main.y3 + Main.y0), (int) (Main.x4 + Main.x0), (int) (Main.y4 + Main.y0));
        painter.fillOval((int) (Main.x4 + Main.x0 - s2 / 2), (int) (Main.y4 + Main.y0 - s2 / 2), (int) s2, (int) s2);
    }
}