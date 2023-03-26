package de.cyklon.snayolo.game;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;

public interface EventZombie {

    public Zombie getZombie();

    public AttackDamage getDamage();

    public default double getHealth() {
        return getZombie().getHealth();
    }

    public default double getMaxHealth() {
        return getZombie().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
    }

    public interface AttackDamage {

        public double getDamage();

        public double getDamage(EntityEquipment equipment);

    }

}
