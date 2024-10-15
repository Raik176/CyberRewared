package org.rhm.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import org.rhm.api.IDeconstructable;
import org.rhm.api.IScannable;
import org.rhm.registries.ItemRegistry;
import org.rhm.util.CyberUtil;

public class KatanaItem extends SwordItem implements IDeconstructable, IScannable {
    public KatanaItem() {
        super(Tiers.IRON, new Properties().attributes(createAttributes(
            Tiers.IRON,
            4,
            -2.4f
        )));
    }

    @Override
    public ItemStack[] getDestructComponents() {
        return new ItemStack[]{
            new ItemStack(ItemRegistry.TITANIUM_MESH),
            new ItemStack(ItemRegistry.CHROME_PLATING),
            new ItemStack(Items.IRON_INGOT, 2)
        };
    }

    @Override
    public ItemStack getScanResult() {
        return CyberUtil.getBlueprintWithItem(this);
    }

    @Override
    public int getScanTime() {
        return 18000;
    }

    @Override
    public float scanSuccessChance() {
        return 50;
    }
}
