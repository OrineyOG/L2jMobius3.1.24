/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jmobius.gameserver.model.actor.request;

import org.l2jmobius.gameserver.data.xml.EnchantItemData;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.item.enchant.EnchantScroll;
import org.l2jmobius.gameserver.model.item.enchant.EnchantSupportItem;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;

/**
 * @author UnAfraid
 */
public class EnchantItemRequest extends AbstractRequest
{
	private volatile int _enchantingItemObjectId;
	private volatile int _enchantingScrollObjectId;
	private volatile int _supportItemObjectId;
	
	public EnchantItemRequest(Player player, int enchantingScrollObjectId)
	{
		super(player);
		_enchantingScrollObjectId = enchantingScrollObjectId;
	}
	
	public Item getEnchantingItem()
	{
		return getActiveChar().getInventory().getItemByObjectId(_enchantingItemObjectId);
	}
	
	// Declare a variable to hold the last message timestamp.
	private long lastMessageTimestamp = 0;
	
	public void setEnchantingItem(int objectId)
	{
		// Check if enough time has elapsed since the last message.
		long currentTime = System.currentTimeMillis();
		if ((currentTime - lastMessageTimestamp) < 15000) // 15 seconds
		{
			return; // Skip sending the message.
		}
		
		// Update the last message timestamp.
		lastMessageTimestamp = currentTime;
		// Set the object ID of the enchanting item
		_enchantingItemObjectId = objectId;
		
		// Fetch the current enchanting item
		Item enchantingItem = getEnchantingItem();
		
		// If the item has just been enchanted, skip the message
		if (enchantingItem.isJustEnchanted())
		{
			enchantingItem.setJustEnchanted(false); // Reset the flag for next time
			return;
		}
		
		// Fetch the EnchantScroll object
		Item enchantingScrollItem = getEnchantingScroll();
		// Fetch the current support item as an Item object
		Item supportItemAsItem = getSupportItem();
		
		// Convert the Item to an EnchantSupportItem
		EnchantSupportItem supportItem = EnchantItemData.getInstance().getSupportItem(supportItemAsItem);
		
		EnchantScroll enchantScroll = EnchantItemData.getInstance().getEnchantScroll(enchantingScrollItem);
		
		if (enchantScroll != null)
		{
			// Fetch the current enchant chance
			double currentChance = enchantScroll.getFinalEnchantChance(getActiveChar(), enchantingItem, supportItem);
			
			// Determine the failure penalty
			String failurePenalty;
			if (currentChance >= 100)
			{
				failurePenalty = "None";
			}
			else if (enchantScroll.isSafe())
			{
				failurePenalty = "None";
			}
			else if (enchantScroll.isBlessed())
			{
				failurePenalty = "Reset to 0";
			}
			else if (enchantScroll.isBlessedDown())
			{
				failurePenalty = "-1";
			}
			else
			{
				failurePenalty = "DESTROYED";
			}
			
			// Create a new System Message with the enchant rate and penalty
			SystemMessage sm = new SystemMessage(SystemMessageId.ENCHANT_RATE_AND_PENALTY);
			sm.addInt((int) Math.round(currentChance)); // Set $s1
			sm.addString(failurePenalty); // Set $s2
			
			// Send the System Message to the player
			getActiveChar().sendPacket(sm);
		}
		else
		{
			// Optionally, you can add a log or a message to the player here
		}
	}
	
	public Item getEnchantingScroll()
	{
		return getActiveChar().getInventory().getItemByObjectId(_enchantingScrollObjectId);
	}
	
	public void setEnchantingScroll(int objectId)
	{
		_enchantingScrollObjectId = objectId;
	}
	
	public Item getSupportItem()
	{
		Item item = getActiveChar().getInventory().getItemByObjectId(_supportItemObjectId);
		return item;
	}
	
	public void setSupportItem(int objectId)
	{
		_supportItemObjectId = objectId;
		
	}
	
	@Override
	public boolean isItemRequest()
	{
		return true;
	}
	
	@Override
	public boolean canWorkWith(AbstractRequest request)
	{
		return !request.isItemRequest();
	}
	
	@Override
	public boolean isUsing(int objectId)
	{
		return (objectId > 0) && ((objectId == _enchantingItemObjectId) || (objectId == _enchantingScrollObjectId) || (objectId == _supportItemObjectId));
	}
	
}
