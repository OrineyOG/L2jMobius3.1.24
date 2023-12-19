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
package org.l2jmobius.gameserver.network.serverpackets.huntingzones;

import org.l2jmobius.gameserver.data.xml.TimedHuntingZoneData;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.TimedHuntingZoneHolder;
import org.l2jmobius.gameserver.model.variables.PlayerVariables;
import org.l2jmobius.gameserver.model.zone.ZoneId;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius
 */
public class TimedHuntingZoneList extends ServerPacket
{
	private final Player _player;
	private final boolean _isInTimedHuntingZone;
	
	public TimedHuntingZoneList(Player player)
	{
		_player = player;
		_isInTimedHuntingZone = player.isInsideZone(ZoneId.TIMED_HUNTING);
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_TIME_RESTRICT_FIELD_LIST.writeId(this);
		final long currentTime = System.currentTimeMillis();
		writeInt(TimedHuntingZoneData.getInstance().getSize()); // zone count
		for (TimedHuntingZoneHolder holder : TimedHuntingZoneData.getInstance().getAllHuntingZones())
		{
			writeInt(holder.getEntryFee() != 0); // required item count
			writeInt(holder.getEntryItemId());
			writeLong(holder.getEntryFee());
			writeInt(holder.isWeekly()); // reset cycle
			writeInt(holder.getZoneId());
			writeInt(holder.getMinLevel());
			writeInt(holder.getMaxLevel());
			writeInt(holder.getInitialTime() / 1000); // remain time base
			int remainingTime = _player.getTimedHuntingZoneRemainingTime(holder.getZoneId());
			if ((remainingTime == 0) && ((_player.getTimedHuntingZoneInitialEntry(holder.getZoneId()) + holder.getResetDelay()) < currentTime))
			{
				remainingTime = holder.getInitialTime();
			}
			writeInt(remainingTime / 1000); // remain time
			writeInt(holder.getMaximumAddedTime() / 1000);
			writeInt(_player.getVariables().getInt(PlayerVariables.HUNTING_ZONE_REMAIN_REFILL + holder.getZoneId(), holder.getRemainRefillTime()));
			writeInt(holder.getRefillTimeMax());
			writeByte(_isInTimedHuntingZone); // field activated
		}
	}
}