package org.rhm.gui;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandler;

public class ScreenInventory extends SimpleInventory {
    private ScreenHandler handler;

    public ScreenInventory(int size) {
        this(size, null);
    }

    public ScreenInventory(int size, ScreenHandler handler) {
        super(size);
        this.handler = handler;
    }

    public void setHandler(ScreenHandler handler) {
        this.handler = handler;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (handler != null) handler.onContentChanged(this);
    }
}
