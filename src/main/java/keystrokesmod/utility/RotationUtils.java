package keystrokesmod.utility;

import keystrokesmod.module.impl.client.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtils {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static float renderPitch;
    public static float prevRenderPitch;
    public static float renderYaw;
    public static float prevRenderYaw;

    public static void setRenderYaw(float yaw) {
        mc.thePlayer.rotationYawHead = yaw;
        if (Settings.rotateBody.isToggled() && Settings.fullBody.isToggled()) {
            mc.thePlayer.renderYawOffset = yaw;
        }
    }

    public static float[] getRotations(BlockPos blockPos, final float n, final float n2) {
        final float[] array = getRotations(blockPos);
        return fixRotation(array[0], array[1], n, n2);
    }

    public static float[] getRotations(final BlockPos blockPos) {
        final double n = blockPos.getX() + 0.45 - mc.thePlayer.posX;
        final double n2 = blockPos.getY() + 0.45 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        final double n3 = blockPos.getZ() + 0.45 - mc.thePlayer.posZ;
        return new float[] { mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float((float)(Math.atan2(n3, n) * 57.295780181884766) - 90.0f - mc.thePlayer.rotationYaw), m(mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float((float)(-(Math.atan2(n2, MathHelper.sqrt_double(n * n + n3 * n3)) * 57.295780181884766)) - mc.thePlayer.rotationPitch)) };
    }

    public static float interpolateValue(float tickDelta, float old, float newFloat) {
        return old + (newFloat - old) * tickDelta;
    }

    public static float[] getRotations(Entity entity, final float n, final float n2) {
        final float[] array = getRotations(entity);
        if (array == null) {
            return null;
        }
        return fixRotation(array[0], array[1], n, n2);
    }

    public static boolean inRange(final BlockPos blockPos, final double n) {
        final float[] array = (float[])RotationUtils.getRotations(blockPos);
        final Vec3 getPositionEyes = mc.thePlayer.getPositionEyes(1.0f);
        final float n2 = -array[0] * 0.017453292f;
        final float n3 = -array[1] * 0.017453292f;
        final float cos = MathHelper.cos(n2 - 3.1415927f);
        final float sin = MathHelper.sin(n2 - 3.1415927f);
        final float n4 = -MathHelper.cos(n3);
        final Vec3 vec3 = new Vec3((double)(sin * n4), (double)MathHelper.sin(n3), (double)(cos * n4));
        return BlockUtils.getBlock(blockPos).getCollisionBoundingBox(mc.theWorld, blockPos, BlockUtils.getBlockState(blockPos)).calculateIntercept(getPositionEyes, getPositionEyes.addVector(vec3.xCoord * n, vec3.yCoord * n, vec3.zCoord * n)) != null;
    }

    public static float[] getRotations(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final double n = entity.posX - mc.thePlayer.posX;
        final double n2 = entity.posZ - mc.thePlayer.posZ;
        double n3;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
            n3 = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        } else {
            n3 = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        }
        return new float[]{mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float((float) (Math.atan2(n2, n) * 57.295780181884766) - 90.0f - mc.thePlayer.rotationYaw), m(mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float((float) (-(Math.atan2(n3, MathHelper.sqrt_double(n * n + n2 * n2)) * 57.295780181884766)) - mc.thePlayer.rotationPitch) + 3.0f)};
    }

    public static float m(final float n) {
        return MathHelper.clamp_float(n, -90.0f, 90.0f);
    }

    public static float[] fixRotation(float n, float n2, final float n3, final float n4) {
        float n5 = n - n3;
        final float abs = Math.abs(n5);
        final float n7 = n2 - n4;
        final float n8 = mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
        final double n9 = n8 * n8 * n8 * 1.2;
        final float n10 = (float) (Math.round((double) n5 / n9) * n9);
        final float n11 = (float) (Math.round((double) n7 / n9) * n9);
        n = n3 + n10;
        n2 = n4 + n11;
        if (abs >= 1.0f) {
            final int n12 = (int) Settings.randomYawFactor.getInput();
            if (n12 != 0) {
                final int n13 = n12 * 100 + Utils.randomize(-30, 30);
                n += Utils.randomize(-n13, n13) / 100.0;
            }
        } else if (abs <= 0.04) {
            n += ((abs > 0.0f) ? 0.01 : -0.01);
        }
        return new float[]{n, m(n2)};
    }

    public static float angle(final double n, final double n2) {
        return (float) (Math.atan2(n - mc.thePlayer.posX, n2 - mc.thePlayer.posZ) * 57.295780181884766 * -1.0);
    }
}
