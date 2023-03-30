package de.cyklon.snayolo;

import de.cyklon.snayolo.util.WaveBase;
import org.bukkit.util.Vector;

public class Config {

    public static class WorldBorder {
        public static final Vector[] borderCenters = {new Vector(0, 0, 0)};

        public static final WaveBase borderSize = new WaveBase() {
            @Override
            public double getDoubleValue(int wave) {
                return switch (wave) {
                    case 8 -> 200;
                    case 7 -> 300;
                    case 6 -> 400;
                    case 5 -> 500;
                    case 4 -> 600;
                    case 3 -> 700;
                    case 2 -> 800;
                    case 1 -> 900;
                    default -> 1000;
                };
            }
        };

        public static final double secondsPerBlock = 2;
    }

}
