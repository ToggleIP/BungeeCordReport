package nl.AverageKevin.BCR;

import java.util.HashMap;
import java.util.UUID;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReportCommand
  extends Command
{
  public ReportCommand(String name)
  {
    super(name);
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
private HashMap<UUID, Long> time = new HashMap();
  private int cooldown = 120000;
  
  public void execute(CommandSender cs, String[] args)
  {
    if ((cs instanceof ProxiedPlayer))
    {
      ProxiedPlayer p = (ProxiedPlayer)cs;
      if (!p.hasPermission("report.use"))
      {
        p.sendMessage(new TextComponent("§9§lREPORT §8§l> §7You don't have enough Permissions!"));
        return;
      }
      if (args.length >= 3)
      {
        ProxiedPlayer t = BungeeCord.getInstance().getPlayer(args[0]);
        if ((t != null) && (t.isConnected()))
        {
          if (canReport(p.getUniqueId()))
          {
            String type = null;
            if (args[1].equalsIgnoreCase("bug")) {
              type = "Bug";
            } else if (args[1].equalsIgnoreCase("hacker")) {
              type = "Hacker";
            } else if (args[1].equalsIgnoreCase("missing_items")) {
              type = "Missing Items";
            } else if (args[1].equalsIgnoreCase("grief")) {
              type = "Grief";
            }
            if ((type != null) && (!type.isEmpty()))
            {
              this.time.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
              
              String msg = "";
              for (int i = 2; i < args.length; i++) {
                msg = msg + args[i] + " ";
              }
              if ((msg != null) && (!msg.isEmpty())) {
                msg = msg.substring(0, msg.length() - 1);
              }
              String server = t.getServer().getInfo().getName();
              
              p.sendMessage(new TextComponent("§c§lREPORT §8§l> §eYour report was submitted!"));
              for (ProxiedPlayer pl : BungeeCord.getInstance().getPlayers()) {
                if (pl.hasPermission("report.staff.see"))
                {
                  pl.sendMessage(new TextComponent("§8§l§m-------------------------------"));
                  pl.sendMessage(new TextComponent("§8> §7Report made by: §6" + p.getName()));
                  pl.sendMessage(new TextComponent("§8> §7Offender: §c" + t.getName()));
                  pl.sendMessage(new TextComponent("§8> §7Server: §c" + server));
                  pl.sendMessage(new TextComponent("§8> §7Type: §c" + type));
                  pl.sendMessage(new TextComponent("§8> §7Reason: §c" + msg));
                  pl.sendMessage(new TextComponent(""));
                  
                  TextComponent st = new TextComponent("§9§l[INVESTIGATE (CLICK HERE)]");
                  st.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§fClick to connect to §e" + server + "§f.").create()));
                  st.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + server));
                  
                  pl.sendMessage(st);
                  pl.sendMessage(new TextComponent("§8§l§m-------------------------------"));
                }
              }
            }
            else
            {
              p.sendMessage(new TextComponent("§c§lREPORT §8§l> §7Invalid type. Available Type: Bug, Hacker, Missing_Items, Grief"));
            }
          }
          else
          {
            double timeleft = Math.round((float)((((Long)this.time.get(p.getUniqueId())).longValue() + this.cooldown - System.currentTimeMillis()) / 1000L * 100L));
            
            p.sendMessage(new TextComponent("§9§lREPORT §8§l> §7You can report a player again in §e" + String.valueOf(timeleft / 100.0D) + " §7seconds!"));
          }
        }
        else {
          p.sendMessage(new TextComponent("§9§lREPORT §8§l> §7You cannot report §e" + args[0] + "§7."));
        }
      }
      else
      {
        p.sendMessage(new TextComponent("§9§lREPORT §8§l> §7/report <player> <type> <message>"));
      }
    }
  }
  
  private boolean canReport(UUID uuid)
  {
    if (this.time.containsKey(uuid))
    {
      long current = System.currentTimeMillis();
      if (((Long)this.time.get(uuid)).longValue() + this.cooldown <= current) {
        return true;
      }
      return false;
    }
    return true;
  }
}
