package org.rhm.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.rhm.CyberRewaredModClient;

@Environment(EnvType.CLIENT)
public class CWModMenuPlugin implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return CyberRewaredModClient::getConfigScreen;
    }
}
