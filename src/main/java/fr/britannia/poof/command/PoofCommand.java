package fr.britannia.poof.command;

import fr.britannia.poof.game.ManagerGame;
import fr.britannia.poof.game.PoofGame;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.util.Random;

public class PoofCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(sender instanceof Player)
        {
            Player player = (Player)sender;
            
            if(label.equalsIgnoreCase("poof"))
            {
                if(args.length == 0)
                {
                    player.sendMessage(PoofGame.prefix() + "§eLancement de la partie");
                    ManagerGame.addGame(player);
                }
                
            }
        }
        return false;
    }
    
    
}
