package com.zurrtum.create.foundation.recipe;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.zurrtum.create.mixin.RecipeMapInvoker;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.HashMap;
import java.util.Map;

public class GeneratedRecipes {
    /**
     * Returns a RecipeMap containing the original recipes plus the generated ones whose id is not already present.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static RecipeMap merge(RecipeMap original, Map<Identifier, Recipe<?>> generated) {
        Map<ResourceKey<Recipe<?>>, RecipeHolder<?>> byKey = new HashMap<>();
        for (RecipeHolder<?> holder : original.values()) {
            byKey.put(holder.id(), holder);
        }
        int added = 0;
        for (Map.Entry<Identifier, Recipe<?>> entry : generated.entrySet()) {
            ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, entry.getKey());
            if (byKey.containsKey(key)) {
                continue;
            }
            byKey.put(key, new RecipeHolder(key, entry.getValue()));
            added++;
        }
        if (added == 0) {
            return original;
        }
        ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> byType = ImmutableMultimap.builder();
        ImmutableMap.Builder<ResourceKey<Recipe<?>>, RecipeHolder<?>> keyed = ImmutableMap.builder();
        for (RecipeHolder<?> holder : byKey.values()) {
            byType.put(holder.value().getType(), holder);
            keyed.put(holder.id(), holder);
        }
        return RecipeMapInvoker.create$new(byType.build(), keyed.build());
    }
}
