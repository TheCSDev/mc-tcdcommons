package io.github.thecsdev.tcdcommons.util.io.tcdapi;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import net.fabricmc.loader.api.metadata.Person;

/**
 * Similar to Fabric loader's {@link Person}, but with extra features.
 */
public final class TcdPerson
{
	// ==================================================
	private final JsonObject data;
	private final String name;
	private final @Nullable URL avatarUrl;
	private final TcdContactInformation contact;
	// ==================================================
	public TcdPerson(JsonObject data) throws IOException
	{
		this.data = Objects.requireNonNull(data).deepCopy();
		try
		{
			this.name = data.get("name").getAsString();
			this.avatarUrl = data.has("avatar_url") ? new URL(data.get("avatar_url").getAsString()) : null;
			this.contact = new TcdContactInformation();
		}
		catch(Exception e) { throw new IOException("Failed to read JSON data.", e); }
	}
	// ==================================================
	/**
	 * Returns the {@link JsonObject} for this object.
	 */
	public final JsonObject getJson() { return this.data; }
	
	/**
	 * Returns the person's name.
	 */
	public final String getName() { return this.name; }
	
	/**
	 * Returns the {@link URL} of the "avatar image" or "profile picture" assigned to the person.
	 * @apiNote {@link Nullable}. May not always be present.
	 */
	public final @Nullable URL getAvatarUrl() { return this.avatarUrl; }
	
	/**
	 * Returns {@link TcdContactInformation} containing the person's
	 * contact information such as links.
	 */
	public final TcdContactInformation getContact() { return this.contact; }
	// ==================================================
	/**
	 * Represents the "contact information" of a given {@link TcdPerson}.
	 */
	public final class TcdContactInformation
	{
		// ==================================================
		private final JsonObject data;
		private final @Nullable URL homepageUrl;
		// ==================================================
		private TcdContactInformation() throws IOException
		{
			try
			{
				//pre-define fields
				this.data = Objects.requireNonNull(TcdPerson.this.data.get("contact").getAsJsonObject());
				URL homepage = null;
				
				//try to load fields
				if(this.data.has("homepage"))
					try { homepage = new URL(this.data.get("homepage").getAsString()); } catch(Exception e) {}
				
				//assign field values
				this.homepageUrl = homepage;
			}
			catch(Exception e) { throw new IOException("Failed to load JSON data.", e); }
		}
		// ==================================================
		/**
		 * Returns the {@link JsonObject} for this object.
		 */
		public final JsonObject getJson() { return this.data; }
		
		/**
		 * Returns the "homepage" {@link URL}.
		 */
		public final @Nullable URL getHomepageUrl() { return this.homepageUrl; }
		// ==================================================
	}
	// ==================================================
}