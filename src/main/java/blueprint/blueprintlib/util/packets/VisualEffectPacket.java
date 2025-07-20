package blueprint.blueprintlib.util.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class VisualEffectPacket {
    public static final Identifier ID = new Identifier("blueprintlib", "visual_effect");

    public final VisualEffectType type;
    public final double x, y, z;
    public final float intensity;
    public final int duration;
    public final int color; // For tint, 0 if unused
    public final float alpha; // For tint, 0 if unused

    public VisualEffectPacket(VisualEffectType type, double x, double y, double z, float intensity, int duration, int color, float alpha) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.intensity = intensity;
        this.duration = duration;
        this.color = color;
        this.alpha = alpha;
    }

    public void write(PacketByteBuf buf) {
        buf.writeEnumConstant(type);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeFloat(intensity);
        buf.writeInt(duration);
        buf.writeInt(color);
        buf.writeFloat(alpha);
    }

    public static VisualEffectPacket read(PacketByteBuf buf) {
        VisualEffectType type = buf.readEnumConstant(VisualEffectType.class);
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        float intensity = buf.readFloat();
        int duration = buf.readInt();
        int color = buf.readInt();
        float alpha = buf.readFloat();
        return new VisualEffectPacket(type, x, y, z, intensity, duration, color, alpha);
    }
}
