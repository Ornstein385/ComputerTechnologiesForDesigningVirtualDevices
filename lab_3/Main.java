import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.Timer;

public class Main {
    static JFrame frame = new JFrame("КТПрВП лабораторная");
    static JLabel stepLabel = new JLabel("шаг интегрирования"), dateLabel = new JLabel();
    static JTextField stepField = new JTextField("0.01");
    static JButton apply = new JButton("применить"), stop = new JButton("пауза"), start = new JButton("пуск");

    static Planet[] planets = new Planet[]{
            new Planet("Солнце", new Vec(0, 0, 0), new Vec(0, 0, 0), 1, Color.YELLOW),
            new Planet("Меркурий", new Vec(0.3590263039200, -0.0229458780572, -0.0494704545935),
                    new Vec(-0.0022692079504, 0.0257700545646, 0.0140014950319), 1.6601E-7, Color.GRAY),
            new Planet("Венера", new Vec(-0.0678814025257, 0.6514805708508, 0.2974322037225),
                    new Vec(-0.0202047774015, -0.0023048192363, 0.0002413085411), 2.4478383E-6, Color.ORANGE),
            new Planet("Земля", new Vec(-0.1746675687485, 0.8878826973429, 0.3848945796490),
                    new Vec(-0.0172179458030, -0.0028624618993, -0.0012400661907), 3.00348959632E-6, Color.GREEN),
            new Planet("Марс", new Vec(-0.8667639099172, -1.1620088222984, -0.5096020231611),
                    new Vec(0.0120799162865, -0.0059678982168, -0.0030632775140), 3.227151E-7, Color.RED),
            new Planet("Юпитер", new Vec(4.6580839119399, -1.6079842796591, -0.8026120479031),
                    new Vec(0.0026255239604, 0.0068288077571, 0.0028631090151), 9.5479194E-4, Color.PINK),
            new Planet("Сатурн", new Vec(6.9600794880566, -6.4221819669368, -2.9523453929819),
                    new Vec(0.0036673089657, 0.0036725811771, 0.0013591340670), 2.858860E-4, Color.LIGHT_GRAY),
            new Planet("Уран", new Vec(14.3976311704513, 12.4207826542699, 5.2362778149696),
                    new Vec(-0.0027147204414, 0.0024550008067, 0.0011134711530), 4.366244E-5, Color.CYAN),
            new Planet("Нептун", new Vec(29.6332690545131, -3.5149104192983, -2.1764797008247),
                    new Vec(0.0004116004817, 0.0029073350314, 0.0011798561193), 5.151389E-5, Color.BLUE)};

    final static double G = 2.95912208286E-4, h2 = -7.0 / 2520.0, h4 = 896.0 / 2520.0, h6 = -6561.0 / 2520.0, h8 = 8192.0 / 2520.0;

    static int width = 1920, height = 1080, delay = 10;

    static long prevTime = 0;
    static double step = 1, chartMax = 1E-10;

    static ArrayList<Double> chart = new ArrayList<>();

    //static long time = 1640995200000L;
    static GregorianCalendar calendar = new GregorianCalendar(2022, Calendar.JANUARY, 1, 0, 0, 0);

    static boolean isWorking = true;

    public static Vec[] getImpDiff(Vec[] tempPos) {
        Vec[] diffImp = new Vec[planets.length];
        for (int i = 0; i < diffImp.length; i++) {
            diffImp[i] = new Vec();
        }
        for (int i = 0; i < planets.length; i++) {
            for (int j = i + 1; j < planets.length; j++) {
                double dist = (tempPos[j].subVec(tempPos[i])).distance();
                diffImp[i] = diffImp[i].addVec(new Vec(G * planets[j].mass * (tempPos[j].x - tempPos[i].x) / dist,
                        G * planets[j].mass * (tempPos[j].y - tempPos[i].y) / dist,
                        G * planets[j].mass * (tempPos[j].z - tempPos[i].z) / dist));
                diffImp[j] = diffImp[j].addVec(new Vec(G * planets[i].mass * (tempPos[i].x - tempPos[j].x) / dist,
                        G * planets[i].mass * (tempPos[i].y - tempPos[j].y) / dist,
                        G * planets[i].mass * (tempPos[i].z - tempPos[j].z) / dist));
            }
        }
        return diffImp;
    }

    public static Vec[] multiStepMethodImp(int h) {
        Vec[] sumImpDiff = new Vec[planets.length];
        Vec[] tempPos = new Vec[planets.length];
        for (int i = 0; i < planets.length; i++) {
            sumImpDiff[i] = new Vec();
            tempPos[i] = new Vec(planets[i].pos);
        }
        for (int i = 0; i < h; i++) {
            Vec[] impDiff = getImpDiff(tempPos);
            for (int j = 0; j < planets.length; j++) {
                sumImpDiff[j] = impDiff[j].div(h).addVec(sumImpDiff[j]);
                tempPos[j] = planets[j].imp.div(h).addVec(sumImpDiff[j]).addVec(tempPos[j]);
            }
        }
        return sumImpDiff;
    }

