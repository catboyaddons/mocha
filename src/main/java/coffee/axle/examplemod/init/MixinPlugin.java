// SPDX-License-Identifier: GPL-3.0-or-later
// SPDX-FileCopyrightText: Linnea Gräf <nea@nea.moe> and Firmament Contributors
// This file is derived from Firmament (https://github.com/FirmamentMC/Firmament)

package coffee.axle.examplemod.init;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {

    AutoDiscoveryPlugin autoDiscoveryPlugin = new AutoDiscoveryPlugin();

    @Override
    public void onLoad(String mixinPackage) {
        MixinExtrasBootstrap.init();
        autoDiscoveryPlugin.setMixinPackage(mixinPackage);
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return autoDiscoveryPlugin.getMixins();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
