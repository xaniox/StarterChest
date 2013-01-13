package com.matzefratze123.starterchest.event;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import com.matzefratze123.starterchest.system.ChestStorage;
import com.matzefratze123.starterchest.system.StarterChestManager;

public class EntityExplodeListener implements Listener {
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {
		if (e.isCancelled())
			return;
		List<Block> removedBlocks = e.blockList();
		ChestStorage[] storages = StarterChestManager.getStorages();
		for (Block block : removedBlocks) {
			if (block.getState() == null)
				continue;
			if (block.getState().getType() != Material.CHEST)
				continue;
			for (ChestStorage storage : storages) {
				if (StarterChestManager.isSameBlockLocation(block.getLocation(), storage.getChestLoc())) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}

}
