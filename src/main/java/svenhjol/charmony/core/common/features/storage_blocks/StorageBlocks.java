package svenhjol.charmony.core.common.features.storage_blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.EntitySpawnReason;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import svenhjol.charmony.api.core.FeatureDefinition;
import svenhjol.charmony.api.core.Configurable;
import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.api.events.EntityTickCallback;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.base.Registerable;
import svenhjol.charmony.core.common.CommonRegistry;

import java.util.EnumSet;
import java.util.function.Supplier;

/** Historical Charm storage blocks: Ender Pearl, Gunpowder, and Sugar. */
@FeatureDefinition(side = Side.Common, description = "Additional compact storage blocks for common items.")
public final class StorageBlocks extends SidedFeature {
    public final Registers registers;
    @Configurable(name = "Ender pearl block converts silverfish", description = "If true, ender pearl blocks convert silverfish to endermites.")
    private static boolean convertSilverfish = true;
    @Configurable(name = "TNT from gunpowder block and sand", description = "If true, adds a TNT recipe using a gunpowder block and sand.")
    private static boolean tntRecipe = true;

    public StorageBlocks(Mod mod) {
        super(mod);
        registers = new Registers(this);
    }

    public boolean convertSilverfish() { return convertSilverfish; }
    public boolean tntRecipe() { return tntRecipe; }

    @Override
    public void run() {
        EntityTickCallback.EVENT.register(entity -> {
            if (enabled() && convertSilverfish && entity instanceof Silverfish silverfish) {
                registers.addGoal(silverfish);
            }
        });
    }

    public static final class Registers {
        public final Registerable<StorageBlock> enderPearlBlock;
        public final Registerable<Item> enderPearlItem;
        public final Registerable<GunpowderStorageBlock> gunpowderBlock;
        public final Registerable<Item> gunpowderItem;
        public final Registerable<SugarStorageBlock> sugarBlock;
        public final Registerable<Item> sugarItem;
        public final Registerable<SoundEvent> gunpowderDissolve;
        public final Registerable<SoundEvent> sugarDissolve;
        private final StorageBlocks feature;

        Registers(StorageBlocks feature) {
            this.feature = feature;
            var registry = CommonRegistry.forFeature(feature);
            enderPearlBlock = registry.block("ender_pearl_block", key -> new EnderPearlStorageBlock(key));
            enderPearlItem = registry.item("ender_pearl_block", key -> new BlockItem(enderPearlBlock.get(), new Item.Properties().setId(key)));
            gunpowderBlock = registry.block("gunpowder_block", key -> new GunpowderStorageBlock(key));
            gunpowderItem = registry.item("gunpowder_block", key -> new BlockItem(gunpowderBlock.get(), new Item.Properties().setId(key)));
            sugarBlock = registry.block("sugar_block", key -> new SugarStorageBlock(key));
            sugarItem = registry.item("sugar_block", key -> new BlockItem(sugarBlock.get(), new Item.Properties().setId(key)));
            gunpowderDissolve = registry.sound("gunpowder_dissolve");
            sugarDissolve = registry.sound("sugar_dissolve");
        }

        void addGoal(Silverfish silverfish) {
            if (silverfish.goalSelector.getAvailableGoals().stream().anyMatch(g -> g.getGoal() instanceof FormEndermiteGoal)) return;
            silverfish.goalSelector.addGoal(2, new FormEndermiteGoal(silverfish, feature));
        }
    }

    public static class StorageBlock extends Block {
        protected StorageBlock(net.minecraft.resources.ResourceKey<Block> key, BlockBehaviour.Properties properties) {
            super(properties.setId(key));
        }
    }

