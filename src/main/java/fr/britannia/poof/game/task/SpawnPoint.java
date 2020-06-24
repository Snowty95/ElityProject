package fr.britannia.poof.game.task;

import fr.britannia.poof.game.PoofGame;
import fr.britannia.poof.tools.PoofPoint;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnPoint extends BukkitRunnable
{
    private final PoofGame game;
    public SpawnPoint(PoofGame game) { this.game = game; }
    
    @Override
    public void run()
    {
        if(this.game.getSlimes().size() >= 3)
        {
            this.game.getSlimes().get(0).delete();
        }
        new PoofPoint(this.game).create();
        
    }
}
