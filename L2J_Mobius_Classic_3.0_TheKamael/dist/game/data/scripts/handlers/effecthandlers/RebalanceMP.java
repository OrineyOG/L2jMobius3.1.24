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

import org.l2jmobius.gameserver.model.Party;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.Summon;
import org.l2jmobius.gameserver.model.effects.AbstractEffect;
import org.l2jmobius.gameserver.model.effects.EffectType;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.util.Util;

/**
 * @author Rok
 */
public class RebalanceMP extends AbstractEffect
{
	public RebalanceMP(StatSet params)
	{
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.REBALANCE_MP;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, Item item)
	{
		if (!effector.isPlayer())
		{
			return;
		}
		
		double fullMP = 0;
		double currentMPs = 0;
		final Party party = effector.getParty();
		if (party != null)
		{
			// Calculate total MP and current MPs
			for (Player member : party.getMembers())
			{
				if (!member.isDead() && Util.checkIfInRange(skill.getAffectRange(), effector, member, true))
				{
					fullMP += member.getMaxMp();
					currentMPs += member.getCurrentMp();
				}
				
				final Summon summon = member.getPet();
				if ((summon != null) && (!summon.isDead() && Util.checkIfInRange(skill.getAffectRange(), effector, summon, true)))
				{
					fullMP += summon.getMaxMp();
					currentMPs += summon.getCurrentMp();
				}
				
				for (Summon servitors : member.getServitors().values())
				{
					if (!servitors.isDead() && Util.checkIfInRange(skill.getAffectRange(), effector, servitors, true))
					{
						fullMP += servitors.getMaxMp();
						currentMPs += servitors.getCurrentMp();
					}
				}
			}
			
			// Calculate the MP percentage
			final double percentMP = currentMPs / fullMP;
			
			// Redistribute MP
			for (Player member : party.getMembers())
			{
				if (!member.isDead() && Util.checkIfInRange(skill.getAffectRange(), effector, member, true))
				{
					double newMP = member.getMaxMp() * percentMP;
					member.setCurrentMp(newMP);
				}
				
				final Summon summon = member.getPet();
				if ((summon != null) && (!summon.isDead() && Util.checkIfInRange(skill.getAffectRange(), effector, summon, true)))
				{
					double newMP = summon.getMaxMp() * percentMP;
					summon.setCurrentMp(newMP);
				}
				
				for (Summon servitors : member.getServitors().values())
				{
					if (!servitors.isDead() && Util.checkIfInRange(skill.getAffectRange(), effector, servitors, true))
					{
						double newMP = servitors.getMaxMp() * percentMP;
						servitors.setCurrentMp(newMP);
					}
				}
			}
		}
	}
}
