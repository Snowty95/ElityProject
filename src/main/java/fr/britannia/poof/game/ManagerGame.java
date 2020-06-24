package fr.britannia.poof.game;

import fr.britannia.poof.tools.PoofPoint;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ManagerGame
{
    private static final Set<PoofGame> games = new HashSet<>();
    
    public static Set<PoofGame> getGames() { return games; }
    
    public static Optional<PoofGame> getGame(Player player)
    {
        return games.stream().filter(game -> game.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst();
    }
    
    public static void addGame(Player player)
    {
        if(!getGame(player).isPresent())
        {
            games.add(new PoofGame(player));
        }
    }
    
    public static void deleteGame(PoofGame game)
    {
        game.getStartGame().cancel();
        game.getTeleportGame().cancel();
        game.getPlayingGame().cancel();
        game.getSpawnPoint().cancel();
        
        if(game.getRabbit() != null)
        {
            game.getPlayer().leaveVehicle();
            game.getRabbit().remove();
        }
        if(game.getPlayer() != null)
        {
            game.getPlayer().getInventory().clear();
            Location location = game.getPlayer().getLocation();
            location.setY(game.getPlayer().getWorld().getHighestBlockYAt(game.getPlayer().getLocation().getBlockX(), game.getPlayer().getLocation().getBlockZ())+1);
            game.getPlayer().teleport(location);
            game.getPlayer().sendMessage(PoofGame.prefix() + "Vous avez quitté le jeux §f:§b'§f(");
        }
        if(!game.getSlimes().isEmpty())
        {
            game.getSlimes().forEach(PoofPoint::remove);
            game.clearSlimes();
        }
        games.remove(game);
    }
    
    public static void deleteGame(Player player)
    {
        getGame(player).ifPresent(ManagerGame::deleteGame);
    }
}
