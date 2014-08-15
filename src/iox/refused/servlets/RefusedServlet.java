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
package iox.refused.servlets;

import iox.refused.IParameter;
import iox.refused.IRenderer;
import iox.refused.impl.CRequest;
import iox.refused.impl.Parameter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class RefusedServlet extends HttpServlet {
	private static final long serialVersionUID = 4548961758023361049L;
	private IRenderer renderer;
	private boolean is_reload;
	private boolean is_threadlocal;
	private ThreadLocal<IRenderer> renderertl;
	public void init() throws ServletException {
		is_reload = reload();
		is_threadlocal = threadlocal();
		if(is_reload){
			// pass;
		} else if(is_threadlocal){
			renderertl = new ThreadLocal<IRenderer>(){
				protected IRenderer initialValue(){
					return makeRenderer();
				}
			};
		} else
			renderer = makeRenderer();
	}
	private IRenderer _renderer(){
		if(is_reload){
			return makeRenderer();
		}
		if(is_threadlocal){
			return renderertl.get();
		}
		return renderer;
	}

	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		IParameter par = new Parameter("",new CRequest(req));
		_renderer().process(par, resp.getWriter());
	}
	protected abstract IRenderer makeRenderer();
	protected boolean reload() { return false; }
	protected boolean threadlocal() { return true; }
}
