package svenhjol.charmony.core.common.features.wood;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charmony.core.common.features.wood.blocks.entity.CustomChestBlockEntity;
import svenhjol.charmony.core.common.features.wood.blocks.entity.CustomTrappedChestBlockEntity;
import svenhjol.charmony.core.base.Registerable;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.common.CommonRegistry;
import svenhjol.charmony.core.common.features.wood.blocks.CustomStairBlock;
import svenhjol.charmony.core.common.features.wood.blocks.CustomWallHangingSignBlock;
import svenhjol.charmony.core.common.features.wood.blocks.CustomWallSignBlock;
import svenhjol.charmony.core.common.features.wood.items.CustomHangingSignItem;
import svenhjol.charmony.core.common.features.wood.items.CustomSignItem;
import svenhjol.charmony.core.common.features.wood.types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class WoodRegistry {
    private final SidedFeature feature;
    private final CommonRegistry commonRegistry;

    public static final Table<Supplier<? extends Item>, Supplier<ItemLike>, CustomWood> ITEM_CREATIVE_TABS = HashBasedTable.create();
    public static final List<Supplier<CustomSignItem>> SIGN_ITEMS = new ArrayList<>();
    public static final List<Supplier<CustomHangingSignItem>> HANGING_SIGN_ITEMS = new ArrayList<>();

    public static final List<Supplier<Boat>> BOATS = new ArrayList<>();
    public static final List<Supplier<Sign>> SIGNS = new ArrayList<>();
    public static final List<Supplier<Chest>> CHESTS = new ArrayList<>();
    public static final List<Supplier<TrappedChest>> TRAPPED_CHESTS = new ArrayList<>();
    private static Registerable<BlockEntityType<CustomChestBlockEntity>> CHEST_BLOCK_ENTITY;
    private static Registerable<BlockEntityType<CustomTrappedChestBlockEntity>> TRAPPED_CHEST_BLOCK_ENTITY;

    private WoodRegistry(CommonRegistry commonRegistry) {
        this.commonRegistry = commonRegistry;
        this.feature = commonRegistry.feature();
        initializeChestBlockEntity(commonRegistry);
    }

    public static WoodRegistry forRegistry(CommonRegistry registry) {
        return new WoodRegistry(registry);
    }

    public void addItemToCreativeTab(Supplier<? extends Item> item, WoodMaterial material, CustomWood woodType) {
        var after = material.creativeMenuPosition().get(woodType);
        ITEM_CREATIVE_TABS.put(item, after, woodType);
    }

    public Barrel barrel(WoodMaterial material) {
        return new Barrel(this, material);
    }

    public Bookshelf bookshelf(WoodMaterial material) {
        return new Bookshelf(this, material);
    }

    public ChiseledBookshelf chiseledBookshelf(WoodMaterial material) {
        return new ChiseledBookshelf(this, material);
    }

    public Chest chest(WoodMaterial material) {
        return new Chest(this, material);
    }

    public TrappedChest trappedChest(WoodMaterial material) {
        return new TrappedChest(this, material);
    }

    public static BlockEntityType<CustomChestBlockEntity> chestBlockEntity() {
        if (CHEST_BLOCK_ENTITY == null) {
            throw new IllegalStateException("Chest block entity was not initialized");
        }
        return CHEST_BLOCK_ENTITY.get();
    }

    public static BlockEntityType<CustomTrappedChestBlockEntity> trappedChestBlockEntity() {
        if (TRAPPED_CHEST_BLOCK_ENTITY == null) {
            throw new IllegalStateException("Trapped chest block entity was not initialized");
        }
        return TRAPPED_CHEST_BLOCK_ENTITY.get();
    }

    private static void initializeChestBlockEntity(CommonRegistry registry) {
        if (CHEST_BLOCK_ENTITY == null) {
            CHEST_BLOCK_ENTITY = registry.blockEntity("chest", () ->
                (pos, state) -> new CustomChestBlockEntity(CHEST_BLOCK_ENTITY.get(), pos, state));
        }
        if (TRAPPED_CHEST_BLOCK_ENTITY == null) {
            TRAPPED_CHEST_BLOCK_ENTITY = registry.blockEntity("trapped_chest", () ->
                (pos, state) -> new CustomTrappedChestBlockEntity(TRAPPED_CHEST_BLOCK_ENTITY.get(), pos, state));
        }
    }

    public Boat boat(WoodMaterial material) {
        var boat = new Boat(this, material);
        BOATS.add(() -> boat);
        return boat;
    }

    public Registerable<EntityType<net.minecraft.world.entity.vehicle.Boat>> boatEntity(WoodMaterial material, Supplier<Item> boatItem) {
        var id = material.getSerializedName() + "_boat";

        EntityType.EntityFactory<net.minecraft.world.entity.vehicle.Boat> factory =
            (entityType, level) -> new net.minecraft.world.entity.vehicle.Boat(entityType, level, boatItem);

        var boatBuilder = EntityType.Builder.of(factory, MobCategory.MISC)
            .noLootTable()
            .sized(1.375f, 0.5652f)
            .eyeHeight(0.5652f)
            .clientTrackingRange(10);

        return commonRegistry.entity(id, () -> boatBuilder);
    }

    public Button button(WoodMaterial material) {
        return new Button(this, material);
    }

    public Registerable<EntityType<net.minecraft.world.entity.vehicle.ChestBoat>> chestBoatEntity(WoodMaterial material, Supplier<Item> chestBoatItem) {
        var id = material.getSerializedName() + "_chest_boat";

        EntityType.EntityFactory<net.minecraft.world.entity.vehicle.ChestBoat> factory =
            (entityType, level) -> new ChestBoat(entityType, level, chestBoatItem);

        var chestBoatBuilder = EntityType.Builder.of(factory, MobCategory.MISC)
            .noLootTable()
            .sized(1.375f, 0.5652f)
            .eyeHeight(0.5652f)
            .clientTrackingRange(10);

        return commonRegistry.entity(id, () -> chestBoatBuilder);
    }

    public CommonRegistry commonRegistry() {
        return commonRegistry;
    }

    public Door door(WoodMaterial material) {
        return new Door(this, material);
    }

    public SidedFeature feature() {
        return feature;
    }

    public Shelf shelf(WoodMaterial material) {
        return new Shelf(this, material);
    }

    public Fence fence(WoodMaterial material) {
        return new Fence(this, material);
    }

    public Gate gate(WoodMaterial material) {
        return new Gate(this, material);
    }

    public HangingSign hangingSign(WoodMaterial material) {
        return new HangingSign(this, material);
    }

    public Leaves leaves(WoodMaterial material) {
        return new Leaves(this, material);
    }

    public Ladder ladder(WoodMaterial material) {
        return new Ladder(this, material);
    }

    public Log log(WoodMaterial material) {
        return new Log(this, material);
    }

    public Planks planks(WoodMaterial material) {
        return new Planks(this, material);
    }

    public PressurePlate pressurePlate(WoodMaterial material) {
        return new PressurePlate(this, material);
    }

    public Sapling sapling(WoodMaterial material) {
        return new Sapling(this, material);
    }

    public Sign sign(WoodMaterial material) {
        var sign = new Sign(this, material);
        SIGNS.add(() -> sign);
        return sign;
    }

    public Slab slab(WoodMaterial material) {
        return new Slab(this, material);
    }

    public Stairs stairs(WoodMaterial material, Planks planks) {
        return new Stairs(this, material, () -> planks);
    }

    public Pair<Registerable<CustomStairBlock>, Registerable<CustomStairBlock.StairBlockItem>> stairsBlockHelper(String id, Supplier<WoodMaterial> material, Supplier<BlockState> state) {
        var block = commonRegistry.block(id, key -> new CustomStairBlock(key, material.get(), state.get()));
        var item = commonRegistry.item(id, key -> new CustomStairBlock.StairBlockItem(key, block));
        return Pair.of(block, item);
    }

    public Trapdoor trapdoor(WoodMaterial material) {
        return new Trapdoor(this, material);
    }

    public Registerable<CustomWallHangingSignBlock> wallHangingSignBlock(String id, SidedFeature feature, WoodMaterial material, Supplier<? extends CeilingHangingSignBlock> hangingSign) {
        return commonRegistry.block(id, key -> new CustomWallHangingSignBlock(key, feature, material, hangingSign.get()));
    }

    public Registerable<CustomWallSignBlock> wallSignBlock(String id, WoodMaterial material, Supplier<? extends StandingSignBlock> standingSign) {
        return commonRegistry.block(id, key -> new CustomWallSignBlock(key, material, standingSign.get()));
    }

    public svenhjol.charmony.core.common.features.wood.types.Wood wood(WoodMaterial material) {
        return new svenhjol.charmony.core.common.features.wood.types.Wood(this, material);
    }
}
