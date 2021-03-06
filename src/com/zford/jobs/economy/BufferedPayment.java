package com.zford.jobs.economy;

import java.util.HashMap;
import java.util.Map.Entry;

import com.zford.jobs.config.JobsConfiguration;
import com.zford.jobs.config.container.JobsPlayer;
import com.zford.jobs.economy.link.EconomyLink;

public class BufferedPayment {
    
    private HashMap<String, Double> payments;
    public BufferedPayment() {
        payments = new HashMap<String, Double>();
    }

    /**
     * Add payment to player's payment buffer
     * @param player - player to be paid
     * @param amount - amount to be paid
     */
    public void pay(JobsPlayer player, double amount) {
        double total = 0;
        String playername = player.getName();
        if (payments.containsKey(playername))
            total = payments.get(playername);
        
        total += amount;
        payments.put(player.getName(), total);
    }
    
    /**
     * Payout all players the amount they are going to be paid
     */
    public void payAll() {
        if (payments.isEmpty())
            return;
        EconomyLink economy = JobsConfiguration.getInstance().getEconomyLink();
        if (economy != null) {
            for (Entry<String, Double> entry : payments.entrySet()) {
                String playername = entry.getKey();
                double total = entry.getValue();
                economy.pay(playername, total);
                economy.updateStats(playername);
            }
        }
        payments.clear();
    }
}
