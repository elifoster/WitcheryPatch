package io.github.elifoster.witcherypatch;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions("io.github.elifoster.witcherypatch")
@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.Name("Witchery Patch")
public class LoadingPlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[] { Transformer.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        Transformer.isEnvObfuscated = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
