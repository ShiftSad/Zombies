package codes.shiftmc.zombies;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

public class BaseZombie extends Zombie {
    public BaseZombie(
            @NotNull Pos position,
            @NotNull Instance instance
    ) {
        super(EntityType.ZOMBIE, position, instance);
        getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1);
    }


}