    public static final class EnderPearlStorageBlock extends StorageBlock {
        EnderPearlStorageBlock(net.minecraft.resources.ResourceKey<Block> key) {
            super(key, BlockBehaviour.Properties.of().sound(SoundType.GLASS).strength(2.0f));
        }
        @Override
        public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
            if (random.nextInt(10) == 0) level.addParticle(net.minecraft.core.particles.ParticleTypes.PORTAL,
                pos.getX() + random.nextDouble(), pos.getY() + 1.1d, pos.getZ() + random.nextDouble(), 0, 0, 0);
        }
    }

    public abstract static class DissolvingStorageBlock extends FallingBlock {
        protected DissolvingStorageBlock(net.minecraft.resources.ResourceKey<Block> key, SoundType sound) {
            super(BlockBehaviour.Properties.of().sound(sound).strength(0.5f).setId(key));
        }
        protected DissolvingStorageBlock(BlockBehaviour.Properties properties) { super(properties); }
        @Override protected abstract MapCodec<? extends FallingBlock> codec();
        protected abstract boolean dissolve(Level level, BlockPos pos);
        @Override protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, net.minecraft.world.level.redstone.Orientation orientation, boolean moving) {
            if (!dissolve(level, pos)) super.neighborChanged(state, level, pos, block, orientation, moving);
        }
        @Override public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
            if (!dissolve(level, pos)) super.onPlace(state, level, pos, oldState, moving);
        }
    }

    public static final class GunpowderStorageBlock extends DissolvingStorageBlock {
        static final MapCodec<GunpowderStorageBlock> CODEC = simpleCodec(GunpowderStorageBlock::new);
        GunpowderStorageBlock(net.minecraft.resources.ResourceKey<Block> key) { this(BlockBehaviour.Properties.of().sound(SoundType.SAND).strength(0.5f).setId(key)); }
        private GunpowderStorageBlock(BlockBehaviour.Properties properties) { super(properties); }
        @Override protected MapCodec<? extends FallingBlock> codec() { return CODEC; }
        @Override public int getDustColor(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos) { return 0x4A3B32; }
        @Override protected boolean dissolve(Level level, BlockPos pos) {
            for (Direction direction : Direction.values()) {
                if (direction != Direction.DOWN && level.getBlockState(pos.relative(direction)).is(Blocks.LAVA)) {
                    level.globalLevelEvent(2001, pos, Block.getId(level.getBlockState(pos)));
                    level.removeBlock(pos, true);
                    level.playSound(null, pos, Mod.getSidedFeature(StorageBlocks.class).registers.gunpowderDissolve.get(), SoundSource.BLOCKS, 1, 1);
                    return true;
                }
            }
            return false;
        }
    }

    public static final class SugarStorageBlock extends DissolvingStorageBlock {
        static final MapCodec<SugarStorageBlock> CODEC = simpleCodec(SugarStorageBlock::new);
        SugarStorageBlock(net.minecraft.resources.ResourceKey<Block> key) { this(BlockBehaviour.Properties.of().sound(SoundType.SAND).strength(0.5f).setId(key)); }
        private SugarStorageBlock(BlockBehaviour.Properties properties) { super(properties); }
        @Override protected MapCodec<? extends FallingBlock> codec() { return CODEC; }
        @Override public int getDustColor(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos) { return 0xF6F1D5; }
        @Override protected boolean dissolve(Level level, BlockPos pos) {
            for (Direction direction : Direction.values()) {
                if (direction != Direction.DOWN && level.getBlockState(pos.relative(direction)).is(Blocks.WATER)) {
                    level.globalLevelEvent(2001, pos, Block.getId(level.getBlockState(pos)));
                    level.removeBlock(pos, true);
                    level.playSound(null, pos, Mod.getSidedFeature(StorageBlocks.class).registers.sugarDissolve.get(), SoundSource.BLOCKS, 1, 1);
                    return true;
                }
            }
            return false;
        }
    }

    private static final class FormEndermiteGoal extends RandomStrollGoal {
        private final Silverfish silverfish;
        private final StorageBlocks feature;
        private Direction facing;
        private boolean merge;
        FormEndermiteGoal(Silverfish silverfish, StorageBlocks feature) { super(silverfish, 0.6); this.silverfish = silverfish; this.feature = feature; setFlags(EnumSet.of(Goal.Flag.MOVE)); }
        @Override public boolean canUse() {
            if (!(silverfish.level() instanceof ServerLevel server) || !server.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) || silverfish.getTarget() != null || !silverfish.getNavigation().isDone()) return false;
            if (silverfish.getRandom().nextFloat() < 0.8f) {
                facing = Direction.getRandom(silverfish.getRandom());
                var p = silverfish.blockPosition().relative(facing);
                if (silverfish.level().getBlockState(p).is(feature.registers.enderPearlBlock.get())) { merge = true; return true; }
            }
            merge = false;
            return super.canUse();
        }
        @Override public boolean canContinueToUse() { return !merge && super.canContinueToUse(); }
        @Override public void start() {
            if (merge && silverfish.level() instanceof ServerLevel server && facing != null) {
                var p = silverfish.blockPosition().relative(facing);
                if (server.getBlockState(p).is(feature.registers.enderPearlBlock.get())) {
                    var endermite = EntityType.ENDERMITE.create(server, EntitySpawnReason.CONVERSION);
                    if (endermite != null) { endermite.setPos(silverfish.getX(), silverfish.getY(), silverfish.getZ()); endermite.setYRot(silverfish.getYRot()); server.addFreshEntity(endermite); server.removeBlock(p, false); silverfish.discard(); }
                }
            }
            super.start();
        }
    }
}
