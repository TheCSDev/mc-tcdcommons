package io.github.thecsdev.tcdcommons.mixin.events;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Maps;

import io.github.thecsdev.tcdcommons.TCDCommons;
import io.github.thecsdev.tcdcommons.api.events.TEntityEvent;
import io.github.thecsdev.tcdcommons.mixin.addons.MixinEntity_AddOn;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;

@Mixin(Entity.class)
public abstract class MixinEntity implements MixinEntity_AddOn
{
	// ==================================================
	private @Unique HashMap<Identifier, Object> tcdcommons_customData = Maps.newHashMap();
	public @Override HashMap<Identifier, Object> tcdcommons_getCustomData() { return tcdcommons_customData; }
	// ==================================================
	protected abstract @Shadow void populateCrashReport(CrashReportSection section);
	// ==================================================
	@Inject(method = "readNbt", at = @At("RETURN"))
	public void onReadNbt(NbtCompound nbt, CallbackInfo callback)
	{
		try { TEntityEvent.READ_NBT.invoker().entityNBTCallback((Entity)(Object)this, nbt); }
		catch(Exception throwable)
		{
			var modId = TCDCommons.getModID();
			CrashReport crashReport = CrashReport.create(throwable, "[" + modId + "] Loading entity custom NBT");
			CrashReportSection crashReportSection = crashReport.addElement("Entity being loaded");
			populateCrashReport(crashReportSection);
			throw new CrashException(crashReport);
		}
	}
	
	@Inject(method = "writeNbt", at = @At("RETURN"))
	public void onWriteNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> callback)
	{
		try { TEntityEvent.WRITE_NBT.invoker().entityNBTCallback((Entity)(Object)this, nbt); }
		catch(Exception throwable)
		{
			var modId = TCDCommons.getModID();
			CrashReport crashReport = CrashReport.create(throwable, "[" + modId + "] Saving entity custom NBT");
			CrashReportSection crashReportSection = crashReport.addElement("Entity being saved");
			populateCrashReport(crashReportSection);
			throw new CrashException(crashReport);
		}
	}
	// ==================================================
}