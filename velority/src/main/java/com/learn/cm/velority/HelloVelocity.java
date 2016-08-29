package com.learn.cm.velority;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class HelloVelocity {
	 public static void main(String[] args) {
	 VelocityEngine ve = new VelocityEngine();
	 ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
	 ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
	 
	 ve.init();
	 
	 Template t = ve.getTemplate("velority/hellovelocity.vm");
	 VelocityContext ctx = new VelocityContext();
	 
	 s student = new s();
	 student.setAge("123");
	 student.setName("sarah");
     
     ctx.put("student", student);
	 
	 StringWriter sw = new StringWriter();
	 
	 
	 t.merge(ctx, sw);
	 
	 System.out.println(sw.toString());
	 System.out.println(String.class);
	 }
	}