package fr.britannia.poof.tools;

import fr.britannia.poof.game.PoofGame;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.util.Vector;

import java.util.Random;

public class PoofPoint
{
    private final Location location;
    private final PoofGame game;
    private int point;
    
    private Slime slime;
    private ArmorStand stand;
    
    public PoofPoint(PoofGame game)
    {
        double x = game.getRabbit().getLocation().getX() + new Random().nextInt(30)-15;
        double y = game.getRabbit().getLocation().getY();
        double z = game.getRabbit().getLocation().getZ() + new Random().nextInt(30)-15;
        this.game = game;
        this.location = new Location(game.getRabbit().getWorld(), x, y, z);
    }
    
    public void create()
    {
        this.slime = (Slime) this.location.getWorld().spawnEntity(this.location, EntityType.SLIME);
        this.stand = (ArmorStand) this.location.getWorld().spawnEntity(this.location, EntityType.ARMOR_STAND);
    
        this.point = new Random().nextInt(3)+1;
        
        // STAND ----->
        stand.setCustomName("§a§l"+this.point);
        stand.setCustomNameVisible(true);
        stand.setVisible(false);
        stand.setGravity(false);
        
        // SLIME ----->
        slime.setSize(2);
        this.game.addSlime(this);
    }
    
    public void delete()
    {
        this.remove();
        this.game.removeSlime(this);
    }
    
    public void remove()
    {
        if(this.slime != null) this.slime.remove();
        if(this.stand != null) this.stand.remove();
    }
    
    public Location getLocation() { return this.location; }
    public int getPoint() { return this.point; }
    
    public void updateSlime()
    {
        this.slime.setVelocity(new Vector(0,0,0));
        if(!this.slime.getLocation().equals(this.location))
        {
            this.slime.teleport(this.location);
        }
    }
    
}
