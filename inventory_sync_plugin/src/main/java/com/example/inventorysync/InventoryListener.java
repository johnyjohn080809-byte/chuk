package com.example.inventorysync;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.entity.Player;

public class InventoryListener implements Listener {

    private InventorySyncManager syncManager;

    public InventoryListener(InventorySyncManager syncManager) {
        this.syncManager = syncManager;
    }

    /**
     * 플레이어가 서버에 접속했을 때 인벤토리 동기화
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // 약간의 딜레이 후 동기화 (플레이어 로드 완료 후)
        syncManager.syncNewPlayer(player);
    }

    /**
     * 플레이어가 인벤토리를 클릭했을 때 (아이템 이동, 인벤토리 조작)
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        
        // 플레이어 인벤토리 클릭만 감지 (상자, 용광로 등 제외)
        if (event.getInventory().getType() != InventoryType.PLAYER) {
            return;
        }

        // 동기화 실행
        syncManager.syncInventoryToAll(player);
    }

    /**
     * 플레이어가 아이템을 드롭했을 때
     */
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        syncManager.syncInventoryToAll(player);
    }

    /**
     * 플레이어가 아이템을 줍기 시도할 때 (Spigot 1.12+)
     */
    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        
        // 아이템 줍기 후 동기화 (약간의 딜레이)
        player.getServer().getScheduler().scheduleSyncDelayedTask(
            player.getServer().getPluginManager().getPlugin("InventorySync"),
            () -> syncManager.syncInventoryToAll(player),
            1L
        );
    }
}
