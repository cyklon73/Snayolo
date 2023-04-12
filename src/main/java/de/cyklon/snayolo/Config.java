package de.cyklon.snayolo;

import de.cyklon.snayolo.util.Util;
import de.cyklon.snayolo.util.WaveBase;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Config {


    public static final Location winnerLocation = Util.Location.create(16, 6, 31, "world");

    public static class WorldBorder {
        /**
         * replaced by world spawn location
         */
        @Deprecated
        public static final Vector borderCenter = new Vector(16, 87, -14);

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
