package io.github.elifoster.witcherypatch;

import com.emoniph.witchery.crafting.DistilleryRecipes;
import com.emoniph.witchery.integration.NEIDistilleryRecipeHandler;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Hooks {
    /**
     * Improvement over {@link DistilleryRecipes#findRecipeFor(ItemStack)} in that it returns every matching recipe,
     * not just the first one.
     * @param result The output ItemStack to query for
     * @return A list of recipes
     */
    public static List<DistilleryRecipes.DistilleryRecipe> findRecipesFor(ItemStack result) {
        List<DistilleryRecipes.DistilleryRecipe> recipes = new ArrayList<>();
        for (DistilleryRecipes.DistilleryRecipe recipe : DistilleryRecipes.instance().recipes) {
            if (recipe.resultsIn(result)) {
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    /**
     * Improvement over {@link NEIDistilleryRecipeHandler#loadCraftingRecipes(ItemStack)} in that it uses
     * {@link #findRecipesFor(ItemStack)} instead.
     * @param handler The handler instance
     * @param result The output ItemStack to query for.
     */
    public static void loadCraftingRecipes(NEIDistilleryRecipeHandler handler, ItemStack result) {
        List<DistilleryRecipes.DistilleryRecipe> recipes = findRecipesFor(result);
        for (DistilleryRecipes.DistilleryRecipe recipe : recipes) {
            handler.arecipes.add(handler.new CachedDistillingRecipe(result, recipe));
        }
    }
}
