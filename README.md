CS351-WaterProject
==================

I (Max) sent out an email and posted on WebCT, but I'm posting here in case you don't check those. I'm trying to set up a time for us to meet up on Monday so that we can talk about and work on the project a bit. I'm free anytime, so just email me some times that you are free and I'll try to get something set up.




ATTENTION: If you are getting "java.lang.OutOfMemoryError: Java heap space" as an error when you try to run the program:

In Run->Run Configuration find the Name of the class you have been running, select it, click the Arguments tab then add:

-Xms512M -Xmx1524M

to the VM Arguments section

-Xms512M is a minimum heap size of 512 Megabytes

-Xmx1524M is a maximum heap size of 1524 Megabytes (1.5 Gigabytes)

If that still doesn't work, you can try amping up the maximum heap size.
Make sure you include units, though! If you don't put M, it will assume you are referring to bytes.




Please add your name and current roles of the project.

Nathan (nacosta): Listings of offers and a bit of server.
-Skype: logic0verflow

Max (mottese): Water flow

Robert(NinerzRuleAll): Plants, TimeKeeper, ElevationData, Climate

Donald (swartzd3): Changing existing project to MVC design.

Vivek (Keviv93): 3D Graphics. Water visualization, sliders, and speed

Michael(masplund): server and network
