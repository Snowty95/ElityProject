package fr.britannia.poof.game.task;

import fr.britannia.poof.game.PoofGame;
import fr.britannia.poof.tools.Particule;
import fr.britannia.poof.tools.PoofPoint;
import fr.britannia.poof.tools.Title;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PlayingGame extends BukkitRunnable
{
    private final PoofGame game;
    public PlayingGame(PoofGame game)
    {
        this.game = game;
    }
    
    private Vector direction;
    public void setDirection(Vector v) { this.direction = v; }
    public Vector getDirection() { return this.direction; }
    
    private final Title title = new Title();
    @Override
    public void run()
    {

        if(this.game.isState(PoofGame.GameState.PLAYING))
        {
            this.game.getRabbit().setVelocity(this.getDirection());
            new Particule(EnumParticle.CLOUD).sendDragged(game.getRabbit().getLocation().add(new Vector(-2*this.getDirection().getX(), this.getDirection().getY(), -2*this.getDirection().getZ())));
            this.game.getSlime().ifPresent(poofPoint ->
            {
                int point = poofPoint.getPoint();
                poofPoint.delete();
                this.game.getPlayer().sendMessage(PoofGame.prefix() + "Vous venez de gagner §8: §b" + point + " §epoint(s)");
                this.game.addPoint(point);
            });
            this.title.sendActionBar(this.game.getPlayer(), PoofGame.prefix() + "§ePoint : " + this.game.getPoint());
        }
        else
        {
            this.game.getRabbit().setVelocity(new Vector(0, 0, 0));
            new Particule(EnumParticle.TOWN_AURA).sendCircle(game.getRabbit().getLocation().add(0, -1, 0), 0.5, 1, 0, 0);
        }
    
        this.game.getSlimes().forEach(PoofPoint::updateSlime);
        if(this.game.getRabbit().getPassenger() == null) this.game.getRabbit().setPassenger(this.game.getPlayer());
    }
}
