package com.example.inventorysync;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

public class Main extends JavaPlugin {

    private InventorySyncManager syncManager;

    @Override
    public void onEnable() {
        // Initialize the inventory sync manager
        syncManager = new InventorySyncManager(this);
        
        // Register event listener
        getServer().getPluginManager().registerEvents(new InventoryListener(syncManager), this);
        
        // Log plugin enabled
        Bukkit.getConsoleSender().sendMessage("§a[InventorySync] 플러그인이 활성화되었습니다!");
        Bukkit.getConsoleSender().sendMessage("§a모든 플레이어의 인벤토리가 동기화됩니다.");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§c[InventorySync] 플러그인이 비활성화되었습니다!");
    }
}
