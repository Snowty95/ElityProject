package fr.britannia.poof.listeners;

import fr.britannia.poof.Poof;
import fr.britannia.poof.game.ManagerGame;
import fr.britannia.poof.game.PoofGame;
import fr.britannia.poof.tools.PoofMethod;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class ListenersGame implements Listener
{
    
    // <------------------ INTERACTION -------------------->
    
    @EventHandler
    public void onInterract(PlayerInteractEvent e)
    {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        ManagerGame.getGame(player).ifPresent(game ->
        {
            if(item == null)
            {
                if(game.isState(PoofGame.GameState.CHOOSE))
                {
                    game.getPlayingGame().setDirection(player.getEyeLocation().getDirection().setY(0));
                    game.setState(PoofGame.GameState.PLAYING);
                }
                return;
            }
            if(item.hasItemMeta())
            {
                if(item.getItemMeta().hasDisplayName())
                {
                    if(e.getAction() == Action.LEFT_CLICK_AIR)
                    {
                        if(game.isState(PoofGame.GameState.PLAYING))
                        {
                            if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§eGauche"))
                            {
                                game.getPlayingGame().setDirection(PoofMethod.getVectorLeft(game.getPlayingGame().getDirection()));
                            }
                            if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§eDroite"))
                            {
                                game.getPlayingGame().setDirection(PoofMethod.getVectorRight(game.getPlayingGame().getDirection()));
                            }
                        }
                        if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§4Feu"))
                        {
                            double x = (new Random().nextDouble() - 0.5) * 2;
                            double y = (new Random().nextDouble() - 0.5) * 2;
                            double z = (new Random().nextDouble() - 0.5) * 2;
                            Firework fire = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                            fire.setVelocity(new Vector(x, y, z).multiply(0.8));
                            fire.setFireworkMeta(PoofMethod.getRandomMeta(fire));
                            new BukkitRunnable()
                            {
                        
                                @Override
                                public void run()
                                {
                                    fire.detonate();
                                }
                            }.runTaskLaterAsynchronously(Poof.getInstance(), 10);
                        }
                    }
                    
                    if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§cQuitter"))
                    {
                        ManagerGame.deleteGame(player);
                    }
                    
                }
            }
            
        });
    }
    
    
    
    // <------------------ DEPLACEMENT -------------------->
    
    @EventHandler
    public void onMove(PlayerMoveEvent e)
    {
        Player player = e.getPlayer();
        ManagerGame.getGame(player).ifPresent(game ->
        {
            if(game.isState(PoofGame.GameState.STARTING))
            {
                if(e.getTo().getX() - e.getFrom().getX() != 0 && e.getTo().getZ() - e.getFrom().getZ() != 0)
                {
                    e.setTo(e.getFrom());
                }
            }
            
        });
    }
    
    
    
    // <-------------------- DAMAGE -------------------->
    
    @EventHandler
    public void onDamage(EntityDamageEvent e)
    {
        if(e.getEntity() instanceof Sheep)
        {
            Sheep sheep = (Sheep) e.getEntity();
            ManagerGame.getGames().forEach(game ->
            {
                if(sheep.getColor() == DyeColor.PINK && sheep.getLocation().getX() == game.getPlayer().getLocation().getX() && sheep.getLocation().getZ() == game.getPlayer().getLocation().getZ())
                    e.setCancelled(true);
            });
        }
        
    }
    
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e)
    {
        if(e.getEntity() instanceof Slime || e.getEntity() instanceof ArmorStand)
        {
            Entity entity = e.getEntity();
            if(e.getDamager() instanceof Player)
            {
                Player player = (Player)e.getDamager();
                ManagerGame.getGame(player).ifPresent(game ->
                {
                    Location loc = entity.getLocation();
                    game.getSlimes().forEach(poofPoint ->
                    {
                        if(poofPoint.getLocation().getBlockX() == loc.getBlockX() && poofPoint.getLocation().getBlockZ() == loc.getBlockZ()) e.setCancelled(true);
                    });
                });
            }
        }
    }
    
}
