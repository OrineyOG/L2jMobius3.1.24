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
package handlers.communityboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.l2jmobius.commons.database.DatabaseFactory;
import org.l2jmobius.gameserver.cache.HtmCache;
import org.l2jmobius.gameserver.data.sql.ClanTable;
import org.l2jmobius.gameserver.data.xml.ClanHallData;
import org.l2jmobius.gameserver.enums.ClanWarState;
import org.l2jmobius.gameserver.handler.CommunityBoardHandler;
import org.l2jmobius.gameserver.handler.IParseBoardHandler;
import org.l2jmobius.gameserver.instancemanager.CastleManager;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.clan.Clan;
import org.l2jmobius.gameserver.model.clan.ClanWar;
import org.l2jmobius.gameserver.model.residences.ClanHall;
import org.l2jmobius.gameserver.model.siege.Castle;
import org.l2jmobius.gameserver.model.skill.Skill;

public class ClanBoardCustom implements IParseBoardHandler
{
	private static final String CLAN_HOME_PATH = "data/html/CommunityBoard/Custom/clanHome.html";
	private static final String CLAN_ROW_PATH = "data/html/CommunityBoard/Custom/clan/clanRow.html";
	private static final String CLAN_DETAIL_PATH = "data/html/CommunityBoard/Custom/clan/clanDetail.html";
	private static final String WAR_MUTUAL_PATH = "data/html/CommunityBoard/Custom/clan/clanWarMutual.html";
	private static final String WAR_BLOOD_DECLARATION_PATH = "data/html/CommunityBoard/Custom/clan/clanWarBloodDeclaration.html";
	private static final String WAR_DECLARATION_PATH = "data/html/CommunityBoard/Custom/clan/clanWarDeclaration.html";
	int totalClans = ClanTable.getInstance().getClanCount();
	private static final int CLANS_PER_PAGE = 10;
	int totalPages = (int) Math.ceil((double) totalClans / CLANS_PER_PAGE);
	
	// Default ClanBoard Commands
	private static final String[] COMMANDS =
	{
		"_bbsclan",
	};
	// Custom ClanBoard Commands, with conditions (if any)
	private static final String[] CUSTOM_COMMANDS =
	{
		// Example: Config.CLANSYSTEM_ENABLED ? "_bbsclaninvite" : null,
		// Add other custom commands based on your game's configurations
	};
	
	@Override
	public String[] getCommunityBoardCommands()
	{
		final List<String> commands = new ArrayList<>();
		commands.addAll(Arrays.asList(COMMANDS));
		commands.addAll(Arrays.asList(CUSTOM_COMMANDS));
		return commands.stream().filter(Objects::nonNull).toArray(String[]::new);
	}
	
	private String replacePlaceholders(String html, Clan clan)
	{
		int clanId = clan.getId(); // This method needs to exist in your Clan class to return the clan's ID.
		int totalPvPKills = getTotalPvPKillsForClan(clanId);
		
		String replacedHtml = html.replace("%clan_name%", clan.getName()).replace("%clan_level%", Integer.toString(clan.getLevel())).replace("%clan_leader%", clan.getLeaderName()).replace("%total_pvp_kills%", Integer.toString(totalPvPKills)).replace("%total_members%", Integer.toString(clan.getMembersCount())).replace("%online_members%", Integer.toString(clan.getOnlineMembersCount()));
		
		// Additional placeholders for Base, Clan Hall, Alliance
		Castle castle = CastleManager.getInstance().getCastleByOwner(clan);
		String base = castle != null ? castle.getName() : "None";
		ClanHall clanHall = ClanHallData.getInstance().getClanHallByClan(clan);
		String clanHallName = clanHall != null ? clanHall.getName() : "None";
		String allyName = clan.getAllyName();
		allyName = (allyName != null) ? allyName : "None";
		
		replacedHtml = replacedHtml.replace("%base%", base).replace("%clan_hall%", clanHallName).replace("%ally_name%", allyName);
		
		// Replace the placeholder with the active clan wars for each type
		String activeWarsDeclarationHtml = generateClanWarInfo(clan, ClanWarState.DECLARATION);
		String activeWarsBloodDeclarationHtml = generateClanWarInfo(clan, ClanWarState.BLOOD_DECLARATION);
		String activeWarsMutualHtml = generateClanWarInfo(clan, ClanWarState.MUTUAL);
		
		replacedHtml = replacedHtml.replace("%clan_war_declaration%", activeWarsDeclarationHtml);
		replacedHtml = replacedHtml.replace("%clan_war_blood_declaration%", activeWarsBloodDeclarationHtml);
		replacedHtml = replacedHtml.replace("%clan_war_mutual%", activeWarsMutualHtml);
		
		String clanSkillsHtml = generateClanSkillsHtml(clan);
		replacedHtml = replacedHtml.replace("%clan_skills%", clanSkillsHtml);
		
		return replacedHtml;
	}
	
