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
package instances.GolbergRoom;

import org.l2jmobius.gameserver.model.Party;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.ItemHolder;
import org.l2jmobius.gameserver.model.instancezone.Instance;
import org.l2jmobius.gameserver.model.itemcontainer.Inventory;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;

import instances.AbstractInstance;

/**
 * @author RobikBobik, Mobius
 * @NOTE: Party instance retail like work.
 * @TODO: Find what all drops from GOLBERG_TREASURE_CHEST
 * @TODO: Golberg skills
 */
public class GolbergRoom extends AbstractInstance
{
	// NPCs
	private static final int SORA = 34091;
	private static final int GOLBERG = 18359;
	private static final int GOLBERG_TREASURE_CHEST = 18357;
	// Items
	private static final int GOLBERG_KEY_ROOM = 91636;
	// Misc
	private static final int TEMPLATE_ID = 207;
	// Reward
	private static final ItemHolder BEST_BOX_A = new ItemHolder(91012, 1);
	private static final ItemHolder WEAPON_BOX_A = new ItemHolder(49785, 1);
	private static final ItemHolder ARMOR_BOX_A = new ItemHolder(49786, 1);
	private static final ItemHolder REC_SOULSHOT_S = new ItemHolder(1808, 1);
	private static final ItemHolder REC_SPIRITSHOT_S = new ItemHolder(3036, 1);
	private static final ItemHolder REC_BLESSED_SPIRITSHOT_S = new ItemHolder(3957, 1);
	private static final ItemHolder REC_BLESSED_SOULSHOT_S = new ItemHolder(91489, 1);
	private static final ItemHolder REC_TATEOSSIAN_EARRING = new ItemHolder(6847, 1);
	private static final ItemHolder REC_TATEOSSIAN_RING = new ItemHolder(6849, 1);
	private static final ItemHolder REC_TATEOSSIAN_NECKLACE = new ItemHolder(6851, 1);
	private static final ItemHolder REC_IC_ARMOR = new ItemHolder(6853, 1);
	private static final ItemHolder REC_IC_GAITERS = new ItemHolder(6855, 1);
	private static final ItemHolder REC_IC_GAUNTLET = new ItemHolder(6857, 1);
	private static final ItemHolder REC_IC_BOOTS = new ItemHolder(6859, 1);
	private static final ItemHolder REC_IC_SHIELD = new ItemHolder(6861, 1);
	private static final ItemHolder REC_IC_HELMET = new ItemHolder(6863, 1);
	private static final ItemHolder REC_DRACO_ARMOR = new ItemHolder(6865, 1);
	private static final ItemHolder REC_DRACO_GLOVES = new ItemHolder(6867, 1);
	private static final ItemHolder REC_DRACO_BOOTS = new ItemHolder(6869, 1);
	private static final ItemHolder REC_DRACO_HELMET = new ItemHolder(6871, 1);
	private static final ItemHolder REC_MA_ARMOR = new ItemHolder(6873, 1);
	private static final ItemHolder REC_MA_GLOVES = new ItemHolder(6875, 1);
	private static final ItemHolder REC_MA_BOOTS = new ItemHolder(6877, 1);
	private static final ItemHolder REC_MA_HELMET = new ItemHolder(6879, 1);
	private static final ItemHolder REC_FORGOTTEN_BLADE = new ItemHolder(6881, 1);
	private static final ItemHolder REC_BASALT_BATTLEHAMMER = new ItemHolder(6883, 1);
	private static final ItemHolder REC_IMPERIAL_STAFF = new ItemHolder(6885, 1);
	private static final ItemHolder REC_ANGEL_SLAYER = new ItemHolder(6887, 1);
	private static final ItemHolder REC_DRAGON_HUNTER_AXE = new ItemHolder(6891, 1);
	private static final ItemHolder REC_SAINT_SPEAR = new ItemHolder(6893, 1);
	private static final ItemHolder REC_DEMON_SPLINTER = new ItemHolder(6895, 1);
	private static final ItemHolder REC_HEAVENS_DIVIDER = new ItemHolder(6897, 1);
	private static final ItemHolder REC_ARCANA_MACE = new ItemHolder(6899, 1);
	private static final ItemHolder REC_SHINING_ARROW = new ItemHolder(6901, 1);
	private static final ItemHolder REC_DRACONIC_BOW = new ItemHolder(7580, 1);
	private static final ItemHolder KEY_FORGOTTEN_BLADE = new ItemHolder(6688, 1);
	private static final ItemHolder KEY_BASALT_BATTLEHAMMER = new ItemHolder(6689, 1);
	private static final ItemHolder KEY_IMPERIAL_STAFF = new ItemHolder(6690, 1);
	private static final ItemHolder KEY_ANGEL_SLAYER = new ItemHolder(6691, 1);
	private static final ItemHolder KEY_DRAGON_HUNTER_AXE = new ItemHolder(6693, 1);
	private static final ItemHolder KEY_SAINT_SPEAR = new ItemHolder(6694, 1);
	private static final ItemHolder KEY_DEMON_SPLINTER = new ItemHolder(6695, 1);
	private static final ItemHolder KEY_HEAVENS_DIVIDER = new ItemHolder(6696, 1);
	private static final ItemHolder KEY_ARCANA_MACE = new ItemHolder(6697, 1);
	private static final ItemHolder KEY_TATEOSSIAN_EARRING = new ItemHolder(6697, 1);
	private static final ItemHolder KEY_TATEOSSIAN_RING = new ItemHolder(6697, 1);
	private static final ItemHolder KEY_TATEOSSIAN_NECKLACE = new ItemHolder(6697, 1);
	private static final ItemHolder KEY_IC_ARMOR = new ItemHolder(6701, 1);
	private static final ItemHolder KEY_IC_GAITERS = new ItemHolder(6702, 1);
	private static final ItemHolder KEY_IC_GAUNTLET = new ItemHolder(6703, 1);
	private static final ItemHolder KEY_IC_BOOTS = new ItemHolder(6704, 1);
	private static final ItemHolder KEY_IC_SHIELD = new ItemHolder(6705, 1);
	private static final ItemHolder KEY_IC_HELMET = new ItemHolder(6706, 1);
	private static final ItemHolder KEY_DRACO_ARMOR = new ItemHolder(6707, 1);
	private static final ItemHolder KEY_DRACO_GLOVES = new ItemHolder(6708, 1);
	private static final ItemHolder KEY_DRACO_BOOTS = new ItemHolder(6709, 1);
	private static final ItemHolder KEY_DRACO_HELMET = new ItemHolder(6710, 1);
	private static final ItemHolder KEY_MA_ARMOR = new ItemHolder(6711, 1);
	private static final ItemHolder KEY_MA_GLOVES = new ItemHolder(6712, 1);
	private static final ItemHolder KEY_MA_BOOTS = new ItemHolder(6713, 1);
	private static final ItemHolder KEY_MA_HELMET = new ItemHolder(6714, 1);
	private static final ItemHolder KEY_DRACONIC_BOW = new ItemHolder(7579, 1);
	private static final ItemHolder DRAGON_DYE_BOX = new ItemHolder(91011, 1);
	private static final ItemHolder REINFORCING_PLATE = new ItemHolder(5550, 1);
	private static final ItemHolder HIGH_GRADE_SUEDE = new ItemHolder(1885, 1);
	private static final ItemHolder CRAFTED_LEATHER = new ItemHolder(1894, 1);
	private static final ItemHolder METALLIC_FIBER = new ItemHolder(1895, 1);
	private static final ItemHolder REORINS_MOLD = new ItemHolder(5551, 1);
	private static final ItemHolder MAESTRO_MOLD = new ItemHolder(4048, 1);
	private static final ItemHolder MITHRIL_ALLOY = new ItemHolder(1890, 1);
	private static final ItemHolder VARNISH_OF_PURITY = new ItemHolder(1887, 1);
	private static final ItemHolder THONS = new ItemHolder(4044, 1);
	private static final ItemHolder ASOFE = new ItemHolder(4043, 1);
	private static final ItemHolder ARCSMITHS_ANVIL = new ItemHolder(5553, 1);
	private static final ItemHolder ORIHARUKON = new ItemHolder(1893, 1);
	private static final ItemHolder WARSMITHS_HOLDER = new ItemHolder(5554, 1);
	private static final ItemHolder WARSMITHS_MOLD = new ItemHolder(5552, 1);
	private static final ItemHolder SYNTHETIC_COKE = new ItemHolder(1888, 1);
	private static final ItemHolder COMPOUND_BRAID = new ItemHolder(1889, 1);
	private static final ItemHolder GEMSTONE_S = new ItemHolder(2134, 1);
	private static final ItemHolder CRYSTAL_S = new ItemHolder(1462, 2);
	private static final ItemHolder REWARD_100KK_ADENA = new ItemHolder(Inventory.ADENA_ID, 100000000);
	private static final ItemHolder REWARD_10KK_ADENA = new ItemHolder(Inventory.ADENA_ID, 10000000);
	private static final ItemHolder REWARD_1KK_ADENA = new ItemHolder(Inventory.ADENA_ID, 1000000);
	
