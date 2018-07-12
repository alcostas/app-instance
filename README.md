# app-instance
**_How to use specifications_** <br/>
_Android Application_ <br/>
Add application class should to extend class `ApplicationInitializer`,
 the application class will have annotation `ConfigurationScanner`, the `target` will be class 
 that contains configurations of instances. <br/>
 _Instance Configuration_ <br/>
 Contains instance configurations described by methods of instance of class. The class should be annotated with `Configuration`, this will indicate
 that class is for configure application instances. To declare configurations for instance add a method that will return expected instance, the 
 method should be annotated with `Instance`, the instance fields will be used to initiate correct instance. The fields represent:
 - `instance`: declare the class/type of instance;
 - `qualifier`: declare a qualifier of instance;
 - `sdkVersion`: represent the minimum of sdk version for that instance may be applied;
 - `scope`: declare the scope of instance, singleton or prototype. <br/>
 
_Inject instance_<br>
To configure class to be initialized, the field of class should be annotated with `InjectInstance`, if `qualifier` is empty will initiate filed
by it type (`Instance#instance`), otherwise will initiate field by name (`Instance#qualifier`), the object by instance may be configured just one 
(except case when it different sdk level has different instance), but by qualifier may be a lot of. <br/>
_To inject the instances to object, from application context get instance content holder, `Context#getApplication#injectInstances` as input parameter
is object that should be completed with instances._ <br/><br/><br/>

**_Good Luck )))_**