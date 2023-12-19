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
package handlers.effecthandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.l2jmobius.commons.util.Rnd;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.effects.AbstractEffect;
import org.l2jmobius.gameserver.model.effects.EffectType;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.skill.AbnormalType;
import org.l2jmobius.gameserver.model.skill.BuffInfo;
import org.l2jmobius.gameserver.model.skill.Skill;

/**
 * Dispel By Slot Probability effect implementation.
 * @author Adry_85, Zoey76
 */
public class DispelBySlotProbability extends AbstractEffect
{
	private final Set<AbnormalType> _dispelAbnormals;
	private final int _rate;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	public static final Map<Creature, List<BuffInfo>> canceledBuffs = new HashMap<>();
	
	public DispelBySlotProbability(StatSet params)
	{
		final String[] dispelEffects = params.getString("dispel").split(";");
		_rate = params.getInt("rate", 100);
		_dispelAbnormals = new HashSet<>(dispelEffects.length);
		for (String slot : dispelEffects)
		{
			_dispelAbnormals.add(Enum.valueOf(AbnormalType.class, slot));
		}
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.DISPEL;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, Item item)
	{
		if (effected == null)
		{
			return;
		}
		
		List<BuffInfo> toCancel = new ArrayList<>();
		
		effected.getEffectList().stopEffects(info ->
		{
			if (!info.getSkill().isIrreplacableBuff() && (Rnd.get(100) < _rate) && _dispelAbnormals.contains(info.getSkill().getAbnormalType()))
			{
				toCancel.add(info);
				return true;
			}
			return false;
		}, true, true);
		
		// Store canceled buffs and their remaining time
		canceledBuffs.put(effected, new ArrayList<>(toCancel));
		
		for (BuffInfo infoToCancel : toCancel)
		{
			// Schedule the restoration of the canceled buff
			scheduler.schedule(() ->
			{
				if ((infoToCancel.getTime() > 0) || (infoToCancel.getTime() < 0))
				{
					effected.getEffectList().add(infoToCancel);
				}
			}, 30, TimeUnit.SECONDS);
		}
	}
}
