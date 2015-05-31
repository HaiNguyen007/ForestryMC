package forestry.apiculture.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.tileentity.TileEntity;

import forestry.core.gadgets.TileForestry;
import forestry.core.interfaces.IActivatable;
import forestry.core.network.PacketCoordinates;
import forestry.core.network.PacketId;
import forestry.core.proxy.Proxies;

public class PacketActiveUpdate extends PacketCoordinates {

	private IActivatable activatable;

	public static void onPacketData(DataInputStream data) throws IOException {
		new PacketActiveUpdate(data);
	}

	private PacketActiveUpdate(DataInputStream data) throws IOException {
		super(data);
	}

	public <T extends TileForestry & IActivatable> PacketActiveUpdate(T tile) {
		super(PacketId.TILE_FORESTRY_ACTIVE, tile);
		this.activatable = tile;
	}

	@Override
	protected void writeData(DataOutputStream data) throws IOException {
		super.writeData(data);
		data.writeBoolean(activatable.isActive());
	}

	@Override
	protected void readData(DataInputStream data) throws IOException {
		super.readData(data);

		boolean active = data.readBoolean();

		TileEntity tile = getTarget(Proxies.common.getRenderWorld());
		if (tile instanceof IActivatable) {
			((IActivatable) tile).setActive(active);
		}
	}
}
