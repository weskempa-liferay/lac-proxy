package acapi.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

@ExtendedObjectClassDefinition(category = "acapi")

@Meta.OCD(
	id = "acapi.configuration.ACAPIConfiguration",
	localization = "content/Language", name = "Liferay Analytics Cloud Proxy Configuration"
)
public interface ACAPIConfiguration {
	@Meta.AD(deflt = "", required = false)
	public String token();
}