	@Override
	public boolean parseCommunityBoardCommand(String command, Player player)
	{
		String returnHtml = null;
		
		// Extract page number from the command (assuming format like "_bbsclan;1" for page 1)
		String[] split = command.split(";");
		int pageNumber = 1; // default to page 1
		if (split.length > 1)
		{
			try
			{
				pageNumber = Integer.parseInt(split[1]);
			}
			catch (NumberFormatException e)
			{
				// Handle potential non-numeric value
				// Perhaps log an error or default to page 1
			}
		}
		
		switch (split[0])
		{
			case "_bbsclan":
			case "_bbsclanlist":
				returnHtml = generateAllClansInfo(pageNumber);
				CommunityBoardHandler.getInstance().addBypass(player, "Clan", command);
				CommunityBoardHandler.separateAndSend(returnHtml, player);
				return true;
			
			case "_bbsclandetail":
				int clanId = Integer.parseInt(split[1]);
				Clan detailedClan = ClanTable.getInstance().getClan(clanId);
				if (detailedClan != null)
				{
					returnHtml = generateClanDetailInfo(detailedClan);
					CommunityBoardHandler.getInstance().addBypass(player, "ClanDetail", command);
					CommunityBoardHandler.separateAndSend(returnHtml, player);
					return true;
				}
				break;
			
			// ... (handle other commands)
		}
		
		return true;
	}
	
	private String generateClanDetailInfo(Clan clan)
	{
		String clanDetailTemplate = HtmCache.getInstance().getHtm(null, CLAN_DETAIL_PATH);
		return replacePlaceholders(clanDetailTemplate, clan);
	}
	
	private String generateAllClansInfo(int pageNumber)
	{
		String mainTemplate = HtmCache.getInstance().getHtm(null, CLAN_HOME_PATH);
		String clanRowTemplate = HtmCache.getInstance().getHtm(null, CLAN_ROW_PATH);
		
		List<Clan> allClans = new ArrayList<>(ClanTable.getInstance().getClans());
		// Sort the list by clan level in descending order
		allClans.sort((clan1, clan2) -> Integer.compare(clan2.getLevel(), clan1.getLevel()));
		int start = (pageNumber - 1) * CLANS_PER_PAGE;
		int end = Math.min(start + CLANS_PER_PAGE, allClans.size());
		
		StringBuilder clanRows = new StringBuilder();
		for (int i = start; i < end; i++)
		{
			Clan clan = allClans.get(i);
			int rank = start + i + 1; // Calculate rank based on the current iteration starting from the 'start' index
			String singleClanHtml = replacePlaceholders(clanRowTemplate, clan);
			singleClanHtml = singleClanHtml.replace("%clan_link%", "_bbsclandetail;" + clan.getId());
			singleClanHtml = singleClanHtml.replace("%clan_rank%", Integer.toString(rank)); // Replace the rank placeholder
			clanRows.append(singleClanHtml);
		}
		
		String paginationHtml = generatePageNumbers(pageNumber, totalPages);
		mainTemplate = mainTemplate.replace("%all_clans_data%", clanRows.toString()).replace("%page_numbers%", paginationHtml);
		
		return mainTemplate;
	}
	
