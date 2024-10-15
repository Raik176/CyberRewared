package org.rhm.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.rhm.api.IDeconstructable;
import org.rhm.api.IRoboticPart;
import org.rhm.api.IScannable;
import org.rhm.registries.ComponentRegistry;
import org.rhm.util.CyberUtil;

public abstract class CyberItem extends Item implements IRoboticPart, IScannable, IDeconstructable {
    public CyberItem(Properties settings) {
        super(settings.component(ComponentRegistry.SCAVENGED, false));
    }

    @Override
    public ItemStack getScanResult() {
        return CyberUtil.getBlueprintWithItem(this);
    }
}
