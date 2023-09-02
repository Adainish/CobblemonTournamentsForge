package io.github.adainish.cobblemontournamentsforge.managers;

import io.github.adainish.cobblemontournamentsforge.obj.data.Reward;

import java.util.HashMap;

public class RewardsManager
{
    public HashMap<String, Reward> rewardMap = new HashMap<>();

    public RewardsManager()
    {

    }

    public void init()
    {
        //init default rewards
        if (this.rewardMap.isEmpty())
        {
            Reward example = new Reward("example");
            this.rewardMap.put(example.id, example);
        }
    }
}
