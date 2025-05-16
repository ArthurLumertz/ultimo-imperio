package net.mojang.thelastempire.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Graphics;

public class GuiCredits extends GuiScreen {

    private String[] lines;

    private int yOldScroll;
    private int yScroll;

    @Override
    public void init() {
        FileHandle creditsFile  = Gdx.files.internal("credits.txt");
        lines = creditsFile.readString().split("\n");
        yScroll = -128;
    }

    @Override
    public void tick() {
    	yOldScroll = yScroll;
        if (yScroll < Graphics.guiInstance.getFontSize() * lines.length) {
            yScroll += 3;
        }
    }

    @Override
    public void draw(Graphics g) {
        float yy = yOldScroll + (yScroll - yOldScroll) * TheLastEmpire.a;
        yy *= 0.3f;

        for (int y = 0; y < lines.length; y++) {
            int index = lines.length - 1 - y;

            if (index == 0) {
                g.setFontSize(48f);
                yy += g.getFontSize() / 1.5f;
            } else if (index == 2 || index == 5 || index == 10 || index == 14) {
                g.setFontSize(24f);
                yy += g.getFontSize() / 2f;
            }

            String str = lines[index];
            g.drawCenteredString(str, width, yy, 0xFFFFFF);

            yy += g.getFontSize() * 2f;
            g.setFontSize(16f);
        }
    }

    @Override
    public boolean pauseGame() {
        return true;
    }

}
