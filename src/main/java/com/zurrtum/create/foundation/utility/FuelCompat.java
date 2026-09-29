package com.zurrtum.create.foundation.utility;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;

/**
 * Remplace {@code level.fuelValues().burnDuration(stack)} (supprimé en 26.3).
 * Le carburant est maintenant le composant {@code COOKING_FUEL}.
 * <p>
 * LIMITE : seules les durées constantes sont lues. Une durée fournie par une référence
 * (ContextIntProvider, qui exige un LootContext) renvoie 0.
 */
public final class FuelCompat {
    private FuelCompat() {
    }

    public static int burnDuration(ItemStack stack) {
        CookingFuel fuel = stack.get(DataComponents.COOKING_FUEL);
        if (fuel == null) {
            return 0;
        }
        if (fuel.burnTime() instanceof ResolvableInt.Constant constant) {
            return constant.value();
        }
        return 0;
    }

    public static boolean isFuel(ItemStack stack) {
        return burnDuration(stack) > 0;
    }
}
