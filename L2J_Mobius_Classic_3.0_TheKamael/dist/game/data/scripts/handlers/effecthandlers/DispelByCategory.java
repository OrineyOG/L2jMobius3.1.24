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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.l2jmobius.gameserver.enums.DispelSlotType;
import org.l2jmobius.gameserver.enums.SkillFinishType;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.effects.AbstractEffect;
import org.l2jmobius.gameserver.model.effects.EffectType;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.skill.BuffInfo;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.model.stats.Formulas;

/**
 * Dispel By Category effect implementation.
 * @author DS, Adry_85, Oriney
 */
public class DispelByCategory extends AbstractEffect
{
	private final DispelSlotType _slot;
	private final int _rate;
	private final int _max;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	public static final Map<Creature, List<BuffInfo>> canceledBuffs = new HashMap<>();
	
	public DispelByCategory(StatSet params)
	{
		_slot = params.getEnum("slot", DispelSlotType.class, DispelSlotType.BUFF);
		_rate = params.getInt("rate", 0);
		_max = params.getInt("max", 0);
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
		if (effected.isDead())
		{
			return;
		}
		
		// Get all cancelable buffs
		final List<BuffInfo> toCancel = Formulas.calcCancelStealEffects(effector, effected, skill, _slot, _rate, _max);
		
		// If no buffs to cancel, return early
		if (toCancel.isEmpty())
		{
			return;
		}
		
		// Store canceled buffs and their remaining time
		canceledBuffs.put(effected, new ArrayList<>(toCancel));
		
		for (BuffInfo infoToCancel : toCancel)
		{
			// Remove the canceled buff from the effected.
			effected.getEffectList().stopSkillEffects(SkillFinishType.REMOVED, infoToCancel.getSkill());
			
			// Schedule the restoration of the canceled buff
			scheduler.schedule(() ->
			{
				// Debug message to print out the buff and its duration
				System.out.println("Attempting to restore buff: " + infoToCancel.getSkill().getName() + ", Duration: " + infoToCancel.getTime());
				
				// Check if the buff's remaining time is greater than 0 or it's an infinite buff (negative)
				if ((infoToCancel.getTime() > 0) || (infoToCancel.getTime() < 0))
				{
					// Debug message to indicate successful restoration
					System.out.println("Restoring buff: " + infoToCancel.getSkill().getName());
					
					// Restore the buff
					effected.getEffectList().add(infoToCancel);
				}
				else
				{
					// Debug message to indicate failure to restore
					System.out.println("Failed to restore buff: " + infoToCancel.getSkill().getName());
				}
			}, 30, TimeUnit.SECONDS); // 30 seconds to restore
		}
	}
}