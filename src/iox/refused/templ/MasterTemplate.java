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
package iox.refused.templ;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import iox.refused.IMetaRenderer;
import iox.refused.IParameter;
import iox.refused.IRenderer;
import iox.refused.impl.Parameter;

public class MasterTemplate implements IRenderer, IMetaRenderer {
	private static final Pattern BLOCK = Pattern.compile("\\<templ\\:include +([^ ]+?)\\/?\\>|[^<]+|.");
	private List<String[]> script;
	private List<Object> childs = Arrays.asList();
	public MasterTemplate(String template){
		script = new ArrayList<String[]>();
		Matcher m = BLOCK.matcher(template);
		StringBuffer sb = new StringBuffer();
		while(m.find()){
			String g = m.group();
			if(g.startsWith("<templ:")){
				if(sb.length()>0){
					script.add(new String[]{sb.toString(),null});
					sb = new StringBuffer();
				}
				script.add(new String[]{null,m.group(1)});
			}else{
				sb.append(g);
			}
		}
		if(sb.length()>0){
			script.add(new String[]{sb.toString(),null});
			sb = new StringBuffer();
		}
	}
	public MasterTemplate(MasterTemplate other,List<Object> _childs){
		script = other.script;
		childs = _childs;
	}
	@Override
	public void process(IParameter req, PrintWriter resp) {
		for(String[] e:script){
			if(e[0]==null){
				IParameter req2 = new Parameter(e[1],req);
				for(Object obj:childs)
					if(obj instanceof IRenderer)
						((IRenderer) obj).process(req2, resp);
			}else
				resp.print(e[0]);
		}
	}
	@Override
	public IRenderer process(IParameter req) {
		List<Object> more = new ArrayList<Object>(childs.size());
		for(Object obj:childs){
			if(obj instanceof IMetaRenderer)
				more.add(((IMetaRenderer) obj).process(req));
			else
				more.add(obj);
		}
		return new MasterTemplate(this, more);
	}
}
