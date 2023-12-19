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
package org.l2jmobius.gameserver.network.serverpackets.ranking;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.instancemanager.RankManager;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author NviX
 */
public class ExOlympiadRankingInfo extends ServerPacket
{
	private final Player _player;
	private final int _tabId;
	private final int _rankingType;
	private final int _unk;
	private final int _classId;
	private final int _serverId;
	private final Map<Integer, StatSet> _playerList;
	private final Map<Integer, StatSet> _snapshotList;
	
	public ExOlympiadRankingInfo(Player player, int tabId, int rankingType, int unk, int classId, int serverId)
	{
		_player = player;
		_tabId = tabId;
		_rankingType = rankingType;
		_unk = unk;
		_classId = classId;
		_serverId = serverId;
		_playerList = RankManager.getInstance().getOlyRankList();
		_snapshotList = RankManager.getInstance().getSnapshotOlyList();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_OLYMPIAD_RANKING_INFO.writeId(this);
		writeByte(_tabId); // Tab id
		writeByte(_rankingType); // ranking type
		writeByte(_unk); // unk, shows 1 all time
		writeInt(_classId); // class id (default 148) or caller class id for personal rank
		writeInt(_serverId); // 0 - all servers, server id - for caller server
		writeInt(933); // unk, 933 all time
		if (!_playerList.isEmpty())
		{
			switch (_tabId)
			{
				case 0:
				{
					if (_rankingType == 0)
					{
						writeInt(_playerList.size() > 100 ? 100 : _playerList.size());
						for (Integer id : _playerList.keySet())
						{
							final StatSet player = _playerList.get(id);
							writeSizedString(player.getString("name")); // name
							writeSizedString(player.getString("clanName")); // clan name
							writeInt(id); // rank
							if (!_snapshotList.isEmpty())
							{
								for (Integer id2 : _snapshotList.keySet())
								{
									final StatSet snapshot = _snapshotList.get(id2);
									if (player.getInt("charId") == snapshot.getInt("charId"))
									{
										writeInt(id2); // previous rank
									}
								}
							}
							else
							{
								writeInt(id);
							}
							writeInt(Config.SERVER_ID); // server id
							writeInt(player.getInt("level")); // level
							writeInt(player.getInt("classId")); // class id
							writeInt(player.getInt("clanLevel")); // clan level
							writeInt(player.getInt("competitions_won")); // win count
							writeInt(player.getInt("competitions_lost")); // lose count
							writeInt(player.getInt("olympiad_points")); // points
							writeInt(player.getInt("count")); // hero counts
							writeInt(player.getInt("legend_count")); // legend counts
						}
					}
					else
					{
						boolean found = false;
						for (Integer id : _playerList.keySet())
						{
							final StatSet player = _playerList.get(id);
							if (player.getInt("charId") == _player.getObjectId())
							{
								found = true;
								final int first = id > 10 ? (id - 9) : 1;
								final int last = _playerList.size() >= (id + 10) ? id + 10 : id + (_playerList.size() - id);
								if (first == 1)
								{
									writeInt(last - (first - 1));
								}
								else
								{
									writeInt(last - first);
								}
								for (int id2 = first; id2 <= last; id2++)
								{
									final StatSet plr = _playerList.get(id2);
									writeSizedString(plr.getString("name"));
									writeSizedString(plr.getString("clanName"));
									writeInt(id2);
									if (!_snapshotList.isEmpty())
									{
										for (Integer id3 : _snapshotList.keySet())
										{
											final StatSet snapshot = _snapshotList.get(id3);
											if (player.getInt("charId") == snapshot.getInt("charId"))
											{
												writeInt(id3); // class rank snapshot
											}
										}
									}
									else
									{
										writeInt(id2);
									}
									writeInt(Config.SERVER_ID);
									writeInt(plr.getInt("level"));
									writeInt(plr.getInt("classId"));
									writeInt(plr.getInt("clanLevel")); // clan level
									writeInt(plr.getInt("competitions_won")); // win count
									writeInt(plr.getInt("competitions_lost")); // lose count
									writeInt(plr.getInt("olympiad_points")); // points
									writeInt(plr.getInt("count")); // hero counts
									writeInt(plr.getInt("legend_count")); // legend counts
								}
							}
						}
						if (!found)
						{
							writeInt(0);
						}
					}
					break;
				}
				case 1:
				{
					if (_rankingType == 0)
					{
						int count = 0;
						for (int i = 1; i <= _playerList.size(); i++)
						{
							final StatSet player = _playerList.get(i);
							if (_classId == player.getInt("classId"))
							{
								count++;
							}
						}
						writeInt(count > 50 ? 50 : count);
						int i = 1;
						for (Integer id : _playerList.keySet())
						{
							final StatSet player = _playerList.get(id);
							if (_classId == player.getInt("classId"))
							{
								writeSizedString(player.getString("name"));
								writeSizedString(player.getString("clanName"));
								writeInt(i); // class rank
								if (!_snapshotList.isEmpty())
								{
									final Map<Integer, StatSet> snapshotRaceList = new ConcurrentHashMap<>();
									int j = 1;
									for (Integer id2 : _snapshotList.keySet())
									{
										final StatSet snapshot = _snapshotList.get(id2);
										if (_classId == snapshot.getInt("classId"))
										{
											snapshotRaceList.put(j, _snapshotList.get(id2));
											j++;
										}
									}
									for (Integer id2 : snapshotRaceList.keySet())
									{
										final StatSet snapshot = snapshotRaceList.get(id2);
										if (player.getInt("charId") == snapshot.getInt("charId"))
										{
											writeInt(id2); // class rank snapshot
										}
									}
								}
								else
								{
									writeInt(i);
								}
								writeInt(Config.SERVER_ID);
								writeInt(player.getInt("level"));
								writeInt(player.getInt("classId"));
								writeInt(player.getInt("clanLevel")); // clan level
								writeInt(player.getInt("competitions_won")); // win count
								writeInt(player.getInt("competitions_lost")); // lose count
								writeInt(player.getInt("olympiad_points")); // points
								writeInt(player.getInt("count")); // hero counts
								writeInt(player.getInt("legend_count")); // legend counts
								i++;
							}
						}
					}
					else
					{
						boolean found = false;
						final Map<Integer, StatSet> classList = new ConcurrentHashMap<>();
						int i = 1;
						for (Integer id : _playerList.keySet())
						{
							final StatSet set = _playerList.get(id);
							if (_player.getBaseClass() == set.getInt("classId"))
							{
								classList.put(i, _playerList.get(id));
								i++;
							}
						}
						for (Integer id : classList.keySet())
						{
							final StatSet player = classList.get(id);
							if (player.getInt("charId") == _player.getObjectId())
							{
								found = true;
								final int first = id > 10 ? (id - 9) : 1;
								final int last = classList.size() >= (id + 10) ? id + 10 : id + (classList.size() - id);
								if (first == 1)
								{
									writeInt(last - (first - 1));
								}
								else
								{
									writeInt(last - first);
								}
								for (int id2 = first; id2 <= last; id2++)
								{
									final StatSet plr = classList.get(id2);
									writeSizedString(plr.getString("name"));
									writeSizedString(plr.getString("clanName"));
									writeInt(id2); // class rank
									writeInt(id2);
									writeInt(Config.SERVER_ID);
									writeInt(player.getInt("level"));
									writeInt(player.getInt("classId"));
									writeInt(player.getInt("clanLevel")); // clan level
									writeInt(player.getInt("competitions_won")); // win count
									writeInt(player.getInt("competitions_lost")); // lose count
									writeInt(player.getInt("olympiad_points")); // points
									writeInt(player.getInt("count")); // hero counts
									writeInt(player.getInt("legend_count")); // legend counts
								}
							}
						}
						if (!found)
						{
							writeInt(0);
						}
					}
					break;
				}
			}
		}
	}
}
