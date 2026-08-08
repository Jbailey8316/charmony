package svenhjol.charmony.core.common.features.conditional_recipes;

import net.fabricmc.fabric.api.resource.v1.DataResourceLoader;
import svenhjol.charmony.core.base.Setup;

public class Registers extends Setup<ConditionalRecipes> {
    public Registers(ConditionalRecipes feature) {
        super(feature);
    }

    @Override
    public Runnable boot() {
        return () -> {
            // Conditional recipe manager.
            DataResourceLoader.get()
                .registerReloader(ConditionalRecipeManager.ID, ConditionalRecipeManager::new);
        };
    }
}
