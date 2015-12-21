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

import iox.refused.IMetaRenderer;
import iox.refused.IRenderer;
import iox.refused.impl.MetaRendererWrapper;
import iox.refused.impl.RendererWrapper;

public class RendererUtil {

    public static IRenderer wrap(Object o) {
        if (o instanceof IMetaRenderer) {
            return new MetaRendererWrapper((IMetaRenderer) o);
        }
        return (IRenderer) o;
    }

    public static IMetaRenderer wrapHeavy(Object o) {
        if (o instanceof IRenderer) {
            return new RendererWrapper((IRenderer) o);
        }
        return (IMetaRenderer) o;
    }
}
