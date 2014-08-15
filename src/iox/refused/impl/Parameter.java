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
package iox.refused.impl;

import iox.refused.IParameter;
import iox.refused.IRequest;

public class Parameter implements IParameter {
	private final String _part;
	private final IRequest _request;
	
	public Parameter(String part, IRequest request) {
		_part = part;
		_request = request;
	}
	
	public Parameter(String part, IParameter par) {
		_part = part;
		_request = par.request();
	}

	@Override
	public String part() {
		return _part;
	}

	@Override
	public IRequest request() {
		return _request;
	}

}
