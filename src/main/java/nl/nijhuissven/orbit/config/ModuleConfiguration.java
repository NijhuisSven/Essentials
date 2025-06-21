package nl.nijhuissven.orbit.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import nl.nijhuissven.orbit.Orbit;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Accessors(fluent = true)
public class ModuleConfiguration {
    
    private final File configFile;
    private final YamlConfigurationLoader loader;
    private final Map<String, ModuleInfo> modules = new HashMap<>();
    
    public ModuleConfiguration() {
        this.configFile = new File(Orbit.instance().getDataFolder(), "modules.yml");
        Path configPath = configFile.toPath();
        this.loader = YamlConfigurationLoader.builder()
                .path(configPath)
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .build();
        
        loadModules();
    }
    
    public void reload() {
        loadModules();
    }
    
    private void loadModules() {
        try {
            CommentedConfigurationNode rootNode = loader.load();
            CommentedConfigurationNode modulesNode = rootNode.node("modules");
            
            if (modulesNode.isList()) {
                List<CommentedConfigurationNode> moduleNodes = modulesNode.childrenList();
                
                for (CommentedConfigurationNode moduleNode : moduleNodes) {
                    String name = moduleNode.node("name").getString("");
                    String version = moduleNode.node("version").getString("1.0.0");
                    boolean enabled = moduleNode.node("enabled").getBoolean(true);

                    ModuleInfo moduleInfo = new ModuleInfo(name, version, enabled);
                    modules.put(name.toLowerCase(), moduleInfo);
                    
                    Orbit.logger().info("Module '" + name + "' loaded: " + (enabled ? "ENABLED" : "DISABLED"));
                }
            }
            
            loader.save(rootNode);
        } catch (IOException e) {
            Orbit.logger().severe("Could not load modules configuration: " + e.getMessage());
            throw new RuntimeException("Failed to load modules configuration", e);
        }
    }
    
    public boolean isModuleEnabled(String moduleName) {
        ModuleInfo module = modules.get(moduleName.toLowerCase());
        if (module == null) {
            Orbit.logger().warning("Module '" + moduleName + "' not found in configuration, defaulting to disabled");
            return false;
        }
        
        if (!module.enabled()) {
            Orbit.logger().info("Module '" + moduleName + "' is disabled in configuration");
            return false;
        }

        return true;
    }
    
    public ModuleInfo getModuleInfo(String moduleName) {
        return modules.get(moduleName.toLowerCase());
    }
    
    public Map<String, ModuleInfo> getAllModules() {
        return new HashMap<>(modules);
    }
    
    public record ModuleInfo(String name, String version, boolean enabled) {}
} 