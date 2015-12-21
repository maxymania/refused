refused
=======

Refused - a sane java web (micro-)anti-framework

Philosophy
----------

Micro-Framework: Refused is minimalistic and lacks features such as:
* Accounts, authentication, authorization, roles, etc.
* Database abstraction or ORM.
* Input validation and input sanitation.
* URL mapping
* Ajax
* Web services
* Web resources

Anti-Framework: Refused doesn't ...
* tell you how you have to write your Applications
* hide the Servlet API
* save you from writing boilerplate code
* stop you from writing/adding your own URL mapping, Input validation, Caching, etc.
* conflicts with other web (anti-)frameworks


Mini-Example
------------

Refused consists of a minimalistic Template Language loosely inspired by TYPO3_Flow's
Fluid template system, as well as a PHP-inspired template language called JHP.

First of all, we need a Servlet:

Here is our `YourMainServlet.java`
```java
import iox.refused.IRenderer;
import iox.refused.impl.MetaRendererWrapper;
import iox.refused.jhp.JHP_Parser;
import iox.refused.servlets.RefusedServlet;
import iox.refused.templ.MasterTemplate;
import iox.refused.util.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class YourMainServlet extends RefusedServlet {
    @Override
    protected IRenderer makeRenderer() {
        String content = TextUtil.loadfile(getServletContext(), "index.templ");
        MasterTemplate mt = new MasterTemplate(content);
        List lst = new ArrayList();
        lst.add(JHP_Parser.compile(TextUtil.loadfile(getServletContext(), "program.jhp")));
        mt = new MasterTemplate(mt,lst);

        // if you want to use an "init" section in your jhp files.
        // return new MetaRendererWrapper(mt);
        return mt;
    }
    @Override
    protected boolean reload() {
        return true;
    }
}
```

Here is our `index.templ`.
```html
...
        </div><!--close sidebar-->
       </div><!--close sidebar_container-->	
	   
	  <div id="content">
        <div class="content_item">
		  <templ:include body/>
		  
		</div><!--close content_item-->
      </div><!--close content-->   
	</div><!--close site_content-->  
...
```

And finally our `program.jhp`.
```
<templ:insert body>
	<h1>Hello World!</h1>
	<p><?jhp
            echo ("Hello Text!");
        ?></p>
</templ:insert>
```

Dependencies
------------

See `pom.xml`.
