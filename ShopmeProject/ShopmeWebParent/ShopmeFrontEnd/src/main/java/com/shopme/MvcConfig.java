package com.shopme;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	//To expose a directory on file system that can be accessible by the clients
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		exposeDirectory("../categories-images", registry);
		exposeDirectory("../brand-logos", registry);
		exposeDirectory("../product-images", registry);
		exposeDirectory("../site-logo", registry);
	}

	public void exposeDirectory(String pathParttern, ResourceHandlerRegistry registry) {
		Path path = Paths.get(pathParttern);
		String absolutePath = path.toFile().getAbsolutePath();
		String logicalPath = pathParttern.replace("../", "") + "/**";

		registry.addResourceHandler(logicalPath).addResourceLocations("file:/" + absolutePath + "/");
	}
}
