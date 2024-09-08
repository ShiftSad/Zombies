package codes.shiftmc.zombies;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Zombie extends EntityCreature {
    public Zombie(@NotNull EntityType entityType, @NotNull Pos position, @NotNull Instance instance) {
        super(entityType);
        setInstance(instance, position);

        addAIGroup(
                List.of(
                    new MeleeAttackGoal(this, 1.6, 20, TimeUnit.SERVER_TICK),
                    new RandomStrollGoal(this, 20)
                ),
                List.of(
                        new LastEntityDamagerTarget(this, 32),
                        new ClosestEntityTarget(this, 32, entity -> entity instanceof Player)
                )
        );
    }

}