	public GolbergRoom()
	{
		super(TEMPLATE_ID);
		addStartNpc(SORA);
		addKillId(GOLBERG, GOLBERG_TREASURE_CHEST);
		addInstanceLeaveId(TEMPLATE_ID);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		switch (event)
		{
			case "ENTER":
			{
				final Party party = player.getParty();
				if (party == null)
				{
					return "no_party.htm";
				}
				if (!hasQuestItems(player, GOLBERG_KEY_ROOM))
				{
					return "no_item.htm";
				}
				takeItems(player, GOLBERG_KEY_ROOM, 1);
				enterInstance(player, npc, TEMPLATE_ID);
				final Instance world = player.getInstanceWorld();
				if (world != null)
				{
					for (Player member : party.getMembers())
					{
						if (member == player)
						{
							continue;
						}
						member.teleToLocation(player, 10, world);
					}
					startQuestTimer("GOLBERG_MOVE", 5000, world.getNpc(GOLBERG), player);
				}
				break;
			}
			case "GOLBERG_MOVE":
			{
				final Instance world = player.getInstanceWorld();
				if (world != null)
				{
					player.sendPacket(new ExShowScreenMessage("Rats have become kings while I've been dormant.", 5000));
					startQuestTimer("NEXT_TEXT", 7000, world.getNpc(GOLBERG), player);
				}
				npc.moveToLocation(11711, -86508, -10928, 0);
				break;
			}
			case "NEXT_TEXT":
			{
				final Instance world = player.getInstanceWorld();
				if (world != null)
				{
					player.sendPacket(new ExShowScreenMessage("Zaken or whatever is going wild all over the southern sea.", 5000));
					startQuestTimer("NEXT_TEXT_2", 7000, world.getNpc(GOLBERG), player);
				}
				break;
			}
			case "NEXT_TEXT_2":
			{
				final Instance world = player.getInstanceWorld();
				if (world != null)
				{
					player.sendPacket(new ExShowScreenMessage("Who dare enter my place? Zaken sent you?", 5000));
				}
				break;
			}
			case "SPAWN_TRESURE":
			{
				final Instance world = player.getInstanceWorld();
				if (world == null)
				{
					return null;
				}
				
				if (world.getParameters().getInt("treasureCounter", 0) == 0)
				{
					world.getParameters().set("treasureCounter", 0);
				}
				
				if (player.isGM())
				{
					if (world.getParameters().getInt("treasureCounter", 0) <= 27)
					{
						addSpawn(GOLBERG_TREASURE_CHEST, 11708 + getRandom(-1000, 1000), -86505 + getRandom(-1000, 1000), -10928, 0, true, -1, true, player.getInstanceId());
						startQuestTimer("SPAWN_TRESURE", 1000, npc, player);
						world.getParameters().increaseInt("treasureCounter", 1);
					}
				}
				else if (player.getParty() != null)
				{
					switch (player.getParty().getMemberCount())
					{
						case 2:
						{
							if (world.getParameters().getInt("treasureCounter", 0) <= 1)
							{
								addSpawn(GOLBERG_TREASURE_CHEST, 11708 + getRandom(-1000, 1000), -86505 + getRandom(-1000, 1000), -10928, 0, true, -1, true, player.getInstanceId());
								startQuestTimer("SPAWN_TRESURE", 1000, npc, player);
								world.getParameters().increaseInt("treasureCounter", 1);
							}
							break;
						}
						case 3:
						{
							if (world.getParameters().getInt("treasureCounter", 0) <= 2)
							{
								addSpawn(GOLBERG_TREASURE_CHEST, 11708 + getRandom(-1000, 1000), -86505 + getRandom(-1000, 1000), -10928, 0, true, -1, true, player.getInstanceId());
								startQuestTimer("SPAWN_TRESURE", 1000, npc, player);
								world.getParameters().increaseInt("treasureCounter", 1);
							}
							break;
						}
						case 4:
						{
							if (world.getParameters().getInt("treasureCounter", 0) <= 4)
							{
								addSpawn(GOLBERG_TREASURE_CHEST, 11708 + getRandom(-1000, 1000), -86505 + getRandom(-1000, 1000), -10928, 0, true, -1, true, player.getInstanceId());
								startQuestTimer("SPAWN_TRESURE", 1000, npc, player);
								world.getParameters().increaseInt("treasureCounter", 1);
							}
							break;
						}
						case 5:
						{
							if (world.getParameters().getInt("treasureCounter", 0) <= 7)
							{
								addSpawn(GOLBERG_TREASURE_CHEST, 11708 + getRandom(-1000, 1000), -86505 + getRandom(-1000, 1000), -10928, 0, true, -1, true, player.getInstanceId());
								startQuestTimer("SPAWN_TRESURE", 1000, npc, player);
								world.getParameters().increaseInt("treasureCounter", 1);
							}
							break;
						}
						case 6:
						{
							if (world.getParameters().getInt("treasureCounter", 0) <= 10)
							{
								addSpawn(GOLBERG_TREASURE_CHEST, 11708 + getRandom(-1000, 1000), -86505 + getRandom(-1000, 1000), -10928, 0, true, -1, true, player.getInstanceId());
								startQuestTimer("SPAWN_TRESURE", 1000, npc, player);
								world.getParameters().increaseInt("treasureCounter", 1);
							}
							break;
						}
						case 7:
						{
							if (world.getParameters().getInt("treasureCounter", 0) <= 13)
							{
								addSpawn(GOLBERG_TREASURE_CHEST, 11708 + getRandom(-1000, 1000), -86505 + getRandom(-1000, 1000), -10928, 0, true, -1, true, player.getInstanceId());
								startQuestTimer("SPAWN_TRESURE", 1000, npc, player);
								world.getParameters().increaseInt("treasureCounter", 1);
							}
							break;
						}
						case 8:
						{
							if (world.getParameters().getInt("treasureCounter", 0) <= 16)
							{
								addSpawn(GOLBERG_TREASURE_CHEST, 11708 + getRandom(-1000, 1000), -86505 + getRandom(-1000, 1000), -10928, 0, true, -1, true, player.getInstanceId());
								startQuestTimer("SPAWN_TRESURE", 1000, npc, player);
								world.getParameters().increaseInt("treasureCounter", 1);
							}
							break;
						}
						case 9:
						{
							if (world.getParameters().getInt("treasureCounter", 0) <= 27)
							{
								addSpawn(GOLBERG_TREASURE_CHEST, 11708 + getRandom(-1000, 1000), -86505 + getRandom(-1000, 1000), -10928, 0, true, -1, true, player.getInstanceId());
								startQuestTimer("SPAWN_TRESURE", 1000, npc, player);
								world.getParameters().increaseInt("treasureCounter", 1);
							}
							break;
						}
					}
				}
				break;
			}
		}
		return null;
	}
	
