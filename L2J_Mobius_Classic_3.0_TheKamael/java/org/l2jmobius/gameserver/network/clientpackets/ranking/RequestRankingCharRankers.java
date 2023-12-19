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
package org.l2jmobius.gameserver.network.clientpackets.ranking;

import org.l2jmobius.commons.network.ReadablePacket;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.ranking.ExRankingCharRankers;

/**
 * @author JoeAlisson
 */
public class RequestRankingCharRankers implements ClientPacket
{
	private int _group;
	private int _scope;
	private int _race;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_group = packet.readByte(); // Tab Id
		_scope = packet.readByte(); // All or personal
		_race = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		client.sendPacket(new ExRankingCharRankers(client.getPlayer(), _group, _scope, _race));
	}
}