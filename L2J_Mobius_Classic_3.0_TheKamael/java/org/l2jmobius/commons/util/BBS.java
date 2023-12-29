package org.l2jmobius.commons.util;

import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.serverpackets.ShowBoard;

/**
 * @author Gabriel Costa Souza Discord: gabsoncs Skype - email: gabriel_costa25@hotmail.com website: l2jgabdev.com
 */
public class BBS
{
	public static void separateAndSend(String html, Player acha)
	{
		if (html == null)
		{
			return;
		}
		html = html.replace("\t", "");
		
		if (html.length() < 16250)
		{
			acha.sendPacket(new ShowBoard(html, "101"));
			acha.sendPacket(new ShowBoard("", "102"));
			acha.sendPacket(new ShowBoard("", "103"));
		}
		else if (html.length() < (16250 * 2))
		{
			acha.sendPacket(new ShowBoard(html.substring(0, 16250), "101"));
			acha.sendPacket(new ShowBoard(html.substring(16250), "102"));
			acha.sendPacket(new ShowBoard("", "103"));
		}
		else if (html.length() < (16250 * 3))
		{
			acha.sendPacket(new ShowBoard(html.substring(0, 16250), "101"));
			acha.sendPacket(new ShowBoard(html.substring(16250, 16250 * 2), "102"));
			acha.sendPacket(new ShowBoard(html.substring(16250 * 2), "103"));
		}
		else
		{
			acha.sendPacket(new ShowBoard("<html><body><br><center>Error: HTML was too long!</center></body></html>", "101"));
			acha.sendPacket(new ShowBoard("", "102"));
			acha.sendPacket(new ShowBoard("", "103"));
		}
		
		if (acha.isGM())
		{
			acha.sendMessage("HtmlLength: " + html.length());
		}
		
	}
}
