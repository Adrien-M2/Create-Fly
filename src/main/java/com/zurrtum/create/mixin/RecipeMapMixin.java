package com.zurrtum.create.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.zurrtum.create.content.kinetics.mixer.PotionRecipe;
import com.zurrtum.create.content.processing.sequenced.SequencedAssemblyRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 26.3: RecipeManager.prepare() no longer exists (recipes come from the recipe registry). The generated
 * sequenced assembly step recipes are added to the RecipeMap built by RecipeMap.create instead.
 */
@Mixin(RecipeMap.class)
public class RecipeMapMixin {
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ModifyReturnValue(method = "create", at = @At("RETURN"))
    private static RecipeMap create$addGenerated(RecipeMap original) {
        Map<ResourceKey<Recipe<?>>, RecipeHolder<?>> byKey = new HashMap<>();
        for (RecipeHolder<?> holder : original.values()) {
            byKey.put(holder.id(), holder);
        }
        int added = 0;
        Map<Identifier, Recipe<?>> generated = new LinkedHashMap<>(SequencedAssemblyRecipe.GENERATE_RECIPES);
        generated.putAll(PotionRecipe.generate(original.values()));
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
