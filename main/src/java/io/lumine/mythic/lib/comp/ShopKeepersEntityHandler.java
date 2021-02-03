package io.lumine.mythic.lib.comp;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import io.lumine.mythic.lib.api.EntityHandler;
import org.bukkit.entity.Entity;

public class ShopKeepersEntityHandler implements EntityHandler{

    @Override
    public boolean isInvulnerable(Entity entity) {
        return ShopkeepersPlugin.getInstance().getShopkeeperRegistry().isShopkeeper(entity);
    }
}

