package svenhjol.charmony.core.common.mixins.player_killed_drop_callback;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.logging.LogUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charmony.api.events.PlayerKilledDropCallback;
import org.slf4j.Logger;

@SuppressWarnings("UnreachableCode")
@Mixin(Player.class)
public abstract class PlayerMixin {
    private static final Logger CHARM$LOGGER = LogUtils.getLogger();
    @Shadow @Final
    Inventory inventory;

    /**
     * Fires the {@link svenhjol.charmony.api.events.PlayerKilledDropCallback} event.
     */
    @WrapWithCondition(
        method = "dropEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Inventory;dropAll()V"
        )
    )
    private boolean hookDropInventory(Inventory instance) {
        CHARM$LOGGER.info("[Totem Debug] PlayerMixin dropEquipment callback uuid={}", ((Player) (Object) this).getUUID());
        InteractionResult result = PlayerKilledDropCallback.EVENT.invoker().interact((Player) (Object) this, this.inventory);
        CHARM$LOGGER.info("[Totem Debug] PlayerMixin callback returned={}", result);
        return result != InteractionResult.SUCCESS;
    }
}
