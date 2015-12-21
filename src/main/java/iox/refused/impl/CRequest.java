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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import iox.refused.IRequest;

public class CRequest implements IRequest {

    private HttpServletRequest request;
    private Map<String, String> params = new HashMap<String, String>();

    public CRequest(HttpServletRequest request) {
        super();
        this.request = request;
    }

    @Override
    public HttpServletRequest request() {
        return request;
    }

    @Override
    public Map<String, String> params() {
        return params;
    }
}
