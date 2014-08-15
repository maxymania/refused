/*
   Copyright 2014 Simon Schmidt

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package iox.refused.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import iox.refused.IParameter;
import iox.refused.IRenderer;

public class Router implements IRenderer {
	private static final Pattern PS=Pattern.compile("/");
	private static class Route{
		public String method;
		public Pattern pat;
		public String[] par;
		public IRenderer render;
	}
	private List<Route> routes = new ArrayList<Route>();
	private IRenderer def;
	
	public Router(IRenderer def) {
		super();
		this.def = def;
	}
	public void process(IParameter req, PrintWriter resp) {
		for(Route route:routes){
			if(!route.method.equalsIgnoreCase(req.request().request().getMethod()))continue;
			Matcher m = route.pat.matcher(req.request().request().getPathInfo());
			if(m.find()){
				int n=route.par.length;
				Map<String, String> params = req.request().params();
				for(int i=0;i<n;++i)
					params.put(route.par[i], m.group(i));
				route.render.process(req, resp);
				return;
			}
		}
		if(def!=null)
			def.process(req, resp);
	}
	public void addRoute(String method,String path,IRenderer renderer){
		StringBuilder bld = new StringBuilder("^");
		int i=0;
		List<String> list = new ArrayList<String>();
		for(String part:PS.split(path)){
			if(i==0)bld.append("/");
			i++;
			if(part.startsWith(":")){
				bld.append("(.*)");
				list.add(part.substring(1));
			}else
				bld.append(Pattern.quote(part));
		};
		bld.append("$");
		Route r = new Route();
		r.method = method;
		r.par    = new String[]{};
		r.pat    = Pattern.compile(bld.toString());
		r.render = renderer;
		routes.add(r);
	}
}