	private String generatePageNumbers(int currentPage, int totalPages)
	{
		StringBuilder pagesHtml = new StringBuilder();
		for (int i = 1; i <= totalPages; i++)
		{
			pagesHtml.append("<td>");
			if (i == currentPage)
			{
				// For the current page, display the number with brackets
				pagesHtml.append("<button value=\"[").append(i).append("]\" width=26 height=17 back=\"L2UI_CT1.ListCTRL_DF_Title_Down\" fore=\"L2UI_CT1.ListCTRL_DF_Title\">");
			}
			else
			{
				// For other pages, display the number without brackets
				pagesHtml.append("<button value=\"").append(i).append("\" action=\"bypass _bbsclanlist;").append(i).append("\" width=26 height=17 back=\"L2UI_CT1.ListCTRL_DF_Title_Down\" fore=\"L2UI_CT1.ListCTRL_DF_Title\">");
			}
			pagesHtml.append("</td>");
		}
		return pagesHtml.toString();
	}
	
	private String generateClanWarInfo(Clan clan, ClanWarState warStateFilter)
	{
		StringBuilder activeWarsBuilder = new StringBuilder();
		
		// Get the list of ClanWar objects involving this clan
		Map<Integer, ClanWar> activeWars = clan.getWarList();
		
		for (Map.Entry<Integer, ClanWar> warEntry : activeWars.entrySet())
		{
			ClanWar war = warEntry.getValue();
			ClanWarState state = war.getClanWarState(clan);
			
			// Only process wars matching the specified warStateFilter
			if (state != warStateFilter)
			{
				continue;
			}
			
			String templatePath = WarStateTemplate(state);
			if (templatePath == null)
			{
				continue; // No template for this state, skip it
			}
			
			String template = HtmCache.getInstance().getHtm(null, templatePath);
			
			Clan opposingClan = ClanTable.getInstance().getClan(warEntry.getKey());
			
			if (opposingClan != null)
			{
				String row = template.replace("%ClanName%", opposingClan.getName());
				activeWarsBuilder.append(row);
			}
		}
		
		return activeWarsBuilder.toString();
	}
	
	private String WarStateTemplate(ClanWarState state)
	{
		switch (state)
		{
			case DECLARATION:
				return WAR_DECLARATION_PATH;
			case BLOOD_DECLARATION:
				return WAR_BLOOD_DECLARATION_PATH;
			case MUTUAL:
				return WAR_MUTUAL_PATH;
			default:
				return null; // No template for this state
		}
	}
	
	private String generateClanSkillsHtml(Clan clan)
	{
		Map<Integer, Skill> skillsMap = clan.getSkills();
		Collection<Skill> skills = skillsMap.values();
		StringBuilder skillsHtmlBuilder = new StringBuilder();
		
		int counter = 0;
		for (Skill skill : skills)
		{
			if ((counter % 3) == 0)
			{
				// Start a new cell after every 3 skills
				if (counter > 0)
				{
					// Close the previous cell if not the first skill
					skillsHtmlBuilder.append("</td>\n");
				}
				// Start a new cell
				skillsHtmlBuilder.append("<td valign='top' align='center'>\n");
			}
			
			String skillNameForIcon = skill.getName().toLowerCase().replace(" ", "_").replace(".", "");
			int skillLevel = skill.getLevel();
			
			// Append the HTML for the skill's icon
			skillsHtmlBuilder.append(String.format("<br><img src=Evo_Community.%s_%d width=32 height=32>\n", skillNameForIcon, skillLevel));
			
			counter++;
		}
		
		// Close the last cell if it was opened
		if ((counter % 3) != 0)
		{
			skillsHtmlBuilder.append("</td>\n");
		}
		return skillsHtmlBuilder.toString();
	}
	
	public int getTotalPvPKillsForClan(int clanId)
	{
		int totalPvPKills = 0;
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT SUM(pvpkills) AS totalPvPKills FROM characters WHERE clanid = ?"))
		{
			ps.setInt(1, clanId);
			
			try (ResultSet rs = ps.executeQuery())
			{
				if (rs.next())
				{
					totalPvPKills = rs.getInt("totalPvPKills");
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return totalPvPKills;
	}
	
	// ... (other necessary methods)
}
