package com.zurrtum.create.foundation.utility;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

/**
 * Point unique d'adaptation pour Minecraft 26.3.
 * <p>
 * Depuis 26.3, {@code Inventory#placeItemBackInInventory} et {@code LivingEntity#drop} prennent un
 * paramètre {@code Prediction}. Tous les appels de Create passent par ici pour n'avoir qu'un seul
 * endroit à corriger.
 * <p>
 * TODO (à faire une seule fois) : {@code Prediction} n'est pas importé ci-dessous, car son package
 * n'a pas pu être vérifié. Dans IntelliJ, placer le curseur sur {@code Prediction} puis Alt+Entrée
 * ("Import class"). Les autres portages 26.3 utilisent {@code Prediction.SERVER_ONLY} pour du code
 * côté serveur ; tous les appelants de Create sont côté serveur.
 */
public final class InventoryCompat {
    private InventoryCompat() {
    }

    public static void placeItemBack(Inventory inventory, ItemStack stack) {
        inventory.placeItemBackInInventory(stack, Prediction.SERVER_ONLY);
    }

    /**
     * Ancien {@code entity.drop(stack, thrownFromHand)}. Le troisième booléen de l'ancien
     * {@code ServerPlayer#drop(stack, true, false)} (déposer en dispersant) n'existe plus en 26.3.
     */
    public static void drop(LivingEntity entity, ItemStack stack, boolean thrownFromHand) {
        entity.drop(stack, thrownFromHand, Prediction.SERVER_ONLY);
    }
}
