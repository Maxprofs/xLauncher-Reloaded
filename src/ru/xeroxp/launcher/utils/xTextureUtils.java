package ru.xeroxp.launcher.utils;

import ru.xeroxp.launcher.gui.elements.xServer;
import ru.xeroxp.launcher.misc.xDebug;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class xTextureUtils {
    public static boolean check() {
        String[] folderNames = {"texturepacks", "resourcepacks"};

        for (String folderName : folderNames) {
            xServer.loadServers();

            boolean one = false;
            for (xServer server : xServer.getServers()) {
                String texturesFolder;
                if (server.getFolder().isEmpty() && !one) {
                    texturesFolder = xFileUtils.getRootDirectory() + File.separator + folderName;
                    one = true;
                } else {
                    texturesFolder = xFileUtils.getRootDirectory() + File.separator + server.getFolder() + File.separator + folderName;
                }

                if (checkTexturePack(new File(texturesFolder))) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean checkTexturePack(File texturesPath) {
        if (!texturesPath.exists()) {
            return false;
        }

        File[] listOfTextures = texturesPath.listFiles();

        assert listOfTextures != null;
        for (File listOfTexture : listOfTextures) {
            if (listOfTexture.isDirectory()) {
                return true;
            }

            if (listOfTexture.isFile()) {
                String texture = listOfTexture.getName();

                if (!texture.toLowerCase().endsWith(".zip")) {
                    return true;
                }

                int col = 0;
                try {
                    ZipFile tZip = new ZipFile(listOfTexture.getPath());
                    Enumeration entries = tZip.entries();

                    while (entries.hasMoreElements()) {
                        ZipEntry entry = (ZipEntry) entries.nextElement();
                        String entryName = entry.getName();

                        if (entryName.equals("pack.png")) {
                            ++col;
                        }

                        if (((entryName.contains("ctm") || entryName.contains("blocks")) && entryName.contains(".png")
                                && (!(entryName.contains("torch") || entryName.contains("overlay") || entryName.contains("dust")))
                                && (entryName.contains("ore") || entryName.contains("wool") || entryName.contains("brick")
                                || entryName.contains("stone") || entryName.contains("sand") || entryName.contains("dirt")
                                || entryName.contains("_block.") || entryName.contains("clay") || entryName.contains("grass_")
                                || entryName.contains("planks") || entryName.contains("log_")))) {
                            ++col;
                            BufferedImage textureImage = ImageIO.read(tZip.getInputStream(entry));

                            int alphaPixels = 0;
                            for (int x = 0; x < textureImage.getWidth(); ++x) {
                                for (int y = 0; y < textureImage.getHeight(); ++y) {
                                    if (textureImage.getRGB(x, y) >> 24 == 0) {
                                        ++alphaPixels;
                                    }
                                }
                            }

                            if (alphaPixels > (textureImage.getWidth() * textureImage.getHeight() / textureImage.getWidth())) {
                                return true;
                            }
                        }

                        if (entry.getName().equals("terrain.png")) {
                            ++col;
                            InputStream entryStream = tZip.getInputStream(entry);
                            BufferedImage textureImage = ImageIO.read(entryStream);

                            int alphaPixels = 0;
                            for (int x = 0; x < 50; ++x) {
                                for (int y = 0; y < 50; ++y) {
                                    if (textureImage.getRGB(x, y) >> 24 == 0) {
                                        ++alphaPixels;
                                    }
                                }
                            }

                            if (alphaPixels / 25 > 5) {
                                return true;
                            }
                        }
                    }
                } catch (IOException e) {
                    xDebug.errorMessage("Failed to open texturepack: " + e.getMessage());
                }

                if (col < 1) {
                    return true;
                }
            }
        }

        return false;
    }
}
