/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import forestry.core.gadgets.TileForestry;
import forestry.core.gui.slots.SlotForestry;
import forestry.core.interfaces.IRestrictedAccess;
import forestry.core.inventory.ItemInventory;
import forestry.core.utils.SlotHelper;

public class ContainerForestry extends Container {

	private final IInventory inventoryAccess;
	private final IRestrictedAccess restrictedAccess;

	public ContainerForestry(TileForestry tileForestry) {
		this.inventoryAccess = tileForestry;
		this.restrictedAccess = tileForestry;
	}

	public ContainerForestry(ItemInventory itemInventory) {
		this.inventoryAccess = itemInventory;
		this.restrictedAccess = null;
	}

	@Override
	public ItemStack slotClick(int slotIndex, int button, int modifier, EntityPlayer player) {
		if (!canAccess(player)) {
			return null;
		}

		Slot slot = (slotIndex < 0) ? null : (Slot) this.inventorySlots.get(slotIndex);
		if (slot instanceof SlotForestry && ((SlotForestry) slot).isPhantom()) {
			return SlotHelper.slotClickPhantom(slot, button, modifier, player);
		}

		return super.slotClick(slotIndex, button, modifier, player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		if (!canAccess(player)) {
			return null;
		}

		return SlotHelper.transferStackInSlot(inventorySlots, player, slotIndex);
	}

	private boolean canAccess(EntityPlayer player) {
		return player != null && (restrictedAccess == null || restrictedAccess.allowsAlteration(player));
	}

	@Override
	public final boolean canInteractWith(EntityPlayer entityplayer) {
		if (inventoryAccess == null) {
			return true;
		}
		return inventoryAccess.isUseableByPlayer(entityplayer);
	}
}
