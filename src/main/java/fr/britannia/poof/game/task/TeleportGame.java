package fr.britannia.poof.game.task;

import fr.britannia.poof.game.PoofGame;
import fr.britannia.poof.tools.Particule;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportGame extends BukkitRunnable
{
    private final PoofGame game;
    public TeleportGame(PoofGame game)
    {
        this.game = game;
    }
    
    @Override
    public void run()
    {
        
        if(this.game.getPlayer().getVelocity().getY() < 0)
        {
            this.game.startTaskPlayingGame();
            cancel();
        }
        else new Particule(EnumParticle.LAVA).sendDragged(this.game.getPlayer().getLocation());
    }
}
