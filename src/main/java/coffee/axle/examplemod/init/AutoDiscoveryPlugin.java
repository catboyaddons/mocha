// SPDX-License-Identifier: GPL-3.0-or-later
// SPDX-FileCopyrightText: Linnea Gräf <nea@nea.moe> and Firmament Contributors
// This file is derived from Firmament (https://github.com/FirmamentMC/Firmament)


package coffee.axle.examplemod.init;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoDiscoveryPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger("examplemod");

    private String mixinPackage;

    public void setMixinPackage(String mixinPackage) {
        this.mixinPackage = mixinPackage;
    }

    @SuppressWarnings("deprecation")
    public URL getBaseUrlForClassUrl(URL classUrl) {
        String string = classUrl.toString();
        if (classUrl.getProtocol().equals("jar")) {
            try {
                return new URL(string.substring(4).split("!")[0]);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        if (string.endsWith(".class")) {
            try {
                return new URL(string.replace("\\", "/")
                        .replace(getClass().getCanonicalName()
                                .replace(".", "/") + ".class", ""));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        return classUrl;
    }

    public String getMixinPackage() {
        return mixinPackage;
    }

    public String getMixinBaseDir() {
        return mixinPackage.replace(".", "/");
    }

    private List<String> mixins = null;

    public void tryAddMixinClass(String className) {
        if (!className.endsWith(".class"))
            return;
        String norm = (className.substring(0, className.length() - ".class".length()))
                .replace("\\", "/")
                .replace("/", ".");
        if (norm.startsWith(getMixinPackage() + ".") && !norm.endsWith(".")) {
            mixins.add(norm.substring(getMixinPackage().length() + 1));
        }
    }

    private void tryDiscoverFromContentFile(URL url) {
        Path file;
        try {
            file = Paths.get(getBaseUrlForClassUrl(url).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        LOGGER.debug("Base directory found at {}", file);
        if (!Files.exists(file)) {
            LOGGER.debug("Skipping non-existing mixin root: {}", file);
            return;
        }
        if (Files.isDirectory(file)) {
            walkDir(file);
        } else {
            walkJar(file);
        }
        LOGGER.debug("Found mixins: {}", mixins);
    }

    public List<String> getMixins() {
        if (mixins != null)
            return mixins;
        try {
            LOGGER.debug("Trying to discover mixins");
            mixins = new ArrayList<>();
            URL classUrl = getClass().getProtectionDomain().getCodeSource().getLocation();
            LOGGER.debug("Found classes at {}", classUrl);
            tryDiscoverFromContentFile(classUrl);
        } catch (Exception e) {
            throw new RuntimeException("Failed to discover mixins", e);
        }
        return mixins;
    }

    private void walkDir(Path classRoot) {
        LOGGER.debug("Trying to find mixins from directory");
        var path = classRoot.resolve(getMixinBaseDir());
        if (!Files.exists(path))
            return;
        try (Stream<Path> classes = Files.walk(path)) {
            classes.map(it -> classRoot.relativize(it).toString())
                    .forEach(this::tryAddMixinClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void walkJar(Path file) {
        LOGGER.debug("Trying to find mixins from jar file");
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(file))) {
            ZipEntry next;
            while ((next = zis.getNextEntry()) != null) {
                tryAddMixinClass(next.getName());
                zis.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
