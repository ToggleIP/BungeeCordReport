package nl.AverageKevin.BCR;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

@SuppressWarnings("unused")
public class Report
  extends Plugin
{
  public void onEnable()
  {
    BungeeCord.getInstance().getPluginManager().registerCommand(this, new ReportCommand("report"));
  }
}
