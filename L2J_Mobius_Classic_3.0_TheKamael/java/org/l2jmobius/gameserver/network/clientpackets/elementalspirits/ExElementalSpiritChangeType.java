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
package org.l2jmobius.gameserver.network.clientpackets.elementalspirits;

import org.l2jmobius.commons.network.ReadablePacket;
import org.l2jmobius.gameserver.enums.ElementalType;
import org.l2jmobius.gameserver.model.ElementalSpirit;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;
import org.l2jmobius.gameserver.network.serverpackets.elementalspirits.ElementalSpiritInfo;

/**
 * @author JoeAlisson
 */
public class ExElementalSpiritChangeType implements ClientPacket
{
	private byte _type;
	private byte _element;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_type = (byte) packet.readByte();
		_element = (byte) packet.readByte(); // 1 - Fire, 2 - Water, 3 - Wind, 4 Earth
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final ElementalSpirit spirit = player.getElementalSpirit(ElementalType.of(_type));
		if (spirit == null)
		{
			client.sendPacket(SystemMessageId.NO_SPIRITS_ARE_AVAILABLE);
			return;
		}
		
		player.changeElementalSpirit(_element);
		client.sendPacket(new ElementalSpiritInfo(player, _element, _type));
		client.sendPacket(new SystemMessage(SystemMessageId.S1_WILL_BE_YOUR_ATTRIBUTE_ATTACK_FROM_NOW_ON).addElementalSpirit(_element));
	}
}
