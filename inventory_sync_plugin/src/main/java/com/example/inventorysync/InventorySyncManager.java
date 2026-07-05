package com.example.inventorysync;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class InventorySyncManager {

    private JavaPlugin plugin;

    public InventorySyncManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * 한 플레이어의 인벤토리를 모든 플레이어에게 동기화
     */
    public void syncInventoryToAll(Player sourcePlayer) {
        PlayerInventory sourceInv = sourcePlayer.getInventory();
        
        // 모든 온라인 플레이어 순회
        for (Player targetPlayer : Bukkit.getOnlinePlayers()) {
            if (targetPlayer == sourcePlayer) {
                continue; // 자신은 스킵
            }
            
            copyInventory(sourceInv, targetPlayer.getInventory());
        }
    }

    /**
     * 소스 인벤토리를 타겟 인벤토리에 복사 (모든 슬롯)
     */
    private void copyInventory(PlayerInventory source, PlayerInventory target) {
        // 핫바 (0-8번 슬롯)
        for (int i = 0; i < 9; i++) {
            target.setItem(i, source.getItem(i));
        }

        // 일반 인벤토리 (9-35번 슬롯)
        for (int i = 9; i < 36; i++) {
            target.setItem(i, source.getItem(i));
        }

        // 갑옷 (헬멧, 흉갑, 레깅스, 부츠)
        target.setHelmet(source.getHelmet());
        target.setChestplate(source.getChestplate());
        target.setLeggings(source.getLeggings());
        target.setBoots(source.getBoots());

        // 오프핸드 (왼손)
        target.setItemInOffHand(source.getItemInOffHand());
    }

    /**
     * 모든 플레이어가 같은 인벤토리를 가지도록 동기화 (온라인 플레이어 기준)
     */
    public void syncAllPlayers() {
        Player[] onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        
        if (onlinePlayers.length == 0) {
            return;
        }

        // 첫 번째 플레이어의 인벤토리를 기준으로
        PlayerInventory sourceInv = onlinePlayers[0].getInventory();

        // 다른 모든 플레이어에게 동기화
        for (int i = 1; i < onlinePlayers.length; i++) {
            copyInventory(sourceInv, onlinePlayers[i].getInventory());
        }
    }

    /**
     * 특정 플레이어가 새로 들어왔을 때 현재 인벤토리 상태에 맞추기
     */
    public void syncNewPlayer(Player newPlayer) {
        Player[] onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        
        if (onlinePlayers.length <= 1) {
            return; // 새 플레이어만 있으면 아무것도 안 함
        }

        // 첫 번째 온라인 플레이어의 인벤토리 찾기 (새 플레이어 제외)
        for (Player player : onlinePlayers) {
            if (player != newPlayer) {
                copyInventory(player.getInventory(), newPlayer.getInventory());
                newPlayer.sendMessage("§a[InventorySync] 인벤토리가 동기화되었습니다!");
                break;
            }
        }
    }
}
