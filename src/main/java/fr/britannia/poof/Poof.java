package fr.britannia.poof;

import fr.britannia.poof.command.PoofCommand;
import fr.britannia.poof.game.ManagerGame;
import fr.britannia.poof.listeners.ListenersGame;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Poof extends JavaPlugin
{
    private static Poof instance;
    public static Poof getInstance() { return instance; }
    
    @Override
    public void onEnable()
    {
        instance = this;
        this.getLogger().info("=========================================");
        this.getLogger().info("PLUGIN ->");
        this.getLogger().info("-> name : " + this.getDescription().getName());
        this.getLogger().info("-> version : " + this.getDescription().getVersion());
        this.getLogger().info("-> author : " + this.getDescription().getAuthors());
        this.registerCommands();
        this.registerListeners();
        this.getLogger().info("=========================================");
        
    }
    
    @Override
    public void onDisable()
    {
        ManagerGame.getGames().forEach(ManagerGame::deleteGame);
    }
    
    public void registerCommands()
    {
        this.getLogger().info("COMMANDS ->");
        try
        {
            this.getCommand("poof").setExecutor(new PoofCommand());
            this.getLogger().info("-> SUCCES register...");
        }
        catch(Exception e)
        {
            this.getLogger().severe("-> ERROR register...");
        }
    }
    
    public void registerListeners()
    {
        this.getLogger().info("LISTENERS ->");
        try
        {
            this.getServer().getPluginManager().registerEvents(new ListenersGame(), this);
            this.getLogger().info("-> SUCCES register...");
        }
        catch(Exception e)
        {
            this.getLogger().severe("-> ERROR register...");
        }
    }
}
