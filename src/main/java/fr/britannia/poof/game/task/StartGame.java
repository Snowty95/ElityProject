package fr.britannia.poof.game.task;

import fr.britannia.poof.Poof;
import fr.britannia.poof.game.PoofGame;
import org.bukkit.Location;
import org.bukkit.entity.Sheep;
import org.bukkit.scheduler.BukkitRunnable;

public class StartGame extends BukkitRunnable
{
    private final PoofGame game;
    public StartGame(PoofGame game)
    {
        this.game = game;
    }
    
    @Override
    public void run()
    {
        if(this.game.getPlayer().getNearbyEntities(0, 0, 0).contains(this.game.getSheep()))
        {
            this.game.startTaskTeleportGame();
            cancel();
        }
    }
}
