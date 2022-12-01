import java.awt.*;

public class Planet {
    String name;
    Vec pos;
    Vec imp;
    double mass;
    Color color;

    public Planet(String name, Vec pos, Vec imp, double mass, Color color) {
        this.name = name;
        this.pos = pos;
        this.imp = imp;
        this.mass = mass;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Planet{" +
                "name='" + name + '\'' +
                ", pos=" + pos +
                ", pulse=" + imp +
                ", mass=" + mass +
                ", color=" + color +
                '}';
    }
}
