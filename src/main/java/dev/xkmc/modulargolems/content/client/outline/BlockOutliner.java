package dev.xkmc.modulargolems.content.client.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

public class BlockOutliner {

	private static Level world;
	private static int time = 0;
	private static List<BlockPos> selection;

	public static void drawOutlines(Player player, List<BlockPos> list) {
		Level level = Minecraft.getInstance().level;
		if (level == null) return;
		if (Minecraft.getInstance().player != player) return;
		time = player.tickCount;
		selection = list;
		world = level;
	}

	public static void renderOutline(PoseStack pose, Vec3 camera) {
		Level level = Minecraft.getInstance().level;
		Player player = Minecraft.getInstance().player;
		if (selection == null || level == null || player == null || level != world || time + 3 < player.tickCount) {
			selection = null;
			return;
		}
		var buffer = Minecraft.getInstance().renderBuffers().bufferSource();
		var vc = buffer.getBuffer(RenderType.lines());

		BlockPos pre = null, first = null;
		float time = (level.getGameTime() + Minecraft.getInstance().getTimer().getRealtimeDeltaTicks()) / 40f % 1;
		for (var pos : selection) {
			renderBox(pose, vc, pos, camera.toVector3f(), pre == null ? 0xff7fff7f : 0xFF7FCDE0);
			if (pre != null) {
				line(pose, vc, pre, pos, camera.toVector3f(), time);
			} else first = pos;
			pre = pos;
		}
		if (pre != null) line(pose, vc, pre, first, camera.toVector3f(), time);
	}

	private static void line(PoseStack pose, VertexConsumer vc, BlockPos a, BlockPos b, Vector3f pos, float time) {
		Vec3 c0 = a.getCenter();
		Vec3 c1 = b.getCenter();
		{
			Vec3 v1 = c0.lerp(c1, time);
			Vec3 v0 = c0;
			if (time >= 0.5) {
				v0 = c0.lerp(c1, time - 0.5);
			}
			var p0 = v0.toVector3f();
			var p1 = v1.toVector3f();
			renderLine(pose, vc, p0.x, p0.y, p0.z, p1.x, p1.y, p1.z, pos, 0xFF7FCDE0);

		}
		if (time < 0.5) {
			Vec3 v1 = c1;
			Vec3 v0 = c0.lerp(c1, time + 0.5);
			var p0 = v0.toVector3f();
			var p1 = v1.toVector3f();
			renderLine(pose, vc, p0.x, p0.y, p0.z, p1.x, p1.y, p1.z, pos, 0xFF7FCDE0);
		}
	}

	private static void renderBox(
			PoseStack pose, VertexConsumer vc, BlockPos box,
			Vector3f pos, int color
	) {
		float offset = 1f / 32;
		renderCube(pose, vc,
				box.getX() + offset, box.getY() + offset, box.getZ() + offset,
				box.getX() + 1 - offset, box.getY() + 1 - offset, box.getZ() + 1 - offset,
				pos, color);
	}

	public static void renderCube(
			PoseStack pose, VertexConsumer vc,
			float x0, float y0, float z0,
			float x1, float y1, float z1,
			Vector3f pos, int color) {
		renderLine(pose, vc, x0, y0, z0, x1, y0, z0, pos, color);
		renderLine(pose, vc, x0, y0, z0, x0, y1, z0, pos, color);
		renderLine(pose, vc, x0, y0, z0, x0, y0, z1, pos, color);
		renderLine(pose, vc, x1, y0, z0, x1, y1, z0, pos, color);
		renderLine(pose, vc, x1, y0, z0, x1, y0, z1, pos, color);
		renderLine(pose, vc, x0, y1, z0, x1, y1, z0, pos, color);
		renderLine(pose, vc, x0, y1, z0, x0, y1, z1, pos, color);
		renderLine(pose, vc, x0, y0, z1, x1, y0, z1, pos, color);
		renderLine(pose, vc, x0, y0, z1, x0, y1, z1, pos, color);
		renderLine(pose, vc, x1, y1, z0, x1, y1, z1, pos, color);
		renderLine(pose, vc, x1, y0, z1, x1, y1, z1, pos, color);
		renderLine(pose, vc, x0, y1, z1, x1, y1, z1, pos, color);
	}

	private static void renderLine(
			PoseStack pose, VertexConsumer vc,
			float x0, float y0, float z0,
			float x1, float y1, float z1,
			Vector3f pos, int color
	) {
		PoseStack.Pose mat = pose.last();
		float rx = x1 - x0;
		float ry = y1 - y0;
		float rz = z1 - z0;
		float len = Mth.sqrt(rx * rx + ry * ry + rz * rz);
		rx /= len;
		ry /= len;
		rz /= len;
		vc.addVertex(mat, x0 - pos.x, y0 - pos.y, z0 - pos.z).setColor(color).setNormal(mat, rx, ry, rz);
		vc.addVertex(mat, x1 - pos.x, y1 - pos.y, z1 - pos.z).setColor(color).setNormal(mat, rx, ry, rz);
	}

}
