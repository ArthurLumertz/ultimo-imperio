package net.mojang.thelastempire.level;

public class Material {

    public static final Material gas = new Material();
    public static final Material earth = new Material();
    public static final Material rock = new Material();
    public static final Material rockSolid = new Material().solid();
    public static final Material woodSolid = new Material().solid();
    public static final Material wood = new Material();
    public static final Material liquid = new Material().liquid();

    private boolean isSolid;
    private boolean isLiquid;

    public Material solid() {
        isSolid = true;
        return this;
    }

    public Material liquid() {
        isLiquid = true;
        return this;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public boolean isLiquid() {
        return isLiquid;
    }

}
