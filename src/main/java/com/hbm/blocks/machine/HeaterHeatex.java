
//Importing functions like HBM packages

package com.hbm.blocks.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.hbm.blocks.BlockDummyable;
import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.tileentity.machine.TileEntityHeaterHeatex;
import com.hbm.util.I18nUtil;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.common.util.ForgeDirection;

//Creates the class that makes the block function
public class HeaterHeatex extends BlockDummyable implements ILookOverlay, ITooltipProvider {
        //sets what level of tool the player needs to break it efficiently
	public HeaterHeatex() {
		super(Material.iron);
	}
        //creates the tile entity
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		//checks for fluids in the heater
		if(meta >= 12) return new TileEntityHeaterHeatex();
		if(hasExtra(meta)) return new TileEntityProxyCombo().fluid();
		return null;
	}
        //Not sure what this means, but im pretty sure its related to hitboxes and the actual size of the model
	@Override
	public int[] getDimensions() {
		return new int[] {0, 0, 1, 1, 1, 1};
	}

	@Override
	public int getOffset() {
		return 1;
	}
	//Detects wether the block is active
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		return this.standardOpenBehavior(world, x, y, z, player, 0);
	}
        //Pretty sure this is related to making machines like the boiler absorb the created heat (hence why you put it on top)
	@Override
	public void printHook(Pre event, World world, int x, int y, int z) {
		
		int[] pos = this.findCore(world, x, y, z);
		
		if(pos == null)
			return;
		
		TileEntity te = world.getTileEntity(pos[0], pos[1], pos[2]);
		
		if(!(te instanceof TileEntityHeaterHeatex))
			return;
		
		TileEntityHeaterHeatex heater = (TileEntityHeaterHeatex) te;
                //Sets the little pop up that appears when you hover over the heater with your cursor
		List<String> text = new ArrayList();
		text.add(String.format(Locale.US, "%,d", heater.heatEnergy) + " TU");
		ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getUnlocalizedName() + ".name"), 0xffff00, 0x404000, text);
	}
        //sets the input ports for the heater in the world, then he just mixes this with the actual model texture
	@Override
	public void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
		super.fillSpace(world, x, y, z, dir, o);

		x += dir.offsetX * o;
		z += dir.offsetZ * o;

		this.makeExtra(world, x + 1, y, z + 1);
		this.makeExtra(world, x + 1, y, z - 1);
		this.makeExtra(world, x - 1, y, z + 1);
		this.makeExtra(world, x - 1, y, z - 1);
	}
        //Adds information to...somewhere
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean ext) {
		this.addStandardInfo(stack, player, list, ext);
	}
}
