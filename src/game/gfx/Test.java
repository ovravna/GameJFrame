package game.gfx;

public class Test {
    public static void main(String[] args) {
        int filter = -0xaa0000;

        int kaka = Math.floorMod(filter/0x10000, 0x100);


//        int r = Math.floorMod(0xffffff56, 0x100);
        System.out.println(Integer.toHexString(filter>>16));

    }
}