	@Override
	public String onKill(Npc npc, Player player, boolean isSummon)
	{
		switch (npc.getId())
		{
			case GOLBERG:
			{
				startQuestTimer("SPAWN_TRESURE", 1000, npc, player);
				final Instance world = npc.getInstanceWorld();
				if (world != null)
				{
					world.finishInstance();
				}
				break;
			}
			case GOLBERG_TREASURE_CHEST:
			{
				final int rand = getRandom(10000);
				if (rand < 5)
				{
					giveItems(player, BEST_BOX_A);
				}
				else if (rand < 15)
				{
					giveItems(player, WEAPON_BOX_A);
				}
				else if (rand < 65)
				{
					giveItems(player, ARMOR_BOX_A);
				}
				else if (rand < 135)
				{
					giveItems(player, REC_SOULSHOT_S);
				}
				else if (rand < 205)
				{
					giveItems(player, REC_SPIRITSHOT_S);
				}
				else if (rand < 275)
				{
					giveItems(player, REC_BLESSED_SPIRITSHOT_S);
				}
				else if (rand < 345)
				{
					giveItems(player, REC_BLESSED_SOULSHOT_S);
				}
				else if (rand < 415)
				{
					giveItems(player, REC_TATEOSSIAN_EARRING);
				}
				else if (rand < 485)
				{
					giveItems(player, REC_TATEOSSIAN_RING);
				}
				else if (rand < 555)
				{
					giveItems(player, REC_TATEOSSIAN_NECKLACE);
				}
				else if (rand < 625)
				{
					giveItems(player, REC_IC_ARMOR);
				}
				else if (rand < 695)
				{
					giveItems(player, REC_IC_GAITERS);
				}
				else if (rand < 765)
				{
					giveItems(player, REC_IC_GAUNTLET);
				}
				else if (rand < 835)
				{
					giveItems(player, REC_IC_BOOTS);
				}
				else if (rand < 905)
				{
					giveItems(player, REC_IC_SHIELD);
				}
				else if (rand < 975)
				{
					giveItems(player, REC_IC_HELMET);
				}
				else if (rand < 1045)
				{
					giveItems(player, REC_DRACO_ARMOR);
				}
				else if (rand < 1115)
				{
					giveItems(player, REC_DRACO_GLOVES);
				}
				else if (rand < 1185)
				{
					giveItems(player, REC_DRACO_BOOTS);
				}
				else if (rand < 1255)
				{
					giveItems(player, REC_DRACO_HELMET);
				}
				else if (rand < 1325)
				{
					giveItems(player, REC_MA_ARMOR);
				}
				else if (rand < 1395)
				{
					giveItems(player, REC_MA_GLOVES);
				}
				else if (rand < 1465)
				{
					giveItems(player, REC_MA_BOOTS);
				}
				else if (rand < 1535)
				{
					giveItems(player, REC_MA_HELMET);
				}
				else if (rand < 1605)
				{
					giveItems(player, REC_FORGOTTEN_BLADE);
				}
				else if (rand < 1675)
				{
					giveItems(player, REC_BASALT_BATTLEHAMMER);
				}
				else if (rand < 1745)
				{
					giveItems(player, REC_IMPERIAL_STAFF);
				}
				else if (rand < 1815)
				{
					giveItems(player, REC_ANGEL_SLAYER);
				}
				else if (rand < 1885)
				{
					giveItems(player, REC_DRAGON_HUNTER_AXE);
				}
				else if (rand < 1955)
				{
					giveItems(player, REC_SAINT_SPEAR);
				}
				else if (rand < 2025)
				{
					giveItems(player, REC_DEMON_SPLINTER);
				}
				else if (rand < 2095)
				{
					giveItems(player, REC_HEAVENS_DIVIDER);
				}
				else if (rand < 2165)
				{
					giveItems(player, REC_ARCANA_MACE);
				}
				else if (rand < 2235)
				{
					giveItems(player, REC_SHINING_ARROW);
				}
				else if (rand < 2305)
				{
					giveItems(player, REC_DRACONIC_BOW);
				}
				else if (rand < 2405)
				{
					giveItems(player, KEY_FORGOTTEN_BLADE);
				}
				else if (rand < 2505)
				{
					giveItems(player, KEY_BASALT_BATTLEHAMMER);
				}
				else if (rand < 2605)
				{
					giveItems(player, KEY_IMPERIAL_STAFF);
				}
				else if (rand < 2705)
				{
					giveItems(player, KEY_ANGEL_SLAYER);
				}
				else if (rand < 2805)
				{
					giveItems(player, KEY_DRAGON_HUNTER_AXE);
				}
				else if (rand < 2905)
				{
					giveItems(player, KEY_SAINT_SPEAR);
				}
				else if (rand < 3005)
				{
					giveItems(player, KEY_DEMON_SPLINTER);
				}
				else if (rand < 3105)
				{
					giveItems(player, KEY_HEAVENS_DIVIDER);
				}
				else if (rand < 3205)
				{
					giveItems(player, KEY_ARCANA_MACE);
				}
				else if (rand < 3305)
				{
					giveItems(player, KEY_TATEOSSIAN_EARRING);
				}
				else if (rand < 3405)
				{
					giveItems(player, KEY_TATEOSSIAN_RING);
				}
				else if (rand < 3505)
				{
					giveItems(player, KEY_TATEOSSIAN_NECKLACE);
				}
				else if (rand < 3605)
				{
					giveItems(player, KEY_IC_ARMOR);
				}
				else if (rand < 3705)
				{
					giveItems(player, KEY_IC_GAITERS);
				}
				else if (rand < 3805)
				{
					giveItems(player, KEY_IC_GAUNTLET);
				}
				else if (rand < 3905)
				{
					giveItems(player, KEY_IC_BOOTS);
				}
				else if (rand < 4005)
				{
					giveItems(player, KEY_IC_SHIELD);
				}
				else if (rand < 4105)
				{
					giveItems(player, KEY_IC_HELMET);
				}
				else if (rand < 4205)
				{
					giveItems(player, KEY_DRACO_ARMOR);
				}
				else if (rand < 4305)
				{
					giveItems(player, KEY_DRACO_GLOVES);
				}
				else if (rand < 4405)
				{
					giveItems(player, KEY_DRACO_BOOTS);
				}
				else if (rand < 4505)
				{
					giveItems(player, KEY_DRACO_HELMET);
				}
				else if (rand < 4605)
				{
					giveItems(player, KEY_MA_ARMOR);
				}
				else if (rand < 4705)
				{
					giveItems(player, KEY_MA_GLOVES);
				}
				else if (rand < 4805)
				{
					giveItems(player, KEY_MA_BOOTS);
				}
				else if (rand < 4905)
				{
					giveItems(player, KEY_MA_HELMET);
				}
				else if (rand < 5005)
				{
					giveItems(player, KEY_DRACONIC_BOW);
				}
				else if (rand < 5085)
				{
					giveItems(player, DRAGON_DYE_BOX);
				}
				else if (rand < 5285)
				{
					giveItems(player, REINFORCING_PLATE);
				}
				else if (rand < 5485)
				{
					giveItems(player, HIGH_GRADE_SUEDE);
				}
				else if (rand < 5685)
				{
					giveItems(player, CRAFTED_LEATHER);
				}
				else if (rand < 5885)
				{
					giveItems(player, METALLIC_FIBER);
				}
				else if (rand < 6085)
				{
					giveItems(player, REORINS_MOLD);
				}
				else if (rand < 6285)
				{
					giveItems(player, MAESTRO_MOLD);
				}
				else if (rand < 6485)
				{
					giveItems(player, MITHRIL_ALLOY);
				}
				else if (rand < 6685)
				{
					giveItems(player, VARNISH_OF_PURITY);
				}
				else if (rand < 6885)
				{
					giveItems(player, THONS);
				}
				else if (rand < 7085)
				{
					giveItems(player, ASOFE);
				}
				else if (rand < 7285)
				{
					giveItems(player, ARCSMITHS_ANVIL);
				}
				else if (rand < 7485)
				{
					giveItems(player, ORIHARUKON);
				}
				else if (rand < 7685)
				{
					giveItems(player, WARSMITHS_HOLDER);
				}
				else if (rand < 7885)
				{
					giveItems(player, WARSMITHS_MOLD);
				}
				else if (rand < 8085)
				{
					giveItems(player, SYNTHETIC_COKE);
				}
				else if (rand < 8285)
				{
					giveItems(player, COMPOUND_BRAID);
				}
				else if (rand < 8785)
				{
					giveItems(player, GEMSTONE_S);
				}
				else if (rand < 9199)
				{
					giveItems(player, CRYSTAL_S);
				}
				else if (rand < 9200)
				{
					giveItems(player, REWARD_100KK_ADENA);
				}
				else if (rand < 9500)
				{
					giveItems(player, REWARD_10KK_ADENA);
				}
				else if (rand < 10000)
				{
					giveItems(player, REWARD_1KK_ADENA);
				}
				break;
			}
		}
		return super.onKill(npc, player, isSummon);
	}
	
	public static void main(String[] args)
	{
		new GolbergRoom();
	}
}
