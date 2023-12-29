package org.l2jmobius.charstats;

/**
 * @author Gabriel Costa Souza
 * Discord: gabsoncs
 * Skype - email: gabriel_costa25@hotmail.com
 * website: l2jgabdev.com
 */

import java.text.NumberFormat;
import java.util.Locale;

import org.l2jmobius.commons.util.BBS;
import org.l2jmobius.gameserver.cache.HtmCache;
import org.l2jmobius.gameserver.enums.AttributeType;
import org.l2jmobius.gameserver.handler.IVoicedCommandHandler;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.item.Armor;
import org.l2jmobius.gameserver.model.item.ItemTemplate;
import org.l2jmobius.gameserver.model.item.type.ArmorType;
import org.l2jmobius.gameserver.model.stats.BaseStat;
import org.l2jmobius.gameserver.model.stats.Stat;

public class CharStatsVCmd implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"getstat",
		"stat",
		"getstats"
	};
	
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target)
	{
		if (command.startsWith("getstat") || command.startsWith("stat"))
		{
			calculateStats(activeChar);
		}
		
		return true;
	}
	
	private void calculateStats(Player player)
	{
		Creature target = null;
		
		double hpRegen = player.getStat().getHpRegen();
		double cpRegen = player.getStat().getCpRegen();
		double mpRegen = player.getStat().getMpRegen();
		
		double hpDrain = player.getStat().getValue(Stat.ABSORB_DAMAGE_PERCENT, 0.);// player.getStat().getValue(Stat.ABSORB_DAMAGE_PERCENT, 0., target, null);
		double mpDrain = player.getStat().getValue(Stat.ABSORB_MANA_DAMAGE_PERCENT, 0.);
		double hpGain = player.getStat().getValue(Stat.HEAL_EFFECT, 100.);
		double mpGain = player.getStat().getValue(Stat.MANA_CHARGE, 100.);
		double critPerc = 100 - (player.getStat().getValue(Stat.CRITICAL_DAMAGE, 1) * 100);
		double critStatic = player.getStat().getValue(Stat.CRITICAL_DAMAGE_ADD, 0);
		// double mCritRate = (player.getStat().getValue(Stat.MCRITICAL_RATE, 1, target, null)) * 10;
		double mCritRate = player.getStat().getMCriticalHit();
		double blowRate = player.getStat().getValue(Stat.BLOW_RATE, 0);
		
		ItemTemplate shld = player.getSecondaryWeaponItem();
		boolean shield = (shld != null) && (shld instanceof Armor) && (((Armor) shld).getItemType() != ArmorType.SIGIL);
		
		double shieldDef = player.getShldDef();
		// double shieldRate = shield ? player.getStat().getValue(Stat.SHIELD_DEFENCE_RATE, 0) : 0.;
		double shieldRate = player.getStat().getValue(Stat.SHIELD_DEFENCE_RATE) * BaseStat.CON.calcBonus(target);
		double fireResist = player.getDefenseElementValue(AttributeType.FIRE);
		double windResist = player.getDefenseElementValue(AttributeType.WIND);
		double waterResist = player.getDefenseElementValue(AttributeType.WATER);
		double earthResist = player.getDefenseElementValue(AttributeType.EARTH);
		double holyResist = player.getDefenseElementValue(AttributeType.HOLY);
		double unholyResist = player.getDefenseElementValue(AttributeType.DARK);
		
		double healReceiveBonus = player.getStat().getValue(Stat.HEAL_EFFECT, 1);
		double healReceiveFlatBonus = player.getStat().getValue(Stat.HEAL_EFFECT_ADD, 0);
		
		double expBonus = player.getStat().getExpBonusMultiplier();
		double spBonus = player.getStat().getSpBonusMultiplier();
		
		double pvpMagicDamage = player.getStat().getMul(Stat.PVP_MAGICAL_SKILL_DAMAGE, 1);
		double pvpPhysicalDamage = player.getStat().getMul(Stat.PVP_PHYSICAL_ATTACK_DAMAGE, 1);
		
		double pveMagicDamage = player.getStat().getMul(Stat.PVE_MAGICAL_SKILL_DAMAGE, 1);
		double pvePhysicalDamage = player.getStat().getMul(Stat.PVE_PHYSICAL_ATTACK_DAMAGE, 1);
		
		double critChanceResist = 100 - (player.getStat().getValue(Stat.DEFENCE_CRITICAL_RATE, 1) * 100);
		double critDamResist = 100 - (player.getStat().getValue(Stat.DEFENCE_CRITICAL_DAMAGE, 1) * 100);
		
		NumberFormat df = NumberFormat.getInstance(Locale.ENGLISH);
		df.setMaximumFractionDigits(1);
		df.setMinimumFractionDigits(1);
		
		String html = HtmCache.getInstance().getHtm(player, "data/html/gabriel/characterStats.htm");
		
		html = html.replace("%hpRegen%", df.format(hpRegen));
		html = html.replace("%pvp%", String.valueOf(player.getPvpKills()));
		html = html.replace("%pk%", String.valueOf(player.getPkKills()));
		html = html.replace("%cpRegen%", df.format(cpRegen));
		html = html.replace("%mpRegen%", df.format(mpRegen));
		html = html.replace("%hpDrain%", df.format(hpDrain));
		html = html.replace("%mpDrain%", df.format(mpDrain));
		html = html.replace("%hpGain%", df.format(hpGain));
		html = html.replace("%mpGain%", df.format(mpGain));
		html = html.replace("%critPerc%", df.format(critPerc));
		html = html.replace("%critStatic%", df.format(critStatic));
		html = html.replace("%mCritRate%", df.format(mCritRate));
		html = html.replace("%blowRate%", df.format(blowRate));
		html = html.replace("%shieldDef%", df.format(shieldDef));
		html = html.replace("%shieldRate%", df.format(shieldRate));
		html = html.replace("%fireResist%", df.format(fireResist));
		html = html.replace("%windResist%", df.format(windResist));
		html = html.replace("%waterResist%", df.format(waterResist));
		html = html.replace("%earthResist%", df.format(earthResist));
		html = html.replace("%holyResist%", df.format(holyResist));
		html = html.replace("%darkResist%", df.format(unholyResist));
		
		html = html.replace("%healReceiveBonus%", df.format(healReceiveBonus));
		html = html.replace("%healReceiveFlatBonus%", df.format(healReceiveFlatBonus));
		
		html = html.replace("%expBonus%", df.format(expBonus));
		html = html.replace("%spBonus%", df.format(spBonus));
		
		html = html.replace("%pvpMagicDamage%", df.format(pvpMagicDamage));
		html = html.replace("%pvpPhysicalDamage%", df.format(pvpPhysicalDamage));
		
		html = html.replace("%pveMagicDamage%", df.format(pveMagicDamage));
		html = html.replace("%pvePhysicalDamage%", df.format(pvePhysicalDamage));
		html = html.replace("%critChanceResist%", df.format(critChanceResist));
		html = html.replace("%critDamResist%", df.format(critDamResist));
		
		BBS.separateAndSend(html, player);
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}