    public static void update(JFrame frame) {

        Vec[] diffImp = new Vec[planets.length];
        for (int i = 0; i < planets.length; i++) {
            diffImp[i] = new Vec();
        }
        Vec[] diffImp_h1 = multiStepMethodImp(1);
        Vec[] diffImp_h2 = multiStepMethodImp(2);
        Vec[] diffImp_h4 = multiStepMethodImp(4);
        Vec[] diffImp_h6 = multiStepMethodImp(6);
        Vec[] diffImp_h8 = multiStepMethodImp(8);
        for (int i = 0; i < planets.length; i++) {
            diffImp[i] = diffImp_h2[i].mul(h2).addVec(diffImp[i]);
            diffImp[i] = diffImp_h4[i].mul(h4).addVec(diffImp[i]);
            diffImp[i] = diffImp_h6[i].mul(h6).addVec(diffImp[i]);
            diffImp[i] = diffImp_h8[i].mul(h8).addVec(diffImp[i]);
        }
        double methodDiff = 0;
        for (int i = 0; i < planets.length; i++) {
            methodDiff += diffImp[i].subVec(diffImp_h1[i]).distance();
            planets[i].imp = planets[i].imp.addVec(diffImp_h1[i]);
            planets[i].pos = planets[i].imp.mul(step).addVec(planets[i].pos);
        }
        chart.add(methodDiff);
        if (methodDiff > chartMax) {
            chartMax = methodDiff;
        }
        if (chart.size() >= 3000) {
            chart.removeAll(chart.subList(0, 1499));
        }
        //time += (long) (86400000 * step);
        calendar.add(Calendar.SECOND, (int) (86400 * step));
        if (System.currentTimeMillis() - prevTime > 1000) {
            prevTime = System.currentTimeMillis();
            dateLabel.setText(calendar.getTime().toString());
        }
        frame.add(new Plane());
        frame.repaint();
    }

    public static void main(String[] args) {
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(1, 1));
        frame.setVisible(true);
        stepLabel.setBounds(0, 0, 150, 30);
        stepField.setBounds(0, 30, 150, 30);
        apply.setBounds(0, 60, 150, 30);
        stop.setBounds(0, 90, 150, 30);
        start.setBounds(0, 120, 150, 30);
        dateLabel.setBounds(150, 0, 300, 30);
        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    step = Double.parseDouble(stepField.getText());
                } catch (Exception ex) {
                }
            }
        });
        frame.add(stepLabel);
        frame.add(stepField);
        frame.add(apply);
        frame.add(stop);
        frame.add(start);
        frame.add(dateLabel);
        MyTimerTask timerTask = new MyTimerTask();
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, delay);
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
        Arrays.sort(Main.planets, new Comparator<Planet>() {
            @Override
            public int compare(Planet o1, Planet o2) {
                return Double.compare(o1.pos.z, o2.pos.z);
            }
        });
        painter.setColor(Color.BLACK);
        painter.fillRect(0, 0, Main.width, Main.height);
        for (int i = 0; i < Main.planets.length; i++) {
            int size = (int) (Math.log(Main.planets[i].mass * 10000000000000.0));
            painter.setColor(Main.planets[i].color);
            painter.fillOval(Main.width / 2 + (int) (Main.planets[i].pos.x * 30) - size / 2,
                    Main.height / 2 + (int) (Main.planets[i].pos.y * 30) - size / 2, size, size);
            painter.setColor(Color.WHITE);
            painter.drawString(Main.planets[i].name, Main.width / 2 + (int) (Main.planets[i].pos.x * 30) - size / 2,
                    Main.height / 2 + (int) (Main.planets[i].pos.y * 30) - size / 2 - 10);
            painter.drawString(Main.planets[i].pos.toShortString(), Main.width / 2 + (int) (Main.planets[i].pos.x * 30) - size / 2,
                    Main.height / 2 + (int) (Main.planets[i].pos.y * 30) - size / 2);
        }

        painter.setColor(Color.WHITE);
        painter.fillRect(0, 0, 350, 30);
        painter.fillRect(0, Main.height - 200, Main.width, 200);
        painter.setColor(Color.BLACK);
        painter.drawString("погрешность методов моделирования:", 10, Main.height - 190);
        int q = (int) (100 / Main.chartMax), shift = Math.max(Main.chart.size() - 1500, 0);
        for (int i = 1; i < Main.chart.size() && i < 1500; i++){
            painter.drawLine(149 + i, (int) (Main.height - 80 - Main.chart.get(i-1 + shift) * q),
                    150 + i, (int) (Main.height - 80 - Main.chart.get(i + shift) * q));
        }
    }
}
